/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
 *    json-droid
 * - Custom Objects
 *    N/A
 * - Linked Classes
 *    N/A
 *
 * Activate commands with:
 *      !MC [platform] [game]
 *          Looks up the game on metacritic and responds with information about it
 *      !Games [platform] [new-releases|coming-soon]
 *          Looks up new releases or games that are coming soon for the input platform
 * 
 */
public class MetaCritic extends ListenerAdapter {
    String key = "***REMOVED***";
//    String testKey = "***REMOVED***";
    private final String USER_AGENT = "Mozilla/5.0";
    ArrayList<ArrayList<String>> gamePlatforms = getGamePlatforms();
    
    @Override
    public void onMessage(final MessageEvent event) {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        
        if (message.startsWith(Global.commandPrefix)&&!message.matches("([ ]{0,}"+Global.commandPrefix+"{1,}[ ]{0,}){1,}")){
            
            String command = message.split(Global.commandPrefix)[1];
            String[] cmdSplit = command.split(" ");
//            System.out.println(command);
            
            if (cmdSplit[0].equalsIgnoreCase("mc")){
                String[] gameSearch = command.split(" ",3);
                if (gameSearch.length!=3){
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"!MC [platform] [game]");
                }
                else{
                    String platform = getPlatform(gameSearch[1]);
                    
                    if (platform==null){
                        String platformlist = new String();
                        for (int i=0;i<gamePlatforms.size()-1;i++)
                            platformlist += gamePlatforms.get(i).get(0) + ", ";
                        platformlist += gamePlatforms.get(gamePlatforms.size()-1).get(0);
                        event.getBot().sendIRC().notice(event.getUser().getNick(),"Input Invalid: !MC [platform] [game] where available gameplatforms include: "+platformlist);
                    }
                    else{
                        try{
//                            System.out.println(gameSearch[2]);
//                            System.out.println(platform);
                            
                            String url = findGameUrl(gameSearch[2],platform);
                            String response = sendGet(url);
                            JSONObject defObject = (JSONObject) new JSONTokener(response).nextValue();
                            
                            if (defObject.get("result").toString().equalsIgnoreCase("false")){
//                                System.out.println("TRUE");
                                
                                url = searchGameUrl(gameSearch[2],platform);
                                response = sendGet(url);
                                defObject = (JSONObject) new JSONTokener(response).nextValue();
                                
                                if(defObject.has("results")&&defObject.getInt("count")!=0){
                                    
                                    JSONArray gameHits = defObject.getJSONArray("results");
                                    JSONObject finalChoice = gameHits.getJSONObject(0);
                                    String name = finalChoice.getString("name");
                                    
                                    url = findGameUrl(name,platform);
                                    response = sendGet(url);
                                    
                                    defObject = (JSONObject) new JSONTokener(response).nextValue();
                                    JSONObject results = defObject.getJSONObject("result");
                                    String score = results.getString("score");
                                    String userScore = results.getString("userscore");
                                    String rating = results.getString("rating");
                                    String rlsdate = results.getString("rlsdate");
                                    String mcurl = results.getString("url");
                                    
                                    if (score.isEmpty())
                                        score = "N/A";
                                    
                                    String fancified = Colors.BOLD+name+Colors.NORMAL+" ("+rlsdate.split("-")[0]+", "+rating+") "+Colors.BOLD+
                                            "MC Critic Rating: "+Colors.NORMAL+score+"/100"+Colors.BOLD+" User Rating: "+Colors.NORMAL+userScore+"/10"+Colors.BOLD+" URL: "+Colors.NORMAL+mcurl;
                                    
                                    event.getBot().sendIRC().message(event.getChannel().getName(),fancified);
                                }
                                else{
                                    event.getBot().sendIRC().message(event.getChannel().getName(),"MetaCritic: No Game Found");
                                }
                            }
                            else{
                                JSONObject results = defObject.getJSONObject("result");
                                
                                String name = results.getString("name");
                                String score = results.getString("score");
                                String userScore = results.getString("userscore");
                                String rating = results.getString("rating");
                                String rlsdate = results.getString("rlsdate");
                                String mcurl = results.getString("url");
                                
                                if (score.isEmpty())
                                    score = "N/A";
                                
                                String fancified = Colors.BOLD+name+Colors.NORMAL+" ("+rlsdate.split("-")[0]+", "+rating+") "+Colors.BOLD+
                                        "MC Critic Rating: "+Colors.NORMAL+score+"/100"+Colors.BOLD+" User Rating: "+Colors.NORMAL+userScore+"/10"+Colors.BOLD+" URL: "+Colors.NORMAL+mcurl;
                                //Colors.BOLD+" Developer: "+Colors.NORMAL+developer+Colors.BOLD+" Publisher: " +Colors.NORMAL+publisher+
                                
                                
                                
                                event.getBot().sendIRC().message(event.getChannel().getName(),fancified);
                            }
                        }catch(Exception ex){

                            ex.printStackTrace();
                            event.getBot().sendIRC().notice(event.getUser().getNick(),"ERROR: Something went terribly wrong");
                        }
                        
                    }
                }
            }
            else if (cmdSplit[0].equalsIgnoreCase("games")){
                if (cmdSplit.length < 3) {
                    String platformlist = new String();
                    for (int i=0;i<gamePlatforms.size()-1;i++)
                        platformlist += gamePlatforms.get(i).get(0) + ", ";
                    platformlist += gamePlatforms.get(gamePlatforms.size()-1).get(0);
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"Input Invalid: !Games [platform] [list-type], where list types include 'coming-soon' and 'new-releases', available gameplatforms include: "+platformlist);
                    return;

                }
                String platform = getPlatform(cmdSplit[1]);
                
                
                if (platform==null){
                    String platformlist = new String();
                    for (int i=0;i<gamePlatforms.size()-1;i++)
                        platformlist += gamePlatforms.get(i).get(0) + ", ";
                    platformlist += gamePlatforms.get(gamePlatforms.size()-1).get(0);
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"Input Invalid: !Games [platform] [list-type], where list types include 'coming-soon' and 'new-releases', available gameplatforms include: "+platformlist);
                }
                else{
                    if(cmdSplit[2].equalsIgnoreCase("coming-soon")||cmdSplit[2].equalsIgnoreCase("new-releases")){
                        try{
                            String url = gameListUrl(cmdSplit[2],platform);
                            String response = sendGet(url);
                            JSONObject defObject = (JSONObject) new JSONTokener(response).nextValue();
                            
                            JSONArray gameHits = defObject.getJSONArray("results");
                            
                            String games = Colors.RED+cmdSplit[2].toUpperCase()+": ";
                            
                            for (int i=0;i<4;i++){
                                JSONObject hit = gameHits.getJSONObject(i);
                                String name = hit.getString("name");
                                String rlsdate = hit.getString("rlsdate");
                                String rating = "";
                                if (hit.has("rating"))
                                    rating = hit.getString("rating");
                                String score = hit.getString("score");
                                
                                if (rating.isEmpty())
                                    games+=Colors.NORMAL+Colors.BOLD+name+Colors.NORMAL+" ("+rlsdate.split("-")[0]+") ";//+Colors.BOLD+"Score: "+Colors.NORMAL+score+"/100"+", ";
                                else
                                    games+=Colors.NORMAL+Colors.BOLD+name+Colors.NORMAL+" ("+rlsdate.split("-")[0]+", "+rating+") ";//+Colors.BOLD+"Score: "+Colors.NORMAL+score+"/100"+", ";
                                if (score.isEmpty())
                                    games+="N/A, ";
                                else if (score.equalsIgnoreCase("tbd"))
                                    games+="TBD, ";
                                else
                                    games+=score+"/100"+", ";
                            }
                            JSONObject hit = gameHits.getJSONObject(4);
                            String name = hit.getString("name");
                            String rlsdate = hit.getString("rlsdate");
                            String rating = "";
                            if (hit.has("rating"))
                                rating = hit.getString("rating");
                            String score = hit.getString("score");
                            
                            if (rating.isEmpty())
                                games+=Colors.NORMAL+Colors.BOLD+name+Colors.NORMAL+" ("+rlsdate.split("-")[0]+") ";//+Colors.BOLD+"Score: "+Colors.NORMAL+score+"/100"+", ";
                            else
                                games+=Colors.NORMAL+Colors.BOLD+name+Colors.NORMAL+" ("+rlsdate.split("-")[0]+", "+rating+") ";//+Colors.BOLD+"Score: "+Colors.NORMAL+score+"/100"+", ";
                            if (score.isEmpty())
                                    games+="N/A";
                                else if (score.equalsIgnoreCase("tbd"))
                                    games+="TBD";
                                else
                                    games+=score+"/100";                        event.getBot().sendIRC().message(event.getChannel().getName(),games);
                        }
                        catch (Exception ex){
//                            System.out.println("ERROR STUPID ERROR");
                            ex.printStackTrace();
                        }
                    }
                    else{
                        String platformlist = new String();
                        for (int i=0;i<gamePlatforms.size()-1;i++)
                            platformlist += gamePlatforms.get(i).get(0) + ", ";
                        platformlist += gamePlatforms.get(gamePlatforms.size()-1).get(0);
                        event.getBot().sendIRC().notice(event.getUser().getNick(),"Input Invalid: !Games [platform] [list-type], where list types include 'coming-soon' and 'new-releases', available gameplatforms include: "+platformlist);
                    }
                }
            }
        }
    }
    
    private String getPlatform(String platform){
        String reformedPlatform = null;
        for (int i = 0; i < gamePlatforms.size(); i++){
            if (gamePlatforms.get(i).contains(platform.toLowerCase()))
                reformedPlatform = gamePlatforms.get(i).get(0);
        }
        return reformedPlatform;
    }
    
    private String findGameUrl(String game, String platform){
        game = game.replaceAll("\\s", "\\+");
//        System.out.println(game);
        return("https://metacritic-2.p.mashape.com/find/game?platform="+platform+"&title="+game);
    }
    private String searchGameUrl(String game, String platform){
        game = game.replaceAll("\\s", "\\+");
//        System.out.println(game);
        return("https://metacritic-2.p.mashape.com/search/game?platform="+platform+"&title="+game);
    }
    
    private String gameListUrl(String type, String platform){
        
        return("https://metacritic-2.p.mashape.com/game-list/"+platform+"/"+type);
    }
    
    private String sendGet(String url) throws Exception {
        
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        
        // optional default is GET
        con.setRequestMethod("GET");
        
        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);
        
        con.setRequestProperty("X-Mashape-Key", key);
        
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
    
    private ArrayList<ArrayList<String>> getGamePlatforms() {
        ArrayList<ArrayList<String>> platformAliases = new ArrayList<>();
        platformAliases.add(new ArrayList(Arrays.asList("ps4","playstation-4")));
        platformAliases.add(new ArrayList(Arrays.asList("xboxone","xbox1","xbone","xbox-one","x1")));
        platformAliases.add(new ArrayList(Arrays.asList("ps3","playstation-3")));
        platformAliases.add(new ArrayList(Arrays.asList("xbox360","xbox-360","x360","360")));
        platformAliases.add(new ArrayList(Arrays.asList("pc")));
        platformAliases.add(new ArrayList(Arrays.asList("wii-u","wiiu")));
        platformAliases.add(new ArrayList(Arrays.asList("3ds")));
        platformAliases.add(new ArrayList(Arrays.asList("vita","playstation-vita","psvita","ps-vita")));
        platformAliases.add(new ArrayList(Arrays.asList("ios","iphone","ipad")));
//        ps4, xboxone, ps3, xbox360, pc, wii-u, 3ds, vita, ios
        
        return platformAliases;
    }
}