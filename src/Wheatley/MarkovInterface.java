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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.w3c.dom.Element;
import org.jborg.*;
import org.pircbotx.Colors;

/**
 *
 * @author Stephen
 */
public class MarkovInterface extends ListenerAdapter{
    static ArrayList<String> botlist = null;
    int newLines = 0;
    String previousMessage = new String();
    int newLinesBeforeUpdate = 10;
    String markovFileName = "ImportMarkov";
    File markovFile = new File(markovFileName);
    
    JBorg Borg = new JBorg(1,10);
    boolean loaded =Borg.loadWords(markovFile);
    
    @Override
    public void onMessage(MessageEvent event) throws FileNotFoundException {
        String message = Colors.removeFormattingAndColors(event.getMessage());

       // boolean bot = isBot(event.getUser().getNick().toString());
        if (!message.startsWith("!")&&!message.startsWith(".")&&!isBot(event.getUser().getNick().toString())){
           Borg.learn(message);
           //new JBorg().loadWords(null)
           newLines++;
           if (newLines>=newLinesBeforeUpdate){
               newLines = 0;
               File oddFile = new File(markovFileName);
               Borg.saveWords(oddFile);
           }
           previousMessage=message;
        }
        if (message.equalsIgnoreCase("!line")){
            //String reply = new JBorg().generateReply(previousMessage);
            String reply = Borg.generateReply(previousMessage);
            event.getBot().sendIRC().message(event.getChannel().getName(), reply);
            previousMessage = reply;
        }
        //previousMessage=message;
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
