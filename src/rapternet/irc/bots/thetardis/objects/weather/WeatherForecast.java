/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package rapternet.irc.bots.thetardis.objects.weather;

import java.util.ArrayList;
import org.pircbotx.Colors;

/**
 *
 * @author Stephen
 */
public class WeatherForecast extends WeatherBasic {
    private String observationTime;    // Time the weather was last updated
    private ArrayList<String> weekDay; // Day of the week
    private ArrayList<String> highF;   // array containing all the high temps (F) for the week
    private ArrayList<String> highC;   // array containing all the high temps (C) for the week
    private ArrayList<String> lowF;    // array containing all the low temps (F) for the week
    private ArrayList<String> lowC;    // array containing all the low temps (C) for the week
    private ArrayList<String> forecastConditions; // Array contiaining the forecast conditions for each day of the week
    
    public WeatherForecast(LocationData location, ArrayList<String> highTempF, ArrayList<String> lowTempF, ArrayList<String> highTempC, ArrayList<String> lowTempC, ArrayList<String> weather, ArrayList<String> day, String pretty){
        super(location, 60);
        this.weekDay = day;
        this.highF = highTempF;
        this.lowF = lowTempF;
        this.highC = highTempC;
        this.lowC = lowTempC;
        this.observationTime = pretty;
        this.forecastConditions = weather;
    }
    
    @Override
    public WeatherType getType(){
        return WeatherType.FORECAST;
    }
    
    @Override
    public String getFormattedResponse(){
        String response;
        // FORECAST CACHE FORMATTED RESPONSE
        response =(Colors.BOLD+this.getCityState()+" Forecast "+Colors.NORMAL+"(High/Low); "+Colors.BOLD+"Updated: "+Colors.NORMAL+this.observationTime.split("on")[0].trim()+"; ");
        
        for (int i=0;i<7;i++){ // Add 7 days to the response (can change for more or less)
            response = response+(Colors.BOLD+this.weekDay.get(i)+": "+Colors.NORMAL+this.forecastConditions.get(i)+", "+this.highF.get(i)+"/"+this.lowF.get(i)+"°F ("+this.highC.get(i)+"/"+this.lowC.get(i)+"°C); ");
        }
        return response;
    }

    @Override
    public ArrayList<String> getExtendedResponseArray() {
        throw new UnsupportedOperationException("ExtendedResponseArray Unavailable for this type of Weather Log");
    }
}
