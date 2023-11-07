package org.Utils;

import org.Constants.*;
import org.Controller.GameController;
import org.Exceptions.InvalidCommand;
import java.util.*;

public class Command {
    private String d_receivedCommand;
    private HashMap<String, ArrayList<String>> l_returnType;
    public Command(String l_command){
        this.d_receivedCommand = l_command.replaceAll(" -"," ");
    }
    public String getMainOperation() throws InvalidCommand{
        String l_mainOperation = d_receivedCommand.split(" ")[0].trim();
        if(!AllTheConstants.allMapCommands.contains(l_mainOperation)){
            throw new InvalidCommand("Invalid command provided.");
        }
        return l_mainOperation;
    }

    public HashMap<String, ArrayList<String>> handleEditContinent() throws InvalidCommand {
        l_returnType = new HashMap<>();
        ArrayList<String> l_allAddOperations = new ArrayList<>();
        ArrayList<String> l_allRemoveOperations = new ArrayList<>();
        String[] l_commandSplit = d_receivedCommand.split(" ");

        if(l_commandSplit.length < 3){
            GameController.log("Command",LogLevel.BASICLOG,"Invalid number of arguments provided for editcontinent");
            throw new InvalidCommand("Invalid command for editcontinent.");
        }
        int i = 1 ;
        while( i < l_commandSplit.length){
            if(l_commandSplit[i].equalsIgnoreCase("add")){
                l_allAddOperations.add(l_commandSplit[i+1]);
                l_allAddOperations.add(l_commandSplit[i+2]);
                i += 3;
            } else if(l_commandSplit[i].equalsIgnoreCase(("remove"))){
                l_allRemoveOperations.add(l_commandSplit[i+1]);
                i += 2;
            }
        }
        l_returnType.put("add",l_allAddOperations);
        l_returnType.put("remove",l_allRemoveOperations);
        return l_returnType;
    }

    public HashMap<String, ArrayList<String>> handleEditCountry() throws InvalidCommand{
        l_returnType = new HashMap<>();
        ArrayList<String> l_allAddOperations = new ArrayList<>();
        ArrayList<String> l_allRemoveOperations = new ArrayList<>();
        String[] l_commandSplit = d_receivedCommand.split(" ");

        if(l_commandSplit.length < 3){
            GameController.log("Command",LogLevel.BASICLOG,"Invalid number of arguments provided for editcountry");
            throw new InvalidCommand("Invalid command for editcountry.");
        }

        int i = 1 ;
        while( i < l_commandSplit.length){
            if(l_commandSplit[i].equalsIgnoreCase("add")){
                l_allAddOperations.add(l_commandSplit[i+1]);
                l_allAddOperations.add(l_commandSplit[i+2]);
                i += 3;
            } else if(l_commandSplit[i].equalsIgnoreCase(("remove"))){
                l_allRemoveOperations.add(l_commandSplit[i+1]);
                i += 2;
            }
        }
        l_returnType.put("add",l_allAddOperations);
        l_returnType.put("remove",l_allRemoveOperations);
        return l_returnType;
    }


    public HashMap<String, ArrayList<String>> handleEditNeighbour() throws InvalidCommand{
        l_returnType = new HashMap<>();
        ArrayList<String> l_allAddOperations = new ArrayList<>();
        ArrayList<String> l_allRemoveOperations = new ArrayList<>();
        String[] l_commandSplit = d_receivedCommand.split(" ");

        if(l_commandSplit.length < 4){
            GameController.log("Command",LogLevel.BASICLOG,"Invalid number of arguments provided for editneighbour");
            throw new InvalidCommand("Invalid command for editneighbour.");
        }
        int i = 1 ;
        while( i < l_commandSplit.length){
            if(l_commandSplit[i].equalsIgnoreCase("add")){
                l_allAddOperations.add(l_commandSplit[i+1]);
                l_allAddOperations.add(l_commandSplit[i+2]);
            } else if(l_commandSplit[i].equalsIgnoreCase(("remove"))){
                l_allRemoveOperations.add(l_commandSplit[i+1]);
                l_allRemoveOperations.add(l_commandSplit[i+2]);
            }
            i += 3;
        }
        l_returnType.put("add",l_allAddOperations);
        l_returnType.put("remove",l_allRemoveOperations);
        return l_returnType;
    }

    public String handleLoadMap() throws InvalidCommand{
        String[] l_commandSplit = d_receivedCommand.split(" ");
        if(l_commandSplit.length != 2){
            GameController.log("Command",LogLevel.BASICLOG,"Invalid number of arguments provided for loadmap");
            throw new InvalidCommand("Invalid command for loadmap");
        }
        return l_commandSplit[1].trim();
    }

    public String handleEditMap() throws InvalidCommand{
        String[] l_commandSplit = d_receivedCommand.split(" ");
        if(l_commandSplit.length != 2){
            GameController.log("Command",LogLevel.BASICLOG,"Invalid number of arguments provided for editmap");
            throw new InvalidCommand("Invalid command for editmap");
        }
        return l_commandSplit[1].trim();
    }

    public String handleSaveMap() throws InvalidCommand{
        String[] l_commandSplit = d_receivedCommand.split(" ");
        if(l_commandSplit.length != 2){
            GameController.log("Command",LogLevel.BASICLOG,"Invalid number of arguments provided for savemap");
            throw new InvalidCommand("Invalid command for savemap");
        }
        return l_commandSplit[1].trim();
    }

    public HashMap<String,ArrayList<String>> handleAddAndRemovePlayer() throws InvalidCommand{
        l_returnType = new HashMap<>();
        ArrayList<String> l_allAddOperations = new ArrayList<>();
        ArrayList<String> l_allRemoveOperations = new ArrayList<>();
        String[] l_commandSplit = d_receivedCommand.split(" ");
        if(l_commandSplit.length < 3){
            GameController.log("Command",LogLevel.BASICLOG,"Invalid number of arguments provided for gameplayer");
            throw new InvalidCommand("Invalid arguments for gameplayer");
        }
        int i = 1;


        while( i < l_commandSplit.length){
            if(l_commandSplit[i].equalsIgnoreCase("add")){
                l_allAddOperations.add(l_commandSplit[i+1]);
            } else if(l_commandSplit[i].equalsIgnoreCase("remove")){
                l_allRemoveOperations.add(l_commandSplit[i+1]);
            }
            i += 1;
        }
        l_returnType.put("add",l_allAddOperations);
        l_returnType.put("remove",l_allRemoveOperations);
        return l_returnType;
    }

    public ArrayList<String> handleDeployArmies() throws InvalidCommand{
        ArrayList<String> l_returnArguments = new ArrayList<>();
        String[] l_commandSplit = d_receivedCommand.split(" ");
        int i = 1;
        if(l_commandSplit.length != 3){
            GameController.log("Command",LogLevel.BASICLOG,"Invalid number of arguments provided for deploy");
            throw new InvalidCommand("Not a valid Deploy command.");
        } else {
            l_returnArguments.add(l_commandSplit[1]);
            l_returnArguments.add(l_commandSplit[2]);
        }
        return l_returnArguments;
    }

    public ArrayList<String> handleAdvanceArmies() throws InvalidCommand{
        ArrayList<String> l_returnArguments = new ArrayList<>();
        String[] l_commandSplit = d_receivedCommand.split(" ");
        int i = 1;
        if(l_commandSplit.length != 4){
            GameController.log("Command",LogLevel.BASICLOG,"Invalid number of arguments provided for advance");
            throw new InvalidCommand("Not a valid Deploy command.");
        } else {
            l_returnArguments.add(l_commandSplit[1]);
            l_returnArguments.add(l_commandSplit[2]);
            l_returnArguments.add(l_commandSplit[3]);
        }
        return l_returnArguments;
    }

    public String handleBlockadeCommand() throws InvalidCommand{
        String[] l_commandSplit = d_receivedCommand.split(" ");
        if(l_commandSplit.length != 2) {
            GameController.log("Command", LogLevel.BASICLOG,"Invalid number of arguments provided for blockade");
            throw new InvalidCommand("Not a valid Blockade Command");
        } else {
            return l_commandSplit[1];
        }

    }

    public ArrayList<String> handleAirliftCommand() throws InvalidCommand{
        ArrayList<String> l_returnArguments = new ArrayList<>();
        String[] l_commandSplit = d_receivedCommand.split(" ");
        int i = 1;
        if(l_commandSplit.length != 4){
            GameController.log("Command",LogLevel.BASICLOG,"Invalid number of arguments provided for airlift");
            throw new InvalidCommand("Not a valid airlift command.");
        } else{
            l_returnArguments.add(l_commandSplit[1]);
            l_returnArguments.add(l_commandSplit[2]);
            l_returnArguments.add(l_commandSplit[3]);
        }
        return l_returnArguments;
    }
    public String handleBombCommand() throws InvalidCommand{
        String[] l_commandSplit = d_receivedCommand.split(" ");

        if(l_commandSplit.length != 2) {
            GameController.log("Command", LogLevel.BASICLOG,"Invalid number of arguments provided for bomb");
            throw new InvalidCommand("Not a valid Bomb Command");
        } else {
            return l_commandSplit[1];
        }

    }

    public String handleNegotiateCommand() throws InvalidCommand{
        String[] l_commandSplit = d_receivedCommand.split(" ");
        if(l_commandSplit.length > 2) {
            GameController.log("Command",LogLevel.BASICLOG,"Invalid number of arguments provided for negotiate");
            throw new InvalidCommand("Not a valid Negotiate Command.");
        } else {
            return l_commandSplit[1];
        }
    }
}
