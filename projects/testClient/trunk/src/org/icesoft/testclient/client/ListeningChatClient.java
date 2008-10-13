package org.icesoft.testclient.client;

import org.icesoft.testclient.ActionThread;
import org.icesoft.testclient.event.LoginEvent;
import org.icesoft.testclient.actor.LoginActor;
import org.icesoft.testclient.actor.Actor;
import org.icesoft.testclient.actor.PingActor;
import org.icesoft.testclient.actor.ChatObserverActor;

import java.util.List;
import java.util.ArrayList;

/**
 * This login client is implemented in the following way:
 * It builds on the ping+receive-updates-receive-updated-views model, but
 * adds a loginActor to the front of the actor queue. That's a 1 time actor only,
 * so even though it's at the head of the actor queue, it's only used once. 
 */
public class ListeningChatClient extends Client {

    public static int clientNumber;


     protected List<ActionThread> actionThreads = new ArrayList<ActionThread>();

    protected List <Actor> reportingActors = new ArrayList<Actor>();

    public void initSubclient()  {

        // Lists of all actors for reporting purposes.
        List <Actor>pingActors = new ArrayList<Actor>();
        List <Actor>receiveActors = new ArrayList<Actor>();


         LoginEvent le = new LoginEvent();
        le.setLoginForm("registrationForm");

        le.setLoginTextComponent("participantHandle");
        long time = System.currentTimeMillis();
        le.setUsername("testClient-"+ time);

        le.setLoginButton("startButton");
        le.setLoginButtonValue("Start");

        le.setFocusComponent("startButton");
        le.setFocusForm("registrationForm");

        LoginActor la = new LoginActor( le );
        

        ActionThread pingThread = new ActionThread(this, "Ping Thread " + clientId);
        actionThreads.add( pingThread );


        ActionThread receiveThread = new ActionThread(this, "Receive Thread "+ clientId);
        actionThreads.add( receiveThread );

        // Set up the receive-updated-views + receive-updates actor
        Actor updatesActor = new ChatObserverActor();

        // Set up the login actor, and the chat observer Actor
        receiveActors.add( la );
        receiveActors.add( updatesActor );

        // Set up the ping-actor
        Actor pingActor = new PingActor();
        pingActors.add( pingActor );

        // override this ping timing.         
        pingThread.overrideInterRequestDelay(PingActor.STANDARD_PING_INTERVAL);


        // Define the list of actors to the Action Thread
        pingThread.setActorList( pingActors ); // starts the thread
        receiveThread.setActorList( receiveActors );  // starts the thread

        // skip the login actor reporting. 
        reportingActors.add( updatesActor  );
//        reportingActors.addAll( pingActors );
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
