package org.icesoft.testclient.actor;

import org.icesoft.testclient.client.Client;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.logging.Logger;
import java.text.NumberFormat;

/**
 * This actor keeps tracks of the current bid on the first item in the auction monitor.
 * Every once in a while, it will submit a bid to the program, although this is not
 * yet implemented.
 * 
 * This consists of being in a receieve-updated-views+ receive-updates loop.
 * The response is parsed.
 *
 * 
 */
public class BiddingAuctionMonitorActor extends ActorBase {

    private static final Logger log = Logger.getLogger("org.icesoft.testclient.client");

//    protected Pattern auctionValuePattern = Pattern.compile("Ice.Community.Application.*session: \'([^,]*)\',", Pattern.DOTALL);
    protected
Pattern auctionValuePattern = Pattern.compile("iceForm:iceTable:0:item_currentPrice]]>.*<!\\[CDATA\\[(\\$[0-9,.]*)", Pattern.DOTALL);


    /**
     * Incomplete. 
     * @param controller
     */
    public void act(Client controller) {

        try {

            // Overall time for these requests is somewhat meaningless,
            // since this post is blocking and waiting for something to happen.

            tempTime = System.currentTimeMillis();
            String updatedViews =
                    controller.post(controller.getShorterUrl() + "block/receive-updated-views",
                                    getData(controller) + "\n");



            log.fine( updatedViews );
           
            tempTime = System.currentTimeMillis();
            System.out.println("Posting receieve-updates");

            String updates =
                    controller.post(controller.getShorterUrl() + "block/receive-updates",
                                    getDataFull(controller));
             runningAverage =
                    updateRunningAverage( (System.currentTimeMillis() - tempTime),
                                          timeSamples, sampleCounter++);
            requestCountThisInterval++;

//            int spos = updates.indexOf("$100" );
//            if (spos > 0) {
//                System.out.println("update->" + updates);
//            }

            try {
                Matcher auctionBidMatcher;
                auctionBidMatcher = auctionValuePattern.matcher( updates );

                auctionBidMatcher.find();
                String currentBid = auctionBidMatcher.group(1);

                NumberFormat nf = NumberFormat.getCurrencyInstance();
                Number n = nf.parse( currentBid );

                System.out.println("Current Bid is: " + currentBid + " bidToBeat: "+ n.floatValue());
            } catch (IllegalStateException e) {
                System.out.println("--> No bid in last update loop");
            } 


            log.fine( updates );



        } catch (Exception e) {
            log.throwing("Exception getting response: " , "a", e);
            errorCount ++;
        }
    }
}
