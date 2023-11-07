package org.Model.Orders;

import org.Controller.GameController;
import org.Model.Country;
import org.Model.GameState;
import org.Model.Player;
import org.Utils.LogLevel;

public class Deploy implements Order {

    private String d_countryToDeploy;
    private Integer d_numOfArmiesToDeploy;
    private Player d_player;

    public Deploy(Player p_player, String p_countryToDeploy, Integer p_numOfArmies){
        this.d_countryToDeploy = p_countryToDeploy;
        this.d_player = p_player;
        this.d_numOfArmiesToDeploy = p_numOfArmies;
    }

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
        GameController.log("Deploy::execute", LogLevel.BASICLOG,"Player " + d_player.getPlayerName() +
                 " deployed " + d_numOfArmiesToDeploy + " to " + d_countryToDeploy);
    }

    @Override
    public boolean valid(GameState p_gameState) {
        Country l_country = d_player.getCapturedCountryByName(d_countryToDeploy);
        return l_country != null;
    }

    @Override
    public String getOrder() {
        return "deploy " + d_countryToDeploy + " " + d_numOfArmiesToDeploy;
    }

}
