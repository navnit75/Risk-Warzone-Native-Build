package org.Model.Phases;

import org.Controller.GameEngine;
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

/**
 * This class handles the issue order phase of the GAME. Described the ISSUE ORDER STATE of the PHASE class
 */
public class IssueOrderPhase extends Phase{


    public IssueOrderPhase(GameEngine l_gameEngine, GameState l_currentGameState) {
        super(l_gameEngine, l_currentGameState);
    }

    /**
     * Helper function to check if the player has more orders left
     * @param l_player : Provided player object
     */
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

    /**
     * Helper function to ask user for extra prompt for continuing the turn
     * @param p_player : Player on behalf of whom prompt should be asked.
     */
    public void askForOrder(Player p_player){
        System.out.println("\nPlease enter command to issue order for player : " + p_player.getPlayerName()
                + " or give showmap command to view current state of the game.");
        String l_commandEntered = d_scanner.nextLine();
        commandHandler(l_commandEntered,p_player);
    }

    /**
     * Helper function which helps in accepting order from the player playing
     * @throws InvalidState : If the user enteres wrong input
     */
    public void orderAcceptingBlock() throws InvalidState{
        do {
                for (Player l_player : d_currentGameState.getPlayerController().getAllPlayers()) {
                    if (l_player.getMoreOrderFlag() && !l_player.getPlayerName().equals("Neutral")) {
                        this.askForOrder(l_player);
                        this.checkForMoreOrders(l_player);

                    }
                }
        } while (d_currentGameState.getPlayerController().checkForMoreOrders());
        d_gameEngine.setOrderExecutionPhase();

    }

    /**
     * {@inheritDoc}
     */
    @Override
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void initPhase() throws InvalidState {
        printStartingOptions();
        GameEngine.log("IssueOrderPhase::initPhase", LogLevel.HEADING,"Issue Order Phase");
        while (d_gameEngine.getCurrentPhase() instanceof IssueOrderPhase) {
              orderAcceptingBlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performDeployArmies(Command l_cmd, Player p_player) throws InvalidCommand, InvalidState {
        GameEngine.log("IssueOrderPhase::performDeployArmies",LogLevel.BASICLOG,p_player.getPlayerName() +
                " trying to perform Deploy order");
        p_player.createDeployOrder(l_cmd);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performAdvanceArmies(Command l_cmd, Player p_player) throws InvalidCommand, InvalidState {
        GameEngine.log("IssueOrderPhase::performAdvanceArmies",LogLevel.BASICLOG,p_player.getPlayerName() +
                " trying to perform Advance Armies order");
        p_player.createAdvanceOrder(l_cmd,d_currentGameState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performCardHandle(Command l_cmd, Player p_player) throws InvalidCommand, InvalidState {
        if(p_player.getAllCards().contains(l_cmd.getMainOperation())){
            GameEngine.log("IssueOrderPhase::performCardHandle",LogLevel.BASICLOG,p_player.getPlayerName() +
                    " trying to perform Card based order");
            p_player.createCardOrder(l_cmd,d_currentGameState);
        }
        else {
            GameEngine.log("IssueOrderPhase::performCardHandle",LogLevel.BASICLOG,"Player doesn't " +
                    "have the before mentioned card.");
            throw new InvalidCommand("Player doesn't have the before mentioned card.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performEditContinent(Command l_cmd, Player p_player) throws MapInvalidException, InvalidCommand {
        System.out.println("Invalid Operation..");
        askForOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performEditCountry(Command l_cmd, Player p_player) throws MapInvalidException, InvalidCommand {
        System.out.println("Invalid Operation..");
        askForOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performEditNeighbour(Command l_cmd, Player p_player) throws MapInvalidException, InvalidCommand {
        System.out.println("Invalid Operation..");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performLoadMap(Command l_cmd, Player p_player) throws InvalidCommand, FileNotFoundException, MapInvalidException {
        System.out.println("Invalid Operation..");
        askForOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performShowMap(Player p_player) throws InvalidCommand {
        MapView l_showColorMap = new MapView(d_currentGameState);
        l_showColorMap.showMap();
        askForOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performValidateMap(Player p_player) throws MapInvalidException, InvalidState {
        System.out.println("Invalid Operation..");
        askForOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performSaveMap(Command l_cmd, Player p_player) throws IOException, MapInvalidException, InvalidCommand {
        System.out.println("Invalid Operation..");
        askForOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performEditMap(Command l_cmd, Player p_player) throws IOException, MapInvalidException, InvalidCommand {
        System.out.println("Invalid Operation..");
        askForOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performAssignCountries(Player p_player) throws InvalidCommand, MapInvalidException, InvalidState {
        System.out.println("Invalid Operation");
        askForOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performCreatePlayers(Command l_cmd, Player p_player) throws InvalidCommand {
        System.out.println("Invalid Operation");
        askForOrder(p_player);
    }


}
