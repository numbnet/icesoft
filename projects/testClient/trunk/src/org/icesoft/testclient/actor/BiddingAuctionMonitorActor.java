package org.icesoft.testclient.actor;

import org.icesoft.testclient.client.Client;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.logging.Logger;
import java.util.Random;
import java.text.NumberFormat;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * This actor keeps tracks of the current bid on the first item in the auction monitor.
 * Every once in a while, it will submit a bid to the program. 
 *
 * This consists of being in a receieve-updated-views+ receive-updates loop.
 * The response is parsed.
 *
 */
public class BiddingAuctionMonitorActor extends ActorBase {

    private static final Logger log = Logger.getLogger("org.icesoft.testclient.client");

    //    protected Pattern auctionValuePattern = Pattern.compile("Ice.Community.Application.*session: \'([^,]*)\',", Pattern.DOTALL);
    protected Pattern auctionValuePattern =
            Pattern.compile("iceForm:iceTable:0:item_currentPrice]]>.*<!\\[CDATA\\[(\\$[0-9,.]*)", Pattern.DOTALL);

    protected Pattern timeUpdatePattern =
            Pattern.compile("<content><!\\[CDATA\\[  ([ d0-9:]*)\\]\\]", Pattern.DOTALL);


    protected NumberFormat nf = NumberFormat.getCurrencyInstance();
    protected Number latestBid = new Float( 10000.0f );
    protected Number maximumBid = new Float ( 10000 + new Random(System.nanoTime()).nextInt(90000)  );
    protected boolean bidFieldExposed;

    /**
     *
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

            String updates =
                    controller.post(controller.getShorterUrl() + "block/receive-updates",
                                    getDataFull(controller));
            if (updates.indexOf("<session-expired/>") >  -1) {
                controller.setRunning(false);
                return;
            }
            runningAverage =
                    updateRunningAverage( (System.currentTimeMillis() - tempTime),
                                          timeSamples, sampleCounter++);
            requestCountThisInterval++;

            Random r = new Random(System.nanoTime());

            // Quickly check if there's a bid
            if (updates.indexOf("$") > -1) {

                try {
                    Matcher auctionBidMatcher;
                    auctionBidMatcher = auctionValuePattern.matcher( updates );

                    auctionBidMatcher.find();
                    String currentBid = auctionBidMatcher.group(1);

                    latestBid = nf.parse( currentBid );
                    System.out.println("Current Bid is: " + currentBid + " bidToBeat: "+ latestBid
                            .floatValue());


                } catch (IllegalStateException e) {
                    System.out.println("--> No bid in last update loop");
                }
            } else if (updates.indexOf("<pong/>") > -1) {
            } else if (updates.indexOf("<noop/>") > -1) {
            } else if (updates.indexOf("item_bidCount") > -1) {
            } else if (updates.indexOf("Effect.Highlight") > -1) { 
            } else {

                // could be a time update
                try {
                    Matcher timeRemainingMatcher;
                    timeRemainingMatcher = timeUpdatePattern.matcher( updates );

                    timeRemainingMatcher.find();
                    String timeField = timeRemainingMatcher.group(1);
                    System.out.print(".");

                    if ((r.nextInt(1000) < 10) && (latestBid != null)) {

                        if (!bidFieldExposed) {

                            String enableFieldRequest = getEnableBidRequest(controller);
                            try {
                                updates = controller.post(controller.getShorterUrl() + "block/send-receive-updates",
                                                          enableFieldRequest );
//                            System.out.println("-> " + updates);
                                bidFieldExposed = true;
                            } catch (IOException ioe) {
                                System.out.println("IOException submitting bid: " + ioe);
                            }

                        }


                        String bidRequest = getBidRequest( latestBid.floatValue() , controller);
                        System.out.println("  Time remaining on item: " + timeField);
                        try {
                            updates = controller.post(controller.getShorterUrl() + "block/send-receive-updates",
                                                      bidRequest );

                            if (latestBid.floatValue() > maximumBid.floatValue()) {
                                controller.setRunning(false);
                                System.out.println("Client iceId: " + controller.getIceId() + " exceeded max bid, exiting");
                                return;
                            } 
                        } catch (IOException ioe) {
                            System.out.println("IOException submitting bid: " + ioe);
                        }
                    }


                } catch (Exception e) {
//                    System.out.println("Exception caught parsing time field: " + e);
//                    System.out.println("---> Error updates: " + updates);
                }
            }

        } catch (Exception e) {
            log.throwing("Exception getting response: " , "a", e);
            errorCountThisInterval++;
        }
    }

    protected String getBidRequest(float oldBid, Client controller) {
        StringBuffer requestBuilder = new StringBuffer();

        try {
            requestBuilder.append("ice.submit.partial=false" );

            requestBuilder.append("&ice.event.target").append("=");
            requestBuilder.append( URLEncoder.encode("iceForm:iceTable:0:item_localBid", "UTF-8" ));

            requestBuilder.append("&ice.event.captured").append("=");
            requestBuilder.append( URLEncoder.encode("iceForm:iceTable:0:item_localBid", "UTF-8" ));

            requestBuilder.append("&ice.event.type=onkeypress" );
            requestBuilder.append("&ice.event.alt=false" );
            requestBuilder.append("&ice.event.ctrl=false" );
            requestBuilder.append("&ice.event.shift=false" );
            requestBuilder.append("&ice.event.meta=false" );
//             requestBuilder.append("&ice.event.x=1191" );
//             requestBuilder.append("&ice.event.y=193" );
//             requestBuilder.append("&ice.event.left=true" );
//             requestBuilder.append("&ice.event.right=false" );
            requestBuilder.append("&ice.event.keycode=13" );


//            requestBuilder.append("&").append(URLEncoder.encode( "iceForm:iceTable:0:image_button_bid_accept", "UTF-8")).append("=");
            requestBuilder.append("&iceForm=" );
            requestBuilder.append("&icefacesCssUpdates=" );
            requestBuilder.append("&javax.faces.ViewState=1" );
//            requestBuilder.append("&javax.faces.RenderKitId=ICEfacesRenderKit" );
            requestBuilder.append("&javax.faces.RenderKitId=" );

            // Construct new bid
            requestBuilder.append("&").append(URLEncoder.encode("iceForm:iceTable:0:item_localBid", "UTF-8")).append("=");
            latestBid = new Float( oldBid+100.0f);

            System.out.println("\nClient: " + controller.getIceId() + " upping bid from: " + oldBid + " to " +
                               latestBid);
            requestBuilder.append( URLEncoder.encode(latestBid.toString(), "UTF-8"));

            requestBuilder.append("&").append(URLEncoder.encode( "iceForm:_idcl", "UTF-8")).append("=");
            requestBuilder.append("&ice.session=" ).append( controller.getIceId() );
            requestBuilder.append("&ice.view=" ).append( controller.getViewNumber() );
            requestBuilder.append("&ice.focus=" ).append(URLEncoder.encode( "iceForm:iceTable:0:item_localBid", "UTF-8") );
            requestBuilder.append("&rand=0.7827899249785862" ).append("\n\n");

        } catch (Exception e) {
            System.out.println("Exception encoding bid: " + e);
        }
        return requestBuilder.toString();
    }

    protected String getEnableBidRequest( Client controller) {
        StringBuffer requestBuilder = new StringBuffer();

        try {
            requestBuilder.append("ice.submit.partial=false" );

            requestBuilder.append("&ice.event.target").append("=");
            requestBuilder.append( URLEncoder.encode("iceForm:iceTable:0:image_button_bid", "UTF-8" ));

            requestBuilder.append("&ice.event.captured").append("=");
            requestBuilder.append( URLEncoder.encode("iceForm:iceTable:0:image_button_bid", "UTF-8" ));

            requestBuilder.append("&ice.event.type=onclick" );
            requestBuilder.append("&ice.event.alt=false" );
            requestBuilder.append("&ice.event.ctrl=false" );
            requestBuilder.append("&ice.event.shift=false" );
            requestBuilder.append("&ice.event.meta=false" );
            requestBuilder.append("&ice.event.x=1149" );
            requestBuilder.append("&ice.event.y=191" );
            requestBuilder.append("&ice.event.left=true" );
            requestBuilder.append("&ice.event.right=false" );
//            requestBuilder.append("&ice.event.keycode=13" );


            requestBuilder.append("&").append(URLEncoder.encode( "iceForm:iceTable:0:image_button_bid", "UTF-8")).append("=");
            requestBuilder.append("&iceForm=iceForm" );
            requestBuilder.append("&icefacesCssUpdates=" );
            requestBuilder.append("&javax.faces.ViewState=1" );
            requestBuilder.append("&javax.faces.RenderKitId=ICEfacesRenderKit" );
//            requestBuilder.append("&javax.faces.RenderKitId=" );

            // Construct new bid
//            requestBuilder.append("&").append(URLEncoder.encode("iceForm:iceTable:0:item_localBid", "UTF-8")).append("=");
//            String val =      Float.toString(oldBid+100.0f);
//            System.out.println("Upping bid from: " + oldBid + " to " + val);
//            requestBuilder.append( URLEncoder.encode(val, "UTF-8"));

            requestBuilder.append("&").append(URLEncoder.encode( "iceForm:_idcl", "UTF-8")).append("=");
            requestBuilder.append("&ice.session=" ).append( controller.getIceId() );
            requestBuilder.append("&ice.view=" ).append( controller.getViewNumber() );
            requestBuilder.append("&ice.focus=" ).append(URLEncoder.encode( "iceForm:iceTable:0:item_localBid", "UTF-8") );
            requestBuilder.append("&rand=0.7827899249785862" ).append("\n\n");

        } catch (Exception e) {
            System.out.println("Exception encoding bid: " + e);
        }
        return requestBuilder.toString();
    }


}
