package com.icesoft.icefaces.samples.showcase.components.fileUpload;

import org.apache.commons.logging.LogFactory;
import org.icefaces.application.showcase.view.bean.examples.component.inputFile.InputFileController;
import org.icefaces.application.showcase.view.bean.examples.component.inputFile.InputFileData;


import org.jboss.seam.annotations.Destroy;


import org.jboss.seam.annotations.Create;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;

import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;



import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;
import java.util.List;
import java.util.Map;


/**
 * <p>The InputFileBean class is the backing bean for the inputfile showcase
 * demonstration. It is used to store the state of the uploaded file.</p>
 *
 * @since 0.3.0
 */



//import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.ScopeType;

import com.icesoft.faces.async.render.RenderManager;
import com.icesoft.faces.async.render.Renderable;
import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import com.icesoft.faces.webapp.xmlhttp.RenderingException;

@Scope(ScopeType.SESSION)
@Name("inputFileController")
public class SeamInputFileController implements Serializable{
	@Logger Log log;

    // File sizes used to generate formatted label
    public static final long MEGABYTE_LENGTH_BYTES = 1048000l;
    public static final long KILOBYTE_LENGTH_BYTES = 1024l;

    // files associated with the current user
    private final List<InputFileData> fileList =
            Collections.synchronizedList(new ArrayList<InputFileData>());
    // latest file uploaded by client
    private InputFileData currentFile;
    // file upload completed percent (Progress)
    private int fileProgress;
    private String parentDirectory="";

    /**
     * <p>Action event method which is triggered when a user clicks on the
     * upload file button.  Uploaded files are added to a list so that user have
     * the option to delete them programatically.  Any errors that occurs
     * during the file uploaded are added the messages output.</p>
     *
     * @param event jsf action event.
     */
    public void uploadFile(ActionEvent event) {
        InputFile inputFile = (InputFile) event.getSource();
        if (inputFile.getStatus() == InputFile.SAVED) {
        	if (this.parentDirectory.equals("")){
        		this.parentDirectory = inputFile.getFile().getParent();
        	}
            // reference our newly updated file for display purposes and
            // added it to our history file list.
            currentFile = new InputFileData(inputFile.getFileInfo(),
                    inputFile.getFile());

            synchronized (fileList) {
            	if (!listContains(currentFile.getFile().getName())){
            		//only add the file to the list if it isn't already there
            		//fileupload will just update a more recent version of the file
                    fileList.add(currentFile);
            	}
            }
        }
    }
    protected boolean listContains(String fileNm){
    	for (InputFileData ifd: this.fileList){
    		if (ifd.getFileInfo().getFileName().trim().equals(fileNm.trim())){
    			return true;
    		}
    	}
    	return false;
    }

    /**
     * <p>This method is bound to the inputFile component and is executed
     * multiple times during the file upload process.  Every call allows
     * the user to finds out what percentage of the file has been uploaded.
     * This progress information can then be used with a progressBar component
     * for user feedback on the file upload progress. </p>
     *
     * @param event holds a InputFile object in its source which can be probed
     *              for the file upload percentage complete.
     */
    public void fileUploadProgress(EventObject event) {
        InputFile ifile = (InputFile) event.getSource();
        fileProgress = ifile.getFileInfo().getPercent();
    }

    /**
     * <p>Allows a user to remove a file from a list of uploaded files.  This
     * methods assumes that a request param "fileName" has been set to a valid
     * file name that the user wishes to remove or delete</p>
     *
     * @param event jsf action event
     */
    public void removeUploadedFile(ActionEvent event) {
        // Get the inventory item ID from the context.
        FacesContext context = FacesContext.getCurrentInstance();
        Map map = context.getExternalContext().getRequestParameterMap();
        String fileName = (String) map.get("fileName");

        synchronized (fileList) {
            InputFileData inputFileData;
            for (int i = 0; i < fileList.size(); i++) {
                inputFileData = (InputFileData)fileList.get(i);
                // remove our file
                if (inputFileData.getFileInfo().getFileName().equals(fileName)) {
                	//first remove it from the server
                	File f = new File(inputFileData.getFile().getAbsolutePath());
   	                if (f.exists()) { 
    	        		f.delete();
   	        	        if (log.isDebugEnabled())log.debug("File "+f.getName()+" is deleted");
    	            }

                	//then remove it from the list
                    fileList.remove(i);
                    break;
                }
            }
        }
    }

    public InputFileData getCurrentFile() {
        return currentFile;
    }

    public int getFileProgress() {
        return fileProgress;
    }

    public List getFileList() {
        return fileList;
    }
    public void destroy(){
    	log.info("destroying InputfileController");
	 	try{
		 	if (this.parentDirectory!=null){
		        /* remove the files that have been uploaded during this session */
		  		File[] files = null;
			    File dir = new File(parentDirectory);
			    /* use a FileFilter to only get files & not directories */
			    if (dir!=null){
			    	FileFilter fileFilter = new FileFilter(){
			    		public boolean accept(File file){
			    			return !file.isDirectory();
			    		}
			    	};
			    	files = dir.listFiles(fileFilter);
			    }
			    if (files!=null){
			    	for (int i=0; i< files.length; i++){
			    		File f = (File)files[i];
			    		String fname=f.getName();
			    		if (f.exists()){
			    			f.delete();
			    			log.info("\t\tfile: "+fname+" deleted");
	 		    			if (log.isDebugEnabled())log.debug("file: "+fname+" deleted");
			    		}
			    	}
			    }
		 	}
	    } catch(Exception ex) {
	    	/* no files to delete */
 	    	log.info("uploadDirectory is not set--no files to delete! exception");
 	    	ex.printStackTrace();
	    } 	
    }
}



