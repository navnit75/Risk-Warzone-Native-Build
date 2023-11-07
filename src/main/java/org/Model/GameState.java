package org.Model;
import org.Controller.*;
import org.Model.Orders.Order;

import java.util.List;

public class GameState {
    private Map d_currentMap = new Map();
    private List<Order> d_unexecutedOrder;
    private PlayerController d_playerController = new PlayerController();
    public Map getCurrentMap(){return this.d_currentMap;}
    public void setCurrentMap(Map l_currentMap){ this.d_currentMap = l_currentMap; }
    public PlayerController getPlayerController(){return this.d_playerController; }
    public int getRandomInteger(int p_max, int p_min){
        return ((int)(Math.random()*(p_max - p_min)) + p_min);
    }

}
