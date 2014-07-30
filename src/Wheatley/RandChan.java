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
            event.respond("http://i.imgur.com/JaKGGo7.jpg");
        }
        
    }
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
        JSONParser parser = new JSONParser();
        KeyFinder finder = new KeyFinder();
        finder.setMatchKey("no");
        try{
            while(!finder.isEnd()){
                parser.parse(jsonText, finder, true);
                if(finder.isFound()){
                    finder.setFound(false);
                    threads.add(finder.getValue().toString());
                }
            }
        }
        catch(ParseException pe){
            pe.printStackTrace();
        }
        
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
            image="http://i.imgur.com/JaKGGo7.jpg";
        }
        
        return(image);
    }
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