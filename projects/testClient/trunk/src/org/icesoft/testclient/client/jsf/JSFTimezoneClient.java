package org.icesoft.testclient.client.jsf;

import org.icesoft.testclient.ActionThread;
import org.icesoft.testclient.actor.Actor;
import org.icesoft.testclient.actor.jsf.JSFTimezoneActor;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * This class sets up the actors to hit the timezone1 JSF only application 
 */
public class JSFTimezoneClient extends JSFClient {

    private static final Logger log = Logger.getLogger("org.icesoft.testclient.actor");

    List <ActionThread>actionThreads = new ArrayList<ActionThread>();
    List<Actor> timezoneActors = new ArrayList<Actor>();

    public void initSubclient()  {

        // Lists of all actors for reporting purposes.

        // The

        ActionThread timezoneThread = new ActionThread(this, "ChangeTimezoneThread " + clientId);
        actionThreads.add( timezoneThread );


        // Set up the receive-updated-views + receive-updates actor
        Actor updatesActor = new JSFTimezoneActor();
        timezoneActors.add( updatesActor );

        // Define the list of actors to the Action Thread
        timezoneThread.setActorList( timezoneActors ); // starts the thread
    }

    public void terminate() {
        running = false;
        for (ActionThread a: actionThreads) {
            a.interrupt();
        }
    }

    public List<Actor> getReportingActors() {
        return timezoneActors;
    }
}
