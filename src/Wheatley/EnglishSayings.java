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
 *
 */
public class EnglishSayings extends ListenerAdapter {
    ArrayList<String> sayings = getSayings();
    
    public void onMessage(MessageEvent event) throws FileNotFoundException, InterruptedException {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        
        if (message.equalsIgnoreCase("!saying")){
//            if (sayings == null){
//                sayings = getSayings();
//            }
            event.getBot().sendIRC().message(event.getChannel().getName(),sayings.get((int) (Math.random()*sayings.size()-1)));
        }
        
        
    }
    private ArrayList<String> getSayings() {
        try{
            Scanner wordfile = new Scanner(new File("englishsayings.txt"));
            ArrayList<String> wordls = new ArrayList<String>();
            while (wordfile.hasNext()){
                wordls.add(wordfile.nextLine());
            }
            wordfile.close();
            return (wordls);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
