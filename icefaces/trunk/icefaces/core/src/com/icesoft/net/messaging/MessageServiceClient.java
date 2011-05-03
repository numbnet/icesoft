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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
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
package com.icesoft.net.messaging;

import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.servlet.ServletContextConfiguration;
import com.icesoft.util.ServerUtility;
import com.icesoft.util.ThreadFactory;

import edu.emory.mathcs.backport.java.util.concurrent.ScheduledThreadPoolExecutor;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MessageServiceClient {
    private static final Log LOG =
        LogFactory.getLog(MessageServiceClient.class);

    private static final int DEFAULT_MESSAGE_MAX_LENGTH = 4 * 1024;
    private static final int DEFAULT_THREAD_POOL_SIZE = 15;
    private static final int DEFAULT_MESSAGE_MAX_DELAY = 100;

    private final Map messageHandlerMap = new HashMap();
    private final Map messagePipelineMap = new HashMap();

    private Administrator administrator;
    private Properties baseMessageProperties = new Properties();
    private String defaultTopicName;
    private MessageServiceConfiguration messageServiceConfiguration;
    private MessageServiceAdapter messageServiceAdapter;
    private String name;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    public MessageServiceClient(
        final MessageServiceAdapter messageServiceAdapter,
        final ServletContext servletContext)
    throws IllegalArgumentException {
        this(null, messageServiceAdapter, servletContext, null);
    }

    public MessageServiceClient(
        final MessageServiceAdapter messageServiceAdapter,
        final ServletContext servletContext, final String servletContextPath)
    throws IllegalArgumentException {
        this(null, messageServiceAdapter, servletContext, servletContextPath);
    }

    public MessageServiceClient(
        final String name, final MessageServiceAdapter messageServiceAdapter,
        final ServletContext servletContext)
    throws IllegalArgumentException {
        this(name, messageServiceAdapter, servletContext, null);
    }

    public MessageServiceClient(
        final String name, final MessageServiceAdapter messageServiceAdapter,
        final ServletContext servletContext, final String servletContextPath)
    throws IllegalArgumentException {
        if (messageServiceAdapter == null) {
            throw new IllegalArgumentException("messageServiceAdapter is null");
        }
        if (servletContext == null) {
            throw new IllegalArgumentException("servletContext is null");
        }
        this.name = name;
        this.messageServiceAdapter = messageServiceAdapter;
        this.messageServiceAdapter.setMessageServiceClient(this);
        Configuration _servletContextConfiguration =
            new ServletContextConfiguration(
                "com.icesoft.net.messaging", servletContext);
        messageServiceConfiguration =
            new MessageServiceConfigurationProperties();
        messageServiceConfiguration.setMessageMaxDelay(
            _servletContextConfiguration.getAttributeAsInteger(
                "messageMaxDelay", DEFAULT_MESSAGE_MAX_DELAY));
        messageServiceConfiguration.setMessageMaxLength(
            _servletContextConfiguration.getAttributeAsInteger(
                "messageMaxLength", DEFAULT_MESSAGE_MAX_LENGTH));
        if (servletContextPath != null) {
            setBaseMessageProperties(servletContextPath);
        } else {
            setBaseMessageProperties(servletContext);
        }
        ThreadFactory _threadFactory = new ThreadFactory();
        _threadFactory.setPrefix("MSC Thread");
        scheduledThreadPoolExecutor =
            new ScheduledThreadPoolExecutor(
                _servletContextConfiguration.getAttributeAsInteger(
                    "threadPoolSize", DEFAULT_THREAD_POOL_SIZE),
                _threadFactory);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Message Service Client - Thread Pool: " + scheduledThreadPoolExecutor.getCorePoolSize());
        }
        defaultTopicName =
            _servletContextConfiguration.getAttribute(
                "defaultTopicName", "icefacesPush");
    }

    /**
     * <p>
     *   Adds the specified <code>messageHandler</code> to this MessageServiceClient for the default topic.
     * </p>
     *
     * @param      messageHandler
     *                 the MessageHandler to be added.
     * @see        MessageServiceAdapter#addMessageHandler(MessageHandler, String)
     * @see        #removeMessageHandler(MessageHandler, String)
     */
    public void addMessageHandler(final MessageHandler messageHandler) {
        addMessageHandler(messageHandler, getDefaultTopicName());
    }

    /**
     * <p>
     *   Adds the specified <code>messageHandler</code> to this
     *   MessageServiceClient for the topic with the specified
     *   <code>topicName</code>.
     * </p>
     *
     * @param      messageHandler
     *                 the MessageHandler to be added.
     * @param      topicName
     *                 the topic name of the topic to be associated with the
     *                 MessageHandler.
     * @see        MessageServiceAdapter#addMessageHandler(MessageHandler, String)
     * @see        #removeMessageHandler(MessageHandler, String)
     */
    public void addMessageHandler(
        final MessageHandler messageHandler, final String topicName) {

        if (messageHandler != null &&
            topicName != null && topicName.trim().length() != 0) {

            Map _messageHandlerWrapperMap;
            if (messageHandlerMap.containsKey(topicName)) {
                _messageHandlerWrapperMap =
                    (Map)messageHandlerMap.get(topicName);
                if (_messageHandlerWrapperMap.containsKey(messageHandler)) {
                    return;
                }
            } else {
                _messageHandlerWrapperMap = new HashMap();
                messageHandlerMap.put(topicName, _messageHandlerWrapperMap);
            }
            MessageSeparator _messageSeparator;
            if (!(messageHandler instanceof MessageSeparator)) {
                _messageSeparator = new MessageSeparator(messageHandler);
            } else {
                _messageSeparator = (MessageSeparator)messageHandler;
            }
            _messageHandlerWrapperMap.put(messageHandler, _messageSeparator);
            messageServiceAdapter.addMessageHandler(
                _messageSeparator, topicName);
        }
    }

    /**
     * <p>
     *   Closes this MessageServiceClient's connection to the message service
     *   and disposes of all relevant resources.
     * </p>
     *
     * @throws     MessageServiceException
     *                 if the message service fails to close the connection due
     *                 to some internal error.
     * @see        MessageServiceAdapter#close()
     * @see        #stop()
     */
    public void close()
    throws MessageServiceException {
        scheduledThreadPoolExecutor.shutdownNow();
        try {
            messageServiceAdapter.close();
        } finally {
            messageHandlerMap.clear();
        }
    }

    /**
     * <p>
     *   Gets the MessageServiceAdapter of this MessageServiceClient.
     * </p>
     *
     * @return     the MessageServiceAdapter.
     */
    public MessageServiceAdapter getMessageServiceAdapter() {
        return messageServiceAdapter;
    }

    /**
     * <p>
     *   Gets the MessageServiceConfiguration of this MessageServiceClient.
     * </p>
     *
     * @return     the MessageServiceConfiguration.
     */
    public MessageServiceConfiguration getMessageServiceConfiguration() {
        return messageServiceConfiguration;
    }

    public Administrator getAdministrator() {
        return administrator;
    }

    public String getDefaultTopicName() {
        return defaultTopicName;
    }

    /**
     * <p>
     *   Gets the name of this MessageServiceClient.
     * </p>
     *
     * @return     the name.
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     *   Gets the topic names of the topics on which this MessageServiceClient
     *   is a publisher.
     * </p>
     *
     * @return     the topic names.
     * @see        MessageServiceAdapter#getPublisherTopicNames()
     * @see        #getSubscriberTopicNames()
     */
    public String[] getPublisherTopicNames() {
        return messageServiceAdapter.getPublisherTopicNames();
    }

    public ScheduledThreadPoolExecutor getScheduledThreadPoolExecutor() {
        return scheduledThreadPoolExecutor;
    }

    /**
     * <p>
     *   Gets the topic names of the topics to which this MessageServiceClient
     *   is a subscriber.
     * </p>
     *
     * @return     the topic names.
     * @see        MessageServiceAdapter#getSubscriberTopicNames()
     * @see        #getPublisherTopicNames()
     */
    public String[] getSubscriberTopicNames() {
        return messageServiceAdapter.getSubscriberTopicNames();
    }

    /**
     * <p>
     *   Checks to see if this MessageServiceClient is publishing on the topic
     *   with the specified <code>topicName</code>.
     * </p>
     *
     * @param      topicName
     *                 the topic name to be checked.
     * @return     <code>true</code> if publishing on the topic with the
     *             specified <code>topicName</code>, <code>false</code> if not.
     * @see        MessageServiceAdapter#isPublishingOn(String)
     * @see        #isSubscribedTo(String)
     */
    public boolean isPublishingOn(final String topicName) {
        return
            topicName != null && topicName.trim().length() != 0 &&
            messageServiceAdapter.isPublishingOn(topicName);
    }

    /**
     * <p>
     *   Checks to see if this MessageServiceClient is subscribed to the topic
     *   with the specified <code>topicName</code>.
     * </p>
     *
     * @param      topicName
     *                 the topic name to be checked.
     * @return     <code>true</code> if subscribed to the topic with the
     *             specified <code>topicName</code>, <code>false</code> if not.
     * @see        MessageServiceAdapter#isSubscribedTo(String)
     * @see        #isPublishingOn(String)
     */
    public boolean isSubscribedTo(final String topicName) {
        return
            topicName != null && topicName.trim().length() != 0 &&
            messageServiceAdapter.isSubscribedTo(topicName);
    }

    /**
     * <p>
     *   Publishes the specified <code>object</code> to the default topic. That is, the <code>object</code> will be put
     *   into a MessagePipeline before actually being published as an ObjectMessage to the message service.
     * </p>
     * <p>
     *   If this MessageServiceClient is not a publisher on the default topic yet, it will automatically add itself as a
     *   publisher.
     * </p>
     *
     * @param      object
     *                 the object to be published.
     * @see        #publishTo(String, Serializable)
     * @see        #publish(Serializable, Properties)
     * @see        #publish(Serializable, Properties, String)
     * @see        #publish(Serializable, String)
     * @see        ObjectMessage
     * @see        MessagePipeline
     */
    public void publish(final Serializable object) {
        publishTo(getDefaultTopicName(), object);
    }

    /**
     * <p>
     *   Publishes the specified <code>object</code> to the default topic. That is, the <code>object</code> will be put
     *   into a MessagePipeline before actually being published as an ObjectMessage, with the specified
     *   <code>messageProperties</code>, to the message service.
     * </p>
     * <p>
     *   If this MessageServiceClient is not a publisher on the default topic yet, it will automatically add itself as a
     *   publisher.
     * </p>
     *
     * @param      object
     *                 the object to be published.
     * @param      messageProperties
     *                 the message properties for the ObjectMessage.
     * @see        #publishTo(String, Serializable, Properties)
     * @see        #publish(Serializable)
     * @see        #publish(Serializable, Properties, String)
     * @see        #publish(Serializable, String)
     * @see        ObjectMessage
     * @see        MessagePipeline
     */
    public void publish(final Serializable object, final Properties messageProperties) {
        publishTo(getDefaultTopicName(), object, messageProperties);
    }

    /**
     * <p>
     *   Publishes the specified <code>object</code> to the default topic. That is, the <code>object</code> will be put
     *   into a MessagePipeline before actually being published as an ObjectMessage, with the specified
     *   <code>messageProperties</code> and <code>messageType</code>, to the message service.
     * </p>
     * <p>
     *   If this MessageServiceClient is not a publisher on the default topic yet, it will automatically add itself as a
     *   publisher.
     * </p>
     *
     * @param      object
     *                 the object to be published.
     * @param      messageProperties
     *                 the message properties for the ObjectMessage.
     * @param      messageType
     *                 the message type for the ObjectMessage.
     * @see        #publishTo(String, Serializable, Properties, String)
     * @see        #publish(Serializable)
     * @see        #publish(Serializable, Properties)
     * @see        #publish(Serializable, String)
     * @see        ObjectMessage
     * @see        MessagePipeline
     */
    public void publish(final Serializable object, final Properties messageProperties, final String messageType) {
        publishTo(getDefaultTopicName(), object, messageProperties, messageType);
    }

    /**
     * <p>
     *   Publishes the specified <code>object</code> to the default topic. That is, the <code>object</code> will be put
     *   into a MessagePipeline before actually being published as an ObjectMessage, with the specified
     *   <code>messageType</code>, to the message service.
     * </p>
     * <p>
     *   If this MessageServiceClient is not a publisher on the default topic yet, it will automatically add itself as a
     *   publisher.
     * </p>
     *
     * @param      object
     *                 the object to be published.
     * @param      messageType
     *                 the message type for the ObjectMessage.
     * @see        #publishTo(String, Serializable, String)
     * @see        #publish(Serializable)
     * @see        #publish(Serializable, Properties)
     * @see        #publish(Serializable, Properties, String)
     * @see        ObjectMessage
     * @see        MessagePipeline
     */
    public void publish(final Serializable object, final String messageType) {
        publishTo(getDefaultTopicName(), object, messageType);
    }

    /**
     * <p>
     *   Publishes the specified <code>text</code> to the default topic. That is, the <code>text</code> will be put into
     *   a MessagePipeline before actually being published as a TextMessage to the message service.
     * </p>
     * <p>
     *   If this MessageServiceClient is not a publisher on the default topic yet, it will automatically add itself as a
     *   publisher.
     * </p>
     *
     * @param      text
     *                 the text to be published.
     * @see        #publishTo(String, Serializable)
     * @see        #publish(String, Properties)
     * @see        #publish(String, Properties, String)
     * @see        #publish(String, String)
     * @see        ObjectMessage
     * @see        MessagePipeline
     */
    public void publish(final String text) {
        publishTo(getDefaultTopicName(), text);
    }

    /**
     * <p>
     *   Publishes the specified <code>text</code> to the default topic. That is, the <code>text</code> will be put into
     *   a MessagePipeline before actually being published as an TextMessage, with the specified
     *   <code>messageProperties</code>, to the message service.
     * </p>
     * <p>
     *   If this MessageServiceClient is not a publisher on the default topic yet, it will automatically add itself as a
     *   publisher.
     * </p>
     *
     * @param      text
     *                 the text to be published.
     * @param      messageProperties
     *                 the message properties for the ObjectMessage.
     * @see        #publishTo(String, Serializable, Properties)
     * @see        #publish(String)
     * @see        #publish(String, Properties, String)
     * @see        #publish(String, String)
     * @see        ObjectMessage
     * @see        MessagePipeline
     */
    public void publish(final String text, final Properties messageProperties) {
        publishTo(getDefaultTopicName(), text, messageProperties);
    }

    /**
     * <p>
     *   Publishes the specified <code>text</code> to the default topic. That is, the <code>text</code> will be put into
     *   a MessagePipeline before actually being published as an TextMessage, with the specified
     *   <code>messageProperties</code> and <code>messageType</code>, to the message service.
     * </p>
     * <p>
     *   If this MessageServiceClient is not a publisher on the default topic yet, it will automatically add itself as a
     *   publisher.
     * </p>
     *
     * @param      text
     *                 the text to be published.
     * @param      messageProperties
     *                 the message properties for the ObjectMessage.
     * @param      messageType
     *                 the message type for the ObjectMessage.
     * @see        #publishTo(String, Serializable, Properties, String)
     * @see        #publish(String)
     * @see        #publish(String, Properties)
     * @see        #publish(String, String)
     * @see        ObjectMessage
     * @see        MessagePipeline
     */
    public void publish(final String text, final Properties messageProperties, final String messageType) {
        publishTo(getDefaultTopicName(), text, messageProperties, messageType);
    }

    /**
     * <p>
     *   Publishes the specified <code>text</code> to the default topic. That is, the <code>text</code> will be put into
     *   a MessagePipeline before actually being published as an TextMessage, with the specified
     *   <code>messageType</code>, to the message service.
     * </p>
     * <p>
     *   If this MessageServiceClient is not a publisher on the default topic yet, it will automatically add itself as a
     *   publisher.
     * </p>
     *
     * @param      text
     *                 the text to be published.
     * @param      messageType
     *                 the message type for the ObjectMessage.
     * @see        #publishTo(String, Serializable, String)
     * @see        #publish(String)
     * @see        #publish(String, Properties)
     * @see        #publish(String, Properties, String)
     * @see        ObjectMessage
     * @see        MessagePipeline
     */
    public void publish(final String text, final String messageType) {
        publishTo(getDefaultTopicName(), text, messageType);
    }

    /**
     * <p>
     *   Publishes the specified <code>object</code> to the default topic. That is, the <code>object</code> will be
     *   published immediately as an ObjectMessage to the message service.
     * </p>
     * <p>
     *   If this MessageServiceClient is not a publisher on the default topic yet, it will automatically add itself as a
     *   publisher.
     * </p>
     *
     * @param      object
     *                 the object to be published.
     * @throws     MessageServiceException
     *                 if the publishing of the message failed.
     * @see        #publishNowTo(String, Serializable)
     * @see        #publishNow(Serializable, Properties)
     * @see        #publishNow(Serializable, Properties, String)
     * @see        #publishNow(Serializable, String)
     * @see        ObjectMessage
     * @see        MessagePipeline
     */
    public void publishNow(final Serializable object)
    throws MessageServiceException {
        publishNowTo(getDefaultTopicName(), object);
    }

    /**
     * <p>
     *   Publishes the specified <code>object</code> to the default topic. That is, the <code>object</code> will be
     *   published immediately as an ObjectMessage, with the specified <code>messageProperties</code>, to the message
     *   service.
     * </p>
     * <p>
     *   If this MessageServiceClient is not a publisher on the default topic yet, it will automatically add itself as a
     *   publisher.
     * </p>
     *
     * @param      object
     *                 the object to be published.
     * @param      messageProperties
     *                 the message properties for the ObjectMessage.
     * @throws     MessageServiceException
     *                 if the publishing of the message failed.
     * @see        #publishNowTo(String, Serializable, Properties)
     * @see        #publishNow(Serializable)
     * @see        #publishNow(Serializable, Properties, String)
     * @see        #publishNow(Serializable, String)
     * @see        ObjectMessage
     * @see        MessagePipeline
     */
    public void publishNow(final Serializable object, final Properties messageProperties)
    throws MessageServiceException {
        publishNowTo(getDefaultTopicName(), object, messageProperties);
    }

    /**
     * <p>
     *   Publishes the specified <code>object</code> to the default topic. That is, the <code>object</code> will be
     *   published immediately as an ObjectMessage, with the specified <code>messageProperties</code> and
     *   <code>messageType</code>, to the message service.
     * </p>
     * <p>
     *   If this MessageServiceClient is not a publisher on the default topic yet, it will automatically add itself as a
     *   publisher.
     * </p>
     *
     * @param      object
     *                 the object to be published.
     * @param      messageProperties
     *                 the message properties for the ObjectMessage.
     * @param      messageType
     *                 the message type for the ObjectMessage.
     * @throws     MessageServiceException
     *                 if the publishing of the message failed.
     * @see        #publishNowTo(String, Serializable, Properties, String)
     * @see        #publishNow(Serializable)
     * @see        #publishNow(Serializable, Properties)
     * @see        #publishNow(Serializable, String)
     * @see        ObjectMessage
     * @see        MessagePipeline
     */
    public void publishNow(final Serializable object, final Properties messageProperties, final String messageType)
    throws MessageServiceException {
        publishNowTo(getDefaultTopicName(), object, messageProperties, messageType);
    }

    /**
     * <p>
     *   Publishes the specified <code>object</code> to the default topic. That is, the <code>object</code> will be
     *   published immediately as an ObjectMessage, with the specified <code>messageType</code>, to the message service.
     * </p>
     * <p>
     *   If this MessageServiceClient is not a publisher on the default topic yet, it will automatically add itself as a
     *   publisher.
     * </p>
     *
     * @param      object
     *                 the object to be published.
     * @param      messageType
     *                 the message type for the ObjectMessage.
     * @throws     MessageServiceException
     *                 if the publishing of the message failed.
     * @see        #publishNowTo(String, Serializable, String)
     * @see        #publishNow(Serializable)
     * @see        #publishNow(Serializable, Properties)
     * @see        #publishNow(Serializable, Properties, String)
     * @see        ObjectMessage
     * @see        MessagePipeline
     */
    public void publishNow(final Serializable object, final String messageType)
    throws MessageServiceException {
        publishNowTo(getDefaultTopicName(), object, messageType);
    }

    /**
     * <p>
     *   Publishes the specified <code>text</code> to the default topic. That is, the <code>text</code> will be
     *   published immediately as a TextMessage to the message service.
     * </p>
     * <p>
     *   If this MessageServiceClient is not a publisher on the default topic yet, it will automatically add itself as a
     *   publisher.
     * </p>
     *
     * @param      text
     *                 the text to be published.
     * @throws     MessageServiceException
     *                 if the publishing of the message failed.
     * @see        #publishNowTo(String, String)
     * @see        #publishNow(String, Properties)
     * @see        #publishNow(String, Properties, String)
     * @see        #publishNow(String, String)
     * @see        TextMessage
     * @see        MessagePipeline
     */
    public void publishNow(final String text)
    throws MessageServiceException {
        publishNowTo(getDefaultTopicName(), text);
    }

    /**
     * <p>
     *   Publishes the specified <code>text</code> to the default topic. That is, the <code>text</code> will be
     *   published immediately as a TextMessage, with the specified <code>messageProperties</code>, to the message
     *   service.
     * </p>
     * <p>
     *   If this MessageServiceClient is not a publisher on the default topic yet, it will automatically add itself as a
     *   publisher.
     * </p>
     *
     * @param      text
     *                 the text to be published.
     * @param      messageProperties
     *                 the message properties for the TextMessage.
     * @throws     MessageServiceException
     *                 if the publishing of the message failed.
     * @see        #publishNowTo(String, String, Properties)
     * @see        #publishNow(String)
     * @see        #publishNow(String, Properties, String)
     * @see        #publishNow(String, String)
     * @see        TextMessage
     * @see        MessagePipeline
     */
    public void publishNow(final String text, final Properties messageProperties)
    throws MessageServiceException {
        publishNowTo(getDefaultTopicName(), text, messageProperties);
    }

    /**
     * <p>
     *   Publishes the specified <code>text</code> to the default topic. That is, the <code>text</code> will be
     *   published immediately as a TextMessage, with the specified <code>messageProperties</code> and
     *   <code>messageType</code>, to the message service.
     * </p>
     * <p>
     *   If this MessageServiceClient is not a publisher on the default topic yet, it will automatically add itself as a
     *   publisher.
     * </p>
     *
     * @param      text
     *                 the text to be published.
     * @param      messageProperties
     *                 the message properties for the TextMessage.
     * @param      messageType
     *                 the message type for the TextMessage.
     * @throws     MessageServiceException
     *                 if the publishing of the message failed.
     * @see        #publishNowTo(String, String, Properties, String)
     * @see        #publishNow(String)
     * @see        #publishNow(String, Properties)
     * @see        #publishNow(String, String)
     * @see        TextMessage
     * @see        MessagePipeline
     */
    public void publishNow(final String text, final Properties messageProperties, final String messageType)
    throws MessageServiceException {
        publishNowTo(getDefaultTopicName(), text, messageProperties, messageType);
    }

    /**
     * <p>
     *   Publishes the specified <code>text</code> to the default topic. That is, the <code>text</code> will be
     *   published immediately as a TextMessage, with the specified <code>messageType</code>, to the message service.
     * </p>
     * <p>
     *   If this MessageServiceClient is not a publisher on the default topic yet, it will automatically add itself as a
     *   publisher.
     * </p>
     *
     * @param      text
     *                 the text to be published.
     * @param      messageType
     *                 the message type for the TextMessage.
     * @throws     MessageServiceException
     *                 if the publishing of the message failed.
     * @see        #publishNowTo(String, String, String)
     * @see        #publishNow(String)
     * @see        #publishNow(String, Properties)
     * @see        #publishNow(String, Properties, String)
     * @see        TextMessage
     * @see        MessagePipeline
     */
    public void publishNow(final String text, final String messageType)
    throws MessageServiceException {
        publishNowTo(getDefaultTopicName(), text, messageType);
    }

    /**
     * <p>
     *   Publishes the specified <code>object</code> to the topic with the specified <code>topicName</code>. That is,
     *   the <code>object</code> will be published immediately as an ObjectMessage to the message service.
     * </p>
     * <p>
     *   If this MessageServiceClient is not a publisher on the topic with the <code>topicName</code> yet, it will
     *   automatically add itself as a publisher.
     * </p>
     *
     * @param      topicName
     *                 the topic name of the topic to publish on.
     * @param      object
     *                 the object to be published.
     * @throws     MessageServiceException
     *                 if the publishing of the message failed.
     * @see        #publishNowTo(String, Serializable, Properties)
     * @see        #publishNowTo(String, Serializable, Properties, String)
     * @see        #publishNowTo(String, Serializable, String)
     * @see        ObjectMessage
     * @see        MessagePipeline
     */
    public void publishNowTo(final String topicName, final Serializable object)
    throws MessageServiceException {
        publishNowTo(topicName, object, (Properties)null);
    }

    /**
     * <p>
     *   Publishes the specified <code>object</code> to the topic with the specified <code>topicName</code>. That is,
     *   the <code>object</code> will be published immediately as an ObjectMessage, with the specified
     *   <code>messageProperties</code>, to the message service.
     * </p>
     * <p>
     *   If this MessageServiceClient is not a publisher on the topic with the <code>topicName</code> yet, it will
     *   automatically add itself as a publisher.
     * </p>
     *
     * @param      topicName
     *                 the topic name of the topic to publish on.
     * @param      object
     *                 the object to be published.
     * @param      messageProperties
     *                 the message properties for the ObjectMessage.
     * @throws     MessageServiceException
     *                 if the publishing of the message failed.
     * @see        #publishNowTo(String, Serializable)
     * @see        #publishNowTo(String, Serializable, Properties, String)
     * @see        #publishNowTo(String, Serializable, String)
     * @see        ObjectMessage
     * @see        MessagePipeline
     */
    public void publishNowTo(final String topicName, final Serializable object, final Properties messageProperties)
    throws MessageServiceException {
        if (topicName != null && topicName.trim().length() != 0 && object != null) {
            publishNowTo(topicName, createMessage(object, baseMessageProperties, messageProperties));
        }
    }

    /**
     * <p>
     *   Publishes the specified <code>object</code> to the topic with the specified <code>topicName</code>. That is,
     *   the <code>object</code> will be published immediately as an ObjectMessage, with the specified
     *   <code>messageProperties</code> and <code>messageType</code>, to the message service.
     * </p>
     * <p>
     *   If this MessageServiceClient is not a publisher on the topic with the <code>topicName</code> yet, it will
     *   automatically add itself as a publisher.
     * </p>
     *
     * @param      topicName
     *                 the topic name of the topic to publish on.
     * @param      object
     *                 the object to be published.
     * @param      messageProperties
     *                 the message properties for the ObjectMessage.
     * @param      messageType
     *                 the message type for the ObjectMessage.
     * @throws     MessageServiceException
     *                 if the publishing of the message failed.
     * @see        #publishNowTo(String, Serializable)
     * @see        #publishNowTo(String, Serializable, Properties)
     * @see        #publishNowTo(String, Serializable, String)
     * @see        ObjectMessage
     * @see        MessagePipeline
     */
    public void publishNowTo(
        final String topicName, final Serializable object, final Properties messageProperties, final String messageType)
    throws MessageServiceException {
        publishNowTo(topicName, object, getMessageProperties(messageProperties, messageType));
    }

    /**
     * <p>
     *   Publishes the specified <code>object</code> to the topic with the specified <code>topicName</code>. That is,
     *   the <code>object</code> will be published immediately as an ObjectMessage, with the specified
     *   <code>messageType</code>, to the message service.
     * </p>
     * <p>
     *   If this MessageServiceClient is not a publisher on the topic with the <code>topicName</code> yet, it will
     *   automatically add itself as a publisher.
     * </p>
     *
     * @param      topicName
     *                 the topic name of the topic to publish on.
     * @param      object
     *                 the object to be published.
     * @param      messageType
     *                 the message type for the ObjectMessage.
     * @throws     MessageServiceException
     *                 if the publishing of the message failed.
     * @see        #publishNowTo(String, Serializable)
     * @see        #publishNowTo(String, Serializable, Properties)
     * @see        #publishNowTo(String, Serializable, Properties, String)
     * @see        ObjectMessage
     * @see        MessagePipeline
     */
    public void publishNowTo(final String topicName, final Serializable object, final String messageType)
    throws MessageServiceException {
        publishNowTo(topicName, object, getMessageProperties(null, messageType));
    }

    /**
     * <p>
     *   Publishes the specified <code>text</code> to the topic with the specified <code>topicName</code>. That is, the
     *   <code>text</code> will be published immediately as a TextMessage to the message service.
     * </p>
     * <p>
     *   If this MessageServiceClient is not a publisher on the topic with the <code>topicName</code> yet, it will
     *   automatically add itself as a publisher.
     * </p>
     *
     * @param      topicName
     *                 the topic name of the topic to publish on.
     * @param      text
     *                 the text to be published.
     * @throws     MessageServiceException
     *                 if the publishing of the message failed.
     * @see        #publishNowTo(String, String, Properties)
     * @see        #publishNowTo(String, String, Properties, String)
     * @see        #publishNowTo(String, String, String)
     * @see        TextMessage
     * @see        MessagePipeline
     */
    public void publishNowTo(final String topicName, final String text)
    throws MessageServiceException {
        publishNowTo(topicName, text, (Properties)null);
    }

    /**
     * <p>
     *   Publishes the specified <code>text</code> to the topic with the specified <code>topicName</code>. That is, the
     *   <code>text</code> will be published immediately as a TextMessage, with the specified
     *   <code>messageProperties</code>, to the message service.
     * </p>
     * <p>
     *   If this MessageServiceClient is not a publisher on the topic with the <code>topicName</code> yet, it will
     *   automatically add itself as a publisher.
     * </p>
     *
     * @param      topicName
     *                 the topic name of the topic to publish on.
     * @param      text
     *                 the text to be published.
     * @param      messageProperties
     *                 the message properties for the TextMessage.
     * @throws     MessageServiceException
     *                 if the publishing of the message failed.
     * @see        #publishNowTo(String, String)
     * @see        #publishNowTo(String, String, Properties, String)
     * @see        #publishNowTo(String, String, String)
     * @see        TextMessage
     * @see        MessagePipeline
     */
    public void publishNowTo(final String topicName, final String text, final Properties messageProperties)
    throws MessageServiceException {
        if (topicName != null && topicName.trim().length() != 0 && text != null && text.trim().length() != 0) {
            publishNowTo(topicName, createMessage(text, baseMessageProperties, messageProperties));
        }
    }

    /**
     * <p>
     *   Publishes the specified <code>text</code> to the topic with the specified <code>topicName</code>. That is, the
     *   <code>text</code> will be published immediately as a TextMessage, with the specified
     *   <code>messageProperties</code> and <code>messageType</code>, to the message service.
     * </p>
     * <p>
     *   If this MessageServiceClient is not a publisher on the topic with the <code>topicName</code> yet, it will
     *   automatically add itself as a publisher.
     * </p>
     *
     * @param      topicName
     *                 the topic name of the topic to publish on.
     * @param      text
     *                 the text to be published.
     * @param      messageProperties
     *                 the message properties for the TextMessage.
     * @param      messageType
     *                 the message type for the TextMessage.
     * @throws     MessageServiceException
     *                 if the publishing of the message failed.
     * @see        #publishNowTo(String, String)
     * @see        #publishNowTo(String, String, Properties)
     * @see        #publishNowTo(String, String, String)
     * @see        TextMessage
     * @see        MessagePipeline
     */
    public void publishNowTo(
        final String topicName, final String text, final Properties messageProperties, final String messageType)
    throws MessageServiceException {
        publishNowTo(topicName, text, getMessageProperties(messageProperties, messageType));
    }

    /**
     * <p>
     *   Publishes the specified <code>text</code> to the topic with the specified <code>topicName</code>. That is, the
     *   <code>text</code> will be published immediately as a TextMessage, with the specified <code>messageType</code>,
     *   to the message service.
     * </p>
     * <p>
     *   If this MessageServiceClient is not a publisher on the topic with the <code>topicName</code> yet, it will
     *   automatically add itself as a publisher.
     * </p>
     *
     * @param      topicName
     *                 the topic name of the topic to publish on.
     * @param      text
     *                 the text to be published.
     * @param      messageType
     *                 the message type for the TextMessage.
     * @throws     MessageServiceException
     *                 if the publishing of the message failed.
     * @see        #publishNowTo(String, String)
     * @see        #publishNowTo(String, String, Properties)
     * @see        #publishNowTo(String, String, Properties, String)
     * @see        TextMessage
     * @see        MessagePipeline
     */
    public void publishNowTo(final String topicName, final String text, final String messageType)
    throws MessageServiceException {
        publishNowTo(topicName, text, getMessageProperties(null, messageType));
    }

    /**
     * <p>
     *   Publishes the specified <code>object</code> to the topic with the specified <code>topicName</code>. That is,
     *   the <code>object</code> will be put into a MessagePipeline before actually being published as an ObjectMessage
     *   to the message service.
     * </p>
     * <p>
     *   If this MessageServiceClient is not a publisher on the topic with the <code>topicName</code> yet, it will
     *   automatically add itself as a publisher.
     * </p>
     *
     * @param      topicName
     *                 the topic name of the topic to publish on.
     * @param      object
     *                 the object to be published.
     * @see        #publishTo(String, Serializable, Properties)
     * @see        #publishTo(String, Serializable, Properties, String)
     * @see        #publishTo(String, Serializable, String)
     * @see        ObjectMessage
     * @see        MessagePipeline
     */
    public void publishTo(final String topicName, final Serializable object) {
        publishTo(topicName, object, (Properties)null);
    }

    /**
     * <p>
     *   Publishes the specified <code>object</code> to the topic with the specified <code>topicName</code>. That is,
     *   the <code>object</code> will be put into a MessagePipeline before actually being published as an ObjectMessage,
     *   with the specified <code>messageProperties</code>, to the message service.
     * </p>
     * <p>
     *   If this MessageServiceClient is not a publisher on the topic with the <code>topicName</code> yet, it will
     *   automatically add itself as a publisher.
     * </p>
     *
     * @param      topicName
     *                 the topic name of the topic to publish on.
     * @param      object
     *                 the object to be published.
     * @param      messageProperties
     *                 the message properties for the ObjectMessage.
     * @see        #publishTo(String, Serializable)
     * @see        #publishTo(String, Serializable, Properties, String)
     * @see        #publishTo(String, Serializable, String)
     * @see        ObjectMessage
     * @see        MessagePipeline
     */
    public void publishTo(final String topicName, final Serializable object, final Properties messageProperties) {
        if (topicName != null && topicName.trim().length() != 0 && object != null) {
            publishTo(topicName, createMessage(object, baseMessageProperties, messageProperties));
        }
    }

    /**
     * <p>
     *   Publishes the specified <code>object</code> to the topic with the specified <code>topicName</code>. That is,
     *   the <code>object</code> will be put into a MessagePipeline before actually being published as an ObjectMessage,
     *   with the specified <code>messageProperties</code> and <code>messageType</code>, to the message service.
     * </p>
     * <p>
     *   If this MessageServiceClient is not a publisher on the topic with the <code>topicName</code> yet, it will
     *   automatically add itself as a publisher.
     * </p>
     *
     * @param      topicName
     *                 the topic name of the topic to publish on.
     * @param      object
     *                 the object to be published.
     * @param      messageProperties
     *                 the message properties for the ObjectMessage.
     * @param      messageType
     *                 the message type for the ObjectMessage.
     * @see        #publishTo(String, Serializable)
     * @see        #publishTo(String, Serializable, Properties)
     * @see        #publishTo(String, Serializable, String)
     * @see        ObjectMessage
     * @see        MessagePipeline
     */
    public void publishTo(
        final String topicName, final Serializable object, final Properties messageProperties,
        final String messageType) {

        publishTo(topicName, object, getMessageProperties(messageProperties, messageType));
    }

    /**
     * <p>
     *   Publishes the specified <code>object</code> to the topic with the specified <code>topicName</code>. That is,
     *   the <code>object</code> will be put into a MessagePipeline before actually being published as an ObjectMessage,
     *   with the specified <code>messageType</code>, to the message service.
     * </p>
     * <p>
     *   If this MessageServiceClient is not a publisher on the topic with the <code>topicName</code> yet, it will
     *   automatically add itself as a publisher.
     * </p>
     *
     * @param      topicName
     *                 the topic name of the topic to publish on.
     * @param      object
     *                 the object to be published.
     * @param      messageType
     *                 the message type for the ObjectMessage.
     * @see        #publishTo(String, Serializable, Properties)
     * @see        #publishTo(String, Serializable, Properties, String)
     * @see        #publishTo(String, Serializable)
     * @see        ObjectMessage
     * @see        MessagePipeline
     */
    public void publishTo(final String topicName, final Serializable object, final String messageType) {
        publishTo(topicName, object, getMessageProperties(null, messageType));
    }

    /**
     * <p>
     *   Publishes the specified <code>text</code> to the topic with the specified <code>topicName</code>. That is, the
     *   <code>text</code> will be put into a MessagePipeline before actually being published as a TextMessage to the
     *   message service.
     * </p>
     * <p>
     *   If this MessageServiceClient is not a publisher on the topic with the <code>topicName</code> yet, it will
     *   automatically add itself as a publisher.
     * </p>
     *
     * @param      topicName
     *                 the topic name of the topic to publish on.
     * @param      text
     *                 the text to be published.
     * @see        #publishTo(String, String, Properties)
     * @see        #publishTo(String, String, Properties, String)
     * @see        #publishTo(String, String, String)
     * @see        TextMessage
     * @see        MessagePipeline
     */
    public void publishTo(final String topicName, final String text) {
        publishTo(topicName, text, (Properties)null);
    }

    /**
     * <p>
     *   Publishes the specified <code>text</code> to the topic with the specified <code>topicName</code>. That is, the
     *   <code>text</code> will be put into a MessagePipeline before actually being published as a TextMessage, with the
     *   specified <code>messageProperties</code>, to the message service.
     * </p>
     * <p>
     *   If this MessageServiceClient is not a publisher on the topic with the <code>topicName</code> yet, it will
     *   automatically add itself as a publisher.
     * </p>
     *
     * @param      topicName
     *                 the topic name of the topic to publish on.
     * @param      text
     *                 the text to be published.
     * @param      messageProperties
     *                 the message properties for the TextMessage.
     * @see        #publishTo(String, String, Properties, String)
     * @see        #publishTo(String, String, String)
     * @see        #publishTo(String, String)
     * @see        TextMessage
     * @see        MessagePipeline
     */
    public void publishTo(final String topicName, final String text, final Properties messageProperties) {
        if (topicName != null && topicName.trim().length() != 0 && text != null && text.trim().length() != 0) {
            publishTo(topicName, createMessage(text, baseMessageProperties, messageProperties));
        }
    }

    /**
     * <p>
     *   Publishes the specified <code>text</code> to the topic with the specified <code>topicName</code>. That is, the
     *   <code>text</code> will be put into a MessagePipeline before actually being published as a TextMessage, with the
     *   specified <code>messageProperties</code> and <code>messageType</code>, to the message service.
     * </p>
     * <p>
     *   If this MessageServiceClient is not a publisher on the topic with the <code>topicName</code> yet, it will
     *   automatically add itself as a publisher.
     * </p>
     *
     * @param      topicName
     *                 the topic name of the topic to publish on.
     * @param      text
     *                 the text to be published.
     * @param      messageProperties
     *                 the message properties for the TextMessage.
     * @param      messageType
     *                 the message type for the TextMessage.
     * @see        #publishTo(String, String, Properties)
     * @see        #publishTo(String, String, String)
     * @see        #publishTo(String, String)
     * @see        TextMessage
     * @see        MessagePipeline
     */
    public void publishTo(
        final String topicName, final String text, final Properties messageProperties, final String messageType) {

        publishTo(topicName, text, getMessageProperties(messageProperties, messageType));
    }

    /**
     * <p>
     *   Publishes the specified <code>text</code> to the topic with the specified <code>topicName</code>. That is, the
     *   <code>text</code> will be put into a MessagePipeline before actually being published as a TextMessage, with the
     *   specified <code>messageType</code>, to the message service.
     * </p>
     * <p>
     *   If this MessageServiceClient is not a publisher on the topic with the <code>topicName</code> yet, it will
     *   automatically add itself as a publisher.
     * </p>
     *
     * @param      topicName
     *                 the topic name of the topic to publish on.
     * @param      text
     *                 the text to be published.
     * @param      messageType
     *                 the message type for the TextMessage.
     * @see        #publishTo(String, String, Properties)
     * @see        #publishTo(String, String, Properties, String)
     * @see        #publishTo(String, String)
     * @see        TextMessage
     * @see        MessagePipeline
     */
    public void publishTo(final String topicName, final String text, final String messageType) {
        publishTo(topicName, text, getMessageProperties(null, messageType));
    }

    /**
     * <p>
     *   Removes the specified <code>MessageHandler</code> from this MessageServiceClient for the default topic.
     * </p>
     *
     * @param      messageHandler
     *                 the MessageHandler to be removed.
     * @see        MessageServiceAdapter#removeMessageHandler(MessageHandler, String)
     * @see        #addMessageHandler(MessageHandler, String)
     */
    public void removeMessageHandler(final MessageHandler messageHandler) {
        removeMessageHandler(messageHandler, getDefaultTopicName());
    }

    /**
     * <p>
     *   Removes the specified <code>MessageHandler</code> from this
     *   MessageServiceClient for the topic with the specified
     *   <code>topicName</code>.
     * </p>
     *
     * @param      messageHandler
     *                 the MessageHandler to be removed.
     * @param      topicName
     *                 the topic name of the topic associated with the
     *                 MessageHandler.
     * @see        MessageServiceAdapter#removeMessageHandler(MessageHandler, String)
     * @see        #addMessageHandler(MessageHandler, String)
     */
    public void removeMessageHandler(
        final MessageHandler messageHandler, final String topicName) {

        if (messageHandler != null &&
            topicName != null && topicName.trim().length() != 0) {

            if (messageHandlerMap.containsKey(topicName)) {
                Map _messageHandlerWrapperMap =
                    (Map)messageHandlerMap.get(topicName);
                if (_messageHandlerWrapperMap.containsKey(messageHandler)) {
                    messageServiceAdapter.removeMessageHandler(
                        (MessageSeparator)
                            _messageHandlerWrapperMap.remove(messageHandler),
                        topicName);
                    if (_messageHandlerWrapperMap.isEmpty()) {
                        messageHandlerMap.remove(topicName);
                    }
                }
            }
        }
    }

    public void setAdministrator(final Administrator administrator) {
        this.administrator = administrator;
    }

    /**
     * <p>
     *   Starts this MessageServiceClient's delivery of incoming messages.
     * </p>
     *
     * @throws     MessageServiceException
     *                 if the message service fails to start message delivery
     *                 due to some internal error.
     * @see        MessageServiceAdapter#start()
     * @see        #stop()
     */
    public void start()
    throws MessageServiceException {
        messageServiceAdapter.start();
    }

    /**
     * <p>
     *   Stops this MessageServiceClient's delivery of incoming messages. The
     *   delivery can be restarted using the <code>start()</code> method.
     * </p>
     * <p>
     *   Stopping this MessageServiceClient's message delivery has no effect on
     *   its ability to send messages.
     * </p>
     *
     * @throws     MessageServiceException
     *                 if the message service fails to stop message delivery due
     *                 to some internal error.
     * @see        MessageServiceAdapter#stop()
     * @see        #start()
     * @see        #close()
     */
    public void stop()
    throws MessageServiceException {
        messageServiceAdapter.stop();
    }

    /**
     * <p>
     *   Subscribes this MessageServiceClient to the default topic. This type of subscription has no message selector
     *   and automatically inhibits the delivery of incoming messages published by this MessageServiceClient's own
     *   connection.
     * </p>
     *
     * @throws     MessageServiceException
     *                 if the message service fails to subscribe due to some internal error.
     * @see        #subscribeTo(String)
     * @see        #subscribe(boolean)
     * @see        #subscribe(MessageSelector)
     * @see        #subscribe(MessageSelector, boolean)
     * @see        #unsubscribe()
     */
    public void subscribe()
    throws MessageServiceException {
        subscribeTo(getDefaultTopicName());
    }

    /**
     * <p>
     *   Subscribes this MessageServiceClient to the default topic. This type of subscription has no message selector.
     * </p>
     *
     * @param      noLocal
     *                 if <code>true</code> inhibits the delivery of incoming messages published by this
     *                 MessageServiceClient's own connection.
     * @throws     MessageServiceException
     *                 if the message service fails to subscribe due to some internal error.
     * @see        #subscribeTo(String, boolean)
     * @see        #subscribe()
     * @see        #subscribe(MessageSelector)
     * @see        #subscribe(MessageSelector, boolean)
     * @see        #unsubscribe()
     */
    public void subscribe(final boolean noLocal)
    throws MessageServiceException {
        subscribeTo(getDefaultTopicName(), noLocal);
    }

    /**
     * <p>
     *   Subscribes this MessageServiceClient to the default topic. This type of subscription automatically inhibits the
     *   delivery of incoming messages published by this MessageServiceClient's own connection.
     * </p>
     *
     * @param      messageSelector
     *                 only incoming messages with properties matching the MessageSelector's expression are delivered.
     * @throws     MessageServiceException
     *                 if the message service fails to subscribe due to some internal error.
     * @see        #subscribeTo(String, MessageSelector)
     * @see        #subscribe()
     * @see        #subscribe(boolean)
     * @see        #subscribe(MessageSelector, boolean)
     * @see        #unsubscribe()
     */
    public void subscribe(final MessageSelector messageSelector)
    throws MessageServiceException {
        subscribeTo(getDefaultTopicName(), messageSelector);
    }

    /**
     * <p>
     *   Subscribes this MessageServiceClient to the default topic.
     * </p>
     *
     * @param      messageSelector
     *                 only incoming messages with properties matching the MessageSelector's expression are delivered.
     * @param      noLocal
     *                 if <code>true</code> inhibits the delivery of incoming messages published by this
     *                 MessageServiceClient's own connection.
     * @throws     MessageServiceException
     *                 if the message service fails to subscribe due to some internal error.
     * @see        #subscribeTo(String, MessageSelector, boolean)
     * @see        #subscribe()
     * @see        #subscribe(boolean)
     * @see        #subscribe(MessageSelector)
     * @see        #unsubscribe()
     */
    public void subscribe(final MessageSelector messageSelector, final boolean noLocal)
    throws MessageServiceException {
        subscribeTo(getDefaultTopicName(), messageSelector, noLocal);
    }

    /**
     * <p>
     *   Subscribes this MessageServiceClient to the topic with the specified <code>topicName</code>. This type of
     *   subscription has no message selector and automatically inhibits the delivery of incoming messages published by
     *   this MessageServiceClient's own connection.
     * </p>
     *
     * @param      topicName
     *                 the topic name of the topic to subscribe to.
     * @throws     MessageServiceException
     *                 if the message service fails to subscribe due to some internal error.
     * @see        MessageServiceAdapter#subscribe(String, MessageSelector, boolean)
     * @see        #subscribeTo(String, MessageSelector, boolean)
     * @see        #subscribeTo(String, boolean)
     * @see        #subscribeTo(String, MessageSelector)
     * @see        #unsubscribeFrom(String)
     */
    public void subscribeTo(final String topicName)
    throws MessageServiceException {
        subscribeTo(topicName, null, true);
    }

    /**
     * <p>
     *   Subscribes this MessageServiceClient to the topic with the specified <code>topicName</code>. This type of
     *   subscription has no message selector.
     * </p>
     *
     * @param      topicName
     *                 the topic name of the topic to subscribe to.
     * @param      noLocal
     *                 if <code>true</code> inhibits the delivery of incoming messages published by this
     *                 MessageServiceClient's own connection.
     * @throws     MessageServiceException
     *                 if the message service fails to subscribe due to some internal error.
     * @see        MessageServiceAdapter#subscribe(String, MessageSelector, boolean)
     * @see        #subscribeTo(String, MessageSelector, boolean)
     * @see        #subscribeTo(String)
     * @see        #subscribeTo(String, MessageSelector)
     * @see        #unsubscribeFrom(String)
     */
    public void subscribeTo(final String topicName, final boolean noLocal)
    throws MessageServiceException {
        subscribeTo(topicName, null, noLocal);
    }

    /**
     * <p>
     *   Subscribes this MessageServiceClient to the topic with the specified <code>topicName</code>. This type of
     *   subscription automatically inhibits the delivery of incoming messages published by this MessageServiceClient's
     *   own connection.
     * </p>
     *
     * @param      topicName
     *                 the topic name of the topic to subscribe to.
     * @param      messageSelector
     *                 only incoming messages with properties matching the MessageSelector's expression are delivered.
     * @throws     MessageServiceException
     *                 if the message service fails to subscribe due to some internal error.
     * @see        MessageServiceAdapter#subscribe(String, MessageSelector, boolean)
     * @see        #subscribeTo(String, MessageSelector, boolean)
     * @see        #subscribeTo(String)
     * @see        #subscribeTo(String, boolean)
     * @see        #unsubscribeFrom(String)
     */
    public void subscribeTo(final String topicName, final MessageSelector messageSelector)
    throws MessageServiceException {
        subscribeTo(topicName, messageSelector, true);
    }

    /**
     * <p>
     *   Subscribes this MessageServiceClient to the topic with the specified <code>topicName</code>.
     * </p>
     *
     * @param      topicName
     *                 the topic name of the topic to subscribe to.
     * @param      messageSelector
     *                 only incoming messages with properties matching the MessageSelector's expression are delivered.
     * @param      noLocal
     *                 if <code>true</code> inhibits the delivery of incoming messages published by this
     *                 MessageServiceClient's own connection.
     * @throws     MessageServiceException
     *                 if the message service fails to subscribe due to some internal error.
     * @see        MessageServiceAdapter#subscribe(String, MessageSelector, boolean)
     * @see        #subscribeTo(String)
     * @see        #subscribeTo(String, boolean)
     * @see        #subscribeTo(String, MessageSelector)
     * @see        #unsubscribeFrom(String)
     */
    public void subscribeTo(final String topicName, final MessageSelector messageSelector, final boolean noLocal)
    throws MessageServiceException {
        if (topicName != null && topicName.trim().length() != 0) {
            messageServiceAdapter.subscribe(
                topicName, messageSelector, noLocal);
        }
    }

    /**
     * <p>
     *   Unsubscribes this MessageServiceClient from the default topic.
     * </p>
     *
     * @throws     MessageServiceException
     *                 if the message service fails to unsubscribe due to some internal error.
     * @see        #subscribe()
     * @see        #subscribe(boolean)
     * @see        #subscribe(MessageSelector)
     * @see        #subscribe(MessageSelector, boolean)
     */
    public void unsubscribe()
    throws MessageServiceException {
        unsubscribeFrom(getDefaultTopicName());
    }

    /**
     * <p>
     *   Unsubscribes this MessageServiceClient from the topic with the specified <code>topicName</code>.
     * </p>
     *
     * @param      topicName
     *                 the topic name of the topic to unsubscribe from.
     * @throws     MessageServiceException
     *                 if the message service fails to unsubscribe due to some internal error.
     * @see        MessageServiceAdapter#unsubscribe(String)
     * @see        #subscribeTo(String)
     * @see        #subscribeTo(String, boolean)
     * @see        #subscribeTo(String, MessageSelector)
     * @see        #subscribeTo(String, MessageSelector, boolean)
     */
    public void unsubscribeFrom(final String topicName)
    throws MessageServiceException {
        if (topicName != null && topicName.trim().length() != 0) {
            try {
                messageServiceAdapter.unsubscribe(topicName);
            } finally {
                messageHandlerMap.remove(topicName);
            }
        }
    }

    private static void addMessageProperties(final Properties messageProperties, final Message message) {
        if (messageProperties == null) {
            return;
        }
        Iterator _messageProperties = messageProperties.entrySet().iterator();
        while (_messageProperties.hasNext()) {
            Map.Entry _messageProperty = (Map.Entry)_messageProperties.next();
            Object _value = _messageProperty.getValue();
            if (_value instanceof Boolean) {
                message.setBooleanProperty((String) _messageProperty.getKey(), ((Boolean) _value).booleanValue());
            } else if (_value instanceof Byte) {
                message.setByteProperty((String) _messageProperty.getKey(), ((Byte) _value).byteValue());
            } else if (_value instanceof Double) {
                message.setDoubleProperty((String) _messageProperty.getKey(), ((Double) _value).doubleValue());
            } else if (_value instanceof Float) {
                message.setFloatProperty((String) _messageProperty.getKey(), ((Float) _value).floatValue());
            } else if (_value instanceof Integer) {
                message.setIntProperty((String) _messageProperty.getKey(), ((Integer) _value).intValue());
            } else if (_value instanceof Long) {
                message.setLongProperty((String) _messageProperty.getKey(), ((Long) _value).longValue());
            } else if (_value instanceof Short) {
                message.setShortProperty((String) _messageProperty.getKey(), ((Short) _value).shortValue());
            } else if (_value instanceof String) {
                message.setStringProperty((String) _messageProperty.getKey(), (String) _value);
            } else {
                message.setObjectProperty((String) _messageProperty.getKey(), _value);
            }
        }
    }

    private static Message createMessage(
        final Serializable object, final Properties baseMessageProperties, final Properties messageProperties) {

        Message _message = new ObjectMessage(object);
        addMessageProperties(baseMessageProperties, _message);
        addMessageProperties(messageProperties, _message);
        return _message;
    }

    private static Message createMessage(
        final String text, final Properties baseMessageProperties, final Properties messageProperties) {

        Message _message = new TextMessage(text);
        addMessageProperties(baseMessageProperties, _message);
        addMessageProperties(messageProperties, _message);
        return _message;
    }

    private static Properties getMessageProperties(final Properties messageProperties, final String messageType) {
        Properties _messageProperties;
        if (messageProperties != null) {
            _messageProperties = messageProperties;
        } else {
            _messageProperties = new Properties();
        }
        _messageProperties.setProperty(Message.MESSAGE_TYPE, messageType);
        return _messageProperties;
    }

    private void publishTo(final String topicName, final Message message) {
        String _messagePipelineId =
            topicName + "/" +
                message.getStringProperty(Message.MESSAGE_TYPE) + "/" +
                message.getStringProperty(Message.DESTINATION_SERVLET_CONTEXT_PATH);
        MessagePipeline _messagePipeline;
        if (messagePipelineMap.containsKey(_messagePipelineId)) {
            _messagePipeline = (MessagePipeline)messagePipelineMap.get(_messagePipelineId);
        } else {
            _messagePipeline = new MessagePipeline(this, topicName);
            messagePipelineMap.put(_messagePipelineId,  _messagePipeline);
        }
        _messagePipeline.enqueue(message);
    }

    private void publishNowTo(final String topicName, final Message message)
    throws MessageServiceException {
        messageServiceAdapter.publish(message, topicName);
    }

    private void setBaseMessageProperties() {
        try {
            baseMessageProperties.setProperty(Message.SOURCE_NODE_ADDRESS, InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException exception) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Failed to get IP address for localhost.", exception);
            }
        }
    }
    
    private void setBaseMessageProperties(final ServletContext servletContext) {
        setBaseMessageProperties(servletContext != null ? ServerUtility.getServletContextPath(servletContext) : null);
    }

    private void setBaseMessageProperties(final String servletContextPath) {
        if (servletContextPath != null) {
            baseMessageProperties.setProperty(Message.SOURCE_SERVLET_CONTEXT_PATH, servletContextPath);
        }
        setBaseMessageProperties();
    }
    
    public static interface Administrator {
        void reconnect();
        
        boolean reconnectNow();
    }
}
