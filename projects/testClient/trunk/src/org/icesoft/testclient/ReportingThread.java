package org.icesoft.testclient;

import org.icesoft.testclient.actor.Actor;
import org.icesoft.testclient.client.Client;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;
import java.text.DateFormat;

/**
 * @author ICEsoft Technologies, Inc.
 */
/**
 * A reporting thread that just goes and asks the actors how they're doing.
 */
class ReportingThread extends Thread  {

    private static final Logger log = Logger.getLogger("org.icesoft.testclient");


    private List<Client> clientList = new ArrayList<Client>();
    private DateFormat dtf = DateFormat.getDateTimeInstance();
    private long reportInterval;


    public ReportingThread(long reportInterval)  {
        super("Reporting Thread");
        this.reportInterval = reportInterval;
        setDaemon(true);
    }

    public void addClient(Client reportingClient) {
        clientList.add( reportingClient );
    }

    public void run()  {

        boolean done = false;
        while (!done) {
            try {

                Thread.sleep( reportInterval);
                Date d = new Date(System.currentTimeMillis() );
                System.out.println("Client status at: " + dtf.format( d ) );

                for (Client c : clientList ) {
                    List <Actor> aList= c.getReportingActors();
                    for (Actor a: aList) {
                        try {
                            System.out.println("-> " + a.report(c) );
                        } catch (Exception e) {
                        }
                    }
                }

            } catch (Exception e) {
                log.throwing(this.getClass().getName(), "run",  e);
            }
        }
    }
}
