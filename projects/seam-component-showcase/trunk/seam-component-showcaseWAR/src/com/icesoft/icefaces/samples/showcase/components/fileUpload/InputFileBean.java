package com.icesoft.icefaces.samples.showcase.components.fileUpload;

import com.icesoft.faces.async.render.RenderManager;
import com.icesoft.faces.async.render.Renderable;
import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import com.icesoft.faces.webapp.xmlhttp.RenderingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import javax.ejb.Remove;
import javax.ejb.Stateful;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

import java.io.File;
import java.io.Serializable;

import java.util.EventObject;


/**
 * <p>The InputFileBean class is the backing bean for the inputfile showcase
 * demonstration. It is used to store the state of the uploaded file.</p>
 *
 * @since 0.3.0
 */

import org.jboss.seam.annotations.Scope;
//import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.ScopeType;

@Name("fileUpload")
@Scope(ScopeType.EVENT)
public class InputFileBean implements Renderable, Serializable{
	private boolean uploadDialog = true;
    private int percent = -1;
    private File file = null;
    
    private String errorMsg = "";

	private final String msgInvalid="Invalid File -- see server log for stack trace";
	private final String msgUnknownSize="Unable to determine file size of requested file";

	private final String msgSingleFileTooLarge=
		"Individual File exceeds size limitation set in context-params";

	private String currentFileName = "none";
	private String uploadDirectory = "";
	
	
    /**
     * Renderable Interface
     */
    private PersistentFacesState state;
    
    @In
    private RenderManager renderManager;

    private static Log log =
        LogFactory.getLog(InputFileBean.class);

    /*
     * constructor
     */
    public InputFileBean() {
    	log.info("initializing InputFileBean "+this.toString());
        state = PersistentFacesState.getInstance();    
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
	/**
     * Method to determine if the upload dialog is open or not
     * put comment in here
     */
	public void setUploadDialog(boolean inDialog){
		uploadDialog=inDialog;
	}
    public boolean getUploadDialog() {
        return this.uploadDialog;
    }
    public void setPercent(int percent) {
        this.percent = percent;
    }

    public int getPercent() {
        return percent;
    }

    public void action(ActionEvent event) {
    	log.info("action version="+this);
	     InputFile inputFile = (InputFile) event.getSource();
	     currentFileName = inputFile.getFileInfo().getFileName();
	     this.percent = inputFile.getFileInfo().getPercent();
	     file = inputFile.getFile();
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
	    		 setError("Error setting session attribute for uploadDirectory");
	    	 }
	     }
	     if (inputFile.getStatus() == InputFile.SAVED) {
	    	 this.currentFileName = inputFile.getFileNamePattern();
//	    	 FacesMessages.instance().add("File: is saved for #{fileUpload.currentFileName}");
//	    	 Events.instance().raiseEvent("buildfiles");
	    	 log.info("file is saved ");
	    	 setError("");
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
	     else setError("nothing");
    }
    public String act(){
    	log.info("act() version="+this);
    	
    	return null;
    }

	/*
	 * this method kicks off the fileUpload and triggers the action method
	 */
  //   @Begin
    public void progress(EventObject event) {
    	 log.info("progress version="+this);
        InputFile ifile = (InputFile) event.getSource(); 
		this.percent = ifile.getFileInfo().getPercent();
		if (renderManager != null) {
		    renderManager.requestRender(this);
        }
    }

     public String getCurrentFileName() {
    	 if (!currentFileName.equals("none"))return currentFileName;
    	 else return "";
     }
     public String getUploadDirectory(){
    	 return this.uploadDirectory;
     }

     public File getFile(){
    	 return this.file;
     }
     /*
      * crude error messaging
      */
 //   @End
 	public void setError(String msg) {
		errorMsg = msg;
		if (log.isDebugEnabled())log.debug("errorMsg="+errorMsg);
	}

	/*
	 * do this for now as not sure how jsp handles FacesMessages
	 */

	public String getErrorMsg(){return errorMsg;}
	   /**
     * Method to determine if the upload dialog is open or not
     * put comment in here
     */

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
    
	@Destroy @Remove
	public void destroy(){
		log.info("destroy");
	}
}



