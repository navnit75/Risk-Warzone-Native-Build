package org.Model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.*;
import java.util.stream.Collectors;
import java.io.File;
import java.util.*;

import org.Controller.GameController;
import org.Exceptions.MapInvalidException;
import org.Constants.AllTheConstants;
import org.Utils.LogLevel;
import org.davidmoten.text.utils.WordWrap;

/**
 * This class is used to Manage a Map
 */
public class Map {


    public static Boolean d_isMapLoaded = false;
    private List<Country> d_allCountriesAsList;
    private List<Continent> d_allContinentsAsList;
    private String d_mapFile;

    public String getMapFile(){ return this.d_mapFile; }
    public void setMapFile(String l_mapFile){this.d_mapFile = l_mapFile;}
    public List<Continent> getAllContinentsList(){ return this.d_allContinentsAsList; }
    public List<Country> getAllCountriesAsList(){ return this.d_allCountriesAsList; }
    public Continent getContinentByName(String p_continentName){
        return d_allContinentsAsList
                .stream()
                .filter(l_continent->l_continent.getContinentName()
                        .equalsIgnoreCase(p_continentName))
                .findFirst()
                .orElse(null);
    }
    public Continent getContinentById(Integer p_continentId){
        if(d_allContinentsAsList == null) return null;
        else return d_allContinentsAsList.get(p_continentId - 1);
    }
    public Country getCountryByName(String p_countryName){
        return d_allCountriesAsList
                .stream()
                .filter(l_country->l_country.getCountryName()
                        .equalsIgnoreCase(p_countryName))
                .findFirst()
                .orElse(null);
    }
    public Country getCountryById(Integer p_countryId){
        if(d_allCountriesAsList == null) return null;
        else return d_allCountriesAsList.get(p_countryId-1);
    }
    public void addContinent(Continent p_continent){
        if(d_allContinentsAsList == null){
            d_allContinentsAsList = new ArrayList<>();
        }
        d_allContinentsAsList.add(p_continent);
    }
    public void addContinent(String p_continentName, Integer p_bonusValue) throws MapInvalidException{
        if(d_allContinentsAsList != null){
            int l_continentId = Math.max(1,d_allContinentsAsList.size() + 1);
            if(getContinentByName(p_continentName) ==  null){
                addContinent(new Continent(l_continentId, p_continentName, p_bonusValue));
                GameController.log("Map::addContinent", LogLevel.BASICLOG,p_continentName + " added to the GameState.");
            } else {
                GameController.log("Map::addContinent", LogLevel.BASICLOG, p_continentName + " already exists in the map.");
                throw new MapInvalidException(p_continentName + " already exists in the map");
            }
        } else {
            d_allContinentsAsList = new ArrayList<>();
            d_allContinentsAsList.add(new Continent(1,p_continentName,p_bonusValue));
        }
    }
    public void addCountry(String p_countryName, String p_continentName) throws MapInvalidException{
        if(getContinentByName(p_continentName) == null){
            throw new MapInvalidException("Map::addCountry" + p_continentName + " doesn't exist.");
        }

        Continent l_continent = getContinentByName(p_continentName);
        if(d_allCountriesAsList != null){
            int l_countryId = Math.max(1,d_allCountriesAsList.size() + 1);
            if(getCountryByName(p_countryName) == null){
                Country l_newCountry = new Country(l_countryId,p_countryName,l_continent.getContinentId());
                d_allCountriesAsList.add(l_newCountry);
                l_continent.addCountry(l_newCountry);
                GameController.log("Map::addCountry",LogLevel.BASICLOG, p_countryName + " added to the GameState");
            } else {
                GameController.log("Map::addCountry",LogLevel.BASICLOG,p_countryName + " already exist in the map");
                throw new MapInvalidException("Country already exists in the map. ");
            }
        } else {
            d_allCountriesAsList = new ArrayList<>();
            Country l_newCountry = new Country(1,p_countryName,l_continent.getContinentId());
            d_allCountriesAsList.add(l_newCountry);
            l_continent.addCountry(l_newCountry);
            GameController.log("Map::addCountry",LogLevel.BASICLOG,p_countryName + " added to the GameState");
        }
    }
    public void addNeighbour(String p_mainCountry , String p_neighbourCountry) throws MapInvalidException{
        if(d_allCountriesAsList != null){
            boolean l_ifBothCountryExist = (getCountryByName(p_mainCountry) != null ) && (getCountryByName(p_neighbourCountry) != null);
            if(l_ifBothCountryExist){
                getCountryByName(p_mainCountry).addNeighbour(getCountryByName(p_neighbourCountry).getCountryId());
                getCountryByName(p_neighbourCountry).addNeighbour(getCountryByName(p_mainCountry).getCountryId());
                GameController.log("Map::addNeighbour",LogLevel.BASICLOG,p_neighbourCountry + " is added as neighbour of " + p_mainCountry);
            } else {
                GameController.log("Map::addNeighbour",LogLevel.BASICLOG,"Adding neighbour failed for (" + p_mainCountry + "," +
                        p_neighbourCountry + ").");
                throw new MapInvalidException("Countries provided doesn't not exist");
            }
        }
    }
    private void removeCountryFromNeighbourOfAllCountry(Country p_countryToBeRemoved) throws MapInvalidException{
        for(Country l_country : d_allCountriesAsList){
            if(l_country.hasNeighbour(p_countryToBeRemoved)){
                l_country.removeNeighbour(p_countryToBeRemoved.getCountryId());
                GameController.log("Map::removeCountryFromNeighbourOfAllCountry",LogLevel.BASICLOG,p_countryToBeRemoved.getCountryName()
                        + " removed from neighbour of " + l_country.getCountryName());
            }
        }
    }
    public void removeCountry(String p_countryName) throws MapInvalidException{
        Country l_countryToBeRemoved = getCountryByName(p_countryName);
        if(getCountryByName(p_countryName) != null){

            // Removed from the continent
            Continent l_tempContinent = getContinentById(l_countryToBeRemoved.getContinentId());
            l_tempContinent.removeCountry(l_countryToBeRemoved);
            // Removed the country from the main list
            d_allCountriesAsList.remove(l_countryToBeRemoved);

            //Remove the country from teh neighbourList of all the countries
            removeCountryFromNeighbourOfAllCountry(l_countryToBeRemoved);
            GameController.log("Map::removeCountry",LogLevel.BASICLOG,p_countryName + " has been removed. " );
        } else {
            GameController.log("Map::removeCountry",LogLevel.BASICLOG,"Country " + p_countryName + "doesn't exist, for removal.");
            throw new MapInvalidException("Country " + p_countryName + "doesn't exist, for removal.");
        }
    }
    public void removeContinent(String p_continentName) throws MapInvalidException{
        Continent l_continentToBeRemoved = getContinentByName(p_continentName);
        if(l_continentToBeRemoved != null){
            for(Country l_country : l_continentToBeRemoved.getCountries()){
                removeCountry(l_country.getCountryName());
            }
            d_allContinentsAsList.remove(l_continentToBeRemoved);
            GameController.log("Map::removeContinent",LogLevel.BASICLOG,"Continent " + p_continentName + " removed successfully.");
        } else {
            GameController.log("Map::removeContinent",LogLevel.BASICLOG,"Continent " + p_continentName + " doesn't exist.");
            throw new MapInvalidException(p_continentName + " doesn't exist.");
        }
    }
    public void removeNeighbour(String p_mainCountry, String p_neighbourCountry) throws MapInvalidException{
        if(d_allContinentsAsList != null){
            boolean l_ifBothCountryExist = (getCountryByName(p_mainCountry) != null ) && (getCountryByName(p_neighbourCountry) != null);
            if(l_ifBothCountryExist){
                getCountryByName(p_mainCountry).removeNeighbour(getCountryByName(p_neighbourCountry).getCountryId());
                getCountryByName(p_neighbourCountry).removeNeighbour(getCountryByName(p_mainCountry).getCountryId());
                GameController.log("Map::removeNeighbour",LogLevel.BASICLOG,"Neighbour pairs (" + p_mainCountry + "," + p_neighbourCountry + "). removed");
            }
            else {
                GameController.log("Map::removeNeighbour",LogLevel.BASICLOG,"Neighbour pairs (" + p_mainCountry + "," + p_neighbourCountry + "). removal " +
                        "unsuccessful");
                throw new MapInvalidException("Neighbour doesn't exist.");
            }
        }
    }
    public boolean mapSanityCheck() throws MapInvalidException{

        try {
            if (d_allContinentsAsList == null || d_allContinentsAsList.isEmpty()) {
                GameController.log("Map::mapSanityCheck", LogLevel.BASICLOG, " Continents Lists are Empty.");
                throw new MapInvalidException("Continents lists are empty.");
            }
            if (d_allCountriesAsList == null || d_allCountriesAsList.isEmpty()) {
                GameController.log("Map::mapSanityCheck", LogLevel.BASICLOG, " Countries Lists are Empty.");
                throw new MapInvalidException("Countries list is empty.");
            }
            for (Country l_country : d_allCountriesAsList) {
                if (l_country.getNeighbourCountries().isEmpty()) {
                    GameController.log("Map::mapSanityCheck", LogLevel.BASICLOG, l_country.getCountryName() + " has no neighbours.");
                    throw new MapInvalidException(l_country.getCountryName() + " doesn't have any neighbours.");
                }
            }
            return true;
        }
        catch(Exception ex){
            throw new MapInvalidException("Map is invalid.");
        }

    }
    private void dfsCountriesInContinentConnectivity(Country p_sourceCountry, HashMap<Integer,Boolean> l_tempVertices,
                                                     Continent p_continent){
        l_tempVertices.put(p_sourceCountry.getCountryId(),true);
        for(Country l_country : p_continent.getCountries()){
            boolean l_dfsPropagationCondition = p_sourceCountry.hasNeighbour(l_country) // Check if the country is neighbour
                    && !(l_tempVertices.get(l_country.getCountryId())); // Check if the country has not been traversed yet
            if(l_dfsPropagationCondition){
                dfsCountriesInContinentConnectivity(l_country, l_tempVertices, p_continent);
            }
        }
    }
    private boolean countriesInContinentConnectivity(Continent p_continent){
        HashMap<Integer,Boolean> l_tempVertices = new HashMap<>();
        for(Country l_country : p_continent.getCountries()){
            l_tempVertices.put(l_country.getCountryId(),false);
        }
        dfsCountriesInContinentConnectivity(p_continent.getCountries().get(0), l_tempVertices, p_continent);
        return !l_tempVertices.containsValue(false);
    }
    public boolean checkContinentBasedConnectivity() throws MapInvalidException{
        for(Continent l_continent : d_allContinentsAsList){
            if(l_continent.getCountries() == null || l_continent.getCountries().isEmpty()) {
                throw new MapInvalidException(l_continent.getContinentName() + " doesn't have any countries.");
            }
            if(!countriesInContinentConnectivity(l_continent)) {
                GameController.log("Map::countriesInContinentConnectivity",LogLevel.BASICLOG,"Continent Based Connectivity : " + l_continent.getContinentName() + " failed connectivity test.");
                return false;
            }
        }
        return true;
    }
    public void dfsAllCountriesConnectivity(Country p_country, HashMap<Integer,Boolean> p_tempVertices){
        p_tempVertices.put(p_country.getCountryId(),true);
        for(int i = 0 ; i < p_country.getNeighbourCountries().size(); i++){
            Country l_country = getCountryById(p_country.getNeighbourCountries().get(i));
            if(!p_tempVertices.get(l_country.getCountryId())){
                dfsAllCountriesConnectivity(l_country,p_tempVertices);
            }
        }
    }
    public boolean checkCountryBasedConnectivity() throws MapInvalidException{
        HashMap<Integer,Boolean> l_tempVertices = new HashMap<>();
        for(Country l_country : d_allCountriesAsList){
            l_tempVertices.put(l_country.getCountryId(),false);
        }
        dfsAllCountriesConnectivity(d_allCountriesAsList.get(0),l_tempVertices);
        for(Entry<Integer,Boolean> l_entry : l_tempVertices.entrySet()){
            if(!l_entry.getValue()){
                String l_logString = getCountryById(l_entry.getKey()).getCountryName()
                        + "is not reachable.";
                GameController.log("Map::checkCountryBasedConnectivity",LogLevel.BASICLOG,l_logString);
                throw new MapInvalidException(l_logString);
            }
        }
        return !l_tempVertices.containsValue(false);
    }

    public List<Country> getNeighbourCountries(Country p_country) throws MapInvalidException {
        List<Country> l_adjCountries = new ArrayList<Country>();

        if (!p_country.getNeighbourCountries().isEmpty()) {
            for (int i : p_country.getNeighbourCountries()) {
                l_adjCountries.add(getCountryById(i));
            }
        } else {
            GameController.log("Map::getNeighbourCountries",LogLevel.BASICLOG,p_country.getCountryName() + " doesn't have any adjacent countries");
            throw new MapInvalidException(p_country.getCountryName() + " doesn't have any adjacent countries");
        }
        return l_adjCountries;
    }

    public boolean checkForDuplicateCountry(){
        HashSet<String> l_tempSet = d_allCountriesAsList
                                    .stream()
                                    .map(Country::getCountryName)
                                    .collect(Collectors.toCollection(HashSet::new));
        return l_tempSet.size() != d_allContinentsAsList.size();
    }
    public boolean validateMap() throws MapInvalidException{
        boolean l_returnResult = true;
        if(!mapSanityCheck()){
            l_returnResult = false;
        } else if(!checkForDuplicateCountry()){
            l_returnResult = false;
        } else if(!checkContinentBasedConnectivity()){
            l_returnResult = false;
        } else if(!checkCountryBasedConnectivity()){
            l_returnResult = false;
        }
        return l_returnResult;
    }

    private void renderSeparator(){
        System.out.println(AllTheConstants.consoleSepartorString.repeat(AllTheConstants.CONSOLE_WIDTH));
    }

    private void renderCenteredString(int p_width, String p_s) {
        String l_centeredString = String.format("%-" + p_width  + "s", String.format("%" + (p_s.length() +
                (p_width - p_s.length()) / 2) + "s", p_s));
        System.out.format(l_centeredString+"\n");
    }

    public void showMap() {
        if(d_allContinentsAsList == null){
            System.out.println("No continents added till now ...");
        } else {
            for (Continent l_continent : d_allContinentsAsList) {
                renderSeparator();
                renderCenteredString(AllTheConstants.CONSOLE_WIDTH,l_continent.getContinentName() + "(Bonus Value: "+ l_continent.getContinentBonusValue() + ")");
                renderSeparator();
                if (l_continent.getCountries() == null){
                    System.out.println("No Countries added...");
                } else {
                    int l_countryIndex = 1 ;
                    for (Country l_country : l_continent.getCountries()) {
                        System.out.printf("%02d. %s \n",l_countryIndex++ ,l_country.getCountryName());
                        if(l_country.getNeighbourCountries() == null){
                            System.out.print("No countries added as neighbour as of now.\n");
                        } else {
                            int l_numOfNeighbours = l_country.getNeighbourCountries().size();
                            StringBuilder l_commaSeparatedCountries = new StringBuilder();
                            l_commaSeparatedCountries.append("Neighbours");
                            l_commaSeparatedCountries.append(": ");
                            for (int i = 0; i < l_numOfNeighbours ; i++) {
                                Country l_neighbourCountry = getCountryById(l_country.getNeighbourCountries().get(i));
                                l_commaSeparatedCountries.append(l_neighbourCountry.getCountryName());

                                if( i < l_numOfNeighbours - 1) {
                                    l_commaSeparatedCountries.append(", ");
                                }
                            }
                            String l_neighbourCountries = WordWrap.from(l_commaSeparatedCountries.toString()).maxWidth(AllTheConstants.CONSOLE_WIDTH).wrap();
                            System.out.println(l_neighbourCountries);
                            System.out.println();
                        }
                    }
                }
            }
        }
    }
    public void loadMap(String p_fileName) throws MapInvalidException{
            try {
                List<String> l_fileLineByLine = new ArrayList<>();

                // Step 1: Read the file into common List
                File myObj = new File(AllTheConstants.defaultMapLocationAppendString + p_fileName);
                Scanner l_scanner = new Scanner(myObj);

                while (l_scanner.hasNextLine()) {
                    String l_readData = l_scanner.nextLine();
                    l_fileLineByLine.add(l_readData);
                }
                l_scanner.close();

                // Step 2 : Create sub list for dividing operation
                List<String> l_readContinents = l_fileLineByLine.subList(l_fileLineByLine.indexOf(AllTheConstants.continentsSeparator) + 1,
                        l_fileLineByLine.indexOf(AllTheConstants.countrySeparator) - 1);
                List<String> l_readCountries = l_fileLineByLine.subList(l_fileLineByLine.indexOf(AllTheConstants.countrySeparator) + 1,
                        l_fileLineByLine.indexOf(AllTheConstants.bordersSeparator) - 1);
                List<String> l_readNeighbours = l_fileLineByLine.subList(l_fileLineByLine.indexOf(AllTheConstants.bordersSeparator) + 1,
                        l_fileLineByLine.size());


                // Step 3: Add all the continents to the main list
                for (String l_continent : l_readContinents) {
                    String[] l_continentLineSplit = l_continent.split(" ");
                    String l_continentName = l_continentLineSplit[0];
                    Integer l_continentBonusValue = Integer.parseInt(l_continentLineSplit[1]);
                    addContinent(l_continentName, l_continentBonusValue);
                }

                // Step 4: Add all the countries to the main list
                for (String l_country : l_readCountries) {
                    String[] l_countryLineSplit = l_country.split(" ");
                    Integer l_continentIndex = Integer.parseInt(l_countryLineSplit[2]);
                    String l_countryName = l_countryLineSplit[1];
                    Continent l_continent = getContinentById(l_continentIndex);
                    addCountry(l_countryName, l_continent.getContinentName());
                }

                // Step 5 : Add Neighbour country
                for (String l_border : l_readNeighbours) {
                    String[] l_borderLineSplit = l_border.split(" ");
                    Integer l_countryId = Integer.parseInt(l_borderLineSplit[0]);
                    Country l_mainCountry = getCountryById(l_countryId);
                    for (int i = 1; i < l_borderLineSplit.length; i++) {
                        Integer l_neighbourCountryId = Integer.parseInt(l_borderLineSplit[i]);
                        Country l_neighbourCountry = getCountryById(l_neighbourCountryId);
                        addNeighbour(l_mainCountry.getCountryName(), l_neighbourCountry.getCountryName());
                    }
                }

                if(validateMap()) {
                    d_isMapLoaded = true;
                } else {
                    d_isMapLoaded = false;
                }
                GameController.log("Map::loadmap", LogLevel.BASICLOG,"Map is loaded successfully.");
            } catch(Exception ex){
                d_isMapLoaded = false;
                GameController.log("Map::loadmap", LogLevel.BASICLOG,"Map is not loaded successfully.");
                throw new MapInvalidException("Invalid Map Provided.");
            }

    }

    public void editMap(String p_fileName) throws IOException , MapInvalidException {
        File l_newFile = new File(AllTheConstants.defaultMapLocationAppendString + p_fileName);

        if (l_newFile.createNewFile()) {
            GameController.log("Map::editMap",LogLevel.BASICLOG,p_fileName + " has been created.");
            System.out.println("File has been created.");
            this.setMapFile(p_fileName);
        } else {
            System.out.println("File already exists.");
            if(!d_isMapLoaded)
                this.loadMap(p_fileName);
            else
                System.out.println("Map is already loaded...");
        }
    }
    public void saveMap(String p_fileName) throws MapInvalidException, IOException{
        boolean l_flagValidate = false;
        if(getMapFile() == null || p_fileName == null) {
            throw new MapInvalidException("File doesn't exist.");
        }

        if (!getMapFile().equalsIgnoreCase(p_fileName)) {
            GameController.log("Map::saveMap",LogLevel.BASICLOG,"Kindly provide same file name to save which you have given for edit");
            throw new MapInvalidException("Kindly provide same file name to save which you have given for edit");
        } else {
            if (this.validateMap()) {
                Files.deleteIfExists(Paths.get(AllTheConstants.defaultMapLocationAppendString + p_fileName));
                FileWriter l_writer = new FileWriter(AllTheConstants.defaultMapLocationAppendString + p_fileName);

                // Write the continent data
                l_writer.write(System.lineSeparator() + AllTheConstants.continentsSeparator + System.lineSeparator());
                for (Continent l_continent : this.getAllContinentsList()) {
                    l_writer.write(
                    l_continent.getContinentName().concat(" ").concat(l_continent.getContinentId().toString())
                                            + System.lineSeparator());
                    }
                    String l_countryMetaData , l_bordersMetaData;
                    List<String> l_bordersList = new ArrayList<>();

                    // Writes Country Objects to File And Organizes Border Data for each of them
                    l_writer.write(System.lineSeparator() + AllTheConstants.countrySeparator + System.lineSeparator());
                    for (Country l_country : this.getAllCountriesAsList()) {
                        l_countryMetaData = new String();
                        l_countryMetaData = l_country.getCountryId().toString().concat(" ").concat(l_country.getCountryName())
                                .concat(" ").concat(l_country.getContinentId().toString());
                        l_writer.write(l_countryMetaData + System.lineSeparator());

                        if (null != l_country.getNeighbourCountries() && !l_country.getNeighbourCountries().isEmpty()) {
                            l_bordersMetaData = new String();
                            l_bordersMetaData = l_country.getCountryId().toString();
                            for (Integer l_adjCountry : l_country.getNeighbourCountries()) {
                                l_bordersMetaData = l_bordersMetaData.concat(" ").concat(l_adjCountry.toString());
                            }
                            l_bordersList.add(l_bordersMetaData);
                        }
                    }

                    // Writes Border data to the File
                    if (!l_bordersList.isEmpty()) {
                        l_writer.write(System.lineSeparator() + AllTheConstants.bordersSeparator + System.lineSeparator());
                        for (String l_borderStr : l_bordersList) {
                            l_writer.write(l_borderStr + System.lineSeparator());
                        }
                    }
                    GameController.log("Map::saveMap",LogLevel.BASICLOG,"Map Saved Successfully");
                    l_writer.close();
            }

        }

    }


}
