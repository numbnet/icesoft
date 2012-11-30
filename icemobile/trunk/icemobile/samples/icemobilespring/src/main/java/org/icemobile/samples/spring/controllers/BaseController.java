package org.icemobile.samples.spring.controllers;

import javax.servlet.http.HttpServletRequest;

import org.icemobile.samples.spring.AjaxUtils;
import org.icemobile.util.ClientDescriptor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.WebRequest;

public abstract class BaseController {
    
    @ModelAttribute
    public void ajaxAttribute(WebRequest request, Model model) {
        model.addAttribute("ajaxRequest", AjaxUtils.isAjaxRequest(request));
    }
    
    @ModelAttribute
    public void viewSizeAttribute(HttpServletRequest request, Model model) {
        model.addAttribute("viewSize", ClientDescriptor.getInstance(request).isHandheldBrowser() ? "small" : "large");
    }
    
    @ModelAttribute
    public void iosAttribute(HttpServletRequest request, Model model) {
        model.addAttribute("ios", ClientDescriptor.getInstance(request).isIOS());
    }

    

}
