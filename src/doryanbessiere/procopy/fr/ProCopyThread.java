package doryanbessiere.procopy.fr;

public abstract class ProCopyThread extends Thread{

    public ProCopyThread(Runnable runnable) {
        super(runnable);
    }

    public abstract void finish();

    @Override
    public void run() {
        super.run();
        finish();
        stop();
    }
}
