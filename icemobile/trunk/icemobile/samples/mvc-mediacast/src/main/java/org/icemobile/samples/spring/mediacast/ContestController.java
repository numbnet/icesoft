package org.icemobile.samples.spring.mediacast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.icepush.PushContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

@Controller
@SessionAttributes({"uploadModel","msg"})
public class ContestController implements ServletContextAware {

	@Inject
	private MediaService mediaService;
	
	@Inject
	private MediaHelper mediaHelper;

	String currentFileName = null;

	private ServletContext servletContext;
	
	private final static String EDIT_KEY_PREFIX = "ice";

	private static final Log log = LogFactory
			.getLog(ContestController.class);
	
	private static final String PAGE_UPLOAD = "upload";
	private static final String PAGE_GALLERY = "gallery";
	private static final String PAGE_VIEWER = "viewer";
	private static final String PAGE_ALL = "all";
	
	
	private static final String DESKTOP = "d";
	private static final String MOBILE = "m";
	private static final String TABLET = "t";
	
	private static final String ACTION_VOTE = "v";
	private static final String ACTION_DELETE = "d";
	
		
	@Autowired
	public ContestController(ServletContext servletContext){
		this.servletContext = servletContext;		
	}

	@ModelAttribute("uploadModel")
	public MediaMessage putAttributeUploadModel(){
		MediaMessage uploadModel = new MediaMessage();
		log.debug("returning new uploadModel="+uploadModel);
		return uploadModel;
	}

	@ModelAttribute
	public void putAttributeAjax(WebRequest request, Model model) {
		model.addAttribute("ajaxRequest", isAjaxRequest(request));
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
	
	@RequestMapping(value="/contest", method = RequestMethod.GET)
	public String getMainPage(
			HttpServletResponse response,
			@RequestParam(value="l", defaultValue=MOBILE) String layout, 
			@RequestParam(value="p", defaultValue=PAGE_UPLOAD) String page,
			@RequestParam(value="e", required=false) String e,
			@RequestParam(value="id", required=false) String id,
			@RequestParam(value="a", required=false) String action,
			@CookieValue(value="votes", required=false) String cookieVotes,
			Model model, 
			@ModelAttribute("uploadModel") MediaMessage uploadModel) {
		
		log.warn("main contest controller l="+layout+", p="+page+", e="+e+", a="+action);
		
		boolean canEdit = canEdit(e);
		
		String view = null;
		if( action == ACTION_DELETE && canEdit){
			deleteMedia(id);
		}
		else if ( action == ACTION_VOTE ){
			doVote(response,id,cookieVotes, model);
		}
		if( layout.equals(TABLET) ){
			page = PAGE_ALL;
		}
		
		addCommonModel(model,uploadModel,layout);
		
		if( page.equals(PAGE_UPLOAD) ){
			addUploadViewModel(canEdit, page, layout, model);
			view = "contest-upload";
		}
		else if( page.equals(PAGE_GALLERY) ){
			addGalleryViewModel(canEdit,model);
			view = "contest-gallery";
		}
		else if( page.equals(PAGE_VIEWER)){
			addViewerViewModel(id,layout,model);
			view = "contest-viewer";
		}
		else if( page.equals(PAGE_ALL)){
			addUploadViewModel(canEdit, page, layout, model);
			addGalleryViewModel(canEdit,model);
			addViewerViewModel(id, layout, model);
			view = "contest-tablet";
		}
		log.warn("forwarding to " + view);
		
		return view;
	}
	
	@RequestMapping(value="/contest-carousel", method = RequestMethod.GET)
	public String getCarouselContent(Model model) {
		model.addAttribute("mediaService", mediaService);
		return "contest-carousel";
	}
	
	@RequestMapping(value="/contest-viewer", method = RequestMethod.GET)
	public String getContestViewerContent(
			@RequestParam(value="l", defaultValue=MOBILE) String layout, 
			@RequestParam(value="p", defaultValue=PAGE_UPLOAD) String page,
			@RequestParam(value="e", required=false) String e,
			@RequestParam(value="id", required=false) String id,
			@RequestParam(value="a", required=false) String action,
			@CookieValue(value="votes", required=false) String cookieVotes,
			Model model, 
			@ModelAttribute("uploadModel") MediaMessage uploadModel) {
		addCommonModel(model, uploadModel, layout);
		addViewerViewModel(id, layout, model);
		return "contest-viewer-panel";
	}
	
	@RequestMapping(value="/contest-photo-list", method=RequestMethod.GET)
	public String getPhotoListContent(
			@RequestParam(value="l", defaultValue=MOBILE) String layout, 
			@RequestParam(value="a", required=false) String action,
			Model model, 
			@ModelAttribute("uploadModel") MediaMessage uploadModel){

		addCommonModel(model, uploadModel, layout);
		return "contest-photo-list";
	}

	

	@RequestMapping(value="/contest", method = RequestMethod.POST, consumes="multipart/form-data")
	public String postUploadPhoto(
			HttpServletRequest request,
			@RequestParam(value = "upload", required = false) MultipartFile file,
			@Valid ContestForm form, BindingResult result, Model model, 
			@ModelAttribute("uploadModel") MediaMessage uploadModel)
					throws IOException {
		
		boolean success = false;

		if (result.hasErrors() || (file != null && file.isEmpty())) {
			uploadModel.setUploadMsg("Sorry, I think you missed something.");
		}
		else{
			if( file != null ){
				log.debug("file: " + file);
				uploadModel.setId(newId());
				saveImage(request, file, uploadModel);
				uploadModel.setDescription(form.getDescription());
				uploadModel.setEmail(form.getEmail());
				uploadModel.setCreated(System.currentTimeMillis());
				mediaHelper.processImage(uploadModel);
				mediaService.addMedia(uploadModel);
				log.debug("successfully added message to mediaService, uploadModel="
						+ uploadModel);
				uploadModel.setUploadMsg("Thank you, your file was uploaded successfully.");
				uploadModel.clear();
				PushContext.getInstance(servletContext).push("photos");				
				success = true;
			}
			else{
				log.warn("upload file was null");
			}			
		}

		return postUploadFormResponseView(isAjaxRequest(request),success,form.getLayout());
	}
	
	private String postUploadFormResponseView(boolean ajax, boolean redirect, String layout){
		String view = null;
		if( ajax ){
			redirect = false;
		}
		if( redirect ){
			view = "redirect:/" + "contest" + urlParams(false,PAGE_UPLOAD,layout);
		}
		else if( MOBILE.equals(layout)){
			view = "contest-upload";
		}
		else if( TABLET.equals(layout) && ajax){
			view = "contest-upload-form";
		}
		else{
			view = "contest-tablet";
		}
		return view;
	}

	private void addCommonModel(Model model, MediaMessage uploadModel, String layout){
		model.addAttribute("mediaService", mediaService);
		if( !model.containsAttribute("uploadModel")){
			model.addAttribute("uploadModel", uploadModel);
		}
		if( layout != null ){
			model.addAttribute("layout",layout);
		}
		log.debug("uploadModel="+uploadModel);
	}
	
	private void addGalleryViewModel(boolean canEdit, Model model){
		model.addAttribute("edit", canEdit);
	}
	
	private void addUploadViewModel(boolean canEdit, String page, String layout, Model model){
		if( PAGE_UPLOAD.equals(page) ){
			page = PAGE_VIEWER;
		}
		model.addAttribute("carouselItems", mediaService
				.getContestMediaImageMarkup(urlParams(canEdit,page,layout)));
	}
	
	private void addViewerViewModel(String id, String layout, Model model){
		MediaMessage msg = mediaService.getMediaMessage(id);
		model.addAttribute("media", msg);
		if( TABLET.equals(layout)){
			model.addAttribute("tab", PAGE_VIEWER);
			
		}
	}
	
	private void deleteMedia(String id) {
		mediaService.removeMessage(id);
		log.debug("removed media message id=" + id);
	}
	
	private void doVote(HttpServletResponse response, String id, String cookieVotes, Model model){
		MediaMessage msg = mediaService.getMediaMessage(id);
		if (msg != null ){
			String voterId = null;
			List<String> votesList = null;
			if( cookieVotes != null ){
				voterId = cookieVotes.substring(0,13);
				String votes = cookieVotes.substring(14);
				votesList = new ArrayList<String>(Arrays.asList(votes.split(",")));
				if( votesList.contains(id)){
					log.debug("attempted duplicate vote!!!");
					model.addAttribute("msg","Looks like you already voted on this one...try another");
				}
			}
			else{
				voterId = newId();
				votesList = new ArrayList<String>();
			}
			votesList.add(id);
			msg.getVotes().add(voterId);
			model.addAttribute("msg","Awesome, thanks for the vote!");
			String newVotes = voterId+":"+ votesList.toString().replaceAll(" ", "").replaceAll("^\\[|\\]$","");
			Cookie cookie = new Cookie("votes", newVotes);
			cookie.setHttpOnly(true);
			cookie.setPath("/");
			response.addCookie(cookie);
			log.debug("recorded vote");

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
		String newFileName = "img-" + uploadModel.getId() + "-orig." + suffix;
		File uploadDir = new File(servletContext.getRealPath("/resources/uploads/"));
		if( uploadDir.exists()){
			uploadDir.mkdir();
		}
		String newPathName = "resources/uploads/" + newFileName;
		File newFile = new File(servletContext.getRealPath("/" + newPathName));
		if ((null != file) && !file.isEmpty()) {
			file.transferTo(newFile);
			currentFileName = newPathName;
		}
		else {
			// use previously uploaded file, such as from ICEmobile-SX
			newFileName = currentFileName(request);
			newFile = new File(servletContext.getRealPath("/" + newPathName));
		}
		Media media = new Media();
		media.setFileName(newFileName);
		media.setFile(newFile);
		media.setType(file.getContentType());
		uploadModel.setPhoto(media);
	}

	private void saveImage(HttpServletRequest request, MultipartFile file,
			MediaMessage uploadModel) throws IOException {

		addNewMediaToUploadModel(request, file, "jpg", uploadModel);
	}
	//TODO look into this
	private String currentFileName(HttpServletRequest request) {
		if (null == currentFileName) {
			return "resources/images/uploaded.jpg";
		}
		return currentFileName;
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

	private static boolean isAjaxUploadRequest(WebRequest webRequest) {
		return webRequest.getParameter("ajaxUpload") != null;
	}

	public void setServletContext(ServletContext sc) {
		servletContext = sc; 
	}
	
	private boolean canEdit(String key){
		boolean result = false;
		log.warn("key="+key);
		if( key != null && key.length() > EDIT_KEY_PREFIX.length() 
				&& key.startsWith(EDIT_KEY_PREFIX)){
			String token = key.substring(EDIT_KEY_PREFIX.length());
			try{
				int val = Integer.parseInt(token);
				int min = currentMinute();
				log.warn("val="+val+", min="+min);
				if( val == min ){
					result = true;
				}
			}
			catch(NumberFormatException nfe){
				//do nothing
			}
		}
		log.warn("edit="+result);
		return result;
	}
	
	private int currentMinute(){
		return new GregorianCalendar().get(Calendar.MINUTE);
	}
	
	private String urlParams(boolean canEdit, String page, String layout){
		String result = "?";
		if( canEdit ){
			result += "e="+EDIT_KEY_PREFIX+currentMinute();
		}
		if( page != null ){
			if( canEdit ){
				result += "&";
			}
			result += "p="+page;
		}
		if( layout != null ){
			if( canEdit || page != null ){
				result += "&";
			}
			result += "l="+layout;
		}
		return result;
	}

}
