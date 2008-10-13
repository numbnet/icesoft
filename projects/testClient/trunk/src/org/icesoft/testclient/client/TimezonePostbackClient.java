package org.icesoft.testclient.client;

import org.icesoft.testclient.ActionThread;
import org.icesoft.testclient.event.MouseClickEvent;
import org.icesoft.testclient.actor.Actor;
import org.icesoft.testclient.actor.TimezoneUpdateActor;

import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Class for Clicking on a timezone application window. 
 */
public class TimezonePostbackClient extends Client {

    private static final Logger log = Logger.getLogger("org.icesoft.testclient.client");

    ActionThread srThread;
    protected List<Actor> timezoneActors = new ArrayList<Actor>();

    protected String mapId;
    protected String formId; 

     protected Pattern mapIdPattern =
            Pattern.compile("<input id=\"([^\"]*:map)" , Pattern.DOTALL);

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
        // No mods necessary
        Actor sendActor = new TimezoneUpdateActor(me);
        timezoneActors.add( sendActor );

        ActionThread srThread = new ActionThread(this, "TimezoneUpdates Thread-"+ clientId);
        srThread.setActorList( timezoneActors );  // starts the thread

    }

    public void terminate() {
        running = false;
        srThread.interrupt();
    }

    public List getReportingActors() {
        return timezoneActors;
    }

    public String getMapId() {
        return mapId;
    }
    
    public String getFormId() {
        return formId;
    }
}
