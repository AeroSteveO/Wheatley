/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package rapternet.irc.bots.common.commands;

import rapternet.irc.bots.common.objects.Command;
import rapternet.irc.bots.common.objects.CommandMetaData;
import rapternet.irc.bots.wheatley.listeners.Global;
import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;
import org.w3c.dom.Element;
import rapternet.irc.bots.wheatley.objects.Env;

/**
 *
 * @author Stephen
 * 
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    Command
 *    CommandMetaData
 * - Utilities
 *    N/A
 * - Linked Classes
 *    Global
 * 
 * Activate Command with:
 *      Wheatley, fix yourself
 *          Ghosts/Recovers nick, rejoins channels it was disconnected from
 * 
 */
public class FixCMD implements Command{
    
    @Override
    public String toString(){
        return("Fix: Ghosts the bots nick, changes nick, identifies, and rejoins channels");
    }
    
    @Override
    public boolean isCommand(String toCheck){
        return toCheck.equalsIgnoreCase(Global.mainNick+", fix yourself");
    }
    
    @Override
    public ArrayList<String> commandTerms(){
        ArrayList<String> a = new ArrayList<>();
        a.add(null);
        return a;
    }
    
    @Override
    public ArrayList<String> help(String command) {
        ArrayList<String> a = new ArrayList<>();
        a.add(Colors.BOLD + Global.mainNick + ", fix yourself" + Colors.NORMAL + ": Tells the bot to recover its nick, change to its main nick, and rejoin all channels from the settings file" );
        return a;
    }
    
    
    @Override
    public void processCommand(Event event){
        
        CommandMetaData data = new CommandMetaData(event,true);
        
        String caller = data.getCaller();
        boolean isVerified = data.isVerifiedBotOwner();

        if(isVerified){
            updateChannelsFromXML();
            event.getBot().sendIRC().message("NickServ", "ghost " + Global.mainNick + " " + Global.nickPass);  //ghost is a depricated command, if it doesn't work, the next command should work
            event.getBot().sendIRC().message("NickServ", "recover " + Global.mainNick + " " + Global.nickPass);//sends both commands, NS can yell about one and do the other
            try{
                Thread.sleep(5000); // wait between killing the ghost to changing nick and registering
            }
            catch (Exception ex){
                
            }
            event.getBot().sendIRC().changeNick(Global.mainNick);
            event.getBot().sendIRC().message("NickServ", "identify " + Global.nickPass);
            
            updateChannelsFromXML();
            for (int i=0;i<Global.channels.size();i++){
                event.getBot().sendIRC().joinChannel(Global.channels.get(i));
            }
            event.getBot().sendIRC().notice(caller,"Fix completed");
        }
    }

    
    private void updateChannelsFromXML(){
        try{
            File fXmlFile = new File(Env.CONFIG_LOCATION + "Settings.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Element baseElement = (Element) dBuilder.parse(fXmlFile).getElementsByTagName("basicsettings").item(0);
            int test = Integer.parseInt(baseElement.getElementsByTagName("test").item(0).getTextContent());
            Element eElement = (Element) dBuilder.parse(fXmlFile).getElementsByTagName("server").item(test);
            
            
            
            for (int i=0;i<eElement.getElementsByTagName("channel").getLength();i++){ //Add channels from XML and load into channels Object
                Global.channels.add(eElement.getElementsByTagName("channel").item(i).getTextContent().toLowerCase());
            }
            
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}