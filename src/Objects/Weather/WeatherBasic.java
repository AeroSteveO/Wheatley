/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Objects;

import java.util.ArrayList;
import org.joda.time.DateTime;

/**
 *
 * @author Stephen
 */
public abstract class WeatherBasic {
    public String zip = new String();       // Zip code of the city
    public DateTime expiration;             // Expiration time of the cache entry
    public String cityState = new String(); // City, State (two letter state)

    public String getZip(){
        return(zip);
    }
    
    public String getCityState(){
        return cityState;
    }

    // Creates an array containing the city, state, and zip all as separate entries
    public ArrayList<String> getLocationData(){
        ArrayList<String> locationData = new ArrayList<>();
        
        locationData.add(cityState.split(",")[0].trim());
        locationData.add(cityState.split(",")[1].trim());
        locationData.add(zip);
        
        return locationData;
    }
    
    // Returns if the cache entry is after its expiration time
    public boolean isAfterExpiration(){
        if (new DateTime().isAfter(expiration)){
            return(true);
        }
        return(false);
    }
    
    public boolean compareLocation(ArrayList<String> locationStrings){
        return (locationStrings.get(2).equalsIgnoreCase(zip)||(locationStrings.get(0).equalsIgnoreCase(cityState.split(",")[0].trim())&&locationStrings.get(1).equalsIgnoreCase(cityState.split(",")[1].trim())));
    }
    
    // Returns true if either part of the city state string contains an error
    public boolean containsError(){
        return (cityState.toLowerCase().contains("error")||zip.toLowerCase().contains("error"));
    }
}
