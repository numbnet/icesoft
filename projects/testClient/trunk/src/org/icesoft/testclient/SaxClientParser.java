package org.icesoft.testclient;

import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.icesoft.testclient.client.ClientDescriptor;

/**
 * @author ICEsoft Technologies, Inc.
 */
public class SaxClientParser implements ContentHandler {


    private enum TOKEN { CLIENT_CLASS, CLIENT_URL, CLIENT_COUNT, CLIENT_INITIAL_DELAY,
                                CLIENT_INTER_DELAY, CLIENT_REPEAT_COUNT, CLIENT_IS_BRANCH, USING_PROXY }

    private TestClientLauncher launcher;

    private  TOKEN tokenInProgress;

    private String className;
    private String testUrl;
    private boolean usingProxy;
    private int clientCount;
    private int initialDelay;
    private int interDelay;
    private int repeatCount;
    private boolean isBranch; 

    private String tempField;

    public SaxClientParser(TestClientLauncher launcher) {
        this.launcher = launcher;
    }



    public void setDocumentLocator (Locator locator) {
    }


   
    public void startDocument ()
            throws SAXException {
    }



    public void endDocument()
            throws SAXException {

            launcher.launch();
    }


    
    public void startPrefixMapping (String prefix, String uri)
            throws SAXException {
        System.out.println("Start prefix mapping: " + prefix);

    }



    public void endPrefixMapping (String prefix)
            throws SAXException {
        System.out.println("End prefix mapping: " + prefix);
    }



    public void startElement (String uri, String localName,
                              String qName, Attributes atts)
            throws SAXException {

        if (localName.equalsIgnoreCase("clientClass") ) {
            tokenInProgress = TOKEN.CLIENT_CLASS;
        } else if (localName.equalsIgnoreCase("testURL") ) {
            tokenInProgress = TOKEN.CLIENT_URL;
        } else if (localName.equalsIgnoreCase("clientCount") ) {
            tokenInProgress = TOKEN.CLIENT_COUNT;
        } else if (localName.equalsIgnoreCase("initialDelay") ) {
            tokenInProgress = TOKEN.CLIENT_INITIAL_DELAY;
        } else if (localName.equalsIgnoreCase("interRequestDelay") ) {
            tokenInProgress = TOKEN.CLIENT_INTER_DELAY;
        } else if (localName.equalsIgnoreCase("repeatCount") ) {
            tokenInProgress = TOKEN.CLIENT_REPEAT_COUNT;
        } else if (localName.equalsIgnoreCase("branch") ) {
            tokenInProgress = TOKEN.CLIENT_IS_BRANCH;
        } else if (localName.equalsIgnoreCase("usingProxy")) {
            tokenInProgress = TOKEN.USING_PROXY;
        } 
    }



    public void endElement (String uri, String localName,
                            String qName)
            throws SAXException {

        if (localName.equalsIgnoreCase("client") ) {
            ClientDescriptor cd = new ClientDescriptor(className,
                                                       testUrl,
                                                       clientCount,
                                                       initialDelay,
                                                       interDelay,
                                                       repeatCount,
                                                       isBranch,
                                                       usingProxy);
            launcher.defineClient( cd );

        } else {

            switch (tokenInProgress) {
                case CLIENT_CLASS:
                    className = tempField;
                    break;

                case CLIENT_URL:
                    testUrl = tempField;
                    break;

                case CLIENT_COUNT:
                    clientCount = parseInt( tempField );
                    break;

                case CLIENT_INITIAL_DELAY:
                    initialDelay = parseInt( tempField );
                    break;

                case CLIENT_INTER_DELAY:
                    interDelay = parseInt( tempField );
                    break;

                case CLIENT_REPEAT_COUNT:
                    repeatCount = parseInt( tempField );
                    break;

                case CLIENT_IS_BRANCH:
                    isBranch = Boolean.parseBoolean ( tempField );
                    break;
                
                case USING_PROXY:
                    usingProxy = Boolean.parseBoolean ( tempField );
                    break; 
            }
        }
    }


    private int parseInt(String val) {

        int temp = 0;
        try {
            temp = Integer.valueOf( val );

        } catch (NumberFormatException nfe) {
            System.out.println("Exception parsing numeric field: " + val);
        }
        return temp;
    }



    public void characters (char ch[], int start, int length)
            throws SAXException{

        tempField = new String( ch, start, length);
    }



    public void ignorableWhitespace (char ch[], int start, int length)
            throws SAXException{
        
    }



    public void processingInstruction (String target, String data)
            throws SAXException {
    }


    
    public void skippedEntity (String name)
            throws SAXException{
        System.out.println("Skipped entity: " + name);

    }
}

