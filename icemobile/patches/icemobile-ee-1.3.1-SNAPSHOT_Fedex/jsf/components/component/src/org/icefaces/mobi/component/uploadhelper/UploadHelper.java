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

package org.icefaces.mobi.component.uploadhelper;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.icefaces.impl.event.FormSubmit;
import org.icefaces.mobi.utils.MobiJSFUtils;
import org.icefaces.util.EnvUtils;
import org.icemobile.util.ClientDescriptor;

public class UploadHelper extends UploadHelperBase {

    public UploadHelper() {
        super();
    }


    public void setInView(boolean inView) {
        super.setInView(inView);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        boolean isEnhanced = EnvUtils.isEnhancedBrowser(facesContext);
        boolean isAuxUpload = EnvUtils.isAuxUploadBrowser(facesContext);
        if (isEnhanced || isAuxUpload) {
            return;
        }
        if (!browserRequiresFullSubmit())  {
            return;
        }
        //the desktop case becomes non-ajax and multipart
        if (inView) {
            UIForm theForm = getContainingForm(getParent());
            if (null != theForm) {
                theForm.getAttributes().put(
                        FormSubmit.DISABLE_CAPTURE_SUBMIT, "true");
                theForm.getAttributes().put(
                        "enctype", "multipart/form-data");
            }
        }
    }

    //Should be in a Util class
    public static UIForm getContainingForm(UIComponent component) {
        if (component instanceof UIForm) {
            return (UIForm) component;
        }
        UIComponent parent = component.getParent();
        while ((!(parent instanceof UIForm)) &&
                (!(parent instanceof UIViewRoot))) {
            if (null == parent) {
                return null;
            }
            parent = parent.getParent();
        }
        if (parent instanceof UIForm) {
            return (UIForm) parent;
        }
        return null;
    }

    boolean browserRequiresFullSubmit()  {
        ClientDescriptor client = MobiJSFUtils.getClientDescriptor();
        return (client.isIEBrowser() 
                || ( client.isAndroidOS() && client.isTabletBrowser()) 
                || client.isBlackBerryOS());
    }

}
