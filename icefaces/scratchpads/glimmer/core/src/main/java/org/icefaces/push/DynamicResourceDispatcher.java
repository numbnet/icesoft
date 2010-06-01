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

package org.icefaces.push;

import org.icefaces.application.DynamicResourceRegistry;
import org.icefaces.push.http.DynamicResource;
import org.icefaces.push.http.DynamicResourceLinker;
import org.icefaces.push.http.MimeTypeMatcher;
import org.icefaces.push.http.Request;
import org.icefaces.push.http.Response;
import org.icefaces.push.http.ResponseHandler;
import org.icefaces.push.http.Server;
import org.icefaces.push.http.standard.CompressingServer;
import org.icefaces.push.http.standard.PathDispatcherServer;
import org.icefaces.push.servlet.SessionDispatcher;
import org.icefaces.util.Base64;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;
import java.io.Serializable;

public class DynamicResourceDispatcher implements Server, DynamicResourceRegistry, Serializable {
    private static Logger log = Logger.getLogger("org.icefaces.resourcedispatcher");
    private static final DynamicResourceLinker.Handler NOOPHandler = new DynamicResourceLinker.Handler() {
        public void linkWith(DynamicResourceLinker linker) {
            //do nothing!
        }
    };
    private PathDispatcherServer dispatcher = new PathDispatcherServer();
    private Server compressResource;
    private MimeTypeMatcher mimeTypeMatcher;
    private String prefix;
    private ArrayList registered = new ArrayList();
    private transient SessionDispatcher.Monitor monitor;

    public DynamicResourceDispatcher(String prefix, MimeTypeMatcher mimeTypeMatcher, SessionDispatcher.Monitor monitor, HttpSession session, Configuration configuration) {
        this.prefix = prefix;
        this.mimeTypeMatcher = mimeTypeMatcher;
        this.monitor = monitor;
        this.compressResource = new CompressingServer(dispatcher, mimeTypeMatcher, configuration);
        //auto-register
        session.setAttribute(DynamicResourceDispatcher.class.getName(), this);
    }

    public void service(Request request) throws Exception {
        try {
            compressResource.service(request);
        } catch (IOException e) {
            //capture & log Tomcat specific exception
            if (e.getClass().getName().endsWith("ClientAbortException")) {
                log.fine("Browser closed the connection prematurely for " + request.getURI());
            } else {
                throw e;
            }
        }
    }

    public URI registerResource(DynamicResource resource) {
        return registerResource(resource, NOOPHandler);
    }

    public URI registerResource(DynamicResource resource, DynamicResourceLinker.Handler handler) {
        if (handler == null)
            handler = NOOPHandler;
        final FileNameOption options = new FileNameOption();
        try {
            resource.withOptions(options);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String filename = options.getFileName();
        String dispatchFilename, uriFilename;
        if (filename == null || filename.trim().equals("")) {
            dispatchFilename = uriFilename = "";
        } else {
            dispatchFilename = convertToEscapedUnicode(filename);
            try {
                uriFilename = java.net.URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
            } catch (UnsupportedEncodingException e) {
                uriFilename = filename;
                e.printStackTrace();
            }
        }
        final String name = prefix + "/" + encode(resource) + "/";
        if (!registered.contains(name)) {
            registered.add(name);
            dispatcher.dispatchOn(".*" + name.replaceAll("\\/", "\\/") + dispatchFilename + "$", new ResourceServer(resource));
            if (handler != NOOPHandler) {
                handler.linkWith(new RelativeResourceLinker(name));
            }
        }

        return URI.create(name + uriFilename);
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
        private final DynamicResource resource;

        public ResourceServer(DynamicResource resource) {
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
            if (options.attachement && options.contentDispositionFileName != null) {
                response.setHeader("Content-Disposition", "attachment; filename" + options.contentDispositionFileName);
            }
            InputStream inputStream = resource.open();
            if (inputStream == null) {
                throw new IOException("Resource of type " + resource.getClass().getName() + "[digest: " +
                        resource.calculateDigest() + "; mime-type: " + options.mimeType +
                        (options.attachement ? "; attachment: " + options.fileName : "") +
                        "] returned a null input stream.");
            } else {
                response.writeBodyFrom(inputStream);
            }
        }

        public void shutdown() {
        }

        private class ResourceOptions implements ExtendedResourceOptions {
            private Date lastModified = new Date();
            private Date expiresBy = monitor.expiresBy();
            private String mimeType;
            private String fileName;
            private boolean attachement;
            private String contentDispositionFileName;

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
            // ICE-4342
            // Encoded filename in Content-Disposition header; to be used in save file dialog;
            // See http://greenbytes.de/tech/tc2231/
            public void setContentDispositionFileName(String contentDispositionFileName) {
                this.contentDispositionFileName = contentDispositionFileName;
            }
        }
    }

    private static String encode(DynamicResource resource) {
        return Base64.encode(String.valueOf(resource.calculateDigest().hashCode()));
    }

    public static String convertToEscapedUnicode(String s) {
        char[] chars = s.toCharArray();
        String hexStr;
        StringBuffer stringBuffer = new StringBuffer(chars.length * 6);
        String[] leadingZeros = {"0000", "000", "00", "0", ""};
        for (int i = 0; i < chars.length; i++) {
            hexStr = Integer.toHexString(chars[i]).toUpperCase();
            stringBuffer.append("\\u");
            stringBuffer.append(leadingZeros[hexStr.length()]);
//            stringBuffer.append("0000".substring(0, 4 - hexStr.length()));
            stringBuffer.append(hexStr);
        }
        return stringBuffer.toString();
    }

    private class RelativeResourceLinker implements DynamicResourceLinker {
        private final String name;

        public RelativeResourceLinker(String name) {
            this.name = name;
        }

        public void registerRelativeResource(String path, DynamicResource relativeResource) {
            String pathExpression = (name + path).replaceAll("\\/", "\\/").replaceAll("\\.", "\\.");
            dispatcher.dispatchOn(".*" + pathExpression + "$", new ResourceServer(relativeResource));
        }
    }

    private class FileNameOption implements DynamicResource.Options {
        private String fileName;

        public String getFileName() {
            return fileName;
        }

        public void setAsAttachement() {
        }

        public void setExpiresBy(Date date) {
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public void setLastModified(Date date) {
        }

        public void setMimeType(String mimeType) {
        }
    }

    public interface ExtendedResourceOptions extends DynamicResource.Options {
        public void setContentDispositionFileName(String contentDispositionFileName);
    }
}
