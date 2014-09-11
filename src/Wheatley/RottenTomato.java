/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Steve-O
 * <srsbsns> !rt - Live Die Repeat: Edge of Tomorrow(2014, PG-13)  Critic Rating: "Certified Fresh" Critic Score: 90 Audience Score: 91 http://rottentomatoes.com/m/live_die_repeat_edge_of_tomorrow/
 */
public class RottenTomato extends ListenerAdapter {
    private String key = "***REMOVED***";
    
    public void onMessage(MessageEvent event) throws Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage().trim());
        if(message.startsWith("!rt ")){
            String search = message.split(" ",2)[1];
            //String movieURL = getMovieSearchURL(search);
            String movieURL = parseMovieSearch(getMovieSearchURL(search));
            event.getBot().sendIRC().message(event.getChannel().getName(), movieURL);
            
        }
    }
    
    private String getMovieSearchURL(String search) throws UnsupportedEncodingException{
        String url = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?q="+URLEncoder.encode(search, "UTF-8")+"&page_limit=10&page=1&apikey="+key;
        System.out.println(url);
        return(url);
    }
    private String parseMovieSearch(String url) throws Exception{
        String movieSearchJSON = readUrl(url);
        JSONParser parser = new JSONParser();
        
        try{
            
            JSONObject movieJSON = (JSONObject) parser.parse(movieSearchJSON);
            long total = (long) movieJSON.get("total");
            if (total>0){
                JSONArray movieMatches = (JSONArray) movieJSON.get("movies");
                JSONObject movieMatched = (JSONObject) movieMatches.get(0);
                JSONObject movieLinks = (JSONObject) movieMatched.get("links");
                System.out.println(movieMatched);
                String movieLink = (String) movieLinks.get("alternate");
                System.out.println(movieLink);
                String movieTitle = (String) movieMatched.get("title");
                System.out.println(movieTitle);
                long movieYear = (long) movieMatched.get("year");
                System.out.println(movieYear);
                String movieMpaaRating = (String) movieMatched.get("mpaa_rating");
                System.out.println(movieMpaaRating);
                
                
                JSONObject movieRatings = (JSONObject) movieMatched.get("ratings");
                System.out.println(movieRatings);
                
                
                String criticRating = (String) movieRatings.get("critics_rating");
                long criticScore  = (long) movieRatings.get("critics_score");
                long audienceScore = (long) movieRatings.get("audience_score");
                System.out.println(criticRating);
                System.out.println(criticScore);
                System.out.println(audienceScore);
                
                if(criticRating.equalsIgnoreCase("null")){
                    
                }
                return(Colors.BOLD+movieTitle+Colors.NORMAL+" ("+movieYear+", "+movieMpaaRating+") "+Colors.BOLD+"Critic Rating:"+Colors.NORMAL+" \""+criticRating+"\" "+Colors.BOLD+"Critic Score:"+Colors.NORMAL+" "+criticScore+" "+Colors.BOLD+"Audience Score:"+Colors.BOLD+" "+audienceScore+" "+movieLink);
            }
            else
                return("Movie Not Found");
        } catch(Exception ex){
            System.out.println(ex.getMessage());
            return("Error");
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
}