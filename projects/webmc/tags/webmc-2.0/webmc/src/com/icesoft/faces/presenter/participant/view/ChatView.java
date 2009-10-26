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
    private int viewSize = 5;

    private Message[] chatView = new Message[viewSize];
    private int position = 0;
    private Presentation presentation;

    public int getViewSize() {
        return viewSize;
    }

	public void setViewSize(int viewSize) {
		this.viewSize = viewSize;
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
     * Method to generate the chat log as an array of Message objects If no
     * presentation has been associated with this view, the list returned will
     * be empty
     *
     * @return list of messages
     */
    public Message[] getView() {
        if (presentation != null) {
            // Fill the array used in the chat dataTable with messages relevant 
        	// to the current position
            for (int i = 0; i < viewSize; i++) {
                chatView[i] = presentation.getMessageAt(
                        position - (viewSize - i - 1));
            }
        }

        return (chatView);
    }

    /**
     * Method to determine if the scroll down button for this view is enabled or
     * disabled, based on the size of the list.
     *
     * @return true to enable the scroll down button
     */
    public boolean getChatScrollDownDisabled() {
        if (presentation != null) {
            if ((presentation.getMessageLogSize() < viewSize) ||
                (position == (presentation.getMessageLogSize() - 1))) {
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
    public boolean getChatScrollUpDisabled() {
        if (presentation != null) {
            if ((presentation.getMessageLogSize() < viewSize) || (position == (
                    viewSize - 1))) {
                return true;
            }
        }

        return false;
    }

    /**
     * Method to scroll the chat log up by one "page" This basically means -=
     * VIEW_SIZE
     *
     * @param event of the scroll
     */
    public void moveChatViewUp(ActionEvent event) {
        // Skip processing if there aren't even enough messages to scroll through
        if (presentation.getMessageLogSize() < viewSize) {
            return;
        }

        // Apply the page up amount
        position -= viewSize;

        // Ensure the position is within bounds of the top
        if (position < (viewSize - 1)) {
            position = viewSize - 1;
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
        if (messageLogSize < viewSize) {
            return;
        }

        // Apply the page down amount
        position += viewSize;

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