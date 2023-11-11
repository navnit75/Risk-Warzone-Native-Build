package org.Controller;

import org.Constants.AllTheConstants;
import org.Model.Continent;
import org.Model.Country;
import org.Model.GameState;
import org.Model.Player;
import org.Utils.LogLevel;
import org.Views.MapView;

import java.util.*;

/**
 * Class to keep the global operation required on players to be kept at one single location
 */
public class PlayerController {
    /**
     * List of all players playing in the game
     */
    private List<Player> d_allPlayers = new ArrayList<>();

    /**
     * Function to get all the players of the game
     * @return d_allPlayers : Returns all the players of the game
     */
    public List<Player> getAllPlayers(){return this.d_allPlayers;}

    /**
     * Validity check for checking if the player exists
     * @param p_playerName : String player name of the player for checking validity
     * @return True or False based on if player exist in the game.
     */
    public boolean checkIfPlayerAlreadyExist(String p_playerName){
        if(d_allPlayers.isEmpty())
            return false;

        for(Player l_player : d_allPlayers){
            if(l_player.getPlayerName().equalsIgnoreCase(p_playerName)){
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the player object by the string value of the player name
     * @param p_playerName : String player name
     * @return Player object if the player exists or return null;
     */
    public Player getPlayerByName(String p_playerName){
        return d_allPlayers
                .stream()
                .filter(l_player->l_player.getPlayerName().equalsIgnoreCase(p_playerName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Adding a player to the game by providing the Player object
     * @param p_player : Player object to be added to the game
     */
    public void addPlayer(Player p_player){
        this.d_allPlayers.add(p_player);
    }

    /**
     * Command handling function for player adding and removing Players.
     * @param p_command : Command object created for the command.
     */
    public void addRemovePlayers(HashMap<String,ArrayList<String>> p_command){
        List<String> l_allAddOperations = p_command.get("add");
        List<String> l_allRemoveOperations = p_command.get("remove");

        for(String p_name : l_allAddOperations){
            if(!this.checkIfPlayerAlreadyExist(p_name)){
                d_allPlayers.add(new Player(p_name));
                System.out.println("Player " + p_name + " added.");
                GameEngine.log("PlayerController::addRemovePlayers", LogLevel.BASICLOG,"Player " + p_name + " added.");
            }
            else {
                GameEngine.log("PlayerController::addRemovePlayers", LogLevel.BASICLOG,"Duplicate player name provided.");
                System.out.println("Player " + p_name + " is already added. \nKindly add a different player !!!");
            }
        }

        for(String p_name : l_allRemoveOperations){
            if(this.checkIfPlayerAlreadyExist(p_name)){
                Player l_removePlayer = getPlayerByName(p_name);
                if(l_removePlayer != null) {
                    d_allPlayers.remove(l_removePlayer);
                    System.out.println("Player " + p_name + " removed.");
                    GameEngine.log("PlayerController::addRemovePlayers", LogLevel.BASICLOG,"Player " + p_name + " removed.");
                } else {
                    System.out.println("Player " + p_name + " doesn't exist.");
                    GameEngine.log("PlayerController::addRemovePlayers",LogLevel.BASICLOG,"Player " + p_name + " doesn't exist");
                }
            }
        }
    }

    /**
     * Helper function to add continents to the list of captured continent of the player , according to the countries
     * captured.
     * @param p_player : Player on whose countries this operation will take place.
     * @param p_gameState : Gamestate of the game.
     */
    public void updateContinents(Player p_player, GameState p_gameState){
        for(Continent l_continent : p_gameState.getCurrentMap().getAllContinentsList()){
            List<Country> l_countriesOwned = p_player.getCountryCaptured();
            if(new HashSet<>(l_countriesOwned).containsAll(l_continent.getCountries())){
                p_player.addContinentOwned(l_continent);
                GameEngine.log("PlayerController::updateContinents",LogLevel.BASICLOG,
                        p_player.getPlayerName() + "->" + l_continent.getContinentName());
            }
        }
    }

    /**
     * Helper function to assign colors to each of the player.
     */
    private void assignColors(){
        if(this.d_allPlayers != null){
            int l_numOfColors = AllTheConstants.COLORS.size();
            Collections.shuffle(AllTheConstants.COLORS);
            for(int i = 0 ; i < d_allPlayers.size() ; i++){
                d_allPlayers.get(i).setColor(AllTheConstants.COLORS.get(i % l_numOfColors));
                GameEngine.log("PlayerController::assignColors",LogLevel.BASICLOG,
                        d_allPlayers.get(i).getPlayerName() + " is assigned color : " + d_allPlayers.get(i).getColor());
            }
        }
    }


    /**
     * Helper function to render the assigned countries to a player
     * @param p_gameState : Current gamestate of the main running game.
     */
    public void showPlayerAssignedCountry(GameState p_gameState){
        MapView l_tempView = new MapView(p_gameState);
        l_tempView.showPlayerBasedCountries();
    }

    /**
     * Handles the assigncountries command from users.
     * And assigns random country to each of the player.
     * @param p_gameState : Current game state of the Game.
     */
    public void assignCountries(GameState p_gameState){
        List<Country> l_countryList = new ArrayList<>(p_gameState.getCurrentMap().getAllCountriesAsList());
        Collections.shuffle(l_countryList);
        int l_countriesCount = l_countryList.size();

        int l_countryIndex = 0 ;
        while(l_countryIndex < l_countriesCount){
            for(Player l_player : d_allPlayers){
                if(l_countryIndex == l_countriesCount)
                    break;
                l_player.addCountryCaptured(l_countryList.get(l_countryIndex));
                GameEngine.log("PlayerController::assignCountries",LogLevel.BASICLOG,
                        l_player.getPlayerName() + "->" + l_countryList.get(l_countryIndex).getCountryName());
                ++l_countryIndex;
            }

        }

        for(Player l_player : d_allPlayers){
            updateContinents(l_player,p_gameState);
        }

        assignColors();
        showPlayerAssignedCountry(p_gameState);

    }

    /**
     * Calculates reinforcement to be provided to  the players , after each turn.
     * @param p_player : For player whose reinforcement number has to be decided.
     * @return Integer : The number of reinforcement .
     */
    public Integer calculateArmiesForPlayer(Player p_player) {
        Integer l_armies = null != p_player.getNumOfArmiesRemaining() ? p_player.getNumOfArmiesRemaining() : 0;

        if (p_player.getCountryCaptured() != null) {
            l_armies = l_armies + Math.max(3, Math.round(p_player.getCountryCaptured().size() / 3));
        }
        if (p_player.getContinentOwned() != null) {
            int l_continentCtrlValue = 0;
            for (Continent l_continent : p_player.getContinentOwned()) {
                l_continentCtrlValue = l_continentCtrlValue + l_continent.getContinentBonusValue();
            }
            l_armies = l_armies + l_continentCtrlValue;
        }
        GameEngine.log("PlayerController::calculateArmiesForPlayer",LogLevel.BASICLOG,
                p_player.getPlayerName() + " has been assigned " + l_armies);
        return l_armies;
    }

    public void assignArmies(GameState p_gameState){
        for(Player l_player : d_allPlayers){
            if(!l_player.getPlayerName().equalsIgnoreCase("Neutral")) {
                Integer l_armies = calculateArmiesForPlayer(l_player);
                l_player.setNumOfArmiesRemaining(l_armies);
            }
        }
    }

    /**
     * Checks if the getMoreOrderFlag of any one of the player is set
     * @return True or False
     */
    public Boolean checkForMoreOrders(){
        return d_allPlayers
                .stream()
                .anyMatch(Player::getMoreOrderFlag);
    }

    /**
     * If any of the Players have unexecuted orders left for a particular turn
     * @return True or Falsef
     */
    public Boolean ordersRemaining(){
        return d_allPlayers
                .stream()
                .anyMatch(l_player -> !l_player
                        .getOrderList()
                        .isEmpty()
                );
    }

    /**
     * Use to reset the player flag after each turn.
     */
    public void resetPlayerFlag(){
        for(Player l_player : d_allPlayers){
            if(!l_player.getPlayerName().equals("Neutral")) {
                l_player.setMoreOrderFlag(true);

            }
            l_player.setCardAssignedForThisTurnFlag(false);
            l_player.clearNegotiatedPlayers();
            GameEngine.log("PlayerController::resetPlayerFlag", LogLevel.BASICLOG,
                    l_player.getPlayerName() + " flags are reset ");
        }

    }
}
