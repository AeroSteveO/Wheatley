/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Objects;

import java.util.ArrayList;
import org.joda.time.DateTime;
import org.pircbotx.Colors;

/**
 *
 * @author Stephen
 */
public class WeatherForecast extends WeatherBasic{
    private String observationTime; // FORECAST AND WEATHER
    private ArrayList<String> weekDay; // FORECAST
    private ArrayList<String> highF;   // FORECAST
    private ArrayList<String> highC;   // FORECAST
    private ArrayList<String> lowF;    // FORECAST
    private ArrayList<String> lowC;    // FORECAST
    private ArrayList<String> forecastConditions; // FORECAST
    
    public WeatherForecast(String inputLocation, String inputZip, ArrayList<String> highTempF, ArrayList<String> lowTempF, ArrayList<String> highTempC, ArrayList<String> lowTempC, ArrayList<String> weather, ArrayList<String> day, String pretty){
        this.cityState = inputLocation;
        this.zip = inputZip;
        this.weekDay = day;
        this.highF = highTempF;
        this.lowF = lowTempF;
        this.highC = highTempC;
        this.lowC = lowTempC;
        this.observationTime = pretty;
        this.forecastConditions = weather;
        this.expiration = new DateTime().plusMinutes(60);
    }
    
    @Override
    public String getType(){
        return "forecast";
    }
    
    @Override
    public String getFormattedResponse(){
        String response = "";
        // FORECAST CACHE FORMATTED RESPONSE
        response =(Colors.BOLD+this.cityState+" Forecast "+Colors.NORMAL+"(High/Low); "+Colors.BOLD+"Updated: "+Colors.NORMAL+this.observationTime.split("on")[0].trim()+"; ");
        //weekDay.size()
        for (int i=0;i<7;i++){
            response = response+(Colors.BOLD+this.weekDay.get(i)+": "+Colors.NORMAL+this.forecastConditions.get(i)+", "+this.highF.get(i)+"/"+this.lowF.get(i)+"°F ("+this.highC.get(i)+"/"+this.lowC.get(i)+"°C); ");
        }
        return response;
    }
}
