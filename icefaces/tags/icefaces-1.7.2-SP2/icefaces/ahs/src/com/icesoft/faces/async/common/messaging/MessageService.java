package com.icesoft.faces.async.common.messaging;

import com.icesoft.net.messaging.MessageHandler;
import com.icesoft.net.messaging.MessageSelector;
import com.icesoft.net.messaging.MessageServiceAdapter;
import com.icesoft.net.messaging.MessageServiceClient;
import com.icesoft.net.messaging.MessageServiceException;
import com.icesoft.net.messaging.expression.Or;
import com.icesoft.net.messaging.jms.JMSAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.io.Serializable;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MessageService {
    private static final Log LOG = LogFactory.getLog(MessageService.class);

    private final Map messageHandlerMap = new HashMap();

    private MessageServiceClient messageServiceClient;

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

    private void setUpMessageServiceClient(
        final ServletContext servletContext) {

        MessageServiceAdapter _messageServiceAdapter =
            new JMSAdapter(servletContext);
        messageServiceClient =
            new MessageServiceClient(
                _messageServiceAdapter.getMessageServiceConfiguration(),
                _messageServiceAdapter,
                servletContext);
        // subscribing to icefaces contextEventTopic...
        MessageHandler _bufferedContextEventsMessageHandler =
            new BufferedContextEventsMessageHandler();
        MessageHandler _contextEventMessageHandler =
            new ContextEventMessageHandler();
        messageHandlerMap.put(
            BufferedContextEventsMessageHandler.Callback.class,
            _bufferedContextEventsMessageHandler);
        messageHandlerMap.put(
            ContextEventMessageHandler.Callback.class,
            _contextEventMessageHandler);
        subscribe(
            MessageServiceClient.CONTEXT_EVENT_TOPIC_NAME,
            new MessageSelector(
                new Or(
                    _bufferedContextEventsMessageHandler.getMessageSelector().
                        getExpression(),
                    _contextEventMessageHandler.getMessageSelector().
                        getExpression())));
        messageServiceClient.addMessageHandler(
            _bufferedContextEventsMessageHandler,
            MessageServiceClient.CONTEXT_EVENT_TOPIC_NAME);
        messageServiceClient.addMessageHandler(
            _contextEventMessageHandler,
            MessageServiceClient.CONTEXT_EVENT_TOPIC_NAME);
        // subscribing to icefaces responseTopic...
        MessageHandler _announcementMessageHandler =
            new AnnouncementMessageHandler();
        MessageHandler _purgeMessageHandler =
            new PurgeMessageHandler();
        MessageHandler _updatedViewsMessageHandler =
            new UpdatedViewsMessageHandler();
        messageHandlerMap.put(
            AnnouncementMessageHandler.Callback.class,
            _announcementMessageHandler);
        messageHandlerMap.put(
            PurgeMessageHandler.Callback.class,
            _purgeMessageHandler);
        messageHandlerMap.put(
            UpdatedViewsMessageHandler.Callback.class,
            _updatedViewsMessageHandler);
        subscribe(
            MessageServiceClient.RESPONSE_TOPIC_NAME,
            new MessageSelector(
                new Or(
                    _announcementMessageHandler.getMessageSelector().
                        getExpression(),
                    new Or(
                        _purgeMessageHandler.getMessageSelector().
                            getExpression(),
                        _updatedViewsMessageHandler.getMessageSelector().
                            getExpression()))));
        messageServiceClient.addMessageHandler(
            _announcementMessageHandler,
            MessageServiceClient.RESPONSE_TOPIC_NAME);
        messageServiceClient.addMessageHandler(
            _purgeMessageHandler,
            MessageServiceClient.RESPONSE_TOPIC_NAME);
        messageServiceClient.addMessageHandler(
            _updatedViewsMessageHandler,
            MessageServiceClient.RESPONSE_TOPIC_NAME);
    }

    private void subscribe(
        final String topicName, final MessageSelector messageSelector) {

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
        }
    }
}
