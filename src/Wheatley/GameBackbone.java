///*
//* To change this license header, choose License Headers in Project Properties.
//* To change this template file, choose Tools | Templates
//* and open the template in the editor.
//*/
//
//package Wheatley;
//
//import com.sun.org.apache.xerces.internal.parsers.DOMParser;
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import org.pircbotx.hooks.ListenerAdapter;
//import org.pircbotx.hooks.events.JoinEvent;
//import org.pircbotx.hooks.events.MessageEvent;
//import org.pircbotx.hooks.events.UserListEvent;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import Wheatley.XmlHelpers.*;
//import javax.xml.transform.OutputKeys;
//import javax.xml.transform.Transformer;
//import javax.xml.transform.TransformerFactory;
//import javax.xml.transform.dom.DOMSource;
//import javax.xml.transform.stream.StreamResult;
//import org.pircbotx.Colors;
//import org.w3c.dom.Document;
//import org.w3c.dom.NamedNodeMap;
//import org.w3c.dom.Text;
//
//
///**
// *
// * @author Steve-O
// */
//public class GameBackbone extends ListenerAdapter{
//    
//    boolean isempty = true;
//    private NodeList UserNodeList = getXML();
//    static String filePath = "SettingUserInfo.XML";
////    List<String> Users = nodeListToArrayList(UserNodeList,"nick");
////    List<String> Money = nodeListToArrayList(UserNodeList,"money");
//    
//    
//    @Override
//    public void onMessage(MessageEvent event) throws InterruptedException, Exception {
//        String message = Colors.removeFormattingAndColors(event.getMessage());
//        
//    }
//    public static void main(String[] args) {
//        //      String filePath = "employee.xml";
//        File xmlFile = new File(filePath);
//        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder dBuilder;
//        try {
//            dBuilder = dbFactory.newDocumentBuilder();
//            Document doc = dBuilder.parse(xmlFile);
//            doc.getDocumentElement().normalize();
//            
//            //update attribute value
//            updateAttributeValue(doc);
//            
//            //update Element value
//            updateElementValue(doc);
//            
//            //delete element
//            deleteElement(doc);
//            
//            //add new element
//            addElement(doc);
//            
//            //write the updated document to file or console
//            doc.getDocumentElement().normalize();
//            TransformerFactory transformerFactory = TransformerFactory.newInstance();
//            Transformer transformer = transformerFactory.newTransformer();
//            DOMSource source = new DOMSource(doc);
//            StreamResult result = new StreamResult(new File("SettingUserInfo_Updated.xml"));
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//            transformer.transform(source, result);
//            System.out.println("XML file updated successfully");
//            
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
//    }
//    
//    //this method should be modified correctly for SettingUserInfo.xml
//    private static void addElement(Document doc) {
//        //Use these for inputs as the stuff gets rebuilt for the Game functions
//        String tagName = "money";
//        String tagValue = "500";
//        String tagParent = "user";
//        NodeList employees = doc.getElementsByTagName(tagParent);
//        Element emp = null;
//        
//        //loop for each employee
//        for(int i=0; i<employees.getLength();i++){
//            
//            emp = (Element) employees.item(i);
//            Element newElement = doc.createElement(tagName);
//            newElement.appendChild(doc.createTextNode(tagValue));
//            emp.appendChild(newElement);
//        }
//    }
//    
//    private static void deleteElement(Document doc) {
//        NodeList employees = doc.getElementsByTagName("Employee");
//        Element emp = null;
//        //loop for each employee
//        for(int i=0; i<employees.getLength();i++){
//            emp = (Element) employees.item(i);
//            Node genderNode = emp.getElementsByTagName("gender").item(0);
//            emp.removeChild(genderNode);
//        }
//        
//    }
//    
//    private static void updateElementValue(Document doc) {
//        String tagName = "money";
//        String tagValue = "500";
//        String tagParent = "user";
//        
//        NodeList employees = doc.getElementsByTagName("Employee");
//        Element emp = null;
//        //loop for each employee
//        for(int i=0; i<employees.getLength();i++){
//            emp = (Element) employees.item(i);
//            Node name = emp.getElementsByTagName("name").item(0).getFirstChild();
//            name.setNodeValue(name.getNodeValue().toUpperCase());
//        }
//    }
//    
//    private static void updateAttributeValue(Document doc) {
//        NodeList employees = doc.getElementsByTagName("Employee");
//        Element emp = null;
//        //loop for each employee
//        for(int i=0; i<employees.getLength();i++){
//            emp = (Element) employees.item(i);
//            String gender = emp.getElementsByTagName("gender").item(0).getFirstChild().getNodeValue();
//            if(gender.equalsIgnoreCase("male")){
//                //prefix id attribute with M
//                emp.setAttribute("id", "M"+emp.getAttribute("id"));
//            }else{
//                //prefix id attribute with F
//                emp.setAttribute("id", "F"+emp.getAttribute("id"));
//            }
//        }
//    }
//    
//    
//    
//    
//    
//    public void onJoin(final JoinEvent event) throws Exception {
//        
//        String nick = event.getUser().getNick();
//        
//    }
//    public void userListEvent(final UserListEvent event) throws Exception {
//        
//    }
//    public NodeList getXML() {
//        try{
//            File fXmlFile = new File("SettingUserInfo.xml");
//            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//            NodeList UserNodeList = dBuilder.parse(fXmlFile).getElementsByTagName("basicsettings");
//            Element UserElement = (Element) dBuilder.parse(fXmlFile).getElementsByTagName("user").item(0);
//        }
//        catch(Exception ex){
//            ex.printStackTrace();
//            Element UserElement = null;
//        }
//        return(UserNodeList);
//    }
//    public List<String> nodeListToArrayList(NodeList nodelist, String tagname) {
//        List<String> converted = new ArrayList<>();
//        
//        for (int i=0;i<nodelist.getLength();i++){
//            Node nNode = nodelist.item(i);
//            Element eElement = (Element) nNode;
//            converted.add(eElement.getElementsByTagName(tagname).item(i).getTextContent());
//            //      Element eElement = (Element) nNode.getElementsByTagName(tagname).item(0);
//        }
//        return(converted);
//    }
//    protected void setNodeValue(String tagName, String value, NodeList nodes) {
//        Node node = getNode(tagName, nodes);
//        if ( node == null )
//            return;
//        
//        // Locate the child text node and change its value
//        NodeList childNodes = node.getChildNodes();
//        for (int y = 0; y < childNodes.getLength(); y++ ) {
//            Node data = childNodes.item(y);
//            if ( data.getNodeType() == Node.TEXT_NODE ) {
//                data.setNodeValue(value);
//                return;
//            }
//        }
//    }
//    
//    protected void addNode(String tagName, String value, Node parent) {
//        Document dom = parent.getOwnerDocument();
//        
//        // Create a new Node with the given tag name
//        Node node = dom.createElement(tagName);
//        
//        // Add the node value as a child text node
//        Text nodeVal = dom.createTextNode(value);
//        Node c = node.appendChild(nodeVal);
//        
//        // Add the new node structure to the parent node
//        parent.appendChild(node);
//    }
//    
//    protected Node getNode(String tagName, NodeList nodes) {
//        for ( int x = 0; x < nodes.getLength(); x++ ) {
//            Node node = nodes.item(x);
//            if (node.getNodeName().equalsIgnoreCase(tagName)) {
//                return node;
//            }
//        }
//        
//        return null;
//    }
//    
//    protected String getNodeValue( Node node ) {
//        NodeList childNodes = node.getChildNodes();
//        for (int x = 0; x < childNodes.getLength(); x++ ) {
//            Node data = childNodes.item(x);
//            if ( data.getNodeType() == Node.TEXT_NODE )
//                return data.getNodeValue();
//        }
//        return "";
//    }
//    
//    protected String getNodeValue(String tagName, NodeList nodes ) {
//        for ( int x = 0; x < nodes.getLength(); x++ ) {
//            Node node = nodes.item(x);
//            if (node.getNodeName().equalsIgnoreCase(tagName)) {
//                NodeList childNodes = node.getChildNodes();
//                for (int y = 0; y < childNodes.getLength(); y++ ) {
//                    Node data = childNodes.item(y);
//                    if ( data.getNodeType() == Node.TEXT_NODE )
//                        return data.getNodeValue();
//                }
//            }
//        }
//        return "";
//    }
//    
//    protected String getNodeAttr(String attrName, Node node ) {
//        NamedNodeMap attrs = node.getAttributes();
//        for (int y = 0; y < attrs.getLength(); y++ ) {
//            Node attr = attrs.item(y);
//            if (attr.getNodeName().equalsIgnoreCase(attrName)) {
//                return attr.getNodeValue();
//            }
//        }
//        return "";
//    }
//    
//    protected String getNodeAttr(String tagName, String attrName, NodeList nodes ) {
//        for ( int x = 0; x < nodes.getLength(); x++ ) {
//            Node node = nodes.item(x);
//            if (node.getNodeName().equalsIgnoreCase(tagName)) {
//                NodeList childNodes = node.getChildNodes();
//                for (int y = 0; y < childNodes.getLength(); y++ ) {
//                    Node data = childNodes.item(y);
//                    if ( data.getNodeType() == Node.ATTRIBUTE_NODE ) {
//                        if ( data.getNodeName().equalsIgnoreCase(attrName) )
//                            return data.getNodeValue();
//                    }
//                }
//            }
//        }
//        return "";
//    }
//}
//
