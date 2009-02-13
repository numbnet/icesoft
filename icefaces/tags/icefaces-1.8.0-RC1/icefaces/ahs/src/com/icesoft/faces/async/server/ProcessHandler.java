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
import com.icesoft.faces.async.common.SequenceNumbers;
import com.icesoft.faces.async.common.UpdatedViews;
import com.icesoft.faces.net.http.HttpRequest;
import com.icesoft.faces.net.http.HttpResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ProtocolException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 *   The <code>ProcessHandler</code> class represents a <code>Handler</code>
 *   that is responsible for processing either the HTTP Request message or the
 *   exception from the HTTP connection and add the resulting HTTP Response
 *   message to the HTTP connection's transaction.
 * </p>
 * <p>
 *   If the HTTP connection has an associated <code>ProtocolException</code>,
 *   the <code>ProcessHandler</code> will create a <code>400</code>
 *   (<code>Bad Request</code>) HTTP Response message and add it to the HTTP
 *   connection's transaction.
 * </p>
 * <p>
 *   If the HTTP connection has an associated <code>IOException</code>, the
 *   <code>ProcessHandler</code> will create a <code>500</code>
 *   (<code>Internal Server Error</code>) HTTP Response message and add it to
 *   the HTTP connection's transaction.
 * </p>
 * <p>
 *   If the HTTP connection's transaction has an HTTP Request message, but the
 *   HTTP Method is not a <code>GET</code>, the <code>ProcessHandler</code> will
 *   create a <code>501</code> (<code>Not Implemented</code>) HTTP Response
 *   message and add it to the HTTP connection's transaction.
 * </p>
 * <p>
 *   If the HTTP connection's transaction has an HTTP Request message, but the
 *   HTTP-Version is neither <code>HTTP/1.0</code> nor <code>HTTP/1.1</code>,
 *   the <code>ProcessHandler</code> will create a <code>505</code>
 *   (<code>HTTP Version Not Supported</code>) HTTP Response message and add it
 *   to the HTTP connection's transaction.
 * </p>
 * <p>
 *   If the HTTP connection's transaction has an HTTP Request message, but the
 *   Request-URI can not be found, the <code>ProcessHandler</code> will create a
 *   <code>404</code> (<code>Not Found</code>) HTTP Response message and add it
 *   to the HTTP connection's transaction.
 * </p>
 * <p>
 *   If the HTTP connection's transaction has an HTTP Request message, but the
 *   <code>Accept</code> values can not be met, the <code>ProcessHandler</code>
 *   will create a <code>406</code> (<code>Not Acceptable</code>) HTTP Response
 *   message and add it to the HTTP connection's transaction.
 * </p>
 * <p>
 *   If the HTTP connection's transaction has an HTTP Request message, but the
 *   containing ICEfacesID and ViewNumber can not be satisfied, the
 *   <code>ProcessHandler</code> pushes itself onto a queue and temporarily
 *   stops execution.
 * </p>
 * <p>
 *   Otherwise, the <code>ProcessHandler</code> will create a <code>200</code>
 *   (<code>OK</code>) HTTP Response message and add it to the HTTP connection's
 *   transaction.
 * </p>
 *
 * @see        ReadHandler
 * @see        WriteHandler
 */
public class ProcessHandler
extends AbstractHandler
implements Handler, Runnable {
    private static final int STATE_UNINITIALIZED = 0;
    private static final int STATE_PROCESSING_REQUEST = 1;
    private static final int STATE_WAITING_FOR_RESPONSE = 2;
    private static final int STATE_RESPONSE_IS_READY = 3;
    private static final int STATE_DONE = 4;

    private static final String ICEFACES_ID = "ice.session";

    private static final Log LOG = LogFactory.getLog(ProcessHandler.class);

    private int state = STATE_UNINITIALIZED;

    private Set iceFacesIdSet;
    private SequenceNumbers sequenceNumbers;
    private List updatedViewsList = new ArrayList();

    private boolean respondWithCloseResponse = false;

    /**
     * <p>
     *   Constructs a <code>ProcessHandler</code> object.
     * </p>
     */
    public ProcessHandler(
        final ExecuteQueue executeQueue, final AsyncHttpServer asyncHttpServer)
    throws IllegalArgumentException {
        super(executeQueue, asyncHttpServer);
    }

    public void run() {
        HttpResponse _httpResponse = null;
        try {
            switch (state) {
                case STATE_UNINITIALIZED :
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("State: Uninitialized");
                    }
                    state = STATE_PROCESSING_REQUEST;
                case STATE_PROCESSING_REQUEST :
                    if (httpConnection.hasThrowable()) {
                        _httpResponse = handleThrowable();
                        state = STATE_RESPONSE_IS_READY;
                        break;
                    }
                    if (httpConnection.hasException()) {
                        _httpResponse = handleException();
                        state = STATE_RESPONSE_IS_READY;
                        break;
                    }
                    HttpRequest _httpRequest =
                        httpConnection.getTransaction().getHttpRequest();
                    if (!_httpRequest.getMethod().
                            equalsIgnoreCase(HttpRequest.GET) &&
                        !_httpRequest.getMethod().
                            equalsIgnoreCase(HttpRequest.POST)) {

                        if (LOG.isDebugEnabled()) {
                            LOG.debug(
                                "501 Not Implemented (" +
                                    "Method: " + _httpRequest.getMethod() + ")");
                        }
                        _httpResponse = new NotImplementedResponse();
                        state = STATE_RESPONSE_IS_READY;
                        break;
                    }
                    String _httpVersion = _httpRequest.getHttpVersion();
                    if (!_httpVersion.equalsIgnoreCase(HttpRequest.HTTP_10) &&
                        !_httpVersion.equalsIgnoreCase(HttpRequest.HTTP_11)) {

                        if (LOG.isDebugEnabled()) {
                            LOG.debug(
                                "505 HTTP Version Not Supported (" +
                                    "HTTP-Version: " + _httpVersion + ")");
                        }
                        _httpResponse = new HttpVersionNotSupportedResponse();
                        state = STATE_RESPONSE_IS_READY;
                        break;
                    }
                    if (!acceptable()) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("406 Not Acceptable");
                        }
                        _httpResponse =
                            new HttpResponse(
                                HttpResponse.HTTP_11,
                                HttpResponse.NOT_ACCEPTABLE,
                                "Not Acceptable");
                        state = STATE_RESPONSE_IS_READY;
                        break;
                    }
                    String _requestUri = _httpRequest.getRequestUri();
                    String _path;
                    if (_httpRequest.getMethod().
                            equalsIgnoreCase(HttpRequest.GET)) {

                        int _index = _requestUri.indexOf("?");
                        if (_index != -1) {
                            _path = _requestUri.substring(0, _index);
                        } else {
                            _path = _requestUri;
                        }
                    } else if (
                        _httpRequest.getMethod().
                            equalsIgnoreCase(HttpRequest.POST)) {

                        _path = _requestUri;
                    } else {
                        _path = null;
                    }
                    if (
                        _path == null ||
                        (
                            !_path.endsWith("/block/receive-updated-views") &&
                            !_path.endsWith("/block/receive-updated-views/")
                        )) {

                        if (LOG.isDebugEnabled()) {
                            LOG.debug(
                                "404 Not Found (" +
                                    "Request-URI: " + _requestUri + ")");
                        }
                        _httpResponse =
                            new HttpResponse(
                                HttpResponse.HTTP_11,
                                HttpResponse.NOT_FOUND,
                                "Not Found");
                        state = STATE_RESPONSE_IS_READY;
                        break;
                    }
                    extractICEfacesIDs();
                    if (iceFacesIdSet.isEmpty()) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug(
                                "404 Not Found (" +
                                    "ICEfaces ID(s): " +
                                        iceFacesIdSet + ")");
                        }
                        _httpResponse =
                            new HttpResponse(
                                HttpResponse.HTTP_11,
                                HttpResponse.NOT_FOUND,
                                "Not Found");
                        state = STATE_RESPONSE_IS_READY;
                        break;
                    }
                    extractSequenceNumbers();
                    // checking pending request...
                    ProcessHandler _processHandler =
                        (ProcessHandler)
                            asyncHttpServer.getSessionManager().getRequestManager().
                                pull(iceFacesIdSet);
                    if (_processHandler != null) {
                        // respond to pending request.
                        if (LOG.isTraceEnabled()) {
                            LOG.trace("Respond to previous pending request");
                        }
                        _processHandler.setRespondWithCloseResponse(true);
                        _processHandler.handle();
                    }
                    state = STATE_WAITING_FOR_RESPONSE;
                case STATE_WAITING_FOR_RESPONSE :
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("State: Waiting for Response");
                    }
                    if (asyncHttpServer.getSessionManager().
                            isValid(iceFacesIdSet)) {

                        if (respondWithCloseResponse) {
                            _httpResponse =
                                new HttpResponse(
                                    HttpResponse.HTTP_11,
                                    HttpResponse.OK,
                                    "OK");
                            _httpResponse.putHeader(
                                HttpResponse.X_CONNECTION, "close");
                            state = STATE_RESPONSE_IS_READY;
                            break;
                        }
                        updatedViewsList =
                            asyncHttpServer.getSessionManager().
                                getUpdatedViewsManager().pull(
                                    iceFacesIdSet, sequenceNumbers);
                        if (updatedViewsList == null ||
                            updatedViewsList.isEmpty()) {

                            asyncHttpServer.getSessionManager().getRequestManager().
                                push(iceFacesIdSet, this);
                            return;
                        } else {
                            _httpResponse =
                                new HttpResponse(
                                    HttpResponse.HTTP_11,
                                    HttpResponse.OK,
                                    "OK");
                            state = STATE_RESPONSE_IS_READY;
                            break;
                        }
                    } else {
                        _httpResponse =
                            new HttpResponse(
                                HttpResponse.HTTP_11,
                                HttpResponse.OK,
                                "OK");
                        _httpResponse.putHeader(HttpResponse.X_CONNECTION, "close");
                        state = STATE_RESPONSE_IS_READY;
                        break;
                    }
                default :
                    // this should never happen!
            }
        } catch (Throwable throwable) {
            if (LOG.isErrorEnabled()) {
                LOG.error(
                    "Unexpected exception or error caught!", throwable);
            }
            _httpResponse = new InternalServerErrorResponse(throwable);
            httpConnection.requestClose();
            state = STATE_RESPONSE_IS_READY;
        }
        switch (state) {
            case STATE_RESPONSE_IS_READY :
                if (LOG.isTraceEnabled()) {
                    LOG.trace("State: Response is Ready");
                }
                httpConnection.getTransaction().setHttpResponse(_httpResponse);
                if (!_httpResponse.isServerError()) {
                    try {
                        addHeaderFields();
                        addEntityBody();
                    } catch (Throwable throwable) {
                        if (LOG.isErrorEnabled()) {
                            LOG.error(
                                "Unexpected exception or error caught!",
                                throwable);
                        }
                        _httpResponse =
                            new InternalServerErrorResponse(throwable);
                        httpConnection.getTransaction().
                            setHttpResponse(_httpResponse);
                        httpConnection.requestClose();
                    }
                }
                WriteHandler _writeHandler =
                    new WriteHandler(executeQueue, asyncHttpServer);
                _writeHandler.setHttpConnection(httpConnection);
                _writeHandler.handle();
                state = STATE_DONE;
            case STATE_DONE :
                if (LOG.isTraceEnabled()) {
                    LOG.trace("State: Done");
                }
                break;
            default :
                // this should never happen!
        }
    }

    private boolean acceptable() {
        HttpRequest _httpRequest =
            httpConnection.getTransaction().getHttpRequest();
        boolean _acceptable = false;
        if (_httpRequest.getHttpVersion().
                equalsIgnoreCase(HttpRequest.HTTP_11)) {

            if (_httpRequest.containsHeader(HttpRequest.ACCEPT)) {
                String[] _acceptFieldValues =
                    _httpRequest.getFieldValues(HttpRequest.ACCEPT);
                for (
                    int i = 0;
                    !_acceptable && i < _acceptFieldValues.length; i++) {

                    StringTokenizer _mediaTypes =
                        new StringTokenizer(_acceptFieldValues[i], ",");
                    while (!_acceptable && _mediaTypes.hasMoreTokens()) {
                        String _mediaType = _mediaTypes.nextToken().trim();
                        int _index = _mediaType.indexOf(";");
                        if (_index != -1) {
                            _mediaType = _mediaType.substring(0, _index);
                        }
                        if (_mediaType.equals("*/*") ||
                            _mediaType.equalsIgnoreCase("text/*") ||
                            _mediaType.equalsIgnoreCase("text/xml")) {

                            _acceptable = true;
                        }
                    }
                }
            } else {
                _acceptable = true;
            }
        } else {
            _acceptable = true;
        }
        return _acceptable;
    }

    private void addEntityBody() {
        HttpResponse _httpResponse =
            httpConnection.getTransaction().getHttpResponse();
        if (_httpResponse.isSuccessful()) {
            byte[] _entityBody = getEntityBody();
            if (_entityBody.length != 0 &&
                !_httpResponse.containsHeader(HttpResponse.X_CONNECTION)) {

                if (_httpResponse.containsHeader(
                        HttpResponse.EntityBody.CONTENT_ENCODING)) {

                    String _contentEncoding =
                        _httpResponse.getFieldValues(
                            HttpResponse.EntityBody.CONTENT_ENCODING)[0];
                    if (_contentEncoding.equalsIgnoreCase("gzip")) {
                        ByteArrayOutputStream _byteArrayOutputStream =
                            new ByteArrayOutputStream();
                        try {
                            GZIPOutputStream _gzipOutputStream =
                                new GZIPOutputStream(_byteArrayOutputStream);
                            _gzipOutputStream.write(_entityBody);
                            _gzipOutputStream.finish();
                            byte[] _bytes =
                                _byteArrayOutputStream.toByteArray();
                            _httpResponse.setEntityBody(
                                new HttpResponse.EntityBody(_bytes));
                            _httpResponse.putHeader(
                                HttpResponse.EntityBody.CONTENT_LENGTH,
                                _bytes.length,
                                true);
                            _gzipOutputStream.close();
                        } catch (IOException exception) {
                            if (LOG.isErrorEnabled()) {
                                LOG.error(
                                    "An I/O error occurred while " +
                                        "applying gzip Content-Encoding!",
                                    exception);
                            }
                        }
                    } else if (_contentEncoding.equalsIgnoreCase("deflate")) {
                        ByteArrayOutputStream _byteArrayOutputStream =
                            new ByteArrayOutputStream();
                        try {
                            DeflaterOutputStream _deflaterOutputStream =
                                new DeflaterOutputStream(
                                    _byteArrayOutputStream);
                            _deflaterOutputStream.write(_entityBody);
                            _deflaterOutputStream.finish();
                            byte[] _bytes =
                                _byteArrayOutputStream.toByteArray();
                            _httpResponse.setEntityBody(
                                new HttpResponse.EntityBody(_bytes));
                            _httpResponse.putHeader(
                                HttpResponse.EntityBody.CONTENT_LENGTH,
                                _bytes.length,
                                true);
                            _deflaterOutputStream.close();
                        } catch (IOException exception) {
                            if (LOG.isErrorEnabled()) {
                                LOG.error(
                                    "An I/O error occurred while " +
                                        "applying deflate Content-Encoding!",
                                    exception);
                            }
                        }
                    }
                } else {
                    _httpResponse.setEntityBody(
                        new HttpResponse.EntityBody(_entityBody));
                }
            }
        }
    }

    private void addEntityHeaderFields() {
        HttpRequest _httpRequest =
            httpConnection.getTransaction().getHttpRequest();
        HttpResponse _httpResponse =
            httpConnection.getTransaction().getHttpResponse();
        if (_httpResponse.isSuccessful()) {
            byte[] _entityBody = getEntityBody();
            if (_entityBody.length != 0 &&
                !_httpResponse.containsHeader(HttpResponse.X_CONNECTION)) {

                if (asyncHttpServer.useCompression()) {
                    if (_httpRequest.containsHeader(
                            HttpRequest.ACCEPT_ENCODING)) {

                        String[] _acceptEncodingFieldValues =
                            _httpRequest.getFieldValues(
                                HttpRequest.ACCEPT_ENCODING);
                        boolean _gzip = false;
                        boolean _deflate = false;
                        boolean _identity = false;
                        for (int i = 0;
                             i < _acceptEncodingFieldValues.length; i++) {

                            StringTokenizer _encodings =
                                new StringTokenizer(
                                    _acceptEncodingFieldValues[i], ",");
                            while (_encodings.hasMoreTokens()) {
                                String _encoding =
                                    _encodings.nextToken().trim();
                                if (!_gzip &&
                                    (
                                        _encoding.equalsIgnoreCase("gzip") ||
                                        _encoding.equalsIgnoreCase("*")
                                    )) {

                                    _gzip = true;
                                } else if (
                                    !_deflate &&
                                    _encoding.equalsIgnoreCase("deflate")) {

                                    _deflate = true;
                                } else if (
                                    !_identity &&
                                    _encoding.equalsIgnoreCase("identity")) {

                                    _identity = true;
                                }
                            }
                        }
                        if (_gzip) {
                            _httpResponse.putHeader(
                                HttpResponse.EntityBody.CONTENT_ENCODING,
                                "gzip",
                                true);
                        } else if (_deflate) {
                            _httpResponse.putHeader(
                                HttpResponse.EntityBody.CONTENT_ENCODING,
                                "deflate",
                                true);
                        }
                    }
                }
                _httpResponse.putHeader(
                    HttpResponse.EntityBody.CONTENT_LENGTH,
                    _entityBody.length,
                    true);
                _httpResponse.putHeader(
                    HttpResponse.EntityBody.CONTENT_TYPE,
                    "text/xml",
                    true);
            } else {
                _httpResponse.putHeader(
                    HttpResponse.EntityBody.CONTENT_LENGTH, 0, true);
            }
            _httpResponse.putHeader(
                HttpResponse.EntityBody.EXPIRES,
                _httpResponse.getFieldValues(HttpResponse.DATE)[0],
                true);
        } else if (_httpResponse.isClientError()) {
            /*
             * Note: Firefox and Safari both seem to need the Content-Length
             *       header eventhough there is no Entity-Body and
             *       "Connection: close" is specified.
             */
            _httpResponse.putHeader(
                HttpResponse.EntityBody.CONTENT_LENGTH, 0, true);
        }
    }

    private void addExtensionHeaderFields() {
//        HttpRequest _httpRequest =
//            httpConnection.getTransaction().getHttpRequest();
        HttpResponse _httpResponse =
            httpConnection.getTransaction().getHttpResponse();
        if (_httpResponse.isSuccessful()) {
            if (!_httpResponse.containsHeader(HttpResponse.X_CONNECTION)) {
                _httpResponse.putHeader(
                    HttpResponse.X_SET_WINDOW_COOKIE,
                    "Sequence_Numbers=\"" + getSequenceNumberValue() + "\"",
                    true);
            }
        }
//        if (LOG.isTraceEnabled()) {
//            try {
//                _httpResponse.putHeader(
//                    "X-Source-Node-Address",
//                    InetAddress.getLocalHost().getHostAddress());
//            } catch (UnknownHostException exception) {
//                // do nothing.
//            }
//        }
    }

    private void addGeneralHeaderFields() {
        HttpRequest _httpRequest =
            httpConnection.getTransaction().getHttpRequest();
        HttpResponse _httpResponse =
            httpConnection.getTransaction().getHttpResponse();
        if (_httpResponse.isSuccessful()) {
            // getting the User-Agent's HTTP-Version...
            String _userAgentHttpVersion = _httpRequest.getHttpVersion();
            // handling caching...
            if (_userAgentHttpVersion.equals(HttpRequest.HTTP_10)) {
                // Pragma header
                _httpResponse.putHeader(HttpResponse.PRAGMA, "no-cache", true);
            } else if (_userAgentHttpVersion.equals(HttpRequest.HTTP_11)) {
                // Cache-Control header
                _httpResponse.putHeader(
                    HttpResponse.CACHE_CONTROL, "no-cache, no-store", true);
            } else {
                if (LOG.isErrorEnabled()) {
                    LOG.error("Unknown HTTP-Version: " + _userAgentHttpVersion);
                }
            }
            // handling persistency...
            if (asyncHttpServer.isPersistent() &&
                !_httpResponse.containsHeader(HttpResponse.X_CONNECTION)) {

                if (_userAgentHttpVersion.equals(HttpRequest.HTTP_10)) {
                    String[] _connectionFieldValues =
                        _httpRequest.getFieldValues(HttpRequest.CONNECTION);
                    if (_connectionFieldValues.length != 0 &&
                        _connectionFieldValues[0].
                            equalsIgnoreCase("keep-alive")) {

                        String[] _keepAliveFieldValues =
                            _httpRequest.getFieldValues(HttpRequest.KEEP_ALIVE);
                        if (_keepAliveFieldValues.length != 0) {

                        }
                        _httpResponse.putHeader(
                            HttpResponse.CONNECTION, "keep-alive", true);
                    } else {
                        httpConnection.requestClose();
                    }
                } else if (_userAgentHttpVersion.equals(HttpRequest.HTTP_11)) {
                    String[] _connectionFieldValues =
                        _httpRequest.getFieldValues(HttpRequest.CONNECTION);
                    if (_connectionFieldValues.length != 0 &&
                        _connectionFieldValues[0].equalsIgnoreCase("close")) {

                        _httpResponse.putHeader(
                            HttpResponse.CONNECTION, "close", true);
                        httpConnection.requestClose();
                    }
                } else {
                    if (LOG.isErrorEnabled()) {
                        LOG.error(
                            "Unknown HTTP-Version: " + _userAgentHttpVersion);
                    }
                }
            } else {
                if (_userAgentHttpVersion.equals(HttpRequest.HTTP_10)) {
                    httpConnection.requestClose();
                } else if (_userAgentHttpVersion.equals(HttpRequest.HTTP_11)) {
                    _httpResponse.putHeader(
                        HttpResponse.CONNECTION, "close", true);
                    httpConnection.requestClose();
                } else {
                    if (LOG.isErrorEnabled()) {
                        LOG.error(
                            "Unknown HTTP-Version: " + _userAgentHttpVersion);
                    }
                }
            }
        } else if (_httpResponse.isClientError()) {
            _httpResponse.putHeader(HttpResponse.CONNECTION, "close", true);
            if (_httpRequest.containsHeader(HttpResponse.PROXY_CONNECTION)) {
                _httpResponse.putHeader(
                    HttpResponse.PROXY_CONNECTION, "close", true);
            }
            httpConnection.requestClose();
        }
        // adding the following response header fields to all responses...
        _httpResponse.putHeader(
            HttpResponse.DATE,
            HttpDate.RFC_1123_DATE_FORMAT.format(new Date()),
            true);
    }

    private void addHeaderFields() {
        addResponseHeaderFields();
        addGeneralHeaderFields();
        addEntityHeaderFields();
        addExtensionHeaderFields();
    }

    private void addResponseHeaderFields() {
        HttpResponse _httpResponse =
            httpConnection.getTransaction().getHttpResponse();
        // adding the following response header fields to all responses...
        _httpResponse.putHeader(
            HttpResponse.SERVER, AsyncHttpServer.NAME, true);
    }

    private void extractICEfacesIDs() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Extracting ICEfaces ID(s)...");
        }
        iceFacesIdSet = new HashSet();
        HttpRequest _httpRequest =
            httpConnection.getTransaction().getHttpRequest();
        if (_httpRequest.getMethod().equalsIgnoreCase(HttpRequest.GET)) {
            String[] _iceFacesIds =
                httpConnection.getTransaction().getHttpRequest().
                    getParameters(ICEFACES_ID);
            for (int i = 0; i < _iceFacesIds.length; i++) {
                if (asyncHttpServer.getSessionManager().
                        isValid(_iceFacesIds[i])) {

                    iceFacesIdSet.add(_iceFacesIds[i]);
                } else {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(
                            "Invalid ICEfaces ID: " + _iceFacesIds[i] + ")");
                    }
                }
            }
        } else if (
            _httpRequest.getMethod().equalsIgnoreCase(HttpRequest.POST)) {

            StringTokenizer _tokens;
            try {
                _tokens =
                    new StringTokenizer(
                        URLDecoder.decode(
                            new String(
                                _httpRequest.getEntityBody().getBytes(),
                                "UTF-8"),
                            "UTF-8"),
                        "&");
            } catch (UnsupportedEncodingException exception) {
                _tokens =
                    new StringTokenizer(
                        new String(_httpRequest.getEntityBody().getBytes()),
                        "&");
            }
            int _tokenCount = _tokens.countTokens();
            for (int i = 0; i < _tokenCount; i++) {
                String _token = _tokens.nextToken();
                if (_token.startsWith(ICEFACES_ID)) {
                    String _iceFacesId =
                        _token.substring(_token.indexOf("=") + 1);
                    if (asyncHttpServer.getSessionManager().
                            isValid(_iceFacesId)) {

                        iceFacesIdSet.add(_iceFacesId);
                    } else {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug(
                                "Invalid ICEfaces ID: " + _iceFacesId + ")");
                        }
                    }
                }
            }
        }
        httpConnection.getTransaction().getHttpRequest().
            setICEfacesIDSet(iceFacesIdSet);
        if (LOG.isDebugEnabled()) {
            LOG.debug("ICEfaces ID(s): " + iceFacesIdSet);
        }
    }

    private void extractSequenceNumbers() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Extracting Sequence Number(s)...");
        }
        sequenceNumbers =
            new SequenceNumbers(
                httpConnection.getTransaction().getHttpRequest().
                    getFieldValues(HttpRequest.X_WINDOW_COOKIE));
        if (LOG.isDebugEnabled()) {
            LOG.debug("Sequence Number(s): " + sequenceNumbers);
        }
    }

    private byte[] getEntityBody() {
        StringBuffer _entityBody = new StringBuffer();
        _entityBody.append("<updated-views>");
        for (int i = 0, _size = updatedViewsList.size(); i < _size; i++) {
            UpdatedViews _updatedViews = (UpdatedViews)updatedViewsList.get(i);
            Set _updatedViewsSet = _updatedViews.getUpdatedViewsSet();
            Iterator _updatedViewsIterator = _updatedViewsSet.iterator();
            for (int j = 0, _jMax = _updatedViewsSet.size() ; j < _jMax; j++) {
                if (j != 0) {
                    _entityBody.append(" ");
                }
                _entityBody.
                    append(_updatedViews.getICEfacesID()).append(":").
                    append(_updatedViewsIterator.next());
            }
        }
        _entityBody.append("</updated-views>\r\n\r\n");
        try {
            return _entityBody.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException exception) {
            return _entityBody.toString().getBytes();
        }
    }

    private String getSequenceNumberValue() {
        StringBuffer _sequenceNumbers = new StringBuffer();
        for (int i = 0, _size = updatedViewsList.size(); i < _size; i++) {
            UpdatedViews _updatedViews = (UpdatedViews)updatedViewsList.get(i);
            if (i != 0) {
                _sequenceNumbers.append(",");
            }
            _sequenceNumbers.
                append(_updatedViews.getICEfacesID()).append(":").
                append(_updatedViews.getSequenceNumber());
        }
        return _sequenceNumbers.toString();
    }

    private HttpResponse handleException() {
        HttpResponse _httpResponse;
        Exception _exception = httpConnection.getException();
        if (_exception instanceof ProtocolException) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("400 Bad Request (ProtocolException)");
            }
            _httpResponse =
                new HttpResponse(
                    HttpResponse.HTTP_11,
                    HttpResponse.BAD_REQUEST,
                    "Bad Request");
        } else if (_exception instanceof IOException) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(
                    "500 Internal Server Error (IOException)", _exception);
            }
            _httpResponse = new InternalServerErrorResponse(_exception);
            httpConnection.requestClose();
        } else {
            if (LOG.isErrorEnabled()) {
                LOG.error("Unhandled exception: " + _exception, _exception);
            }
            _httpResponse = new InternalServerErrorResponse(_exception);
            httpConnection.requestClose();
        }
        return _httpResponse;
    }

    private HttpResponse handleThrowable() {
        HttpResponse _httpResponse =
            new InternalServerErrorResponse(httpConnection.getThrowable());
        httpConnection.requestClose();
        return _httpResponse;
    }

    private void setRespondWithCloseResponse(boolean respondWithCloseResponse) {
        this.respondWithCloseResponse = respondWithCloseResponse;
    }
}