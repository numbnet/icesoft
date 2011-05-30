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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
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

package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.webapp.http.common.*;
import com.icesoft.faces.webapp.http.common.standard.CacheControlledServer;
import com.icesoft.faces.webapp.http.common.standard.CompressingServer;
import com.icesoft.faces.webapp.http.common.standard.PathDispatcherServer;
import com.icesoft.faces.webapp.http.servlet.NotFoundServer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

public class ResourceServer implements Server {
    private static final Log log = LogFactory.getLog(ResourceServer.class);
    private Server dispatcher;

    public ResourceServer(Configuration configuration, MimeTypeMatcher mimeTypeMatcher, FileLocator fileLocator) {
        PathDispatcherServer pathDispatcher = new PathDispatcherServer();
        pathDispatcher.dispatchOn(".*xmlhttp\\/ice-static\\/.*", new CacheControlledServer(new ServeStaticResource(mimeTypeMatcher)));
        pathDispatcher.dispatchOn(".*xmlhttp\\/javascript-blocked$", new RedirectOnJSBlocked(configuration));
        pathDispatcher.dispatchOn(".*xmlhttp\\/.*\\/.*\\.js$", new CacheControlledServer(new ServeJSCode()));
        pathDispatcher.dispatchOn(".*xmlhttp\\/css\\/.*", new CacheControlledServer(new ServeCSSResource(mimeTypeMatcher)));
        pathDispatcher.dispatchOn(".*xmlhttp\\/blank$", new CacheControlledServer(new ServeBlankPage()));
        pathDispatcher.dispatchOn(".*xmlhttp\\/wait\\-cursor$", new CacheControlledServer(new WaitCursorPage()));
        pathDispatcher.dispatchOn(".*xmlhttp\\/", new NotFoundServer());
        //match any path that does not point to WEB-INF directory
        pathDispatcher.dispatchOn("^(?!.*WEB\\-INF.*).*$", new FileServer(fileLocator, mimeTypeMatcher));
        dispatcher = new CompressingServer(pathDispatcher, mimeTypeMatcher, configuration);
    }

    public void service(Request request) throws Exception {
        try {
            dispatcher.service(request);
        } catch (IOException e) {
            //capture & log Tomcat specific exception            
            if (e.getClass().getName().endsWith("ClientAbortException")) {
                log.debug("Browser closed the connection prematurely for " + request.getURI());
            } else {
                throw e;
            }
        }
    }

    public void shutdown() {
        dispatcher.shutdown();
    }
}
