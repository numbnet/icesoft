/*
 * Version: MPL 1.1
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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 */

package com.icesoft.faces.webapp;

import org.icefaces.push.http.MimeTypeMatcher;
import org.icefaces.push.http.Request;
import org.icefaces.push.http.Response;
import org.icefaces.push.http.ResponseHandler;
import org.icefaces.push.http.Server;
import org.icefaces.push.http.standard.NotFoundHandler;

import java.io.InputStream;


public class ServeCSSResource implements Server {
    private static final String Package = "com/icesoft/faces/resources/css/";
    private ClassLoader loader;
    private MimeTypeMatcher matcher;

    public ServeCSSResource(MimeTypeMatcher mimeTypeMatcher) {
        loader = this.getClass().getClassLoader();
        matcher = mimeTypeMatcher;
    }

    public void service(Request request) throws Exception {
        final String path = request.getURI().getPath();
        String file = path.substring(path.lastIndexOf("css/") + 4, path.length());
        final InputStream in = loader.getResourceAsStream(Package + file);

        if (in == null) {
            request.respondWith(new NotFoundHandler("Cannot find CSS file for " + path));
        } else {
            request.respondWith(new ResponseHandler() {
                public void respond(Response response) throws Exception {
                    response.setHeader("Content-Type", matcher.mimeTypeFor(path));
                    response.writeBodyFrom(in);
                }
            });
        }
    }

    public void shutdown() {
    }
}
