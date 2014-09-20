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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Steve-O
 *      Original Bot: SRSBSNS by: saigon
 *
 * IMDb searches implement the OMDb API
 * http://www.omdbapi.com/
 * Rotten Tomato searches implement the Rotten Tomato API
 *
 * Activate Command with:
 *      !rt [movie]
 *          Responds with the rotten tomato ratings for the input movie
 *      !recommend [movie]
 *          Responds with a list of movies similar to the one input
 *      !imdb [movie]
 *      !imdb [movie] [year]
 *          Responds with the IMDB ratings for the input movie
 *      !rating [movie]
 *      !rating [movie] [year]
 *          Responds with the IMDB and rotten tomato ratings for the input movie
 *
 *
 */
public class MovieRatings extends ListenerAdapter {
    boolean wideSearch = false;
    private String key = "***REMOVED***";
    
    public void onMessage(MessageEvent event) throws Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage().trim());
        if (message.toLowerCase().matches("!imdb\\s[a-z\\s]+\\s[0-9]{4}")){
            String[] msgSplit = message.split(" ");
            String year = msgSplit[msgSplit.length-1];
            String movieTitle = message.split(" ",2)[1].split(year)[0];
            event.getBot().sendIRC().message(event.getChannel().getName(),parseImdbMovieSearch(imdbUrlWithYear(movieTitle,year)));
        }
        else if (message.toLowerCase().startsWith("!imdb ")){
            String movieTitle = message.split(" ",2)[1];
            event.getBot().sendIRC().message(event.getChannel().getName(),parseImdbMovieSearch(imdbUrl(movieTitle)));
        }
        if (message.toLowerCase().matches("!rt\\s[a-z\\s]+\\s[0-9]{4}")){
            String[] msgSplit = message.split(" ");
            String year = msgSplit[msgSplit.length-1];
            String movieTitle = message.split(" ",2)[1].split(year)[0];
            event.getBot().sendIRC().message(event.getChannel().getName(),parseRottenMovieSearch(rottentMovieSearchURL(movieTitle),year));
        }
        else if(message.toLowerCase().startsWith("!rt ")){
            String search = message.split(" ",2)[1];
            //String movieInformation = rottentMovieSearchURL(search);
            String movieInformation = parseRottenMovieSearch(rottentMovieSearchURL(search),null);
            event.getBot().sendIRC().message(event.getChannel().getName(), movieInformation);
        }
        else if (message.toLowerCase().startsWith("!recommend ")){
            String search = message.split(" ",2)[1];
            String id = getRottenMovieID(rottentMovieSearchURL(search));
            if (!id.equalsIgnoreCase("Error")&&!id.equalsIgnoreCase("Movie Not Found")){
                event.getBot().sendIRC().message(event.getChannel().getName(), parseRottenSimilarMovies(id));
            }
            else
                event.respond(id); // Sends Error Recieved to chan
        }
        else if (message.toLowerCase().startsWith("!rating ")){
            wideSearch = true;
            String movie = message.split(" ",2)[1];
            String rotten;
            String imdb;
            if ((movie.toLowerCase().matches("[a-z\\s]+\\s[0-9]{4}"))){
                String[] msgSplit = message.split(" ");
                String year = msgSplit[msgSplit.length-1];
                String movieTitle = movie.split(year)[0];
                imdb = parseImdbRating(imdbUrlWithYear(movieTitle,year));
                rotten = parseRottenMovieSearch(rottentMovieSearchURL(movieTitle),year);
            }
            else {
                System.out.println("rating no year");
//                String movieTitle = message.split(" ",2)[1];
                imdb = parseImdbRating(imdbUrl(movie));
                rotten = parseRottenMovieSearch(rottentMovieSearchURL(movie),null);
            }
            String response = rotten.replace("#&#",Colors.BOLD+"IMDb: "+Colors.NORMAL+imdb+"/10");
            event.getBot().sendIRC().message(event.getChannel().getName(), response);
            wideSearch = false;
        }
    }
    private String rottenMovieSimilarURL(String search) {
        return "http://api.rottentomatoes.com/api/public/v1.0/movies/"+search+"/similar.json?limit=5&apikey="+key;
    }
    private String rottentMovieSearchURL(String search) throws UnsupportedEncodingException{
        return "http://api.rottentomatoes.com/api/public/v1.0/movies.json?q="+URLEncoder.encode(search, "UTF-8")+"&page_limit=10&page=1&apikey="+key;
    }
    private String imdbUrl(String movieTitle) throws UnsupportedEncodingException{
        return ("http://www.omdbapi.com/?t="+URLEncoder.encode(movieTitle.trim(), "UTF-8"));
    }
    private String imdbUrlWithYear(String movieTitle, String year) throws UnsupportedEncodingException{
        return ("http://www.omdbapi.com/?t="+URLEncoder.encode(movieTitle.trim(), "UTF-8")+"&y="+URLEncoder.encode(year.trim(), "UTF-8"));
    }
    private String parseRottenSimilarMovies(String id) throws Exception{
        String similarJSON = readUrl(rottenMovieSimilarURL(id));
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
    private String getRottenMovieID(String url) throws Exception{
        String movieSearchJSON = readUrl(url);
        JSONParser parser = new JSONParser();
        
        try{
            
            JSONObject movieJSON = (JSONObject) parser.parse(movieSearchJSON);
            long total = (long) movieJSON.get("total");
            if (total>0){
                JSONArray movieMatches = (JSONArray) movieJSON.get("movies");
                JSONObject movieMatched = (JSONObject) movieMatches.get(0);
                String movieId = (String) movieMatched.get("id");
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
    private String parseImdbMovieSearch(String url) throws Exception{
        String movieSearchJSON = readUrl(url);
        JSONParser parser = new JSONParser();
        
        try{
            JSONObject movieJSON = (JSONObject) parser.parse(movieSearchJSON);
            String title = (String) (String) movieJSON.get("Title");
//            String year = (String) (String) movieJSON.get("Year");
            String mpaaRating = (String) (String) movieJSON.get("Rated");
            String imdbRating = (String) (String) movieJSON.get("imdbRating");
            String id = (String) (String) movieJSON.get("imdbID");
            String release = (String) (String) movieJSON.get("Released");
            String link = "http://imdb.com/title/"+id+"/";
            String response = Colors.BOLD+title+": "+Colors.NORMAL+"("+mpaaRating+") "+Colors.BOLD+"IMDb: "+Colors.NORMAL+imdbRating+"/10 "+Colors.BOLD+"Release Date: "+Colors.NORMAL+formatImdbDate(release)+Colors.BOLD+" Link: "+Colors.NORMAL+link;
            
            return(response);
        }
        catch (Exception ex){
            try{
                JSONObject movieJSON = (JSONObject) parser.parse(movieSearchJSON);
                String error = (String) (String) movieJSON.get("Error");
                return(error);
            }
            catch(Exception e){
                e.printStackTrace();
                return("ERROR");
            }
        }
    }
    private String parseImdbRating(String url) throws Exception{
        String movieSearchJSON = readUrl(url);
        JSONParser parser = new JSONParser();
        
        try{
            JSONObject movieJSON = (JSONObject) parser.parse(movieSearchJSON);
            String imdbRating = (String) (String) movieJSON.get("imdbRating");
            return(imdbRating);
        }
        catch (Exception ex){
            ex.printStackTrace();
            return("");
        }
    }
    private String formatImdbDate(String date){
        String[] dateSplit = date.split(" ");
        return(dateSplit[1]+" "+dateSplit[0]+", "+dateSplit[2]);
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
    /*
    * Set year to null to parse without using the year
    * Otherwise set the year to whatever string you want to find
    * If no year matches the input, the first entry in the array of movies
    * will be the movie used in the response
    */
    private String parseRottenMovieSearch(String url, String year) throws Exception {
        String movieSearchJSON = readUrl(url);
        JSONParser parser = new JSONParser();
        
        try{
            
            JSONObject movieJSON = (JSONObject) parser.parse(movieSearchJSON);
            long total = (long) movieJSON.get("total");
//            System.out.println(total);
            if (total>0){
                int movieLocation = 0;
                JSONArray movieMatches = (JSONArray) movieJSON.get("movies");
                if (year!=null){
                    for (int i=0;i<movieMatches.size();i++){
                        JSONObject movieMatched = (JSONObject) movieMatches.get(i);
                        long movieYear = (long) movieMatched.get("year");
                        if (year.equalsIgnoreCase(String.valueOf(movieYear))){
                            movieLocation=i;
                            break;
                        }
                    }
                }
//                JSONArray movieMatches = (JSONArray) movieJSON.get("movies");
                JSONObject movieMatched = (JSONObject) movieMatches.get(movieLocation);
                JSONObject movieLinks = (JSONObject) movieMatched.get("links");
                String movieLink = (String) movieLinks.get("alternate");
                String movieTitle = (String) movieMatched.get("title");
                long movieYear = (long) movieMatched.get("year");
                System.out.println(movieYear);
                String movieMpaaRating = (String) movieMatched.get("mpaa_rating");
                JSONObject movieRatings = (JSONObject) movieMatched.get("ratings");
                String criticRating = (String) movieRatings.get("critics_rating");
                long criticScore  = (long) movieRatings.get("critics_score");
                long audienceScore = (long) movieRatings.get("audience_score");
                String response = Colors.BOLD+movieTitle+Colors.NORMAL+" ("+movieYear+", "+movieMpaaRating+") ";
                if(criticRating!=null){
                    response = response + Colors.BOLD+"Critic Rating:"+Colors.NORMAL+" \""+criticRating+"\" ";
                }
                if(criticRating!=null&&criticScore!=-1){
                    response = response + Colors.BOLD+"Critic Score:"+Colors.NORMAL+" "+criticScore+" ";
                    if (wideSearch==true){
                        response = response+"#&# ";
                    }
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
}