/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Objects.Weather;

import java.util.ArrayList;
import org.joda.time.DateTime;
import org.pircbotx.Colors;

/**
 *
 * @author Stephen
 */
public class WeatherConditions extends WeatherBasic implements WeatherCacheInterface{
    
    private String observationTime; // Time the weather was last updated
    
    private String conditions; // Conditions of the day (sunny, t-storm, etc)
    private String humidity;   // Current percent humidity
    private String temp;       // Current termperature string (contiaining C and F)
    private String windMPH;    // Wind speed in MPH
    private String windKPH;    // Wind speed in KPH
    private String windDir;    // Direction of wind
    
    public WeatherConditions(String inputLocation,String inputZip, String inputWeather, String hum, String tmp, String windImperial, String windMetric, String windDirection, String obsTime) {
        this.conditions = inputWeather;
        if (hum.equals("-999%"))
            hum = null; // Null out humidity instead of throwing crap values to the channel
        this.humidity = hum;
        this.temp = tmp;
        this.windKPH = windMetric;
        this.windMPH = windImperial;
        this.windDir = windDirection;
        this.cityState = inputLocation;
        this.observationTime = obsTime;
        this.zip = inputZip;
        this.expiration = new DateTime().plusMinutes(60);
    }

    @Override
    public String getType(){
        return "weather";
    }
    
    @Override
    public String getFormattedResponse(){
        String response = Colors.BOLD+this.cityState+"; Updated: "+Colors.NORMAL+this.observationTime+"; "+Colors.BOLD+"Conditions: "+Colors.NORMAL+this.conditions+"; "+
                Colors.BOLD+"Temperature: "+Colors.NORMAL+this.temp+"; ";
                if (humidity != null) // Humidity is null when there is no humidity to report
                    response += Colors.BOLD+"Humidity: "+Colors.NORMAL+this.humidity+"; ";
                            
                response += Colors.BOLD+"Wind: "+Colors.NORMAL+this.windMPH+" ("+this.windKPH+") "+this.windDir;
        return response;
    }

    // This is not at all used here
    @Override
    public String getExtendedResponse() {
        throw new UnsupportedOperationException("getExtendedResponse Unavailable for this type of Weather Log");
    }

    // This is not at all used here
    @Override
    public ArrayList<String> getExtendedResponseArray() {
        throw new UnsupportedOperationException("ExtendedResponseArray Unavailable for this type of Weather Log");
    }
}
