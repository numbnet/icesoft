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
package org.icesoft.util;

import static org.icesoft.util.StringUtilities.isNullOrIsEmpty;

import java.util.logging.Level;
import java.util.logging.Logger;

public class URIUtilities {
    private static final Logger LOGGER = Logger.getLogger(URIUtilities.class.getName());

    public static String getHostName(final String uri) {
        if (isNullOrIsEmpty(uri)) {
            return uri;
        }
        String _uri = uri.trim();
        int _currentIndex;
        int _beginIndex = 0;
        int _endIndex = _uri.length();
        if (_uri.startsWith("http://")) {
            _beginIndex = "http://".length();
        } else if (_uri.startsWith("https://")) {
            _beginIndex = "https://".length();
        }
        _currentIndex = _uri.indexOf("/", _beginIndex);
        if (_currentIndex != -1) {
            _endIndex = _currentIndex;
        }
        _uri = _uri.substring(_beginIndex, _endIndex);
        _beginIndex = 0;
        _endIndex = _uri.length();
        _currentIndex = _uri.indexOf(":", _beginIndex);
        if (_currentIndex != -1) {
            _endIndex = _currentIndex;
        }
        return _uri.substring(_beginIndex, _endIndex);
    }

}
