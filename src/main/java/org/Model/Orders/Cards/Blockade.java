package org.Model.Orders.Cards;

import org.Controller.GameEngine;
import org.Model.Country;
import org.Model.GameState;
import org.Model.Player;
import org.Utils.LogLevel;

/**
 * Class to handle the blockade command based order.
 */
public class Blockade implements Card {

    /**
     * Player who has issued this order
     */
    private Player d_cardOwner;

    /**
     * Country Player wants to put blockade on
     */
    Country d_country;

    /**
     * Parameterized constructor
     * @param p_player : Player object
     * @param p_country : Country object
     */
    public Blockade(Player p_player, Country p_country){
        this.d_cardOwner = p_player;
        this.d_country = p_country;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(GameState p_gameState) {
        if(valid(p_gameState)){
            Integer l_newArmy = Math.max(1,d_country.getArmies()) * 3;
            d_country.setArmies(l_newArmy);
            d_cardOwner.getCountryCaptured().remove(d_country);
            Player l_neutral = p_gameState.getPlayerController().getPlayerByName("Neutral");
            l_neutral.addCountryCaptured(d_country);
            d_cardOwner.removeCard("blockade");
            GameEngine.log("Blockade::execute", LogLevel.BASICLOG,d_cardOwner.getPlayerName() +
                    " blockades " + d_country.getCountryName());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean valid(GameState p_gameState) {
        Country l_tempCheck = d_cardOwner.getCapturedCountryByName(d_country.getCountryName());
        if(l_tempCheck == null){
            System.out.println("Country is not owned by the Player.");
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOrder() {
        return "blockade " + d_country.getCountryName();
    }

    /**
     * {@inheritDoc}
     */
    public Boolean validateCommand(GameState p_gameState) {
        Country l_tempCheck = p_gameState.getCurrentMap().getCountryByName(d_country.getCountryName());
        if(l_tempCheck == null){
            System.out.println("Invalid Target Country");
            return false;
        }
        return true;
    }
}
