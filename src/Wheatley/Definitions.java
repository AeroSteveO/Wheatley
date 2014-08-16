/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Steve-O
 * Original Bot = SRSBSNS
 *
 * Activate Commands With
 *      [definition]?
 *          respond with the definition of that word/phrase (if there is a definition in the db)
 *      !addDef [word/phrase/ @ [word/phrase]
 *      !adDef [word/phrase] @ [word/phrase]
 *          adds the corresponding definition to the db, the word being the first part, the definition of said word being the second
 *      !delDef [word/phrase]
 *      !deleteDef [word/phrase]
 *          Removes the corresponding def from the database
 *      !listDefs
 *          Sends a PM with all the defs available
 *      !randdef
 *          Responds with a random definition form the DB
 */
public class Definitions extends ListenerAdapter {
    ArrayList<String> definitions = getDefinitions();
    ArrayList<String> words = getWordsFromDefs(definitions);
    public void onMessage(MessageEvent event) throws FileNotFoundException, InterruptedException {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        
        if (message.equalsIgnoreCase("!randef")||message.equalsIgnoreCase("!randdef")){
            int randNum = (int) (Math.random()*definitions.size()-1);
            event.getBot().sendIRC().message(event.getChannel().getName(),definitions.get(randNum).split("@")[0].trim()+": "+definitions.get(randNum).split("@")[1].trim());
        }
        
        if (message.endsWith("?")){
            if (containsIgnoreCase(words,message.split("\\?")[0])){
                event.getBot().sendIRC().message(event.getChannel().getName(),Colors.BOLD+message.split("\\?")[0].toLowerCase()+Colors.NORMAL+": "+definitions.get(indexOfIgnoreCase(words,message.split("\\?")[0])).split("@")[1].trim());
            }
        }
        
        if (message.equalsIgnoreCase("!listdefs")){
            String wordList = "";
            for (int i=0;i<words.size();i++){
                wordList = wordList + words.get(i)+", ";
            }
            event.getBot().sendIRC().message(event.getUser().getNick(),wordList);
        }
        // ADDING DEFINITIONS
        if((message.startsWith("!adddef")||message.startsWith("!addef"))&&message.split("@").length==2&&event.getUser().getNick().equalsIgnoreCase(Global.BotOwner)&&!containsIgnoreCase(words,message.split(" ",2)[1].split("@")[0].trim())){
            String filename = "definitions.txt";
            String addition = message.split(" ",2)[1];
            
            try{
                File file =new File(filename);
                //if file doesnt exists, then create it
                if(!file.exists()){
                    file.createNewFile();
                }
                //true = append file
                FileWriter fileWritter = new FileWriter(file.getName(),true);
                BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
                bufferWritter.write("\n"+addition);
                bufferWritter.close();
                event.getBot().sendIRC().message(event.getChannel().getName(),"Success: "+addition+" was added to "+ filename);
            }catch(IOException e){
                e.printStackTrace();
                event.getBot().sendIRC().notice(event.getUser().getNick(),"SOMETHING BROKE: FILE NOT UPDATED");
            }
            definitions = getDefinitions();
            words = getWordsFromDefs(definitions);
        }
        else if (event.getUser().getNick().equalsIgnoreCase(Global.BotOwner)&&(message.startsWith("!adddef")||message.startsWith("!addef"))&&containsIgnoreCase(words,message.split(" ",2)[1].split("@")[0].trim())){
            event.getBot().sendIRC().notice(event.getUser().getNick(),"Definition already exists");
        }
        else if((message.startsWith("!adddef")||message.startsWith("!addef"))&&!(message.split("@").length==2)&&event.getUser().getNick().equalsIgnoreCase(Global.BotOwner)){
            event.getBot().sendIRC().notice(event.getUser().getNick(),"Improperly formed defintion add command");
        }
        else if (message.startsWith("!adddef")||message.startsWith("!addef")){
            event.getBot().sendIRC().notice(event.getUser().getNick(),"You do not have access to this function");
        }
        
        // REMOVING DEFINITIONS
        if((message.startsWith("!deldef")||message.startsWith("!deletedef"))&&containsIgnoreCase(words,message.split(" ",2)[1])&&event.getUser().getNick().equalsIgnoreCase(Global.BotOwner)){
            int index = indexOfIgnoreCase(words, message.split(" ",2)[1]);
            try{
                File log = new File("definitionLog.txt");
                if(!log.exists()){
                    log.createNewFile();
                }
                FileWriter fileWritter = new FileWriter(log.getName(),true);
                BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
                bufferWritter.write("\n"+definitions.get(index));
                bufferWritter.close();
            }catch (Exception e){
                event.getBot().sendIRC().notice(event.getUser().getNick(),"SOMETHING BROKE: LOG NOT UPDATED");
            }
            
            definitions.remove(index);
            words.remove(index);
            File fnew=new File("definitions.txt");
            try{
                FileWriter f2 = new FileWriter(fnew, false);
                for (int i=0;i<definitions.size()-1;i++)
                    f2.write(definitions.get(i)+"\n");
                
                f2.write(definitions.get(definitions.size()-1));
                f2.close();
                event.getBot().sendIRC().message(event.getChannel().getName(),"Success: "+message.split(" ",2)[1]+" was removed from definitions.txt");
            } catch (IOException e) {
                e.printStackTrace();
                event.getBot().sendIRC().notice(event.getUser().getNick(),"SOMETHING BROKE: DEF NOT DELETED");
            }
            
        }
        else if ((message.startsWith("!deldef")||message.startsWith("!deletedef"))&&event.getUser().getNick().equalsIgnoreCase(Global.BotOwner)){
            event.getBot().sendIRC().notice(event.getUser().getNick(),"Definition not found");
        }
        else if (message.startsWith("!deldef")||message.startsWith("!deletedef")){
            event.getBot().sendIRC().notice(event.getUser().getNick(),"You do not have access to this function");
        }
        
        // Updating definitions already in the db
        if((message.startsWith("!updatedef")||message.startsWith("!updef"))&&message.split("@").length==2&&containsIgnoreCase(words,message.split(" ",2)[1].split("@")[0].trim())&&event.getUser().getNick().equalsIgnoreCase(Global.BotOwner)){
            int index = indexOfIgnoreCase(words, message.split(" ",2)[1].split("@")[0].trim());
            try{
                File log = new File("definitionLog.txt");
                if(!log.exists()){
                    log.createNewFile();
                }
                FileWriter fileWritter = new FileWriter(log.getName(),true);
                BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
                bufferWritter.write("\n"+definitions.get(index));
                bufferWritter.close();
            }catch (Exception e){
                event.getBot().sendIRC().notice(event.getUser().getNick(),"SOMETHING BROKE: LOG NOT UPDATED");
            }
            definitions.remove(index);
            words.remove(index);
            definitions.add(message.split(" ",2)[1].trim());
            words = getWordsFromDefs(definitions);
            File fnew=new File("definitions.txt");
            try {
                FileWriter f2 = new FileWriter(fnew, false);
                for (int i=0;i<definitions.size()-1;i++)
                    f2.write(definitions.get(i)+"\n");
                
                f2.write(definitions.get(definitions.size()-1));
                f2.close();
                event.getBot().sendIRC().message(event.getChannel().getName(),"Success: "+message.split(" ",2)[1].split("@")[0].trim()+" was updated in definitions.txt");
            } catch (IOException e) {
                e.printStackTrace();
                event.getBot().sendIRC().notice(event.getUser().getNick(),"SOMETHING BROKE: FILE NOT UPDATED");
            }
        }
        else if ((message.startsWith("!updatedef")||message.startsWith("!updef"))&&event.getUser().getNick().equalsIgnoreCase(Global.BotOwner)){
            event.getBot().sendIRC().notice(event.getUser().getNick(),"Improperly formed update command");
        }
        else if (message.startsWith("!updatedef")||message.startsWith("!updef")){
            event.getBot().sendIRC().notice(event.getUser().getNick(),"You do not have access to this function");
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
    private boolean containsIgnoreCase(ArrayList<String> o,String thing) {
        for (String s : o) {
            if (thing.equalsIgnoreCase(s)) return true;
        }
        return false;
    }
    private int indexOfIgnoreCase(ArrayList<String> o,String thing) {
        for (int i=0;i<o.size();i++) {
            if (thing.equalsIgnoreCase(o.get(i))) return i;
        }
        return -1;
    }
}