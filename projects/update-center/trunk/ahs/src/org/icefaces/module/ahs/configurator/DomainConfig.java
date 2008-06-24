package org.icefaces.module.ahs.configurator;

import com.sun.appserv.addons.AddonException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents the domain.xml file that we need to modify for AHS to be operational.  Basically we need
 * to add 2 JMS topics, a ConnectionFactory (if one does not yet exist), a connection pool (if one does not exist),
 * and Comet support on the HTTP listener on port 8080 (if it's not already active).
 * <p/>
 * There are a number of things to do to improve this:
 * <p/>
 * - make some of the settings configurable via properties rather than hard-coded
 * - use XPath rather than straight DOM functions for locating elements
 * - general robustness improvements
 * - better encapsulation of data structures (e.g. classes for configuration nodes)
 */
public class DomainConfig {

    private static Logger log = Logger.getLogger(DomainConfig.class.getPackage().getName());

    public static final String[] TOPICS = {"icefacesContextEventTopic", "icefacesResponseTopic"};

    private File domainConfigFile;
    private Document document;

    public DomainConfig(File domainConfigFile) throws AddonException {
        this.domainConfigFile = domainConfigFile;
        try {
            DocumentBuilderFactory builderFactory =
                    DocumentBuilderFactory.newInstance();
            builderFactory.setValidating(false);
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            document = builder.parse(domainConfigFile);
        } catch (ParserConfigurationException e) {
            String msg = "can not parse domain.xml";
            log.log(Level.SEVERE, msg, e);
            throw new AddonException(msg, e);
        } catch (SAXException e) {
            String msg = "can not parse domain.xml";
            log.log(Level.SEVERE, msg, e);
            throw new AddonException(msg, e);
        } catch (IOException e) {
            String msg = "can not parse domain.xml";
            log.log(Level.SEVERE, msg, e);
            throw new AddonException(msg, e);
        }
    }


    public Document getDocument() {
        return this.document;
    }


    public void addCometSetting() throws AddonException {

        //TODO:  This is a bit fragile as the id of the listener could change or perhaps comet support
        //should be added to a different (or more than one) http-listener.  Should update this to be
        //more robust and flexible (e.g. accept configurable properties).

        Element listenerElement = findElementWithAttribute("http-listener", "port", "8080");
        if (listenerElement == null) {
            String msg = "could not find " + "http-listener" + " with " + "port" + " = " + "8080";
            log.log(Level.WARNING, msg);
            throw new AddonException(msg);
        }

        Element cometElement = findElementWithAttribute(listenerElement, "property", "name", "cometSupport");
        if (cometElement == null) {
            Element cometProperty = document.createElement("property");
            cometProperty.setAttribute("name", "cometSupport");
            cometProperty.setAttribute("value", "true");
            listenerElement.appendChild(cometProperty);
            log.log(Level.FINEST, "comet support added");
        } else {
            String cometSupportValue = cometElement.getAttribute("value");
            if (cometSupportValue == null || !cometSupportValue.equalsIgnoreCase("true")) {
                cometElement.setAttribute("value", "true");
                log.log(Level.FINEST, "comet support turned on");
            }
        }
    }


    public void addJMSTopics() throws AddonException {

        //Add topics and connection entries to <resources> section as necessary
        Element resources = getSingleElement("resources");
        for (String theTopic : TOPICS) {
            addElementIfNecessary(resources, "admin-object-resource", "jndi-name", theTopic, createTopicResource(theTopic));
        }
        addElementIfNecessary(resources, "connector-resource", "jndi-name", "ConnectionFactory", createConnectorResource());
        addElementIfNecessary(resources, "connector-connection-pool", "name", "ConnectionFactory", createConnectorConnectionPool());

        //Add topics and connection entries to <server> section as necessary.  The assumption here is that there
        //is a single <servers> section with a single <server> child.
        Element servers = getSingleElement("servers");
        Element server = getSingleElement(servers, "server");
        for (String theTopic : TOPICS) {
            addElementIfNecessary(server, "resource-ref", "ref", theTopic, createResourceRef(theTopic));
        }
        addElementIfNecessary(server, "resource-ref", "ref", "ConnectionFactory", createResourceRef("ConnectionFactory"));
    }


    public void removeJMSTopics() throws AddonException {

        //We only remove the entries related to the ICEfaces specific topics, not the ConnectionFactory or the
        //connection pool as they may be used by other, non-ICEfaces, JMS topics.

        //Remove topics from <resources> section if possible
        Element resources = getSingleElement("resources");
        for (String theTopic : TOPICS) {
            removeElementIfPossible(resources, "admin-object-resource", "jndi-name", theTopic);
        }

        //Remove topics from <server> section if possible
        Element servers = getSingleElement("servers");
        Element server = getSingleElement(servers, "server");
        for (String theTopic : TOPICS) {
            removeElementIfPossible(server, "resource-ref", "ref", theTopic);
        }

    }

    private Element findElementWithAttribute(String searchTagName, String searchAttributeName, String searchAttributeValue) {
        return findElementWithAttribute(document.getDocumentElement(), searchTagName, searchAttributeName, searchAttributeValue);
    }

    private Element findElementWithAttribute(Element parent, String searchTagName, String searchAttributeName, String searchAttributeValue) {

        NodeList elementList = parent.getElementsByTagName(searchTagName);
        for (int index = 0; index < elementList.getLength(); index++) {
            Element result = (Element) elementList.item(index);
            String attr = result.getAttribute(searchAttributeName);
            if (attr != null && attr.equalsIgnoreCase(searchAttributeValue)) {
                log.log(Level.FINEST, "found <" + searchTagName + "> that has attribute '" + searchAttributeName + "'");
                return result;
            }
        }
        log.log(Level.FINEST, "could not find <" + searchTagName + "> that has attribute '" + searchAttributeName + "'");
        return null;
    }

    private Element getSingleElement(Element parent, String tagName) throws AddonException {
        NodeList resources = parent.getElementsByTagName(tagName);
        if (resources.getLength() != 1) {
            String msg = "could not find a single node for " + tagName;
            log.log(Level.WARNING, msg);
            throw new AddonException(msg);
        }

        return (Element) resources.item(0);
    }

    private Element getSingleElement(String tagName) throws AddonException {
        return getSingleElement(document.getDocumentElement(), tagName);
    }

    private void addElementIfNecessary(Element parent, String tagName, String attrName, String attrValue, Element child) {
        String msg = "added";
        Element el = findElementWithAttribute(parent, tagName, attrName, attrValue);
        if (el == null) {
            parent.appendChild(child);
        } else {
            msg = "did not add (already exists)";
        }
        log.log(Level.FINEST, msg +
                "\n  parent         : " + parent.getTagName() +
                "\n  child          : " + child.getTagName() +
                "\n  tag            : " + tagName +
                "\n  attribute name : " + attrName +
                "\n  attribute value: " + attrValue
        );

    }

    private void removeElementIfPossible(Element parent, String tagName, String attrName, String attrValue) {
        String msg = "removed";
        Element child = findElementWithAttribute(parent, tagName, attrName, attrValue);
        if (child != null) {
            parent.removeChild(child);
        } else {
            msg = "did not remove (already gone)";
        }
        log.log(Level.FINEST, msg +
                "\n  parent         : " + parent.getTagName() +
                "\n  child          : " + child +
                "\n  tag            : " + tagName +
                "\n  attribute name : " + attrName +
                "\n  attribute value: " + attrValue
        );

    }


    private Element createResourceRef(String ref) {
        Element resourceRef = document.createElement("resource-ref");
        resourceRef.setAttribute("enabled", "true");
        resourceRef.setAttribute("ref", ref);
        return resourceRef;
    }


    private Element createConnectorConnectionPool() {
        Element pool = document.createElement("connector-connection-pool");
        pool.setAttribute("associate-with-thread", "false");
        pool.setAttribute("connection-creation-retry-attempts", "0");
        pool.setAttribute("connection-creation-retry-interval-in-seconds", "10");
        pool.setAttribute("connection-definition-name", "javax.jms.ConnectionFactory");
        pool.setAttribute("connection-leak-reclaim", "false");
        pool.setAttribute("connection-leak-timeout-in-seconds", "0");
        pool.setAttribute("fail-all-connections", "false");
        pool.setAttribute("idle-timeout-in-seconds", "300");
        pool.setAttribute("is-connection-validation-required", "false");
        pool.setAttribute("lazy-connection-association", "false");
        pool.setAttribute("lazy-connection-enlistment", "false");
        pool.setAttribute("match-connections", "true");
        pool.setAttribute("max-connection-usage-count", "0");
        pool.setAttribute("max-pool-size", "32");
        pool.setAttribute("max-wait-time-in-millis", "60000");
        pool.setAttribute("name", "ConnectionFactory");
        pool.setAttribute("pool-resize-quantity", "2");
        pool.setAttribute("resource-adapter-name", "jmsra");
        pool.setAttribute("steady-pool-size", "8");
        pool.setAttribute("validate-atmost-once-period-in-seconds", "0");

        return pool;
    }

    private Element createConnectorResource() {

        Element connectionResource = document.createElement("connector-resource");
        connectionResource.setAttribute("enabled", "true");
        connectionResource.setAttribute("jndi-name", "ConnectionFactory");
        connectionResource.setAttribute("object-type", "user");
        connectionResource.setAttribute("pool-name", "ConnectionFactory");

        return connectionResource;
    }

    private Element createTopicResource(String topic) {

        Element adminObjectResource = document.createElement("admin-object-resource");
        adminObjectResource.setAttribute("enabled", "true");
        adminObjectResource.setAttribute("jndi-name", topic);
        adminObjectResource.setAttribute("object-type", "user");
        adminObjectResource.setAttribute("res-adapter", "jmsra");
        adminObjectResource.setAttribute("res-type", "javax.jms.Topic");

        Element prop = document.createElement("property");
        prop.setAttribute("name", "Name");
        prop.setAttribute("value", topic);

        adminObjectResource.appendChild(prop);

        return adminObjectResource;
    }

    public void save() throws FileNotFoundException, TransformerException {
        if (document == null) {
            //Nothing to save
            return;
        }
        Source input = new DOMSource(document);
        Result output = new StreamResult(new BufferedOutputStream(new FileOutputStream(domainConfigFile)));

        TransformerFactory xfactory = TransformerFactory.newInstance();
        Transformer transformer = xfactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,
                document.getDoctype().getPublicId());
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
                document.getDoctype().getSystemId());
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.transform(input, output);
    }

}
