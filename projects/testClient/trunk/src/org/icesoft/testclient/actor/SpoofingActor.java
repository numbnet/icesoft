package org.icesoft.testclient.actor;

import org.icesoft.testclient.client.Client;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.net.URLEncoder;
import java.net.URL;
import java.net.URLConnection;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;

/**
 * Use the spoofing actor to fake JSessionID values and ice.session values to secretly
 * pretend to be someone else! Amazing. Needs to be compiled with the new values for
 * those parameters once they are discovered (and before they time out, naturlich) 
 */
public class SpoofingActor extends ActorBase {

    protected static final Logger log = Logger.getLogger("org.icesoft.testclient.actor");



    // overriding paramters
    protected String JSessionId = "26072DB87331784E857D23E2F4AAFFB5";
    protected String iceSessionId = "K983ePaTw2fMFztf7lcEog";
    protected String iceViewId = "1";  



    public void act(Client controller) {

        try {

            // Overall time for these requests is somewhat meaningless,
            // since this post is blocking and waiting for something to happen.

            tempTime = System.currentTimeMillis();

            String command =  "block/dispose-views";

            String updates;
            if (JSessionId != null) {
                updates = this.post(controller.getShorterUrl() + command,
                                    getData(controller) + "\n");
            } else {
                updates = controller.post(controller.getShorterUrl() + command,
                                          getData(controller) + "\n");
            }

            System.out.println(updates);


            if (log.isLoggable(Level.FINE )) {
                log.fine ( updates );
            }

        } catch (Exception e) {
            log.throwing(this.getClass().getName(), "act, receive-updated-views",  e);

            errorCountThisInterval++;
        }
    }

    /**
     * Use the URL from the controller, but override everything else. 
     * @param controller  The running Client
     * @return the command to submit.  
     */
     public String getData(Client controller)  {
        StringBuffer data = new StringBuffer();
        try {
            data.append( URLEncoder.encode("ice.session", "UTF-8")).append("=");
            data.append( URLEncoder.encode(iceSessionId, "UTF-8"));

            data.append( URLEncoder.encode("ice.view", "UTF-8")).append("=");
            data.append( URLEncoder.encode(iceViewId, "UTF-8"));

            data.append("&").append( URLEncoder.encode("rand", "UTF-8")).append("=");
            data.append( URLEncoder.encode(String.valueOf(Math.random()), "UTF-8" ));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data.toString();
    }

    /**
     * A local implementation of post, used to pass in fudged JSessionId's if
     * desired. 
     * @param uri
     * @param data
     * @return
     * @throws IOException
     */
    public String post(String uri, String data) throws IOException {
//        System.out.println("POST to:" + uri + ", data:[" + data+ "]");

            String result;
            URL urlURL = new URL(uri);
            URLConnection urlConnection = urlURL.openConnection();
            urlConnection.setDoOutput(true);

        // Use the overriding value

            urlConnection.setRequestProperty("Cookie", JSessionId);
            OutputStreamWriter urlWriter = new OutputStreamWriter(
                    urlConnection.getOutputStream() );
            urlWriter.write(data);
            urlWriter.flush();
            urlWriter.close();

            // URLConnection throws IOException if the response is in error
            result = Client.getInputAsString(
                    new InputStreamReader(urlConnection.getInputStream()) );
            return result;
        }


}
