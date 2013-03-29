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

import org.icemobile.samples.spring.FileUploadUtils;
import org.icemobile.samples.spring.ModelBean;
import org.icemobile.spring.controller.ICEmobileBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Controller
@SessionAttributes({"camcorderBean","camcorderUploadReady","camcorderMessage","camcorderUpload"})
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
                             @RequestParam(value = "camvid", required = false) MultipartFile file,
                             Model model) throws IOException {
	    if( file != null || request.getParameter("camvid") != null ){
    	    String videoFilename = FileUploadUtils.saveVideo(request, "camvid", file, null);
            model.addAttribute("camcorderUploadReady", true);
    		model.addAttribute("camcorderMessage", "Hello " + modelBean.getName() +
                    ", your video was uploaded successfully.");
    		model.addAttribute("camcorderUpload", "./"+videoFilename);
	    }
	    model.addAttribute("camcorderBean",modelBean);
    }

}
