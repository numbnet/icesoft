/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 */

package com.icesoft.faces.webapp.http.portlet;

import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * Because we are using a RequestDispatcher to bridge portlet handling into our servlet
 * based framework, we "lose" some of the characteristics of the Portlet API.  When
 * the dispatched call arrives at the ICEfaces MainServlet, the request and response
 * objects are wrapped as servlet versions and no longer accessible as portlet types.
 *
 * What we currently do, then, is save instances of those things that the portlet
 * developer might want to access and make them accessible on the other side of
 * the dispatched call.  This class is simply the envelope they are carried in.
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

    public void release()  {
        this.request = null;
        this.response = null;
    }
}
