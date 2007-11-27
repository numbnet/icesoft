package com.icesoft.faces.async.common.messaging;

import com.icesoft.util.net.messaging.MessageHandler;
import com.icesoft.util.net.messaging.MessageSelector;
import com.icesoft.util.net.messaging.MessageServiceClient;
import com.icesoft.util.net.messaging.MessageServiceException;
import com.icesoft.util.net.messaging.expression.Or;
import com.icesoft.util.net.messaging.jms.JMSAdapter;
import com.icesoft.util.net.messaging.jms.JMSProviderConfiguration;
import com.icesoft.util.net.messaging.jms.JMSProviderConfigurationProperties;

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

        JMSProviderConfiguration _jmsProviderConfiguration =
            new JMSProviderConfigurationProperties(servletContext);
        messageServiceClient =
            new MessageServiceClient(
                _jmsProviderConfiguration,
                new JMSAdapter(_jmsProviderConfiguration),
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
                    "Failed to subscribe to topic: " + topicName, exception);
            }
        }
    }
}
