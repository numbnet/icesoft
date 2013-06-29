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

import java.io.IOException;

import org.icemobile.application.Resource;
import org.icemobile.samples.spring.ModelBean;
import org.icemobile.spring.annotation.ICEmobileResource;
import org.icemobile.spring.annotation.ICEmobileResourceStore;
import org.icemobile.spring.controller.ICEmobileBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@SessionAttributes({"camcorderBean","camcorderUploadReady","camcorderMessage","camcorderUpload"})
@ICEmobileResourceStore(bean="basicResourceStore")
public class CamcorderController extends ICEmobileBaseController{

	@ModelAttribute("camcorderBean")
	public ModelBean createBean() {
		return new ModelBean();
	}

	@RequestMapping(value = "/camcorder", method=RequestMethod.GET)
    public void get(ModelBean camcorderBean, Model model)  {
    }

    @RequestMapping(value = "/camcorder", method = RequestMethod.POST, 
            consumes = "application/x-www-form-urlencoded")
    public void formPost(
            HttpServletRequest request, 
            ModelBean modelBean,
            Model model) {
	    model.addAttribute("camcorderBean",modelBean);
    }

	@RequestMapping(value = "/camcorder", method=RequestMethod.POST)
	public void processVideo(HttpServletRequest request, ModelBean modelBean,
	        @ICEmobileResource("video") Resource videoUpload, Model model) throws IOException {
	    if( videoUpload != null ){
	     // if the uploaded file is not a video display an error
            if (!videoUpload.getContentType().startsWith("video")) {
                model.addAttribute("camcorderError",
                        "Sorry " + modelBean.getName()
                                + ", only video uploads are allowed.");
            } else {
                try {
                    String videoUrl = "icemobile-store/"+ videoUpload.getUuid();
                    model.addAttribute("camcorderUpload", videoUrl);
                    model.addAttribute("camcorderUploadReady", true);
                    // tell the user their photo has been uploaded
                    model.addAttribute("camcorderMessage",
                            "Hello " + modelBean.getName()
                                    + ", your video was uploaded successfully.");
                } catch (Exception e) {
                    //there was some problem opening the uploaded file or 
                    //creating a new one for the media dir
                    model.addAttribute("camcorderError",
                            "Sorry " + modelBean.getName()
                                    + ", there was a problem saving the image file.");
                }
            }
	    }
	    model.addAttribute("camcorderBean",modelBean);
    }

}
