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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import Wheatley.WeatherLog.WeatherCache;
import org.pircbotx.PircBotX;

//event.respond(searchURL);
//  "http://api.wunderground.com/api/***REMOVED***/conditions/q/in/west_lafayette.json";
//http://api.wunderground.com/api/***REMOVED***/alerts/q/IA/Des_Moines.json
//http://api.wunderground.com/api/***REMOVED***/forecast/q/CA/San_Francisco.json

/**
 *
 * @author Stephen
 *      Original Bot: LilWayne by: i dunno who
 *
 * Activate Command with:
 *      !w [zip]
 *      !weather [zip]
 *      !w [city, state 2 digit code]
 *      !weather [city, state 2 digit code]
 *          responds with the weather for that city, if nothing is input,
 *          responds with the weather for the stock zip code
 *      !f [zip]
 *      !forecast [zip]
 *      !f [city, state 2 digit code]
 *      !forecast [city, state 2 digit code]
 *          responds with the 7 day forecast for that city, if nothing is input,
 *          responds with the 7 day forecast for the stock zip code
 *      !a [zip]
 *      !alerts [zip]
 *      !a [city, state 2 digit code]
 *      !alerts [city, state 2 digit code]
 *          responds with the any available weather alerts for that city, if nothing is input,
 *          responds with the any available weather alerts for the stock zip code
 */
public class Weather extends ListenerAdapter{
    String key = "***REMOVED***";
    String stockZip = "47906";
    String location = null;
    WeatherCache localCache = new WeatherCache();
    AlertTime alertUpdater = new AlertTime(Global.bot);
    
    Thread t = new Thread(alertUpdater);
    boolean started = startAlertTime(t);
    //Runner parallel = new Runner(Global.bot);
    
    /**
     * <LilWayne> West Lafayette, IN Forecast (High/Low); Updated: 10:04 PM EDT; Friday:
     * Thunderstorm, 90/72°F (32/22°C); Saturday: Thunderstorm, 88/68°F (31/20°C); Sunday:
     * Thunderstorm, 86/72°F (30/22°C); Monday: Partly Cloudy, 90/72°F (32/22°C); Tuesday:
     * Partly Cloudy, 93/72°F (34/22°C); Wednesday: Chance of a Thunderstorm, 86/59°F (30/15°C);
     *
     * <LilWayne> West Lafayette, IN; Updated: 9:54 PM EDT; Conditions: Clear; Temperature:
     * 74°F (23°C); Humidity: 97%; High/Low: 90/68°F (32/20°C); Wind: Calm;
     */
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage().toLowerCase());
        
        if (message.toLowerCase().startsWith("!w")||message.toLowerCase().startsWith("!f")||message.toLowerCase().startsWith("!a")){
            String[] msgSplit = message.split(" ",2);
            String command;
            if (msgSplit.length>1)
                command = msgSplit[1];
            else
                command="";
            if (command.equalsIgnoreCase("")){ //dunno if this works yet
                location = stockZip;
            }
            else if(command.matches("[a-zA-Z\\s]+\\,\\s[a-zA-Z]{2}")){
                String[] cityState = command.split(",");
                location = cityState[1].trim()+"/"+cityState[0].trim().replaceAll(" ", "_");
            }
            else if(command.matches("[0-9]{5}")){
                location = command;
            }
            // end of location reuse setup, hopefully
            
        }
        if (message.toLowerCase().startsWith("!w")){
            if (location != null){
                String search;
                localCache.purge();
                if(location.matches("[0-9]{5}"))
                    search = location;
                else
                    search = location.replaceAll("_", " ").replace("/",", ");
                if(localCache.size()>0&&localCache.containsEntry(search,"weather")){
                    event.getBot().sendIRC().message(event.getChannel().getName(),localCache.getCacheEntry(search,"weather").getFormattedResponse());
                    
                    
                }
                else{
                    event.getBot().sendIRC().message(event.getChannel().getName(),getCurrentWeather(readUrl(weatherUrl(location))));
                }
                
                
                if(localCache.size()>0&&localCache.containsEntry(search,"alerts")){
                    String[] alertResponse = localCache.getCacheEntry(search,"alerts").getFormattedResponse().split("~");
                    for (int i=0;i<alertResponse.length;i++){
                        event.getBot().sendIRC().message(event.getChannel().getName(),Colors.BOLD+Colors.RED+alertResponse[i].trim());
                    }
                }
                else{
                    String[] alertResponse = getCurrentAlerts(readUrl(alertUrl(location))).split("!");
                    if (!alertResponse[0].equalsIgnoreCase("Error Parsing Alerts")&&!alertResponse[0].equalsIgnoreCase("No Current Weather Alerts")){
                        for (int i=0;i<alertResponse.length;i++){
                            event.getBot().sendIRC().message(event.getChannel().getName(),Colors.BOLD+Colors.RED+alertResponse[i].trim());
                        }
                    }
                }
                
            }
        }
        if (message.toLowerCase().startsWith("!f")){
            if (location != null){
                String search;
                localCache.purge();
                if(location.matches("[0-9]{5}"))
                    search = location;
                else
                    search = location.replaceAll("_", " ").replace("/",", ");
                
                if(localCache.size()>0&&localCache.containsEntry(search,"forecast")){
                    event.getBot().sendIRC().message(event.getChannel().getName(),localCache.getCacheEntry(search,"forecast").getFormattedResponse());
                }
                else{
                    event.getBot().sendIRC().message(event.getChannel().getName(),getCurrentForecast(readUrl(forecastUrl(location))));
                }
            }
        }
        if (message.toLowerCase().startsWith("!a")){
            if (location != null){
                String search;
                localCache.purge();
                if(location.matches("[0-9]{5}"))
                    search = location;
                else
                    search = location.replaceAll("_", " ").replace("/",", ");
                if(localCache.size()>0&&localCache.containsEntry(search,"alerts")){
                    String[] alertResponse = localCache.getCacheEntry(search,"alerts").getFormattedResponse().split("~");
                    for (int i=0;i<alertResponse.length;i++){
                        event.getBot().sendIRC().message(event.getChannel().getName(),Colors.BOLD+Colors.RED+alertResponse[i].trim());
                    }
                }
                else{
                    String[] alertResponse = getCurrentAlerts(readUrl(alertUrl(location))).split("!");
                    if (alertResponse[0].equalsIgnoreCase("Error Parsing Alerts")||alertResponse[0].equalsIgnoreCase("No Current Weather Alerts")){
                        event.getBot().sendIRC().message(event.getChannel().getName(),alertResponse[0].trim());
                    }else{
                        for (int i=0;i<alertResponse.length;i++){
                            System.out.println(alertResponse[i]);
                            event.getBot().sendIRC().message(event.getChannel().getName(),Colors.BOLD+Colors.RED+alertResponse[i].trim());
                        }
                    }
                }
            }
            
        }
        location = null;
    }
    private boolean startAlertTime(Thread t){
        try{
            //Thread t = new Thread(alertUpdater);
            alertUpdater.giveT(t);
            t.start();
            return(true);
        }
        catch(Exception ex){
            ex.printStackTrace();
            return(false);
        }
    }
    public  class AlertTime implements Runnable {
        int time = 1*10; // 15 min converted to seconds
        String channel = "#dtella";
        boolean updateAlerts = true;
        //int key;
        PircBotX bot;// = Global.bot;
        Thread t;
        
        AlertTime(PircBotX bot) {
            this.bot=bot;
            //AlertTime runnable = new AlertTime();
            //this.t = new Thread(this);
            //this.giveT(t);
            //t.start();
        }
        public void end() throws InterruptedException{
            //this.t.close(); //Close this EventQueue
            this.updateAlerts = false;
            t.join(1000); //Ensure the thread also closes
        }
        
        public void giveT(Thread t) {
            this.t = t;
        }
        
        @Override
        public void run() {
            while(updateAlerts){
                try { // No need to loop for this thread
                    
                    Thread.sleep(time*1000);
                    this.bot.sendIRC().message("#dtella", "STUFF");
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
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
    
    private String getCurrentAlerts(String jsonData) {
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
                WeatherLog alert = new WeatherLog( cityState,  locationData.get(2),alertType,alertExpiration);
                localCache.add(alert);
                return(alert.getFormattedResponse());
            }
            else
                return("No Current Weather Alerts");
            
            
        }catch (Exception ex){
            ex.printStackTrace();
            return("Error Parsing Alerts");
        }
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
            WeatherLog forecast = new WeatherLog( cityState,  locationData.get(2),  highF, lowF, highC, lowC, forecastConditions, weekDay, date);
            response = forecast.getFormattedResponse();
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
            return(weatherData.getFormattedResponse());
        }
        catch(Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            return("ERROR");
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
}
