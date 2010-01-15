package com.icesoft.net.messaging;

import com.icesoft.util.Properties;

import edu.emory.mathcs.backport.java.util.concurrent.LinkedBlockingQueue;
import edu.emory.mathcs.backport.java.util.concurrent.ScheduledFuture;
import edu.emory.mathcs.backport.java.util.concurrent.ScheduledThreadPoolExecutor;
import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class QueueMessagePublisher
implements MessagePublisher {
    private static final Log LOG = LogFactory.getLog(QueueMessagePublisher.class);

    private static final Runnable NOOP =
        new Runnable() {
            public void run() {
                // do nothing...
            }
        };

    private final LinkedBlockingQueue messageQueue = new LinkedBlockingQueue();

    private final DefaultMessageService defaultMessageService;
    private final MessageServiceClient messageServiceClient;
    private final ScheduledFuture scheduledFuture;

    private boolean stopped = false;

    public QueueMessagePublisher(
        final DefaultMessageService defaultMessageService,
        final MessageServiceClient messageServiceClient,
        final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor) {

        this.defaultMessageService = defaultMessageService;
        this.messageServiceClient = messageServiceClient;
        this.scheduledFuture =
            scheduledThreadPoolExecutor.schedule(
                new Runnable() {
                    public void run() {
                        while (!stopped) {
                            try {
                                ((Runnable)messageQueue.take()).run();
                            } catch (InterruptedException exception) {
                                // todo: what to do?
                            }
                        }
                        messageQueue.clear();
                        scheduledFuture.cancel(false);
                    }
                },
                0,
                TimeUnit.MILLISECONDS);
    }

    public void publish(final Serializable objectMessage) {
        if (!stopped) {
            messageQueue.offer(
                new Runnable() {
                    public void run() {
                        messageServiceClient.publish(objectMessage);
                    }
                });
        }
    }

    public void publish(final Serializable objectMessage, final Properties messageProperties) {
        if (!stopped) {
            messageQueue.offer(
                new Runnable() {
                    public void run() {
                        messageServiceClient.publish(objectMessage, messageProperties);
                    }
                });
        }
    }

    public void publish(final Serializable objectMessage, final Properties messageProperties, final String messageType) {
        if (!stopped) {
            messageQueue.offer(
                new Runnable() {
                    public void run() {
                        messageServiceClient.publish(objectMessage, messageProperties, messageType);
                    }
                });
        }
    }

    public void publish(final Serializable objectMessage, final String messageType) {
        if (!stopped) {
            messageQueue.offer(
                new Runnable() {
                    public void run() {
                        messageServiceClient.publish(objectMessage, messageType);
                    }
                });
        }
    }

    public void publish(final String textMessage) {
        if (!stopped) {
            messageQueue.offer(
                new Runnable() {
                    public void run() {
                        messageServiceClient.publish(textMessage);
                    }
                });
        }
    }

    public void publish(final String textMessage, final Properties messageProperties) {
        if (!stopped) {
            messageQueue.offer(
                new Runnable() {
                    public void run() {
                        messageServiceClient.publish(textMessage, messageProperties);
                    }
                });
        }
    }

    public void publish(final String textMessage, final Properties messageProperties, final String messageType) {
        if (!stopped) {
            messageQueue.offer(
                new Runnable() {
                    public void run() {
                        messageServiceClient.publish(textMessage, messageProperties, messageType);
                    }
                });
        }
    }

    public void publish(final String textMessage, final String messageType) {
        if (!stopped) {
            messageQueue.offer(
                new Runnable() {
                    public void run() {
                        messageServiceClient.publish(textMessage, messageType);
                    }
                });
        }
    }

    public void publishNow(final Serializable objectMessage) {
        if (!stopped) {
            messageQueue.offer(
                new Runnable() {
                    public void run() {
                        try {
                            // throws MessageServiceException
                            messageServiceClient.publishNow(objectMessage);
                        } catch (MessageServiceException exception) {
                            if (defaultMessageService.reconnectNow()) {
                                run();
                            }
                        }
                    }
                });
        }
    }

    public void publishNow(final Serializable objectMessage, final Properties messageProperties) {
        if (!stopped) {
            messageQueue.offer(
                new Runnable() {
                    public void run() {
                        try {
                            // throws MessageServiceException
                            messageServiceClient.publishNow(objectMessage, messageProperties);
                        } catch (MessageServiceException exception) {
                            if (defaultMessageService.reconnectNow()) {
                                run();
                            }
                        }
                    }
                });
        }
    }

    public void publishNow(final Serializable objectMessage, final Properties messageProperties, final String messageType) {
        if (!stopped) {
            messageQueue.offer(
                new Runnable() {
                    public void run() {
                        try {
                            // throws MessageServiceException
                            messageServiceClient.publishNow(objectMessage, messageProperties, messageType);
                        } catch (MessageServiceException exception) {
                            if (defaultMessageService.reconnectNow()) {
                                run();
                            }
                        }
                    }
                });
        }
    }

    public void publishNow(final Serializable objectMessage, final String messageType) {
        if (!stopped) {
            messageQueue.offer(
                new Runnable() {
                    public void run() {
                        try {
                            // throws MessageServiceException
                            messageServiceClient.publishNow(objectMessage, messageType);
                        } catch (MessageServiceException exception) {
                            if (defaultMessageService.reconnectNow()) {
                                run();
                            }
                        }
                    }
                });
        }
    }

    public void publishNow(final String textMessage) {
        if (!stopped) {
            messageQueue.offer(
                new Runnable() {
                    public void run() {
                        try {
                            messageServiceClient.publishNow(textMessage);
                        } catch (MessageServiceException exception) {
                            if (defaultMessageService.reconnectNow()) {
                                run();
                            }
                        }
                    }
                });
        }
    }

    public void publishNow(final String textMessage, final Properties messageProperties) {
        if (!stopped) {
            messageQueue.offer(
                new Runnable() {
                    public void run() {
                        try {
                            // throws MessageServiceException
                            messageServiceClient.publishNow(textMessage, messageProperties);
                        } catch (MessageServiceException exception) {
                            if (defaultMessageService.reconnectNow()) {
                                run();
                            }
                        }
                    }
                });
        }
    }

    public void publishNow(final String textMessage, final Properties messageProperties, final String messageType) {
        if (!stopped) {
            messageQueue.offer(
                new Runnable() {
                    public void run() {
                        try {
                            // throws MessageServiceException
                            messageServiceClient.publishNow(textMessage, messageProperties, messageType);
                        } catch (MessageServiceException exception) {
                            if (defaultMessageService.reconnectNow()) {
                                run();
                            }
                        }
                    }
                });
        }
    }

    public void publishNow(final String textMessage, final String messageType) {
        if (!stopped) {
            messageQueue.offer(
                new Runnable() {
                    public void run() {
                        try {
                            messageServiceClient.publishNow(textMessage, messageType);
                        } catch (MessageServiceException exception) {
                            if (defaultMessageService.reconnectNow()) {
                                run();
                            }
                        }
                    }
                });
        }
    }

    public void stop() {
        stopped = true;
        messageQueue.offer(NOOP);
    }
}