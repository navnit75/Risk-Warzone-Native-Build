package org.Model.Phases;

import org.Controller.GameEngine;
import org.Model.Country;
import org.Model.GameState;
import org.Model.Player;
import org.Utils.Command;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class IssueOrderPhaseTest {

    GameEngine d_gameEngine;
    GameState d_gameState;
    Phase d_startUpPhase, d_issueOrderPhase;

    @Before
    public void setUp() throws Exception {
        d_gameEngine = new GameEngine();
        d_gameState = new GameState();
        d_startUpPhase = new StartUpPhase(d_gameEngine,d_gameState);
        d_issueOrderPhase = new IssueOrderPhase(d_gameEngine,d_gameState);
        GameEngine.setLoggerContext("testLog.txt");
    }

    @Test
    public void testPerformDeploy(){
        Command l_loadMap = new Command("loadmap europe.map");
        Command l_gamePlayer = new Command("gameplayer -add p1 -add p2");
        try{
            GameEngine d_gameEngineMock = mock(GameEngine.class);
            Phase d_newPhase = new StartUpPhase(d_gameEngineMock,d_gameState);
            d_newPhase.performLoadMap(l_loadMap,null);
            d_newPhase.performCreatePlayers(l_gamePlayer, null);
            doNothing().when(d_gameEngineMock).setIssueOrderPhase();
            d_newPhase.performAssignCountries(null);

            Player l_playerOne = d_gameState.getPlayerController().getPlayerByName("p1");
            Integer l_playerOneArmies = l_playerOne.getNumOfArmiesRemaining();
            Country l_randomCountry  = l_playerOne.getCountryCaptured().get(0);
            String cmd = "deploy " + l_randomCountry.getCountryName() + " " + l_playerOneArmies;
            Command l_deployCmd = new Command(cmd);
            d_issueOrderPhase.performDeployArmies(l_deployCmd,d_gameState.getPlayerController().getPlayerByName("p1"));

            assertEquals(l_playerOne.getOrderList().get(0).getOrder(), cmd);
        } catch(Exception ex){
            assertNull(ex);
        }
    }

    @Test
    public void testPerformAdvance(){
        Command l_loadMap = new Command("loadmap europe.map");
        Command l_gamePlayer = new Command("gameplayer -add p1 -add p2");
        try{
            GameEngine d_gameEngineMock = mock(GameEngine.class);
            Phase d_newPhase = new StartUpPhase(d_gameEngineMock,d_gameState);
            d_newPhase.performLoadMap(l_loadMap,null);
            d_newPhase.performCreatePlayers(l_gamePlayer, null);
            doNothing().when(d_gameEngineMock).setIssueOrderPhase();
            d_newPhase.performAssignCountries(null);

            Player l_playerOne = d_gameState.getPlayerController().getPlayerByName("p1");
            Player l_playerTwo = d_gameState.getPlayerController().getPlayerByName("p2");

            Integer l_playerOneArmies = l_playerOne.getNumOfArmiesRemaining();
//            Integer l_playerTwoArmies = l_playerTwo.getNumOfArmiesRemaining();

            Country l_attackingCountry  = l_playerOne.getCountryCaptured().get(0);
            Country l_defendingCountry = null;

            // find the neighbour country of attacking country which belongs to player 2
            for(Integer l_countryId : l_attackingCountry.getNeighbourCountries()){
                Country temp_Storage = d_gameState.getCurrentMap().getCountryById(l_countryId);
                if(l_playerTwo.getCountryCaptured().contains(temp_Storage)){
                    l_defendingCountry = temp_Storage;
                    break;
                }
            }



            String cmd = "advance " + l_attackingCountry.getCountryName() + " " + l_defendingCountry.getCountryName() + " " + l_playerOneArmies;
            Command l_advCommand = new Command(cmd);
            d_issueOrderPhase.performAdvanceArmies(l_advCommand,l_playerOne);

            assertEquals(l_playerOne.getOrderList().get(0).getOrder(), cmd);
        } catch(Exception ex){
            assertNull(ex);
        }
    }

    @Test
    public void testAirliftCard(){
        Command l_loadMap = new Command("loadmap europe.map");
        Command l_gamePlayer = new Command("gameplayer -add p1 -add p2");
        try{
            GameEngine d_gameEngineMock = mock(GameEngine.class);
            Phase d_newPhase = new StartUpPhase(d_gameEngineMock,d_gameState);
            d_newPhase.performLoadMap(l_loadMap,null);
            d_newPhase.performCreatePlayers(l_gamePlayer, null);
            doNothing().when(d_gameEngineMock).setIssueOrderPhase();
            d_newPhase.performAssignCountries(null);

            Player l_playerOne = d_gameState.getPlayerController().getPlayerByName("p1");
            l_playerOne.getAllCards().add("airlift");
            Integer l_playerOneArmies = l_playerOne.getNumOfArmiesRemaining();
            Country l_sourceCountry  = l_playerOne.getCountryCaptured().get(0);
            Country l_targetCountry = l_playerOne.getCountryCaptured().get(1);
            String cmd = "airlift " + l_sourceCountry.getCountryName() + " " + l_targetCountry.getCountryName() + " " + l_playerOneArmies;
            Command l_airliftCommand = new Command(cmd);
            d_issueOrderPhase.performCardHandle(l_airliftCommand,l_playerOne);
            assertEquals(l_playerOne.getOrderList().get(0).getOrder(), cmd);
        } catch(Exception ex){
            assertNull(ex);
        }
    }

    @Test
    public void testBombCard(){
        Command l_loadMap = new Command("loadmap europe.map");
        Command l_gamePlayer = new Command("gameplayer -add p1 -add p2");
        try{
            GameEngine d_gameEngineMock = mock(GameEngine.class);
            Phase d_newPhase = new StartUpPhase(d_gameEngineMock,d_gameState);
            d_newPhase.performLoadMap(l_loadMap,null);
            d_newPhase.performCreatePlayers(l_gamePlayer, null);
            doNothing().when(d_gameEngineMock).setIssueOrderPhase();
            d_newPhase.performAssignCountries(null);

            Player l_playerOne = d_gameState.getPlayerController().getPlayerByName("p1");
            l_playerOne.getAllCards().add("bomb");
            Player l_playerTwo = d_gameState.getPlayerController().getPlayerByName("p2");

            Integer l_playerOneArmies = l_playerOne.getNumOfArmiesRemaining();
            Country l_sourceCountry  = l_playerOne.getCountryCaptured().get(0);
            Country l_targetCountry  = null;

            for(Integer l_countryId : l_sourceCountry.getNeighbourCountries()){
                Country temp_Storage = d_gameState.getCurrentMap().getCountryById(l_countryId);
                if(l_playerTwo.getCountryCaptured().contains(temp_Storage)){
                    l_targetCountry = temp_Storage;
                    break;
                }
            }
            String cmd = "bomb " + l_targetCountry.getCountryName();
            Command l_airliftCommand = new Command(cmd);
            d_issueOrderPhase.performCardHandle(l_airliftCommand,l_playerOne);
            assertEquals(l_playerOne.getOrderList().get(0).getOrder(), cmd);
        } catch(Exception ex){
            assertNull(ex);
        }
    }

    @Test
    public void testNegotiateCard(){
        Command l_loadMap = new Command("loadmap europe.map");
        Command l_gamePlayer = new Command("gameplayer -add p1 -add p2");
        try{
            GameEngine d_gameEngineMock = mock(GameEngine.class);
            Phase d_newPhase = new StartUpPhase(d_gameEngineMock,d_gameState);
            d_newPhase.performLoadMap(l_loadMap,null);
            d_newPhase.performCreatePlayers(l_gamePlayer, null);
            doNothing().when(d_gameEngineMock).setIssueOrderPhase();
            d_newPhase.performAssignCountries(null);
            Player l_playerOne = d_gameState.getPlayerController().getPlayerByName("p1");
            l_playerOne.getAllCards().add("negotiate");
            String cmd = "negotiate p2";
            Command l_airliftCommand = new Command(cmd);
            d_issueOrderPhase.performCardHandle(l_airliftCommand,l_playerOne);
            assertEquals(l_playerOne.getOrderList().get(0).getOrder(), cmd);
        } catch(Exception ex){
            assertNull(ex);
        }
    }

    @Test
    public void testBlockadeCard(){
        Command l_loadMap = new Command("loadmap europe.map");
        Command l_gamePlayer = new Command("gameplayer -add p1 -add p2");
        try{
            GameEngine d_gameEngineMock = mock(GameEngine.class);
            Phase d_newPhase = new StartUpPhase(d_gameEngineMock,d_gameState);
            d_newPhase.performLoadMap(l_loadMap,null);
            d_newPhase.performCreatePlayers(l_gamePlayer, null);
            doNothing().when(d_gameEngineMock).setIssueOrderPhase();
            d_newPhase.performAssignCountries(null);
            Player l_playerOne = d_gameState.getPlayerController().getPlayerByName("p1");
            l_playerOne.getAllCards().add("blockade");
            Country l_sourceCountry  = l_playerOne.getCountryCaptured().get(0);
            String cmd = "blockade " + l_sourceCountry.getCountryName();
            Command l_blockadeCommand = new Command(cmd);
            d_issueOrderPhase.performCardHandle(l_blockadeCommand,l_playerOne);
            assertEquals(l_playerOne.getOrderList().get(0).getOrder(), cmd);
        } catch(Exception ex){
            assertNull(ex);
        }
    }

}