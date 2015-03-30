package Wheatley;

import Objects.GameList;
import Objects.Score.ScoreArray;
import Utils.GameUtils;
import com.google.common.collect.ImmutableSortedSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
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
 *
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    ScoreArray
 *    GameList
 * - Linked Classes
 *    Global
 *
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
    
    public static GameList activeGame = new GameList();    // To be implemented in games
    public static ScoreArray scores = new ScoreArray();
    String filename = "gameScores.json";
    boolean loaded = startScores();
    int lottoNumber = (int) (0+(Math.random()*100-0+1));
    
    int lottoBaseWin = 100;
    int lottoWinnings = lottoBaseWin;
    int lottoCost = 5;
    private final List<Integer> guessList = Collections.synchronizedList( new  ArrayList<Integer>());
    
    //Min + (int)(Math.random() * ((Max - Min) + 1))
    @Override
    public void onMessage(final MessageEvent event) throws Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        if (message.startsWith(Global.commandPrefix)&&!GameUtils.areGamesBlocked(event.getChannel().getName())&&!message.matches("([ ]{0,}!{1,}[ ]{0,}){1,}")){

            String command = message.split(Global.commandPrefix)[1];
            String[] cmdSplit = command.split(" ");
            
            if (command.equalsIgnoreCase("flush")
                    &&event.getUser().getNick().equalsIgnoreCase(Global.botOwner)){
                
                if (event.getUser().isVerified()){
                    activeGame.clear();
                    scores.clean();
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"Cleaned out game files");
                }
                else{
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"You do not have access to this function");
                }
            }
            else if (cmdSplit[0].equalsIgnoreCase("cheat")
                    &&(event.getUser().getNick().equalsIgnoreCase(Global.botOwner)||event.getUser().getNick().equalsIgnoreCase("burg"))){
                if (event.getUser().isVerified()){
                    if (event.getBot().getUserChannelDao().userExists(cmdSplit[1])){
                        if (cmdSplit.length < 3) {
                            
                            event.getBot().sendRaw().rawLineNow("tban " + event.getChannel().getName() + " 10m " + cmdSplit[1] + "!*@*");
                            event.getChannel().send().kick(event.getBot().getUserChannelDao().getUser(cmdSplit[1]), "You have been temporarily banned from the #Casino for cheating");
                        }
                        else if (cmdSplit.length>3){
                            String time;
                            String[] kill;
                            String reason;
                            if (cmdSplit[2].matches("[0-9]+[s|d|m|h|y]")){
                                kill = Colors.removeFormattingAndColors(event.getMessage()).split(" ",4);
                                time = cmdSplit[2];
                                reason = kill[3];
                            }
                            else {
                                kill = Colors.removeFormattingAndColors(event.getMessage()).split(" ",3);
                                time = "10m";
                                reason = kill[2];
                            }
                            
                            String user = event.getBot().getUserChannelDao().getUser(kill[1]).getNick();
                            event.getBot().sendRaw().rawLineNow("tban " + event.getChannel().getName() + " " + time + " " + user + "!*@*");
                            event.getChannel().send().kick(event.getBot().getUserChannelDao().getUser(user), "You have been temporarily banned from the #Casino for cheating | <"+event.getUser().getNick()+"> "+reason);
                        }
                        else {
                            event.getBot().sendRaw().rawLineNow("tban " + event.getChannel().getName() + " " + cmdSplit[2] + " " + cmdSplit[1] + "!*@*");
                            event.getChannel().send().kick(event.getBot().getUserChannelDao().getUser(cmdSplit[1]), "You have been temporarily banned from the #Casino for cheating");
                        }
                        scores.subtractScore(cmdSplit[1], 500);
                    }
                    else{
                        event.getBot().sendIRC().notice(event.getUser().getNick(),"User not found");
                    }
                }
                else{
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"You do not have access to this function");
                }
                
            }
            else if (command.equalsIgnoreCase("save")
                    &&event.getUser().getNick().equalsIgnoreCase(Global.botOwner)){
                
                if(event.getUser().isVerified()){
                    
                    scores.clean();
                    scores.saveToJSON();
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"Saved game score json");
                }
                else{
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"You do not have access to this function");
                }
            }
            
            else if (command.equalsIgnoreCase("money")){ // Get your current score
                int userScore = scores.getScore(event.getUser().getNick());
                if (userScore == Integer.MIN_VALUE){
                    event.getBot().sendIRC().notice(event.getUser().getNick(), "USER NOT FOUND");
                }
                else
                    event.respond("You currently have $"+userScore);
            }
            
            else if (cmdSplit[0].equalsIgnoreCase("money")&&command.split(" ").length==2){ // Get someone elses current score
                String user = command.split(" ")[1];
                int userScore = scores.getScore(user);
                if (userScore ==Integer.MIN_VALUE){
                    event.getBot().sendIRC().notice(event.getUser().getNick(), "USER NOT FOUND");
                }
                else
                    event.respond(user+" currently has $"+userScore);
            }
            
            else if(command.toLowerCase().equalsIgnoreCase("list games")&&event.getUser().getNick().equalsIgnoreCase(Global.botOwner)){
                if (event.getUser().isVerified()){
                    
                    ArrayList<String> descriptions = activeGame.getCurrentGameDescriptions();
                    if (descriptions.isEmpty()){
                        event.getBot().sendIRC().message(event.getChannel().getName(),"No currently active games");
                    }
                    for (int i=0;i<descriptions.size();i++){
                        event.getBot().sendIRC().message(event.getChannel().getName(),descriptions.get(i));
                    }
                }
                else{
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"You do not have access to this function");
                }
            }
            
            else if (cmdSplit[0].equalsIgnoreCase("money")&&command.split(" ").length==3&&event.getUser().getNick().equalsIgnoreCase(Global.botOwner)){
                if(event.getUser().isVerified()) {
                    
                    String user = command.split(" ")[1];
                    String score = command.split(" ")[2];
                    int userCurrentScore = scores.getScore(user);
                    
                    if (userCurrentScore < Integer.MIN_VALUE){
                        event.getBot().sendIRC().notice(event.getUser().getNick(), "USER NOT FOUND");
                    }
                    
                    else if (!score.matches("[0-9]+")){
                        event.getBot().sendIRC().notice(event.getUser().getNick(),"Input number must be a positive integer");
                    }
                    
//                    else if(score.matches("\\-[0-9]+")){
//                        event.getBot().sendIRC().notice(event.getUser().getNick(),"You cannot give a user a negative score");
//                    }
                    
                    else{
                        scores.setScore(user, Integer.parseInt(score));
                        event.getBot().sendIRC().message(event.getChannel().getName(),user+" currently has $"+scores.getScore(user));
                    }
                }
            }
            
            else if (cmdSplit[0].equalsIgnoreCase("give")&&command.split(" ").length==3) {
                String sender = event.getUser().getNick();
                String reciever = command.split(" ")[1];
                String toGive = command.split(" ")[2];
                
                if (toGive.matches("[0-9]+")){
                    
                    if (scores.getScore(reciever)==Integer.MIN_VALUE){
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
            
            else if (cmdSplit[0].equalsIgnoreCase("merge")&&command.split(" ").length==3&&event.getUser().getNick().equalsIgnoreCase(Global.botOwner)){
                if (event.getUser().isVerified()) {
                    
                    String mergeThis = command.split(" ")[1];
                    String mergeIntoThis = command.split(" ")[2];
                    
                    if (scores.getScore(mergeThis)==Integer.MIN_VALUE){
                        event.getBot().sendIRC().notice(event.getUser().getNick(), mergeThis+": USER NOT FOUND");
                    }
                    else if (scores.getScore(mergeIntoThis)==Integer.MIN_VALUE){
                        event.getBot().sendIRC().notice(event.getUser().getNick(), mergeIntoThis+": USER NOT FOUND");
                    }
                    
                    else{
                        scores.merge(mergeThis,mergeIntoThis);
                        event.getBot().sendIRC().message(event.getChannel().getName(),mergeIntoThis+" currently has $"+scores.getScore(mergeIntoThis));
                    }
                }
            }
            else if (cmdSplit[0].equalsIgnoreCase("lotto")){
                if (cmdSplit.length==1){
                    event.getBot().sendIRC().message(event.getChannel().getName(),"Current lottery winnings are at $"+lottoWinnings);
                }
//                else if(lottoCost>GameControl.scores.getScoreObj(event.getUser().getNick()).getScore()){
//                    event.getBot().sendIRC().message(event.getChannel().getName(),event.getUser().getNick()+": You do not have enough money to buy a lotto ticket");
//                }
                else if (cmdSplit.length==2){
                    if (cmdSplit[1].equalsIgnoreCase("list")){
                        String guesses = "";
                        
                        if (guessList.size()>0){
                            
                            ArrayList<Integer> sortedGuesses = new ArrayList<>();
                            sortedGuesses.addAll(guessList);
                            Collections.sort(sortedGuesses);
//                            synchronized(guessList){
                            for (int i=0;i<sortedGuesses.size()-1;i++){
                                guesses+=sortedGuesses.get(i)+", ";
                            }
                            guesses+=sortedGuesses.get(sortedGuesses.size()-1);
//                            }
                            
                            event.getBot().sendIRC().notice(event.getUser().getNick(),Colors.BOLD+ "Lotto Numbers Guessed: "+Colors.NORMAL+sortedGuesses);
                        }
                        else{
                            event.getBot().sendIRC().notice(event.getUser().getNick(),"No Lotto numbers have been guessed yet");
                        }
                    }
                    else if(cmdSplit[1].matches("[0-9]{1,3}")){
                        if(lottoCost>GameControl.scores.getScoreObj(event.getUser().getNick()).getScore()){
                            event.getBot().sendIRC().message(event.getChannel().getName(),event.getUser().getNick()+": You do not have enough money to buy a lotto ticket");
                        }
                        else{
                            synchronized(guessList){
                                int guess = Integer.parseInt(cmdSplit[1]);
                                if (guessList.contains(guess)){
                                    event.getBot().sendIRC().notice(event.getUser().getNick(),Colors.BOLD+ "Lotto: "+Colors.NORMAL+"Value already guessed | Use "+Colors.BOLD+"!lotto list"+Colors.NORMAL+" to see the full guess list");
                                }
                                else if (guess>100){
                                    event.getBot().sendIRC().notice(event.getUser().getNick(),Colors.BOLD+ "Lotto: "+Colors.NORMAL+"Input must be an integer value between 0 and 100");
                                }
                                else if (guess==lottoNumber){
                                    int WheatleyGain = (int) (lottoWinnings * .4);
                                    lottoWinnings = (int) (lottoWinnings *.6);
                                    event.getBot().sendIRC().message(event.getChannel().getName(),Colors.BOLD+"Congratulations "+Colors.NORMAL+event.getUser().getNick()+", you won $"+lottoWinnings);
                                    GameControl.scores.addScore(event.getUser().getNick(), lottoWinnings);
                                    GameControl.scores.addScore(event.getBot().getNick(), WheatleyGain);
                                    GameControl.scores.subtractScore(event.getBot().getNick(), lottoBaseWin);
                                    lottoNumber = (int) (0+(Math.random()*100-0+1));
                                    lottoWinnings = lottoBaseWin;
                                    guessList.clear();
                                }
                                else{
                                    event.getBot().sendIRC().message(event.getChannel().getName(),"Sorry "+event.getUser().getNick()+", but you lost $"+lottoCost);
                                    GameControl.scores.subtractScore(event.getUser().getNick(), lottoCost);
                                    lottoWinnings += lottoCost;
                                    guessList.add(guess);
                                }
                            }
                        }
                    }
                    else if(cmdSplit[1].matches("[0-9]{1,3}\\-[0-9]{1,3}")){
//                        if(lottoCost>GameControl.scores.getScoreObj(event.getUser().getNick()).getScore()){
//                            event.getBot().sendIRC().message(event.getChannel().getName(),event.getUser().getNick()+": You do not have enough money to buy a lotto ticket");
//                        }
//                        else{
                        int min = Integer.parseInt(cmdSplit[1].split("-")[0]);
                        int max = Integer.parseInt(cmdSplit[1].split("-")[1]);
                        int range = max - min + 1;
                        
                        if (min>max){
                            event.getBot().sendIRC().notice(event.getUser().getNick(),Colors.BOLD+ "Lotto: "+Colors.NORMAL+"Input minimum must be less than the maximum");
                        }
                        else if (max>100){
                            event.getBot().sendIRC().notice(event.getUser().getNick(),Colors.BOLD+ "Lotto: "+Colors.NORMAL+"Input maximum greater than maximum lottery number");
                        }
                        else if (min<0){
                            event.getBot().sendIRC().notice(event.getUser().getNick(),Colors.BOLD+ "Lotto: "+Colors.NORMAL+"Input must be a non-negative integer value between 0 and 100");
                        }
                        else{
                            if((lottoCost*range)>GameControl.scores.getScoreObj(event.getUser().getNick()).getScore()){
                                event.getBot().sendIRC().message(event.getChannel().getName(),event.getUser().getNick()+": You do not have enough money to buy a lotto ticket");
                            }
                            else{
                                int current = min;
                                boolean win = false;
                                while(current<=max&&!win){
                                    
                                    if (current==lottoNumber){
                                        int WheatleyGain = (int) (lottoWinnings * .4);
                                        lottoWinnings = (int) (lottoWinnings *.6);
                                        event.getBot().sendIRC().message(event.getChannel().getName(),Colors.BOLD+"Congratulations "+Colors.NORMAL+event.getUser().getNick()+", you won $"+lottoWinnings);
                                        GameControl.scores.addScore(event.getUser().getNick(), lottoWinnings);
                                        GameControl.scores.addScore(event.getBot().getNick(), WheatleyGain);
                                        GameControl.scores.subtractScore(event.getBot().getNick(), lottoBaseWin);
                                        lottoNumber = (int) (0+(Math.random()*100-0+1));
                                        lottoWinnings = lottoBaseWin;
                                        guessList.clear();
                                        win=true;
                                    }
                                    else{
                                        boolean containsNum = false;
                                        synchronized(guessList){
                                            if (guessList.contains(current)){
                                                event.getBot().sendIRC().notice(event.getUser().getNick(),Colors.BOLD+ "Lotto: "+Colors.NORMAL+"Value already guessed | Use "+Colors.BOLD+"!lotto list"+Colors.NORMAL+" to see the full guess list");
                                            }
                                            else{
                                                event.getBot().sendIRC().message(event.getChannel().getName(),"Sorry "+event.getUser().getNick()+", but "+current+" is incorrect, you lost $"+lottoCost);
                                                GameControl.scores.subtractScore(event.getUser().getNick(), lottoCost);
                                                lottoWinnings += lottoCost;
                                                guessList.add(current);
                                            }
                                        }
                                    }
                                    current++;
                                }
                            }
                        }
//                        }
                    }
                    
                    else
                        event.getBot().sendIRC().notice(event.getUser().getNick(),Colors.BOLD+ "Lotto: "+Colors.NORMAL+"Input must be an integer value between 0 and 100");
                }
            }
            
//            else if (cmdSplit[0].equalsIgnoreCase("makeitrain")){
//                if(cmdSplit.length>1){
//                    if (cmdSplit.length>2){
//                        event.getBot().sendIRC().notice(event.getUser().getNick(), "!MakeItRain only accepts up to 2 inputs");
//                    }
//                    else if(cmdSplit[1].matches("[0-9]+")){
//                        int rain = Integer.parseInt(cmdSplit[1]);
//                        if(scores.getScoreObj(event.getUser().getNick()).getScore()<rain){
//                            event.getBot().sendIRC().notice(event.getUser().getNick(), "You don't have enough to make it rain that much");
//                        }
//                        else{
//
//                        }
//
//                    }
//                    else{
//                        event.getBot().sendIRC().notice(event.getUser().getNick(), "!MakeItRain only accepts non-negative integer as an input");
//                    }
//                }
//                else{
//                    int rain = 0;
//                    if(scores.getScoreObj(event.getUser().getNick()).getScore()<rain){
//                        event.getBot().sendIRC().notice(event.getUser().getNick(), "You don't have enough to make it rain that much");
//                    }else{
//
//                    }
//                }
//            }
        }
    }
    
    private boolean startScores(){
        boolean loaded;
        try{
            scores.setFilename(filename);
//            scores.setBaseScore(500);
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
        if (!GameUtils.areGamesBlocked(event.getChannel().getName())){
            if (!scores.containsUser(event.getUser().getNick())){
                scores.addUser(event.getUser().getNick());
                scores.saveToJSON();
            }
        }
    }
    
    @Override
    public void onUserList(UserListEvent event){
        
        if (!GameUtils.areGamesBlocked(event.getChannel().getName())){
            
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
}