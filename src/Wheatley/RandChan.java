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
import org.pircbotx.Colors;

/**
 *
 * @author Steve-O
 * --created the json grabbing and parsing parts
 * Previous Bot: Poopsock by khwain
 * --created the russian roulette/throttling in the original randchan function
 *
 * Besides the one specialty library, this is plug and play
 *
 * Activate Commands with:
 *      !randchan [board]
 *          responds with a link to a random 4chan image in the given board,
 *          if no board is given, then it responds with a random image from a
 *          random board
 *      !set rcall
 *          Changes the number of randchan calls that can be made per specified 
 *          amount of time, if no value is input, gives the current settings
 *      !set rtime
 *          Changes the amount of time between randchan call flushes, randchan 
 *          is stopped when # of calls > rcall over the course of rtime, if 
 *          no value is input, gives the current settings
 * 
 * Requires:
 *      json-simple-1.1.1
 *      KeyFinder.java
 *
 */
public class RandChan extends ListenerAdapter {
    
    private LinkedList<Long> timeLog = new LinkedList<Long>();
    private int maxLog = 5;
    private long maxTime = 10*1000;
    List<String> boardList = getBoardList();
    List<String> boardTitles = getBoardTitles();
    
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage().trim());
        try{
            if (message.equalsIgnoreCase("!randchan dict")||message.equalsIgnoreCase("!randchan dictionary")||message.equalsIgnoreCase("!randchan list")){
                String boards = Colors.RED+boardList.get(0)+": "+Colors.NORMAL+boardTitles.get(0)+", ";
                for(int i=1;i<boardList.size()-1;i++){
                    boards = boards+Colors.RED+boardList.get(i)+": "+Colors.NORMAL+boardTitles.get(i)+", ";
                }
                event.getBot().sendIRC().message(event.getUser().getNick(),boards);
            }
            else if(message.toLowerCase().matches("!randchan(\\s+\\p{Alnum}+)?")) {
                //Little bit of throttling up in here
                Date d = new Date();
                long currentTime = d.getTime();
                if(timeLog.size() > maxLog) {
                    while(timeLog.size()>0 && currentTime - timeLog.getLast() > maxTime) {
                        timeLog.pollLast();
                    }
                    if(timeLog.size()>maxLog) {
                        event.getBot().sendIRC().notice(event.getUser().getNick(), "Current number of randchan calls are greater than the rate limiting system allows");
                        return;
                    }
                }
                else{
                    //Russian roulette up in here
                    if(((int)(Math.random()*6))==0) {
                        event.getChannel().send().kick(event.getUser(), "No soup for you");
                        event.getBot().sendIRC().notice(event.getUser().getNick(), "NO SOUP FOR YOU");
                        return;
                    }
                    String[] splitString = event.getMessage().split("\\s+");
                    if(splitString.length>1) {
                        if(boardList.contains(splitString[1])) {
                            timeLog.addFirst(d.getTime());
                            event.respond(get4ChanImage(splitString[1]));
                        }
                        else {
                            timeLog.addFirst(d.getTime());
                            event.getChannel().send().kick(event.getUser(), "Die");
                        }
                    }
                    else {
                        timeLog.addFirst(d.getTime());
                        event.respond(get4ChanImage(boardList.get((int) (Math.random()*boardList.size()-1)).toString()));
                    }
                }
            }
            if (message.toLowerCase().matches("!set rcall [0-9]*")&&event.getUser().getNick().equalsIgnoreCase(Global.BotOwner)){
                maxLog = Integer.parseInt(message.split(" ")[2]);
                long sec = maxTime/1000;
                event.getBot().sendIRC().notice(event.getUser().getNick(), maxLog+" calls can now be made per every "+sec+"s");
            }
            if (message.toLowerCase().matches("!set rtime [0-9]*")&&event.getUser().getNick().equalsIgnoreCase(Global.BotOwner)){
                maxTime = Integer.parseInt(message.split(" ")[2])*1000;
                long sec = maxTime/1000;
                event.getBot().sendIRC().notice(event.getUser().getNick(), maxLog+" calls can now be made per every "+sec+"s");
            }
            if (message.equalsIgnoreCase("!set rcall")||message.equalsIgnoreCase("!set utime")){
                long sec = maxTime/1000;
                event.getBot().sendIRC().notice(event.getUser().getNick(), maxLog+" calls can now be made per every "+sec+"s");
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
    
    
    //Gets a full list of 4Chan board titles using the 4chan json
    private List<String> getBoardTitles(){
        JSONParser parser = new JSONParser();
        List<String> titles = new ArrayList<>();
        try{
            JSONObject jsonObject = (JSONObject) parser.parse(readUrl("http://a.4cdn.org/boards.json"));
            JSONArray boardsTemp = (JSONArray) jsonObject.get("boards");
            for (int i=0; i<boardsTemp.size(); i++) {
                jsonObject = (JSONObject) parser.parse(boardsTemp.get(i).toString());
                titles.add((String) jsonObject.get("title"));
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        return(titles);
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