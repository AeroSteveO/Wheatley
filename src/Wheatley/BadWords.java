/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

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
 */
public class BadWords extends ListenerAdapter{
    static ArrayList<String> badwords = null;
    @Override
    public void onMessage(final MessageEvent event) throws Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        if (badwords == null)
            badwords = getBadWords();
        if (message.toLowerCase().startsWith("!update badwordlist"))
            if (message.split(" ").length==3)
                badwords.add(message.split(" ")[2]);
        
        for (int i=0;i<badwords.size();i++){
            if (message.contains(badwords.get(i))&&!event.getChannel().isHalfOp(event.getUser())&&!event.getChannel().isOwner(event.getUser())&&!event.getChannel().isOp(event.getUser())&&!event.getChannel().isSuperOp(event.getUser()))
                event.getChannel().send().kick(event.getUser(), "Don't say "+badwords.get(i)+".  That's just turrable!");
        }
        if (message.equalsIgnoreCase("!badwords")){
            String a=badwords.get(0);
            event.getBot().sendIRC().message(event.getChannel().getName(), "Users below HOP will be kicked for saying any of the following:");
            for (int i=1;i<badwords.size();i++){
                a = a + ", " + badwords.get(i);
            }
            event.getBot().sendIRC().message(event.getChannel().getName(), a);
        }
    }
    
    public ArrayList<String> getBadWords() throws FileNotFoundException{
        try{
            Scanner wordfile = new Scanner(new File("badwordlist.txt"));
            ArrayList<String> wordls = new ArrayList<String>();
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
