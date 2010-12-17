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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 */

package org.icefaces.impl.push;

import org.icefaces.impl.push.DynamicResourceDispatcher;
import org.icefaces.impl.push.http.DynamicResource;
import org.icefaces.impl.push.http.DynamicResourceLinker;

import javax.faces.context.FacesContext;
import java.net.URI;
import java.util.Map;

public interface DynamicResourceRegistry {
    /**
     * Register resource to be served. The URI is encoded using
     * {@link javax.faces.application.ViewHandler#getResourceURL(javax.faces.context.FacesContext,String)} so
     * that proper resolution is achieved when template subdirectories or
     * forwards are used.
     *
     * @param resource the resource
     * @return the URI of the resource
     */
    URI registerResource(DynamicResource resource);

    /**
     * Register resource to be served. The URI is encoded using
     * {@link javax.faces.application.ViewHandler#getResourceURL(javax.faces.context.FacesContext,String)} so
     * that proper resolution is achieved when template subdirectories or
     * forwards are used.
     *
     * @param resource      the resource
     * @param linkerHandler handler used to specify any other resource relatively
     *                      referenced by the main resource (such as '@import' rules)
     * @return the URI of the resource
     */
    URI registerResource(DynamicResource resource, DynamicResourceLinker.Handler linkerHandler);

    public class Locator {

        public static DynamicResourceRegistry locate(FacesContext context) {
            Map applicationMap = context.getExternalContext().getApplicationMap();
            return (DynamicResourceRegistry) applicationMap.get(DynamicResourceDispatcher.class.getName());
        }
    }
}
