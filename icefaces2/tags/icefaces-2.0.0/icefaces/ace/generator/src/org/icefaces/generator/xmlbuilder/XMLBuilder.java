package org.icefaces.generator.xmlbuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;

import org.icefaces.generator.utils.FileWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;


public abstract class XMLBuilder {
    private Document document;
    private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    private String fileName;
    Properties properties = new Properties();
    public XMLBuilder(String fileName) {
        this.fileName = fileName;
        properties.put(OutputKeys.INDENT, "yes");          
        try {
            document = factory.newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
    
    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public void write() {
        FileWriter.writeXML(getDocument(), getFileName(), getProperties());
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
    
    public Element addNode(Element parent, String name, String value) {
        Element node = getDocument().createElement(name);           
        Text node_text = getDocument().createTextNode(value);
        node.appendChild(node_text);
        parent.appendChild(node); 
        return node;
    }
}
