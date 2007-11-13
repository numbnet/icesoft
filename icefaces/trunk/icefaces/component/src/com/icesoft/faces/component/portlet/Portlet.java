package com.icesoft.faces.component.portlet;

import com.icesoft.jasper.Constants;

import javax.faces.component.UINamingContainer;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The Portlet component is a naming container that is used to wrap any ICEfaces page fragment
 * that is designed to be deployed as a portlet.  Currently, it's main purpose is to provide
 * the proper namespace to the portlet's component heirarchy by
 */
public class Portlet extends UINamingContainer {

    private static Log log = LogFactory.getLog(Portlet.class);

    private String namespace;

    private String style;
    private String styleClass;

    public Portlet() {
        FacesContext facesCtxt = getFacesContext();

        if( facesCtxt == null ){
            if (log.isDebugEnabled()) {
                log.debug("could not access FacesContext");
            }
            return;
        }
        ExternalContext extCtxt = facesCtxt.getExternalContext();
        Map requestMap = extCtxt.getRequestMap();
        namespace = (String) requestMap.get(Constants.NAMESPACE_KEY);
        if( namespace != null ){

            //ICE-2250 If there is a valid namespace, we need to set it so that the
            //superclass instance is properly set otherwise the getClientId
            //logic is incorrect since it does not use getId().
            super.setId(namespace);
        }
    }

    public String getId() {
        if( namespace != null ){
            return namespace;
        }
        return super.getId();
    }

    public void setId(String id) {
        //Always use the namespace if it is available.  Otherwise defer
        //to normal operations.  This should allow us to use the portlet
        //component in regular web apps without undue side effects.
        if( namespace == null ){
            super.setId(id);
        }
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
