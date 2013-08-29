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
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.icemobile.application.Resource;
import org.icemobile.samples.spring.FileUploadUtils;
import org.icemobile.samples.spring.ModelBean;
import org.icemobile.spring.annotation.ICEmobileResource;
import org.icemobile.spring.annotation.ICEmobileResourceStore;
import org.icemobile.spring.controller.ICEmobileBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

@Controller
@SessionAttributes({"microphoneBean","micUploadReady","micMessage","micUpload"})
@ICEmobileResourceStore(bean="basicResourceStore")
public class MicrophoneController extends ICEmobileBaseController{
    
    private static final Log log = LogFactory
            .getLog(MicrophoneController.class);

	@ModelAttribute("microphoneBean")
	public ModelBean createBean() {
		return new ModelBean();
	}

	@RequestMapping(value = "/jqmmic", method=RequestMethod.GET)
    public void processJqmmic()  {
    }
    
	@RequestMapping(value = "/jqmmic", method=RequestMethod.POST)
	public void processJqmmic(HttpServletRequest request, ModelBean modelBean, 
	        @ICEmobileResource("mic") Resource audioUpload, Model model) throws IOException {
        this.processMicrophone(request, modelBean, audioUpload, model);
    }

	@RequestMapping(value = "/microphone", method=RequestMethod.GET)
    public void processMicrophone(Model model)  {
    }

    @RequestMapping(value = "/microphone", method = RequestMethod.POST, 
            consumes = "application/x-www-form-urlencoded")
    public void formPost(
            HttpServletRequest request, 
            ModelBean microphoneBean,
            Model model) {
        model.addAttribute("microphoneBean",microphoneBean);
    }

	@RequestMapping(value = "/microphone", method=RequestMethod.POST)
	public void processMicrophone(HttpServletRequest request, ModelBean microphoneBean, 
	        @ICEmobileResource("mic") Resource audioUpload, 
            Model model){
	    
	    if( audioUpload != null ){
	     // if the uploaded file is not an audio file display an error
            if (!audioUpload.getContentType().startsWith("audio")) {
                model.addAttribute("micError",
                        "Sorry " + microphoneBean.getName()
                                + ", only audio uploads are allowed.");
            } else {
                try {
                    String videoUrl = "icemobile-store/"+ audioUpload.getUuid();
                    model.addAttribute("micUpload", videoUrl);
                    model.addAttribute("micUploadReady", true);
                    // tell the user their photo has been uploaded
                    model.addAttribute("micMessage",
                            "Hello " + microphoneBean.getName()
                                    + ", your recording was uploaded successfully.");
                } catch (Exception e) {
                    //there was some problem opening the uploaded file or 
                    //creating a new one for the media dir
                    model.addAttribute("micError",
                            "Sorry " + microphoneBean.getName()
                                    + ", there was a problem saving the image file.");
                }
            }
        }
        model.addAttribute("microphoneBean",microphoneBean);

    }


	@RequestMapping(value = "/jsonmic", method=RequestMethod.POST)
	public @ResponseBody MicUpdate jsonMicrophone(HttpServletRequest request, ModelBean modelBean, @RequestParam(value = "mic", required = false) MultipartFile file, Model model) throws IOException {

        String newFileName = FileUploadUtils.saveImage(request, "mic", file, null);

        Map additionalParams = modelBean.getAdditionalInfo();
        String imcheck = " ";
        String jqcheck = " ";
        if (null != additionalParams)  {
            if (additionalParams.keySet().contains("icemobile"))  {
                imcheck = "*";
            }
            if (additionalParams.keySet().contains("jquery"))  {
                jqcheck = "*";
            }
        }
        return new MicUpdate( "Thanks for the sound, " + modelBean.getName() +
                " and your interest in [" + imcheck +
                "] ICEmobile and [" + jqcheck + "] jquery.", 
                request.getContextPath() + "/" + newFileName );
    }

}

class MicUpdate  {
    private String path;
    private String message;

    public MicUpdate( String message, String path)  {
        this.path = path;
        this.message = message;
    }

    public String getPath()  {
        return path;
    }

    public String getMessage()  {
        return message;
    }
}
