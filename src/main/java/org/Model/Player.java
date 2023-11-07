package org.Model;

import org.Constants.AllTheConstants;
import org.Controller.GameController;
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

public class Player {
    private String d_color;
    private String d_playerName;
    List<Country> d_countryCaptured;
    List<Continent> d_continentsOwned;
    List<Order> d_orderList;
    Integer d_numOfArmiesRemaining;
    List<String> d_cardsOwnedByPlayer  = new ArrayList<String>();
    List<Player> d_negotiatedPlayers = new ArrayList<Player>();
    Boolean d_moreOrdersFlag;
    Boolean d_cardAssignedForThisTurnFlag;

    public Player(){}
    public Player(String p_playerName){
        this.d_playerName = p_playerName;
        this.d_numOfArmiesRemaining = 0;
        this.d_countryCaptured = new ArrayList<Country>();
        this.d_orderList = new ArrayList<Order>();
        this.d_moreOrdersFlag = true;
        this.d_cardAssignedForThisTurnFlag = false;
    }
    public String getPlayerName(){return this.d_playerName; }
    public void setPlayerName(String p_playerName){this.d_playerName =  p_playerName; }
    public String getColor(){ return this.d_color; }
    public void setColor(String p_color){ this.d_color = p_color; }
    public List<Country> getCountryCaptured(){return this.d_countryCaptured; }
    public void setCountryCaptured(List<Country> p_countryCaptured){this.d_countryCaptured = p_countryCaptured; }
    public Boolean getMoreOrderFlag(){return this.d_moreOrdersFlag; }
    public Boolean getCardAssignedForThisTurnFlag(){return this.d_cardAssignedForThisTurnFlag; }
    public void setCardAssignedForThisTurnFlag(Boolean p_flag){this.d_cardAssignedForThisTurnFlag = p_flag; }
    public void setMoreOrderFlag(Boolean p_moreOrders){this.d_moreOrdersFlag = p_moreOrders; }
    public void addCountryCaptured(Country p_country){
        if(this.d_countryCaptured == null) d_countryCaptured = new ArrayList<>();
        d_countryCaptured.add(p_country);
        GameController.log("Player::addCountryCaptured",LogLevel.BASICLOG,d_playerName +
                " --CAPTURED--> " + p_country.getCountryName());
    }

    public List<Player> getAllNegotiatedPlayers(){ return this.d_negotiatedPlayers; }
    public void setAllNegotiatedPlayers(List<Player> p_negotiatedPlayers){ this.d_negotiatedPlayers = p_negotiatedPlayers; }
    public Player getNegotiatedPlayerByName(String p_playerName){
        return d_negotiatedPlayers
                .stream()
                .filter(l_player->l_player.getPlayerName().equals(p_playerName))
                .findFirst()
                .orElse(null);
    }
    public void addNegotiatedPlayer(Player p_player){
        this.d_negotiatedPlayers.add(p_player);
    }
    public Boolean negotiationCheckForAttack(String p_country){
        for(Player l_player : d_negotiatedPlayers){
            if(l_player.getCapturedCountryByName(p_country) != null)
                return true; // Cannot attack
        }
        return false; // Can attack
    }
    public void clearNegotiatedPlayers(){ d_negotiatedPlayers.clear(); }

    public Country getCapturedCountryByName(String p_countryName){
        return d_countryCaptured
                .stream()
                .filter(l_country->l_country.getCountryName()
                        .equalsIgnoreCase(p_countryName))
                .findFirst()
                .orElse(null);
    }
    public Continent getCapturedContinentByName(String p_continentName){
        return d_continentsOwned
                .stream()
                .filter(l_continent->l_continent.getContinentName()
                        .equalsIgnoreCase(p_continentName))
                .findFirst()
                .orElse(null);
    }
    public List<Continent> getContinentOwned(){return this.d_continentsOwned;}
    public void addContinentOwned(Continent p_continent){
        if(d_continentsOwned == null)
            d_continentsOwned = new ArrayList<>();
        d_continentsOwned.add(p_continent);
    }
    public List<Order> getOrderList(){return this.d_orderList; }
    public void setOrderList(List<Order> p_orderList){ this.d_orderList = p_orderList; }
    public Integer getNumOfArmiesRemaining(){ return this.d_numOfArmiesRemaining; }
    public void setNumOfArmiesRemaining(Integer d_num){ this.d_numOfArmiesRemaining = d_num; }
    public List<String> getAllCards(){ return this.d_cardsOwnedByPlayer; }
    private Boolean validateDeployOrderArmies(int p_numOfArmiesToDeploy){
        return p_numOfArmiesToDeploy <= this.d_numOfArmiesRemaining;
    }
    public void createDeployOrder(Command l_cmd) throws InvalidCommand, InvalidState {
        String l_targetCountry;
        Integer l_noOfArmies;

        l_targetCountry = l_cmd.handleDeployArmies().get(0);
        l_noOfArmies = Integer.parseInt(l_cmd.handleDeployArmies().get(1));
        if (!validateDeployOrderArmies(l_noOfArmies)) {
            GameController.log("Player::createDeployOrder", LogLevel.BASICLOG,"Deploy order starting " +
                    "validation failed");
            GameController.log("Player::createDeployOrder", LogLevel.BASICLOG,"Num of Reinforcement" +
                        " Player has " + this.getNumOfArmiesRemaining() + " Num of armies in the order : " + l_noOfArmies);
            System.out.println("Given deploy order cant be executed as armies in deploy order exceeds player's" +
                        " unallocated armies.");
            throw new InvalidState("You don't have enough Reinforcement for deployment to " + l_targetCountry);
        } else {
            Integer l_unallocatedarmies = d_numOfArmiesRemaining - l_noOfArmies;
            this.issue_order(new Deploy(this, l_targetCountry,l_noOfArmies));
            this.setNumOfArmiesRemaining(l_unallocatedarmies);
            GameController.log("Player::createDeployOrder", LogLevel.BASICLOG,"Deploy order object" +
                    " created and ready for execution" +
                    " Reinforcement Armies : " + l_unallocatedarmies);
        }

    }
    public void createAdvanceOrder(Command l_cmd,GameState p_currentGameState) throws InvalidCommand {
            Country l_sourceCountry = p_currentGameState.getCurrentMap().getCountryByName(l_cmd.handleAdvanceArmies().get(0));
            Country l_defendingCountry = p_currentGameState.getCurrentMap().getCountryByName(l_cmd.handleAdvanceArmies().get(1));
            Integer l_numOfArmies = Integer.parseInt(l_cmd.handleAdvanceArmies().get(2));

            boolean l_preValidityCheck = (l_sourceCountry != null) && (l_defendingCountry != null) &&
                    (l_numOfArmies >= 0) && (l_sourceCountry.hasNeighbour(l_defendingCountry));
            if (l_preValidityCheck) {
                issue_order(new Advance(this, l_sourceCountry, l_defendingCountry, l_numOfArmies));
                GameController.log("Player::createAdvanceOrder", LogLevel.BASICLOG, "Advance order object" +
                        " created and ready for execution" +
                        " Advancing Armies  : " + l_numOfArmies +
                        " From : " + l_sourceCountry.getCountryName() +
                        " To : " + l_defendingCountry.getCountryName());
            } else {
                GameController.log("Player::createAdvanceOrder", LogLevel.BASICLOG, "Advance order failed");
                System.out.println("Invalid advance order provided");
            }



    }
    public void issue_order(Order d_order){
        if(this.d_orderList == null)
            d_orderList = new ArrayList<Order>();
        this.d_orderList.add(d_order);
    }
    public Order next_order(){
        if(d_orderList == null || d_orderList.isEmpty())
            return null;
        Order l_order = this.d_orderList.get(0);
        this.d_orderList.remove(0);
        return l_order;

    }
    public void assignCard(GameState p_gameState){
        int l_numOfCards = AllTheConstants.CARDS.size();
        int l_cardIndex = p_gameState.getRandomInteger(l_numOfCards,0);
        if(!d_cardAssignedForThisTurnFlag){
            this.d_cardsOwnedByPlayer.add(AllTheConstants.CARDS.get(l_cardIndex));
            GameController.log("Player::assignCard",LogLevel.BASICLOG,
                    this.d_playerName + " has been assigned " +
                    "Card : " + AllTheConstants.CARDS.get(l_cardIndex));
            setCardAssignedForThisTurnFlag(true);
        } else {
            GameController.log("Player::assignCard",LogLevel.BASICLOG,
                    this.d_playerName + " has CAPTURED but not assigned with card" +
                    " as he has already been assigned card in this turn");
        }
    }
    public void removeCard(String p_card){
        this.d_cardsOwnedByPlayer.remove(p_card);
        GameController.log("Player::removeCard",LogLevel.BASICLOG,
                this.d_playerName + " has used : " + p_card);
    }
    public void createAirliftOrderCard(Command l_cmd, GameState p_gameState) throws InvalidCommand, InvalidState {
        Country l_sourceCountry = p_gameState.getCurrentMap().getCountryByName(l_cmd.handleAirliftCommand().get(0));
        Country l_targetCountry = p_gameState.getCurrentMap().getCountryByName(l_cmd.handleAirliftCommand().get(1));
        Integer l_numOfArmies = Integer.parseInt(l_cmd.handleAirliftCommand().get(2));
        if(l_sourceCountry == null || l_targetCountry == null){
            GameController.log("Player::createAirliftOrder",LogLevel.BASICLOG," Invalid Airlift Order provided");
            throw new InvalidState("Airlift countries doesn't exist");

        } else {
            Card l_newCard = new Airlift(this, l_sourceCountry,l_targetCountry,l_numOfArmies);
            if(l_newCard.validateCommand(p_gameState)){
                GameController.log("Player::createAirliftOrder",LogLevel.BASICLOG," Airlift order object " +
                        " created and ready for execution" +
                        " Source Country : " + l_sourceCountry.getCountryName() +
                        " Target Country : " + l_targetCountry.getCountryName() +
                        " Army Count : " + l_numOfArmies);
                issue_order(l_newCard);
            }
        }
    }

    public void createBlockadeOrderCard(Command l_cmd, GameState p_gameState) throws InvalidCommand, InvalidState {
        Country l_country = p_gameState.getCurrentMap().getCountryByName(l_cmd.handleBlockadeCommand());
        if(l_country == null){
            GameController.log("Player::createBlockadeOrder",LogLevel.BASICLOG," Invalid Blockade Order" +
                    " country doesn't exist");
            throw new InvalidState("Country doesn't exist");

        } else {
            Card l_newCard = new Blockade(this, l_country);
            if(l_newCard.validateCommand(p_gameState)) {
                GameController.log("Player::createBlockadeOrder", LogLevel.BASICLOG, " Blockade order object " +
                        " created and ready for execution" +
                        " Blockade Country : " + l_country.getCountryName());
                issue_order(l_newCard);
            }
        }
    }

    public void createBombOrderCard(Command l_cmd, GameState p_gameState) throws InvalidCommand, InvalidState {
        Country l_country = p_gameState.getCurrentMap().getCountryByName(l_cmd.handleBombCommand());
        if(l_country == null){
            GameController.log("Player::createBombOrder", LogLevel.BASICLOG,"Invalid Bomb Order" +
                    "Country " + l_cmd.handleBombCommand() + " doesn't exist");
            throw new InvalidState("To be Bombed " + l_cmd.handleBombCommand() + "country doesn't exist.");
        } else {
            Card l_newCard = new Bomb(this, l_country);
            if(l_newCard.validateCommand(p_gameState)){
                GameController.log("Player::createBombOrder", LogLevel.BASICLOG, " Bomb order object " +
                        " created and ready for execution" +
                        " Bombed Country : " + l_country.getCountryName());
                issue_order(l_newCard);
            }
        }
    }

    public void createNegotiateOrderCard(Command l_cmd, GameState p_gameState) throws InvalidCommand, InvalidState {
        Player p_player1 = this;
        Player p_player2 = p_gameState.getPlayerController().getPlayerByName(l_cmd.handleNegotiateCommand());
        if(p_player2 == null){
            throw new InvalidState("Invalid Negotiate Order " +
                    "Player : " + l_cmd.handleNegotiateCommand() + " doesn't exist.");
        } else {
            Card l_newCard = new Negotiate(p_player1,p_player2);
            if(l_newCard.validateCommand(p_gameState)){
                GameController.log("Player::createNegotiateOrderCard", LogLevel.BASICLOG,
                        " Negotiate order object created and ready for execution" +
                        " Card Owner : " + p_player1.getPlayerName() +
                        " Negotiated With : " + p_player2.getPlayerName());
                issue_order(l_newCard);
            }
        }
    }

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
                GameController.log("Player::createCardOrder", LogLevel.BASICLOG,
                        "Invalid Card Provided." + l_cmd.getMainOperation());
                throw new InvalidState("Invalid Card Provided.");
            }
        }
    }


}


