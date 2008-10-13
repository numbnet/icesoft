package org.icesoft.testclient.client;


import org.icesoft.testclient.actor.Actor;
import org.icesoft.testclient.actor.SyncTestUpdatesActor;
import org.icesoft.testclient.actor.ReceiveUpdatesActor;
import org.icesoft.testclient.actor.PingActor;
import org.icesoft.testclient.ActionThread;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * This single client will launch three threads to do it's work.
 * First the receive updates thread,
 * then a ping thread, and slower, in the background, the send-receive-updates thread
 * to tab between textfields  in the application.
 * 
 */
public class SyncTestPostbackClient extends Client {

    private static final Logger log = Logger.getLogger("org.icesoft.testclient.client");


    List <ActionThread>actionThreads = new ArrayList<ActionThread>();

    List <Actor> reportingActors = new ArrayList<Actor>();
    



    public void initSubclient() {

        List <Actor> sendUpdatesActors = new ArrayList<Actor>();
        List <Actor>pingActors = new ArrayList<Actor>();
        List <Actor>receiveActors = new ArrayList<Actor>();



        ActionThread srThread = new ActionThread(this, "SendReceiveUpdates Thread-"+ clientId);
        actionThreads.add( srThread );
        
        ActionThread pingThread = new ActionThread(this, "Ping Thread " + clientId);
        actionThreads.add( pingThread);

        ActionThread receiveThread = new ActionThread(this, "Receive Thread "+ clientId);
        actionThreads.add(receiveThread);

        // Set up the receive-updated-views + receive-updates actor
        Actor updatesActor = new ReceiveUpdatesActor();
        receiveActors.add( updatesActor );

        // Set up the ping-actor
        Actor pingActor = new PingActor();
        pingActors.add( pingActor );

        Actor sendReceiveActor = new SyncTestUpdatesActor();
        sendUpdatesActors.add( sendReceiveActor );
        
        pingThread.overrideInterRequestDelay( PingActor.STANDARD_PING_INTERVAL);

        // Define the list of actors to the Action Thread
        pingThread.setActorList( pingActors ); // starts the thread
        receiveThread.setActorList( receiveActors );  // starts the thread
        srThread.setActorList( sendUpdatesActors );  // starts the thread
        

        reportingActors.addAll( receiveActors );
        reportingActors.addAll( pingActors );
        reportingActors.addAll( sendUpdatesActors );
        

    }

    public void terminate() {
        running = false;
        for (ActionThread a: actionThreads) {
            a.interrupt();
        }
    }

    public List getReportingActors() {
        return reportingActors;
    } 
}
