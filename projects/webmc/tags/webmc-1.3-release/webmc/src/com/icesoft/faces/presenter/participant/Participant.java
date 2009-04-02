/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 *
 */
package com.icesoft.faces.presenter.participant;

import com.icesoft.faces.async.render.RenderManager;
import com.icesoft.faces.async.render.Renderable;
import com.icesoft.faces.component.ext.HtmlInputText;
import com.icesoft.faces.context.DisposableBean;
import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.Highlight;
import com.icesoft.faces.presenter.chat.Message;
import com.icesoft.faces.presenter.participant.view.ChatView;
import com.icesoft.faces.presenter.participant.view.ParticipantView;
import com.icesoft.faces.presenter.presentation.Presentation;
import com.icesoft.faces.presenter.presentation.PresentationManager;
import com.icesoft.faces.presenter.presentation.PresentationManagerBean;
import com.icesoft.faces.presenter.slide.Slide;
import com.icesoft.faces.presenter.util.MessageBundleLoader;
import com.icesoft.faces.presenter.util.StringResource;
import com.icesoft.faces.webapp.xmlhttp.FatalRenderingException;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import com.icesoft.faces.webapp.xmlhttp.RenderingException;
import com.icesoft.faces.webapp.xmlhttp.TransientRenderingException;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.faces.component.UIData;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class that represents a single user of the application. They can be either a
 * viewer or moderator, and manage the UI level functionality such as status
 * messages, chatting, slide changing, etc.
 */
public class Participant extends ParticipantInfo implements Renderable, DisposableBean {
    private static Log log = LogFactory.getLog(Participant.class);
    private static final int HIGHLIGHT_TIME = 4000;

    private int role = ParticipantInfo.ROLE_VIEWER;
    private int moderatorSelection;
    private LoginBean loginBean = new LoginBean(this);
    private PresentationManagerBean presentationManager;
    private Presentation presentation;
    private PersistentFacesState state;
    private ChatView chatView = new ChatView();
    private ParticipantView participantView = new ParticipantView();
    private String chatMessage = "";
    private String statusMessage;
    private Effect statusEffect;
    private Effect messageEffect;
    private RenderManager renderManager;
    private UIData participantsTable;
    private HtmlInputText chatMessageField = null;
    private boolean moderatorDialog = false;
    private boolean confirmDialog = false;
    private boolean uploadDialog = true;
    private boolean slideTypePres = true;
    private boolean mobile = false;
    private boolean mobileSniffed = false;
    private boolean loggedIn = false;
    double scale = (double)Slide.MOBILE_MAX_WIDTH/(double)Slide.MAX_WIDTH;
    private int preloadMaxIterations = 0;
    private int preloadIterations = 0;
    private int preloadCurrentPage = 0;

    public Participant() {
        super();
        state = PersistentFacesState.getInstance();
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
        
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    /**
     * Method to set the presentation manager This should be called from 
     * faces-config on initialization.
     *
     * @param presentationManager to set
     */
    public void setPresentationManager(
            PresentationManagerBean presentationManager) {
        this.presentationManager = presentationManager;
    }

    public Presentation getPresentation() {
        return presentation;
    }

    public void setPresentation(Presentation presentation) {
        this.presentation = presentation;
    }

    public PersistentFacesState getState() {
        return state;
    }

    public ChatView getChatView() {
        return chatView;
    }

    /**
     * Method to get the chat view as a list of participants This is done so it
     * can be used in a dataTable on the page
     *
     * @return chat view list
     */
    public Message[] getChatViewList() {
    	return (chatView.getView());
    }

    public ParticipantView getParticipantView() {
        return participantView;
    }

    /**
     * Method to get the participant view as a list of participants This is done
     * so it can be used in a dataTable on the page.
     * This method also refreshes the PersistentFacesState.
     *
     * @return participant view list
     */
    public Participant[] getParticipantViewList() {
        state = PersistentFacesState.getInstance();
        return (participantView.getView());
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public Effect getStatusEffect() {
        return statusEffect;
    }
    
    public Effect getMessageEffect() {
        return messageEffect;
    }

    public RenderManager getRenderManager() {
        return renderManager;
    }
    
    /**
     * Method to set the render manager This should be called from faces-config
     * on initialization In addition this method will set the loginBean's
     * renderer
     *
     * @param renderManager to set
     */
    public void setRenderManager(RenderManager renderManager) {
        this.renderManager = renderManager;
        loginBean.setLoginPageRenderer(
                renderManager.getOnDemandRenderer("loginPageRenderer"));
        loginBean.addRenderable();
    }

    /**
     * Method to get the bound participant table This is necessary to be able to
     * determine which user was clicked in case a moderator wishes to transfer
     * their power
     *
     * @return participantsTable
     */
    public UIData getParticipantsTable() {
        return participantsTable;
    }

    public void setParticipantsTable(UIData participantsTable) {
        this.participantsTable = participantsTable;
    }

    public HtmlInputText getChatMessageField() {
        return chatMessageField;
    }

    public void setChatMessageField(HtmlInputText chatMessageField) {
        this.chatMessageField = chatMessageField;
    }

    /**
     * Method to determine if the moderator dialog is open or not
     *
     * @return moderatorDialog
     */
    public boolean getModeratorDialog() {
        return moderatorDialog;
    }

    public void setModeratorDialog(boolean moderatorDialog) {
        this.moderatorDialog = moderatorDialog;
    }

    /**
     * Method to determine if the confirm logout dialog is open or not
     *
     * @return confirmDialog
     */
    public boolean getConfirmDialog() {
        return confirmDialog;
    }

    public void setConfirmDialog(boolean confirmDialog) {
        this.confirmDialog = confirmDialog;
    }

    /**
     * Method to determine if the upload dialog is open or not
     *
     * @return uploadDialog
     */
    public boolean getUploadDialog() {
        return uploadDialog;
    }

    public void setUploadDialog(boolean uploadDialog) {
        if (isModerator()) {
            this.uploadDialog = uploadDialog;
        }
    }

    public boolean getSlideTypePres() {
        return slideTypePres;
    }

    public void setSlideTypePres(boolean slideTypePres) {
        this.slideTypePres = slideTypePres;
    }

    /**
     * Method called when the first tab above the slide image is clicked
     *
     * @return "slideTypeOne"
     */
    public String toggleSlideTypeOne() {
        slideTypePres = true;

        return "slideTypeOne";
    }

    /**
     * Method called when the second tab above the slide image is clicked
     *
     * @return "slideTypeTwo"
     */
    public String toggleSlideTypeTwo() {
        slideTypePres = false;

        return "slideTypeTwo";
    }

    /**
     * This method performs browser sniffing the first time it is called to 
     * determine whether or not the Participant is on a mobile browser.  
     * Afterwards, it simply returns the result of the initial browser sniffing.
     *
     * @return boolean is the Participant using a mobile browser
     */
	public boolean isMobile() {
        if(!mobileSniffed){
			HttpServletRequest request = (HttpServletRequest)state.getFacesContext().getExternalContext().getRequest();
	        String useragent = request.getHeader("user-agent");
	        String agent = useragent.toLowerCase();
	        if ((agent.indexOf("safari") != -1 && agent.indexOf("mobile") != -1)  
	         || (agent.indexOf("opera") != -1 && agent.indexOf("240x320") != -1)) {
	        	mobile = true;
	    		// Mobile browsers have limited space - set chatView to 3 lines.
	        	chatView.setViewSize(3);
	        }
	        mobileSniffed = true;
        }
		return mobile;
	}
	
	public boolean getTestMobile(){
		mobile = true;
		chatView.setViewSize(3);
		mobileSniffed = true;
		return mobile;
	}

	public void setMobile(boolean mobile) {
		this.mobile = mobile;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

    /**
     * Method to safely get the current slide object
     *
     * @return the slide, or null on error or if no presentation / slide exists
     */
    public Slide getCurrentSlide() {
        if (presentation != null) {
            try {
                return presentation.getPermissionSlide(isModerator(), mobile);
            }catch (Exception failedToGet) {
                /* Any error would be mentioned through the get methods.
                   This exception is therefore caught and ignored so if
                   something does go wrong, it won't show up to the user
                */
            }
        }
        return null;
    }
    
    /**
     * Method to safely get the info slide.
     *
     * @return the slide, or null on error or if no presentation / slide exists
     */
    public Slide getInfoSlide() {
        return presentation.getInfoSlide(mobile);

    }

    /**
     * Method to get a series of slides not yet displayed on the page.  This is 
     * used by a hidden dataTable on the page.
     *
     * @return list of Slides to preload
     */
    public Slide[] getPreloadSlides() {
        if(presentation != null){
            if (preloadIterations < preloadMaxIterations) {
                Slide[] preloadSlides =
                        new Slide[PresentationManager.SLIDE_PRELOAD_COUNT];
                for (int i = 0; i < PresentationManager.SLIDE_PRELOAD_COUNT; i++) {
                    if(preloadCurrentPage > presentation.getLastSlideNumber()){
                    	preloadCurrentPage = 1;
                    }
                	preloadSlides[i] = presentation.getSlide(preloadCurrentPage,mobile);
        		    preloadCurrentPage++;
                }
                preloadIterations++;
                return preloadSlides;
            }
        }
        return new Slide[0];
    }

    /**
     * Method to trigger preloading of slides.  This is called when a 
     * Participant logs in to a new presentation or when an existing 
     * presentation uploads new slides.
     * 
     */
    public void preload(){
        if(presentation.getLastSlideNumber() > 0){
        	preloadMaxIterations = presentation.getLastSlideNumber()/PresentationManager.SLIDE_PRELOAD_COUNT;
        	if(presentation.getLastSlideNumber()%PresentationManager.SLIDE_PRELOAD_COUNT >0){
        		preloadMaxIterations += 1;
        	}
        	preloadCurrentPage = presentation.getCurrentSlideNumber();
        	preloadIterations = 0;
        // There is no file uploaded for the presentation, reset variables.
        }else{
            preloadMaxIterations = 0;
            preloadIterations = 0;
            preloadCurrentPage = 0;        	
        }
    }

    /**
     * Convenience method to update the status message This will change the text,
     * and re-fire the status effect In addition the status message is trimmed to
     * a set length
     *
     * @param status new text
     */
    public void updateStatus(String status) {
        if (statusEffect == null) {
            statusEffect = new Highlight("#F2830C");
            statusEffect.setDuration(HIGHLIGHT_TIME / 2000);
        }
        statusEffect.setFired(false);

        statusMessage = StringResource.trimLength(status, 70);
    }
    
    /**
     * Convenience method to fire the message effect.
     */
    public void buildMessageEffect() {
        if (messageEffect == null) {
            messageEffect = new Highlight("#F2830C");
            messageEffect.setDuration(HIGHLIGHT_TIME / 2000);
        }
        messageEffect.setFired(false);
    }

	/**
     * Method to check if the role of this participant is ROLE_MODERATOR
     *
     * @return true if role is ROLE_MODERATOR, false otherwise
     */
    public boolean isModerator() {
        return role == ROLE_MODERATOR;
    }

    /**
     * Method to perform the login action for this participant This will create
     * (if participant is a moderator) a new presentation, or join an existing
     * one (if the participant is a viewer) Then a slew of local variables are
     * updated to get the user ready for the main functionality of webmc
     *
     * @return "loginSuccess" or "failed"
     */
    public String login() {
    	// Validation is done in the application logic because validators are 
    	// not fired when "required" is removed from a component and the 
    	// component is left blank (JSF behavior).
    	
        // Get the role from the context.
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map params = facesContext.getExternalContext().getRequestParameterMap();
        String loginSource = (String) params.get("loginSource");
        if(loginSource != null){
        	if(!loginBean.validatePresentation()){
        		return "failed";
        	}
        	if(loginBean.getPresentationPassword() != null){
	        	String inputPassword = loginBean.getPresentationPassword().trim();
	            if (loginSource.startsWith("view")) {
	                if(!loginBean.validatePassword(inputPassword)){
	                	return "failed";
	                }
	            }
	            // Moderator password validation not required, so
	            // we set the password to the trimmed input String.
	            loginBean.setPresentationPassword(inputPassword);
	        }
        }

        if (isModerator()) {
            presentation = presentationManager
                    .createPresentation(this, loginBean.getPresentationName());
            uploadDialog = true;
        } else {
            presentation = presentationManager
                    .getPresentation(loginBean.getPresentationName());
        }

        if (presentation != null) {
                // Update the chat and participant view with the presentation they are in
                chatView.setPresentation(presentation);
                participantView.setPresentation(presentation);

                // Let the participant join the presentation, and update existing users through chat
                presentationManager.joinPresentation(this, presentation);
                presentation
                        .addChatMessage(firstName, MessageBundleLoader.getMessage("bean.participant.joinedPresentation"));
                chatView.useBottomView();
                // Upon entering a presentation, preload the slides.
                preload();
                // Handle leaving the login page
                loggedIn=true;

                return "loginSuccess";
        }

        return "failed";
    }

    /**
     * Method to logout from the current presentation If the participant is a
     * moderator, the presentation will be ended, otherwise other viewers will
     * be notified of the participant leaving.  This will reset the login page 
     * renderer and cleanup the local presentation variables.
     * All the exceptions here are caught and ignored, as an error in one phase
     * of the logout should not stop the other phases from completing.
     *
     * @return "logout"
     */
    public String logout() {
        try {
            switch (role) {
                case ROLE_MODERATOR:
                    presentation.addChatMessage(firstName,
                    		MessageBundleLoader.getMessage("bean.participant.moderatorLogout"));
                    break;
                case ROLE_VIEWER:
                    presentation.addChatMessage(firstName,
                    		MessageBundleLoader.getMessage("bean.participant.viewerLogout"));
                    break;
            }

            loggedIn = false;

        } catch (Exception failedLogout1) { }

        try {
            presentation.deleteSkypeEntry(this.getSkype());
        } catch (Exception failedLogout2) { }
        
        try {
            presentation.removeParticipant(this);
            presentation = null;
        } catch (Exception failedLogout3) { }

        try {
            // Reset the various fields
            loginBean.clearFields();
            this.clearFields();
            role = ParticipantInfo.ROLE_VIEWER;
            toggleSlideTypeOne();
        } catch (Exception failedLogout4) { }

        return "logout";
    }

    /**
     * Method to popup the confirm logout dialog
     *
     * @return "confirmLogout"
     */
    public String confirmLogout() {
        confirmDialog = true;
        return "confirmLogout";
    }

    /**
     * Method called when the "Yes" button is clicked on the confirm logout
     * dialog
     *
     * @return "logout"
     */
    public String confirmLogoutYes() {
        confirmDialog = false;
        return logout();
    }

    /**
     * Method called when the "No" button is clicked on the confirm logout
     * dialog
     *
     * @return "confirmLogoutNo"
     */
    public String confirmLogoutNo() {
        confirmDialog = false;
        return "confirmLogoutNo";
    }

    /**
     * Method to add the current chat message text to the global message log
     *
     * @param event of the submit
     */
    public void addChatMessage(ActionEvent event) {
        presentation.addChatMessage(firstName, chatMessage);
        chatMessage = "";
    }

    /**
     * Method to set the focus to the chat message component The component
     * should be bound automatically
     */
    public void refreshChatMessageFocus() {
        if (chatMessageField != null) {
            chatMessageField.requestFocus();
        }
    }

    /**
     * Method to pass moderator status to another user
     *
     * @param event of the submit
     */
    public void passModerators(ActionEvent event) {
        moderatorDialog = true;
        moderatorSelection = participantsTable.getRowIndex();
    }

    /**
     * Method called when the moderator confirms they want to give away their
     * powers
     *
     * @return "switchModeratorsYes"
     */
    public String switchModeratorsYes() {
        role = ROLE_VIEWER;
        presentation.switchModerators(
                participantView.getPosition() + moderatorSelection);
        moderatorDialog = false;
        return "switchModeratorsYes";
    }

    /**
     * Method called when the moderator reconsiders giving away their powers,
     * and instead cancels the popup confirmation dialog
     *
     * @return "switchModeratorsNo"
     */
    public String switchModeratorsNo() {
        moderatorDialog = false;
        return "switchModeratorsNo";
    }
    
   /**
    * Method to return whether or not the participant has entered a skype name
    *
    *@return boolean true if name was entered otherwise false
    */
    public boolean isHasSkype()
    {
        return !getSkype().equals("");
    }

    /**
     * Method to email the presentation's chat log to this participant A generic
     * body and header will be created, and sent using the JavaMail API which is
     * handled through MailTool
     *
     * @return "emailMessageLog"
     */
    public String emailMessageLog() {
        Boolean success = Boolean.FALSE;
        try {
            Method sendMessageMethod = Class.forName(
                "com.icesoft.faces.presenter.mail.MailTool" )
                        .getMethod("sendMessage", new Class[] 
                                {String.class, String.class, String.class});
            String header = (
                "This is an automated email message. Please do not reply to this address.\n" +
                "Message log for presentation " + this.presentation.getName() +
                ":\n" +
                "-----------------------------------------------------------------------\n"
        );

            StringBuffer content = new StringBuffer();
            for (int i = 0; i < this.presentation.getMessageLog().size(); i++) {
                content.append(((Message) this.presentation.getMessageLog()
                    .get(i)).toString()).append("\n");
            }

            success = (Boolean) sendMessageMethod
                    .invoke("Message Log from WebMC",
                            (header + content.toString()),
                            this.email);
        } catch (Exception e) {
           if (log.isWarnEnabled()) {
               log.warn("Email message log failed ", e);
           }
        }

        if (success.booleanValue()) {
            updateStatus("Successfully emailed chat log to " + email);
        }
        else {
            updateStatus("Failed to email chat log to " + email);
        }

        return "emailMessageLog";
    }

    public String getSkype() {
        return skype;
    }

    /**
     * Listens to input from a commandLink surrounding the presentation slide in
     * the UI and sets the coordinates of the pointer image.
     *
     * @param event ActionEvent.
     */
    public void pointer(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        Map requestParams =
                context.getExternalContext().getRequestParameterMap();
        // get mouse coordinate of user click
        if(!mobile){
        	// Subtract the width of the pointer image so it points to the spot 
        	// you clicked.
            presentation.setPointerX(Integer.parseInt((String) requestParams.get("ice.event.x"))-14);
            presentation.setPointerY(Integer.parseInt((String) requestParams.get("ice.event.y")));
        }else{
        	// The desktop<->mobile algorithm does not position the pointer 
        	// where expected, so the integers have to be modified and tested to
        	// position the pointer properly.
            presentation.setPointerX((int)Math.rint((double)Integer.parseInt((String) requestParams.get("ice.event.x"))/scale-10));
            presentation.setPointerY((int)Math.rint((double)Integer.parseInt((String) requestParams.get("ice.event.y"))/scale+23));        	
        }
        
        presentation.requestOnDemandRender();
    }
    
    public String getPointerClass(){
        if(mobile){
        	// The desktop<->mobile algorithm does not position the pointer 
        	// where expected, so the integers have to be modified and tested to
        	// position the pointer properly.
        	double mobileY = Math.rint(((double)(presentation.getPointerY()-23))*scale);
        	double mobileX = Math.rint(((double)(presentation.getPointerX()-18))*scale);
        	return "top: " + (int)mobileY + "px; left: " + (int)mobileX + "px;";
        }else{
            return "top: " + presentation.getPointerY() + "px; left: " + presentation.getPointerX() + "px;"; 	
        }
    }

	public String getDirectlyEnterPresentation(){
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		String tempParameter = (String)externalContext.getRequestParameterMap().get("s");
		if(tempParameter != null){
			if(loggedIn){
			    logout();
			}
            loginBean.setPresentationName(tempParameter);
            firstName = ((HttpServletRequest)externalContext.getRequest()).getRemoteHost();
            login();
	   		try{
	            ((HttpServletResponse)externalContext.getResponse()).sendRedirect("index.jsp");
			}catch(IOException e){
			    e.printStackTrace();
			}
	        return "index.jsp";
		}else{
		    return null;
		}
	}

    /**
     * Method to refresh PersistentFacesState in login.jspx.
     *
     * @return null
     */
    public String getRefreshPersistentFacesState() {
        state = PersistentFacesState.getInstance();
        return null;
    }
    
    /**
     * Method called when any level of rendering exception happens.  In the case
     * of a non-transient (ie: fatal) rendering exception, the user will be
     * logged out.
     *
     * @param renderingException that occurred
     */
    public void renderingException(RenderingException renderingException) {
        if (log.isDebugEnabled() &&
                renderingException instanceof TransientRenderingException) {
            log.debug("Transient Rendering exception for Participant " + firstName + ":", renderingException);
        } else if (renderingException instanceof FatalRenderingException) {
            if (log.isDebugEnabled()) {
                log.debug("Fatal rendering exception for Participant " + firstName + ", logging out:", renderingException);
            }
            if(loggedIn){
                logout();
            }
        }
    }

	public void dispose() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Participant " + firstName + " Disposed - logging out");
        }
        if(loggedIn){
            logout();
	    }
		loginBean.removeRenderable();
	}

}
