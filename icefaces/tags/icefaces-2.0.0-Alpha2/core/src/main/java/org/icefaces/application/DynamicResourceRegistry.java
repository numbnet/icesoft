package org.icefaces.application;

import org.icefaces.push.DynamicResourceDispatcher;
import org.icefaces.push.http.DynamicResource;
import org.icefaces.push.http.DynamicResourceLinker;

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
            Map session = context.getExternalContext().getSessionMap();
            return (DynamicResourceRegistry) session.get(DynamicResourceDispatcher.class.getName());
        }
    }
}
