package org.Views;

import java.util.List;

import org.davidmoten.text.utils.WordWrap;
import org.Constants.AllTheConstants;
import org.Exceptions.*;
import org.Model.Continent;
import org.Model.Country;
import org.Model.GameState;
import org.Model.Map;
import org.Model.Player;

/**
 * This is the MapView Class.
 */
public class MapView {
    List<Player> d_players;
    GameState d_gameState;
    Map d_map;
    List<Country> d_countries;
    List<Continent> d_continents;

    /**
     * Reset Color ANSI Code.
     */
    public static final String ANSI_RESET = "\u001B[0m";

    /**
     * Constructor to initialise MapView.
     *
     * @param p_gameState Current GameState.
     */
    public MapView(GameState p_gameState){
        d_gameState = p_gameState;
        d_map = p_gameState.getCurrentMap();
        d_players = p_gameState.getPlayerController().getAllPlayers();
        d_countries = d_map.getAllCountriesAsList();
        d_continents = d_map.getAllContinentsList();
    }

    /**
     * Returns the Colored String.
     *
     * @param p_color Color to be changed to.
     * @param p_stringToBePrinted String to be changed color of.
     * @return colored string.
     */
    private String getColorizedString(String p_color, String p_stringToBePrinted) {
        if(p_color == null) return p_stringToBePrinted;
        return p_color + p_stringToBePrinted + ANSI_RESET;
    }

    /**
     * Renders the Center String for Heading.
     *
     * @param p_width Defined width in formatting.
     * @param p_s String to be rendered.
     */
    private void renderCenteredString (int p_width, String p_s) {
        String l_centeredString = String.format("%-" + p_width  + "s", String.format("%" + (p_s.length() +
                (p_width - p_s.length()) / 2) + "s", p_s));
        System.out.format(l_centeredString+"\n");
    }

    /**
     * Renders the Separator for heading.
     *
     */
    private void renderSeparator(){
        StringBuilder l_separator = new StringBuilder();

        l_separator.append("*".repeat(AllTheConstants.CONSOLE_WIDTH));
        System.out.format("*%s*%n", l_separator);
    }

    /**
     * Renders the continent Name with formatted centered string and separator.
     *
     * @param p_continentName Continent Name to be rendered.
     */
    private void renderContinentName(String p_continentName){
        String l_continentName = p_continentName+" ( "+"BONUS VALUE"+" : "+
                d_gameState.getCurrentMap().getContinentByName(p_continentName).getContinentBonusValue()+" )";

        renderSeparator();
        if(d_players != null){
            l_continentName = getColorizedString(getContinentColor(p_continentName), l_continentName);
        }
        renderCenteredString(AllTheConstants.CONSOLE_WIDTH, l_continentName);
        renderSeparator();
    }

    /**
     * Renders the Country Name as Formatted.
     *
     * @param p_index Index of Countries.
     * @param p_countryName Country Name to be rendered.
     * @return Returns the Formatted String
     */
    private String getFormattedCountryName(int p_index, String p_countryName){
        String l_indexedString = String.format("%02d. %s", p_index, p_countryName);

        if(d_players != null){
            String l_armies = "( "+"Armies"+" : "+ getCountryArmies(p_countryName)+" )";
            l_indexedString = String.format("%02d. %s %s", p_index, p_countryName, l_armies);
        }
        return getColorizedString(getCountryColor(p_countryName), String.format("%-30s", l_indexedString));
    }

    /**
     * Renders Adjacent Countries in Formatted Settings.
     *
     * @param p_countryName Country Name to be rendered.
     * @param p_adjCountries List of adjacent countries to be rendered.
     */
    private void renderFormattedNeighbourCountryName(String p_countryName, List<Country> p_adjCountries){
        StringBuilder l_commaSeparatedCountries = new StringBuilder();

        for(int i=0; i<p_adjCountries.size(); i++) {
            l_commaSeparatedCountries.append(p_adjCountries.get(i).getCountryName());
            if(i<p_adjCountries.size()-1)
                l_commaSeparatedCountries.append(", ");
        }
        String l_adjacentCountry = "Neighbours"+" : "+ WordWrap.from(l_commaSeparatedCountries.toString()).maxWidth(AllTheConstants.CONSOLE_WIDTH).wrap();
        System.out.println(getColorizedString(getCountryColor(p_countryName),l_adjacentCountry));
        System.out.println();
    }

    /**
     * Method that renders the number of cards owned by the player.
     *
     * @param p_player Player Instance
     */
    private void renderCardsOwnedByPlayers(Player p_player){
        StringBuilder l_cards = new StringBuilder();

        for(int i=0; i<p_player.getAllCards().size(); i++) {
            l_cards.append(p_player.getAllCards().get(i));
            if(i<p_player.getAllCards().size()-1)
                l_cards.append(", ");
        }

        String l_cardsOwnedByPlayer = "Cards Owned : "+ WordWrap.from(l_cards.toString()).maxWidth(AllTheConstants.CONSOLE_WIDTH).wrap();
        System.out.println(getColorizedString(p_player.getColor(),l_cardsOwnedByPlayer));
        System.out.println();
    }

    /**
     * Gets the Color of Country based on Player.
     *
     * @param p_countryName Country Name to be rendered.
     * @return Color of Country.
     */
    private String getCountryColor(String p_countryName){
        if(getCountryOwner(p_countryName) != null){
            return getCountryOwner(p_countryName).getColor();
        }else{
            return null;
        }
    }

    /**
     * Gets the Color of continent based on Player.
     *
     * @param p_continentName Continent Name to be rendered.
     * @return Color of continent.
     */
    private String getContinentColor(String p_continentName){
        if(getContinentOwner(p_continentName) != null){
            return getContinentOwner(p_continentName).getColor();
        }else{
            return null;
        }
    }

    /**
     * Gets the player who owns the country.
     *
     * @param p_countryName Name of country
     * @return the player object
     */
    private Player getCountryOwner(String p_countryName){
        if (d_players != null) {
            for (Player p: d_players){
                if(p.getCountryCaptured().contains(d_map.getCountryByName(p_countryName))){
                    return p;
                }
            }
        }
        return null;
    }

    /**
     * Renders the Player Information in formatted settings.
     *
     * @param p_index Index of the Player
     * @param p_player Player Object
     */
    private void renderPlayerInfo(Integer p_index, Player p_player){
        String l_playerInfo = String.format("%02d. %s : %s / %s= %d", p_index,"PLAYER NAME",p_player.getPlayerName(),
                "ARMIES AVAILABLE ", p_player.getNumOfArmiesRemaining());

        System.out.println(getColorizedString(p_player.getColor(),l_playerInfo));
    }

    /**
     * Renders the Players in Formatted Settings.
     *
     */
    private void renderPlayers(){
        int l_counter = 0;

        renderSeparator();
        renderCenteredString(AllTheConstants.CONSOLE_WIDTH, "GAME PLAYERS");
        renderSeparator();

        for(Player p: d_players){
            if(p.getPlayerName().equalsIgnoreCase("Neutral"))
                continue;
            l_counter++;
            renderPlayerInfo(l_counter, p);
            renderCardsOwnedByPlayers(p);
        }
    }

    /**
     * Gets the continent owner.
     *
     * @param p_continentName continent name
     * @return player object
     */
    private Player getContinentOwner(String p_continentName){
        if (d_players != null) {
            for (Player p: d_players){
                if(p.getContinentOwned() != null && p.getContinentOwned().contains(d_map.getContinentByName(p_continentName))){
                    return p;
                }
            }
        }
        return null;
    }

    /**
     * Gets the number of armies for a country.
     *
     * @param p_countryName name of the country
     * @return number of armies
     */
    private Integer getCountryArmies(String p_countryName){
        Integer l_armies = d_gameState.getCurrentMap().getCountryByName(p_countryName).getArmies();
        if(l_armies == null)
            return 0;
        return l_armies;
    }

    /**
     * Returns Unallocated Player Armies.
     *
     * @param p_player Player Object.
     * @return String to fit with Player.
     */
    private String getPlayerArmies(Player p_player){
        return "(Armies Available: " + p_player.getNumOfArmiesRemaining()+")";
    }

    /**
     * This method displays the list of continents and countries present in the .map files alongside current state of the game.
     */
    public void showMap() {

        if(d_players != null){
            renderPlayers();
        }

        if (d_continents != null) {
            for(Continent l_continent : d_continents ) {
                renderContinentName(l_continent.getContinentName());
                List<Country> l_countries = l_continent.getCountries();
                final int[] l_countryIndex = {1};
                if (!l_countries.isEmpty()) {
                    for(Country l_country : l_countries) {
                        String l_formattedCountryName = getFormattedCountryName(l_countryIndex[0]++, l_country.getCountryName());
                        System.out.println(l_formattedCountryName);
                        try {
                            List<Country> l_neighbourCountries = d_map.getNeighbourCountries(l_country);
                            renderFormattedNeighbourCountryName(l_country.getCountryName(), l_neighbourCountries);
                        } catch (MapInvalidException l_invalidMap) {
                            System.out.println(l_invalidMap.getMessage());
                        }
                    }
                } else {
                    System.out.println("No countries are present in the continent!");
                }
            }
        } else {
            System.out.println("No continents to display!");
        }
    }

    /**
     * This method displays the colored string of the countries assigned to each player
     */
    public void showPlayerBasedCountries(){
        if(d_players != null){
            for(Player l_player : d_players){
                renderSeparator();
                renderCenteredString(AllTheConstants.CONSOLE_WIDTH,getColorizedString(l_player.getColor(), "Countries Assigned to " + l_player.getPlayerName()));
                renderSeparator();
                int l_countryIndex = 1;
                for(Country l_country : l_player.getCountryCaptured()){
                    String l_toPrint = String.format("%02d. %s\n",l_countryIndex++,l_country.getCountryName());
                    System.out.print(getColorizedString(l_player.getColor(),l_toPrint ));
                }
            }
        }
    }
}

