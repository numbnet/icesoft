package org.icesoft.testclient.actor;

import org.icesoft.testclient.client.Client;

import java.net.URLEncoder;
import java.util.logging.Logger;

/**
 * This Actor generates a Tab Event between certain textfields. Useful, really, only
 * for the syncTest application that was looking for deadlocks. 
 */
public class SyncTestUpdatesActor extends ActorBase {

    private static final Logger log = Logger.getLogger("org.icesoft.testclient.actor");


    public void act(Client controller) {

        try {

            tempTime = System.currentTimeMillis();
            String updates =
                    controller.post(controller.getShorterUrl() + "block/send-receive-updates",
                                    getDataFull(controller));
              runningAverage =
                    updateRunningAverage( (System.currentTimeMillis() - tempTime),
                                          timeSamples, sampleCounter++);
            requestCountThisInterval++;
            
            log.fine(updates);

        } catch (Exception e) {
            log.throwing(this.getClass().getName(), "act", e);
            errorCountThisInterval++;
        }
    }

    public String getData(Client controller)  {
        StringBuffer data = new StringBuffer();
        try {
            data.append( URLEncoder.encode("ice.session", "UTF-8")).append("=");
            data.append( URLEncoder.encode(controller.getIceId(), "UTF-8"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data.toString();
    }



    public String getDataFull(Client controller)  {
        StringBuffer data = new StringBuffer( getData(controller) );
        try {

            // works with the syncTest application, and that's about it. 
            data.append("&").append(URLEncoder.encode("ice.view", "UTF-8")).append( "=" );
            data.append( URLEncoder.encode(controller.getViewNumber(), "UTF-8"));

            data.append("&").append("ice.submit.partial=true").append("&");

            data.append(URLEncoder.encode("ice.event.target", "UTF-8")).append("=");
            data.append(URLEncoder.encode("textFieldSeven:textG", "UTF-8")).append("&");

            data.append(URLEncoder.encode("ice.event.captured", "UTF-8")).append("=");
            data.append(URLEncoder.encode("textFieldSeven:textG", "UTF-8")).append("&");

            data.append(URLEncoder.encode("ice.event.type", "UTF-8")).append("=");
            data.append(URLEncoder.encode("onblur", "UTF-8")).append("&");

            data.append(URLEncoder.encode("textFieldSeven", "UTF-8")).append("=");
            data.append(URLEncoder.encode("", "UTF-8")).append("&");

            data.append(URLEncoder.encode("icefacesCssUpdates", "UTF-8")).append("=");
            data.append(URLEncoder.encode("", "UTF-8")).append("&");

            data.append(URLEncoder.encode("textFieldSeven:textG", "UTF-8")).append("=");
            data.append(URLEncoder.encode("", "UTF-8")).append("&");

            data.append(URLEncoder.encode("focus_hidden_field", "UTF-8")).append("=");
            data.append(URLEncoder.encode("", "UTF-8")).append("&");

            data.append(URLEncoder.encode("ice.focus", "UTF-8")).append("=");
            data.append(URLEncoder.encode("", "UTF-8"));

            data.append("&").append( URLEncoder.encode("rand", "UTF-8")).append("=");
            data.append( URLEncoder.encode(String.valueOf(Math.random()), "UTF-8" ));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data.toString();
    }
}
