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
package com.icesoft.net.messaging.jms;

import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.servlet.ServletContextConfiguration;
import com.icesoft.net.messaging.AbstractMessageServiceAdapter;
import com.icesoft.net.messaging.Message;
import com.icesoft.net.messaging.MessageHandler;
import com.icesoft.net.messaging.MessageSelector;
import com.icesoft.net.messaging.MessageServiceAdapter;
import com.icesoft.net.messaging.MessageServiceException;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.io.IOException;

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

    private final Configuration configuration;

    private JMSProviderConfiguration[] jmsProviderConfigurations;
    private int index = -1;

    private InitialContext initialContext;
    private TopicConnectionFactory topicConnectionFactory;

    public JMSAdapter(final ServletContext servletContext)
    throws IllegalArgumentException {
        super(servletContext);
        configuration = new ServletContextConfiguration(servletContext);
        String _messagingProperties =
            servletContext.getInitParameter(MESSAGING_PROPERTIES);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Messaging Properties (web.xml): " + _messagingProperties);
        }
        if (_messagingProperties != null) {
            try {
                this.jmsProviderConfigurations =
                    new JMSProviderConfiguration[] {
                        new JMSProviderConfigurationProperties(
                            getClass().getResourceAsStream(_messagingProperties), configuration)
                    };
            } catch (IOException exception) {
                if (LOG.isErrorEnabled()) {
                    LOG.error("An error occurred while reading properties: " + _messagingProperties, exception);
                }
            }
        }
        if (this.jmsProviderConfigurations == null) {
            this.jmsProviderConfigurations =
                new JMSProviderConfiguration[] {
                    new JMSProviderConfigurationProperties(configuration)
                };
        }
    }

    public void addMessageHandler(
        final MessageHandler messageHandler, final String topicName) {

        if (messageHandler != null &&
            topicName != null && topicName.trim().length() != 0) {

            synchronized (topicSubscriberMap) {
                if (topicSubscriberMap.containsKey(topicName)) {
                    ((JMSSubscriberConnection)
                        topicSubscriberMap.get(topicName)).
                            addMessageHandler(messageHandler);
                }
            }
        }
    }

    public void close()
    throws MessageServiceException {
        synchronized (topicPublisherMap) {
            synchronized (topicSubscriberMap) {
                if (!topicPublisherMap.isEmpty()) {
                    Iterator _jmsPublisherConnections =
                        topicPublisherMap.values().iterator();
                    while (_jmsPublisherConnections.hasNext()) {
                        try {
                            // throws JMSException.
                            ((JMSPublisherConnection)
                                _jmsPublisherConnections.next()).close();
                        } catch (JMSException exception) {
                            throw new MessageServiceException(exception);
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
                            throw new MessageServiceException(exception);
                        }
                    }
                    topicSubscriberMap.clear();
                }
                topicConnectionFactory = null;
                if (initialContext != null) {
                    try {
                        initialContext.close();
                    } catch (NamingException exception) {
                        throw new MessageServiceException(exception);
                    } finally {
                        initialContext = null;
                    }
                }
            }
        }
    }

    public JMSProviderConfiguration getJMSProviderConfiguration() {
        return index != -1 ? jmsProviderConfigurations[index] : null;
    }

    public void initialize()
    throws MessageServiceException {
        Properties _environmentProperties = new Properties();
        String _initialContextFactory;
        for (int i = 0; i < jmsProviderConfigurations.length; i++) {
            _initialContextFactory =
                jmsProviderConfigurations[i].getInitialContextFactory();
            if (_initialContextFactory != null) {
                _environmentProperties.setProperty(
                    JMSProviderConfiguration.INITIAL_CONTEXT_FACTORY,
                    _initialContextFactory);
            }
            String _providerUrl =
                jmsProviderConfigurations[i].getProviderURL();
            if (_providerUrl != null) {
                _environmentProperties.setProperty(
                    JMSProviderConfiguration.PROVIDER_URL,
                    _providerUrl);
            }
            String _urlPackagePrefixes =
                jmsProviderConfigurations[i].getURLPackagePrefixes();
            if (_urlPackagePrefixes != null) {
                _environmentProperties.setProperty(
                    JMSProviderConfiguration.URL_PACKAGE_PREFIXES,
                    _urlPackagePrefixes);
            }
            if (LOG.isDebugEnabled()) {
                StringBuffer _environment = new StringBuffer();
                _environment.append("Trying JMS Environment:\r\n");
                Iterator _properties =
                    _environmentProperties.entrySet().iterator();
                while (_properties.hasNext()) {
                    Map.Entry _property = (Map.Entry)_properties.next();
                    _environment.append("        ");
                    _environment.append(_property.getKey());
                    _environment.append(" = ");
                    _environment.append(_property.getValue());
                    _environment.append("\r\n");
                }
                LOG.debug(_environment.toString());
            }
            try {
                // throws NamingException.
                initialContext = new InitialContext(_environmentProperties);
                // throws NamingException.
                topicConnectionFactory =
                    (TopicConnectionFactory)
                        initialContext.lookup(
                            jmsProviderConfigurations[i].
                                getTopicConnectionFactoryName());
                index = i;
                if (LOG.isDebugEnabled()) {
                    StringBuffer _environment = new StringBuffer();
                    _environment.append("Using JMS Environment:\r\n");
                    Iterator _properties =
                        _environmentProperties.entrySet().iterator();
                    while (_properties.hasNext()) {
                        Map.Entry _property = (Map.Entry)_properties.next();
                        _environment.append("        ");
                        _environment.append(_property.getKey());
                        _environment.append(" = ");
                        _environment.append(_property.getValue());
                        _environment.append("\r\n");
                    }
                    LOG.debug(_environment.toString());
                }
                messageServiceConfiguration = jmsProviderConfigurations[index];
                break;
            } catch (NamingException exception) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(
                        "Failed JMS Environment: " + exception.getMessage());
                }
                if (initialContext != null) {
                    try {
                        initialContext.close();
                    } catch (NamingException e) {
                        // ignoring this one.
                    }
                }
                if ((i + 1) == jmsProviderConfigurations.length) {
                    throw new MessageServiceException(exception);
                }
            }
        }
    }

    public void publish(final Message message, final String topicName)
    throws MessageServiceException {
        if (message != null &&
            topicName != null && topicName.trim().length() != 0) {

            synchronized (topicPublisherMap) {
                JMSPublisherConnection _jmsPublisherConnection;
                if (!topicPublisherMap.containsKey(topicName)) {
                    try {
                        // throws NamingException.
                        _jmsPublisherConnection =
                            new JMSPublisherConnection(
                                lookUpTopic(topicName),
                                getJMSProviderConfiguration().getUserName(),
                                getJMSProviderConfiguration().getPassword(),
                                this);
                    } catch (NamingException exception) {
                        throw new MessageServiceException(exception);
                    }
                    try {
                        // throws JMSException, JMSSecurityException.
                        _jmsPublisherConnection.open();
                        topicPublisherMap.put(
                            topicName, _jmsPublisherConnection);
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
            }
        }
    }

    public void removeMessageHandler(
        final MessageHandler messageHandler, final String topicName) {

        if (messageHandler != null &&
            topicName != null && topicName.trim().length() != 0) {

            synchronized (topicSubscriberMap) {
                if (topicSubscriberMap.containsKey(topicName)) {
                    ((JMSSubscriberConnection)
                        topicSubscriberMap.get(topicName)).
                            removeMessageHandler(messageHandler);
                }
            }
        }
    }

    public void start()
    throws MessageServiceException {
        synchronized (topicSubscriberMap) {
            if (!topicSubscriberMap.isEmpty()) {
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
            }
        }
    }

    public void stop()
    throws MessageServiceException {
        synchronized (topicSubscriberMap) {
            if (!topicSubscriberMap.isEmpty()) {
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
            }
        }
    }

    public void subscribe(
        final String topicName, final MessageSelector messageSelector,
        final boolean noLocal)
    throws MessageServiceException {
        if (topicName != null && topicName.trim().length() != 0) {
            synchronized (topicSubscriberMap) {
                JMSSubscriberConnection _jmsSubscriberConnection;
                if (!topicSubscriberMap.containsKey(topicName)) {
                    try {
                        // throws NamingException.
                        _jmsSubscriberConnection =
                            new JMSSubscriberConnection(
                                lookUpTopic(topicName),
                                getJMSProviderConfiguration().getUserName(),
                                getJMSProviderConfiguration().getPassword(),
                                this);
                    } catch (NamingException exception) {
                        throw new MessageServiceException(exception);
                    }
                    try {
                        // throws JMSException, JMSSecurityException.
                        _jmsSubscriberConnection.open();
                        topicSubscriberMap.put(
                            topicName, _jmsSubscriberConnection);
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
            }
        }
    }

    public void unsubscribe(final String topicName)
    throws MessageServiceException {
        if (topicName != null && topicName.trim().length() != 0) {
            synchronized (topicSubscriberMap) {
                if (topicSubscriberMap.containsKey(topicName)) {
                    JMSSubscriberConnection _jmsSubscriberConnection =
                        (JMSSubscriberConnection)
                            topicSubscriberMap.remove(topicName);
                    MessageServiceException _messageServiceException = null;
                    try {
                        _jmsSubscriberConnection.stop();
                    } catch (JMSException exception) {
                        // do nothing.
                    }
                    try {
                        _jmsSubscriberConnection.unsubscribe();
                    } catch (JMSException exception) {
                        _messageServiceException =
                            new MessageServiceException(exception);
                    }
                    try {
                        _jmsSubscriberConnection.close();
                    } catch (JMSException exception) {
                        // do nothing.
                    }
                    if (_messageServiceException != null) {
                        throw _messageServiceException;
                    }
                }
            }
        }
    }

    TopicConnectionFactory getTopicConnectionFactory() {
        return topicConnectionFactory;
    }

    private synchronized Topic lookUpTopic(final String topicName)
    throws NamingException {
        String _topicNamePrefix =
            jmsProviderConfigurations[index].getTopicNamePrefix();
        // throws NamingException.
        return
            (Topic)
                initialContext.lookup(
                    (_topicNamePrefix == null ? "" : _topicNamePrefix) +
                        topicName);
    }
}
