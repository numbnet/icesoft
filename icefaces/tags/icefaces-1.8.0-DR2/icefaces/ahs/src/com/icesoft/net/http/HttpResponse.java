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
package com.icesoft.net.http;

import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.ProtocolException;

/**
 * <p>
 *   The <code>HttpResponse</code> class represents an actual HTTP response
 *   message. In addition to its superclass <code>HttpMessage</code>, it is
 *   responsible for the status-line elements: status-code and reason-phrase.
 * </p>
 * <p>
 *   Additionally, it has all the HTTP 1.0 and 1.1 status code and response
 *   header constants, as well as a couple of commonly used extension response
 *   header constants, as a convenience and uniform use of these HTTP elements.
 * </p>
 * <p>
 *   Finally, it contains a couple of convenience methods, namely
 *   <code>getMessage(boolean)</code> and <code>getStatusLine</code> for
 *   <code>String</code> representations of these aspects of the HTTP response
 *   message.
 * </p>
 * <p>
 *   For detailed information on the HyperText Transfer Protocol and some of its
 *   extensions see:
 *   <ul>
 *     <li>
 *       <a href="http://www.ietf.org/rfc/rfc1945.txt"
 *          target="_top">RFC 1945</a> - "Hypertext Transfer Protocol -- HTTP/1.0"
 *     </li>
 *     <li>
 *       <a href="http://www.ietf.org/rfc/rfc2068.txt"
 *          target="_top">RFC 2068</a> - "Hypertext Transfer Protocol -- HTTP/1.1"
 *     </li>
 *     <li>
 *       <a href="http://www.ietf.org/rfc/rfc2109.txt"
 *          target="_top">RFC 2109</a> - "HTTP State Management Mechanism"
 *     </li>
 *     <li>
 *       <a href="http://www.ietf.org/rfc/rfc2616.txt"
 *          target="_top">RFC 2616</a> - "Hypertext Transfer Protocol -- HTTP/1.1"
 *     </li>
 *     <li>
 *       <a href="http://www.ietf.org/rfc/rfc2965.txt"
 *          target="_top">RFC 2965</a> - "HTTP State Management Mechanism"
 *     </li>
 *   </ul>
 * </p>
 */
public class HttpResponse
extends HttpMessage
implements Cloneable, Serializable {
    private static final HttpParser HTTP_PARSER = new HttpParser();

    /** HTTP 1.1 informational status code 100: Continue. */
    public static final int CONTINUE = 100;

        /** HTTP 1.1 informational status code 101: Switching Protocols. */
    public static final int SWITCHING_PROTOCOLS = 101;

    /** HTTP 1.0 successful status code 200: OK. */
    public static final int OK = 200;

    /** HTTP 1.0 successful status code 201: Created. */
    public static final int CREATED = 201;

    /** HTTP 1.0 successful status code 202: Accepted. */
    public static final int ACCEPTED = 202;

    /** HTTP 1.1 successful status code 203: Non Authoritative Information. */
    public static final int NON_AUTHORITATIVE_INFORMATION = 203;

    /** HTTP 1.0 successful status code 204: No Content. */
    public static final int NO_CONTENT = 204;

    /** HTTP 1.1 successful status code 205: Reset Content. */
    public static final int RESET_CONTENT = 205;

    /** HTTP 1.1 successful status code 206: Partial Content. */
    public static final int PARTIAL_CONTENT = 206;

    /** HTTP 1.0 redirection status code 300: Multiple Choices. */
    public static final int MULTIPLE_CHOICES = 300;

    /** HTTP 1.0 redirection status code 301: Moved Permanently. */
    public static final int MOVED_PERMANENTLY = 301;

    /** HTTP 1.0 redirection status code 302: Moved Temporarily. */
    public static final int MOVED_TEMPORARILY = 302;

    /** HTTP 1.1 redirection status code 302: Found. */
    public static final int FOUND = 302;

    /** HTTP 1.1 redirection status code 303: See Other. */
    public static final int SEE_OTHER = 303;

    /** HTTP 1.0 redirection status code 304: Not Modified. */
    public static final int NOT_MODIFIED = 304;

    /** HTTP 1.1 redirection status code 305: Use Proxy. */
    public static final int USE_PROXY = 305;

    /** HTTP 1.1 redirection status code 306: Unused. */
    public static final int UNUSED = 306;

    /** HTTP 1.1 redirection status code 307: Temporary Redirect. */
    public static final int TEMPORARY_REDIRECT = 307;

    /** HTTP 1.0 client error status code 400: Bad Request. */
    public static final int BAD_REQUEST = 400;

    /** HTTP 1.0 client error status code 401: Unauthorized. */
    public static final int UNAUTHORIZED = 401;

    /** HTTP 1.1 client error status code 402: Payment Required. */
    public static final int PAYMENT_REQUIRED = 402;

    /** HTTP 1.0 client error status code 403: Forbidden. */
    public static final int FORBIDDEN = 403;

    /** HTTP 1.0 client error status code 404: Not Found. */
    public static final int NOT_FOUND = 404;

    /** HTTP 1.1 client error status code 405: Method Not Allowed. */
    public static final int METHOD_NOT_ALLOWED = 405;

    /** HTTP 1.1 client error status code 406: Not Acceptable. */
    public static final int NOT_ACCEPTABLE = 406;

    /** HTTP 1.1 client error status code 407: Proxy Authentication Required. */
    public static final int PROXY_AUTHENTICATION_REQUIRED = 407;

    /** HTTP 1.1 client error status code 408: Request Timeout. */
    public static final int REQUEST_TIMEOUT = 408;

    /** HTTP 1.1 client error status code 409: Conflict. */
    public static final int CONFLICT = 409;

    /** HTTP 1.1 client error status code 410: Gone. */
    public static final int GONE = 410;

    /** HTTP 1.1 client error status code 411: Length Required. */
    public static final int LENGTH_REQUIRED = 411;

    /** HTTP 1.1 client error status code 412: Precondition Failed. */
    public static final int PRECONDITION_FAILED = 412;

    /** HTTP 1.1 client error status code 413: Request Entity Too Large. */
    public static final int REQUEST_ENTITY_TOO_LARGE = 413;

    /** HTTP 1.1 client error status code 414: Request URI Too Long. */
    public static final int REQUEST_URI_TOO_LONG = 414;

    /** HTTP 1.1 client error status code 415: Unsupported Media Type. */
    public static final int UNSUPPORTED_MEDIA_TYPE = 415;

    /** HTTP 1.1 client error status code 416: Requested Range Not Satisfiable. */
    public static final int REQUESTED_RANGE_NOT_SATISFIABLE = 416;

    /** HTTP 1.1 client error status code 417: Expectation Failed. */
    public static final int EXPECTATION_FAILED = 417;

    /** HTTP 1.0 server error status code 500: Internal Server Error. */
    public static final int INTERNAL_SERVER_ERROR = 500;

    /** HTTP 1.0 server error status code 501: Not Implemented. */
    public static final int NOT_IMPLEMENTED = 501;

    /** HTTP 1.0 server error status code 502: Bad Gateway. */
    public static final int BAD_GATEWAY = 502;

    /** HTTP 1.0 server error status code 503: Service Unavailable. */
    public static final int SERVICE_UNAVAILABLE = 503;

    /** HTTP 1.1 server error status code 504: Gateway Timeout. */
    public static final int GATEWAY_TIMEOUT = 504;

    /** HTTP 1.1 server error status code 505: HTTP Version Not Supported. */
    public static final int HTTP_VERSION_NOT_SUPPORTED = 505;

    /** HTTP 1.1 response header: Accept-Ranges. */
    public static final String ACCEPT_RANGES = "Accept-Ranges";

    /** HTTP 1.1 response header: Age. */
    public static final String AGE = "Age";

    /** HTTP 1.1 response header: ETag. */
    public static final String ETAG = "ETag";

    /** HTTP 1.0 response header: Location. */
    public static final String LOCATION = "Location";

    /** HTTP 1.1 response header: Proxy-Authenticate. */
    public static final String PROXY_AUTHENTICATE = "Proxy-Authenticate";

    /** HTTP 1.1 response header: Public. */
    public static final String PUBLIC = "Public";

    /** HTTP 1.1 response header: Retry-After. */
    public static final String RETRY_AFTER = "Retry-After";

    /** HTTP 1.0 response header: Server. */
    public static final String SERVER = "Server";

    /** HTTP 1.0 response header: Title. */
    public static final String TITLE = "Title";

    /** HTTP 1.1 response header: Vary. */
    public static final String VARY = "Vary";

    /** HTTP 1.0 response header: WWW-Authenticate. */
    public static final String WWW_AUTHENTICATE = "WWW-Authenticate";

    /** HTTP response header (extension): Refresh. */
    public static final String REFRESH = "Refresh";

    /** HTTP response header (extension): Set-Cookie. */
    public static final String SET_COOKIE         = "Set-Cookie";

    /** HTTP response header (extension): Set-Cookie2. */
    public static final String SET_COOKIE2        = "Set-Cookie2";

    // cache response directives...
//    public static final String PUBLIC           = "public";
    public static final String PRIVATE          = "private";
    public static final String NO_CACHE         = "no-cache";
    public static final String NO_STORE         = "no-store";
    public static final String NO_TRANSFORM     = "no-transform";
    public static final String MUST_REVALIDATE  = "must-revalidate";
    public static final String PROXY_REVALIDATE = "proxy-revalidate";
    public static final String MAX_AGE          = "max-age";
    public static final String S_MAX_AGE        = "s-maxage";

    private int statusCode;
    private String reasonPhrase;

    private long responseTime;

    /**
     * <p>
     *   Constructs a new <code>HttpResponse</code> with the specified
     *   <code>httpVersion</code>, <code>statusCode</code> and
     *   <code>reasonPhrase</code>.
     * </p>
     *
     * @param      httpVersion
     *                 the HTTP version of the HTTP response to be constructed.
     * @param      statusCode
     *                 the status code of the HTTP response to be constructed.
     * @param      reasonPhrase
     *                 the reason phrase of the HTTP response to be constructed.
     * @throws     IllegalArgumentException
     *                 if one of the following occurs:
     *                 <ul>
     *                     <li>the specified <code>httpVersion</code> is either
     *                         <code>null</code> or empty;
     *                     </li>
     *                     <li>the specified <code>statusCode</code> is lesser
     *                         than <code>0</code>;
     *                     </li>
     *                     <li>the specified <code>reasonPhrase</code> is
     *                         <code>null</code>.
     *                     </li>
     *                 </ul>
     */
    public HttpResponse(
        final String httpVersion, final int statusCode,
        final String reasonPhrase)
    throws IllegalArgumentException {
        super(httpVersion);
        setStatusCode(statusCode);
        setReasonPhrase(reasonPhrase);
    }

    public boolean equals(final Object object) {
        return
            object instanceof HttpResponse &&
            ((HttpResponse)object).reasonPhrase.equalsIgnoreCase(
                reasonPhrase) &&
            ((HttpResponse)object).statusCode == statusCode &&
            super.equals(object);
    }

    public String getMessage(final boolean showBody) {
        StringBuffer _stringBuffer = new StringBuffer();
        // status line...
        _stringBuffer.append(getStatusLine());
        _stringBuffer.append(CR_LF);
        // headers...
        int _size = headerMap.getSize();
        for (int i = 0; i < _size; i++) {
            _stringBuffer.append(headerMap.getFieldName(i));
            _stringBuffer.append(COLON);
            _stringBuffer.append(SPACE);
            _stringBuffer.append(headerMap.getFieldValue(i));
            _stringBuffer.append(CR_LF);
        }
        // blank line headers termination...
        _stringBuffer.append(CR_LF);
        // entity body...
        if (showBody && entityBody != null) {
            _stringBuffer.append(entityBody.toString());
        }
        return _stringBuffer.toString();
    }

    /**
     * <p>
     *   Returns the reason-phrase of this <code>HttpResponse</code>.
     * </p>
     *
     * @return     the reason-phrase.
     * @see        #setReasonPhrase(String)
     */
    public String getReasonPhrase() {
        return reasonPhrase;
    }

    /**
     * <p>
     *   Gets the response time in milliseconds of this
     *   <code>HttpResponse</code>.
     * </p>
     * <p>
     *   That is, the time in milliseconds this <code>HttpResponse</code> was
     *   received.
     * </p>
     *
     * @return     the response time.
     * @see        #setResponseTime(long)
     */
    public long getResponseTime() {
        return responseTime;
    }

    public String getStartLine() {
        return getStatusLine();
    }

    /**
     * <p>
     *   Returns the status-code of this <code>HttpResponse</code>.
     * </p>
     *
     * @return    the status-code.
     * @see       #setStatusCode(int)
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * <p>
     *   Convenience method for getting the actual <code>String</code>
     *   representation of the status-line element of this
     *   <code>HttpResponse</code>.
     * </p>
     *
     * @return     the <code>String</code> representation of the status-line.
     */
    public String getStatusLine() {
        return httpVersion + SPACE + statusCode + SPACE + reasonPhrase;
    }

    /**
     * <p>
     *   Determines if this <code>HttpResponse</code> is a client error
     *   response. That is, if the Status-Code is in the form of
     *   <code>4xx</code>.
     * </p>
     * <p>
     *   This class of Status-Code is intended for cases in which the client
     *   seems to have erred.
     * </p>
     *
     * @return     <code>true</code> if this <code>HttpResponse</code> is a
     *             client error response, <code>false</code> if not.
     * @see        #isClientError(int)
     */
    public boolean isClientError() {
        return isClientError(statusCode);
    }

    /**
     * <p>
     *   Determines if the specified <code>statusCode</code> is a client error
     *   Status-Code. That is, if the specified <code>statusCode</code> is in
     *   the form of <code>4xx</code>.
     * </p>
     *
     * @param      statusCode
     * @return     <code>true</code> if the specified <code>statusCode</code> is
     *             a client error Status-Code, <code>false</code> if not.
     */
    public static boolean isClientError(final int statusCode) {
        return statusCode >= 400 && statusCode < 500;
    }

    /**
     * <p>
     *   Determines if this <code>HttpResponse</code> is an informational
     *   response. That is, if the Status-Code is in the form of
     *   <code>1xx</code>.
     * </p>
     * <p>
     *   This class of Status-Code indicates a provisional response, consisting
     *   only of the Status-Line and optional headers.
     * </p>
     *
     * @return     <code>true</code> if this <code>HttpResponse</code> is an
     *             informational response, <code>false</code> if not.
     * @see        #isInformational(int)
     */
    public boolean isInformational() {
        return isInformational(statusCode);
    }

    /**
     * <p>
     *   Determines if the specified <code>statusCode</code> is an informational
     *   Status-Code. That is, if the specified <code>statusCode</code> is in
     *   the form of <code>1xx</code>.
     * </p>
     *
     * @param      statusCode
     * @return     <code>true</code> if the specified <code>statusCode</code> is
     *             an informational Status-Code, <code>false</code> if not.
     */
    public static boolean isInformational(final int statusCode) {
        return statusCode >= 100 && statusCode < 200;
    }

    /**
     * <p>
     *   Determines if this <code>HttpResponse</code> is a redirection response.
     *   That is, if the Status-Code is in the form of <code>3xx</code>.
     * </p>
     * <p>
     *   This class of Status-Code indicates that further action needs to be
     *   taken by the user agent in order to fulfill the request.
     * </p>
     *
     * @return     <code>true</code> if this <code>HttpResponse</code> is a
     *             redirection response, <code>false</code> if not.
     * @see        #isRedirection(int)
     */
    public boolean isRedirection() {
        return isRedirection(statusCode);
    }

    /**
     * <p>
     *   Determines if the specified <code>statusCode</code> is a redirection
     *   Status-Code. That is, if the specified <code>statusCode</code> is in
     *   the form of <code>3xx</code>.
     * </p>
     *
     * @param      statusCode
     * @return     <code>true</code> if the specified <code>statusCode</code> is
     *             a redirection Status-Code, <code>false</code> if not.
     */
    public static boolean isRedirection(final int statusCode) {
        return statusCode >= 300 && statusCode < 400;
    }

    /**
     * <p>
     *   Determines if this <code>HttpResponse</code> is a server error
     *   response. That is, if the Status-Code is in the form of
     *   <code>5xx</code>.
     * </p>
     * <p>
     *   This class of Status-Code is intended for cases in which the server
     *   is aware that it has erred or is incapable of performing the request.
     * </p>
     *
     * @return     <code>true</code> if this <code>HttpResponse</code> is a
     *             server error response, <code>false</code> if not.
     * @see        #isServerError(int)
     */
    public boolean isServerError() {
        return isServerError(statusCode);
    }

    /**
     * <p>
     *   Determines if the specified <code>statusCode</code> is a server error
     *   Status-Code. That is, if the specified <code>statusCode</code> is in
     *   the form of <code>5xx</code>.
     * </p>
     *
     * @param      statusCode
     * @return     <code>true</code> if the specified <code>statusCode</code> is
     *             a server error Status-Code, <code>false</code> if not.
     */
    public static boolean isServerError(final int statusCode) {
        return statusCode >= 500 && statusCode < 600;
    }

    /**
     * <p>
     *   Determines if this <code>HttpResponse</code> is a successful response.
     *   That is, if the Status-Code is in the form of <code>2xx</code>.
     * </p>
     * <p>
     *   This class of Status-Code indicates that the client's request was
     *   successfully received, understood, and accepted.
     * </p>
     *
     * @return     <code>true</code> if this <code>HttpResponse</code> is a
     *             successful response, <code>false</code> if not.
     * @see        #isSuccessful(int)
     */
    public boolean isSuccessful() {
        return isSuccessful(statusCode);
    }

    /**
     * <p>
     *   Determines if the specified <code>statusCode</code> is a successful
     *   Status-Code. That is, if the specified <code>statusCode</code> is in
     *   the form of <code>2xx</code>.
     * </p>
     *
     * @param      statusCode
     * @return     <code>true</code> if the specified <code>statusCode</code> is
     *             a successful Status-Code, <code>false</code> if not.
     */
    public static boolean isSuccessful(final int statusCode) {
        return statusCode >= 200 && statusCode < 300;
    }

    /**
     * <p>
     *   Sets the reason-phrase of this <code>HttpResponse</code> to the
     *   specified <code>reasonPhrase</code>.
     * </p>
     *
     * @param      reasonPhrase
     *                 the new reason-phrase.
     * @throws     IllegalArgumentException
     *                 if the specified <code>reasonPhrase</code> is
     *                 <code>null</code>.
     * @see        #getReasonPhrase()
     */
    public void setReasonPhrase(final String reasonPhrase)
    throws IllegalArgumentException {
        if (reasonPhrase == null) {
            throw new IllegalArgumentException("reasonPhrase is null");
        }
        this.reasonPhrase = reasonPhrase;
    }

    /**
     * <p>
     *   Sets the response time in milliseconds of this
     *   <code>HttpResponse</code> to the specified <code>responseTime</code>.
     * </p>
     * <p>
     *   That is, the time in milliseconds this <code>HttpResponse</code> was
     *   received.
     * </p>
     *
     * @param      responseTime
     *                 the new response time.
     * @see        #getResponseTime()
     */
    public void setResponseTime(final long responseTime) {
        this.responseTime = responseTime;
    }

    /**
     * <p>
     *   Sets the status-code of this <code>HttpResponse</code> to the specified
     *   <code>statusCode</code>.
     * </p>
     *
     * @param      statusCode
     *                 the new status-code.
     * @throws     IllegalArgumentException
     *                 if the specified <code>statusCode</code> is lesser than
     *                 <code>0</code>.
     * @see        #getStatusCode()
     */
    public void setStatusCode(final int statusCode)
    throws IllegalArgumentException {
        if (statusCode < 0) {
            throw new IllegalArgumentException("statusCode < 0");
        }
        this.statusCode = statusCode;
    }

    public String toString() {
        return "HttpResponse [" + getStatusLine() + "]";
    }

    static HttpResponse createHttpResponse(
        final HttpURLConnection httpUrlConnection)
    throws ProtocolException {
        int _index = 0;
        HttpResponse _httpResponse =
            HTTP_PARSER.parseStatusLine(
                httpUrlConnection.getHeaderField(_index++));
        String _fieldName;
        while (
            (_fieldName = httpUrlConnection.getHeaderFieldKey(_index)) !=
                null) {

            _httpResponse.putHeader(
                _fieldName, httpUrlConnection.getHeaderField(_index++));
        }
        return _httpResponse;
    }
}
