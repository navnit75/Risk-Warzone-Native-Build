package org.Model;

import org.Constants.AllTheConstants;
import org.Controller.GameEngine;
import org.Exceptions.MapInvalidException;
import org.Utils.LogLevel;
import org.davidmoten.text.utils.WordWrap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * This class is used to Manage a Map
 */
public class Map {

    /**
     * Static flag to show if any of the map is loaded or not
     */
    public static Boolean d_isMapLoaded = false;

    /**
     * List keeping all the countries read from the map
     */
    private List<Country> d_allCountriesAsList;

    /**
     * List keeping all the continents read from the map
     */
    private List<Continent> d_allContinentsAsList;

    /**
     * List keeping the map file from which map is loaded.
     */
    private String d_mapFile;

    /**
     * String for keeping the track of map editing file
     */
    private String d_mapEditFile = null;


    /**
     * Gets the files from which map is loaded
     * @return String : name of the file
     */
    public String getMapFile(){ return this.d_mapFile; }

    /**
     * Set the map file from which map should be loaded
     * @param l_mapFile
     */
    public void setMapFile(String l_mapFile){this.d_mapFile = l_mapFile;}

    /**
     * Getter for the country list
     * @return List of countries
     */
    public List<Continent> getAllContinentsList(){ return this.d_allContinentsAsList; }

    /**
     * Getter for the Continent list
     * @return List of Continent
     */
    public List<Country> getAllCountriesAsList(){ return this.d_allCountriesAsList; }

    /**
     * Helper function to find the Continent object by name
     * @return Continent object if not found null is returned
     */
    public Continent getContinentByName(String p_continentName){
        return d_allContinentsAsList
                .stream()
                .filter(l_continent->l_continent.getContinentName()
                        .equalsIgnoreCase(p_continentName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Helper function to find the Continent by id
     * @param p_continentId : Id of the continent
     * @return Continent object with the respective id or null is returned
     */
    public Continent getContinentById(Integer p_continentId){
        return d_allContinentsAsList
                .stream()
                .filter(l_continent-> Objects.equals(l_continent.getContinentId(), p_continentId)).
                findFirst().
                orElse(null);

    }

    /**
     * Helper function to get the country from list of country
     * @param p_countryName : Name of the country
     * @return Country object with respective Name
     */
    public Country getCountryByName(String p_countryName){
        return d_allCountriesAsList
                .stream()
                .filter(l_country->l_country.getCountryName()
                        .equalsIgnoreCase(p_countryName))
                .findFirst()
                .orElse(null);
    }


    /**
     * Helper function to get the country from list of country
     * @param p_countryId : Id of the country
     * @return Country object with respective Id
     */
    public Country getCountryById(Integer p_countryId){
        return d_allCountriesAsList
                .stream()
                .filter(l_country-> Objects.equals(l_country.getCountryId(), p_countryId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Helper function to add Provided continent object to list
     * @param p_continent : Continent object
     */
    public void addContinent(Continent p_continent){
        if(d_allContinentsAsList == null){
            d_allContinentsAsList = new ArrayList<>();
        }
        d_allContinentsAsList.add(p_continent);
    }

    /**
     * Helper function to add continent to main list
     * @param p_continentName : Name of the continent
     * @param p_bonusValue: Bonus value provide to continent
     */
    public void addContinent(String p_continentName, Integer p_bonusValue){
        if(d_allContinentsAsList != null){
            int l_continentId = Math.max(1,d_allContinentsAsList.size() + 1);
            if(getContinentByName(p_continentName) ==  null){
                addContinent(new Continent(l_continentId, p_continentName, p_bonusValue));
                GameEngine.log("Map::addContinent", LogLevel.BASICLOG,p_continentName + " added to the GameState.");
            } else {
                GameEngine.log("Map::addContinent", LogLevel.BASICLOG, p_continentName + " already exists in the map.");
                System.out.println("Continent " + p_continentName + " is already there in the map\nHence is not added.!!");
            }
        } else {
            d_allContinentsAsList = new ArrayList<>();
            d_allContinentsAsList.add(new Continent(1,p_continentName,p_bonusValue));
        }
    }

    /**
     * Helper function to add country to the list of countries
     * @param p_countryName : Name of the country
     * @param p_continentName : Name of the continent to which country should be added
     * @throws MapInvalidException : Throws if invalid name of the continent is provided.
     */
    public void addCountry(String p_countryName, String p_continentName) throws MapInvalidException{
        if(getContinentByName(p_continentName) == null){
            throw new MapInvalidException(p_continentName + " doesn't exist.\nError in adding of country " + p_countryName);
        }

        Continent l_continent = getContinentByName(p_continentName);
        if(d_allCountriesAsList != null){
            int l_countryId = Math.max(1,d_allCountriesAsList.size() + 1);
            if(getCountryByName(p_countryName) == null){
                Country l_newCountry = new Country(l_countryId,p_countryName,l_continent.getContinentId());
                d_allCountriesAsList.add(l_newCountry);
                l_continent.addCountry(l_newCountry);
                GameEngine.log("Map::addCountry",LogLevel.BASICLOG, p_countryName + " added to the GameState");
            } else {
                GameEngine.log("Map::addCountry",LogLevel.BASICLOG,p_countryName + " already exist in the map");
                System.out.println("Country " + p_countryName + " already there in the map.\nHence country not added.!!");
            }
        } else {
            d_allCountriesAsList = new ArrayList<>();
            Country l_newCountry = new Country(1,p_countryName,l_continent.getContinentId());
            d_allCountriesAsList.add(l_newCountry);
            l_continent.addCountry(l_newCountry);
            GameEngine.log("Map::addCountry",LogLevel.BASICLOG,p_countryName + " added to the GameState");
        }
    }

    /**
     * Helper function to add neighbours
     * @param p_mainCountry : The main country to which neighbour should be added
     * @param p_neighbourCountry : The neighbour country
     * @throws MapInvalidException : Any kind of errors in the parameters provided
     */
    public void addNeighbour(String p_mainCountry , String p_neighbourCountry) throws MapInvalidException{
        if(d_allCountriesAsList != null){
            boolean l_ifBothCountryExist = (getCountryByName(p_mainCountry) != null ) && (getCountryByName(p_neighbourCountry) != null);
            if(l_ifBothCountryExist){
                getCountryByName(p_mainCountry).addNeighbour(getCountryByName(p_neighbourCountry).getCountryId());
                getCountryByName(p_neighbourCountry).addNeighbour(getCountryByName(p_mainCountry).getCountryId());
                GameEngine.log("Map::addNeighbour",LogLevel.BASICLOG,p_neighbourCountry + " is added as neighbour of " + p_mainCountry);
            } else {
                GameEngine.log("Map::addNeighbour",LogLevel.BASICLOG,"Adding neighbour failed for (" + p_mainCountry + "," +
                        p_neighbourCountry + ").");
                throw new MapInvalidException("Countries provided doesn't not exist");
            }
        }
    }

    /**
     * Helper method to help remove the country from its neighbours
     * @param p_countryToBeRemoved : Country which has to be removed from neighbours of all the country
     */
    private void removeCountryFromNeighbourOfAllCountry(Country p_countryToBeRemoved){
        for(Integer l_countryId : p_countryToBeRemoved.getNeighbourCountries()){
            getCountryById(l_countryId).removeNeighbour(p_countryToBeRemoved.getCountryId());
            GameEngine.log("Map::removeCountryFromNeighbourOfAllCountry",LogLevel.BASICLOG,p_countryToBeRemoved.getCountryName()
                    + " removed from neighbour of " + getCountryById(l_countryId));

        }
    }

    /**
     * Helper function to remove a country from the map
     * @param p_countryName : Name of the country to be removed
     * @throws MapInvalidException : In case of any error conditions
     */
    public void removeCountry(String p_countryName) throws MapInvalidException{
        Country l_countryToBeRemoved = getCountryByName(p_countryName);
        if(getCountryByName(p_countryName) != null){

            // Removed from the continent , neighbours still not removed
            Continent l_tempContinent = getContinentById(l_countryToBeRemoved.getContinentId());
            l_tempContinent.removeCountry(l_countryToBeRemoved);
            //Remove the country from the neighbourList of all the countries
            removeCountryFromNeighbourOfAllCountry(l_countryToBeRemoved);
            // Removed the country from the main list , still not removed from neighbours
            d_allCountriesAsList.remove(l_countryToBeRemoved);
            GameEngine.log("Map::removeCountry",LogLevel.BASICLOG,p_countryName + " has been removed. " );
        } else {
            GameEngine.log("Map::removeCountry",LogLevel.BASICLOG,"Country " + p_countryName + "doesn't exist, for removal.");
            throw new MapInvalidException("Country " + p_countryName + "doesn't exist, for removal.");
        }
    }

    /**
     * Helper method to remove continent
     * @param p_continentName : Name of the continent
     * @throws MapInvalidException : In case of any error conditions
     */
    public void removeContinent(String p_continentName) throws MapInvalidException{
        Continent l_continentToBeRemoved = getContinentByName(p_continentName);
        if(l_continentToBeRemoved != null){
            ArrayList<Country> l_tempList = new ArrayList<>(l_continentToBeRemoved.getCountries());

            for(Country l_country : l_tempList){
                removeCountry(l_country.getCountryName());
            }
            d_allContinentsAsList.remove(l_continentToBeRemoved);
            GameEngine.log("Map::removeContinent",LogLevel.BASICLOG,"Continent " + p_continentName + " removed successfully.");
        } else {
            GameEngine.log("Map::removeContinent",LogLevel.BASICLOG,"Continent " + p_continentName + " doesn't exist.");
            throw new MapInvalidException(p_continentName + " doesn't exist.");
        }
    }

    /**
     * Helper function to remove the neighbours
     * @param p_mainCountry : Main country
     * @param p_neighbourCountry : Neighbour country which has to be removed from Main Country list of neighbours
     * @throws MapInvalidException : Thrown in case of invalidation with parameters
     */
    public void removeNeighbour(String p_mainCountry, String p_neighbourCountry) throws MapInvalidException{
        if(d_allContinentsAsList != null){
            boolean l_ifBothCountryExist = (getCountryByName(p_mainCountry) != null ) && (getCountryByName(p_neighbourCountry) != null);
            if(l_ifBothCountryExist){
                getCountryByName(p_mainCountry).removeNeighbour(getCountryByName(p_neighbourCountry).getCountryId());
                getCountryByName(p_neighbourCountry).removeNeighbour(getCountryByName(p_mainCountry).getCountryId());
                GameEngine.log("Map::removeNeighbour",LogLevel.BASICLOG,"Neighbour pairs (" + p_mainCountry + "," + p_neighbourCountry + "). removed");
            }
            else {
                GameEngine.log("Map::removeNeighbour",LogLevel.BASICLOG,"Neighbour pairs (" + p_mainCountry + "," + p_neighbourCountry + "). removal " +
                        "unsuccessful");
                throw new MapInvalidException("Neighbour doesn't exist.");
            }
        }
    }

    /**
     * Helper function to check if the map is valid
     * @return Boolean : True if map is valid
     * @throws MapInvalidException : If map is invalid
     */
    public boolean mapSanityCheck() throws MapInvalidException{

        try {
            if (d_allContinentsAsList == null || d_allContinentsAsList.isEmpty()) {
                GameEngine.log("Map::mapSanityCheck", LogLevel.BASICLOG, " Continents Lists are Empty.");
                throw new MapInvalidException("Continents lists are empty.");
            }
            if (d_allCountriesAsList == null || d_allCountriesAsList.isEmpty()) {
                GameEngine.log("Map::mapSanityCheck", LogLevel.BASICLOG, " Countries Lists are Empty.");
                throw new MapInvalidException("Countries list is empty.");
            }
            for (Country l_country : d_allCountriesAsList) {
                if (l_country.getNeighbourCountries().isEmpty()) {
                    GameEngine.log("Map::mapSanityCheck", LogLevel.BASICLOG, l_country.getCountryName() + " has no neighbours.");
                    throw new MapInvalidException(l_country.getCountryName() + " doesn't have any neighbours.");
                }
            }
            return true;
        }
        catch(Exception ex){
            throw new MapInvalidException("Map is invalid.");
        }

    }

    /**
     * Helper function to check the reachability of the countries in a continent
     * @param p_sourceCountry : Country from which we should start checking reachability
     * @param l_tempVertices : Temporary structure to store the reachability result
     * @param p_continent : Continent in which reachability has to be checked
     */
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

    /**
     * Helper function to check the reachability of the countries in a continent
     * @param p_continent : Continent for which reachability has to be checked
     */
    private boolean countriesInContinentConnectivity(Continent p_continent){
        HashMap<Integer,Boolean> l_tempVertices = new HashMap<>();
        for(Country l_country : p_continent.getCountries()){
            l_tempVertices.put(l_country.getCountryId(),false);
        }
        dfsCountriesInContinentConnectivity(p_continent.getCountries().get(0), l_tempVertices, p_continent);
        return !l_tempVertices.containsValue(false);
    }

    /**
     * Helper function to check the Continent based connectivity
     * @return True or False : based on connectivity of the map
     * @throws MapInvalidException : if the map is not fully reachable
     */
    public boolean checkContinentBasedConnectivity() throws MapInvalidException{
        for(Continent l_continent : d_allContinentsAsList){
            if(l_continent.getCountries() == null || l_continent.getCountries().isEmpty()) {
                throw new MapInvalidException(l_continent.getContinentName() + " doesn't have any countries.");
            }
            if(!countriesInContinentConnectivity(l_continent)) {
                GameEngine.log("Map::countriesInContinentConnectivity",LogLevel.BASICLOG,"Continent Based Connectivity : " + l_continent.getContinentName() + " failed connectivity test.");
                return false;
            }
        }
        return true;
    }

    /**
     * Runs Depth first search on the list of all countries
     * @param p_country : Source country
     * @param p_tempVertices : Results of the operation
     */
    public void dfsAllCountriesConnectivity(Country p_country, HashMap<Integer,Boolean> p_tempVertices){
        p_tempVertices.put(p_country.getCountryId(),true);
        for(int i = 0 ; i < p_country.getNeighbourCountries().size(); i++){
            Country l_country = getCountryById(p_country.getNeighbourCountries().get(i));
            if(!p_tempVertices.get(l_country.getCountryId())){
                dfsAllCountriesConnectivity(l_country,p_tempVertices);
            }
        }
    }

    /**
     * Helper function to check the connectivity of the whole map
     * @return True of false
     * @throws MapInvalidException : If the map is invalid
     */
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
                GameEngine.log("Map::checkCountryBasedConnectivity",LogLevel.BASICLOG,l_logString);
                throw new MapInvalidException(l_logString);
            }
        }
        return !l_tempVertices.containsValue(false);
    }

    /**
     * Helper function for getting neighbour countries of the provided country
     * @param p_country : Country for which neighbour country has to be returned
     * @return List of country
     */
    public List<Country> getNeighbourCountries(Country p_country) {
        List<Country> l_adjCountries = new ArrayList<Country>();

        if (!p_country.getNeighbourCountries().isEmpty()) {
            for (int i : p_country.getNeighbourCountries()) {
                l_adjCountries.add(getCountryById(i));
            }
        } else {
            GameEngine.log("Map::getNeighbourCountries",LogLevel.BASICLOG,p_country.getCountryName() + " doesn't have any adjacent countries");
            System.out.println("Country " + p_country.getCountryName() + " doesn't have any neighbour countries");
        }
        return l_adjCountries;
    }

    /***
     * Helper function to check for duplicate countries in the list
     * @return True of false on the basis of the validation of the country list
     */
    public boolean checkForDuplicateCountry(){
        HashSet<String> l_tempSet = d_allCountriesAsList
                .stream()
                .map(Country::getCountryName)
                .collect(Collectors.toCollection(HashSet::new));
        return l_tempSet.size() != d_allContinentsAsList.size();
    }

    /**
     * Helper function to validate the map which has been loaded
     * @return True or False on basic of validation test
     * @throws MapInvalidException : Handling the exception from previous checks
     */
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

    /**
     * Helper function to print separators
     */
    private void renderSeparator(){
        System.out.println(AllTheConstants.consoleSepartorString.repeat(AllTheConstants.CONSOLE_WIDTH));
    }

    private void renderCenteredString(int p_width, String p_s) {
        String l_centeredString = String.format("%-" + p_width  + "s", String.format("%" + (p_s.length() +
                (p_width - p_s.length()) / 2) + "s", p_s));
        System.out.format(l_centeredString+"\n");
    }

    /**
     * Helper function to show the map data
     */
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

    /**
     * Helper function to load map data
     * @param p_fileName : Name of the file from which map should be loaded
     * @throws MapInvalidException : error conditions if the map being loaded is invalid
     */
    public void loadMap(String p_fileName) throws MapInvalidException{
        try {
            List<String> l_fileLineByLine = new ArrayList<>();

            // Step 1: Read the file into common List
            File myObj = new File(AllTheConstants.defaultMapLocationAppendString + p_fileName);
            Scanner l_scanner = new Scanner(myObj);


            this.setMapFile(p_fileName);

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
            GameEngine.log("Map::loadmap", LogLevel.BASICLOG,"Map is loaded successfully.");
        } catch(Exception ex){
            d_isMapLoaded = false;
            GameEngine.log("Map::loadmap", LogLevel.BASICLOG,"Map is not loaded successfully.");
            throw new MapInvalidException("Invalid Map Provided.");
        }

    }

    /**
     * Helper function to facilitate the editmap feature of the game.
     * @param p_fileName : File name which needs to be created in case of user enters edit map
     * @throws IOException : If any error occurs while creating files
     * @throws MapInvalidException : If the map is invalid
     */
    public void editMap(String p_fileName) throws IOException , MapInvalidException {
        File l_newFile = new File(AllTheConstants.defaultMapLocationAppendString + p_fileName);

        if (l_newFile.createNewFile()) {
            GameEngine.log("Map::editMap",LogLevel.BASICLOG,p_fileName + " has been created.");
            System.out.println("File has been created.");
            this.d_mapEditFile = p_fileName;

        } else {
            System.out.println("File already exists.");
            if(!d_isMapLoaded)
                this.loadMap(p_fileName);
            else {
                System.out.println(this.d_mapFile + " is already loaded.");
            }
        }
    }

    /**
     * Helper function to check the validity of savemap command
     * @param p_fileNameProvidedByUser : Name of the file
     * @return True or False : if the map is valid
     * @throws MapInvalidException : If the map is invalid
     */
    private Boolean saveMapValidityCheck(String p_fileNameProvidedByUser) throws MapInvalidException{
        // Check if the player has already loaded some map
        if(d_isMapLoaded && this.d_mapFile != null) {
            return true;
        }
        // Second condition if player has started with editmap
        else if(d_mapEditFile != null && d_mapEditFile.equalsIgnoreCase(p_fileNameProvidedByUser)){
            return true;
        }
        else {
            GameEngine.log("Map::saveMapValidityCheck",LogLevel.BASICLOG, """
                    Invalid SaveMap operation.\s
                    Either use loadmap to load a Map then use savemap.\s
                    Or use the editmap operation to create new map.
                    """);

            throw new MapInvalidException("""
                    Invalid SaveMap operation.\s
                    Either use loadmap to load a Map then use savemap.\s
                    Or use the editmap operation to create new map.
                    """);

        }
    }

    /**
     * Helper function to provide savemap feature to the users
     * @param p_fileName ; Filename to which map needs to be saved
     * @throws MapInvalidException : If the map loaded is invalid
     * @throws IOException : if the file doesn't exist
     */
    public void saveMap(String p_fileName) throws MapInvalidException, IOException{
        boolean l_flagValidate = false;
        if(p_fileName == null) {
            throw new MapInvalidException("Invalid FileName provided");
        }
        if(saveMapValidityCheck(p_fileName)){
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
                GameEngine.log("Map::saveMap",LogLevel.BASICLOG,"Map Saved Successfully");
                l_writer.close();
            }

        }

    }


}
