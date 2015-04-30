/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Commands;

import Objects.Command;
import Objects.CommandMetaData;
import Utils.TextUtils;
import Wheatley.Global;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;

/**
 *
 * @author Stephen
 *
 * Requirements:
 * - APIs
 *    JSON-Droid
 * - Custom Objects
 *    Command
 * - Utilities
 *    N/A
 * - Linked Classes
 *    N/A
 *
 */
public class IpCmd implements Command{
    
    @Override
    public String toString(){
        return("IP information command");
    }
    
    @Override
    public boolean isCommand(String toCheck){
        return toCheck.equalsIgnoreCase(Global.mainNick+", whats your ip");
    }
    
    @Override
    public ArrayList<String> commandTerms(){
        ArrayList<String> a = new ArrayList<>();
        a.add("ip");
        return a;
    }
    
    @Override
    public void processCommand(Event event){
        
        CommandMetaData data = new CommandMetaData(event,true);
        
        String caller = data.getCaller();
        boolean isVerified = data.isVerifiedBotOwner();
        String[] cmdSplit = data.getCommandSplit();
        String respondTo = data.respondToCallerOrMessageChan();
        
        if (cmdSplit.length==2){
            try{
                String ip;
                if(!cmdSplit[1].matches("([0-9]+\\.+)+")){
                    String yourURLStr = java.net.URLEncoder.encode(cmdSplit[1], "UTF-8");
                    URL url;
                    try{
                        url = new URL(yourURLStr);
                    }catch (Exception ex){
                        try{
                            url = new URL("http://"+yourURLStr);
                        }
                        catch(Exception ey){
                            event.getBot().sendIRC().notice(caller, "ERROR GETTING IP FROM URL");
                            return;
                        }
                    }
                    InetAddress address = InetAddress.getByName(url.getHost());
                    ip = address.getHostAddress();
                }else
                    ip = cmdSplit[1];
                try{
                    String ipInfo = TextUtils.readUrl("http://ipinfo.io/"+ip+"/json");
                    JSONObject ipJSON = (JSONObject) new JSONTokener(ipInfo).nextValue();
                    
                    String ipAddress = ipJSON.getString("ip");
                    String city = ipJSON.getString("city");
                    String region = ipJSON.getString("region");
                    String country = ipJSON.getString("country");
                    String org = ipJSON.getString("org");
                    
                    event.getBot().sendIRC().message(respondTo, Colors.BOLD+"IP: "+Colors.NORMAL+ipAddress+Colors.BOLD+" Location: "+Colors.NORMAL+city+", "+region+", "+country+Colors.BOLD+" Organization: "+Colors.NORMAL+org);
                }
                catch (Exception ex){
                    System.out.println(ex.getMessage());
//                    ex.printStackTrace();
                    event.getBot().sendIRC().notice(caller, "ERROR PARSING IP ADDRESS");
                }
            }catch(Exception ex){
                ex.printStackTrace();
                event.getBot().sendIRC().notice(caller, "IP lookup failed");
            }
        }
        else if(cmdSplit.length==1 && isVerified){
            try{
                String ipInfo = TextUtils.readUrl("http://ipinfo.io/json");
//            JSONParser parser = new JSONParser();
                
                
                JSONObject ipJSON = (JSONObject) new JSONTokener(ipInfo).nextValue();
                String ipAddress = (String) ipJSON.get("ip");
                event.getBot().sendIRC().notice(caller, "Current IP Address is: "+ipAddress);
            }
            catch (Exception ex){
                System.out.println(ex.getMessage());
                ex.printStackTrace();
                event.getBot().sendIRC().notice(caller, "ERROR PARSING IP ADDRESS");
            }
        }
        else if (cmdSplit.length==1){
            event.getBot().sendIRC().notice(caller, "USE: "+Global.commandPrefix+"IP [ip addr | url]");
        }
        else{
            event.getBot().sendIRC().notice(caller, "IP accepts 1 input and no more");
        }
    }
}