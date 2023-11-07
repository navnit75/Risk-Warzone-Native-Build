package org.Model.Orders;

import org.Model.GameState;

public interface Order {
    /**
     * Method that will be called by the Receiver to execute the Order.
     *
     * @param p_gameState current state of the game.
     */
    public void execute(GameState p_gameState);

    /**
     * Validates order.
     *
     * @return boolean true or false
     * @param p_gameState GameState Instance
     */
    public boolean valid(GameState p_gameState);

    public String getOrder();

}
