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
import org.icemobile.samples.spring.ModelBean;
import org.icemobile.samples.spring.FileUploadUtils;
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
@SessionAttributes({ "cameraMessage", "cameraUpload", "cameraBean" })
@ICEmobileResourceStore(bean="basicResourceStore")
public class CameraController extends ICEmobileBaseController {

    private static final Log LOG = LogFactory.getLog(CameraController.class);

    @RequestMapping(value = "/camera", method = RequestMethod.GET)
    public void get(HttpServletRequest request, ModelBean cameraBean,
            Model model) {
        model.addAttribute("cameraBean", cameraBean);
    }

    @ModelAttribute("cameraBean")
    public ModelBean createBean() {
        return new ModelBean();
    }

@RequestMapping(value = "/camera", method = RequestMethod.POST)
public void post(HttpServletRequest request, ModelBean cameraBean,
        @ICEmobileResource("cam") Resource cameraUpload,
        Model model) {
    
    LOG.info("cameraUpload: " + cameraUpload);

    if (cameraUpload != null) {
        // if the uploaded file is not an image display an error
        if (!cameraUpload.getContentType().startsWith("image")) {
            model.addAttribute("cameraError",
                    "Sorry " + cameraBean.getName()
                            + ", only image uploads are allowed.");
        } else {
            try {
                String photoUrl = "icemobile-store/"+ cameraUpload.getUuid();
                model.addAttribute("cameraUpload", photoUrl);

                // tell the user their photo has been uploaded
                model.addAttribute("cameraMessage",
                        "Hello " + cameraBean.getName()
                                + ", your file was uploaded successfully.");
            } catch (Exception e) {
                //there was some problem opening the uploaded file or 
                //creating a new one for the media dir
                model.addAttribute("cameraError",
                        "Sorry " + cameraBean.getName()
                                + ", there was a problem saving the image file.");
            }
        }
    }
    // always add the bean back to the model to save other form data
    model.addAttribute("cameraBean", cameraBean);
}

    @RequestMapping(value = "/html5cam", method = RequestMethod.POST)
    public @ResponseBody CamUpdate html5Camera(
            HttpServletRequest request,
            @RequestParam(value = "_cam") MultipartFile cameraUpload,
            Model model) throws IOException {

            String newFileName = FileUploadUtils.saveImage(
                request, "cam", cameraUpload, null);
            String photoUrl = request.getContextPath() + "/" + newFileName;

        return new CamUpdate("Thanks!", photoUrl);
    }

    @RequestMapping(value = "/jsoncam", method = RequestMethod.POST)
    public @ResponseBody CamUpdate jsonCamera(
            HttpServletRequest request,
            ModelBean cameraBean,
            @ICEmobileResource("cam") Resource cameraUpload,
            Model model) throws IOException {

        if (cameraUpload != null) {
            // if the uploaded file is not an image display an error
            if (!cameraUpload.getContentType().startsWith("image")) {
                model.addAttribute("cameraError",
                        "Sorry " + cameraBean.getName()
                                + ", only image uploads are allowed.");
            } else {

                String photoUrl = "icemobile-store/"+ cameraUpload.getUuid();
                model.addAttribute("cameraUpload", photoUrl);

                // tell the user their photo has been uploaded
                model.addAttribute("cameraMessage",
                        "Hello " + cameraBean.getName()
                                + ", your file was uploaded successfully.");
                
                Map<String,String> additionalParams = cameraBean.getAdditionalInfo();
                String imcheck = " ";
                String jqcheck = " ";
                if (null != additionalParams) {
                    if (additionalParams.keySet().contains("icemobile")) {
                        imcheck = "*";
                    }
                    if (additionalParams.keySet().contains("jquery")) {
                        jqcheck = "*";
                    }
                }
                
                return new CamUpdate("Thanks for the photo, " + cameraBean.getName()
                        + " and your interest in [" + imcheck + "] ICEmobile and ["
                        + jqcheck + "] jquery.", request.getContextPath() + "/"
                        + photoUrl);
            }
        } 
        else{
          //there was some problem with the upload
            model.addAttribute("cameraError",
                    "Sorry " + cameraBean.getName()
                            + ", there was a problem saving the image file.");
        }
        // always add the bean back to the model to save other form data
        model.addAttribute("cameraBean", cameraBean);
        
        return null;
    }
}

class CamUpdate {
    private String path;
    private String message;

    public CamUpdate(String message, String path) {
        this.path = path;
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public String getMessage() {
        return message;
    }
}
