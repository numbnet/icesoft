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

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.icemobile.samples.spring.FileUploadUtils;
import org.icemobile.samples.spring.ModelBean;
import org.icemobile.spring.controller.ICEmobileBaseController;

import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

@Controller
@SessionAttributes({"cameraMessage", "cameraUpload", "cameraBean"})
public class CameraController extends ICEmobileBaseController{
    
    private static final Log log = LogFactory
            .getLog(CameraController.class);


    @RequestMapping(value = "/camera", method = RequestMethod.GET)
    public void get(HttpServletRequest request, ModelBean cameraBean, Model model) {
        model.addAttribute("cameraBean",cameraBean);
    }

    @ModelAttribute("cameraBean")
    public ModelBean createBean() {
        return new ModelBean();
    }

    @RequestMapping(value = "/camera", method = RequestMethod.POST, 
            consumes = "application/x-www-form-urlencoded")
    public void formPost(
            HttpServletRequest request, 
            ModelBean cameraBean,
            Model model) {
        model.addAttribute("cameraBean",cameraBean);
    }

    @RequestMapping(value = "/camera", method = RequestMethod.POST)
    public void post(
            HttpServletRequest request, 
            ModelBean cameraBean,
            @RequestParam(value = "cam", required = false) MultipartFile file,
            Model model) {
       if( file != null || request.getParameterMap().get("cam") != null ){
            processUpload(request,cameraBean,file,model);
        }
        model.addAttribute("cameraBean",cameraBean);
    }

    public void processUpload(
            HttpServletRequest request, 
            ModelBean cameraBean,
            MultipartFile file,
            Model model) {
        log.info("processUpload() "+ cameraBean);
        String newFileName;
        try {
            newFileName = FileUploadUtils.saveImage(request, "cam", file, null);
            model.addAttribute("cameraMessage", "Hello " + cameraBean.getName() 
                    + ", your file was uploaded successfully.");
            if (null != newFileName) {
                model.addAttribute("cameraUpload", newFileName);
            } 
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("cameraMessage","There was an error uploading the image.");
        }
    }

    @RequestMapping(value = "/jsoncam", method = RequestMethod.POST)
    public
    @ResponseBody
    CamUpdate jsonCamera(HttpServletRequest request, ModelBean modelBean,
                         @RequestParam(value = "camera-file", required = false) MultipartFile file,
                         @RequestParam(value = "cam", required = false) MultipartFile inputFile,
                         Model model) throws IOException {

        String newFileName = FileUploadUtils.saveImage(request, "cam", file, inputFile);
        
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

        return new CamUpdate( "Thanks for the photo, " + modelBean.getName() +
                " and your interest in [" + imcheck +
                "] ICEmobile and [" + jqcheck + "] jquery.", 
                request.getContextPath() + "/" + newFileName );
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
