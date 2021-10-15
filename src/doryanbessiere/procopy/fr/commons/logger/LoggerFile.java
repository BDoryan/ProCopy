package doryanbessiere.procopy.fr.commons.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class LoggerFile extends LoggerListener {

    private File directory;
    private File file;
    private FileWriter writer;

    public LoggerFile(File directory) {
        this.directory = directory;

        System.out.println(directory.getPath());
        if(!directory.exists()){
            directory.mkdirs();
        }

        String name = new SimpleDateFormat("DD-MM-yyyy").format(System.currentTimeMillis());
        try {
            int i = 0;
            while(true){
                this.file = new File(directory, name + (i > 0 ? "-"+i : "")+".log");
                if(!this.file.exists())
                    break;

                i++;
            }
            this.file.createNewFile();
            this.writer = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            this.writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void log(LoggerMessage log) {
        try {
            writer.write(log.toMessage());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
