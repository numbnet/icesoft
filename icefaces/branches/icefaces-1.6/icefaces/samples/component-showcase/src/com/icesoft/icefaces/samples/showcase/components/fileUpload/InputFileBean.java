package org.icefaces.application.showcase.component.fileUpload;
/* Version: MPL 1.1/GPL 2.0/LGPL 2.1
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


import com.icesoft.faces.async.render.RenderManager;
import com.icesoft.faces.async.render.Renderable;
import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import com.icesoft.faces.webapp.xmlhttp.RenderingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

/**
 * <p>The InputFileBean class is the backing bean for the inputfile showcase
 * demonstration. It is used to store the state of the uploaded file.</p>
 *
 * @since 0.3.0
 */
public class InputFileBean  implements Renderable {

    private int percent = -1;
    private File file = null;
	private boolean uploadDialog;
 
    private String errorMsg = "";
	private List filesList = new ArrayList();
	
	private final long maxSize = 1024000;
	private final int maxFiles = 5;

	private final String msgInvalid="Invalid File -- see server log for stack trace";
	private final String msgUnknownSize="Unable to determine file size of requested file";
	private final String msgExceededAllocation="Allotted disc space exceeded: Remove unwanted files before further uploads";
	private final String msgSingleFileTooLarge=
		"Individual File exceeds size limitation set in context-params";
	private final String msgNumberFilesExceeded=
		"Number of files has been exceeded: Remove unwanted files before further uploads";


	private String currentFileName = "none";
	private String uploadDirectory = "";
    private String remainingSpace;
	private long currentSize = 0;
	
    private FileEntry currentFileEntry;

    /**
     * Renderable Interface
     */
    private PersistentFacesState state;
    private RenderManager renderManager;

    private static Log log =
        LogFactory.getLog(InputFileBean.class);

    /*
     * constructor
     */
    public InputFileBean() {
        state = PersistentFacesState.getInstance();
        uploadDialog=false;    
    }

    /**
     * Sets the Render Manager.
     *
     * @param renderManager
     */
    public void setRenderManager(RenderManager renderManager) {
        this.renderManager = renderManager;
    }

    /**
     * Gets RenderManager, just try to satisfy WAS
     *
     * @return RenderManager null
     */
    public RenderManager getRenderManager() {
        return null;
    }

    /**
     * Get the PersistentFacesState.
     *
     * @return state the PersistantFacesState
     */
    public PersistentFacesState getState() {
        return state;
    }

    /**
     * Handles rendering exceptions for the progress bar.
     *
     * @param renderingException the exception that occured
     */
    public void renderingException(RenderingException renderingException) {
        renderingException.printStackTrace();
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public int getPercent() {
        return percent;
    }

    public void action(ActionEvent event) {
	     InputFile inputFile = (InputFile) event.getSource();
	     currentFileName = inputFile.getFileInfo().getFileName();
	     String currentFileType = inputFile.getFileInfo().getContentType();
	     this.percent = inputFile.getFileInfo().getPercent();
	     file = inputFile.getFile();
	     currentFileEntry = new FileEntry(file, currentFileType); 
	     if (uploadDirectory.equals("")){
	    	 /* put the uploadDirectory into a session attribute so files
	    	  * can be cleaned up when session ends
	    	  */
	    	 uploadDirectory=file.getParent();
	    	 log.info("uploadDirectory is:"+uploadDirectory);
	    	 try{
	    		 FacesContext context = FacesContext.getCurrentInstance();
	    		 HttpSession session = (HttpSession)(context.getExternalContext().getSession(false));
	    		 session.setAttribute("uploadDirectory", uploadDirectory);
 	    		 log.info("context path ="+context.getExternalContext().getRequestContextPath());
//	    		 context.getExternalContext().getRequestMap().put("uploadDirectory", uploadDirectory);
	    		 String attrCheck = (String)session.getAttribute("uploadDirectory");
	    		 log.info("after setting attribute & attr.uploadDir="+attrCheck);
	    	 }catch (Exception e){
	    		 log.info("Error setting session attribute for uploadDirectory");
	    	 }
	     }
	     if (inputFile.getStatus() == InputFile.SAVED) {
	    	 buildFilesList();
	     }
	     if (inputFile.getStatus() == InputFile.INVALID) {
	         inputFile.getFileInfo().getException().printStackTrace();
	         setError(msgInvalid);
	     }
	     if (inputFile.getStatus() == InputFile.SIZE_LIMIT_EXCEEDED) {
	         inputFile.getFileInfo().getException().printStackTrace();
	         setError(msgSingleFileTooLarge);
	     }
	     if (inputFile.getStatus() == InputFile.UNKNOWN_SIZE) {
	         inputFile.getFileInfo().getException().printStackTrace();
	         setError(msgUnknownSize);
	    }
    }



	/*
	 * this method kicks off the fileUpload and triggers the action method
	 */
    public void progress(EventObject event) {
        InputFile ifile = (InputFile) event.getSource(); 
		this.percent = ifile.getFileInfo().getPercent();
		if (renderManager != null) {
		    renderManager.requestRender(this);
        }
    }

    /*
     * returns remaining space allocated to user for upload of files
     * as a String
     */
    public String getRemainingSpace(){
    	return remainingSpace;
    }
    
     public String getCurrentFileName() {
    	 if (!currentFileName.equals("none"))return currentFileName;
    	 else return "";
     }

     /*
      * crude error messaging
      */
 	private void setError(String msg) {
		errorMsg = msg;
		if (log.isDebugEnabled())log.debug("errorMsg="+errorMsg);
	}

	public List getFilesList() {
		return filesList;
	}
	
	/*
	 * read the files from the upload directory on the server and check to see that
	 * the local limits are not exceeded (max # files and max disk space allotted
	 * for this user.
	 */
	public void buildFilesList(){
		/* get files from server directory */
		refreshList();
		/*if they have exceeded max number of files or max size allowed, delete last file*/
    	if (filesList.size() > this.maxFiles){
	    		log.info("MaxFilesExceeded");
	    		//get rid of the last uploaded file
	    		setError(msgNumberFilesExceeded);
	    		deleteCurrentFileEntry();
	    }
	    else if (currentSize > this.maxSize){
	    		log.info("max size Exceeded");
	    		setError(msgExceededAllocation);
	    		deleteCurrentFileEntry();	 
	    }
	    else setError("");
	    /* calculate remaining allotted space */
	     remainingSpace=String.valueOf((maxSize-currentSize));
	}

	/* may need to refresh the list more than once per each upload */
	public void refreshList(){
		filesList.clear();
		currentSize=0;
		File[] files = null;
		String parentDir = currentFileEntry.getFolder();
	    File dir = new File(parentDir);
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
	    		FileEntry f = new FileEntry(files[i]);
	    		filesList.add(f);   
	    	    this.currentSize += f.getSize();
	    	}
	    }
	}
	/* deletes currentFileEntry as it violates max Number of files or max allotted space */
	public void deleteCurrentFileEntry(){
		File f = new File(currentFileEntry.getAbsolutePath());
		if (f.exists())f.delete();
		refreshList();
	}
	
	/*
	 * deletes the selected file(s) from the server
	 */
	public void deleteFiles(ActionEvent e) {
	     for (int i = filesList.size()-1; i >= 0 ; i--) {
	        FileEntry fr = (FileEntry)filesList.get(i);
	        if (fr.getSelected().booleanValue()) {
	    	   //remove from the server and the filesList
	          filesList.remove(i);
	          File f = new File(fr.getAbsolutePath());
	          if(f.exists())f.delete();
	       }
	   }
	   refreshList();
	}
	/*
	 * do this for now as not sure how jsp handles FacesMessages
	 */

	public String getErrorMsg(){return errorMsg;}
	   /**
     * Method to determine if the upload dialog is open or not
     * put comment in here
     */
    public boolean getUploadDialog() {
        return uploadDialog;
    }
    /**
     * Method to set the popup status of the file upload dialog 
     *
     */
    public void closeUploadDialog() {
            this.uploadDialog = false;
    }
    /**
     * Method to set the popup status of the file upload dialog 
     */
    public void openUploadDialog() {
            this.uploadDialog = true;
    }
}


