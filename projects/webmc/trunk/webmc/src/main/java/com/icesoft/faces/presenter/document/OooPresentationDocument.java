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

import javax.servlet.ServletContext;
import javax.faces.context.FacesContext;

import com.icesoft.faces.presenter.document.base.PresentationDocument;
import com.icesoft.faces.presenter.presentation.Presentation;
import com.icesoft.faces.presenter.slide.Slide;
import com.icesoft.faces.presenter.util.ImageScaler;
import com.icesoft.faces.presenter.util.MessageBundleLoader;

import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Class representing the source document used for the Presentation. The
 * Participant initiates the loading of a file which causes the
 * OooPresentationDocument to create slides for the presentation.
 */
public class OooPresentationDocument extends CommonPresentationDocument implements PresentationDocument {
    private static Log log = LogFactory.getLog(OooPresentationDocument.class);
    OfficeManager officeManager;

    public OooPresentationDocument(Presentation presentation) {
        this.presentation = presentation;
        officeManager = OooManager.getOfficeManager();
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
     * Method to clean up the conversion process This includes canceling and
     * deleting any processed slides
     */
    public void dispose() {
        cancel();
        externalConverterFile = null;
    }

    /**
     * Method to load the passed File.  The conversion and loading will be done
     * in a separate thread
     *
     * @param sourceFile to load
     */
    public void load(File sourceFile) {
        externalConverterFile = sourceFile;
        OooLoader oooLoader = new OooLoader();
        Thread loader = new Thread(oooLoader, "Ooo Loader");
        loader.start();
    }

    /**
     * Internal class used to perform the background loading of a presentation
     */
    private class OooLoader implements Runnable {
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
                updateStatus(MessageBundleLoader.getMessage("bean.presentationDocument.loading") + " " +
                             presentation.getName());

                // Get the absolute path to pass to PowerPoint
                String fullPath = externalConverterFile.getAbsolutePath();

                long ourTimestamp = System.currentTimeMillis();
                String hashString = String.valueOf(
                        (presentation.getPrefix() + ourTimestamp).hashCode() )
                        .substring(1,8);

                String baseDirectory = File.separator + "tmp" + File.separator + hashString;                
                File directoryStructure = new File(baseDirectory);

                fileCreator(fullPath, baseDirectory);

                File dest = new File(externalConverterFile.getParentFile().getCanonicalPath() + 
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
                int beginIndex = ROOT_CONTEXT.length();
                String slideBaseDirectory = dest.getCanonicalPath()
                        .substring(beginIndex);
                String slideMobileDirectory = slideBaseDirectory 
                        + File.separator + "mobile" + File.separator;

                slides = slideCreator(generatedFiles, slideBaseDirectory, false);
                Arrays.sort(generatedFilesMobile, new SlideComparator()); 
                slidesMobile = slideCreator(generatedFilesMobile, slideMobileDirectory, true);                
                
                externalConverterFilePages = slides.length;
                // We can show the navigation controls and set the first slide
                loaded = true;
                // After loading new slides, trigger preload of slides in the clients.
                presentation.preload();
                // Delete the uploaded file here once we are done extraction.
                externalConverterFile.delete();

                updateStatus(MessageBundleLoader.getMessage("bean.presentationDocument.completedLoading") + " " +
                             presentation.getName() + " (" +
                             externalConverterFilePages + " pages)");
                presentation.setCurrentSlideNumber(1);

            } catch (Exception e) {
                updateStatus(MessageBundleLoader.getMessage("bean.presentationDocument.loadingError") + " " + e.getMessage());
                if (log.isErrorEnabled()) {
                    log.error("Could not load PowerPoint document for " +
                              presentation.getName(), e);
                }
            }
        }
        
	    private void fileCreator(String fullPath, String baseDirectory) throws Exception{

            OfficeDocumentConverter converter = 
                    new OfficeDocumentConverter(officeManager);
            converter.convert( new File(fullPath), 
                    new File(baseDirectory + File.separator + "out.html") );
            //remove generated .html
            File baseDir = new File(baseDirectory);
            File[] files = baseDir.listFiles();
            for (int i = 0; i < files.length; i++)  {
                String name = files[i].getName();
                if (name.endsWith(".html"))  {
                    files[i].delete();
                }
            }
	    }
	
	    private Slide[] slideCreator(File[] files, String base, boolean mobile){
	    	Slide[] slidesCreated = new Slide[files.length];
	        for (int i = 0; i < files.length; i++) {
	            slidesCreated[i] = new Slide( base + 
	                    File.separator + files[i].getName(),mobile );
	        }
	        
	        return slidesCreated;
	    }

    }
    
    /**
     * Delete files generated from uploaded presentation.
     */
    public void deleteGeneratedFiles() {
    }

}
