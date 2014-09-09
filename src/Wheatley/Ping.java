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
 * @author Steve-O
 * original Ping function by theDoctor
 * idea for ping function by jnick
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
        if ((message.toLowerCase().startsWith("!ping"))) { 
            String[] totalip = event.getMessage().split(" ");
            String[] pingresponse = new String[2];
            String[] address = new String[2];
            String pingmessage = new String();
            System.setProperty("java.net.preferIPv4Stack" , "true");
            if (totalip.length==3&&totalip[1].equalsIgnoreCase("check")&&event.getUser().getNick().equals(Global.BotOwner)&&!event.getChannel().getName().equals("#dtella")){
                try{
                    File fXmlFile = new File("SettingPing.xml");
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Element serverelement = (Element) dBuilder.parse(fXmlFile).getElementsByTagName("ping").item(0);
                    for (int i=0;i<serverelement.getElementsByTagName("server").getLength();i++){
                        if(totalip[2].equalsIgnoreCase(serverelement.getElementsByTagName("category").item(i).getTextContent())){
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
            else if (totalip.length < 2&& !event.getBot().getUserChannelDao().getChannels(event.getBot().getUserChannelDao().getUser("SamwiseGamgee")).contains(event.getChannel())) {
                event.getBot().sendIRC().notice(event.getUser().getNick(), "You didn't enter a vaild lookup. Enter !ping <HOSTNAME OR IP> <PORT>");
            }
            else {
                if (totalip.length>2){
                    address[0] = totalip[1];
                    address[1] = totalip[2];
                }
                else{
                    address[0] = totalip[1];
                    address[1] = "80";
                }
                pingresponse = pinghost(address);
                if (pingresponse[0].equals("true"))
                    event.getBot().sendIRC().message(event.getChannel().getName(),address[0]+":"+address[1] + " is " +Colors.GREEN + "online"+Colors.NORMAL);
                else
                    event.getBot().sendIRC().message(event.getChannel().getName(), address[0]+":"+address[1] + " is " + Colors.RED + "offline"+Colors.NORMAL);
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
                } catch (Exception ex) {
                    ex.printStackTrace();
                    response[0] = "false";
                } finally {
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
            catch (Exception ex) {
                response[1] = "Hostname not found";
                response[0]="false";
                ex.printStackTrace();
            }
        }
        return(response);
    }
}