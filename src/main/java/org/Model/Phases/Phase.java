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

public abstract class Phase {
    protected GameState d_currentGameState;
    protected GameController d_gameController;
    protected Scanner d_scanner = new Scanner(System.in);

    public Phase(GameController l_gameController, GameState l_currentGameState) {
        this.d_gameController = l_gameController;
        this.d_currentGameState = l_currentGameState;
    }

    public GameState getCurrentGameState() {
        return this.d_currentGameState;
    }

    public void setGameState(GameState l_currentGameState) {
        this.d_currentGameState = l_currentGameState;
    }

    protected abstract void performEditContinent(Command l_cmd, Player p_player) throws MapInvalidException, InvalidCommand;

    protected abstract void performEditCountry(Command l_cmd, Player p_player) throws MapInvalidException, InvalidCommand;

    protected abstract void performEditNeighbour(Command l_cmd, Player p_player) throws MapInvalidException, InvalidCommand;

    protected abstract void performLoadMap(Command l_cmd, Player p_player) throws InvalidCommand, FileNotFoundException, MapInvalidException;

    protected abstract void performShowMap(Player p_player) throws InvalidCommand;

    protected abstract void performValidateMap(Player p_player) throws MapInvalidException, InvalidState;

    protected abstract void performSaveMap(Command l_cmd, Player p_player) throws IOException, MapInvalidException, InvalidCommand;

    protected abstract void performEditMap(Command l_cmd, Player p_player) throws IOException, MapInvalidException, InvalidCommand;

    protected abstract void performAssignCountries(Player p_player) throws InvalidCommand, MapInvalidException, InvalidState;

    protected abstract void performCreatePlayers(Command l_cmd, Player p_player) throws InvalidCommand;

    public abstract void initPhase() throws InvalidState;

    public abstract void performDeployArmies(Command l_cmd, Player p_player) throws InvalidCommand, InvalidState;

    public abstract void performAdvanceArmies(Command l_cmd, Player p_player) throws InvalidCommand, InvalidState;

    public abstract void performCardHandle(Command l_cmd, Player p_player) throws InvalidCommand, InvalidState;

    public abstract void printStartingOptions();

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
