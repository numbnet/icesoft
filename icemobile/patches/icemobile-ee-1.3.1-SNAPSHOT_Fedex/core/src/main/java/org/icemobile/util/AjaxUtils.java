/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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

package org.icemobile.util;

import javax.servlet.http.HttpServletRequest;

public class AjaxUtils {
    
    private static final String HEADER_FACES_REQUEST = "Faces-Request";
    private static final String FACES_AJAX = "partial/ajax";
    private static final String HEADER_REQUESTED_WITH = "X-Requested-With";
    private static final String XMLHTTPREQUEST = "XMLHttpRequest";

	public static boolean isAjaxRequest(HttpServletRequest request) {
		String requestedWith = request.getHeader(HEADER_FACES_REQUEST);
		if (FACES_AJAX.equals(requestedWith))  {
            return true;
        }

		requestedWith = request.getHeader(HEADER_REQUESTED_WITH);
		return requestedWith != null ? XMLHTTPREQUEST.equals(requestedWith) : false;
	}


}
