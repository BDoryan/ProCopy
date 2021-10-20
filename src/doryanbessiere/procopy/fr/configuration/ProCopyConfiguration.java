package doryanbessiere.procopy.fr.configuration;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ProCopyConfiguration {

    private ProCopyMode mode = ProCopyMode.COPY;
    private boolean ignoreError = false;
    private boolean overwrite = true;
    private boolean useMultiThreading = false;
    private boolean clearDestinationDirectory = true;

    private boolean selectAll = true;

    private ArrayList<String> selects = new ArrayList<>();

    private HashMap<ProCopyType, Boolean> blacklists = new HashMap<>();

    private int maxThread = 20;

    private String[] ignoreExtensions = null;

    public ProCopyConfiguration() {
        this.blacklists.put(ProCopyType.EMPTY_FILE, false);
        this.blacklists.put(ProCopyType.EMPTY_DIRECTORY, false);
        this.blacklists.put(ProCopyType.HIDDEN_DIRECTORY, false);
        this.blacklists.put(ProCopyType.HIDDEN_FILE, false);
        this.blacklists.put(ProCopyType.SUB_DIRECTORIES, false);
    }

    public ProCopyConfiguration(ProCopyMode mode, boolean ignoreError, boolean overwrite, boolean copyEmptyDirectory, boolean copyEmptyFile, boolean copySubFolders, boolean copyHiddenFile, boolean copyHiddenDirectory, boolean useMultiThreading, boolean clearDestinationDirectory, int maxThread) {
        this.mode =mode;
        this.ignoreError = ignoreError;
        this.overwrite = overwrite;
        this.blacklists.put(ProCopyType.EMPTY_FILE, !copyEmptyFile);
        this.blacklists.put(ProCopyType.EMPTY_DIRECTORY, !copyEmptyDirectory);
        this.blacklists.put(ProCopyType.HIDDEN_DIRECTORY, !copyEmptyDirectory);
        this.blacklists.put(ProCopyType.HIDDEN_FILE, !copyEmptyFile);
        this.blacklists.put(ProCopyType.SUB_DIRECTORIES, !copySubFolders);
        this.useMultiThreading = useMultiThreading;
        this.clearDestinationDirectory = clearDestinationDirectory;
        this.maxThread = maxThread;
    }

    public ArrayList<String> getSelects() {
        return selects;
    }

    public ArrayList<File> getFileSelects() {
        ArrayList<File> files = new ArrayList<>();
        selects.forEach(file -> {
            files.add(new File(file));
        });
        return files;
    }

    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }

    public boolean isSelectAll() {
        return selectAll;
    }

    public ProCopyMode getMode() {
        return mode;
    }

    public void setIgnoreExtensions(String[] ignoreExtensions) {
        this.ignoreExtensions = ignoreExtensions;
    }

    public void setClearDestinationDirectory(boolean clearDestinationDirectory) {
        this.clearDestinationDirectory = clearDestinationDirectory;
    }

    public boolean isExtensionIgnored(File file){
        return isExtensionIgnored(FilenameUtils.getExtension(file.getName()));
    }

    public boolean isExtensionIgnored(String extension){
        if(ignoreExtensions == null)return false;
        for(String extension_ : ignoreExtensions){
            System.out.println(extension_ +" / "+extension);
            if(extension.replace(".", "").equalsIgnoreCase(extension_.replace(".", "")))
                return true;
        }
        return false;
    }

    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }

    public String[] getIgnoreExtensions() {
        return ignoreExtensions;
    }

    public int getMaxThread() {
        return maxThread;
    }

    public boolean mustClearDestinationDirectory() {
        return clearDestinationDirectory;
    }

    public void setMaxThread(int maxThread) {
        this.maxThread = maxThread;
    }

    public void setIgnoreError(boolean ignoreError) {
        this.ignoreError = ignoreError;
    }

    public void setUseMultiThreading(boolean useMultiThreading) {
        this.useMultiThreading = useMultiThreading;
    }

    public boolean mustUseMultiThreading() {
        return useMultiThreading;
    }

    public boolean mustIgnoreError() {
        return ignoreError;
    }

    public boolean mustOverwrite() {
        return overwrite;
    }

    public void setBlacklists(ProCopyType type, boolean value) {
        this.blacklists.put(type, value);
    }

    public boolean isBlacklisted(ProCopyType type){
        return this.blacklists.get(type);
    }

    public void addSelect(File base) {
        System.out.println("AddDirectory : "+base.getPath());
        if(base.isDirectory() && base.listFiles() != null && base.listFiles().length > 0){
            for(File file : base.listFiles()){
                if(file.isDirectory()){
                    addSelect(file);
                } else {
                    selects.add(file.getPath());
                    System.out.println("AddFile : "+file.getPath());
                }
            }
        }
        selects.add(base.getPath());
    }

    public void removeSelect(File base){
        System.out.println("RemoveDirectory : "+base.getPath());
        if(base.isDirectory()){
            for(File file : base.listFiles()){
                if(file.isDirectory()){
                    removeSelect(file);
                } else {
                    if(!selects.contains(file.getPath())){
                        System.err.println("il n'existe pas");
                    } else {
                        selects.remove(file.getPath());
                        System.out.println("RemoveFile : "+file.getPath());
                    }
                }
            }
        }
        selects.remove(base.getPath());
        System.out.println("---");
        selects.forEach(path -> {
            System.out.println(" - "+path);
        });
    }

    public void setSelects(ArrayList<String> selects) {
        this.selects.clear();
        this.selects.addAll(selects);
    }
}
