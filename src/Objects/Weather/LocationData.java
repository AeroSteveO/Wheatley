/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Objects.Weather;

import java.util.ArrayList;

/**
 *
 * @author Stephen
 */
public class LocationData {
    private final String city;
    private final String state;
    private final String zip;
    
    public LocationData(String zip, String city, String state) {
        this.zip = zip;
        this.city = city;
        this.state = state;
    }

    public String getCity() {
        return city;
    }
    
    public String getCityForURL() {
        return city.replaceAll(" ", "_");
    }

    public String getState() {
        return state;
    }
    
    public String getZip() {
        return zip;
    }
    
    public ArrayList<String> toArrayList() {
                ArrayList<String> locationData = new ArrayList<>();
        
        locationData.add(city);
        locationData.add(state);
        locationData.add(zip);
        
        return locationData;

    }
    
    public boolean equals(LocationData loc) {
        return (loc.city.equalsIgnoreCase(this.city) && loc.state.equalsIgnoreCase(this.state) && loc.zip.equals(this.zip));
    }
    
    public boolean equalsCityOrZip(LocationData loc) {
        return ((loc.city.equalsIgnoreCase(this.city) && loc.state.equalsIgnoreCase(this.state)) || loc.zip.equals(this.zip));
    }
    
    public String toString() {
        return (city + ", " + state);
    }
}
