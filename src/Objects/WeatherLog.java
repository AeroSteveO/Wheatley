/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package Objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.pircbotx.Colors;
import org.joda.time.DateTime;

/**
 *
 * @author Steve-O
 *
 * Requirements:
 * - APIs
 *    Joda-Time-2.3
 * - Custom Objects
 *    N/A
 * - Linked Classes
 *    N/A
 *
 * Weather Log object for use with Weather Module
 * Allows for caching and removing old cache copies of weather
 *
 * There are 3 Types of supported cache types
 *      Weather
 *          General weather conditions for the day
 *      Forecast
 *          7 day forecast with low/high temp and conditions
 *      Alert
 *          Currently active weather alerts
 *
 */
public class WeatherLog {
    private final String zip;       // ALL Cache Types
    private DateTime expiration;    // ALL Cache Types
    private final String cacheType; // ALL Cache Types
    private final String cityState; // ALL Cache Types
    private String conditions; // WEATHER
    private String humidity;   // WEATHER
    private String temp;       // WEATHER
    private String windMPH;    // WEATHER
    private String windKPH;    // WEATHER
    private String windDir;    // WEATHER
    private String observationTime; // FORECAST AND WEATHER
    private ArrayList<String> weekDay; // FORECAST
    private ArrayList<String> highF;   // FORECAST
    private ArrayList<String> highC;   // FORECAST
    private ArrayList<String> lowF;    // FORECAST
    private ArrayList<String> lowC;    // FORECAST
    private ArrayList<String> forecastConditions; // FORECAST
    private String alertType;     // ALERT
    private String alertExpires;  // ALERT
    private String alertText;     // ALERT
    
    // WEATHER TYPE LOG ENTRY
    public WeatherLog(String inputLocation,String inputZip, String inputWeather, String hum, String tmp, String windImperial, String windMetric, String windDirection, String obsTime) {
        this.conditions = inputWeather;
        this.humidity = hum;
        this.temp = tmp;
        this.windKPH = windMetric;
        this.windMPH = windImperial;
        this.windDir = windDirection;
        this.cityState = inputLocation;
        this.observationTime = obsTime;
        this.zip = inputZip;
        this.cacheType = "weather";
        this.expiration = new DateTime().plusMinutes(60);
    }
    // FORECAST TYPE LOG ENTRY
    public WeatherLog(String inputLocation, String inputZip, ArrayList<String> highTempF, ArrayList<String> lowTempF, ArrayList<String> highTempC, ArrayList<String> lowTempC, ArrayList<String> weather, ArrayList<String> day, String pretty){
        this.cityState = inputLocation;
        this.zip = inputZip;
        this.cacheType = "forecast";
        this.weekDay = day;
        this.highF = highTempF;
        this.lowF = lowTempF;
        this.highC = highTempC;
        this.lowC = lowTempC;
        this.observationTime = pretty;
        this.forecastConditions = weather;
        this.expiration = new DateTime().plusMinutes(60);
    }
    // ALERT TYPE LOG ENTRY
    public WeatherLog(String inputLocation, String inputZip, String alertDescription, String alertExpiration, String alertText){
        this.cityState = inputLocation;
        this.zip = inputZip;
        this.alertType = alertDescription;
        this.alertExpires = alertExpiration;
        this.cacheType = "alert";
        this.expiration = new DateTime().plusMinutes(30);
        this.alertText = alertText;
    }
    
    public ArrayList<String> getLocationData(){
        ArrayList<String> locationData = new ArrayList<>();
        
        locationData.add(cityState.split(",")[0].trim());
        locationData.add(cityState.split(",")[1].trim());
        locationData.add(zip);
        
        return locationData;
    }
    
    public void updateExpiration(int index, String expiration){
        this.alertExpires = expiration;
        this.expiration = new DateTime().plusMinutes(30);
    }
    
    public String getAlertType(){
        return this.alertType;
    }
    
    public ArrayList<String> getLongResponse(){
        
        ArrayList<String> respond = new ArrayList<>();
        
        if (this.cacheType.equalsIgnoreCase("alert")){
            if (!this.alertType.equalsIgnoreCase("Error Parsing Alerts")||this.alertType.equalsIgnoreCase("No Current Weather Alerts")){
                String[] alertLines = alertText.split("\\u000A");
                
                respond.add(Colors.BOLD+Colors.RED+"Alert Full Text: ");
                
                for (int i=0;i<alertLines.length;i++){
                    if (alertLines[i].trim().length()>0)
                        respond.add(alertLines[i].trim());
                }
            }
            else{
                respond.add(alertType);
            }
        }
        else{
            respond.add("Formatted Response Unavailable for this type of Weather Log");
        }
        return (respond);
    }
    //GETS FORMATTED STRING FOR IRC
    public String getFormattedResponse(){
        String response = "";
        // ALERT CACHE FORMATTED RESPONSE
        if (this.cacheType.equalsIgnoreCase("alert")){
            if (!this.alertType.equalsIgnoreCase("Error Parsing Alerts")||this.alertType.equalsIgnoreCase("No Current Weather Alerts")){
                response = Colors.RED + Colors.BOLD + "WEATHER ALERT "+Colors.NORMAL+Colors.BOLD+"For: " + Colors.NORMAL+this.cityState+ Colors.BOLD+" Description: "+Colors.NORMAL+this.alertType+Colors.BOLD+" Ending: "+Colors.NORMAL+this.alertExpires;
            }
            else
                response = alertType;
            
            // FORECAST CACHE FORMATTED RESPONSE
        }else if (this.cacheType.equalsIgnoreCase("forecast")){
            response =(Colors.BOLD+this.cityState+" Forecast "+Colors.NORMAL+"(High/Low); "+Colors.BOLD+"Updated: "+Colors.NORMAL+this.observationTime.split("on")[0].trim()+"; ");
            //weekDay.size()
            for (int i=0;i<7;i++){
                response = response+(Colors.BOLD+this.weekDay.get(i)+": "+Colors.NORMAL+this.forecastConditions.get(i)+", "+this.highF.get(i)+"/"+this.lowF.get(i)+"°F ("+this.highC.get(i)+"/"+this.lowC.get(i)+"°C); ");
            }
            
            // WEATHER CACHE FORMATTED RESPONSE
        }else if (this.cacheType.equalsIgnoreCase("weather")){
            response = (Colors.BOLD+this.cityState+"; Updated: "+Colors.NORMAL+this.observationTime+"; "+Colors.BOLD+"Conditions: "+Colors.NORMAL+this.conditions+"; "+
                    Colors.BOLD+"Temperature: "+Colors.NORMAL+this.temp+"; "+Colors.BOLD+"Humidity: "+Colors.NORMAL+this.humidity+"; "+Colors.BOLD+"Wind: "+Colors.NORMAL+this.windMPH+" ("+this.windKPH+") "+this.windDir);
        }
        else
            throw new UnsupportedOperationException("Formatted Response Unavailable for this type of Weather Log");
        
        return(response);
    }
    private boolean isAfterExpiration(){
        if (new DateTime().isAfter(expiration)){
            return(true);
        }
        return(false);
    }
    public static class WeatherCache {
        private final List<WeatherLog> cache = Collections.synchronizedList( new ArrayList<WeatherLog>());
        
        public void add(WeatherLog log){
            this.cache.add(log);
        }
        public void clear(){
            this.cache.clear();
        }
        public int size(){
            return (this.cache.size());
        }
        public WeatherLog getCacheEntry(String locationString, String type){
            purge();
            int idx = -1;
            
            synchronized(cache){
                for(int i = 0; i < cache.size(); i++) {
                    if ((cache.get(i).zip.equalsIgnoreCase(locationString)||cache.get(i).cityState.equalsIgnoreCase(locationString))&&cache.get(i).cacheType.equalsIgnoreCase(type)) {
                        idx = i;
                        return(cache.get(idx));
                    }
                }
                return(cache.get(idx));
            }
        }
        
        public ArrayList<String>getAllAlertTypes(String locationString, String type){
            ArrayList<String> alertTypes = new ArrayList<>();
            List<WeatherLog> alerts = getCacheArray(locationString, type);
            
            for (int i=0;i<alerts.size();i++){
                alertTypes.add(alerts.get(i).getAlertType());
            }
            
            return(alertTypes);
        }
        
        public ArrayList<String> getFormattedAlertArray(String locationString, String type){
            ArrayList<String> formattedAlerts = new ArrayList<>();
            List<WeatherLog> alerts = getCacheArray(locationString, "alert");
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
            List<WeatherLog> alerts = getCacheArray(locationString, type);
            
            for (int i=0;i<alerts.size();i++){
                formattedAlerts.addAll(alerts.get(i).getLongResponse());
                
            }
            return(formattedAlerts);
        }
        
        public List<WeatherLog> getCacheArray(String locationString, String type){
            purge();
            int idx = -1;
            List<WeatherLog> cacheReturn = new ArrayList<>();
            
            synchronized(cache){
                for(int i = 0; i < cache.size(); i++) {
                    if ((cache.get(i).zip.equalsIgnoreCase(locationString)||cache.get(i).cityState.equalsIgnoreCase(locationString))&&cache.get(i).cacheType.equalsIgnoreCase(type)) {
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
                    if ((cache.get(i).zip.equalsIgnoreCase(locationString)||cache.get(i).cityState.equalsIgnoreCase(locationString))&&cache.get(i).cacheType.equalsIgnoreCase(type)) {
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
}