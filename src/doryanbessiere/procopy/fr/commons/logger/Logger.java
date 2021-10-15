package doryanbessiere.procopy.fr.commons.logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Logger {

    private String name;
    private ArrayList<LoggerListener> listeners = new ArrayList<>();
    private List<LoggerMessage> logs = new ArrayList<>();

    public Logger(String name) {
        this.name = name;
    }

    public Logger(String name, LoggerFile loggerFile) {
        this.name = name;
        listeners.add(loggerFile);
    }

    public void clearLogs(){
        logs.clear();
    }

    public void logInfo(String message){
        log(Type.INFO, message);
    }

    public void logError(String message){
        log(Type.ERROR, message);
    }

    public void logDebug(String message){
        log(Type.DEBUG, message);
    }

    public void logWarning(String message){
        log(Type.WARNING, message);
    }

    public void log(Type type, String message){
        LoggerMessage loggerMessage = new LoggerMessage(this, type, System.currentTimeMillis(), message);
        loggerMessage.print();
        logs.add(loggerMessage);
        listeners.forEach(listener -> listener.log(loggerMessage));
    }

    public String getName() {
        return name;
    }

    public ArrayList<LoggerListener> getListeners() {
        return listeners;
    }

    public List<LoggerMessage> getLogs(){
        return logs;
    }

    public List<LoggerMessage> getLogs(Type type){
        return this.logs.stream().filter(loggerMessage -> (type == null || loggerMessage.getType() == type)).collect(Collectors.toList());
    }

    public static enum Type {
        ERROR("[ERREUR]"),
        INFO("[INFO]"),
        DEBUG("[DEBUG]"),
        WARNING("[ATTENTION]");

        private String prefix;

        Type(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }
    }
}
