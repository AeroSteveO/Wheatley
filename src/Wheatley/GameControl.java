package Wheatley;

import Objects.Score;
import Objects.Score.ScoreArray;
import com.google.common.collect.ImmutableSortedSet;
import java.util.ArrayList;
import java.util.Iterator;
import org.pircbotx.Colors;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.UserListEvent;

/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

/**
 *
 * @author Stephen
 * ADMIN COMMANDS
 * Activate Command with:
 *      !set [user] [value]
 *          sets the input users money to the input money value
 *      !save
 *          Saves everyones score to JSON and removes duplicate entries if any were made
 *      !list games
 *          Lists out the currently active games to the sender
 * 
 * USER COMMANDS
 * Activate Command with:
 *      !score
 *          Responds with your score, if a game is currently running, it gives both
 *          your current score and your overall trivia score, otherwise it just gives
 *          your overall score
 *      !score [user]
 *          Responds with the users score, if a game is currently running, it gives both
 *          their current score and their overall trivia score, otherwise it just gives
 *          their overall score
 *
 */
public class GameControl extends ListenerAdapter {
    public static ScoreArray scores = new ScoreArray();
    String filename = "gameScores.json";
    boolean loaded = startScores();
    
    
    @Override
    public void onMessage(final MessageEvent event) throws Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        if (message.startsWith(Global.commandPrefix)&&!Global.channels.areGamesBlocked(event.getChannel().getName())){
            String command = message.split(Global.commandPrefix)[1];
            
            if (command.equalsIgnoreCase("flush")&&event.getUser().getNick().equalsIgnoreCase(Global.botOwner)){
                
            }
            
            else if (command.equalsIgnoreCase("save")&&Global.botAdmin.contains(event.getUser().getNick())){
                scores.removeDupes();
                scores.saveToJSON();
            }
            
            else if (command.equalsIgnoreCase("money")){ // Get your current score
                int userScore = scores.getScore(event.getUser().getNick());
                if (userScore < 0){
                    event.getBot().sendIRC().notice(event.getUser().getNick(), "USER NOT FOUND");
                }
                else
                    event.respond("You currently have $"+userScore);
            }
            
            else if (command.toLowerCase().matches("money\\s[a-z\\|]+")){ // Get someone elses current score
                String user = command.split(" ")[1];
                int userScore = scores.getScore(user);
                if (userScore < 0){
                    event.getBot().sendIRC().notice(event.getUser().getNick(), "USER NOT FOUND");
                }
                else
                    event.respond(user+" currently has $"+userScore);
            }
            
            else if(command.toLowerCase().equalsIgnoreCase("list games")&&event.getUser().getNick().equalsIgnoreCase(Global.botOwner)){
                ArrayList<String> descriptions = Global.activeGame.getCurrentGameDescriptions();
                for (int i=0;i<descriptions.size();i++){
                    event.getBot().sendIRC().message(event.getChannel().getName(),descriptions.get(i));
                }
            }
            
            else if (command.toLowerCase().matches("set\\s[a-z\\|\\-\\`]+\\s[0-9]+")&&event.getUser().getNick().equalsIgnoreCase(Global.botOwner)) {
                String user = command.split(" ")[1];
                String score = command.split(" ")[2];
                scores.setScore(user, Integer.parseInt(score));
                event.getBot().sendIRC().message(event.getChannel().getName(),user+" currently has $"+scores.getScore(user));
            }
        }
    }
    
    private boolean startScores(){
        boolean loaded;
        try{
            scores.setFilename(filename);
            scores.setBaseScore(500);
            loaded = scores.loadFromJSON();
        }
        catch (Exception ex){
            System.out.println("SCORES FAILED TO LOAD");
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    
    @Override
    public void onJoin(JoinEvent event){
        if (!Global.channels.areGamesBlocked(event.getChannel().getName())){
            if (!scores.containsUser(event.getUser().getNick())){
                scores.addUser(event.getUser().getNick());
                scores.saveToJSON();
            }
        }
    }
    
    @Override
    public void onUserList(UserListEvent event){
        ImmutableSortedSet users = event.getUsers();
        
        Iterator<User> iterator = users.iterator();
        boolean modified = false;
        while(iterator.hasNext()) {
            User element = iterator.next();
            if (!scores.containsUser(element.getNick())){
                //temp = (User)users.floor(temp);
                scores.addUser(element.getNick());
                System.out.println(element.getNick());
                modified = true;
            }
        }
        if (modified)
            scores.saveToJSON();
    }
}
