package net.kio.its.logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private final ILogger iLogger;

    public Logger(ILogger iLogger) {
        this.iLogger = iLogger;
    }

    public void log(String message) {log(LogType.INFO, message);}
    public void log(LogType logType, String message){
        if(logType == LogType.DEBUG && !iLogger.isDebug()) return;
        System.out.println(logType.getPrefix() + getCurrentStringTime() + "  |  " + ConsoleColors.YELLOW_BOLD + iLogger.getName() + ConsoleColors.RESET + logType.getColor() + "  |\t" + message + ConsoleColors.RESET);
    }

    private String getCurrentStringTime(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
    }

}
