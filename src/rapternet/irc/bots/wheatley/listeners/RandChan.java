/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package rapternet.irc.bots.wheatley.listeners;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.pircbotx.Colors;

/**
 *
 * @author Steve-O
 * --created the json grabbing and parsing parts
 * Previous Bot: Poopsock by khwain
 * --created the russian roulette/throttling in the original randchan function
 *
 * Requirements:
 * - APIs
 *    JSON (AOSP JSON parser)
 * - Custom Objects
 *    Throttle
 * - Linked Classes
 *    Global
 *
 * Activate Commands with:
 *      !randchan [board]
 *          responds with a link to a random 4chan image in the given board,
 *          if no board is given, then it responds with a random image from a
 *          random board
 *      !board [board name]
 *          responds with the title for the board
 *      !board list
 *          responds with a PM containing all the board names and titles
 *      !set rcall
 *          Changes the number of randchan calls that can be made per specified
 *          amount of time, if no value is input, gives the current settings
 *      !set rtime
 *          Changes the amount of time between randchan call flushes, randchan
 *          is stopped when # of calls > rcall over the course of rtime, if
 *          no value is input, gives the current settings
 *
 */
public class RandChan extends ListenerAdapter {
    
    private LinkedList<Long> timeLog = new LinkedList<>();
    private int maxLog = 2;
    private long maxTime = 100*1000;
//    private List<String> boardList = getBoardList();
//    private List<String> boardTitles = getBoardTitles();
    private List<List<String>> boardInfo = getBoardInfo();
//    private Throttle rThrottle = new Throttle("randchan");
//    boolean setup = setupThrottle(maxLog,maxTime);
    private String type = "randchan";
    
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage());//.trim());
        
        if (message.startsWith(Global.commandPrefix)&&!message.matches("([ ]{0,}"+Global.commandPrefix+"{1,}[ ]{0,}){1,}")){
            
            String command = message.split(Global.commandPrefix)[1];
            String[] cmdSplit = command.split(" ");
            
            try{
                if (cmdSplit[0].equalsIgnoreCase("board")){
                    if(cmdSplit.length==2){
                        
                        if(cmdSplit[1].equalsIgnoreCase("list")){
                            String boards = "";
                            for(int i=0;i<boardInfo.size()-1;i++){
                                boards = boards+Colors.RED+boardInfo.get(i).get(0)+": "+Colors.NORMAL+boardInfo.get(i).get(1)+", ";
                            }
                            event.getBot().sendIRC().message(event.getUser().getNick(),boards);
                        }
                        
                        else{
                            boolean found = false;
                            int i=0;
                            while (!found && i<boardInfo.size()-1){
                                
                                if(boardInfo.get(i).get(0).equalsIgnoreCase(cmdSplit[1])){
                                    found = true;
                                    event.getBot().sendIRC().message(event.getChannel().getName(),Colors.RED+boardInfo.get(i).get(0)+": "+Colors.NORMAL+boardInfo.get(i).get(1));
                                }
                                i++;
                            }
                            if (!found){
                                event.getBot().sendIRC().message(event.getChannel().getName(), "Board is not allowed/Does not exist");
                            }
                        }
                    }
                }
                else if (command.equalsIgnoreCase("randchan list")){
                    String boards = "";
                    for(int i=0;i<boardInfo.size()-1;i++){
                        boards = boards+Colors.RED+boardInfo.get(i).get(0)+": "+Colors.NORMAL+boardInfo.get(i).get(1)+", ";
                    }
                    event.getBot().sendIRC().message(event.getUser().getNick(),boards);
                }
                
                else if(message.toLowerCase().matches("!randchan(\\s+\\p{Alnum}+)?")) {
                    if(!Global.throttle.isThrottleActive(type,event.getChannel().getName())){
                        String[] splitString = event.getMessage().split("\\s+");
                        if(splitString.length>1) {
                            if(isBoardAllowed(splitString[1])) {
                                event.respond(get4ChanImage(splitString[1]));
                            }
                            else {
                                event.getBot().sendIRC().notice(event.getUser().getNick(), "Board is not allowed/Does not exist");
                            }
                        }
                        else {
                            event.respond(get4ChanImage(boardInfo.get((int) (Math.random()*boardInfo.size()-1)).get(0).toString()));
                        }
                    }else{
                        event.getBot().sendIRC().notice(event.getUser().getNick(), "Current number of randchan calls are greater than the rate limiting system allows");
                    }
                }
                
//                if (message.toLowerCase().matches("!set rcall [0-9]*")&&(event.getUser().getNick().equalsIgnoreCase(Global.botOwner)||event.getUser().getNick().equalsIgnoreCase("theDoctor"))&&event.getUser().isVerified()){
//                    maxLog = Integer.parseInt(message.split(" ")[2])-1;
//                    long sec = maxTime/1000;
//                    Global.throttle.setMaxLog(type, maxLog, event.getChannel().getName());
//                    event.getBot().sendIRC().notice(event.getUser().getNick(), Integer.toString(maxLog+1)+" calls can now be made per every "+sec+"s");
//                }
//
//                if (message.toLowerCase().matches("!set rtime [0-9]*")&&(event.getUser().getNick().equalsIgnoreCase(Global.botOwner)||event.getUser().getNick().equalsIgnoreCase("theDoctor"))&&event.getUser().isVerified()){
//                    maxTime = Integer.parseInt(message.split(" ")[2])*1000;
//                    long sec = maxTime/1000;
//                    Global.throttle.setMaxTime(type, maxTime, event.getChannel().getName());
//                    event.getBot().sendIRC().notice(event.getUser().getNick(), Integer.toString(maxLog+1)+" calls can now be made per every "+sec+"s");
//                }
//
//                if (message.equalsIgnoreCase("!set rcall")||message.equalsIgnoreCase("!set rtime")){
//                    long sec = maxTime/1000;
//                    event.getBot().sendIRC().notice(event.getUser().getNick(), Integer.toString(maxLog+1)+" calls can now be made per every "+sec+"s");
//                }
                
//                if (message.equalsIgnoreCase("!setup")){
//                    setupThrottle(maxLog,maxTime, event);
//                }
            }
            catch(Exception ex){
                ex.printStackTrace();
                System.out.println(ex.getMessage());
                event.respond("http://i.imgur.com/JaKGGo7.jpg"); // Throws hanson if theres an error
            }
        }
    }
    private boolean isBoardAllowed(String board){
        for (int i=0;i<boardInfo.size();i++){
            if (boardInfo.get(i).get(0).equalsIgnoreCase(board)){
                return(true);
            }
        }
        return(false);
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
    
//    //Gets a full list of 4Chan boards using the 4chan json
//    private List<String> getBoardList(){
//        JSONParser parser = new JSONParser();
//        List<String> boards = new ArrayList<>();
//        try{
//            JSONObject jsonObject = (JSONObject) parser.parse(readUrl("http://a.4cdn.org/boards.json"));
//            JSONArray boardsTemp = (JSONArray) jsonObject.get("boards");
//            for (int i=0; i<boardsTemp.size(); i++) {
//                jsonObject = (JSONObject) parser.parse(boardsTemp.get(i).toString());
//                boards.add((String) jsonObject.get("board"));
//            }
//        }
//        catch(Exception ex){
//            ex.printStackTrace();
//            System.out.println(ex.getMessage());
//        }
//        return(boards);
//    }
    
    
//    //Gets a full list of 4Chan board titles using the 4chan json
//    private List<String> getBoardTitles(){
//        JSONParser parser = new JSONParser();
//        List<String> titles = new ArrayList<>();
//        try{
//            JSONObject jsonObject = (JSONObject) parser.parse(readUrl("http://a.4cdn.org/boards.json"));
//            JSONArray boardsTemp = (JSONArray) jsonObject.get("boards");
//            for (int i=0; i<boardsTemp.size(); i++) {
//                jsonObject = (JSONObject) parser.parse(boardsTemp.get(i).toString());
//                titles.add((String) jsonObject.get("title"));
//            }
//        }
//        catch(Exception ex){
//            ex.printStackTrace();
//            System.out.println(ex.getMessage());
//        }
//        return(titles);
//    }
    
    
    private String get4ChanImage(String board) throws Exception {
        String image = new String();
//        List<String> threads = new ArrayList<>();
//        List<String> filename = new ArrayList<>();
        List<String> extension = new ArrayList<>();
        
        String jsonText = readUrl("http://a.4cdn.org/"+board+"/threads.json");
        
        JSONArray pages = (JSONArray) new JSONTokener(jsonText).nextValue();
        
//        threads = JSONKeyFinder(jsonText,"no");
        boolean find = false;
        
        while(!find){ //If theres nothing added into the extension list, then there must not be any images in that thread
            
            JSONArray threadList = pages.getJSONObject((int) (Math.random()*pages.length()-1)).getJSONArray("threads");
            String threadNumber = threadList.getJSONObject((int) (Math.random()*threadList.length()-1)).getString("no");
            
            jsonText = readUrl("http://a.4cdn.org/"+board+"/thread/"+threadNumber+".json");
            try{
                JSONObject postsObject = (JSONObject) new JSONTokener(jsonText).nextValue();
                JSONArray posts = postsObject.getJSONArray("posts");
                JSONObject choice = posts.getJSONObject((int) (Math.random()*posts.length()-1));
                
                find = choice.has("ext")&&choice.has("tim");
                if(find)
                    image = "http://i.4cdn.org/"+board+"/"+choice.getLong("tim")+choice.getString("ext");
            }
            catch(Exception pe){
                pe.printStackTrace();
                image="http://i.imgur.com/JaKGGo7.jpg"; // Throw Hanson if theres an error
            }
        }
        return(image);
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
//    private boolean setupThrottle(int maxLog, long maxTime){
//        Global.throttle.createMaxLog(type,String.valueOf(maxLog), "ALL");
//        Global.throttle.createMaxTime(type, String.valueOf(maxTime), "ALL");
//        return(true);
//
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
//
//        Global.throttle.createMaxLog(type, String.valueOf(maxLog), "ALL");
//        Global.throttle.createMaxTime(type, String.valueOf(maxTime), "ALL");
//        return(true);
//    }
    
    private List<List<String>> getBoardInfo() {
        List<List<String>> info = new ArrayList<>();
//        JSONParser parser = new JSONParser();
        try{
            JSONObject jsonObject = (JSONObject) new JSONTokener(readUrl("http://a.4cdn.org/boards.json")).nextValue();
            JSONArray boardsTemp = (JSONArray) jsonObject.get("boards");
            for (int i=0; i<boardsTemp.length(); i++) {
                List<String> singleBoardInfo = new ArrayList<>();
                jsonObject = (JSONObject) new JSONTokener(boardsTemp.get(i).toString()).nextValue();
                singleBoardInfo.add( jsonObject.getString("board"));
                singleBoardInfo.add( jsonObject.getString("title"));
                info.add(singleBoardInfo);
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        return(info);
    }
}