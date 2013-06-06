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

package org.icemobile.samples.spring.controllers;

import org.icemobile.samples.spring.AccordionBean;
import org.icemobile.samples.spring.AudioBean;
import org.icemobile.samples.spring.ButtonsBean;
import org.icemobile.samples.spring.CarouselBean;
import org.icemobile.samples.spring.DateTimeSpinnerBean;
import org.icemobile.samples.spring.FlipSwitchBean;
import org.icemobile.samples.spring.InputTextBean;
import org.icemobile.samples.spring.ListBean;
import org.icemobile.samples.spring.MenuButtonBean;
import org.icemobile.samples.spring.PanelConfirmationBean;
import org.icemobile.samples.spring.PanelPopupBean;
import org.icemobile.samples.spring.TabsetBean;
import org.icemobile.samples.spring.VideoBean;
import org.icemobile.spring.annotation.ICEmobileResourceStore;
import org.icemobile.spring.controller.ICEmobileBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * General Controller for echoing simple input pages
 */
@Controller
@SessionAttributes({"geolocationBean", "carouselBean", "panelConfirmationBean", "buttonsBean",
    "panelPopupBean"})
@ICEmobileResourceStore(bean="icemobileResourceStore")
public class EchoController extends ICEmobileBaseController{

    @RequestMapping(value = "/menu")
    public void doMenuRequest() {
    }
    
    @RequestMapping(value = "/accordion")
    public void doRequest(
        @ModelAttribute("accordionBean") AccordionBean model) {
    }

    @RequestMapping(value = "/buttons")
    public void doRequest(
        @ModelAttribute("buttonsBean")ButtonsBean model) {
    }

    @RequestMapping(value = "/carousel")
    public void doRequest(
        @ModelAttribute("carouselBean") CarouselBean model) {
    }
    
    @ModelAttribute("carouselBean")
    public CarouselBean createBean() {
        return new CarouselBean();
    }

    @RequestMapping(value = "/datetime")
    public void doRequest(
        @ModelAttribute("dateTimeSpinnerBean")
        DateTimeSpinnerBean model) {
    }
    
    @RequestMapping(value = "/fieldset")
    public void doFieldsetRequest() {
    }

    @RequestMapping(value = "/flipswitch")
    public void doRequest(
        @ModelAttribute("flipSwitchBean") FlipSwitchBean model) {
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
    public void doRequest(@ModelAttribute("panelPopupBean") PanelPopupBean model) {
    }
    
    @RequestMapping(value = "/panelconfirmation")
    public void doRequest(@ModelAttribute("panelConfirmationBean") PanelConfirmationBean model) {
    }
    
    @RequestMapping(value = "/menubutton")
    public void doRequest(@ModelAttribute("menuButtonBean") MenuButtonBean model) {
    }
    
    @RequestMapping(value = "/audioplayer")
    public void doRequest(@ModelAttribute("audioBean") AudioBean model) {
    }
    
    @RequestMapping(value = "/videoplayer")
    public void doRequest(@ModelAttribute("videoBean") VideoBean model) {
    }
    
    @ModelAttribute("panelConfirmationBean")
    public PanelConfirmationBean createPanelConfirmationBean() {
        return new PanelConfirmationBean();
    }
    
    @ModelAttribute("buttonsBean")
    public ButtonsBean createButtonsBean() {
        return new ButtonsBean();
    }
    
    @ModelAttribute("panelPopupBean")
    public PanelPopupBean createPopupBean() {
        return new PanelPopupBean();
    }

}

