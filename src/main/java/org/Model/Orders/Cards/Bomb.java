package org.Model.Orders.Cards;

import org.Controller.GameController;
import org.Model.Country;
import org.Model.GameState;
import org.Model.Player;
import org.Utils.LogLevel;

public class Bomb implements Card {
    private Player d_attacker;
    private Country d_countryToBomb;

    public Bomb(Player p_player, Country p_country){
        this.d_attacker = p_player;
        this.d_countryToBomb = p_country;
    }
    @Override
    public void execute(GameState p_gameState) {
        if(valid(p_gameState)){
            Integer l_newArmyCount = (int)Math.floor((double) Math.max(1, d_countryToBomb.getArmies()) / 2);
            d_countryToBomb.setArmies(l_newArmyCount);
            d_attacker.removeCard("bomb");
            GameController.log("Bomb::execute", LogLevel.BASICLOG,d_attacker.getPlayerName() +
                    " bombed " + d_countryToBomb.getCountryName());
        } else {
            GameController.log("Bomb::execute",LogLevel.BASICLOG,"Invalid Bomb Operation");
            System.out.println("Invalid Bomb Operation");
        }
    }

    @Override
    public boolean valid(GameState p_gameState) {
        Country l_tempCheck = d_attacker.getCapturedCountryByName(d_countryToBomb.getCountryName());
        if(l_tempCheck != null){
            System.out.println("Target country is owned by Player. Cannot bomb.");
            return false;
        }

        if(d_attacker.negotiationCheckForAttack(d_countryToBomb.getCountryName())){
            System.out.println("Cannot bomb the country , as the player has negotiated");
            return false;
        }
        return true;
    }

    @Override
    public String getOrder() {
        return "bomb " + d_countryToBomb.getCountryName();
    }

    @Override
    public Boolean validateCommand(GameState p_gameState) {
       for(Country l_country : d_attacker.getCountryCaptured()){
           if(l_country.hasNeighbour(d_countryToBomb))
               return true;
       }
       return false;
    }
}
