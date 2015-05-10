/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import Utils.TextUtils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Stephen
 *
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    N/A
 * - Utilities
 *    N/A
 * - Linked Classes
 *    N/A
 *
 */
public class Recommendations extends ListenerAdapter{
    private String key = "***REMOVED***";
    private final String USER_AGENT = "Mozilla/5.0";
    
    //https://www.tastekid.com/read/api
    //https://duckduckgo.com/api
    @Override
    public void onMessage(final MessageEvent event) {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        
        if (message.startsWith(Global.commandPrefix)&&!message.matches("([ ]{0,}"+Global.commandPrefix+"{1,}[ ]{0,}){1,}")){
            
            String command = message.split(Global.commandPrefix)[1];
            String[] cmdSplit = command.split(" ");
//            System.out.println(command);
            
            if (cmdSplit[0].equalsIgnoreCase("recommend")){
                if (cmdSplit.length>=2){
                    String search = command.split(" ",2)[1];
                    String url = getTasteKidURL(search);
                    try{
                        String json = sendGet(url);
                        JSONObject similar = (JSONObject) new JSONTokener(json).nextValue();
                        JSONObject results = similar.getJSONObject("Similar");
                        JSONArray info = results.getJSONArray("Results");
                        if (info.length()==0){
                            event.respond("No recommendations found");
                        }
                        else{
                            String type = info.getJSONObject(0).getString("Type");
                            if (!type.equalsIgnoreCase("music")){
                                type+="s";
                            }
                            String recommendations = Colors.RED+"("+WordUtils.capitalize(type)+") "+Colors.NORMAL;
                            for (int i=0;i<info.length()-1;i++){
                                if((recommendations.length()+info.getJSONObject(i).getString("Name").length()+info.getJSONObject(info.length()-1).getString("Name").length()+19) < 425){
                                    recommendations+=info.getJSONObject(i).getString("Name")+", ";
                                }
                            }
                            if(recommendations.length()+info.getJSONObject(info.length()-1).getString("Name").length() + 12 < 425){
                                recommendations+=info.getJSONObject(info.length()-1).getString("Name");
                            }else{
                                recommendations = recommendations.substring(0,recommendations.length()-2);
                            }
                            event.getBot().sendIRC().message(event.getChannel().getName(),recommendations);
                        }
                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
                else{
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"Usage: !recommend [media] returns recommendations similar to the input");
                }
            }
        }
    }
    
    private String getTasteKidURL(String search){
        
        try {
            return ("http://www.tastekid.com/api/similar?q="+URLEncoder.encode(search, "UTF-8")+"&k="+key+"&callback");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return null;
//            Logger.getLogger(Recommendations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private String sendGet(String url) throws Exception {
        
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        
        // optional default is GET
        con.setRequestMethod("GET");
        
        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);
        
//        con.setRequestProperty("X-Mashape-Key", key);
        
        int responseCode = con.getResponseCode();
//        System.out.println("\nSending 'GET' request to URL : " + url);
//        System.out.println("Response Code : " + responseCode);
        
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        
        //print result
//        System.out.println(response.toString());
        return response.toString();
    }
}
