package org.icefaces.mobi.component.panelpopup;

import javax.faces.context.FacesContext;

public class PanelPopup extends PanelPopupBase {
     public static final String BLACKOUT_PNL_CLASS = "mobi-date-bg-inv ";
     public static final String CONTAINER_CLASS = "mobi-panelpopup-container ";
     public static final String CLIENT_CONTAINER_CLASS = "mobi-date-container-hide ";
     public static final String TITLE_CLASS = "mobi-date-title-container ";
     public static final String INTERIOR_CONT_CLASS = "mobi-date-select-container ";
     public static final String BUTTON_CLASS = "mobi-button mobi-button-default ";

    protected FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}


}
