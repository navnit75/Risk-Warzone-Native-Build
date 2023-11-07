package org.Controller;

public class Main {

    public static void main(String[] args) {

        GameController l_game  = new GameController();
        GameController.setLoggerContext("logFile.txt");
        try {
            l_game.getCurrentPhase().initPhase();
        } catch (org.Exceptions.InvalidState invalidState) {
            throw new RuntimeException(invalidState);
        }

    }
}