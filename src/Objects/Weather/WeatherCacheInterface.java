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
public interface WeatherCacheInterface {
    
    public String getFormattedResponse();
    public String getExtendedResponse();
    public boolean isAfterExpiration();
    public ArrayList<String>  getExtendedResponseArray();
    public boolean compareLocation(ArrayList<String> locationStrings);
    public boolean containsError();
    public String getZip();
    public String getCityState();
    public String getType();
    public ArrayList<String> getLocationData();
}
