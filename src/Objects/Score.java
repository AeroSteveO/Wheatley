/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Objects;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Stephen
 *
 * Requirements:
 * - APIs
 *    JSON-Simple-1.1.1
 * - Custom Objects
 *    N/A
 *
 * Object:
 *      Score
 * - Object that contains an int score for the input user, and keeps track of it
 * - Object can be completely controlled through the score array
 *
 * Methods:
 *      setScore - Sets the score as the input integer
 *      add      - Adds the input integer onto the current score
 *      subtract - Subtracts the current integer from the current score
 *     *getScore - Gets the current score integer from the object
 *     *getUser  - Returns the user string from the object
 *     *toString - Produces an easy to read string from the score and user
 *
 * Object:
 *      ScoreArray
 * - Contains scores of users, and allows for easy management of said scores, as well
 *   as easy save/load from JSON
 *
 * Methods:
 *     *setBaseScore  - Sets the base score that all new users start with
 *     *setFilename   - Sets the filename to load and save to
 *                      If no filename is input, the object will not save the scores
 *     *addScore      - Adds the input integer onto the current score of the input user
 *     *subtractScore - Subtracts the current integer from the current score of the input user
 *     *setScore      - Sets the current score of the input nick to the input int
 *     *getScore      - Gets the current score of the input user
 *                    - Returns -1 if no user/score is found
 *     *getScoreObj   - Gets the current score object of the input user
 *                      If no object is found, it creates a new score object and
 *                      returns that
 *     *containsUser  - Returns false if the input user is not in the score array
 *     *saveToJSON    - Saves the current score array to JSON
 *     *loadFromJSON  - Loads the score array values from JSON
 *     *copyOutZeros  - Returns a score array of all the same users, but with
 *                      all zero for their scores
 *     *merge         - Merges one score array into the other, adding scores from one
 *                      to the current scores of the second, if there is no current
 *                      score in the second for a user, it adds that user and their score
 *     *compareTo     - Used to enable sorting using collections.sort
 *      removeDupes       - Removes duplicate entries from the score object
 *      removeDtellaUsers - Removes users whos nick starts with '|'
 *      removeIdlePlayers - Removes users whos score is the same as the base score
 *     *clean             - Removes duplicates, idle players, and dtella users
 *
 * Note: Only commands marked with a * are available for use outside the object
 */
public class Score implements Comparable<Score> {
    private String user;
    private int score;
    
    
    public Score(String user){
        this.user = user;
        this.score = 0;
    }
    
    public Score(String user, int baseScore){
        this.user = user;
        this.score = baseScore;
    }
    
    private void setScore(int score){
        this.score = score;
    }
    
    private void add(int intToAdd){
        this.score=this.score+intToAdd;
    }
    
    private void subtract (int intToSubtract){
        this.score = this.score - intToSubtract;
    }
    
    public String getUser(){
        return this.user;
    }
    
    public int getScore(){
        return this.score;
    }
//    public JSONObject getJSON(){
//        JSONObject score = new JSONObject();
//        score.put(user,score);
////        score.put("score",score);
//        String jsonText = JSONValue.toJSONString(score);
//        System.out.print(jsonText);
//        return(score);
//    }
    @Override
    public String toString(){
        return(this.user+": "+this.score);
    }
    
    @Override
    public int compareTo(Score compareScore) {
        
        int compareQuantity = compareScore.getScore();
        
        //ascending order
        //return this.score - compareQuantity;
        
        //descending order
        return compareQuantity - this.score;
        
    }
    
    public static class ScoreArray {
        
        private final List<Score> scores = Collections.synchronizedList( new  ArrayList<Score>());
        private String filename="doNotSave";
        private int baseScore=0;
        
        public void sort(){
            Collections.sort(scores);
        }
        
        public ArrayList<Score> toArrayList(){
            ArrayList<Score> scoreArray = new ArrayList<>();
            synchronized(scores){
                for (int i=0;i<scores.size();i++){
                    scoreArray.add(scores.get(i));
                }
            }
            return(scoreArray);
        }
        
        public List<Score> getList(){
            return scores;
        }
        
        public void add(Score score){
            this.scores.add(score);
        }
        
        public void clear(){
            this.scores.clear();
        }
        
        public int size(){
            return (this.scores.size());
        }
        
        public Score get(int i){
            return (this.scores.get(i));
        }
        
        public void setBaseScore(int score){
            baseScore = score;
        }
        
        public void setFilename(String filename){
            this.filename = filename;
        }
        
        public void addScore(String nick, int toAdd){
            getScoreObj(nick).add(toAdd);
            this.saveToJSON();
        }
        
        public int addScore(String nick, int basePrize, int wordLength, int x1, int x2){
            
            basePrize += wordLength - (wordLength * x1 / x2) ;
            getScoreObj(nick).add(basePrize);
            this.saveToJSON();
            
            return basePrize;
        }
        
        public void subtractScore(String nick, int toSubtract){
            getScoreObj(nick).subtract(toSubtract);
            this.saveToJSON();
        }
        
        public void setScore(String nick, int score){
            getScoreObj(nick).setScore(score);
            this.saveToJSON();
        }
        
        public ScoreArray copyOutZeros(){
            synchronized(scores){
                ScoreArray zeros = new ScoreArray();
                for (int i=0;i<scores.size();i++){
                    zeros.add(new Score(scores.get(i).user));
                }
                return zeros;
            }
        }
        
        public void merge(ScoreArray toBeMerged){
            for (int i=0;i<toBeMerged.size();i++){
                this.addScore(toBeMerged.get(i).user, toBeMerged.get(i).getScore());
            }
            this.saveToJSON();
        }
        
        public void merge(String mergeThis, String mergeIntoThis){
            this.addScore(mergeIntoThis, this.getScore(mergeThis)-baseScore);
            this.getScoreObj(mergeThis).setScore(baseScore);
            this.saveToJSON();
        }
        
        public boolean containsUser(String nick){
            
            synchronized(scores){
                for(int i = 0; i < scores.size(); i++) {
                    if (scores.get(i).user.equalsIgnoreCase(nick)) {
                        return true;
                    }
                }
                return false;
            }
        }
        
        public void addUser(String nick){
            if (!containsUser(nick)){
                scores.add(new Score(nick,baseScore));
            }
        }
        
        public Score getScoreObj(String nick){
            synchronized(scores){
                for(int i = 0; i < scores.size(); i++) {
                    if (scores.get(i).user.equalsIgnoreCase(nick)) {
                        return (scores.get(i));
                    }
                }
                scores.add(new Score(nick,baseScore));
                return (this.getScoreObj(nick));
            }
        }
        
//        public int getScoreIdx(String nick){
//            synchronized(scores){
//                for(int i = 0; i < this.size(); i++) {
//                    if (scores.get(i).user.equalsIgnoreCase(nick)) {
//                        return (i);
//                    }
//                }
//                scores.add(new Score(nick,baseScore));
//                return (this.getScoreIdx(nick));
//            }
//        }
        
        public int getScore(String nick){
            synchronized(scores){
                for(int i = 0; i < scores.size(); i++) {
                    if (scores.get(i).user.equalsIgnoreCase(nick)) {
                        return (scores.get(i).score);
                    }
                }
//            throw new NullPointerException("User not contained in Score array");
                return (-1);
            }
        }
        
        public void clean(){
            this.removeDupes();
            this.removeDtellaUsers();
            this.removeIdlePlayers();
            this.saveToJSON();
        }
        
        private void removeIdlePlayers(){
            synchronized(scores){
                for(int i = 0; i < this.size(); i++) {
                    
                    if (scores.get(i).getScore()==baseScore) {
                        System.out.println("removed user "+scores.get(i).user);
                        scores.remove(i);
                        i--;
                    }
                }
            }
        }
        
        private void removeDtellaUsers(){
            synchronized(scores){
                for(int i = 0; i < this.size(); i++) {
                    
                    if (scores.get(i).user.startsWith("|")) {
                        System.out.println("removed user "+scores.get(i).user);
                        scores.remove(i);
                        i--;
                    }
                }
            }
        }
        
        private void removeDupes(){
            synchronized(scores){
                ArrayList<String> usersContained = new ArrayList<>();
                for(int i = 0; i < scores.size(); i++) {
                    
                    if (!usersContained.contains(scores.get(i).user)) {
                        usersContained.add(scores.get(i).user);
                    }
                    
                    else if (usersContained.contains(scores.get(i).user)){
                        scores.remove(i);
                        i--;
                    }
                }
            }
        }
        
        public boolean saveToJSON(){
            if (!this.filename.equalsIgnoreCase("doNotSave")){
                JSONObject scoreList = new JSONObject();
//            JSONArray score = new JSONArray();
                synchronized(scores){
                    for (int i=0;i<scores.size();i++){
                        scoreList.put(scores.get(i).user,scores.get(i).score);
                    }
                }
                String addition = JSONValue.toJSONString(scoreList);
                try{
                    File file =new File(filename);
                    
                    //if file doesnt exists, then create it
//                if(!file.exists()){
                    file.createNewFile();
//                }
                    
                    //true = append file
                    FileWriter fileWritter = new FileWriter(file.getName());
                    BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
                    bufferWritter.write(addition);
                    bufferWritter.close();
                    
                }catch(IOException e){
                    System.out.println(filename+" HAS NOT BEEN SAVED");
                    e.printStackTrace();
                    return false;
                }
                return true;
            }
            return false;
        }
        
        public boolean loadFromJSON() throws IOException{
            try{
                String jsonText = loadText(); // Should only have one line of text
                
                if (jsonText!=null&&!jsonText.equals("")){
                    JSONParser parser = new JSONParser();
                    JSONObject scores = (JSONObject) parser.parse(jsonText);
                    Set users = scores.keySet();
                    Iterator<String> iterator = users.iterator();
                    while(iterator.hasNext()) {
                        String element = iterator.next();
                        this.add(new Score(element, (int) (long) scores.get(element)));
                    }
//                String user = users.toArray();
//                    for (int i=0;i<users.length;i++){
//                        this.add(new Score(users[i],(int) scores.get(users[i])));
//                        System.out.println(this.get(i).toString());
//                    }
                }
                else{
                    System.out.println(filename+" IS EMPTY");
                }
            }catch(Exception e){
                System.out.println(filename+" HAS NOT BEEN LOADED");
                System.out.println(e.getMessage());
                e.printStackTrace();
                return false;
            }
            return true;
        }
        
        public String loadText() throws FileNotFoundException, IOException{
            File file =new File(filename);
            //if file doesnt exists, then create it
            if(!file.exists()){
                file.createNewFile();
                return null;
            }
            
            try{
                Scanner wordfile = new Scanner(new File(filename));
                String wordls = "";
                while (wordfile.hasNext()){
                    wordls= wordls+(wordfile.next());
                }
                wordfile.close();
                return (wordls);
            } catch (FileNotFoundException ex) {
                System.out.println("TEXT LOADER FAILED");
                ex.printStackTrace();
                return null;
            }
        }
    }
}