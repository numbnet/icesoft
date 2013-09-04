/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
 
package org.icemobile.jsp.tags;
import org.icemobile.component.IDevice;

public class CamcorderTag extends DeviceTag implements IDevice{
    
    private int maxtime;

    public CamcorderTag()  {
        init();
    }
    public void setParams(String params) {
        int width = getMaxwidth() ;
        int height = getMaxheight();
        //default value of unset in params is Integer.MIN_VALUE
        //only commonality between iPhone and android is duration or maxTime
        //simplify this scripting when devices have this implemented and is final api
        int unset = Integer.MIN_VALUE;
        int numParams = 0;
        String attributeSeparator = "&";
        if (maxtime != unset || width != unset || height != unset) {
            params += ",'";
        }
        if (maxtime != unset) {
            if (numParams > 0) {
                params += attributeSeparator;
            }
            params += "maxtime=" + maxtime;
            numParams++;
        }
        if (width != Integer.MIN_VALUE) {
            if (numParams > 0) {
                params += attributeSeparator;
            }
            params += "maxwidth=" + width;
            numParams++;
        }
        if (height != Integer.MIN_VALUE) {
            if (numParams > 0) {
                params += attributeSeparator;
            }
            params += "maxheight=" + height;
            numParams++;
        }
        if (numParams > 0) {
            params += "'";
        }
    }

    public int getMaxtime() {
        return maxtime;
    }

    public void setMaxtime(int maxtime) {
        this.maxtime = maxtime;
    }
    
    public void release(){
        super.release();
        init();
    }
    
    private void init(){
        this.maxtime = Integer.MIN_VALUE;
        this.command = "camcorder";
        this.buttonLabel = "Camcorder";
    }
}