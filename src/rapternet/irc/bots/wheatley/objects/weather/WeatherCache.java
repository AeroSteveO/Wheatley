/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package rapternet.irc.bots.wheatley.objects.weather;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.pircbotx.Colors;

/**
 *
 * @author Stephen
 */
public class WeatherCache {
    private final List<WeatherBasic> cache = Collections.synchronizedList( new ArrayList<WeatherBasic>());
    
    public void add(WeatherBasic log){
        this.cache.add(log);
    }
    
    public void clear(){
        this.cache.clear();
    }

    public boolean isEmpty() {
        return (this.cache.isEmpty());
    }
    public WeatherBasic getCacheEntry(String locationString, WeatherType type){
        purge();
        
        synchronized(cache){
            for(int i = 0; i < cache.size(); i++) {
                if ((cache.get(i).getZip().equalsIgnoreCase(locationString)||cache.get(i).getCityState().equalsIgnoreCase(locationString))&&cache.get(i).getType() == (type)) {
                    return(cache.get(i));
                }
            }
            return null;
        }
    }
        
    public ArrayList<String> getFormattedAlertArray(String locationString){
        ArrayList<String> formattedAlerts = new ArrayList<>();
        List<WeatherBasic> alerts = getCacheArray(locationString, WeatherType.ALERT);
        if (alerts.size()>0){
            for (int i=0;i<alerts.size()-1;i++){
                formattedAlerts.add(alerts.get(i).getFormattedResponse());
            }
            formattedAlerts.add(alerts.get(alerts.size()-1).getFormattedResponse()+Colors.BOLD+" Type: "+Colors.NORMAL+"'!alerts full [zip]' for the full alert text");
        }
        else
            return(new ArrayList<>(Arrays.asList("Error")));
        
        return(formattedAlerts);
    }
    
    public ArrayList<String> getAllAlertsLongResponse(String locationString){
        ArrayList<String> formattedAlerts = new ArrayList<>();
        List<WeatherBasic> alerts = getCacheArray(locationString, WeatherType.ALERT);
        
        for (int i=0;i<alerts.size();i++){
            formattedAlerts.addAll(alerts.get(i).getExtendedResponseArray());
            
        }
        return(formattedAlerts);
    }
    
    public boolean addNewAlert(WeatherAlerts newAlert) {
        List<WeatherBasic> currentAlerts = getCacheArray(newAlert.getCityState(), WeatherType.ALERT);
        //(ArrayList<WeatherAlerts>)
        for (int i=0; i < currentAlerts.size(); i++) {
            if (((WeatherAlerts) currentAlerts.get(i)).getAlertType().equalsIgnoreCase(newAlert.getAlertType()) && currentAlerts.get(i).getZip().equals(newAlert.getZip())) {
                ((WeatherAlerts) currentAlerts.get(i)).updateExpiration(newAlert.getExpiration());
                return false;
            }
            else {
                cache.add(newAlert);
                return true;
            }
        }
        return false;
    }
    
    public List<WeatherBasic> getCacheArray(String locationString, WeatherType type){
        purge();
        int idx = -1;
        List<WeatherBasic> cacheReturn = new ArrayList<>();
        
        synchronized(cache){
            for(int i = 0; i < cache.size(); i++) {
                if ((cache.get(i).getZip().equalsIgnoreCase(locationString)||cache.get(i).getCityState().equalsIgnoreCase(locationString))&&cache.get(i).getType() == (type)) {
                    idx = i;
                    cacheReturn.add(cache.get(idx));
                }
            }
            return(cacheReturn);
        }
    }
    
    public boolean containsEntry(String locationString, WeatherType type) {
        purge();
        
        synchronized(cache){
            for(int i = 0; i < cache.size(); i++) {
                if ((cache.get(i).getZip().equalsIgnoreCase(locationString)||cache.get(i).getCityState().equalsIgnoreCase(locationString))&&cache.get(i).getType() == (type)) {
                    return(true);
                }
            }
            return(false);
        }
    }
    
    // Removes all cache entries that are past their expiration
    private void purge() {
        synchronized(cache) {
            for (int i=0; i < cache.size(); i++) {
                if(cache.get(i).isAfterExpiration()) {
                    cache.remove(i);
                    i--;
                }
            }
        }
    }
}