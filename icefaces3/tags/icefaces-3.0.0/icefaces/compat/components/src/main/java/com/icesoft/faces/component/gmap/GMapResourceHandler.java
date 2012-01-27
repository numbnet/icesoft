package com.icesoft.faces.component.gmap;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

public class GMapResourceHandler extends ResourceHandlerWrapper {
    private ResourceHandler handler;
    private String gmapKey;

    public GMapResourceHandler(ResourceHandler handler) {
        this.handler = handler;
        gmapKey = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("com.icesoft.faces.gmapKey");
    }

    public ResourceHandler getWrapped() {
        return handler;
    }

    public Resource createResource(String resourceName) {
        return createResource(resourceName, null, null);
    }

    public Resource createResource(String resourceName, String libraryName) {
        return createResource(resourceName, libraryName, null);
    }

    public Resource createResource(String resourceName, String libraryName, String contentType) {
        if ("gmap/gmap.js".equals(resourceName)) {
            Resource resource = super.createResource(resourceName, libraryName, contentType);
            //change returned resource to point to a different URI
            return new URIResource(resource, "http://maps.google.com/maps?file=api&v=2&key=" + gmapKey);
        } else {
            return super.createResource(resourceName, libraryName, contentType);
        }
    }

    private class URIResource extends Resource {
        private final Resource resource;
        private String uri;

        private URIResource(Resource resource, String uri) {
            this.resource = resource;
            this.uri = uri;
        }

        public InputStream getInputStream() throws IOException {
            return resource.getInputStream();
        }

        public Map<String, String> getResponseHeaders() {
            return resource.getResponseHeaders();
        }

        public String getRequestPath() {
            return uri;
        }

        public URL getURL() {
            return resource.getURL();
        }

        public boolean userAgentNeedsUpdate(FacesContext context) {
            return resource.userAgentNeedsUpdate(context);
        }
    }
}
