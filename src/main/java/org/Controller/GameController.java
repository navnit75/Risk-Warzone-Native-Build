package org.Controller;
import org.Constants.AllTheConstants;
import org.Exceptions.InvalidState;
import org.Model.*;
import org.Model.Phases.IssueOrderPhase;
import org.Model.Phases.OrderExecutionPhase;
import org.Model.Phases.Phase;
import org.Model.Phases.StartUpPhase;
import org.Utils.LogEntryBuffer;
import org.Utils.LogLevel;
import org.Utils.LogWriter;

/**
 * Entry point of the game.
 * Keep track of the state game is in.
 * Allows for defining Logging Context, as the file where the log of the game should be saved.
 */
public class GameController {
    /**
     * Data structure to keep the current gameplay details.
     */
    private GameState d_gameState = new GameState();

    /**
     * Stores the details of the current phase game is in
     */
    private Phase d_currentPhase = new StartUpPhase(this,d_gameState);

    /**
     * Logger object which is used to update logs based on Observer pattern
     */
    private static LogEntryBuffer d_logger;

    /**
     * Gets the current phase of the game , as per state pattern
     * @return d_currentPhase object , which stores the current phase of the game
     */
    public Phase getCurrentPhase() { return this.d_currentPhase; }

    /**
     * Sets the current phase for the game to run, as per the State Pattern
     * @param l_phase , object to store the Phase object, to which game should change to
     */
    public void setCurrentPhase(Phase l_phase){ this.d_currentPhase = l_phase; }

    /**
     * Changes the Execution of the game to IssueOrderPhase
     * @throws InvalidState in case if there is abrupt command provided by user
     */
    public void setIssueOrderPhase() throws InvalidState{
        setCurrentPhase(new IssueOrderPhase(this,d_gameState));
        try {
            getCurrentPhase().initPhase();
        } catch (org.Exceptions.InvalidState invalidState) {
            throw new InvalidState("Invalid Command Provided.");
        }
    }

    /**
     * Sets the game to Order Execution Phase according to the State Pattern
     * @throws InvalidState in case if an abrupt command is provided by user.
     */
    public void setOrderExecutionPhase() throws InvalidState{
        setCurrentPhase(new OrderExecutionPhase(this,d_gameState));
        try {
            getCurrentPhase().initPhase();
        } catch (org.Exceptions.InvalidState invalidState) {
            throw new InvalidState("Invalid Command Provided.");
        }
    }

    /**
     * Sets the context for the logger, by defining the file the Logs should be written to
     * @param p_fileName : The file name of the file
     */
     public static void setLoggerContext(String p_fileName){
        if(d_logger == null)
            d_logger = new LogEntryBuffer();

        LogWriter logFile = new LogWriter(AllTheConstants.defaultLogLocationAppendString + p_fileName);
        d_logger.addObserver(logFile);
    }

    /**
     * Static function to make the logging available for the Classes of the game
     * @param p_fileName : Basically denotes the file and function from which the log is coming
     * @param p_level : Basically denotes the level of the Log, could be HEADING, SUBHEADING and BASICLOG
     * @param p_logging : Log message to be written in the file.
     */
    public static void log(String p_fileName, LogLevel p_level, String p_logging){
        d_logger.writeToBuffer(p_level, "[" + p_fileName + "] :" + p_logging);
    }

}
