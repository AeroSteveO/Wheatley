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
    public String zip = new String();       // ALL Cache Types
    public DateTime expiration;                   // ALL Cache Types
    public String cityState = new String(); // ALL Cache Types

    public String getZip(){
        return(zip);
    }
    
    public String getCityState(){
        return cityState;
    }

    public ArrayList<String> getLocationData(){
        ArrayList<String> locationData = new ArrayList<>();
        
        locationData.add(cityState.split(",")[0].trim());
        locationData.add(cityState.split(",")[1].trim());
        locationData.add(zip);
        
        return locationData;
    }
    
    public boolean isAfterExpiration(){
        if (new DateTime().isAfter(expiration)){
            return(true);
        }
        return(false);
    }
    
    public boolean compareLocation(ArrayList<String> locationStrings){
        return (locationStrings.get(2).equalsIgnoreCase(zip)||(locationStrings.get(0).equalsIgnoreCase(cityState.split(",")[0].trim())&&locationStrings.get(1).equalsIgnoreCase(cityState.split(",")[1].trim())));
    }
    
    public boolean containsError(){
        return (cityState.toLowerCase().contains("error")||zip.toLowerCase().contains("error"));
    }
}
