/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.UserListEvent;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Steve-O
 */
public class GameBackbone extends ListenerAdapter{
    
    boolean isempty = true;
    private NodeList UserNodeList = getXML();
    
    List<String> Users = nodeListToArrayList(UserNodeList,"nick");
    List<String> Money = nodeListToArrayList(UserNodeList,"money");
    //List<String> Money = UserElement.getElementsByTagName("money");
    
    //for (int i=0;i<UserNodeList.getLength();i++){
    

    @Override
    public void onMessage(final MessageEvent event) throws Exception {
                
    }
    public void onJoin(final JoinEvent event) throws Exception {
        
        String nick = event.getUser().getNick();
        for (int i=0;i<Users.size();i++){
//            try{
//                UserElement.getElementsByTagName(nick).getElementsByTagName("Money").toString();
//                isempty = false;
//            }
//            catch (Exception ex){
//                isempty = true;
//            }
//            isempty = false;
            
//            if (Users.get(i).equalsIgnoreCase(nick)){
//                isempty = false;
//            }
        }
        if (isempty){
            Users.add(nick);
            Money.add("500");
           // XmlHelpers.addNode("nick", nick, );
        }
        
    }
    public void userListEvent(final UserListEvent event) throws Exception {
        
        //   com.google.common.collect.ImmutableSortedSet<User> Users = event.getUsers();
        //   ArrayList<User> stuff = Users.toArray();
//        for (int i=0;i<Users.size();i++){
//            if (Users.get(i).equalsIgnoreCase(nick)){
//                isempty = false;
//            }
//        }
//        if (isempty){
//            Users.add(nick);
//            Money.add("500");
//        }
        
    }
    public NodeList getXML() {
        try{
            File fXmlFile = new File("SettingUserInfo.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            NodeList UserNodeList = dBuilder.parse(fXmlFile).getElementsByTagName("basicsettings");
            Element UserElement = (Element) dBuilder.parse(fXmlFile).getElementsByTagName("user").item(0);
        }
        catch(Exception ex){
            ex.printStackTrace();
            Element UserElement = null;
        }
        return(UserNodeList);
    }
        public List<String> nodeListToArrayList(NodeList nodelist, String tagname) {
            List<String> converted = new ArrayList<>();

            for (int i=0;i<nodelist.getLength();i++){
                Node nNode = nodelist.item(i);
                Element eElement = (Element) nNode;
                converted.add(eElement.getElementsByTagName(tagname).item(i).getTextContent());
          //      Element eElement = (Element) nNode.getElementsByTagName(tagname).item(0);
            }
        return(converted);
    }
}

