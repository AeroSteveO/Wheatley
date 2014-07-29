/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.pircbotx.Colors;
import org.pircbotx.hooks.*;
import org.pircbotx.hooks.events.*;

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
                    //get4ChanImage(defaultBoard);
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
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
        List<String> threads = new ArrayList<>();
        System.out.println(board+"\n");
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
        System.out.println("http://a.4cdn.org/"+board+"/thread/"+threadNumber+".json");
        List<String> filename = new ArrayList<>();
        List<String> extension = new ArrayList<>();
        String image = new String();
        try{
            parser = new JSONParser();
            finder = new KeyFinder();
            finder.setMatchKey("tim");
            while(!finder.isEnd()){
                parser.parse(jsonText, finder, true);
                if(finder.isFound()){
                    finder.setFound(false);
                    filename.add(finder.getValue().toString());
                }
            }
            parser = new JSONParser();
            finder = new KeyFinder();
            finder.setMatchKey("ext");
            while(!finder.isEnd()){
                
                parser.parse(jsonText, finder, true);
                if(finder.isFound()){
                    finder.setFound(false);
                    extension.add(finder.getValue().toString());
                }
            }
            int filenum = (int) (Math.random()*filename.size());
            image = "http://i.4cdn.org/"+board+"/"+filename.get(filenum)+extension.get(filenum);
        }
        catch(ParseException pe){
            pe.printStackTrace();
        }
        
        return(image);
    }
}
