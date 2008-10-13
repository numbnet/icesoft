package org.icesoft.testclient.client;


import org.icesoft.testclient.ActionThread;
import org.icesoft.testclient.actor.Actor;
import org.icesoft.testclient.actor.ReceiveUpdatesActor;

import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Just a brief test class to see if the calls to the receieve updates
 * methods are in fact blocking calls. Well, receive-updated-views anyway.
 * They are. Thank you very much, ladies and germs. 
 */
public class TestReceiveUpdatesOnlyClient extends Client {

    private static final Logger log = Logger.getLogger("org.icesoft.testclient.client"); 


    List<ActionThread> actionThreads = new ArrayList<ActionThread>();
    List <Actor> reportingActors = new ArrayList<Actor>();


    public void initSubclient() {

        List <Actor>receiveActors = new ArrayList<Actor>();


        ActionThread receiveThread = new ActionThread(this, "Receive Thread "+ clientId);
        actionThreads.add(receiveThread);

        // Set up the receive-updated-views + receive-updates actor
        Actor updatesActor = new ReceiveUpdatesActor();
        receiveActors.add( updatesActor );


        // Define the list of actors to the Action Thread
        receiveThread.setActorList( receiveActors );  // starts the thread
        reportingActors.addAll( receiveActors );
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
