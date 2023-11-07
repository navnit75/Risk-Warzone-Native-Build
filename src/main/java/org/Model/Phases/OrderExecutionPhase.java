package org.Model.Phases;

import org.Controller.GameController;
import org.Exceptions.InvalidCommand;
import org.Exceptions.InvalidState;
import org.Exceptions.MapInvalidException;
import org.Model.GameState;
import org.Model.Orders.Order;
import org.Model.Player;
import org.Utils.Command;
import org.Utils.LogLevel;
import org.Views.MapView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Comparator;

public class OrderExecutionPhase extends Phase{
    public OrderExecutionPhase(GameController l_gameController, GameState l_currentGameState) {
        super(l_gameController, l_currentGameState);
    }

    @Override
    protected void performEditContinent(Command l_cmd, Player p_player) throws MapInvalidException, InvalidCommand {
        System.out.println("Invalid Operation.");
    }

    @Override
    protected void performEditCountry(Command l_cmd, Player p_player) throws MapInvalidException, InvalidCommand {
        System.out.println("Invalid Operation.");
    }

    @Override
    protected void performEditNeighbour(Command l_cmd, Player p_player) throws MapInvalidException, InvalidCommand {
        System.out.println("Invalid Operation.");
    }

    @Override
    protected void performLoadMap(Command l_cmd, Player p_player) throws InvalidCommand, FileNotFoundException, MapInvalidException {
        System.out.println("Invalid Operation.");
    }

    @Override
    protected void performShowMap(Player p_player) throws InvalidCommand {
        MapView l_mapView = new MapView(d_currentGameState);
        l_mapView.showMap();
    }

    @Override
    protected void performValidateMap(Player p_player) throws MapInvalidException, InvalidState {
        System.out.println("Invalid Operation.");
    }

    @Override
    protected void performSaveMap(Command l_cmd, Player p_player) throws IOException, MapInvalidException, InvalidCommand {
        System.out.println("Invalid Operation.");
    }

    @Override
    protected void performEditMap(Command l_cmd, Player p_player) throws IOException, MapInvalidException, InvalidCommand {
        System.out.println("Invalid Operation.");
    }

    @Override
    protected void performAssignCountries(Player p_player) throws InvalidCommand, MapInvalidException, InvalidState {
        System.out.println("Invalid Operation.");
    }

    @Override
    protected void performCreatePlayers(Command l_cmd, Player p_player) throws InvalidCommand {
        System.out.println("Invalid Operation.");
    }

    public void printStartingOptions(){
        System.out.println("""
                
                PHASE -> ORDER EXECUTION
                *********************************************************************************
                                    WARZONE GAME ( ORDER EXECUTION) 
                *********************************************************************************
                01. \"showmap\" to know the current State of the players 
                02. (Y/y) to continue with the game and provide 
                03. (N/n) to exit the game now and declare the winner 
                04. exit 
                """);
    }

    public void printAllOrdersExecuted(){
        System.out.println("""
                -> !!! ALL ORDERS EXECUTED !!!
                """);
    }
    @Override
    public void initPhase() throws InvalidState {
        GameController.log("OrderExecutionPhase::initPhase", LogLevel.HEADING,"Order Execution Phase");
        executeOrders();
        GameController.log("OrdeExecutionPhase::initPhase",LogLevel.BASICLOG,"All orders executed.");
        printAllOrdersExecuted();
        if(checkEndOfTheGame())
            commandHandler("exit",null);
        printStartingOptions();

        while(d_gameController.getCurrentPhase() instanceof OrderExecutionPhase){
            String l_userInput = d_scanner.nextLine();
            if(l_userInput.equalsIgnoreCase("showmap")) {
                commandHandler("showmap", null);
                printStartingOptions();
            } else if(l_userInput.equalsIgnoreCase("y")){
                d_currentGameState.getPlayerController().assignArmies(d_currentGameState);
                d_currentGameState.getPlayerController().resetPlayerFlag();
                GameController.log("OrderExecutionPhase::initPhase",LogLevel.BASICLOG," Received the " +
                        "user input to continue with the game " +
                        "changing the state to ISSUE ORDER PHASE");
                d_gameController.setIssueOrderPhase();
            } else if(l_userInput.equalsIgnoreCase("N")){
                GameController.log("OrderExecutionPhase::initPhase",LogLevel.BASICLOG," User doesn't " +
                        "want to play.  " + "WINNER is : " + declareWinner().getPlayerName());
                System.out.println(declareWinner().getPlayerName() + " is the winner on the basis of Maximum country " +
                        "owned");
                commandHandler("exit",null);

            } else {
                throw new InvalidState("Invalid Input");
            }
        }
    }

    private Player declareWinner(){
        Player l_player = d_currentGameState.getPlayerController().getAllPlayers()
                .stream()
                .max(Comparator.comparingInt(lPlayer -> lPlayer.getCountryCaptured().size()))
                .orElse(null);
        if(l_player != null) {
            GameController.log("OrderExecutionPhase::declareWinner", LogLevel.BASICLOG,
                    l_player.getPlayerName() + " is the declared winner. ");
        }
        return l_player;
    }

    private void addPlayerNeutral(){
        Player p_player = getCurrentGameState().getPlayerController().getPlayerByName("Neutral");
        if(p_player == null){
            Player l_playerNeutral = new Player("Neutral");
            l_playerNeutral.setMoreOrderFlag(false);
            getCurrentGameState().getPlayerController().addPlayer(l_playerNeutral);
            GameController.log("OrdeExecutionPhase::addPlayerNeutral",LogLevel.BASICLOG,"Neutral player added.");
        }
    }
    public void executeOrders(){
        addPlayerNeutral();
        // Executing orders

        GameController.log("OrderExecutionPhase::executeOrder",LogLevel.BASICLOG,"Order Execution Phase");
        while(d_currentGameState.getPlayerController().ordersRemaining()){
            for(Player l_player : d_currentGameState.getPlayerController().getAllPlayers()){

                Order l_order = l_player.next_order();
                if(l_order != null){
                    l_order.execute(d_currentGameState);
                    GameController.log("OrderExecutionPhase::executeOrder",LogLevel.BASICLOG,
                            l_player.getPlayerName() + " -> " + l_order.getOrder());

                }
            }
        }

    }

    @Override
    public void performDeployArmies(Command l_cmd, Player p_player) throws InvalidCommand {
        System.out.println("Invalid Operation");
    }

    @Override
    public void performAdvanceArmies(Command l_cmd, Player p_player) throws InvalidCommand, InvalidState {
        System.out.println("Invalid Operation.");
    }

    @Override
    public void performCardHandle(Command l_cmd, Player p_player) throws InvalidCommand, InvalidState {
        System.out.println("Invalid Operation.");
    }

    public Boolean checkEndOfTheGame(){
        int l_totalCountriesCount = d_currentGameState.getCurrentMap().getAllCountriesAsList().size();
        for(Player l_player : d_currentGameState.getPlayerController().getAllPlayers()){
            if(l_player.getCountryCaptured().size() == l_totalCountriesCount){
                System.out.println(l_player.getPlayerName() + " HAS WIN THE GAME.");
                System.out.println("EXITING THE GAME");
                GameController.log("OrderExecutionPhase::checkEndOfTheGame",LogLevel.BASICLOG,l_player.getPlayerName()
                + " WINS THE GAME.");
                return true;
            }
        }
        return false;
    }
}
