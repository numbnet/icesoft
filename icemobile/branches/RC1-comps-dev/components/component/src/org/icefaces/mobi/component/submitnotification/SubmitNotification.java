package org.icefaces.mobi.component.submitnotification;

import javax.faces.context.FacesContext;


public class SubmitNotification extends SubmitNotificationBase {

     public static final String BLACKOUT_PNL_CLASS = "mobi-submitnotific-bg ";
     public static final String BLACKOUT_PNL_HIDE_CLASS = "mobi-submitnotific-bg-hide ";
     public static final String CONTAINER_HIDE_CLASS = "mobi-submitnotific-container-hide ";
     public static final String CONTAINER_CLASS = "mobi-submitnotific-container ";
     public static final String TITLE_CLASS = "mobi-date-title-container ";


     protected FacesContext getFacesContext() {
		 return FacesContext.getCurrentInstance();
	 }
}
