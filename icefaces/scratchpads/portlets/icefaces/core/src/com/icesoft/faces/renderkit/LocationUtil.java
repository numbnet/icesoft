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
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
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
 *
 */

package com.icesoft.faces.renderkit;

import com.icesoft.jasper.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class LocationUtil {

    private static Log log = LogFactory.getLog(LocationUtil.class);

    public static String getAppBase(FacesContext facesContext) {
        ExternalContext extCtxt = facesContext.getExternalContext();
        String base = extCtxt.getRequestContextPath();
        return base + "/";
    }

    public static String getResourcePath(FacesContext facesContext,
                                         String resource) {

        //Context and resource must be non-null
        if (facesContext == null) {
            throw new NullPointerException("context cannot be null");
        }

        if (resource == null) {
            throw new NullPointerException("path cannot be null");
        }

        ExternalContext extCtxt = facesContext.getExternalContext();

        //Purely for debugging purposes
        //dumpInfo(extCtxt, resource);

        // Components that render out links to resources like images, CSS,
        // JavaScript, etc. must do it correctly.  In a normal web app, there
        // isn't much to do but in a portlet environment, we have to resolve
        // these with a bit of work.
        if (isPortlet(extCtxt)) {
            resource = resolveFully(extCtxt, resource);
        }

        //Encoding may or may not be strictly necessary but we'll do it to
        //be safe.
        return extCtxt.encodeResourceURL(resource);
    }

    public static boolean isPortlet(ExternalContext extCtxt) {
        return extCtxt.getRequestMap().get(Constants.PORTLET_KEY) != null;
    }

    /**
     * Resolves references fragements to the full resource reference including
     * the context path and the servlet path.
     */
    private static String resolveFully(ExternalContext extCtxt,
                                       String resource) {

        String base = extCtxt.getRequestContextPath() +
                              extCtxt.getRequestServletPath();

        try {
            URI baseURI = new URI(base);
            URI resourceURI = new URI(resource);
            URI resolvedURI = baseURI.resolve(resourceURI);
            return resolvedURI.toString();

        } catch (URISyntaxException e) {
            if( log.isWarnEnabled() ){
                log.warn( "could not resolve URI's based on" +
                          "\n  context : " + extCtxt.getRequestContextPath() +
                          "\n  path    : " + extCtxt.getRequestServletPath() +
                          "\n  resource: " + resource, e );
            }
            return resource;
        }


    }

    private static void dumpInfo(ExternalContext extCtxt, String resource) {
        String encodedResource = extCtxt.encodeResourceURL(resource);

        URI resourceURI = null;
        try {
            resourceURI = new URI(resource);
        } catch (URISyntaxException e) {
        }

        URL resourceURL = null;
        try {
            resourceURL = extCtxt.getResource(resource);
        } catch (MalformedURLException e) {
        }

        StringBuffer buff = new StringBuffer("RESOURCE INFO");
        buff.append("\n  resource    : ");
        buff.append(resource);
        buff.append("\n  encoded     : ");
        buff.append(encodedResource);
        buff.append("\n  resource    : ");
        buff.append(resource);
        buff.append("\n  resource    : ");
        buff.append(resource);
        buff.append("\n  resource    : ");
        buff.append(resource);
        buff.append("\n  resource    : ");
        buff.append(resource);
        buff.append("\n  resource    : ");
        buff.append(resource);
        buff.append("\n  resource    : ");
        buff.append(resource);

        if (log.isInfoEnabled()) {
            log.info("RESOURCE INFO" +
                     "\n  resource    : " + resource +
                     "\n  encoded     : " + encodedResource +
                     "\n  context path: " + extCtxt.getRequestContextPath() +
                     "\n  path info   : " + extCtxt.getRequestPathInfo() +
                     "\n  servlet path: " + extCtxt.getRequestServletPath() +
                     "\n  resource URL: " + resourceURL +
                     "\n  resource URI: " + resourceURI +
                     "\n  is portlet  : " + isPortlet(extCtxt) +
                     "\n  request uri : " +
                     ((HttpServletRequest) extCtxt.getRequest()).getRequestURI()
            );
        }
    }
}
