package org.icefaces.mobi.component.panelpopup;

import javax.faces.context.FacesContext;

public class PanelPopup extends PanelPopupBase {

    // CSS class names used by the component renderer.
    public static final String BLACKOUT_PNL_HIDDEN_CLASS = "mobi-panelpopup-bg-hide ";
    public static final String BLACKOUT_PNL_CLASS = "mobi-panelpopup-bg ";
    public static final String CONTAINER_CLASS = "mobi-panelpopup-container ";
    public static final String CLIENT_CONTAINER_CLASS = "mobi-panelpopup-container-hide ";
    // TODO refine or remove
    public static final String TITLE_CLASS = "mobi-panelpopup-title-container ";

    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }


}
