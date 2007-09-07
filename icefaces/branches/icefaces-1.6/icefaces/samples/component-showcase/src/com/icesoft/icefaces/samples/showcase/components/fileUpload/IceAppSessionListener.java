package org.icefaces.application.showcase.component.fileUpload;
import java.io.File;
import java.io.FileFilter;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * The session listener class is used to remove uploaded files from the
 * upload directory on the server when the session is exited
 */
public class IceAppSessionListener implements javax.servlet.http.HttpSessionListener {

	  private HttpSession session = null;
//	  private static Log log =
//	        LogFactory.getLog(IceAppSessionListener.class);

	  /**
	   * This method is called by the servlet container just after http session is
	   * created. 
	   *
	   * @param <b>event</b> HttpSessionEvent
	   */
	  public void sessionCreated(HttpSessionEvent event) {
//		if (log.isDebugEnabled())log.debug("session is created");
	    session = event.getSession();
	  }
	  /**

	   * The method is called by the servlet container just before http session is
	   * destroyed. The implementation removes all files from the upload directory
	   * that is referenced by the session attribute "uploadDirectory"
	   *
	   * @param <b>event</b> HttpSessionEvent
	   */
	  public void sessionDestroyed(HttpSessionEvent event) {
//		  if (log.isDebugEnabled())log.debug("shutting down session");
	    session = event.getSession();
	    String uploadDirectory = null;

	    try {
	    	/* get the session attribute value for the uploadDirectory */

	      uploadDirectory = (String) session.getAttribute("uploadDirectory");
	      if (uploadDirectory!=null)System.out.println(uploadDirectory);
//	      else log.info("uploadDirectory is null");
	      if(uploadDirectory != null) {

	        /* remove the files that have been uploaded during this session */
	  		File[] files = null;
		    File dir = new File(uploadDirectory);
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
//		    			if (log.isDebugEnabled())log.debug("file: "+fname+" deleted");
		    		}
		    		
		    		
		    	}
		    }
	      }
	    } catch(Exception ex) {
	    	/* no files to delete */
//	    	log.info("uploadDirectory is not set--no files to delete!");
	    }
	  }
	}

