/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import Utils.TextUtils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.jsoup.Jsoup;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Steve-O
 *      Original Bot: SRSBSNS by: Saigon
 *
 * Requirements:
 * - APIs
 *    Jsoup
 * - Custom Objects
 *    N/A
 * - Linked Classes
 *    Global
 *
 * Activate Command with:
 *      !lasturl
 *          Pulls up the last url seen in the current channel
 *      !secondlasturl
 *          Pulls up the second last url seen in the current channel
 *      !christmas
 *          Outputs the current number of days till Christmas
 *      !summon [user]
 *          Sends a PM to the user stating that they have been summoned
 *      !tell [user] [statement]
 *          Sends the given statement to the user via PM
 *      !srsbsns
 *          wat
 *      !lasturl
 *          Responds with the last URL mentioned in the channel
 *      !secondlasturl
 *          Responds with the second last URL mentioned in the channel
 *      !quack [query]
 *          Responds with information about the input query from Duck Duck Go
 * 
 * Source:
 *    Duck Duck Go
 *    https://duckduckgo.com/api
 *
 */
public class SRSBSNS extends ListenerAdapter {
    private final String USER_AGENT = "Mozilla/5.0";
//    !metacritic (metacritic.com rating),
    
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage());
//        String[] messageArray = message.split(" ");
        String currentChan = event.getChannel().getName();
        
        
//        if (messageArray[0].equalsIgnoreCase("!bankick")) {
////            String[] kill = messageArray;
//            if (messageArray.length < 3) {
//                event.getBot().sendRaw().rawLineNow("tban " + event.getChannel().getName() + " 1m " + event.getBot().getUserChannelDao().getUser(messageArray[1]).getNick() + "!*@*");
////                output.kick(event.getBot().getUserChannelDao().getUser(kill[1]), "theDoctor has sent you to an alternate universe. #AlternateUniverse");
//                event.getChannel().send().kick(event.getBot().getUserChannelDao().getUser(messageArray[1]), "theDoctor has sent you to an alternate universe. #AlternateUniverse");
//            }
//            else if (messageArray.length>4){
//                String time;
//                String[] kill;
//                String reason;
//                if (messageArray[2].matches("[0-9]+[s|d|m|h]")){
//                    kill = Colors.removeFormattingAndColors(event.getMessage()).split(" ",4);
//                    time = messageArray[2];
//                    reason = kill[3];
//                }
//                else {
//                    kill = Colors.removeFormattingAndColors(event.getMessage()).split(" ",3);
//                    time = "1m";
//                    reason = kill[2];
//                }
//
//                String user = event.getBot().getUserChannelDao().getUser(kill[1]).getNick();
//                event.getBot().sendRaw().rawLineNow("tban " + event.getChannel().getName() + " " + time + " " + event.getBot().getUserChannelDao().getUser(user).getNick() + "!*@*");
//                event.getChannel().send().kick(event.getBot().getUserChannelDao().getUser(user), "<"+event.getUser().getNick()+"> "+reason);
//            }
//            else {
//                event.getBot().sendRaw().rawLineNow("tban " + event.getChannel().getName() + " " + messageArray[2] + " " + event.getBot().getUserChannelDao().getUser(messageArray[1]).getNick() + "!*@*");
////                output.kick(event.getBot().getUserChannelDao().getUser(kill[1]), "theDoctor has sent you to an alternate universe. #AlternateUniverse");
//                event.getChannel().send().kick(event.getBot().getUserChannelDao().getUser(messageArray[1]), "theDoctor has sent you to an alternate universe. #AlternateUniverse");
//            }
//        }
        
        if (!event.getBot().getUserChannelDao().getChannels(event.getBot().getUserChannelDao().getUser("theTardis")).contains(event.getChannel())) {
            // separete input by spaces ( URLs don't have spaces )
            String [] parts = message.split("\\s");
            // Attempt to convert each item into an URL.
            for( String item : parts ) try {
                URL url = new URL(item);  // If this fails, the string is not a URL
                // If possible then replace with anchor...
                Global.channels.getChan(currentChan).setSecondLastUrl(Global.channels.getChan(currentChan).getLastUrl());
                Global.channels.getChan(currentChan).setLastUrl(item);
            } catch (MalformedURLException e) { // If exception happens, then its not a URL
            }
            
            if (message.equalsIgnoreCase("!lasturl")){
                if (!Global.channels.getChan(currentChan).getLastUrl().equals("")){
                    String title;
                    
                    org.jsoup.nodes.Document finaldoc = Jsoup.connect(Global.channels.getChan(currentChan).getLastUrl()).get();
                    if (finaldoc == null) {
                        title= "No Title Found";
                    } else {
                        title = finaldoc.title();
                    }
                    event.getBot().sendIRC().message(currentChan,Colors.BOLD+"Last URL: "+Colors.NORMAL+Global.channels.getChan(currentChan).getLastUrl()+Colors.BOLD+" Title: "+Colors.NORMAL+title);
                }
                else
                    event.getBot().sendIRC().message(currentChan,"No previous URL found");
            }
            
            if (message.equalsIgnoreCase("!secondlasturl")){
                if (!Global.channels.getChan(currentChan).getSecondLastUrl().equals("")){
                    org.jsoup.nodes.Document finaldoc = Jsoup.connect(Global.channels.getChan(currentChan).getSecondLastUrl()).get();
                    String title = finaldoc.title();
                    event.getBot().sendIRC().message(currentChan,Colors.BOLD+"Second to last URL: "+Colors.NORMAL+Global.channels.getChan(currentChan).getSecondLastUrl()+Colors.BOLD+" Title: "+Colors.NORMAL+title);
                }
                else
                    event.getBot().sendIRC().message(currentChan,"Currently less than 2 URLs found");
            }
            
            if (message.equalsIgnoreCase("!srsbsns"))
                event.getBot().sendIRC().action(currentChan,"wat");
            
            if(message.equalsIgnoreCase("!christmas")) {
                GregorianCalendar now = new GregorianCalendar();
                GregorianCalendar christmas = new GregorianCalendar();
                christmas.set(Calendar.MONTH, 11);
                christmas.set(Calendar.DAY_OF_MONTH, 25);
                if(christmas.before(now)) {
                    christmas.add(Calendar.YEAR, 1);
                }
                else if(christmas.equals(now)) {
                    event.respond("It's Christmas!");
                }
                christmas.set(Calendar.HOUR_OF_DAY, 0);
                christmas.set(Calendar.MINUTE, 0);
                christmas.set(Calendar.SECOND, 0);
                long diff = (christmas.getTimeInMillis() - now.getTimeInMillis())/1000;
                long days = (diff/86400);
                diff %= 86400;
                long hours = diff/3600;
                diff %= 3600;
                long minutes = diff/60;
                diff %=60;
                long seconds = diff;
                event.respond(String.format("There are %d days, %d hours, %d minutes, and %d seconds until Christmas", days, hours, minutes, seconds));
            }
            //String message = event.getMessage().trim();
            if(!event.getBot().getUserChannelDao().getChannels(event.getBot().getUserChannelDao().getUser("BlarghleBot")).contains(event.getChannel())){
                if(message.matches("!summon\\s+[^\\s]+")) {
                    String target = message.split("\\s+")[1];
                    if(event.getBot().getUserChannelDao().getAllUsers().contains(event.getBot().getUserChannelDao().getUser(target))) {
                        //If the user is in the same channel as the summon
                        event.getBot().sendIRC().notice(event.getUser().getNick(), Colors.BOLD+"!summon "+Colors.NORMAL+target+" has been PMed");
                        event.getBot().sendIRC().message(event.getBot().getUserChannelDao().getUser(target).getNick(), Colors.BOLD+"!summon "+Colors.NORMAL+"you have been summoned by "+event.getUser().getNick()+" from "+event.getChannel().getName());
                    }
                    else {
                        event.getBot().sendIRC().notice(event.getUser().getNick(), Colors.BOLD+"!summon "+Colors.NORMAL+"user not in channel");
                    }
                }
            }
            
            if (message.startsWith(Global.commandPrefix)&&!message.matches("([ ]{0,}"+Global.commandPrefix+"{1,}[ ]{0,}){1,}")){
                
                String command = message.split(Global.commandPrefix,2)[1];
                String[] cmdSplit = command.split(" ");
                if (cmdSplit[0].equalsIgnoreCase("quack")){
                    if (cmdSplit.length==1){
                        event.getBot().sendIRC().notice(event.getUser().getNick(), "Usage: !quack [search query] returns information about your query");
                    }
                    else{
                        String search = command.split(" ",2)[1];
                        search = search.replaceAll("!", "");
                        String url = duckDuckUrl(search);
                        System.out.println(url+"&pretty=1");
                        String json = readUrlUsingGet(url);
                        JSONObject similar = (JSONObject) new JSONTokener(json).nextValue();
                        String topic = similar.getString("Heading");
                        String info = similar.getString("Abstract");
                        String hitUrl = similar.getString("AbstractURL");
                        
                        if (info.isEmpty()&&similar.getJSONArray("RelatedTopics").length()==0){
                            event.getBot().sendIRC().message(event.getChannel().getName(),"No results available for "+search);
                            return;
                        }
                        else if (info.isEmpty()){
                            JSONObject firstHit = similar.getJSONArray("RelatedTopics").getJSONObject(0);
                            if (firstHit.has("Text")){
                                info = firstHit.getString("Text");
                                hitUrl = firstHit.getString("FirstURL");
                            }
                            else if (firstHit.has("Topics")){
                                firstHit = firstHit.getJSONArray("Topics").getJSONObject(0);
                                info = firstHit.getString("Text");
                                hitUrl = firstHit.getString("FirstURL");
                            }
                            else{
                                event.getBot().sendIRC().message(event.getChannel().getName(),"No results available for "+search);
                                return;
                            }
                        }
//                        if (info.length()+topic.length()+hitUrl.length()>396){
//                            info = info.substring(0,396-topic.length()-hitUrl.length())+"...";
//                        }
                        //Colors.BOLD+topic+Colors.NORMAL+info.split(topic,2)[1]
                        event.getBot().sendIRC().message(event.getChannel().getName(),info+" ("+hitUrl+")");
                    }
                }
            }
        }
        if (!event.getBot().getUserChannelDao().getChannels(event.getBot().getUserChannelDao().getUser("Hermes")).contains(event.getChannel())) {
            if(message.toLowerCase().startsWith("!tell")&&message.split(" ").length>2) {
                String target = message.split("\\s+")[1];
                String tell = message.split(target)[1];
                if(event.getBot().getUserChannelDao().getAllUsers().contains(event.getBot().getUserChannelDao().getUser(target))) {
                    //If the user is in the same channel as the summon
                    event.getBot().sendIRC().notice(event.getUser().getNick(), Colors.BOLD+"!tell "+Colors.NORMAL+target+" has been PMed");
                    event.getBot().sendIRC().message(event.getBot().getUserChannelDao().getUser(target).getNick(), event.getUser().getNick() + " wants me to tell you: "+tell);
                }
                else {
                    event.getBot().sendIRC().notice(event.getUser().getNick(), Colors.BOLD+"!tell "+Colors.NORMAL+"user not in channel");
                }
            }
        }
        if(message.equalsIgnoreCase("mein leader, i summon thee")) {
            if(event.getBot().getUserChannelDao().getAllUsers().contains(event.getBot().getUserChannelDao().getUser("theDoctor"))) {
                //If the user is in the same channel as the summon
                event.getBot().sendIRC().notice(event.getUser().getNick(), Colors.BOLD+"!summon "+Colors.NORMAL+"theDoctor has been PMed");
                event.getBot().sendIRC().message(event.getBot().getUserChannelDao().getUser("theDoctor").getNick(), "mein leader, your humble servent "+event.getUser().getNick()+" hath summoned you to the place known as "+event.getChannel().getName());
            }
            else {
                event.getBot().sendIRC().notice(event.getUser().getNick(), Colors.BOLD+"!summon "+Colors.NORMAL+"user not in channel");
            }
        }
    }
    
    private String duckDuckUrl(String search){
        try{
            return "http://api.duckduckgo.com/?q="+URLEncoder.encode(search, "UTF-8")+"&format=json";
        }
        catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    
    private String readUrlUsingGet(String url) throws Exception {
        
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        
        con.setRequestMethod("GET");
        
        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("format", "json");
        
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
        
//        System.out.println(response.toString());
        return response.toString();
    }
}