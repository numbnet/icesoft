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

import com.icesoft.faces.async.render.OnDemandRenderer;
import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.presenter.document.UnknownPresentationDocument;
import com.icesoft.faces.presenter.document.ZipPresentationDocument;
import com.icesoft.faces.presenter.document.base.PresentationDocument;
import com.icesoft.faces.presenter.document.factory.DocumentFactory;
import com.icesoft.faces.presenter.participant.Participant;
import com.icesoft.faces.presenter.participant.ParticipantInfo;
import com.icesoft.faces.presenter.slide.Slide;
import com.icesoft.faces.presenter.thread.SlideshowTimerBean;
import com.icesoft.faces.presenter.util.FileNameFilter;
import com.icesoft.faces.presenter.util.StringResource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.EventObject;

/**
 * Class used to handle the UI interaction and control of a presentation The
 * actual information of the presentation is held in PresentationInfo, this just
 * uses that information in the context of JSF pages
 */
public class Presentation extends PresentationInfo {
    private static final int DEFAULT_SLIDE_NUMBER = 0;

    private static Log log = LogFactory.getLog(Presentation.class);

    private int currentSlideNumber = DEFAULT_SLIDE_NUMBER;
    private int progress = -1;
    private String sessionId;
    private OnDemandRenderer renderer;
    private PresentationDocument document;
    private File parentFile;
    private ArrayList uploadedFiles = new ArrayList();
    private PresentationManagerBean manager;
    private Hashtable preloadedTable;
    private SlideshowTimerBean stimer = new SlideshowTimerBean(this);
    private long creationDate = System.currentTimeMillis();
    
    private ArrayList skypeList = new ArrayList();

    public Presentation() {
    }

    public Presentation(String name, String password, Participant moderator,
                        int maxParticipants, OnDemandRenderer renderer) {
        super(name, password, moderator, maxParticipants);
        this.renderer = renderer;
    }

    /**
     * Method to return the manager of this presentation
     *
     * @return the manager
     */
    public PresentationManagerBean getManager() {
        return manager;
    }

    /**
     * Method to get the slideshow timer
     *
     * @return stimer
     */
    public SlideshowTimerBean getTimer() {
        return stimer;
    }

    /**
     * Method to get the progress number
     *
     * @return progress
     */
    public int getProgress() {
        return progress;
    }

    /**
     * Method to get the session id
     *
     * @return sessionId
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Method to get the current slide number
     *
     * @return currentSlideNumber
     */
    public int getCurrentSlideNumber() {
        return currentSlideNumber;
    }
    
    /**
     * Method to get the creation date
     *
     * @return creationDate
     */
    public long getCreationDate() {
        return creationDate;
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
     * that if no slideshow is loaded, a different information slide will be
     * displayed for the moderator and viewer
     *
     * @param moderator true if the user is a moderator
     * @return the slide, or null on error
     */
    public Slide getPermissionSlide(boolean moderator) {
        return getSlide(currentSlideNumber, moderator);
    }

    /**
     * Method to return a Slide object for a given slide number This should be
     * called instead from a wrapper method such as getPermissionSlide
     *
     * @param slideNumber to get
     * @return the slide, or null if no document is loaded or an error occurred
     */
    private Slide getSlide(int slideNumber) {
        return getSlide(slideNumber, false);
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
    private Slide getSlide(int slideNumber, boolean isModerator) {
        if ((document == null) ||
            (currentSlideNumber == DEFAULT_SLIDE_NUMBER)) {
            return Slide.getDefaultSlide(isModerator);
        }

        Slide requestedSlide = document.getSlide(slideNumber);
        if (requestedSlide == null) {
            return Slide.getDefaultSlide(isModerator);
        }

        return requestedSlide;
    }

    /**
     * Method to get a series of slides that have not yet been displayed on the
     * page This is used by a hidden dataTable on the page, to attempt to
     * preload the next few slides
     *
     * @return list of Slides to preload
     */
    public Slide[] getPreloadSlides() {
        if (document == null) {
            return new Slide[0];
        }

        if (preloadedTable == null) {
            preloadedTable = new Hashtable(getLastSlideNumber());
        }

        if (preloadedTable.size() < getLastSlideNumber()) {
            Slide[] preloadSlides =
                    new Slide[PresentationManager.SLIDE_PRELOAD_COUNT];

            int stoppingPoint = currentSlideNumber + PresentationManager
                    .SLIDE_PRELOAD_COUNT;
            for (int i = currentSlideNumber; i < stoppingPoint; i++) {
                preloadSlides[i - currentSlideNumber] = getSlide(i);
                preloadedTable.put(new Integer(i), new Integer(i));
            }
            return preloadSlides;
        }

        return new Slide[0];
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
     * Method to set the manager owner of this presentation
     *
     * @param manager owner
     */
    public void setManager(PresentationManagerBean manager) {
        this.manager = manager;
    }

    /**
     * Method to set the progress number
     *
     * @param progress new
     */
    public void setProgress(int progress) {
        this.progress = progress;
    }

    /**
     * Method to set the session id
     *
     * @param sessionId new
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    
    /**
     * Method to set the creation date
     *
     * @param creationDate new
     */
    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

   /**
     * Method to set a specific slide number This doesn't actually set a
     * variable, but is needed to fufill the JSF page requirements
     *
     * @param slide to set
     */
    public void setSpecificSlideNumber(Object slide) {
    }

    /**
     * Method to set the current slide number The passed value can be invalid
     * and will be fixed before setting If the slide number is less than 1, it
     * becomes 1, if it is greater than the end of the current slideshow, it
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
     * Wrapper method to start the autoplay slideshow
     */
    public void startAutoPlay() {
        if (!stimer.isRunning()) {
            stimer.startSlideshow();
            addChatMessage(moderator.getFirstName(), "Slideshow mode started.");
        } else {
            moderator.updateStatus("Slideshow mode is already running");
        }
    }

    /**
     * Wrapper method to stop the autoplay, with status messages enabled
     */
    public void stopAutoPlay() {
        stopAutoPlay(false);
    }

    /**
     * Method to stop the running autoplay slideshow, and recreate the timer in
     * case autoplay is to be restarted
     *
     * @param silent true to hide status messages
     */
    public void stopAutoPlay(boolean silent) {
        if (stimer.isRunning()) {
            stimer.stopSlideshow();
            stimer = new SlideshowTimerBean(this);
            if (!silent) {
                addChatMessage(moderator.getFirstName(), "Slideshow mode stopped.");
            }
        } else {
            if (!silent) {
                moderator.updateStatus("Slideshow mode was not running");
            }
        }
    }

    /**
     * Method called from the front end pages when the autoplay checkbox is
     * changed by the moderator. This will either start or stop the slideshow
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
                        "Invalid page number \'" + fetchedValue + "\' entered");
            }
        } else {
            moderator.updateStatus("Invalid blank page number entered");
        }
    }

    /**
     * Method to add a participant to this presentation, and the associated
     * renderer
     *
     * @param participant to add
     */
    public void addParticipant(Participant participant) {
        renderer.add(participant);

        if (log.isInfoEnabled()) {
            log.info("Added participant " + participant.getFirstName() + " to " + name + " (renderer: " + renderer + ")");
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
            renderer.remove(participant);
            participants.remove(participant);
        }catch (Exception removeError) {
            if (log.isErrorEnabled()) {
                removeError.printStackTrace();
                log.error("Error removing participant " +
                          participant.getFirstName() +
                          " from the presentation " + name +
                          " as well as the renderer " + renderer);
            }
        }

        if (participant.isModerator()) {
            endPresentation();
        }
    }

    /**
     * Method to stop this presentation This means cancelling autoplay, cleaning
     * up the document and associated files, and letting the manager know the
     * presentation is done
     */
    public void endPresentation() {
        try {
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

    /**
     * Method to determine if the creation date of this presentation is still
     * valid in comparison to PRESENTATION_TIMEOUT_MINS.
     *
     * @return false if presentation age is more than the timeout
     */
    public boolean isCreationDateValid() {
        long diffMinutes = Math.abs((System.currentTimeMillis() - creationDate)/(60*1000));
        
        return (diffMinutes < PresentationManagerBean.PRESENTATION_TIMEOUT_MINS);
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
        getModerator()
                .updateStatus("Uploading your presentation, please wait...");

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
                        "Presentation cannot be loaded (maximum filesize was exceeded)");
                break;
            default:
                closeUploadDialog();
                getModerator().updateStatus(
                        "Presentation cannot be loaded (specify a valid file to use)");
                break;
        }
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
        parentFile = toLoad;

        // Stop and reset any previous presentation
        stopAutoPlay(true);
        currentSlideNumber = DEFAULT_SLIDE_NUMBER;

        log.info("Loading slideshow \'" + parentFile.getName() + "\' for " + name);

        // If the file is not a default presentation, then it should be
        // removed normally when the presentation ends, or worst case when
        // the app server shuts down
        if (!isDefault) {
            uploadedFiles.add(parentFile);
            parentFile.deleteOnExit();
        }

        // Clean up the old document if it exists
        if (document != null) {
            document.dispose();
            document = null;
        }

        // Force a new preload of the images
        preloadedTable = null;

        // Close the upload dialog on the page
        closeUploadDialog();
        requestOnDemandRender();

        // Get a new document from the factory, based on the input file
        document = DocumentFactory.createDocument(parentFile, this);
        document.load(parentFile);

        // If the presentation format is unknown / invalid, clear the document
        if (document instanceof UnknownPresentationDocument) {
            document.dispose();
            document = null;
            return;
        }

        // Start autoplay as needed
        if (getAutoPlay()) {
            startAutoPlay();
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
     * filesystem artifacts
     */
    public void closeDocument() {
        if (document == null) {
            return;
        }

        document.dispose();
        document = null;
        currentSlideNumber = DEFAULT_SLIDE_NUMBER;
        deleteFiles();
    }

    /**
     * Method to clean up the files created by a presentation This is normally
     * .zip and image files If a .zip was found, the extracted files will also
     * be removed
     */
    private void deleteFiles() {
        for (int i = 0; i < uploadedFiles.size(); i++) {
            File uploadedFile = (File) uploadedFiles.get(i);
            int indexOfFileFormat = uploadedFile.getName().lastIndexOf(".");
            String fileNameWithoutType =
                    uploadedFile.getName().substring(0, indexOfFileFormat);
            
            if (uploadedFile.getName().toLowerCase().indexOf(".zip") != -1) {
                deleteExtractedFiles(fileNameWithoutType);
            }

            File[] contents = uploadedFile.getParentFile()
                    .listFiles(new FileNameFilter(fileNameWithoutType));
            for (int y = 0; y < contents.length; y++) {
                contents[y].delete();
            }
         }
    }

    /**
     * Method to clean up the extracted files from a zip presentation
     *
     * @param fileNameWithoutType to cleanup
     */
    private void deleteExtractedFiles(String fileNameWithoutType) {
        File targetFolder = new File(parentFile.getParentFile() + File
                .separator + ZipPresentationDocument
                .EXTRACTED_FOLDER + File.separator + getPrefix() + "-" + fileNameWithoutType);
        File[] contents = targetFolder.listFiles();

        for (int i = 0; i < contents.length; i++) {
            contents[i].delete();
        }
        targetFolder.delete();
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

            current.updateStatus(firstName + ": \"" + chatMessage + "\"");
        }
        requestOnDemandRender();
    }

    /**
     * Convience method to get the last position in the chat log
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

        addChatMessage(moderator.getFirstName(), "Moderation passed off to " +
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
        if (event != null) {
            // Set the new value of progress
            InputFile file = (InputFile)event.getSource();
            if (file != null) {
                this.progress = file.getFileInfo().getPercent();

                // Render so the user sees the new progress
                requestOnDemandRender();
            }
        }
    }

    /**
     * Convience method to safely call request render
     */
    public void requestOnDemandRender() {
        if (renderer != null) {
            renderer.requestRender();
        }
        else {
            if (log.isErrorEnabled()) {
                log.error("Renderer for presentation " + name + " is null");
            }
        }
    }
}