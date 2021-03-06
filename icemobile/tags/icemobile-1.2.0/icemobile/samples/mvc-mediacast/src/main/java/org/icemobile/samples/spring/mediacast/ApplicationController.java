/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
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
package org.icemobile.samples.spring.mediacast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.icemobile.jsp.tags.TagUtil;
import org.icemobile.util.SXUtils;
import org.icepush.PushContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

@Controller
@SessionAttributes(value = {"desktop", "sxThumbnail", "enhanced",
        ApplicationController.SX_VIDEO_UPLOAD_KEY,
        ApplicationController.SX_AUDIO_UPLOAD_KEY,
        ApplicationController.SX_PHOTO_UPLOAD_KEY,
        TagUtil.USER_AGENT_COOKIE, TagUtil.CLOUD_PUSH_KEY},
        types=UploadForm.class)
public class ApplicationController implements ServletContextAware {

    @Inject
    private MediaService mediaService;

    @Inject
    private MediaHelper mediaHelper;

    String currentFileName = null;

    private ServletContext servletContext;

    private static final Log log = LogFactory
            .getLog(ApplicationController.class);

    private static final String PAGE_UPLOAD = "upload";
    private static final String PAGE_GALLERY = "gallery";
    private static final String PAGE_VIEWER = "viewer";
    private static final String PAGE_ALL = "all";


    private static final String DESKTOP = "d";
    private static final String MOBILE = "m";
    private static final String TABLET = "t";

    public static final String SX_PHOTO_UPLOAD_KEY = "sxPhotoUpload";
    public static final String SX_VIDEO_UPLOAD_KEY = "sxVideoUpload";
    public static final String SX_AUDIO_UPLOAD_KEY = "sxAudioUpload";

    @PostConstruct
    public void init(){
        ensureUploadDirExists();
    }

    @Autowired
    public ApplicationController(ServletContext servletContext){
        this.servletContext = servletContext;       
    }

    @ModelAttribute("msg")
    public String putAttributeMsg(){
        return "";
    }
    
    @ModelAttribute("admin")
    public boolean putAttributeAdmin(){
        return false;
    }

    @ModelAttribute
    public void putAttributeAjax(WebRequest request, Model model) {
        model.addAttribute("ajaxRequest", isAjaxRequest(request));
    }

    @ModelAttribute
    public void putAttributeDesktop(WebRequest request, Model model){
        model.addAttribute("desktop", Utils.isDesktop(request.getHeader("User-Agent")));
    }

    @ModelAttribute
    public void putAttributeEnhanced(HttpServletRequest request, Model model){
        boolean enhanced = Utils.isEnhanced(request);
        if( !model.containsAttribute("enhanced") || enhanced){
            model.addAttribute("enhanced", enhanced);
        }
    }
    

    @ModelAttribute
    public void putAttributeView(WebRequest request, Model model) {
        String view = DESKTOP;
        if( Utils.isMobileBrowser(request.getHeader("User-Agent"))){
            view = MOBILE;
        }
        else if( Utils.isTabletBrowser(request.getHeader("User-Agent"))){
            view = TABLET;
        }
        model.addAttribute("view", view);
    }

    @RequestMapping(value="/app", method = RequestMethod.GET)
    public String get( HttpServletRequest request,
            HttpServletResponse response,
            UploadForm form,
            Model model, 
            @ModelAttribute("msg") String msg) {
        
        log.debug("form="+form);

        String view = null;
        if( TABLET.equals(form.getL()) ){
            form.setP(PAGE_ALL);
        }
        
        addCommonModel(model,form.getL());

        if( PAGE_UPLOAD.equals(form.getP()) ){
            addUploadViewModel(PAGE_UPLOAD, form.getL(), model);
            view = "upload-page";
        }
        else if( PAGE_GALLERY.equals(form.getP()) ){
            view = "gallery-page";
        }
        else if( PAGE_VIEWER.equals(form.getP())){
            addViewerViewModel(form.getPhotoId(),form.getL(), model, false, false);
            view = "viewer";
        }
        else if( PAGE_ALL.equals(form.getP())){
            addUploadViewModel(PAGE_ALL, form.getL(), model);
            addViewerViewModel(form.getPhotoId(), form.getL(), model, false, false);
            view = "tablet";
        }
        log.debug("forwarding to " + view);

        return view;
    }

    @RequestMapping(value="/carousel")
    public String getCarouselContent(UploadForm form, Model model) {
        model.addAttribute("carouselItems", mediaService
                .getMediaImageMarkup(form.getL()));
        model.addAttribute("layout",form.getL());
        return "carousel";
    }
    
    @RequestMapping(value="/viewer", method = RequestMethod.GET)
    public String getViewerContent(@RequestParam(value="action", required=false) String action, 
            UploadForm form, Model model) {
        log.info(form);
        String actionParam = form.cleanParam(action);
        boolean back = "back".equals(actionParam);
        boolean forward = "forward".equals(actionParam);
        addCommonModel(model, form.getL());
        addViewerViewModel(form.getPhotoId(), form.getL(), model, back, forward);
        return "viewer-panel";
    }

    @RequestMapping(value="/photo-list", method=RequestMethod.GET)
    public String getPhotoListContent(UploadForm form, Model model){
        addCommonModel(model, form.getL());
        return "photo-list";
    }

    @RequestMapping(value="/photo-list-json", method=RequestMethod.GET, produces="application/json")
    public @ResponseBody List<MediaMessageTransfer> getPhotoListJSON(
            @RequestParam(value="since") long since,
            @RequestParam(value="_", required=false) String jqTimestamp){

        List<MediaMessage> list = mediaService.getMediaCopy();
        List<MediaMessageTransfer> results = new ArrayList<MediaMessageTransfer>();
        
        if( list != null && list.size() > 0 ){
            Iterator<MediaMessage> iter = list.iterator();
            while( iter.hasNext() ){
                MediaMessage msg = iter.next();
                if(  msg.getCreated() > since || since == 0){
                    log.debug(msg);
                    MediaMessageTransfer transfer = new MediaMessageTransfer(msg);
                    log.debug(transfer);
                    results.add(transfer);

                }
            }
        }
        return results;
    }

    private File getSXThumbnailFile(String sessionId){
        return new File(servletContext.getRealPath("/resources/uploads")
                +File.separator+"sx-"+sessionId+"-small.png");
    }

    @RequestMapping(value="/app", method = RequestMethod.POST, consumes="multipart/form-data")
    public String postUpload(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "camera", required = false) MultipartFile cameraMultiPart,
            @RequestParam(value = "camcorder", required = false) MultipartFile camcorderMultiPart,
            @RequestParam(value = "microphone", required = false) MultipartFile microphoneMultiPart,
            @RequestParam(value="fullPost", defaultValue="true") String fullPost,
            @RequestParam(value="operation", required=false) String operation,
            UploadForm form, BindingResult result,
            @ModelAttribute("msg") String msg,
            @ModelAttribute("desktop") boolean desktop,
            Model model)
                    throws IOException {
        
        log.info("postUpload()");

        log.info("user-agent="+request.getHeader("User-Agent"));
        log.info(form);
        
        if( cameraMultiPart != null ){
            log.info("incoming camera upload " + cameraMultiPart.getContentType() + cameraMultiPart.getSize() );
        }
        if( camcorderMultiPart != null ){
            log.info("incoming video upload " + camcorderMultiPart.getContentType() + camcorderMultiPart.getSize() );
        }
        if( microphoneMultiPart != null ){
            log.info("incoming microphone upload " + microphoneMultiPart.getContentType() + microphoneMultiPart.getSize() );
        }
        
        if( operation != null && "cancel".equals(operation)){
            log.info("redirecting on cancel");
            request.getSession().removeAttribute(SX_PHOTO_UPLOAD_KEY);
            request.getSession().removeAttribute(SX_AUDIO_UPLOAD_KEY);
            request.getSession().removeAttribute(SX_VIDEO_UPLOAD_KEY);
            return "redirect:/" + "app?l=" + form.getL()+(MOBILE.equals(form.getL())?"p=upload":"");
        }
        
        boolean hasUpload = cameraMultiPart != null || camcorderMultiPart != null || microphoneMultiPart != null;

        //SX Image upload before full form post
        if( hasUpload && !"true".equals(fullPost) && SXUtils.isSXRegistered(request)){
            log.info("SX upload");
            String sessionId = request.getSession().getId();
            if( cameraMultiPart != null ){
                File sxPhotoMasterFile = getOriginalPhotoFile(sessionId);
                cameraMultiPart.transferTo(sxPhotoMasterFile);
                request.getSession().setAttribute(SX_PHOTO_UPLOAD_KEY, sxPhotoMasterFile);
                File sxThumbnail = mediaHelper.processSmallImage(sxPhotoMasterFile, "sx-"+sessionId);
                model.addAttribute("sxThumbnail",sxThumbnail);
            }
            else if( camcorderMultiPart != null ){
                File sxVideoMasterFile = getOriginalVideoFile(sessionId);
                camcorderMultiPart.transferTo(sxVideoMasterFile);
                request.getSession().setAttribute(SX_VIDEO_UPLOAD_KEY, sxVideoMasterFile);
                model.addAttribute(SX_VIDEO_UPLOAD_KEY,sxVideoMasterFile);
            }
            else if( microphoneMultiPart != null ){
                File sxAudioMasterFile = getOriginalAudioFile(sessionId);
                microphoneMultiPart.transferTo(sxAudioMasterFile);
                request.getSession().setAttribute(SX_AUDIO_UPLOAD_KEY, sxAudioMasterFile);
                model.addAttribute(SX_AUDIO_UPLOAD_KEY,sxAudioMasterFile);
            }
            return postUploadFormResponseView(false, false, form.getL());
        }

        boolean success = false;

        File sxPhotoUpload = (File)request.getSession().getAttribute(SX_PHOTO_UPLOAD_KEY);
        File sxVideoUpload = (File)request.getSession().getAttribute(SX_VIDEO_UPLOAD_KEY);
        File sxAudioUpload = (File)request.getSession().getAttribute(SX_AUDIO_UPLOAD_KEY);
        log.info("sxPhotoUpload="+sxPhotoUpload);
        log.info("sxVideoUpload="+sxVideoUpload);
        log.info("sxAudioUpload="+sxAudioUpload);
        
        File[] processedImageFiles = null;
        MediaMessage media = new MediaMessage();
        String id = newId();
        media.setId(id);
        media.setDescription(form.getDescription());
        media.setCreated(System.currentTimeMillis());
        //Save and process image files
        if( cameraMultiPart != null || sxPhotoUpload != null ){
            log.info("saving photo upload..");
            File origPhotoFile = getOriginalPhotoFile(id);
            if( sxPhotoUpload != null ){
                sxPhotoUpload.renameTo(origPhotoFile);
                File sxThumbnail = getSXThumbnailFile(request.getSession().getId());
                if( sxThumbnail.exists() ){
                    sxThumbnail.delete();
                }
                request.getSession().removeAttribute(SX_PHOTO_UPLOAD_KEY);
            }
            else{
                saveMultipartUploadToFile(cameraMultiPart,getOriginalPhotoFile(id));
            }
            processedImageFiles = mediaHelper.processSmallAndLargeImages(origPhotoFile, id);
            if( processedImageFiles != null ){
                media.setSmallPhoto(processedImageFiles[0]);
                media.setLargePhoto(processedImageFiles[1]);
            }
        }
        //Save uploaded video files
        if( camcorderMultiPart != null || sxVideoUpload != null ){
            log.info("saving video upload...");
            File origVideoFile = getOriginalVideoFile(id);
            if( sxVideoUpload != null ){
                sxVideoUpload.renameTo(origVideoFile);
                request.getSession().removeAttribute(SX_VIDEO_UPLOAD_KEY);
            }
            else{
                saveMultipartUploadToFile(camcorderMultiPart,origVideoFile);
            }
            media.setVideo(origVideoFile);
        }
        //Save uploaded audio files
        if( microphoneMultiPart != null || sxAudioUpload != null ){
            log.info("saving audio upload...");
            File origAudioFile = getOriginalAudioFile(id);
            if( sxAudioUpload != null ){
                sxAudioUpload.renameTo(origAudioFile);
                request.getSession().removeAttribute(SX_AUDIO_UPLOAD_KEY);
            }
            else{
                saveMultipartUploadToFile(microphoneMultiPart,origAudioFile);
            }
            media.setAudio(origAudioFile);
        }
        log.info("media.isHasMedia()="+media.isHasMedia());
        if( media.isHasMedia() ){
            mediaService.addMedia(media);
            log.info("created new media: "+media);
    
            model.addAttribute("msg","Thank you, your files were uploaded successfully.");
            
            PushContext pc = PushContext.getInstance(servletContext);
            pc.push("photos");  
    
            success = true;
        }
        addCommonModel(model, form.getL());
        return postUploadFormResponseView(isAjaxRequest(request),success,form.getL());
    }

    private String postUploadFormResponseView(boolean ajax, boolean redirect, String layout){
        String view = null;
        if( ajax ){
            redirect = false;
        }
        if( redirect ){
            view = "redirect:/" + "app?l=" + layout
                    +(MOBILE.equals(layout)?"p=upload":"");
        }
        else if( MOBILE.equals(layout)){
            view = "upload-page";
        }
        else {
            view = "upload-form";
        }
        return view;
    }

    private void addCommonModel(Model model, String layout){
        model.addAttribute("mediaService", mediaService);
        model.addAttribute("layout",layout);
        model.addAttribute("updated",System.currentTimeMillis());
    }

    private void addUploadViewModel(String page, String layout, Model model){
        if( PAGE_UPLOAD.equals(page) ){
            page = PAGE_VIEWER;
        }
        model.addAttribute("carouselItems", mediaService
                .getMediaImageMarkup(layout));
    }

    private void addViewerViewModel(String photoId, String layout, Model model, boolean back, boolean forward){

        MediaMessage msg = null;
        if( photoId != null ){
            if( back || forward ){
                List<MediaMessage> list = mediaService.getMediaListSortedByTime();
                if( list != null ){
                    for(int i = 0 ; i < list.size() ; i++ ){
                        String pid = list.get(i).getId();
                        if( pid != null && pid.equals(photoId) ){
                            int target = (back ? i-1 : i+1);
                            if( target == -1 ){
                                target = list.size()-1;
                            }
                            else if( target == list.size() ){
                                target = 0;
                            }
                            msg = list.get(target);
                        }
                    }
                }
            }
            else{
                msg = mediaService.getMediaMessage(photoId);
            }

        }
        else{
            List<MediaMessage> list = mediaService.getMediaCopy();
            if( list.size() > 0 ){
                msg = list.get(0);
            }
        }
        model.addAttribute("media", msg);
    }

    private String newId() {
        return Long.toString(
                Math.abs(UUID.randomUUID().getMostSignificantBits()), 32);
    }

    private File getOriginalPhotoFile(String id){
        String fileName = id + ".jpg";
        String path = getUploadsDir() + File.separator + fileName;
        return new File(path);
    }

    private File getOriginalVideoFile(String id){
        String fileName = id + ".mp4";
        String path = getUploadsDir() + File.separator + fileName;
        return new File(path);
    }
    
    private File getOriginalAudioFile(String id){
        String fileName = id + ".m4a";
        String path = getUploadsDir() + File.separator + fileName;
        return new File(path);
    }
    
    private File getUploadsDir(){
        return new File(servletContext.getRealPath("/resources/uploads"));
    }

    private void ensureUploadDirExists(){
        File uploadsDir = getUploadsDir();
        boolean exists = uploadsDir.exists();
        if( !exists){
            exists = uploadsDir.mkdir();
        }
        log.info("attempting checking upload dir " + exists + ", "+uploadsDir.getAbsolutePath());
    }

    private File saveMultipartUploadToFile(MultipartFile upload, File file){
        if( upload != null && !upload.isEmpty()){
            ensureUploadDirExists();
            log.info("writing new image file " + file.getAbsolutePath());
            try {
                upload.transferTo(file);
            } catch( Exception e) {
                log.warn("could not write file ", e);
                e.printStackTrace();
            } 
        }
        return file;
    }

    private static boolean isAjaxRequest(WebRequest webRequest) {
        String requestedWith = webRequest.getHeader("Faces-Request");
        if ("partial/ajax".equals(requestedWith))  {
            return true;
        }

        requestedWith = webRequest.getHeader("X-Requested-With");
        return requestedWith != null ? "XMLHttpRequest".equals(requestedWith) : false;
    }

    private static boolean isAjaxRequest(HttpServletRequest webRequest) {
        String requestedWith = webRequest.getHeader("Faces-Request");
        if ("partial/ajax".equals(requestedWith))  {
            return true;
        }

        requestedWith = webRequest.getHeader("X-Requested-With");
        return requestedWith != null ? "XMLHttpRequest".equals(requestedWith) : false;
    }

    public void setServletContext(ServletContext sc) {
        servletContext = sc; 
    }

}
