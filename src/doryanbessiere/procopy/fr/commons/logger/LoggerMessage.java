package doryanbessiere.procopy.fr.commons.logger;

import java.text.SimpleDateFormat;

public class LoggerMessage {

    private Logger logger;

    private Logger.Type type;
    private long datetime;
    private String message;

    public LoggerMessage(Logger logger, Logger.Type type, long datetime, String message) {
        this.logger = logger;

        this.type = type;
        this.datetime = datetime;
        this.message = message;
    }

    public String toMessage(){
        String name = logger.getName();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        return "["+(simpleDateFormat.format(System.currentTimeMillis()))+"] ["+name+"] " +type.getPrefix()+ " " +message;
    }

    public void print(){
        String message = toMessage();
        if(type == Logger.Type.ERROR){
            System.err.println(message);
        } else {
            System.out.println(message);
        }
    }

    public Logger getLogger() {
        return logger;
    }

    public Logger.Type getType() {
        return type;
    }

    public long getDatetime() {
        return datetime;
    }

    public String getMessage() {
        return message;
    }
}
