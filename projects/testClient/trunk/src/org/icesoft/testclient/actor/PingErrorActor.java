package org.icesoft.testclient.actor;


import org.icesoft.testclient.client.Client;

import java.net.URLEncoder;
import java.util.logging.Logger;

/**
 * This Actor generates a ping request withouth all of the necessary fields. 
 */
public class PingErrorActor extends ActorBase {

    private static final Logger log = Logger.getLogger("org.icesoft.testclient.actor");

    /**
     * Behave badly in this class. Omit a field. 
     * @param controller StandalonePingReceiveUpdatesClient object for resources
     */
    public void act(Client controller)  {

        try {

            tempTime = System.currentTimeMillis();
            String errorResult  = controller.post(controller.getShorterUrl() + "block/ping",
                                                  getDataFull(controller));
             runningAverage =
                    updateRunningAverage( (System.currentTimeMillis() - tempTime),
                                          timeSamples, sampleCounter++);
            requestCountThisInterval++;
            
            log.fine(errorResult);

        } catch (Exception e)  {
            log.fine ( "Error Response properly thrown" );
            requestCountThisInterval++;
            errorCountThisInterval++;
        }
    }

    /**
     * Return a String missing ice.session
     */
    public String getData(Client controller)  {
        StringBuffer data = new StringBuffer();
        try {
//            data.append( URLEncoder.encode("ice.session", "UTF-8")).append("=");
            data.append( URLEncoder.encode(controller.getIceId(), "UTF-8"));
            data.append("&").append( URLEncoder.encode("rand", "UTF-8")).append("=");
            data.append( URLEncoder.encode(String.valueOf(Math.random()), "UTF-8" ));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data.toString();
    }
}
