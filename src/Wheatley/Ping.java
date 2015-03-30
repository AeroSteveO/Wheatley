/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;

/**
 *
 * @author Stphen    -- Added XML scraping to check groups of IP's by the owner
 * @author theDoctor -- Made the original ping function using Socket
 * @author jnick     -- Came up with the idea for the ping function
 *
 * Requirements:
 * - APIs
 *    Jaxen-1.1.6
 * - Custom Objects
 *    N/A
 * - Linked Classes
 *    Global
 *
 * Modified to allow me to remotely check to see if my servers are up
 *
 * Activate Command with:
 *      !ping [host] [port]
 *      !ping google.com
 *      !ping mc.myminecraft.com 25565
 *      !ping check [XML group]
 *      !ping check LAN
 */
public class Ping extends ListenerAdapter {
    
    @Override
    public void onMessage(MessageEvent event) throws FileNotFoundException, InterruptedException {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        
        
        
        if (message.startsWith(Global.commandPrefix)&&!message.matches("([ ]{0,}"+Global.commandPrefix+"{1,}[ ]{0,}){1,}")){
            String command = message.split(Global.commandPrefix)[1];
            String[] cmdSplit = command.split(" ");
            if(cmdSplit[0].equalsIgnoreCase("ping")) {
                String[] totalip = new String[2];
                String[] pingresponse = new String[2];;
                String[] address = new String[2];
                String pingmessage = new String();
                if (cmdSplit.length==1){
                    event.getBot().sendIRC().notice(event.getUser().getNick(), Colors.BOLD+"Ping: "+Colors.NORMAL+"too few inputs");
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
                    event.getBot().sendIRC().notice(event.getUser().getNick(), Colors.BOLD+"Ping: "+Colors.NORMAL+"too many inputs");
                    return;
                }
                
                
//            totalip = event.getMessage().split(" ");
//            pingresponse = new String[2];
//            address = new String[2];
                
                System.setProperty("java.net.preferIPv4Stack" , "true");
                if (cmdSplit.length==3&&cmdSplit[1].equalsIgnoreCase("check")&&!event.getChannel().getName().equals("#dtella")){
                    if(event.getUser().getNick().equals(Global.botOwner)){
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
                                        event.getBot().sendIRC().message(event.getChannel().getName(),pingmessage);
                                        pingmessage = null;
                                    }
                                }
                            }
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    else{
                        event.getBot().sendIRC().notice(event.getUser().getNick(),"You do not have access to this function");
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
                        event.getBot().sendIRC().message(event.getChannel().getName(),totalip[0]+":"+totalip[1] + " is " +Colors.GREEN + "online"+Colors.NORMAL);
                    else
                        event.getBot().sendIRC().message(event.getChannel().getName(), totalip[0]+":"+totalip[1] + " is " + Colors.RED + "offline"+Colors.NORMAL);
                }
            }
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
//                            ex.printStackTrace();
                        }
                    }
                }
            }
            catch (Exception ex) {
                response[1] = "Hostname not found";
                response[0]="false";
//                ex.printStackTrace();
            }
        }
        return(response);
    }
}