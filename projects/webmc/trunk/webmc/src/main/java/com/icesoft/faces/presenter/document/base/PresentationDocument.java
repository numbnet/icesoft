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
package com.icesoft.faces.presenter.document.base;

import com.icesoft.faces.presenter.slide.Slide;

import java.io.File;

/**
 * Interface representing a document readable by the presenter Key elements are
 * handled such as the loading status, slide list and number of slides, etc.
 */
public interface PresentationDocument {
    /**
     * Method will load a File into a readable presentation (normally
     * involves steps of conversion)
     *
     * @param source file
     */
    public void load(File source);

    /**
     * Intended to closeDocument loading a document after loading has been
     * initiated
     */
    public void cancel();

    /**
     * Convenience method to get the loaded status of this document
     *
     * @return true if the document is loaded
     */
    public boolean isLoaded();

    /**
     * Method to set the loaded status of this document
     *
     * @param loaded status to use
     */
    public void setLoaded(boolean loaded);

    /**
     * Method to get a slide at the passed slide number
     *
     * @param slideNumber to get
     * @param mobile to get mobile browser sized slide
     * @return resulting slide
     */
    public Slide getSlide(int slideNumber, boolean mobile);

    /**
     * Convenience method to get the total number of slides in this document
     *
     * @return number of slides
     */
    public int getNumberOfSlides();

    /**
     * Method to get the deletion status of this document
     *
     * @return whether to delete the presentation on exit
     */
    public boolean deleteOnExit();

    /**
     * Method to get the deletion status of this document
     *
     * @param flag true to delete the presentation once converted
     */
    public void setDeleteOnExit(boolean flag);

    /**
     * Dispose of the existing document and any slides created during the
     * conversion
     */
    public void dispose();
    
    /**
     * Delete files generated from uploaded presentation.
     */
    public void deleteGeneratedFiles();
}