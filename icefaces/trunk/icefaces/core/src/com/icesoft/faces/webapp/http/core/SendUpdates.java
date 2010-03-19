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

import com.icesoft.faces.webapp.command.Command;
import com.icesoft.faces.webapp.command.CommandQueue;
import com.icesoft.faces.webapp.command.NOOP;
import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.common.standard.FixedXMLContentHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class SendUpdates implements Server {
    private static final Log LOG = LogFactory.getLog(SendUpdates.class);
    private static final Command NOOP = new NOOP();
    private Map commandQueues;
    private PageTest pageTest;
    private static boolean debugDOMUpdate;

    public SendUpdates(final Configuration configuration, final Map commandQueues, final PageTest pageTest) {
        this.commandQueues = commandQueues;
        this.pageTest = pageTest;
        debugDOMUpdate = configuration
                .getAttributeAsBoolean("debugDOMUpdate", false);
    }

    public void service(final Request request) throws Exception {
        if (!pageTest.isLoaded()) {
            request.respondWith(new ReloadResponse(""));
        } else {
            request.respondWith(new Handler(commandQueues, request));
        }
    }

    public void shutdown() {
    }

    public static class Handler extends FixedXMLContentHandler {
        private final Request request;
        private Map commandQueues;

        public Handler(Map commandQueues, Request request) {
            this.commandQueues = commandQueues;
            this.request = request;
        }

        public void writeTo(Writer writer) throws IOException {
            String viewIdentifier = request.getParameter("ice.view");
            if (ViewIdVerifier.isValid(viewIdentifier) && commandQueues.containsKey(viewIdentifier)) {
                CommandQueue queue = (CommandQueue) commandQueues.get(viewIdentifier);
                Command command = queue.take();
                if (SendUpdates.debugDOMUpdate) {
                    //logging can be problematic in different server
                    //environments
                    System.out.println(command);
                }
                if (LOG.isTraceEnabled()) {
                    LOG.trace(command);
                }
                command.serializeTo(writer);
            } else {
                NOOP.serializeTo(writer);
            }
        }
    }
}
