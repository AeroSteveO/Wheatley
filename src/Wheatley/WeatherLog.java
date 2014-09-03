/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package Wheatley;

import java.util.ArrayList;
import org.pircbotx.Colors;
import org.joda.time.DateTime;

/**
 *
 * @author Steve-O
 *
 * Weather Log object for use with Weather Module
 * Allows for caching and removing old cache copies of weather
 *
 */
public class WeatherLog {
    private String conditions;
    private String humidity;
    private String temp;
    private String windMPH;
    private String windKPH;
    private String windDir;
    private String cityState;
    private String observationTime;
    private String zip;
    private DateTime expiration;
    private String cacheType;
    private ArrayList<String> weekDay;
    private ArrayList<String> highF;
    private ArrayList<String> highC;
    private ArrayList<String> lowF;
    private ArrayList<String> lowC;
    private ArrayList<String> forecastConditions;
    private ArrayList<String> alertType;
    private ArrayList<String> alertExpires;
    
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
    public WeatherLog(String inputLocation, String inputZip, ArrayList<String> alertDescription, ArrayList<String> alertExpiration){
        this.cityState = inputLocation;
        this.zip = inputZip;
        this.alertType = alertDescription;
        this.alertExpires = alertExpiration;
        this.cacheType = "alert";
        this.expiration = new DateTime().plusMinutes(30);
    }
//    public WeatherLog(String locationSearch, String type){
//        this.searchLocation = locationSearch;
//        this.cacheType = type;
//        weatherLogger();
//        
//        
//    }
    
    private void weatherLogger(){  // Part of work to move more code into the object and make implementing it easier
        if (this.cacheType.equalsIgnoreCase("alert")){// does not work yet
            this.expiration = new DateTime().plusMinutes(15);
            
            
            
            
        }else if (this.cacheType.equalsIgnoreCase("weather")){
            this.expiration = new DateTime().plusMinutes(60);
            
            
            
            
        }else if (this.cacheType.equalsIgnoreCase("forecast")){
            this.expiration = new DateTime().plusMinutes(60);
            
            
            
        }
    }

    public void removeAlert(int index){
        this.alertType.remove(index);
        this.alertExpires.remove(index);
    }
    public void addAlert(String alert, String expiration){
        this.alertType.add(alert);
        this.alertExpires.add(expiration);
        this.expiration = new DateTime().plusMinutes(30);
    }
    public void updateExpiration(int index, String expiration){
        this.alertExpires.add(index, expiration);
    }
    public ArrayList<String> getAlertType(){
        return this.alertType;
    }
    public ArrayList<String> getAlertExpiration(){
        return this.alertExpires;
    }
    public void updateExpiration(){
        this.expiration = new DateTime().plusMinutes(30);
    }

    //GETS FORMATTED STRING FOR IRC
    public String getFormattedResponse(){
        String response = "";
        // ALERT CACHE FORMATTED RESPONSE
        if (this.cacheType.equalsIgnoreCase("alert")){
            if (!this.alertType.get(0).equalsIgnoreCase("Error Parsing Alerts")||this.alertType.get(0).equalsIgnoreCase("No Current Weather Alerts")){
                for (int i=0;i<this.alertType.size();i++){
                    response =response+ "WEATHER ALERT FOR: " + Colors.NORMAL+this.cityState+ Colors.BOLD+" Description: "+Colors.NORMAL+this.alertType.get(i)+Colors.BOLD+" Ending: "+Colors.NORMAL+this.alertExpires.get(i) + " !";
                }
            }
            else
                response = alertType.get(0);
            
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
            response = "Formatted Response Unavailable for this type of Weather Log";
        
        return(response);
    }
    public boolean isAfterExpiration(){
        if (new DateTime().isAfter(expiration)){
            return(true);
        }
        return(false);
    }
    public static class WeatherCache extends ArrayList<WeatherLog>{
        public WeatherLog getCacheEntry(String zip, String type){
            int idx = -1;
            for(int i = 0; i < this.size(); i++) {
                if ((this.get(i).zip.equalsIgnoreCase(zip)&&this.get(i).cacheType.equalsIgnoreCase(type))||(this.get(i).cityState.equalsIgnoreCase(zip)&&this.get(i).cacheType.equalsIgnoreCase(type))) {
                    idx = i;
                    //System.out.println("Found Cached Entry");
                    break;
                }
            }
            return(this.get(idx));
        }
        public boolean containsEntry(String location,String type){
            for(int i = 0; i < this.size(); i++) {
                if ((this.get(i).zip.equalsIgnoreCase(location)&&this.get(i).cacheType.equalsIgnoreCase(type))||(this.get(i).cityState.equalsIgnoreCase(location)&&this.get(i).cacheType.equalsIgnoreCase(type))) {
                    return(true);
                }
            }
            return(false);
        }
        public void purge(){
            for (int i=0;i<this.size();i++){
                if(this.get(i).isAfterExpiration()){
                    this.remove(i);
                    i--;
                }
            }
        }
    }
}
