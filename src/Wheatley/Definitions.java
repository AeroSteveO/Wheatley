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
public class Definitions extends ListenerAdapter {
    ArrayList<String> definitions = getDefinitions();
    ArrayList<String> words = getWordsFromDefs(definitions);
    public void onMessage(MessageEvent event) throws FileNotFoundException, InterruptedException {
        String message = Colors.removeFormattingAndColors(event.getMessage());
//        if (definitions == null){
//            definitions = getDefinitions();
//        }
        if (message.equalsIgnoreCase("!randef")||message.equalsIgnoreCase("!randdef")){
            int randNum = (int) (Math.random()*definitions.size()-1);
            event.getBot().sendIRC().message(event.getChannel().getName(),definitions.get(randNum).split("@")[0].trim()+": "+definitions.get(randNum).split("@")[1].trim());
        }
        
        if (message.endsWith("?")){
            if (containsIgnoreCase(words,message.split("\\?")[0])){
                event.getBot().sendIRC().message(event.getChannel().getName(),message.split("\\?")[0].toLowerCase()+": "+definitions.get(indexOfIgnoreCase(words,message.split("\\?")[0])).split("@")[1].trim());
            }
        }
        
        
        
        
    }
    private ArrayList<String> getDefinitions() {
        try{
            Scanner wordfile = new Scanner(new File("definitions.txt"));
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
    private ArrayList<String> getWordsFromDefs(ArrayList<String> definitions){
        ArrayList<String> words = new ArrayList<String>();
        for (int i=0;i<definitions.size();i++){
            words.add(definitions.get(i).split("@")[0].trim());
        }
        return words;
    }
    public boolean containsIgnoreCase(ArrayList<String> o,String thing) {
        for (String s : o) {
            if (thing.equalsIgnoreCase(s)) return true;
        }
        return false;
    }
    public int indexOfIgnoreCase(ArrayList<String> o,String thing) {
        for (int i=0;i<o.size();i++) {
            if (thing.equalsIgnoreCase(o.get(i))) return i;
        }
        return -1;
    }
}
