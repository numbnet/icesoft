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
package com.icesoft.net.messaging.jms;

import com.icesoft.net.messaging.AbstractMessageServiceAdapter;
import com.icesoft.net.messaging.Message;
import com.icesoft.net.messaging.MessageHandler;
import com.icesoft.net.messaging.MessageSelector;
import com.icesoft.net.messaging.MessageServiceAdapter;
import com.icesoft.net.messaging.MessageServiceClient;
import com.icesoft.net.messaging.MessageServiceException;
import com.icesoft.util.ThreadFactory;

import edu.emory.mathcs.backport.java.util.concurrent.Executors;
import edu.emory.mathcs.backport.java.util.concurrent.ExecutorService;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.jms.InvalidDestinationException;
import javax.jms.InvalidSelectorException;
import javax.jms.JMSException;
import javax.jms.JMSSecurityException;
import javax.jms.MessageFormatException;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JMSAdapter
extends AbstractMessageServiceAdapter
implements MessageServiceAdapter {
    private static final Log LOG = LogFactory.getLog(JMSAdapter.class);

    private JMSProviderConfiguration jmsProviderConfiguration;

    private InitialContext initialContext;
    private TopicConnectionFactory topicConnectionFactory;

    private ExecutorService executorService;

    public JMSAdapter(final JMSProviderConfiguration jmsProviderConfiguration)
    throws IllegalArgumentException {
        super(jmsProviderConfiguration);
        this.jmsProviderConfiguration = jmsProviderConfiguration;
        ThreadFactory _threadFactory = new ThreadFactory();
        _threadFactory.setPrefix("MessageReceiver Thread");
        executorService = Executors.newCachedThreadPool(_threadFactory);
    }

    public JMSAdapter(final ServletContext servletContext)
    throws IllegalArgumentException {
        super(servletContext);
        String _messagingProperties =
            servletContext.getInitParameter(MESSAGING_PROPERTIES);
        LOG.info("Messaging Properties (web.xml): " + _messagingProperties);
        if (_messagingProperties != null) {
            try {
                this.jmsProviderConfiguration =
                    new JMSProviderConfigurationProperties(
                        getClass().getResourceAsStream(_messagingProperties));
            } catch (IOException exception) {
                if (LOG.isErrorEnabled()) {
                    LOG.error(
                        "An error occurred " +
                            "while reading properties: " + _messagingProperties,
                        exception);
                }
            }
        }
        if (this.jmsProviderConfiguration == null) {
            String _serverInfo = servletContext.getServerInfo();
            if (_serverInfo.startsWith("Sun Java System Application Server")) {
                _messagingProperties = "glassfish.properties";
            }
            LOG.info("Messaging Properties: " + _messagingProperties);
            try {
                this.jmsProviderConfiguration =
                    new JMSProviderConfigurationProperties(
                        getClass().getResourceAsStream(_messagingProperties));
            } catch (IOException exception) {
                if (LOG.isErrorEnabled()) {
                    LOG.error(
                        "An error occurred " +
                            "while reading properties: " + _messagingProperties,
                        exception);
                }
            }
        }
        ThreadFactory _threadFactory = new ThreadFactory();
        _threadFactory.setPrefix("MessageReceiver Thread");
        executorService = Executors.newCachedThreadPool(_threadFactory);
    }

    public void addMessageHandler(
        final MessageHandler messageHandler, final String topicName) {

        if (messageHandler != null &&
            topicName != null && topicName.trim().length() != 0) {

            if (topicSubscriberMap.containsKey(topicName)) {
//                synchronized (topicSubscriberMap) {
//                    if (topicSubscriberMap.containsKey(topicName)) {
                        ((JMSSubscriberConnection)
                            topicSubscriberMap.get(topicName)).
                                addMessageHandler(messageHandler);
//                    }
//                }
            }
        }
    }

    public void closeConnection()
    throws MessageServiceException {
//        synchronized (topicPublisherMap) {
//            synchronized (topicPublisherMap) {
                MessageServiceException _messageServiceException = null;
                if (!topicPublisherMap.isEmpty()) {
                    Iterator _jmsPublisherConnections =
                        topicPublisherMap.values().iterator();
                    while (_jmsPublisherConnections.hasNext()) {
                        try {
                            // throws JMSException.
                            ((JMSPublisherConnection)
                                _jmsPublisherConnections.next()).close();
                        } catch (JMSException exception) {
                            if (_messageServiceException == null) {
                                _messageServiceException =
                                    new MessageServiceException(exception);
                            }
                        }
                    }
                    topicPublisherMap.clear();
                }
                if (!topicSubscriberMap.isEmpty()) {
                    Iterator _jmsSubscriberConnections =
                        topicSubscriberMap.values().iterator();
                    while (_jmsSubscriberConnections.hasNext()) {
                        try {
                            // throws JMSException.
                            ((JMSSubscriberConnection)
                                _jmsSubscriberConnections.next()).close();
                        } catch (JMSException exception) {
                            if (_messageServiceException == null) {
                                _messageServiceException =
                                    new MessageServiceException(exception);
                            }
                        }
                    }
                    topicSubscriberMap.clear();
                }
                if (_messageServiceException != null) {
                    throw _messageServiceException;
                }
//            }
//        }
    }

    public JMSProviderConfiguration getJMSProviderConfiguration() {
        return jmsProviderConfiguration;
    }

    public void publish(final Message message, final String topicName)
    throws MessageServiceException {
        if (message != null &&
            topicName != null && topicName.trim().length() != 0) {

//            synchronized (topicPublisherMap) {
                if (topicConnectionFactory == null) {
                    try {
                        initialize();
                    } catch (NamingException exception) {
                        throw new MessageServiceException(exception);
                    }
                }
                JMSPublisherConnection _jmsPublisherConnection;
                if (!topicPublisherMap.containsKey(topicName)) {
                    try {
                        // throws NamingException.
                        _jmsPublisherConnection =
                            new JMSPublisherConnection(
                                lookUpTopic(topicName), this);
                        // throws JMSException, JMSSecurityException.
                        _jmsPublisherConnection.open();
                        topicPublisherMap.put(
                            topicName, _jmsPublisherConnection);
                    } catch (NamingException exception) {
                        throw new MessageServiceException(exception);
                    } catch (JMSSecurityException exception) {
                        throw new MessageServiceException(exception);
                    } catch (JMSException exception) {
                        throw new MessageServiceException(exception);
                    }
                } else {
                    _jmsPublisherConnection =
                        (JMSPublisherConnection)
                            topicPublisherMap.get(topicName);
                }
                try {
                    // throws
                    //     InvalidDestinationException, JMSException,
                    //     MessageFormatException.
                    _jmsPublisherConnection.publish(message);
                } catch (InvalidDestinationException exception) {
                    throw new MessageServiceException(exception);
                } catch (MessageFormatException exception) {
                    throw new MessageServiceException(exception);
                } catch (JMSException exception) {
                    throw new MessageServiceException(exception);
                }
//            }
        }
    }

    public void removeMessageHandler(
        final MessageHandler messageHandler, final String topicName) {

        if (messageHandler != null &&
            topicName != null && topicName.trim().length() != 0) {

            if (topicSubscriberMap.containsKey(topicName)) {
//                synchronized (topicSubscriberMap) {
//                    if (topicSubscriberMap.containsKey(topicName)) {
                        ((JMSSubscriberConnection)
                            topicSubscriberMap.get(topicName)).
                                removeMessageHandler(messageHandler);
//                    }
//                }
            }
        }
    }

    public void start()
    throws MessageServiceException {
        if (!topicSubscriberMap.isEmpty()) {
//            synchronized (topicSubscriberMap) {
//                if (!topicSubscriberMap.isEmpty()) {
                    Iterator _jmsSubscriberConnections =
                        topicSubscriberMap.values().iterator();
                    MessageServiceException _messageServiceException = null;
                    while (_jmsSubscriberConnections.hasNext()) {
                        try {
                            // throws JMSException.
                            ((JMSSubscriberConnection)
                                _jmsSubscriberConnections.next()).start();
                        } catch (JMSException exception) {
                            if (_messageServiceException == null) {
                                _messageServiceException =
                                    new MessageServiceException(exception);
                            }
                        }
                    }
                    if (_messageServiceException != null) {
                        throw _messageServiceException;
                    }
//                }
//            }
        }
    }

    public void stop()
    throws MessageServiceException {
        if (!topicSubscriberMap.isEmpty()) {
//            synchronized (topicSubscriberMap) {
//                if (!topicSubscriberMap.isEmpty()) {
                    Iterator _jmsSubscriberConnections =
                        topicSubscriberMap.values().iterator();
                    MessageServiceException _messageServiceException = null;
                    while (_jmsSubscriberConnections.hasNext()) {
                        try {
                            // throws JMSException.
                            ((JMSSubscriberConnection)
                                _jmsSubscriberConnections.next()).stop();
                        } catch (JMSException exception) {
                            if (_messageServiceException == null) {
                                _messageServiceException =
                                    new MessageServiceException(exception);
                            }
                        }
                    }
                    if (_messageServiceException != null) {
                        throw _messageServiceException;
                    }
//                }
//            }
        }
    }

    public void subscribe(
        final String topicName, final MessageSelector messageSelector,
        final boolean noLocal)
    throws MessageServiceException {
        if (topicName != null && topicName.trim().length() != 0) {
//            synchronized (topicSubscriberMap) {
                if (topicConnectionFactory == null) {
                    try {
                        initialize();
                    } catch (NamingException exception) {
                        throw new MessageServiceException(exception);
                    }
                }
                JMSSubscriberConnection _jmsSubscriberConnection;
                if (!topicSubscriberMap.containsKey(topicName)) {
                    try {
                        // throws NamingException.
                        _jmsSubscriberConnection =
                            new JMSSubscriberConnection(
                                lookUpTopic(topicName), this);
                        _jmsSubscriberConnection.open();
                        topicSubscriberMap.put(
                            topicName, _jmsSubscriberConnection);
                    } catch (NamingException exception) {
                        throw new MessageServiceException(exception);
                    } catch (JMSSecurityException exception) {
                        throw new MessageServiceException(exception);
                    } catch (JMSException exception) {
                        throw new MessageServiceException(exception);
                    }
                } else {
                    _jmsSubscriberConnection =
                        (JMSSubscriberConnection)
                            topicSubscriberMap.get(topicName);
                }
                try {
                    _jmsSubscriberConnection.subscribe(
                        messageSelector, noLocal);
                } catch (InvalidDestinationException exception) {
                    throw new MessageServiceException(exception);
                } catch (InvalidSelectorException exception) {
                    throw new MessageServiceException(exception);
                } catch (JMSException exception) {
                    throw new MessageServiceException(exception);
                }
//            }
        }
    }

    public void unsubscribe(final String topicName)
    throws MessageServiceException {
        if (topicName != null && topicName.trim().length() != 0) {
            if (topicSubscriberMap.containsKey(topicName)) {
//                synchronized (topicSubscriberMap) {
//                    if (topicSubscriberMap.containsKey(topicName)) {
                        try {
                            ((JMSSubscriberConnection)
                                topicSubscriberMap.get(topicName)).
                                    unsubscribe();
                        } catch (JMSException exception) {
                            throw new MessageServiceException(exception);
                        }
//                    }
//                }
            }
        }
    }

    TopicConnectionFactory getTopicConnectionFactory() {
        return topicConnectionFactory;
    }

    ExecutorService getExecutorService() {
        return executorService;
    }

    private void initialize()
    throws NamingException {
        Properties _environmentProperties = new Properties();
        String _initialContextFactory =
            jmsProviderConfiguration.getInitialContextFactory();
        if (_initialContextFactory != null) {
            _environmentProperties.setProperty(
                JMSProviderConfiguration.INITIAL_CONTEXT_FACTORY,
                _initialContextFactory);
        }
        String _providerUrl =
            jmsProviderConfiguration.getProviderURL();
        if (_providerUrl != null) {
            _environmentProperties.setProperty(
                JMSProviderConfiguration.PROVIDER_URL,
                _providerUrl);
        }
        String _urlPackagePrefixes =
            jmsProviderConfiguration.getURLPackagePrefixes();
        if (_urlPackagePrefixes != null) {
            _environmentProperties.setProperty(
                JMSProviderConfiguration.URL_PACKAGE_PREFIXES,
                _urlPackagePrefixes);
        }
        if (LOG.isInfoEnabled()) {
            StringBuffer _environment = new StringBuffer();
            _environment.append("JMS Environment:\r\n");
            Iterator _properties = _environmentProperties.entrySet().iterator();
            while (_properties.hasNext()) {
                Map.Entry _property = (Map.Entry)_properties.next();
                _environment.append("        ");
                _environment.append(_property.getKey());
                _environment.append(" = ");
                _environment.append(_property.getValue());
                _environment.append("\r\n");
            }
            LOG.info(_environment.toString());
        }
        // throws NamingException.
        initialContext = new InitialContext(_environmentProperties);
        // throws NamingException.
        topicConnectionFactory =
            (TopicConnectionFactory)
                initialContext.lookup(
                    jmsProviderConfiguration.getTopicConnectionFactoryName());
    }

    private Topic lookUpTopic(final String topicName)
    throws NamingException {
        String _topicNamePrefix = jmsProviderConfiguration.getTopicNamePrefix();
        try {
            // throws NamingException.
            return
                (Topic)
                    initialContext.lookup(
                        (_topicNamePrefix == null ? "" : _topicNamePrefix) +
                            topicName);
        } catch (NamingException exception) {
            // temporary trying the old topic names...
            try {
                if (MessageServiceClient.CONTEXT_EVENT_TOPIC_NAME.
                        equals(topicName)) {

                    return
                        (Topic)
                            initialContext.lookup(
                                (_topicNamePrefix == null ? "" : _topicNamePrefix) +
                                    "icefaces.contextEventTopic");
                } else if (MessageServiceClient.RENDER_TOPIC_NAME.
                        equals(topicName)) {
                    
                    return
                        (Topic)
                            initialContext.lookup(
                                (_topicNamePrefix == null ? "" : _topicNamePrefix) +
                                    "icefaces.renderTopic");
                } else if (MessageServiceClient.RESPONSE_TOPIC_NAME.
                        equals(topicName)) {

                    return
                        (Topic)
                            initialContext.lookup(
                                (_topicNamePrefix == null ? "" : _topicNamePrefix) +
                                    "icefaces.responseTopic");
                } else {
                    throw exception;
                }
            } catch (NamingException e) {
                throw exception;
            }
        }
    }

    /*
     * Taken from "Java Message Service - Version 1.1 April 12, 2002":
     *
     * 2.8 Multithreading
     *
     * JMS could have required that all its objects support concurrent use.
     * Since support for concurrent access typically adds some overhead and
     * complexity, the JMS design restricts its requirement for concurrent
     * access to those objects that would naturally be shared by a multithreaded
     * client. The remainder are designed to be accessed by one logical thread
     * of control at a time.
     *
     *     JMS Object        | Supports Concurrent Use
     *     ------------------+------------------------
     *     Destination       | YES
     *     ConnectionFactory | YES
     *     Connection        | YES
     *     Session           | NO
     *     MessageProducer   | NO
     *     MessageConsumer   | NO
     *
     * JMS defines some specific rules that restrict the concurrent use of
     * Sessions. Since they require more knowledge of JMS specifics than we have
     * presented at this point, they will be described later. Here we will
     * describe the rationale for imposing them.
     *
     * There are two reasons for restricting concurrent access to Sessions.
     * First, Sessions are the JMS entity that supports transactions. It is very
     * difficult to implement transactions that are multithreaded. Second,
     * Sessions support asynchronous message consumption. It is important that
     * JMS not require that client code used for asynchronous message
     * consumption be capable of handling multiple, concurrent messages. In
     * addition, if a Session has been set up with multiple, asynchronous
     * consumers, it is important that the client is not forced to handle the
     * case where these separate consumers are concurrently executing. These
     * restrictions make JMS easier to use for typical clients. More
     * sophisticated clients can get the concurrency they desire by using
     * multiple sessions.
     */

    /*
     * Taken from "Java 2 Platform Enterprise Edition Specification, v1.3"
     *
     * J2EE.6.7 Java Message Service (JMS) 1.0 Requirements
     *
     * Note that the JMS API creates threads to deliver messages to message
     * listeners. The use of this message listener facility may be limited by
     * the restrictions on the use of threads in various containers. In EJB
     * containers, for instance, it is typically not possible to create threads.
     * The following methods must not be used by application components
     * executing in containers that prevent them from creating threads:
     *
     *     - javax.jms.Session method setMessageListener
     *     - javax.jms.Session method getMessageListener
     *     - javax.jms.Session method run
     *     - javax.jms.QueueConnection method createConnectionConsumer
     *     - javax.jms.TopicConnection method createConnectionConsumer
     *     - javax.jms.TopicConnection method createDurableConnectionConsumer
     *     - javax.jms.MessageConsumer method getMessageListener
     *     - javax.jms.MessageConsumer method setMessageListener
     *
     * In addition, use of the following methods on javax.jms.Connection objects
     * by applications in web and EJB containers may interfere with the
     * connection management functions of the container and must not be used:
     *
     *     - setExceptionListener
     *     - stop
     *     - setClientID
     *
     * A J2EE container may throw a JMSException if the application component
     * violates these restrictions.
     */

    /*
     * Taken from "Java 2 Platform Enterprise Edition Specification, v1.4"
     *
     * J2EE.6.6 Java Message Service (JMS) 1.1 Requirements
     *
     * The following methods may only be used by application components
     * executing in the application client container:
     *
     *     - javax.jms.Session method setMessageListener
     *     - javax.jms.Session method getMessageListener
     *     - javax.jms.Session method run
     *     - javax.jms.QueueConnection method createConnectionConsumer
     *     - javax.jms.TopicConnection method createConnectionConsumer
     *     - javax.jms.TopicConnection method createDurableConnectionConsumer
     *     - javax.jms.MessageConsumer method getMessageListener
     *     - javax.jms.MessageConsumer method setMessageListener
     *     - javax.jms.Connection method setExceptionListener
     *     - javax.jms.Connection method stop
     *     - javax.jms.Connection method setClientID
     *
     * A J2EE container may throw a JMSException (if allowed by the method) if
     * the application component violates these restrictions.
     *
     * Application components in the web and EJB containers must not attempt to
     * create more than one active (not closed) Session object per connection.
     * An attempt to use the Connection object’s createSession method when an
     * active Session object exists for that connection should be prohibited by
     * the container. The container may throw a JMSException if the application
     * component violates this restriction. Application client containers must
     * support the creation of multiple sessions for each connection.
     */
}
