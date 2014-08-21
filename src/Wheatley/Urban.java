/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.pircbotx.Colors;

/**
 *
 * @author Steve-O
 * --created the json grabbing and parsing parts
 * Previous Bot: SRSBSNS
 * --utilizes the randchan function from poopsock/khwain as a base
 *
 * Besides the one specialty library, this is plug and play
 *
 * Activate Commands with:
 *      !udict [word]
 *          responds with the urban dictionary reference to the given word
 *          includes the first definition, example, and link to the page
 *
 * Requires:
 *      json-simple-1.1.1
 *      KeyFinder.java
 *
 */
public class Urban extends ListenerAdapter {
    
    private LinkedList<Long> timeLog = new LinkedList<Long>();
    private static final int MAX_LOG = 2;
    private static final long MAX_TIME = 30*1000;
    private int defNum = 0;
    
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        try{
            String message = Colors.removeFormattingAndColors(event.getMessage().trim());
            if(message.matches("!udict[\\sA-Za-z0-9]*")) {
                Date d = new Date();
                long currentTime = d.getTime();
                if(timeLog.size() > MAX_LOG) {
                    while(timeLog.size()>0 && currentTime - timeLog.getLast() > MAX_TIME) {
                        timeLog.pollLast();
                    }
                    if(timeLog.size()>MAX_LOG) {
                        event.getBot().sendIRC().notice(event.getUser().getNick(), "DIAF");
                        return;
                    }
                }
                else{
                String[] splitString = message.split("\\s+",2);
                if(splitString.length>1) {
                    timeLog.addFirst(d.getTime());
                    event.getBot().sendIRC().message(event.getChannel().getName(),getDefinition(splitString[1]));
                }
                else {
                    timeLog.addFirst(d.getTime());
                    event.getBot().sendIRC().message(event.getChannel().getName(),getDefinition(""));
                }
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            event.respond("Error: Definition Not Found"); // Throws hanson if theres an error
        }
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
    private String getDefinition(String term){
        JSONParser parser = new JSONParser();
        try{
            String jsonObject = (readUrl("http://api.urbandictionary.com/v0/define?term="+term.trim().replaceAll(" ", "%20")));
            //jsonObject = jsonObject.replaceAll("(\\r|\\n)", "");
            //JSONString directLink = (JSONString) jsonObject.get("permalink");
            List<String> directLink = JSONKeyFinder(jsonObject,"permalink");
            List<String> word = JSONKeyFinder(jsonObject,"word");
            List<String> definition = JSONKeyFinder(jsonObject,"definition");
            List<String> example = JSONKeyFinder(jsonObject,"example");
            List<String> results = JSONKeyFinder(jsonObject,"result_type");
            String slimmedDef;
            String slimmedExample;
            
            System.out.println(definition.get(defNum));
            System.out.println(example.get(defNum));
            
            if(results.get(defNum).equalsIgnoreCase("no_results"))
                return("Error: Definition Not Found");
            
            if (definition.get(defNum).length()>150)
                slimmedDef = definition.get(defNum).replaceAll("[\t\r\n]", "").substring(0,Math.min(definition.get(defNum).length(),150))+"...";
            else
                slimmedDef = definition.get(defNum).replaceAll("[\t\r\n]", "");
            
            if (example.get(defNum).length()>150)
                slimmedExample = example.get(defNum).replaceAll("[\t\r\n]", "").substring(0,Math.min(example.get(defNum).length(),150))+"... ";
            else
                slimmedExample = example.get(defNum).replaceAll("[\t\r\n]", "")+" ";
            
            System.out.println(slimmedExample);
            
            return(Colors.BOLD+word.get(defNum)+Colors.NORMAL+" : "+slimmedDef+" "+Colors.BOLD+"Example : "+Colors.NORMAL+slimmedExample + directLink.get(defNum));
        }catch (Exception e){
            e.printStackTrace();
            return("Error: Definition Not Found");
            
        }
    }
    
    //Finds the given key in the json string using keyfinder.java
    private List<String> JSONKeyFinder(String jsonText,String jsonKey) throws ParseException{
        JSONParser parser = new JSONParser();
        KeyFinder finder = new KeyFinder();
        List<String> matchedJson = new ArrayList<>();
        finder.setMatchKey(jsonKey);
        while(!finder.isEnd()){
            parser.parse(jsonText, finder, true);
            if(finder.isFound()){
                finder.setFound(false);
                matchedJson.add(finder.getValue().toString());
            }
        }
        return(matchedJson);
    }
}