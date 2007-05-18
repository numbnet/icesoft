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
import com.icesoft.faces.presenter.presentation.AutoPresentation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Class representing the source document used for the Presentation. The
 * Participant initiates the loading of a file which causes the
 * MultiPresentationDocument to create .png slides for the presentation.
 */
public class ZipPresentationDocument implements PresentationDocument {
    private static Log log = LogFactory.getLog(ZipPresentationDocument.class);

    public static final String EXTRACTED_FOLDER = "extracted";
    public static final String URL_SLASH = "/";
    private static final int MIN_ENTRY_SIZE =
            1000; // minimum zip entry to include (in bytes)

    private boolean loaded = false;
    private int externalConverterFilePages;
    private int lastLoadedSlide = 0;
    private Presentation presentation;
    private File externalConverterFile;
    private Slide[] slides;

    public ZipPresentationDocument(Presentation presentation) {
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
     * Method to closeDocument the loading / conversion of the current zip
     * document
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
    public Slide getSlide(int slideNumber) {
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

        return slides[slideNumber - 1];
    }

    /**
     * Convience method to get the total number of slides
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
     * Method to clean up the conversion process This includes cancelling and
     * deleting any processed slides
     */
    public void dispose() {
        cancel();
        externalConverterFile = null;
    }

    /**
     * Convience method to update the UI status message
     *
     * @param message to update with
     */
    private void updateStatus(String message) {
        presentation.getModerator().updateStatus(message);
    }

    /**
     * Method to load the passed File (which will be a zip) The conversion and
     * loading will be done in a seperate thread
     *
     * @param sourceFile to load
     */
    public void load(File sourceFile) {
        externalConverterFile = sourceFile;
        DocumentLoader loader = new DocumentLoader();
        Thread docLoader = new Thread(loader, "Document Loader");
        docLoader.start();
    }

    /**
     * Internal class used to perform the background loading of a zip slide
     * presentation
     */
    private class DocumentLoader implements Runnable {
        private void copyInputStream(InputStream in, OutputStream out)
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

                // Setup the zipfile used to read entries
                ZipFile zf =
                        new ZipFile(externalConverterFile.getAbsolutePath());

                // Make any necessary parent directories
                int indexOfFileFormat =
                        externalConverterFile.getName().lastIndexOf(".");
                String baseDirectory;
                long ourTimestamp = System.currentTimeMillis();

                // Setup the base directory, which can be based on two cases
                // The first is a user uploaded file, which goes right into web/
                // The second is a default initial presentation, which would be in
                // the web/PRESENTATION_FOLDER_NAME/ folder, and needs a different parent
                if (externalConverterFile.getParentFile().getName()
                        .equals(AutoPresentation.PRESENTATION_FOLDER_NAME)) {
                    baseDirectory = externalConverterFile.getParentFile()
                            .getParentFile() +
                                             File.separator + EXTRACTED_FOLDER +
                                             File.separator + presentation.getPrefix() +
                                             ourTimestamp + File.separator;
                } else {
                    baseDirectory = externalConverterFile.getParentFile() +
                                    File.separator + EXTRACTED_FOLDER + File.separator +
                                    presentation.getPrefix() + ourTimestamp + File.separator;
                }
                
                // Ensure the extraction directories exist
                File directoryStructure = new File(baseDirectory);
                if (!directoryStructure.exists()) {
                    directoryStructure.mkdirs();
                }

                // Loop through all entries in the zip and extract as necessary
                ArrayList generatedFiles = new ArrayList(0);
                ZipEntry currentEntry;
                int loopIndex = 0;
                for (Enumeration entries = zf.entries();
                     entries.hasMoreElements();) {
                    currentEntry = (ZipEntry) entries.nextElement();
                    if (!currentEntry.isDirectory()) {
                        loopIndex++;
                        File toAdd = new File(
                                baseDirectory, replaceUserFilename(currentEntry.getName(), loopIndex));
                        if (currentEntry.getSize() > MIN_ENTRY_SIZE) {
                            // Make sure to only deal with image files
                            if ((toAdd.getName().toLowerCase().indexOf(".jpg") != -1) ||
                                (toAdd.getName().toLowerCase().indexOf(".png") != -1) ||
                                (toAdd.getName().toLowerCase().indexOf(".gif") != -1)) {
                                copyInputStream(zf.getInputStream(currentEntry),
                                                new BufferedOutputStream(
                                                        new FileOutputStream(
                                                                toAdd)));
                                toAdd.deleteOnExit();
                                
                                // Scale the image as needed
                                ImageScaler.aspectScaleImage(toAdd);
                                
                                generatedFiles.add(toAdd);
                            }
                        }
                    }
                }

                Collections.sort(generatedFiles, new SlideComparator());

                // Update the user if no slides were generated (which is unlikely)
                if (generatedFiles.size() < 1) {
                    if (log.isErrorEnabled()) {
                        log.error("No slides generated for " + presentation.getName());
                    }
                    updateStatus("No slides were generated for " +
                                 presentation.getName() +
                                 ", is the zip file valid?");
                    return;
                }

                // Generate a list of Slide objects from the extracted files
                // This list can then be used by the presentation
                baseDirectory = EXTRACTED_FOLDER + URL_SLASH +
                                presentation.getPrefix() + ourTimestamp + URL_SLASH;
                externalConverterFilePages = generatedFiles.size();
                ArrayList slideArray = new ArrayList(0);
                File currentFile;
                for (int i = 0; i < generatedFiles.size(); i++) {
                    currentFile = (File) generatedFiles.get(i);
                    slideArray.add(new Slide(
                            baseDirectory + currentFile.getName()));
                }
                slides = (Slide[]) slideArray
                        .toArray(new Slide[slideArray.size()]);

                // We can show the navigation controls and set the first slide
                loaded = true;

                updateStatus("Completed loading presentation " +
                             presentation.getName() + " (" +
                             externalConverterFilePages + " pages)");
                presentation.setCurrentSlideNumber(1);
            } catch (Exception e) {
                updateStatus("Could not load a document because of " + e.getMessage());
                if (log.isErrorEnabled()) {
                    log.error("Could not load zip document for " +
                              presentation.getName(), e);
                }
            }
        }
    }
    
    private String replaceUserFilename(String name, int slideNumber) {
        if (name.indexOf(".") != -1) {
            return "Slide" + slideNumber + name.substring(name.indexOf("."));
        }
        
        return "Slide" + slideNumber;
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
         * Convience method to get numbers from a filename, in the hope of
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