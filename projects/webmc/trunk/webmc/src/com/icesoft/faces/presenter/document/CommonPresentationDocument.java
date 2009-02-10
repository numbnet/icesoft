package com.icesoft.faces.presenter.document;

import java.io.File;
import java.util.Comparator;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.icesoft.faces.presenter.presentation.Presentation;
import com.icesoft.faces.presenter.slide.Slide;

public class CommonPresentationDocument {
    private static Log log = LogFactory.getLog(CommonPresentationDocument.class);

    protected boolean loaded = false;
    
	protected Presentation presentation;
	
	protected int externalConverterFilePages;
    private int lastLoadedSlide = 0;
    protected File externalConverterFile;
    protected Slide[] slides;
    protected Slide[] slidesMobile;
    
    protected static String ROOT_CONTEXT = "";
    
    static {
        try {
        ROOT_CONTEXT = new File( ((ServletContext) FacesContext
                .getCurrentInstance().getExternalContext()
                .getContext()).getRealPath("") ).getCanonicalPath();
        } catch (Exception e)  {
            if (log.isErrorEnabled()) {
                log.error("Could not initialize presentation storage", e);
            }
        }
    }
    
    /**
     * Method to retrieve the Slide object corresponding to the passed slide
     * number
     *
     * @param slideNumber to get
     * @return Slide at slideNumber (or null if not found)
     */
    public Slide getSlide(int slideNumber, boolean mobile) {
        // Don't bother if the slide list is already null
        if (slides == null) {
            return null;
        }
        // Notification of page loading
        if (!loaded && slideNumber > lastLoadedSlide) {
            return null;
        }
        // Ensure a valid range is requested
        if (slideNumber < 1) {
            slideNumber = 1;
        }
        if (slideNumber > externalConverterFilePages) {
            slideNumber = externalConverterFilePages;
        }

        if (log.isTraceEnabled()) {
            log.trace("Returning slide number " + slideNumber + ": " +
                      slides[slideNumber - 1].getLocation());
        }
        // Mobile sized Slide for a mobile browser
        if(mobile){
        	return slidesMobile[slideNumber - 1];
        }
        // Desktop sized Slide for a desktop browser
        return slides[slideNumber - 1];
    }

    /**
     * Convenience method to get the total number of slides
     *
     * @return number of slides (or 0 on error)
     */
    public int getNumberOfSlides() {
        if (externalConverterFile == null) {
            return 0;
        }
        return externalConverterFilePages;
    }

    /**
     * Convenience method to update the UI status message
     *
     * @param message to update with
     */
    protected void updateStatus(String message) {
        presentation.getModerator().updateStatus(message);
    }
    
    private boolean isDeleteOnExit;

    /**
     * Method to get the deletion status of this document
     *
     * @return whether to delete the presentation on exit
     */
    public boolean deleteOnExit()  {
        return isDeleteOnExit;
    }

    /**
     * Method to get the deletion status of this document
     *
     * @param flag true to delete the presentation once converted
     */
    public void setDeleteOnExit(boolean flag)  {
        isDeleteOnExit = flag;
    }

    /**
     * Internal class used to compare two slides and check for differences
     */
    protected class SlideComparator implements Comparator {
        /**
         * Method to perform the actual comparison and sorting of files
         * This would be called automatically when this comparator is used
         * by a Collections.sort call
         *
         * @param a1 first file to compare
         * @param a2 second file to compare
         * @return 0 if the files are equal, 1 if a1 is above a2, -1 if a2 is above a1
         */
        public int compare(Object a1, Object a2) {
            File file1 = null;
            File file2 = null;
            if (a1 instanceof File) {
                file1 = (File) a1;
            }
            if (a2 instanceof File) {
                file2 = (File) a2;
            }

            int number1 = 0;
            int number2 = 0;
            if (file1 != null) {
                number1 = getNumberFromFile(file1.getName());
            }
            if (file2 != null) {
                number2 = getNumberFromFile(file2.getName());
            }

            if (number1 > number2) return 1;
            else if (number1 < number2) return -1;

            return 0;
        }

        /**
         * Wrapper method to safely determine if the passed object equals this
         *
         * @param obj to check
         * @return true if the objects are equal
         */
        public boolean equals(Object obj) {
            return obj != null && this.equals(obj);
        }

        /**
         * Convenience method to get numbers from a filename, in the hope of
         * ordering the files properly
         * For example, a file named Slide2.jpg would extract as 2, and therefore
         * could be sorted about 3, etc.
         *
         * @param name of the file
         * @return numbers in the file, or -1 on error / no numbers present
         */
        private int getNumberFromFile(String name) {
            try {
                String toReturn = "";
                char current;
                for (int i = 0; i < name.length(); i++) {
                    current = name.charAt(i);
                    if (Character.isDigit(current)) {
                        toReturn += current;
                    }
                    if (current == '.') {
                        break;
                    }
                }
                return Integer.parseInt(toReturn);
            } catch (Exception failed) {
                return -1;
            }
        }
    }

}
