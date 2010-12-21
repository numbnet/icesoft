/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
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
 */

package org.icefaces.impl.push.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;


/**
 * This class serves as a JSF 2.0 resource for ICEpush. Although the Resource superclass defines several methods, only
 * the getRequestPath() method was necessary to implement for the purposes of ICEpush.
 */
public class ICEpushListenResource extends Resource {

    // Public Constants
    public static final String RESOURCE_NAME = "listen.icepush";

    // Private Constants
    private static final String DUMMY_RESOURCE_NAME = "bridge.js";

    // Private Data Members
    private ResourceHandler resourceHandler;
    private String requestPath;

    public ICEpushListenResource(ResourceHandler resourceHandler) {
        this.resourceHandler = resourceHandler;
    }

    @Override
    public String toString() {
        return getRequestPath();
    }

    @Override
    public boolean userAgentNeedsUpdate(FacesContext context) {
        return true;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public String getRequestPath() {
        if (requestPath == null) {
            // We want to reuse the JSF implementation's robust getRequestPath() method, but Mojarra will refuse to
            // create a resource for file that does not exist. For this reason, we ask the implementation to create a
            // temporary/dummy resource for a file that actually exists.
            Resource dummyResource = resourceHandler.createResource(DUMMY_RESOURCE_NAME);
            // Get the request path for the dummy resource, and simply swap out the dummy's resource name for the
            // ICEpushListenResource name.
            requestPath = dummyResource.getRequestPath();
            requestPath = requestPath.replace(DUMMY_RESOURCE_NAME, RESOURCE_NAME);
        }
        return requestPath;
    }

    @Override
    public Map<String, String> getResponseHeaders() {
        return null;
    }

    @Override
    public URL getURL() {
        return null;
    }

}
