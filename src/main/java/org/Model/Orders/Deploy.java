package org.Model.Orders;

import org.Controller.GameEngine;
import org.Model.Country;
import org.Model.GameState;
import org.Model.Player;
import org.Utils.LogLevel;

/**
 * Class used to handle deploy based orders
 */
public class Deploy implements Order {

    /**
     * Source country to which armies to be deployed
     */
    private String d_countryToDeploy;

    /**
     * Num of armies to be deployed
     */
    private Integer d_numOfArmiesToDeploy;

    /**
     * Player who wants to deploy
     */
    private Player d_player;

    public Deploy(Player p_player, String p_countryToDeploy, Integer p_numOfArmies){
        this.d_countryToDeploy = p_countryToDeploy;
        this.d_player = p_player;
        this.d_numOfArmiesToDeploy = p_numOfArmies;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(GameState p_gameState) {
        if(this.valid(p_gameState)){

            Country l_country = p_gameState.getCurrentMap().getCountryByName(d_countryToDeploy);
            Integer l_newArmyCount = l_country.getArmies();

            if(l_newArmyCount == null){
                l_newArmyCount = d_numOfArmiesToDeploy;
            } else {
                l_newArmyCount += d_numOfArmiesToDeploy;
            }
            l_country.setArmies(l_newArmyCount);
        } else {
            d_player.setNumOfArmiesRemaining(d_player.getNumOfArmiesRemaining() + d_numOfArmiesToDeploy);
        }
        GameEngine.log("Deploy::execute", LogLevel.BASICLOG,"Player " + d_player.getPlayerName() +
                 " deployed " + d_numOfArmiesToDeploy + " to " + d_countryToDeploy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean valid(GameState p_gameState) {
        Country l_country = d_player.getCapturedCountryByName(d_countryToDeploy);
        return l_country != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOrder() {
        return "deploy " + d_countryToDeploy + " " + d_numOfArmiesToDeploy;
    }

}
