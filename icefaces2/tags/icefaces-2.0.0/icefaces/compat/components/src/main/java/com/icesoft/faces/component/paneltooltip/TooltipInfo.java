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

package com.icesoft.faces.component.paneltooltip;

import java.io.Serializable;


public class TooltipInfo implements Serializable {
    private String src = new String();
    private String state = "hide";
    private String x = "0px";
    private String y = "0px";
    private boolean eventFired;
    public TooltipInfo() {
    }
    
    public TooltipInfo(String info[]) {
        populateValues(info);
    }
    
    public void populateValues(String info[]) {
        String _src = info[1].split("=")[1];
        String _state = info[2].split("=")[1];
        if (!getState().equals(_state) || !getSrc().equals(_src)) {
    
            //change the x and y only on valueChangeEvent
            x = info[3].split("=")[1]+"px";
            y = info[4].split("=")[1]+"px";
            src = _src;
            state = _state;
            eventFired = true;
    
        }
    }
    public String getSrc() {
        return src;
    }
    
    public void setSrc(String src) {
        this.src = src;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    public String getX() {
        return x;
    }
    
    public void setX(String x) {
        this.x = x;
    }
    
    public String getY() {
        return y;
    }
    
    public void setY(String y) {
        this.y = y;
    }
    
    public boolean isEventFired() {
        return eventFired;
    }
    
    public void setEventFired(boolean eventFired) {
        this.eventFired = eventFired;
    }
}    
