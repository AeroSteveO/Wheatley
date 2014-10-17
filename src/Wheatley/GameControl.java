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
 *      !money [user] [value]
 *          sets the input users money to the input money value
 *      !save
 *          Saves everyones score to JSON and removes duplicate entries if any were made
 *      !list games
 *          Lists out the currently active games to the sender
 *      !merge [user a] [user b]
 *          Merges the score of user a into user b, and resets user a's score to
 *          the base score used by the scoring array
 *
 * USER COMMANDS
 * Activate Command with:
 *      !money
 *          Responds with your score, if a game is currently running, it gives both
 *          your current score and your overall trivia score, otherwise it just gives
 *          your overall score
 *      !money [user]
 *          Responds with the users score, if a game is currently running, it gives both
 *          their current score and their overall trivia score, otherwise it just gives
 *          their overall score
 *      !give [user] [amount]
 *          Gives the input user the input amount, if the input amount is negative,
 *          it sends an error to the user, if you don't have enough to give, it
 *          sends an error to the user
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
            
            else if (command.toLowerCase().startsWith("money")&&command.split(" ").length==2){ // Get someone elses current score
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
            
            else if (command.toLowerCase().startsWith("money")&&command.split(" ").length==3&&event.getUser().getNick().equalsIgnoreCase(Global.botOwner)) {
                String user = command.split(" ")[1];
                String score = command.split(" ")[2];
                int userCurrentScore = scores.getScore(user);
                
                if (userCurrentScore < 0){
                    event.getBot().sendIRC().notice(event.getUser().getNick(), "USER NOT FOUND");
                }
                
                else if (!score.matches("[0-9]+")){
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"Input number must be an integer");
                }
                
                else if(score.matches("\\-[0-9]+")){
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"You cannot give a user a negative score");
                }
                
                else{
                    scores.setScore(user, Integer.parseInt(score));
                    event.getBot().sendIRC().message(event.getChannel().getName(),user+" currently has $"+scores.getScore(user));
                }
            }
            
            else if (command.toLowerCase().startsWith("give")&&command.split(" ").length==3) {
                String sender = event.getUser().getNick();
                String reciever = command.split(" ")[1];
                String toGive = command.split(" ")[2];
                
                if (toGive.matches("[0-9]+")){
                    
                    if (scores.getScore(reciever)<1){
                        event.getBot().sendIRC().notice(event.getUser().getNick(), "USER NOT FOUND");
                    }
                    
                    else{
                        
                        int givings = Integer.parseInt(command.split(" ")[2]);
                        
                        if (scores.getScore(sender)>givings){
                            scores.addScore(reciever,givings);
                            scores.subtractScore(sender,givings);
                            event.getBot().sendIRC().message(event.getChannel().getName(),reciever+" currently has $"+scores.getScore(reciever)+", "+sender+" currently has $"+scores.getScore(sender));
                        }
                        else{
                            event.getBot().sendIRC().message(event.getChannel().getName(),sender+": You do not have enough money to give that much");
                        }
                    }
                }
                
                else if(toGive.matches("\\-[0-9]+")){
                    event.getBot().sendIRC().notice(sender,"You cannot take money through the give command");
                }
                
                else{
                    event.getBot().sendIRC().notice(sender,"Input number must be an integer");
                }
            }
            
            else if (command.toLowerCase().startsWith("merge")&&command.split(" ").length==3&&event.getUser().getNick().equalsIgnoreCase(Global.botOwner)) {
                String mergeThis = command.split(" ")[1];
                String mergeIntoThis = command.split(" ")[2];
                                
                if (scores.getScore(mergeThis)<1){
                    event.getBot().sendIRC().notice(event.getUser().getNick(), mergeThis+": USER NOT FOUND");
                }
                else if (scores.getScore(mergeIntoThis)<1){
                    event.getBot().sendIRC().notice(event.getUser().getNick(), mergeIntoThis+": USER NOT FOUND");
                }
                
                else{
                    scores.merge(mergeThis,mergeIntoThis);
                    event.getBot().sendIRC().message(event.getChannel().getName(),mergeIntoThis+" currently has $"+scores.getScore(mergeIntoThis));
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
