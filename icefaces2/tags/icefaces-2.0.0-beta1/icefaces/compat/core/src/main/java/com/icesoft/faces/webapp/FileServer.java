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

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

public class FileServer implements Server {
    private FileLocator locator;
    private MimeTypeMatcher mimeTypeMatcher;

    public FileServer(FileLocator locator, MimeTypeMatcher mimeTypeMatcher) {
        this.locator = locator;
        this.mimeTypeMatcher = mimeTypeMatcher;
    }

    public void service(Request request) throws Exception {
        final String path = request.getURI().getPath();
        final File file = locator.locate(path);
        if (file.exists()) {
            request.respondWith(new ResponseHandler() {
                public void respond(Response response) throws Exception {
                    String mimeType = mimeTypeMatcher.mimeTypeFor(path);
                    Date lastModified = new Date(file.lastModified());

                    response.setHeader("Content-Type", mimeType);
                    response.setHeader("Last-Modified", lastModified);
                    response.writeBodyFrom(new FileInputStream(file));
                }
            });
        } else {
            request.respondWith(new NotFoundHandler("Could not find file at " + path));
        }
    }

    public void shutdown() {
    }
}
