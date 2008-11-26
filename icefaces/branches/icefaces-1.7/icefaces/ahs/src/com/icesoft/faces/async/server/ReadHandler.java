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
import com.icesoft.faces.net.http.HttpParser;
import com.icesoft.faces.net.http.HttpRequest;

import com.icesoft.net.http.HttpMessage;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.ProtocolException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 *   The <code>ReadHandler</code> class represents a <code>Handler</code> that
 *   is responsible for reading the HTTP Request message from the HTTP
 *   connection and add it to the HTTP connection's transaction.
 * </p>
 * <p>
 *   If the HTTP connection is broken, the <code>ReadHandler</code> will request
 *   a close on the HTTP connection and reset it, ultimately resulting in
 *   invalidating the HTTP connection.
 * </p>
 * <p>
 *   If parsing the Request-Line and Headers of the incoming HTTP Request
 *   message causes a <code>ProtocolException</code>, the
 *   <code>ReadHandler</code> will set the exception on the HTTP connection,
 *   ultimately resulting in sending back a <code>400</code>
 *   (<code>Bad Request</code>) HTTP Response message to the User-Agent.
 * </p>
 * <p>
 *   If an I/O error occurs during the reading of the incoming HTTP Request
 *   message causing an <code>IOException</code>, the <code>ReadHandler</code>
 *   will set the exception on the HTTP connection, ultimately resulting in
 *   sending back a <code>500</code> (<code>Internal Server Error</code>) HTTP
 *   Response message to the User-Agent.
 * </p>
 *
 * @see        ProcessHandler
 */
public class ReadHandler
extends AbstractHandler
implements Handler, Runnable {
    private static final HttpParser HTTP_PARSER = new HttpParser();

    private static final int STATE_UNINITIALIZED = 0;
    private static final int STATE_REQUEST_URI   = 1;
    private static final int STATE_HEADERS       = 2;
    private static final int STATE_ENTITY_BODY   = 3;
    private static final int STATE_COMPLETED     = 4;

    private static final int EOS = -1;

    private static final byte CR = '\r';
    private static final byte LF = '\n';

    private static final Log LOG = LogFactory.getLog(ReadHandler.class);

    private HttpRequest httpRequest;
    private int state = STATE_UNINITIALIZED;

    private InputStream inputStream;
    private byte[] bytes = new byte[4096];
    private int beginIndex = 0;
    private int endIndex = 0;
    private int length;
    private StringBuffer buffer = new StringBuffer();
    private ByteArrayOutputStream entityBodyBuffer =
        new ByteArrayOutputStream();

    /**
     * <p>
     *   Constructs a <code>ReadHandler</code> object.
     * </p>
     */
    public ReadHandler(
        final ExecuteQueue executeQueue, final AsyncHttpServer asyncHttpServer)
    throws IllegalArgumentException {
        super(executeQueue, asyncHttpServer);
    }

    public void run() {
        try {
            inputStream = httpConnection.getInputStream();
            switch (state) {
                case STATE_UNINITIALIZED :
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("State: Uninitialized");
                    }
                    state = STATE_REQUEST_URI;
                case STATE_REQUEST_URI :
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("State: Request-URI");
                    }
                    while (state == STATE_REQUEST_URI) {
                        length = inputStream.read(bytes);
                        if (length == -1) {
                            // User-Agent closed the connection?!
                            // (Linux; Windows seems to throw a SocketException)
                            httpConnection.requestClose();
                            if (httpConnection.getTransaction().
                                    hasHttpRequest()) {
                                asyncHttpServer.cancelRequest(
                                    httpConnection.getTransaction().
                                        getHttpRequest().getICEfacesIDSet());
                            }
                            httpConnection.reset();
                            if (LOG.isTraceEnabled()) {
                                LOG.trace(
                                    "Close: User-Agent closed the connection!");
                            }
                            return;
                        } else if (length == 0) {
                            park();
                            return;
                        }
                        for (; endIndex < length; endIndex++) {
                            if (bytes[endIndex] == CR &&
                                endIndex + 1 < length &&
                                bytes[endIndex + 1] == LF) {

                                String _requestUri =
                                    new String(
                                        bytes,
                                        beginIndex,
                                        endIndex - beginIndex,
                                        "UTF-8");
                                if (LOG.isTraceEnabled()) {
                                    LOG.trace(
                                        "Request-URI: [" + _requestUri + "]");
                                }
                                httpRequest =
                                    (HttpRequest)
                                        HTTP_PARSER.parseRequestLine(
                                            _requestUri);
                                state = STATE_HEADERS;
                                if (endIndex + 2 < length) {
                                    beginIndex = endIndex += 2;
                                } else if (!read()) {
                                    park();
                                    return;
                                }
                                if (buffer.length() != 0) {
                                    buffer.delete(0, buffer.length() - 1);
                                }
                                break;
                            }
                            if (state == STATE_REQUEST_URI) {
                                buffer.append(
                                    new String(
                                        bytes,
                                        beginIndex,
                                        endIndex - beginIndex,
                                        "UTF-8"));
                            }
                        }
                    }
                case STATE_HEADERS :
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("State: Headers");
                    }
                    while (state == STATE_HEADERS) {
                        while (endIndex < length) {
                            if (bytes[endIndex] == CR &&
                                endIndex + 1 < length &&
                                bytes[endIndex + 1] == LF) {

                                if (endIndex == beginIndex) {
                                    String _method = httpRequest.getMethod();
                                    if (_method.equalsIgnoreCase(
                                            HttpRequest.GET)) {

                                        state = STATE_COMPLETED;
                                    } else if (
                                        _method.equalsIgnoreCase(
                                            HttpRequest.POST)) {

                                        state = STATE_ENTITY_BODY;
                                        if (endIndex + 2 < length) {
                                            beginIndex = endIndex += 2;
                                        } else if (!read()) {
                                            park();
                                            return;
                                        }
                                    }
                                    break;
                                }
                                String _header =
                                    new String(
                                        bytes,
                                        beginIndex,
                                        endIndex - beginIndex,
                                        "UTF-8");
                                if (LOG.isTraceEnabled()) {
                                    LOG.trace("Header: [" + _header + "]");
                                }
                                HTTP_PARSER.parseHeader(_header, httpRequest);
                                if (endIndex + 2 < length) {
                                    beginIndex = endIndex += 2;
                                } else if (!read()) {
                                    park();
                                    return;
                                }
                                if (buffer.length() != 0) {
                                    buffer.delete(0, buffer.length() - 1);
                                }
                            } else {
                                endIndex++;
                            }
                        }
                        if (state == STATE_HEADERS) {
                            buffer.append(
                                new String(
                                    bytes,
                                    beginIndex,
                                    endIndex - beginIndex,
                                    "UTF-8"));
                            if (!read()) {
                                park();
                                return;
                            }
                        }
                    }
                case STATE_ENTITY_BODY :
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("State: Entity-Body");
                    }
                    while (state == STATE_ENTITY_BODY) {
                        if (beginIndex < length) {
                            entityBodyBuffer.write(
                                bytes, beginIndex, length - beginIndex);
                            if (entityBodyBuffer.size() ==
                                    httpRequest.getFieldValueAsInt(
                                        HttpRequest.EntityBody.
                                            CONTENT_LENGTH)) {

                                byte[] _entityBody =
                                    entityBodyBuffer.toByteArray();
                                if (LOG.isTraceEnabled()) {
                                    LOG.trace(
                                        "Entity-Body: " +
                                            "[" +
                                                new String(
                                                    _entityBody, "UTF-8") +
                                            "]");
                                }
                                httpRequest.setEntityBody(
                                    new HttpMessage.EntityBody(_entityBody));
                                state = STATE_COMPLETED;
                                break;
                            }
                        }
                        if (state == STATE_ENTITY_BODY && !read()) {
                            park();
                            return;
                        }
                    }
                case STATE_COMPLETED :
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("State: Completed");
                    }
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(
                            "HTTP Request received from " +
                                httpConnection.getRemoteSocketAddress() +
                                    ":\r\n\r\n" +
                                        httpRequest.getMessage(true));
                    }
                    httpConnection.getTransaction().setHttpRequest(httpRequest);
                    break;
                default :
                    // This should never happen!
            }
        } catch (ProtocolException exception) {
            if (LOG.isErrorEnabled()) {
                LOG.error(
                    "An error occurred while " +
                        "parsing the incoming HTTP request!",
                    exception);
            }
            httpConnection.setException(exception);
        } catch (IOException exception) {
            if (LOG.isErrorEnabled()) {
                LOG.error(
                    "An I/O error occurred while " +
                        "reading the incoming HTTP request!",
                    exception);
            }
            httpConnection.setException(exception);
        } catch (Throwable throwable) {
            if (LOG.isErrorEnabled()) {
                LOG.error(
                    "Unexpected exception or error caught!", throwable);
            }
            httpConnection.setThrowable(throwable);
        } finally {
            if (!httpConnection.isCloseRequested() && inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException exception) {
                    if (LOG.isErrorEnabled()) {
                        LOG.error(
                            "An I/O error occurred while " +
                                "closing the input stream",
                            exception);
                    }
                } catch (Throwable throwable) {
                    if (LOG.isErrorEnabled()) {
                        LOG.error(throwable);
                    }
                }                      
            }
        }
        ProcessHandler _processHandler =
            new ProcessHandler(executeQueue, asyncHttpServer);
        _processHandler.setHttpConnection(httpConnection);
        _processHandler.handle();
        asyncHttpServer.getHttpConnectionAcceptor(asyncHttpServer.getPort()).
            doneReading(httpConnection);
    }

    private void park() {
        asyncHttpServer.getHttpConnectionAcceptor(asyncHttpServer.getPort()).
            setPendingReadHandler(httpConnection, this);
    }

    private boolean read()
    throws IOException, ProtocolException {
        beginIndex = endIndex = 0;
        switch (length = inputStream.read(bytes)) {
            case 0 :
                return false;
            case EOS :
                throw
                    new ProtocolException(
                        "Unexpected end of stream!");
            default :
                return true;
        }
    }
}
