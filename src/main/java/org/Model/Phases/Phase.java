package org.Model.Phases;
import org.Controller.*;
import org.Exceptions.InvalidCommand;
import org.Exceptions.InvalidState;
import org.Exceptions.MapInvalidException;
import org.Model.GameState;
import org.Model.Player;
import org.Utils.Command;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Handles the state of the Game, using various phases.
 * This class represents the parent class of all the phases present in the game.
 * This class model follows STATE PATTERN for the game.
 */
public abstract class Phase {
    /**
     * Stores the current game state object
     */
    protected GameState d_currentGameState;

    /**
     * Stores the current Game Engine  object
     */
    protected GameEngine d_gameEngine;

    /**
     * Stores a scanner object to read input from user, can be closed upon exit of the game
     */
    protected Scanner d_scanner = new Scanner(System.in);

    /**
     * Parameterized constructor
     * @param l_gameEngine : Game engine object used for changing phases
     * @param l_currentGameState : Game state object used for changing the game state
     */
    public Phase(GameEngine l_gameEngine, GameState l_currentGameState) {
        this.d_gameEngine = l_gameEngine;
        this.d_currentGameState = l_currentGameState;
    }

    /**
     * Getter for current game state
     * @return GameState
     */
    public GameState getCurrentGameState() {
        return this.d_currentGameState;
    }

    /**
     * Setter for current gamestate object
     * @param l_currentGameState : the gamestate to be set for this game
     */
    public void setGameState(GameState l_currentGameState) {
        this.d_currentGameState = l_currentGameState;
    }

    /**
     * Used for performing the edit continent operation for the game , only applicable for START EXECUTION PHASE
     * @param l_cmd : Command provided by user
     * @param p_player : null for START EXECUTION PHASE
     * @throws MapInvalidException : If there is some issues with the map
     * @throws InvalidCommand : If there is some issues with the command
     */
    protected abstract void performEditContinent(Command l_cmd, Player p_player) throws MapInvalidException, InvalidCommand;

    /**
     * Used for performing the edit country operation for the game , only applicable for START EXECUTION PHASE
     * @param l_cmd : Command provides by user
     * @param p_player : null for START EXECUTION PHASE
     * @throws MapInvalidException : if there is some issues with the map
     * @throws InvalidCommand : If there is some issues with the command
     */
    protected abstract void performEditCountry(Command l_cmd, Player p_player) throws MapInvalidException, InvalidCommand;

    /**
     * Used for performing the edit neighbor operation for the game , only applicable for START EXECUTION PHASE
     * @param l_cmd : Command provides by user
     * @param p_player : null for START EXECUTION PHASE
     * @throws MapInvalidException : if there is some issues with the map
     * @throws InvalidCommand : If there is some issues with the command
     */
    protected abstract void performEditNeighbour(Command l_cmd, Player p_player) throws MapInvalidException, InvalidCommand;

    /**
     * Used for performing the load map operation for the game , only applicable for START EXECUTION PHASE
     * @param l_cmd : Command provides by user
     * @param p_player : null for START EXECUTION PHASE
     * @throws MapInvalidException : if there is some issues with the map
     * @throws InvalidCommand : If there is some issues with the command
     * @throws FileNotFoundException : If there is no file found for the map
     */
    protected abstract void performLoadMap(Command l_cmd, Player p_player) throws InvalidCommand, FileNotFoundException, MapInvalidException;

    /**
     * Used for performing the showmap operation for the game
     * @param p_player : null for START EXECUTION PHASE , player can be provided in ISSUE_ORDER  and ORDER_EXECUTION phase
     * @throws InvalidCommand : If there is some issues with the command
     */
    protected abstract void performShowMap(Player p_player) throws InvalidCommand;

    /**
     * Used for performing the validate operation for the game , only applicable for START EXECUTION PHASE
     * @param p_player : null for START EXECUTION PHASE
     * @throws MapInvalidException : if there is some issues with the map
     * @throws InvalidState : If there is some issues with the command
     */
    protected abstract void performValidateMap(Player p_player) throws MapInvalidException, InvalidState;

    /**
     * Used for performing the savemap operation for the game , only applicable for START EXECUTION PHASE
     * @param l_cmd : Command provides by user
     * @param p_player : null for START EXECUTION PHASE
     * @throws MapInvalidException : if there is some issues with the map
     * @throws InvalidCommand  If there is some issues with the command
     * @throws IOException If there is some issues with the file
     */
    protected abstract void performSaveMap(Command l_cmd, Player p_player) throws IOException, MapInvalidException, InvalidCommand;

    /**
     * Used for performing the edit map operation for the game , only applicable for START EXECUTION PHASE
     * @param l_cmd : Command provides by user
     * @param p_player : null for START EXECUTION PHASE
     * @throws MapInvalidException : if there is some issues with the map
     * @throws InvalidCommand : If there is some issues with the command
     */
    protected abstract void performEditMap(Command l_cmd, Player p_player) throws IOException, MapInvalidException, InvalidCommand;

    /**
     * Used for performing the edit country operation for the game , only applicable for START EXECUTION PHASE
     * @param p_player : null for START EXECUTION PHASE
     * @throws MapInvalidException : if there is some issues with the map
     * @throws InvalidCommand : If there is some issues with the command
     */
    protected abstract void performAssignCountries(Player p_player) throws InvalidCommand, MapInvalidException, InvalidState;

    /**
     * Used for performing the create player for the game , only applicable for START EXECUTION PHASE
     * @param p_player : null for START EXECUTION PHASE
     * @throws InvalidCommand : If there is some issues with the command
     */
    protected abstract void performCreatePlayers(Command l_cmd, Player p_player) throws InvalidCommand;

    /**
     * Used as the start point of the game for each phase
     * Basically takes input from the uses and decides what has to be done
     * @throws InvalidState : If user provided invalid input
     */
    public abstract void initPhase() throws InvalidState;

    /**
     * Used for performing the deploy operation for the game , only applicable for ISSUE_ORDER_PHASE
     * @param l_cmd : Command provides by user
     * @param p_player : null for START EXECUTION PHASE
     * @throws InvalidCommand : If there is some issues with the command
     */
    public abstract void performDeployArmies(Command l_cmd, Player p_player) throws InvalidCommand, InvalidState;

    /**
     * Used for performing the advance operation for the game , only applicable for ISSUE_ORDER_PHASE
     * @param l_cmd : Command provides by user
     * @param p_player : null for START EXECUTION PHASE
     * @throws InvalidCommand : If there is some issues with the command
     */
    public abstract void performAdvanceArmies(Command l_cmd, Player p_player) throws InvalidCommand, InvalidState;

    /**
     * Used for performing the card handles ( bomb , blockade , negotiate, airlift ) for the game , only applicable for ISSUE_ORDER_PHASE
     * @param l_cmd : Command provides by user
     * @param p_player : null for START EXECUTION PHASE
     * @throws InvalidCommand : If there is some issues with the command
     */
    public abstract void performCardHandle(Command l_cmd, Player p_player) throws InvalidCommand, InvalidState;

    /**
     * Contains the start up message of each phase
    */
    public abstract void printStartingOptions();

    /**
     * Main command handler of the Game delegates the command to specific function on basic of command provide
     * @param p_command : Command object provided to the game
     * @param p_player : Player
     */
    protected void commandHandler(String p_command, Player p_player) {
        try {
            Command l_cmd = new Command(p_command);
            String l_mainOperation = l_cmd.getMainOperation();
            switch (l_mainOperation) {
                case "editcontinent": {
                    performEditContinent(l_cmd,p_player);
                    break;
                }
                case "editcountry": {
                    performEditCountry(l_cmd,p_player);
                    break;
                }
                case "editneighbor": {
                    performEditNeighbour(l_cmd,p_player);
                    break;
                }
                case "loadmap": {
                    performLoadMap(l_cmd,p_player);
                    break;
                }
                case "showmap": {
                    performShowMap(p_player);
                    break;
                }
                case "validatemap": {
                    performValidateMap(p_player);
                    break;
                }
                case "savemap":{
                    performSaveMap(l_cmd,p_player);
                    break;
                }
                case "editmap":{
                    performEditMap(l_cmd,p_player);
                    break;
                }
                case "gameplayer":{
                    performCreatePlayers(l_cmd,p_player);
                    break;
                }
                case "assigncountries":{
                    performAssignCountries(p_player);
                    break;
                }
                case "deploy": {
                    performDeployArmies(l_cmd,p_player);
                    break;
                }
                case "advance": {
                    performAdvanceArmies(l_cmd,p_player);
                    break;
                }
                case "airlift":
                case "blockade":
                case "negotiate":
                case "bomb": {
                    performCardHandle(l_cmd, p_player);
                    break;
                }
                case "exit": {
                    d_scanner.close();
                    System.exit(0);
                    break;
                }
            }
        } catch (MapInvalidException | IOException | InvalidCommand | InvalidState e) {
            System.out.println(e.getMessage() + System.lineSeparator());
        }


    }
}
