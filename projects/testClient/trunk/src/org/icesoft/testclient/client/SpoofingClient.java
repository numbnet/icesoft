package org.icesoft.testclient.client;

import org.icesoft.testclient.actor.Actor;
import org.icesoft.testclient.actor.SpoofingActor;
import org.icesoft.testclient.ActionThread;

import java.util.List;
import java.util.ArrayList;

/**
 * The idea behind the spoofer client is to submit a request on behalf
 * of someone else, therefore, no logging in is required, and the Actor will
 * likely be modified in source to do what's actually required
 */
public class SpoofingClient extends Client {


    protected List<Actor> spoofingActors;
    ActionThread spThread;

    /**
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

        try {

            initialRequestDelay = initialDelay;
            this.repeatDelay = repeatDelay;
            this.repeatCount = repeatCount;
            this.clientId = clientId;
            this.branch = isBranch;

            this.initialUrl = url;
            if (!this.initialUrl.endsWith("/") ) {
                this.initialUrl += "/";
            }


            initSubclient();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
  public void initSubclient() {


        Actor spoofingActor = new SpoofingActor();

        spoofingActors  = new ArrayList<Actor>();
        spoofingActors.add (spoofingActor);


        spThread = new ActionThread(this, "SpoofingActor Thread-"+ clientId);
        spThread.setActorList( spoofingActors );  // starts the thread

    }

     public void terminate() {
        running = false;
        spThread.interrupt();
    }

    public List<Actor> getReportingActors() {
        return spoofingActors;
    }
}
