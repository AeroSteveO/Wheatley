/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rapternet.irc.bots.thetardis.objects.weather;

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
       
    @Override
    public String toString() {
        return (city + ", " + state);
    }
}
