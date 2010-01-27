package com.icesoft.net.messaging;

import com.icesoft.util.Properties;

import java.io.Serializable;

public interface MessagePublisher {
    void publish(Serializable objectMessage);

    void publish(Serializable objectMessage, Properties messageProperties);

    void publish(Serializable objectMessage, Properties messageProperties, String messageType);

    void publish(Serializable objectMessage, String messageType);

    void publish(String textMessage);

    void publish(String textMessage, Properties messageProperties);

    void publish(String textMessage, Properties messageProperties, String messageType);

    void publish(String textMessage, String messageType);

    void publishNow(Serializable objectMessage);

    void publishNow(Serializable objectMessage, Properties messageProperties);

    void publishNow(Serializable objectMessage, Properties messageProperties, String messageType);

    void publishNow(Serializable objectMessage, String messageType);

    void publishNow(String textMessage);

    void publishNow(String textMessage, Properties messageProperties);

    void publishNow(String textMessage, Properties messageProperties, String messageType);

    void publishNow(String textMessage, String messageType);
    
    void stop();
}
