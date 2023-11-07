package org.Model.Phases;

import org.Controller.GameController;
import org.Model.Country;
import org.Model.GameState;
import org.Model.Player;
import org.Utils.Command;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.Or;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

public class OrderExecutionPhaseTest {
    GameController d_gameController;
    GameState d_gameState;
    Phase d_startUpPhase, d_issueOrderPhase;
    OrderExecutionPhase d_orderExecutionPhase;

    @Before
    public void setup(){
        d_gameController = new GameController();
        d_gameState = new GameState();
        d_startUpPhase = new StartUpPhase(d_gameController,d_gameState);
        d_issueOrderPhase = new IssueOrderPhase(d_gameController,d_gameState);
        d_orderExecutionPhase = new OrderExecutionPhase(d_gameController,d_gameState);
        GameController.setLoggerContext("testLog.txt");
    }

    @Test
    public void performDeployCommand(){
        Command l_loadMap = new Command("loadmap europe.map");
        Command l_gamePlayer = new Command("gameplayer -add p1 -add p2");
        try{
            GameController d_gameControllerMock = mock(GameController.class);
            Phase d_newPhase = new StartUpPhase(d_gameControllerMock,d_gameState);
            d_newPhase.performLoadMap(l_loadMap,null);
            d_newPhase.performCreatePlayers(l_gamePlayer, null);
            doNothing().when(d_gameControllerMock).setIssueOrderPhase();
            d_newPhase.performAssignCountries(null);

            Player l_playerOne = d_gameState.getPlayerController().getPlayerByName("p1");
            Player l_playerTwo = d_gameState.getPlayerController().getPlayerByName("p2");


            Integer l_playerOneArmies = l_playerOne.getNumOfArmiesRemaining();
            Integer l_playerTwoArmies = l_playerTwo.getNumOfArmiesRemaining();

            Country l_randomCountry  = l_playerOne.getCountryCaptured().get(0);
            Country l_otherCountry   = l_playerTwo.getCountryCaptured().get(0);

            String cmdOne = "deploy " + l_randomCountry.getCountryName() + " " + l_playerOneArmies;
            String cmdTwo = "deploy " + l_otherCountry.getCountryName() + " " + l_playerTwoArmies;

            Command l_deployCmd = new Command(cmdOne);
            Command l_deployCmdTwo = new Command(cmdTwo);

            d_issueOrderPhase.performDeployArmies(l_deployCmd,l_playerOne);
            d_issueOrderPhase.performDeployArmies(l_deployCmdTwo,l_playerTwo);

            d_orderExecutionPhase.executeOrders();

            assertEquals(l_randomCountry.getArmies(), l_playerOneArmies);
            assertEquals(l_randomCountry.getArmies(), l_playerTwoArmies);


        } catch(Exception ex){
            assertNull(ex);
        }
    }

    @Test
    public void performDeployCommandInvalidationTest(){
        Command l_loadMap = new Command("loadmap europe.map");
        Command l_gamePlayer = new Command("gameplayer -add p1 -add p2");
        try{
            GameController d_gameControllerMock = mock(GameController.class);
            Phase d_newPhase = new StartUpPhase(d_gameControllerMock,d_gameState);
            d_newPhase.performLoadMap(l_loadMap,null);
            d_newPhase.performCreatePlayers(l_gamePlayer, null);
            doNothing().when(d_gameControllerMock).setIssueOrderPhase();
            d_newPhase.performAssignCountries(null);

            Player l_playerOne = d_gameState.getPlayerController().getPlayerByName("p1");
            Player l_playerTwo = d_gameState.getPlayerController().getPlayerByName("p2");


            Integer l_playerOneArmies = l_playerOne.getNumOfArmiesRemaining();
            Integer l_playerTwoArmies = l_playerTwo.getNumOfArmiesRemaining();

            Country l_randomCountry  = l_playerOne.getCountryCaptured().get(0);
            Country l_otherCountry   = l_playerTwo.getCountryCaptured().get(0);

            String cmdOne = "deploy " + l_randomCountry.getCountryName() + " " + l_playerOneArmies + 1;
            String cmdTwo = "deploy " + l_otherCountry.getCountryName() + " " + l_playerTwoArmies;

            Command l_deployCmd = new Command(cmdOne);
            Command l_deployCmdTwo = new Command(cmdTwo);

            d_issueOrderPhase.performDeployArmies(l_deployCmd,l_playerOne);
            d_issueOrderPhase.performDeployArmies(l_deployCmdTwo,l_playerTwo);

            d_orderExecutionPhase.executeOrders();

        } catch(Exception ex){
            assertTrue(ex.getMessage().startsWith("You don't have enough Reinforcement for deployment"));
        }
    }

    @Test
    public void performAdvanceCommand(){
        Command l_loadMap = new Command("loadmap europe.map");
        Command l_gamePlayer = new Command("gameplayer -add p1 -add p2");
        try{
            GameController d_gameControllerMock = mock(GameController.class);
            Phase d_newPhase = new StartUpPhase(d_gameControllerMock,d_gameState);
            d_newPhase.performLoadMap(l_loadMap,null);
            d_newPhase.performCreatePlayers(l_gamePlayer, null);
            doNothing().when(d_gameControllerMock).setIssueOrderPhase();
            d_newPhase.performAssignCountries(null);

            Player l_playerOne = d_gameState.getPlayerController().getPlayerByName("p1");
            Player l_playerTwo = d_gameState.getPlayerController().getPlayerByName("p2");


            Integer l_playerOneArmies = l_playerOne.getNumOfArmiesRemaining();
            Integer l_playerTwoArmies = l_playerTwo.getNumOfArmiesRemaining();

            Country l_randomCountry  = l_playerOne.getCountryCaptured().get(0);
            Country l_otherCountry   = null;

            for(Integer l_countryId : l_randomCountry.getNeighbourCountries()){
                Country l_neighbourCountry = d_gameState.getCurrentMap().getCountryById(l_countryId);
                if(l_playerTwo.getCountryCaptured().contains(l_neighbourCountry)){
                    l_otherCountry = l_neighbourCountry;
                }
            }

            String cmdOne = "deploy " + l_randomCountry.getCountryName() + " " + l_playerOneArmies;
//            String cmdTwo = "deploy " + l_otherCountry.getCountryName() + " " + (l_playerTwoArmies - 2);
            String cmdTwo = "advance " + l_randomCountry.getCountryName() + " " + l_otherCountry.getCountryName() + " " + (l_playerOneArmies - 1);

            Command l_deployCmd = new Command(cmdOne);
            Command l_advanceCmd = new Command(cmdTwo);
//            Command l_deployCmdTwo = new Command(cmdTwo);

            d_issueOrderPhase.performDeployArmies(l_deployCmd,l_playerOne);
            d_issueOrderPhase.performAdvanceArmies(l_advanceCmd,l_playerOne);

            d_orderExecutionPhase.executeOrders();
            assertTrue(l_playerOneArmies - 1 == l_otherCountry.getArmies());
        } catch(Exception ex){
            assertNull(ex);
        }
    }

    @Test
    public void performAdvanceCommandInvalid(){
        Command l_loadMap = new Command("loadmap europe.map");
        Command l_gamePlayer = new Command("gameplayer -add p1 -add p2");
        try{
            GameController d_gameControllerMock = mock(GameController.class);
            Phase d_newPhase = new StartUpPhase(d_gameControllerMock,d_gameState);
            d_newPhase.performLoadMap(l_loadMap,null);
            d_newPhase.performCreatePlayers(l_gamePlayer, null);
            doNothing().when(d_gameControllerMock).setIssueOrderPhase();
            d_newPhase.performAssignCountries(null);

            Player l_playerOne = d_gameState.getPlayerController().getPlayerByName("p1");
            Player l_playerTwo = d_gameState.getPlayerController().getPlayerByName("p2");


            Integer l_playerOneArmies = l_playerOne.getNumOfArmiesRemaining();
            Integer l_playerTwoArmies = l_playerTwo.getNumOfArmiesRemaining();

            Country l_randomCountry  = l_playerOne.getCountryCaptured().get(0);
            Country l_otherCountry   = null;

            for(Integer l_countryId : l_randomCountry.getNeighbourCountries()){
                Country l_neighbourCountry = d_gameState.getCurrentMap().getCountryById(l_countryId);
                if(l_playerTwo.getCountryCaptured().contains(l_neighbourCountry)){
                    l_otherCountry = l_neighbourCountry;
                }
            }

            String cmdOne = "deploy " + l_randomCountry.getCountryName() + " " + l_playerOneArmies;
//            String cmdTwo = "deploy " + l_otherCountry.getCountryName() + " " + (l_playerTwoArmies - 2);
            String cmdTwo = "advance " + l_randomCountry.getCountryName() + " " + l_otherCountry.getCountryName() + " " + (l_playerOneArmies);

            Command l_deployCmd = new Command(cmdOne);
            Command l_advanceCmd = new Command(cmdTwo);
//            Command l_deployCmdTwo = new Command(cmdTwo);

            d_issueOrderPhase.performDeployArmies(l_deployCmd,l_playerOne);
            d_issueOrderPhase.performAdvanceArmies(l_advanceCmd,l_playerOne);

            d_orderExecutionPhase.executeOrders();
            assertFalse(l_playerOneArmies == l_otherCountry.getArmies());
        } catch(Exception ex){
            assertNull(ex);
        }
    }
    @Test
    public void performAdvanceCommandInvalidTwo(){
        Command l_loadMap = new Command("loadmap europe.map");
        Command l_gamePlayer = new Command("gameplayer -add p1 -add p2");
        try{
            GameController d_gameControllerMock = mock(GameController.class);
            Phase d_newPhase = new StartUpPhase(d_gameControllerMock,d_gameState);
            d_newPhase.performLoadMap(l_loadMap,null);
            d_newPhase.performCreatePlayers(l_gamePlayer, null);
            doNothing().when(d_gameControllerMock).setIssueOrderPhase();
            d_newPhase.performAssignCountries(null);

            Player l_playerOne = d_gameState.getPlayerController().getPlayerByName("p1");
            Integer l_playerOneArmies = l_playerOne.getNumOfArmiesRemaining();

            Country l_randomCountry  = l_playerOne.getCountryCaptured().get(0);
            Country l_otherCountry   = d_gameState.getCurrentMap().getCountryById(l_randomCountry.getNeighbourCountries().get(0));


            String cmdOne = "deploy " + l_randomCountry.getCountryName() + " " + l_playerOneArmies;
            String cmdTwo = "advance " + l_randomCountry.getCountryName() + " " + l_otherCountry.getCountryName() + " " + (l_playerOneArmies - 1);

            Command l_deployCmd = new Command(cmdOne);
            Command l_advanceCmd = new Command(cmdTwo);

            d_issueOrderPhase.performDeployArmies(l_deployCmd,l_playerOne);
            d_issueOrderPhase.performAdvanceArmies(l_advanceCmd,l_playerOne);

            d_orderExecutionPhase.executeOrders();
            assertTrue(l_playerOneArmies -1  == l_otherCountry.getArmies());
        } catch(Exception ex){
            assertNull(ex);
        }
    }

    @Test
    public void performAdvanceCommandInvalidThree(){
        Command l_loadMap = new Command("loadmap europe.map");
        Command l_gamePlayer = new Command("gameplayer -add p1 -add p2");
        try{
            GameController d_gameControllerMock = mock(GameController.class);
            Phase d_newPhase = new StartUpPhase(d_gameControllerMock,d_gameState);
            d_newPhase.performLoadMap(l_loadMap,null);
            d_newPhase.performCreatePlayers(l_gamePlayer, null);
            doNothing().when(d_gameControllerMock).setIssueOrderPhase();
            d_newPhase.performAssignCountries(null);

            Player l_playerOne = d_gameState.getPlayerController().getPlayerByName("p1");
            Integer l_playerOneArmies = l_playerOne.getNumOfArmiesRemaining();

            Country l_randomCountry  = l_playerOne.getCountryCaptured().get(0);
            Country l_otherCountry   = d_gameState.getCurrentMap().getCountryById(l_randomCountry.getNeighbourCountries().get(0));


            String cmdOne = "deploy " + l_randomCountry.getCountryName() + " " + l_playerOneArmies;
            String cmdTwo = "advance " + l_randomCountry.getCountryName() + " " + l_otherCountry.getCountryName() + " " + (l_playerOneArmies);

            Command l_deployCmd = new Command(cmdOne);
            Command l_advanceCmd = new Command(cmdTwo);

            d_issueOrderPhase.performDeployArmies(l_deployCmd,l_playerOne);
            d_issueOrderPhase.performAdvanceArmies(l_advanceCmd,l_playerOne);

            d_orderExecutionPhase.executeOrders();
            assertFalse(l_playerOneArmies  == l_otherCountry.getArmies());
        } catch(Exception ex){
            assertNull(ex);
        }
    }

    @Test
    public void performAdvanceCommandInvalidFour(){
        Command l_loadMap = new Command("loadmap europe.map");
        Command l_gamePlayer = new Command("gameplayer -add p1 -add p2");
        try{
            GameController d_gameControllerMock = mock(GameController.class);
            Phase d_newPhase = new StartUpPhase(d_gameControllerMock,d_gameState);
            d_newPhase.performLoadMap(l_loadMap,null);
            d_newPhase.performCreatePlayers(l_gamePlayer, null);
            doNothing().when(d_gameControllerMock).setIssueOrderPhase();
            d_newPhase.performAssignCountries(null);

            Player l_playerOne = d_gameState.getPlayerController().getPlayerByName("p1");
            Player l_playerTwo = d_gameState.getPlayerController().getPlayerByName("p2");


            Integer l_playerOneArmies = l_playerOne.getNumOfArmiesRemaining();
            Integer l_playerTwoArmies = l_playerTwo.getNumOfArmiesRemaining();

            Country l_randomCountry  = l_playerOne.getCountryCaptured().get(0);
            Country l_otherCountry   = null;

            for(Integer l_countryId : l_randomCountry.getNeighbourCountries()){
                Country l_neighbourCountry = d_gameState.getCurrentMap().getCountryById(l_countryId);
                if(l_playerTwo.getCountryCaptured().contains(l_neighbourCountry)){
                    l_otherCountry = l_neighbourCountry;
                }
            }

            String cmdOne = "deploy " + l_randomCountry.getCountryName() + " " + 10;
            l_playerOne.setNumOfArmiesRemaining(10);

            String cmdTwo = "deploy " + l_otherCountry.getCountryName() + " " + 1;
            String cmdThree = "advance " + l_randomCountry.getCountryName() + " " + l_otherCountry.getCountryName() + " " + 9;

            Command l_deployCmd = new Command(cmdOne);
            Command l_deployCmdTwo = new Command(cmdTwo);
            Command l_advanceCmd = new Command(cmdThree);

            d_issueOrderPhase.performDeployArmies(l_deployCmd,l_playerOne);
            d_issueOrderPhase.performDeployArmies(l_deployCmdTwo,l_playerTwo);
            d_issueOrderPhase.performAdvanceArmies(l_advanceCmd,l_playerOne);

            d_orderExecutionPhase.executeOrders();
            assertTrue(1 == l_randomCountry.getArmies());
            assertTrue(l_otherCountry.getArmies() > 1);
            assertTrue(l_playerOne.getAllCards().size() == 1);

        } catch(Exception ex){
            assertNull(ex);
        }
    }

    @Test
    public void performAdvanceCommandInvalidFive(){
        Command l_loadMap = new Command("loadmap europe.map");
        Command l_gamePlayer = new Command("gameplayer -add p1 -add p2");
        try{
            GameController d_gameControllerMock = mock(GameController.class);
            Phase d_newPhase = new StartUpPhase(d_gameControllerMock,d_gameState);
            d_newPhase.performLoadMap(l_loadMap,null);
            d_newPhase.performCreatePlayers(l_gamePlayer, null);
            doNothing().when(d_gameControllerMock).setIssueOrderPhase();
            d_newPhase.performAssignCountries(null);

            Player l_playerOne = d_gameState.getPlayerController().getPlayerByName("p1");
            Player l_playerTwo = d_gameState.getPlayerController().getPlayerByName("p2");


            Integer l_playerOneArmies = l_playerOne.getNumOfArmiesRemaining();
            Integer l_playerTwoArmies = l_playerTwo.getNumOfArmiesRemaining();

            Country l_randomCountry  = l_playerOne.getCountryCaptured().get(0);
            Country l_otherCountry   = null;

            for(Integer l_countryId : l_randomCountry.getNeighbourCountries()){
                Country l_neighbourCountry = d_gameState.getCurrentMap().getCountryById(l_countryId);
                if(l_playerTwo.getCountryCaptured().contains(l_neighbourCountry)){
                    l_otherCountry = l_neighbourCountry;
                }
            }

            String cmdOne = "deploy " + l_randomCountry.getCountryName() + " " + l_playerOneArmies;
            l_playerTwo.setNumOfArmiesRemaining(10);

            String cmdTwo = "deploy " + l_otherCountry.getCountryName() + " " + 10;
            String cmdThree = "advance " + l_randomCountry.getCountryName() + " " + l_otherCountry.getCountryName() + " " + (l_playerOneArmies - 1);

            Command l_deployCmd = new Command(cmdOne);
            Command l_deployCmdTwo = new Command(cmdTwo);
            Command l_advanceCmd = new Command(cmdThree);

            d_issueOrderPhase.performDeployArmies(l_deployCmd,l_playerOne);
            d_issueOrderPhase.performDeployArmies(l_deployCmdTwo,l_playerTwo);
            d_issueOrderPhase.performAdvanceArmies(l_advanceCmd,l_playerOne);

            d_orderExecutionPhase.executeOrders();
            assertTrue(1 == l_randomCountry.getArmies());
            assertTrue(l_otherCountry.getArmies() > 1);

        } catch(Exception ex){
            assertNull(ex);
        }
    }

    @Test
    public void performAirliftCommand(){
        Command l_loadMap = new Command("loadmap europe.map");
        Command l_gamePlayer = new Command("gameplayer -add p1 -add p2");
        try{
            GameController d_gameControllerMock = mock(GameController.class);
            Phase d_newPhase = new StartUpPhase(d_gameControllerMock,d_gameState);
            d_newPhase.performLoadMap(l_loadMap,null);
            d_newPhase.performCreatePlayers(l_gamePlayer, null);
            doNothing().when(d_gameControllerMock).setIssueOrderPhase();
            d_newPhase.performAssignCountries(null);

            Player l_playerOne = d_gameState.getPlayerController().getPlayerByName("p1");
//            Player l_playerTwo = d_gameState.getPlayerController().getPlayerByName("p2");


            Integer l_playerOneArmies = l_playerOne.getNumOfArmiesRemaining();
//            Integer l_playerTwoArmies = l_playerTwo.getNumOfArmiesRemaining();

            Country l_randomCountry  = l_playerOne.getCountryCaptured().get(0);
            Country l_otherCountry   = null;

            for(Country l_country : l_playerOne.getCountryCaptured()){
                if(!l_country.equals(l_randomCountry) && !l_randomCountry.getNeighbourCountries().contains(l_country.getCountryId())){
                    l_otherCountry = l_country;
                }
            }

            l_playerOne.setNumOfArmiesRemaining(10);
            l_playerOne.getAllCards().add("airlift");
            String cmdOne = "deploy " + l_randomCountry.getCountryName() + " " + 10;

//            String cmdTwo = "deploy " + l_otherCountry.getCountryName() + " " + 10;
            String cmdTwo = "airlift " + l_randomCountry.getCountryName() + " " + l_otherCountry.getCountryName() + " " + 10;

            Command l_deployCmd = new Command(cmdOne);
            Command l_deployCmdTwo = new Command(cmdTwo);
//            Command l_advanceCmd = new Command(cmdThree);

            d_issueOrderPhase.performDeployArmies(l_deployCmd,l_playerOne);
            d_issueOrderPhase.performCardHandle(l_deployCmdTwo,l_playerOne);
//            d_issueOrderPhase.performAdvanceArmies(l_advanceCmd,l_playerOne);

            d_orderExecutionPhase.executeOrders();
            assertTrue(l_otherCountry.getArmies() == 10);

        } catch(Exception ex){
            assertNull(ex);
        }
    }

    @Test
    public void performBlockadeCard(){
        Command l_loadMap = new Command("loadmap europe.map");
        Command l_gamePlayer = new Command("gameplayer -add p1 -add p2");
        try{
            GameController d_gameControllerMock = mock(GameController.class);
            Phase d_newPhase = new StartUpPhase(d_gameControllerMock,d_gameState);
            d_newPhase.performLoadMap(l_loadMap,null);
            d_newPhase.performCreatePlayers(l_gamePlayer, null);
            doNothing().when(d_gameControllerMock).setIssueOrderPhase();
            d_newPhase.performAssignCountries(null);

            Player l_playerOne = d_gameState.getPlayerController().getPlayerByName("p1");
//            Player l_playerTwo = d_gameState.getPlayerController().getPlayerByName("p2");
//            Player l_neutral = d_gameState.getPlayerController().getPlayerByName("Neutral");



            Integer l_playerOneArmies = l_playerOne.getNumOfArmiesRemaining();
//            Integer l_playerTwoArmies = l_playerTwo.getNumOfArmiesRemaining();

            Country l_randomCountry  = l_playerOne.getCountryCaptured().get(0);
            String cmdOne = "deploy " + l_randomCountry.getCountryName() + " " + l_playerOneArmies;
            String cmdTwo = "blockade " + l_randomCountry.getCountryName();

            Command l_deployCmd = new Command(cmdOne);
            Command l_blockadeCmd = new Command(cmdTwo);
            l_playerOne.getAllCards().add("blockade");
            d_issueOrderPhase.performDeployArmies(l_deployCmd,l_playerOne);
            d_issueOrderPhase.performCardHandle(l_blockadeCmd,l_playerOne);

            d_orderExecutionPhase.executeOrders();
            Player l_neutral = d_gameState.getPlayerController().getPlayerByName("Neutral");
            assertEquals(l_neutral.getCountryCaptured().get(0),l_randomCountry);

        } catch(Exception ex){
            assertNull(ex);
        }
    }

    @Test
    public void performBombCard(){
        Command l_loadMap = new Command("loadmap europe.map");
        Command l_gamePlayer = new Command("gameplayer -add p1 -add p2");
        try{
            GameController d_gameControllerMock = mock(GameController.class);
            Phase d_newPhase = new StartUpPhase(d_gameControllerMock,d_gameState);
            d_newPhase.performLoadMap(l_loadMap,null);
            d_newPhase.performCreatePlayers(l_gamePlayer, null);
            doNothing().when(d_gameControllerMock).setIssueOrderPhase();
            d_newPhase.performAssignCountries(null);

            Player l_playerOne = d_gameState.getPlayerController().getPlayerByName("p1");
            Player l_playerTwo = d_gameState.getPlayerController().getPlayerByName("p2");


            Integer l_playerOneArmies = l_playerOne.getNumOfArmiesRemaining();
            Integer l_playerTwoArmies = l_playerTwo.getNumOfArmiesRemaining();

            Country l_randomCountry  = l_playerOne.getCountryCaptured().get(0);
            Country l_otherCountry   = null;

            for(Integer l_countryId : l_randomCountry.getNeighbourCountries()){
                Country l_neighbourCountry = d_gameState.getCurrentMap().getCountryById(l_countryId);
                if(l_playerTwo.getCountryCaptured().contains(l_neighbourCountry)){
                    l_otherCountry = l_neighbourCountry;
                }
            }

            String cmdOne = "deploy " + l_randomCountry.getCountryName() + " " + l_playerOneArmies;
            l_playerTwo.setNumOfArmiesRemaining(10);

            String cmdTwo = "deploy " + l_otherCountry.getCountryName() + " " + 10;
            String cmdThree = "bomb " + l_otherCountry.getCountryName();

            Command l_deployCmd = new Command(cmdOne);
            Command l_deployCmdTwo = new Command(cmdTwo);
            Command l_advanceCmd = new Command(cmdThree);
            l_playerOne.getAllCards().add("bomb");
            d_issueOrderPhase.performDeployArmies(l_deployCmd,l_playerOne);
            d_issueOrderPhase.performDeployArmies(l_deployCmdTwo,l_playerTwo);
            d_issueOrderPhase.performCardHandle(l_advanceCmd,l_playerOne);

            d_orderExecutionPhase.executeOrders();
            assertTrue(5 == l_otherCountry.getArmies());

        } catch(Exception ex){
            assertNull(ex);
        }
    }

    @Test
    public void performNegotiateCard(){
        Command l_loadMap = new Command("loadmap europe.map");
        Command l_gamePlayer = new Command("gameplayer -add p1 -add p2");

        try{
            GameController d_gameControllerMock = mock(GameController.class);
            Phase d_newPhase = new StartUpPhase(d_gameControllerMock,d_gameState);
            d_newPhase.performLoadMap(l_loadMap,null);
            d_newPhase.performCreatePlayers(l_gamePlayer, null);
            doNothing().when(d_gameControllerMock).setIssueOrderPhase();
            d_newPhase.performAssignCountries(null);

            Player l_playerOne = d_gameState.getPlayerController().getPlayerByName("p1");
            Player l_playerTwo = d_gameState.getPlayerController().getPlayerByName("p2");



            Integer l_playerOneArmies = l_playerOne.getNumOfArmiesRemaining();
            Integer l_playerTwoArmies = l_playerTwo.getNumOfArmiesRemaining();

            Country l_randomCountry  = l_playerOne.getCountryCaptured().get(0);
            Country l_otherCountry   = null;

            for(Integer l_countryId : l_randomCountry.getNeighbourCountries()){
                Country l_neighbourCountry = d_gameState.getCurrentMap().getCountryById(l_countryId);
                if(l_playerTwo.getCountryCaptured().contains(l_neighbourCountry)){
                    l_otherCountry = l_neighbourCountry;
                }
            }

            String cmdOne = "deploy " + l_randomCountry.getCountryName() + " " + l_playerOneArmies;
            String cmdTwo = "deploy " + l_otherCountry.getCountryName() + " " + l_playerTwoArmies;
//            String cmdAdvance = "advance " + l_randomCountry.getCountryName() + " " + l_otherCountry.getCountryName() + " " + (l_playerOneArmies - 1);
            String cmdThree = "negotiate " + l_playerTwo.getPlayerName();

            Command l_deployCmd = new Command(cmdOne);
            Command l_deployCmdTwo = new Command(cmdTwo);
            Command l_negotiateCmd = new Command(cmdThree);
//            Command l_advanceCmd = new Command(cmdAdvance);

            l_playerOne.getAllCards().add("negotiate");
            d_issueOrderPhase.performDeployArmies(l_deployCmd,l_playerOne);
            d_issueOrderPhase.performDeployArmies(l_deployCmdTwo,l_playerTwo);
            d_issueOrderPhase.performCardHandle(l_negotiateCmd,l_playerOne);
//            d_issueOrderPhase.performAdvanceArmies(l_advanceCmd,l_playerOne);
            d_orderExecutionPhase.executeOrders();
            assertEquals(l_playerOne.getAllNegotiatedPlayers().get(0), l_playerTwo);


        } catch(Exception ex){
            assertNull(ex);
        }
    }

    @Test
    public void performNegotiateCardOne(){
        Command l_loadMap = new Command("loadmap europe.map");
        Command l_gamePlayer = new Command("gameplayer -add p1 -add p2");

        try{
            GameController d_gameControllerMock = mock(GameController.class);
            Phase d_newPhase = new StartUpPhase(d_gameControllerMock,d_gameState);
            d_newPhase.performLoadMap(l_loadMap,null);
            d_newPhase.performCreatePlayers(l_gamePlayer, null);
            doNothing().when(d_gameControllerMock).setIssueOrderPhase();
            d_newPhase.performAssignCountries(null);

            Player l_playerOne = d_gameState.getPlayerController().getPlayerByName("p1");
            Player l_playerTwo = d_gameState.getPlayerController().getPlayerByName("p2");



            Integer l_playerOneArmies = l_playerOne.getNumOfArmiesRemaining();
            Integer l_playerTwoArmies = l_playerTwo.getNumOfArmiesRemaining();

            Country l_randomCountry  = l_playerOne.getCountryCaptured().get(0);
            Country l_otherCountry   = null;

            for(Integer l_countryId : l_randomCountry.getNeighbourCountries()){
                Country l_neighbourCountry = d_gameState.getCurrentMap().getCountryById(l_countryId);
                if(l_playerTwo.getCountryCaptured().contains(l_neighbourCountry)){
                    l_otherCountry = l_neighbourCountry;
                }
            }

            String cmdOne = "deploy " + l_randomCountry.getCountryName() + " " + l_playerOneArmies;
            String cmdTwo = "deploy " + l_otherCountry.getCountryName() + " " + l_playerTwoArmies;
            String cmdAdvance = "advance " + l_randomCountry.getCountryName() + " " + l_otherCountry.getCountryName() + " " + (l_playerOneArmies - 1);
            String cmdThree = "negotiate " + l_playerTwo.getPlayerName();

            Command l_deployCmd = new Command(cmdOne);
            Command l_deployCmdTwo = new Command(cmdTwo);
            Command l_negotiateCmd = new Command(cmdThree);
            Command l_advanceCmd = new Command(cmdAdvance);

            l_playerOne.getAllCards().add("negotiate");
            d_issueOrderPhase.performDeployArmies(l_deployCmd,l_playerOne);
            d_issueOrderPhase.performDeployArmies(l_deployCmdTwo,l_playerTwo);
            d_issueOrderPhase.performCardHandle(l_negotiateCmd,l_playerOne);
            d_issueOrderPhase.performAdvanceArmies(l_advanceCmd,l_playerOne);
            d_orderExecutionPhase.executeOrders();
            assertEquals(l_randomCountry.getArmies(),l_playerOneArmies);
            assertEquals(l_otherCountry.getArmies(),l_playerTwoArmies);


        } catch(Exception ex){
            assertNull(ex);
        }
    }
}