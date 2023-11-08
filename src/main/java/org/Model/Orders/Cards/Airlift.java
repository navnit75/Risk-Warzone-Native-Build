package org.Model.Orders.Cards;

import org.Controller.GameEngine;
import org.Model.Country;
import org.Model.GameState;
import org.Model.Player;
import org.Utils.LogLevel;

/**
 * Class to handle the airlift command based order.
 */
public class Airlift implements Card {

    /**
     * Player who has the airlift card
     */
    private Player d_cardOwner;

    /**
     * Source country from where player wants to airlift armies
     */
    private Country d_sourceCountry;

    /**
     * Destination country to where the armies needs to be airlifted
     */
    private Country d_destinationCountry;

    /**
     * Num of armies to be airlifted
     */
    private Integer d_numOfAirliftArmies;

    /**
     * Parameterized constructor
     * @param p_cardOwner : Player who has the airlift card
     * @param p_sourceCountry : Source country from where player wants to airlift armies
     * @param p_destinationCountry ; Destination country to where the armies needs to be airlifted
     * @param p_numOfArmies : Num of armies to be airlifted
     */
    public Airlift(Player p_cardOwner, Country p_sourceCountry, Country p_destinationCountry, Integer p_numOfArmies){
        this.d_cardOwner = p_cardOwner;
        this.d_sourceCountry = p_sourceCountry;
        this.d_destinationCountry = p_destinationCountry;
        this.d_numOfAirliftArmies = p_numOfArmies;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(GameState p_gameState) {
        if(valid(p_gameState)){
            Integer l_newSourceArmy = d_sourceCountry.getArmies() - d_numOfAirliftArmies;
            d_sourceCountry.setArmies(l_newSourceArmy);

            Integer l_newTargetArmy = d_destinationCountry.getArmies() + d_numOfAirliftArmies;
            d_destinationCountry.setArmies(l_newTargetArmy);
            d_cardOwner.removeCard("airlift");
            GameEngine.log("Airlift::execute", LogLevel.BASICLOG,d_cardOwner.getPlayerName() +
                    ":"+d_sourceCountry.getCountryName()+"--AIRLIFT-->"+d_destinationCountry.getCountryName()+
                    "("+d_numOfAirliftArmies+")");

        } else {
            System.out.println("Invalid Airlift command.");
        }

    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public boolean valid(GameState p_gameState) {
        Country l_tempResult  = d_cardOwner.getCapturedCountryByName(d_sourceCountry.getCountryName());
        if(l_tempResult == null){
            System.out.println("Source country is not owned by player");
            return false;
        }

        l_tempResult = d_cardOwner.getCapturedCountryByName(d_destinationCountry.getCountryName());
        if(l_tempResult == null){
            System.out.println("Destination country is not owned by player");
            return false;
        }

        if(d_numOfAirliftArmies > d_sourceCountry.getArmies()){
            System.out.println("Source country doesn't have required army to airlift");
            return false;
        }
        return true;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOrder() {
        return "airlift " + d_sourceCountry.getCountryName() + " " + d_destinationCountry.getCountryName() + " " +
                d_numOfAirliftArmies;
    }

    /**
     * {@inheritDoc}
     */
    public Boolean validateCommand(GameState p_gameState) {
        return true;
    }


}
