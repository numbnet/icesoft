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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 *   The <code>HttpRequest</code> class represents an actual HTTP request
 *   message. In addition to its superclass <code>HttpMessage</code>, it is
 *   responsible for the request-line elements: method and request-URI.
 * </p>
 * <p>
 *   Additionally, it has all the HTTP 1.0 and 1.1 method and request header
 *   constants, as well as a couple of commonly used extension request header
 *   constants, as a convenience and uniform use of these HTTP elements.
 * </p>
 * <p>
 *   Finally, it contains a couple of convenience methods, namely
 *   <code>getMessage(boolean)</code> and <code>getRequestLine()</code> for
 *   <code>String</code> representations of these aspects of the HTTP request
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
public class HttpRequest
extends HttpMessage
implements Cloneable, Serializable {
    /** HTTP 1.1 method: CONNECT. */
    public static final String CONNECT = "CONNECT";

    /** HTTP 1.1 method: DELETE. */
    public static final String DELETE = "DELETE";

    /** HTTP 1.0 method: GET. */
    public static final String GET = "GET";

    /** HTTP 1.0 method: HEAD. */
    public static final String HEAD = "HEAD";

    /** HTTP 1.1 method: OPTIONS. */
    public static final String OPTIONS = "OPTIONS";

    /** HTTP 1.0 method: POST. */
    public static final String POST = "POST";

    /** HTTP 1.1 method: PUT. */
    public static final String PUT = "PUT";

    /** HTTP 1.1 method: TRACE. */
    public static final String TRACE = "TRACE";

    /** HTTP 1.1 request header: Accept. */
    public static final String ACCEPT = "Accept";

    /** HTTP 1.1 request header: Accept-Charset. */
    public static final String ACCEPT_CHARSET = "Accept-Charset";

    /** HTTP 1.1 request header: Accept-Encoding. */
    public static final String ACCEPT_ENCODING = "Accept-Encoding";

    /** HTTP 1.1 request header: Accept-Language. */
    public static final String ACCEPT_LANGUAGE = "Accept-Language";

    /** HTTP 1.0 request header: Authorization. */
    public static final String AUTHORIZATION = "Authorization";

    /** HTTP 1.1 request header: Expect. */
    public static final String EXPECT = "Expect";

    /** HTTP 1.0 request header: From. */
    public static final String FROM = "From";

    /** HTTP 1.1 request header: Host. */
    public static final String HOST = "Host";

    /** HTTP 1.0 request header: If-Modified-Since. */
    public static final String IF_MODIFIED_SINCE = "If-Modified-Since";

    /** HTTP 1.1 request header: If-Match. */
    public static final String IF_MATCH = "If-Match";

    /** HTTP 1.1 request header: If-None-Match. */
    public static final String IF_NONE_MATCH = "If-None-Match";

    /** HTTP 1.1 request header: If-Range. */
    public static final String IF_RANGE = "If-Range";

    /** HTTP 1.1 request header: If-Unmodified-Since. */
    public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";

    /** HTTP 1.1 request header: Max-Forwards. */
    public static final String MAX_FORWARDS = "Max-Forwards";

    /** HTTP 1.1 request header: Proxy-Authorization. */
    public static final String PROXY_AUTHORIZATION = "Proxy-Authorization";

    /** HTTP 1.1 request header: Range. */
    public static final String RANGE = "Range";

    /** HTTP 1.0 request header: Referer. */
    public static final String REFERER = "Referer";

    /** HTTP 1.1 request header: TE. */
    public static final String TE = "TE";

    /** HTTP 1.0 request header: User-Agent. */
    public static final String USER_AGENT = "User-Agent";

    /** HTTP request header (extension): Cookie. */
    public static final String COOKIE = "Cookie";

    /** HTTP request header (extension): Cookie2. */
    public static final String COOKIE2 = "Cookie2";

    // cache request directives...
    public static final String NO_CACHE       = "no-cache";
    public static final String NO_STORE       = "no-store";
    public static final String MAX_AGE        = "max-age";
    public static final String MAX_STALE      = "max-stale";
    public static final String MIN_FRESH      = "min-fresh";
    public static final String NO_TRANSFORM   = "no-transform";
    public static final String ONLY_IF_CACHED = "only-if-cached";

    private static final Log LOG = LogFactory.getLog(HttpRequest.class);

    private final Map parameterMap = new HashMap();
    private final Map cookieMap = new HashMap();

    private String method;
    private String requestUri;

    private long requestTime;

    /**
     * <p>
     *   Constructs a new <code>HttpRequest</code> with the specified
     *   <code>method</code>, <code>requestUri</code> and
     *   <code>httpVersion</code>.
     * </p>
     *
     * @param      method
     *                 the HTTP method of the HTTP request to be constructed.
     * @param      requestUri
     *                 the request URI of the HTTP request to be constructed.
     * @param      httpVersion
     *                 the HTTP version of the HTTP request to be constructed.
     * @throws     IllegalArgumentException
     *                 if one of the following occurs:
     *                 <ul>
     *                     <li>the specified <code>method</code> is either
     *                         <code>null</code> or empty;
     *                     </li>
     *                     <li>the specified <code>requestUri</code> is either
     *                         <code>null</code> or empty;
     *                     </li>
     *                     <li>the specified <code>httpVersion</code> is either
     *                         <code>null</code> or empty.
     *                 </ul>
     */
    public HttpRequest(
        final String method, final String requestUri, final String httpVersion)
    throws IllegalArgumentException {
        super(httpVersion);
        setMethod(method);
        setRequestUri(requestUri);
    }

    public boolean equals(final Object object) {
        return
            object instanceof HttpRequest &&
            ((HttpRequest)object).method.equalsIgnoreCase(method) &&
            ((HttpRequest)object).requestUri.equalsIgnoreCase(requestUri) &&
            super.equals(object);
    }

    public String getCookie(final String name) {
        return (String)cookieMap.get(name);
    }

    public Map getCookieMap() {
        return Collections.unmodifiableMap(cookieMap);
    }

    public String getMessage(final boolean showBody) {
        StringBuffer _stringBuffer = new StringBuffer();
        // request line...
        _stringBuffer.append(getRequestLine());
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
     *   Returns the method of this <code>HttpRequest</code>.
     * </p>
     *
     * @return     the method.
     * @see        #setMethod(String)
     */
    public String getMethod() {
        return method;
    }

    public String getParameter(final String name) {
        if (parameterMap.containsKey(name)) {
            return ((String[])parameterMap.get(name))[0];
        } else {
            return null;
        }
    }

    /**
     * <p>
     *   Gets the parameter map containing all the name-value pairs of the
     *   query-part of the Request-URI.
     * </p>
     *
     * @return     the query map.
     */
    public Map getParameterMap() {
        return Collections.unmodifiableMap(parameterMap);
    }

    public String[] getParameters(final String name) {
        return (String[])parameterMap.get(name);
    }

    /**
     * <p>
     *   Convenience method for getting the actual <code>String</code>
     *   representation of the request-line element of this
     *   <code>HttpRequest</code>.
     * </p>
     *
     * @return     the <code>String</code> representation of the request-line.
     */
    public String getRequestLine() {
        return method + SPACE + requestUri + SPACE + httpVersion;
    }

    /**
     * <p>
     *   Gets the request time in milliseconds of this <code>HttpRequest</code>.
     * </p>
     * <p>
     *   That is, the time in milliseconds this <code>HttpRequest</code> was
     *   send.
     * </p>
     *
     * @return     the request time.
     * @see        #setRequestTime(long)
     */
    public long getRequestTime() {
        return requestTime;
    }

    /**
     * <p>
     *   Returns the request-URI of this <code>HttpRequest</code>.
     * </p>
     *
     * @return     the request-URI.
     * @see        #setRequestUri(String)
     */
    public String getRequestUri() {
        return requestUri;
    }

    public String getStartLine() {
        return getRequestLine();
    }

    public void putHeader(final String fieldName, final String fieldValue) {
        super.putHeader(fieldName, fieldValue);
        if (fieldName.equals(COOKIE)) {
            extractCookies(fieldValue, cookieMap);
        }
    }

    public void setEntityBody(final EntityBody entityBody) {
        super.setEntityBody(entityBody);
        if (this.entityBody.bytes != null) {
            putHeader(
                EntityBody.CONTENT_LENGTH,
                Integer.toString(this.entityBody.bytes.length));
        }
        if (!containsHeader(EntityBody.CONTENT_TYPE)) {
            try {
                String _contentType =
                    URLConnection.guessContentTypeFromStream(
                        new ByteArrayInputStream(this.entityBody.bytes));
                if (_contentType != null) {
                    putHeader(EntityBody.CONTENT_TYPE, _contentType);
                }
            } catch (IOException exception) {
                if (LOG.isErrorEnabled()) {
                    LOG.error("An I/O exception occurred!", exception);
                }
            }
        }
    }

    /**
     * <p>
     *   Sets the method of this <code>HttpRequest</code> to the specified
     *   <code>method</code>.
     * </p>
     *
     * @param      method
     *                 the new method.
     * @throws     IllegalArgumentException
     *                 if the specified <code>method</code> is either
     *                 <code>null</code> or empty.
     * @see        #getMethod()
     * @see        #CONNECT
     * @see        #DELETE
     * @see        #GET
     * @see        #HEAD
     * @see        #OPTIONS
     * @see        #POST
     * @see        #PUT
     * @see        #TRACE
     */
    public void setMethod(final String method)
    throws IllegalArgumentException {
        if (method == null) {
            throw new IllegalArgumentException("method is null");
        }
        if (method.trim().length() == 0) {
            throw new IllegalArgumentException("method is empty");
        }
        this.method = method;
    }

    /**
     * <p>
     *   Sets the request time in milliseconds of this <code>HttpRequest</code>
     *   to the specified <code>requestTime</code>.
     * </p>
     * <p>
     *   That is, the time in milliseconds this <code>HttpRequest</code> was
     *   send.
     * </p>
     *
     * @param      requestTime
     *                 the new request time.
     * @see        #getRequestTime()
     */
    public void setRequestTime(final long requestTime) {
        this.requestTime = requestTime;
    }

    /**
     * <p>
     *   Sets the request-URI of this <code>HttpRequest</code> to the specified
     *   <code>requestUri</code>.
     * </p>
     *
     * @param      requestUri
     *                 the new request-URI.
     * @throws     IllegalArgumentException
     *                 if the specified <code>requestUri</code> is either
     *                 <code>null</code> or empty.
     * @see        #getRequestUri()
     */
    public void setRequestUri(final String requestUri)
    throws IllegalArgumentException {
        if (requestUri == null) {
            throw new IllegalArgumentException("requestUri is null");
        }
        if (requestUri.trim().length() == 0) {
            throw new IllegalArgumentException("requestUri is empty");
        }
        String _requestUri;
        try {
            _requestUri = URLDecoder.decode(requestUri, "UTF-8");
        } catch (UnsupportedEncodingException exception) {
            _requestUri = requestUri;
        }
        if (!_requestUri.equals(this.requestUri)) {
            this.requestUri = _requestUri;
            parameterMap.clear();
            extractQuery(this.requestUri, parameterMap);
        }
    }

    public String toString() {
        return "HttpRequest [" + getRequestLine() + "]";
    }

    private static void extractCookies(
        final String cookieString, final Map cookieMap) {

        StringTokenizer _cookies = new StringTokenizer(cookieString, ";");
        int _tokenCount = _cookies.countTokens();
        for (int i = 0; i < _tokenCount; i++) {
            String _cookie = _cookies.nextToken();
            int _index = _cookie.indexOf("=");
            cookieMap.put(
                _cookie.substring(0, _index).trim(),
                _cookie.substring(_index + 1).trim());
        }
    }

    private static void extractQuery(
        final String requestUri, final Map queryMap) {

        int _index = requestUri.indexOf("?");
        if (_index != -1) {
            StringTokenizer _parameters =
                new StringTokenizer(requestUri.substring(_index + 1), "&");
            int _tokenCount = _parameters.countTokens();
            for (int i = 0; i < _tokenCount; i++) {
                String _parameter = _parameters.nextToken();
                _index = _parameter.indexOf("=");
                String _name = _parameter.substring(0, _index);
                String _value = _parameter.substring(_index + 1);
                if (!queryMap.containsKey(_name)) {
                    queryMap.put(_name, new String[] {_value});
                } else {
                    String[] _oldValues = (String[])queryMap.remove(_name);
                    String[] _newValues = new String[_oldValues.length + 1];
                    System.arraycopy(
                        _oldValues, 0, _newValues, 0, _oldValues.length);
                    _newValues[_newValues.length - 1] = _value;
                    queryMap.put(_name, _newValues);
                }
            }
        }
    }
}
