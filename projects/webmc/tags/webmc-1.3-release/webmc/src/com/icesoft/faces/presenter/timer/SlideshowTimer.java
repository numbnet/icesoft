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
package com.icesoft.faces.presenter.timer;

import com.icesoft.faces.presenter.presentation.Presentation;

/**
 * Class used to automatically transition a presentation
 * This is done with a thread, and gives the appearance of a slide show.
 */
public class SlideshowTimer extends Thread {
    protected Presentation parent;
    protected Integer changeDelay = new Integer(4); // delay in seconds
    protected boolean keepRunning = false;

    public SlideshowTimer() {
    }

    public SlideshowTimer(Presentation parent) {
        super();

        this.parent = parent;
    }

    public Presentation getParent() {
        return parent;
    }

    public void setParent(Presentation parent) {
        this.parent = parent;
    }

    public Integer getChangeDelay() {
        return changeDelay;
    }

    public void setChangeDelay(Integer changeDelay) {
        this.changeDelay = changeDelay;
    }

    /**
     * Method to get the change delay (as milliseconds)
     *
     * @return changeDelay milliseconds
     */
    public int getChangeDelayAsMilliseconds() {
        return changeDelay.intValue()*1000;
    }

    /**
     * Method to determine if the slide show thread is currently running
     *
     * @return keepRunning
     */
    public boolean isRunning() {
        return keepRunning;
    }

    /**
     * Wrapper method to start the slide show
     * This sets the keepRunning variable and kicks off the thread
     */
    public void startSlideshow() {
        keepRunning = true;
        start();
    }

    /**
     * Method to stop the slide show
     * The check if the slide show is even running should be done at a higher 
     * level  This will reset the keepRunning variable, and interrupt the thread,
     * which will then stop itself when it sees that keepRunning is false
     */
    public void stopSlideshow() {
        keepRunning = false;
        interrupt();
    }

    /**
     * Method to provide the core thread functionality of the slide show
     * This basically sleeps for changeDelay (as milliseconds), then forwards
     * the presentation's slide
     */
    public void run() {
        try {
            do {
                Thread.sleep(getChangeDelayAsMilliseconds());

                // Make sure the parent is valid
                if (parent == null) {
                    keepRunning = false;
                    break;
                }

                // If we're still alive, forward the page
                if (keepRunning) {
                    parent.forwardOne(null);
                }
            } while (keepRunning);
        } catch (InterruptedException stopped) {
            /* Intentionally ignored - interrupts are used to stop the thread loop */
        } catch (Exception threadError) {
            keepRunning = false;
        }
    }
}