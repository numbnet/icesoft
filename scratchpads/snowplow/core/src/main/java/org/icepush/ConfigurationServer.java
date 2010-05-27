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

package org.icepush;

import org.icepush.http.Request;
import org.icepush.http.Server;
import org.icepush.http.standard.FixedXMLContentHandler;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

public class ConfigurationServer extends FixedXMLContentHandler implements Server {
    private static final Logger log = Logger.getLogger(ConfigurationServer.class.getName());
    private static final int defaultServerErrorRetries = 3;
    private static final int defaultBlockingConnectionTimeout = 10000;
    private static final String defaultFileExtension = "";

    private Server blockingConnectionServer;
    private String configurationMessage;

    public ConfigurationServer(final ServletContext servletContext, Configuration configuration, final Server server) {
        blockingConnectionServer = server;
        String contextPath = configuration.getAttribute("contextPath", (String) servletContext.getAttribute("contextPath"));
        long blockingConnectionTimeout = configuration.getAttributeAsLong("blockingConnectionTimeout", defaultBlockingConnectionTimeout);
        int serverErrorRetries = configuration.getAttributeAsInteger("serverErrorRetryTimeouts", defaultServerErrorRetries);
        String fileExtension = configuration.getAttribute("fileExtension", defaultFileExtension);
        configurationMessage =
                "<configuration" +
                        (blockingConnectionTimeout != defaultBlockingConnectionTimeout ?
                                " heartbeatTimeout=\"" + blockingConnectionTimeout + "\"" : "") +
                        (serverErrorRetries != defaultServerErrorRetries ?
                                " serverErrorRetryTimeouts=\"" + serverErrorRetries + "\"" : "") +
                        (!fileExtension.equals(defaultFileExtension) ?
                                " fileExtension=\"" + fileExtension + "\"" : "") +
                        (contextPath != null ?
                                " contextPath=\"" + contextPath + "\"" : "") +
                        "/>";

    }

    public void service(Request request) throws Exception {
        if (!request.containsParameter("ice.sendConfiguration") || configurationMessage.length() == "<configuration/>".length()) {
            blockingConnectionServer.service(request);
            log.fine("Re-configured bridge.");
        } else {
            request.respondWith(this);
        }
    }

    public void writeTo(Writer writer) throws IOException {
        writer.write(configurationMessage);
    }

    public void shutdown() {
        blockingConnectionServer.shutdown();
    }
}