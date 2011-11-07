/*
 * Version: MPL 1.1
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
 * 2004-2009 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.impl.application;

import org.icefaces.util.EnvUtils;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.FacesContext;

public class MyFacesResourceHandlingFix extends ResourceHandlerWrapper {
    private ResourceHandler handler;
    private boolean fixResourceNames;

    public MyFacesResourceHandlingFix(ResourceHandler handler) {
        this.handler = handler;
        this.fixResourceNames = EnvUtils.isUniqueResourceURLs(FacesContext.getCurrentInstance()) && EnvUtils.isMyFaces();
    }

    public Resource createResource(String resourceName) {
        return super.createResource(stripQueryString(resourceName));
    }

    public Resource createResource(String resourceName, String libraryName) {
        return super.createResource(stripQueryString(resourceName), libraryName);
    }

    public Resource createResource(String resourceName, String libraryName, String contentType) {
        return super.createResource(stripQueryString(resourceName), libraryName, contentType);
    }

    public ResourceHandler getWrapped() {
        return handler;
    }

    private String stripQueryString(String resourceName) {
        if (fixResourceNames) {
            int position = resourceName.indexOf("?");
            return position > -1 ? resourceName.substring(0, position) : resourceName;
        } else {
            return resourceName;
        }
    }
}
