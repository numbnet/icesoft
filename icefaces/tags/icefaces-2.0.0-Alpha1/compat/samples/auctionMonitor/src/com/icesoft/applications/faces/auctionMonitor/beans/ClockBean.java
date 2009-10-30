/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
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
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 *
 */

package com.icesoft.applications.faces.auctionMonitor.beans;

//import com.icesoft.faces.async.render.IntervalRenderer;
//import com.icesoft.faces.async.render.RenderManager;
import com.icesoft.faces.async.render.SessionRenderer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class used to control the background clock of the entire auction monitor By
 * queuing a render call every pollInterval (default 1000) milliseconds, this
 * class allows the auction monitor UI to have ticking clocks In addition this
 * class will help AuctionBean maintain a list of the number of users online
 * through incrementUsers and decrementUsers
 */
//public class ClockBean implements Renderable, DisposableBean {
public class ClockBean implements Serializable {
    private static Log log = LogFactory.getLog(ClockBean.class);
//    private IntervalRenderer clock;
    private int pollInterval = 1000;
    private static Thread thread = null;
    private boolean isRunning = true;
    private String autoLoad = " ";

    private static final String AUTO_LOAD = "ClockBean-Loaded";
    private static final String INTERVAL_RENDERER_GROUP = "clock";

    public ClockBean() {
        AuctionBean.incrementUsers();
    }

    @PostConstruct
    public synchronized void renderPeriodically() {
        if (null != thread)  {
            return;
        }
        thread = new Thread("Auction Clock Thread") {
            public void run() {
                while (isRunning) {
                    try {
                        Thread.sleep(pollInterval);
                    } catch (InterruptedException e) { }
                    SessionRenderer.render("auction");
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    public String getAutoLoad() {
        if (" ".equals(autoLoad)) {
            autoLoad = AUTO_LOAD;
        }
        return autoLoad;
    }

    public void setPollInterval(int interval) {
        pollInterval = interval;
    }

    public int getPollInterval() {
        return pollInterval;
    }

    @PreDestroy
    public void dispose()  {
        isRunning = false;
    }

}
