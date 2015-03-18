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
public class WeatherAlerts extends WeatherBasic{
    private String alertType;     // ALERT
    private String alertExpires;  // ALERT
    private String alertText;     // ALERT
    
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
        
        if (!this.alertType.equalsIgnoreCase("Error Parsing Alerts")||this.alertType.equalsIgnoreCase("No Current Weather Alerts")){
//                for (int i=0;i<alertText.size();i++){
//                response = response + "Alert Full Text: "+Colors.NORMAL+response+alertText+ " !";
            String[] alertLines = alertText.split("\\u000A");
            
            respond.add(Colors.BOLD+Colors.RED+"Alert Full Text: "+Colors.NORMAL + alertLines[0]);
            
            for (int i=1;i<alertLines.length;i++){
                respond.add(alertLines[i]);
            }
        }
        else{
            respond.add(alertType);
        }
        return (respond);
        
    }
    public String getAlertType(){
        return this.alertType;
    }
    public void updateExpiration(int index, String expiration){
        this.alertExpires = expiration;
        this.expiration = new DateTime().plusMinutes(30);
    }
}