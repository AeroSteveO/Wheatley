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
public class WeatherAlerts extends WeatherBasic implements WeatherCacheInterface{
    private String alertType;     // Type of the alert (simple type)
    private String alertExpires;  // Expiration of the alert (string from wunderground)
    private String alertText;     // The full text of the alert
    
    public WeatherAlerts(String inputLocation, String inputZip, String alertDescription, String alertExpiration, String alertText){
        this.cityState = inputLocation;
        this.zip = inputZip;
        this.alertType = alertDescription;
        this.alertExpires = alertExpiration;
        this.expiration = new DateTime().plusMinutes(30);
        this.alertText = alertText;
    }
    
    @Override
    public String getType(){
        return "alert";
    }
    
    @Override
    public String getFormattedResponse(){
        String response = "";
        if (!this.containsError())
            response = Colors.RED + Colors.BOLD + "WEATHER ALERT "+Colors.NORMAL+Colors.BOLD+"For: " + Colors.NORMAL+this.cityState+ Colors.BOLD+" Description: "+Colors.NORMAL+this.alertType+Colors.BOLD+" Ending: "+Colors.NORMAL+this.alertExpires;
        else
            response = alertType;
        return response;
    }
    
    @Override
    public ArrayList<String>  getExtendedResponseArray(){
        ArrayList<String> respond = new ArrayList<>();
        
        if (!this.alertType.equalsIgnoreCase("Error Parsing Alerts")&&!this.alertType.equalsIgnoreCase("No Current Weather Alerts")){
        // ^No need to try and get an extended response for a dud alert
            
            String[] alertLines = alertText.split("\\u000A"); // split by new lines in the alert
            respond.add(Colors.BOLD+Colors.RED+"Alert Full Text: "+Colors.NORMAL + alertLines[0]);
            
            for (int i=1;i<alertLines.length;i++){
                respond.add(alertLines[i]);
            }
        }
        else{
            respond.add(alertType); // Return that theres no alert extended response if there is none
        }
        return (respond);
        
    }
    public String getAlertType(){
        return this.alertType;
    }
    
    public String getExpiration(){
        return this.alertExpires;
    }
    
    public void updateExpiration(String expiration){
        this.alertExpires = expiration;
        this.expiration = new DateTime().plusMinutes(30); // updates the expiration of the alert cache entry if its still valid
    }

    @Override
    public String getExtendedResponse() {
        throw new UnsupportedOperationException("getExtendedResponse Unavailable for this type of Weather Log");
    }
}