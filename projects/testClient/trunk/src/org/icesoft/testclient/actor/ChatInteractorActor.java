package org.icesoft.testclient.actor;

import org.icesoft.testclient.client.Client;
import org.icesoft.testclient.event.Event;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * This Actor sends messages to the simpleChat application. It uses a custom
 * made Event class that generates the Id's for all the textfields and buttons
 * that need be pressed to do so.  
 *
 */
public class ChatInteractorActor extends ReceiveUpdatesActor {


    protected Pattern participantPattern = Pattern.compile("Participants: \\[([0-9]*)\\]", Pattern.DOTALL);
    protected Pattern messagesPattern = Pattern.compile("Messages: \\[([0-9]*)\\]", Pattern.DOTALL);

    protected static int messageCount;
    protected static int userCount;

    // send a message encoding
    protected Event clickDetails;

    protected int getUpdatesEvery = 20;
    protected int loopCount;

    


    /**
     * It's up to the client of this actor to construct the Event subclass
     * that encapsulates the login process, and fills it in with stuff.
     * @param clickEvent  The event that contains the information of the click
     */
    public ChatInteractorActor( Event clickEvent) {
        super();
        this.clickDetails = clickEvent;
    }

    /**
     * Send a chat message, then turn around and receive the updates therein.
     * This means clients using this actor don't have to use a separate receive updates
     * actor. 
     * @param controller Client
     */
    public void act(Client controller ) {

        try {

//            System.out.println("--> sending message: " + getDataFull(controller) );
            tempTime = System.currentTimeMillis();
            System.out.println("Chatting! ");
            String chatResult  = controller.post(controller.getShorterUrl() + "block/send-receive-updates",
                                                  getDataFull(controller));

//            System.out.println("Result: " + chatResult);

            runningAverage =
                    updateRunningAverage( (System.currentTimeMillis() - tempTime),
                                          timeSamples, sampleCounter++);
            requestCountThisInterval++;
            log.fine( chatResult );

        } catch (Exception e)  {
            
            log.throwing(this.getClass().getName(), "act, send-receive-updates",  e);
            errorCountThisInterval++;
        }

        // now allow the superclass to just receive updates
        if ((loopCount++ % getUpdatesEvery) == 0) {
            super.act( controller );

            // if everything went well, then the member variable updates should
            // have been updated
            if (super.updates != null) {

                System.out.println("Super.updates = " + super.updates);
                Matcher particiapantMatcher
                        = participantPattern.matcher(super.updates);

                Matcher messagesMatcher = messagesPattern.matcher(super.updates);

                try {
                    particiapantMatcher.find();
                    String pCount = particiapantMatcher.group(1);
                    int uCount = Integer.parseInt( pCount );
                    if (uCount > userCount) {
                        userCount = uCount;
                        System.out.println("--> New Participant count: " + userCount);
                    }
                } catch (IllegalStateException e) {


                } catch (NumberFormatException nfe) {}

                try {

                    messagesMatcher.find();
                    String mCount = messagesMatcher.group(1);
                    int messCount = Integer.parseInt( mCount );
                    if (messCount > messageCount) {
                        messageCount = messCount;
                        System.out.println("New MessageCount: " + messageCount);
                    }
                } catch (IllegalStateException e) {
                    // not a big problem. Not in all updates
                } catch (NumberFormatException nfe) {}
            }
        }
    }

    /**
     * This extension to the base actor uses the getDataFull method from teh
     * superclass, and appends to it the loginEvent from the Event subclass that
     * encapsulates a chat button press with textfield. That, in turn, is an
     * extension to a click event so it's all built up in pieces.
     *
     * @param controller Client controller 
     * @return String encoding the chat message 
     */
    public String getDataFull(Client controller) {

        // get big
        StringBuffer data = new StringBuffer( super.getDataFull(controller) );

        data.append(clickDetails.encodeEvent( controller ));
        return data.toString();
    }
}
