/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import Objects.WeatherAlerts;
import Objects.WeatherCache;
import Objects.WeatherCacheInterface;
import Objects.WeatherConditions;
import Objects.WeatherForecast;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import java.io.IOException;
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author Stephen
 * Original Bot: LilWayne
 *
 * Requirements:
 * - APIs
 *    JSON (AOSP JSON parser)
 * - Custom Objects
 *    WeatherCache
 *    WeatherAlerts
 *    WeatherConditions
 *    WeatherForecast
 *    WeatherCacheInterface
 *    AlertTime (Built in)
 * - Linked Classes
 *    Global
 *
 * Activate Command with:
 *      !w [location]
 *      !weather [location]
 *          Location can be:
 *            [cities, states 2 digit code]
 *            [zip]
 *          Responds with the weather for that cities, if nothing is input,
 *          Responds with the weather for the stock zip code
 *      !f [location]
 *      !forecast [location]
 *          Location can be:
 *            [cities, states 2 digit code]
 *            [zip]
 *          Responds with the 7 day forecast for that cities, if nothing is input,
 *          Responds with the 7 day forecast for the stock zip code
 *      !a [location]
 *      !alert [location]
 *      !alerts [location]
 *          Location can be:
 *            [cities, states 2 digit code]
 *            [zip]
 *          Responds with the any available weather alerts for that cities, if nothing is input,
 *          Responds with the any available weather alerts for the stock zip code
 *      !a full [location]
 *      !alert full [location]
 *      !alerts full [location]
 *      !a [location] full
 *      !alert [location] full
 *      !alerts [location] full
 *          Location can be:
 *            [cities, states 2 digit code]
 *            [zip]
 *          Responds with a PM containing the full text of the current alerts
 *      !nuke local cache
 *          Clears the local cache of weather, alerts, and forecast data
 *          Can help clear up errors
 *
 * Latent Functionality
 *      Automatically updates the main channel with weather alerts
 */

public class Weather extends ListenerAdapter{
    String key = "***REMOVED***";    // API KEY, DO NOT LOSE
    String stockZip = "77002";          // Stock location for weather/alerts/forecast/auto alerts
    WeatherCache localCache = new WeatherCache(); // Initiate cache of weather data
    
    
    // Initiate Auto-Alert System
    String alertChannel = "#rapterverse";              // Channel to send weather alert updates to
    int alertUpdateTime = 15*60;                  // 15 min converted to seconds
    boolean updateAlerts = true;                  // True if the bot should send weather alert updates
    AlertTime alertUpdater = new AlertTime(alertChannel,updateAlerts,alertUpdateTime,stockZip);     // Initiate auto-alert object
    Thread t = new Thread(alertUpdater);          // Give it a thread to run in
    boolean started = startAlertTime(t);          // Start the auto-alert thread
    
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        String location = null;
        String message = Colors.removeFormattingAndColors(event.getMessage().toLowerCase());
        String[] msgSplit = message.split(" ",2);
        boolean locationDataRetrieved = false;
        ArrayList<String> locationData = new ArrayList<>();
        
        if (!event.getBot().getUserChannelDao().getChannels(event.getBot().getUserChannelDao().getUser("theTardis")).contains(event.getChannel())) {
            
            if (message.equalsIgnoreCase("!nuke local cache")&&event.getUser().getNick().equalsIgnoreCase(Global.botOwner)&&event.getUser().isVerified()){
                localCache.clear();
                event.respond("Local Cache Nuked");
            }
            if ((msgSplit[0].equalsIgnoreCase("!w")||msgSplit[0].equalsIgnoreCase("!weather")
                    ||msgSplit[0].equalsIgnoreCase("!f")||msgSplit[0].equalsIgnoreCase("!forecast")
                    ||msgSplit[0].equalsIgnoreCase("!a")||msgSplit[0].equalsIgnoreCase("!alerts")||msgSplit[0].equalsIgnoreCase("!alert"))){
                
                
                location = parseLocationFromMessage(message);
                
                if (location == null) { // When all else fails, geo lookup
                    if (msgSplit[1].startsWith("full")){
                        msgSplit[1]=msgSplit[1].replace("full","");
                    }
                    else if (msgSplit[1].endsWith("full")){
                        msgSplit[1]=msgSplit[1].replace("full","");
                    }
                    locationData = getLocationData(msgSplit[1].trim().replaceAll(" ", "_"));
                    if (locationData == null) { // When geo lookup and location parsing fail, location has not been determined
                        //event.getBot().sendIRC().message(event.getChannel().getName(),"Error Retrieving Location Data");
                        event.respond("Location not found: Parser and GeoLookup failed to decypher what you input");
                    }
                    locationDataRetrieved = true;
                    location = (locationData.get(1).trim()+"/"+locationData.get(0).trim().replaceAll(" ", "_"));
                }
//                if (msgSplit.length==1||(message.split(" ").length==2&&msgSplit[1].equalsIgnoreCase("full"))){
//                    location = stockZip;
//                }
//
//                // Now this is a nice location to parse
//                // If the location is formatted as "city, st" where st is the state 2 letter string
//                // Or of course just a straight zip, we love zips
//                else if (msgSplit[1].matches("([0-9]{5})|([a-z\\s]+\\,\\s[a-z]{2})")){
//                    location = parseLocationFromMessage(message);
//                }
//
//                // Remove "full" from the front of the location string
//                else if (message.matches("(!alerts\\sfull|!alert\\sfull|!a\\sfull)\\s(([0-9]{5})|([a-z\\s]+\\,\\s[a-z]{2}))")){
//                    location = parseLocationFromMessage(message.split(" ",2)[1]);
//                }
//
//                // Remove "full" from the end of the location string
//                else if (message.matches("(!alerts|!alert|!a)\\s(([0-9]{5})|([a-z\\s]+\\,\\s[a-z]{2}))(\\sfull)")){
//                    location = parseLocationFromMessage(message.split("full")[0].trim());
//                }
//
//                // The ugliest of parsing parts
//                else {
//                    if (msgSplit[1].startsWith("full")){
//                        msgSplit[1]=msgSplit[1].replace("full","");
//                    }
//                    else if (msgSplit[1].endsWith("full")){
//                        msgSplit[1]=msgSplit[1].replace("full","");
//                    }
//
//                    locationData = getLocationData(msgSplit[1].trim().replaceAll(" ", "_"));
//                    if (locationData == null) { // If location data is null, location has not been determined
//                        event.getBot().sendIRC().message(event.getChannel().getName(),"Error Retrieving Location Data");
//                        return;
//                    }
//
//
//                    location = locationData.get(1).trim()+"/"+locationData.get(0).trim().replaceAll(" ", "_");
//                    locationDataRetrieved = true;
//                }
                
                if(message.startsWith("!w")){
                    
                    String search = getSearchStringForCacheSearch(location);
                    
                    if(localCache.size()>0&&localCache.containsEntry(search,"weather")){
                        event.getBot().sendIRC().message(event.getChannel().getName(),localCache.getCacheEntry(search,"weather").getFormattedResponse());
                    }
                    else{
                        event.getBot().sendIRC().message(event.getChannel().getName(),getCurrentWeather(location));
                    }
                    
                    if(localCache.size()>0&&localCache.containsEntry(search,"alert")){
                        ArrayList<String> alertResponses = localCache.getFormattedAlertArray(search,"alert");
                        if (!alertResponses.get(0).equalsIgnoreCase("No Current Weather Alerts"))
                            for (int i=0;i<alertResponses.size();i++){
                                event.getBot().sendIRC().message(event.getChannel().getName(),alertResponses.get(i));
                            }
                    }
                    
                    else{
                        if (!locationDataRetrieved){
                            locationData = localCache.getCacheEntry(search,"weather").getLocationData();
                        }
                        
                        ArrayList<String> alertResponse = getCurrentAlerts(location,locationData);
                        
                        if (alertResponse.size()>0){
                            if (!alertResponse.get(0).equalsIgnoreCase("No Current Weather Alerts"))
                                for (int i=0;i<alertResponse.size();i++){
                                    event.getBot().sendIRC().message(event.getChannel().getName(),alertResponse.get(i));
                                }
                        }
                    }
                }
                
                else if (message.startsWith("!f")){ // START FORECAST COMMAND LOGIC
                    
                    String search = getSearchStringForCacheSearch(location);
                    
                    if(localCache.size()>0&&localCache.containsEntry(search,"forecast")){
                        event.getBot().sendIRC().message(event.getChannel().getName(),localCache.getCacheEntry(search,"forecast").getFormattedResponse());
                    }
                    else{
                        event.getBot().sendIRC().message(event.getChannel().getName(),getCurrentForecast(location));
                    }
                }// END FORECAST COMMAND LOGIC
                
                else if (message.startsWith("!a")){ // START ALERT COMMAND PROCESSING
                    
                    String search;
                    search = getSearchStringForCacheSearch(location);
                    
                    if(localCache.size()>0&&localCache.containsEntry(search,"alert")){ // START ALERT CACHE PROCESSING
                        if (!message.matches("(?i).*full.*")){ // IF COMMAND IS NOT FOR THE FULL ALERT TEXT
                            ArrayList<String> alertResponses = localCache.getFormattedAlertArray(search,"alert");
                            
                            for (int i=0;i<alertResponses.size();i++){
                                event.getBot().sendIRC().message(event.getChannel().getName(),alertResponses.get(i));
                            }
                        }
                        else { // IF THE COMMAND IS FOR THE FULL ALERT TEXT
                            ArrayList<String> alertFullText = localCache.getAllAlertsLongResponse(search);
                            for (int c = 0;c<alertFullText.size();c++){
                                event.getBot().sendIRC().message(event.getUser().getNick(),alertFullText.get(c));
                            }
                        }
                    } // END CACHE PROCESSING
                    else{
                        
                        if (!locationDataRetrieved){
                            locationData = getLocationData(location);
                            if (locationData == null) { // If location data is null, location has not been determined
                                event.getBot().sendIRC().message(event.getChannel().getName(),"Error Retrieving Location Data");
                                return;
                            }
                            
                        }
                        ArrayList<String> alertResponse = getCurrentAlerts(location,locationData);
                        
                        if (!message.matches("(?i).*full.*")){
                            
                            if (alertResponse.size()>0){
                                if (alertResponse.get(0).equalsIgnoreCase("Error Parsing Alerts")||alertResponse.get(0).equalsIgnoreCase("No Current Weather Alerts")||alertResponse.get(0).equalsIgnoreCase("ERROR")){
                                    event.getBot().sendIRC().message(event.getChannel().getName(),alertResponse.get(0).trim());
                                }
                                else {
                                    for (int i=0;i<alertResponse.size();i++){
                                        event.getBot().sendIRC().message(event.getChannel().getName(),alertResponse.get(i));
                                    }
                                }
                            }
                        }
                        else {
                            ArrayList<String> derpina = getCurrentAlerts(location,locationData); // this array doesn't matter, the method just needs to run
                            
                            ArrayList<String> alertFullText = localCache.getAllAlertsLongResponse(search);
                            
                            for (int i=0;i<alertFullText.size();i++){
                                event.getBot().sendIRC().message(event.getUser().getNick(),alertFullText.get(i));
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
    
    private String parseLocationFromMessage(String message){
        String[] msgSplit = message.split(" ",2);
        String location;
        
        // No input location tells us to use the stock zipcode
        if (msgSplit.length==1||(message.split(" ").length==2&&msgSplit[1].equalsIgnoreCase("full"))){
            return(stockZip);
        }
        // Catch regular zip code inputs
        else if (msgSplit[1].matches("([0-9]{5})")){
            return(msgSplit[1]); // Return the plain zip, thats a nice happy location string
        }
        // Now this is a nice location to parse
        // If the location is formatted as "city, st" where st is the state 2 letter string
        // Or of course just a straight zip, we love zips
        else if (msgSplit[1].matches("([a-z\\s]+\\,\\s[a-z]{2})")) {
            location = (msgSplit[1]);
        }
        
        // Remove "full" from the front of the location string
//        else if (message.matches("(!alerts\\sfull|!alert\\sfull|!a\\sfull)\\s(([0-9]{5})|([a-z\\s]+\\,\\s[a-z]{2}))")){
////            location = (message.split(" ",2)[1]);
//            location = (message.replaceAll("(!alerts|!alerts|!a)\\sfull", "").trim());
//        }
        
        // Remove "full" from the location string
        else if (message.matches("(!alerts|!alert|!a)\\s(([0-9]{5})|([a-z\\s]+\\,\\s[a-z]{2}))(\\sfull)") ||
                message.matches("(!alerts\\sfull|!alert\\sfull|!a\\sfull)\\s(([0-9]{5})|([a-z\\s]+\\,\\s[a-z]{2}))") ){
//            location = (message.split("full")[0].trim());
            location = (message.replace("full", "").replaceAll("(!alerts|!alerts|!a)", "").trim());
        }
        
        // Instead of geo lookup right here, do it in the onMessage method, and use
        // The method variables there so geo lookup doesn't get repeated
        else {
            return null;
        }
        
        String response="";
        
        if(location.matches("[a-zA-Z\\s]+\\,\\s[a-zA-Z]{2}")){
            String[] cityState = location.split(",");
            response = cityState[1].trim()+"/"+cityState[0].trim().replaceAll(" ", "_");
        }
        else if(location.matches("[0-9]{5}")){
            response = location;
        }
        return (response);
    }
    
//    private String parseLocationFromMessage(String message){
//        String[] msgSplit = message.split(" ",2);
//        String command;
//        String response="";
//        if (msgSplit.length>1)
//            command = msgSplit[1];
//        else
//            command="";
//        if(command.matches("[a-zA-Z\\s]+\\,\\s[a-zA-Z]{2}")){
//            String[] cityState = command.split(",");
//            response = cityState[1].trim()+"/"+cityState[0].trim().replaceAll(" ", "_");
//        }
//        else if(command.matches("[0-9]{5}")){
//            response = command;
//        }
//        return (response);
//    }
    
    private boolean startAlertTime(Thread alertThread){
        try{
            alertUpdater.giveT(alertThread);
            alertThread.start();
            return(true);
        }
        catch(Exception ex){
            System.out.println("ALERT UPDATER FAILED TO START");
            ex.printStackTrace();
            return(false);
        }
    }
    public class AlertTime implements Runnable {
        Thread t;                     // Thread for weather alert updates
        boolean updateAlerts;         // Boolean to start or stop alert updates
        String channel;               // Channel to announce updates to
        int time;                     // Time inbetween each update
        String zip;
        
        AlertTime(String chan, boolean update, int interval, String zip) {
            this.time = interval;
            this.updateAlerts = update;
            this.channel=chan;
            this.zip = zip;
        }
        public void end() throws InterruptedException{
            this.updateAlerts = false;
            this.t.interrupt();
            this.t.join(1000); //Ensure the thread also closes
        }
        public void giveT(Thread t) {
            this.t = t;
        }
        @Override
        public void run() {
            while(updateAlerts){
                try {
                    Thread.sleep(time*1000);
                    boolean updated = updateCurrentAlerts(stockZip);
                } catch (Exception ex) {
                }
            }
        }
        private boolean updateCurrentAlerts(String location) throws Exception {
//            JSONParser parser = new JSONParser();
            String jsonData = readUrl(alertUrl(location));
            ArrayList<String> alertType = new ArrayList<>();
            ArrayList<String> alertExpiration = new ArrayList<>();
            ArrayList<String> alertText = new ArrayList<>();
            try{
                JSONObject alertJSON = (JSONObject) new JSONTokener(jsonData).nextValue();
                JSONArray alertArrayJSON = (JSONArray) alertJSON.get("alerts");
                if (alertArrayJSON.length()!=0){
                    for (int i=0;i<alertArrayJSON.length();i++){
                        JSONObject alert = (JSONObject) alertArrayJSON.get(i);
                        alertType.add((String) alert.get("description"));
                        alertExpiration.add((String) alert.get("expires"));
                        alertText.add((String) alert.get("message"));
                    }
                    
                    ArrayList<String> locationData = getLocationData(stockZip);
                    
                    String cityState = locationData.get(0)+", "+locationData.get(1);
                    String zip = locationData.get(2);
                    
                    ArrayList<WeatherCacheInterface> newAlerts = new ArrayList<>();
                    for (int i=0;i<alertType.size();i++){
                        newAlerts.add(new WeatherAlerts(cityState, zip,alertType.get(i), alertExpiration.get(i), alertText.get(i)));
                    }
                    
                    if (!localCache.containsEntry( zip,"alert")){
                        
                        for (int i=0;i<alertType.size();i++){
                            WeatherAlerts alert = new WeatherAlerts( cityState, zip,alertType.get(i), alertExpiration.get(i), alertText.get(i));
                            localCache.add(alert);
                        }
                        
                        ArrayList<String> alertResponses = localCache.getFormattedAlertArray(locationData.get(2),"alert");
                        
                        for (int i=0;i<alertResponses.size();i++){
                            Global.bot.sendIRC().message(channel,alertResponses.get(i));
                        }
                    }
                    else{
                        for (int j=0;j<newAlerts.size();j++){
                            if (newAlerts.get(j) instanceof WeatherAlerts){
                                boolean isAlertNew = true;
                                for (int i=0;i<localCache.size();i++){
                                    
                                    if (((WeatherAlerts) localCache.get(i)).getAlertType().equalsIgnoreCase(((WeatherAlerts) newAlerts.get(j)).getAlertType())){
                                        ((WeatherAlerts) localCache.get(i)).updateExpiration(((WeatherAlerts) newAlerts.get(j)).getExpiration());
                                        isAlertNew=false;
                                    }
                                }
                                if (isAlertNew){
//                                WeatherAlerts newAlert = new WeatherAlerts(cityState, zip,alertType.get(j), alertExpiration.get(j), alertText.get(j));
                                    localCache.add(newAlerts.get(j));
                                    Global.bot.sendIRC().message(channel,Colors.RED+Colors.BOLD+"WEATHER ALERT "+Colors.NORMAL+Colors.BOLD+"For: " + Colors.NORMAL+cityState+ Colors.BOLD+" Description: "+Colors.NORMAL+alertType.get(j)+Colors.BOLD+" Ending: "+Colors.NORMAL+alertExpiration.get(j)+Colors.BOLD+" Type: "+Colors.NORMAL+"'!alerts full [zip]' for the full alert text");
                                }
                            }
                        }
                    }
                    return(true);
                }
                else
                    return(false);
            }catch (Exception ex){
                return(false);
            }
        }
    }
    
    private ArrayList<String> getCurrentAlerts(String location, ArrayList<String> locationData) throws Exception {
//        JSONParser parser = new JSONParser();
        
        String jsonData = readUrl(alertUrl(location));
        
        ArrayList<String> alertType = new ArrayList<>();
        ArrayList<String> alertExpiration = new ArrayList<>();
        ArrayList<String> alertText = new ArrayList<>();
        try{
            JSONObject alertJSON = (JSONObject) new JSONTokener(jsonData).nextValue();
            JSONArray alertArrayJSON = (JSONArray) alertJSON.get("alerts");
//            System.out.println(alertArrayJSON);
            if (alertArrayJSON!=null && alertArrayJSON.length()!=0){
                for (int i=0;i<alertArrayJSON.length();i++){
                    JSONObject alert = (JSONObject) alertArrayJSON.get(i);
                    alertType.add((String) alert.get("description"));
                    alertExpiration.add((String) alert.get("expires"));
//                    System.out.println(alertType.get(alertType.size()-1));
                    alertText.add((String) alert.get("message"));
                }
                
                String cityState = locationData.get(0)+", "+locationData.get(1);
                
                for (int i=0;i<alertType.size();i++){
                    WeatherAlerts alert = new WeatherAlerts( cityState,  locationData.get(2),alertType.get(i),alertExpiration.get(i), alertText.get(i));
                    localCache.add(alert);
                }
                return(localCache.getFormattedAlertArray(cityState, locationData.get(2)));
            }
            else
                return new ArrayList<String>(Arrays.asList("No Current Weather Alerts"));
            
            
        }catch (Exception ex){
            ex.printStackTrace();
            return new ArrayList<String>(Arrays.asList("Error Parsing Alerts"));
        }
    }
    private String getCurrentForecast(String location) throws Exception {
        
        String jsonData = readUrl(forecastUrl(location));
        
//        JSONParser parser = new JSONParser();
        String response;
        ArrayList<String> weekDay = new ArrayList<>();
        ArrayList<String> highF = new ArrayList<>();
        ArrayList<String> highC = new ArrayList<>();
        ArrayList<String> lowF = new ArrayList<>();
        ArrayList<String> lowC = new ArrayList<>();
        ArrayList<String> forecastConditions = new ArrayList<>();
        String date = "";
        //simpleforecast
        try{
            JSONObject forecastJSON = (JSONObject) new JSONTokener(jsonData).nextValue();
            forecastJSON = forecastJSON.getJSONObject("forecast");
            forecastJSON = forecastJSON.getJSONObject("simpleforecast");
            JSONArray forecastDay = forecastJSON.getJSONArray("forecastday");
            JSONObject day = forecastDay.getJSONObject(0);
            day = day.getJSONObject("date");
            date = day.getString("pretty");
            
            
            //weekDay = JSONKeyFinder(forecastDay.toString(),"weekday"); //all thats needed for the weekday array
//            weekDay.add("stuff");weekDay.add("stuff");weekDay.add("stuff");weekDay.add("stuff");
//            weekDay.add("stuff");weekDay.add("stuff");weekDay.add("stuff");
            
            for (int i=0;i<forecastDay.length();i++){
                JSONObject period = forecastDay.getJSONObject(i);
                JSONObject high = period.getJSONObject("high");
                highF.add(high.getString("fahrenheit"));
                highC.add(high.getString("celsius"));
                JSONObject low = period.getJSONObject("low");
                lowF.add(low.getString("fahrenheit"));
                lowC.add(low.getString("celsius"));
                forecastConditions.add(period.getString("conditions"));
                
                weekDay.add(period.getJSONObject("date").getString("weekday"));
            }
            
            ArrayList<String> locationData = getLocationData(location);
            if (locationData == null) { // If location data is null, location has not been determined
                return ("Error: Forecast not found");
            }
            
            String cityState = locationData.get(0)+", "+locationData.get(1);
            WeatherForecast forecast = new WeatherForecast( cityState,  locationData.get(2),  highF, lowF, highC, lowC, forecastConditions, weekDay, date);
            response = forecast.getFormattedResponse();
            localCache.add(forecast);
            return(response);
            
        }
        catch(Exception ex){
            return("Error: Forecast not found");
        }
    }
    
    private ArrayList<String> getLocationData(String location) {
        
        ArrayList<String> locationData = new ArrayList<>();
        
        try{
            String jsonData = readUrl(geoLookupUrl(location));
            JSONObject jsonObject = (JSONObject) new JSONTokener(jsonData).nextValue();
            
            if (jsonObject.toString().toLowerCase().matches(".*\"results\".*")){
                ArrayList<String> cities = new ArrayList<>();
                ArrayList<String> states = new ArrayList<>();
                String city;
                String state;
                jsonObject = jsonObject.getJSONObject("response");
                
                JSONArray locationJSON = (JSONArray) jsonObject.get("results");
                
                for (int i=0;i<locationJSON.length();i++){
                    JSONObject period = locationJSON.getJSONObject(i);
                    cities.add(period.getString("city"));
                    states.add(period.getString("state"));
                }
                int randLocation = (int) (Math.random()*locationJSON.length());
                city = (cities.get(randLocation));
                state = (states.get(randLocation));
                locationData = getLocationData((city+", "+state).trim().replaceAll(" ", "_"));
                
                
            } else{
                
                JSONObject locationJSON = jsonObject.getJSONObject("location");
                
                locationData.add(locationJSON.getString("city"));
                locationData.add(locationJSON.getString("state"));
                locationData.add(locationJSON.getString("zip"));
            }
        }catch (Exception ex){
            return null;
        }
        return(locationData);
    }
    
    private String getCurrentWeather(String location) throws Exception {
        String jsonData = readUrl(weatherUrl(location));
        
        String zip;
        String cityState;
        String tempString;
        String observationTime;
        String weather;
        String humidity;
        String windDir;
        String windMPH;
        String windKPH;
        
        try{
            
            // GRABBING ALL THE LOCATION DATA
            JSONObject jsonObject = (JSONObject) new JSONTokener(jsonData).nextValue();
            JSONObject weatherTemp = jsonObject.getJSONObject("current_observation");
            JSONObject locationData = weatherTemp.getJSONObject("display_location");
            zip = locationData.getString("zip");
            cityState = locationData.getString("full");
            
            // GRABBING ALL THE WEATHER DATA
            JSONObject currentWeather = weatherTemp;//(JSONObject) weatherTemp.get("estimated");
            tempString = currentWeather.getString("temperature_string");
            weather = currentWeather.getString("weather");
            windDir = currentWeather.getString("wind_dir");
            windMPH = currentWeather.getString("wind_mph").toString()+" mph";
            windKPH = currentWeather.getString("wind_kph").toString()+" Kph";
            humidity = currentWeather.getString("relative_humidity");
            observationTime =currentWeather.getString("observation_time").split("Last Updated on",2)[1];
            WeatherConditions weatherData = new WeatherConditions(cityState,zip, weather, humidity, tempString, windMPH, windKPH, windDir, observationTime);
            localCache.add(weatherData);
            return(weatherData.getFormattedResponse());
        }
        catch(Exception ex){
            return("ERROR: Location Not Found");
        }
    }
    
    //converts URL to string, primarily used to string-ify json text
    private static String readUrl(String urlString) throws Exception {
        System.out.println(urlString);
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder buffer = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);
            return buffer.toString();
        }catch(IOException ex){
            return(null);
        }finally {
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