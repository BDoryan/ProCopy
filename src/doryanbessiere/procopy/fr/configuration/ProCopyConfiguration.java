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

    private ArrayList<File> blacklistFiles = new ArrayList<>();
    private ArrayList<File> othersFiles = new ArrayList<>();

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
}
