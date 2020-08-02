/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package rapternet.irc.bots.wheatley.gamecommands;

import rapternet.irc.bots.wheatley.objects.CommandGame;
import rapternet.irc.bots.common.objects.CommandMetaData;
import rapternet.irc.bots.wheatley.objects.TimedWaitForQueue;
import rapternet.irc.bots.common.utils.BotUtils;
import static rapternet.irc.bots.wheatley.listeners.GameListener.scores;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.pircbotx.Colors;
import org.pircbotx.hooks.events.MessageEvent;
import rapternet.irc.bots.wheatley.objects.Env;
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
public class GameBombCMD implements CommandGame {
    // Woohooo basic variables for junk
    int time = 10;  // Seconds
    ArrayList<String> colorls = null; // list of potential wire colors
    int prize = 70; // $
    int loss = 50; // potential money lost 
    
    @Override
    public boolean isGame() {
            return true;
    }
    @Override
    public boolean isShortGame() {
        return true;
    }
            
    @Override
    public void processCommand(MessageEvent event) {
        List<String> colours = new ArrayList<>();
        String colorlist = "";
        
        CommandMetaData data = new CommandMetaData(event,false);
        String caller = data.getCaller();
        String respondTo = data.respondToIgnoreMessage();
        String[] cmdSplit = data.getCommandSplit();
        
        
        
        if (scores.getScore(caller)<loss){
            event.getBot().sendIRC().notice(caller,"You don't have enough money to afford the potential loss in this game");
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
                    event.getBot().sendIRC().notice(caller,"The only available option for this command is 'classic'");
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
            
//            String player = event.getUser().getNick();
            
            for (int i=0; i<colours.size()-1;i++){
                colorlist = colorlist + colours.get(i) + ", ";
            }
            colorlist = colorlist + colours.get(colours.size()-1);
            
            int key = (int) (Math.random()*100000+1);
            
            String solution = colours.get((int) (Math.random()*colours.size()));
            event.respond("You recieved the bomb. You have " + time + " seconds to defuse it by cutting the right cable." + Colors.BOLD + " Choose your destiny:" + Colors.NORMAL);
            event.getBot().sendIRC().message(respondTo,"Wire colors include: " + colorlist);
            try {
                TimedWaitForQueue queue = new TimedWaitForQueue(event, time, key);
                boolean running = true;
                while (running){
                    
                    MessageEvent CurrentEvent = queue.waitFor(MessageEvent.class);
                    if (CurrentEvent.getMessage().equalsIgnoreCase(Integer.toString(key))){
                        event.getBot().sendIRC().message(respondTo,"the bomb explodes in front of " + caller + ". Seems like you did not even notice the big beeping suitcase. You lose $"+loss);
                        colours.clear();
                        scores.subtractScore(caller, loss);
                        scores.addScore(event.getBot().getNick(), loss);
                        running = false;
                    }
                    else if (CurrentEvent.getMessage().equalsIgnoreCase(solution)&&CurrentEvent.getUser().getNick().equalsIgnoreCase(caller)&&CurrentEvent.getChannel().getName().equalsIgnoreCase(respondTo)){
                        event.getBot().sendIRC().message(respondTo, caller + " defused the bomb. Seems like he was wise enough to buy a defuse kit. You win $"+prize );
                        colours.clear();
                        scores.addScore(caller, prize);
                        running = false;
                    }
                    else if (!CurrentEvent.getMessage().equalsIgnoreCase(solution)&&CurrentEvent.getUser().getNick().equalsIgnoreCase(caller)&&CurrentEvent.getChannel().getName().equalsIgnoreCase(respondTo)){
                        int moneyLoss = 20;
                        event.getBot().sendIRC().message(respondTo,"The bomb explodes in " + caller + "'s hands. You lost your life and - even worse - $" + moneyLoss + ". The right color would have been "+Colors.BOLD+Colors.RED+solution);
                        colours.clear();
                        scores.subtractScore(caller, moneyLoss);
                        scores.addScore(event.getBot().getNick(), moneyLoss);
                        running = false;
                    }
                }
                queue.close();
            }catch (InterruptedException ex) {
                event.getBot().sendIRC().message("#rapterverse", BotUtils.formatPastebinPost(ex));
            }
        }
    }
    
    public ArrayList<String> getColorList() {
        try{
            Scanner wordfile = new Scanner(new File(Env.CONFIG_LOCATION + "colorlist.txt"));
            ArrayList<String> colorls = new ArrayList<>();
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
    
    
    @Override
    public ArrayList<String> commandTerms() {
        ArrayList<String> a = new ArrayList<>();
        a.add("bomb");
        return a;
    }
    
    @Override
    public boolean isCommand(String toCheck) {
        return false;
    }
}
