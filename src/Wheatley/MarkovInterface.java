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
 */
public class MarkovInterface extends ListenerAdapter{
    static ArrayList<String> botlist = null;
    int newLines = 0;
    String previousMessage = new String();
    int newLinesBeforeUpdate = 10;
    String markovFileName = "ImportedMarkov";
    File markovFile = new File(markovFileName);
    boolean speakUp = true;
    JBorg Borg = new JBorg(1,10);
    boolean loaded = Borg.loadWords(markovFile);
    int chance = 100; //chance of wheatley to speak
    
    @Override
    public void onMessage(MessageEvent event) throws FileNotFoundException {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        
        //Toggle off Markov Chain Talking
        if (message.equalsIgnoreCase(Global.MainNick + ", shutup")||message.equalsIgnoreCase("!mute")||message.equalsIgnoreCase(Global.MainNick+" shutup"))
            speakUp = false;
        
        //Toggle on Markov Chain Talking
        if (message.equalsIgnoreCase(Global.MainNick + ", speak up")||message.equalsIgnoreCase("!speak"))
            speakUp = true;
        //||event.getChannel().isOwner(event.getUser())
        if (message.toLowerCase().startsWith("!set chance ")&&event.getUser().getNick().equalsIgnoreCase(Global.BotOwner)){
            String[] chanceSplit = message.split(" ");
            chance = Integer.parseInt(chanceSplit[chanceSplit.length-1]);
        }
        
        if (!message.startsWith("!")&&!message.startsWith(".")&&!isBot(event.getUser().getNick().toString())&&!Pattern.matches("[a-zA-Z_0-9]+?", message.toLowerCase())&&!Pattern.matches("[a-zA-Z]{1}", message)){
            Borg.learn(message);
            newLines++;
            
            //Automatically Save lines every 10 new messages added
            if (newLines>=newLinesBeforeUpdate){
                newLines = 0;
                Borg.saveWords(markovFile);
            }
            
            //Automatically speak with a 1/chance probability
            if (chance==((int) (Math.random()*chance)+1)&&speakUp){
                String reply = Borg.generateReply(message);
                event.getBot().sendIRC().message(event.getChannel().getName(), reply);
            }
            previousMessage=message;
        }
        
        //Command Wheatley to save his lines
        if (message.equalsIgnoreCase("!save lines")&&event.getUser().getNick().equalsIgnoreCase(Global.BotOwner)){
            newLines = 0;
            Borg.saveWords(markovFile);
        }
        
        //Command Wheatley to speak a line
        if (message.equalsIgnoreCase("!line")){
            String reply = Borg.generateReply(previousMessage);
            event.getBot().sendIRC().message(event.getChannel().getName(), reply);
            previousMessage = reply;
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
            //for (int i=0;i<botlist.size();i++){
            if (nick.equalsIgnoreCase(botlist.get(i))){
                bot=true;
            }
            i++;
        }
        return(bot);
    }
}
