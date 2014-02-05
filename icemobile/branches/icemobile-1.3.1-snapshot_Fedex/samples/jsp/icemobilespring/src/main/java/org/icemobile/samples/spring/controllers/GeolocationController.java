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

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.icemobile.samples.spring.GeolocationBean;
import org.icemobile.spring.controller.ICEmobileBaseController;
import org.icepush.PushContext;
import org.icepush.PushNotification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.WebApplicationContext;

/** Spring MVC Controller for the Geolocation example. */
@Controller
@SessionAttributes({ "geolocationBean" })
public class GeolocationController extends ICEmobileBaseController {

    @Autowired
    private WebApplicationContext context;

    private ArrayList markers = new ArrayList();
    {
        //sample data for verification on startup
        markers.add(new String[]{
                "+51.07","-114.09","+1000"});
    }

    /** Create the geolocationBean.
     *  @param request The servlet request
     *  @return The newly created GeolocationBean
     */
    @ModelAttribute("geolocationBean")
    public GeolocationBean createGeolocationBean(HttpServletRequest request) {
        GeolocationBean bean = new GeolocationBean();
        return bean;
    }
    
    /**
     * Create the available enableHighPrecision types.
     * @return the list of types
     */
    @ModelAttribute("enableHighPrecisionTypes")
    public List<String> enableHighPrecisionTypes() {
        List<String> types = new ArrayList<String>();
        types.add("asNeeded");
        types.add("true");
        types.add("false");
        return types;
    }

    /** Handle the geolocation get request. 
     *  @param geolocationBean The geolocation bean
     */
    @RequestMapping(value = "/geolocation", method = RequestMethod.GET)
    public void get(Model model,
            @ModelAttribute("geolocationBean") GeolocationBean geolocationBean) {
        model.addAttribute("geolocationBean",geolocationBean);
    }
    
    /** Handle the geolocation post request. 
     *  @param geolocationBean The geolocation bean
     *  @param model The Spring model
     */
    @RequestMapping(value = "/geolocation", method = RequestMethod.POST)
    public void post(Model model,
            GeolocationBean geolocationBean) {
        model.addAttribute("geolocationBean", geolocationBean);
    }

    @RequestMapping(value = "/geospy", method = RequestMethod.POST)
    public @ResponseBody String geospy(
            HttpServletRequest request,
            @RequestParam(value = "_geo") String[] coordinates,
            Model model)  {

        PushContext pushContext = PushContext.getInstance(
                context.getServletContext() );
        pushContext.push("geospy");

        markers.add(coordinates);
        return "Thanks!";
    }

    @RequestMapping(value = "/geospymarkers", method = RequestMethod.GET)
    public @ResponseBody GeoMarkers geospyMarkers(
            HttpServletRequest request,
            Model model)  {

        GeoMarkers geoMarkers = new GeoMarkers();
        geoMarkers.markers = this.markers;
        return geoMarkers;
    }

}

class GeoMarkers {
    public ArrayList markers;
}
