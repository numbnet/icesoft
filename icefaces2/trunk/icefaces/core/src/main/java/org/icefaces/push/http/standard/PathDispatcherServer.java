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

package org.icefaces.push.http.standard;

import org.icefaces.push.http.Request;
import org.icefaces.push.http.Server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Pattern;

public class PathDispatcherServer implements Server {
    private List matchers = new ArrayList();
    private List servers = new ArrayList();

    public void service(Request request) throws Exception {
        String path = request.getURI().getPath();
        ListIterator i = matchers.listIterator();
        while (i.hasNext()) {
            int index = i.nextIndex();
            Pattern pattern = (Pattern) i.next();
            if (pattern.matcher(path).find()) {
                Server server = (Server) servers.get(index);
                server.service(request);
                return;
            }
        }

        request.respondWith(new NotFoundHandler("Could not find resource at " + path));
    }

    public void dispatchOn(String pathExpression, final Server toServer) {
        matchers.add(Pattern.compile(pathExpression));
        servers.add(toServer);
    }

    public void shutdown() {
        Iterator i = servers.iterator();
        while (i.hasNext()) {
            Server server = (Server) i.next();
            server.shutdown();
        }
    }
}
