package com.icesoft.faces.component.portlet;

import com.icesoft.jasper.Constants;

import javax.faces.component.UINamingContainer;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.util.Map;

/**
 * The Portlet component is a naming container that is used to wrap any ICEfaces page fragment
 * that is designed to be deployed as a portlet.  Currently, it's main purpose is to provide
 * the proper namespace to the portlet's component heirarchy by
 */
public class Portlet extends UINamingContainer {
    private String style;
    private String styleClass;

    public String getClientId(FacesContext context) {

        // Currently we store the namespace in the initial request
        // map so that's it's available for subsequent Ajax requests.
        ExternalContext extCtxt = context.getExternalContext();
        Map requestMap = extCtxt.getRequestMap();
        return (String) requestMap.get(Constants.NAMESPACE_KEY);
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }
}
