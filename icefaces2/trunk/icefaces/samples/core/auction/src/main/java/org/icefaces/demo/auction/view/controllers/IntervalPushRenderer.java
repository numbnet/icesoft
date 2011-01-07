/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */
package org.icefaces.demo.auction.view.controllers;

import org.icefaces.application.PortableRenderer;
import org.icefaces.application.PushRenderer;
import org.icefaces.demo.auction.view.util.FacesUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The interval render setup up a timer task that is responsible for initiating
 * the server side push at set intervals. The interval is configurable with
 * the context parameter org.icefaces.sample.auction.interval.
 * <p/>
 * The timer will shutdown its self when there server shuts down via @PreDestroy.
 *
 * @author ICEsoft Technologies Inc.
 * @since 2.0
 */
@ManagedBean(eager = true)
@ApplicationScoped
public class IntervalPushRenderer {

    private static Logger log = Logger.getLogger(IntervalPushRenderer.class.getName());

    public static final String INTERVAL_RENDER_GROUP = "auctionInterval";

    private static int POLLING_INTERVAL = 1000;

    static {
        String interval = FacesUtils.getFacesParameter(
                "org.icefaces.sample.auction.interval");
        if (interval != null) {
            try {
                POLLING_INTERVAL = Integer.parseInt(interval);
            } catch (NumberFormatException e) {
                log.log(Level.WARNING, "Error applying org.icefaces.demo.auction.interval, " +
                        "must be valid integer.", e);
            }
        }

    }

    private Timer intervalTimer;
    private TimerTask renderTask;

    /**
     * Constructs an new instance of the intervalTimer and sets up the timer
     * task.  The timer tasks main goal is to flush the service layer cache
     * as well as initiate a server push the render group INTERVAL_RENDER_GROUP.
     */
    public IntervalPushRenderer() {

        final PortableRenderer renderer = PushRenderer.getPortableRenderer(
                FacesContext.getCurrentInstance());

        renderTask = new TimerTask() {
            public void run() {
                try {
                    // NOTE: this thread doesn't 

                    // make the push call to everyone in the interval group
                    renderer.render(INTERVAL_RENDER_GROUP);
                    if (log.isLoggable(Level.FINEST)) {
                        log.finest("Render done for 'auction' using " + intervalTimer);
                    }
                } catch (Throwable e) {
                    log.log(Level.WARNING, "Error running interval timer task.", e);
                }
            }
        };
    }

    /**
     * Starts our interval timer task. Intended to keep running until this
     * Application scoped bean is shutdown by the server.
     */
    @PostConstruct
    public void initializeIntervalRender() {
        if (null != intervalTimer) {
            intervalTimer.cancel();
        }
        intervalTimer = new Timer(true);
        intervalTimer.schedule(renderTask, 0, POLLING_INTERVAL);
    }

    /**
     * Stops the interval timer, generally only for server shutdown.
     */
    @PreDestroy
    public void cleanup() {
        if (null != intervalTimer) {
            intervalTimer.purge();
            intervalTimer.cancel();
        }
        if (log.isLoggable(Level.FINEST)) {
            log.finest("cleaning up " + intervalTimer);
        }
    }
}
