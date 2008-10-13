package org.icesoft.testclient.client;

import org.icesoft.testclient.ActionThread;
import org.icesoft.testclient.event.LoginEvent;
import org.icesoft.testclient.event.SendTextEvent;
import org.icesoft.testclient.actor.Actor;
import org.icesoft.testclient.actor.LoginActor;
import org.icesoft.testclient.actor.ChatObserverActor;
import org.icesoft.testclient.actor.PingActor;
import org.icesoft.testclient.actor.ChatInteractorActor;

import java.util.List;
import java.util.ArrayList;
import java.awt.*;

/**
 * This login client is implemented in the following way:
 * It builds on the ping+receive-updates-receive-updated-views model, but
 * adds a loginActor to the front of the actor queue. That's a 1 time actor only,
 * so even though it's at the head of the actor queue, it's only used once.
 */
public class TalkingChatClient extends Client {

    public static int clientNumber;


     protected List<ActionThread> actionThreads = new ArrayList<ActionThread>();

    protected List <Actor> reportingActors = new ArrayList<Actor>();

    public void initSubclient()  {

        // Lists of all actors for reporting purposes.
        List <Actor>pingActors = new ArrayList<Actor>();
        List <Actor>talkingActors = new ArrayList<Actor>();


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

        // These may need to be updated to match what the page generates.
        // This needs to be something. REGEX? For now, I've manually defined the ID's
        // in the page source

        SendTextEvent ste = new SendTextEvent();
        ste.setComponentId("sendMessageForm:sendMessageButton");
        ste.setComponentValue("Send");
        ste.setSendForm("sendMessageForm");
        ste.setTextfieldId("messageInputText");
        ste.setBaseMessage(clientId + " sends a message: " );
        ste.setP( new Point( 281, 145));

        ChatInteractorActor cia = new ChatInteractorActor( ste );

        ActionThread pingThread = new ActionThread(this, "Ping Thread " + clientId);
        actionThreads.add( pingThread );

        ActionThread sendThread = new ActionThread(this, "Chat Thread " + clientId);
        actionThreads.add( sendThread);

        // Set up the login actor, and the chat interactor Actor
        talkingActors.add( la );
        talkingActors.add( cia );

        // Set up the ping-actor
        Actor pingActor = new PingActor();
        pingActors.add( pingActor );

        pingThread.overrideInterRequestDelay(PingActor.STANDARD_PING_INTERVAL);

        // Define the list of actors to the Action Thread
        pingThread.setActorList( pingActors ); // starts the thread
        sendThread.setActorList( talkingActors );  // starts the thread

        reportingActors.add( cia );
//        reportingActors.addAll( talkingActors );
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
