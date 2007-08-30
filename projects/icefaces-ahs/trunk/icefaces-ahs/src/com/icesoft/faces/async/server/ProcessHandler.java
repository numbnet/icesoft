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

import com.icesoft.faces.util.net.http.HttpRequest;
import com.icesoft.faces.util.net.http.HttpResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.ProtocolException;
import java.net.UnknownHostException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Iterator;
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
    private static final String ICEFACES_ID = "ice.session";
    private static final String UPDATED_VIEWS_START_TAG = "<updated-views>";
    private static final String UPDATED_VIEWS_END_TAG = "</updated-views>";

    private static final Log LOG = LogFactory.getLog(ProcessHandler.class);

    private String iceFacesId;
    private long sequenceNumber = -1;
    private UpdatedViews updatedViews;

    static {
        if (LOG.isDebugEnabled()) {
            LOG.warn(
                "Log level DEBUG is enabled. " +
                "This will expose each node's address into the HTTP Responses!");
        }
    }

    /**
     * <p>
     *   Constructs a <code>ProcessHandler</code> object with the specified
     *   <code>httpConnection</code>.
     * </p>
     *
     * @param      httpConnection
     *                 the HTTP connection that is to be handled by the
     *                 <code>ProcessHandler</code> to be created.
     */
    public ProcessHandler(final HttpConnection httpConnection) {
        super(httpConnection);
    }

    public void reset() {
        iceFacesId = null;
        sequenceNumber = -1;
        updatedViews = null;
        super.reset();
    }

    public void run() {
        HttpResponse _httpResponse;
        if (httpConnection.hasException()) {
            _httpResponse = handleException();
        } else {
            HttpRequest _httpRequest =
                httpConnection.getTransaction().getHttpRequest();
            if (!_httpRequest.getMethod().equalsIgnoreCase(HttpRequest.GET) &&
                !_httpRequest.getMethod().equalsIgnoreCase(HttpRequest.POST)) {

                if (LOG.isDebugEnabled()) {
                    LOG.debug(
                        "501 Not Implemented (" +
                            "Method: " + _httpRequest.getMethod() + ")");
                }
                _httpResponse =
                    new HttpResponse(
                        HttpResponse.HTTP_11,
                        HttpResponse.NOT_IMPLEMENTED,
                        "Not Implemented");
            } else {
                String _httpVersion = _httpRequest.getHttpVersion();
                if (!_httpVersion.equalsIgnoreCase(HttpRequest.HTTP_10) &&
                    !_httpVersion.equalsIgnoreCase(HttpRequest.HTTP_11)) {

                    if (LOG.isDebugEnabled()) {
                        LOG.debug(
                            "505 HTTP Version Not Supported (" +
                                "HTTP-Version: " + _httpVersion + ")");
                    }
                    _httpResponse =
                        new HttpResponse(
                            HttpResponse.HTTP_11,
                            HttpResponse.HTTP_VERSION_NOT_SUPPORTED,
                            "HTTP Version Not Supported");
                } else if (!acceptable()) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("406 Not Acceptable");
                    }
                    _httpResponse =
                        new HttpResponse(
                            HttpResponse.HTTP_11,
                            HttpResponse.NOT_ACCEPTABLE,
                            "Not Acceptable");
                } else {
                    String _requestUri = _httpRequest.getRequestUri();
                    String _path;
                    if (_httpRequest.getMethod().equalsIgnoreCase(
                            HttpRequest.GET)) {

                        int _index = _requestUri.indexOf("?");
                        if (_index != -1) {
                            _path = _requestUri.substring(0, _index);
                        } else {
                            _path = _requestUri;
                        }
                    } else if (
                        _httpRequest.getMethod().equalsIgnoreCase(
                            HttpRequest.POST)) {

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
                    } else {
                        if (iceFacesId == null) {
                            extract();
                        }
                        if (!asyncHttpServer.isValid(iceFacesId)) {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug(
                                    "404 Not Found (" +
                                        "ICEfaces ID: " + iceFacesId + ")");
                            }
                            _httpResponse =
                                new HttpResponse(
                                    HttpResponse.HTTP_11,
                                    HttpResponse.NOT_FOUND,
                                    "Not Found");
                        } else {
                            if (sequenceNumber == -1) {
                                extractSequenceNumber();
                            }
                            updatedViews =
                                asyncHttpServer.pullPendingUpdatedViews(
                                    iceFacesId, sequenceNumber);
                            if (updatedViews != null) {
                                _httpResponse =
                                    new HttpResponse(
                                        HttpResponse.HTTP_11,
                                        HttpResponse.OK,
                                        "OK");
                            } else {
                                asyncHttpServer.pushPendingRequest(
                                    iceFacesId, this);
                                return;
                            }
                        }
                    }
                }
            }
        }
        httpConnection.getTransaction().setHttpResponse(_httpResponse);
        addHeaderFields();
        addEntityBody();
        handlerPool.getWriteHandler(httpConnection).handle();
        handlerPool.returnProcessHandler(this);
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
            if (_entityBody.length != 0) {
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
                                        "applying compress Content-Encoding!",
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
            if (_entityBody.length != 0) {
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
        } else if (_httpResponse.isServerError()) {
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
            _httpResponse.putHeader(
                HttpResponse.X_SET_WINDOW_COOKIE,
                "Sequence_Number=\"" + getSequenceNumberValue() + "\"",
                true);
        }
        if (LOG.isDebugEnabled()) {
            try {
                _httpResponse.putHeader(
                    "X-Source-Node-Address",
                    InetAddress.getLocalHost().getHostAddress());
            } catch (UnknownHostException exception) {
                // do nothing.
            }
        }
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
            if (asyncHttpServer.isPersistent()) {
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
            if (_httpResponse.containsHeader(HttpResponse.PROXY_CONNECTION)) {
                _httpResponse.putHeader(
                    HttpResponse.PROXY_CONNECTION, "close", true);
            }
            httpConnection.requestClose();
        } else if (_httpResponse.isServerError()) {
            _httpResponse.putHeader(HttpResponse.CONNECTION, "close", true);
            if (_httpResponse.containsHeader(HttpResponse.PROXY_CONNECTION)) {
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

    private void extract() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Extracting...");
        }
        final HttpRequest _httpRequest =
            httpConnection.getTransaction().getHttpRequest();
        if (_httpRequest.getMethod().equalsIgnoreCase(HttpRequest.GET)) {
            extractICEfacesID(
                httpConnection.getTransaction().getHttpRequest().
                    getParameter(ICEFACES_ID));
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
            final int _tokenCount = _tokens.countTokens();
            for (int i = 0; i < _tokenCount; i++) {
                final String _token = _tokens.nextToken();
                if (_token.startsWith(ICEFACES_ID)) {
                    extractICEfacesID(
                        _token.substring(_token.indexOf("=") + 1));
                }
            }
        }
    }

    private void extractICEfacesID(final String value) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Extracting ICEfaces ID...");
        }
        final StringTokenizer _iceFacesIds = new StringTokenizer(value, ",");
        final int _tokenCount = _iceFacesIds.countTokens();
        for (int i = 0; i < _tokenCount; i++) {
            final String _iceFacesId = _iceFacesIds.nextToken();
            if (_iceFacesId.trim().length() != 0) {
                iceFacesId = _iceFacesId;
                break;
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("ICEfaces ID: " + iceFacesId);
        }
    }

    private void extractSequenceNumber() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Extracting Sequence Number...");
        }
        final String[] _xWindowCookieFieldValues =
            httpConnection.getTransaction().getHttpRequest().
                getFieldValues(HttpRequest.X_WINDOW_COOKIE);
        for (int i = 0; i < _xWindowCookieFieldValues.length; i++) {
            final StringTokenizer _xWindowCookieValues =
                new StringTokenizer(_xWindowCookieFieldValues[i], ";");
            while (_xWindowCookieValues.hasMoreTokens()) {
                final String _xWindowCookieValue =
                    _xWindowCookieValues.nextToken().trim();
                if (_xWindowCookieValue.startsWith("Sequence_Number")) {
                    try {
                        sequenceNumber =
                            Long.parseLong(
                                _xWindowCookieValue.substring(
                                    _xWindowCookieValue.indexOf("\"") + 1,
                                    _xWindowCookieValue.lastIndexOf("\"")));
                    } catch (NumberFormatException exception) {
                        // do nothing.
                    }
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Sequence Number: " + sequenceNumber);
                    }
                    return;
                }
            }
        }
    }

    private byte[] getEntityBody() {
        final StringBuffer _updates = new StringBuffer();
        final Iterator _updatedViews =
            updatedViews.getUpdatedViewsSet().iterator();
        while (_updatedViews.hasNext()) {
            _updates.append(_updatedViews.next());
            _updates.append(' ');
        }
        return
            (
                UPDATED_VIEWS_START_TAG +
                    _updates.toString() +
                UPDATED_VIEWS_END_TAG
            ).getBytes();
    }

    private String getSequenceNumberValue() {
        return String.valueOf(updatedViews.getSequenceNumber());
    }

    private HttpResponse handleException() {
        final HttpResponse _httpResponse;
        final Exception _exception = httpConnection.getException();
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
            _httpResponse =
                new HttpResponse(
                    HttpResponse.HTTP_11,
                    HttpResponse.INTERNAL_SERVER_ERROR,
                    "Internal Server Error");
            final ByteArrayOutputStream _byteArrayOutputStream =
                new ByteArrayOutputStream();
            _exception.printStackTrace(new PrintStream(_byteArrayOutputStream));
            _httpResponse.setEntityBody(
                new HttpResponse.EntityBody(
                    _byteArrayOutputStream.toByteArray()));
        } else {
            if (LOG.isErrorEnabled()) {
                LOG.error("Unhandled exception: " + _exception, _exception);
            }
            _httpResponse =
                new HttpResponse(
                    HttpResponse.HTTP_11,
                    HttpResponse.INTERNAL_SERVER_ERROR,
                    "Internal Server Error");
            final ByteArrayOutputStream _byteArrayOutputStream =
                new ByteArrayOutputStream();
            _exception.printStackTrace(new PrintStream(_byteArrayOutputStream));
            _httpResponse.setEntityBody(
                new HttpResponse.EntityBody(
                    _byteArrayOutputStream.toByteArray()));
        }
        return _httpResponse;
    }
}