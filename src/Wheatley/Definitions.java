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
 *      tell [user] about [definition]
 *          Sends a pm to the user with the definition of the give word/phrase
 *
 */
public class Definitions extends ListenerAdapter {
    ArrayList<String> definitions = getDefinitions();
    ArrayList<String> words = getWordsFromDefs(definitions);
    String definitionsFileName = "definitions.txt";
    String definitionLogName   = "definitionLog.txt";
    
    public void onMessage(MessageEvent event) throws FileNotFoundException, InterruptedException {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        
        if (message.equalsIgnoreCase("!randef")||message.equalsIgnoreCase("!randdef")){
            int randNum = (int) (Math.random()*definitions.size()-1);
            event.getBot().sendIRC().message(event.getChannel().getName(),Colors.BOLD+definitions.get(randNum).split("@")[0].trim()+": "+Colors.NORMAL+definitions.get(randNum).split("@")[1].trim());
        }
        
        if (message.endsWith("?")&&message.split("\\?",2)[0].length()>0){
            if (containsIgnoreCase(words,message.split("\\?")[0])){
                event.getBot().sendIRC().message(event.getChannel().getName(),Colors.BOLD+message.split("\\?")[0].toLowerCase()+Colors.NORMAL+": "+definitions.get(indexOfIgnoreCase(words,message.split("\\?")[0])).split("@")[1].trim());
            }
        }
        
        if (message.equalsIgnoreCase("!list defs")){
            String wordList = "";
            for (int i=0;i<words.size();i++){
                wordList = wordList + words.get(i)+", ";
            }
            event.getBot().sendIRC().message(event.getUser().getNick(),wordList);
        }
        
        if (message.toLowerCase().startsWith("tell ")&&message.toLowerCase().split(" ")[2].toLowerCase().startsWith("about")){
            String user = message.split(" ")[1];
            if(event.getBot().getUserChannelDao().getAllUsers().contains(event.getBot().getUserChannelDao().getUser(user))) {
                //If the user is in the same channel as the summon
                String defWord = message.split("about")[1].trim();//.split(" ",2)[2];
                if (containsIgnoreCase(words,defWord)){
                    String def = definitions.get(indexOfIgnoreCase(words,defWord)).split("@")[1].trim();
                    event.getBot().sendIRC().notice(event.getUser().getNick(),user+" has been PMed");
                    event.getBot().sendIRC().message(event.getBot().getUserChannelDao().getUser(user).getNick(),event.getUser().getNick()+" wants me to tell you about: "+Colors.BOLD+defWord+Colors.NORMAL+ ": "+Colors.NORMAL+def);
                }
                else if (!event.getBot().getUserChannelDao().getChannels(event.getBot().getUserChannelDao().getUser("srsbsns")).contains(event.getChannel())) {
                    event.getBot().sendIRC().notice(event.getUser().getNick(), Colors.BOLD+"tell "+Colors.NORMAL+"definition not found");
                }
            }
            else if (!event.getBot().getUserChannelDao().getChannels(event.getBot().getUserChannelDao().getUser("srsbsns")).contains(event.getChannel())) {
                event.getBot().sendIRC().notice(event.getUser().getNick(), Colors.BOLD+"tell "+Colors.NORMAL+"user not in channel");
            }
        }
        
        // ADDING DEFINITIONS
        if((message.startsWith("!adddef")||message.startsWith("!addef"))&&message.split("@").length==2&&event.getUser().getNick().equalsIgnoreCase(Global.botOwner)&&!containsIgnoreCase(words,message.split(" ",2)[1].split("@")[0].trim())){
            String addition = message.split(" ",2)[1];
            
            try{
                File file =new File(definitionsFileName);
                //if file doesnt exists, then create it
                if(!file.exists()){
                    file.createNewFile();
                }
                //true = append file
                FileWriter fileWritter = new FileWriter(file.getName(),true);
                BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
                bufferWritter.write("\n"+addition);
                bufferWritter.close();
                event.getBot().sendIRC().notice(event.getUser().getNick(),"Success: "+addition+" was added to "+ definitionsFileName);
            }catch(IOException e){
                e.printStackTrace();
                event.getBot().sendIRC().notice(event.getUser().getNick(),"SOMETHING BROKE: FILE NOT UPDATED");
            }
            definitions = getDefinitions();
            words = getWordsFromDefs(definitions);
        }
        else if (event.getUser().getNick().equalsIgnoreCase(Global.botOwner)&&(message.startsWith("!adddef")||message.startsWith("!addef"))&&containsIgnoreCase(words,message.split(" ",2)[1].split("@")[0].trim())){
            event.getBot().sendIRC().notice(event.getUser().getNick(),"Definition already exists");
        }
        else if((message.startsWith("!adddef")||message.startsWith("!addef"))&&!(message.split("@").length==2)&&event.getUser().getNick().equalsIgnoreCase(Global.botOwner)){
            event.getBot().sendIRC().notice(event.getUser().getNick(),"Improperly formed defintion add command: !adddef word or phrase @ definition phrase");
        }
        else if (message.startsWith("!adddef")||message.startsWith("!addef")){
            event.getBot().sendIRC().notice(event.getUser().getNick(),"You do not have access to this function");
        }
        
        // REMOVING DEFINITIONS
        if((message.startsWith("!deldef")||message.startsWith("!deletedef"))&&containsIgnoreCase(words,message.split(" ",2)[1])&&event.getUser().getNick().equalsIgnoreCase(Global.botOwner)){
            int index = indexOfIgnoreCase(words, message.split(" ",2)[1]);
            try{
                File log = new File(definitionLogName);
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
            File fnew=new File(definitionsFileName);
            try{
                FileWriter f2 = new FileWriter(fnew, false);
                for (int i=0;i<definitions.size()-1;i++)
                    f2.write(definitions.get(i)+"\n");
                
                f2.write(definitions.get(definitions.size()-1));
                f2.close();
                event.getBot().sendIRC().notice(event.getUser().getNick(),"Success: "+message.split(" ",2)[1]+" was removed from "+definitionsFileName);
            } catch (IOException e) {
                e.printStackTrace();
                event.getBot().sendIRC().notice(event.getUser().getNick(),"SOMETHING BROKE: DEF NOT DELETED");
            }
            
        }
        else if ((message.startsWith("!deldef")||message.startsWith("!deletedef"))&&event.getUser().getNick().equalsIgnoreCase(Global.botOwner)){
            event.getBot().sendIRC().notice(event.getUser().getNick(),"Definition not found");
        }
        else if (message.startsWith("!deldef")||message.startsWith("!deletedef")){
            event.getBot().sendIRC().notice(event.getUser().getNick(),"You do not have access to this function");
        }
        
        // Updating definitions already in the db
        if((message.startsWith("!updatedef")||message.startsWith("!updef"))&&message.split("@").length==2&&containsIgnoreCase(words,message.split(" ",2)[1].split("@")[0].trim())&&event.getUser().getNick().equalsIgnoreCase(Global.botOwner)){
            int index = indexOfIgnoreCase(words, message.split(" ",2)[1].split("@")[0].trim());
            try{
                File log = new File(definitionLogName);
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
            File fnew=new File(definitionsFileName);
            try {
                FileWriter f2 = new FileWriter(fnew, false);
                for (int i=0;i<definitions.size()-1;i++)
                    f2.write(definitions.get(i)+"\n");
                
                f2.write(definitions.get(definitions.size()-1));
                f2.close();
                event.getBot().sendIRC().notice(event.getUser().getNick(),"Success: "+message.split(" ",2)[1].split("@")[0].trim()+" was updated in "+definitionsFileName);
            } catch (IOException e) {
                e.printStackTrace();
                event.getBot().sendIRC().notice(event.getUser().getNick(),"SOMETHING BROKE: FILE NOT UPDATED");
            }
        }
        else if ((message.startsWith("!updatedef")||message.startsWith("!updef"))&&event.getUser().getNick().equalsIgnoreCase(Global.botOwner)){
            event.getBot().sendIRC().notice(event.getUser().getNick(),"Improperly formed update command: !updef word phrase @ definition phrase");
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