package com.icesoft.faces.push.server;

import com.icesoft.net.messaging.MessageHandler;
import com.icesoft.net.messaging.MessageSelector;
import com.icesoft.net.messaging.MessageServiceAdapter;
import com.icesoft.net.messaging.MessageServiceClient;
import com.icesoft.net.messaging.MessageServiceException;
import com.icesoft.net.messaging.AbstractMessageHandler;
import com.icesoft.net.messaging.Message;
import com.icesoft.net.messaging.TextMessage;
import com.icesoft.net.messaging.expression.Or;
import com.icesoft.net.messaging.expression.Equal;
import com.icesoft.net.messaging.expression.Identifier;
import com.icesoft.net.messaging.expression.StringLiteral;
import com.icesoft.net.messaging.jms.JMSAdapter;
import com.icesoft.util.Properties;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.icefaces.push.server.BufferedContextEventsMessageHandler;
import org.icefaces.push.server.ContextEventMessageHandler;
import org.icefaces.push.server.UpdatedViewsMessageHandler;

public class MessageService
extends org.icefaces.push.server.MessageService {
    private static final Log LOG = LogFactory.getLog(MessageService.class);

    public MessageService(final ServletContext servletContext) {
        super(servletContext);
    }

    protected void setUpMessageServiceClient(
        final ServletContext servletContext) {

        MessageServiceAdapter _messageServiceAdapter =
            new JMSAdapter(servletContext);
        messageServiceClient =
            new MessageServiceClient(
                _messageServiceAdapter.getMessageServiceConfiguration(),
                _messageServiceAdapter,
                servletContext);
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
        MessageHandler _purgeMessageHandler =
            new PurgeMessageHandler();
        MessageHandler _updatedViewsMessageHandler =
            new UpdatedViewsMessageHandler();
        messageHandlerMap.put(
            BufferedContextEventsMessageHandler.Callback.class,
            _bufferedContextEventsMessageHandler);
        messageHandlerMap.put(
            ContextEventMessageHandler.Callback.class,
            _contextEventMessageHandler);
        messageHandlerMap.put(
            PurgeMessageHandler.Callback.class,
            _purgeMessageHandler);
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
                                new Or(
                                    _purgeMessageHandler.
                                        getMessageSelector().getExpression(),
                                    _updatedViewsMessageHandler.
                                        getMessageSelector().
                                            getExpression()))))));
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
            _purgeMessageHandler,
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
        messageServiceClient.removeMessageHandler(
            (MessageHandler)
                messageHandlerMap.get(
                    PurgeMessageHandler.Callback.class),
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
}
