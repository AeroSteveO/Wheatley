/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package rapternet.irc.bots.wheatley.commands;

import rapternet.irc.bots.common.objects.Command;
import rapternet.irc.bots.common.objects.CommandMetaData;
import rapternet.irc.bots.wheatley.listeners.Global;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;
import org.w3c.dom.Element;

/**
 *
 * @author Stephen    -- Added XML scraping to check groups of IP's by the owner
 * @author theDoctor  -- Made the original ping function using Socket
 * @author jnick      -- Came up with the idea for the ping function
 *
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    N/A
 * - Linked Classes
 *    N/A
 *
 * Modified to allow me to remotely check to see if my servers are up
 *
 * Activate Command with:
 *      !ping [host] [port]
 *      !ping google.com
 *      !ping mc.myminecraft.com 25565
 *          Tries to open a socket to the input site via the input port
 *          If it is able to, the ping succeeds
 *          If it is unable to, the ping fails
 *      !ping check [XML group]
 *      !ping check LAN
 *          Pings each site and port grouping in the XML group input, and returns
 *          the results
 *
 */
public class PingCMD implements Command{
    
    @Override
    public String toString(){
        return("Ghosts the bots nick, changes nick, identifies, and rejoins channels");
    }
    
    @Override
    public boolean isCommand(String toCheck){
        return false;
    }
    
    @Override
    public ArrayList<String> commandTerms(){
        ArrayList<String> a = new ArrayList<>();
        a.add("ping");
        return a;
    }
    
    @Override
    public ArrayList<String> help(String command) {
        ArrayList<String> a = new ArrayList<>();
        a.add(Colors.BOLD + Global.commandPrefix + "ping [IP or URL]" + Colors.NORMAL + ": Attempts to ping the input IP or URL and responds with whether its online or offline");
        return a;
    }

    
    @Override
    public void processCommand(Event event){
        
        CommandMetaData data = new CommandMetaData(event,false);
        String[] cmdSplit = data.getCommandSplit();
//        boolean pingCheck = false;
        
        if (cmdSplit.length==3 && cmdSplit[0].equalsIgnoreCase("ping")&&cmdSplit[1].equalsIgnoreCase("check")){
            data = new CommandMetaData(event,true);
//            pingCheck = true;
        }
        
        String caller = data.getCaller();
        String respondTo = data.respondToIgnoreMessage();
        
        boolean isVerified = data.isVerifiedBotOwner();
//            String command = message.split(Global.commandPrefix)[1];
//            String[] cmdSplit = command.split(" ");
        
        
        String[] totalip = new String[2];
        String[] pingresponse;
        String[] address = new String[2];
        String pingmessage;
        
        if (cmdSplit.length==1){
            event.getBot().sendIRC().notice(caller, Colors.BOLD+"Ping: "+Colors.NORMAL+"too few inputs");
            return;
        }
        else if (cmdSplit[1].equalsIgnoreCase("check")){
            
        }
        else if (cmdSplit.length==2){
            if (cmdSplit[1].contains(":")){
                totalip=cmdSplit[1].split(":");
            }
            else{
                totalip[0]=cmdSplit[1];
                totalip[1]="80";
            }
        }
        else if (cmdSplit.length==3){
            totalip[0]=cmdSplit[1];
            totalip[1]=cmdSplit[2];
        }
        else{
            event.getBot().sendIRC().notice(caller, Colors.BOLD+"Ping: "+Colors.NORMAL+"too many inputs");
            return;
        }
        
        
        System.setProperty("java.net.preferIPv4Stack" , "true");
        if (cmdSplit.length==3&&cmdSplit[1].equalsIgnoreCase("check")){//&&!event.getChannel().getName().equals("#dtella")
            if(isVerified){
                try{
                    File fXmlFile = new File("SettingPing.xml");
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Element serverelement = (Element) dBuilder.parse(fXmlFile).getElementsByTagName("ping").item(0);
                    for (int i=0;i<serverelement.getElementsByTagName("server").getLength();i++){
                        if(cmdSplit[2].equalsIgnoreCase(serverelement.getElementsByTagName("category").item(i).getTextContent())){
                            address[0]=serverelement.getElementsByTagName("address").item(i).getTextContent();
                            address[1]="80";
                            pingresponse = pinghost(address);
                            if (pingresponse[0].equals("true"))
                                pingmessage = serverelement.getElementsByTagName("name").item(i).getTextContent() + " is " +Colors.GREEN + "online"+Colors.NORMAL;
                            else
                                pingmessage = serverelement.getElementsByTagName("name").item(i).getTextContent() + " is " + Colors.RED + "offline"+Colors.NORMAL;
                            
                            if (!serverelement.getElementsByTagName("port").item(i).getTextContent().isEmpty()&&serverelement.getElementsByTagName("serviceaddr").item(i).getTextContent().isEmpty()){
                                address[1]=serverelement.getElementsByTagName("port").item(i).getTextContent();
                                pingresponse = pinghost(address);
                                if (pingresponse[0].equalsIgnoreCase("true"))
                                    pingmessage = pingmessage + " and " + serverelement.getElementsByTagName("service").item(i).getTextContent() + " is " +Colors.GREEN + "online"+Colors.NORMAL;
                                else
                                    pingmessage = pingmessage + " and " + serverelement.getElementsByTagName("service").item(i).getTextContent() + " is " + Colors.RED + "offline"+Colors.NORMAL;
                            }
                            if (!serverelement.getElementsByTagName("serviceaddr").item(i).getTextContent().isEmpty()){
                                address[0]=serverelement.getElementsByTagName("serviceaddr").item(i).getTextContent();
                                address[1]=serverelement.getElementsByTagName("port").item(i).getTextContent();
                                pingresponse = pinghost(address);
                                if (pingresponse[0].equalsIgnoreCase("true"))
                                    pingmessage = pingmessage + " and " + serverelement.getElementsByTagName("service").item(i).getTextContent() + " is " +Colors.GREEN + "online"+Colors.NORMAL;
                                else
                                    pingmessage = pingmessage + " and " + serverelement.getElementsByTagName("service").item(i).getTextContent() + " is " + Colors.RED + "offline"+Colors.NORMAL;
                            }
                            if (pingmessage!=null){
                                event.getBot().sendIRC().message(respondTo,pingmessage);
                            }
                        }
                    }
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            else{
                event.getBot().sendIRC().notice(caller,"You do not have access to this function");
            }
        }
//            else if (totalip.length < 2&& !event.getBot().getUserChannelDao().getChannels(event.getBot().getUserChannelDao().getUser("SamwiseGamgee")).contains(event.getChannel())) {
//                event.getBot().sendIRC().notice(event.getUser().getNick(), "You didn't enter a vaild lookup. Enter !ping <HOSTNAME OR IP> <PORT>");
//            }
        else {
//                if (totalip.length>2){
//                    address = totalip;
//                }
//                else{
//                    address[0] = totalip[1];
//                    address[1] = "80";
//                }
            pingresponse = pinghost(totalip);
            if (pingresponse[0].equals("true"))
                event.getBot().sendIRC().message(respondTo,totalip[0]+":"+totalip[1] + " is " +Colors.GREEN + "online"+Colors.NORMAL);
            else
                event.getBot().sendIRC().message(respondTo, totalip[0]+":"+totalip[1] + " is " + Colors.RED + "offline"+Colors.NORMAL);
        }
    }
    
    
    public String[] pinghost(String[] address) {
        String[] response = new String[2];
        int checkport = Integer.parseInt(address[1]);
        if (address.length > 0) {
            try {
                Socket socket = null;
                
                try {
                    socket = new Socket(address[0], checkport);
                    response[0] = "true";
                } catch (IOException ex) {
//                    ex.printStackTrace();
                    response[0] = "false";
                } finally {
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException ex) {
//                            ex.printStackTrace(); // We don't care if it fails
                        }
                    }
                }
            }
            catch (Exception ex) {
                response[1] = "Hostname not found";
                response[0]="false";
            }
        }
        return(response);
    }
}