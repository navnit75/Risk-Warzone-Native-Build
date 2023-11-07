package org.Model;
import org.Controller.GameController;
import org.Controller.Main;
import org.Exceptions.MapInvalidException;
import org.Utils.LogLevel;

import java.util.List;
import java.util.ArrayList;

public class Country {
    private Integer d_armies;
    private Integer d_countryId;
    private Integer d_continentId;
    private String d_countryName;
    private List<Integer> d_neighbourCountries;

    public Country(Integer p_countryId, String p_countryName, int p_continentId){
        this.d_countryId = p_countryId;
        this.d_countryName = p_countryName;
        this.d_continentId = p_continentId;
        this.d_armies = 0;
    }

    public Integer getArmies(){return this.d_armies; }
    public Integer getCountryId(){return this.d_countryId; }
    public Integer getContinentId(){return this.d_continentId; }
    public String getCountryName(){return this.d_countryName; }
    public List<Integer> getNeighbourCountries(){return this.d_neighbourCountries; }

    public void setArmies(Integer p_armies){ this.d_armies = p_armies; }
    public void setCountryId(Integer p_countryId){ this.d_countryId = p_countryId; }
    public void setContinentId(Integer p_continentId){ this.d_continentId = p_continentId; }
    public void setCountryName(String p_continentName){ this.d_countryName = p_continentName; }
    public void addNeighbour(Integer p_countryId){
        if(d_neighbourCountries == null)
            d_neighbourCountries = new ArrayList<>();
        if(!d_neighbourCountries.contains(p_countryId)){
            d_neighbourCountries.add(p_countryId);
        } else{
            GameController.log("Country::addNeighbour", LogLevel.BASICLOG,p_countryId +
                    " is already neighbour of " + this.getCountryName());
        }
    }
    public void removeNeighbour(Integer p_countryId) throws MapInvalidException{
        if(d_neighbourCountries.contains(p_countryId)){
            d_neighbourCountries.remove(p_countryId);
        } else {
            GameController.log("Country::removeNeighbour",LogLevel.BASICLOG,p_countryId + "  is not a neighbour of " + this.getCountryName());
            throw new MapInvalidException("Neighbour doesn't exist");
        }
    }
    public boolean hasNeighbour(Country p_country){
        return !d_neighbourCountries.isEmpty() && d_neighbourCountries.contains(p_country.getCountryId());
    }
}
