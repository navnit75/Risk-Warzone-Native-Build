package org.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Observable;
import java.util.Observer;

/**
 * Log writer class basically defines an observer which is subscribed to an Observable
 * Follows the OBSERVER PATTERN design
 */
public class LogWriter implements Observer {
    /**
     * Keeps the log file
     */
    private String d_logFile;

    /**
     * Setter for the log file
     * @param p_fileName
     */
    public LogWriter(String p_fileName){
        d_logFile = p_fileName;
    }

    /**
     * Flag use for clearing the file, before adding the log at the start of the game.
     */
    private boolean clearLogFileFlag = false;

    /**
     * Log file may contain the log from previous run
     * This function clears the file before adding the current run logs
     */
    public void clearLogFileFromLastRun(){
        if(clearLogFileFlag == false) {
            try {
                File l_file = new File(d_logFile);
                if (l_file.exists()) {
                    FileWriter l_fileWriter = new FileWriter(l_file, false);
                } else {
                    l_file.createNewFile();
                }
                clearLogFileFlag = true;
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * This function is called anytime observable state changes , according to the OBSERVER PATTERN
     */
    public void update(Observable o, Object arg) {
        clearLogFileFromLastRun();
        LogEntryBuffer l_logMessage = (LogEntryBuffer)o;
        String l_logMessageString = l_logMessage.getLogMsg();
        try{
            Files.write(Paths.get(d_logFile), l_logMessageString.getBytes(StandardCharsets.UTF_8),
                        StandardOpenOption.APPEND);
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }
}
