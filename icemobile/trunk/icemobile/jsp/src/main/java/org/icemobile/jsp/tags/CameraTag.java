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
import java.io.IOException;
import java.lang.Override;
import java.lang.String;
import javax.servlet.http.HttpSession;

import org.icemobile.component.IDevice;
import org.icemobile.util.ClientDescriptor;

public class CameraTag extends DeviceTag implements IDevice{

    public CameraTag()  {
        init();
    }

    public void setParams(String params){
        int width = getMaxwidth() ;
        int height = getMaxheight();
        if ( (width != Integer.MIN_VALUE) || (height != Integer.MIN_VALUE) ) {
            this.setParams ("','maxwidth=" + width +
            "&maxheight=" + height + "'");
        }
    }
    
    public void release(){
        super.release();
        init();
    }
    
    private void init(){
        this.command = "camera";
        this.buttonLabel = "Camera";
    }

}