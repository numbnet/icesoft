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
package org.icefaces.mobi.component.panelpopup;

import javax.faces.context.FacesContext;

import org.icefaces.mobi.utils.MobiJSFUtils;
import org.icemobile.component.IPanelPopup;
import org.icemobile.util.ClientDescriptor;

public class PanelPopup extends PanelPopupBase implements IPanelPopup {

    // CSS class names used by the component renderer.
 /*   public static final String BLACKOUT_PNL_HIDDEN_CLASS = "mobi-panelpopup-bg-hide ";
    public static final String BLACKOUT_PNL_CLASS = "mobi-panelpopup-bg ";
    public static final String CONTAINER_CLASS = "mobi-panelpopup-container ";
    public static final String HIDDEN_CONTAINER_CLASS = "mobi-panelpopup-container-hide ";  */

 //   public static final String TITLE_CLASS = "mobi-panelpopup-title-container ";
  //  public static final String TITLE_CLASS = "mobi-panelpopup-title-container ";

    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }
     public ClientDescriptor getClient() {
        return MobiJSFUtils.getClientDescriptor();
    }

}
