/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package rapternet.irc.bots.wheatley.listeners;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Steve-O
 * original bot functions by Blarghedy
 * Who's lazy and doesn't run his bot much
 * 
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    N/A
 * - Linked Classes
 *    Global
 *    UpdateFiles
 * 
 * Activate Command with:
 *      !badwords
 *      !list bad words
 *      !list badwords
 *          lists out all current bad words
 *      !update badwordlist word
 *          adds the given word to the ArrayList of bad words, but doesn't update the text file
 * 
 */
public class BadWords extends ListenerAdapter{
    static ArrayList<String> badwords = null;
    
    @Override
    public void onMessage(final MessageEvent event) throws Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        
        if ((event.getBot().getUserChannelDao().containsUser("BlarghleBot") && !event.getBot().getUserChannelDao().getChannels(event.getBot().getUserChannelDao().getUser("BlarghleBot")).contains(event.getChannel())) || !event.getBot().getUserChannelDao().containsUser("BlarghleBot")) {
            
            // UPDATING THE BADWORD LIST
            if (badwords == null)
                badwords = getBadWords();
            if (message.toLowerCase().startsWith("!update badwords") && event.getUser().getNick().equals(Global.botOwner))
                if (message.split(" ").length==3)
                    badwords.add(message.split(" ")[2]);
            
            // CHECKING TO SEE IF THE MESSAGE CONTAINS ANY BAD WORDS
            String[] stuff = message.split(" ");
            for (int i=0;i<badwords.size();i++){
                for (int j=0;j<stuff.length;j++){
                    if (stuff[j].equalsIgnoreCase(badwords.get(i)) && !event.getUser().getNick().startsWith("|")){
                        if(!event.getChannel().isHalfOp(event.getUser())&&!event.getChannel().isOwner(event.getUser())&&!event.getChannel().isOp(event.getUser())&&!event.getChannel().isSuperOp(event.getUser())){
                            event.getChannel().send().kick(event.getUser(), "Don't say "+badwords.get(i)+".  That's just turrable!");
                        }
                        break;
                    }
                }
            }
            
            // GETTING THE LIST OF BAD WORDS FROM THE ARRAY
            if (message.equalsIgnoreCase("!badwords")||message.equalsIgnoreCase("!list bad words")||message.equalsIgnoreCase("!list badwords")){
                String a=badwords.get(0);
                event.getBot().sendIRC().message(event.getChannel().getName(), "Users below HOP will be kicked for saying any of the following:");
                for (int i=1;i<badwords.size();i++){
                    a = a + ", " + badwords.get(i);
                }
                event.getBot().sendIRC().message(event.getChannel().getName(), a);
            }
        }
    }
    
    public ArrayList<String> getBadWords() throws FileNotFoundException{
        try{
            Scanner wordfile = new Scanner(new File("badwords.txt"));
            ArrayList<String> wordls = new ArrayList<>();
            while (wordfile.hasNext()){
                wordls.add(wordfile.next());
            }
            wordfile.close();
            return (wordls);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
