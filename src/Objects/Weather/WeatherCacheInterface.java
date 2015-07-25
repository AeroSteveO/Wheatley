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
 * 
 */
public interface WeatherCacheInterface {
    
    public String getFormattedResponse();
    
    public boolean isAfterExpiration();
    
    public ArrayList<String>  getExtendedResponseArray();
    
    public boolean compareLocation(LocationData data);
    
    public boolean containsError();
    
    public String getZip();
    
    public String getCityState();
    
    public WeatherType getType();
    
    public LocationData getLocationData();
}
