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

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

public class RequestUtilities {
    private static final Logger LOGGER = Logger.getLogger(RequestUtilities.class.getName());

    public static boolean isFirefox(final HttpServletRequest request) {
        String _userAgent = request.getHeader("User-Agent");
        return
            _userAgent != null &&
            _userAgent.contains("Mozilla/") && _userAgent.contains("Gecko/") && _userAgent.contains("Firefox/");
    }
}
