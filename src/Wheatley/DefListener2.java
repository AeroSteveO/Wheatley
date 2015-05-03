/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import Objects.Definitions;
import Utils.IRCUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Steve-O
 * Original Bot = SRSBSNS
 *
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    Definitions
 * - Linked Classes
 *    Global
 *
 * Activate Commands With
 *      [definition]?
 *          respond with the definition of that word/phrase (if there is a definition in the db)
 *      !mkdef [word/phrase] @ [word/phrase]
 *          adds the corresponding definition to the db, the word being the first part, the definition of said word being the second
 *      !rmdef [word/phrase]
 *          Removes the corresponding def from the database
 *      !overdef [word/phrase] @ [word/phrase]
 *          Updates the input def to the input phrase
 *      !listDefs
 *          Sends a PM with all the defs available
 *      !randdef
 *          Responds with a random definition form the DB
 *      tell [user] about [definition]
 *          Sends a pm to the user with the definition of the give word/phrase
 *      !whodef [word/phrase]
 *          Responds with who created the def and when the def was created
 *      !load
 *          Loads defs from previous generation def text files of the form "word or phrase @ defintion text"
 *
 */
public class DefListener2 extends ListenerAdapter {
    Definitions defs = new Definitions("definitions.json");
    Definitions defsLog = new Definitions("definitionLog.json");
    
    @Override
    public void onMessage(MessageEvent event) throws FileNotFoundException, InterruptedException {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        String[] msgSplit = message.split(" ");
        
        if (message.equalsIgnoreCase("!randef")||message.equalsIgnoreCase("!randdef")){
            String word = defs.getRandomWord();
            event.getBot().sendIRC().message(event.getChannel().getName(),Colors.BOLD+defs.getWordWithCase(word)+": "+Colors.NORMAL+defs.getDefOfWord(word));
        }
        else if (msgSplit[0].equalsIgnoreCase("!whodef")){
            String word = message.split(" ",2)[1];
            if(defs.containsDef(word)){
                
                event.getBot().sendIRC().message(event.getChannel().getName(), Colors.BOLD+defs.getWordWithCase(word)+Colors.NORMAL+" was defined by "+defs.getOriginator(word)+" at "+IRCUtils.getTimestamp(String.valueOf(Long.parseLong(defs.getTimeOfDef(word))*1000)));
            }
            else if (!event.getBot().getUserChannelDao().getChannels(event.getBot().getUserChannelDao().getUser("theTardis")).contains(event.getChannel())) {
                event.getBot().sendIRC().notice(event.getUser().getNick(), Colors.BOLD+"WhoDef: "+Colors.NORMAL+"Definition not found");
            }
        }
        
        if (message.endsWith("?")&&message.split("\\?",2)[0].length()>0&&message.split("\\?").length==1&&StringUtils.countMatches(message, "?")==1){
            if (defs.containsDef(message.split("\\?")[0])){
                String word = message.split("\\?")[0];
//                int index = indexOfIgnoreCase(words,message.split("\\?")[0]);
                event.getBot().sendIRC().message(event.getChannel().getName(),Colors.BOLD+defs.getWordWithCase(word)+Colors.NORMAL+": "+defs.getDefOfWord(word));
            }
        }
        //Global.getTimestamp(Long.toString(rs.getLong("TIME")*1000))
        //event.getTimestamp() / 1000
        
        
        if (message.equalsIgnoreCase("!list defs")){
            
            ArrayList<String> sortedWords = defs.getDefList();
//            sortedWords.addAll(words);
            Collections.sort(sortedWords, String.CASE_INSENSITIVE_ORDER);
            
            String wordList = "";
            for (int i=0;i<sortedWords.size();i++){
                wordList = wordList + sortedWords.get(i)+", ";
            }
            event.getBot().sendIRC().message(event.getUser().getNick(),wordList);
        }
        
        if (msgSplit[0].equalsIgnoreCase("tell")&&msgSplit[2].equalsIgnoreCase("about")){
            String user = message.split(" ")[1];
            if(event.getBot().getUserChannelDao().getAllUsers().contains(event.getBot().getUserChannelDao().getUser(user))) {
                
                //If the user is in the same channel as the summon
                String defWord = message.split("about")[1].trim();//.split(" ",2)[2];
                if (defs.containsDef(defWord)){
                    String def = defs.getDefOfWord(defWord);
                    event.getBot().sendIRC().notice(event.getUser().getNick(),user+" has been PMed");
                    event.getBot().sendIRC().message(event.getBot().getUserChannelDao().getUser(user).getNick(),event.getUser().getNick()+" wants me to tell you about: "+Colors.BOLD+defs.getWordWithCase(defWord)+Colors.NORMAL+ ": "+Colors.NORMAL+def);
                }
                else if (!event.getBot().getUserChannelDao().getChannels(event.getBot().getUserChannelDao().getUser("theTardis")).contains(event.getChannel())) {
                    event.getBot().sendIRC().notice(event.getUser().getNick(), Colors.BOLD+"tell "+Colors.NORMAL+"definition not found");
                }
            }
            else if (!event.getBot().getUserChannelDao().getChannels(event.getBot().getUserChannelDao().getUser("theTardis")).contains(event.getChannel())) {
                event.getBot().sendIRC().notice(event.getUser().getNick(), Colors.BOLD+"tell "+Colors.NORMAL+"user not in channel");
            }
        }
        
        
        
        if(msgSplit[0].equalsIgnoreCase("!load")){//||msgSplit[0].equalsIgnoreCase("!addef")
            
            if (event.getUser().getNick().equalsIgnoreCase(Global.botOwner)&&event.getUser().isVerified()){
                
                addDefsFromFile("definitions.txt", String.valueOf(event.getTimestamp()/1000));
                addDefLogFromFile("definitionLog.txt", String.valueOf(event.getTimestamp()/1000));
            }
        }
        
        
        
        
        // ADDING DEFINITIONS
        if(msgSplit[0].equalsIgnoreCase("!mkdef")){//||msgSplit[0].equalsIgnoreCase("!addef")
            
            if (event.getUser().getNick().equalsIgnoreCase(Global.botOwner)&&event.getUser().isVerified()){
                
                if(message.split("@").length!=2){
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"Improperly formed defintion add command: !mkdef word or phrase @ definition phrase");
                }
                
                else if (StringUtils.countMatches(message.split("@")[0], "?")>0){
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"Improperly formed defintion add command: Definition terms may not contain a \"?\"");
                }
                
                else if (defs.containsDef(message.split(" ",2)[1].split("@")[0].trim())){
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"Definition already exists");
                }
                
                else {
                    String word = message.split(" ",2)[1].split("@")[0].trim();
                    String def = message.split("@")[1].trim();
                    String user = event.getUser().getNick();
                    String time = String.valueOf(event.getTimestamp()/1000);
                    defs.createDef(word,def,user,time);
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"Success: "+word+" was added to the definitions file");
                }
            }
            else
                event.getBot().sendIRC().notice(event.getUser().getNick(),"You do not have access to this function");
        }
        
        // REMOVING DEFINITIONS
        if(msgSplit[0].equalsIgnoreCase("!rmdef")){//||msgSplit[0].equalsIgnoreCase("!deletedef")
            
            if(event.getUser().getNick().equalsIgnoreCase(Global.botOwner)&&event.getUser().isVerified()){
                String word = message.split(" ",2)[1];
                if (!defs.containsDef(word)){
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"Definition not found");
                }
                else{
                    String logWord = defs.getWordWithCase(word);
                    int i = 0;
                    while(defsLog.containsDef(logWord)){
                        logWord = word+String.valueOf(i);
                        i++;
                    }
                    defsLog.createDef(logWord,defs.getDefOfWord(word),defs.getOriginator(word),defs.getTimeOfDef(word));
                    
                    
                    boolean success = defs.deleteDef(word);
                    if(success)
                        event.getBot().sendIRC().notice(event.getUser().getNick(),"Success: "+word+" was removed from the definitions file");
                    else
                        event.getBot().sendIRC().notice(event.getUser().getNick(),"SOMETHING BROKE: DEF NOT DELETED");
                    
                }
            }
            else
                event.getBot().sendIRC().notice(event.getUser().getNick(),"You do not have access to this function");
        }
        
        // Updating definitions already in the db
        
        if(msgSplit[0].equalsIgnoreCase("!overdef")) {//||msgSplit[0].equalsIgnoreCase("!updef")
            
            if (event.getUser().getNick().equalsIgnoreCase(Global.botOwner)&&event.getUser().isVerified()){
                
                if(!(message.split("@").length==2)){
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"Improperly formed update command: !overdef word phrase @ definition phrase");
                }
                
                else if (!defs.containsDef(message.split(" ",2)[1].split("@")[0].trim())){
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"Def currently does not exist, please use !adddef");
                }
                
                else{
                    
                    String word = message.split(" ",2)[1].split("@")[0].trim();
                    System.out.println(word);
                    String logWord = defs.getWordWithCase(word);
                    int i = 0;
                    while(defsLog.containsDef(logWord)){
                        logWord = word+String.valueOf(i);
                        i++;
                    }
                    defsLog.createDef(logWord,defs.getDefOfWord(word),defs.getOriginator(word),defs.getTimeOfDef(word));
                    boolean success = defs.deleteDef(word);
                    defs.createDef(word, message.split("@")[1], event.getUser().getNick(), String.valueOf(event.getTimestamp()/1000));
                    
                    if (success)
                        event.getBot().sendIRC().notice(event.getUser().getNick(),"Success: "+message.split(" ",2)[1].split("@")[0].trim()+" was updated in the definitions file");
                    else
                        event.getBot().sendIRC().notice(event.getUser().getNick(),"SOMETHING BROKE: FILE NOT UPDATED");
                    
                }
            }
            else
                event.getBot().sendIRC().notice(event.getUser().getNick(),"You do not have access to this function");
        }
    }
    
    
    private void addDefsFromFile(String filename, String time){
        File file = new File(filename);
        if (!file.exists())
            return;
        try{
            Scanner wordfile = new Scanner(new File(filename));
            ArrayList<String> wordls = new ArrayList<String>();
            while (wordfile.hasNext()){
                wordls.add(wordfile.nextLine());
            }
            wordfile.close();
            
            for(int i=0;i<wordls.size();i++){
                defs.createDef(wordls.get(i).split("@")[0].trim(), wordls.get(i).split("@")[1].trim(), Global.botOwner, time);
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return;
        }
    }
    private void addDefLogFromFile(String filename, String time){
        File file = new File(filename);
        if (!file.exists())
            return;
        try{
            Scanner wordfile = new Scanner(new File(filename));
            ArrayList<String> wordls = new ArrayList<String>();
            while (wordfile.hasNext()){
                wordls.add(wordfile.nextLine());
            }
            wordfile.close();
            
            for(int i=0;i<wordls.size();i++){
                String word = wordls.get(i).split("@")[0].trim();
                String logWord = word;
                int o = 0;
                while (defsLog.containsDef(logWord)){
                    logWord = word + String.valueOf(o);
                    o++;
                }
                defsLog.createDef(logWord, wordls.get(i).split("@")[1].trim(), Global.botOwner, time);
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return;
        }
    }
}