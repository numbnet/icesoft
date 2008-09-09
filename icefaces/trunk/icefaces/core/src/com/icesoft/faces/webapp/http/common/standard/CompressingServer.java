package com.icesoft.faces.webapp.http.common.standard;

import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.common.MimeTypeMatcher;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.RequestProxy;
import com.icesoft.faces.webapp.http.common.Response;
import com.icesoft.faces.webapp.http.common.ResponseHandler;
import com.icesoft.faces.webapp.http.common.ResponseProxy;
import com.icesoft.faces.webapp.http.common.Server;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPOutputStream;

public class CompressingServer implements Server {
    private static final Log log = LogFactory.getLog(CompressingServer.class);
    private Server server;
    private MimeTypeMatcher mimeTypeMatcher;
    private List noCompressForMimeTypes;

    public CompressingServer(Server server, MimeTypeMatcher mimeTypeMatcher, Configuration configuration) {
        this.server = server;
        this.mimeTypeMatcher = mimeTypeMatcher;
        this.noCompressForMimeTypes = Arrays.asList(configuration.getAttribute("compressResourcesExclusions",
                "image/gif image/png image/jpeg image/tiff " +
                        "application/pdf application/zip application/x-compress application/x-gzip application/java-archive " +
                        "video/x-sgi-movie audio/x-mpeg video/mp4 video/mpeg"
        ).split(" "));
    }

    public void service(Request request) throws Exception {
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
                    try {
                        CompressingResponse compressingResponse = new CompressingResponse(response);
                        handler.respond(compressingResponse);
                        compressingResponse.finishCompression();
                    } catch (IOException e) {
                        //a ClientAbortException (cause: SocketException) might be thrown if the browser closed
                        //the connection before consuming the entire stream
                        log.warn("Connection aborted", e);
                    }
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
        int len = 0;
        while ((len = input.read(buf)) > -1) output.write(buf, 0, len);
    }
}
