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
package org.icefaces.push.server;

import com.icesoft.net.messaging.AbstractMessageHandler;
import com.icesoft.net.messaging.Message;
import com.icesoft.net.messaging.MessageHandler;
import com.icesoft.net.messaging.MessageSelector;
import com.icesoft.net.messaging.MessageServiceClient;
import com.icesoft.net.messaging.MessageServiceException;
import com.icesoft.net.messaging.TextMessage;
import com.icesoft.net.messaging.expression.Equal;
import com.icesoft.net.messaging.expression.Identifier;
import com.icesoft.net.messaging.expression.Or;
import com.icesoft.net.messaging.expression.StringLiteral;
import com.icesoft.net.messaging.http.HttpAdapter;
import com.icesoft.util.Properties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MessageService {
    private static final Log LOG = LogFactory.getLog(MessageService.class);

    protected final Map messageHandlerMap = new HashMap();

    protected MessageServiceClient messageServiceClient;

    public MessageService(final ServletContext servletContext) {
        setUpMessageServiceClient(servletContext);
    }

    public void close() {
        try {
            messageServiceClient.close();
        } catch (MessageServiceException exception) {
            if (LOG.isFatalEnabled()) {
                LOG.fatal(
                    "Failed to close the message service client!", exception);
            }
        }
    }

    public MessageServiceClient getMessageServiceClient() {
        return messageServiceClient;
    }

    public void publish(
        final Serializable object, final Properties messageProperties,
        final String topicName) {

        messageServiceClient.publish(object, messageProperties, topicName);
    }

    public void publish(
        final Serializable object, final Properties messageProperties,
        final String messageType, final String topicName) {

        messageServiceClient.publish(
            object, messageProperties, messageType, topicName);
    }

    public void publish(final Serializable object, final String topicName) {
        messageServiceClient.publish(object, topicName);
    }

    public void publish(
        final String text, final String messageType, final String topicName) {

        messageServiceClient.publish(text, messageType, topicName);
    }

    public void publish(
        final String text, final Properties messageProperties,
        final String topicName) {

        messageServiceClient.publish(text, messageProperties, topicName);
    }

    public void publish(
        final String text, final Properties messageProperties,
        final String messageType, final String topicName) {

        messageServiceClient.publish(
            text, messageProperties, messageType, topicName);
    }

    public void publish(final String text, final String topicName) {
        messageServiceClient.publish(text, topicName);
    }

    public void publish(
        final Serializable object, final String messageType,
        final String topicName) {

        messageServiceClient.publish(object, messageType, topicName);
    }

    public void setCallback(
        final Class callbackClass, final MessageHandler.Callback callback) {

        if (messageHandlerMap.containsKey(callbackClass)) {
            ((MessageHandler)messageHandlerMap.get(callbackClass)).
                setCallback(callback);
        }
    }

    public void start() {
        if (messageServiceClient != null) {
            try {
                messageServiceClient.start();
            } catch (MessageServiceException exception) {
                if (LOG.isFatalEnabled()) {
                    LOG.fatal("Failed to start message delivery!", exception);
                }
            }
        }
    }

    public void stop() {
        if (messageServiceClient != null) {
            try {
                messageServiceClient.stop();
            } catch (MessageServiceException exception) {
                if (LOG.isFatalEnabled()) {
                    LOG.fatal("Failed to stop message delivery!", exception);
                }
            }
        }
    }

    protected void setUpMessageServiceClient(
        final ServletContext servletContext) {

        messageServiceClient =
            new MessageServiceClient(
                new HttpAdapter(servletContext), servletContext);
        MessageHandler _helloMessageHandler =
            new AbstractMessageHandler(
                new MessageSelector(
                    new Equal(
                        new Identifier(Message.MESSAGE_TYPE),
                        new StringLiteral("Presence")))) {

                public void handle(Message message) {
                    if (message instanceof TextMessage) {
                        if (((TextMessage)message).getText().equals("Hello")) {
                            Properties _messageProperties = new Properties();
                            _messageProperties.setStringProperty(
                                Message.DESTINATION_SERVLET_CONTEXT_PATH,
                                message.getStringProperty(
                                    Message.SOURCE_SERVLET_CONTEXT_PATH));
                            messageServiceClient.publish(
                                new StringBuffer().
                                    append("Acknowledge").append(";").
                                    append(ProductInfo.PRODUCT).append(";").
                                    append(ProductInfo.PRIMARY).append(";").
                                    append(ProductInfo.SECONDARY).append(";").
                                    append(ProductInfo.TERTIARY).append(";").
                                    append(ProductInfo.RELEASE_TYPE).append(";").
                                    append(ProductInfo.BUILD_NO).append(";").
                                    append(ProductInfo.REVISION).
                                        toString(),
                                _messageProperties,
                                "Presence",
                                MessageServiceClient.PUSH_TOPIC_NAME);
                        }
                    }
                }
            };
        MessageHandler _bufferedContextEventsMessageHandler =
            new BufferedContextEventsMessageHandler();
        MessageHandler _contextEventMessageHandler =
            new ContextEventMessageHandler();
        MessageHandler _updatedViewsMessageHandler =
            new UpdatedViewsMessageHandler();
        messageHandlerMap.put(
            BufferedContextEventsMessageHandler.Callback.class,
            _bufferedContextEventsMessageHandler);
        messageHandlerMap.put(
            ContextEventMessageHandler.Callback.class,
            _contextEventMessageHandler);
        messageHandlerMap.put(
            UpdatedViewsMessageHandler.Callback.class,
            _updatedViewsMessageHandler);
        try {
            subscribe(
                MessageServiceClient.PUSH_TOPIC_NAME,
                new MessageSelector(
                    new Or(
                        _helloMessageHandler.
                            getMessageSelector().getExpression(),
                        new Or(
                            _bufferedContextEventsMessageHandler.
                                getMessageSelector().getExpression(),
                            new Or(
                                _contextEventMessageHandler.
                                    getMessageSelector().getExpression(),
                                _updatedViewsMessageHandler.
                                    getMessageSelector().getExpression())))));
        } catch (MessageServiceException exception) {
            tearDownMessageServiceClient();
            return;
        }
        messageServiceClient.addMessageHandler(
            _helloMessageHandler,
            MessageServiceClient.PUSH_TOPIC_NAME);
        messageServiceClient.addMessageHandler(
            _bufferedContextEventsMessageHandler,
            MessageServiceClient.PUSH_TOPIC_NAME);
        messageServiceClient.addMessageHandler(
            _contextEventMessageHandler,
            MessageServiceClient.PUSH_TOPIC_NAME);
        messageServiceClient.addMessageHandler(
            _updatedViewsMessageHandler,
            MessageServiceClient.PUSH_TOPIC_NAME);
    }

    protected void tearDownMessageServiceClient() {
        stop();
        messageServiceClient.removeMessageHandler(
            (MessageHandler)
                messageHandlerMap.get(
                    UpdatedViewsMessageHandler.Callback.class),
            MessageServiceClient.PUSH_TOPIC_NAME);
        try {
            messageServiceClient.unsubscribe(
                MessageServiceClient.PUSH_TOPIC_NAME);
        } catch (MessageServiceException exception) {
            // ignore exception.
        }
        messageServiceClient.removeMessageHandler(
            (MessageHandler)
                messageHandlerMap.get(
                    ContextEventMessageHandler.Callback.class),
            MessageServiceClient.PUSH_TOPIC_NAME);
        messageServiceClient.removeMessageHandler(
            (MessageHandler)
                messageHandlerMap.get(
                    BufferedContextEventsMessageHandler.Callback.class),
            MessageServiceClient.PUSH_TOPIC_NAME);
        try {
            messageServiceClient.unsubscribe(
                MessageServiceClient.PUSH_TOPIC_NAME);
        } catch (MessageServiceException exception) {
            // ignore exception.
        }
        messageHandlerMap.clear();
        close();
        messageServiceClient = null;
    }

    protected void subscribe(
        final String topicName, final MessageSelector messageSelector)
    throws MessageServiceException {
        try {
            messageServiceClient.subscribe(topicName, messageSelector);
        } catch (MessageServiceException exception) {
            if (LOG.isFatalEnabled()) {
                LOG.fatal(
                    "\r\n" +
                    "\r\n" +
                    "Failed to subscribe to topic: " + topicName + "\r\n" +
                    "    Exception message: " +
                        exception.getMessage() + "\r\n" +
                    "    Exception cause: " +
                        exception.getCause() + "\r\n");
            }
            throw exception;
        }
    }
}
