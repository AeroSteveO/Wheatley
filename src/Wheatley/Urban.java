/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

//import Objects.KeyFinder;
import com.google.common.collect.ImmutableSortedSet;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.pircbotx.Channel;
import org.pircbotx.Colors;

/**
 *
 * @author Steve-O
 * --created the json grabbing and parsing parts
 * Previous Bot: SRSBSNS
 * --utilizes the randchan function from poopsock/khwain as a base
 *
 * Requirements:
 * - APIs
 *    JSON (AOSP JSON parser)
 * - Custom Objects
 *    KeyFinder
 *    Throttle
 * - Linked Classes
 *    Global
 *
 * Activate Commands with:
 *      !udict [word]
 *          responds with the urban dictionary reference to the given word
 *          includes the first definition, example, and link to the page
 *
 *
 */
public class Urban extends ListenerAdapter {
    
    private LinkedList<Long> timeLog = new LinkedList<Long>();
    private int maxLog = 2;
    private long maxTime = 30*1000;
//    private Throttle uThrottle = new Throttle("udict");
//    boolean setup = setupThrottle(maxLog,maxTime);
    private String type = "udict";
    
    @Override
    public void onMessage(MessageEvent event) {
//        if (!event.getBot().getUserChannelDao().getChannels(event.getBot().getUserChannelDao().getUser("theTardis")).contains(event.getChannel())) {
        try{
            String message = Colors.removeFormattingAndColors(event.getMessage().trim());
            if(message.toLowerCase().matches("!udict[\\sA-Za-z0-9\\'\\-\\_\\.]*")) {
                int defNum = 0;
                if(!Global.throttle.isThrottleActive(type, event.getChannel().getName())){
                    String[] splitString = message.split("\\s+",2);
                    if(splitString.length>1) {
                        event.getBot().sendIRC().message(event.getChannel().getName(),getDefinition(splitString[1],defNum));
                    }
                    else {
                        event.getBot().sendIRC().notice(event.getUser().getNick(),"Please input a word to get the definition of");
                    }
                    
                }else{
                    event.getBot().sendIRC().notice(event.getUser().getNick(), "Please Wait Before Sending Another Call for Udict");
                }
            }
            else if(message.toLowerCase().split(" ",2)[0].equalsIgnoreCase("!udict")) {
                event.getBot().sendIRC().message(event.getChannel().getName(),"Udict only accepts a-z, 0-9, and ['-_.]");
            }
//            if (message.toLowerCase().matches("!set ucall [0-9]*")&&(event.getUser().getNick().equalsIgnoreCase(Global.botOwner)||event.getUser().getNick().equalsIgnoreCase("theDoctor"))&&event.getUser().isVerified()){
//                maxLog = Integer.parseInt(message.split(" ")[2]);
//                long sec = maxTime/1000;
//                Global.throttle.setMaxLog(type, maxLog, event.getChannel().getName());
//                event.getBot().sendIRC().notice(event.getUser().getNick(), maxLog+" calls can now be made per every "+sec+"s");
//            }
//            if (message.toLowerCase().matches("!set utime [0-9]*")&&(event.getUser().getNick().equalsIgnoreCase(Global.botOwner)||event.getUser().getNick().equalsIgnoreCase("theDoctor"))&&event.getUser().isVerified()){
//                maxTime = Integer.parseInt(message.split(" ")[2])*1000;
//                Global.throttle.setMaxTime(type, maxTime, event.getChannel().getName());
//                long sec = maxTime/1000;
//                event.getBot().sendIRC().notice(event.getUser().getNick(), maxLog+" calls can now be made per every "+sec+"s");
//            }
//            if (message.equalsIgnoreCase("!set ucall")||message.equalsIgnoreCase("!set utime")){
//                long sec = maxTime/1000;
//                
//                event.getBot().sendIRC().notice(event.getUser().getNick(), maxLog+" calls can now be made per every "+sec+"s");
//            }
            
//            if (message.equalsIgnoreCase("!setup")){
//                setupThrottle(maxLog,maxTime, event);
//            }
            
        }
        catch(Exception ex){
            event.respond("Error: Definition Not Found"); // Throws hanson if theres an error
        }
//        }
    }
    
    //converts URL to string, primarily used to string-ify json text
    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);
            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }
    private String getDefinition(String term, int defNum){
//        JSONParser parser = new JSONParser();
        try{
            String jsonObject = (readUrl("http://api.urbandictionary.com/v0/define?term="+term.trim().replaceAll(" ", "%20")));
            
            JSONObject defObject = (JSONObject) new JSONTokener(jsonObject).nextValue();
            JSONArray defList = defObject.getJSONArray("list");
            JSONObject chosenDefinition = defList.getJSONObject(defNum);
            
            String results = defObject.getString("result_type");
            if(results.equalsIgnoreCase("no_results"))
                return("Error: Definition Not Found");
            
            
//            List<String> directLink = JSONKeyFinder(jsonObject,"permalink");
//            List<String> word = JSONKeyFinder(jsonObject,"word");
//            List<String> definition = JSONKeyFinder(jsonObject,"definition");
//            List<String> example = JSONKeyFinder(jsonObject,"example");
//            List<String> results = JSONKeyFinder(jsonObject,"result_type");
            String slimmedDef;
            String slimmedExample;
            
            
            String directLink = chosenDefinition.getString("permalink");
            String word = chosenDefinition.getString("word");
            String definition = chosenDefinition.getString("definition");
            String example = chosenDefinition.getString("example");

            if (definition.length()>150){
                definition = definition.replaceAll("[\t\r\n]", "");
                slimmedDef = definition.substring(0,Math.min(definition.length(),150))+"...";
            }else
                slimmedDef = definition.replaceAll("[\t\r\n]", "");
            
            if (example.length()>150){
                example=example.replaceAll("[\t\r\n]", "");
                slimmedExample = example.substring(0,Math.min(example.length(),150))+"... ";
            }else
                slimmedExample = example.replaceAll("[\t\r\n]", "")+" ";
            
            return(Colors.BOLD+word+Colors.NORMAL+" : "+slimmedDef+" "+Colors.BOLD+"Example : "+Colors.NORMAL+slimmedExample + directLink);
        }catch (Exception e){
            e.printStackTrace();
            return("Error: Definition Not Found");
            
        }
    }
    
    //Finds the given key in the json string using keyfinder.java
//    private List<String> JSONKeyFinder(String jsonText,String jsonKey) {
//        JSONParser parser = new JSONParser();
//        KeyFinder finder = new KeyFinder();
//        List<String> matchedJson = new ArrayList<>();
//        finder.setMatchKey(jsonKey);
//        while(!finder.isEnd()){
//            parser.parse(jsonText, finder, true);
//            if(finder.isFound()){
//                finder.setFound(false);
//                matchedJson.add(finder.getValue().toString());
//            }
//        }
//        return(matchedJson);
//    }
    
//    private boolean setupThrottle(int maxLog, long maxTime, MessageEvent event) {
//        ImmutableSortedSet<Channel> channels = event.getBot().getUserBot().getChannels();
//        
//        Iterator<Channel> iterator = channels.iterator();
//        while(iterator.hasNext()) {
//            Channel element = iterator.next();
//            
//            Global.throttle.create("NA", "NA", element.getName());
//            
//        }
//        Global.throttle.createMaxLog(type,String.valueOf(maxLog), "ALL");
//        Global.throttle.createMaxTime(type, String.valueOf(maxTime), "ALL");
//        return(true);
//    }
//    private boolean setupThrottle(int maxLog, long maxTime){
//        Global.throttle.createMaxLog(type,String.valueOf(maxLog), "ALL");
//        Global.throttle.createMaxTime(type, String.valueOf(maxTime), "ALL");
//        return(true);
//        
//    }
}