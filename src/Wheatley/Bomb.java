/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.WaitForQueue;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Steve-O
 * Based on the C# IRC bot, CasinoBot
 * which is generally unstable and requires windows to run
 */
public class Bomb extends ListenerAdapter {
    // Woohooo basic variables for junk
    int time = 10;
    
    ArrayList<String> colorls = null;
    
    @Override
    public void onMessage(MessageEvent event) throws FileNotFoundException, InterruptedException {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        
        if (colorls == null) {
            colorls = getColorList();
        }
        if (message.equalsIgnoreCase("!bomb")){
            String player = event.getUser().getNick();
            List<String> colours = new ArrayList<>();
            String colorlist = "";
            for (int i=0;i<5;i++){
                colours.add(colorls.get((int) (Math.random()*colorls.size()-1)).toLowerCase());
            }
            
//            if (colorlist.equals("")){
            for (int i=0; i<colours.size()-1;i++){
                colorlist = colorlist + colours.get(i) + ", ";
            }
            colorlist = colorlist + colours.get(colours.size()-1);
//            }
            DateTime dt = new DateTime();
            DateTime end = dt.plusSeconds(time);
            String solution = colours.get((int) (Math.random()*colours.size()-1));
            event.respond("You recieved the bomb. You have " + time + " seconds to defuse it by cutting the right cable." + Colors.BOLD + " Choose your destiny:" + Colors.NORMAL);
            event.getBot().sendIRC().message(event.getChannel().getName(),"Wire colors include: " + colorlist);
            WaitForQueue queue = new WaitForQueue(event.getBot());
            while (true){
                MessageEvent CurrentEvent = queue.waitFor(MessageEvent.class);
                dt = new DateTime();
                if (dt.isAfter(end)){
                    event.getBot().sendIRC().message(event.getChannel().getName(),"the bomb explodes in front of " + player + ". Seems like you did not even notice the big beeping suitcase.");
                    colours.clear();
                    queue.close();
                }
                else if (CurrentEvent.getMessage().equalsIgnoreCase(solution)&&CurrentEvent.getUser().getNick().equals(player)){
                    event.getBot().sendIRC().message(event.getChannel().getName(), player + " defused the bomb. Seems like he was wise enough to buy a defuse kit." );
                    colours.clear();
                    queue.close();
                }
                else if (!CurrentEvent.getMessage().equalsIgnoreCase(solution)&&CurrentEvent.getUser().getNick().equals(player)) {
                    event.getBot().sendIRC().message(event.getChannel().getName(),"The bomb explodes in " + player + "'s hands. You lost your life.");
                    colours.clear();
                    queue.close();
                }
            }
        }
    }
    public ArrayList<String> getColorList() throws FileNotFoundException{
        try{
            Scanner wordfile = new Scanner(new File("colorlist.txt"));
            ArrayList<String> colorls = new ArrayList<String>();
            while (wordfile.hasNextLine()){
                colorls.add(wordfile.nextLine());
            }
            wordfile.close();
            return (colorls);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(omgword.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
