package org.Model.Phases;

import org.Controller.GameEngine;
import org.Exceptions.InvalidCommand;
import org.Exceptions.InvalidState;
import org.Exceptions.MapInvalidException;
import org.Model.Continent;
import org.Model.Country;
import org.Model.GameState;
import org.Utils.Command;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class StartUpPhaseTest {
    GameEngine d_gameEngine;
    GameState d_gameState;
    Phase d_phase;

    @Before
    public void beforeStartUpPhaseTest(){
        d_gameEngine = new GameEngine();
        d_gameState = new GameState();
        d_phase = new StartUpPhase(d_gameEngine,d_gameState);
        GameEngine.setLoggerContext("testLog.txt");
    }

    @Test
    public void testPerformLoadMap(){
        Command l_loadMapCommand  = new Command("loadmap canada.map");
        try{
            d_phase.performLoadMap(l_loadMapCommand,null);
            assertEquals(31, d_gameState.getCurrentMap().getAllCountriesAsList().size());
        } catch(MapInvalidException | FileNotFoundException | InvalidCommand ex){
            assertNull(ex);
        }
    }

    @Test
    public void testPerformLoadMapInvalid(){
        Command l_loadMapCommand  = new Command("loadmap Invalid_candaMap1.map");
        try{
            d_phase.performLoadMap(l_loadMapCommand,null);
        } catch(MapInvalidException | FileNotFoundException | InvalidCommand ex){
            assertEquals(ex.getMessage(), "Invalid Map Provided.");
        }
    }
    @Test
    public void testCreatePlayers(){
        Command l_playerCommand  = new Command("gameplayer -add p1 -add p2");
        Command l_loadMapCommand  = new Command("loadmap canada.map");
        try {
            d_phase.performLoadMap(l_loadMapCommand,null);
            d_phase.performCreatePlayers(l_playerCommand, null);
            assertEquals(d_gameState.getPlayerController().getAllPlayers().size() , 2);
        } catch(InvalidCommand | MapInvalidException | FileNotFoundException ex){
            assertNull(ex);
        }

    }

    @Test
    public void testAssignCountries(){
        Command l_playerCommand  = new Command("gameplayer -add p1 -add p2");
        Command l_loadMapCommand  = new Command("loadmap canada.map");
        try{
            GameEngine d_gameEngineMock = mock(GameEngine.class);
            GameState d_gameStateNew = new GameState();
            Phase d_newPhase = new StartUpPhase(d_gameEngineMock,d_gameStateNew);
            d_newPhase.performLoadMap(l_loadMapCommand,null);
            d_newPhase.performCreatePlayers(l_playerCommand, null);

            doNothing().when(d_gameEngineMock).setIssueOrderPhase();
            d_newPhase.performAssignCountries(null);

            Boolean checkCondition =
                    d_gameStateNew.getPlayerController().getPlayerByName("p1").getCountryCaptured().size() != 0 &&
                            d_gameStateNew.getPlayerController().getPlayerByName("p2").getCountryCaptured().size() != 0;
            assertTrue(checkCondition);
        } catch(InvalidCommand | MapInvalidException | InvalidState| FileNotFoundException ex){
            assertNull(ex);
        }
    }

    @Test
    public void testPerformEditCountry(){
        Command l_editMapCommand = new Command("editmap testCountry.map");
        Command l_editContinentCommand = new Command("editcontinent -add Asia 4");
        Command l_editCountryCommand = new Command("editcountry -add India Asia -add China Asia");
        Command l_editNeighbourCommand = new Command("editneighbor -add India China");

        try{
            d_phase.performEditMap(l_editMapCommand,null);
            d_phase.performEditContinent(l_editContinentCommand,null);
            d_phase.performEditCountry(l_editCountryCommand,null);
            d_phase.performEditNeighbour(l_editNeighbourCommand,null);

            List<Country> countriesMade = d_gameState.getCurrentMap().getAllCountriesAsList();
            assertEquals("India",countriesMade.get(0).getCountryName());
            assertEquals("China",countriesMade.get(1).getCountryName());
        }catch(IOException | MapInvalidException | InvalidCommand  ex){
            assertNull(ex);
        }
    }

    @Test
    public void testPerformEditContinent(){
        Command l_editMapCommand = new Command("editmap testContinent.map");
        Command l_editContinentCommand = new Command("editcontinent -add Asia 4");
        Command l_editCountryCommand = new Command("editcountry -add India Asia -add China Asia");
        Command l_editNeighbourCommand = new Command("editneighbor -add India China");

        try{
            d_phase.performEditMap(l_editMapCommand,null);
            d_phase.performEditContinent(l_editContinentCommand,null);
            d_phase.performEditCountry(l_editCountryCommand,null);
            d_phase.performEditNeighbour(l_editNeighbourCommand,null);

            List<Continent> continentsMade = d_gameState.getCurrentMap().getAllContinentsList();
            assertEquals("Asia",continentsMade.get(0).getContinentName());
        }catch(IOException | MapInvalidException | InvalidCommand  ex){
            assertNull(ex);
        }
    }

    @Test
    public void testPerformEditNeighbors(){
        Command l_editMapCommand = new Command("editmap testNeighbor.map");
        Command l_editContinentCommand = new Command("editcontinent -add Asia 4");
        Command l_editCountryCommand = new Command("editcountry -add India Asia -add China Asia");
        Command l_editNeighbourCommand = new Command("editneighbor -add India China");

        try{
            d_phase.performEditMap(l_editMapCommand,null);
            d_phase.performEditContinent(l_editContinentCommand,null);
            d_phase.performEditCountry(l_editCountryCommand,null);
            d_phase.performEditNeighbour(l_editNeighbourCommand,null);

            Integer neighBourCountry = d_gameState.getCurrentMap().getCountryByName("India").getNeighbourCountries().get(0);
            Country neighbourCountry = d_gameState.getCurrentMap().getCountryById(neighBourCountry);

            assertEquals("China",neighbourCountry.getCountryName());
        }catch(IOException | MapInvalidException | InvalidCommand  ex){
            assertNull(ex);
        }
    }

    @Test
    public void testPerformShowMap(){
        Command l_loadMapCommand  = new Command("loadmap canada.map");
        try {
            d_phase.performLoadMap(l_loadMapCommand, null);
            d_phase.performShowMap(null);
            assertTrue(true);
        } catch(InvalidCommand | FileNotFoundException | MapInvalidException ex){
            assertNull(ex);
        }

    }

}