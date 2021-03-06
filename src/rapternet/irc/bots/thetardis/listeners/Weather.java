/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package rapternet.irc.bots.thetardis.listeners;

import rapternet.irc.bots.thetardis.objects.weather.LocationData;
import rapternet.irc.bots.thetardis.objects.weather.WeatherAlerts;
import rapternet.irc.bots.thetardis.objects.weather.WeatherCache;
import rapternet.irc.bots.thetardis.objects.weather.WeatherBasic;
import rapternet.irc.bots.thetardis.objects.weather.WeatherConditions;
import rapternet.irc.bots.thetardis.objects.weather.WeatherForecast;
import rapternet.irc.bots.thetardis.objects.weather.WeatherType;
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
import rapternet.irc.bots.wheatley.listeners.Global;

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
 *    WeatherBasic
 *    AlertTime (Built in)
 *    LocationData
 *    WeatherType
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
    String key;
    String stockZip = "77002";          // Stock location for weather/alerts/forecast/auto alerts
    WeatherCache localCache = new WeatherCache(); // Initiate cache of weather data
    
    
    // Initiate Auto-Alert System
    String alertChannel = "#rapterverse";              // Channel to send weather alert updates to
    int alertUpdateTime = 15*60;                  // 15 min converted to seconds
    boolean updateAlerts = true;                  // True if the bot should send weather alert updates
    AlertTime alertUpdater = new AlertTime(alertChannel,updateAlerts,alertUpdateTime,stockZip);     // Initiate auto-alert object
    Thread t = new Thread(alertUpdater);          // Give it a thread to run in
    boolean started = startAlertTime(t);          // Start the auto-alert thread
    
    public Weather() {
        if (!Global.settings.contains("wundergroundapi")) {
            Global.settings.create("wundergroundapi","AddHere");
            System.out.println("ERROR: NO WUNDERGROUND API KEY");
        }
        key = Global.settings.get("wundergroundapi");
    }
    
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        String location;
        String message = Colors.removeFormattingAndColors(event.getMessage().toLowerCase());
        String[] msgSplit = message.split(" ",2);
        boolean locationDataRetrieved = false;
        LocationData locationData = null;
        
        if ((event.getBot().getUserChannelDao().containsUser("theTardis") && !event.getBot().getUserChannelDao().getChannels(event.getBot().getUserChannelDao().getUser("theTardis")).contains(event.getChannel())) || !event.getBot().getUserChannelDao().containsUser("theTardis")) {
            
            if (message.equalsIgnoreCase("!nuke local cache")&&event.getUser().getNick().equalsIgnoreCase(Global.botOwner)&&event.getUser().isVerified()){
                localCache.clear();
                event.respond("Local Cache Nuked");
            }
            if ((msgSplit[0].equals("!w")||msgSplit[0].equals("!weather")
                    ||msgSplit[0].equals("!f")||msgSplit[0].equals("!forecast")
                    ||msgSplit[0].equals("!a")||msgSplit[0].equals("!alerts")||msgSplit[0].equals("!alert"))){
                
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
                        event.respond("Location not found: Parser and GeoLookup failed to decypher what you input");
                    }
                    locationDataRetrieved = true;
                    location = (locationData.getState() + "/" + locationData.getCityForURL());
                }
                
                if(message.startsWith("!w")){
                    
                    String search = getSearchStringForCacheSearch(location);
                    
                    if(!localCache.isEmpty() && localCache.containsEntry(search, WeatherType.WEATHER)){
                        event.getBot().sendIRC().message(event.getChannel().getName(),localCache.getCacheEntry(search, WeatherType.WEATHER).getFormattedResponse());
                    }
                    else{
                        event.getBot().sendIRC().message(event.getChannel().getName(),getCurrentWeather(location));
                    }
                    
                    if(!localCache.isEmpty() && localCache.containsEntry(search, WeatherType.WEATHER)){
                        ArrayList<String> alertResponses = localCache.getFormattedAlertArray(search);
                        if (!alertResponses.get(0).equalsIgnoreCase("No Current Weather Alerts"))
                            for (int i=0;i<alertResponses.size();i++){
                                event.getBot().sendIRC().message(event.getChannel().getName(),alertResponses.get(i));
                            }
                    }
                    
                    else{
                        if (!locationDataRetrieved){
                            locationData = localCache.getCacheEntry(search, WeatherType.WEATHER).getLocationData();
                        }
                        
                        ArrayList<String> alertResponse = getCurrentAlerts(location, locationData);
                        
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
                    
                    if(!localCache.isEmpty() && localCache.containsEntry(search, WeatherType.FORECAST)){
                        event.getBot().sendIRC().message(event.getChannel().getName(),localCache.getCacheEntry(search, WeatherType.FORECAST).getFormattedResponse());
                    }
                    else{
                        event.getBot().sendIRC().message(event.getChannel().getName(),getCurrentForecast(location));
                    }
                }// END FORECAST COMMAND LOGIC
                
                else if (message.startsWith("!a")){ // START ALERT COMMAND PROCESSING
                    
                    String search;
                    search = getSearchStringForCacheSearch(location);
                    
                    if(!localCache.isEmpty() && localCache.containsEntry(search, WeatherType.ALERT)){ // START ALERT CACHE PROCESSING
                        if (!message.matches("(?i).*full.*")){ // IF COMMAND IS NOT FOR THE FULL ALERT TEXT
                            ArrayList<String> alertResponses = localCache.getFormattedAlertArray(search);
                            
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
                        ArrayList<String> alertResponse = getCurrentAlerts(location, locationData);
                        
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
                            getCurrentAlerts(location,locationData); // this array doesn't matter, the method just needs to run
                            
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
        if (msgSplit.length == 1 || (message.split(" ").length == 2 && msgSplit[1].equalsIgnoreCase("full"))){
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
        // Remove "full" from the location string
        else if (message.matches("(!alerts|!alert|!a)\\s(([0-9]{5})|([a-z\\s]+\\,\\s[a-z]{2}))(\\sfull)") ||
                message.matches("(!alerts\\sfull|!alert\\sfull|!a\\sfull)\\s(([0-9]{5})|([a-z\\s]+\\,\\s[a-z]{2}))") ){
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
        }
        public void giveT(Thread t) {
            this.t = t;
        }
        @Override
        public void run() {
            while(updateAlerts){
                try {
                    Thread.sleep(time*1000);
                    updateCurrentAlerts(stockZip);
                } catch (Exception ex) {
                }
            }
        }
        private boolean updateCurrentAlerts(String location) throws Exception {
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
                    
                    LocationData locationInfo = getLocationData(stockZip);
                    
                    ArrayList<WeatherBasic> newAlerts = new ArrayList<>();
                    for (int i=0;i<alertType.size();i++){
                        newAlerts.add(new WeatherAlerts(locationInfo,alertType.get(i), alertExpiration.get(i), alertText.get(i)));
                    }
                    
                    if (!localCache.containsEntry( zip, WeatherType.ALERT)){
                        
                        for (int i=0;i<alertType.size();i++){
                            WeatherAlerts alert = new WeatherAlerts(locationInfo, alertType.get(i), alertExpiration.get(i), alertText.get(i));
                            localCache.add(alert);
                        }
                        
                        ArrayList<String> alertResponses = localCache.getFormattedAlertArray(locationInfo.getZip());
                        
                        for (int i=0;i<alertResponses.size();i++){
                            Global.bot.sendIRC().message(channel,alertResponses.get(i));
                        }
                    }
                    else{
                        for (int j=0; j < newAlerts.size(); j++){
                            if (newAlerts.get(j) instanceof WeatherAlerts) {
                                if (localCache.addNewAlert((WeatherAlerts) newAlerts.get(j))){
                                    Global.bot.sendIRC().message(channel,Colors.RED+Colors.BOLD+"WEATHER ALERT "+Colors.NORMAL+Colors.BOLD+"For: " + Colors.NORMAL + locationInfo.toString() + Colors.BOLD+" Description: "+Colors.NORMAL+alertType.get(j)+Colors.BOLD+" Ending: "+Colors.NORMAL+alertExpiration.get(j)+Colors.BOLD+" Type: "+Colors.NORMAL+"'!alerts full [zip]' for the full alert text");
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
    
    private ArrayList<String> getCurrentAlerts(String location, LocationData locationData) throws Exception {
        
        String jsonData = readUrl(alertUrl(location));
        
        ArrayList<String> alertType = new ArrayList<>();
        ArrayList<String> alertExpiration = new ArrayList<>();
        ArrayList<String> alertText = new ArrayList<>();
        try{
            JSONObject alertJSON = (JSONObject) new JSONTokener(jsonData).nextValue();
            JSONArray alertArrayJSON = (JSONArray) alertJSON.get("alerts");
            if (alertArrayJSON!=null && alertArrayJSON.length()!=0){
                for (int i=0;i<alertArrayJSON.length();i++){
                    JSONObject alert = (JSONObject) alertArrayJSON.get(i);
                    alertType.add((String) alert.get("description"));
                    alertExpiration.add((String) alert.get("expires"));
                    alertText.add((String) alert.get("message"));
                }
                
                for (int i=0;i<alertType.size();i++){
                    WeatherAlerts alert = new WeatherAlerts(locationData,alertType.get(i),alertExpiration.get(i), alertText.get(i));
                    localCache.add(alert);
                }
                return(localCache.getFormattedAlertArray(locationData.getZip()));
            }
            else
                return new ArrayList<>(Arrays.asList("No Current Weather Alerts"));
            
            
        }catch (Exception ex){
            ex.printStackTrace();
            return new ArrayList<>(Arrays.asList("Error Parsing Alerts"));
        }
    }
    private String getCurrentForecast(String location) throws Exception {
        
        String jsonData = readUrl(forecastUrl(location));
        
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
            
            LocationData locationData = getLocationData(location);
            if (locationData == null) { // If location data is null, location has not been determined
                return ("Error: Forecast not found");
            }
            
            WeatherForecast forecast = new WeatherForecast( locationData,  highF, lowF, highC, lowC, forecastConditions, weekDay, date);
            response = forecast.getFormattedResponse();
            localCache.add(forecast);
            return(response);
            
        }
        catch(Exception ex){
            return("Error: Forecast not found");
        }
    }
    
    private LocationData getLocationData(String location) {
        LocationData locationData;
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
                locationData = new LocationData(locationJSON.getString("zip"), locationJSON.getString("city"), locationJSON.getString("state"));
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
            LocationData loc = new LocationData(zip, cityState.split(",")[0].trim(), cityState.split(",")[1].trim());
            
            // GRABBING ALL THE WEATHER DATA
            JSONObject currentWeather = weatherTemp;//(JSONObject) weatherTemp.get("estimated");
            tempString = currentWeather.getString("temperature_string");
            weather = currentWeather.getString("weather");
            windDir = currentWeather.getString("wind_dir");
            windMPH = currentWeather.getString("wind_mph")+" mph";
            windKPH = currentWeather.getString("wind_kph")+" Kph";
            humidity = currentWeather.getString("relative_humidity");
            observationTime =currentWeather.getString("observation_time").split("Last Updated on",2)[1];
            WeatherConditions weatherData = new WeatherConditions(loc, weather, humidity, tempString, windMPH, windKPH, windDir, observationTime);
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