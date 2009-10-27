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
package com.icesoft.faces.presenter.participant.view;

import com.icesoft.faces.presenter.participant.Participant;
import com.icesoft.faces.presenter.presentation.Presentation;

import javax.faces.event.ActionEvent;

/**
 * Class used at a page level to represent the participant list This controls
 * how many participants are displayed, scrolling up and down, etc.
 */
public class ParticipantView {
    private static final int VIEW_SIZE = 5;

    private Participant[] participantView = new Participant[VIEW_SIZE];
    private int position = 0;
    private Presentation presentation;

    public ParticipantView() {
    }

    public int getViewSize() {
        return VIEW_SIZE;
    }

    public int getPosition() {
        return position;
    }
    
    public void setPosition(int position) {
        this.position = position;
    }

    public Presentation getPresentation() {
        return presentation;
    }
    
    public void setPresentation(Presentation presentation) {
        this.presentation = presentation;
    }

    /**
     * Method to generate the view as an array of Participant objects If no
     * presentation has been associated with this view, the list returned will
     * be empty
     *
     * @return list of participants
     */
    public Participant[] getView() {
        if (presentation != null) {
            // Fill the array used in the participant dataTable with participants relevant to current position
            for (int i = 0; i < VIEW_SIZE; i++) {
                participantView[i] =
                        presentation.getParticipantAt(position + i);
            }
        }

        return (participantView);
    }

    /**
     * Method to determine if the scroll down button for this view is enabled or
     * disabled, based on the size of the list.
     *
     * @return true to enable the scroll down button
     */
    public boolean getParticipantScrollDownDisabled() {
        if (presentation != null) {
            if ((presentation.getParticipantsSize() < VIEW_SIZE) ||
                (position == (presentation.getParticipantsSize() -
                              VIEW_SIZE))) {
                return true;
            }
        }

        return false;
    }

    /**
     * Method to determine if the scroll up button for this view is enabled or
     * disabled, based on the size of the list.
     *
     * @return true to enable the scroll up button
     */
    public boolean getParticipantScrollUpDisabled() {
        if (presentation != null) {
            if ((presentation.getParticipantsSize() < VIEW_SIZE) ||
                (position == 0)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Method to scroll the participant view up by one "page" This basically
     * means -= VIEW_SIZE
     *
     * @param event of the scroll
     */
    public void moveParticipantViewUp(ActionEvent event) {
        // Skip processing if there aren't even enough participants to scroll through
        if (presentation.getParticipantsSize() < VIEW_SIZE) {
            return;
        }

        // Apply the page up amount
        position -= VIEW_SIZE;

        // Ensure the position is within bounds of the top
        if (position < 0) {
            position = 0;
        }
    }

    /**
     * Method to scroll the participant view down by one "page" This basically
     * means += VIEW_SIZE
     *
     * @param event of the scroll
     */
    public void moveParticipantViewDown(ActionEvent event) {
        // Skip processing if there aren't even enough participants to scroll through
        int participantSize = presentation.getParticipantsSize();
        if (participantSize < VIEW_SIZE) {
            return;
        }

        // Apply the page down amount
        position += VIEW_SIZE;

        // Ensure the position is within the bounds of the bottom
        if (position > (participantSize - VIEW_SIZE)) {
            position = participantSize - VIEW_SIZE;
        }
    }
}