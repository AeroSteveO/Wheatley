/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package rapternet.irc.bots.wheatley.commands;

import rapternet.irc.bots.common.objects.Command;
import rapternet.irc.bots.common.objects.CommandMetaData;
import rapternet.irc.bots.wheatley.listeners.Global;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;

/**
 *
 * @author Stephen
 */
public class EnglishSayingsCMD implements Command {
    ArrayList<String> sayings = getSayings();
    
    @Override
    public String toString() {
        return("Saying: outputs a randomly built saying based off of old sayings");
    }
    
    @Override
    public boolean isCommand(String toCheck) {
        return toCheck.equalsIgnoreCase(Global.mainNick+", you know what they say"); // Phrase that when spoken will activate the command
    }
    
    @Override
    public ArrayList<String> commandTerms() {
        ArrayList<String> a = new ArrayList<>();
        a.add("saying"); 
        return a;
    }
    
    @Override
    public ArrayList<String> help(String command) {
        ArrayList<String> a = new ArrayList<>();
        a.add(Colors.BOLD + Global.commandPrefix + "saying" + Colors.NORMAL + ": Provides a randomly generated english saying of some form" );
        a.add(Colors.BOLD + Global.commandPrefix + "saying [-,.]" + Colors.NORMAL + ": Provides a randomly generated english in the specified form" );
        a.add(Colors.BOLD + "[-]" + Colors.NORMAL + ": Generates a random 2 part hyphenated saying" );
        a.add(Colors.BOLD + "[.]" + Colors.NORMAL + ": Returns a random saying from the saying database" );
        a.add(Colors.BOLD + "[,]" + Colors.NORMAL + ": Generates a random 2 part comma separated saying" );
        return a;
    }

    
    @Override
    public void processCommand(Event event) {
        
        CommandMetaData data = new CommandMetaData(event, false);
        
        String[] cmdSplit = data.getCommandSplit();
        String respondTo = data.respondToCallerOrMessageChan();
        
        if (cmdSplit == null || cmdSplit.length == 1){
            switch((int) (Math.random()*3+1)) {
                case 1:
                    event.getBot().sendIRC().message(respondTo, sayings.get((int) (Math.random()*sayings.size()-1)));
                    break;
                case 2:
                    event.getBot().sendIRC().message(respondTo, randHyphenatedSaying());
                    break;
                case 3:
                    event.getBot().sendIRC().message(respondTo, randCommaSeparatedSaying());
                    break;
            }
        }
        else if (cmdSplit.length == 2) {
            if (cmdSplit[1].equals("-")){ //hyphenated sayings only
                event.getBot().sendIRC().message(respondTo, randHyphenatedSaying());
            }
            if (cmdSplit[1].equals(",")){ //comma separated sayings only
                event.getBot().sendIRC().message(respondTo, randCommaSeparatedSaying());
            }
            if (cmdSplit[1].equals(".")){ //purist sayings only
                event.getBot().sendIRC().message(respondTo, sayings.get((int) (Math.random()*sayings.size()-1)));
            }
        }
        
        
    }
    
    private ArrayList<String> getSayings() {
        try{
            Scanner wordfile = new Scanner(new File("englishsayings.txt"));
            ArrayList<String> wordls = new ArrayList<>();
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
        String saying;
        ArrayList<String> start = new ArrayList<>();
        ArrayList<String> middle = new ArrayList<>();
        ArrayList<String> end = new ArrayList<>();
        
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
        ArrayList<String> start = new ArrayList<>();
        ArrayList<String> end = new ArrayList<>();
        
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
