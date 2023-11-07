package org.Model.Orders.Cards;

import org.Model.GameState;
import org.Model.Orders.Order;

public interface Card extends Order {
    public Boolean validateCommand(GameState p_gameState);
}
