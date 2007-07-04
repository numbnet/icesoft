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
package com.icesoft.faces.presenter.document;

import com.icesoft.faces.presenter.document.base.PresentationDocument;
import com.icesoft.faces.presenter.presentation.Presentation;
import com.icesoft.faces.presenter.slide.Slide;

import java.io.File;

/**
 * Class used to handle a document that does not match a known format Normally
 * the moderator would be notified of the invalid file, and no further action
 * would be taken
 */
public class UnknownPresentationDocument implements PresentationDocument {
    private Presentation presentation;
    private boolean loaded = false;

    public UnknownPresentationDocument(Presentation presentation) {
        this.presentation = presentation;
    }

    /**
     * Method to get the loaded status
     *
     * @return boolean loaded
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Method to set the loaded status
     *
     * @param loaded new status
     */
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    /**
     * Method to retrieve the Slide object corresponding to the passed slide
     * number
     *
     * @param slideNumber to get
     * @return Slide at slideNumber (or null if not found)
     */
    public Slide getSlide(int slideNumber) {
        return null;
    }

    /**
     * Convience method to get the total number of slides
     *
     * @return number of slides (or 0 on error)
     */
    public int getNumberOfSlides() {
        return 0;
    }

    /**
     * Method to closeDocument the loading / conversion of the current zip
     * document
     */
    public void cancel() {
    }

    /**
     * Method to clean up the conversion process This includes cancelling and
     * deleting any processed slides
     */
    public void dispose() {
    }

    /**
     * Method to load the passed File (which will be a zip) The conversion and
     * loading will be done in a seperate thread
     *
     * @param sourceFile to load
     */
    public void load(File sourceFile) {
        updateStatus(
                "Presentation cannot be loaded (cannot recognize the file format)");
    }

    /**
     * Convience method to update the UI status message
     *
     * @param message to update with
     */
    private void updateStatus(String message) {
        presentation.getModerator().updateStatus(message);
    }
}