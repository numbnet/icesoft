package com.icesoft.icefaces.samples.taxi;

import com.icesoft.faces.async.render.RenderManager;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The Simulation manager is a singleton responsible for running the live
 * simulations.
 */
public class SimulationManager {

    private static Log log = LogFactory.getLog(SimulationManager.class);

    /**
     * Time interval between new client creation requests
     */
    public static final int TAXI_CLIENT_UPDATE_TIME = 10;

    /**
     * Time interval between new client creation requests
     */
    public static final int TAXI_TAXI_UPDATE_TIME = 15;

    /**
     * Initial delay before updates will start
     */
    public static final int INITIAL_DELAY = 1;

    // thread management
    private static ScheduledThreadPoolExecutor scheduledExecutor;
    // thread specific configuration
    private int corePoolSize = 1;
    private int threadPriority = Thread.NORM_PRIORITY;

    private Runnable taxiClientBot;
    private Runnable taxiDriverBot;

    private TaxiRequestController taxiRequestController;

    private RenderManager renderManager;

    private boolean started;

    public SimulationManager() {

        // setup the thread pool/scheduler
        scheduledExecutor =
                new ScheduledThreadPoolExecutor(corePoolSize);
        scheduledExecutor.setThreadFactory(new ThreadFactory() {
            public Thread newThread(java.lang.Runnable command) {
                Thread newThread = new Thread(command);
                newThread.setPriority(threadPriority);
                return newThread;
            }
        });

        taxiClientBot = new TaxiClientBot();
        taxiDriverBot = new TaxiDriverBot();

    }

    /**
     * Start up the simulation with two worker bots.  One for making request
     * and the other for consuming requests.
     */
    public void startSimulation() {

        if (!started) {

            started = true;

            scheduledExecutor.scheduleWithFixedDelay(
                    taxiClientBot, 0, TAXI_CLIENT_UPDATE_TIME, TimeUnit.SECONDS);

            // start the drivers a little later.
            scheduledExecutor.scheduleWithFixedDelay(
                    taxiDriverBot, TAXI_TAXI_UPDATE_TIME,
                    TAXI_TAXI_UPDATE_TIME, TimeUnit.SECONDS);
        }
    }

    /**
     * Stops the simulation/thread executor in a semi-safe manner.
     */
    public void stopSimulation() {

        scheduledExecutor.remove(taxiClientBot);
        scheduledExecutor.remove(taxiDriverBot);

        scheduledExecutor.shutdown();
    }


    public void setRenderManager(RenderManager renderManager) {
        this.renderManager = renderManager;
    }

    public void setTaxiRequestController(TaxiRequestController taxiRequestController) {
        this.taxiRequestController = taxiRequestController;
    }


    /**
     * Helper class to make client requests.
     */
    class TaxiClientBot implements Runnable {

        private int count;

        public void run() {
            try {
                ArrayList<TaxiRequestWrapper> clientList =
                        taxiRequestController.getTaxiList(null);

                // max client view size is 6
                if (clientList.size() < 5){

                    TaxiRequest request = new TaxiRequest();
                    request.setName("Bot Request " + count++);
                    request.setAddress(count + "00 10th ST. SW.");
                    request.setIntersection("16th Ave. and 10th St.");
                    request.setCity("Calgary");
                    request.setComments("look for bot #"+count);

                    taxiRequestController.requestTaxiDriverPickup(
                            new TaxiClientUser(), request);

                    // server side push the update.
                    renderManager.getOnDemandRenderer(
                            Mediator.TAXI_DRIVER_GROUP_RENDERER).requestRender();
                }


            } catch (Throwable e) {
                log.info("Taxi Client Bot trouble:", e);
            }
        }
    }

    /**
     * Helper class to make client requests.
     */
    class TaxiDriverBot implements Runnable {

        private TaxiDriverUser taxiDriver = new TaxiDriverUser();

        public void run() {
            try {

                ArrayList<TaxiRequestWrapper> clientList =
                        taxiRequestController.getTaxiList(taxiDriver);

                // we don't wont to grab too many request as it get hard to demo.
                // with real clients.
                if (clientList.size() > 2){

                    // find the first client that we can accept and mark them as arrived
                    for (TaxiRequestWrapper client : clientList) {
                        if (taxiRequestController.lockTaxiRequest(
                                taxiDriver, client.getTaxiRequest())) {

                            // mark the client as picked up
                            if (taxiRequestController.acceptTaxiClientPickup(
                                    taxiDriver, client.getTaxiRequest(), 15)) {

                                taxiRequestController.arrived(client.getTaxiRequest());

                                // server side push the update.
                                renderManager.getOnDemandRenderer(
                                    Mediator.TAXI_DRIVER_GROUP_RENDERER).requestRender();

                            }

                            // unlock the request
                            taxiRequestController.unLockTaxiRequest(client.getTaxiRequest());

                            // that's all we do for an interval of work.
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                log.info("Taxi Driver Bot trouble:", e);
            }
        }
    }
}
