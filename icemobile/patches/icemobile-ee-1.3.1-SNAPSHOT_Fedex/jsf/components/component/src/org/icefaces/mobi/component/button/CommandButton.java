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

package org.icefaces.mobi.component.button;


import org.icefaces.mobi.component.onlinestatus.OnlineStatusListener;
import org.icefaces.mobi.utils.MobiJSFUtils;
import org.icemobile.component.IButton;
import org.icemobile.util.ClientDescriptor;


public class CommandButton extends CommandButtonBase implements IButton, OnlineStatusListener{

    private boolean parentDisabled = false;
    public ClientDescriptor getClient() {
         return MobiJSFUtils.getClientDescriptor();
    }

    public boolean isParentDisabled(){
        return false;// not handled for JSF in javascript
    }

    public void setParentDisabled(boolean disabled){
        this.parentDisabled = disabled;
    }

}
