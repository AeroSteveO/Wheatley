/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Objects.Weather;

import java.util.ArrayList;
import org.joda.time.DateTime;

/**
 *
 * @author Stephen
 */
public abstract class WeatherBasic {
    public DateTime expiration;             // Expiration time of the cache entry
    private final LocationData location;
    
    public WeatherBasic(LocationData location, int minutesTillExpiration) {
        this.expiration = new DateTime().plusMinutes(minutesTillExpiration);
        this.location = location;
    }
    
    public String getZip(){
        return location.getZip();
    }
    
    public String getCityState(){
        return location.toString();
    }

    // Creates an array containing the city, state, and zip all as separate entries
    public LocationData getLocationData(){
        return location;
    }
    
    // Returns if the cache entry is after its expiration time
    public boolean isAfterExpiration(){
        if (new DateTime().isAfter(expiration)){
            return(true);
        }
        return(false);
    }
    
    public boolean compareLocation(LocationData loc) {
        return location.equalsCityOrZip(loc);
    }
    
    // Returns true if either part of the city state string contains an error
    public boolean containsError(){
        return (location.getCity().toLowerCase().contains("error") || location.getState().toLowerCase().contains("error") || location.getZip().toLowerCase().contains("error"));
    }
}
