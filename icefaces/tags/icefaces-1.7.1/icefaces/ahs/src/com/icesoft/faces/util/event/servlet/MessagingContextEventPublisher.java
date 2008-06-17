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
package com.icesoft.faces.util.event.servlet;

import com.icesoft.net.messaging.MessageServiceClient;
import com.icesoft.net.messaging.MessageServiceException;
import com.icesoft.net.messaging.MessageServiceAdapter;
import com.icesoft.net.messaging.jms.JMSAdapter;
import com.icesoft.net.messaging.jms.JMSProviderConfiguration;
import com.icesoft.net.messaging.jms.JMSProviderConfigurationProperties;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MessagingContextEventPublisher
implements ContextEventPublisher {
    private static final String BUFFERED_CONTEXT_EVENTS_MESSAGE_TYPE =
        "BufferedContextEvents";
    private static final String CONTEXT_EVENT_MESSAGE_TYPE =
        "ContextEvent";
    private static final Log LOG =
        LogFactory.getLog(MessagingContextEventPublisher.class);

    private ContextEventRepeater contextEventRepeater;
    private MessageServiceClient messageServiceClient;

    private AnnouncementMessageHandler announcementMessageHandler =
        new AnnouncementMessageHandler() {
            public void publishBufferedContextEvents() {
                ContextEvent[] _contextEvents =
                    contextEventRepeater.getBufferedContextEvents();
                if (_contextEvents.length != 0) {
                    StringBuffer _message = new StringBuffer();
                    for (int i = 0; i < _contextEvents.length; i++) {
                        if (i != 0) {
                            _message.append("\r\n");
                        }
                        _message.append(createMessage(_contextEvents[i]));
                    }
                    publish(
                        _message.toString(),
                        BUFFERED_CONTEXT_EVENTS_MESSAGE_TYPE);
                }
            }
        };

    public MessagingContextEventPublisher() {
        // do nothing.
    }

    public void publish(final ContextEvent event)
    throws Exception {
        if (event instanceof ContextInitializedEvent) {
            ServletContext _servletContext =
                ((ContextInitializedEvent)event).getNestedServletContextEvent().
                    getServletContext();
            setUpMessageServiceClient(_servletContext);
        } else {
            publish(createMessage(event), CONTEXT_EVENT_MESSAGE_TYPE);
            if (event instanceof ContextDestroyedEvent) {
                tearDownMessageServiceClient();
            }
        }
    }

    public void setContextEventRepeater(
        final ContextEventRepeater contextEventRepeater) {

        this.contextEventRepeater = contextEventRepeater;
    }

    private String createMessage(final ContextEvent event) {
        if (event instanceof ContextDestroyedEvent) {
            return "ContextDestroyed";
        } else if (event instanceof ICEfacesIDDisposedEvent) {
            return
                "ICEfacesIDDisposed;" +
                    ((ICEfacesIDDisposedEvent)event).getICEfacesID();
        } else if (event instanceof ICEfacesIDRetrievedEvent) {
            return
                "ICEfacesIDRetrieved;" +
                    ((ICEfacesIDRetrievedEvent)event).getICEfacesID();
        } else if (event instanceof SessionDestroyedEvent) {
            return
                "SessionDestroyed;" +
                    ((SessionDestroyedEvent)event).getICEfacesID();
        } else if (event instanceof ViewNumberRetrievedEvent) {
            return
                "ViewNumberRetrieved;" +
                    ((ViewNumberRetrievedEvent)event).getICEfacesID() + ";" +
                    ((ViewNumberRetrievedEvent)event).getViewNumber();
        } else {
            return null;
        }
    }

    private void publish(final String message, final String messageType) {
        if (message != null) {
            messageServiceClient.publish(
                message,
                messageType,
                MessageServiceClient.CONTEXT_EVENT_TOPIC_NAME);
        }
    }

    private void setUpMessageServiceClient(final ServletContext servletContext)
    throws MessageServiceException {
        MessageServiceAdapter _messMessageServiceAdapter =
            new JMSAdapter(servletContext);
        messageServiceClient =
            new MessageServiceClient(
                _messMessageServiceAdapter.getMessageServiceConfiguration(),
                _messMessageServiceAdapter,
                servletContext);
        try {
            messageServiceClient.subscribe(
                MessageServiceClient.CONTEXT_EVENT_TOPIC_NAME,
                announcementMessageHandler.getMessageSelector());
        } catch (MessageServiceException exception) {
            if (LOG.isFatalEnabled()) {
                LOG.fatal(
                    "\r\n" +
                    "\r\n" +
                    "Failed to subscribe to topic: " +
                        MessageServiceClient.CONTEXT_EVENT_TOPIC_NAME + "\r\n" +
                    "    Exception message: " +
                        exception.getMessage() + "\r\n" +
                    "    Exception cause: " +
                        exception.getCause() + "\r\n\r\n" +
                    "The icefaces-ahs.jar is included in the deployment, but " +
                        "the JMS topics are not\r\n" +
                    "configured correctly on the application server. If you " +
                        "intended to use the\r\n" +
                    "Asynchronous HTTP Server (AHS), please refer to the " +
                        "ICEfaces Developer's Guide\r\n" +
                    "for instructions on how to configure the JMS topics on " +
                        "the application server.\r\n" +
                    "If you did not intend to use AHS, please remove the " +
                        "icefaces-ahs.jar from your\r\n" +
                    "deployment and try again.\r\n");
            }
            throw exception;
        }
        messageServiceClient.addMessageHandler(
            announcementMessageHandler,
            MessageServiceClient.CONTEXT_EVENT_TOPIC_NAME);
        try {
            messageServiceClient.start();
        } catch (MessageServiceException exception) {
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
        messageServiceClient.removeMessageHandler(
            announcementMessageHandler,
            MessageServiceClient.CONTEXT_EVENT_TOPIC_NAME);
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
