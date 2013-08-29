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

package org.icefaces.mobi.component.getenhanced;

import javax.faces.context.FacesContext;

import org.icefaces.mobi.utils.MobiJSFUtils;
import org.icemobile.component.IGetEnhanced;
import org.icemobile.util.ClientDescriptor;
import org.icemobile.util.Constants;

public class GetEnhanced extends GetEnhancedBase implements IGetEnhanced {

    public String getICEmobileRegisterSXScript() {
        return MobiJSFUtils.getICEmobileRegisterSXScript();
    }

    public ClientDescriptor getClient() {
        return MobiJSFUtils.getClientDescriptor();
    }

    public boolean isIOSSmartBannerRendered() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return Boolean.TRUE.equals(facesContext.getAttributes()
                .put(Constants.IOS_SMART_APP_BANNER_KEY, Boolean.TRUE));
    }

}
