package org.Controller;
import org.Constants.AllTheConstants;
import org.Model.*;
import org.Model.Phases.IssueOrderPhase;
import org.Model.Phases.OrderExecutionPhase;
import org.Model.Phases.Phase;
import org.Model.Phases.StartUpPhase;
import org.Utils.LogEntryBuffer;
import org.Utils.LogLevel;
import org.Utils.LogWriter;

public class GameController {
    private GameState d_gameState = new GameState();
    private Phase d_currentPhase = new StartUpPhase(this,d_gameState);
    private static LogEntryBuffer d_logger;
    public Phase getCurrentPhase() { return this.d_currentPhase; }
    public void setCurrentPhase(Phase l_phase){ this.d_currentPhase = l_phase; }
    public void setIssueOrderPhase(){
        setCurrentPhase(new IssueOrderPhase(this,d_gameState));
        try {
            getCurrentPhase().initPhase();
        } catch (org.Exceptions.InvalidState invalidState) {
            throw new RuntimeException(invalidState);
        }
    }
    public void setOrderExecutionPhase(){
        setCurrentPhase(new OrderExecutionPhase(this,d_gameState));
        try {
            getCurrentPhase().initPhase();
        } catch (org.Exceptions.InvalidState invalidState) {
            throw new RuntimeException(invalidState);
        }
    }
     public static void setLoggerContext(String p_fileName){
        if(d_logger == null)
            d_logger = new LogEntryBuffer();

        LogWriter logFile = new LogWriter(AllTheConstants.defaultLogLocationAppendString + p_fileName);
        d_logger.addObserver(logFile);
    }
    public static void log(String p_fileName, LogLevel p_level, String p_logging){
        d_logger.writeToBuffer(p_level, "[" + p_fileName + "] :" + p_logging);
    }

}
