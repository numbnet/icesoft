package com.icesoft.faces.component.portlet;

import com.icesoft.jasper.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

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

    public String getId() {
        String ns = getNamespace();
        if (ns != null) {
            return ns;
        }
        String sid = super.getId();
        return sid;
    }

    public void setId(String id) {
        //Always use the namespace if it is available.  Otherwise defer
        //to normal operations.  This should allow us to use the portlet
        //component in regular web apps without undue side effects.
        String ns = getNamespace();
        if (ns != null) {
            super.setId(ns);
        }
        else {
            super.setId(id);
        }
    }
    
    public String getClientId(FacesContext facesContext) {
        String sclientId = super.getClientId(facesContext);
        return sclientId;
    }
    
    private synchronized String getNamespace() {
        if (namespace == null) {
            FacesContext facesCtxt = getFacesContext();
            
            if (facesCtxt == null) {
                if (log.isDebugEnabled()) {
                    log.debug("Could not access FacesContext");
                }
                return null;
            }
            ExternalContext extCtxt = facesCtxt.getExternalContext();
            if(extCtxt == null) {
                if (log.isDebugEnabled()) {
                    log.debug("Could not access ExternalContext");
                }
                return null;
            }
            Map requestMap = extCtxt.getRequestMap();
            namespace = (String) requestMap.get(Constants.NAMESPACE_KEY);
        }
        return namespace;
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
