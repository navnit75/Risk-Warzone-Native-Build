package org.Model;

import org.Controller.GameController;
import org.Controller.Main;
import org.Exceptions.MapInvalidException;
import org.Utils.LogLevel;

import java.util.ArrayList;
import java.util.List;


public class Continent {
    private Integer d_continentId;
    private String d_continentName;
    private Integer d_continentBonusValue;
    private List<Country> d_countries;

    public Continent(){}

    public Continent(Integer p_continentId, String p_continentName, Integer p_continentBonusValue){
        this.d_continentId = p_continentId;
        this.d_continentName = p_continentName;
        this.d_continentBonusValue = p_continentBonusValue;
    }

    public Integer getContinentId(){return this.d_continentId; }
    public String getContinentName(){return this.d_continentName; }
    public Integer getContinentBonusValue(){return this.d_continentBonusValue;}
    public List<Country> getCountries(){return this.d_countries; }

    public void setContinentId(Integer p_continentId){this.d_continentId = p_continentId; }
    public void setContinentName(String p_continentName){this.d_continentName = p_continentName; }
    public void setContinentBonusValue(Integer p_continentBonusValue){this.d_continentBonusValue = p_continentBonusValue; }

    public void addCountry(Country p_country){
        if(d_countries == null){
            d_countries = new ArrayList<>();
        }
        if(!d_countries.contains(p_country)){
            this.d_countries.add(p_country);
        } else {
            GameController.log("Continent::addCountry", LogLevel.BASICLOG,this.getContinentId() + " already contains " + p_country.getCountryName());
        }
    }

    public void removeCountry(Country p_country) throws MapInvalidException {
        if(d_countries == null){
            GameController.log("Continent::removeCountry", LogLevel.BASICLOG, "Continents have no countries.");
            throw new MapInvalidException("Continents have no countries." + System.lineSeparator());
        }
        if(d_countries.contains(p_country)){
            this.d_countries.remove(p_country);
        } else {
            GameController.log("Continent::removeCountry", LogLevel.BASICLOG,p_country.getCountryName() + " is not a part of " + this.getContinentName());
            System.out.println(p_country.getCountryName() + " is not a part of " + this.getContinentName());
        }
    }

    public boolean hasCountry(Country p_country){
        boolean l_returnValue = false;
        if(!d_countries.isEmpty() && d_countries.contains(p_country)){
            l_returnValue = true;
        }
        return l_returnValue;
    }

}
