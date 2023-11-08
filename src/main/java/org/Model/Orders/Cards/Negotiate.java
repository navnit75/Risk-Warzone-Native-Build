package org.Model.Orders.Cards;

import org.Controller.GameEngine;
import org.Model.GameState;
import org.Model.Player;
import org.Utils.LogLevel;

/**
 * Class to handle the Negotiate command based order.
 */
public class Negotiate implements Card{
    /**
     * Player who owns the Negotiate card
     */
    private Player d_cardOwner;

    /**
     * Player with whom the current player wants to negotiate with
     */
    private Player d_anotherPlayer;

    /**
     * Parameterized Constructor
     * @param p_player1 : Current Player object
     * @param p_player2 : Player current player wants to negotiate with
     */
    public Negotiate(Player p_player1 , Player p_player2){
        d_cardOwner = p_player1;
        d_anotherPlayer = p_player2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(GameState p_gameState) {
        d_cardOwner.addNegotiatedPlayer(d_anotherPlayer);
        d_anotherPlayer.addNegotiatedPlayer(d_cardOwner);
        d_cardOwner.removeCard("negotiate");
        GameEngine.log("Negotiate::execute", LogLevel.BASICLOG,d_cardOwner.getPlayerName() +
                " negotiated with " + d_anotherPlayer.getPlayerName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean valid(GameState p_gameState) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOrder() {
        return "negotiate " + d_anotherPlayer.getPlayerName();
    }

    /**
     * {@inheritDoc}
     */
    public Boolean validateCommand(GameState p_gameState) {
        return d_cardOwner.getNegotiatedPlayerByName(d_anotherPlayer.getPlayerName()) == null;
    }
}
