package org.icesoft.testclient.actor;


import org.icesoft.testclient.client.Client;
import org.icesoft.testclient.client.TimezonePostbackClient;
import org.icesoft.testclient.event.Event;
import org.icesoft.testclient.event.MouseClickEvent;

import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.logging.Logger;
import java.awt.Point;

/**
 * This class generates a string that simulates pressing on various places within
 * the Timezone application. This must be used with caution as the x,y coordinates
 * to press to achieve interaction vary with the exact application (becuase of artwork) 
 */
public class TimezoneUpdateActor extends ActorBase {

    private static final Logger log = Logger.getLogger("org.icesoft.testclient.actor");

    // Offset from their JSF alternates due to some crazy xOffset, yOffset?
    private Point[] mapPoints = { new Point (52,8+142),    // alaska
                                  new Point (208,116+142), // pacific
                                  new Point (232,28+142),  // mountain
                                  new Point (302,46+142),  // central
                                  new Point (409,58+142),  // eastern
                                  new Point (45,207+142)  }; // hawaii

    private int pointIndex;
    private Event clickEvent;

    public TimezoneUpdateActor(Event clickEvent) {
        this.clickEvent = clickEvent;
    }

    

    public void act(Client controller) {

        try {

            tempTime = System.currentTimeMillis();
            String updates =
                    controller.post(controller.getShorterUrl() + "block/send-receive-updates",
                                    getDataFull(controller));
             runningAverage =
                    updateRunningAverage( (System.currentTimeMillis() - tempTime), timeSamples, sampleCounter++);
            requestCountThisInterval++;

            // Every once in a while, parse this value from the page.
            if (requestCountThisInterval % 25 == 0) {
                Matcher mapIdMatcher;
                mapIdMatcher = memoryStatusPattern.matcher( updates );

                mapIdMatcher.find();
                memoryStatus = mapIdMatcher.group(1).trim();
            }
            log.fine(updates);

        } catch (Exception e) {
            log.throwing("Error getting response: ", "a", e);
            errorCountThisInterval++;
        }
    }




    public String getDataFull(Client controller)  {
        
        StringBuffer data = new StringBuffer( super.getDataFull( controller ) );

        TimezonePostbackClient tpc = (TimezonePostbackClient) controller;
        String mapId = tpc.getMapId();
        String formId = tpc.getFormId();
        
        try {

            Point p = mapPoints[ pointIndex++ % mapPoints.length];
            MouseClickEvent me = (MouseClickEvent) clickEvent;
            // update the x,y location in the default mouseClickEvent
            me.setP( p );

            // works with the timezone application, and that's about it
            data.append(clickEvent.encodeEvent( controller ) );

            data.append(URLEncoder.encode(mapId, "UTF-8")).append("=");
            data.append(URLEncoder.encode("", "UTF-8")).append("&");

            data.append(URLEncoder.encode(formId, "UTF-8")).append("=");
            data.append(URLEncoder.encode(formId, "UTF-8")).append("&");

            data.append(URLEncoder.encode("ice.focus", "UTF-8")).append("=");
            data.append(URLEncoder.encode(mapId, "UTF-8")).append("&");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return data.toString();
    }

    public String report(Client controller) {
        return super.report( controller ) + " Total Memory: " + memoryStatus; 
    }
}
