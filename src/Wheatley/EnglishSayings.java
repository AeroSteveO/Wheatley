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
import java.util.regex.Pattern;
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
            switch((int) (Math.random()*3+1)) {
                case 1:
                    event.getBot().sendIRC().message(event.getChannel().getName(),sayings.get((int) (Math.random()*sayings.size()-1)));
                    break;
                case 2:
                    event.getBot().sendIRC().message(event.getChannel().getName(),randHyphenatedSaying());
                    break;
                case 3:
                    event.getBot().sendIRC().message(event.getChannel().getName(),randCommaSeparatedSaying());
                    break;
            } 
        }
        if (message.equalsIgnoreCase("!saying -")){ //hyphenated sayings only
            event.getBot().sendIRC().message(event.getChannel().getName(),randHyphenatedSaying());
        }
        if (message.equalsIgnoreCase("!saying ,")){ //comma separated sayings only
            event.getBot().sendIRC().message(event.getChannel().getName(),randCommaSeparatedSaying());
        }
        if (message.equalsIgnoreCase("!saying .")){ //purist sayings only
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
    private String randHyphenatedSaying() {
        String saying = "";
        ArrayList<String> start = new ArrayList<String>();
        ArrayList<String> end = new ArrayList<String>();
        
        for (int i=0;i<sayings.size();i++){
            if (Pattern.matches("[a-zA-Z]+\\-[a-z,A-Z]+", sayings.get(i))){
                start.add(sayings.get(i).split("-")[0]);
                end.add(sayings.get(i).split("-")[1]);
//                System.out.print(sayings.get(i));
            }
        }
        return (start.get((int) (Math.random()*start.size()-1))+"-"+end.get((int) (Math.random()*end.size()-1)));
    }
        private String randCommaSeparatedSaying() {
        String saying = "";
        ArrayList<String> start = new ArrayList<String>();
        ArrayList<String> end = new ArrayList<String>();
        
        for (int i=0;i<sayings.size();i++){
            if (Pattern.matches("[a-zA-Z\\s]+\\,[a-zA-Z\\s]+", sayings.get(i))){
                try {
                start.add(sayings.get(i).split(",")[0]);
                end.add(sayings.get(i).split(",")[1]);
//                System.out.println(sayings.get(i));
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return (start.get((int) (Math.random()*start.size()-1))+","+end.get((int) (Math.random()*end.size()-1)));
    }
    
}
