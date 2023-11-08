package org.Model.Orders.Cards;

import org.Model.GameState;
import org.Model.Orders.Order;

/**
 * Class describes the Cards available to Players, though its a subclass of the Order
 * Its parent to all the card based classes i.e airlift,blockade, bomb, negotiate
 */
public interface Card extends Order {

    /**
     * Contains extravalidation conditions required for the card validation
     * @param p_gameState : Game state contained in one object
     * @return Boolean
     */
    public Boolean validateCommand(GameState p_gameState);
}
