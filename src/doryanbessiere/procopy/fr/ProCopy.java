package doryanbessiere.procopy.fr;

import doryanbessiere.procopy.fr.commons.Commons;
import doryanbessiere.procopy.fr.commons.logger.Logger;
import doryanbessiere.procopy.fr.commons.logger.LoggerFile;
import doryanbessiere.procopy.fr.configuration.ProCopyConfiguration;
import doryanbessiere.procopy.fr.configuration.ProCopyMode;
import doryanbessiere.procopy.fr.configuration.ProCopyType;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProCopy extends Thread {

    public static Logger LOGGER = new Logger("ProCopy", new LoggerFile(new File(Commons.localDirectory(), "logs")));

    private File source;
    private File destination;

    private ArrayList<ProCopyListener> listeners = new ArrayList<>();

    private ProCopyConfiguration configuration = new ProCopyConfiguration();

    public ProCopy(File source, File destination) {
        this.source = source;
        this.destination = destination;
    }

    private List<ProCopyThread> threads = Collections.synchronizedList(new ArrayList<>());
    private AtomicBoolean finish = new AtomicBoolean(true);

    private void error(Exception exception){
        if(!configuration.mustIgnoreError())
        {
            threads.forEach(thread -> thread.stop());
        }
        exception.printStackTrace();
    }

    public boolean result(){
        return LOGGER.getLogs(Logger.Type.ERROR).size() == 0;
    }

    private long start;
    private long elapsed;

    @Override
    public void run(){
        start = System.currentTimeMillis();
        listeners.forEach(listener -> listener.start());
        finish.set(false);
        LOGGER.logInfo("Démarrage du processus de copie...");
        if(!destination.isDirectory() && destination.exists()){
            error(new Exception(destination+", destination is not a directory !"));
            LOGGER.logError("Le fichier de destination n'est pas un répertoire!");
            return;
        }

        destination.mkdirs();

        if(configuration.mustClearDestinationDirectory()){
            LOGGER.logInfo("Nettoyage du dossier destination, celui-ci vas se retrouver vide.");
            try {
                for(File file : destination.listFiles()){
                    if(file.isFile()){
                        FileUtils.forceDelete(file);
                    } else if (file.isDirectory()){
                        FileUtils.deleteDirectory(file);
                    }
                }
            } catch (IOException e) {
                error(e);
                LOGGER.logError("Impossible de supprimer tout le contenu du dossier destination.");
            }
        }
        calculatesLength();
        if(source.isFile()){
            copyFile(source, destination);
        } else if (source.isDirectory()){
            copyDirectory(source, destination, true);
        }

        if(configuration.getMode() == ProCopyMode.CUT){
            try {
                FileUtils.deleteDirectory(source);
            } catch (IOException e) {
                e.printStackTrace();
                error(e);
                LOGGER.logError("Impossible de supprimer tout le contenu du dossier source.");
            }
            source.mkdirs();
        }

        while(threads.size() != 0){}

        finish();
    }

    public void finish(){
        elapsed = System.currentTimeMillis() - start;
        LOGGER.logInfo("La copie de tout les fichiers est terminée. (temps="+elapsed+"'ms)");
        if(!result()){
            LOGGER.logError(LOGGER.getLogs(Logger.Type.ERROR).size() + " erreur(s) ont été détecté.");
        } else {
            LOGGER.logInfo("Aucune erreur détecté lors du déroulage du processus.");
        }
        listeners.forEach(listener -> listener.finish());
    }

    public void calculatesLength(){
        calculatesLengthByDirectory(source);
    }

    private void calculatesLengthByDirectory(File directory){
        if(!configuration.isSelectAll() && !configuration.getSelects().contains(directory))return;
        Arrays.stream(directory.listFiles()).forEach(file -> {
            if(!configuration.isSelectAll() && !configuration.getSelects().contains(directory))return;
            if(file.isDirectory() && file.listFiles() != null && file.listFiles().length > 0){
                calculatesLengthByDirectory(file);
            } else {
                total += file.length();
            }
        });
    }

    long total;
    long current = 0L;

    public void copyFile(File source, File destination){
        LOGGER.logInfo("Copie du fichier : "+source.getPath());
        if(!configuration.isSelectAll() && !configuration.getSelects().contains(source)){
            LOGGER.logWarning("Le fichier ("+source.getPath()+") n'a pas été copié, ce fichier n'est pas dans la sélection.");
            return;
        }

        current += source.length();
        listeners.forEach(listener -> listener.progress(current, total));
        try {
            if(source.length() == 0 && configuration.isBlacklisted(ProCopyType.EMPTY_FILE)){
                LOGGER.logWarning("Le fichier ("+source.getPath()+") n'a pas à être copié, ce fichier est vide.");
                return;
            }
            if(source.isHidden() && configuration.isBlacklisted(ProCopyType.HIDDEN_FILE)){
                LOGGER.logWarning("Le fichier ("+source.getPath()+") n'a pas à être copié, ce fichier est caché.");
                return;
            }
            File target = new File(destination, source.getName());
            if(target.exists() && !configuration.mustOverwrite()){
                LOGGER.logWarning("Le fichier ("+source.getPath()+") n'a pas à être copié, ce fichier existe déjà.");
                return;
            }
            if(!configuration.isExtensionIgnored(source)){
                if(target.exists()){
                    LOGGER.logInfo("  -> Suppression du fichier existant : "+target.getPath());
                    FileUtils.forceDelete(target);
                }
                FileUtils.copyFileToDirectory(source, destination);
                LOGGER.logInfo("Le fichier ("+source.getPath()+") a été copié avec succès.");
            } else {
                LOGGER.logWarning("Le fichier ("+source.getPath()+") a une extension qui ne lui permet pas d'être copiée.");
            }
        } catch (IOException e) {
            error(e);
            LOGGER.logError("Impossible de copier le fichier : "+source.getPath());
        }
    }

    private void copyDirectory(File source, File destination){
        copyDirectory(source, destination, false);
    }

    private void copyDirectory(File source, File destination, boolean ignoreSubFolder){
        LOGGER.logInfo("Copie du contenu du dossier : "+source);
        if(!configuration.isSelectAll() && !configuration.getSelects().contains(source)){
            LOGGER.logWarning("Le dossier ("+source.getPath()+") n'a pas été copié, ce dossier n'est pas dans la sélection.");
            return;
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(source.listFiles() != null && source.listFiles().length == 0 && configuration.isBlacklisted(ProCopyType.EMPTY_DIRECTORY)){
                    LOGGER.logWarning("Le dossier ("+source.getPath()+") n'a pas à être copié, ce dossier est vide.");
                    return;
                }
                if(source.isHidden() &&  configuration.isBlacklisted(ProCopyType.HIDDEN_DIRECTORY)){
                    LOGGER.logWarning("Le dossier ("+source.getPath()+") n'a pas à être copié, ce dossier est caché.");
                    return;
                }
                if(!ignoreSubFolder && configuration.isBlacklisted(ProCopyType.SUB_DIRECTORIES)){
                    LOGGER.logWarning("Le dossier ("+source.getPath()+") n'a pas à être copié, c'est un sous-dossier.");
                    return;
                }
                if(!destination.exists()){
                    destination.mkdirs();
                }

                if(source.listFiles() == null)return;
                for(File file : source.listFiles()){
                    if(file.isDirectory()){
                        File destination_ = new File(destination.getPath() + "/" + file.getPath().replace(source.getPath(),""));
                        copyDirectory(file, destination_);
                    } else if (file.isFile()){
                        copyFile(file, destination);
                    }
                }
            }
        };
        boolean needWait = threads.size() >= configuration.getMaxThread();
        if(configuration.mustUseMultiThreading() && !needWait){
            LOGGER.logInfo("Nouveau thread créer : "+(threads.size() + 1)+" threads");
            ProCopyThread thread = new ProCopyThread(runnable) {
                @Override
                public void finish() {
                    ProCopy.this.threads.remove(this);
                }
            };
            this.threads.add(thread);
            thread.start();
        } else {
            runnable.run();
        }
    }

    public void setConfiguration(ProCopyConfiguration configuration) {
        this.configuration = configuration;
    }

    public File getSource() {
        return source;
    }

    public File getDestination() {
        return destination;
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public ArrayList<ProCopyListener> getListeners() {
        return listeners;
    }

    public long getElapsed() {
        return this.elapsed;
    }
}
