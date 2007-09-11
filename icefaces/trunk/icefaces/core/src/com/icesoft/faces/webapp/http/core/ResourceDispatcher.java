package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.context.Resource;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Response;
import com.icesoft.faces.webapp.http.common.ResponseHandler;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.common.standard.CompressingServer;
import com.icesoft.faces.webapp.http.common.standard.PathDispatcherServer;
import com.icesoft.util.encoding.Base64;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;

public class ResourceDispatcher implements Server {
    private PathDispatcherServer dispatcher = new PathDispatcherServer();
    private Server compressResource = new CompressingServer(dispatcher);
    private String prefix;
    private ArrayList registered = new ArrayList();

    public ResourceDispatcher(String prefix) {
        this.prefix = prefix;
    }

    public void service(Request request) throws Exception {
        compressResource.service(request);
    }

    public URI registerResource(String mimeType, Resource resource) {
        String name = prefix + encode(resource);
        if (!registered.contains(name)) {
            registered.add(name);
            dispatcher.dispatchOn(".*" + name, new ResourceServer(mimeType, resource));
        }

        return URI.create(name);
    }

    public void shutdown() {
        compressResource.shutdown();
        registered.clear();
    }

    private static class ResourceServer implements Server, ResponseHandler {
        private ResponseHandler notModified = new ResponseHandler() {
            public void respond(Response response) throws Exception {
                response.setStatus(304);
                response.setHeader("ETag", encode(resource));
                response.setHeader("Date", new Date());
                response.setHeader("Last-Modified", resource.lastModified());
            }
        };
        private String mimeType;
        private final Resource resource;

        public ResourceServer(String mimeType, Resource resource) {
            this.mimeType = mimeType;
            this.resource = resource;
        }

        public void service(Request request) throws Exception {
            try {
                Date modifiedSince = request.getHeaderAsDate("If-Modified-Since");
                if (resource.lastModified().getTime() > modifiedSince.getTime() + 1000) {
                    request.respondWith(this);
                } else {
                    request.respondWith(notModified);
                }
            } catch (Exception e) {
                request.respondWith(this);
            }
        }

        public void respond(Response response) throws Exception {
            response.setHeader("ETag", encode(resource));
            response.setHeader("Cache-Control", "private");
            response.setHeader("Content-Type", mimeType);
            response.setHeader("Last-Modified", resource.lastModified());
            response.writeBodyFrom(resource.open());
        }

        public void shutdown() {
        }
    }

    private static String encode(Resource resource) {
        return Base64.encode(String.valueOf(resource.calculateDigest().hashCode()));
    }
}
