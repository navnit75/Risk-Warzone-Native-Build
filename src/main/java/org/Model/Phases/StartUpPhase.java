package org.Model.Phases;

import org.Controller.GameEngine;
import org.Model.GameState;
import org.Model.Map;
import org.Model.Player;
import org.Utils.Command;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.Exceptions.*;
import org.Utils.LogLevel;

/**
 * Class denotes the start up phase STATE of the game
 */
public class StartUpPhase extends Phase{

    public StartUpPhase(GameEngine l_gameEngine, GameState l_currentGameState) {
        super(l_gameEngine, l_currentGameState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performEditContinent(Command l_cmd, Player p_player) throws MapInvalidException, InvalidCommand {
        ArrayList<String> l_addContinents = l_cmd.handleEditContinent().get("add");
        ArrayList<String> l_removeContinents = l_cmd.handleEditContinent().get("remove");
        if(!l_addContinents.isEmpty()){
            for(int i = 0 ; i < l_addContinents.size(); i += 2){
                this.getCurrentGameState().getCurrentMap().addContinent(l_addContinents.get(i),
                        Integer.parseInt(l_addContinents.get(i+1)));
                }
        }
        if(!l_removeContinents.isEmpty()){
            for(String l_continent : l_removeContinents){
                this.getCurrentGameState().getCurrentMap().removeContinent(l_continent);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performEditCountry(Command l_cmd, Player p_player) throws MapInvalidException, InvalidCommand {
        ArrayList<String> l_addCountries = l_cmd.handleEditCountry().get("add");
        ArrayList<String> l_removeCountries = l_cmd.handleEditCountry().get("remove");
        if(!l_addCountries.isEmpty()){
            for(int i = 0 ; i < l_addCountries.size(); i += 2){
                this.getCurrentGameState().getCurrentMap().addCountry(l_addCountries.get(i),
                        l_addCountries.get(i+1));
            }
        }
        if(!l_removeCountries.isEmpty()){
            for(String l_country : l_removeCountries){
                this.getCurrentGameState().getCurrentMap().removeCountry(l_country);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performEditNeighbour(Command l_cmd, Player p_player) throws MapInvalidException, InvalidCommand {
        ArrayList<String> l_addNeighbours = l_cmd.handleEditNeighbour().get("add");
        ArrayList<String> l_removeNeighbours = l_cmd.handleEditNeighbour().get("remove");
        if(!l_addNeighbours.isEmpty()){
            for(int i = 0 ; i < l_addNeighbours.size(); i += 2){
                this.getCurrentGameState().getCurrentMap().addNeighbour(l_addNeighbours.get(i),
                        l_addNeighbours.get(i+1));
            }
        }
        if(!l_removeNeighbours.isEmpty()){
            for(int i = 0 ; i < l_removeNeighbours.size(); i += 2){
                this.getCurrentGameState().getCurrentMap().removeNeighbour(l_removeNeighbours.get(i),
                        l_removeNeighbours.get(i+1));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performLoadMap(Command l_cmd, Player p_player) throws InvalidCommand, FileNotFoundException, MapInvalidException {
        GameEngine.log("StartUpPhase::peformLoadMap",LogLevel.SUBHEADING,"MAP section starts");
        getCurrentGameState().getCurrentMap().loadMap(l_cmd.handleLoadMap());
        if(Map.d_isMapLoaded){
            System.out.println("Map loaded successfully!");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performShowMap(Player p_player) throws InvalidCommand {
        getCurrentGameState().getCurrentMap().showMap();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performValidateMap(Player p_player) throws MapInvalidException, InvalidState {
        if(!Map.d_isMapLoaded)
            throw new InvalidState("Please load the map first.");
        Boolean l_result = getCurrentGameState().getCurrentMap().validateMap();
        if(l_result){
            GameEngine.log("StartUpPhase::performValidateMap",LogLevel.BASICLOG,"Map Validation " +
                    "Successful.");
            System.out.println("Map Validation Successful!");
        } else{
            GameEngine.log("StartUpPhase::performValidateMap",LogLevel.BASICLOG,"Map Validation" +
                    " Unsuccessful.");
            System.out.println("Map is Invalid!");
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performSaveMap(Command l_cmd , Player p_player) throws IOException, MapInvalidException, InvalidCommand {
        getCurrentGameState().getCurrentMap().saveMap(l_cmd.handleSaveMap());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performEditMap(Command l_cmd, Player p_player) throws IOException, MapInvalidException, InvalidCommand {
        GameEngine.log("StartUpPhase::performMapEdit",LogLevel.SUBHEADING,"MAP section starts");
        getCurrentGameState().getCurrentMap().editMap(l_cmd.handleEditMap());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performAssignCountries(Player p_player) throws InvalidCommand, MapInvalidException, InvalidState {
        // Two mandatory checks has to be done here
        // 1. If the map is loaded
        // 2. If the players are added
        if(!Map.d_isMapLoaded){
            GameEngine.log("StartUpPhase::performAssignCountries",LogLevel.BASICLOG,"Map not " +
                    "loaded, assigncountries failed");
            throw new MapInvalidException("Please load the map before peforming assigncountries");
        }

        if(d_currentGameState.getPlayerController().getAllPlayers().isEmpty()){
            GameEngine.log("StartUpPhase::performAssignCountries",LogLevel.BASICLOG,"No players" +
                    " added, assigncountries failed");
            throw new InvalidState("Please add the players before performing assigncountries");
        }

        if(d_currentGameState.getPlayerController().getAllPlayers().size() > d_currentGameState.getCurrentMap().getAllCountriesAsList().size()){
            GameEngine.log("StartUpPhase::performAssignCountries", LogLevel.BASICLOG," Game should "
            + "have more countries than players ");
            throw new InvalidState("Game should have less players than the number of countries.");
        }

        if(d_currentGameState.getPlayerController().getAllPlayers().size() < 2){
            GameEngine.log("StartUpPhase::performAssignCountries",LogLevel.BASICLOG,"Number of " +
                    "players are less than 1, assigncountries failed");
            throw new InvalidState("We need at least two players , to play this game. Kindly add more players....");
        }

        d_currentGameState.getPlayerController().assignCountries(d_currentGameState);
        d_currentGameState.getPlayerController().assignArmies(d_currentGameState);
        GameEngine.log("StartUpPhase::performAssignCountries",LogLevel.BASICLOG," changing the " +
                "phase to Issue Orde Phase");
        d_gameEngine.setIssueOrderPhase();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performCreatePlayers(Command l_cmd, Player p_player) throws InvalidCommand {
        if(!Map.d_isMapLoaded){
            System.out.println("Please load the map before creating players ...");
            return;
        }
        GameEngine.log("StartUpPhase::performCreatePlayers",LogLevel.SUBHEADING,"Player Section Starts");
        d_currentGameState.getPlayerController().addRemovePlayers(l_cmd.handleAddAndRemovePlayer());
        // Player validity check happens before the assigncountries

    }

    /**
     * {@inheritDoc}
     */
    public void printStartingOptions(){
        System.out.println("""
                *********************************************************************************
                                    WARZONE GAME ( START UP PHASE ) 
                *********************************************************************************
                01. editcontinent -add continentID continentvalue -remove continentID
                02. editcountry -add countryID continentID -remove countryID
                03. editneighbor -add countryID neighborcountryID -remove countryID neighborcountryID
                04. showmap
                05. savemap filename
                06. editmap filename
                07. validatemap
                08. gameplayer -add playername -remove playername
                09. assigncountries
                10. loadmap
                """);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initPhase() throws InvalidState {
        GameEngine.log("StartUpPhase : initPhase", LogLevel.HEADING,"Start Up Phase");
        printStartingOptions();
        while(d_gameEngine.getCurrentPhase() instanceof StartUpPhase){
            System.out.println(System.lineSeparator() + "Enter game commands or type exit for quitting....");
            String l_commandEntered = this.d_scanner.nextLine();
            GameEngine.log("StartUpPhase::initPhase",LogLevel.BASICLOG,"Command Entered \""+ l_commandEntered + "\"");
            commandHandler(l_commandEntered,null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performDeployArmies(Command l_cmd, Player p_player) throws InvalidCommand {
        System.out.println("Invalid Operation");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performAdvanceArmies(Command l_cmd, Player p_player) throws InvalidCommand, InvalidState {
        System.out.println("Invalid Operation");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performCardHandle(Command l_cmd, Player p_player) throws InvalidCommand, InvalidState {
        System.out.println("Invalid Operation");
    }
}
