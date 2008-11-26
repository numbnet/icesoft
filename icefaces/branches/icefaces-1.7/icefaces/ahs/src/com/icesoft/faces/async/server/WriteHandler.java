/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
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
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 */
package com.icesoft.faces.async.server;

import com.icesoft.faces.async.common.ExecuteQueue;
import com.icesoft.faces.net.http.HttpResponse;
import com.icesoft.faces.net.http.HttpRequest;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 *   The <code>WriteHandler</code> class represents a <code>Handler</code> that
 *   is responsible for writing the HTTP Response message contained in the HTTP
 *   connection's transaction to the HTTP connection.
 * </p>
 * <p>
 *   If there is not HTTP Response message contained in the HTTP connection's
 *   transaction nothing is written!
 * </p>
 *
 * @see        ProcessHandler
 */
public class WriteHandler
extends AbstractHandler
implements Handler, Runnable {
    private static final Log LOG = LogFactory.getLog(WriteHandler.class);

    /**
     * <p>
     *   Constructs a <code>WriteHandler</code> object.
     * </p>
     */
    public WriteHandler(
        final ExecuteQueue executeQueue, final AsyncHttpServer asyncHttpServer)
    throws IllegalArgumentException {
        super(executeQueue, asyncHttpServer);
    }

    public void run() {
        if (httpConnection.getTransaction().hasHttpResponse()) {
            HttpResponse _httpResponse =
                httpConnection.getTransaction().getHttpResponse();
            try {
                httpConnection.write(_httpResponse.getBytes());
                if (LOG.isDebugEnabled()) {
                    HttpRequest _httpRequest =
                        httpConnection.getTransaction().getHttpRequest();
                    LOG.debug(
                        "HTTP Response send to " +
                            httpConnection.getRemoteSocketAddress() + ":\r\n" +
                            "[" +
                                (_httpRequest != null ?
                                    _httpRequest.getRequestLine() : null) +
                            "]\r\n\r\n" +
                            _httpResponse.getMessage(false));
                }
            } catch (IOException exception) {
                if (LOG.isErrorEnabled()) {
                    LOG.error(
                        "An I/O error occurred while writing HTTP Response!",
                        exception);
                }
                /*
                 * An IOException occurred while writing the HTTP Response.
                 * Close the connection from the server end, as the User-Agent
                 * cannot be informed of the error at this point.
                 */
                httpConnection.requestClose();
            } catch (Throwable throwable) {
                if (LOG.isErrorEnabled()) {
                    LOG.error(
                        "Unexpected exception or error caught!",
                        throwable);
                }
                /*
                 * An Exception or Error occurred while writing the HTTP
                 * Response. Close the connection from the server end, as the
                 * User-Agent cannot be informed of the error at this point.
                 */
                httpConnection.requestClose();
            }
            httpConnection.reset();
        }
    }
}
