package org.icesoft.testclient.actor;

import org.icesoft.testclient.client.Client;
import org.icesoft.testclient.event.Event;


import java.util.logging.Logger;

/**
 * The LoginActor Effectively logs in to the Chat Application. The implementation
 * of this class separates information required to press the login button along
 * with the login text fields into an instance of Event, while this class just
 * contains the normal ActorBase generated strings. This class can do alternate
 * forms of login just by passing in a different event, and can be used as a
 * delegate with other Actors to generate an Actor that logs in, does something,
 * logs out, etc.  
 */
public class LoginActor extends ActorBase {

    // only log in once
    protected boolean oneTime; 
    protected Event loginEvent;
    private static final Logger log = Logger.getLogger("org.icesoft.testclient.client");



    /**
     * It's up to the client of this actor to construct the Event subclass
     * that encapsulates the login process, and fills it in with stuff. 
     * @param loginEvent The Event Object containing the necessary UI information
     * required to generate the string containing the login request
     */
    public LoginActor( Event loginEvent) {
        super();
        this.loginEvent = loginEvent;
    }


    public void act(Client controller ) {

        if (!oneTime) {
            try {

//                System.out.println("--> Logging in: " + getDataFull(controller) );
                tempTime = System.currentTimeMillis();
                String loginResult  = controller.post(controller.getShorterUrl() + "block/send-receive-updates",
                                                      getDataFull(controller));


//                System.out.println("Result: " + loginResult);
                runningAverage =
                        updateRunningAverage( (System.currentTimeMillis() - tempTime),
                                              timeSamples, sampleCounter++);
                requestCountThisInterval++;

               log.fine(loginResult);
            } catch (Exception e)  {
                log.throwing( "Exception caught in ping: ", "a" , e);
                errorCountThisInterval++;
            }

            oneTime = true;
        }         
    }


    /**
     * This extension to the base actor uses the getDataFull method from teh
     * superclass, and appends to it the loginEvent from the Event subclass that
     * encapsulates a login button press. That, in turn, is an extension to a click event
     * so it's all built up in pieces.
     * 
     * @param controller
     * @return
     */
    public String getDataFull(Client controller) {

        // get big
        StringBuffer data = new StringBuffer( super.getDataFull(controller) );

        data.append(loginEvent.encodeEvent( controller ));
        return data.toString();
    }
}
