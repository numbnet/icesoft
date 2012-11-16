package com.icesoft.faces.component.gmap;

import org.icefaces.impl.util.Base64;
import org.icefaces.impl.util.Util;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GMapResourceHandler extends ResourceHandlerWrapper {
    private static final String GMAP_JS = "gmap/gmap.js";
    private static final String GMAP_MAIN_JS = "gmap/main.js";
    private static final byte[] NO_BYTES = new byte[0];
    private ResourceHandler handler;
    private String gmapKey;
    private Resource gmapJS;
    private Resource mainJS;

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
        Resource resource = super.createResource(resourceName, libraryName, contentType);
        if (GMAP_JS.equals(resourceName) && gmapKey != null) {
            if (gmapJS == null) {
                //change returned resource to point to a different URI
				if(!FacesContext.getCurrentInstance().getExternalContext().isSecure())
                return gmapJS = recreateResource(resource, "http://maps.googleapis.com/maps/api/js?key=" + gmapKey + "&sensor=true");
				else
				return gmapJS = recreateResource(resource, "https://maps.googleapis.com/maps/api/js?key=" + gmapKey + "&sensor=true");
            } else {
                //return cached resource
                return gmapJS;
            }
        } 
		else if (GMAP_MAIN_JS.equals(resourceName)) {
            if (mainJS == null) {
                //change returned resource to point to a different URI
                return mainJS = recreateResource(resource, "http://maps.gstatic.com/intl/en_ALL/mapfiles/400d/maps3.api/main.js");
            } else {
                //return cached resource
                return mainJS;
            }
        }
		else {
            return resource;
        }
    }

    private Resource recreateResource(Resource resource, String url) {
        byte[] content;
        try {
            InputStream in = new URL(url).openConnection().getInputStream();
            content = readIntoByteArray(in);
        } catch (IOException e) {
            content = NO_BYTES;
        }
        return new ResourceEntry(GMAP_JS, resource.getRequestPath(), content);
    }

    private static byte[] readIntoByteArray(InputStream in) throws IOException {
        byte[] buffer = new byte[4096];
        int bytesRead;
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead); // write
        }
        out.flush();

        return out.toByteArray();
    }

    private class ResourceEntry extends Resource {
        private Date lastModified = new Date();
        private String localPath;
        private String requestPath;
        private byte[] content;
        private String mimeType;

        private ResourceEntry(String localPath, String requestPath, byte[] content) {
            this.localPath = localPath;
            this.requestPath = requestPath;
            this.content = content;
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            this.mimeType = externalContext.getMimeType(localPath);
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(content);
        }

        public Map<String, String> getResponseHeaders() {

            HashMap headers = new HashMap();
            headers.put("ETag", eTag());
            headers.put("Cache-Control", "public");
            headers.put("Content-Type", mimeType);
            headers.put("Date", Util.HTTP_DATE.format(new Date()));
            headers.put("Last-Modified", Util.HTTP_DATE.format(lastModified));

            return headers;
        }

        public String getContentType() {
            return mimeType;
        }

        public String getRequestPath() {
            return requestPath;
        }

        public URL getURL() {
            try {
                return FacesContext.getCurrentInstance().getExternalContext().getResource(localPath);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        public boolean userAgentNeedsUpdate(FacesContext context) {
            try {
                Date modifiedSince = Util.HTTP_DATE.parse(context.getExternalContext().getRequestHeaderMap().get("If-Modified-Since"));
                return lastModified.getTime() > modifiedSince.getTime() + 1000;
            } catch (Throwable e) {
                return true;
            }
        }

        private String eTag() {
            return Base64.encode(String.valueOf(localPath.hashCode()));
        }
    }

}
