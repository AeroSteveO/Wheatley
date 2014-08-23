/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package Wheatley;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.Date;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.pircbotx.*;
import org.pircbotx.hooks.*;
import org.pircbotx.hooks.events.*;
import org.pircbotx.hooks.types.*;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author tangd
 */
public class WeatherModule extends ListenerAdapter
{
    public static final int GOOGLE = 1,
            WUNDERGROUND = 0;
    WeatherFetcher weatherModule;
    private LinkedHashMap<String, ForecastLog> cachedForecast = new LinkedHashMap<String, ForecastLog>();
    private LinkedHashMap<String, WeatherLog> cachedWeather = new LinkedHashMap<String, WeatherLog>();
    private LinkedHashSet<String> enabledChannels = new LinkedHashSet<String>();
    private String defaultSearch = "47906";
    boolean _metricFirst;

    
    public void loadConfig(Elements configData) {
        
//        Elements channels = configData.select("enabledChannel");
//        for(Element e : channels) {
//        enabledChannels.add(e.attr("name"));
//        }
//        defaultSearch = configData.select("default").attr("location");
//        _metricFirst = configData.select("useMetric").attr("value").equals("1");
//        int weatherSelector = Integer.parseInt(configData.select("source").attr("value"));
//        switch(weatherSelector) {
//        case WeatherModule.GOOGLE: {
//        weatherModule = new Google(_metricFirst);
//        break;
//        }
//        case WeatherModule.WUNDERGROUND: {
//        weatherModule = new Wunderground(_metricFirst);
//        break;
//        }
//        default: {
//        weatherModule = new Wunderground(_metricFirst);
//        }
//        }
        
    }
    
    public void onMessage(MessageEvent event) throws Exception {
        
        handleWeather(event);
        
    }
    public void onMessage(PrivateMessageEvent event) throws Exception {
        handleWeather(event);
    }
    public void handleWeather(GenericMessageEvent event) {
        String s = event.getMessage().trim();  //Get the message string
        String location = null;
        if (s.startsWith("!w")) {
            //If we are getting weather.  Do this so that the logic for weather
            //is written only once
            if (s.equals("!w")) {
                //If we are getting the weather for default location
                location = defaultSearch;
            }
            else if (s.matches("!w\\s+[^\\s]+.*")) {
                //If this matches !w adfasdf
                location = s.substring(2).trim();
            }
            WeatherLog log = cachedWeather.get(location);
            if (log != null && log.getExpiration() < (new Date()).getTime()) {
                //This means that the cache is fresh
                event.respond(log.getFormattedWeather());
            }
            else {
                log = weatherModule.getWeather(location);
                cachedWeather.put(location, log);
                event.respond(log.getFormattedWeather());
            }
        } else if (s.startsWith("!forecast")) {
            if (s.equals("!forecast")) {
                //If we are getting the forecast for the default location
                location = defaultSearch;
            }
            else if (s.matches("!forecast\\s+[^\\s]+.*")) {
                //If this matches !forecast asdfasdf
                location = s.substring(9).trim();
            }
            ForecastLog log = cachedForecast.get(location);
            if (log != null && log.getExpiration() < (new Date()).getTime()) {
                //This means that the cache is fresh
                event.respond(log.getFormattedForecast());
            }
            else {
                log = weatherModule.getForecast(location);
                cachedForecast.put(location, log);
                event.respond(log.getFormattedForecast());
            }
        }
    }
    /*
    * This is legacy code from PircBot, not pircbotx
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
    if(enabledChannels.contains(channel)) {
    if(message.equals("!w")) {
    //              mainBot.sendMessage(channel, weatherModule.getWeather(defaultSearch));
    }
    else if(message.startsWith("!w ")) {
    //                mainBot.sendMessage(channel, weatherModule.getWeather(message.substring(3)));
    }
    else if(message.equals("!forecast")) {
    //              mainBot.sendMessage(channel, weatherModule.getForecast(defaultSearch));
    }
    else if(message.startsWith("!forecast ")) {
    //                mainBot.sendMessage(channel, weatherModule.getForecast(message.substring(10)));
    }
    }
    }
    */
    /*
    public void register() {
    //     mainBot = b;
    //     mainBot.messageHandlers.add((MessageHandler)this);
    }
    */
    public interface WeatherFetcher {
        public WeatherLog getWeather(String searchString);
        public ForecastLog getForecast(String searchString);
    }

}
