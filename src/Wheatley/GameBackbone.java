/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.UserListEvent;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import Wheatley.XmlHelpers.*;
import org.pircbotx.Colors;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Text;


/**
 *
 * @author Steve-O
 */
public class GameBackbone extends ListenerAdapter{
    
    boolean isempty = true;
    private NodeList UserNodeList = getXML();
    String fileName = "SettingUserInfo.XML";
//    List<String> Users = nodeListToArrayList(UserNodeList,"nick");
//    List<String> Money = nodeListToArrayList(UserNodeList,"money");
    
    
    @Override
    public void onMessage(MessageEvent event) throws InterruptedException, Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        doXML();
    }
    
    public void doXML() throws Exception {
        
        try{
        DOMParser parser = new DOMParser();
        parser.parse(fileName);
        Document doc = parser.getDocument();
        
        // Get the document's root XML node
        NodeList root = doc.getChildNodes();
        
        // Navigate down the hierarchy to get to the CEO node
        Node settingType = getNode("UserSetting", root);
        Node user = getNode("user", settingType.getChildNodes() );
//        String execType = getNodeAttr("type", user);
        
        // Load the executive's data from the XML
        NodeList nodes = user.getChildNodes();
        String nick = getNodeValue("nick", nodes);
        String money = getNodeValue("money", nodes);
        String level = getNodeValue("adminlevel", nodes);
//        String city = getNodeValue("city", nodes);
//        String state = getNodeValue("state", nodes);
//        String zip = getNodeValue("zip", nodes);
        
        System.out.println("Executive Information:");
//        System.out.println("Type: " + execType);
        System.out.println(nick + ", $" + money);
        System.out.println(level);
//        System.out.println(city + ", " + state + " " + zip);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        
    }
    
//    @Override
//    public void onMessage(final MessageEvent event) throws Exception {
//        
//    }
    public void onJoin(final JoinEvent event) throws Exception {
        
        String nick = event.getUser().getNick();
//        for (int i=0;i<Users.size();i++){
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
//        }
//        if (isempty){
//            Users.add(nick);
//            Money.add("500");
//            // XmlHelpers.addNode("nick", nick, );
//        }
        
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
        protected void setNodeValue(String tagName, String value, NodeList nodes) {
        Node node = getNode(tagName, nodes);
        if ( node == null )
            return;
        
        // Locate the child text node and change its value
        NodeList childNodes = node.getChildNodes();
        for (int y = 0; y < childNodes.getLength(); y++ ) {
            Node data = childNodes.item(y);
            if ( data.getNodeType() == Node.TEXT_NODE ) {
                data.setNodeValue(value);
                return;
            }
        }
    }
    
    protected void addNode(String tagName, String value, Node parent) {
        Document dom = parent.getOwnerDocument();
        
        // Create a new Node with the given tag name
        Node node = dom.createElement(tagName);
        
        // Add the node value as a child text node
        Text nodeVal = dom.createTextNode(value);
        Node c = node.appendChild(nodeVal);
        
        // Add the new node structure to the parent node
        parent.appendChild(node);
    }
    
    protected Node getNode(String tagName, NodeList nodes) {
        for ( int x = 0; x < nodes.getLength(); x++ ) {
            Node node = nodes.item(x);
            if (node.getNodeName().equalsIgnoreCase(tagName)) {
                return node;
            }
        }
        
        return null;
    }
    
    protected String getNodeValue( Node node ) {
        NodeList childNodes = node.getChildNodes();
        for (int x = 0; x < childNodes.getLength(); x++ ) {
            Node data = childNodes.item(x);
            if ( data.getNodeType() == Node.TEXT_NODE )
                return data.getNodeValue();
        }
        return "";
    }
    
    protected String getNodeValue(String tagName, NodeList nodes ) {
        for ( int x = 0; x < nodes.getLength(); x++ ) {
            Node node = nodes.item(x);
            if (node.getNodeName().equalsIgnoreCase(tagName)) {
                NodeList childNodes = node.getChildNodes();
                for (int y = 0; y < childNodes.getLength(); y++ ) {
                    Node data = childNodes.item(y);
                    if ( data.getNodeType() == Node.TEXT_NODE )
                        return data.getNodeValue();
                }
            }
        }
        return "";
    }
    
    protected String getNodeAttr(String attrName, Node node ) {
        NamedNodeMap attrs = node.getAttributes();
        for (int y = 0; y < attrs.getLength(); y++ ) {
            Node attr = attrs.item(y);
            if (attr.getNodeName().equalsIgnoreCase(attrName)) {
                return attr.getNodeValue();
            }
        }
        return "";
    }
    
    protected String getNodeAttr(String tagName, String attrName, NodeList nodes ) {
        for ( int x = 0; x < nodes.getLength(); x++ ) {
            Node node = nodes.item(x);
            if (node.getNodeName().equalsIgnoreCase(tagName)) {
                NodeList childNodes = node.getChildNodes();
                for (int y = 0; y < childNodes.getLength(); y++ ) {
                    Node data = childNodes.item(y);
                    if ( data.getNodeType() == Node.ATTRIBUTE_NODE ) {
                        if ( data.getNodeName().equalsIgnoreCase(attrName) )
                            return data.getNodeValue();
                    }
                }
            }
        }
        return "";
    }
}

