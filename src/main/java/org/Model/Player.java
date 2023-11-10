package org.Model;

import org.Constants.AllTheConstants;
import org.Controller.GameEngine;
import org.Exceptions.InvalidCommand;
import org.Exceptions.InvalidState;
import org.Model.Orders.Advance;
import org.Model.Orders.Cards.*;
import org.Model.Orders.Deploy;
import org.Model.Orders.Order;
import org.Utils.Command;
import org.Utils.LogLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * Player class handles the properties related a Player of the game and provide the features to change those properties.
 */
public class Player {
    /**
     * Color assigned to player would be used in rendering the players on Showmap part of the project
     */
    private String d_color;

    /**
     * Name of the player
     */
    private String d_playerName;

    /**
     * Contains the list of the country captured by Player
     */
    List<Country> d_countryCaptured;

    /**
     * Contains the lsit of the continent captured by Player
     */
    List<Continent> d_continentsOwned;

    /**
     * Stored order list , follows COMMAND PATTERN. Executed when the game goes to ORDER EXECUTION PHASE
     */
    List<Order> d_orderList;

    /**
     * Keeps a count of the NUM OF ARMIES
     */
    Integer d_numOfArmiesRemaining;

    /**
     * Keeps a track of the card assigned to player for a particular TURN
     */
    List<String> d_cardsOwnedByPlayer  = new ArrayList<String>();

    /**
     * List of players the current player has used NEGOTIATE card against.
     * Is useful when doing the ADVANCE operation as this list can be checked for avoiding the advance.
     */
    List<Player> d_negotiatedPlayers = new ArrayList<Player>();

    /**
     * Temporary flag to keep track of the order input for a player in ISSUE ORDER PHASE
     */
    Boolean d_moreOrdersFlag;

    /**
     * Flag to keep track of if card has been assigned to a Player.
     * Because , it might happen that a player captures two territories in a single turn.
     * But game requires we only provide single. This flag is used to toggle between true and false to keep track of
     * the use case.
     */
    Boolean d_cardAssignedForThisTurnFlag;

    /**
     * Default constructor
     */
    public Player(){}

    /**
     * Parameterized constructor
     * @param p_playerName : Name of the player
     */
    public Player(String p_playerName){
        this.d_playerName = p_playerName;
        this.d_numOfArmiesRemaining = 0;
        this.d_countryCaptured = new ArrayList<Country>();
        this.d_orderList = new ArrayList<Order>();
        this.d_moreOrdersFlag = true;
        this.d_cardAssignedForThisTurnFlag = false;
    }

    /**
     * Getter for the Player name data member
     * @return d_playerName : Name of the player
     */
    public String getPlayerName(){return this.d_playerName; }

    /**
     * Setter fo the player name data member
     * @param p_playerName : Name of the player to be set
     */
    public void setPlayerName(String p_playerName){this.d_playerName =  p_playerName; }

    /**
     * Gets the color assigned to Player
     * @return d_color : the color string assigned to the Player
     */
    public String getColor(){ return this.d_color; }

    /**
     * Sets the color assigned to the player
     * @param p_color : the color string assigned to the player
     */
    public void setColor(String p_color){ this.d_color = p_color; }

    /**
     * Returns the list of the country captured by player
     * @return d_countryCaptured : List of the country captured  by player
     */
    public List<Country> getCountryCaptured(){return this.d_countryCaptured; }

    /**
     * Sets the list of the country captured by player
     * @param p_countryCaptured : List of the country to be set for a player
     */
    public void setCountryCaptured(List<Country> p_countryCaptured){this.d_countryCaptured = p_countryCaptured; }

    /**
     * Getter to get the current state of the getMoreOrderFlag
     * @return d_moreOrdersFlag
     */
    public Boolean getMoreOrderFlag(){return this.d_moreOrdersFlag; }

    /**
     * Getter to get the current state of the cardAssignedForThisTurnFlag data member
     * @return d_cardAssignedForThisTurnFlag
     */
    public Boolean getCardAssignedForThisTurnFlag(){return this.d_cardAssignedForThisTurnFlag; }

    /**
     * setter for the d_cardAssignedForThisTurnFlag
     * @param p_flag : The value to be set
     */
    public void setCardAssignedForThisTurnFlag(Boolean p_flag){this.d_cardAssignedForThisTurnFlag = p_flag; }

    /**
     * Setter for the d_moreOrdersFlags
     * @param p_moreOrders : the value to be set
     */
    public void setMoreOrderFlag(Boolean p_moreOrders){this.d_moreOrdersFlag = p_moreOrders; }

    /**
     * Add a country to the list of country captured by player
     * @param p_country : The country to be added.
     */
    public void addCountryCaptured(Country p_country){
        if(this.d_countryCaptured == null) d_countryCaptured = new ArrayList<>();
        d_countryCaptured.add(p_country);
        GameEngine.log("Player::addCountryCaptured",LogLevel.BASICLOG,d_playerName +
                " --CAPTURED--> " + p_country.getCountryName());
    }

    /**
     * Getter to get the list of all players to which current player have negotiated to
     * @return d_negotiatedPlayers : gets the value of data member of class d_negotiatedPlayers
     */
    public List<Player> getAllNegotiatedPlayers(){ return this.d_negotiatedPlayers; }

    /**
     * Setter to get the list of all players to which current player have negotiated to
     * @param p_negotiatedPlayers : sets the value of d_negotiatedPlayers to p_negotiatedPlayers
     */
    public void setAllNegotiatedPlayers(List<Player> p_negotiatedPlayers){ this.d_negotiatedPlayers = p_negotiatedPlayers; }

    /**
     * Returns the player object , provided the name. With whom the current player has TRUCE with
     * @param p_playerName : Name of the player contained in a string
     * @return l_player : Returns a player object if the player is found in the negotiated list, if not found returns null.
     */
    public Player getNegotiatedPlayerByName(String p_playerName){
        return d_negotiatedPlayers
                .stream()
                .filter(l_player->l_player.getPlayerName().equals(p_playerName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Add a player to d_negotiatedPLayers
     * @param p_player : Object representing a player to be added to the negotiated list
     */
    public void addNegotiatedPlayer(Player p_player){
        this.d_negotiatedPlayers.add(p_player);
    }

    /**
     * Check if the country to be attacked, if the current player has TRUCE with it.
     * OR we can say, the country to be attacked is not owned by a player. With whom the current player has negotiated with.
     * @param p_country : Name of the country for which it needs to be checked
     * @return true / false : If country cannot be attacked returns TRUE or return FALSE
     */
    public Boolean negotiationCheckForAttack(String p_country){
        for(Player l_player : d_negotiatedPlayers){
            if(l_player.getCapturedCountryByName(p_country) != null)
                return true; // Cannot attack
        }
        return false; // Can attack
    }

    /**
     * If the player has used the negotiate card, usually the d_negotiatedPlayers list will have an entry
     * This entry needs to be cleared. When the turn ends. This function facilitates that.
     */
    public void clearNegotiatedPlayers(){ d_negotiatedPlayers.clear(); }

    /**
     * Searching the list of the country by the country name and returns the country object representing particular country.
     * @param p_countryName : Name of the country
     * @return l_country , if the country is found, else returns NULL.
     */
    public Country getCapturedCountryByName(String p_countryName){
        return d_countryCaptured
                .stream()
                .filter(l_country->l_country.getCountryName()
                        .equalsIgnoreCase(p_countryName))
                .findFirst()
                .orElse(null);
    }


    /**
     * Searching the list of the continent by the continent name and returns the continent object representing particular continent.
     * @param p_continentName : Name of the continent
     * @return l_continent , if the continent is found , else returns NULL.
     */
    public Continent getCapturedContinentByName(String p_continentName){
        return d_continentsOwned
                .stream()
                .filter(l_continent->l_continent.getContinentName()
                        .equalsIgnoreCase(p_continentName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Getter for the list of Continents owned by player
     * @return d_continentOwned
     */
    public List<Continent> getContinentOwned(){return this.d_continentsOwned;}

    /**
     * Adds the continent to the list of the Continent owned by player
     * @param p_continent : Continent object to be added
     */
    public void addContinentOwned(Continent p_continent){
        if(d_continentsOwned == null)
            d_continentsOwned = new ArrayList<>();
        d_continentsOwned.add(p_continent);
    }

    /**
     * Returns the list of not executed order of the player.
     * @return d_orderList : list of not executed order
     */
    public List<Order> getOrderList(){return this.d_orderList; }

    /**
     * Setter for the list of the not executed order.
     * @param p_orderList : the list of orders to be set
     */
    public void setOrderList(List<Order> p_orderList){ this.d_orderList = p_orderList; }

    /**
     * Getter for the number of reinforcement remaining with the player
     * @return d_numOfArmiesRemaining :Stores the number of reinforcement for player
     */
    public Integer getNumOfArmiesRemaining(){ return this.d_numOfArmiesRemaining; }

    /**
     * Setter for the number of reinforcement
     * @param d_num : Number to be set
     */
    public void setNumOfArmiesRemaining(Integer d_num){ this.d_numOfArmiesRemaining = d_num; }

    /**
     * Gets a list of cards assigned to player
     * @return d_cardsOwnedByPlayer : List of string contains the cards held by player
     */
    public List<String> getAllCards(){ return this.d_cardsOwnedByPlayer; }

    /**
     * Helper function for validation of armies to deploy
     * @param p_numOfArmiesToDeploy : Number of armies player wants to deploy
     * @return true or false : If validation is successful TRUE or else false
     */
    private Boolean validateDeployOrderArmies(int p_numOfArmiesToDeploy){
        return p_numOfArmiesToDeploy <= this.d_numOfArmiesRemaining;
    }

    /**
     * Helper function to help in creation of the Deploy Order
     * Stores the Deploy order in player based list called d_orderList
     * Later executed in OrderExecutionPhase of the game.
     * @param l_cmd : Command object which contains the deploy order string
     * @throws InvalidCommand : Throws InvalidCommand if the command provided by user is wrong
     * @throws InvalidState : If player doesn't have enough reinforcement to continue with the deployment
     */
    public void createDeployOrder(Command l_cmd) throws InvalidCommand, InvalidState {
        String l_targetCountry;
        Integer l_noOfArmies;

        l_targetCountry = l_cmd.handleDeployArmies().get(0);
        l_noOfArmies = Integer.parseInt(l_cmd.handleDeployArmies().get(1));
        if (!validateDeployOrderArmies(l_noOfArmies)) {
            GameEngine.log("Player::createDeployOrder", LogLevel.BASICLOG,"Deploy order starting " +
                    "validation failed");
            GameEngine.log("Player::createDeployOrder", LogLevel.BASICLOG,"Num of Reinforcement" +
                    " Player has " + this.getNumOfArmiesRemaining() + " Num of armies in the order : " + l_noOfArmies);
            System.out.println("Given deploy order cant be executed as armies in deploy order exceeds player's" +
                    " unallocated armies.");
            throw new InvalidState("You don't have enough Reinforcement for deployment to " + l_targetCountry);
        } else {
            Integer l_unallocatedarmies = d_numOfArmiesRemaining - l_noOfArmies;
            this.issue_order(new Deploy(this, l_targetCountry,l_noOfArmies));
            this.setNumOfArmiesRemaining(l_unallocatedarmies);
            GameEngine.log("Player::createDeployOrder", LogLevel.BASICLOG,"Deploy order object" +
                    " created and ready for execution" +
                    " Reinforcement Armies : " + l_unallocatedarmies);
        }

    }

    /**
     * Helper function to help in creation of the Advance Order
     * Stores the Advance order in player based list called d_orderList
     * Later executed in OrderExecutionPhase of the game.
     * @param l_cmd : Command object which contains the advance order string
     * @throws InvalidCommand : Throws InvalidCommand if the command provided by user is wrong
     */
    public void createAdvanceOrder(Command l_cmd,GameState p_currentGameState) throws InvalidCommand {
        Country l_sourceCountry = p_currentGameState.getCurrentMap().getCountryByName(l_cmd.handleAdvanceArmies().get(0));
        Country l_defendingCountry = p_currentGameState.getCurrentMap().getCountryByName(l_cmd.handleAdvanceArmies().get(1));
        Integer l_numOfArmies = Integer.parseInt(l_cmd.handleAdvanceArmies().get(2));

        boolean l_preValidityCheck = (l_sourceCountry != null) && (l_defendingCountry != null) &&
                (l_numOfArmies >= 0) && (l_sourceCountry.hasNeighbour(l_defendingCountry));
        if (l_preValidityCheck) {
            issue_order(new Advance(this, l_sourceCountry, l_defendingCountry, l_numOfArmies));
            GameEngine.log("Player::createAdvanceOrder", LogLevel.BASICLOG, "Advance order object" +
                    " created and ready for execution" +
                    " Advancing Armies  : " + l_numOfArmies +
                    " From : " + l_sourceCountry.getCountryName() +
                    " To : " + l_defendingCountry.getCountryName());
        } else {
            GameEngine.log("Player::createAdvanceOrder", LogLevel.BASICLOG, "Advance order failed");
            System.out.println("Invalid advance order provided");
        }
    }

    /**
     * Function as specified by build document which stores the order to the top of the order list according to COMMAND
     * PATTERN design.
     * @param d_order : Command object which contains the order needed for execution
     */
    public void issue_order(Order d_order){
        if(this.d_orderList == null)
            d_orderList = new ArrayList<Order>();
        this.d_orderList.add(d_order);
    }

    /**
     * Function as specified by build document which returns the order required for execution
     * @return l_order : The order needed for execution
     */
    public Order next_order(){
        if(d_orderList == null || d_orderList.isEmpty())
            return null;
        Order l_order = this.d_orderList.get(0);
        this.d_orderList.remove(0);
        return l_order;

    }

    /**
     * Assign random cards to player according to the turn.
     * @param p_gameState : Gamestate object for various common function.
     */
    public void assignCard(GameState p_gameState){
        int l_numOfCards = AllTheConstants.CARDS.size();
        int l_cardIndex = p_gameState.getRandomInteger(l_numOfCards,0);
        if(!d_cardAssignedForThisTurnFlag){
            this.d_cardsOwnedByPlayer.add(AllTheConstants.CARDS.get(l_cardIndex));
            GameEngine.log("Player::assignCard",LogLevel.BASICLOG,
                    this.d_playerName + " has been assigned " +
                            "Card : " + AllTheConstants.CARDS.get(l_cardIndex));
            setCardAssignedForThisTurnFlag(true);
        } else {
            GameEngine.log("Player::assignCard",LogLevel.BASICLOG,
                    this.d_playerName + " has CAPTURED but not assigned with card" +
                            " as he has already been assigned card in this turn");
        }
    }

    /**
     * Remove card from the player , list of cards.
     * @param p_card : The card which needs to be removed
     */
    public void removeCard(String p_card){
        this.d_cardsOwnedByPlayer.remove(p_card);
        GameEngine.log("Player::removeCard",LogLevel.BASICLOG,
                this.d_playerName + " has used : " + p_card);
    }

    /**
     * Function to create an Airlift based order and later using issue_order for adding the order to the players order list
     * @param l_cmd : Command object which has store the command issued from player
     * @param p_gameState : Gamestate object specifying the current state of the game
     * @throws InvalidCommand : If the command is invalid , the exception is raised
     * @throws InvalidState : If the countries provided is invalid , InvalidState exception is raised.
     */
    public void createAirliftOrderCard(Command l_cmd, GameState p_gameState) throws InvalidCommand, InvalidState {
        Country l_sourceCountry = p_gameState.getCurrentMap().getCountryByName(l_cmd.handleAirliftCommand().get(0));
        Country l_targetCountry = p_gameState.getCurrentMap().getCountryByName(l_cmd.handleAirliftCommand().get(1));
        Integer l_numOfArmies = Integer.parseInt(l_cmd.handleAirliftCommand().get(2));
        if(l_sourceCountry == null || l_targetCountry == null){
            GameEngine.log("Player::createAirliftOrder",LogLevel.BASICLOG," Invalid Airlift Order provided");
            throw new InvalidState("Airlift countries doesn't exist");

        } else {
            Card l_newCard = new Airlift(this, l_sourceCountry,l_targetCountry,l_numOfArmies);
            if(l_newCard.validateCommand(p_gameState)){
                GameEngine.log("Player::createAirliftOrder",LogLevel.BASICLOG," Airlift order object " +
                        " created and ready for execution" +
                        " Source Country : " + l_sourceCountry.getCountryName() +
                        " Target Country : " + l_targetCountry.getCountryName() +
                        " Army Count : " + l_numOfArmies);
                issue_order(l_newCard);
            }
        }
    }

    /**
     * Function to create  Bloackade based order and later using issue_order for adding the order to the players order list
     * @param l_cmd : Command object which has store the command issued from player
     * @param p_gameState : Gamestate object specifying the current state of the game
     * @throws InvalidCommand : If the command is invalid , the exception is raised
     * @throws InvalidState : If the countries provided is invalid , InvalidState exception is raised.
     */
    public void createBlockadeOrderCard(Command l_cmd, GameState p_gameState) throws InvalidCommand, InvalidState {
        Country l_country = p_gameState.getCurrentMap().getCountryByName(l_cmd.handleBlockadeCommand());
        if(l_country == null){
            GameEngine.log("Player::createBlockadeOrder",LogLevel.BASICLOG," Invalid Blockade Order" +
                    " country doesn't exist");
            throw new InvalidState("Country doesn't exist");

        } else {
            Card l_newCard = new Blockade(this, l_country);
            if(l_newCard.validateCommand(p_gameState)) {
                GameEngine.log("Player::createBlockadeOrder", LogLevel.BASICLOG, " Blockade order object " +
                        " created and ready for execution" +
                        " Blockade Country : " + l_country.getCountryName());
                issue_order(l_newCard);
            }
        }
    }

    /**
     * Function to create Bomb based order and later using issue_order for adding the order to the players order list
     * @param l_cmd : Command object which has store the command issued from player
     * @param p_gameState : Gamestate object specifying the current state of the game
     * @throws InvalidCommand : If the command is invalid , the exception is raised
     * @throws InvalidState : If the countries provided is invalid , InvalidState exception is raised.
     */
    public void createBombOrderCard(Command l_cmd, GameState p_gameState) throws InvalidCommand, InvalidState {
        Country l_country = p_gameState.getCurrentMap().getCountryByName(l_cmd.handleBombCommand());
        if(l_country == null){
            GameEngine.log("Player::createBombOrder", LogLevel.BASICLOG,"Invalid Bomb Order" +
                    "Country " + l_cmd.handleBombCommand() + " doesn't exist");
            throw new InvalidState("To be Bombed " + l_cmd.handleBombCommand() + "country doesn't exist.");
        } else {
            Card l_newCard = new Bomb(this, l_country);
            if(l_newCard.validateCommand(p_gameState)){
                GameEngine.log("Player::createBombOrder", LogLevel.BASICLOG, " Bomb order object " +
                        " created and ready for execution" +
                        " Bombed Country : " + l_country.getCountryName());
                issue_order(l_newCard);
            }
        }
    }

    /**
     * Function to create  Negotiate based order and later using issue_order for adding the order to the players order list
     * @param l_cmd : Command object which has store the command issued from player
     * @param p_gameState : Gamestate object specifying the current state of the game
     * @throws InvalidCommand : If the command is invalid , the exception is raised
     * @throws InvalidState : If the countries provided is invalid , InvalidState exception is raised.
     */
    public void createNegotiateOrderCard(Command l_cmd, GameState p_gameState) throws InvalidCommand, InvalidState {
        Player p_player1 = this;
        Player p_player2 = p_gameState.getPlayerController().getPlayerByName(l_cmd.handleNegotiateCommand());
        if(p_player2 == null){
            throw new InvalidState("Invalid Negotiate Order " +
                    "Player : " + l_cmd.handleNegotiateCommand() + " doesn't exist.");
        } else {
            Card l_newCard = new Negotiate(p_player1,p_player2);
            if(l_newCard.validateCommand(p_gameState)){
                GameEngine.log("Player::createNegotiateOrderCard", LogLevel.BASICLOG,
                        " Negotiate order object created and ready for execution" +
                                " Card Owner : " + p_player1.getPlayerName() +
                                " Negotiated With : " + p_player2.getPlayerName());
                issue_order(l_newCard);
            }
        }
    }

    /**
     * Function to create any Card based order and later using issue_order for adding the order to the players order list
     * @param l_cmd : Command object which has store the command issued from player
     * @param p_gameState : Gamestate object specifying the current state of the game
     * @throws InvalidCommand : If the command is invalid , the exception is raised
     * @throws InvalidState : If the countries provided is invalid , InvalidState exception is raised.
     */
    public void createCardOrder(Command l_cmd , GameState p_gameState) throws InvalidCommand, InvalidState{
        switch(l_cmd.getMainOperation()){
            case "airlift":{
                this.createAirliftOrderCard(l_cmd,p_gameState);
                break;
            }
            case "blockade":{
                this.createBlockadeOrderCard(l_cmd,p_gameState);
                break;
            }
            case "bomb":{
                this.createBombOrderCard(l_cmd,p_gameState);
                break;
            }
            case "negotiate":{
                this.createNegotiateOrderCard(l_cmd,p_gameState);
                break;
            }
            default:{
                GameEngine.log("Player::createCardOrder", LogLevel.BASICLOG,
                        "Invalid Card Provided." + l_cmd.getMainOperation());
                throw new InvalidState("Invalid Card Provided.");
            }
        }
    }

}


