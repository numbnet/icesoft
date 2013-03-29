/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
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
 *
 */

package org.icemobile.samples.springbasic;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * General Controller for echoing simple input pages
 */
@Controller
public class EchoController {

    @ModelAttribute
    public void ajaxAttribute(WebRequest request, Model model) {
        model.addAttribute("ajaxRequest", AjaxUtils.isAjaxRequest(request));
    }

    @RequestMapping(value = "/accordion")
    public void doRequest(
        @ModelAttribute("accordionBean") AccordionBean model) {
    }

    @RequestMapping(value = "/buttons")
    public void doRequest(Model model,
                          @RequestParam(value = "submitB", required = false)
                          String submitted) {
        if (submitted != null) {
            model.addAttribute("pressed", "[" + submitted + "]");
        }
    }

    @RequestMapping(value = "/carousel")
    public void doRequest(
        @ModelAttribute("carouselBean") CarouselBean model) {
    }

    @RequestMapping(value = "/datetime")
    public void doRequest(
        @ModelAttribute("dateTimeSpinnerBean")
        DateTimeSpinnerBean model) {
    }

    @RequestMapping(value = "/flipswitch")
    public void doRequest(
        @ModelAttribute("flipSwitchBean") FlipSwitchBean model) {
    }

    @RequestMapping(value = "/geolocation")
    public void doRequest(
        @ModelAttribute("geolocationBean") GeolocationBean model) {
    }

    @RequestMapping(value = "/inputtext")
    public void doRequest(
        @ModelAttribute("inputTextBean") InputTextBean model) {
    }

    @RequestMapping(value = "/list")
    public void doRequest(@ModelAttribute("listBean") ListBean model) {
    }

    @RequestMapping(value = "/tabset")
    public void doRequest(@ModelAttribute("tabsetBean") TabsetBean model) {
    }

    @RequestMapping(value = "/panelPopup")
    public void doRequest(
        @ModelAttribute("panelPopupBean") PanelPopupBean model) {
    }

}

