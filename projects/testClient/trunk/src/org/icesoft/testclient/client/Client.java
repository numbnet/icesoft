package org.icesoft.testclient.client;

import org.icesoft.testclient.actor.Actor;

import java.net.URL;
import java.net.URLConnection;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * This Client class is the superclass for Client instances that setup
 * normal test Actor associations that do work. It holds some configuration information,
 * and provides useful utility methods for the associated Actors. Client subclasses are responsible for
 * parsing out the information necessary for continued requests, including <ol>
 * <li> Cookie </li>
 * <li> View number </li>
 * <li> ice.session </li>
 *
 * This class defers to the concrete implementation to set up the Actors.
 * Each instance of a client represents a single session connection to the server.
 * Testing instances that want to share information and behaviour, such as setting
 * up a Ping + receive-updates loop, with partial submits ongoing will need to
 * create a single Client subclass to do so, using prebuilt (Hopefully) Actors
 * to combine together to do the work. <p>
 *
 * Testing instances that want to hit the server with multiple users will
 * want to create N instances of a single Client subclass, and launch them all.  
 *
 * The Client holds the timing parameters for the ActionThread. This works as
 * expected if the Client only has one type of actor, but gets a bit tricky if
 * the Client defines multiple actors. In those cases, the Client can override
 * the default timing settings on the ActionThreads running the separate Actors,
 * either with something custom parsed from a file, or just hardcoded.
 *  
 */
public abstract class Client {

    private static final Logger log = Logger.getLogger(" org.icesoft.testclient.client");


    // Initial URL refers to the initial page, which may not just be the name of the app.
    // eg http://host:port/pathA/pathB/pathC/page1.iface
    protected String initialUrl = "";

    // Shorter URL refers to just the app portion, suitable for adding the
    // /block/ping and /block/receive-updates commands too,  eg.  http://host:port/pathA/
    protected String shorterUrl;

    public Pattern branchViewNumberPattern = Pattern.compile("viewIdentifier=([^;]*);", Pattern.DOTALL);
    public Pattern branchIceIdPattern = Pattern.compile("window.session=\'([^\']*)\';", Pattern.DOTALL);
    public Pattern viewNumberPattern = Pattern.compile("Ice.Community.Application.*view:([^,]*),", Pattern.DOTALL);
    public Pattern iceIdPattern = Pattern.compile("Ice.Community.Application.*session: \'([^,]*)\',", Pattern.DOTALL);

//    public Pattern shortUrlPattern = Pattern.compile("(http://.*:[0-9]*/[^/.]*/)", Pattern.DOTALL);

    protected String initialResponse;
    
    protected    String iceId = "";
    protected    String viewNumber = "";
    protected    String cookie = "";

    protected boolean branch;

    protected int initialRequestDelay;
    protected int repeatCount;
    protected int repeatDelay;
    protected int clientId;

    final int REPORT_INTERVAL = 60000;

    // There is one open, fixed URLConnection class for all POSTS. 
    protected URLConnection urlConnectionForPosts;
    protected OutputStreamWriter outStreamForPosts;
    protected InputStreamReader inStreamForPosts; 

    protected boolean running = true;

    /**
     *
     * @param url URL of page to connect to. 
     * @param initialDelay Delay before first request.
     * @param repeatCount Number of requests to make before quitting
     * @param repeatDelay Delay between each request
     * @param isBranch True if this is to work against 1.6 ICEfaces
     */
    public void init(String url,
                     int initialDelay,
                     int repeatCount,
                     int repeatDelay,
                     boolean isBranch,
                     boolean isUsingProxy,
                     int clientId) {

        try {

            initialRequestDelay = initialDelay;
            this.repeatDelay = repeatDelay;
            this.repeatCount = repeatCount;
            this.clientId = clientId;
            this.branch = isBranch;


            this.initialUrl = url;

            if (isUsingProxy) {
                int spos = url.indexOf("//");
                if (spos > -1) {
                    spos = url.indexOf(":", spos+1);
                    spos++;
                    int epos = url.indexOf("/", spos);
                    if (epos > -1) {

                        String prior = url.substring(0, spos);
                        String posterior = url.substring(epos);
                        int port = Integer.parseInt( url.substring(spos, epos ));
                        this.initialUrl = prior + Integer.toString( port + clientId ) +  posterior;
                    }
                }
            }

            System.out.println("Launching client: " + clientId + " to URL: " + initialUrl);

            if (!this.initialUrl.endsWith("/") ) {
                this.initialUrl += "/";
            } 


            URL firstPageUrl = new URL(url);
            long start = System.currentTimeMillis();
            URLConnection firstPageConnection = firstPageUrl.openConnection();
            InputStream firstPageStream = firstPageConnection.getInputStream();
            cookie = firstPageConnection.getHeaderField("Set-Cookie");
            cookie = cookie.substring(0, cookie.indexOf(" "));
            initialResponse = getInputAsString(
                    new InputStreamReader(firstPageStream) );

            System.out.println("FirstPage Load Time: " + (System.currentTimeMillis() - start) + " ms");
//            System.out.println("Set-cookie: " + cookie);
           
            Matcher viewNumberMatcher;
            if (branch) {
                viewNumberMatcher = branchViewNumberPattern.matcher(initialResponse);
            } else {
                viewNumberMatcher = viewNumberPattern.matcher( initialResponse );
            }
            viewNumberMatcher.find();
            viewNumber = viewNumberMatcher.group(1).trim();

            shorterUrl = extractShorterUrl(initialUrl);
            if (!shorterUrl.endsWith("/")) {
                shorterUrl += "/";
            } 

            Matcher iceIdMatcher;

            if (branch) {
                iceIdMatcher = branchIceIdPattern.matcher(initialResponse);
            } else {
                iceIdMatcher = iceIdPattern.matcher( initialResponse );
            }
            iceIdMatcher.find();
            iceId = iceIdMatcher.group(1);


            System.out.println("ICE.session = " + iceId);

            if (log.isLoggable(Level.FINE)) {
                log.fine(initialResponse);
                log.fine("Cookie: " + cookie);
                log.fine("ViewNumber: " + viewNumber);
                log.fine("ice.session: " + iceId);
            }
            // Let the Client subclass do it's initialization

            log.info(this + " Client: " + clientId + " at Url: " + shorterUrl + " running with: " + repeatCount + " reps - requests every: " + repeatDelay + " ms");
            initSubclient();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Allow the subclass to do its setup
     */
    public abstract void initSubclient();

    
    /**
     * Allow the subclass to terminate its actor threads
     */
    public abstract void terminate();

    
    /**
     * Allow the subclass to retrieve reporting actors
     * @return  A List of Actors that will perform reporting
     */
    public abstract List <Actor> getReportingActors();


    public static String getInputAsString(Reader in) {
        char[] buf = new char[1024];
        StringWriter out = new StringWriter();

        try {
            int l = 1;
            while (l > 0) {
                l = in.read(buf);
                if (l > 0) {
                    out.write(buf, 0, l);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toString();

    }


    /**
     * Send a message to the server, and get a response.
     *
     * @param uri URI of the entire resource. Server URL + path
     * @param data Request parameter string
     * @return Result from the server
     *
     * @throws IOException if server responds with server errors, various network
     * problems, etc.
     */
    public String post(String uri, String data) throws IOException {
//        System.out.println("POST to:" + uri + ", data:[" + data+ "]");

        String result;

        URL urlURL = new URL(uri);
        
        URLConnection urlConnection = urlURL.openConnection();
        urlConnection.setDoOutput(true);
        urlConnection.setRequestProperty("Cookie", cookie);
        OutputStreamWriter urlWriter = new OutputStreamWriter(
                urlConnection.getOutputStream() );
//
//        try {
//            if (urlConnectionForPosts == null) {
//
//                urlConnectionForPosts = urlURL.openConnection();
//                urlConnectionForPosts.setDoOutput(true);
//                urlConnectionForPosts.setRequestProperty("Cookie", cookie);
//                outStreamForPosts = new OutputStreamWriter(
//                        urlConnectionForPosts.getOutputStream() );
//                inStreamForPosts = new InputStreamReader( urlConnectionForPosts.getInputStream() );
//
//            }
//        } catch (Exception e) {
//            System.out.println("Exception caught opening streams: " + e);
//        }


//        outStreamForPosts.write( data );
//        outStreamForPosts.flush();

//         result = getInputAsString( inStreamForPosts );


        urlWriter.write(data);
        urlWriter.flush();
        urlWriter.close();
//
        // URLConnection throws IOException if the response is in error
        result = getInputAsString( new InputStreamReader(urlConnection.getInputStream()) );
        return result;
    }

    private String extractShorterUrl(String initialUrl) {

        int epos = initialUrl.indexOf(":");
        if (epos > -1) {
            epos = initialUrl.indexOf(":", epos+1);
            epos = initialUrl.indexOf("/", epos+1);
            if (epos > -1) {
                epos = initialUrl.indexOf("/", epos+1);

                if (epos == -1) {
                    return initialUrl;
                } else {
                    return initialUrl.substring( 0, epos);
                }
            }
        }
        throw new IllegalArgumentException ("Illegal URL Format for this test program, missing parts"); 
    }


    public String getInitialUrl() {
        return initialUrl;
    }

    public String getIceId() {
        return iceId;
    }

    public String getCookie() {
        return cookie;
    }

    public boolean isBranch() {
        return branch;
    }

    public String getViewNumber() {
        return viewNumber;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public int getRepeatCount() {
        return repeatCount;
    } 

    public String toString() {
        return getClass().getName();
    }


    public int getInitialRequestDelay() {
        return initialRequestDelay;
    }

    public int getRepeatDelay() {
        return repeatDelay;
    }

    public String getInitialResponse() {
        return initialResponse;
    }

    public void setViewNumber(String viewNumber) {
        this.viewNumber = viewNumber;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public void setIceId(String iceId) {
        this.iceId = iceId;
    }

    public void setInitialUrl(String url) {
        this.initialUrl = url;
    }

    /**
     * Get the shorter form of the page URL, consisting of http://host:port/app/
     * suitable for appending the blocking commands to. This method should be
     * used by all actors wishing to construct commands for the server from
     * the intial page URL during testing.  
     *
     * @return The shorter form URL.
     */
    public String getShorterUrl() {
        return shorterUrl;
    }

    public void setShorterUrl(String shorterUrl) {
        this.shorterUrl = shorterUrl;
    }
}
