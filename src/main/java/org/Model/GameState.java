package org.Model;
import org.Controller.*;
import org.Model.Orders.Order;
import java.util.List;

/**
 * Common data structure of the game , contains the state of the game.
 * Contains the common functions and data structure to store the MAP , PLAYERS and PHASES etc.
 */
public class GameState {

    /**
     * Stores the current map on which players are playing
     */
    private Map d_currentMap = new Map();

    /**
     * Stores the current player controller , which contains all the player based operations
     */
    private PlayerController d_playerController = new PlayerController();

    /**
     * Getter for the current map
     * @return Map : the current map
     */
    public Map getCurrentMap(){return this.d_currentMap;}

    /**
     * Setter for the current map
     * @param l_currentMap : The map which needs to be set
     */
    public void setCurrentMap(Map l_currentMap){ this.d_currentMap = l_currentMap; }

    /**
     * Getter for te player controller.
     * @return PlayerController : Contains the player based features and data members
     */
    public PlayerController getPlayerController(){return this.d_playerController; }

    /**
     * Helper function to generate rando numbers
     * @param p_max : Maximum range
     * @param p_min : Minimum range
     * @return Integer : Number generated
     */
    public int getRandomInteger(int p_max, int p_min){
        return ((int)(Math.random()*(p_max - p_min)) + p_min);
    }

}
