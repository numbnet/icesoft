package com.icesoft.faces.webapp.http.portlet;

import com.icesoft.jasper.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.io.IOException;

/**
 * The MainPortlet is the entry point for ICEfaces-based portlets.  The goal is
 * to set up the environment as required and then dispatch the request to the
 * MainServlet and let the framework do all the normal processing.  It's
 * basically only the initial page load that we care about.  The rest of the
 * processing is handled between the ICEfaces JavaScript bridge and the
 * MainServlet via AJAX mechanisms.
 */
public class MainPortlet extends GenericPortlet {

    private static Log log = LogFactory.getLog(MainPortlet.class);

    private static final String PORTLET_MARKER = "portlet";
    private PortletConfig portletConfig;

    public MainPortlet() {
        super();
    }

    public void init(PortletConfig portletConfig) throws PortletException {
        this.portletConfig = portletConfig;
        super.init(portletConfig);
    }

    protected void doEdit(RenderRequest renderRequest, RenderResponse renderResponse)
            throws PortletException, IOException {
        String viewId = getViewID(Constants.EDIT_KEY);
        doInclude(renderRequest,renderResponse,viewId);
    }

    protected void doHelp(RenderRequest renderRequest, RenderResponse renderResponse)
            throws PortletException, IOException {
        String viewId = getViewID(Constants.HELP_KEY);
        doInclude(renderRequest,renderResponse,viewId);
    }

    protected void doView(RenderRequest renderRequest, RenderResponse renderResponse)
            throws PortletException, IOException {
        String viewId = getViewID(Constants.VIEW_KEY);
        doInclude(renderRequest,renderResponse,viewId);
    }

    private String getViewID(String key) throws PortletException {
        String viewId = portletConfig.getInitParameter(key);
        if (viewId != null) {
            return viewId;
        }

        if (log.isErrorEnabled()) {
            log.error("cannot find view id for " + key);
        }
        throw new PortletException("cannot find view id for " + key);
    }

    /**
     * The doInclude method is called to properly set up and call the request dispatcher
     * to the MainServlet of the ICEfaces framework.
     * 
     * @param renderRequest the original RenderRequest
     * @param renderResponse the original RenderResponse
     * @param viewId the id of the view to dispatch
     * @throws IOException thrown by the dispatcher.include call
     * @throws PortletException thrown by the dispatcher.include call
     */
    protected void doInclude(RenderRequest renderRequest, RenderResponse renderResponse, String viewId)
            throws IOException, PortletException {

        //Portlets are provided in a namespace which is used to uniquely
        //identify aspects (e.g. JavaScript, JSF component IDs) of the portlet.
        //JSF uses the ExternalContext.encodeNamespace() method to do this.
        //Because we are dispatching, we have to ensure that we make this
        //setting available to the ICEfaces framework.
        addAttribute(renderRequest, Constants.NAMESPACE_KEY, renderResponse.getNamespace());

        //General marker attribute that shows that this request originated from
        //a portlet environment.
        addAttribute(renderRequest, Constants.PORTLET_KEY, PORTLET_MARKER);

        //We request a dispatcher for the actual resource which is typically
        //an .iface.  This maps to the proper handler, typically the ICEfaces
        //MainServlet which takes over the processing.
        PortletContext ctxt = portletConfig.getPortletContext();
        PortletRequestDispatcher disp = ctxt.getRequestDispatcher(viewId);

        if (disp == null) {
            throw new PortletException("could not find dispatcher for " + viewId);
        }

        //IMPORTANT: See the JavaDoc for this class
        PortletArtifactWrapper wrapper =
                new PortletArtifactWrapper(portletConfig, renderRequest, renderResponse);
        addAttribute(renderRequest, PortletArtifactWrapper.PORTLET_ARTIFACT_KEY, wrapper);

        // Jack: This is a temporary fix for JBoss Portal. We should come up
        //       with a better fix in our framework that makes sure the
        //       Content-Type is set either before or when the
        //       ServletExternalContext.getWriter(String encoding) method is
        //       invoked.
        renderResponse.setContentType("text/html");
        disp.include(renderRequest, renderResponse);
    }


    private static void addAttribute(RenderRequest req, String key, Object value) {
        if (key != null && value != null) {
            req.setAttribute(key, value);
        }
    }
}
