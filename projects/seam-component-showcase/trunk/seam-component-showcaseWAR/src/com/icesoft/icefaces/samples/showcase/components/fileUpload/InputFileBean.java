package com.icesoft.icefaces.samples.showcase.components.fileUpload;

import com.icesoft.faces.async.render.RenderManager;
import com.icesoft.faces.async.render.Renderable;
import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import com.icesoft.faces.webapp.xmlhttp.RenderingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.contexts.Contexts;



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



//import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.ScopeType;

@Name("fileUpload")
@Scope(ScopeType.EVENT)
public class InputFileBean implements Renderable, Serializable{
	private boolean uploadDialog = false;
    private int percent = -1;
    private File file = null;
    
    private String errorMsg = "";

	private final String msgInvalid="Invalid File -- see server log for stack trace";
	private final String msgUnknownSize="Unable to determine file size of requested file";

	private final String msgSingleFileTooLarge=
		"Individual File exceeds size limitation set in context-params";

	private String currentFileName = "none";
	private String uploadDirectory = "";
	private boolean updateFlag = false;
	
	
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
		this.uploadDialog=inDialog;
	}
    public boolean getUploadDialog() {
        return this.uploadDialog;
    }
    public void setPercent(int percent) {
        this.percent = percent;
    }

    public int getPercent() {
    	state= PersistentFacesState.getInstance(); 
        return this.percent;
    }

    public void action(ActionEvent event) {
	     InputFile inputFile = (InputFile) event.getSource();
	     this.percent = inputFile.getFileInfo().getPercent();
	     file = inputFile.getFile();
	     currentFileName = file.getName();
	     if (uploadDirectory.equals("")){
	    	 /* put the uploadDirectory into a session attribute so files
	    	  * can be cleaned up when session ends
	    	  */
	    	 uploadDirectory=file.getParent();
	    	 if (log.isDebugEnabled())log.debug("uploadDirectory is:"+uploadDirectory);
	    	 try{
	    		 FacesContext context = FacesContext.getCurrentInstance();
	    		 HttpSession session = (HttpSession)(context.getExternalContext().getSession(false));
	    		 session.setAttribute("uploadDirectory", uploadDirectory);
	    	 }catch (Exception e){
	    		 setError("Error setting session attribute for uploadDirectory");
	    	 }
	     }
	     if (inputFile.getStatus() == InputFile.SAVED) {
	    	 this.currentFileName = inputFile.getFileNamePattern();
	    	 this.updateFlag=true;
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
	     else setError("no error");  //just now for debug
    }


	/*
	 * this method kicks off the fileUpload and triggers the action method
	 */
    public void progress(EventObject event) {
    	this.updateFlag = false;
        InputFile ifile = (InputFile) event.getSource(); 
		this.percent = ifile.getFileInfo().getPercent();
		if (renderManager != null) {
		    renderManager.requestRender(this);
        }
    }

     public String getCurrentFileName() {
    	 if (file !=null){ 
    	   return this.file.getName();
    	 }
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
 	public void setError(String msg) {
		errorMsg = msg;
		if (log.isDebugEnabled())log.debug("errorMsg="+errorMsg);
	}

	/*
	 * do this for now as not sure how jsp handles FacesMessages
	 */

	public String getErrorMsg(){
		return errorMsg;
	}
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
    
	@Destroy 
	public void destroy(){
		log.info("destroy");
//   	    updateFileList();
	}


	public boolean isUpdateFlag() {
		return updateFlag;
	}


	public void setUpdateFlag(boolean updateFlag) {
		this.updateFlag = updateFlag;
	}
}



