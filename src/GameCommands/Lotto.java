/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package GameCommands;

import Objects.CommandGame;
import Objects.CommandMetaData;
import static Wheatley.GameListener.scores;
import Wheatley.Global;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.pircbotx.Colors;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Stephen
 */
public class Lotto implements CommandGame {
    
    int lottoNumber = (int) (0+(Math.random()*100-0+1));
    
    int lottoBaseWin = 100;
    int lottoCost = 5;
    private final List<Integer> guessList = Collections.synchronizedList( new  ArrayList<Integer>());
    
    
    @Override
    public boolean isGame() {
        return true;
    }
    
    @Override
    public boolean isShortGame() {
        return true;
    }
    
    @Override
    public void processCommand(MessageEvent event){
        
        CommandMetaData data = new CommandMetaData(event, false);
        
        String sender = data.getCaller();
        String[] cmdSplit = data.getCommandSplit();
        String channel = data.getCommandChannel();
        
        if (!Global.settings.contains("lottowinnings")){
            Global.settings.create("lottowinnings",String.valueOf(lottoBaseWin));
        }
        
        if (cmdSplit.length==1){
            event.getBot().sendIRC().message(channel,"Current lottery winnings are at $"+Global.settings.get("lottowinnings"));
        }
//                else if(lottoCost>GameControl.scores.getScoreObj(sender).getScore()){
//                    event.getBot().sendIRC().message(channel,sender+": You do not have enough money to buy a lotto ticket");
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
                    
                    event.getBot().sendIRC().notice(sender,Colors.BOLD+ "Lotto Numbers Guessed: "+Colors.NORMAL+sortedGuesses);
                }
                else{
                    event.getBot().sendIRC().notice(sender,"No Lotto numbers have been guessed yet");
                }
            }
            else if(cmdSplit[1].matches("[0-9]{1,3}")){
                if(lottoCost>scores.getScoreObj(sender).getScore()){
                    event.getBot().sendIRC().message(channel, sender+": You do not have enough money to buy a lotto ticket");
                }
                else{
                    synchronized(guessList){
                        int guess = Integer.parseInt(cmdSplit[1]);
                        if (guessList.contains(guess)){
                            event.getBot().sendIRC().notice(sender,Colors.BOLD+ "Lotto: "+Colors.NORMAL+"Value already guessed | Use "+Colors.BOLD+"!lotto list"+Colors.NORMAL+" to see the full guess list");
                        }
                        else if (guess>100){
                            event.getBot().sendIRC().notice(sender,Colors.BOLD+ "Lotto: "+Colors.NORMAL+"Input must be an integer value between 0 and 100");
                        }
                        else if (guess==lottoNumber){
                            int WheatleyGain = (int) (Integer.parseInt(Global.settings.get("lottowinnings")) * .4);
                            int lottoWinnings = (int) (Integer.parseInt(Global.settings.get("lottowinnings")) *.6);
                            event.getBot().sendIRC().message(channel,Colors.BOLD+"Congratulations "+Colors.NORMAL+sender+", you won $"+lottoWinnings);
                            scores.addScore(sender, lottoWinnings);
                            scores.addScore(event.getBot().getNick(), WheatleyGain);
                            scores.subtractScore(event.getBot().getNick(), lottoBaseWin);
                            lottoNumber = (int) (0+(Math.random()*100-0+1));
                            Global.settings.set("lottowinnings", String.valueOf(lottoBaseWin));
                            guessList.clear();
                        }
                        else{
                            event.getBot().sendIRC().message(channel,"Sorry "+sender+", but you lost $"+lottoCost);
                            scores.subtractScore(sender, lottoCost);
                            Global.settings.set("lottowinnings", String.valueOf(Integer.parseInt(Global.settings.get("lottowinnings"))+lottoCost));
                            guessList.add(guess);
                        }
                    }
                }
            }
            else if(cmdSplit[1].matches("[0-9]{1,3}\\-[0-9]{1,3}")){
//                        if(lottoCost>scores.getScoreObj(sender).getScore()){
//                            event.getBot().sendIRC().message(channel,sender+": You do not have enough money to buy a lotto ticket");
//                        }
//                        else{
                int min = Integer.parseInt(cmdSplit[1].split("-")[0]);
                int max = Integer.parseInt(cmdSplit[1].split("-")[1]);
                int range = max - min + 1;
                
                if (min>max){
                    event.getBot().sendIRC().notice(sender,Colors.BOLD+ "Lotto: "+Colors.NORMAL+"Input minimum must be less than the maximum");
                }
                else if (max>100){
                    event.getBot().sendIRC().notice(sender,Colors.BOLD+ "Lotto: "+Colors.NORMAL+"Input maximum greater than maximum lottery number");
                }
                else if (min<0){
                    event.getBot().sendIRC().notice(sender,Colors.BOLD+ "Lotto: "+Colors.NORMAL+"Input must be a non-negative integer value between 0 and 100");
                }
                else{
                    if((lottoCost*range)>scores.getScoreObj(sender).getScore()){
                        event.getBot().sendIRC().message(channel,sender+": You do not have enough money to buy a lotto ticket");
                    }
                    else{
                        int current = min;
                        boolean win = false;
                        while(current<=max&&!win){
                            
                            if (current==lottoNumber){
                                int WheatleyGain = (int) (Integer.parseInt(Global.settings.get("lottowinnings")) * .4);
                                int lottoWinnings = (int) (Integer.parseInt(Global.settings.get("lottowinnings")) *.6);
                                event.getBot().sendIRC().message(channel,Colors.BOLD+"Congratulations "+Colors.NORMAL+sender+", you won $"+lottoWinnings);
                                scores.addScore(sender, lottoWinnings);
                                scores.addScore(event.getBot().getNick(), WheatleyGain);
                                scores.subtractScore(event.getBot().getNick(), lottoBaseWin);
                                lottoNumber = (int) (0+(Math.random()*100-0+1));
                                Global.settings.get("lottowinnings", String.valueOf(lottoBaseWin));
                                guessList.clear();
                                win=true;
                            }
                            else{
                                boolean containsNum = false;
                                synchronized(guessList){
                                    if (guessList.contains(current)){
                                        event.getBot().sendIRC().notice(sender,Colors.BOLD+ "Lotto: "+Colors.NORMAL+"Value already guessed | Use "+Colors.BOLD+"!lotto list"+Colors.NORMAL+" to see the full guess list");
                                    }
                                    else{
                                        event.getBot().sendIRC().message(channel,"Sorry "+sender+", but "+current+" is incorrect, you lost $"+lottoCost);
                                        scores.subtractScore(sender, lottoCost);
                                        Global.settings.set("lottowinnings",String.valueOf(Integer.parseInt(Global.settings.get("lottowinnings"))+lottoCost));
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
                event.getBot().sendIRC().notice(sender,Colors.BOLD+ "Lotto: "+Colors.NORMAL+"Input must be an integer value between 0 and 100");
        }
    }
    
    @Override
    public ArrayList<String> commandTerms(){
        ArrayList<String> a = new ArrayList<>();
        a.add("lotto");
        return a;
    }
    
    @Override
    public boolean isCommand(String toCheck){
        return false;
    }
    
}
