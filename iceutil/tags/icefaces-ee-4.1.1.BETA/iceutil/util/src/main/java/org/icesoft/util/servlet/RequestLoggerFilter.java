/*
 * Copyright 2004-2016 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.icesoft.util.servlet;

import static org.icesoft.util.StringUtilities.toCamelCase;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

@WebFilter(
    filterName = "Request Logger",
    asyncSupported = true,
    urlPatterns = {
        "/*"
    }
)
public class RequestLoggerFilter
implements Filter {
    private static final Logger LOGGER = Logger.getLogger(RequestLoggerFilter.class.getName());

    public void init(final FilterConfig filterConfig)
    throws ServletException {
        // Do nothing.
    }

    public void doFilter(
        final ServletRequest request, final ServletResponse response, final FilterChain filterChain)
    throws IOException, ServletException {
        ServletRequest _request = request;
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(
                Level.FINE,
                "\r\n" +
                "\r\n" +
                "Request:\r\n" +
                "\r\n" +
                (
                    _request instanceof HttpServletRequest ?
                        toString(
                            (HttpServletRequest)
                                (_request = new CachingHTTPServletRequest((HttpServletRequest)_request))
                        ) :
                        toString(
                            _request = new CachingServletRequest(_request)
                        )
                )
            );
        }
        filterChain.doFilter(_request, response);
    }

    public void destroy() {
        // Do nothing.
    }

    protected static String toString(final HttpServletRequest request)
    throws IOException {
        StringBuilder _requestStringBuilder = new StringBuilder();
        _requestStringBuilder.
            append("(").
                append("From: ").
                    append(request.getRemoteAddr()).append("/").append(request.getRemoteHost()).append(":").
                        append(request.getRemotePort()).append(", ").
                append("To: ").
                    append(request.getLocalAddr()).append("/").append(request.getLocalName()).append(":").
                        append(request.getLocalPort()).
            append(")\r\n").
            append(request.getMethod()).append(" ").append(request.getRequestURI()).append(" ").
                append(request.getProtocol()).append("\r\n");
        Enumeration<String> _headerNames = request.getHeaderNames();
        while (_headerNames.hasMoreElements()) {
            String _headerName = _headerNames.nextElement();
            Enumeration _headerValues = request.getHeaders(_headerName);
            while (_headerValues.hasMoreElements()) {
                _requestStringBuilder.
                    append(toCamelCase(_headerName, "-", "-")).append(": ").
                        append(_headerValues.nextElement()).append("\r\n");
            }
        }
        _requestStringBuilder.append("\r\n");
        InputStream _in = request.getInputStream();
        byte[] _buffer = new byte[8192];
        int _numberOfBytesRead;
        while ((_numberOfBytesRead = _in.read(_buffer, 0, _buffer.length)) != -1) {
            _requestStringBuilder.append(new String(_buffer, 0, _numberOfBytesRead));
        }
        _requestStringBuilder.append("\r\n");
        return _requestStringBuilder.toString();
    }

    protected static String toString(final ServletRequest request)
    throws IOException {
        StringBuilder _requestStringBuilder = new StringBuilder();
        _requestStringBuilder.
            append("(").
                append("From: ").
                    append(request.getRemoteAddr()).append("/").append(request.getRemoteHost()).append(":").
                        append(request.getRemotePort()).append(", ").
                append("To: ").
                    append(request.getLocalAddr()).append("/").append(request.getLocalName()).append(":").
                        append(request.getLocalPort()).
            append(")\r\n").
            append("\r\n");
        InputStream _in = request.getInputStream();
        byte[] _buffer = new byte[8192];
        int _numberOfBytesRead;
        while ((_numberOfBytesRead = _in.read(_buffer, 0, _buffer.length)) != -1) {
            _requestStringBuilder.append(new String(_buffer, 0, _numberOfBytesRead));
        }
        _requestStringBuilder.append("\r\n");
        return _requestStringBuilder.toString();
    }
}
