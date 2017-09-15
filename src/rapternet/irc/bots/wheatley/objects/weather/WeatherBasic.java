/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package rapternet.irc.bots.wheatley.objects.weather;

import java.util.ArrayList;
import org.joda.time.DateTime;

/**
 *
 * @author Stephen
 * 
 * Interface:
 *      WeatherCacheInterface
 * - This interface is used by the weather cache in order to ascertain the use
 *   and information contained within entries within the cache. This interface
 *   also defines how that information is to be accessed
 * 
 * Methods:
 *     *getCityState  - Returns a string of the form "City, St" using the two letter
 *                      abbreviation of the state
 *     *getZip        - Returns the zip code of the entry
 *     *getType       - Returns the type of the weather cache entry
 *                      (forecast, conditions, alert)
 *     *getLocationData - Returns an object of type LocationData that contains
 *                        the city, state, and zip code information
 *     *containsError   - Returns true if one of the data entries in the object
 *                        contains an error
 *     *compareLocation - Returns true if the two locations are equivalent
 *     *getExtendedResponseArray - Only used in the weather alerts, returns an
 *                                 array containing the full text of the alert
 *     *isAfterExpiration    - Returns true if the cache entry is after its 
 *                             expiration time and should be purged from the cache
 *     *getFormattedResponse - Returns a pretty print format string of the
 *                             information contained in that cache entry
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
        return (location.getCity().toLowerCase().contains("error") || 
                location.getState().toLowerCase().contains("error") || 
                location.getZip().toLowerCase().contains("error"));
    }
    
    abstract public String getFormattedResponse();
    
    abstract public ArrayList<String>  getExtendedResponseArray();
    
    abstract public WeatherType getType();
}
