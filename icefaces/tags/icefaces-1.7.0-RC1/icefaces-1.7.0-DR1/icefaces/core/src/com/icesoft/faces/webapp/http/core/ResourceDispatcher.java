package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.context.ByteArrayResource;
import com.icesoft.faces.context.Resource;
import com.icesoft.faces.webapp.http.common.MimeTypeMatcher;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Response;
import com.icesoft.faces.webapp.http.common.ResponseHandler;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.common.standard.CompressingServer;
import com.icesoft.faces.webapp.http.common.standard.PathDispatcherServer;
import com.icesoft.util.encoding.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ResourceDispatcher implements Server {
    private PathDispatcherServer dispatcher = new PathDispatcherServer();
    private Server compressResource = new CompressingServer(dispatcher);
    private MimeTypeMatcher mimeTypeMatcher;
    private String prefix;
    private ArrayList registered = new ArrayList();

    public ResourceDispatcher(String prefix, MimeTypeMatcher mimeTypeMatcher) {
        this.prefix = prefix;
        this.mimeTypeMatcher = mimeTypeMatcher;
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

    public URI registerZippedResources(Resource resource) throws IOException {
        String name = prefix + encode(resource);
        if (!registered.contains(name)) {
            registered.add(name);
            ZipInputStream unzippedStream = new ZipInputStream(resource.open());
            ZipEntry entry;
            while ((entry = unzippedStream.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    String entryName = entry.getName();
                    String mimeType = mimeTypeMatcher.mimeTypeFor(entryName);
                    String pathExpression = (name + "/" + entryName).replaceAll("\\/", "\\/").replaceAll("\\.", "\\.");
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    copy(unzippedStream, out);
                    Resource byteArrayResource = new ByteArrayResource(out.toByteArray());
                    dispatcher.dispatchOn(".*" + pathExpression, new ResourceServer(mimeType, byteArrayResource));
                }
            }
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

    private static void copy(InputStream input, OutputStream output) throws IOException {
        byte[] buf = new byte[4096];
        int len = 0;
        while ((len = input.read(buf)) > -1) output.write(buf, 0, len);
    }
}
