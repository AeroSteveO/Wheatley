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
 */
public class GameControl extends ListenerAdapter {
    public static ScoreArray scores = new ScoreArray();
    String filename = "gameScores.json";
    boolean loaded = startScores();
    
    
    @Override
    public void onMessage(final MessageEvent event) throws Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        if (message.startsWith(Global.commandPrefix)){
            String command = message.split(Global.commandPrefix)[1];
            
            if (command.equalsIgnoreCase("flush")&&event.getUser().getNick().equalsIgnoreCase(Global.botOwner)){
                
            }
            
            else if (command.equalsIgnoreCase("score")){ // Get your current score
                event.respond("Your current score is: "+scores.getScore(event.getUser().getNick()));
            }
            
            else if (command.matches("score\\s[a-z\\|]+")){ // Get someone elses current score
                String user = command.split(" ")[1];
                event.respond(user+"'s current score is: "+scores.getScore(user));
            }
            
            else if(command.equalsIgnoreCase("list games")&&event.getUser().getNick().equalsIgnoreCase(Global.botOwner)){
                ArrayList<String> descriptions = Global.activeGame.getCurrentGameDescriptions();
                for (int i=0;i<descriptions.size();i++){
                    event.getBot().sendIRC().message(event.getChannel().getName(),descriptions.get(i));
                }
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
                scores.add(new Score(event.getUser().getNick()));
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
                scores.add(new Score(element.getNick()));
                System.out.println(element.getNick());
                modified = true;
            }
        }
        if (modified)
            scores.saveToJSON();
    }
}
