/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.icemobile.samples.mobileshowcase.view.components;

/**
 * The View selector component test for small/large view detection.
 */

import org.icefaces.component.utils.Utils;
import org.icemobile.samples.mobileshowcase.util.FacesUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Map;

@ManagedBean
@SessionScoped
public class ViewSelector implements Serializable{

    private boolean smallView;
    private boolean largeView;

    public ViewSelector(){
        FacesContext fc = FacesContext.getCurrentInstance();
        Utils.DeviceType deviceType  = Utils.getDeviceType(fc);
        if (deviceType.equals(Utils.DeviceType.ipad) ||
                deviceType.equals(Utils.DeviceType.honeycomb)){
            largeView = true;
        }else{
            smallView = true;
        }
    }

    public boolean isSmallView(){
        return smallView;
    }

    public boolean isLargeView(){
        return largeView;
    }
}
