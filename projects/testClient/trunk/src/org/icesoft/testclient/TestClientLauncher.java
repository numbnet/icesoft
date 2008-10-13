package org.icesoft.testclient;

import org.icesoft.testclient.client.Client;
import org.icesoft.testclient.client.ClientDescriptor;
import org.xml.sax.XMLReader;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLReaderFactory;

import java.util.List;
import java.util.ArrayList;

/**
 * @author ICEsoft Technologies, Inc.
 */
public class TestClientLauncher {

    public static void main(String[] args)  {

        for (int idx = 0; idx < args.length; idx ++ ) {
            if ((args[0].startsWith("-h") ||
                 args[0].startsWith("?")) ) {
                usage();
                return;
            }
        } 

        TestClientLauncher tcl = new TestClientLauncher();
        tcl.setup(args);

    }


    private static void usage() {

        System.out.println("java TestClientLauncher [URL] | " +
                           "                        [-f 'config file'] " +
                           "                        [-c 'override ClientCount'] " +
                           "                        [-u 'override URL'] " +
                           "                        [-d 'override request delay'] " +
                           "                        [-r 'override repeatCount'] " +
                           "                        [-s 'interclient start delay']" );
        
        System.out.println("Where 'file' is a test configuration file");
        System.out.println("Default implementation just runs a ping suite against 'URL'");

    }


    private int overrideClientCount;
    private String overrideURL;
    private int interClientStartDelay = 400;
    private int overrideInterRequestDelay;
    private int overrideRepeatCount;

    private int sumOfClients;


    private List <ClientDescriptor> clientList = new ArrayList<ClientDescriptor>();


    /**
     * Manually define a test environment
     * @param args
     */
    public void setup(String[] args) {

        String configFile = null;
        for (int idx = 0; idx < args.length; idx ++) {
            if (args[idx].indexOf("-f") > -1) {
                configFile = args[idx+1];
            }  else if (args[idx].indexOf("-c") > -1) {
                try {
                    overrideClientCount = Integer.parseInt(args[idx+1]);
                    System.out.println("Client count override: " + overrideClientCount);

                } catch(Exception e) {
                    System.out.println("Exception defining OVerride User count: " + e);
                } 
            } else if (args[idx].indexOf("-u") > -1) {
                if (args.length > idx+1) {
                    overrideURL = args[idx+1];
                    System.out.println("URL override: " + overrideURL);
                }
            }  else if (args[idx].indexOf("-s") > -1) {
                try {
                    interClientStartDelay = Integer.parseInt(args[idx+1]);
                    System.out.println("Interclient Start delay override: " + interClientStartDelay);
                } catch(Exception e) {
                    System.out.println("Exception defining OVerride User count: " + e);
                }
            }  else if (args[idx].indexOf("-d") > -1) {
                try {
                    overrideInterRequestDelay = Integer.parseInt(args[idx+1]);
                    System.out.println("Interrequest delay override: " + overrideInterRequestDelay);
                } catch(Exception e) {
                    System.out.println("Exception defining Override Inter-Request delay: " + e);
                }
            } else if (args[idx].indexOf("-r") > -1) {
                try {
                    overrideRepeatCount = Integer.parseInt(args[idx+1]);
                    System.out.println("Repeat count override: " + overrideRepeatCount);
                } catch(Exception e) {
                    System.out.println("Exception defining Override repeat count: " + e);
                }
            }

        }

        if (configFile != null) {
            loadTestFromXML(configFile);
        } else {

            loadSimpleTest(args[0]);
        }

         try {
            synchronized(Thread.currentThread() )  {
                Thread.currentThread().wait();
            }
        } catch (InterruptedException ie) { }
    }


    /**
     * Allow external operator to define clients via a client descriptor
     *
     * @param descriptor Descriptor of client class and behaviour
     */
    public void defineClient( ClientDescriptor descriptor ) {
        clientList.add( descriptor );
    }



    /**
     * Launch the clients
     */
    public void launch() {

        if (clientList.isEmpty() ) {
            System.out.println("No Test Clients defined, quitting... ");
            return;
        }

        ReportingThread reporter = new ReportingThread( 60000 );
        for (ClientDescriptor c : clientList) {

            try {

                int clientCount = (overrideClientCount == 0) ? c.getClientCount() : overrideClientCount;
                System.out.println("Launching: " + clientCount + " Clients ");
                for (int idx = 0; idx < clientCount; idx ++ ) {

                    long now = System.currentTimeMillis();
                    Client client = (Client) Class.forName( c.getClientClass() ).newInstance();
                    client.init( (overrideURL == null) ? c.getUrl() : overrideURL,
                                 c.getInitialRequestDelay(), // this is obsolete now, use clientStartDelay
                                 (overrideRepeatCount == 0) ? c.getRequestCount() : overrideRepeatCount,
                                 (overrideInterRequestDelay == 0) ? c.getInterRequestDelay(): overrideInterRequestDelay,
                                 c.isBranch(),
                                 idx);
                    reporter.addClient( client );
                    try {
                        long time = System.currentTimeMillis() - now;
                        if (time < interClientStartDelay) {
                            now = interClientStartDelay - time;
                            Thread.sleep(now);
                        } 
                    } catch (InterruptedException ie) { }
                }


            } catch (Exception e) {
                System.out.println("Exception launching client: " + e);
            }
        }

        reporter.start();

    }


    /**
     * Parse an xml file to obtain the Client definition, and run it.
     * ContentHandler will perform the ClientDescriptor building, and then launch the
     * test suite if any clients were found 
     */
    private void loadTestFromXML(String filename) {

        ContentHandler myHandler = new SaxClientParser( this );

        try {
            XMLReader parser = XMLReaderFactory.createXMLReader();
            parser.setContentHandler( myHandler );
            parser.parse( filename );

            for (ClientDescriptor c: clientList) {
                sumOfClients += c.getClientCount();
            }

        } catch (SAXException se) {
            System.out.println("SAX Exception parsing: " + filename);

        } catch (Exception e) {
            System.out.println("Exception doing parsing: " + e);
        }
    }



    private void loadSimpleTest(String URL) {

        // I'll need to improve initial response parsing. I'll need
        // more information than just branch or not, since we'll likely
        // keep changing them.

//        ClientDescriptor pingClient = new ClientDescriptor( "org.icesoft.testclient.client.StandalonePingReceiveUpdatesClient",
//                                                            "http://10.18.40.11:8080/syncTest",
//                                                            1,       // 1 client
//                                                            10000,    // 1 sec initial delay
//                                                            20000,   // 20 seconds between each
//                                                            10000,   // Do 10000 of them
//                                                            branch); // passed. Can be Head or branch
//
//          ClientDescriptor srClient = new ClientDescriptor( "org.icesoft.testclient.client.SyncTestPostbackClient",
//                                                            "http://10.18.40.11:8080/syncTest",
//                                                            4,
//                                                            10000,
//                                                            400,
//                                                            40000,
//                                                            branch);

        ClientDescriptor pingClient = new ClientDescriptor( "org.icesoft.testclient.client.StandalonePingReceiveUpdatesClient",
                                                            URL,
                                                            1,       // 1 client
                                                            10000,    // 1 sec initial delay
                                                            20000,   // 20 seconds between each
                                                            10000,   // Do 10000 of them
                                                            false); // passed. Can be Head or branch

        ClientDescriptor srClient = new ClientDescriptor( "org.icesoft.testclient.client.SyncTestPostbackClient",
                                                          URL,
                                                          2,
                                                          10000,
                                                          400,
                                                          40000,
                                                          false);

        defineClient(pingClient);
        defineClient(srClient);
        launch();
    }
}
