package org.icemobile.samples.spring.mediacast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/media")
@SessionAttributes({"uploadModel"})
public class ApplicationController implements ServletContextAware {

	@Inject
	private MediaService mediaService;
	
	String currentFileName = null;
	
	private static final Log log = LogFactory
			.getLog(ApplicationController.class);
	
	private static final String DELIMETERS = "[, \t\n\r\f]";
        
        private ServletContext servletContext;
	
	@ModelAttribute("uploadModel")
	public MediaMessage getUploadModel(){
		MediaMessage uploadModel = new MediaMessage();
		log.debug("returning new uploadModel="+uploadModel);
		return uploadModel;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String getMediacastPage(Model model, 
			@ModelAttribute("uploadModel") MediaMessage uploadModel) {
		model.addAttribute("mediaService", mediaService);
		if( !model.containsAttribute("uploadModel")){
			model.addAttribute("uploadModel", uploadModel);
		}
		log.debug("uploadModel="+uploadModel);
		return "mediacast";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String getMediaViewerPage(@PathVariable String id, Model model) {
		MediaMessage msg = mediaService.getMediaMessage(id);
		if (msg != null) {
			log.debug("found media " + msg);
			model.addAttribute("media", msg);
			return "mediaviewer";
		} else {
			log.warn("Could not find message with id=" + id);
			return "redirect:/";
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String deleteMedia(@PathVariable String id) {
		mediaService.removeMessage(id);
		log.debug("removed media message id=" + id);
		return "redirect:/";
	}

	@RequestMapping(method = RequestMethod.POST, consumes="multipart/form-data")
	public String processUpload(
			HttpServletRequest request,
			@RequestParam(value = "upload", required = false) MultipartFile file,
			Model model, 
			@ModelAttribute("uploadModel") MediaMessage uploadModel,
			final RedirectAttributes redirectAttrs)
			throws IOException {
		
		if( file != null ){
			log.info("incoming upload " + file.getContentType() + ", " + file.getBytes() );
		}
		
		if( file != null ){
			String type = request.getParameter("type");
			if( "photo".equals(type)){
				saveImage(request, file, uploadModel);
			}
			else if( "video".equals(type)){
				saveVideo(request, file, uploadModel);
			}
			else if( "audio".equals(type)){
				saveAudio(request, file, uploadModel);
			}
			log.debug("processUpload() type=" + type + ", file="+file);
			if ((null != file) && !file.isEmpty()) {
				uploadModel.setUploadMsg(String.format("Thank you, your %s file was uploaded successfully.", type));
				log.debug("successfully uploaded media, uploadModel=" + uploadModel);
			}
			redirectAttrs.addFlashAttribute("uploadModel", uploadModel);
			
			return "redirect:/";
		}
		else{
			log.warn("upload file was null");
			return "mediacast";
		}
		
	}

	@RequestMapping(method = RequestMethod.POST)
	public String submitMessage(Model model, 
			@RequestParam String tags,
			@RequestParam String title, 
			@RequestParam String description,
			@RequestParam String geolocation,
			@ModelAttribute("uploadModel") MediaMessage uploadModel, 
			SessionStatus status) {

		if (uploadModel.getId() == null) {
			uploadModel.setId(newId());
		}
		if (tags != null && tags.indexOf(" ") > -1) {
			model.addAttribute("tags", tags);
			uploadModel.setTags(Arrays.asList(tags.split(DELIMETERS)));
		}

		uploadModel.setDescription(description);
		uploadModel.setTitle(title);
		setGeolocation(geolocation, uploadModel);
		
		mediaService.addMedia(uploadModel);
		log.debug("successfully added message to mediaService, uploadModel="
				+ uploadModel);
		uploadModel.clear();
		status.setComplete();
		return "redirect:/";
	}
	
	@RequestMapping(value="/gallery", method=RequestMethod.GET)
	public String showGallery(Model model, 
			@RequestParam(value = "filters", required = false) String filters){
		
		GalleryModel galleryModel = new GalleryModel();
		galleryModel.setTags(mediaService.getTags());
		galleryModel.setTagsMap(mediaService.getTagsMap());
				
		if( filters != null ){
			galleryModel.setFilters(Arrays.asList(filters.split(DELIMETERS)));
			galleryModel.setFilterString(filters);
			Iterator<MediaMessage> iter = mediaService.getMedia().iterator();
			while( iter.hasNext() ){
				MediaMessage msg = iter.next();
				for( String tag : msg.getTags() ){
					if( filters.contains(tag)){
						galleryModel.getFilteredMessages().add(msg);
						break;
					}
				}
			}
		}
		else{
			galleryModel.setFilteredMessages(new ArrayList<MediaMessage>(mediaService.getMedia()));
		}
		galleryModel.setFilteredMessagesCount(galleryModel.getFilteredMessages().size());
		
		model.addAttribute("galleryModel", galleryModel);
		
		return "gallery";
	}
	
	private void setGeolocation(String geolocation, MediaMessage uploadModel){
		log.debug("geolocation="+geolocation);
		if( geolocation != null ){
			String[] parts = geolocation.split(",");
			
			if( parts.length > 0 ){
				try{
					uploadModel.setLatitude(Double.parseDouble(parts[0]));
				}
				catch(NumberFormatException e){
					//do nothing
				}
			}
			if( parts.length > 1 ){
				try{
					uploadModel.setLongitude( Double.parseDouble(parts[1]) );
				}
				catch(NumberFormatException e){
					//do nothing
				}
			}
			if( parts.length > 2 ){
				try{
					uploadModel.setAltitude(Double.parseDouble(parts[2]));
				}
				catch(NumberFormatException e){
					//do nothing
				}
			}
			if( parts.length > 3 ){
				try{
					uploadModel.setDirection(Double.parseDouble(parts[3]));
				}
				catch(NumberFormatException e){
					//do nothing
				} 
			}
			
		}
	}

	private String newId() {
		return Long.toString(
				Math.abs(UUID.randomUUID().getMostSignificantBits()), 32);
	}
	
	private void addNewMediaToUploadModel(HttpServletRequest request, MultipartFile file, 
			String suffix, MediaMessage uploadModel) throws IOException {
		if (uploadModel.getId() == null) {
			uploadModel.setId(newId());
		}
		String newFileName = "img-" + uploadModel.getId() + "." + suffix;
		String newPathName = "resources/uploads/" + newFileName;
		File newFile = new File(servletContext.getRealPath("/" + newPathName));
		if ((null != file) && !file.isEmpty()) {
			file.transferTo(newFile);
			currentFileName = newPathName;
		}
                else {
			// use previously uploaded file, such as from ICEmobile-SX
			newFileName = getCurrentFileName(request);
			newFile = new File(servletContext.getRealPath("/" + newPathName));
		}
		newFile.deleteOnExit();
		uploadModel.setLargePhoto(newFile);//TODO need to process
	}

	private void saveImage(HttpServletRequest request, MultipartFile file,
			MediaMessage uploadModel) throws IOException {

		addNewMediaToUploadModel(request, file, "jpg", uploadModel);
	}

	private void saveVideo(HttpServletRequest request, MultipartFile file,
			MediaMessage uploadModel) throws IOException {
		
		addNewMediaToUploadModel(request, file, "mp4", uploadModel);
	}

	private void saveAudio(HttpServletRequest request, MultipartFile file,
			MediaMessage uploadModel) throws IOException {
		
		addNewMediaToUploadModel(request, file, "m4a", uploadModel);
	}

	//TODO look into this
	private String getCurrentFileName(HttpServletRequest request) {
		if (null == currentFileName) {
			return "resources/images/uploaded.jpg";
		}
		return currentFileName;
	}

    public void setServletContext(ServletContext sc) {
       servletContext = sc;
    }

}
