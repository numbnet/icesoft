package org.icesoft.testclient.client;

import org.icesoft.testclient.ActionThread;
import org.icesoft.testclient.event.MouseClickEvent;
import org.icesoft.testclient.actor.Actor;
import org.icesoft.testclient.actor.TimezoneUpdateActor;
import org.icesoft.testclient.actor.ViewCreateDestroyActor;

import java.util.logging.Logger;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * This client defines 2 actors. One is the createDestroyView actor, which
 * just does that type of thing, but it delegates to another actor to do
 * something between those two requests. In this case, a nice bit of
 * timezone interaction would be just the thing.
 * 
 *
 * Class for simulating the on-blur partial submit from a textfield
 * for a specific application
 */
public class TimezoneMultiViewClient extends TimezonePostbackClient {

    private static final Logger log = Logger.getLogger("org.icesoft.testclient.client");

    public void initSubclient() {

        Matcher mapIdMatcher;
        mapIdMatcher = mapIdPattern.matcher( super.initialResponse );

        mapIdMatcher.find();
        mapId = mapIdMatcher.group(1).trim();
        int spos = mapId.indexOf(":");
        if (spos > -1) {
            formId = mapId.substring(0, spos);
        } else {
            System.out.println("FormId not found? ");
            return;
        }

        MouseClickEvent me = new MouseClickEvent();
        // No modifications for the sendUpdatesActor
        Actor sendUpdatesActor = new TimezoneUpdateActor(me);

        // Now create the view creator/destroyer actor with the sendActor as Delegate
        ViewCreateDestroyActor vcd = new ViewCreateDestroyActor();
        List<Actor> delegate = new ArrayList<Actor>();
        delegate.add(sendUpdatesActor);
        vcd.initDelegateList( delegate );

        timezoneActors.add( vcd );

        ActionThread srThread = new ActionThread(this, "TimezoneUpdates Thread-"+ clientId);
        srThread.setActorList( timezoneActors );  // starts the thread

    }

}
