/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import Objects.TimedWaitForQueue;
import Utils.GameUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Steve-O
 * Based on the C# IRC bot, CasinoBot
 * which is generally unstable and requires windows to run
 *
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    TimedWaitForQueue
 * - Linked Classes
 *    Global
 *    GameControl
 *
 * Activate Command with:
 *      !bomb
 *          Gives the user a bomb, the user has to pick the correctly colored
 *          wire to cut before the time limit or the user loses
 *
 */
public class GameBomb extends ListenerAdapter {
    // Woohooo basic variables for junk
    int time = 10;  // Seconds
    String blockedChan = "#dtella";
    ArrayList<String> colorls = null;
    int prize = 70; // $
    int loss = 50;
    
    @Override
    public void onMessage(MessageEvent event) throws InterruptedException {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        
        if (message.startsWith(Global.commandPrefix)&&!message.matches("([ ]{0,}"+Global.commandPrefix+"{1,}[ ]{0,}){1,}")){
            String command = message.split(Global.commandPrefix)[1];
            String[] cmdSplit = command.split(" ");
            
            
            if (cmdSplit[0].equalsIgnoreCase("bomb")&&!GameUtils.areGamesBlocked(event.getChannel().getName())){
                List<String> colours = new ArrayList<>();
                String colorlist = "";
                
                if (GameControl.scores.getScore(event.getUser().getNick())<loss){
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"You don't have enough money to afford the potential loss in this game");
                }
                else{
                    
                    if (cmdSplit.length>1){
                        if(cmdSplit[1].equalsIgnoreCase("classic")){
                            colours.add("red");
                            colours.add("blue");
                            colours.add("green");
                            colours.add("yellow");
                            colours.add("black");
                        }
                        else{
                            event.getBot().sendIRC().notice(event.getUser().getNick(),"The only available option for this command is 'classic'");
                            return;
                        }
                    }
                    else{
                        if (colorls == null) {
                            colorls = getColorList();
                        }
                        ArrayList<Integer> colorNumbers = new ArrayList<>();
                        
                        for (int i=0;i<5;i++){
                            int rndNum = (int) (Math.random()*colorls.size()-1);
                            
                            if(!colorNumbers.contains(rndNum)){
                                colours.add(colorls.get(rndNum).toLowerCase());
                                colorNumbers.add(rndNum);
                                
                            }
                            else
                                i--;
                        }
                    }
                    
                    String player = event.getUser().getNick();
                    
                    for (int i=0; i<colours.size()-1;i++){
                        colorlist = colorlist + colours.get(i) + ", ";
                    }
                    colorlist = colorlist + colours.get(colours.size()-1);
                    
                    int key = (int) (Math.random()*100000+1);
                    
                    String solution = colours.get((int) (Math.random()*colours.size()));
                    event.respond("You recieved the bomb. You have " + time + " seconds to defuse it by cutting the right cable." + Colors.BOLD + " Choose your destiny:" + Colors.NORMAL);
                    event.getBot().sendIRC().message(event.getChannel().getName(),"Wire colors include: " + colorlist);
                    TimedWaitForQueue queue = new TimedWaitForQueue(event, time, key);
                    while (true){
                        MessageEvent CurrentEvent = queue.waitFor(MessageEvent.class);
                        if (CurrentEvent.getMessage().equalsIgnoreCase(Integer.toString(key))){
                            event.getBot().sendIRC().message(event.getChannel().getName(),"the bomb explodes in front of " + player + ". Seems like you did not even notice the big beeping suitcase. You lose $"+loss);
                            colours.clear();
                            GameControl.scores.subtractScore(player, loss);
                            GameControl.scores.addScore(event.getBot().getNick(), loss);
                            queue.close();
                        }
                        else if (CurrentEvent.getMessage().equalsIgnoreCase(solution)&&CurrentEvent.getUser().getNick().equalsIgnoreCase(player)&&CurrentEvent.getChannel().getName().equalsIgnoreCase(event.getChannel().getName())){
                            event.getBot().sendIRC().message(event.getChannel().getName(), player + " defused the bomb. Seems like he was wise enough to buy a defuse kit. You win $"+prize );
                            colours.clear();
                            GameControl.scores.addScore(player,prize);
                            queue.close();
                        }
                        else if (!CurrentEvent.getMessage().equalsIgnoreCase(solution)&&CurrentEvent.getUser().getNick().equalsIgnoreCase(player)&&CurrentEvent.getChannel().getName().equalsIgnoreCase(event.getChannel().getName())){
                            int moneyLoss = 20;
                            event.getBot().sendIRC().message(event.getChannel().getName(),"The bomb explodes in " + player + "'s hands. You lost your life and - even worse - $"+moneyLoss+". The right color would have been "+Colors.BOLD+Colors.RED+solution);
                            colours.clear();
                            GameControl.scores.subtractScore(player, moneyLoss);
                            GameControl.scores.addScore(event.getBot().getNick(), moneyLoss);
                            queue.close();
                        }
                    }
                }
            }
        }
    }
    public ArrayList<String> getColorList() {
        try{
            Scanner wordfile = new Scanner(new File("colorlist.txt"));
            ArrayList<String> colorls = new ArrayList<String>();
            while (wordfile.hasNextLine()){
                colorls.add(wordfile.nextLine());
            }
            wordfile.close();
            return (colorls);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}