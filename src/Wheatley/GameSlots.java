/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import java.util.ArrayList;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Stephen
 * Activate Command with:
 *      !slot
 *      !slots
 *          Plays a game of slots with the base bet as the amount bet ($10)
 *      !slot [bet value]
 *      !slots [bet value]
 *          Plays a game of slots using the input value as the mount bet
 *
 */
public class GameSlots extends ListenerAdapter {
    ArrayList<String> slotWords = getSlotArray();
    int baseBet = 10;
    
    @Override
    public void onMessage(MessageEvent event) {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        if (message.startsWith(Global.commandPrefix)){
            String command = message.split(Global.commandPrefix)[1].toLowerCase();
            String[] cmdSplit = command.split(" ");
            if ((cmdSplit[0].equalsIgnoreCase("slot")||cmdSplit[0].equalsIgnoreCase("slots"))&&!Global.channels.areGamesBlocked(event.getChannel().getName())){
                
                int bet = baseBet;
                if (cmdSplit.length>2){
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"This command takes 1 integer input maximum");
                    return;
                }
                if (cmdSplit.length==2){
                    
                    if (cmdSplit[1].matches("[0-9]+")){
                        bet = Integer.parseInt(cmdSplit[1]);
                    }
                    
                    else if(cmdSplit[1].matches("\\-[0-9]+")){
                        event.getBot().sendIRC().notice(event.getUser().getNick(),"You cannot bet on your own failure");
                        return;
                    }
                    
                    else{
                        event.getBot().sendIRC().notice(event.getUser().getNick(),"Input number must be an integer");
                        return;
                    }
                }
                
                if (bet>GameControl.scores.getScore(event.getUser().getNick())){
                    event.getBot().sendIRC().message(event.getChannel().getName(),event.getUser().getNick()+": You do not have enough money to bet that much");
                    return;
                }
                
                ArrayList<Integer> prizes = getPrizeArray(bet);
                ArrayList<String> slots = new ArrayList<>();
                String slotString = "";
                
                for (int i=0;i<3;i++){
                    slots.add(slotWords.get((int) (Math.random()*slotWords.size())));
                    slotString = slotString +"("+slots.get(i)+") ";
                }
                                
                if (slots.get(0).equalsIgnoreCase(slotWords.get(6))&&slots.get(1).equalsIgnoreCase(slotWords.get(6))&&slots.get(2).equalsIgnoreCase(slotWords.get(6))){
                    event.getBot().sendIRC().message(event.getChannel().getName(),slotString+ "| C0nGr47ul47!0nz "+event.getUser().getNick()+", u won $"+prizes.get(3)+" - go pwn som n00bs :>");
                    GameControl.scores.addScore(event.getUser().getNick(), prizes.get(3));
                }
                
                else if (slots.get(0).equalsIgnoreCase(slots.get(1))&&slots.get(1).equalsIgnoreCase(slots.get(2))){
                    event.getBot().sendIRC().message(event.getChannel().getName(),slotString + "| Congratulations "+event.getUser().getNick()+", you won $"+prizes.get(2));
                    GameControl.scores.addScore(event.getUser().getNick(), prizes.get(2));
                }
                
                else if (slots.get(0).equalsIgnoreCase(slots.get(1))||slots.get(1).equalsIgnoreCase(slots.get(2))){
                    event.getBot().sendIRC().message(event.getChannel().getName(),slotString + "| Congratulations "+event.getUser().getNick()+", you won $"+prizes.get(1));
                    GameControl.scores.addScore(event.getUser().getNick(), prizes.get(1));
                }
                
                else if (!slots.get(0).equalsIgnoreCase(slots.get(1))&&!slots.get(1).equalsIgnoreCase(slots.get(2))){//&&!slots.get(0).equalsIgnoreCase(slots.get(2))
                    event.getBot().sendIRC().message(event.getChannel().getName(),slotString + "| Sorry "+event.getUser().getNick()+", but you lost $"+-prizes.get(0));
                    GameControl.scores.addScore(event.getUser().getNick(), prizes.get(0));
                }

                else{
                    event.getBot().sendIRC().message(event.getChannel().getName(),slotString + "| Sorry "+event.getUser().getNick()+", but  something broke, the botowner has been notified");
                    event.getBot().sendIRC().notice(Global.botOwner,slotString + "| Something broke, no score found");
                }
            }
        }
    }
    
    private ArrayList<Integer> getPrizeArray(int bet) {
        
        ArrayList<Integer> prizes = new ArrayList<>();
        prizes.add(-bet);
        prizes.add(bet*3);
        prizes.add(bet*30);
        prizes.add(1337);
        
        while(prizes.get(2)>prizes.get(3)){
            prizes.set(3, (prizes.get(3)-4)*10+7);
        }
        
        return prizes;
    }
    
    private ArrayList<String> getSlotArray() {
        ArrayList<String> slotWords = new ArrayList<>();
        slotWords.add("Apple");
        slotWords.add("Pear");
        slotWords.add("Cherry");
        slotWords.add("Citron");
        slotWords.add("Orange");
        slotWords.add("Banana");
        slotWords.add("Grape");
        slotWords.add("Lemon");
        slotWords.add("Sun");
        slotWords.add("1337");
        return slotWords;
    }
}
