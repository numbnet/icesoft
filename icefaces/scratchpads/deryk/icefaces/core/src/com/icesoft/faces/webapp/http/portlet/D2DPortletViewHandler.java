package com.icesoft.faces.webapp.http.portlet;

import com.icesoft.faces.application.D2DViewHandler;
import com.icesoft.faces.webapp.http.servlet.ServletExternalContext;
import com.icesoft.jasper.Constants;

import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.application.ViewHandler;
import java.util.Map;
import java.net.URL;
import java.net.MalformedURLException;

public class D2DPortletViewHandler extends D2DViewHandler {


    public D2DPortletViewHandler() {
        super();
        if( log.isInfoEnabled() ){
            log.info( "constructing" );
        }
    }

    public D2DPortletViewHandler(ViewHandler delegate) {
        super(delegate);
        if( log.isInfoEnabled() ){
            log.info( "setting delegate view handler " + delegate );
        }
    }

    /**
     * When a request is initiated by a portlet, the view ID is not created
     *
     * Here we are attempting to set the initial view provided by the portlet
     * configuration as the view ID.  It is set as a request attribute in the
     * MainPortlet. The portlet context returns the request URI with the
     * context path prepended. For the servlet to find it, the context path
     * needs to be removed.  This method allows the parent to create the view
     * root and only attempts to apply a new portlet view ID if the existing
     * one is null or "default".
     */
    public UIViewRoot createView(FacesContext context, String viewId) {
        UIViewRoot root = super.createView(context, viewId);
        String viewID = root.getViewId();

        if( log.isInfoEnabled() ){
            log.info( "[portlet mode] view ID from parent view handler: " + viewID );
        }

//        //If the parent view handler couldn't get a decent view ID, then
//        //we'll attempt to get a portlet savvy one.
//        if (viewID == null || viewID.trim().length() == 0 || viewID.equals(DEFAULT_VIEW_ID)) {
//            String newID = null;
//
//            ExternalContext extCtxt = context.getExternalContext();
//            if (extCtxt instanceof ServletExternalContext) {
//                ServletExternalContext sec = (ServletExternalContext) extCtxt;
//
//                //If the request URI or the context path are null, then we
//                //can't attempt to construct our new portlet view ID.
//                String reqURI = sec.getRequestURI();
//                String reqCtxtPath = sec.getRequestContextPath();
//                if (reqURI == null || reqCtxtPath == null) {
//                    return root;
//                }
//
//                if (reqURI.startsWith(reqCtxtPath)) {
//                    newID = reqURI.substring(reqCtxtPath.length());
//                    if (log.isInfoEnabled()) {
//                        log.info("[portlet mode] setting view ID to: " + newID);
//                    }
//                    root.setViewId(newID);
//                }
//            } else {
//                if( log.isWarnEnabled() ){
//                    log.warn( "external context is not a ServletExternalContext" );
//                }
//            }
//        }

        return root;
    }

}
