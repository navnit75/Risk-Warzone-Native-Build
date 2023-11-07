package org.Model.Phases;

import org.Controller.GameController;
import org.Exceptions.InvalidCommand;
import org.Exceptions.InvalidState;
import org.Exceptions.MapInvalidException;
import org.Model.GameState;
import org.Model.Player;
import org.Utils.Command;
import org.Utils.LogLevel;
import org.Views.MapView;

import java.io.FileNotFoundException;
import java.io.IOException;

public class IssueOrderPhase extends Phase{


    public IssueOrderPhase(GameController l_gameController, GameState l_currentGameState) {
        super(l_gameController, l_currentGameState);
    }

    public void checkForMoreOrders(Player l_player){
        System.out.println("\nDo you still want to give order for player : " + l_player.getPlayerName()
                + " in next turn ? \nPress Y for Yes or N for No");
        String l_nextOrderCheck = d_scanner.nextLine();
        if(l_nextOrderCheck.equalsIgnoreCase("Y") || l_nextOrderCheck.equalsIgnoreCase("N")){
            l_player.setMoreOrderFlag(l_nextOrderCheck.equalsIgnoreCase("Y"));
        } else {
            System.out.println("Invalid input provided. ");
            checkForMoreOrders(l_player);
        }
    }

    public void askForOrder(Player p_player){
        System.out.println("\nPlease enter command to issue order for player : " + p_player.getPlayerName()
                + " or give showmap command to view current state of the game.");
        String l_commandEntered = d_scanner.nextLine();
        commandHandler(l_commandEntered,p_player);
    }
    public void orderAcceptingBlock() throws InvalidState{
        do {
                for (Player l_player : d_currentGameState.getPlayerController().getAllPlayers()) {
                    if (l_player.getMoreOrderFlag() && !l_player.getPlayerName().equals("Neutral")) {
                        this.askForOrder(l_player);
                        this.checkForMoreOrders(l_player);

                    }
                }
        } while (d_currentGameState.getPlayerController().checkForMoreOrders());
        d_gameController.setOrderExecutionPhase();

    }

    public void printStartingOptions(){
        System.out.println("""
                
                *********************************************************************************
                                    WARZONE GAME ( ISSUE ORDERS ) 
                *********************************************************************************
                01. deploy countryID numarmies
                02. advance countrynamefrom countynameto numarmies
                03. bomb countryID ( Only if the player has card BOMB )
                04. blockade countryID ( Only if the player has card BLOCKADE )
                05. airlift sourcecountryID targetcountryID numarmies ( Only if the player has card AIRLIFT )
                06. negotiate playerID ( Only if the player has card NEGOTIATE )
                07. showmap
                """);
    }
    @Override
    public void initPhase() throws InvalidState {
        printStartingOptions();
        GameController.log("IssueOrderPhase::initPhase", LogLevel.HEADING,"Issue Order Phase");
        while (d_gameController.getCurrentPhase() instanceof IssueOrderPhase) {
              orderAcceptingBlock();
        }
    }

    @Override
    public void performDeployArmies(Command l_cmd, Player p_player) throws InvalidCommand, InvalidState {
        GameController.log("IssueOrderPhase::performDeployArmies",LogLevel.BASICLOG,p_player.getPlayerName() +
                " trying to perform Deploy order");
        p_player.createDeployOrder(l_cmd);

    }

    @Override
    public void performAdvanceArmies(Command l_cmd, Player p_player) throws InvalidCommand, InvalidState {
        GameController.log("IssueOrderPhase::performAdvanceArmies",LogLevel.BASICLOG,p_player.getPlayerName() +
                " trying to perform Advance Armies order");
        p_player.createAdvanceOrder(l_cmd,d_currentGameState);
    }

    @Override
    public void performCardHandle(Command l_cmd, Player p_player) throws InvalidCommand, InvalidState {
        if(p_player.getAllCards().contains(l_cmd.getMainOperation())){
            GameController.log("IssueOrderPhase::performCardHandle",LogLevel.BASICLOG,p_player.getPlayerName() +
                    " trying to perform Card based order");
            p_player.createCardOrder(l_cmd,d_currentGameState);
        }
        else {
            GameController.log("IssueOrderPhase::performCardHandle",LogLevel.BASICLOG,"Player doesn't " +
                    "have the before mentioned card.");
            throw new InvalidCommand("Player doesn't have the before mentioned card.");
        }
    }

    @Override
    protected void performEditContinent(Command l_cmd, Player p_player) throws MapInvalidException, InvalidCommand {
        System.out.println("Invalid Operation..");
        askForOrder(p_player);
    }

    @Override
    protected void performEditCountry(Command l_cmd, Player p_player) throws MapInvalidException, InvalidCommand {
        System.out.println("Invalid Operation..");
        askForOrder(p_player);
    }

    @Override
    protected void performEditNeighbour(Command l_cmd, Player p_player) throws MapInvalidException, InvalidCommand {
        System.out.println("Invalid Operation..");
    }

    @Override
    protected void performLoadMap(Command l_cmd, Player p_player) throws InvalidCommand, FileNotFoundException, MapInvalidException {
        System.out.println("Invalid Operation..");
        askForOrder(p_player);
    }

    @Override
    protected void performShowMap(Player p_player) throws InvalidCommand {
        MapView l_showColorMap = new MapView(d_currentGameState);
        l_showColorMap.showMap();
        askForOrder(p_player);
    }

    @Override
    protected void performValidateMap(Player p_player) throws MapInvalidException, InvalidState {
        System.out.println("Invalid Operation..");
        askForOrder(p_player);
    }

    @Override
    protected void performSaveMap(Command l_cmd, Player p_player) throws IOException, MapInvalidException, InvalidCommand {
        System.out.println("Invalid Operation..");
        askForOrder(p_player);
    }

    @Override
    protected void performEditMap(Command l_cmd, Player p_player) throws IOException, MapInvalidException, InvalidCommand {
        System.out.println("Invalid Operation..");
        askForOrder(p_player);
    }

    @Override
    protected void performAssignCountries(Player p_player) throws InvalidCommand, MapInvalidException, InvalidState {
        System.out.println("Invalid Operation");
        askForOrder(p_player);
    }

    @Override
    protected void performCreatePlayers(Command l_cmd, Player p_player) throws InvalidCommand {
        System.out.println("Invalid Operation");
        askForOrder(p_player);
    }


}
