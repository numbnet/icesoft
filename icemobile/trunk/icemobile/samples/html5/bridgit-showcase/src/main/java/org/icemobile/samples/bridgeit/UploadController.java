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
package org.icemobile.samples.bridgeit;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.icemobile.application.Resource;
import org.icemobile.spring.annotation.ICEmobileResource;
import org.icemobile.spring.annotation.ICEmobileResourceStore;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"photos"})
@ICEmobileResourceStore(bean="basicResourceStore")
public class UploadController {
    
    @RequestMapping(value = "/camcorder", method=RequestMethod.POST)
    public void cameraUpload(HttpServletRequest request, @ICEmobileResource("camera") Resource cameraUpload, Model model) throws IOException {
        if( cameraUpload != null ){
         // if the uploaded file is not a video display an error
            if (cameraUpload.getContentType().startsWith("image")) {
                try {
                    String photoUrl = "icemobile-store/"+ cameraUpload.getUuid();
                    model.addAttribute("cameraUpload", photoUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
