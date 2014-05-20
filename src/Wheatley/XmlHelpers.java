/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;


import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;



/**
 *
 * @author Steve-O
 * http://www.drdobbs.com/jvm/creating-and-modifying-xml-in-java/240150782
 * http://www.drdobbs.com/jvm/easy-dom-parsing-in-java/231002580
 */
public class XmlHelpers {
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
