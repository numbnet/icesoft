package org.icemobile.samples.spring.mediacast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

@Controller
@SessionAttributes({"uploadModel","msg","admin"})
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
	public void putAttributeDesktop(WebRequest request, Model model){
		model.addAttribute("desktop", Utils.isDesktop(request.getHeader("User-Agent")));
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
	public String get(
			HttpServletResponse response,
			@RequestParam(value="l", defaultValue=MOBILE) String layout, 
			@RequestParam(value="p", defaultValue=PAGE_UPLOAD) String page,
			@RequestParam(value="id", required=false) String id,
			@RequestParam(value="a", required=false) String action,
			@CookieValue(value="votes", required=false) String cookieVotes,
			Model model, 
			@ModelAttribute("uploadModel") MediaMessage uploadModel) {
		
		log.warn("main contest controller l="+layout+", p="+page+", a="+action +", cookies="+cookieVotes);
		
		String view = null;
		if (ACTION_VOTE.equals(action) ){
			doVote(response,id,cookieVotes, model);
		}
		if( layout.length() > 1 ){
			layout = layout.substring(0,1);
		}
		if( layout.equals(TABLET) ){
			page = PAGE_ALL;
		}
		
		addCommonModel(model,uploadModel,layout);
		
		if( page.equals(PAGE_UPLOAD) ){
			addUploadViewModel(page, layout, model);
			view = "contest-upload";
		}
		else if( page.equals(PAGE_GALLERY) ){
			view = "contest-gallery";
		}
		else if( page.equals(PAGE_VIEWER)){
			addViewerViewModel(id,layout,model);
			view = "contest-viewer";
		}
		else if( page.equals(PAGE_ALL)){
			addUploadViewModel(page, layout, model);
			addViewerViewModel(id, layout, model);
			view = "contest-tablet";
		}
		log.warn("forwarding to " + view);
		
		return view;
	}
	
	@RequestMapping(value="/contest-carousel", method = RequestMethod.GET)
	public String getCarouselContent(@RequestParam(value="l", defaultValue=MOBILE) String layout, 
			Model model) {
		model.addAttribute("carouselItems", mediaService
				.getContestMediaImageMarkup(layout));
		return "contest-carousel";
	}
	
	@RequestMapping(value="/contest-viewer", method = RequestMethod.GET)
	public String getContestViewerContent(
			@RequestParam(value="l", defaultValue=MOBILE) String layout, 
			@RequestParam(value="id", required=false) String id,
			Model model, 
			@ModelAttribute("uploadModel") MediaMessage uploadModel) {
		addCommonModel(model, uploadModel, layout);
		addViewerViewModel(id, layout, model);
		return "contest-viewer-panel";
	}
	
	@RequestMapping(value="/contest-photo-list", method=RequestMethod.GET)
	public String getPhotoListContent(
			@RequestParam(value="l", defaultValue=MOBILE) String layout, 
			Model model, 
			@ModelAttribute("uploadModel") MediaMessage uploadModel){
		addCommonModel(model, uploadModel, layout);
		
		return "contest-photo-list";
	}
	
	@RequestMapping(value="/contest-photo-list-json", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<MediaMessageTransfer> getPhotoListJSON(
			@RequestParam(value="since") long since){
		List<MediaMessage> list = mediaService.getMediaCopy();
		List<MediaMessageTransfer> results = new ArrayList<MediaMessageTransfer>();
		if( list != null && list.size() > 0 ){
			Iterator<MediaMessage> iter = list.iterator();
			while( iter.hasNext() ){
				MediaMessage msg = iter.next();
				if( msg.getLastVote() > since || msg.getCreated() > since || since == 0){
					results.add(new MediaMessageTransfer(msg));
				}
			}
		}
		return results;
	}

	

	@RequestMapping(value="/contest-admin", method = RequestMethod.POST)
	public String postAdminPage(HttpServletRequest request, String password, 
			String[] delete, Model model)
					throws IOException {
		log.info("password="+password+", delete="+delete);
		if( password != null ){
			boolean canEdit = canEdit(password);
			log.info("canEdit="+canEdit);
			if( canEdit ){
				model.addAttribute("admin",Boolean.valueOf(canEdit));
				model.addAttribute("mediaService", mediaService);
			}
		}
		else{
			boolean isAdmin = (Boolean)model.asMap().get("admin");
			if( delete != null && delete.length > 0 && isAdmin ){
				for( String id : delete ){
					mediaService.removeMessage(id);
				}
				model.addAttribute("mediaService", mediaService);
			}
		}
		
		return "contest-admin";
	}
	
	@RequestMapping(value="/contest-admin", method = RequestMethod.GET)
	public String getAdminPage()
					throws IOException {
		return "contest-admin";
	}
	
	
	@RequestMapping(value="/contest", method = RequestMethod.POST, consumes="multipart/form-data")
	public String post(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "upload", required = false) MultipartFile file,
			@Valid ContestForm form, BindingResult result,
			@ModelAttribute("uploadModel") MediaMessage uploadModel,
			@CookieValue(value="votes", required=false) String cookieVotes,
			Model model)
					throws IOException {
		
		log.info(form);
		
		boolean success = false;
		
		if( "gallery".equals(form.getForm())){
			doVote(response, form.getPhotoId(), cookieVotes, model);
			addCommonModel(model, uploadModel, form.getLayout());
			return "contest-photo-list";
		}
		

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
				PushContext.getInstance(servletContext).push("carousel");		
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
			view = "redirect:/" + "contest?l=" + layout
					+(MOBILE.equals(layout)?"p=upload":"");
		}
		else if( MOBILE.equals(layout)){
			view = "contest-upload";
		}
		else {
			view = "contest-upload-form";
		}
		return view;
	}

	private void addCommonModel(Model model, MediaMessage uploadModel, String layout){
		model.addAttribute("mediaService", mediaService);
		if( !model.containsAttribute("uploadModel")){
			model.addAttribute("uploadModel", uploadModel);
		}
		if( layout != null && layout.length() > 0){
			model.addAttribute("layout",layout.substring(0,1));
		}
		model.addAttribute("updated",System.currentTimeMillis());
	}
	
	private void addUploadViewModel(String page, String layout, Model model){
		if( PAGE_UPLOAD.equals(page) ){
			page = PAGE_VIEWER;
		}
		model.addAttribute("carouselItems", mediaService
				.getContestMediaImageMarkup(layout));
	}
	
	private void addViewerViewModel(String id, String layout, Model model){
		MediaMessage msg = mediaService.getMediaMessage(id);
		model.addAttribute("media", msg);
		if( TABLET.equals(layout)){
			model.addAttribute("tab", PAGE_VIEWER);
		}
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
					return;
				}
			}
			else{
				voterId = newId();
				votesList = new ArrayList<String>();
			}
			votesList.add(id);
			msg.getVotes().add(voterId);
			msg.setLastVote(System.currentTimeMillis());
			model.addAttribute("msg","Awesome, thanks for the vote!");
			String newVotes = voterId+":"+ votesList.toString().replaceAll(" ", "").replaceAll("^\\[|\\]$","");
			Cookie cookie = new Cookie("votes", newVotes);
			cookie.setHttpOnly(true);
			cookie.setPath("/");
			response.addCookie(cookie);
			PushContext.getInstance(servletContext).push("photos");
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

}
