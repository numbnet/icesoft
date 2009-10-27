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
import com.icesoft.faces.presenter.util.MessageBundleLoader;

import javax.faces.event.ValueChangeEvent;

import javax.faces.model.SelectItem;

/**
 * Class used to handle the page level setup of slide show mode
 * This mainly includes letting the user set the delay between
 * slide changes.
 */
public class SlideshowTimerBean extends SlideshowTimer {
    private static final SelectItem[] CHANGE_DELAY_ITEMS =
            generateChangeDelayItems();
    private static final int MIN_CHANGE_COUNT = 2;
    private static final int MAX_CHANGE_COUNT = 8;

    public SlideshowTimerBean() {
        super();
    }

    public SlideshowTimerBean(Presentation parent) {
        super(parent);
    }

    public SelectItem[] getChangeDelayItems() {
        return CHANGE_DELAY_ITEMS;
    }

    /**
     * Method to generate a list of SelectItems that are used
     * to set the change delay from the page
     * The list will be between MIN_CHANGE_COUNT and MAX_CHANGE_COUNT
     *
     * @return generated list
     */
    private static SelectItem[] generateChangeDelayItems() {
        int size = MAX_CHANGE_COUNT - MIN_CHANGE_COUNT;
        int index = 0;
        SelectItem[] toReturn = new SelectItem[size];

        for (int i = MIN_CHANGE_COUNT; i < MAX_CHANGE_COUNT; i++) {
            toReturn[index] = new SelectItem(new Integer(i), String.valueOf(i) + "s");
            index++;
        }

        return toReturn;
    }

    /**
     * Method called when the value of the change delay dropdown on
     * the page has been changed
     * This is used to notify the moderator (through an updateStatus) of
     * the change
     *
     * @param vce of the event
     */
    public void changeDelayListener(ValueChangeEvent vce) {
        parent.getModerator().updateStatus(
        		MessageBundleLoader.getMessage("bean.slideshowTimerBean.changeDelayListener.statusMessage1")
        		+ " " + vce.getNewValue() + " " + 
        		MessageBundleLoader.getMessage("bean.slideshowTimerBean.changeDelayListener.statusMessage2"));
    }
}