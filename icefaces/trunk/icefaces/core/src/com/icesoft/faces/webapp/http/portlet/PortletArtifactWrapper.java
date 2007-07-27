package com.icesoft.faces.webapp.http.portlet;

import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * WARNING! WARNING! WARNING!
 * <p/>
 * This class is temporary and should not be relied on as it is extremely likely
 * not to be the final solution.
 * <p/>
 * Because we use a dispatcher to pass requests from the MainPortlet to the
 * MainServlet, the resulting portlet artifacts (like request, response, etc.)
 * are wrapped to look like servlet artifacts and certain types and APIs that
 * are specific to portlets are not available to the developer.  As a temporary
 * solution, we provide this class stored as a request attribute that can be
 * retrieved.
 */
public class PortletArtifactWrapper {

    public static final String PORTLET_ARTIFACT_KEY =
            "com.icesoft.faces.portlet.artifact";

    private PortletConfig portletConfig;
    private RenderRequest request;
    private RenderResponse response;

    public PortletArtifactWrapper(PortletConfig portletConfig,
                               RenderRequest request, RenderResponse response) {
        this.portletConfig = portletConfig;
        this.request = request;
        this.response = response;
    }

    public RenderRequest getRequest() {
        return this.request;
    }

    public RenderResponse getResponse() {
        return response;
    }

    public PortletConfig getPortletConfig() {
        return (this.portletConfig);
    }
}
