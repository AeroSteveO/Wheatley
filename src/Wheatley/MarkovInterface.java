/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.w3c.dom.Element;
import org.jborg.*;
import org.pircbotx.Colors;

/**
 *
 * @author Steve-O
 * JBorg markov chain integration, based off seeborg which is based on pyborg
 * 
 * Activate Commands With
 *      !shutup
 *      !speak up
 *          To turn on and off the random response markov generator
 *      !set chance [num]
 *          To set the chance of the markov generator to respond (1/[num] chance)
 *      !save lines
 *          To save the current lines used in markov chain generation
 *      !line
 *          To command the bot to respond with a line
 *      [botnick], what do you think of [item]
 *          To force a seed word into the generator and command a reply
 *
 */
public class MarkovInterface extends ListenerAdapter{
    ArrayList<String> botlist = null;
    int newLines = 0;
    int newLinesBeforeUpdate = 10;
    
    String markovFileName = "ImportedMarkov";
    File markovFile = new File(markovFileName);
    JBorg Borg = new JBorg(1,15);
    boolean loaded = Borg.loadWords(markovFile);
    
    @Override
    public void onMessage(MessageEvent event) throws FileNotFoundException {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        String currentChan = event.getChannel().getName();
        int channelIndex = getChanIdx(currentChan);
        
        //Toggle off Markov Chain Talking
        if (Pattern.matches(Global.MainNick + ", (shutup|shut\\s+up)",message)||message.equalsIgnoreCase("!mute")||Pattern.matches("(shutup|shut\\s+up)\\s+"+Global.MainNick,message)){
            Global.Channels.get(channelIndex).speak = false;
        }
        
        //Toggle on Markov Chain Talking
        if (message.equalsIgnoreCase(Global.MainNick + ", speak up")||message.equalsIgnoreCase("!speak")){
            Global.Channels.get(channelIndex).speak = true;
        }
        
        //||event.getChannel().isOwner(event.getUser())
        if (message.toLowerCase().startsWith("!set chance ")&&(event.getUser().getNick().equalsIgnoreCase(Global.BotOwner)||event.getChannel().isOwner(event.getUser()))){
            String[] chanceSplit = message.split(" ");
            int inputChance = Integer.parseInt(chanceSplit[chanceSplit.length-1]);
            if (inputChance>0)
                Global.Channels.get(channelIndex).chance = inputChance;
            else
                event.getBot().sendIRC().message(currentChan, "Chance must be an integer value greater than 0");
        }
        if (message.toLowerCase().startsWith(Global.MainNick.toLowerCase()+", what do you think of")){
            String[] keyWord = message.split(" ");
            event.getBot().sendIRC().message(currentChan, Borg.generateReply(keyWord[keyWord.length-1]));
        }
        
        if (!message.startsWith("!")&&!message.startsWith(".")&&!isBot(event.getUser().getNick().toString())&&
                !Pattern.matches("[a-zA-Z_0-9]+?", message.toLowerCase())&&!Pattern.matches("[a-zA-Z]{1}", message)&&
                !Pattern.matches("[a-zA-Z_0-9]+\\++", message.toLowerCase())&&
                !message.toLowerCase().startsWith(Global.MainNick.toLowerCase()+", ")){
            Borg.learn(message);
            newLines++;
            
            //Automatically Save lines every 10 new messages added
            if (newLines>=newLinesBeforeUpdate){
                newLines = 0;
                Borg.saveWords(markovFile);
            }
            
            //Automatically speak with a 1/chance probability
            if (Global.Channels.get(channelIndex).chance==((int) (Math.random()*Global.Channels.get(channelIndex).chance)+1)&&Global.Channels.get(channelIndex).speak){
                String reply = Borg.generateReply(message);
                event.getBot().sendIRC().message(currentChan, reply);
            }
            Global.Channels.get(channelIndex).previousMessage = message;
        }
        
        //Command Wheatley to save his lines
        if (message.equalsIgnoreCase("!save lines")&&event.getUser().getNick().equalsIgnoreCase(Global.BotOwner)){
            newLines = 0;
            Borg.saveWords(markovFile);
        }
        
        //Command Wheatley to speak a line
        if (message.equalsIgnoreCase("!line")){
            String reply = Borg.generateReply(Global.Channels.get(channelIndex).previousMessage);
            event.getBot().sendIRC().message(currentChan, reply);
            Global.Channels.get(channelIndex).previousMessage = message;
        }
    }
    
    public ArrayList<String> getBotList() throws FileNotFoundException{
        try{
            ArrayList<String> botlist = new ArrayList<String>();
            File fXmlFile = new File("SettingMarkov.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Element eElement = (Element) dBuilder.parse(fXmlFile).getElementsByTagName("ignorebots").item(0);
            for (int i=0;i<eElement.getElementsByTagName("bot").getLength();i++)
            {
                botlist.add(eElement.getElementsByTagName("bot").item(i).getTextContent());
            }
            return (botlist);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return(null);
        }
    }
    
    public boolean isBot(String nick) throws FileNotFoundException {
        boolean bot = false;
        if (botlist==null){
            botlist = getBotList();
        }
        int i=0;
        while(bot==false&&i<botlist.size()){
            if (nick.equalsIgnoreCase(botlist.get(i))){
                bot=true;
            }
            i++;
        }
        return(bot);
    }
    
    public int getChanIdx(String toCheck){
        int idx = -1;
        for(int i = 0; i < Global.Channels.size(); i++) {
            if (Global.Channels.get(i).name.equalsIgnoreCase(toCheck)) {
                idx = i;
                break;
            }
        }
        if (idx==-1){
            Global.Channels.add(new ChannelStore(toCheck));
            idx = Global.Channels.size();
        }
        return (idx);
    }
}