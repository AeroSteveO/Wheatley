/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rapternet.irc.bots.wheatley.listeners;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Steve-O
 *      Function Based off one on theTardis, written by theDoctor
 *      Modified to run off an XML file
 * 
 */
public class AutodlText extends ListenerAdapter {
    
        @Override
        public void onMessage(final MessageEvent event) throws Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage());        
        if (event.getUser().getNick().equals("SHODAN")){  //Auto Download bot nick
            if(message.startsWith("Saved")){
                // LOAD XML
                File fXmlFile = new File("Settings.xml");
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(fXmlFile);
                NodeList nList = doc.getElementsByTagName("email");
                int mailsetting = 0;  //NOTE: can setup multiple email profiles for different functions here
                Node nNode = nList.item(mailsetting);
                Element eElement = (Element) nNode;
                //SEND EMAIL STUFF
                Email email = new SimpleEmail();
                email.setHostName(eElement.getElementsByTagName("hostname").item(0).getTextContent());
                email.setSmtpPort(465);
                email.setAuthenticator(new DefaultAuthenticator(eElement.getElementsByTagName("username").item(0).getTextContent(), eElement.getElementsByTagName("password").item(0).getTextContent()));
                email.setSSLOnConnect(true);
                email.setFrom(eElement.getElementsByTagName("from").item(0).getTextContent());
                email.setSubject(eElement.getElementsByTagName("subject").item(0).getTextContent());
                email.setMsg(message);
                email.addTo(eElement.getElementsByTagName("to").item(0).getTextContent());
                email.send();
                event.getBot().sendIRC().message(event.getChannel().getName(),"AAAaaaAAHhhH I just connected to an email server, I feel dirty");
            }
        }
    }
}