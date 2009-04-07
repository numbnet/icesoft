package org.icesoft.testclient.actor.jsf;

import org.icesoft.testclient.client.Client;
import org.icesoft.testclient.client.jsf.JSFClient;
import org.icesoft.testclient.actor.ActorBase;

import java.net.URLEncoder;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.logging.Logger;

/**
 * @author ICEsoft Technologies, Inc.
 */
public class JSFTimezoneActor extends ActorBase {

    private static final Logger log = Logger.getLogger("org.icesoft.testclient.actor.jsf");

    private Point[] mapPoints = { new Point (44,8),    // alaska
                                  new Point (200,116), // pacific
                                  new Point (224,28),  // mountain
                                  new Point (294,46),  // central
                                  new Point (401,58),  // eastern
                                  new Point (37,207) }; // hawaii

    private int pointIndex;

    


    /**
     * Behave properly in this class
     * @param controller (JSF)Client object for resources
     */
    public void act(Client controller)  {

        try {

            tempTime = System.currentTimeMillis();
            String timezoneResult  = controller.post(controller.getShorterUrl() + "timezone.faces",
                                                     getDataFull(controller));

//            System.out.println("Timezone result: " + timezoneResult);

              runningAverage =
                    updateRunningAverage( (System.currentTimeMillis() - tempTime),
                                          timeSamples, sampleCounter++);
            requestCountThisInterval++;
            
            // Every once in a while, parse this value from the page. 
            if (requestCountThisInterval % 10 == 0) {
                Matcher mapIdMatcher;
                mapIdMatcher = memoryStatusPattern.matcher( timezoneResult );

                mapIdMatcher.find();
                memoryStatus = mapIdMatcher.group(1).trim();
            } 
            
            log.fine( timezoneResult );
        } catch (Exception e)  {
            log.throwing(this.getClass().getName(), "act", e);
            errorCountThisInterval++;
        }
    }

    /**
     * Retrieve the common parts of all requests. Return a string containing
     * common request parameters here.
     * @param controller Client containing configuration information
     * @return Shorter String containing request parameters
     */
    public String getData(Client controller)  {
        StringBuffer data = new StringBuffer();
        JSFClient jsfClient = (JSFClient) controller;
        try {
//            data.append( URLEncoder.encode("JSESSIONID", "UTF-8")).append("=");
//            data.append( URLEncoder.encode(jsfClient.getCookie(), "UTF-8"));


        } catch (Exception e) {
            e.printStackTrace();
        }
        return data.toString();
    }

    /**
     * Get the full data package
     * @param controller
     * @return
     */
    public String getDataFull(Client controller)  {
        StringBuffer data = new StringBuffer( getData(controller) );
        JSFClient jsfClient = (JSFClient) controller;

        try {

            Point p = mapPoints[ pointIndex++ % mapPoints.length];

            // the jsf 1.2 implementation of this is quite complex!  
            data.append(URLEncoder.encode(jsfClient.getFormId(), "UTF-8" ) ).append( "=");
            data.append(URLEncoder.encode(jsfClient.getFormId(), "UTF-8" ) ); 

            data.append("&").append(URLEncoder.encode(jsfClient.getMapId(), "UTF-8")).append( ".x=" );
            data.append( URLEncoder.encode(Integer.toString(p.x), "UTF-8"));

            data.append("&").append(URLEncoder.encode(jsfClient.getMapId(), "UTF-8")).append( ".y=" );
            data.append( URLEncoder.encode(Integer.toString(p.y), "UTF-8"));

            data.append("&").append(URLEncoder.encode("javax.faces.ViewState", "UTF-8")).append( "=" );
            data.append( URLEncoder.encode(jsfClient.getViewState(), "UTF-8"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data.toString();
    }

    public String report(Client controller) {
        return super.report( controller ) + " Total Memory: " + memoryStatus;
    }
}
