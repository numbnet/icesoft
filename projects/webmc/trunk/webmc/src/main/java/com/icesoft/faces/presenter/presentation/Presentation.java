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
package com.icesoft.faces.presenter.presentation;

import org.icefaces.application.PushRenderer;
import org.icefaces.application.PortableRenderer;
//import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.presenter.document.UnknownPresentationDocument;
import com.icesoft.faces.presenter.document.ZipPresentationDocument;
import com.icesoft.faces.presenter.document.base.PresentationDocument;
import com.icesoft.faces.presenter.document.factory.DocumentFactory;
import com.icesoft.faces.presenter.participant.Participant;
import com.icesoft.faces.presenter.participant.ParticipantInfo;
import com.icesoft.faces.presenter.slide.Slide;
import com.icesoft.faces.presenter.timer.SlideshowTimerBean;
import com.icesoft.faces.presenter.util.MessageBundleLoader;
import com.icesoft.faces.presenter.util.StringResource;

import org.icefaces.component.fileentry.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.EventObject;

/**
 * Class used to handle the UI interaction and control of a presentation The
 * actual information of the presentation is held in PresentationInfo, this 
 * class uses that information in the context of JSF pages.
 */
public class Presentation extends PresentationInfo {
    private static final int DEFAULT_SLIDE_NUMBER = 0;

    private static Log log = LogFactory.getLog(Presentation.class);

    private int currentSlideNumber = DEFAULT_SLIDE_NUMBER;
    private int progress = -1;
    private long lastFileProgressTime = 0L;
    private PresentationDocument document;
    private File parentFile;
    private PresentationManagerBean manager;
    private SlideshowTimerBean stimer = new SlideshowTimerBean(this);
    private PortableRenderer renderer = PushRenderer.getPortableRenderer();
    boolean usingPointer = false;
    private int pointerX = 0;
    private int pointerY = 0;
    
    private ArrayList skypeList = new ArrayList();

    public Presentation() {
    }

    public Presentation(String name, String password, Participant moderator) {
        super(name, password, moderator);
    }

    public int getCurrentSlideNumber() {
        return currentSlideNumber;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public PresentationManagerBean getManager() {
        return manager;
    }
    
    public void setManager(PresentationManagerBean manager) {
        this.manager = manager;
    }

    public SlideshowTimerBean getTimer() {
        return stimer;
    }

	public boolean isUsingPointer() {
		return usingPointer;
	}

	public void setUsingPointer(boolean usingPointer) {
		this.usingPointer = usingPointer;
	}

	public int getPointerX() {
		return pointerX;
	}

	public void setPointerX(int pointerX) {
		this.pointerX = pointerX;
	}

	public int getPointerY() {
		return pointerY;
	}

	public void setPointerY(int pointerY) {
		this.pointerY = pointerY;
	}

    /**
     * Method to get the total number of slides in the current document
     *
     * @return the slide count, or -1 if no document is loaded
     */
    public int getLastSlideNumber() {
        if (document == null) {
            return -1;
        }

        return document.getNumberOfSlides();
    }

    /**
     * Method to return the current slide number as an object This is used by
     * the front end to allow a user to set a slide number in the presentation
     *
     * @return object version of currentSlideNumber
     */
    public Object getSpecificSlideNumber() {
        return new Integer(currentSlideNumber);
    }

    /**
     * Method to get a slide with the proper permissions This is mainly done so
     * that if no slide show is loaded, a different information slide will be
     * displayed for the moderator and viewer
     *
     * @param moderator true if the user is a moderator
     * @return the slide, or null on error
     */
    public Slide getPermissionSlide(boolean moderator, boolean mobile) {
        return getSlide(currentSlideNumber, moderator, mobile);
    }

    /**
     * Method to return a Slide object for a given slide number This should be
     * called instead from a wrapper method such as getPermissionSlide
     *
     * @param slideNumber to get
     * @return the slide, or null if no document is loaded or an error occurred
     */
    public Slide getSlide(int slideNumber, boolean mobile) {
        return getSlide(slideNumber, false, mobile);
    }

    /**
     * Method to return a Slide object for a given slide number This should be
     * called instead from a wrapper method such as getPermissionSlide The
     * method will also get the proper default information slide if needed,
     * based on the type of user requesting the slide object
     *
     * @param slideNumber to get
     * @param isModerator true if the requester is a moderator
     * @return the slide, or null if no document is loaded or an error occurred
     */
    private Slide getSlide(int slideNumber, boolean isModerator, boolean mobile) {
        if ((document == null) ||
            (currentSlideNumber == DEFAULT_SLIDE_NUMBER)) {
            return Slide.getDefaultSlide(isModerator, mobile);
        }

        Slide requestedSlide = document.getSlide(slideNumber, mobile);
        if (requestedSlide == null) {
            return Slide.getDefaultSlide(isModerator, mobile);
        }

        return requestedSlide;
    }

   /**
     * Method to set a specific slide number This doesn't actually set a
     * variable, but is needed to fulfill the JSF page requirements
     *
     * @param slide to set
     */
    public void setSpecificSlideNumber(Object slide) {
    }

    /**
     * Method to set the current slide number The passed value can be invalid
     * and will be fixed before setting If the slide number is less than 1, it
     * becomes 1, if it is greater than the end of the current slide show, it
     * loops around and also becomes 1. In addition this method will request a
     * render to show the new slide to all the participants
     *
     * @param slideNumber to change to
     */
    public void setCurrentSlideNumber(int slideNumber) {
        if (slideNumber < 1) {
            slideNumber = 1;
        }
        if ((getLastSlideNumber() > 0) &&
            (slideNumber > getLastSlideNumber())) {
            slideNumber = 1;
        }
        if (slideNumber == currentSlideNumber) {
            return;
        }
        currentSlideNumber = slideNumber;

        if (log.isTraceEnabled()) {
            log.trace("Current slide number is now " + currentSlideNumber);
        }

        requestOnDemandRender();
    }

    /**
     * Method to determine which image to load for the various moderator control
     * buttons. Returning a false would mean the faded / disabled button is
     * shown. Return true just shows the normal enabled buttons
     *
     * @return true to show the normal control button images
     */
    public boolean getImageLoaded() {
        if (!isLoaded()) {
            return false;
        }
        if (getAutoPlay()) {
            return false;
        }
        return true;
    }

    /**
     * Method to determine if a document is loaded or not
     *
     * @return true if the current document is not null and is loaded
     */
    public boolean isLoaded() {
        return document != null && document.isLoaded();
    }

    /**
     * Wrapper method to start the auto-play slide show
     */
    public void startAutoPlay() {
        if (!stimer.isRunning()) {
            stimer.startSlideshow();
            addChatMessage(moderator.getFirstName(), MessageBundleLoader.getMessage("bean.presentation.startAutoPlay.chatMessage"));
        } else {
            moderator.updateStatus(MessageBundleLoader.getMessage("bean.presentation.startAutoPlay.moderator.statusMessage"));
        }
    }

    /**
     * Wrapper method to stop the auto-play, with status messages enabled
     */
    public void stopAutoPlay() {
        stopAutoPlay(false);
    }

    /**
     * Method to stop the running auto-play slide show, and recreate the timer 
     * in case auto-play is to be restarted
     *
     * @param silent true to hide status messages
     */
    public void stopAutoPlay(boolean silent) {
    	if (stimer.isRunning()) {
            stimer.stopSlideshow();
            stimer = new SlideshowTimerBean(this);
            if (!silent) {
                addChatMessage(moderator.getFirstName(), MessageBundleLoader.getMessage("bean.presentation.stopAutoPlay.chatMessage"));
            }
        } else {
            if (!silent) {
                moderator.updateStatus(MessageBundleLoader.getMessage("bean.presentation.stopAutoPlay.moderator.statusMessage"));
            }
        }
    }

    /**
     * Method called from the front end pages when the auto-play checkbox is
     * changed by the moderator. This will either start or stop the slide show
     * based on the new value
     *
     * @param vce event of the change
     */
    public void autoPlayChange(ValueChangeEvent vce) {
        Object newValue = vce.getNewValue();

        if (newValue != null) {
            if (isLoaded()) {
                if (Boolean.TRUE.equals(newValue)) {
                    startAutoPlay();
                } else {
                    stopAutoPlay();
                }
            }
        }
    }

    /**
     * Method called from the front end pages when pointer checkbox is
     * changed by the moderator.
     *
     * @param vce event of the change
     */
    public void pointerChange(ValueChangeEvent vce) {
        requestOnDemandRender();
    }
    
    /**
     * Method called from the front end pages when the specific slide number the
     * moderator can enter has changed
     *
     * @param vce of the change
     */
    public void specificSlideNumberChanged(ValueChangeEvent vce) {
        Object fetchedValue = vce.getNewValue();

        if ((fetchedValue != null) &&
            (fetchedValue.toString().trim().length() > 0)) {
            try {
                setCurrentSlideNumber(
                        Integer.parseInt(fetchedValue.toString()));
            } catch (NumberFormatException nfe) {
                moderator.updateStatus(
                		MessageBundleLoader.getMessage("bean.presentation.specificSlideNumberChanged.invalid") + " \'" + fetchedValue + "\' entered");
            }
        } else {
            moderator.updateStatus(MessageBundleLoader.getMessage("bean.presentation.specificSlideNumberChanged.invalidBlank"));
        }
    }

    /**
     * Method to add a participant to this presentation, and the associated
     * renderer
     *
     * @param participant to add
     */
    public void addParticipant(Participant participant) {
        PushRenderer.addCurrentSession(name);
        if (log.isInfoEnabled()) {
            log.info("Added participant " + participant.getFirstName() + " to " + name );
        }
        if (!participants.contains(participant)) {
            participants.add(participant);
        }
        skypeList.add(participant.getSkype());
    }
    
    /**
     * Method to return the array list of skype names as a string that skype can use
     *@return String
     */
    public String getSkypeNames()
    {
        String names = "";
        for (int i = 1;i<skypeList.size();i++)
        {
            if(skypeList.get(i).equals(""))
            {

            }
            else if(i == (skypeList.size()-1))
            {
                names = names+skypeList.get(i);
            }
            else
            {
            names = names+skypeList.get(i)+";";
            }
        }
        return names;
    }

    public String getSkypeScript()  {
        String skypeURL = "skype:" + getSkypeNames() + "?call";
        return "window.open('" + skypeURL + "','skypewindow','width=10,height=10');";
    }
    
    /**
     * Method to remove a participants skype name from the call list
     *@param entry to remove
     */
    public void deleteSkypeEntry(String entry)
    {
        skypeList.remove(entry);
    }

    /**
     * Method to remove a participant from this presentation, and the associated
     * renderer. Also, if the participant was a moderator the presentation will
     * be ended
     *
     * @param participant to remove
     */
    public void removeParticipant(Participant participant) {
        try {
            PushRenderer.removeCurrentSession(name);
            participants.remove(participant);
        }catch (Exception removeError) {
            if (log.isErrorEnabled()) {
                removeError.printStackTrace();
                log.error("Error removing participant " +
                          participant.getFirstName() +
                          " from the presentation " + name );
            }
        }
        if (participant.isModerator()) {
            endPresentation();
        }
    }

    /**
     * Method to stop this presentation This means canceling auto-play, removing
     * the pointer, cleaning up the document and associated files, and letting 
     * the manager know the presentation is done.
     */
    public void endPresentation() {
        try {
        	usingPointer = false;
            stopAutoPlay(true);
            closeDocument();
        }catch (Exception failedMinor) {
            if (log.isErrorEnabled()) {
                log.error(
                        "Failed to close the presentation document because of : " +
                        failedMinor.getMessage());
            }
        }
        if (manager != null) {
            manager.endPresentation(this);
        } else {
            PresentationManager.getInstance().removePresentation(this);
        }
        if (log.isInfoEnabled()) {
            log.info("Presentation " + name + " has concluded");
        }
    }

	public void loadFile(FileEntryEvent event) {
		System.out.println("*** file event ***");
		
        FileEntry fileEntry = (FileEntry) event.getSource();
        FileEntryResults results = fileEntry.getResults();
		FileEntryResults.FileInfo fileInfo = results.getFiles().get(0);
		if (fileInfo != null) {
			if (fileInfo.isSaved()) {
				load(fileInfo.getFile(), false);
			}
		}
	}
	
    /**
     * Method called when a new presentation is loaded through the front page
     * parentFile upload component. This basically ensures that a valid
     * parentFile is present, and uses the DocumentFactory to load the desired
     * document
     *
     * @param event of the load
     */
    public void load(ActionEvent event) {
System.out.println("Update to new FileEntry component");
//commenting out InputFile related
/*
        getModerator()
                .updateStatus(MessageBundleLoader.getMessage("bean.presentation.load.moderatorStatus"));
        // Get a valid input file component that triggered the event
        InputFile inputFileComponent;
        if (event != null) {
            inputFileComponent = (InputFile) event.getSource();
        } else {
            if (log.isErrorEnabled()) {
                log.error("Load failed because the event was null");
            }
            closeUploadDialog();
            return;
        }
        // Determine what to do based on the input file component status
        // Ideally the status is InputFile.SAVED, in which case the file will
        //  be set, and the list of uploadedFiles increased
        switch (inputFileComponent.getStatus()) {
            case InputFile.SAVED:
                load(inputFileComponent.getFile(), false);
                break;
            case InputFile.SIZE_LIMIT_EXCEEDED:
                closeUploadDialog();
                getModerator().updateStatus(
                		MessageBundleLoader.getMessage("bean.presentation.load.moderatorStatus.sizeLimitExceeded"));
                break;
            default:
                closeUploadDialog();
                getModerator().updateStatus(
                		MessageBundleLoader.getMessage("bean.presentation.load.moderatorStatus.invalidFile"));
                break;
        }
*/
    }

    /**
     * Generic load method that can be called externally to force load a file
     * into this presentation
     * This method removes the reliance on the inputFile component, or page level
     * interaction
     *
     * @param toLoad new presentation
     * @param isDefault if the presentation is a generic initial presentation
     */
    public void load(File toLoad, boolean isDefault) {
        // Stop and reset any previous presentation
        stopAutoPlay(true);
        currentSlideNumber = DEFAULT_SLIDE_NUMBER;
    	
    	// Clean up the old document if it exists
        if (document != null) {
        	closeDocument();
        }
    	
    	parentFile = toLoad;
        // delete the file when the app server shuts down.
        if (!isDefault)  {
            parentFile.deleteOnExit();
        }

        log.info("Loading slideshow \'" + parentFile.getName() + "\' for " + name);

        // Close the upload dialog on the page
        closeUploadDialog();
        requestOnDemandRender();

        // Get a new document from the factory, based on the input file
        document = DocumentFactory.createDocument(parentFile, this);
        //do not delete the built-in presentations
        document.setDeleteOnExit(!isDefault);
        document.load(parentFile);

        // If the presentation format is unknown / invalid, clear the document
        if (document instanceof UnknownPresentationDocument) {
            document.dispose();
            document = null;
            return;
        }

        // Start auto-play as needed
        if (getAutoPlay()) {
            startAutoPlay();
        }
    }

    /**
     * Method to trigger preloading of slides.  This is called when the 
     * moderator of an existing presentation uploads new slides.
     * 
     */
    public void preload(){
        for(int i=0; i<participants.size(); i++){
        	((Participant)participants.get(i)).preload();
        }
    }
    
    
    /**
     * Navigation method to move the slides back one slide
     *
     * @param event of the navigation
     */
    public void backOne(ActionEvent event) {
        setCurrentSlideNumber(currentSlideNumber - 1);
    }

    /**
     * Navigation method to move the slides back to the start
     *
     * @param event of the navigation
     */
    public void backAll(ActionEvent event) {
        setCurrentSlideNumber(0);
    }

    /**
     * Navigation method to move the slides forward one slide
     *
     * @param event of the navigation
     */
    public void forwardOne(ActionEvent event) {
        setCurrentSlideNumber(currentSlideNumber + 1);
    }

    /**
     * Navigation method to move the slides forward to the end
     *
     * @param event of the navigation
     */
    public void forwardAll(ActionEvent event) {
        setCurrentSlideNumber(getLastSlideNumber());
    }

    /**
     * Method called when the moderator wishes to load a new presentation This
     * simply displays the upload dialog for the moderator
     *
     * @param event of the action
     */
    public void newDocument(ActionEvent event) {
        progress = -1;
        moderator.setUploadDialog(true);
    }

    /**
     * Method called when the moderator closes the upload dialog
     *
     * @return null for faces-config navigation
     */
    public String closeUploadDialog() {
        moderator.setUploadDialog(false);
        moderator.refreshChatMessageFocus();

        return null;
    }

    /**
     * Method called to cleanup the existing document, as well as any related
     * file system artifacts
     */
    public void closeDocument() {
        if (document == null) {
            return;
        }
        document.deleteGeneratedFiles();
        document.dispose();
        document = null;
        currentSlideNumber = DEFAULT_SLIDE_NUMBER;

    }

    /**
     * Method to add a new chat message to the existing presentation log
     *
     * @param firstName   of the user
     * @param chatMessage text to add
     */
    public void addChatMessage(String firstName, String chatMessage) {
        // Check if the message is null or empty
        if ((chatMessage == null) || (chatMessage.trim().length() == 0)) {
            return;
        }

        // Remove tags and check for URL linkage
        chatMessage = StringResource.cleanTags(chatMessage);
        chatMessage = StringResource.urlRecognize(chatMessage);

        // Add message to log, position Participants view at bottom of messageLog
        messageLog.addMessage(firstName, chatMessage);

        Participant current;
        for (int i = 0; i < participants.size(); i++) {
            current = ((Participant) participants.get(i));
            if (current.getChatView().getPosition() == (bottom() - 1)) {
                current.getChatView().setPosition(bottom());
            }
            current.buildMessageEffect();
            current.updateStatus(firstName + ": \"" + chatMessage + "\"");
        }
        requestOnDemandRender();
    }

    /**
     * Convenience method to get the last position in the chat log
     *
     * @return bottom index of the chat log
     */
    public int bottom() {
        if (messageLog != null) {
            return (messageLog.size() - 1);
        }
        return 0;
    }

    /**
     * Method called when a moderator wishes to transfer their abilities to
     * another user
     *
     * @param index of the new moderator (in the participant list)
     */
    public void switchModerators(int index) {
        Participant newModerator = getParticipantAt(index);
        newModerator.setRole(ParticipantInfo.ROLE_MODERATOR);
        addChatMessage(moderator.getFirstName(), MessageBundleLoader.getMessage("bean.presentation.switchModerators.chatMessage") + " " +
                                                 newModerator.getFirstName());
        skypeList.add(moderator.getSkype());
        moderator = newModerator;
        skypeList.remove(moderator.getSkype());
    }

    /**
     * Method called from the page when the progress of the file upload changes
     * This is used to update the progress percent value, which is displayed as
     * a progress bar on the upload dialog
     *
     * @param event of the change
     */
    public void progressChange(EventObject event) {
System.out.println("file upload not supported yet");
//commenting out upload pending FileEntry
/*
        if (event != null) {
            // Set the new value of progress
            InputFile file = (InputFile)event.getSource();
            if (file != null) {
                this.progress = file.getFileInfo().getPercent();

                // Render so the user sees the new progress
                long now = System.currentTimeMillis();
                if(  ( (now - lastFileProgressTime) >= 1000L ) ||
                     this.progress == 0 ||
                     this.progress == 100  )
                {
                    lastFileProgressTime = now;
                    System.out.println("Requesting render for progress " + this.progress);
                    requestOnDemandRender();
                }
            }
        }
*/
    }

    /**
     * Convenience method to safely call request render
     */
    public void requestOnDemandRender() {
        renderer.render(name);
    }

}
