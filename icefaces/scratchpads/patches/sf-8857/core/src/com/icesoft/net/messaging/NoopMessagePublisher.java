package com.icesoft.net.messaging;

import com.icesoft.util.Properties;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NoopMessagePublisher
implements MessagePublisher {
    private static final Log LOG = LogFactory.getLog(NoopMessagePublisher.class);

    public NoopMessagePublisher() {
        // do nothing...
    }                                                     

    public void publish(final Serializable objectMessage) {}

    public void publish(final Serializable objectMessage, final Properties messageProperties) {}

    public void publish(final Serializable objectMessage, final Properties messageProperties, final String messageType) {}

    public void publish(final Serializable objectMessage, final String messageType) {}

    public void publish(final String textMessage) {}

    public void publish(final String textMessage, final Properties messageProperties) {}

    public void publish(final String textMessage, final Properties messageProperties, final String messageType) {}

    public void publish(final String textMessage, final String messageType) {}

    public void publishNow(final Serializable objectMessage) {}

    public void publishNow(final Serializable objectMessage, final Properties messageProperties) {}

    public void publishNow(final Serializable objectMessage, final Properties messageProperties, final String messageType) {}

    public void publishNow(final Serializable objectMessage, final String messageType) {}

    public void publishNow(final String textMessage) {}

    public void publishNow(final String textMessage, final Properties messageProperties) {}

    public void publishNow(final String textMessage, final Properties messageProperties, final String messageType) {}

    public void publishNow(final String textMessage, final String messageType) {}

    public void stop() {}
}
