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
     * Dispose of the existing document and any slides created during the
     * conversion
     */
    public void dispose();
}