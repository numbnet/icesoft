package org.icesoft.testclient.actor;

import org.icesoft.testclient.client.Client;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * This actor extends the ReceiveUpdatesActor, and allows it to act and parses
 * the returnValue of that Actors act() method. Pretty Basic stuff. Used to see
 * that the messages are being distributed properly among all the chat room attendees.
 */
public class ChatObserverActor extends ReceiveUpdatesActor {


    protected Pattern participantPattern = Pattern.compile("Participants: \\[([0-9]*)\\]", Pattern.DOTALL);
    protected Pattern messagesPattern = Pattern.compile("Messages: \\[([0-9]*)\\]", Pattern.DOTALL);

    protected static int messageCount;
    protected static int userCount;


    /**
     * There's nothing extra
     * @param controller
     */
    public void act(Client controller ) {
        super.act( controller );

        // if everything went well, then the member variable updates should
        // have been updated

        if (super.updates != null) {
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
