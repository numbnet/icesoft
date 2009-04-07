package org.icesoft.testclient.actor;


import org.icesoft.testclient.client.Client;

import java.net.URLEncoder;
import java.util.regex.Pattern;
import java.util.List;

/**
 * The ActorBase class just does some commonly necessary things.
 * getFullData() returns a common basic message containing fields from the
 * Client's initial request. It does reporting, and manages average request
 * times for the subclass.
 *
 * I don't know of any real great reason for getData and getFullData except to
 * allow a customer to access a sub part of the functionality. 
 *
 *
 */
public abstract class ActorBase implements Actor {

    /**
     * Number of requests delivered in this reporting interval
     */
    protected int requestCountThisInterval;

    /**
     * Total number of requests delivered over lifetime of executable
     */
    protected int cumulativeRequestCount;

    /**
     * Number of errors this reporting period
     */
    protected int errorCountThisInterval;

    /**
     * Total number of errors encountered by this Actor
     */
    protected int cumulativeErrorCount;

    protected long tempTime;
    protected long timeSamples[] = new long[12];
    protected long runningAverage; 

    protected String memoryStatus;
    protected int sampleCounter;
    

     protected Pattern memoryStatusPattern =
            Pattern.compile("Total Memory:([^\"]*:end)" , Pattern.DOTALL);

    /**
     * Retrieve the common parts of all requests. Return a string containing
     * common request parameters here. This method returns only <ul>
     * <li>ice.session</li>
     * <li>rand</li>
     * </ul>
     * @param controller Client containing configuration information
     * @return Shorter String containing request parameters
     */
    public String getData(Client controller)  {
        StringBuffer data = new StringBuffer();
        try {
            data.append( URLEncoder.encode("ice.session", "UTF-8")).append("=");
            data.append( URLEncoder.encode(controller.getIceId(), "UTF-8"));
            
            data.append("&").append( URLEncoder.encode("rand", "UTF-8")).append("=");
            data.append( URLEncoder.encode(String.valueOf(Math.random()), "UTF-8" ));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data.toString();
    }

    


    /**
     * Retrieve an extended version of a request. Custom Actor clients should override
     * this method to define custom payloads. This method calls <code>getData</code> to obtain
     * the common request information before adding to it. This method appends <ul>
     * <li>ice.view or ice.view.all</li>
     * <li>focus_hidden_field=&</li>
     * <li>icefacesCssUpdates=&</li>
     * </ul>
     * to the request
     *
     * @see #getData(Client)
     * @param controller Client containing configuration info
     * @return String containing complete request
     */
    public String getDataFull(Client controller)  {
        StringBuffer data = new StringBuffer( getData(controller) );
        try {

            if (controller.isBranch() ) {
                 data.append("&").append(URLEncoder.encode("ice.view.all", "UTF-8")).append( "=" );
            } else {
                data.append("&").append(URLEncoder.encode("ice.view", "UTF-8")).append( "=" );
            }
            
            data.append( URLEncoder.encode(controller.getViewNumber(), "UTF-8")).append("&");
            data.append("focus_hidden_field=&");
            data.append("icefacesCssUpdates=&\n"); 

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data.toString();
    }

    /**
     * Retrieve the formatted Resport string. 
     * @return A formatted string that reports the progress of this Actor over
     * the last minute
     */
    public String report(Client controller) {
        cumulativeErrorCount += errorCountThisInterval;
        cumulativeRequestCount += requestCountThisInterval;

        StringBuffer ret =new StringBuffer();
        ret.append( toString() ).append(" - ice.session: ").append(controller.getIceId() ).append("\n"); 
        ret.append(" \t requests: ").append(requestCountThisInterval).append( "\t cumulative: ").append(cumulativeRequestCount);
        ret.append( "\t cumulative errors: ").append( cumulativeErrorCount).
                append("\t average: " ).append ( runningAverage ).append( " ms/request");

        requestCountThisInterval = 0;
        errorCountThisInterval = 0;
        return ret.toString();
    }

    /**
     * An algorithm implementation for calculating a running average of elapsed time.
     * This method can be used with stock member variables of this class, or may
     * be used just to update subclass variables if other things are measured
     *
     * typical use
     *
     * super.updateRunningAverage ('request time', timeSamples, sampleCounter++ );
     * 
     * @param elapsedTime Time for this last update
     * @param runningBuffer buffer of last 'n' times
     * @param timeIndex  ever increasing message index
     * @return The running average of the buffer contents
     */
    protected int updateRunningAverage(long elapsedTime, long runningBuffer[], int timeIndex) {

        runningBuffer[ timeIndex % runningBuffer.length] = elapsedTime;
        int localCount = 0;  
        for (int idx = 0; idx < runningBuffer.length; idx ++ ) {
            localCount += runningBuffer[idx];
        }
        return localCount / runningBuffer.length;
    }

    /**
     * Do nothing in the base. This is pretty much up to the individual Actor.
     * This method can be called on an Actor instance allowing writers of the Actor
     * to add code for what it's supposed to do, but then to delegate to this list
     * of Actors to do what already has been written. An example is the
     * TimezoneCreateDestroyActor which joins in, pokes the application, destroys the view,
     * and repeats. No need to reimpliment the Actor that pokes Timezone, that's already
     * been done, so pass it in, and the new Actor will delegate where appropriate.  
     *  
     * @param delegates List of Actors to do work around the main Actor
     */
    public void initDelegateList(List<Actor> delegates) {
    }

    public String toString() {
        String className = getClass().toString();
        int epos = className.lastIndexOf(".");
        if (epos > -1) {
            return className.substring(epos+1);
        } else {
            return className;
        }
    }
}
