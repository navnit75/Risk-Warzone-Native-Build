package org.Model.Orders;

import org.Controller.GameController;
import org.Model.Country;
import org.Model.GameState;
import org.Model.Player;
import org.Utils.LogLevel;
import java.util.Objects;


public class Advance implements Order {
    private Player d_player;
    private Country d_attackingCountry;
    private Country d_defendingCountry;
    private Integer d_advancingArmies;

    public Advance(Player p_player, Country p_attackingCountry , Country  p_defendingCountry, Integer p_attackingArmies){
        this.d_player = p_player;
        this.d_defendingCountry = p_defendingCountry;
        this.d_attackingCountry = p_attackingCountry;
        this.d_advancingArmies = p_attackingArmies;
    }
    private Player getCountryOwner(String p_countryName, GameState p_gameState){
        for (Player p: p_gameState.getPlayerController().getAllPlayers()){
                if(p.getCountryCaptured().contains(p_gameState.getCurrentMap().getCountryByName(p_countryName))){
                    return p;
                }
            }
        return null;
    }

    @Override
    public void execute(GameState p_gameState) {
        Player l_ownerOfDefendingCountry = getCountryOwner(d_defendingCountry.getCountryName(),p_gameState);
        if(valid(p_gameState)) {
            if (l_ownerOfDefendingCountry.getPlayerName().equalsIgnoreCase(d_player.getPlayerName())) {
                 deployArmies();
            } else if (d_defendingCountry.getArmies() == 0) {
                conquerDefendingCountry(l_ownerOfDefendingCountry,p_gameState);
            } else {
                 simulateAttack(l_ownerOfDefendingCountry, p_gameState);
            }
        } else {
            GameController.log("Advance::execute",LogLevel.BASICLOG,"error in the execution of Advance Command.");
            System.out.println("Error in execution of Advance command.");
        }

    }

    private void deployArmies(){
        int l_newArmyCount = d_defendingCountry.getArmies() + d_advancingArmies;
        d_defendingCountry.setArmies(l_newArmyCount);
        d_attackingCountry.setArmies(d_attackingCountry.getArmies() - d_advancingArmies);
        GameController.log("Advance::deployArmies",LogLevel.BASICLOG,d_player.getPlayerName() +
                " deployed " + d_advancingArmies + " to " + d_defendingCountry.getCountryName() + " as the " +
                d_attackingCountry.getCountryName() + " is neighbour");
    }

    private void conquerDefendingCountry(Player p_defender, GameState p_gameState){
        d_defendingCountry.setArmies(d_advancingArmies);
        d_attackingCountry.setArmies(d_attackingCountry.getArmies() - d_advancingArmies);
        p_defender.getCountryCaptured().remove(d_defendingCountry);
        d_player.getCountryCaptured().add(d_defendingCountry);
        p_gameState.getPlayerController().updateContinents(d_player,p_gameState);
        d_player.assignCard(p_gameState);
        GameController.log("Advance::conquerDefendingCountry",LogLevel.BASICLOG,d_player.getPlayerName() +
                " captured " + d_defendingCountry.getCountryName());


    }

    public void simulateAttack(Player p_defender, GameState p_gameState){
        int l_numOfAttackerArmies = d_advancingArmies; // 10
        int l_numOfDefendingArmies = d_defendingCountry.getArmies(); //3
        int l_successfulAttacks = 0;
        int l_successfulDefense = 0;
        for(int i = 0 ; i < l_numOfAttackerArmies; i++){
            int l_randomNum = (int)(Math.random() * 100);
            if(l_randomNum <= 60)
                l_successfulAttacks++;
        }
        for(int i = 0 ; i < l_numOfDefendingArmies; i++){
            int l_randomNum = (int)(Math.random() * 100);
            if(l_randomNum <= 70)
                l_successfulDefense++;
        }
        int l_updatedDefenderArmy = l_successfulAttacks >= l_numOfDefendingArmies ? 0 :
                l_numOfDefendingArmies - l_successfulAttacks;
        int l_updatedAttackerArmy = l_successfulDefense >= l_numOfAttackerArmies ? 0 :
                l_numOfAttackerArmies - l_successfulDefense;
        if(l_updatedDefenderArmy == 0 && l_updatedAttackerArmy != 0){
            p_defender.getCountryCaptured().remove(d_defendingCountry);
            d_defendingCountry.setArmies(l_updatedAttackerArmy);
            d_attackingCountry.setArmies(d_attackingCountry.getArmies() - d_advancingArmies);
            d_player.getCountryCaptured().add(d_defendingCountry);
            d_player.assignCard(p_gameState);
            GameController.log("Advance::simulateAttack",LogLevel.BASICLOG,d_player.getPlayerName() +
                    " captured " + d_defendingCountry.getCountryName());
            GameController.log("Advance::simulateAttack",LogLevel.BASICLOG,"Defending Country Army : " +
                    d_defendingCountry.getArmies());

        } else {

            d_defendingCountry.setArmies(l_updatedDefenderArmy);
            d_attackingCountry.setArmies(d_attackingCountry.getArmies() - d_advancingArmies + l_updatedAttackerArmy);
            GameController.log("Advance::simulateAttack",LogLevel.BASICLOG,"Attack Unsuccessful");
            GameController.log("Advance::simulateAttack", LogLevel.BASICLOG,d_attackingCountry.getCountryName()
                    + " --ADV--> " + d_defendingCountry.getCountryName());
            GameController.log("Advance::simulateAttack", LogLevel.BASICLOG,"Updated Defender Army : "
                    + l_updatedDefenderArmy);
            GameController.log("Advance::simulateAttack", LogLevel.BASICLOG,"Updated Attacker Army : "
                    + l_numOfAttackerArmies);
        }
        p_gameState.getPlayerController().updateContinents(d_player,p_gameState);

    }



    @Override
    public boolean valid(GameState p_gameState) {
        Country l_firstCheck = d_player.getCapturedCountryByName(d_attackingCountry.getCountryName());
        if(l_firstCheck == null) {
            System.out.println("Source country is not owned by player. ");
            return false;
        }
        if(d_advancingArmies > l_firstCheck.getArmies()){
            System.out.println("Source country doesn't have required army to attack.");
            return false;
        }
        if((l_firstCheck.getArmies() - d_advancingArmies) < 1){
            System.out.println("All armies cannot be used for attacking, at least one should remain for guarding the " +
                    "territory");
            return false;
        }
        if(d_player.negotiationCheckForAttack(d_defendingCountry.getCountryName())){
            System.out.println("Cannot attack the country , as the player has negotiated");
            return false;
        }
        return true;
    }

    @Override
    public String getOrder() {
        return "advance " + d_attackingCountry.getCountryName() + " " + d_defendingCountry.getCountryName() + " " +
                d_advancingArmies;
    }


}
