/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.pircbotx.Colors;

/**
 *
 * @author Stephen
 */
public class WeatherCache2 {
    private final List<WeatherCacheInterface> cache = Collections.synchronizedList( new ArrayList<WeatherCacheInterface>());
    
    public void add(WeatherCacheInterface log){
        this.cache.add(log);
    }
    public void clear(){
        this.cache.clear();
    }
    public int size(){
        return (this.cache.size());
    }
    public WeatherCacheInterface getCacheEntry(String locationString, String type){
        purge();
        int idx = -1;
        
        synchronized(cache){
            for(int i = 0; i < cache.size(); i++) {
                if ((cache.get(i).getZip().equalsIgnoreCase(locationString)||cache.get(i).getCityState().equalsIgnoreCase(locationString))&&cache.get(i).getType().equalsIgnoreCase(type)) {
                    idx = i;
                    return(cache.get(idx));
                }
            }
            return(cache.get(idx));
        }
    }
    
    public ArrayList<String>getAllAlertTypes(String locationString, String type){
        ArrayList<String> alertTypes = new ArrayList<>();
        List<WeatherCacheInterface> alerts = getCacheArray(locationString, type);
        
        for (int i=0;i<alerts.size();i++){
//            alertTypes.add(alerts.get(i).getAlertType());
        }
        
        return(alertTypes);
    }
    
    public ArrayList<String> getFormattedAlertArray(String locationString, String type){
        ArrayList<String> formattedAlerts = new ArrayList<>();
        List<WeatherCacheInterface> alerts = getCacheArray(locationString, "alert");
        if (alerts.size()>0){
            for (int i=0;i<alerts.size()-1;i++){
                formattedAlerts.add(alerts.get(i).getFormattedResponse());
            }
//                System.out.println(alerts.size());
            formattedAlerts.add(alerts.get(alerts.size()-1).getFormattedResponse()+Colors.BOLD+" Type: "+Colors.NORMAL+"'!alerts full [zip]' for the full alert text");
        }
        else
            return(new ArrayList<>(Arrays.asList("Error")));
        
        return(formattedAlerts);
    }
    
    public ArrayList<String> getAllAlertsLongResponse(String locationString, String type){
        ArrayList<String> formattedAlerts = new ArrayList<>();
        List<WeatherCacheInterface> alerts = getCacheArray(locationString, type);
        
        for (int i=0;i<alerts.size();i++){
//            formattedAlerts.addAll(alerts.get(i).getLongResponse());
            
        }
        return(formattedAlerts);
    }
    
    public List<WeatherCacheInterface> getCacheArray(String locationString, String type){
        purge();
        int idx = -1;
        List<WeatherCacheInterface> cacheReturn = new ArrayList<>();
        
        synchronized(cache){
            for(int i = 0; i < cache.size(); i++) {
                if ((cache.get(i).getZip().equalsIgnoreCase(locationString)||cache.get(i).getCityState().equalsIgnoreCase(locationString))&&cache.get(i).getType().equalsIgnoreCase(type)) {
                    idx = i;
                    cacheReturn.add(cache.get(idx));
                }
            }
            return(cacheReturn);
        }
    }
    
    public boolean containsEntry(String locationString,String type){
        purge();
//            System.out.println(locationString);
        
        synchronized(cache){
            for(int i = 0; i < cache.size(); i++) {
//                    System.out.println("Search String: "+locationString);
//                    System.out.println(cache.get(i).cityState);
//                    System.out.println(cache.get(i).zip);
//                    System.out.println(cache.get(i).cacheType);
                if ((cache.get(i).getZip().equalsIgnoreCase(locationString)||cache.get(i).getCityState().equalsIgnoreCase(locationString))&&cache.get(i).getType().equalsIgnoreCase(type)) {
//                        System.out.println("Found Cached Entry " + cache.get(i).cacheType);
                    return(true);
                }
            }
            return(false);
        }
    }
    private void purge(){
        synchronized(cache){
            for (int i=0;i<cache.size();i++){
                if(cache.get(i).isAfterExpiration()){
                    cache.remove(i);
                    i--;
                }
            }
        }
    }
}