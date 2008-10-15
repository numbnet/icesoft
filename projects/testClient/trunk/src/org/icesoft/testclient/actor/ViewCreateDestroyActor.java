package org.icesoft.testclient.actor;

import org.icesoft.testclient.client.Client;

import java.net.URL;
import java.net.URLConnection;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.List;
import java.util.ArrayList;

/**
 * This actor is constructed with a reference to another actor
 * for the following purpose. It will connect, allow the
 * referenced actor to do its thing, then will destroy the view.
 *
 * This is an example of using delegation between actors to achieve more.
 * This is an alternative to using inheritance for expanding the capabilities
 * of the Actors without doing repetition.
 */
public class ViewCreateDestroyActor extends ActorBase {


    protected List<Actor> delegates = new ArrayList();

    protected boolean firstRequestPass = true;


    public void initDelegateList(List<Actor> delegates) {
        this.delegates.addAll( delegates );
    }
    


    public void act(Client controller ) {


        // We need to request the url again only. A GET request, so we can't
        // use the client.post() method (since it sets doOutput(true) which does post )
        // but we need to have the JSession ID cookie set so that the server
        // treats this as a new View creation, rather than a request from a
        // new user. 
        // the

        try {


            if (!firstRequestPass) {

                System.out.println("Doing the login");


                URL firstPageURL = new URL(controller.getShorterUrl() );
                long start = System.currentTimeMillis();
                URLConnection firstPageConnection = firstPageURL.openConnection();
                firstPageConnection.setRequestProperty("Cookie", controller.getCookie() );
                InputStream firstPageStream = firstPageConnection.getInputStream();

                String initialResponse = Client.getInputAsString(
                        new InputStreamReader(firstPageStream) );

                System.out.println("Refetch new loadTime Load Time: " + (System.currentTimeMillis() - start) + " ms");

                Matcher viewNumberMatcher;
                if (controller.isBranch()) {
                    viewNumberMatcher = controller.branchViewNumberPattern.matcher(initialResponse);
                } else {
                    viewNumberMatcher = controller.viewNumberPattern.matcher( initialResponse );
                }
                viewNumberMatcher.find();

                // update the controller with the new view number
                controller.setViewNumber(viewNumberMatcher.group(1).trim());

                System.out.println("New View numb3r: " + viewNumberMatcher.group(1).trim());


                Matcher iceIdMatcher;

                if (controller.isBranch() ) {
                    iceIdMatcher = controller.branchIceIdPattern.matcher(initialResponse);
                } else {
                    iceIdMatcher = controller.iceIdPattern.matcher( initialResponse );
                }

                iceIdMatcher.find();

                String iceId = iceIdMatcher.group(1);
                System.out.println("Updated ICEID == " + iceId );
//            controller.setIceId( iceId );

            }
            firstRequestPass = false;
            

            Thread.sleep( 1000 );
            // Ok that's the re-login part, now allow the other actor to act. 

            for (Actor a: delegates ) {
                a.act( controller );
            }

//             Now, dispose of this view and bugger off eh.

            Thread.sleep( 5000 );
            System.out.println("OK, disposing-view");

            String updates =
                    controller.post(controller.getShorterUrl() + "block/dispose-views",
                                    getDataFull(controller));

            requestCountThisInterval ++;

        } catch (Exception e ) {

            System.out.println("Exception: " + e);
            e.printStackTrace();

        }

    }





}