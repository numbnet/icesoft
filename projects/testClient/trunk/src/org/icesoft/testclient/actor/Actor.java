package org.icesoft.testclient.actor;

import org.icesoft.testclient.client.Client;

import java.util.List;

/**
 * The Actor interface defines the methods to be implemented by Classes wishing
 * to act in some meaningful way with a Web Application. Typically, an Actor
 * will do only a single request and it's up to the Client to configure multiple
 * Actors to perform some testing work. For example, there is a <code>PingActor</code> that
 * simply sends a Ping request, and it should be used in conjunction with the
 * <code>ReceiveUpdatesActor</code> to perform the basic heartbeat+update checking
 * action that is provided by the browser. It makes good
 * sense to have them in separate threads so the Ping can act at its time interval
 * while the receive-updated-views and recieve-updates command can work in tandem
 * at the frequency the application requires.
 * <p>
 * If you are running a chat application, the PingActor works every 50 seconds
 * while the RecieveUpdatesActor operates as fast as there are chat messages.
 * <p>
 * However, Actors higher up the food chain can be written two ways. They can
 * extend simpler Actors with inheritance and then call super.act() before
 * doing their own action, or they can do something meaningful with the delegate
 * Actor list (along with writing a custom client to set the whole thing up)
 * to extend the Actor behaviour with less code.
 *  
 */
public interface Actor {

    /**
     * Get Data returns a potentially short form of the request
     * @param controller controller to get resources from
     * @return String representing the short form of request
     */
    public String getData(Client controller);

    /**
     * GetFataFull may return a longer form of the request
     * @param controller controller to get resources from
     * @return String representing the short form of request
     */
    public String getDataFull(Client controller);

    /**
     * Do the work here. Actors should be reasonably atomic and do a single
     * piece of work. They should support the repeat count and the inter request
     * delay, but they may choose not to for purposes of being bad actors to
     * test the framework.
     *
     * It's also possible to write an actor that does one request and returns.
     * The ActionThread now loops, so multiple actors can be strung together
     * in an ActorChain, providing the ability to progress through a complex transaction,
     * without having to do it inside a single act method. This should help writing
     * more, simpler Actors. 
     *  
     * @param controller StandalonePingReceiveUpdatesClient object for resources.
     */
    public void act (Client controller);


    /**
     * Testing out extending functionality with delegation rather than inheritance.
     * Inheritance works, but it's awfully difficult to approach this from any
     * distance and imagine what it is you want to do. The purpose of this method
     * is to allow clients to build up lists of Actors to pass to a high level
     * actor. The high level actor will call act() on all the delegates in whatever
     * order it chooses when its act() method is called, allowing a bit more
     * separation between the Actors to build up functionality.

     * @param delegates List of Actors to be reused 
     */
    public void initDelegateList(List<Actor> delegates);

    /**
     * StandalonePingReceiveUpdatesClient will request a report of progress at various intervals. Number of
     * requests of your type, number of errors, etc.
     *
     * @return A report for make better world progress !!
     */
    public String report(Client controller);
        

}
