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

import com.icesoft.faces.presenter.chat.Message;
import com.icesoft.faces.presenter.presentation.Presentation;

import javax.faces.event.ActionEvent;

/**
 * Class used at a page level to represent the chat log This controls which
 * messages are displayed, scrolling up and down, etc.
 */
public class ChatView {
    private static final int VIEW_SIZE = 7;

    private Message[] chatView = new Message[VIEW_SIZE];
    private int position = 0;
    private Presentation presentation;

    /**
     * Method to return the position of this view
     *
     * @return position
     */
    public int getPosition() {
        return position;
    }

    /**
     * Method to return the presentation this view is used for
     *
     * @return presentation
     */
    public Presentation getPresentation() {
        return presentation;
    }

    /**
     * Method to return the size of this view
     *
     * @return VIEW_SIZE
     */
    public int getViewSize() {
        return VIEW_SIZE;
    }

    /**
     * Method to generate the chat log as an array of Message objects If no
     * presentation has been associated with this view, the list returned will
     * be empty
     *
     * @return list of messages
     */
    public Message[] getView() {
        if (presentation != null) {
            // Fill the array used in the chat dataTable with messages relevant to current position
            for (int i = 0; i < VIEW_SIZE; i++) {
                chatView[i] = presentation.getMessageAt(
                        position - (VIEW_SIZE - i - 1));
            }
        }

        return (chatView);
    }

    /**
     * Method to determine if the scroll down button for this view is enabled or
     * disabled, based on how big the list is
     *
     * @return true to enable the scroll down button
     */
    public boolean getChatScrollDownDisabled() {
        if (presentation != null) {
            if ((presentation.getMessageLogSize() < VIEW_SIZE) ||
                (position == (presentation.getMessageLogSize() - 1))) {
                return true;
            }
        }

        return false;
    }

    /**
     * Method to determine if the scroll up button for this view is enabled or
     * disabled, based on how big the list is
     *
     * @return true to enable the scroll up button
     */
    public boolean getChatScrollUpDisabled() {
        if (presentation != null) {
            if ((presentation.getMessageLogSize() < VIEW_SIZE) || (position == (
                    VIEW_SIZE - 1))) {
                return true;
            }
        }

        return false;
    }

    /**
     * Method to set the position of this view
     *
     * @param position new
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Method to set the presentation this view is associated with
     *
     * @param presentation new
     */
    public void setPresentation(Presentation presentation) {
        this.presentation = presentation;
    }

    /**
     * Method to scroll the chat log up by one "page" This basically means -=
     * VIEW_SIZE
     *
     * @param event of the scroll
     */
    public void moveChatViewUp(ActionEvent event) {
        // Skip processing if there aren't even enough messages to scroll through
        if (presentation.getMessageLogSize() < VIEW_SIZE) {
            return;
        }

        // Apply the page up amount
        position -= VIEW_SIZE;

        // Ensure the position is within bounds of the top
        if (position < (VIEW_SIZE - 1)) {
            position = VIEW_SIZE - 1;
        }
    }

    /**
     * Method to scroll the chat log down by one "page" This basically means +=
     * VIEW_SIZE
     *
     * @param event of the scroll
     */
    public void moveChatViewDown(ActionEvent event) {
        // Skip processing if there aren't even enough messages to scroll through
        int messageLogSize = presentation.getMessageLogSize();
        if (messageLogSize < VIEW_SIZE) {
            return;
        }

        // Apply the page down amount
        position += VIEW_SIZE;

        // Ensure the position is within the bounds of the bottom
        if (position > messageLogSize - 1) {
            position = messageLogSize - 1;
        }
    }

    /**
     * Method to force the view position to the bottom of the chat log
     */
    public void useBottomView() {
        position = presentation.bottom();
    }
}