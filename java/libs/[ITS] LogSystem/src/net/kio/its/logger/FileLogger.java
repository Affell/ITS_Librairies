package net.kio.its.logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.zip.GZIPOutputStream;

public class FileLogger {

    private final Logger logger;

    private File logFile;

    public FileLogger(Logger logger) {
        this.logger = logger;
        Runtime.getRuntime().addShutdownHook(new Thread(this::closeLogFile));
    }

    public void initFileLogger() {
        File dir = new File("./logs/");
        if (!dir.exists()) dir.mkdir();
        logFile = new File("./logs/latest.log");
        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            new FileWriter(logFile).close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeLog(String line){
        if(logFile != null && logFile.exists()){
            try {
                FileWriter fileWriter = new FileWriter(logFile, true);
                fileWriter.write(line + "\n");
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeLogFile(){
        if(logFile != null && logFile.exists()){
            try (
                FileOutputStream zipFile = new FileOutputStream("./logs/" + logger.getCurrentStringTime(true));
                GZIPOutputStream gzipOutputStream = new GZIPOutputStream(zipFile);
            )
            {
                gzipOutputStream.write(Files.readAllBytes(logFile.toPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
