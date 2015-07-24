/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import Objects.MapArray;
import Utils.IRCUtils;
import Utils.TextUtils;
import static Utils.TextUtils.loadTextAsList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.jborg.*;
import org.pircbotx.Colors;

/**
 *
 * @author Steve-O
 * Jborg markov chain integration, based off seeborg which is based on pyborg
 *
 * Requirements:
 * - APIs
 *    Jborg (https://code.google.com/p/jborg/)
 * - Custom Objects
 *    N/A
 * - Linked Classes
 *    Global
 *
 * Activate Commands With
 *     !mute
 *     !speak
 *         To turn on and off the random response markov generator
 *     !set chance [num]
 *         To set the chance of the markov generator to respond (1/[num] chance)
 *     !save
 *         To save the current lines used in markov chain generation
 *     !line
 *     [botnick], go on
 *     [botnick], continue
 *         To command the bot to respond with a line
 *     !words
 *         Responds with some stats about the markov database
 *     [botnick], what do you think of [item]
 *     [botnick], what do you think about [item]
 *         To force a seed word into the generator and command a reply
 *
 * ToDo:
 *
 */
public class MarkovInterface extends ListenerAdapter{
    ArrayList<String> botList = null;
    int newLines = 0;
    int newLinesBeforeUpdate = 10;
    MapArray previousLine = new MapArray(2);
    
    String botListFileName = "botList.txt";
    String markovFileName = "ImportedMarkov";
    File markovFile = new File(markovFileName);
    final JBorg borg = new JBorg(1,15);
    boolean loaded = borg.loadWords(markovFile);
    
    @Override
    public void onMessage(MessageEvent event) throws FileNotFoundException {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        String channel = event.getChannel().getName();
        
        //Toggle off Markov Chain Talking
        if (Pattern.matches(Global.mainNick + ", (shutup|shut\\s+up)",message)||message.equalsIgnoreCase("!mute")||Pattern.matches("(shutup|shut\\s+up)\\s+"+Global.mainNick,message)) {
            setMarkovSpeech("false", event.getChannel().getName());
            event.getBot().sendIRC().notice(event.getUser().getNick(), "Markov chain system muted");
        }
        
        //Toggle on Markov Chain Talking
        if (message.equalsIgnoreCase(Global.mainNick + ", speak up")||message.equalsIgnoreCase("!speak")) {
            setMarkovSpeech("true", event.getChannel().getName());
            event.getBot().sendIRC().notice(event.getUser().getNick(), "Markov chain system un-muted");
        }
        if (message.startsWith(Global.commandPrefix)&&!message.matches("([ ]{0,}"+Global.commandPrefix+"{1,}[ ]{0,}){1,}")){
            String command = message.split(Global.commandPrefix)[1];
            String[] cmdSplit = command.split(" ");
            
            if (cmdSplit[0].equalsIgnoreCase("words")) {
                event.getBot().sendIRC().message(channel, "I know "+borg.words.size()+" words ("+borg.numContexts+" contexts, "+String.valueOf(borg.numContexts/borg.words.size())+" per word), "+borg.lines.size()+" lines.");
            }
            if (message.toLowerCase().startsWith("!set chance ")
                    && ((event.getUser().getNick().equalsIgnoreCase(Global.botOwner) || event.getChannel().isOwner(event.getUser()))
                    && event.getUser().isVerified())) {
                
                String[] chanceSplit = message.split(" ");
                String chance = chanceSplit[chanceSplit.length-1];
                
                if(chance.matches("[0-9]+")) {
                    int inputChance = Integer.parseInt(chanceSplit[chanceSplit.length-1]);
                    
                    if (inputChance > 0) {
                        setMarkovChance(String.valueOf(inputChance), event.getChannel().getName());
                        event.getBot().sendIRC().notice(event.getUser().getNick(), "Chance set to: 1/"+inputChance);
                    }
                    
                    else
                        event.getBot().sendIRC().notice(event.getUser().getNick(), "Chance must be an integer value greater than 0");
                }
                else
                    event.getBot().sendIRC().notice(event.getUser().getNick(), "Chance must be an integer value greater than 0");
            }
            else if (cmdSplit[0].equalsIgnoreCase("ignore") && event.getUser().getNick().equalsIgnoreCase(Global.botOwner) && event.getUser().isVerified()) {
                if (cmdSplit.length > 2){
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"!Ignore: This command accepts a single input");
                }
                else {
                    botList.add(cmdSplit[1]);
                    TextUtils.addToDocIfUnique(botListFileName, cmdSplit[1]);
                    event.getBot().sendIRC().notice(event.getUser().getNick(),"Success! " + cmdSplit[1] + " was added to the Markov ignore list");
                }
            }
            else if (cmdSplit[0].equalsIgnoreCase("ignorelist")) {
                event.getBot().sendIRC().message(event.getUser().getNick(),"Ignored Users: " + IRCUtils.arrayListToString(botList));
            }
        }
        
        if (message.toLowerCase().startsWith(Global.mainNick.toLowerCase()+", what do you think of")|| message.toLowerCase().startsWith(Global.mainNick.toLowerCase()+", what do you think about")){
            
            String[] keyWord = message.replace("?","").split(" ");
            
            try{
                String response = "";
                int count = 0;
                while(response.split(" ").length<2){
                    response = borg.generateReply(keyWord[keyWord.length-1]);
                    count++;
                }
                event.getBot().sendIRC().message(channel, response);
                previousLine.addToLog(channel, message);
            }
            catch(Exception ex){
                event.getBot().sendIRC().notice(event.getUser().getNick(), "Word "+Colors.BOLD+keyWord[keyWord.length-1].trim()+Colors.NORMAL+" not found in the Markov system");
            }
        }
        
        if (!message.startsWith("!")&&!message.startsWith(".")&&
                !isBot(event.getUser().getNick().toString())&&
                !Pattern.matches("[a-zA-Z_0-9]+?", message.toLowerCase())&&
                !Pattern.matches("[a-zA-Z]{1}", message)&&
                !Pattern.matches("[a-zA-Z_0-9]+\\++", message.toLowerCase())&&
                !Pattern.matches("[0-9]+", message.toLowerCase())&&
                !(message.split(" ").length==1)&&
                !message.toLowerCase().startsWith(Global.mainNick.toLowerCase()+", ")){
            borg.learn(message);
            newLines++;
            
            //Automatically Save lines every 10 new messages added
            if (newLines >= newLinesBeforeUpdate) {
                synchronized(borg) {
                    newLines = 0;
                    borg.saveWords(markovFile);
                }
            }
            
            //Automatically speak with a 1/chance probability
            if (getMarkovChance(event.getChannel().getName()) == ((int) (Math.random()*getMarkovChance(event.getChannel().getName()))+1) && getMarkovSpeech(event.getChannel().getName())) {
                String reply;
                try{
                    reply = borg.generateReply(message);
                    event.getBot().sendIRC().message(channel, reply);
                }
                catch (Exception ex){
                    
                }
            }
            previousLine.addToLog(channel, message);
        }
        
        //Command Wheatley to save his lines
        if (message.equalsIgnoreCase("!save")
                && (event.getUser().getNick().equalsIgnoreCase(Global.botOwner)
                && event.getUser().isVerified())) {
            
            newLines = 0;
            borg.saveWords(markovFile);
        }
        
        //Command Wheatley to speak a line
        else if (message.equalsIgnoreCase("!line") 
                || message.equalsIgnoreCase(Global.mainNick + ", go on") 
                || message.equalsIgnoreCase(Global.mainNick + ", continue")) {
            
            ArrayList<String> reply = new ArrayList<>();
            String response = " ";
            for (int i=0;i<3;i++){
                reply.add(borg.generateReply(previousLine.getArray(channel).get(0).get(0)));
            }
            for (int i=0;i<3;i++){
                if (response.length()<reply.get(i).length())
                    response = reply.get(i);
            }
            event.getBot().sendIRC().message(channel, response);
            previousLine.addToLog(channel, response);
        }
    }
    
    public ArrayList<String> getBotList() throws FileNotFoundException{
        try{
            ArrayList<String> bots = loadTextAsList(botListFileName);
            
//            File fXmlFile = new File("SettingMarkov.xml");
//            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//            Element eElement = (Element) dBuilder.parse(fXmlFile).getElementsByTagName("ignorebots").item(0);
//            for (int i=0;i<eElement.getElementsByTagName("bot").getLength();i++)
//            {
//                bots.add(eElement.getElementsByTagName("bot").item(i).getTextContent());
//            }
            if (bots == null)
                return (new ArrayList<String>());
            
            return (bots);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return(null);
        }
    }
    
    private boolean isBot(String nick) throws FileNotFoundException {
        if (botList==null){
            botList = getBotList();
        }
        
        for(int i=0;i<botList.size();i++){
            if (nick.equalsIgnoreCase(botList.get(i))){
                return (true);
            }
        }
        return(false);
    }
    private void setMarkovSpeech(String speak, String channel){
        if (Global.settings.contains("markovspeak",channel)){
            Global.settings.set("markovspeak", speak, channel);
        }
        else{
            Global.settings.create("markovspeak", speak, channel);
        }
    }
    private void setMarkovChance(String chance, String channel){
        if (Global.settings.contains("markovchance",channel)){
            Global.settings.set("markovchance", chance, channel);
        }
        else{
            Global.settings.create("markovchance", chance, channel);
        }
    }
    private boolean getMarkovSpeech(String channel){
        if (Global.settings.contains("markovspeak",channel)){
            return Boolean.valueOf(Global.settings.get("markovspeak", channel));
        }
        else{
            Global.settings.create("markovspeak", "true", channel);
            return true;
        }
    }
    private int getMarkovChance(String channel){
        if (Global.settings.contains("markovchance",channel)){
            return Integer.valueOf(Global.settings.get("markovchance", channel));
        }
        else{
            Global.settings.create("markovchance", String.valueOf(100), channel);
            return(100);
        }
    }
}