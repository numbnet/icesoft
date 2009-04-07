package org.icesoft.testclient.actor;

import org.icesoft.testclient.client.Client;

import java.util.logging.Logger;

/**
 * @author ICEsoft Technologies, Inc.
 */
public class PingActor extends ActorBase {

 private static final Logger log = Logger.getLogger("org.icesoft.testclient.actor");

    public static final int STANDARD_PING_INTERVAL = 50000;

    /**
     * Behave properly in this class
     * @param controller StandalonePingReceiveUpdatesClient object for resources
     */
    public void act(Client controller)  {

        try {

            // This is somewhat irrelevant also, since this is only the time it
            // takes for the request to come back empty.

            // temporary workaround until I can configure the PingActor and the receiveUpdates
            // actor separately
            tempTime = System.currentTimeMillis();
            String pingResult  = controller.post(controller.getShorterUrl() + "block/ping",
                                                 getDataFull(controller));

             runningAverage =
                    updateRunningAverage( (System.currentTimeMillis() - tempTime),
                                          timeSamples, sampleCounter++);
            requestCountThisInterval++;

            
           log.fine(pingResult);
        } catch (Exception e)  {
            log.throwing( "Exception caught in ping: ", "a",  e);
            errorCountThisInterval++;
        }
    }
}

