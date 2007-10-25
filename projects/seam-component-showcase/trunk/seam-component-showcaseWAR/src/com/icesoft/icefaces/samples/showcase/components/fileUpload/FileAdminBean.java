package com.icesoft.icefaces.samples.showcase.components.fileUpload;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import org.jboss.seam.annotations.Create;
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
public class FileAdminBean implements Serializable{
	/* each user is allowed a max number of files =6 or 
	 * max size allowance to upload of 10MB
	 */
	private final int MAXFILESALLOWED=4;
	private final int MAXSIZEALLOWED=1024000;
	private final String msgExceededAllocation=
		"Allotted disc space exceeded: Remove unwanted files before further uploads";
	private final String msgNumberFilesExceeded=
		"Number of files has been exceeded: Remove unwanted files before further uploads";
	private long currentSize = 0;
	private String remainingSpace;

    private boolean updateList = false;
    private String currentFileName="";
    private String parentDir = "";
	private InputFileBean fileUpload;
	
	/* the users list of files */
	private List<FileEntry> filesList = new ArrayList<FileEntry>();
	
    private static Log log =
           LogFactory.getLog(FileAdminBean.class);	
  	
    public void setFileUpload(InputFileBean ifb){
    	log.info("setFileUpload");
    	if (ifb==null){
    		log.info("\t\t >>>creating new fileUpload");
    	}
    	else fileUpload=ifb;
//		log.info("setFileUpload version="+fileUpload.toString());
//		log.info("setFileUpload fileAdmin version="+this.toString());
    }

	public InputFileBean getFileUpload(){
		if (fileUpload==null){
			log.info("inputFileBean is null");
    		fileUpload = new InputFileBean();
		}
		else {
//			log.info("gettingInputFileBean percent="+fileUpload.getPercent());
//			if (fileUpload.getPercent()>-1)this.percent=fileUpload.getPercent();
		}
//		log.info("getFileUpload version="+fileUpload.toString());
//		log.info("getFileUpload fileAdmin version="+this.toString());
//try{
//   	 java.lang.reflect.Method[] methods = fileUpload.getClass().getMethods();
//   	 for(int i = 0; i < methods.length; i++) {
//   		 log.info(methods[i].toGenericString());
//   	 }
//}catch (Exception e){
//	log.info("\t\t-> couldn't get list of methods for fileUpload "+e);
//	//e.printStackTrace();
//}
		return this.fileUpload;
	}

	public List<FileEntry> getFilesList() {
		log.info("getFilesList");
		if (this.fileUpload!=null && fileUpload.isUpdateFlag())buildFilesList();	
		return filesList;
	}
	/*
	 * read the files from the upload directory on the server and check to see that
	 * the local limits are not exceeded (max # files and max disk space allotted
	 * for this user.
	 */
	public void buildFilesList(){
		/* get files from server directory */
		List<FileEntry> tempList = refreshList();
		log.info("buildFilesList tempList size="+tempList.size());
		/*if they have exceeded max number of files or max size allowed, delete last file*/
    	if (tempList.size() > this.MAXFILESALLOWED){
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
	    else {
	    	fileUpload.setError("");
	        /* calculate remaining allotted space */
	     	remainingSpace=String.valueOf((MAXSIZEALLOWED-currentSize));
	     	/* update the filesList from tempList */
	     	if (tempList.isEmpty())filesList.clear();
	     	else{
		     	Iterator i = tempList.iterator();
		     	while (i.hasNext()){
		     		FileEntry fe = (FileEntry)i.next();
		     		String fname = fe.getFileName();
		     		if (!listContains(fname))
		     			filesList.add(fe);
		     	}
	     	}
	    }
    	if (fileUpload!=null)fileUpload.setUpdateFlag(false);
    	this.updateList= false;
	}
	
	public boolean listContains(String fname){
		Iterator i = filesList.iterator();
		while (i.hasNext()){
			if (((FileEntry)i.next()).getFileName().equals(fname))
				return true;
		}
		return false;
	}
	
	/* may need to refresh the list more than once per each upload */
	public ArrayList<FileEntry> refreshList(){
		log.info("refreshList");
	    parentDir= null;
	    ArrayList<FileEntry> tempList = new ArrayList<FileEntry>();
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
	    		tempList.add(f);   
	    	    this.currentSize += f.getSize();
	    	}
	      }
	    }
	    return tempList;
	}
	/* deletes currentFileEntry as it violates max Number of files or max allotted space */
	public void deleteCurrentFileEntry(){
		log.info("deletecurrentFileEntry");
		File f = getLastFile();
		f.delete();
		log.info("\t\t-> deleted "+f.getName());
		//add the size back into remaining space
		this.remainingSpace = String.valueOf(this.MAXSIZEALLOWED - currentSize + f.length());
		FileEntry fe = this.getFileEntry(f.getName());
		filesList.remove(fe);
	}
	/*
	 * deletes the selected file(s) from the server
	 */

	public void deleteFiles(ActionEvent e) {
		log.info("in deleteFiles with filesList size="+filesList.size());
        ArrayList<FileEntry> deletedEntry = new ArrayList<FileEntry>();
    	ArrayList<Integer> deletedEntries = new ArrayList<Integer>();
	     for (int i =0; i <filesList.size() ; i++) {
	        FileEntry fr = (FileEntry)filesList.get(i);
	        log.info("i="+i+" fr="+fr.toString());
	        if (fr.getSelected().booleanValue()) {
	    	   //remove from the server and the filesList
	          log.info("selected file to delete is "+fr.getFileName());
	          deletedEntries.add(i);
	             deletedEntry.add(fr);
	          File f = new File(fr.getAbsolutePath());
	//          if(f.exists()){

	        	  f.delete();
	        	  log.info("File "+f.getName()+" is deleted");
//	          }
//	          else log.info("File "+f.getName()+" NOT DELETED!!");
	       }
	   }
	     for (int i=0;i<deletedEntry.size();i++){
	    	 log.info("\t deleteEntries is of size="+deletedEntries.size());
	    	 boolean worked = filesList.remove(deletedEntry.get(i));
	    	 log.info("\t after remove worked = "+worked+" & size="+deletedEntries.size());
	     }
	     log.info("\t\t end of deleteFiles & size="+filesList.size());
	}

	public FileEntry getFileEntry(String fileNm){
		Iterator i = filesList.iterator();
		while (i.hasNext()){
			FileEntry fe = (FileEntry)i.next();
			if (fe.getSelected())return fe;
		}
		return null;		
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

    /*
     * can remove the renderManager and any files uploaded upon clean up
     */
//	@Remove
	@Destroy
	public void destroy() {
		///can get rid of the uploaded files here unless have ejb3 container
	 	log.info("FileInfoBean: seam destroying...");
	 	if (fileUpload.getRenderManager()!=null){
	 		fileUpload.getRenderManager().dispose();
	 	}
	 	//delete uploaded files since the session is now over/destroyed
	 	
	 	try{
		 	if (this.parentDir!=null){
		        /* remove the files that have been uploaded during this session */
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

	public String getCurrentFileName() {
		if (fileUpload!=null)currentFileName=fileUpload.getCurrentFileName();
		log.info("   fileName uploaded = "+currentFileName);
		return currentFileName;
	}


}
