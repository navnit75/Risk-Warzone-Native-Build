package org.Model.Orders;

import org.Model.GameState;

/**
 * Order interface to handle variety of Orders which can be executed in the game.
 * In coordination with the COMMAND PATTERN design pattern.
 */
public interface Order {
    /**
     * Method that will be called by the Receiver to execute the Order.
     * @param p_gameState current state of the game.
     */
    public void execute(GameState p_gameState);

    /**
     * Validates order.
     * @return boolean true or false
     * @param p_gameState GameState Instance
     */
    public boolean valid(GameState p_gameState);

    /**
     * Returns the exact order string provided to execute order.
     * @return : String representation of the order
     */
    public String getOrder();

}
