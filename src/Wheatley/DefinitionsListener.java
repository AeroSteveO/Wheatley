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
 *    N/A
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
 *
 */
public class DefinitionsListener extends ListenerAdapter {
    ArrayList<String> definitions = getDefinitions();
    ArrayList<String> words = getWordsFromDefs(definitions);
    String definitionsFileName = "definitions.txt";
    String definitionLogName   = "definitionLog.txt";
    
    @Override
    public void onMessage(MessageEvent event) throws FileNotFoundException, InterruptedException {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        String[] msgSplit = message.split(" ");
        
        if (message.equalsIgnoreCase("!randef")||message.equalsIgnoreCase("!randdef")){
            
            int randNum = (int) (Math.random()*definitions.size()-1);
            event.getBot().sendIRC().message(event.getChannel().getName(),Colors.BOLD+definitions.get(randNum).split("@")[0].trim()+": "+Colors.NORMAL+definitions.get(randNum).split("@")[1].trim());
        }
        
        if (message.endsWith("?")&&message.split("\\?",2)[0].length()>0&&message.split("\\?").length==1&&StringUtils.countMatches(message, "?")==1){
            if (containsIgnoreCase(words,message.split("\\?")[0])){
                int index = indexOfIgnoreCase(words,message.split("\\?")[0]);
                event.getBot().sendIRC().message(event.getChannel().getName(),Colors.BOLD+words.get(index)+Colors.NORMAL+": "+definitions.get(index).split("@")[1].trim());
            }
        }
        
        if (message.equalsIgnoreCase("!list defs")){
            
            ArrayList<String> sortedWords = new ArrayList<>();
            sortedWords.addAll(words); // Add all words from the words array, so we don't affect the order of the words array
            Collections.sort(sortedWords, String.CASE_INSENSITIVE_ORDER); // Case insensitive order, cause we don't care 'bout no case
            
            //Create a pretty list of words
            String wordList = "";
            for (int i=0;i<sortedWords.size();i++){
                wordList = wordList + sortedWords.get(i)+", ";
            }// END create pretty list of words
            
            event.getBot().sendIRC().message(event.getUser().getNick(),wordList);
        }
        
        if (msgSplit[0].equalsIgnoreCase("tell")&&msgSplit[2].equalsIgnoreCase("about")){
            String user = message.split(" ")[1];
            if(event.getBot().getUserChannelDao().getAllUsers().contains(event.getBot().getUserChannelDao().getUser(user))) {
                
                //If the user is in the same channel as the summon
                String defWord = message.split("about")[1].trim();// Split the def you want to send from the user you want to send it to
                if (containsIgnoreCase(words,defWord)){
                    String def = definitions.get(indexOfIgnoreCase(words,defWord)).split("@")[1].trim(); // Split the definition off the full "word @ def" string
                    event.getBot().sendIRC().notice(event.getUser().getNick(),user+" has been PMed"); // Inform the sender of the successful command
                    event.getBot().sendIRC().message(event.getBot().getUserChannelDao().getUser(user).getNick(),event.getUser().getNick()+" wants me to tell you about: "+Colors.BOLD+defWord+Colors.NORMAL+ ": "+Colors.NORMAL+def);
                }
                else if (!event.getBot().getUserChannelDao().getChannels(event.getBot().getUserChannelDao().getUser("theTardis")).contains(event.getChannel())) {
                    event.getBot().sendIRC().notice(event.getUser().getNick(), Colors.BOLD+"tell "+Colors.NORMAL+"definition not found");
                }
            }
            else if (!event.getBot().getUserChannelDao().getChannels(event.getBot().getUserChannelDao().getUser("theTardis")).contains(event.getChannel())) {
                event.getBot().sendIRC().notice(event.getUser().getNick(), Colors.BOLD+"tell "+Colors.NORMAL+"user not in channel");
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
                
                else if (containsIgnoreCase(words,message.split(" ",2)[1].split("@")[0].trim())){
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"Definition already exists");
                }
                
                else {
                    
                    String addition = message.split(" ",2)[1]; // remove the command words from the definition addition
                    
                    try{
                        File file =new File(definitionsFileName);
                        //if file doesnt exists, then create it
                        if(!file.exists()){ 
                            file.createNewFile();
                        }
                        
                        FileWriter fileWritter = new FileWriter(file.getName(),true);
                        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
                        bufferWritter.write("\n"+addition); // Append our new definition text on the end of the file
                        bufferWritter.close();
                        event.getBot().sendIRC().notice(event.getUser().getNick(),"Success: "+addition+" was added to "+ definitionsFileName);
                    }catch(IOException e){
                        e.printStackTrace();
                        event.getBot().sendIRC().notice(event.getUser().getNick(),"SOMETHING BROKE: FILE NOT UPDATED");
                    }
                    definitions = getDefinitions(); // Add our new definition to the defs list
                    words = getWordsFromDefs(definitions); // Add our new to the words list
                }
            }
            else
                event.getBot().sendIRC().notice(event.getUser().getNick(),"You do not have access to this function");
        }
        
        // REMOVING DEFINITIONS
        if(msgSplit[0].equalsIgnoreCase("!rmdef")){
            
            if(event.getUser().getNick().equalsIgnoreCase(Global.botOwner)&&event.getUser().isVerified()){
                
                if (!containsIgnoreCase(words, message.split(" ",2)[1])){ // Can't remove a non-existant def
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"Definition not found");
                }
                else{
                    int index = indexOfIgnoreCase(words, message.split(" ",2)[1]);
                    try{
                        // Add all removed definitions to a definition log file, just in case
                        File log = new File(definitionLogName);
                        if(!log.exists()){// Create a new log file if we don't have one already
                            log.createNewFile();
                        }
                        FileWriter fileWritter = new FileWriter(log.getName(),true);
                        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
                        bufferWritter.write("\n"+definitions.get(index)); // Append file with our new log entry
                        bufferWritter.close();
                    }catch (Exception e){
                        event.getBot().sendIRC().notice(event.getUser().getNick(),"SOMETHING BROKE: LOG NOT UPDATED");
                    }
                    
                    // Now remove the unwanted definition from the defs file
                    definitions.remove(index); // Remove from def list
                    words.remove(index);       // Remove from word list
                    
                    // Overwrite the old file, with a brand new file
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
            }
            else
                event.getBot().sendIRC().notice(event.getUser().getNick(),"You do not have access to this function");
        }
        
        // Updating definitions already in the db
        
        if(msgSplit[0].equalsIgnoreCase("!overdef")) {
                        
            if (event.getUser().getNick().equalsIgnoreCase(Global.botOwner)&&event.getUser().isVerified()){
                
                if(!(message.split("@").length==2)){
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"Improperly formed update command: !overdef word phrase @ definition phrase");
                }
                
                else if (!containsIgnoreCase(words,message.split(" ",2)[1].split("@")[0].trim())){
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"Def currently does not exist, please use !adddef");
                }
                
                else{
                    
                    int index = indexOfIgnoreCase(words, message.split(" ",2)[1].split("@")[0].trim());
                    try{
                        // Add all removed definitions to a definition log file, just in case
                        File log = new File(definitionLogName);
                        if(!log.exists()){ // Create a new log file if we don't have one already
                            log.createNewFile();
                        }
                        FileWriter fileWritter = new FileWriter(log.getName(),true);
                        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
                        bufferWritter.write("\n"+definitions.get(index)); // Append file with our new log entry
                        bufferWritter.close();
                    }catch (Exception e){
                        event.getBot().sendIRC().notice(event.getUser().getNick(),"SOMETHING BROKE: LOG NOT UPDATED");
                    }
                    definitions.remove(index); // Remove from def list
                    words.remove(index);       // Remove from word list
                    definitions.add(message.split(" ",2)[1].trim()); // Add new definition to def list
                    words = getWordsFromDefs(definitions);           // Get new wordlist
                    
                    // Overwrite the old def file, write a new one with the updated def
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
            }
            else
                event.getBot().sendIRC().notice(event.getUser().getNick(),"You do not have access to this function");
        }
    }
    
    // Load definitions from file
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
    
    // Split the def words off of the definitions from the "word @ def" string
    private ArrayList<String> getWordsFromDefs(ArrayList<String> definitions){
        ArrayList<String> words = new ArrayList<>();
        for (int i=0;i<definitions.size();i++){
            words.add(definitions.get(i).split("@")[0].trim());
        }
        return words;
    }
    
    // Check if the arraylist contains a string, ignoring case
    private boolean containsIgnoreCase(ArrayList<String> o,String thing) {
        for (String s : o) {
            if (thing.equalsIgnoreCase(s)) return true;
        }
        return false;
    }
    
    // Get the index of a string from an array, ignoring case
    private int indexOfIgnoreCase(ArrayList<String> o,String thing) {
        for (int i=0;i<o.size();i++) {
            if (thing.equalsIgnoreCase(o.get(i))) return i;
        }
        return -1;
    }
}