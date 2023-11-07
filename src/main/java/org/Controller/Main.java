package org.Controller;

/**
 * A main class to signify the starting of the game.
 * Usually creates a gamecontroller object and defines the context for the logging.
 */
public class Main {


    public static void main(String[] args) {

        GameController l_game  = new GameController();
        GameController.setLoggerContext("logFile.txt");
        try {
            l_game.getCurrentPhase().initPhase();
        } catch (Exception ex){

        }

    }
}