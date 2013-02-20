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

package org.icemobile.spring.controller;

import javax.servlet.http.HttpServletRequest;

import org.icemobile.util.AjaxUtils;
import org.icemobile.util.ClientDescriptor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * A base ICEmobile controller to populate some commonly used 
 * model attributes for an ICEmobile page.
 *
 */
public abstract class ICEmobileBaseController {
    
    /**
     * Add the attribute 'ajaxRequest' to the Model if the current request
     * is an Ajax request. This attribute can be used on pages to modify
     * the response for Ajax.
     */
    @ModelAttribute
    public void ajaxAttribute(HttpServletRequest request, Model model) {
        model.addAttribute("ajaxRequest", AjaxUtils.isAjaxRequest(request));
    }
    
    /**
     * Add the attribute 'viewSize' to the Model. 'small' for handheld
     * devices and 'large' for tablets or desktops. The attribute can
     * be used on pages to adapt content to type of device.
     */
    @ModelAttribute
    public void viewSizeAttribute(HttpServletRequest request, Model model) {
        model.addAttribute("viewSize", 
                ClientDescriptor.getInstance(request)
                    .isHandheldBrowser() ? "small" : "large");
    }
    
    /**
     * Add the attribute 'sxRegistered' to the Model. The attribute can be
     * used to detect if the ICEmobile SX app has been registered yet for 
     * the session.
     */
    @ModelAttribute
    public void sxRegistered(HttpServletRequest request, Model model){
        model.addAttribute("sxRegistered", 
                ClientDescriptor.getInstance(request).isSXRegistered());
    }

    

}
