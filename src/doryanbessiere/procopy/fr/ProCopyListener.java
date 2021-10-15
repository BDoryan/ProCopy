package doryanbessiere.procopy.fr;

import java.io.File;

public abstract class ProCopyListener {

    public abstract void start();
    public abstract void finish();
    public abstract void progress(long current, long total);

}
