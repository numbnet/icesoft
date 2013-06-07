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

public class MicrophoneTag extends DeviceTag implements IDevice {
    
    private int maxtime;

    public MicrophoneTag()  {
        init();
    }
    public void setParams(String params) {
        int maxtime = getMaxtime();
        //default value of unset in params is Integer.MIN_VALUE
        //only commonality between iPhone and android is duration or maxTime
        //simplify this scripting when devices have this implemented and is final api
        int unset = Integer.MIN_VALUE;
        if (maxtime != unset ) {
            params += ",'";
            params += "maxtime=" + maxtime;
            params += "'";
        }
    }

    public int getMaxtime() {
        return maxtime;
    }

    public void setMaxtime(int maxtime) {
        this.maxtime = maxtime;
    }
    
    private void init(){
        maxtime=Integer.MIN_VALUE;
        this.command = "microphone";
        this.buttonLabel = "Record";
    }
    
    public void release(){
        super.release();
        init();
    }
}