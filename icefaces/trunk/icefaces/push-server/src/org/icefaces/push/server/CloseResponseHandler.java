package org.icefaces.push.server;

import com.icesoft.faces.webapp.http.common.ResponseHandler;
import com.icesoft.faces.webapp.http.common.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CloseResponseHandler
implements ResponseHandler {
    private static final Log LOG = LogFactory.getLog(CloseResponseHandler.class);

    public void respond(final Response response)
    throws Exception {
        /*
         * let the bridge know that this blocking connection should
         * not be re-initialized...
         */
        // entity header fields
        response.setHeader("Content-Length", "0");
        // extension header fields
        response.setHeader("X-Connection", "close");
    }
}
