package org.icesoft.testclient.actor;

import org.icesoft.testclient.client.Client;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * @author ICEsoft Technologies, Inc.
 */
public class ReceiveUpdatesActor extends ActorBase {

    protected static final Logger log = Logger.getLogger("org.icesoft.testclient.actor");


    protected String updatedViews;
    protected String updates;
    protected long blockingAverage;

    protected long[] blockingSamples = new long[5];


    public void act(Client controller) {

        String url = controller.getShorterUrl();

        try {

            // Overall time for these requests is somewhat meaningless,
            // since this post is blocking and waiting for something to happen.

            tempTime = System.currentTimeMillis();
            updatedViews = null;


            updatedViews =
                    controller.post(url + "block/receive-updated-views",
                                    getData(controller) + "\n");
            blockingAverage = updateRunningAverage((System.currentTimeMillis() - tempTime),
                    blockingSamples,
                    requestCountThisInterval);

            if (log.isLoggable(Level.FINE )) {
                log.fine ( updatedViews );
            }
        } catch (Exception e) {
            log.throwing(this.getClass().getName(), "act, receive-updated-views",  e);

            System.out.println("Exception thrown reading reply: " + e);
            e.printStackTrace();
            System.out.println(updatedViews);
            errorCountThisInterval++;
            return;
        }

        try { 

            tempTime = System.currentTimeMillis();

            updates = null;
            updates =
                    controller.post(url + "block/receive-updates",
                                    getDataFull(controller));
            if (updates.indexOf("<session-expired/>") >  -1) {
                controller.setRunning(false);
                return;
            }
            runningAverage =
                    updateRunningAverage( (System.currentTimeMillis() - tempTime), timeSamples, sampleCounter++);
            requestCountThisInterval++;

            log.fine( updates );
//            System.out.println(updates);


        } catch (Exception e) {
            log.throwing(this.getClass().getName(), "act, receive-updates",  e);

            System.out.println("Exception thrown reading receive-updates, " + e);
            System.out.println(updates);
            errorCountThisInterval++;
        }
    }    
}
