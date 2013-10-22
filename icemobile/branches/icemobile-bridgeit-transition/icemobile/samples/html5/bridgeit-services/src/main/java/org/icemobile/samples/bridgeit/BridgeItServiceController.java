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
@SessionAttributes({"photos","videos","recordings", "arPhoto"})
@ICEmobileResourceStore(bean="basicResourceStore")
public class BridgeItServiceController {

    //private List<RealityMessage> arMessages = new ArrayList<RealityMessage>();
    
    @ModelAttribute("photos")
    public List<String> createPhotoList() {
        System.out.println("createPhotoList()");
        return new ArrayList<String>();
    }
    
    @ModelAttribute("videos")
    public List<String> createVideoList() {
        return new ArrayList<String>();
    }
    
    @ModelAttribute("recordings")
    public List<String> createAudioList() {
        return new ArrayList<String>();
    }
    
    @ModelAttribute("arPhoto")
    public String createARPhoto() {
        return new String();
    }
    
    @RequestMapping(value = "/camera-upload", method=RequestMethod.POST, produces="application/json")
    public @ResponseBody List<String> cameraUpload(HttpServletRequest request,
            @ICEmobileResource("cameraBtn") Resource cameraUpload,
            @ModelAttribute("photos") List<String> photos) throws IOException {
        System.out.println("entering /camera-upload");
        if( cameraUpload != null ){
            if (cameraUpload.getContentType().startsWith("image")) {
                try {
                    photos.add( "icemobile-store/"+ cameraUpload.getUuid() );
                    System.out.println("returning " + photos);
                    return photos;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    
    @RequestMapping(value = "/ar-upload", method=RequestMethod.POST, produces="application/json")
    public @ResponseBody String arUpload(HttpServletRequest request,
            @ICEmobileResource("arCameraBtn") Resource cameraUpload,
            @ModelAttribute("arPhoto") String arPhoto) throws IOException {
        System.out.println("entering /ar-upload");
        if( cameraUpload != null ){
            if (cameraUpload.getContentType().startsWith("image")) {
                try {
                    arPhoto = "service/icemobile-store/"+ cameraUpload.getUuid();
                    System.out.println("returning " + arPhoto);
                    return arPhoto;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @RequestMapping(value = "/audio-upload", method=RequestMethod.POST, produces="application/json")
    public @ResponseBody List<String> audioUpload(HttpServletRequest request, 
            @ICEmobileResource("micBtn") Resource audioUpload, 
            @ModelAttribute("recordings") List<String> recordings) throws IOException {
        if( audioUpload != null ){
            if (audioUpload.getContentType().startsWith("audio")) {
                try {
                    recordings.add( "icemobile-store/"+ audioUpload.getUuid() );
                    System.out.println("returning " + recordings);
                    return recordings;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    
    @RequestMapping(value = "/ar", method=RequestMethod.POST, produces="application/json")
    public void postAugmentedReality(HttpServletRequest request) throws IOException {
        System.out.println("postAugmentedReality()");
    }
        
    @RequestMapping(value="/photo-list", method=RequestMethod.GET, produces="application/json")
    public @ResponseBody List<String> getPhotoList(
            @RequestParam(value="since") long since,
            @RequestParam(value="_", required=false) String jqTimestamp,
            @ModelAttribute("photos") List<String> photos){

        return photos;
    }
    
    @RequestMapping(value = "/video-upload", method=RequestMethod.POST, produces="application/json")
    public @ResponseBody List<String> camcorderUpload(HttpServletRequest request, 
            @ICEmobileResource("camcorderBtn") Resource camcorderUpload, 
            @ModelAttribute("videos") List<String> videos) throws IOException {
        if( camcorderUpload != null ){
            if (camcorderUpload.getContentType().startsWith("video")) {
                try {
                    videos.add( "icemobile-store/"+ camcorderUpload.getUuid() );
                    System.out.println("returning " + videos);
                    return videos;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    
    @RequestMapping(value="/video-list", method=RequestMethod.GET, produces="application/json")
    public @ResponseBody List<String> getVideoList(
            @RequestParam(value="since") long since,
            @RequestParam(value="_", required=false) String jqTimestamp,
            @ModelAttribute("videos") List<String> videos){

        return videos;
    }

}
