/*
 * Version: MPL 1.1
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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 */
package org.icefaces.application.showcase.view.bean.examples.layoutPanel.panelToolTip;
import java.io.Serializable;

/**
 * @since 1.7
 */
public class PanelToolTipModel implements Serializable{

    private String hideOn = "mouseout";

    private String hoverDelay ="1000";

    private boolean draggable;

    private String displayOn = "hover";

    private boolean moveWithMouse = false;

    public String getHideOn() {
        return hideOn;
    }

    public void setHideOn(String hideOn) {
        this.hideOn = hideOn;
    }

    public String getHoverDelay() {
        return hoverDelay;
    }

    public int getHoverDelayTime(){
        try {
            return Integer.parseInt(hoverDelay);
        } catch (NumberFormatException e) { // ICE-4753
            return 500;
        }
    }

    public void setHoverDelay(String hoverDelay) {
        this.hoverDelay = hoverDelay;
    }

    public boolean getDraggable() {
        return draggable;
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    public String getDisplayOn() {
        return displayOn;
    }

    public void setDisplayOn(String displayOn) {
        this.displayOn = displayOn;
    }

    public boolean isMoveWithMouse() {
        return moveWithMouse;
    }

    public void setMoveWithMouse(boolean moveWithMouse) {
        this.moveWithMouse = moveWithMouse;
    }
}
