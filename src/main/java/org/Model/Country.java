package org.Model;
import org.Controller.GameEngine;
import org.Exceptions.MapInvalidException;
import org.Utils.LogLevel;
import java.util.List;
import java.util.ArrayList;

/**
 * Country model class, which contains features and data members related to a country.
 */
public class Country {
    /**
     * Current number of armies in the country
     */
    private Integer d_armies;

    /**
     * Id of the country
     */
    private Integer d_countryId;

    /**
     * Id of the continent it lies in
     */
    private Integer d_continentId;

    /**
     * Name of the country
     */
    private String d_countryName;

    /**
     * List of the neighbour country
     */
    private List<Integer> d_neighbourCountries;

    /**
     * Parameterized Constructor
     * @param p_countryId : Id of the country
     * @param p_countryName : Name of the country
     * @param p_continentId : Continent to which country belongs to
     */
    public Country(Integer p_countryId, String p_countryName, int p_continentId){
        this.d_countryId = p_countryId;
        this.d_countryName = p_countryName;
        this.d_continentId = p_continentId;
        this.d_armies = 0;
    }

    /**
     * getter for getting the armies
     * @return Integer
     */
    public Integer getArmies(){return this.d_armies; }

    /**
     * Getter for the country id
     * @return Integer
     */
    public Integer getCountryId(){return this.d_countryId; }

    /**
     * Getter for the continent id
     * @return Integer
     */
    public Integer getContinentId(){return this.d_continentId; }

    /**
     * Getter for the country name
     * @return String : Name of the country
     */
    public String getCountryName(){return this.d_countryName; }

    /**
     * Getter for getting the list associated with neighbour country
     * @return List : of all neighbourhood countries.
     */
    public List<Integer> getNeighbourCountries(){return this.d_neighbourCountries; }

    /**
     * Set the number of armies in the country
     * @param p_armies : The number of armies to be set
     */
    public void setArmies(Integer p_armies){ this.d_armies = p_armies; }

    /**
     * Setter for the country id
     * @param p_countryId : Country id to be set
     */
    public void setCountryId(Integer p_countryId){ this.d_countryId = p_countryId; }

    /**
     * Setter for the continent id
     * @param p_continentId : Continent id to be set
     */
    public void setContinentId(Integer p_continentId){ this.d_continentId = p_continentId; }

    /**
     * Setter for the Country name
     * @param p_continentName : Country name
     */
    public void setCountryName(String p_continentName){ this.d_countryName = p_continentName; }

    /**
     * We can add another country as neighbour
     * @param p_countryId : Country ID to be added as neighbour
     */
    public void addNeighbour(Integer p_countryId){
        if(d_neighbourCountries == null)
            d_neighbourCountries = new ArrayList<>();
        if(!d_neighbourCountries.contains(p_countryId)){
            d_neighbourCountries.add(p_countryId);
        } else{
            GameEngine.log("Country::addNeighbour", LogLevel.BASICLOG,p_countryId +
                    " is already neighbour of " + this.getCountryName());
        }
    }

    /**
     * Method to remove country as neighbour of the current country by providing the id of the country to be removed.
     * @param p_countryId : Country id to be removed
     * @throws MapInvalidException : In case the country doesn't exist
     */
    public void removeNeighbour(Integer p_countryId) throws MapInvalidException{
        if(d_neighbourCountries.contains(p_countryId)){
            d_neighbourCountries.remove(p_countryId);
        } else {
            GameEngine.log("Country::removeNeighbour",LogLevel.BASICLOG,p_countryId + "  is not a neighbour of " + this.getCountryName());
            throw new MapInvalidException("Neighbour doesn't exist");
        }
    }

    /**
     * Check if the following country has given country as neighbour
     * @param p_country : Country to be checked for neighbour
     * @return True or False : if the following country is its neighbour
     */
    public boolean hasNeighbour(Country p_country){
        return !d_neighbourCountries.isEmpty() && d_neighbourCountries.contains(p_country.getCountryId());
    }
}
