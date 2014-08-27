/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package Wheatley;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import org.pircbotx.Colors;
import org.joda.time.DateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Steve-O
 *
 * Weather Log object for use with Weather Module
 * Allows for caching and removing old cache copies of weather
 *
 */
public class WeatherLog {
    String key = "***REMOVED***";
    private String searchLocation;
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
    public WeatherLog(String locationSearch, String type){
        this.searchLocation = locationSearch;
        this.cacheType = type;
        weatherLogger();
        
        
    }
    
    private void weatherLogger(){
        if (this.cacheType.equalsIgnoreCase("alert")){
            this.expiration = new DateTime().plusMinutes(15);
            
            
            
            
        }else if (this.cacheType.equalsIgnoreCase("weather")){
            this.expiration = new DateTime().plusMinutes(60);
            
            
            
            
        }else if (this.cacheType.equalsIgnoreCase("forecast")){
            this.expiration = new DateTime().plusMinutes(60);
            
            
            
        }
    }

    private String weatherUrl(String inputLocation){
        return("http://api.wunderground.com/api/"+key+"/conditions/q/"+inputLocation+".json");
    }
    private String forecastUrl(String inputLocation){
        return ("http://api.wunderground.com/api/"+key+"/forecast10day/q/"+inputLocation+".json");
    }
    private String geoLookupUrl(String inputLocation){
        return ("http://api.wunderground.com/api/"+key+"/geolookup/q/"+inputLocation+".json");
    }
    private String alertUrl(String inputLocation){
        return("http://api.wunderground.com/api/"+key+"/alerts/q/"+inputLocation+".json");
    }
    
    private void getCurrentAlerts(String jsonData) {
        JSONParser parser = new JSONParser();
        ArrayList<String> alertType = new ArrayList<>();
        ArrayList<String> alertExpiration = new ArrayList<>();
        try{
            JSONObject alertJSON = (JSONObject) parser.parse(jsonData);
            JSONArray alertArrayJSON = (JSONArray) alertJSON.get("alerts");
            if (!alertArrayJSON.isEmpty()){
                for (int i=0;i<alertArrayJSON.size();i++){
                    JSONObject alert = (JSONObject) alertArrayJSON.get(i);
                    alertType.add((String) alert.get("description"));
                    alertExpiration.add((String) alert.get("expires"));
                    System.out.println(alertType.get(alertType.size()-1));
                }
                
                ArrayList<String> locationData = getLocationData();
                String cityState = locationData.get(0)+", "+locationData.get(1);
                //WeatherLog alert = new WeatherLog( cityState,  locationData.get(2),alertType,alertExpiration);
                //localCache.add(alert);         // SSSSSSSSSSSSTTTTTUUUUUUFFFFFFF to add to local cache looky here
                //return(alert.getFormattedResponse());
            }
            else{
                String description = ("No Current Weather Alerts");
            }
            
        }catch (Exception ex){
            ex.printStackTrace();
            //return("Error Parsing Alerts");
        }
    }
    
    private void getCurrentForecast(String jsonData) throws ParseException{
        JSONParser parser = new JSONParser();
        String response;
        ArrayList<String> weekDay;
        ArrayList<String> highF = new ArrayList<>();
        ArrayList<String> highC = new ArrayList<>();
        ArrayList<String> lowF = new ArrayList<>();
        ArrayList<String> lowC = new ArrayList<>();
        ArrayList<String> forecastConditions = new ArrayList<>();
        String date = "";
        //simpleforecast
        try{
            JSONObject forecastJSON = (JSONObject) parser.parse(jsonData);
            forecastJSON = (JSONObject) forecastJSON.get("forecast");
            forecastJSON = (JSONObject) forecastJSON.get("simpleforecast");
            JSONArray forecastDay = (JSONArray) forecastJSON.get("forecastday");
            JSONObject day = (JSONObject) forecastDay.get(0);
            day = (JSONObject) day.get("date");
            date = (String) day.get("pretty");
            weekDay = JSONKeyFinder(forecastDay.toString(),"weekday"); //all thats needed for the weekday array
            
            //int max = forecastDay.size();
            
            for (int i=0;i<forecastDay.size();i++){
                JSONObject period = (JSONObject) forecastDay.get(i);
                JSONObject high = (JSONObject) period.get("high");
                highF.add((String) high.get("fahrenheit"));
                highC.add((String) high.get("celsius"));
                JSONObject low = (JSONObject) period.get("low");
                lowF.add((String) high.get("fahrenheit"));
                lowC.add((String) high.get("celsius"));
                forecastConditions.add((String) period.get("conditions"));
            }
            
            ArrayList<String> locationData = getLocationData();
            String cityState = locationData.get(0)+", "+locationData.get(1);
            //WeatherLog forecast = new WeatherLog( cityState,  locationData.get(2),  highF, lowF, highC, lowC, forecastConditions, weekDay, date);
            //response = forecast.getFormattedResponse();
            //localCache.add(forecast); //            AAAAADDDDDDDDDDDD THEE local forecast to the cache of stuff needs to be done
            //return(response);
            
        }
        catch(Exception ex){
            ex.printStackTrace();
            //return("Error: Forecast not found");
        }
    }
    private ArrayList<String> getLocationData() throws Exception{
        JSONParser parser = new JSONParser();
        ArrayList<String> locationData = new ArrayList<>();
        String jsonData = readUrl(geoLookupUrl(this.searchLocation));
        try{
            JSONObject jsonObject = (JSONObject) parser.parse(jsonData);
            JSONObject locationJSON = (JSONObject) jsonObject.get("location");
            locationData.add((String) locationJSON.get("city"));
            locationData.add((String) locationJSON.get("state"));
            locationData.add((String) locationJSON.get("zip"));
            
        }catch (Exception ex){
            ex.printStackTrace();
            locationData.add("Error");
            locationData.add("Error");
            locationData.add("Error");
        }
        return(locationData);
    }
    
    //Finds the given key in the json string using keyfinder.java
    private ArrayList<String> JSONKeyFinder(String jsonText,String jsonKey) throws ParseException{
        JSONParser parser = new JSONParser();
        KeyFinder finder = new KeyFinder();
        ArrayList<String> matchedJson = new ArrayList<>();
        finder.setMatchKey(jsonKey);
        while(!finder.isEnd()){
            parser.parse(jsonText, finder, true);
            if(finder.isFound()){
                finder.setFound(false);
                matchedJson.add(finder.getValue().toString());
            }
        }
        return(matchedJson);
    }
    private void getCurrentWeather(String jsonData) throws ParseException{
        JSONParser parser = new JSONParser();
        String zip = "Unavailable";
        String cityState = "";
        String tempString = "";
        String observationTime = "";
        String weather = "";
        String humidity = "";
//        String high = ""; //Still have to figure out high and low temp
//        String low = "";
        String windDir = "";
        String windMPH = "";
        String windKPH = "";
        
        try{
            
            // GRABBING ALL THE LOCATION DATA
            JSONObject jsonObject = (JSONObject) parser.parse(jsonData);
            JSONObject boardsTemp = (JSONObject) jsonObject.get("current_observation");
            JSONObject locationData = (JSONObject) boardsTemp.get("display_location");
            zip =  (String) locationData.get("zip");
            cityState = (String)locationData.get("full");
            
            // GRABBING ALL THE WEATHER DATA
            JSONObject currentWeather = boardsTemp;//(JSONObject) boardsTemp.get("estimated");
            tempString =(String) currentWeather.get("temperature_string");
            weather =(String) currentWeather.get("weather");
            windDir =(String) currentWeather.get("wind_dir");
            windMPH =(String) currentWeather.get("wind_mph").toString()+" mph";
            windKPH =(String) currentWeather.get("wind_kph").toString()+" Kph";
            humidity =(String) currentWeather.get("relative_humidity");
            observationTime =(String) currentWeather.get("observation_time").toString().split("Last Updated on",2)[1];
            //WeatherLog weatherData = new WeatherLog(cityState,zip, weather, humidity, tempString, windMPH, windKPH, windDir, observationTime);
            //localCache.add(weatherData);           ///////////// NNNNNNNEEEDDDDDDDDDDDD WEATHER CONDITIONS TO ADD TO LOCAL CACHE
            //return(weatherData.getFormattedResponse());
        }
        catch(Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            //return("ERROR");
        }
    }
    
    //converts URL to string, primarily used to string-ify json text
    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);
            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
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
                    Colors.BOLD+"Temperature: "+Colors.NORMAL+this.temp+"; "+Colors.BOLD+"Humidity: "+Colors.NORMAL+this.humidity+"; "+Colors.NORMAL+"Wind: "+this.windMPH+" ("+this.windKPH+") "+this.windDir);
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
