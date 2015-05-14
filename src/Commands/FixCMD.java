/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Commands;

import Objects.ChannelStore;
import Objects.Command;
import Objects.CommandMetaData;
import Wheatley.Global;
import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.pircbotx.hooks.Event;
import org.w3c.dom.Element;

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
        return("Ghosts the bots nick, changes nick, identifies, and rejoins channels");
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
    public void processCommand(Event event){
        
        CommandMetaData data = new CommandMetaData(event,true);
        
        String caller = data.getCaller();
        boolean isVerified = data.isVerifiedBotOwner();
        String[] cmdSplit = data.getCommandSplit();
        
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
            Global.channels.removeDupes();
            for (int i=0;i<Global.channels.size();i++){
                event.getBot().sendIRC().joinChannel(Global.channels.get(i).toString());
            }
            event.getBot().sendIRC().notice(caller,"Fix completed");
        }
    }
    private void updateChannelsFromXML(){
        
        try{
            File fXmlFile = new File("Settings.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Element baseElement = (Element) dBuilder.parse(fXmlFile).getElementsByTagName("basicsettings").item(0);
            int test = Integer.parseInt(baseElement.getElementsByTagName("test").item(0).getTextContent());
            Element eElement = (Element) dBuilder.parse(fXmlFile).getElementsByTagName("server").item(test);
            
            
            
            for (int i=0;i<eElement.getElementsByTagName("channel").getLength();i++){ //Add channels from XML and load into channels Object
                Global.channels.add(new ChannelStore(eElement.getElementsByTagName("channel").item(i).getTextContent()));
            }
            
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}