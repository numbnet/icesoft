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

package org.icefaces.push.http.standard;

import org.icefaces.push.Configuration;
import org.icefaces.push.http.MimeTypeMatcher;
import org.icefaces.push.http.Request;
import org.icefaces.push.http.RequestProxy;
import org.icefaces.push.http.Response;
import org.icefaces.push.http.ResponseHandler;
import org.icefaces.push.http.ResponseProxy;
import org.icefaces.push.http.Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPOutputStream;

public class CompressingServer implements Server {
    private Server server;
    private MimeTypeMatcher mimeTypeMatcher;
    private boolean compressResources;
    private List noCompressForMimeTypes;

    public CompressingServer(Server server, MimeTypeMatcher mimeTypeMatcher, Configuration configuration) {
        this.server = server;
        this.mimeTypeMatcher = mimeTypeMatcher;
        this.compressResources = configuration.getAttributeAsBoolean("compressResources", true);
        this.noCompressForMimeTypes = Arrays.asList(configuration.getAttribute("compressResourcesExclusions",
                "image/gif image/png image/jpeg image/tiff " +
                        "application/pdf application/zip application/x-compress application/x-gzip application/java-archive " +
                        "video/x-sgi-movie audio/x-mpeg video/mp4 video/mpeg"
        ).split(" "));
    }

    public void service(Request request) throws Exception {
        if (compressResources) {
            String mimeType = mimeTypeMatcher.mimeTypeFor(request.getURI().getPath());
            if (noCompressForMimeTypes.contains(mimeType)) {
                server.service(request);
            } else {
                String acceptEncodingHeader = request.getHeader("Accept-Encoding");
                if (acceptEncodingHeader != null && (acceptEncodingHeader.indexOf("gzip") >= 0 || acceptEncodingHeader.indexOf("compress") >= 0)) {
                    server.service(new CompressingRequest(request));
                } else {
                    server.service(request);
                }
            }
        } else {
            server.service(request);
        }
    }

    public void shutdown() {
        server.shutdown();
    }

    private class CompressingRequest extends RequestProxy {
        public CompressingRequest(Request request) {
            super(request);
        }

        public void respondWith(final ResponseHandler handler) throws Exception {
            request.respondWith(new ResponseHandler() {
                public void respond(Response response) throws Exception {
                    CompressingResponse compressingResponse = new CompressingResponse(response);
                    handler.respond(compressingResponse);
                    compressingResponse.finishCompression();
                }
            });
        }
    }

    private class CompressingResponse extends ResponseProxy {
        private GZIPOutputStream output;

        public CompressingResponse(Response response) {
            super(response);
            response.setHeader("Content-Encoding", "gzip");
        }

        public OutputStream writeBody() throws IOException {
            return output = new GZIPOutputStream(response.writeBody());
        }

        public void writeBodyFrom(InputStream in) throws IOException {
            try {
                copy(in, writeBody());
            } finally {
                in.close();
            }
        }

        public void finishCompression() throws IOException {
            if (output != null) {
                output.finish();
            }
        }
    }

    private static void copy(InputStream input, OutputStream output) throws IOException {
        byte[] buf = new byte[4096];
        int len;
        while ((len = input.read(buf)) > -1) output.write(buf, 0, len);
    }
}
