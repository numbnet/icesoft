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
import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.Highlight;
import com.icesoft.faces.presenter.chat.Message;
import com.icesoft.faces.presenter.mail.MailTool;
import com.icesoft.faces.presenter.participant.view.ChatView;
import com.icesoft.faces.presenter.participant.view.ParticipantView;
import com.icesoft.faces.presenter.presentation.Presentation;
import com.icesoft.faces.presenter.presentation.PresentationManager;
import com.icesoft.faces.presenter.presentation.PresentationManagerBean;
import com.icesoft.faces.presenter.slide.Slide;
import com.icesoft.faces.presenter.util.StringResource;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import com.icesoft.faces.webapp.xmlhttp.RenderingException;
import com.icesoft.faces.webapp.xmlhttp.TransientRenderingException;

import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSessionListener;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class that represents a single user of the application. They can be either a
 * viewer or moderator, and manage the UI level functionality such as status
 * messages, chatting, slide changing, etc.
 */
public class Participant extends ParticipantInfo implements Renderable, HttpSessionListener {
    private static final int HIGHLIGHT_TIME = 4000;
    private static final int MAXIMUM_TRANSIENT_EXCEPTIONS = 50;

    private static Log log = LogFactory.getLog(Participant.class);

    private int role = ParticipantInfo.ROLE_VIEWER;
    private int moderatorSelection;
    private int transientExceptionCount = 0;
    private long lastTransientException = -1;
    private LoginBean loginBean = new LoginBean(this);
    private PresentationManagerBean presentationManager;
    private Presentation presentation;
    private PersistentFacesState state;
    private ChatView chatView = new ChatView();
    private ParticipantView participantView = new ParticipantView();
    private String chatMessage = "";
    private String statusMessage;
    private Effect statusEffect;
    private RenderManager renderManager;
    private UIData participantsTable;
    private HtmlInputText chatMessageField = null;
    private boolean moderatorDialog = false;
    private boolean confirmDialog = false;
    private boolean uploadDialog = false;
    private boolean slideTypePres = true;
    

    public Participant() {
        super();
        state = PersistentFacesState.getInstance();
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
     * Method to determine if the moderator dialog is open or not
     *
     * @return moderatorDialog
     */
    public boolean getModeratorDialog() {
        return moderatorDialog;
    }

    /**
     * Method to determine if the confirm logout dialog is open or not
     *
     * @return confirmDialog
     */
    public boolean getConfirmDialog() {
        return confirmDialog;
    }

    /**
     * Method to determine if the upload dialog is open or not
     *
     * @return uploadDialog
     */
    public boolean getUploadDialog() {
        return uploadDialog;
    }

    /**
     * Method to get which type of slide is being displayed This is for the tabs
     * above the slide image
     *
     * @return slideTypePres
     */
    public boolean getSlideTypePres() {
        return slideTypePres;
    }

    /**
     * Method to get the role
     *
     * @return role
     */
    public int getRole() {
        return role;
    }

    /**
     * Method to get the state If the state is initially null, a new state will
     * be assigned through PersistentFacesState.getInstance
     *
     * @return state
     */
    public PersistentFacesState getState() {
        return state;
    }

    /**
     * Method to get the presentation this participant belongs to
     *
     * @return presentation
     */
    public Presentation getPresentation() {
        return presentation;
    }

    /**
     * Method to get the render manager
     *
     * @return renderManager
     */
    public RenderManager getRenderManager() {
        return renderManager;
    }

    /**
     * Method to get the login bean
     *
     * @return loginBean
     */
    public LoginBean getLoginBean() {
        return loginBean;
    }

    /**
     * Method to get the current chat message
     *
     * @return chatMessage
     */
    public String getChatMessage() {
        return chatMessage;
    }

    /**
     * Method to get the status message
     *
     * @return statusMessage
     */
    public String getStatusMessage() {
        state = PersistentFacesState.getInstance();
        return statusMessage;
    }

    /**
     * Method to get the chat view object
     *
     * @return chatView
     */
    public ChatView getChatView() {
        return chatView;
    }

    /**
     * Method to get the participant view object
     *
     * @return participantView
     */
    public ParticipantView getParticipantView() {
        return participantView;
    }

    /**
     * Method to get the participant view as a list of participants This is done
     * so it can be used in a dataTable on the page
     *
     * @return participant view list
     */
    public Participant[] getParticipantViewList() {
        return (participantView.getView());
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

    /**
     * Method to get the component bound to the chat message field
     *
     * @return chatMessageField
     */
    public HtmlInputText getChatMessageField() {
        return chatMessageField;
    }

    /**
     * Method to get the http session for this participant This is retreived
     * from the external context
     *
     * @return session
     */
    public HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);
    }

    /**
     * Method to get the status effect used on the status message
     *
     * @return statusEffect
     */
    public Effect getStatusEffect() {
        return statusEffect;
    }

    /**
     * Method to safely get the current slide object
     *
     * @return the slide, or null on error or if no presentation / slide exists
     */
    public Slide getCurrentSlide() {
        if (presentation != null) {
            try {
                return presentation.getPermissionSlide(isModerator());
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
     * Method to set the popup status of the moderator dialog
     *
     * @param moderatorDialog new
     */
    public void setModeratorDialog(boolean moderatorDialog) {
        this.moderatorDialog = moderatorDialog;
    }

    /**
     * Method to set the popup status of the confirm logout dialog
     *
     * @param confirmDialog new
     */
    public void setConfirmDialog(boolean confirmDialog) {
        this.confirmDialog = confirmDialog;
    }

    /**
     * Method to set the popup status of the file upload dialog This will only
     * be set for a moderator
     *
     * @param uploadDialog new
     */
    public void setUploadDialog(boolean uploadDialog) {
        if (isModerator()) {
            this.uploadDialog = uploadDialog;
        }
    }

    /**
     * Method to set the presentation manager
     *
     * @param presentationManager new
     */
    public void setPresentationManager(
            PresentationManagerBean presentationManager) {
        this.presentationManager = presentationManager;
    }

    /**
     * Method to set the presentation
     *
     * @param presentation new
     */
    public void setPresentation(Presentation presentation) {
        this.presentation = presentation;
    }

    /**
     * Method to set the slide type
     *
     * @param slideTypePres new
     */
    public void setSlideTypePres(boolean slideTypePres) {
        this.slideTypePres = slideTypePres;
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
     * Method to set the current chat message
     *
     * @param chatMessage new
     */
    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    /**
     * Method to set the bound participants table
     *
     * @param participantsTable bound
     */
    public void setParticipantsTable(UIData participantsTable) {
        this.participantsTable = participantsTable;
    }

    /**
     * Method to set the bound chat message field
     *
     * @param chatMessageField bound
     */
    public void setChatMessageField(HtmlInputText chatMessageField) {
        this.chatMessageField = chatMessageField;
    }

    /**
     * Convience method to update the status message This will change the text,
     * and refire the status effect In addition the status message is trimmed to
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
     * Method to set the role of this participant
     *
     * @param role new
     */
    public void setRole(int role) {
        this.role = role;
        
    }
    
    /**
     * Method to perform the login action for this participant This will create
     * (if participant is a moderator) a new presentation, or join an existing
     * one (if the participant is a viewer) Then a slew of local variables are
     * updated to get the user ready for the main functionality of webmc
     *
     * @return "loginSuccess" or "failed" for faces-config navigation
     */
    public String login() {
        if (isModerator()) {
            presentation = presentationManager
                    .createPresentation(this, loginBean.getPresentationName());
        } else {
            presentation = presentationManager
                    .getPresentation(loginBean.getPresentationName());
        }

        if (presentation != null) {
            if (presentation.hasSlotsLeft()) {
                // Update the chat and participant view with the presentation they are in
                chatView.setPresentation(presentation);
                participantView.setPresentation(presentation);

                // Let the participant join the presentation, and update existing users through chat
                presentationManager.joinPresentation(this, presentation);
                presentation
                        .addChatMessage(firstName, "Joined the Presentation.");
                chatView.useBottomView();

                // Handle leaving the login page
                if (getSession() != null) {
                    getSession().setAttribute("LoggedIn", "true");
                }
                loginBean.setSlotsAvailable();

                // Smooth the login -> index transition
                loginBean.startTransitionRender(
                        presentationManager.getRenderManager(),
                        presentation.getName() + firstName + lastName);

                return "loginSuccess";
            } else {
                loginBean.setSlotsNone();
            }
        }

        return "failed";
    }
    
   

    /**
     * Method to logout from the current presentation If the participant is a
     * moderator, the presentation will be ended, otherwise other viewers will
     * just be notified of the leave This will reset the login page renderer and
     * cleanup the local presentation variables
     * All the exceptions here are caught and ignored, as an error in one phase
     * of the logout should not stop the other phases from completing. Plus, there
     * isn't much we could do on a user logout error (they're already leaving after
     * all).
     *
     * @return "logout" for faces-config navigation
     */
    public String logout() {
        try {
            switch (role) {
                case ROLE_MODERATOR:
                    presentation.addChatMessage(firstName,
                                                "Moderator has logged out. End of presentation.");
                    break;
                case ROLE_VIEWER:
                    presentation.addChatMessage(firstName,
                                                "Left the presentation.");
                    break;
            }

            if (getSession() != null) {
                getSession().setAttribute("LoggedIn", "false");
            }
        } catch (Exception failedLogout1) { }

        try {
            presentation.deleteSkypeEntry(this.getSkype());
        } catch (Exception failedLogout2) { }
        
        try {
            presentation.removeParticipant(this);
            presentation = null;

            loginBean.addRenderable();
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
     * @return "confirmLogout" for faces-config navigation
     */
    public String confirmLogout() {
        confirmDialog = true;
        return "confirmLogout";
    }

    /**
     * Method called when the "Yes" button is clicked on the confirm logout
     * dialog
     *
     * @return "logout" for faces-config navigation
     */
    public String confirmLogoutYes() {
        confirmDialog = false;
        return logout();
    }

    /**
     * Method called when the "No" button is clicked on the confirm logout
     * dialog
     *
     * @return "confirmLogoutNo" for faces-config navigation
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
     * @return "switchModeratorsYes" for faces-config navigation
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
     * @return "switchModeratorsNo" for faces-config navigation
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
     * Method called when any level of rendering exception happens In the case
     * of a non-transient (ie: fatal) rendering exception, the user will be
     * logged out
     *
     * @param renderingException that occurred
     */
    public void renderingException(RenderingException renderingException) {
        if (log.isErrorEnabled()) {
            log.error("Rendering exception for " + firstName, renderingException);
        }
        
        // This is a failsafe created after it was noticed (on the live server) that
        // there is a possibility of continuous transient exceptions being thrown, without
        // a fatal exception eventually be thrown (as should happen)
        // This meant some users would close their browser, and their session would
        // fail to be properly destroyed on timeout, so instead they would be left in a
        // sort of 'limbo' of continuous transient exceptions
        // So now we keep track of transient exceptions, if MAXIMUM_TRANSIENT_EXCEPTIONS
        // occur within 10 seconds (or less) of each other, it is assumed that something has
        // gone wrong and the user will be logged out
        if (renderingException instanceof TransientRenderingException) {
            if ((System.currentTimeMillis() - lastTransientException) >= 10000) {
                transientExceptionCount++;
                lastTransientException = System.currentTimeMillis();
            }
            else {
                transientExceptionCount = 0;
            }
        }
        else {
            if (log.isErrorEnabled()) {
                if ((firstName != null) && (!firstName.equals(""))) {
                    log.error("Rendering exception was fatal, going to log " + firstName + " out");
                }
            }
            logout();
        }
        
        if (transientExceptionCount > MAXIMUM_TRANSIENT_EXCEPTIONS) {
            if (log.isErrorEnabled()) {
                if ((firstName != null) && (!firstName.equals(""))) {
                    log.error("The maximum transient exceptions (" + MAXIMUM_TRANSIENT_EXCEPTIONS + " has been reached for " + firstName + ", going to log them out");
                }
            }
            logout();
        }
    }

    /**
     * Method called when the first tab above the slide image is clicked
     *
     * @return "slideTypeOne" for faces-config navigation
     */
    public String toggleSlideTypeOne() {
        slideTypePres = true;

        return "slideTypeOne";
    }

    /**
     * Method called when the second tab above the slide image is clicked
     *
     * @return "slideTypeTwo" for faces-config navigation
     */
    public String toggleSlideTypeTwo() {
        slideTypePres = false;

        return "slideTypeTwo";
    }

    /**
     * Method to email the presentation's chat log to this participant A generic
     * body and header will be created, and sent using the JavaMail API which is
     * handled through MailTool
     *
     * @return "emailMessageLog" for faces-config navigation
     */
    public String emailMessageLog() {
        String header = (
                "This is an automated email message. Please do not reply to this address.\n" +
                "Message log for presentation " + this.presentation.getName() +
                ":\n" +
                "-----------------------------------------------------------------------\n"
        );

        StringBuffer content = new StringBuffer();
        for (int i = 0; i < this.presentation.getMessageLog().size(); i++) {
            content.append(((Message) this.presentation.getMessageLog().get(i))
                    .toString()).append("\n");
        }

        if (MailTool.sendMessage("Message Log from WebMC",
                                 (header + content.toString()),
                                 this.email)) {
            updateStatus("Successfully emailed chat log to " + email);
        }
        else {
            updateStatus("Failed to email chat log to " + email);
        }

        return "emailMessageLog";
    }

    /**
     * Method called when the http session listener tells us a session was created
     *
     * @param httpSessionEvent of the creation
     */
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
    }

    /**
     * Method called when the http session listener tells us a session was destroyed
     * This will get the singleton PresentationManager and let it know the session
     * was destroyed, which may result in a stale presentation being removed
     *
     * @param httpSessionEvent of the destruction
     */
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        String destroyedId = httpSessionEvent.getSession().getId();

        if (log.isInfoEnabled()) {
            log.info("Session destroyed for id: " + destroyedId);
        }

        PresentationManager.getInstance().destroySessionId(destroyedId);
    }

    public String getSkype() {
        return skype;
    }
}