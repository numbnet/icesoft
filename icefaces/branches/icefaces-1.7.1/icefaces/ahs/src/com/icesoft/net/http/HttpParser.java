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

import java.net.ProtocolException;
import java.util.StringTokenizer;

public class HttpParser {
    /**
     * <p>
     *   Parses the specified HTTP <code>header</code> as defined by the
     *   specification. The specification defines the header to be as follows:
     *   <code>message-header = field-name ":" [ field-value ]</code>.
     * </p>
     * <p>
     *   As a result of the parsing, the header is added to the specified
     *   <code>httpMessage</code>.
     * </p>
     *
     * @param      header
     *                 the header to be parsed.
     * @param      httpMessage
     *                 the HTTP message to which the header is part of.
     * @throws     IllegalArgumentException
     *                 if one of the following occurs:
     *                 <ul>
     *                     <li>the specified <code>header</code> is
     *                         <code>null</code>;
     *                     </li>
     *                     <li>the specified <code>httpMessage</code> is
     *                         <code>null</code>.
     *                     </li>
     *                 </ul>
     * @throws     ProtocolException
     *                 if some part of the header could not be parsed properly.
     */
    public void parseHeader(final String header, final HttpMessage httpMessage)
    throws IllegalArgumentException, ProtocolException {
        if (header == null) {
            throw new IllegalArgumentException("header is null");
        }
        if (httpMessage == null) {
            throw new IllegalArgumentException("httpMessage is null");
        }
        int _index = header.indexOf(HttpMessage.COLON);
        if (_index != -1) {
            String _fieldName = header.substring(0, _index).trim();
            String _fieldValue = header.substring(_index + 1).trim();
            if (_fieldValue.length() == 0) {
                _fieldValue = "unknown/unknown";
            }
            httpMessage.putHeader(_fieldName, _fieldValue);
        } else {
            throw new ProtocolException("Invalid header: " + header);
        }
    }

    /**
     * <p>
     *   Parses the specified HTTP Request <code>requestLine</code> as defined
     *   by the specification. The specification defines the Request-Line to be
     *   as follows:
     *   <code>Request-Line = Method SP Request-URI SP HTTP-Version CRLF</code>
     *   (the specified <code>requestLine</code> should not contain the
     *   <code>CRLF</code> at the end.
     * </p>
     * <p>
     *   As a result of the parsing, a <code>HttpRequest</code> object is
     *   returned containing the Method, Request-URI and HTTP-Version.
     * </p>
     *
     * @param      requestLine
     *                 the Request-Line to be parsed.
     * @return     the HTTP Request containing the parsed Method, Request-URI
     *             and HTTP-Version as parts of the Request-Line.
     * @throws     ProtocolException
     *                 if some part of the Request-Line could not be parsed
     *                 properly.
     */
    public HttpRequest parseRequestLine(final String requestLine)
    throws ProtocolException {
        if (requestLine == null) {
            return null;
        }
        StringTokenizer _tokens = new StringTokenizer(requestLine);
        int _count = _tokens.countTokens();
        if (_count-- != 0) {
            String _method = _tokens.nextToken();
            if (_count-- != 0) {
                String _requestUri = _tokens.nextToken();
                if (_count-- != 0) {
                    String _httpVersion = parseVersion(_tokens.nextToken());
                    return
                        createHttpRequest(_method, _requestUri, _httpVersion);
                } else {
                    throw new ProtocolException(
                        "Invalid Request-Line; missing HTTP-Version: " +
                            requestLine);
                }
            } else {
                throw new ProtocolException(
                    "Invalid Request-Line; missing Request-URI: " +
                        requestLine);
            }
        } else {
            throw new ProtocolException("Invalid Request-Line: empty");
        }
    }

    /**
     * <p>
     *   Parses the specified HTTP Response <code>statusLine</code> as defined
     *   by the specification. The specification defines the Status-Line to be
     *   as follows:
     *   <code>Status-Line = HTTP-Version SP Status-Code SP Reason-Phrase CRLF</code>
     *   (the specified <code>statusLine</code> should not contain the
     *   <code>CRLF</code> at the end.
     * </p>
     * <p>
     *   As a result of the parsing, a <code>HttpResponse</code> object is
     *   returned containing the HTTP-Version, Status-Code and Reason-Phrase.
     * </p>
     *
     * @param      statusLine
     *                 the Status-Line to be parsed.
     * @return     the HTTP Response containing the parsed HTTP-Version,
     *             Status-Code and Reason-Phrase parts of the Status-Line.
     * @throws     ProtocolException
     *                 if some part of the Status-Line could not be parsed
     *                 properly.
     */
    public HttpResponse parseStatusLine(final String statusLine)
    throws ProtocolException {
        if (statusLine == null) {
            return null;
        }
        StringTokenizer _tokens = new StringTokenizer(statusLine);
        int _count = _tokens.countTokens();
        if (_count-- != 0) {
            String _httpVersion = parseVersion(_tokens.nextToken());
            if (_count-- != 0) {
                int _statusCode = parseStatusCode(_tokens.nextToken());
                StringBuffer _reasonPhrase = new StringBuffer();
                while (_count-- != 0) {
                    _reasonPhrase.append(_tokens.nextToken());
                    if (_count != 0) {
                        _reasonPhrase.append(" ");
                    }
                }
                return
                    createHttpResponse(
                        _httpVersion, _statusCode, _reasonPhrase.toString());
            } else {
                throw new ProtocolException(
                    "Invalid Status-Line; missing Status-Code: " + statusLine);
            }
        } else {
            throw new ProtocolException("Invalid Status-Line: empty");
        }
    }

    protected HttpRequest createHttpRequest(
        final String method, final String requestUri, final String httpVersion)
    throws IllegalArgumentException {
        return new HttpRequest(method, requestUri, httpVersion);
    }

    protected HttpResponse createHttpResponse(
        final String httpVersion, final int statusCode,
        final String reasonPhrase)
    throws IllegalArgumentException {
        return new HttpResponse(httpVersion, statusCode, reasonPhrase);
    }

    private static int parseStatusCode(final String statusCode)
    throws ProtocolException {
        if (statusCode.length() != 3) {
            throw new ProtocolException("Invalid status code: " + statusCode);
        }
        int _statusCode = 0;
        for (int i = 0; i < 3; i++) {
            char _char = statusCode.charAt(i);
            if (Character.isDigit(_char)) {
                _statusCode =
                    _statusCode * 10 +
                        Integer.parseInt(new Character(_char).toString());
            } else {
                throw
                    new ProtocolException("Invalid status code: " + statusCode);
            }
        }
        return _statusCode;
    }

    private static String parseVersion(final String version)
    throws ProtocolException {
        if (!version.startsWith("HTTP/")) {
            throw new ProtocolException("Invalid version: " + version);
        }
        int _index = 5;
        int _length = version.length();
        while (Character.isDigit(version.charAt(_index))) {
            if (++_index == _length) {
                throw new ProtocolException("Invalid version: " + version);
            }
        }
        if (version.charAt(_index++) != '.') {
            throw new ProtocolException("Invalid version: " + version);
        }
        while (Character.isDigit(version.charAt(_index))) {
            if (++_index == _length) {
                return version;
            }
        }
        throw new ProtocolException("Invalid version: " + version);
    }
}
