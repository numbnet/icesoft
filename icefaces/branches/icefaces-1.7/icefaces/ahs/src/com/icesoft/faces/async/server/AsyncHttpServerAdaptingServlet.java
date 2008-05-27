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
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
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
package com.icesoft.faces.async.server;

// todo: handle updated views queue exceeded messages!
//import com.icesoft.faces.async.common.messaging.UpdatedViewsQueueExceededMessageHandler;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.common.standard.StreamingContentHandler;
import com.icesoft.faces.webapp.http.core.ViewQueue;
import com.icesoft.net.messaging.MessageServiceAdapter;
import com.icesoft.net.messaging.MessageServiceClient;
import com.icesoft.net.messaging.MessageServiceException;
import com.icesoft.net.messaging.jms.JMSAdapter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;

public class AsyncHttpServerAdaptingServlet
implements Server {
    private static final String UPDATED_VIEWS_MESSAGE_TYPE = "UpdatedViews";
    private static final Log LOG =
        LogFactory.getLog(AsyncHttpServerAdaptingServlet.class);

    private static final Object messageServiceClientLock = new Object();
    private static MessageServiceClient messageServiceClient;

    private long sequenceNumber;
    // todo: implement this!
//    private boolean updatedViewsQueueExceeded = false;

    // todo: handle updated views queue exceeded messages!
//    private UpdatedViewsQueueExceededMessageHandler
//        updatedViewsQueueExceededMessageHandler =
//            new UpdatedViewsQueueExceededMessageHandler() {
//                protected void updatedViewsQueueExceeded(
//                    final String iceFacesId) {
//
//                    if (iceFacesId == null || iceFacesId.trim().length() == 0) {
//                        return;
//                    }
//                    // todo: implement this!
//                }
//            };

    public AsyncHttpServerAdaptingServlet(
        final String iceFacesId, final Collection synchronouslyUpdatedViews,
        final ViewQueue allUpdatedViews, final ServletContext servletContext)
    throws MessageServiceException {
        synchronized (messageServiceClientLock) {
            if (messageServiceClient == null) {
                setUpMessageClientService(servletContext);
            }
        }
        allUpdatedViews.onPut(
            new Runnable() {
                public void run() {
                    allUpdatedViews.removeAll(synchronouslyUpdatedViews);
                    synchronouslyUpdatedViews.clear();
                    Set _viewIdentifierSet = new HashSet(allUpdatedViews);
                    if (!_viewIdentifierSet.isEmpty()) {
                        String[] _viewIdentifiers =
                            (String[])
                                _viewIdentifierSet.toArray(
                                    new String[_viewIdentifierSet.size()]);
                        StringWriter _stringWriter = new StringWriter();
                        for (int i = 0; i < _viewIdentifiers.length; i++) {
                            if (i != 0) {
                                _stringWriter.write(',');
                            }
                            _stringWriter.write(_viewIdentifiers[i]);
                        }
                        messageServiceClient.publish(
                            iceFacesId + ";" +                    // ICEfaces ID
                                ++sequenceNumber + ";" +      // Sequence Number
                                _stringWriter.toString(),        // Message Body
                            UPDATED_VIEWS_MESSAGE_TYPE,
                            MessageServiceClient.RESPONSE_TOPIC_NAME);
                    }
                }
            });
    }

    public void service(final Request request)
    throws Exception {
        request.respondWith(new StreamingContentHandler("text/plain", "UTF-8") {
            public void writeTo(Writer writer) throws IOException {
                writer.write("Asynchronous HTTP Server is enabled.\n");
                writer.write("Check your server configuration.\n");
            }
        });
    }

    public void shutdown() {
        tearDownMessageServiceClient();
    }

    private void setUpMessageClientService(final ServletContext servletContext)
    throws MessageServiceException {
        MessageServiceAdapter _messageServiceAdapter =
            new JMSAdapter(servletContext);
        messageServiceClient =
            new MessageServiceClient(
                _messageServiceAdapter.getMessageServiceConfiguration(),
                _messageServiceAdapter,
                servletContext);
        // todo: handle updated views queue exceeded messages!
//        try {
//            messageServiceClient.subscribe(
//                MessageServiceClient.RESPONSE_TOPIC_NAME,
//                updatedViewsQueueExceededMessageHandler.getMessageSelector());
//        } catch (MessageServiceException exception) {
//            if (LOG.isFatalEnabled()) {
//                messageServiceClient = null;
//                LOG.fatal(
//                    "\r\n" +
//                    "\r\n" +
//                    "Failed to subscribe to topic: " +
//                        MessageServiceClient.RESPONSE_TOPIC_NAME + "\r\n" +
//                    "    Exception message: " +
//                        exception.getMessage() + "\r\n" +
//                    "    Exception cause: " +
//                        exception.getCause() + "\r\n\r\n" +
//                    "The icefaces-ahs.jar is included in the deployment, but " +
//                        "the JMS topics are not\r\n" +
//                    "configured correctly on the application server. If you " +
//                        "intended to use the\r\n" +
//                    "Asynchronous HTTP Server (AHS), please refer to the " +
//                        "ICEfaces Developer's Guide\r\n" +
//                    "for instructions on how to configure the JMS topics on " +
//                        "the application server.\r\n" +
//                    "If you did not intend to use AHS, please remove the " +
//                        "icefaces-ahs.jar from your\r\n" +
//                    "deployment and try again.\r\n");
//            }
//            throw exception;
//        }
//        messageServiceClient.addMessageHandler(
//            updatedViewsQueueExceededMessageHandler,
//            MessageServiceClient.RESPONSE_TOPIC_NAME);
        try {
            messageServiceClient.start();
        } catch (MessageServiceException exception) {
            messageServiceClient = null;
            if (LOG.isFatalEnabled()) {
                LOG.fatal("Failed to start message delivery!", exception);
            }
            throw exception;
        }
    }

    private void tearDownMessageServiceClient() {
        try {
            messageServiceClient.stop();
        } catch (MessageServiceException exception) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Failed to stop message delivery!", exception);
            }
        }
        // todo: handle updated views queue exceeded messages!
//        messageServiceClient.removeMessageHandler(
//            updatedViewsQueueExceededMessageHandler,
//            MessageServiceClient.RESPONSE_TOPIC_NAME);
        try {
            messageServiceClient.close();
        } catch (MessageServiceException exception) {
            if (LOG.isErrorEnabled()) {
                LOG.error(
                    "Failed to close connection due to some internal error!",
                    exception);
            }
        }
    }
}
