/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import Wheatley.WeatherLog.WeatherCache;

//event.respond(searchURL);
//  "http://api.wunderground.com/api/***REMOVED***/conditions/q/in/west_lafayette.json";
//http://api.wunderground.com/api/***REMOVED***/alerts/q/IA/Des_Moines.json
//http://api.wunderground.com/api/***REMOVED***/forecast/q/CA/San_Francisco.json

/**
 *
 * @author Stephen
 *
 *
 * <LilWayne> West Lafayette, IN Forecast (High/Low); Updated: 10:04 PM EDT; Friday:
 * Thunderstorm, 90/72°F (32/22°C); Saturday: Thunderstorm, 88/68°F (31/20°C); Sunday:
 * Thunderstorm, 86/72°F (30/22°C); Monday: Partly Cloudy, 90/72°F (32/22°C); Tuesday:
 * Partly Cloudy, 93/72°F (34/22°C); Wednesday: Chance of a Thunderstorm, 86/59°F (30/15°C);
 *
 * <LilWayne> West Lafayette, IN; Updated: 9:54 PM EDT; Conditions: Clear; Temperature:
 * 74°F (23°C); Humidity: 97%; High/Low: 90/68°F (32/20°C); Wind: Calm;
 */
public class Weather extends ListenerAdapter{
    String key = "***REMOVED***";
    String stockZip = "47906";
    String location = null;
    WeatherCache localCache = new WeatherCache();
    @Override
    public void onMessage(final MessageEvent event) throws Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage().toLowerCase());
        
        if (message.toLowerCase().startsWith("!w")){
            if (message.equalsIgnoreCase("!w")||message.equalsIgnoreCase("!weather")){
                location = stockZip;
            }
            else if(message.matches("!w [a-zA-Z\\s]+\\,\\s[a-zA-Z]{2}")||message.matches("!weather [a-zA-Z\\s]+\\,\\s[a-zA-Z]{2}")){
                String[] tmp = message.split(" ",2);
                String[] cityState = tmp[1].split(",");
                location = cityState[1].trim()+"/"+cityState[0].trim().replaceAll(" ", "_");
                System.out.println(location);
            }
            else if(message.matches("!w [0-9]{5}")||message.matches("!weather [0-9]{5}")){
                location = message.split(" ",2)[1];
            }
            if (location != null){
                String search;
                if(location.matches("[0-9]{5}"))
                    search = location;
                else
                    search = location.replaceAll("_", " ").replace("/",", ");
                if(localCache.size()>0&&localCache.containsEntry(search,"weather")){
                    event.respond(localCache.getCacheEntry(search,"weather").getFormattedWeather());
                }
                else{
                    event.respond(getCurrentWeather(readUrl(weatherUrl(location))));
                }
            }
            
        }
        if (message.toLowerCase().startsWith("!f")){
            if (message.equalsIgnoreCase("!f")||message.equalsIgnoreCase("!forecast")){
                location = stockZip;
            }
            else if(message.toLowerCase().matches("!f [a-zA-Z\\s]\\,\\s[a-zA-Z]{2}")||message.toLowerCase().matches("!forecast [a-zA-Z\\s]\\,\\s[a-zA-Z]{2}")){
                String[] tmp = message.split(" ",2);
                String[] cityState = tmp[1].split(",");
                location = cityState[1].replaceAll(" ", "_")+"/"+cityState[0].replaceAll(" ", "_");
            }
            else if(message.toLowerCase().matches("!f [0-9]{5}")||message.toLowerCase().matches("!forecast [0-9]{5}")){
                location = message.split(" ",2)[1];
                
            }
            if (location != null){
                String search;
                
                if(location.matches("[0-9]{5}"))
                    search = location;
                else
                    search = location.replaceAll("_", " ").replace("/",", ");
                
                if(localCache.size()>0&&localCache.containsEntry(search,"forecast")){
                    System.out.println("local cache contains entry");
                    event.respond(localCache.getCacheEntry(search,"forecast").getFormattedWeather());
                }
                else{
                    event.respond(getCurrentForecast(readUrl(forecastUrl(location))));
                }
            }
        }
//        if (message.toLowerCase().matches("!w [a-zA-Z ]\\, [a-zA-Z]{2}")||message.toLowerCase().matches("!weather [a-zA-Z ]\\, [a-zA-Z]{2}")){
//            String[] tmp = message.split(" ",2);
//            String[] cityState = tmp[1].split(",");
//            event.respond((getCurrentWeather(readUrl(weatherUrl(cityState[1].replaceAll(" ", "_")+"/"+cityState[0])))));
//        }
//        if (message.toLowerCase().matches("!w [0-9]{5}")||message.toLowerCase().matches("!weather [0-9]{5}")){
//            event.respond((getCurrentWeather(readUrl(weatherUrl(message.split(" ",2)[1])))));
//            //String weatherData = readUrl(searchURL);
//        }
//        if (message.toLowerCase().matches("!f [a-zA-Z\\s]\\,\\s[a-zA-Z]{2}")||message.toLowerCase().matches("!forecast [a-zA-Z\\s]\\,\\s[a-zA-Z]{2}")){
//            String[] tmp = message.split(" ",2);
//            String[] cityState = tmp[1].split(",");
//            String searchURL = "http://api.wunderground.com/api/"+key+"/forecast/q/"+cityState[1].replaceAll(" ", "_")+"/"+cityState[0]+".json";
//            //String weatherData = readUrl(searchURL);
//        }
//        if (message.toLowerCase().matches("!f [0-9]{5}")||message.toLowerCase().matches("!forecast [0-9]{5}")){
//            String zip = message.split(" ",2)[1];
//            String searchURL = "http://api.wunderground.com/api/"+key+"/forecast/q/"+zip+".json";
//            //event.respond(searchURL);
//            //String weatherData = readUrl(searchURL);
//        }
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
    private String getCurrentForecast(String jsonData) throws ParseException{
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
            JSONObject jsonObject = (JSONObject) parser.parse(jsonData);
            //System.out.println(jsonObject.toString());
            jsonObject = (JSONObject) jsonObject.get("forecast");
            JSONObject forecastTemp = (JSONObject) jsonObject.get("simpleforecast");
            //System.out.println(forecastTemp.toString());
            JSONArray forecastDay = (JSONArray) forecastTemp.get("forecastday");
            JSONObject day = (JSONObject) forecastDay.get(0);
            day = (JSONObject) day.get("date");
            date = (String) day.get("pretty");
            //System.out.println(forecastDay.get(1).toString());
            weekDay = JSONKeyFinder(forecastDay.toString(),"weekday"); //all thats needed for the weekday array
            //System.out.println(weekDay.toString());
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
            System.out.println(cityState);
            System.out.println(locationData.get(2));
            System.out.println(date);
            WeatherLog forecast = new WeatherLog( cityState,  locationData.get(2),  highF, lowF, highC, lowC, forecastConditions, weekDay, date);
            response = forecast.getFormattedForecast();
            localCache.add(forecast);
            return(response);
            
        }
        catch(Exception ex){
            ex.printStackTrace();
            return("Error: Forecast not found");
        }
    }
    private ArrayList<String> getLocationData() throws Exception{
        JSONParser parser = new JSONParser();
        ArrayList<String> locationData = new ArrayList<>();
        String jsonData = readUrl(geoLookupUrl(location));
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
    private String getCurrentWeather(String jsonData) throws ParseException{
        JSONParser parser = new JSONParser();
//        List<String> boards = new ArrayList<>();
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
            WeatherLog weatherData = new WeatherLog(cityState,zip, weather, humidity, tempString, windMPH, windKPH, windDir, observationTime);
            localCache.add(weatherData);
//            String response = (cityState+"; Updated: "+observationTime+"; "+"Conditions: "+weather+"; "+
//                    Colors.BOLD+"Temperature: "+tempString+"; "+"Humidity: "+humidity+"; "+"Wind: "+windMPH+" ("+windKPH+") "+windDir);
//            System.out.println(response);
            return(weatherData.getFormattedWeather());
        }
        catch(Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            return("ERROR");
            //return(false);
        }
        //return(weatherData.getFormattedWeather());
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
}
