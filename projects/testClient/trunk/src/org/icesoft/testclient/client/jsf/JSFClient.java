package org.icesoft.testclient.client.jsf;


import org.icesoft.testclient.client.Client;
import org.icesoft.testclient.actor.Actor;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.net.URL;
import java.net.URLConnection;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * The JSFClient holds some configuration information, and provides useful
 * utility methods for Actors wishing to interact with a JSF only application.
 * This class makes the initial request
 * to the server, and captures the <code>JSESSIONID</code> cookie, as well as
 * the JSF1.2 name prefaced formId, mapId field for use by the actors later.
 *
 *
 */
public abstract class JSFClient extends Client {

   private static final Logger log = Logger.getLogger("org.icesoft.testclient.client.jsf");


    protected Pattern formIdPattern = Pattern.compile("<form id=\"([^\"]*)" , Pattern.DOTALL);
    protected Pattern mapIdPattern =
            Pattern.compile("<input id=\"([^\"]*:map)" , Pattern.DOTALL);
    protected Pattern viewStatePattern =
            Pattern.compile("<input type=\"hidden\" name=\"javax.faces.ViewState\" id=\"javax.faces.ViewState\" value=\"([^\"]*)" , Pattern.DOTALL);

    

    // Initial fields. 
    protected String formId;
    protected String mapId;
    protected String viewState;
    /**
     * Override the init method to parse out the JSF information in the first
     * request, namely, the JSESSIONID
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
                     int clientId) {

        String firstPage = null;
        String field = null;

        try {

            initialRequestDelay = initialDelay;
            this.repeatDelay = repeatDelay;
            this.repeatCount = repeatCount;
            this.clientId = clientId;
            this.url = url;
            
//            this.branch = isBranch;

            if (!this.url.endsWith("/") ) {
                this.url += "/";
            }


            URL firstPageURL = new URL(url);
            
            URLConnection firstPageConnection = firstPageURL.openConnection();
            InputStream firstPageStream = firstPageConnection.getInputStream();


            firstPage = getInputAsString(
                    new InputStreamReader(firstPageStream) );
            cookie = firstPageConnection.getHeaderField("Set-Cookie");

            StringTokenizer st = new StringTokenizer(cookie, ";");
            while (st.hasMoreTokens() ) {
                String temp = st.nextToken();
                if (temp.indexOf("JSESSIONID") > -1) {
                    cookie = temp.substring(temp.indexOf("=")+1 );
                }
            } 

//            System.out.println("First page: " + firstPage);
//            System.out.println("Cookie_______________________: " + cookie);
//            cookie = cookie.substring(0, cookie.indexOf("\""));


            field = "form Id";
            Matcher formIdMatcher;
                formIdMatcher = formIdPattern.matcher(firstPage);

            field = "mapId";
            Matcher mapIdMatcher;
                mapIdMatcher = mapIdPattern.matcher( firstPage );

            field = "viewState";
            Matcher viewStateMatcher;
                viewStateMatcher = viewStatePattern.matcher( firstPage ); 


            formIdMatcher.find();
            formId = formIdMatcher.group(1).trim();


            mapIdMatcher.find();
            mapId = mapIdMatcher.group(1).trim();

            viewStateMatcher.find();
            viewState = viewStateMatcher.group(1).trim();


            System.out.println("Form = " + formId);
            System.out.println("mapid = " + mapId);
            System.out.println("viewState = " + viewState + "\n\n");

            // Let the JSFClient subclass do it's initialization
            log.info(this + " Running with: " + repeatCount + " reps - requests every: " + repeatDelay + " ms");
            initSubclient();

        } catch (IllegalStateException ise) {
            System.out.println("Could not locate field: " + field + ", Page == " + firstPage);
            

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Send a message to the server, and get a response. We must override this
     * for JSF since we don't include the cookie request header. I guess I could
     * leave it as is and just fill it in with nothing, etc.
     * 
     *
     * @param uri URI of the entire resource. Server URL + path
     * @param data Request parameter string
     * @return Result from the server
     *
     * @throws java.io.IOException if server responds with server errors, various network
     * problems, etc.
     */
    public String post(String uri, String data) throws IOException {
//        System.out.println("POST " + uri + " " + data);

        String result;
        URL urlURL = new URL(uri);
        URLConnection urlConnection = urlURL.openConnection();

        // Ah, the simple act of writing data causes the protocol to use POST.
        // A GET request will only open the connection to a URL, and read. Any
        // data transfered in the GET is done through the URL, and isn't considered
        // to be data. 
        urlConnection.setDoOutput(true);
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        urlConnection.setRequestProperty("Content-Length", String.valueOf( data.length()+2 ) );
        urlConnection.setRequestProperty("Cookie", "JSESSIONID=" + cookie);
        OutputStreamWriter urlWriter = new OutputStreamWriter(
                urlConnection.getOutputStream() );
        urlWriter.write(data);
        urlWriter.flush();
        urlWriter.close();
        

        // URLConnection throws IOException if the response is in error
        result = getInputAsString(
                new InputStreamReader(urlConnection.getInputStream()) );
        return result;
    }

    // proprietary getters

    public String getFormId() {
        return formId;
    }

    public String getMapId() {
        return mapId;
    }

    public String getViewState() {
        return viewState;
    }

    

}
