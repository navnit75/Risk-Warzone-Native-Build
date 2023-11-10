package org.Utils;

import org.Constants.AllTheConstants;
import org.Controller.GameEngine;
import org.Exceptions.InvalidCommand;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Utility class to handle the Command received from players.
 */
public class Command {
    /**
     * Command string received from user
     */
    private String d_receivedCommand;

    /**
     * Helper data member for returning the result of handling each command separately
     */
    private HashMap<String, ArrayList<String>> l_returnType;

    /**
     * parameterized constructor
     * @param l_command : Command string
     */
    public Command(String l_command){
        this.d_receivedCommand = l_command.replaceAll(" -"," ");
    }

    /**
     * Method returns the Main Operation of the command.
     * @return String : Main operation substring of the command
     * @throws InvalidCommand : In case wrong operation is provided
     */
    public String getMainOperation() throws InvalidCommand{
        String l_mainOperation = d_receivedCommand.split(" ")[0].trim();
        if(!AllTheConstants.allMapCommands.contains(l_mainOperation)){
            throw new InvalidCommand("Invalid command provided.");
        }
        return l_mainOperation;
    }

    /**
     * Method handles command string related to editing continents
     * @return HashMap : of the all the add and remove based arguments
     * @throws InvalidCommand :  In case wrong operation is provided.
     */
    public HashMap<String, ArrayList<String>> handleEditContinent() throws InvalidCommand {
        l_returnType = new HashMap<>();
        ArrayList<String> l_allAddOperations = new ArrayList<>();
        ArrayList<String> l_allRemoveOperations = new ArrayList<>();
        String[] l_commandSplit = d_receivedCommand.split(" ");

        if(l_commandSplit.length < 3){
            GameEngine.log("Command::handleEditContinent",LogLevel.BASICLOG,"Invalid number of " +
                    "arguments provided for editcontinent");
            throw new InvalidCommand("Invalid command for editcontinent.");
        }
        int i = 1 ;
        String l_firstArgument = "" , l_secondArgument = "";
        try {
            while (i < l_commandSplit.length) {
                if (l_commandSplit[i].equalsIgnoreCase("add")) {
                    l_firstArgument = l_commandSplit[i + 1];
                    l_secondArgument = l_commandSplit[i + 2];
                    l_allAddOperations.add(l_firstArgument.trim());
                    l_allAddOperations.add(l_secondArgument.trim());
                    i += 3;
                } else if (l_commandSplit[i].equalsIgnoreCase(("remove"))) {
                    l_firstArgument = l_commandSplit[i+1];
                    l_allRemoveOperations.add(l_firstArgument.trim());
                    i += 2;
                } else {
                    GameEngine.log("Command::handleEditContinent",LogLevel.BASICLOG,"Invalid " +
                            "operation provided.");
                    throw new InvalidCommand("");
                }
            }
        } catch(Exception l_e){
            GameEngine.log("Command::handleEditContinent",LogLevel.BASICLOG,"Invalid " +
                    "commmand provided.");
            throw new InvalidCommand("Invalid arguments provided for editcontinent operation !!!.");
        }
        l_returnType.put("add",l_allAddOperations);
        l_returnType.put("remove",l_allRemoveOperations);
        return l_returnType;
    }

    /**
     * Method handles command string related to editing country
     * @return HashMap : of the all the add and remove based arguments
     * @throws InvalidCommand :  In case wrong operation is provided.
     */
    public HashMap<String, ArrayList<String>> handleEditCountry() throws InvalidCommand{
        l_returnType = new HashMap<>();
        ArrayList<String> l_allAddOperations = new ArrayList<>();
        ArrayList<String> l_allRemoveOperations = new ArrayList<>();
        String[] l_commandSplit = d_receivedCommand.split(" ");

        if(l_commandSplit.length < 3){
            GameEngine.log("Command::handleEditCountry",LogLevel.BASICLOG,"Invalid number of arguments provided for editcountry");
            throw new InvalidCommand("Invalid command for editcountry.");
        }
        String l_firstArgument = "" , l_secondArgument = "";
        try {
            int i = 1;
            while (i < l_commandSplit.length) {
                if (l_commandSplit[i].equalsIgnoreCase("add")) {
                    l_firstArgument = l_commandSplit[i + 1];
                    l_secondArgument = l_commandSplit[i + 2];
                    l_allAddOperations.add(l_firstArgument.trim());
                    l_allAddOperations.add(l_secondArgument.trim());
                    i += 3;
                } else if (l_commandSplit[i].equalsIgnoreCase(("remove"))) {
                    l_firstArgument = l_commandSplit[i+1];
                    l_allRemoveOperations.add(l_firstArgument.trim());
                    i += 2;
                } else{
                    GameEngine.log("Command::handleEditCountry",LogLevel.BASICLOG,"Invalid " +
                            "operation provided.");
                    throw new InvalidCommand("");
                }
            }
        } catch(Exception l_e){
            GameEngine.log("Command::handleEditCountry",LogLevel.BASICLOG,"Invalid " +
                    "editcountry commmand provided.");
            throw new InvalidCommand("Invalid arguments provided for editcountry operation !!!.");
        }
        l_returnType.put("add",l_allAddOperations);
        l_returnType.put("remove",l_allRemoveOperations);
        return l_returnType;
    }

    /**
     * Method handles command string related to editing neighbours
     * @return HashMap : of the all the add and remove based arguments
     * @throws InvalidCommand :  In case wrong operation is provided.
     */
    public HashMap<String, ArrayList<String>> handleEditNeighbour() throws InvalidCommand{
        l_returnType = new HashMap<>();
        ArrayList<String> l_allAddOperations = new ArrayList<>();
        ArrayList<String> l_allRemoveOperations = new ArrayList<>();
        String[] l_commandSplit = d_receivedCommand.split(" ");
        String l_firstArgument = "" , l_secondArgument = "";

        if(l_commandSplit.length < 4){
            GameEngine.log("Command::handleEditNeighbour",LogLevel.BASICLOG,"Invalid number of " +
                    "arguments provided for editneighbour");
            throw new InvalidCommand("Invalid command for editneighbour. !!!");
        }
        try {
            int i = 1;
            while (i < l_commandSplit.length) {
                if (l_commandSplit[i].equalsIgnoreCase("add")) {
                    l_firstArgument = l_commandSplit[i + 1];
                    l_secondArgument = l_commandSplit[i + 2];
                    l_allAddOperations.add(l_firstArgument.trim());
                    l_allAddOperations.add(l_secondArgument.trim());
                } else if (l_commandSplit[i].equalsIgnoreCase(("remove"))) {
                    l_firstArgument = l_commandSplit[i + 1];
                    l_secondArgument = l_commandSplit[i + 2];
                    l_allRemoveOperations.add(l_firstArgument.trim());
                    l_allRemoveOperations.add(l_secondArgument.trim());
                } else {
                    GameEngine.log("Command::handleEditNeighbour", LogLevel.BASICLOG, "Invalid " +
                            "arguments provided for editneighbour");
                    throw new InvalidCommand("Invalid arguments provided with editneighbour operation. !!!");
                }
                i += 3;
            }
        }
        catch(Exception l_e){
            GameEngine.log("Command::handleEditNeighbour", LogLevel.BASICLOG, "Invalid arguments" +
                    " provided for editneighbour");
            throw new InvalidCommand("Invalid arguments provided with editneighbour operation. !!!");
        }
        l_returnType.put("add",l_allAddOperations);
        l_returnType.put("remove",l_allRemoveOperations);
        return l_returnType;
    }

    /**
     * Method handles command string related to loadmap command
     * @return String : filename which needs to be loaded
     * @throws InvalidCommand :  In case wrong operation is provided.
     */
    public String handleLoadMap() throws InvalidCommand{
        String[] l_commandSplit = d_receivedCommand.split(" ");
        if(l_commandSplit.length != 2){
            GameEngine.log("Command::handleLoadMap",LogLevel.BASICLOG,"Invalid number of " +
                    "arguments provided for loadmap");
            throw new InvalidCommand("Invalid command for loadmap");
        } else {
            if(l_commandSplit[1] != null){
                return l_commandSplit[1];
            } else {
                GameEngine.log("Command::handleLoadMap",LogLevel.BASICLOG,"Invalid " +
                        "arguments provided for loadmap");
                throw new InvalidCommand("Invalid arguments provided for loadmap operation.");
            }
        }
    }

    /**
     * Method handles command string related to editing map
     * @return String : filename which needs to be loaded
     * @throws InvalidCommand :  In case wrong operation is provided.
     */
    public String handleEditMap() throws InvalidCommand{
        String[] l_commandSplit = d_receivedCommand.split(" ");
        if(l_commandSplit.length != 2){
            GameEngine.log("Command::handleEditMap",LogLevel.BASICLOG,"Invalid number of arguments provided for editmap");
            throw new InvalidCommand("Invalid command for editmap");
        }
        else {
            if(l_commandSplit[1] != null){
                return l_commandSplit[1];
            } else {
                GameEngine.log("Command::handleLoadMap",LogLevel.BASICLOG,"Invalid " +
                        "arguments provided for editmap");
                throw new InvalidCommand("Invalid arguments provided for editmap operation.");
            }
        }
    }

    /**
     * Method handles command string related to saving map
     * @return String : filename with which map needs to be saved
     * @throws InvalidCommand :  In case wrong operation is provided.
     */
    public String handleSaveMap() throws InvalidCommand{
        String[] l_commandSplit = d_receivedCommand.split(" ");
        if(l_commandSplit.length != 2){
            GameEngine.log("Command::handleSaveMap",LogLevel.BASICLOG,"Invalid number of arguments provided for savemap");
            throw new InvalidCommand("Invalid command for savemap");
        }else {
            if(l_commandSplit[1] != null){
                return l_commandSplit[1];
            } else {
                GameEngine.log("Command::handleLoadMap",LogLevel.BASICLOG,"Invalid " +
                        "arguments provided for editmap");
                throw new InvalidCommand("Invalid arguments provided for savemap operation.");
            }
        }

    }

    /**
     * Method handles command string related to adding and removing of player
     * @return HashMap : of the all the add and remove based arguments
     * @throws InvalidCommand :  In case wrong operation is provided.
     */
    public HashMap<String,ArrayList<String>> handleAddAndRemovePlayer() throws InvalidCommand{
        l_returnType = new HashMap<>();
        ArrayList<String> l_allAddOperations = new ArrayList<>();
        ArrayList<String> l_allRemoveOperations = new ArrayList<>();
        String[] l_commandSplit = d_receivedCommand.split(" ");
        if(l_commandSplit.length < 3){
            GameEngine.log("Command::handleAddAndRemovePlayer",LogLevel.BASICLOG,"Invalid number of arguments provided for gameplayer");
            throw new InvalidCommand("Invalid arguments for gameplayer");
        }
        int i = 1;

        try {
            while (i < l_commandSplit.length) {
                if (l_commandSplit[i].equalsIgnoreCase("add")) {
                    l_allAddOperations.add(l_commandSplit[i + 1]);
                } else if (l_commandSplit[i].equalsIgnoreCase("remove")) {
                    l_allRemoveOperations.add(l_commandSplit[i + 1]);
                } else {
                    GameEngine.log("Command::handleAddAndRemovePlayer",LogLevel.BASICLOG,"Invalid argument provided for gameplayer operation");
                    throw new InvalidCommand("");
                }
                i += 2;
            }
        }catch(Exception l_e){
            GameEngine.log("Command::handleAddAndRemovePlayer",LogLevel.BASICLOG,"Invalid argument provided for gameplayer operation");
            throw new InvalidCommand("Invalid arguments provided for gameplayer operation.");
        }
        l_returnType.put("add",l_allAddOperations);
        l_returnType.put("remove",l_allRemoveOperations);
        return l_returnType;
    }

    /**
     * Method handles command string related to handling of deploy command
     * @return ArrayList : of the all the country based arguments provided
     * @throws InvalidCommand :  In case wrong operation is provided.
     */
    public ArrayList<String> handleDeployArmies() throws InvalidCommand{
        ArrayList<String> l_returnArguments = new ArrayList<>();
        String[] l_commandSplit = d_receivedCommand.split(" ");
        int i = 1;
        if(l_commandSplit.length != 3){
            GameEngine.log("Command::handleDeployArmy",LogLevel.BASICLOG,"Invalid number of arguments provided for deploy");
            throw new InvalidCommand("Not a valid Deploy command.");
        } else {
            try{
                String l_arg;
                for(int j = 1 ; j < 3 ; j++){
                    l_arg = l_commandSplit[j];
                    l_returnArguments.add(l_arg);
                }
                Integer l_tempInteger = Integer.parseInt(l_returnArguments.get(1));
            } catch(Exception l_e){
                GameEngine.log("Command::handleDeployArmy",LogLevel.BASICLOG,"Invalid arguments provided for deploy");
                throw new InvalidCommand("Invalid arguments for airlift operation provided.");
            }
        }
        return l_returnArguments;
    }

    /**
     * Method handles command string related to handling of advance command
     * @return ArrayList : of the all the country based arguments provided
     * @throws InvalidCommand :  In case wrong operation is provided.
     */
    public ArrayList<String> handleAdvanceArmies() throws InvalidCommand{
        ArrayList<String> l_returnArguments = new ArrayList<>();
        String[] l_commandSplit = d_receivedCommand.split(" ");
        int i = 1;
        if(l_commandSplit.length != 4){
            GameEngine.log("Command::handleAdvanceArmies",LogLevel.BASICLOG,"Invalid number of arguments provided for advance");
            throw new InvalidCommand("Not a valid Deploy command.");
        } else {
            try{
                String l_arg;
                for(int j = 1 ; j < 4 ; j++){
                    l_arg = l_commandSplit[j];
                    l_returnArguments.add(l_arg);
                }
                Integer l_tempInteger = Integer.parseInt(l_returnArguments.get(2));
            } catch(Exception l_e){
                GameEngine.log("Command::handleAdvanceArmies",LogLevel.BASICLOG,"Invalid arguments provided for advance");
                throw new InvalidCommand("Invalid arguments for advance operation provided.");
            }
        }
        return l_returnArguments;
    }

    /**
     * Method handles command string related to handling of blockade command
     * @return String : Returns the name of the country, on which bloakade has to be applied
     * @throws InvalidCommand :  In case wrong operation is provided.
     */
    public String handleBlockadeCommand() throws InvalidCommand{
        String[] l_commandSplit = d_receivedCommand.split(" ");
        if(l_commandSplit.length != 2) {
            GameEngine.log("Command::handleBlockadeCommand", LogLevel.BASICLOG,"Invalid number of arguments provided for blockade");
            throw new InvalidCommand("Not a valid Blockade Command");
        } else {
            if(l_commandSplit[1] != null){
                return l_commandSplit[1];
            } else {
                throw new InvalidCommand("Invalid arguments provided for blockade operation.");
            }
        }

    }

    /**
     * Method handles command string related to handling of deploy Airlift
     * @return ArrayList : of the all the country based arguments provided
     * @throws InvalidCommand :  In case wrong operation is provided.
     */
    public ArrayList<String> handleAirliftCommand() throws InvalidCommand{
        ArrayList<String> l_returnArguments = new ArrayList<>();
        String[] l_commandSplit = d_receivedCommand.split(" ");
        if(l_commandSplit.length != 4){
            GameEngine.log("Command::handleAirliftCommand",LogLevel.BASICLOG,"Invalid number of arguments provided for airlift");
            throw new InvalidCommand("Not a valid airlift command.");
        } else{
            try{
                String l_arg;
                for(int j = 1 ; j < 4 ; j++){
                    l_arg = l_commandSplit[j];
                    l_returnArguments.add(l_arg);
                }
                Integer l_tempInteger = Integer.parseInt(l_returnArguments.get(2));
            } catch(Exception l_e){
                GameEngine.log("Command::handleAirliftCommand",LogLevel.BASICLOG,"Invalid arguments provided for airlift");
                throw new InvalidCommand("Invalid arguments for airlift operation provided.");
            }

        }
        return l_returnArguments;
    }

    /**
     * Method handles command string related to handling of Bomb command
     * @return String : Returns the name of the country, on which bomb has to be applied
     * @throws InvalidCommand :  In case wrong operation is provided.
     */
    public String handleBombCommand() throws InvalidCommand{
        String[] l_commandSplit = d_receivedCommand.split(" ");

        if(l_commandSplit.length != 2) {
            GameEngine.log("Command::handleBombCommand", LogLevel.BASICLOG,"Invalid number of arguments provided for bomb");
            throw new InvalidCommand("Not a valid Bomb Command");
        } else {
            if(l_commandSplit[1] != null){
                return l_commandSplit[1];
            } else {
                GameEngine.log("Command::handleBombCommand", LogLevel.BASICLOG,"Invalid arguments provided for bomb");
                throw new InvalidCommand("Invalid arguments provided for bomb operation.");
            }
        }

    }

    /**
     * Method handles command string related to handling of negotiate command
     * @return String : Returns the name of the player , with which current player wants to negotiate
     * @throws InvalidCommand :  In case wrong operation is provided.
     */
    public String handleNegotiateCommand() throws InvalidCommand{
        String[] l_commandSplit = d_receivedCommand.split(" ");
        if(l_commandSplit.length > 2) {
            GameEngine.log("Command::handleNegotiateCommand",LogLevel.BASICLOG,"Invalid number of arguments provided for negotiate");
            throw new InvalidCommand("Not a valid Negotiate Command.");
        } else {
            if(l_commandSplit[1] != null){
                return l_commandSplit[1];
            } else {
                GameEngine.log("Command::handleNegotiateCommand",LogLevel.BASICLOG,"Invalid arguments provided for negotiate");
                throw new InvalidCommand("Invalid arguments provided for negotiate operation.");
            }
        }
    }
}
