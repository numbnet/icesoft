package com.icesoft.icefaces.samples.showcase.components.fileUpload;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remove;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;

import org.jboss.seam.annotations.Name;

import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Manager;
import org.jboss.seam.ScopeType;




/**
 * This session scoped bean manages a list of <FileUploadComp>
 * Due to browser limitations, only 1 instance of an InputfileBean
 * may be enabled at a time.  
 * 
 * @author jguglielmin
 *
 */

@Name("fileAdminBean")
@Scope(ScopeType.SESSION)
public class FileAdminBean{
	/* each user is allowed a max number of files =6 or 
	 * max size allowance to upload of 10MB
	 */
	private final int MAXFILESALLOWED=6;
	private final int MAXSIZEALLOWED=1024000;
	private final String msgExceededAllocation=
		"Allotted disc space exceeded: Remove unwanted files before further uploads";
	private final String msgNumberFilesExceeded=
		"Number of files has been exceeded: Remove unwanted files before further uploads";
	private long currentSize = 0;
	private String remainingSpace;
	
	//for checking purposes
	private String convId;
	private boolean longRunning;
	
	private boolean deleteFlag = false;

	private InputFileBean fileUpload;
	
	/* the users list of files */
	private List<FileEntry> filesList = new ArrayList<FileEntry>();
	
    private static Log log =
           LogFactory.getLog(FileAdminBean.class);	
  	
	@In(create=true)
    public void setFileUpload(InputFileBean ifb){
    	log.info("setFileUpload");
    	if (ifb==null){
    		log.info("\t\t >>>creating new fileUpload");
    		fileUpload = new InputFileBean();
    	}
    }

	public InputFileBean getFileUpload(){
		return this.fileUpload;
	}

	public List<FileEntry> getFilesList() {
		log.info("getFilesList");
		buildFilesList();
		return filesList;
	}
	/*
	 * read the files from the upload directory on the server and check to see that
	 * the local limits are not exceeded (max # files and max disk space allotted
	 * for this user.
	 */
// 	@Observer("buildfiles")
	public void buildFilesList(){
		/* get files from server directory */
		log.info("buildFilesList and deleteFlag="+deleteFlag);
		if (!deleteFlag)refreshList();
		/*if they have exceeded max number of files or max size allowed, delete last file*/
    	if (filesList.size() > this.MAXFILESALLOWED){
	    		log.info("MaxFilesExceeded");
	    		//get rid of the last uploaded file
	    		fileUpload.setError(msgNumberFilesExceeded);
	    		deleteCurrentFileEntry();
	    }
	    else if (currentSize > this.MAXSIZEALLOWED){
	    		log.info("max size Exceeded");
	    		fileUpload.setError(msgExceededAllocation);
	    		deleteCurrentFileEntry();	 
	    }
	    else fileUpload.setError("");
	    /* calculate remaining allotted space */
	     remainingSpace=String.valueOf((MAXSIZEALLOWED-currentSize));
	}
	/* may need to refresh the list more than once per each upload */
	public void refreshList(){
		log.info("refreshList and deleteFlag="+deleteFlag);
	    String parentDir= null;
		filesList.clear();
		currentSize=0;
	    try {
	    	/* get the session attribute value for the uploadDirectory */
   		  FacesContext context = FacesContext.getCurrentInstance();
		  HttpSession session = (HttpSession)(context.getExternalContext().getSession(false));
	      parentDir = (String)session.getAttribute("uploadDirectory");
	      if (parentDir!=null)log.info("parentDir is "+parentDir);
	      else log.info("uploadDirectory is null");
	    }catch (Exception e) {}
	    if (parentDir != null){
		  File[] files = null;
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
	}
	/* deletes currentFileEntry as it violates max Number of files or max allotted space */
	public void deleteCurrentFileEntry(){
		log.info("deletecurrentFileEntry");
		File f = getLastFile();
		if (f!=null && f.exists())f.delete();
		else log.info("unable to delete file "+f.getName());
		refreshList();
	}
	/*
	 * deletes the selected file(s) from the server
	 */
//    @End
	public void deleteFiles(ActionEvent e) {
		deleteFlag = true;
		log.info("in deleteFiles with filesList size="+filesList.size());
	     for (int i =0; i <filesList.size() ; i++) {
	        FileEntry fr = (FileEntry)filesList.get(i);
	        log.info("i="+i+" fr="+fr.toString());
	        if (fr.getSelected().booleanValue()) {
	    	   //remove from the server and the filesList
	          log.info("selected file to delete is "+fr.getFileName());
	          filesList.remove(i);
	          File f = new File(fr.getAbsolutePath());
	          if(f.exists()){
	        	  f.delete();
	        	  log.info("File "+f.getName()+" is deleted");
	        	  deleteFlag=false; //reset delete flag
	          }
	          else log.info("File "+f.getName()+" NOT DELETED!!");
	       }
	   }
	   refreshList();
	}
	
	public File getLastFile(){
		log.info("getLastFile");
		if (filesList.size() > 0){
			FileEntry fe = (FileEntry)filesList.get(filesList.size()-1);
			log.info("last file is "+fe.getFileName());
			return new File(fe.getAbsolutePath());
		}
		else {
			log.info("no files to delete");
			return null;
		}
	}
    /*
     * returns remaining space allocated to user for upload of files
     * as a String
     */
    public String getRemainingSpace(){
    	return remainingSpace;
    }

    //checking stuff
    public String getConvId(){
    	log.info("convId="+convId);
        return Manager.instance().getCurrentConversationId();
    }
    public boolean isLongRunning(){
        Manager manager = Manager.instance();
    	return manager.isLongRunningConversation();
    }
    
	@Remove
	@Destroy
	public void destroy() {
		///can get rid of the uploaded files here unless have ejb3 container
	 	log.info("seam destroying...");
	 	if (fileUpload.getRenderManager()!=null){
	 		fileUpload.getRenderManager().dispose();
	 	}
	}
}
