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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.icemobile.application.Resource;
import org.icemobile.spring.annotation.ICEmobileResource;
import org.icemobile.spring.annotation.ICEmobileResourceStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@ICEmobileResourceStore(bean="basicResourceStore")
public class BridgeItServiceController {

    private static final Log LOG = LogFactory.getLog(BridgeItServiceController.class);

    
    private String getBaseURL(HttpServletRequest request){
        return request.getScheme() + "://" + request.getServerName() 
            + ":" + request.getServerPort() + request.getContextPath() + "/";
    }
    
    @RequestMapping(value = "/camera-upload", method=RequestMethod.POST, produces="application/json")
    public @ResponseBody String cameraUpload(HttpServletRequest request,
            @ICEmobileResource("cameraBtn") Resource cameraUpload) throws IOException {
        if( cameraUpload != null ){
            if (cameraUpload.getContentType().startsWith("image")) {
                try {
                    String retval = getBaseURL(request) + "store/"+ cameraUpload.getUuid();
                    LOG.debug("uploaded image: " + retval);
                    return retval;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @RequestMapping(value = "/audio-upload", method=RequestMethod.POST, produces="application/json")
    public @ResponseBody String audioUpload(HttpServletRequest request, 
            @ICEmobileResource("micBtn") Resource audioUpload) throws IOException {
        if( audioUpload != null ){
            if (audioUpload.getContentType().startsWith("audio")) {
                try {
                    String retval = getBaseURL(request) + "store/"+ audioUpload.getUuid();
                    LOG.debug("uploade audio: " + retval);
                    return retval;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    
        
    @RequestMapping(value = "/video-upload", method=RequestMethod.POST, produces="application/json")
    public @ResponseBody String camcorderUpload(HttpServletRequest request, 
            @ICEmobileResource("camcorderBtn") Resource camcorderUpload) throws IOException {
        if( camcorderUpload != null ){
            if (camcorderUpload.getContentType().startsWith("video")) {
                try {
                    String retval = getBaseURL(request) + "store/"+ camcorderUpload.getUuid();
                    LOG.debug("uploaded video: " + retval);
                    return retval;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
