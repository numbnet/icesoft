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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.icemobile.application.Resource;
import org.icemobile.samples.spring.MediaSpotBean;
import org.icemobile.spring.annotation.ICEmobileResource;
import org.icemobile.spring.annotation.ICEmobileResourceStore;
import org.icemobile.spring.controller.ICEmobileBaseController;
import org.icemobile.util.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"augmentedRealityMessage", "augmentedRealityUpload", "mediaspotBean"})
@ICEmobileResourceStore(bean="arResourceStore")
public class MediaSpotController extends ICEmobileBaseController{
    HashMap<String,MediaSpotBean> messages = new HashMap<String,MediaSpotBean>();
    MediaSpotBean selectedMessage = null;
    int count = 0;
    String currentFileName = null;
    
    private static final Log log = LogFactory
            .getLog(MediaSpotController.class);

    private List<Map<String,String>> getMarkerList(HttpServletRequest request)  {

        String urlBase = Utils.getBaseURL(request);

        List<Map<String,String>> markerList = new ArrayList<Map<String,String>>();
        Map<String,String> marker;

        marker = new HashMap<String,String>();
        marker.put("label", "icemobile");
        marker.put("model", urlBase + "/resources/3d/icemobile.obj" );
        markerList.add(marker);

        marker = new HashMap<String,String>();
        marker.put("label", "puz1");
        marker.put("model", urlBase + "/resources/3d/puz1.obj" );
        markerList.add(marker);

        marker = new HashMap<String,String>();
        marker.put("label", "puz2");
        marker.put("model", urlBase + "/resources/3d/puz2.obj" );
        markerList.add(marker);

        return markerList;
    }

	@ModelAttribute("mediaspotBean")
    public MediaSpotBean createBean() {
        return new MediaSpotBean();
    }
    
    @RequestMapping(value = "/mediaspot", method=RequestMethod.GET)
    public void processGet(HttpServletRequest request, Model model)  {
		model.addAttribute("locations", messages.values());
		model.addAttribute("markers", getMarkerList(request));
        if (null != selectedMessage) {
            model.addAttribute("selection", selectedMessage.getTitle());
            model.addAttribute("imgPath", 
                    selectedMessage.getFileName());
        }
    }

    @RequestMapping(value = "/mediaspot", method = RequestMethod.POST, 
            consumes = "application/x-www-form-urlencoded")
    public void formPost(
            HttpServletRequest request, 
            Model model) {
        processGet(request, model);
    }

    @RequestMapping(value = "/mediaspot", method=RequestMethod.POST)
    public void post(HttpServletRequest request, 
            @ICEmobileResource("spotcam") Resource photoUpload,
            MediaSpotBean spotBean,
            Model model){
        log.info("post(): " + spotBean);
        
        if( photoUpload != null ){
            processPhotoUpload(request,photoUpload,spotBean,model);
        }
        else{
            processMarkerSubmit(request, model);
        }
    }

	public void processPhotoUpload(HttpServletRequest request, 
            Resource photoUpload,
            MediaSpotBean spotBean,
            Model model)  {
	    log.info("postPhotoUpload() photoUpload="+photoUpload);
        try {
            String photoUrl = null;
            if (null != photoUpload )  {
                photoUrl = "icemobile-ar-store/"+ photoUpload.getUuid();
                spotBean.setFileName(photoUrl);
                String title = spotBean.getTitle();
                if ((null == title) || "".equals(title))  {
                    spotBean.setTitle("Marker" + count++);
                }
                messages.put(spotBean.getTitle(), spotBean);
            }
    		model.addAttribute("locations", messages.values());
    		model.addAttribute("augmentedRealityMessage", "Hello your file was uploaded successfully, you may now enter augmented reality.");
    		model.addAttribute("augmentedRealityUpload", photoUrl);
            String selection = spotBean.getSelection();
            MediaSpotBean mySelectedMessage = messages.get(selection);
            if (null != mySelectedMessage) {
                selectedMessage = mySelectedMessage;
            }
            if (null != selectedMessage) {
                model.addAttribute("selection", selectedMessage.getTitle());
                model.addAttribute("imgPath", 
                        selectedMessage.getFileName());
            }
        } catch (Exception e) {
            model.addAttribute("augmentedRealityMessage", "Sorry, there was a problem processing the image upload.");
            e.printStackTrace();
        }
    }
	
	private void processMarkerSubmit(HttpServletRequest request, Model model)  {
	    log.info("processMarkerSubmit()");
		model.addAttribute("locations", messages.values());
		model.addAttribute("markers", getMarkerList(request));
        if (null != selectedMessage) {
            model.addAttribute("selection", selectedMessage.getTitle());
            model.addAttribute("imgPath", 
                    selectedMessage.getFileName());
        }
    }
    

}
