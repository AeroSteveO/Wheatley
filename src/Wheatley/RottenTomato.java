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
 *      Original Bot: SRSBSNS by: i dunno who
 *
 * Activate Command with:
 *      !rt [movie]
 *          Responds with the rotten tomato ratings for the input movie
 *      !recommend [movie]
 *          Responds with a list of movies similar to the one input
 *
 */
public class RottenTomato extends ListenerAdapter {
    private String key = "***REMOVED***";
    
    public void onMessage(MessageEvent event) throws Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage().trim());
        if(message.toLowerCase().startsWith("!rt ")){
            String search = message.split(" ",2)[1];
            //String movieInformation = getMovieSearchURL(search);
            String movieInformation = parseMovieSearch(getMovieSearchURL(search));
            event.getBot().sendIRC().message(event.getChannel().getName(), movieInformation);
        }
        if (message.toLowerCase().startsWith("!recommend ")){
            String search = message.split(" ",2)[1];
            String id = getMovieID(getMovieSearchURL(search));
            if (!id.equalsIgnoreCase("Error")&&!id.equalsIgnoreCase("Movie Not Found")){
                event.getBot().sendIRC().message(event.getChannel().getName(), parseSimilarMovies(id));
            }
            else
                event.respond(id);
        }
    }
    private String getMovieSimilarURL(String search) {
        return "http://api.rottentomatoes.com/api/public/v1.0/movies/"+search+"/similar.json?limit=5&apikey="+key;
    }
    private String getMovieSearchURL(String search) throws UnsupportedEncodingException{
        return "http://api.rottentomatoes.com/api/public/v1.0/movies.json?q="+URLEncoder.encode(search, "UTF-8")+"&page_limit=10&page=1&apikey="+key;
    }
    private String parseSimilarMovies(String id) throws Exception{
        String similarJSON = readUrl(getMovieSimilarURL(id));
        JSONParser parser = new JSONParser();
//        ArrayList<String> movieTitles = new ArrayList<>();
//        ArrayList<String> movieYears = new ArrayList<>();
//        ArrayList<String> movieRatings = new ArrayList<>();
        String title;
        long year;
        String rating;
        String response = "";
        try{
            
            JSONObject movieJSON = (JSONObject) parser.parse(similarJSON);
            JSONArray movieMatches = (JSONArray) movieJSON.get("movies");
            
            if (movieMatches.size()>0){
                
                response =Colors.BOLD+"Similar Movies Found: "+Colors.NORMAL;
                for (int i=0;i<movieMatches.size()-1;i++){
                    JSONObject movie = (JSONObject) movieMatches.get(i);
                    title = (String) movie.get("title");
                    year = (long) movie.get("year");
                    rating = (String) movie.get("mpaa_rating");
//                    movieTitles.add((String) movie.get("title"));
//                    movieYears.add((String) movie.get("year"));
//                    movieRatings.add((String) movie.get("mpaa_rating"));
                    response = response +Colors.BOLD+ title+Colors.NORMAL + " ("+year+", "+rating+"), ";
                    
                }
                JSONObject movie = (JSONObject) movieMatches.get(movieMatches.size()-1);
                title = (String) movie.get("title");
                year = (long) movie.get("year");
                rating = (String) movie.get("mpaa_rating");
                response = response +Colors.BOLD+ title+Colors.NORMAL + " ("+year+", "+rating+")";
                
                return(response);
            }
            else
                return("No Recommendations Found");
        } catch(Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return("Error");
        }
    }
    private String getMovieID(String url) throws Exception{
        String movieSearchJSON = readUrl(url);
        JSONParser parser = new JSONParser();
        
        try{
            
            JSONObject movieJSON = (JSONObject) parser.parse(movieSearchJSON);
            long total = (long) movieJSON.get("total");
            if (total>0){
                JSONArray movieMatches = (JSONArray) movieJSON.get("movies");
                JSONObject movieMatched = (JSONObject) movieMatches.get(0);
                JSONObject movieLinks = (JSONObject) movieMatched.get("links");
//                System.out.println(movieMatched);
                String movieLink = (String) movieLinks.get("alternate");
//                System.out.println(movieLink);
                String movieTitle = (String) movieMatched.get("title");
                String movieId = (String) movieMatched.get("id");
//                System.out.println(movieId);
                
                return(movieId);
            }
            else
                return("Movie Not Found");
        } catch(Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return("Error");
        }
        
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
//                System.out.println(movieMatched);
                String movieLink = (String) movieLinks.get("alternate");
//                System.out.println(movieLink);
                String movieTitle = (String) movieMatched.get("title");
//                System.out.println(movieTitle);
                long movieYear = (long) movieMatched.get("year");
                System.out.println(movieYear);
                String movieMpaaRating = (String) movieMatched.get("mpaa_rating");
//                System.out.println(movieMpaaRating);
                
                
                JSONObject movieRatings = (JSONObject) movieMatched.get("ratings");
//                System.out.println(movieRatings);
                
                
                String criticRating = (String) movieRatings.get("critics_rating");
                long criticScore  = (long) movieRatings.get("critics_score");
                long audienceScore = (long) movieRatings.get("audience_score");
//                System.out.println(criticRating);
//                System.out.println(criticScore);
//                System.out.println(audienceScore);
                String response = Colors.BOLD+movieTitle+Colors.NORMAL+" ("+movieYear+", "+movieMpaaRating+") ";
                if(criticRating!=null){
                    response = response + Colors.BOLD+"Critic Rating:"+Colors.NORMAL+" \""+criticRating+"\" ";
                }
                if(criticRating!=null&&criticScore!=-1){
                    response = response + Colors.BOLD+"Critic Score:"+Colors.NORMAL+" "+criticScore+" ";
                }
                if(audienceScore!=0){
                    response = response + Colors.BOLD+"Audience Score:"+Colors.BOLD+" "+audienceScore+" ";
                }
                response = response + movieLink;
                return(response);
            }
            else
                return("Movie Not Found");
        } catch(Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
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