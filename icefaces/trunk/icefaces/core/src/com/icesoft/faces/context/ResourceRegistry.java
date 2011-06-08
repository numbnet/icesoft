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

package com.icesoft.faces.context;

import java.net.URI;

/**
 * A ResourceRegistry is meant to be used by the component renderers to load
 * Javascript code, CSS rules, and also register any kind of resource the
 * component renderer needs (such as images, movies, flash...). The resources are bound to the user session
 * that registered them, once the session expired/invalidated the resources are discarded automatically.
 * For an application wide resource data each user session will have to register a resource referencing back to
 * this data.
 */
public interface ResourceRegistry {

    /**
     * Register Javascript code to be served and load the code by inserting a
     * reference to it into the page. The code will be loaded only once, the
     * number of method invocations or number of component instances using this
     * registry are irrelevant.
     *
     * @param resource the Javascript code
     * @return the URI of the resource
     */
    URI loadJavascriptCode(Resource resource);

    /**
     * Register Javascript code to be served and load the code by inserting a
     * reference to it into the page. The code will be loaded only once, the
     * number of method invocations or number of component instances using this
     * registry are irrelevant.
     *
     * @param resource      the main Javascript code
     * @param linkerHandler handler used to specify any other resource relatively
     *                      referenced by the main resource
     * @return the URI of the resource
     */
    URI loadJavascriptCode(Resource resource, ResourceLinker.Handler linkerHandler);

    /**
     * Register CSS rules to be served and load the rules by inserting a
     * reference to them into the page. The rules will be loaded only once, the
     * number of method invocations or number of component instances using this
     * registry are irrelevant.
     *
     * @param resource the css rules
     * @return the URI of the resource
     */
    URI loadCSSRules(Resource resource);

    /**
     * Register CSS rules to be served and load the rules by inserting a
     * reference to them into the page. The rules will be loaded only once, the
     * number of method invocations or number of component instances using this
     * registry are irrelevant.
     *
     * @param resource      the main css rules
     * @param linkerHandler handler used to specify any other resource relatively
     *                      referenced by the main resource (such as '@import' rules)
     * @return the URI of the resource
     */
    URI loadCSSRules(Resource resource, ResourceLinker.Handler linkerHandler);

    /**
     * Register resource to be served. The URI is encoded using
     * {@link javax.faces.application.ViewHandler#getResourceURL(javax.faces.context.FacesContext,String)} so
     * that proper resolution is achieved when template subdirectories or
     * forwards are used.
     *
     * @param resource the resource
     * @return the URI of the resource
     */
    URI registerResource(Resource resource);

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
    URI registerResource(Resource resource, ResourceLinker.Handler linkerHandler);

    //deprecated methods -----------------------------------------------------------------------------------------------

    /**
     * Register resource to be served. The URI is encoded using
     * {@link javax.faces.application.ViewHandler#getResourceURL(javax.faces.context.FacesContext,String)} so
     * that proper resolution is achieved when template subdirectories or
     * forwards are used.
     *
     * @param mimeType the mime-type of the resource
     * @param resource the resource
     * @return the URI of the resource
     * @deprecated use {@link ResourceRegistry#registerResource(Resource)} instead
     */
    URI registerResource(String mimeType, Resource resource);

    /**
     * Register resource to be served. The URI is encoded using
     * {@link javax.faces.application.ViewHandler#getResourceURL(javax.faces.context.FacesContext,String)} so
     * that proper resolution is achieved when template subdirectories or
     * forwards are used.
     *
     * @param mimeType      the mime-type of the resource
     * @param resource      the resource
     * @param linkerHandler handler used to specify any other resource relatively
     *                      referenced by the main resource (such as '@import' rules)
     * @return the URI of the resource
     * @deprecated use {@link ResourceRegistry#registerResource(Resource,  ResourceLinker.Handler)} instead
     */
    URI registerResource(String mimeType, Resource resource, ResourceLinker.Handler linkerHandler);

    /**
     * Register resource to be served. The URI is encoded using
     * {@link javax.faces.application.ViewHandler#getResourceURL(javax.faces.context.FacesContext,String)} so
     * that proper resolution is achieved when template subdirectories or
     * forwards are used.
     *
     * @param name     the name of the resource
     * @param resource the resource
     * @return the URI of the resource
     * @deprecated use {@link ResourceRegistry#registerResource(Resource)} instead
     */
    URI registerNamedResource(String name, Resource resource);

    /**
     * Register resource to be served. The URI is encoded using
     * {@link javax.faces.application.ViewHandler#getResourceURL(javax.faces.context.FacesContext,String)} so
     * that proper resolution is achieved when template subdirectories or
     * forwards are used.
     *
     * @param name          the name of the resource
     * @param resource      the resource
     * @param linkerHandler handler used to specify any other resource relatively
     *                      referenced by the main resource (such as '@import' rules)
     * @return the URI of the resource
     * @deprecated use {@link ResourceRegistry#registerResource(Resource,  ResourceLinker.Handler)} instead
     */
    URI registerNamedResource(String name, Resource resource, ResourceLinker.Handler linkerHandler);

    /**
     * Register resource to be served. The URI is *not* encoded by
     * {@link javax.faces.application.ViewHandler#getResourceURL(javax.faces.context.FacesContext,String)}. The returned
     * path starts from the web-application context path which is considered to be the root.
     *
     * @param resource the resource
     * @return the URI of the resource
     */
    URI registerResourceWithRelativePath(Resource resource);

    /**
     * Register resource to be served. The URI is *not* encoded by
     * {@link javax.faces.application.ViewHandler#getResourceURL(javax.faces.context.FacesContext,String)}. The returned
     * path starts from the web-application context path which is considered to be the root.
     *
     * @param resource      the resource
     * @param linkerHandler handler used to specify any other resource relatively
     *                      referenced by the main resource (such as '@import' rules)
     * @return the URI of the resource
     */
    URI registerResourceWithRelativePath(Resource resource, ResourceLinker.Handler linkerHandler);

    /**
     * Deregister the resource.
     *
     * @param resource the registered resource
     */
    void deregisterResource(Resource resource);

    /**
     * Test if resource was registred.
     *
     * @param resource tested resourced
     * @return true if resource was registered previously
     */
    boolean isRegistered(Resource resource);
}