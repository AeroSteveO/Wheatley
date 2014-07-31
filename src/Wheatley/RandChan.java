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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Steve-O
 * --created the json grabbing and parsing parts
 * Previous Bot: Poopsock by khwain
 * --created the russian roulette/throttling in the original randchan function
 * 
 * This function requires the json-simple-1.1.1 library
 * Besides the one specialty library, this is plug and play
 * 
 * Activate Commands with:
 *      !randchan [board]
 *          responds with a link to a random 4chan image in the given board,
 *          if no board is given, then it responds with a random image from a 
 *          random board
 */
public class RandChan extends ListenerAdapter {
    
    private LinkedList<Long> timeLog = new LinkedList<Long>();
    private static final int MAX_LOG = 3;
    private static final long MAX_TIME = 30*1000;
    List<String> boardList = getBoardList();
    
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        try{
            if(event.getMessage().trim().matches("!randchan(\\s+\\p{Alnum}+)?")) {
                
                //Little bit of throttling up in here
                Date d = new Date();
                long currentTime = d.getTime();
                if(timeLog.size() > MAX_LOG) {
                    while(timeLog.size()>0 && currentTime - timeLog.getLast() > MAX_TIME) {
                        timeLog.pollLast();
                    }
                    if(timeLog.size()>MAX_LOG) {
                        event.getChannel().send().kick( event.getUser(), "DIAF");
                        return;
                    }
                }
                //Russian roulette up in here
                if(((int)(Math.random()*6))==0) {
                    event.getChannel().send().kick(event.getUser(), "No soup for you");
                    return;
                }
                String[] splitString = event.getMessage().split("\\s+");
                if(splitString.length>1) {
                    if(boardList.contains(splitString[1])) {
                        timeLog.addFirst(d.getTime());
                        event.respond(get4ChanImage(splitString[1]));
                    }
                    else {
                        event.getChannel().send().kick(event.getUser(), "Die");
                    }
                    get4ChanImage(splitString[1]);
                }
                else {
                    timeLog.addFirst(d.getTime());
                    event.respond(get4ChanImage(boardList.get((int) (Math.random()*boardList.size()-1)).toString()));
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            event.respond("http://i.imgur.com/JaKGGo7.jpg"); // Throws hanson if theres an error
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
    
    //Gets a full list of 4Chan boards using the 4chan json
    private List<String> getBoardList(){
        JSONParser parser = new JSONParser();
        List<String> boards = new ArrayList<>();
        try{
            JSONObject jsonObject = (JSONObject) parser.parse(readUrl("http://a.4cdn.org/boards.json"));
            JSONArray boardsTemp = (JSONArray) jsonObject.get("boards");
            for (int i=0; i<boardsTemp.size(); i++) {
                jsonObject = (JSONObject) parser.parse(boardsTemp.get(i).toString());
                boards.add((String) jsonObject.get("board"));
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        return(boards);
    }
    
    private String get4ChanImage(String board) throws Exception {
        String image = new String();
        List<String> threads = new ArrayList<>();
        List<String> filename = new ArrayList<>();
        List<String> extension = new ArrayList<>();
        
        String jsonText = readUrl("http://a.4cdn.org/"+board+"/threads.json");
        threads = JSONKeyFinder(jsonText,"no");
        while(extension.isEmpty()){ //If theres nothing added into the extension list, then there must not be any images in that thread
            
            String threadNumber = threads.get((int) (Math.random()*threads.size()-1));
            jsonText = readUrl("http://a.4cdn.org/"+board+"/thread/"+threadNumber+".json");
            try{
                filename = JSONKeyFinder(jsonText,"tim");
                extension = JSONKeyFinder(jsonText,"ext");
                int filenum = (int) (Math.random()*filename.size());
                image = "http://i.4cdn.org/"+board+"/"+filename.get(filenum)+extension.get(filenum);
            }
            catch(ParseException pe){
                pe.printStackTrace();
                image="http://i.imgur.com/JaKGGo7.jpg"; // Throw Hanson if theres an error
            }
        }
        return(image);
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