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

package org.icefaces.sample.timezone;

import com.icesoft.faces.async.render.IntervalRenderer;
import com.icesoft.faces.async.render.RenderManager;
import com.icesoft.faces.async.render.Renderable;
import com.icesoft.faces.context.DisposableBean;
import com.icesoft.faces.webapp.xmlhttp.FatalRenderingException;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import com.icesoft.faces.webapp.xmlhttp.RenderingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

/**
 * Bean backing the Time Zone application. This bean uses the RenderManager to
 * update state at a specified interval. It uses an injected session-scoped bean
 * to manage the timezone information and current selections.
 */

public class TimeZoneBean implements Renderable, DisposableBean {

    private static Log log = LogFactory.getLog(TimeZoneBean.class);

    /**
     * Time interval, in milliseconds, between renders.
     */
    private final int renderInterval = 1000;

    /**
     * The state associated with the current user that can be used for
     * server-initiated render calls.
     */
    private PersistentFacesState state;

    /**
     * A named render group that can be shared by all TimeZoneBeans for
     * server-initiated render calls.  Setting the interval determines the
     * frequency of the render call.
     */
    private IntervalRenderer clock;

    /**
     * Session bean that stores the current selections for this session.
     */
    private TimeZoneSelections timeZoneSelections;

    /**
     * Constructor initializes time zones.
     */
    public TimeZoneBean() {
        state = PersistentFacesState.getInstance();
    }


    public TimeZoneSelections getTimeZoneSelections() {
        return timeZoneSelections;
    }

    public void setTimeZoneSelections(TimeZoneSelections timeZoneSelections) {
        this.timeZoneSelections = timeZoneSelections;
    }

    /**
     * Used to create, setup, and start an IntervalRenderer from the passed
     * renderManager This is used in conjunction with faces-config.xml to allow
     * the same single render manager to be set in all TimeZoneBeans
     *
     * @param renderManager RenderManager to get the IntervalRenderer from
     */
    public void setRenderManager(RenderManager renderManager) {
        clock = renderManager.getIntervalRenderer("clock");
        clock.setInterval(renderInterval);
        clock.add(this);
        clock.requestRender();
    }

    /**
     * Gets RenderManager
     *
     * @return RenderManager null
     */
    public RenderManager getRenderManager() {
        return null;
    }

    //
    // Renderable interface
    //

    /**
     * Gets the current instance of PersistentFacesState
     *
     * @return PersistentFacesState state
     */
    public PersistentFacesState getState() {
        return state;
    }

    /**
     * Callback to inform us that there was an Exception while rendering.
     * Continue from a transientRenderingException but not
     * from a FatalRenderingException
     *
     * @param renderingException render exception passed in frome framework.
     */
    public void renderingException(RenderingException renderingException) {

        if (log.isDebugEnabled()) {
            log.debug("Rendering exception: ", renderingException);
        }

        if (renderingException instanceof FatalRenderingException) {
            performCleanup();
        }
    }

    /**
     * Used to properly shut-off the ticking clock.
     *
     * @return true if properly shut-off, false if not.
     */
    protected boolean performCleanup() {
        try {
            if (clock != null) {
                clock.remove(this);
                // whether or not this is necessary depends on how 'shutdown'
                // you want an empty renderer. If it's emptied often, the cost
                // of shutdown+startup is too great
                if (clock.isEmpty()) {
                    clock.dispose();
                }
                clock = null;
            }
            return true;
        } catch (Exception failedCleanup) {
            if (log.isErrorEnabled()) {
                log.error("Failed to cleanup a clock bean", failedCleanup);
            }

        }
        return false;
    }

    /**
     * Disposes a view either due to a window closing
     * or a timeout.
     */
    public void dispose() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Dispose TimeZoneBean for a user - cleaning up");
        }
        performCleanup();
    }

    //
    // Implicit interfaces as defined by the callbacks in the web files
    //

    /**
     * Listens to client input from commandButtons in the UI map and sets the
     * selected time zone.
     *
     * @param event ActionEvent.
     */
    public void listen(ActionEvent event) {
        timeZoneSelections.updateSelectedTimezone();
    }

    /**
     * Adds or removes a <code>TimeZoneWrapper</code> to
     * <code>checkedTimeZoneList</code> when a selectBooleanCheckbox
     * ValueChangeEvent is fired from the UI.
     *
     * @param event ValueChangeEvent.
     */
    public void timeZoneChanged(ValueChangeEvent event) {
        timeZoneSelections.updatedCheckedTimezones(event);
    }

}