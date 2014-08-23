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
    @Override
    public void onMessage(final MessageEvent event) throws Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        
        if (message.toLowerCase().startsWith("!w")){
            if (message.equalsIgnoreCase("!w")||message.equalsIgnoreCase("!weather")){
                location = stockZip;
                // event.respond((getCurrentWeather(readUrl(weatherUrl(stockZip)))));
            }
            else if(message.toLowerCase().matches("!w [a-zA-Z ]\\, [a-zA-Z]{2}")||message.toLowerCase().matches("!weather [a-zA-Z ]\\, [a-zA-Z]{2}")){
                String[] tmp = message.split(" ",2);
                String[] cityState = tmp[1].split(",");
                location = cityState[1].replaceAll(" ", "_")+"/"+cityState[0];
            }
            else if(message.toLowerCase().matches("!w [0-9]{5}")||message.toLowerCase().matches("!weather [0-9]{5}")){
                location = message.split(" ",2)[1];
            }
            if (location != null){
                weatherUrl(location);
            }
            
        }
        if (message.toLowerCase().startsWith("!f")){
            if (message.equalsIgnoreCase("!f")||message.equalsIgnoreCase("!forecast")){
                location = stockZip;
                //event.respond(getCurrentForecast(readUrl(forecastUrl(stockZip))));
            }
            else if(message.toLowerCase().matches("!f [a-zA-Z\\s]\\,\\s[a-zA-Z]{2}")||message.toLowerCase().matches("!forecast [a-zA-Z\\s]\\,\\s[a-zA-Z]{2}")){
                String[] tmp = message.split(" ",2);
                String[] cityState = tmp[1].split(",");
                location = cityState[1].replaceAll(" ", "_")+"/"+cityState[0];
            }
            else if(message.toLowerCase().matches("!f [0-9]{5}")||message.toLowerCase().matches("!forecast [0-9]{5}")){
                location = message.split(" ",2)[1];
                
            }
            if (location != null){
                forecastUrl(location);
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
        return("http://api.wunderground.com/api/"+key+"/forecast/q/"+inputLocation+".json");
    }
    private String forecastUrl(String inputLocation){
        return ("http://api.wunderground.com/api/"+key+"/forecast/q/"+inputLocation+".json");
    }
    private String getCurrentForecast(String jsonData){
        
        
        
        return("");
    }
    
    private String getCurrentWeather(String jsonData) throws ParseException{
        JSONParser parser = new JSONParser();
        List<String> boards = new ArrayList<>();
        String zip = "Unavailable";
        String cityState = "";
        String tempString = "";
        String observationTime = "";
        String weather = "";
        String humidity = "";
        String high = ""; //Still have to figure out high and low temp
        String low = "";
        String windDir = "";
        String windMPH = "";
        String windKPH = "";
        
        try{
            
            // GRABBING ALL THE LOCATION DATA
            JSONObject jsonObject = (JSONObject) parser.parse(jsonData);
            JSONObject boardsTemp = (JSONObject) jsonObject.get("current_observation");
            JSONObject locationData = (JSONObject) boardsTemp.get("display_location");
            zip =  ((String) locationData.get("zip"));
            cityState = (String)locationData.get("full");
            
            // GRABBING ALL THE WEATHER DATA
            JSONObject currentWeather = (JSONObject) boardsTemp.get("estimated");
            tempString =(String) currentWeather.get("temperature_string");
            weather =(String) currentWeather.get("weather");
            windDir =(String) currentWeather.get("wind_dir");
            windMPH =(String) currentWeather.get("wind_mph");
            windKPH =(String) currentWeather.get("wind_kph");
            humidity =(String) currentWeather.get("relative_humidity");
            observationTime =(String) currentWeather.get("observation_time");
            humidity =(String) currentWeather.get("relative_humidity");
            humidity =(String) currentWeather.get("relative_humidity");
        }
        catch(Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            //return(false);
        }
        return(zip);
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
