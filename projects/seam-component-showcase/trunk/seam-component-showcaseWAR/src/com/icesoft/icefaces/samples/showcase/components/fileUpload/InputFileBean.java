package com.icesoft.icefaces.samples.showcase.components.fileUpload;

import com.icesoft.faces.async.render.RenderManager;
import com.icesoft.faces.async.render.Renderable;
import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import com.icesoft.faces.webapp.xmlhttp.RenderingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.FacesMessage;

import org.jboss.seam.annotations.Destroy;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.core.Manager;
import org.jboss.seam.faces.FacesMessages;



import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.portlet.PortletSession;
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

	private String currentFileName = "none";
	private String statusMsg = "";
	private String uploadDirectory = "";
	private boolean updateFlag = false;


	FacesMessage facesMessage;
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
 //      	log.info("initializing InputFileBean "+this.toString()+" renderManager="+renderManager);
    }

 
    /**
     * Sets the Render Manager.
     *
     * @param renderManager
     */
    public void setRenderManager(RenderManager renderManager) {
       if (renderManager !=null) this.renderManager = renderManager;
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
        return this.percent;
    }

    /**
     * progress method actually kicks off the process and then
     * action is invoked.  This method writes the upload directory string
     * to a session attribute for file management and then 
     * checks to see if the file is saved or any exceptions have
     * occured during the fileupload.
     * @param event
     */
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
	    		 Object o = FacesContext.getCurrentInstance().getExternalContext().getSession(false);
	    		 if (o instanceof javax.servlet.http.HttpSession){
    	    		((HttpSession)o).setAttribute("uploadDirectory", uploadDirectory);
    	    		if (log.isDebugEnabled())log.debug("httpSession");
	    		 }
	    		 else if (o instanceof javax.portlet.PortletSession){
	    			 if (log.isDebugEnabled())log.debug("portletSession");
	    			 ((PortletSession)o).setAttribute("uploadDirectory", uploadDirectory);
	    		 }
	    	 }catch (Exception e){
	    		 log.info("error setting upload directory in session attribute ");
	    		 e.printStackTrace();
	    	 }
	     }
	     if (inputFile.getStatus() == InputFile.SAVED) {
	    	 this.currentFileName = inputFile.getFileNamePattern();
	    	 this.updateFlag=true;		 
	     }
	     else if (inputFile.getStatus() == InputFile.INVALID) {
	            FacesContext.getCurrentInstance().addMessage(null,
	                    new FacesMessage(FacesMessage.SEVERITY_INFO,
	                            "TEST MESSAGE INVALID",
	                            null));
	     }
	     else if (inputFile.getStatus() == InputFile.SIZE_LIMIT_EXCEEDED) {
	            FacesContext.getCurrentInstance().addMessage(null,
	                    new FacesMessage(FacesMessage.SEVERITY_INFO,
	                            "TEST MESSAGE SIZE LIMIT",
	                            null));
	     }
	     else if (inputFile.getStatus() == InputFile.UNKNOWN_SIZE) {
	            FacesContext.getCurrentInstance().addMessage(null,
	                    new FacesMessage(FacesMessage.SEVERITY_INFO,
	                            "TEST MESSAGE UNKNOW SIZE",
	                            null));
	    }
        else if (inputFile.getStatus() == InputFile.INVALID_NAME_PATTERN) {
        	FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "TEST MESSAGE INVALID NAME",
                        null));
        }
    }

	/*
	 * this method kicks off the fileUpload and triggers the action method
	 */

    public void progress(EventObject event) {
    	this.updateFlag = false;
        InputFile ifile = (InputFile) event.getSource(); 
		this.percent = ifile.getFileInfo().getPercent();
  	    reRender();
    }

/**
 * using state.execute and render seems to work better in standard request
 * scope (rather than using the RenderManager class and it's methods.
 */
	protected void reRender() {
		PersistentFacesState _state = PersistentFacesState.getInstance();
		   if(_state != null){
			  state = _state;
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
 

 	public void toggleDialog(){
 		uploadDialog=!uploadDialog;
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
	public String getStatusMsg() {
		return statusMsg;
	}
	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}

}



