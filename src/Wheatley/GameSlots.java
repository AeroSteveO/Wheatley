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
 *          Plays a game of slots
 * 
 */
public class GameSlots extends ListenerAdapter {
    ArrayList<Integer> prizes = getPrizeArray();
    ArrayList<String> slotWords = getSlotArray();
    
    @Override
    public void onMessage(MessageEvent event) {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        if (message.startsWith(Global.commandPrefix)){
            String command = message.split(Global.commandPrefix)[1].toLowerCase();
            if (command.equalsIgnoreCase("slot")||command.equalsIgnoreCase("slots")){
                ArrayList<String> slots = new ArrayList<>();
                String slotString = "";
                
                for (int i=0;i<3;i++){
                    slots.add(slotWords.get((int) (Math.random()*slotWords.size()-1)));
                    slotString = slotString +"("+slots.get(i)+") ";
                }
                
//                event.getBot().sendIRC().message(event.getChannel().getName(),slotString);
                
                if (slots.get(0).equalsIgnoreCase(slotWords.get(6))&&slots.get(1).equalsIgnoreCase(slotWords.get(6))&&slots.get(2).equalsIgnoreCase(slotWords.get(6))){
                    event.getBot().sendIRC().message(event.getChannel().getName(),slotString+ "C0nGr47ul47!0nz "+event.getUser().getNick()+" u won $"+prizes.get(3)+" - go pwn som n00bs :>");
                    GameControl.scores.addScore(event.getUser().getNick(), prizes.get(3));
                }
                
                else if (slots.get(0).equalsIgnoreCase(slots.get(1))&&slots.get(1).equalsIgnoreCase(slots.get(2))){
                    event.getBot().sendIRC().message(event.getChannel().getName(),slotString + "Congratulations "+event.getUser().getNick()+" you won $"+prizes.get(2));
                    GameControl.scores.addScore(event.getUser().getNick(), prizes.get(2));
                }
                
                else if (!slots.get(0).equalsIgnoreCase(slots.get(1))&&!slots.get(1).equalsIgnoreCase(slots.get(2))&&!slots.get(0).equalsIgnoreCase(slots.get(2))){
                    event.getBot().sendIRC().message(event.getChannel().getName(),slotString + "Sorry "+event.getUser().getNick()+" but you lost $"+-prizes.get(0));
                    GameControl.scores.addScore(event.getUser().getNick(), prizes.get(0));
                }
                
                else{
                    event.getBot().sendIRC().message(event.getChannel().getName(),slotString + "Congratulations "+event.getUser().getNick()+" you won $"+prizes.get(1));
                    GameControl.scores.addScore(event.getUser().getNick(), prizes.get(1));
                }
            }
        }
    }
    
    private ArrayList<Integer> getPrizeArray() {
        ArrayList<Integer> prizes = new ArrayList<>();
        prizes.add(-10);
        prizes.add(30);
        prizes.add(300);
        prizes.add(1337);
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
        slotWords.add("1337");
        return slotWords;
    }
}
