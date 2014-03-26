package com.droid.horoscope.net;

import com.droid.horoscope.model.Horoscopes;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by james on 26/03/14.
 */
public class XMLParser {

    public static final String RESPONSE_DATA = "responseData";
    public static final String RESPONSE_STATUS = "responseStatus";
    public static final String RESPONSE_DETAILS = "responseDetails";

    public static final String ITEM = "item";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String LINK = "link";

    private String xmlStr;
    private ArrayList<Horoscopes> horoscopesDetails;

    public XMLParser(String xmlStr){
        this.xmlStr = xmlStr;
    }

    public Document getDomElements() throws SAXException, ParserConfigurationException, IOException{
        Document document = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xmlStr));
        document = db.parse(is);
        return document;
    }

    //Get each xml child element value by passing element node name
    public String getValue(Element item, String tagName){
        NodeList nodeList = item.getElementsByTagName(tagName);
        return this.getElementValue(nodeList.item(0));
    }

    public final String getElementValue(Node node){
        Node child;
        if(node != null){
            if(node.hasChildNodes()){
                for(child=node.getFirstChild(); child != null; child.getNextSibling()){
                    if(child.getNodeType() == Node.TEXT_NODE){
                        return child.getNodeValue();
                    }
                }
            }
        }
        return null;
    }
}
