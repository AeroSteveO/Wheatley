/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import Objects.WeatherLog;
import Objects.KeyFinder;
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
import Objects.WeatherLog.WeatherCache;

//event.respond(searchURL);
//  "http://api.wunderground.com/api/***REMOVED***/conditions/q/in/west_lafayette.json";
//http://api.wunderground.com/api/***REMOVED***/alerts/q/IA/Des_Moines.json
//http://api.wunderground.com/api/***REMOVED***/forecast/q/CA/San_Francisco.json

/**
 *
 * @author Stephen
 *      Original Bot: LilWayne
 *
 * Activate Command with:
 *      !w [location]
 *      !weather [location]
 *          Location can be:
 *              [city, state 2 digit code]
 *              [zip]
 *          Responds with the weather for that city, if nothing is input,
 *          Responds with the weather for the stock zip code
 *      !f [location]
 *      !forecast [location]
 *          Location can be:
 *              [city, state 2 digit code]
 *              [zip]
 *          Responds with the 7 day forecast for that city, if nothing is input,
 *          Responds with the 7 day forecast for the stock zip code
 *      !a [location]
 *      !alert [location]
 *      !alerts [location]
 *          Location can be:
 *              [city, state 2 digit code]
 *              [zip]
 *          Responds with the any available weather alerts for that city, if nothing is input,
 *          Responds with the any available weather alerts for the stock zip code
 *      !a full [location]
 *      !alert full [location]
 *      !alerts full [location]
 *      !a [location] full
 *      !alert [location] full
 *      !alerts [location] full
 *          Location can be:
 *              [city, state 2 digit code]
 *              [zip]
 *          Responds with a PM containing the full text of the current alerts
 *      !nuke local cache
 *          Clears the local cache of weather, alerts, and forecast data
 *          Can help clear up errors
 */
public class Weather extends ListenerAdapter{
    String key = "***REMOVED***";    // API KEY, DO NOT LOSE
    String stockZip = "47906";          // Stock location for weather/alerts/forecast/auto alerts
    String location = null;             // Location for the weather command, or null if no command
    WeatherCache localCache = new WeatherCache(); // Initiate cache of weather data
    
    
    // Initiate Auto-Alert System
    String alertChannel = "#dtella";              // Channel to send weather alert updates to
    int alertUpdateTime = 15*60;                  // 15 min converted to seconds
    boolean updateAlerts = true;                  // True if the bot should send weather alert updates
    AlertTime alertUpdater = new AlertTime(alertChannel,updateAlerts,alertUpdateTime);     // Initiate auto-alert object
    Thread t = new Thread(alertUpdater);          // Give it a thread to run in
    boolean started = startAlertTime(t);          // Start the auto-alert thread
    
    /**
     *
     * @param event
     * @throws java.lang.Exception
     *
     */
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        location = null;
        String message = Colors.removeFormattingAndColors(event.getMessage().toLowerCase());
        if (message.equalsIgnoreCase("!nuke local cache")){
            localCache.clear();
            event.respond("Local Cache Nuked");
        }
        if ((message.equalsIgnoreCase("!w")||message.equalsIgnoreCase("!weather")||message.equalsIgnoreCase("!f")||message.equalsIgnoreCase("!forecast")||message.equalsIgnoreCase("!a")||message.equalsIgnoreCase("!alerts")||message.equalsIgnoreCase("!alert"))){
            location = stockZip;
        }
        else if (message.matches("(!weather|!w)\\s(([0-9]{5})|([a-z\\s]+\\,\\s[a-z]{2}))")||message.matches("(!forecast|!f)\\s(([0-9]{5})|([a-z\\s]+\\,\\s[a-z]{2}))")||message.matches("(!alerts|!alert|!a)\\s(([0-9]{5})|([a-z\\s]+\\,\\s[a-z]{2}))")){
            parseLocationFromMessage(message);
        }
        else if (message.matches("(!alerts\\sfull|!alert\\sfull|!a\\sfull)\\s(([0-9]{5})|([a-z\\s]+\\,\\s[a-z]{2}))")){
            parseLocationFromMessage(message.split(" ",2)[1]);
        }
        else if (message.matches("(!alerts|!alert|!a)\\s(([0-9]{5})|([a-z\\s]+\\,\\s[a-z]{2}))(\\sfull)")){
            parseLocationFromMessage(message.split("full")[0].trim());
        }
        else if ((message.startsWith("!w ")||message.startsWith("!weather ")||message.startsWith("!f ")||message.startsWith("!forecast ")||message.startsWith("!a ")||message.startsWith("!alerts ")||message.startsWith("!alert "))){
            String[] msgSplit = message.split(" ",2);
            ArrayList<String> locationData = getLocationData(msgSplit[1].trim().replaceAll(" ", "_"));
            location = locationData.get(1).trim()+"/"+locationData.get(0).trim().replaceAll(" ", "_");
        }
        if (message.startsWith("!w")){
            if (location != null){
                String search;
                localCache.purge();
//                if(location.matches("[0-9]{5}"))
                search = getSearchStringForCacheSearch(location);
//                else
//                    search = location.replaceAll("_", " ").replace("/",", ");
                if(localCache.size()>0&&localCache.containsEntry(search,"weather")){
                    event.getBot().sendIRC().message(event.getChannel().getName(),localCache.getCacheEntry(search,"weather").getFormattedResponse());
                }
                else{
                    event.getBot().sendIRC().message(event.getChannel().getName(),getCurrentWeather(readUrl(weatherUrl(location))));
                }
                
                
                if(localCache.size()>0&&localCache.containsEntry(search,"alert")){
                    String[] alertResponse = localCache.getCacheEntry(search,"alert").getFormattedResponse().split("!");
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
        if (message.startsWith("!f")){
            if (location != null){
                String search;
                localCache.purge();
                search = getSearchStringForCacheSearch(location);
//                if(location.matches("[0-9]{5}"))
//                    search = location;
//                else
//                    search = location.replaceAll("_", " ").replace("/",", ");
                
                if(localCache.size()>0&&localCache.containsEntry(search,"forecast")){
                    event.getBot().sendIRC().message(event.getChannel().getName(),localCache.getCacheEntry(search,"forecast").getFormattedResponse());
                }
                else{
                    event.getBot().sendIRC().message(event.getChannel().getName(),getCurrentForecast(readUrl(forecastUrl(location))));
                }
            }
        }
        if (message.startsWith("!a")){
            if (location != null){
                String search;
                localCache.purge();
                search = getSearchStringForCacheSearch(location);
//                if(location.matches("[0-9]{5}"))
//                    search = location;
//                else
//                    search = location.replaceAll("_", " ").replace("/",", ");
                
                if(localCache.size()>0&&localCache.containsEntry(search,"alert")){
                    if (!message.matches("(?i).*full.*")){
                        String[] alertResponse = localCache.getCacheEntry(search,"alert").getFormattedResponse().split("!");
                        for (int i=0;i<alertResponse.length;i++){
                            event.getBot().sendIRC().message(event.getChannel().getName(),Colors.BOLD+Colors.RED+alertResponse[i].trim());
                        }
                    }
                    else {
                        String[] alertFullText = localCache.getCacheEntry(search,"alert").getLongResponse().split("!");
                        for (int i=0;i<alertFullText.length;i++){
                            String[] alertLines = alertFullText[i].split("\\u000A");
                            event.getBot().sendIRC().message(event.getUser().getNick(),Colors.BOLD+Colors.RED+alertLines[0].trim());
                            for (int c = 1;c<alertLines.length;c++){
                                event.getBot().sendIRC().message(event.getUser().getNick(),alertLines[c].trim());
                            }
                        }
                    }
                }
                else{
                    if (!message.matches("(?i).*full.*")){
                        String[] alertResponse = getCurrentAlerts(readUrl(alertUrl(location))).split("!");
                        if (alertResponse[0].equalsIgnoreCase("Error Parsing Alerts")||alertResponse[0].equalsIgnoreCase("No Current Weather Alerts")||alertResponse[0].equalsIgnoreCase("ERROR")){
                            event.getBot().sendIRC().message(event.getChannel().getName(),alertResponse[0].trim());
                        }
                        else {
                            for (int i=0;i<alertResponse.length;i++){
                                event.getBot().sendIRC().message(event.getChannel().getName(),Colors.BOLD+Colors.RED+alertResponse[i].trim());
                            }
                        }
                    }
                    else {
                        String derp = getCurrentAlerts(readUrl(alertUrl(location)));
                        String[] alertFullText = localCache.getCacheEntry(search,"alert").getLongResponse().split("!");
                        for (int i=0;i<alertFullText.length;i++){
                            String[] alertLines = alertFullText[i].split("\\u000A");
                            event.getBot().sendIRC().message(event.getUser().getNick(),Colors.BOLD+Colors.RED+alertLines[0].trim());
                            for (int c = 1;c<alertLines.length;c++){
                                event.getBot().sendIRC().message(event.getUser().getNick(),alertLines[c].trim());
                            }
                        }
                    }
                }
            }
        }
    }
    private String getSearchStringForCacheSearch(String locationString){
        String search;
        
        if(locationString.matches("[0-9]{5}"))
            search = locationString;
        else if (locationString.matches("[a-zA-Z]{2}\\/[a-zA-Z\\_]+")){
            search = locationString.split("/")[1].replaceAll("_", " ")+", "+locationString.split("/")[0];
        }
        else
            search = locationString.replaceAll("_", " ").replace("/",", ");
        
        return(search);
    }
    
    private void parseLocationFromMessage(String message){
        String[] msgSplit = message.split(" ",2);
        String command;
        if (msgSplit.length>1)
            command = msgSplit[1];
        else
            command="";
        if(command.matches("[a-zA-Z\\s]+\\,\\s[a-zA-Z]{2}")){
            String[] cityState = command.split(",");
            location = cityState[1].trim()+"/"+cityState[0].trim().replaceAll(" ", "_");
        }
        else if(command.matches("[0-9]{5}")){
            location = command;
        }
    }
    
    private boolean startAlertTime(Thread alertThread){
        try{
            //Thread t = new Thread(alertUpdater);
            alertUpdater.giveT(alertThread);
            alertThread.start();
            return(true);
        }
        catch(Exception ex){
            ex.printStackTrace();
            return(false);
        }
    }
    public class AlertTime implements Runnable {
        Thread t;                     // Thread for weather alert updates
        boolean updateAlerts;         // Boolean to start or stop alert updates
        String channel;               // Channel to announce updates to
        int time;                     // Time inbetween each update
        AlertTime(String chan, boolean update, int interval) {
            this.time = interval;
            this.updateAlerts = update;
            this.channel=chan;
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
                    //System.out.println("SEND MESSAGE NAO");
                    boolean updated = updateCurrentAlerts(readUrl(alertUrl(stockZip)));
                    // Global.bot.sendIRC().message("#rapterverse", "STUFF");
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        private boolean updateCurrentAlerts(String jsonData) {
            JSONParser parser = new JSONParser();
            ArrayList<String> alertType = new ArrayList<>();
            ArrayList<String> alertExpiration = new ArrayList<>();
            ArrayList<String> alertText = new ArrayList<>();
            try{
                JSONObject alertJSON = (JSONObject) parser.parse(jsonData);
                JSONArray alertArrayJSON = (JSONArray) alertJSON.get("alerts");
                if (!alertArrayJSON.isEmpty()){
                    for (int i=0;i<alertArrayJSON.size();i++){
                        JSONObject alert = (JSONObject) alertArrayJSON.get(i);
                        alertType.add((String) alert.get("description"));
                        alertExpiration.add((String) alert.get("expires"));
                        alertText.add((String) alert.get("message"));
                        //System.out.println(alertType.get(alertType.size()-1));
                    }
                    
                    ArrayList<String> locationData = getLocationData(stockZip);
                    String cityState = locationData.get(0)+", "+locationData.get(1);
                    String zip = locationData.get(2);
                    
                    if (!localCache.containsEntry( zip,"alert")){
                        WeatherLog alert = new WeatherLog( cityState,  zip,alertType,alertExpiration, alertText);
                        localCache.add(alert);
                        Global.bot.sendIRC().message(channel, Colors.RED+Colors.BOLD+alert.getFormattedResponse());
                    }
                    WeatherLog oldAlert = localCache.getCacheEntry(zip, "alert");
                    for (int i=0;i<alertType.size();i++){
                        
                        if (oldAlert.getAlertType().contains(alertType.get(i))){
                            for (int j=0;i<oldAlert.getAlertType().size();i++){
                                localCache.getCacheEntry(zip, "alert").updateExpiration(j,alertExpiration.get(i));
                            }
                        }
                        else if (!oldAlert.getAlertType().contains(alertType.get(i))){
                            localCache.getCacheEntry(zip, "alert").addAlert(alertType.get(i), alertExpiration.get(i), alertText.get(i));
                            String response = Colors.RED+Colors.BOLD+"WEATHER ALERT FOR: " + Colors.NORMAL+cityState+ Colors.BOLD+" Description: "+Colors.NORMAL+alertType.get(i)+Colors.BOLD+" Ending: "+Colors.NORMAL+alertExpiration.get(i);
                            Global.bot.sendIRC().message(channel, response);
                        }
                    }
                    return(true);
                }
                else
                    return(false);
            }catch (Exception ex){
                ex.printStackTrace();
                return(false);
            }
        }
    }
    
    private String getCurrentAlerts(String jsonData) {
        JSONParser parser = new JSONParser();
        ArrayList<String> alertType = new ArrayList<>();
        ArrayList<String> alertExpiration = new ArrayList<>();
        ArrayList<String> alertText = new ArrayList<>();
        try{
            JSONObject alertJSON = (JSONObject) parser.parse(jsonData);
            JSONArray alertArrayJSON = (JSONArray) alertJSON.get("alerts");
            if (!alertArrayJSON.isEmpty()){
                for (int i=0;i<alertArrayJSON.size();i++){
                    JSONObject alert = (JSONObject) alertArrayJSON.get(i);
                    alertType.add((String) alert.get("description"));
                    alertExpiration.add((String) alert.get("expires"));
//                    System.out.println(alertType.get(alertType.size()-1));
                    alertText.add((String) alert.get("message"));
                }
                
                ArrayList<String> locationData = getLocationData(location);
                String cityState = locationData.get(0)+", "+locationData.get(1);
                WeatherLog alert = new WeatherLog( cityState,  locationData.get(2),alertType,alertExpiration, alertText);
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
            
            for (int i=0;i<forecastDay.size();i++){
                JSONObject period = (JSONObject) forecastDay.get(i);
                JSONObject high = (JSONObject) period.get("high");
                highF.add((String) high.get("fahrenheit"));
                highC.add((String) high.get("celsius"));
                JSONObject low = (JSONObject) period.get("low");
                lowF.add((String) low.get("fahrenheit"));
                lowC.add((String) low.get("celsius"));
                forecastConditions.add((String) period.get("conditions"));
            }
            
            ArrayList<String> locationData = getLocationData(location);
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
    private ArrayList<String> getLocationData(String location) throws Exception{
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
            try{
                ArrayList<String> city = new ArrayList<>();
                ArrayList<String> state = new ArrayList<>();
                ArrayList<String> zip = new ArrayList<>();
                
                JSONObject jsonObject = (JSONObject) parser.parse(jsonData);
                jsonObject = (JSONObject) jsonObject.get("response");
                JSONArray locationJSON = (JSONArray) jsonObject.get("results");
                
                for (int i=0;i<locationJSON.size();i++){
                    JSONObject period = (JSONObject) locationJSON.get(i);
                    city.add((String) period.get("city"));
                    state.add((String) period.get("state"));
                    //zip.add((String) period.get("zip"));
                }
                int randLocation = (int) (Math.random()*locationJSON.size());
                locationData.add(city.get(randLocation));
                locationData.add(state.get(randLocation));
//                locationData.add(zip.get(randLocation));
                return(locationData);
                
            }catch (Exception e){
                e.printStackTrace();
                locationData.add("Error");
                locationData.add("Error");
                locationData.add("Error");
            }
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
            JSONObject weatherTemp = (JSONObject) jsonObject.get("current_observation");
            JSONObject locationData = (JSONObject) weatherTemp.get("display_location");
            zip =  (String) locationData.get("zip");
            cityState = (String)locationData.get("full");
            
            // GRABBING ALL THE WEATHER DATA
            JSONObject currentWeather = weatherTemp;//(JSONObject) weatherTemp.get("estimated");
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
//            System.out.println(ex.getMessage());
            return("ERROR: Location Not Found");
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
}
