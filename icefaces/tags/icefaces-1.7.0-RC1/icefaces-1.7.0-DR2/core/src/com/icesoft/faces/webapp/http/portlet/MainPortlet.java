package com.icesoft.faces.webapp.http.portlet;

import com.icesoft.jasper.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.io.IOException;

/**
 * The MainPortlet is the entry point for ICEfaces-based portlets.  The goal is
 * we set up the environment as required and then dispatch the request to the
 * MainServlet and let the framework do all the normal processing.  It's
 * basically only the initial page load that we care about.  The rest of the
 * processing is handled between the ICEfaces JavaScript bridge and the
 * MainServlet via AJAX mechanisms.  The main activities we do on the first page
 * load are:
 * <p/>
 * - Get the initial view from the portlet config and set it as a request
 * attribute.  We use the key "javax.servlet.include.request_uri" as portlets
 * are fragrments and so to the framework, they are treated much like includes.
 * <p/>
 * - Get a request dispatcher for the view (typically an .iface resource) and
 * call the include() method of the dispatcher.  By checking for the include
 * attribute on the request, the framework should process it correctly.
 */
public class MainPortlet implements Portlet {

    private static Log log = LogFactory.getLog(MainPortlet.class);

    private static final String PORTLET_MARKER = "portlet";
    private PortletConfig portletConfig;

    public void init(PortletConfig portletConfig)
            throws PortletException {

        this.portletConfig = portletConfig;

        if (log.isTraceEnabled()) {
            log.trace("portlet config: " + portletConfig);
        }
    }

    public void destroy() {

        if( log == null ){
            return;
        }
        
        if (log.isTraceEnabled()) {
            log.trace("portlet config: " + portletConfig);
        }
    }

    public void processAction(
            ActionRequest actionRequest, ActionResponse actionResponse)
            throws IOException, PortletException {
    }

    public void render(
            RenderRequest renderRequest, RenderResponse renderResponse)
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

        //Get the inital view that is configured in the portlet.xml file
        PortletMode portletMode = renderRequest.getPortletMode();
        String viewId = null;
        if (portletMode == PortletMode.VIEW) {
            viewId = portletConfig.getInitParameter(Constants.VIEW_KEY);
            if (viewId == null) {
                if (log.isErrorEnabled()) {
                    log.error(Constants.VIEW_KEY + " is not properly configured");
                }
                throw new PortletException(Constants.VIEW_KEY + " is not properly configured");
            }
        } else if (portletMode == PortletMode.EDIT) {
            viewId = portletConfig.getInitParameter(Constants.EDIT_KEY);
            if (viewId == null) {
                if (log.isErrorEnabled()) {
                    log.error(Constants.EDIT_KEY + " is not properly configured");
                }
                throw new PortletException(Constants.EDIT_KEY + " is not properly configured");
            }
        } else if (portletMode == PortletMode.HELP) {
            viewId = portletConfig.getInitParameter(Constants.HELP_KEY);
            if (viewId == null) {
                if (log.isErrorEnabled()) {
                    log.error(Constants.HELP_KEY + " is not properly configured");
                }
                throw new PortletException(Constants.HELP_KEY + " is not properly configured");
            }
        }

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
        if (log.isTraceEnabled()) {
            log.trace(key + ": " + value);
        }
    }

}
