package org.icesoft.testclient.client;


import org.icesoft.testclient.ActionThread;
import org.icesoft.testclient.actor.Actor;

import org.icesoft.testclient.actor.PingActor;
import org.icesoft.testclient.actor.BiddingAuctionMonitorActor;

import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * This AuctionMonitorUpdatesClient will launch the standard ping and recieve-updates
 * Actors, except one of the actors will be the BiddingAuctionMonitorActor.  
 */
public class AuctionMonitorUpdatesClient extends Client {

    private static final Logger log = Logger.getLogger( "org.icesoft.testclient.client");
   

    List<ActionThread> actionThreads = new ArrayList<ActionThread>();

    List <Actor> reportingActors = new ArrayList<Actor>();

    public void initSubclient()  {

        // Lists of all actors for reporting purposes.
        List <Actor>pingActors = new ArrayList<Actor>();
        List <Actor>receiveActors = new ArrayList<Actor>();


        ActionThread pingThread = new ActionThread(this, "Ping Thread " + clientId);
        actionThreads.add( pingThread);
        

        ActionThread receiveThread = new ActionThread(this, "Receive Thread "+ clientId);
        actionThreads.add(receiveThread);

        // Set up the receive-updated-views + receive-updates actor
        Actor updatesActor = new BiddingAuctionMonitorActor();
        receiveActors.add( updatesActor );

        // Set up the ping-actor
        Actor pingActor = new PingActor();
        pingActors.add( pingActor );

        pingThread.overrideInterRequestDelay(PingActor.STANDARD_PING_INTERVAL);
        // Define the list of actors to the Action Thread
        pingThread.setActorList( pingActors ); // starts the thread
        receiveThread.setActorList( receiveActors );  // starts the thread

        reportingActors.addAll( receiveActors );
        reportingActors.addAll( pingActors );
    }

    public void terminate() {
        running = false;
        for (ActionThread a: actionThreads) {
            a.interrupt();
        }
    }

    public List<Actor> getReportingActors() {
        return reportingActors;
    }
        
}
