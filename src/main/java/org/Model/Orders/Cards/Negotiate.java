package org.Model.Orders.Cards;

import org.Controller.GameController;
import org.Model.GameState;
import org.Model.Player;
import org.Utils.LogLevel;

public class Negotiate implements Card{
    private Player d_cardOwner;
    private Player d_anotherPlayer;

    public Negotiate(Player p_player1 , Player p_player2){
        d_cardOwner = p_player1;
        d_anotherPlayer = p_player2;
    }

    @Override
    public void execute(GameState p_gameState) {
        d_cardOwner.addNegotiatedPlayer(d_anotherPlayer);
        d_anotherPlayer.addNegotiatedPlayer(d_cardOwner);
        d_cardOwner.removeCard("negotiate");
        GameController.log("Negotiate::execute", LogLevel.BASICLOG,d_cardOwner.getPlayerName() +
                " negotiated with " + d_anotherPlayer.getPlayerName());
    }

    @Override
    public boolean valid(GameState p_gameState) {
        return true;
    }

    @Override
    public String getOrder() {
        return "negotiate " + d_anotherPlayer.getPlayerName();
    }


    public Boolean validateCommand(GameState p_gameState) {
        return d_cardOwner.getNegotiatedPlayerByName(d_anotherPlayer.getPlayerName()) == null;
    }
}
