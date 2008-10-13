package org.icesoft.testclient;

import org.icesoft.testclient.actor.Actor;
import org.icesoft.testclient.client.Client;

import java.util.List;
import java.util.ArrayList;

/**
 * The ActionThread is the Thread that runs through the Actors to do the work
 * against the server. There is one ActionThread per client. The behaviour is:
 * <pre>
 *  start
 *    doInitialDelay()
 *    for (repeatCount )
 *       for (Actor a: actorList)
 *          a.act( controller );
 *       }
 *       doInterRequestDelay()
 *    }
 * </pre>  
 */
public class ActionThread extends Thread {

    List <Actor> actorList;
    Client controller;

    protected int initialDelay;
    protected int repeatCount;
    protected int interRequestDelay;


    /**
     * Define the actionThread. This will extract the loop parameters out of the
     * controller, but they may be overridden at run time using the
     * property setters. 
     * 
     * @param controller Client
     * @param name String name
     */
     public ActionThread(Client controller, String name)  {
         super(name);
         this.controller = controller;
         setDaemon(true);
         this.initialDelay = controller.getInitialRequestDelay();
         this.repeatCount = controller.getRepeatCount();
         this.interRequestDelay = controller.getRepeatDelay();
    }

    public ActionThread(Client controller)  {
        this(controller, "Anonymous Action Thread");
    }

    /**
     * Define the work load for this thread, and start the thread
     * @param actors List of actors that will be acted on.
     */
    public void setActorList(List <Actor> actors) {

        actorList = actors;
        this.start();
    }

    /**
     * Loop through the list of Actors in the actor list until the controlling
     * client is done. 
     */
    public void run()  {
        try {

            if (initialDelay  > 0) {
                 Thread.sleep( initialDelay );
            }
            for (int idx = 0; idx < repeatCount; idx ++ ) {

                for (Actor a: actorList) {
                    a.act( controller );
                }
                
                if (interRequestDelay  > 0) {
                    Thread.sleep( interRequestDelay );
                }
            }
        } catch (Exception e)  {
            e.printStackTrace();
        }
        
    }

    public void terminate() {

    }

    public void insertActorAtHead(Actor a) {
        ArrayList <Actor> newActors = new ArrayList<Actor>();
        newActors.add( a );
        newActors.addAll( actorList );
        actorList = newActors;
    }

    public void addActor(Actor a) {
        actorList.add(a);
    }


    /**
     * Call this to override the setting from the controller. May be used
     * prior to the thread starting only
     * @param initialDelay
     */
    public void overrideInitialDelay(int initialDelay) {
        this.initialDelay = initialDelay;
    }

    /**
     * Alter the time delay between executing the act loop
     * @param interRequestDelay
     */
    public void overrideInterRequestDelay(int interRequestDelay) {
        this.interRequestDelay = interRequestDelay;
    }

    /**
     * Alter the number of times the loop will be executed before exiting
     * @param repeatCount
     */
    public void overrideRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }
}
