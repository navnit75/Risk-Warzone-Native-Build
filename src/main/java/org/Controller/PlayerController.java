package org.Controller;
import org.Constants.AllTheConstants;
import org.Model.*;
import org.Utils.LogLevel;
import org.Views.MapView;

import java.awt.font.GlyphMetrics;
import java.util.*;

/**
 * Class to keep the global operation required on the
 * Player class
 */
public class PlayerController {
    private List<Player> d_allPlayers = new ArrayList<>();
    public List<Player> getAllPlayers(){return this.d_allPlayers;}

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
    public Player getPlayerByName(String p_playerName){
        if(d_allPlayers.isEmpty()){
            return null;
        } else {
            for(Player d_player : d_allPlayers){
                if(d_player.getPlayerName().equalsIgnoreCase(p_playerName))
                    return d_player;
            }
        }
        return null;
    }

    public void addPlayer(Player p_player){
        this.d_allPlayers.add(p_player);
    }

    public void addRemovePlayers(HashMap<String,ArrayList<String>> p_command){
        List<String> l_allAddOperations = p_command.get("add");
        List<String> l_allRemoveOperations = p_command.get("remove");

        for(String p_name : l_allAddOperations){
            if(!this.checkIfPlayerAlreadyExist(p_name)){
                d_allPlayers.add(new Player(p_name));
                System.out.println("Player " + p_name + " added.");
                GameController.log("PlayerController::addRemovePlayers", LogLevel.BASICLOG,"Player " + p_name + " added.");
            }
        }

        for(String p_name : l_allRemoveOperations){
            if(this.checkIfPlayerAlreadyExist(p_name)){
                Player l_removePlayer = getPlayerByName(p_name);
                if(l_removePlayer != null) {
                    d_allPlayers.remove(l_removePlayer);
                    System.out.println("Player " + p_name + " removed.");
                    GameController.log("PlayerController::addRemovePlayers", LogLevel.BASICLOG,"Player " + p_name + " removed.");
                } else {
                    System.out.println("Player " + p_name + " doesn't exist.");
                    GameController.log("PlayerController::addRemovePlayers",LogLevel.BASICLOG,"Player " + p_name + " doesn't exist");
                }
            }
        }
    }

    public void updateContinents(Player p_player, GameState p_gameState){
        for(Continent l_continent : p_gameState.getCurrentMap().getAllContinentsList()){
            List<Country> l_countriesOwned = p_player.getCountryCaptured();
            if(new HashSet<>(l_countriesOwned).containsAll(l_continent.getCountries())){
                p_player.addContinentOwned(l_continent);
                GameController.log("PlayerController::updateContinents",LogLevel.BASICLOG,
                        p_player.getPlayerName() + "->" + l_continent.getContinentName());
            }
        }
    }

    private void assignColors(){
        if(this.d_allPlayers != null){
            int l_numOfColors = AllTheConstants.COLORS.size();
            for(int i = 0 ; i < d_allPlayers.size() ; i++){
                d_allPlayers.get(i).setColor(AllTheConstants.COLORS.get(i % l_numOfColors));
                GameController.log("PlayerController::assignColors",LogLevel.BASICLOG,
                        d_allPlayers.get(i).getPlayerName() + " is assigned color : " + d_allPlayers.get(i).getColor());
            }
        }
    }



    public void showPlayerAssignedCountry(GameState p_gameState){
        MapView l_tempView = new MapView(p_gameState);
        l_tempView.showPlayerBasedCountries();
    }

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
               GameController.log("PlayerController::assignCountries",LogLevel.BASICLOG,
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
        GameController.log("PlayerController::calculateArmiesForPlayer",LogLevel.BASICLOG,
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

    public Boolean checkForMoreOrders(){
        return d_allPlayers
                .stream()
                .anyMatch(Player::getMoreOrderFlag);
    }

    public Boolean ordersRemaining(){
        return d_allPlayers
                .stream()
                .anyMatch(l_player -> !l_player
                        .getOrderList()
                        .isEmpty()
                );
    }

    public void resetPlayerFlag(){
        for(Player l_player : d_allPlayers){
            if(!l_player.getPlayerName().equals("Neutral")) {
                l_player.setMoreOrderFlag(true);

            }
            l_player.setCardAssignedForThisTurnFlag(false);
            l_player.clearNegotiatedPlayers();
            GameController.log("PlayerController::resetPlayerFlag", LogLevel.BASICLOG,
                    l_player.getPlayerName() + " flags are reset ");
        }

    }

    public Player findPlayerByName(String p_playerName){
        return d_allPlayers
                .stream()
                .filter(l_player->l_player.getPlayerName().equals(p_playerName))
                .findFirst()
                .orElse(null);
    }

}
