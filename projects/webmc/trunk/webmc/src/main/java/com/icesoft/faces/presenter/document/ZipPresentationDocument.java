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
import com.icesoft.faces.presenter.util.MessageBundleLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Class representing the source document used for the Presentation. The
 * Participant initiates the loading of a file which causes the
 * ZipPresentationDocument to create slides for the presentation.
 */
public class ZipPresentationDocument extends CommonPresentationDocument implements PresentationDocument {
    private static Log log = LogFactory.getLog(ZipPresentationDocument.class);

    public static final String EXTRACTED_FOLDER = "extracted";
    public static final String URL_SLASH = "/";
    private static final int MIN_ENTRY_SIZE =
            1000; // minimum zip entry to include (in bytes)

    private String baseDirectory;
    private String mobileDirectory;
    
    public ZipPresentationDocument(Presentation presentation) {
        this.presentation = presentation;
    }

    public boolean isLoaded() {
        return loaded;
    }

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
     * Method to clean up the conversion process This includes canceling and
     * deleting any processed slides
     */
    public void dispose() {
        cancel();
        externalConverterFile = null;
    }

    /**
     * Method to load the passed File (which will be a zip) The conversion and
     * loading will be done in a separate thread
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
                updateStatus(MessageBundleLoader.getMessage("bean.presentationDocument.loading") + " " +
                             presentation.getName());

                // Setup the zipfile used to read entries
                ZipFile zf =
                        new ZipFile(externalConverterFile.getAbsolutePath());

                long ourTimestamp = System.currentTimeMillis();

                // Setup the base directory
                baseDirectory = externalConverterFile.getParentFile().getCanonicalPath() +
                                         File.separator + EXTRACTED_FOLDER +
                                         File.separator + presentation.getPrefix() +
                                         ourTimestamp + File.separator;

                mobileDirectory = baseDirectory + "mobile" + File.separator;
                
                // Ensure the extraction directories exist
                File directoryStructure = new File(mobileDirectory);
                if (!directoryStructure.exists()) {
                    directoryStructure.mkdirs();
                    directoryStructure.deleteOnExit();
                }

                // Loop through all entries in the zip and extract as necessary
                ArrayList generatedFiles = fileCreator(zf, baseDirectory, false);
              	ArrayList generatedFilesMobile = fileCreator(zf, mobileDirectory, true);
                zf.close();
                
                // Update the user if no slides were generated (which is unlikely)
                if (generatedFiles.size() < 1) {
                    if (log.isErrorEnabled()) {
                        log.error("No slides generated for " + presentation.getName());
                    }
                    updateStatus(MessageBundleLoader.getMessage("bean.zipPresentationDocument.noSlides1") + " " +
                                 presentation.getName() +
                                 MessageBundleLoader.getMessage("bean.zipPresentationDocument.noSlides2"));
                    return;
                }

                // Generate a list of Slide objects from the extracted files.
                // Convert a file path to a relative url using String objects.
                int beginIndex = ROOT_CONTEXT.length();
                String slideBaseDirectory;
                if(File.separator.equals("\\")){
                    slideBaseDirectory = baseDirectory.substring(beginIndex).replaceAll(File.separator + File.separator, URL_SLASH);
                }else{
            	    slideBaseDirectory = baseDirectory.substring(beginIndex);
                }
                String slideMobileDirectory = slideBaseDirectory + "mobile" + File.separator;
                externalConverterFilePages = generatedFiles.size();

                slides = slideCreator(generatedFiles,slideBaseDirectory,false);
                slidesMobile = slideCreator(generatedFilesMobile,slideMobileDirectory,true);

                // We can show the navigation controls and set the first slide
                loaded = true;
                // After loading new slides, trigger preload of slides in the clients.
                presentation.preload();
                // Delete the uploaded file here once we are done extraction.
                if (deleteOnExit())  {
                    externalConverterFile.delete();
                }

                updateStatus(MessageBundleLoader.getMessage("bean.presentationDocument.completedLoading") + " " +
                             presentation.getName() + " (" +
                             externalConverterFilePages + " pages)");
                presentation.setCurrentSlideNumber(1);
     
            } catch (Exception e) {
                updateStatus(MessageBundleLoader.getMessage("bean.presentationDocument.loadingError") + " " + e.getMessage());
                if (log.isErrorEnabled()) {
                    log.error("Could not load zip document for " +
                              presentation.getName(), e);
                }
            }
        }

        private Slide[] slideCreator(ArrayList generatedFiles, String slideBaseDirectory, boolean mobile){
            ArrayList slideArray = new ArrayList(0);
            File currentFile;
            for (int i = 0; i < generatedFiles.size(); i++) {
                currentFile = (File) generatedFiles.get(i);
                if (currentFile.getName().endsWith(".url"))  {
                    //read the .url file for a URL
                    String URL = null;
                    try {
                        URL = (new DataInputStream( 
                                new FileInputStream(currentFile))).readLine();
                        Slide slide = new Slide(URL, false);
                        //could base this on the URL extension
                        slide.setMovieSlide(true);
                        slideArray.add(slide);
                    } catch (Exception e)  {
                        if (log.isErrorEnabled()) {
                            log.error("Could not load .url file " +
                                      currentFile.getName(), e);
                        }
                    }
                } else {
                    slideArray.add(new Slide(
                            slideBaseDirectory + currentFile.getName(),mobile));
                }
            }
            return (Slide[]) slideArray
                    .toArray(new Slide[slideArray.size()]);    	
        }
        
        public ArrayList fileCreator(ZipFile zf, String baseDirectory, boolean mobile)throws IOException{
            ArrayList files = new ArrayList(0);
            ZipEntry currentEntry;
            int loopIndex = 0;
            for (Enumeration entries = zf.entries();
                 entries.hasMoreElements();) {
                currentEntry = (ZipEntry) entries.nextElement();
                if (!currentEntry.isDirectory()) {
                    loopIndex++;
                    File toAdd = new File(
                            baseDirectory, replaceUserFilename(currentEntry.getName(), loopIndex));
                    toAdd.deleteOnExit();
                    if (currentEntry.getSize() > MIN_ENTRY_SIZE) {
                        // Make sure to only deal with image files
                        if ((toAdd.getName().toLowerCase().indexOf(".jpg") != -1) ||
                            (toAdd.getName().toLowerCase().indexOf(".png") != -1) ||
                            (toAdd.getName().toLowerCase().indexOf(".gif") != -1)) {
	                        	copyInputStream(zf.getInputStream(currentEntry),
	                                            new BufferedOutputStream(
	                                                    new FileOutputStream(
	                                                            toAdd)));                            
                            // Scale the image as needed
                            if(mobile){
                            	ImageScaler.aspectScaleImage(toAdd, Slide.MOBILE_MAX_WIDTH, Slide.MOBILE_MAX_HEIGHT);	
                            }else{
                                ImageScaler.aspectScaleImage(toAdd, Slide.MAX_WIDTH, Slide.MAX_HEIGHT);
                            }
                            
                            files.add(toAdd);
                        }
                    } 
                    if (toAdd.getName().endsWith(".url")) {
                        copyInputStream(zf.getInputStream(currentEntry),
                                new BufferedOutputStream(new FileOutputStream(toAdd)));                            
                        files.add(toAdd);
                    }
                }
            }

            Collections.sort(files, new SlideComparator());
            
            return files;
        }
    
    }

    private String replaceUserFilename(String name, int slideNumber) {
        if (name.indexOf(".") != -1) {
            return "Slide" + slideNumber + name.substring(name.indexOf("."));
        }
        
        return "Slide" + slideNumber;
    }

    /**
     * Delete files generated from uploaded presentation.
     */
    public void deleteGeneratedFiles() {
    	deleteGeneratedFiles(mobileDirectory);
    	deleteGeneratedFiles(baseDirectory);
    }
    
    /**
     * Method to clean up the extracted files from a zip presentation
     *
     * @param directory to cleanup
     */
    private void deleteGeneratedFiles(String directory) {
        File targetFolder = new File(directory);
        File[] contents = targetFolder.listFiles();
        for (int i = 0; i < contents.length; i++) {
            contents[i].delete();
        }
        targetFolder.delete();
    }

}