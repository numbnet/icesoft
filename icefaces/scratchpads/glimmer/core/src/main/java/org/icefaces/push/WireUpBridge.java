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
 * 2004-2009 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
*/

package org.icefaces.push;

import org.icefaces.push.http.AbstractServer;
import org.icefaces.push.http.Request;
import org.icefaces.push.http.Response;
import org.icefaces.push.http.standard.NoCacheContentHandler;

import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;

public class WireUpBridge extends AbstractServer {
    private CurrentContext context;
    private HttpSession session;

    public WireUpBridge(CurrentContext context, HttpSession session) {
        this.context = context;
        this.session = session;
    }

    public void service(Request request) throws Exception {
        request.respondWith(new NoCacheContentHandler("text/javascript", "UTF-8") {
            public void respond(Response response) throws Exception {
                super.respond(response);
                response.writeBodyFrom(new ByteArrayInputStream(
                        ("ice.onLoad(function() {try {" +
                                "document.body.bridge = ice.Application({" +
                                "blockUI: false," +
                                "session: '" + session.getId() + "'," +
                                "connection: {" +
                                "heartbeat: {}," +
                                "context: {current: '" + context.getPath() + "/',async: '" + context.getPath() + "/'}" +
                                "}," +
                                "messages: {sessionExpired: 'User Session Expired',connectionLost: 'Network Connection Interrupted',serverError: 'Server Internal Error',description: 'To reconnect click the Reload button on the browser or click the button below',buttonText: 'Reload'}}," +
                                "document.body); } catch (e) {alert(e);};" +
                                "});").getBytes("UTF-8")
                ));
            }
        });
    }
}