package org.Model;

import org.Constants.AllTheConstants;
import org.Controller.GameController;
import org.Exceptions.MapInvalidException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MapTest {
    Map d_map;

    @Before
    public void beforeMapTest(){
        GameController l_gameController = new GameController();
        GameController.setLoggerContext("testLog.txt");
        d_map = new Map();
    }

    @Test
    public void testLoadMap(){
        try {
            d_map.loadMap("europe.map");
            assertEquals(d_map.getAllCountriesAsList().size(), 24);
        } catch(MapInvalidException e){
            assertNull(e);
        }
    }

    @Test
    public void testLoadMapSecond(){
        try {
            d_map.loadMap("europeInvalidMap2.map");
        } catch(MapInvalidException e){
            assertEquals(e.getMessage(),"Invalid Map Provided.");
        }
    }

    @Test
    public void testLoadMapThird(){
        try{
            d_map.loadMap("Invalid_canadaMap1.map");
        } catch(MapInvalidException e){
            assertEquals(e.getMessage(),"Invalid Map Provided.");
        }
    }

    @Test
    public void testMapValidity(){
        try {
            d_map.loadMap("europe.map");
            d_map.validateMap();
        } catch (MapInvalidException ex){
            assertNull(ex);
        }
    }


    @Test
    public void testMapValiditySecond(){
        try {
            d_map.loadMap("europeInvalid2.map");
            d_map.validateMap();
        } catch (MapInvalidException ex){
            assertEquals(ex.getMessage(),"Invalid Map Provided.");
        }
    }


    @Test
    public void testMapValidityThird(){
        try {
            d_map.loadMap("Invalid_canadaMap1.map");
            d_map.validateMap();
        } catch (MapInvalidException ex){
            assertEquals(ex.getMessage(),"Invalid Map Provided.");
        }
    }

}