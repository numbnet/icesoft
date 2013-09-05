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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
public class AugmentedRealityController {

    private List<RealityMessage> arMessages = new ArrayList<RealityMessage>();
    

    @RequestMapping(value = "/post-ar-message", method=RequestMethod.POST, produces="application/json")
    public @ResponseBody List<RealityMessage> postARMessage(WebRequest webRequest) throws IOException {
        String title = webRequest.getParameter("title");
        String photo = webRequest.getParameter("photo");
        String lat = webRequest.getParameter("lat");
        String lng = webRequest.getParameter("lng");
        System.out.println("entering /post-ar-message title="+ title 
            + ", photo="+ photo + ", lat=" + lat + ", lng=" + lng);
        if( title != null && photo != null && lat != null && lng != null ){
            try{
                float latitude = Float.parseFloat(lat);
                float longitude = Float.parseFloat(lng);
                RealityMessage msg = new RealityMessage();
                msg.setFileName(photo);
                msg.setTitle(title);
                msg.setLocation(latitude, longitude);
                arMessages.add(msg);
                System.out.println("returning " + arMessages);
                return arMessages;
            }
            catch(NumberFormatException nfe){
                System.out.println("NumberFormatException: " + nfe.getMessage());
                nfe.printStackTrace();
            }
        }
        return null;
    }
    
    @RequestMapping(value = "/ar-messages", method=RequestMethod.GET, produces="application/json")
    public @ResponseBody List<RealityMessage> getARMessages(WebRequest webRequest) throws IOException {
         return arMessages;
    }
    
    @RequestMapping(value = "/echo", method=RequestMethod.POST, produces="application/json")
    public @ResponseBody Map postEcho(HttpServletRequest request) throws IOException {
        return request.getParameterMap();
    }
}

