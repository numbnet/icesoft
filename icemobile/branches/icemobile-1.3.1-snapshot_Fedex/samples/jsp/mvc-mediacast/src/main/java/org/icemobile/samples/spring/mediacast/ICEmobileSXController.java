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

package org.icemobile.samples.spring.mediacast;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import org.icemobile.util.SXUtils;

/**
 * Controller for registration of ICEmobile-SX in the session.
 * Add this controller to your Spring MVC application to support
 * ICEmobile-SX.
 *
 * ICEmobile-SX makes use of an "auxiliary upload" technique where
 * an HTTP POST containing an uploaded photo or String value from
 * a scanned barcode occurs outside the brower and separate from 
 * the other Ajax interaction by the user.  This requires the upload
 * to be stored for that user across multiple requests.  The simplest
 * mechanism for this is to use a Servlet session.  Additionally, it
 * is necessary to record whether the user has ICEmobile-SX installed,
 * and this is recorded in the session.
 *
 * A future version of ICEmobile will provide for request parameters,
 * short-lived upload identifier keys, and a registration cookie, 
 * thereby avoiding the session requirement.  Additionally, the
 * /icemobile path will be configurable.
 *
 */
@Controller
public class ICEmobileSXController {

    @ResponseBody
    @RequestMapping(value="/icemobile", method = RequestMethod.POST)
    public String postICEmobileSX(HttpServletRequest request)  {
        SXUtils.setSXSessionKeys(request);
        return "Welcome to ICEmobile";
    }

}
