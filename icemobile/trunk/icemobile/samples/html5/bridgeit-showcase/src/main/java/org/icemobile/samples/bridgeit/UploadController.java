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

import javax.servlet.http.HttpServletRequest;

import org.icemobile.application.Resource;
import org.icemobile.spring.annotation.ICEmobileResource;
import org.icemobile.spring.annotation.ICEmobileResourceStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"photos","videos"})
@ICEmobileResourceStore(bean="basicResourceStore")
public class UploadController {
    
    @ModelAttribute("photos")
    public List<String> createPhotoList() {
        return new ArrayList<String>();
    }
    
    @ModelAttribute("videos")
    public List<String> createVideoList() {
        return new ArrayList<String>();
    }
    
    @RequestMapping(value = "/camera-upload", method=RequestMethod.POST)
    public void cameraUpload(HttpServletRequest request, 
            @ICEmobileResource("camera") Resource cameraUpload, 
            @ModelAttribute("photos") List<String> photos) throws IOException {
        if( cameraUpload != null ){
            if (cameraUpload.getContentType().startsWith("image")) {
                try {
                    photos.add( "icemobile-store/"+ cameraUpload.getUuid() );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @RequestMapping(value="/photo-list", method=RequestMethod.GET, produces="application/json")
    public @ResponseBody List<String> getPhotoList(
            @RequestParam(value="since") long since,
            @RequestParam(value="_", required=false) String jqTimestamp,
            @ModelAttribute("photos") List<String> photos){

        return photos;
    }
    
    @RequestMapping(value = "/camcorder-upload", method=RequestMethod.POST)
    public void camcorderUpload(HttpServletRequest request, 
            @ICEmobileResource("camcorder") Resource camcorderUpload, 
            @ModelAttribute("videos") List<String> videos) throws IOException {
        if( camcorderUpload != null ){
            if (camcorderUpload.getContentType().startsWith("video")) {
                try {
                    videos.add( "icemobile-store/"+ camcorderUpload.getUuid() );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @RequestMapping(value="/video-list", method=RequestMethod.GET, produces="application/json")
    public @ResponseBody List<String> getVideoList(
            @RequestParam(value="since") long since,
            @RequestParam(value="_", required=false) String jqTimestamp,
            @ModelAttribute("videos") List<String> videos){

        return videos;
    }
}
