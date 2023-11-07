package org.Model.Phases;

import org.Controller.GameController;
import org.Model.GameState;
import org.Model.Map;
import org.Model.Player;
import org.Utils.Command;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.Exceptions.*;
import org.Utils.LogLevel;

public class StartUpPhase extends Phase{

    public StartUpPhase(GameController l_gameController, GameState l_currentGameState) {
        super(l_gameController, l_currentGameState);
    }

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

    @Override
    protected void performLoadMap(Command l_cmd, Player p_player) throws InvalidCommand, FileNotFoundException, MapInvalidException {
        GameController.log("StartUpPhase::peformLoadMap",LogLevel.SUBHEADING,"MAP section starts");
        getCurrentGameState().getCurrentMap().loadMap(l_cmd.handleLoadMap());
        if(Map.d_isMapLoaded){
            System.out.println("Map loaded successfully!");
        }
    }

    @Override
    protected void performShowMap(Player p_player) throws InvalidCommand {
        getCurrentGameState().getCurrentMap().showMap();
    }

    @Override
    protected void performValidateMap(Player p_player) throws MapInvalidException, InvalidState {
        if(!Map.d_isMapLoaded)
            throw new InvalidState("Please load the map first.");
        Boolean l_result = getCurrentGameState().getCurrentMap().validateMap();
        if(l_result){
            GameController.log("StartUpPhase::performValidateMap",LogLevel.BASICLOG,"Map Validation " +
                    "Successful.");
            System.out.println("Map Validation Successful!");
        } else{
            GameController.log("StartUpPhase::performValidateMap",LogLevel.BASICLOG,"Map Validation" +
                    " Unsuccessful.");
            System.out.println("Map is Invalid!");
        }

    }

    @Override
    protected void performSaveMap(Command l_cmd , Player p_player) throws IOException, MapInvalidException, InvalidCommand {
        getCurrentGameState().getCurrentMap().saveMap(l_cmd.handleSaveMap());
    }

    @Override
    protected void performEditMap(Command l_cmd, Player p_player) throws IOException, MapInvalidException, InvalidCommand {
        GameController.log("StartUpPhase::performMapEdit",LogLevel.SUBHEADING,"MAP section starts");
        getCurrentGameState().getCurrentMap().editMap(l_cmd.handleEditMap());
    }

    @Override
    protected void performAssignCountries(Player p_player) throws InvalidCommand, MapInvalidException, InvalidState {
        // Two mandatory checks has to be done here
        // 1. If the map is loaded
        // 2. If the players are added
        if(!Map.d_isMapLoaded){
            GameController.log("StartUpPhase::performAssignCountries",LogLevel.BASICLOG,"Map not " +
                    "loaded, assigncountries failed");
            throw new MapInvalidException("Please load the map before peforming assigncountries");
        }

        if(d_currentGameState.getPlayerController().getAllPlayers().isEmpty()){
            GameController.log("StartUpPhase::performAssignCountries",LogLevel.BASICLOG,"No players" +
                    " added, assigncountries failed");
            throw new InvalidState("Please add the players before performing assigncountries");
        }

        if(d_currentGameState.getPlayerController().getAllPlayers().size() > d_currentGameState.getCurrentMap().getAllCountriesAsList().size()){
            GameController.log("StartUpPhase::performAssignCountries", LogLevel.BASICLOG," Game should "
            + "have more countries than players ");
            throw new InvalidState("Game should have less players than the number of countries.");
        }

//        if(d_currentGameState.getPlayerController().getAllPlayers().size() < 2){
//            GameController.log("StartUpPhase::performAssignCountries",LogLevel.BASICLOG,"Number of " +
//                    "players are less than 1, assigncountries failed");
//            throw new InvalidState("We need at least two players , to play this game. Kindly add more players....");
//        }

        d_currentGameState.getPlayerController().assignCountries(d_currentGameState);
        d_currentGameState.getPlayerController().assignArmies(d_currentGameState);
        GameController.log("StartUpPhase::performAssignCountries",LogLevel.BASICLOG," changing the " +
                "phase to Issue Orde Phase");
        d_gameController.setIssueOrderPhase();
    }

    @Override
    protected void performCreatePlayers(Command l_cmd, Player p_player) throws InvalidCommand {
        if(!Map.d_isMapLoaded){
            System.out.println("Please load the map before creating players ...");
            return;
        }
        GameController.log("StartUpPhase::performCreatePlayers",LogLevel.SUBHEADING,"Player Section Starts");
        d_currentGameState.getPlayerController().addRemovePlayers(l_cmd.handleAddAndRemovePlayer());
        // Player validity check happens before the assigncountries

    }

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
                """);
    }

    @Override
    public void initPhase() throws InvalidState {
        GameController.log("StartUpPhase : initPhase", LogLevel.HEADING,"Start Up Phase");
        printStartingOptions();
        while(d_gameController.getCurrentPhase() instanceof StartUpPhase){
            System.out.println(System.lineSeparator() + "Enter game commands or type exit for quitting....");
            String l_commandEntered = this.d_scanner.nextLine();
            GameController.log("StartUpPhase::initPhase",LogLevel.BASICLOG,"Command Entered \""+ l_commandEntered + "\"");
            commandHandler(l_commandEntered,null);
        }
    }

    @Override
    public void performDeployArmies(Command l_cmd, Player p_player) throws InvalidCommand {
        System.out.println("Invalid Operation");
    }

    @Override
    public void performAdvanceArmies(Command l_cmd, Player p_player) throws InvalidCommand, InvalidState {
        System.out.println("Invalid Operation");
    }

    @Override
    public void performCardHandle(Command l_cmd, Player p_player) throws InvalidCommand, InvalidState {
        System.out.println("Invalid Operation");
    }
}
