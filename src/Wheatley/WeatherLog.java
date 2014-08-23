/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Wheatley;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author tangd
 */
public abstract class WeatherLog {
    private long expiration;                    //Denotes when this log should be discarded
    private long observedTime;                 //When the weather service observed the weather
    private int tempF, tempC, windSpeed;     //Current Conditions
    private String  windDirection;             //Wind Direction
    private String conditions, humidity;      //Humidity % and weather condition
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("h:m a zzz (MMM d, yyyy)");
    public WeatherLog(long ttl, long observed, int tempF, int tempC, int windSpeed, String windDirection, String condition, String humidity) {
        this.expiration = (new Date()).getTime()+ttl;
        this.observedTime = observed;
        this.tempF = tempF;
        this.tempC = tempC;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.conditions = condition;
        this.humidity = humidity;
    }

    public String getConditions() {
        return conditions;
    }

    public long getExpiration() {
        return expiration;
    }

    public String getHumidity() {
        return humidity;
    }

    public long getObservedTime() {
        return observedTime;
    }

    public int getTempC() {
        return tempC;
    }

    public int getTempF() {
        return tempF;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public SimpleDateFormat getDateFormatter() {
        return dateFormatter;
    }
    public String getFormattedWeather() {
        return null;
    }
    public abstract void setFormattedString();
}
