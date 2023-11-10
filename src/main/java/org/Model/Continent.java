package org.Model;

import org.Controller.GameEngine;
import org.Exceptions.MapInvalidException;
import org.Utils.LogLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle the features related to continent as well its data members
 */
public class Continent{
    /**
     * Id of the current continent
     */
    private Integer d_continentId;

    /**
     * Name of the current continent
     */
    private String d_continentName;

    /**
     * Bonus value provided to the player in case he captures all the country in the Continent
     */
    private Integer d_continentBonusValue;

    /**
     * All the countries in the continent
     */
    private List<Country> d_countries;

    /**
     * Default constructor
     */
    public Continent(){}

    /**
     * Parameterized constructor
     * @param p_continentId : Id of the continent
     * @param p_continentName : Name of the continent
     * @param p_continentBonusValue : Bonus value of the continent
     */
    public Continent(Integer p_continentId, String p_continentName, Integer p_continentBonusValue){
        this.d_continentId = p_continentId;
        this.d_continentName = p_continentName;
        this.d_continentBonusValue = p_continentBonusValue;
    }
    /**
     * Getter for the id of the continent
     * @return Integer
     */
    public Integer getContinentId(){return this.d_continentId; }

    /**
     * Getter for the name of the continent
     * @return String
     */
    public String getContinentName(){return this.d_continentName; }

    /**
     * Getter for the bonus value of the continent
     * @return Integer
     */
    public Integer getContinentBonusValue(){return this.d_continentBonusValue;}

    /**
     * Getter for the list of the countries in continent
     * @return List of the countries
     */
    public List<Country> getCountries(){return this.d_countries; }

    /**
     * Setter for the continent id
     * @param p_continentId : Id to be set
     */
    public void setContinentId(Integer p_continentId){this.d_continentId = p_continentId; }

    /**
     * Setter for the continent name
     * @param p_continentName : Name to be set
     */
    public void setContinentName(String p_continentName){this.d_continentName = p_continentName; }

    /**
     * Setter for the continent bonus value
     * @param p_continentBonusValue : Contains the bonus value of the continent
     */
    public void setContinentBonusValue(Integer p_continentBonusValue){this.d_continentBonusValue = p_continentBonusValue; }


    /**
     * This method helps in adding a particular country into a continent
     * @param p_country : Country to be added
     */
    public void addCountry(Country p_country){
        if(d_countries == null){
            d_countries = new ArrayList<>();
        }
        if(!d_countries.contains(p_country)){
            this.d_countries.add(p_country);
        } else {
            GameEngine.log("Continent::addCountry", LogLevel.BASICLOG,this.getContinentId() + " already contains " + p_country.getCountryName());
        }
    }

    /**
     * This method helps in removing the country from the continent
     * @param p_country : Country to be removed
     * @throws MapInvalidException : If the country to be removed doesn't exist
     */
    public void removeCountry(Country p_country) throws MapInvalidException {
        if(d_countries == null){
            GameEngine.log("Continent::removeCountry", LogLevel.BASICLOG, "Continents have no countries.");
            throw new MapInvalidException("Continents have no countries." + System.lineSeparator());
        }
        if(d_countries.contains(p_country)){
            this.d_countries.remove(p_country);
        } else {
            GameEngine.log("Continent::removeCountry", LogLevel.BASICLOG,p_country.getCountryName() + " is not a part of " + this.getContinentName());
            System.out.println(p_country.getCountryName() + " is not a part of " + this.getContinentName());
        }
    }

    /**
     * Check if the continent has particular country
     * @param p_country : Country to be checked
     * @return Boolean : if country exist true or else false
     */
    public boolean hasCountry(Country p_country){
        boolean l_returnValue = false;
        if(!d_countries.isEmpty() && d_countries.contains(p_country)){
            l_returnValue = true;
        }
        return l_returnValue;
    }

}
