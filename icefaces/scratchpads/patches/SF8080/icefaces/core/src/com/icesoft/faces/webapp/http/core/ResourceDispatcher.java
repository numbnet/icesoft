package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.context.Resource;
import com.icesoft.faces.context.ResourceLinker;
import com.icesoft.faces.webapp.http.common.MimeTypeMatcher;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Response;
import com.icesoft.faces.webapp.http.common.ResponseHandler;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.common.standard.CompressingServer;
import com.icesoft.faces.webapp.http.common.standard.PathDispatcherServer;
import com.icesoft.faces.webapp.http.servlet.SessionDispatcher;
import com.icesoft.util.encoding.Base64;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;

public class ResourceDispatcher implements Server {
    private static final ResourceLinker.Handler NOOPHandler = new ResourceLinker.Handler() {
        public void linkWith(ResourceLinker linker) {
            //do nothing!
        }
    };
    private PathDispatcherServer dispatcher = new PathDispatcherServer();
    private Server compressResource;
    private MimeTypeMatcher mimeTypeMatcher;
    private String prefix;
    private ArrayList registered = new ArrayList();
    private SessionDispatcher.Monitor monitor;

    public ResourceDispatcher(String prefix, MimeTypeMatcher mimeTypeMatcher, SessionDispatcher.Monitor monitor, Configuration configuration) {
        this.prefix = prefix;
        this.mimeTypeMatcher = mimeTypeMatcher;
        this.monitor = monitor;
        this.compressResource = new CompressingServer(dispatcher, mimeTypeMatcher, configuration);
    }

    public void service(Request request) throws Exception {
        compressResource.service(request);
    }

    public URI registerResource(Resource resource) {
        return registerResource(resource, NOOPHandler);
    }

    public URI registerResource(Resource resource, ResourceLinker.Handler handler) {
    	if( handler == null )
    		handler = NOOPHandler;
    	final String name = prefix + encode(resource) + "/";
        if (!registered.contains(name)) {
            registered.add(name);
            dispatcher.dispatchOn(".*" + name.replaceAll("\\/", "\\/") + "$", new ResourceServer(resource));
            if (handler != NOOPHandler) {
                handler.linkWith(new RelativeResourceLinker(name));
            }
        }

        return URI.create(name);
    }

    public void shutdown() {
        compressResource.shutdown();
        registered.clear();
    }

    private class ResourceServer implements Server, ResponseHandler {
        private final Date lastModified = new Date();
        private final ResponseHandler notModified = new ResponseHandler() {
            public void respond(Response response) throws Exception {
                response.setStatus(304);
                response.setHeader("ETag", encode(resource));
                response.setHeader("Date", new Date());
                response.setHeader("Last-Modified", lastModified);
                response.setHeader("Expires", monitor.expiresBy());
            }
        };
        private final Resource resource;

        public ResourceServer(Resource resource) {
            this.resource = resource;
        }

        public void service(Request request) throws Exception {
            try {
                Date modifiedSince = request.getHeaderAsDate("If-Modified-Since");
                if (lastModified.getTime() > modifiedSince.getTime() + 1000) {
                    request.respondWith(this);
                } else {
                    request.respondWith(notModified);
                }
            } catch (Exception e) {
                request.respondWith(this);
            }
        }

        public void respond(final Response response) throws Exception {
            ResourceOptions options = new ResourceOptions();
            resource.withOptions(options);
            if (options.mimeType == null && options.fileName != null) {
                options.mimeType = mimeTypeMatcher.mimeTypeFor(options.fileName);
            }
            response.setHeader("ETag", encode(resource));
            response.setHeader("Cache-Control", "public");
            response.setHeader("Content-Type", options.mimeType);
            response.setHeader("Last-Modified", options.lastModified);
            response.setHeader("Expires", options.expiresBy);
            if (options.attachement) {
                response.setHeader("Content-Disposition", "attachment; filename=" + options.fileName);
            }
            response.writeBodyFrom(resource.open());
        }

        public void shutdown() {
        }

        private class ResourceOptions implements Resource.Options {
            private Date lastModified = new Date();
            private Date expiresBy = monitor.expiresBy();
            private String mimeType;
            private String fileName;
            private boolean attachement;

            public void setMimeType(String type) {
                mimeType = type;
            }

            public void setLastModified(Date date) {
                lastModified = date;
            }

            public void setFileName(String name) {
                fileName = name;
            }

            public void setExpiresBy(Date date) {
                expiresBy = date;
            }

            public void setAsAttachement() {
                attachement = true;
            }
        }
    }

    private static String encode(Resource resource) {
        return Base64.encode(String.valueOf(resource.calculateDigest().hashCode()));
    }

    private class RelativeResourceLinker implements ResourceLinker {
        private final String name;

        public RelativeResourceLinker(String name) {
            this.name = name;
        }

        public void registerRelativeResource(String path, Resource relativeResource) {
            String pathExpression = (name + path).replaceAll("\\/", "\\/").replaceAll("\\.", "\\.");
            dispatcher.dispatchOn(".*" + pathExpression + "$", new ResourceServer(relativeResource));
        }
    }
}
