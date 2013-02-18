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

public abstract class ICEmobileBaseController {
    
    @ModelAttribute
    public void ajaxAttribute(HttpServletRequest request, Model model) {
        model.addAttribute("ajaxRequest", AjaxUtils.isAjaxRequest(request));
    }
    
    @ModelAttribute
    public void viewSizeAttribute(HttpServletRequest request, Model model) {
        model.addAttribute("viewSize", ClientDescriptor.getInstance(request).isHandheldBrowser() ? "small" : "large");
    }
    
    @ModelAttribute
    public void sxRegistered(HttpServletRequest request, Model model){
        model.addAttribute("sxRegistered", ClientDescriptor.getInstance(request).isSXRegistered());
    }

    

}
