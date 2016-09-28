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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

public class RequestUtilities {
    private static final Logger LOGGER = Logger.getLogger(RequestUtilities.class.getName());

    public static Map<String, String> getQueryParameterMap(final HttpServletRequest request) {
        Map<String, String> _queryParameterMap = new HashMap<String, String>();
        String _queryString = request.getQueryString();
        if (_queryString != null && _queryString.trim().length() != 0) {
            String[] _nameValuePairs = _queryString.split("&");
            for (final String _nameValuePair : _nameValuePairs) {
                String[] _tokens = _nameValuePair.split("=");
                _queryParameterMap.put(_tokens[0], _tokens[1]);
            }
        }
        return _queryParameterMap;
    }

    public static boolean isFirefox(final HttpServletRequest request) {
        String _userAgent = request.getHeader("User-Agent");
        return
            _userAgent != null &&
            _userAgent.contains("Mozilla/") && _userAgent.contains("Gecko/") && _userAgent.contains("Firefox/");
    }

    public static String toString(final HttpServletRequest request) {
        StringBuilder _stringBuilder = new StringBuilder();
        _stringBuilder.append(request.getMethod()).append(" ").append(request.getRequestURI()).append("\r\n");
        Enumeration<String> _headerNames = request.getHeaderNames();
        while (_headerNames.hasMoreElements()) {
            String _headerName = _headerNames.nextElement();
            Enumeration<String> _headerValues = request.getHeaders(_headerName);
            while (_headerValues.hasMoreElements()) {
                String _headerValue = _headerValues.nextElement();
                _stringBuilder.append(_headerName).append(": ").append(_headerValue).append("\r\n");
            }
        }
        _stringBuilder.append("\r\n");
        Enumeration<String> _parameterNames = request.getParameterNames();
        if (_parameterNames.hasMoreElements()) {
            while (_parameterNames.hasMoreElements()) {
                String _parameterName = _parameterNames.nextElement();
                String[] _parameterValues = request.getParameterValues(_parameterName);
                for (final String _parameterValue : _parameterValues) {
                    _stringBuilder.append(_parameterName).append(" = ").append(_parameterValue).append("\r\n");
                }
            }
            _stringBuilder.append("\r\n");
        }
        return _stringBuilder.toString();
    }
}
