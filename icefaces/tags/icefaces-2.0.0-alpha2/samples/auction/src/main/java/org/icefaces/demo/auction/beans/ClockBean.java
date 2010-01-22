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

package org.icefaces.demo.auction.beans;

import org.icefaces.application.PushRenderer;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;

/**
 * Class used to control the background clock of the entire auction monitor By
 * queuing a render call every pollInterval (default 1000) milliseconds, this
 * class allows the auction monitor UI to have ticking clocks In addition this
 * class will help AuctionBean maintain a list of the number of users online
 * through incrementUsers and decrementUsers
 */
@ManagedBean(name = "ClockBean")
@ApplicationScoped
public class ClockBean  {

    private static Logger log = Logger.getLogger(ClockBean.class.getName());
    
    private int pollInterval = 1000;
    private String autoLoad = " ";

    private static final String AUTO_LOAD = "ClockBean-Loaded";
    
    private Timer clockTimer = null;
    private TimerTask renderTask = new TimerTask()  {
        public void run() {
            PushRenderer.render("auction");
            if( log.isLoggable(Level.FINEST) ){
                log.fine("render done for 'auction' using " + clockTimer);
            }
        }
    };

    public ClockBean() {
    }

    @PostConstruct
    public void renderPeriodically() {
        setPollInterval(pollInterval);
    }

    @PreDestroy
    public void cleanup(){
        if (null != clockTimer)  {
            clockTimer.cancel();
        }
        if( log.isLoggable(Level.FINEST) ){
            log.finest("cleaning up " + clockTimer);
        }
    }

    public String getAutoLoad() {
        if (" ".equals(autoLoad)) {
            autoLoad = AUTO_LOAD;
        }
        return autoLoad;
    }

    public void setPollInterval(int interval) {
        if (null != clockTimer)  {
            clockTimer.cancel();
        }
        pollInterval = interval;
        clockTimer = new Timer(true);
        clockTimer.schedule(renderTask, 0, pollInterval);
    }

    public int getPollInterval() {
        return pollInterval;
    }

}
