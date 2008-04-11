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
import com.icesoft.faces.presenter.util.ImageScaler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Class representing the source document used for the Presentation. The
 * Participant initiates the loading of a file which causes the
 * PptPresentationDocument to create slides for the presentation.
 */
public class PptPresentationDocument implements PresentationDocument {
    private static Log log = LogFactory.getLog(PptPresentationDocument.class);

    public static  String MS_PPT_PATH = "Microsoft PowerPoint";
    public static final String EXTRACTED_FOLDER = "extracted";
    public static final String URL_SLASH = "/";

    private boolean loaded = false;
    private int externalConverterFilePages;
    private int lastLoadedSlide = 0;
    private Presentation presentation;
    private File externalConverterFile;
    private Slide[] slides;
    private Slide[] slidesMobile;

    public PptPresentationDocument(Presentation presentation) {
        this.presentation = presentation;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    /**
     * Method to closeDocument the loading / conversion of the current document
     */
    public void cancel() {
        loaded = false;
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
     * Method to clean up the conversion process This includes canceling and
     * deleting any processed slides
     */
    public void dispose() {
        cancel();
        externalConverterFile = null;
    }

    /**
     * Convenience method to update the UI status message
     *
     * @param message to update with
     */
    private void updateStatus(String message) {
        presentation.getModerator().updateStatus(message);
    }

    /**
     * Method to load the passed File.  The conversion and loading will be done
     * in a separate thread
     *
     * @param sourceFile to load
     */
    public void load(File sourceFile) {
        externalConverterFile = sourceFile;
        PptLoader loader = new PptLoader();
        Thread pptLoader = new Thread(loader, "PowerPoint Loader");
        pptLoader.start();
    }

    /**
     * Internal class used to perform the background loading of a presentation
     */
    private class PptLoader implements Runnable {
        private void copyFile(FileInputStream in, FileOutputStream out)
                throws IOException {
		    byte[] buffer = new byte[1024];
            int len;
		
		    while ((len = in.read(buffer)) >= 0) {
		        out.write(buffer, 0, len);
		    }
		
		    in.close();
		    out.close();
		}

        public void run() {
            try {
                // Notify the moderator and log the path
                updateStatus("Unpacking and checking presentation " +
                             presentation.getName());

                // Get the absolute path to pass to PowerPoint
                String fullPath = externalConverterFile.getAbsolutePath();

                long ourTimestamp = System.currentTimeMillis();
                String hashString = String.valueOf(
                        (presentation.getPrefix() + ourTimestamp).hashCode() )
                        .substring(1,8);

                String baseDirectory = File.separator + "tmp" + File.separator + hashString;                
                File directoryStructure = new File(baseDirectory);

                fileCreator(fullPath,baseDirectory);

                File dest = new File(externalConverterFile.getParentFile() + 
                        File.separator + hashString);
                dest.deleteOnExit();
                directoryStructure.renameTo(dest);
                
                File[] generatedFiles = dest.listFiles();
                File[] generatedFilesMobile = new File[generatedFiles.length];
                
                File destMobile = new File(dest, "mobile");
                destMobile.mkdir();
                
                for(int i=0; i<generatedFiles.length; i++){
                    File toAdd = new File(destMobile, 
                    		generatedFiles[i].getName());
                    copyFile(new FileInputStream(generatedFiles[i]),
	                                new FileOutputStream(toAdd));

                    ImageScaler.aspectScaleImage(toAdd, Slide.MOBILE_MAX_WIDTH, Slide.MOBILE_MAX_HEIGHT);
                            
                    generatedFilesMobile[i]=toAdd;
                }
                
                Arrays.sort(generatedFiles, new SlideComparator());                
                slides = slideCreator(generatedFiles, hashString, false);
                Arrays.sort(generatedFilesMobile, new SlideComparator()); 
                slidesMobile = slideCreator(generatedFilesMobile, hashString, true);                
                
                externalConverterFilePages = slides.length;
                // We can show the navigation controls and set the first slide
                loaded = true;

                updateStatus("Completed loading presentation " +
                             presentation.getName() + " (" +
                             externalConverterFilePages + " pages)");
                presentation.setCurrentSlideNumber(1);
            } catch (Exception e) {
                updateStatus("Could not load a document because of " + e.getMessage());
                if (log.isErrorEnabled()) {
                    log.error("Could not load PowerPoint document for " +
                              presentation.getName(), e);
                }
            }
        }
        
	    private void fileCreator(String fullPath, String baseDirectory) throws Exception{
            String baseDirectoryMac = baseDirectory.replaceFirst(File.separator,"");
            baseDirectoryMac = baseDirectoryMac.replaceAll(File.separator,":");
	    	
	        String[] command = {
	                "osascript",
	                "-e",
	                "tell application \"" + MS_PPT_PATH + "\"",
	                "-e",
	                "open \"" + fullPath + "\" ",
	                "-e",
	                "do Visual Basic \"ActivePresentation.Export  Path:=\\\"" + 
	                        baseDirectoryMac + 
	                        "\\\", FilterName:=\\\"jpg\\\"\"",
	                "-e",
	                "close presentation 1 saving no",
	                "-e",
	               "end tell"
	            };
	            
	            Runtime rt = Runtime.getRuntime();
	            Process p = null;
	
	            p = rt.exec (command);
	            p.waitFor();    	
	    	
	    }
	
	    private Slide[] slideCreator(File[] files, String hashString, boolean mobile){
	    	String base;
	    	if(mobile){
	    		base = hashString + File.separator + "mobile";
	    	}else{
	    		base = hashString;
	    	}
	    	Slide[] slidesCreated = new Slide[files.length];
	        for (int i = 0; i < files.length; i++) {
	            slidesCreated[i] = new Slide( base + 
	                    File.separator + files[i].getName(),mobile );
	        }
	        
	        return slidesCreated;
	    }

    }

    /**
     * Internal class used to compare two slides and check for differences
     */
    private class SlideComparator implements Comparator {
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
