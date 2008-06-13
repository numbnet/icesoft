package org.icefaces.module.ahs.configurator;

import com.sun.appserv.addons.AddonException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class DomainConfig {

    public static final String LISTENER_TAG = "http-listener";
    public static final String LISTENER_ATTR_NAME = "port";
    public static final String LISTENER_ATTR_VALUE = "8080";

    public static final String PROPERTY_TAG = "property";
    public static final String COMET_NAME = "cometSupport";
    public static final String COMET_VALUE = "true";

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
            throw new AddonException("can not parse domain.xml: " + e, e);
        } catch (SAXException e) {
            throw new AddonException("can not parse domain.xml: " + e, e);
        } catch (IOException e) {
            throw new AddonException("can not parse domain.xml: " + e, e);
        }
    }

    public Document getDocument() {
        return this.document;
    }

    public void addCometSetting() throws AddonException {

        //Not sure why this doesn't work.  Should probably start using XPath
//        Element listenerElement = doc.getElementById(listenerId);

        Element listenerElement = getListenerElement(LISTENER_ATTR_VALUE);
        if (listenerElement == null) {
            throw new AddonException("could not find " + LISTENER_TAG + " with " +
                    LISTENER_ATTR_NAME + " = " + LISTENER_ATTR_VALUE);
        }

        if (!isCometEnabled(listenerElement)) {
            Element cometProperty = document.createElement(PROPERTY_TAG);
            cometProperty.setAttribute("name", COMET_NAME);
            cometProperty.setAttribute("value", COMET_VALUE);
            listenerElement.appendChild(cometProperty);
        }
    }

    public Element getListenerElement(String listenerPort) {

        //TODO:  This is fragile as the id of the listener could change or perhaps comet support
        //should be added to a different (or more than one) http-listener.  Should update this to be
        //more robust and flexible (e.g. accept configurable properties).
        NodeList listenerList = document.getElementsByTagName(LISTENER_TAG);
        Element listenerElement = null;
        for (int index = 0; index < listenerList.getLength(); index++) {
            Element listener = (Element) listenerList.item(index);
            String port = listener.getAttribute(LISTENER_ATTR_NAME);
            if (port != null && port.equalsIgnoreCase(listenerPort)) {
                listenerElement = listener;
                break;
            }
        }
        return listenerElement;
    }

    private boolean isCometEnabled(Element listenerElement) {
        NodeList listenerKids = listenerElement.getElementsByTagName(PROPERTY_TAG);
        for (int index = 0; index < listenerKids.getLength(); index++) {
            Node kidNode = listenerKids.item(index);
            Element kid = (Element) listenerKids.item(index);
            if (kid.getTagName().equalsIgnoreCase(PROPERTY_TAG)) {
                String attrName = kid.getAttribute("name");
                if (attrName != null && attrName.equalsIgnoreCase(COMET_NAME)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void addJMSTopics() throws AddonException {

        //We are make the assumption that if we can find a single valid topic entry, then the whole configuration is
        //valid.  This might need to change in the future so that we check all the required entries.
        if (areTopicsEnabled()) {
            return;
        }

        //If there are no valid topics, we add all the necessary configuration elements.

        //Get the resources Node (there should be just one)
        NodeList resources = document.getElementsByTagName("resources");
        if (resources.getLength() != 1) {
            throw new AddonException("too many resources nodes");
        }

        Element elemResources = (Element) resources.item(0);
        for (String theTopic : TOPICS) {
            elemResources.appendChild(createTopicResource(theTopic));
        }

        elemResources.appendChild(createConnectorResource());
        elemResources.appendChild(createConnectorConnectionPool());

        NodeList servers = document.getElementsByTagName("server");
        if (servers.getLength() != 1) {
            throw new AddonException("too many server nodes");
        }

        Element server = (Element) servers.item(0);
        server.appendChild(createResourceRef("ConnectionFactory"));
        for (String theTopic : TOPICS) {
            server.appendChild(createResourceRef(theTopic));
        }
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

        Element prop = document.createElement(PROPERTY_TAG);
        prop.setAttribute("name", "Name");
        prop.setAttribute("value", topic);

        adminObjectResource.appendChild(prop);

        return adminObjectResource;
    }

    public boolean areTopicsEnabled() {
        //If any admin-object-resource is a valid ICEfaces AHS topic, then we assume
        //the whole configuration is valid.
        NodeList items = document.getElementsByTagName("admin-object-resource");

        for (int index = 0; index < items.getLength(); index++) {
            Element elem = (Element) items.item(index);
            String attrJNDIName = elem.getAttribute("jndi-name");
            if (attrJNDIName != null && isICEfacesTopic(attrJNDIName)) {
                return true;
            }
        }

        return false;
    }

    public boolean isICEfacesTopic(String topicName) {
        if (topicName == null) {
            return false;
        }
        for (String topic : TOPICS) {
            if (topicName.equals(topic)) {
                return true;
            }
        }
        return false;
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
