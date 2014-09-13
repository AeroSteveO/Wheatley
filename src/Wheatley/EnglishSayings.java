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
 * Source: http://www.phrases.org.uk/meanings/phrases-and-sayings-list.html
 *
 * Activate Commands With:
 *      !saying
 *          responds with a random saying using a randomly chosen type
 *      !saying [, . -]
 *          responds with a random saying using the specified type, comma separated saying (,),
 *          hyphenated saying (-), or a straight up saying (.)
 * 
 * Requires: 
 *      englishsayings.txt
 *
 */
public class EnglishSayings extends ListenerAdapter {
    ArrayList<String> sayings = getSayings();
    
    public void onMessage(MessageEvent event) throws FileNotFoundException, InterruptedException {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        
        if (message.equalsIgnoreCase("!saying")||message.equalsIgnoreCase("you know what they say")||message.equalsIgnoreCase(Global.mainNick+", you know what they say")){
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
        ArrayList<String> middle = new ArrayList<String>();
        ArrayList<String> end = new ArrayList<String>();
        
        for (int i=0;i<sayings.size();i++){
            if (Pattern.matches("[-a-zA-Z]+", sayings.get(i))){
                String[] grabbedSaying = sayings.get(i).split("-");
                
                if (grabbedSaying.length==2){
                    start.add(grabbedSaying[0]);
                    end.add(grabbedSaying[1]);
                }
                else if (grabbedSaying.length>2){
//                    System.out.println("three part hyphenated saying");
                    start.add(grabbedSaying[0]);
                    end.add(grabbedSaying[grabbedSaying.length-1]);
//                    for(int c=1;c<grabbedSaying.length-2;i++){
//                        middle.add(grabbedSaying[c]);
//                        System.out.println(grabbedSaying[c]);
//                    }
                    middle.add(grabbedSaying[1]);
                }
//                System.out.println(sayings.get(i));
            }
        }
        saying = start.get((int) (Math.random()*start.size()-1))+"-";
        int size = (int) (Math.random()*110-1);
//        int mid = (int) (Math.random()*3-1);
//        int index;
//        for (int i=0; i<=mid;i++){
//            index = (int) (Math.random()*middle.size()-1);
//            saying = saying + middle.get(index)+"-";
//            //middle.remove(index);
//        }
        if (size>90)
            saying = saying+middle.get((int) (Math.random()*middle.size()-1))+"-";
        
        saying = saying+end.get((int) (Math.random()*end.size()-1));
        return (saying);
        
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
