package com.icesoft.icefaces.samples.showcase.components.fileUpload;

import com.icesoft.faces.async.render.RenderManager;
import com.icesoft.faces.async.render.Renderable;
import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import com.icesoft.faces.webapp.xmlhttp.RenderingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.core.Manager;



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
public class InputFileBean implements Renderable, Serializable{
	private boolean uploadDialog = false;
    private int percent = -1;
    private File file = null;
    
    private String errorMsg = "";

	private String currentFileName = "none";
	private String uploadDirectory = "";
	private boolean updateFlag = false;

	
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
       	log.info("initializing InputFileBean "+this.toString()+" renderManager="+renderManager);
    }

 
    /**
     * Sets the Render Manager.
     *
     * @param renderManager
     */
    public void setRenderManager(RenderManager renderManager) {
        this.renderManager = renderManager;
       	log.info("setRenderManager version="+this.renderManager);
    }

    /**
     * Gets RenderManager, just try to satisfy WAS
     *
     * @return RenderManager null
     */
    public RenderManager getRenderManager() {
        return this.renderManager;
    }

    /**
     * Get the PersistentFacesState.
     *
     * @return state the PersistantFacesState
     */
    public PersistentFacesState getState() {
    	log.info("getting State="+state);
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
    	PersistentFacesState _state = PersistentFacesState.getInstance();
    	if (_state!=null)state=_state;
    	log.info("getPercent version="+this+" percent="+this.percent);
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
	     }
	     if (inputFile.getStatus() == InputFile.SIZE_LIMIT_EXCEEDED) {
	         inputFile.getFileInfo().getException().printStackTrace();
	     }
	     if (inputFile.getStatus() == InputFile.UNKNOWN_SIZE) {
	         inputFile.getFileInfo().getException().printStackTrace();
	    }
	    // else setError("no error");  //just now for debug
    }


	/*
	 * this method kicks off the fileUpload and triggers the action method
	 */

    public void progress(EventObject event) {
    	this.updateFlag = false;
        InputFile ifile = (InputFile) event.getSource(); 
		this.percent = ifile.getFileInfo().getPercent();
		log.info("percent="+this.percent+" version="+this);

  	   PersistentFacesState _state = PersistentFacesState.getInstance();
	   if(_state != null){
		  state = _state;
		  /**
		   * don't do it this way as it doesn't work!!! state is null in 
		   * renderManager.
		   */
//		  if (renderManager!=null){
//			  renderManager.requestRender(this);
//		  }
		  /**
		   * have to do it this way since Seam manages the contexts and
		   * you don't always have access to the state.  Better to use
		   * an old state rather than a null one!
		   */
			try{
				state.execute();
				state.render();
			}catch(Exception re){
				re.printStackTrace();
			}
			
		}
		else {
			log.info("state is null");
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


    public void closeUploadDialog() {
            this.uploadDialog = false;
//            Manager.instance().beginConversation();
            log.info("closeUploadDialog dialog="+uploadDialog+" LR="+Manager.instance().isLongRunningConversation());
    }
    /**
     * Method to set the popup status of the file upload dialog 
     */

    public void openUploadDialog() {
            this.uploadDialog = true;
 //           Manager.instance().beginConversation();
            log.info("openUploadDialog dialog="+uploadDialog+" LR="+Manager.instance().isLongRunningConversation());
    }
    
 	@Destroy 
	public void destroy(){
		log.info("destroy InputFileBean");
	}


	public boolean isUpdateFlag() {
		return updateFlag;
	}


	public void setUpdateFlag(boolean updateFlag) {
		this.updateFlag = updateFlag;
	}
}



