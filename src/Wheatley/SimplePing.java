/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.Socket;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author theDoctor
 * idea from jnick
 * 
 * Saved alongside Ping.java as a simple version that forgoes checking servers/sites
 * saved in an XML file
 * 
 */
public class SimplePing extends ListenerAdapter{
    @Override
    public void onMessage(MessageEvent event) throws FileNotFoundException, InterruptedException {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        if (message.startsWith("!pong")) {
            String[] totalip = event.getMessage().split(" ");
            int checkport = 0;
            if (totalip.length < 2) {
                event.getBot().sendIRC().notice(event.getUser().getNick(), "You didn't enter a vaild lookup. Enter !ping <HOSTNAME OR IP> <PORT>");
            }
            if (totalip.length == 2) {
                event.getBot().sendIRC().notice(event.getUser().getNick(), "No port specified, deafualting 80");
                checkport = 80;
            }
            if (totalip.length > 1) {
                checkport = checkport == 0 ? Integer.parseInt(totalip[2]) : 80;
                try {
                    InetAddress inet = InetAddress.getByName(totalip[1]);
                    if (totalip[1].equals(inet.getHostAddress())) {
                        event.getBot().sendIRC().notice(event.getUser().getNick(), "Pinging " + totalip[1] + ":" + checkport + "...");
                    } else {
                        event.getBot().sendIRC().notice(event.getUser().getNick(), "Pinging " + totalip[1] + " IP: " + inet.getHostAddress() + ":" + checkport + "...");
                    }
                    Socket socket = null;
                    boolean reachable = false;
                    try {
                        socket = new Socket(totalip[1], checkport);
                        reachable = true;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        if (socket != null) {
                            try {
                                socket.close();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                    event.getBot().sendIRC().message(event.getChannel().getName(), reachable ? totalip[1] + ":" + checkport + " is reachable" : totalip[1] + ":" + checkport + " is NOT reachable");
                } catch (Exception ex) {
                    event.getBot().sendIRC().notice(event.getUser().getNick(), "Hostname not found");
                    ex.printStackTrace();
                }
                
            }
        }
    }
}
//    public String[] pinghost(String[] address) {
//        String[] response = new String[2];
////        int checkport = 0;
////        boolean reachable = false;
////        if (address.length == 1) {
////            response[1]= "No port specified, deafualting 80";
////            checkport = 80;
////        }
////        else
//            int checkport = Integer.parseInt(address[1]);
//        if (address.length > 0) {
//            try {
////                InetAddress inet = InetAddress.getByName(address[0]);
////                if (address[0].equals(inet.getHostAddress())) {
////                    response[1]= "Pinging " + address[0] + ":" + checkport + "...";
////                } else {
////                    response[1]= "Pinging " + address[0] + " IP: " + inet.getHostAddress() + ":" + checkport + "...";
////                }
//                Socket socket = null;
//                
//                try {
//                    socket = new Socket(address[0], checkport);
//                    response[0] = "true";
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                    response[0] = "false";
//                } finally {
//                    if (socket != null) {
//                        try {
//                            socket.close();
//                        } catch (IOException ex) {
//                            ex.printStackTrace();
//                        }
//                    }
//                }
//                //response = reachable ? address[1] + ":" + checkport + " is reachable" : address[1] + ":" + checkport + " is NOT reachable";
//            }
//            catch (Exception ex) {
//                response[1] = "Hostname not found";
//                response[0]="false";
//                ex.printStackTrace();
//            }
//        }
//        return(response);
//    }
//}
//
//
////Document doc = dBuilder.parse(fXmlFile);
////NodeList serverlist = doc.getElementsByTagName("ping");
////   Node basenode = basesettings.item(0);
////Element serverelement = (Element) serverlist.item(0);
////int test = Integer.parseInt(serverelement.getElementsByTagName("test").item(0).getTextContent());
//
//// Get server settings from XML file for the server defined by the above commands
////NodeList nList = doc.getElementsByTagName("server");
////Node nNode = nList.item(test);
////Element eElement = (Element) nNode;
//
////            if (totalip.length < 2) {
////                // http://pastebin.com/1NbG2h88
////                event.getBot().sendIRC().notice(event.getUser().getNick(), "You didn't enter a vaild lookup. Enter !ping <HOSTNAME OR IP> <PORT>");
////            }
////            if (totalip.length == 2) {
////                event.getBot().sendIRC().notice(event.getUser().getNick(), "No port specified, deafualting 80");
////                checkport = 80;
////            }
////            if (totalip.length > 1) {
////                checkport = checkport == 0 ? Integer.parseInt(totalip[2]) : 80;
////                try {
////                    InetAddress inet = InetAddress.getByName(totalip[1]);
////                    if (totalip[1].equals(inet.getHostAddress())) {
////                        event.getBot().sendIRC().notice(event.getUser().getNick(), "Pinging " + totalip[1] + ":" + checkport + "...");
////                    } else {
////                        event.getBot().sendIRC().notice(event.getUser().getNick(), "Pinging " + totalip[1] + " IP: " + inet.getHostAddress() + ":" + checkport + "...");
////                    }
////                    Socket socket = null;
////                    boolean reachable = false;
////                    try {
////                        socket = new Socket(totalip[1], checkport);
////                        reachable = true;
////                    } catch (Exception ex) {
////                        ex.printStackTrace();
////                    } finally {
////                        if (socket != null) {
////                            try {
////                                socket.close();
////                            } catch (Exception ex) {
////                                ex.printStackTrace();
////                            }
////                        }
////                    }
////                    event.getBot().sendIRC().message(event.getChannel().getName(), reachable ? totalip[1] + ":" + checkport + " is reachable" : totalip[1] + ":" + checkport + " is NOT reachable");
////                } catch (Exception ex) {
////                    event.getBot().sendIRC().notice(event.getUser().getNick(), "Hostname not found");
////                    ex.printStackTrace();
////                }
////
////            }