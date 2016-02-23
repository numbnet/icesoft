/*
 * Copyright 2004-2016 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.icesoft.notify.cloud.core;

import static org.icesoft.util.StringUtilities.isNullOrIsEmpty;

import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.icesoft.util.Configuration;
import org.icesoft.util.ConfigurationException;
import org.icesoft.util.SystemConfiguration;
import org.icesoft.util.servlet.ServletContextConfiguration;

public class EmailNotificationProvider
extends AbstractNotificationProvider
implements NotificationProvider {
    private static final Logger LOGGER = Logger.getLogger(EmailNotificationProvider.class.getName());

    private static final class Constant {
        private static final String PROTOCOL = "mailto";
    }
    private static final class Property {
        private static final class Name {
            private static final String DEBUG = "notify.cloud.email.debug";
            private static final String ENABLED = "notify.cloud.email.enabled";
            private static final String FROM = "notify.cloud.email.from";
            private static final String HOST = "notify.cloud.email.host";
            private static final String PASSWORD = "notify.cloud.email.password";
            private static final String PORT = "notify.cloud.email.port";
            private static final String SCHEME = "notify.cloud.email.scheme";
            private static final String SECURITY = "notify.cloud.email.security";
            private static final String USER_NAME = "notify.cloud.email.userName";
            private static final String VERIFY = "notify.cloud.email.verify";
        }
        private static final class DefaultValue {
            private static final boolean DEBUG = false;
            private static final boolean ENABLED = true;
            private static final String FROM = "nobody@localhost.com";
            private static final String HOST = "localhost";
            private static final String SECURITY = Security.NONE;
            private static final String PASSWORD = "";
            private static final int PORT = 25;
            private static final String SCHEME = "smtp";
            private static final int SECURE_PORT = 465;
            private static final String SECURE_SCHEME = "smtps";
            private static final String USER_NAME = "";
            private static final boolean VERIFY = false;
        }
    }
    private static final class Security {
        private static final String NONE = "NONE";
        private static final String SSL = "SSL";
        private static final String TLS = "TLS";
    }

    private final InternetAddress from;
    private final String host;
    private final String password;
    private final int port;
    private final String scheme;
    private final Session session;
    private final String userName;

    private EmailNotificationProvider(
        final String from, final String scheme, final String host, final int port, final String userName,
        final String password, final String security, final boolean verify, final boolean debug)
    throws IllegalArgumentException {
        super();
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(
                Level.FINE,
                "Initializing E-mail Notification Provider.  " +
                    "(" +
                        "From: '" + from + "', " +
                        "Scheme: '" + scheme + "', " +
                        "Host: '" + host + "', " +
                        "Port: '" + port + "'" +
                        "User Name: '" + userName + "'" +
                        "Password: '" + password.replaceAll(".", "*") + "'" +
                        "Security: '" + security + "', " +
                        "Verify: '" + verify + "'" +
                        "Debug: '" + debug + "'" +
                    ")"
            );
        }
        try {
            this.from = new InternetAddress(from);
        } catch (final AddressException exception) {
            throw new IllegalArgumentException("Illegal argument from: '" + from + "'", exception);
        }
        this.scheme = scheme;
        this.host = host;
        this.port = port;
        this.userName = userName;
        this.password = password;
        Properties _properties = new Properties();
        _properties.setProperty("mail.smtp.auth", "true");
        if (debug) {
            _properties.setProperty("mail.debug", "true");
        }
        if (security.equalsIgnoreCase(Security.SSL)) {
            _properties.put("mail.smtp.ssl.enable", "true");
            _properties.put("mail.smtp.ssl.protocols", "SSLv3");
        } else if (security.equalsIgnoreCase(Security.TLS)) {
            _properties.put("mail.smtp.starttls.enable", "true");
            _properties.put("mail.smtp.ssl.protocols", "TLSv1");
        }
        if (verify) {
            _properties.setProperty("mail.smtps.socketFactory.class", "com.icesoft.notify.util.DummySSLSocketFactory");
            _properties.setProperty("mail.smtps.socketFactory.fallback", "false");
        }
        session = Session.getInstance(_properties);
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO, "E-mail Notification Provider is on.");
        }
    }

    public String getProtocol() {
        return Constant.PROTOCOL;
    }

    public void send(final Map<String, String> propertyMap, final String notifyBackURI) {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(
                Level.FINE,
                "Sending E-mail Cloud Push Notification with " +
                    "Properties '" + propertyMap + "' to Notify-Back-URI '" + notifyBackURI + "'."
            );
        }
        try {
            MimeMessage _mimeMessage = new MimeMessage(getSession());
            _mimeMessage.setFrom(getFrom());
            _mimeMessage.setSubject(propertyMap.get(NotificationProvider.Property.Name.SUBJECT));
            _mimeMessage.setText(propertyMap.get(NotificationProvider.Property.Name.DETAIL));
            Transport _transport = getSession().getTransport(getScheme());
            _transport.connect(getHost(), getPort(), getUserName(), getPassword());
            _transport.sendMessage(
                _mimeMessage,
                new InternetAddress[] {
                    new InternetAddress(notifyBackURI.substring("mailto:".length()))
                }
            );
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(
                    Level.FINE,
                    "Successfully sent E-mail Cloud Push Notification with " +
                        "Properties '" + propertyMap + "' to Notify-Back-URI '" + notifyBackURI + "'."
                );
            }
        } catch (final MessagingException exception) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(
                    Level.WARNING,
                    "Failed to send E-mail Cloud Push Notification with " +
                        "Properties '" + propertyMap + "' to Notify-Back-URI '" + notifyBackURI + "'.",
                    exception
                );
            }
        }
    }

    @Override
    public String toString() {
        return
            new StringBuilder().
                append("EmailNotificationProvider[").
                    append(classMembersToString()).
                append("]").
                    toString();
    }

    protected String classMembersToString() {
        return
            new StringBuilder().
                append("from: '").append(getFrom()).append("', ").
                append("host: '").append(getHost()).append("', ").
                append("password: '").append(getPassword().hashCode()).append("', ").
                append("port: '").append(getPort()).append("', ").
                append("scheme: '").append(getScheme()).append("', ").
                append("session: '").append(getSession()).append("', ").
                append("userName: ").append(getUserName()).append("'").
                    toString();
    }

    protected InternetAddress getFrom() {
        return from;
    }

    protected String getHost() {
        return host;
    }

    protected String getPassword() {
        return password;
    }

    protected int getPort() {
        return port;
    }

    protected String getScheme() {
        return scheme;
    }

    protected Session getSession() {
        return session;
    }

    protected String getUserName() {
        return userName;
    }

    @WebListener
    public static class AutoRegistration
    extends AbstractAutoRegistration
    implements ServletContextListener {
        private static final Logger LOGGER = Logger.getLogger(AutoRegistration.class.getName());

        @Override
        public void contextDestroyed(final ServletContextEvent event) {
            if (hasNotificationProvider()) {
                super.contextDestroyed(event);
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.log(Level.INFO, "E-mail Notification Provider unregistered successfully.");
                }
            }
        }

        @Override
        public void contextInitialized(final ServletContextEvent event) {
            new Thread(
                new RegistrationTask(event.getServletContext()), "E-mail Notification Provider set-up thread"
            ).start();
        }

        protected class RegistrationTask
        extends AbstractRegistrationTask
        implements Runnable {
            protected RegistrationTask(final ServletContext servletContext) {
                super(servletContext);
            }

            @Override
            public void run() {
                awaitSignal();
                Configuration _configuration =
                    new SystemConfiguration(
                        new ServletContextConfiguration(
                            getServletContext()
                        )
                    );
                String _enabledPropertyName =
                    _configuration.getAttribute(
                        "notify.cloud.email.enabled.property.name", Property.Name.ENABLED
                    );
                boolean _enabledPropertyValue;
                try {
                    _enabledPropertyValue = _configuration.getAttributeAsBoolean(_enabledPropertyName);
                } catch (final ConfigurationException exception) {
                    if (!_enabledPropertyName.equals(Property.Name.ENABLED)) {
                        _enabledPropertyName = Property.Name.ENABLED;
                        _enabledPropertyValue =
                            _configuration.getAttributeAsBoolean(
                                _enabledPropertyName,
                                Property.DefaultValue.ENABLED
                            );
                    } else {
                        _enabledPropertyValue = Property.DefaultValue.ENABLED;
                    }
                }
                String _securityPropertyName =
                    _configuration.getAttribute(
                        "notify.cloud.email.security.property.name", Property.Name.SECURITY
                    );
                String _securityPropertyValue;
                try {
                    _securityPropertyValue = _configuration.getAttribute(_securityPropertyName);
                } catch (final ConfigurationException exception) {
                    if (!_securityPropertyName.equals(Property.Name.SECURITY)) {
                        _securityPropertyName = Property.Name.SECURITY;
                        _securityPropertyValue =
                            _configuration.getAttribute(
                                _securityPropertyName,
                                Property.DefaultValue.SECURITY
                            );
                    } else {
                        _securityPropertyValue = Property.DefaultValue.SECURITY;
                    }
                }
                String _verifyPropertyName =
                    _configuration.getAttribute(
                        "notify.cloud.email.verify.property.name", Property.Name.VERIFY
                    );
                boolean _verifyPropertyValue;
                try {
                    _verifyPropertyValue = _configuration.getAttributeAsBoolean(_verifyPropertyName);
                } catch (final ConfigurationException exception) {
                    if (!_verifyPropertyName.equals(Property.Name.VERIFY)) {
                        _verifyPropertyName = Property.Name.VERIFY;
                        _verifyPropertyValue =
                            _configuration.getAttributeAsBoolean(
                                _verifyPropertyName,
                                Property.DefaultValue.VERIFY
                            );
                    } else {
                        _verifyPropertyValue = Property.DefaultValue.VERIFY;
                    }
                }
                String _fromPropertyName =
                    _configuration.getAttribute(
                        "notify.cloud.email.from.property.name", Property.Name.FROM
                    );
                String _fromPropertyValue;
                try {
                    _fromPropertyValue = _configuration.getAttribute(_fromPropertyName);
                } catch (final ConfigurationException exception) {
                    if (!_fromPropertyName.equals(Property.Name.FROM)) {
                        _fromPropertyName = Property.Name.FROM;
                        _fromPropertyValue =
                            _configuration.getAttribute(
                                _fromPropertyName,
                                Property.DefaultValue.FROM
                            );
                    } else {
                        _fromPropertyValue = Property.DefaultValue.FROM;
                    }
                }
                String _schemePropertyName =
                    _configuration.getAttribute(
                        "notify.cloud.email.scheme.property.name", Property.Name.SCHEME
                    );
                String _schemePropertyValue;
                try {
                    _schemePropertyValue = _configuration.getAttribute(_schemePropertyName);
                } catch (final ConfigurationException exception) {
                    if (!_schemePropertyName.equals(Property.Name.SCHEME)) {
                        _schemePropertyName = Property.Name.SCHEME;
                        _schemePropertyValue =
                            _configuration.getAttribute(
                                _schemePropertyName,
                                _securityPropertyValue.equalsIgnoreCase(Security.SSL) ||
                                _securityPropertyValue.equalsIgnoreCase(Security.TLS) ?
                                    Property.DefaultValue.SECURE_SCHEME :
                                    Property.DefaultValue.SCHEME
                            );
                    } else {
                        _schemePropertyValue =
                            _securityPropertyValue.equalsIgnoreCase(Security.SSL) ||
                            _securityPropertyValue.equalsIgnoreCase(Security.TLS) ?
                                Property.DefaultValue.SECURE_SCHEME :
                                Property.DefaultValue.SCHEME;
                    }
                }
                String _hostPropertyName =
                    _configuration.getAttribute(
                        "notify.cloud.email.host.property.name", Property.Name.HOST
                    );
                String _hostPropertyValue;
                try {
                    _hostPropertyValue = _configuration.getAttribute(_hostPropertyName);
                } catch (final ConfigurationException exception) {
                    if (!_hostPropertyName.equals(Property.Name.HOST)) {
                        _hostPropertyName = Property.Name.HOST;
                        _hostPropertyValue =
                            _configuration.getAttribute(
                                _hostPropertyName,
                                Property.DefaultValue.HOST
                            );
                    } else {
                        _hostPropertyValue = Property.DefaultValue.HOST;
                    }
                }
                String _portPropertyName =
                    _configuration.getAttribute(
                        "notify.cloud.email.port.property.name", Property.Name.PORT
                    );
                int _portPropertyValue;
                try {
                    _portPropertyValue = _configuration.getAttributeAsInteger(_portPropertyName);
                } catch (final ConfigurationException exception) {
                    if (!_portPropertyName.equals(Property.Name.PORT)) {
                        _portPropertyName = Property.Name.PORT;
                        _portPropertyValue =
                            _configuration.getAttributeAsInteger(
                                _portPropertyName,
                                _securityPropertyValue.equalsIgnoreCase(Security.SSL) ||
                                _securityPropertyValue.equalsIgnoreCase(Security.TLS) ?
                                    Property.DefaultValue.SECURE_PORT :
                                    Property.DefaultValue.PORT
                            );
                    } else {
                        _portPropertyValue =
                            _securityPropertyValue.equalsIgnoreCase(Security.SSL) ||
                            _securityPropertyValue.equalsIgnoreCase(Security.TLS) ?
                                Property.DefaultValue.SECURE_PORT :
                                Property.DefaultValue.PORT;
                    }
                }
                String _userNamePropertyName =
                    _configuration.getAttribute(
                        "notify.cloud.email.userName.property.name", Property.Name.USER_NAME
                    );
                String _userNamePropertyValue;
                try {
                    _userNamePropertyValue = _configuration.getAttribute(_userNamePropertyName);
                } catch (final ConfigurationException exception) {
                    if (!_userNamePropertyName.equals(Property.Name.USER_NAME)) {
                        _userNamePropertyName = Property.Name.USER_NAME;
                        _userNamePropertyValue =
                            _configuration.getAttribute(
                                _userNamePropertyName,
                                Property.DefaultValue.USER_NAME
                            );
                    } else {
                        _userNamePropertyValue = Property.DefaultValue.USER_NAME;
                    }
                }
                String _passwordPropertyName =
                    _configuration.getAttribute(
                        "notify.cloud.email.password.property.name", Property.Name.PASSWORD
                    );
                String _passwordPropertyValue;
                try {
                    _passwordPropertyValue = _configuration.getAttribute(_passwordPropertyName);
                } catch (final ConfigurationException exception) {
                    if (!_passwordPropertyName.equals(Property.Name.PASSWORD)) {
                        _passwordPropertyName = Property.Name.PASSWORD;
                        _passwordPropertyValue =
                            _configuration.getAttribute(
                                _passwordPropertyName,
                                Property.DefaultValue.PASSWORD
                            );
                    } else {
                        _passwordPropertyValue = Property.DefaultValue.PASSWORD;
                    }
                }
                String _debugPropertyName =
                    _configuration.getAttribute(
                        "notify.cloud.email.debug.property.name", Property.Name.DEBUG
                    );
                boolean _debugPropertyValue;
                try {
                    _debugPropertyValue = _configuration.getAttributeAsBoolean(_debugPropertyName);
                } catch (final ConfigurationException exception) {
                    if (!_debugPropertyName.equals(Property.Name.DEBUG)) {
                        _debugPropertyName = Property.Name.DEBUG;
                        _debugPropertyValue =
                            _configuration.getAttributeAsBoolean(
                                _debugPropertyName,
                                Property.DefaultValue.DEBUG
                            );
                    } else {
                        _debugPropertyValue = Property.DefaultValue.DEBUG;
                    }
                }
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(
                        Level.FINE,
                        "\r\n" +
                        "\r\n" +
                        "E-mail Notification Provider configuration: \r\n" +
                        "-    " + _enabledPropertyName + " = " + _enabledPropertyValue + "\r\n" +
                        "-    " + _securityPropertyName + " = " + _securityPropertyValue + "\r\n" +
                        "-    " + _verifyPropertyName + " = " + _verifyPropertyValue + "\r\n" +
                        "-    " + _fromPropertyName + " = " + _fromPropertyValue + "\r\n" +
                        "-    " + _schemePropertyName + " = " + _schemePropertyValue + "\r\n" +
                        "-    " + _hostPropertyName + " = " + _hostPropertyValue + "\r\n" +
                        "-    " + _portPropertyName + " = " + _portPropertyValue + "\r\n" +
                        "-    " + _userNamePropertyName + " = " + _userNamePropertyValue + "\r\n" +
                        "-    " + _passwordPropertyName + " = " + _passwordPropertyValue.replaceAll(".", "*") + "\r\n" +
                        "-    " + _debugPropertyName + " = " + _debugPropertyValue + "\r\n"
                    );
                }
                if (!_enabledPropertyValue) {
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.log(
                            Level.INFO,
                            "E-mail Notification Provider is disabled by configuration."
                        );
                    }
                    return;
                }
                try {
                    Class.forName("javax.mail.Message");
                    if (isNullOrIsEmpty(_fromPropertyValue)) {
                        if (LOGGER.isLoggable(Level.INFO)) {
                            LOGGER.log(
                                Level.INFO,
                                "E-mail Notification Provider is off.  (Missing " + _fromPropertyName + ")"
                            );
                        }
                        return;
                    }
                    if (isNullOrIsEmpty(_hostPropertyValue)) {
                        if (LOGGER.isLoggable(Level.INFO)) {
                            LOGGER.log(
                                Level.INFO,
                                "E-mail Notification Provider is off.  (Missing " + _hostPropertyName + ")"
                            );
                        }
                        return;
                    }
                    if (isNullOrIsEmpty(_securityPropertyValue)) {
                        if (LOGGER.isLoggable(Level.INFO)) {
                            LOGGER.log(
                                Level.INFO,
                                "E-mail Notification Provider is off.  (Missing " + _securityPropertyName + ")"
                            );
                        }
                        return;
                    }
                    if (isNullOrIsEmpty(_schemePropertyValue)) {
                        if (LOGGER.isLoggable(Level.INFO)) {
                            LOGGER.log(
                                Level.INFO,
                                "E-mail Notification Provider is off.  (Missing " + _schemePropertyName + ")"
                            );
                        }
                        return;
                    }
                    if (isNullOrIsEmpty(_userNamePropertyValue)) {
                        if (LOGGER.isLoggable(Level.INFO)) {
                            LOGGER.log(
                                Level.INFO,
                                "E-mail Notification Provider is off.  (Missing " + _userNamePropertyName + ")"
                            );
                        }
                        return;
                    }
                    if (isNullOrIsEmpty(_passwordPropertyValue)) {
                        if (LOGGER.isLoggable(Level.INFO)) {
                            LOGGER.log(
                                Level.INFO,
                                "E-mail Notification Provider is off.  (Missing " + _passwordPropertyName + ")"
                            );
                        }
                        return;
                    }
                    setNotificationProvider(
                        new EmailNotificationProvider(
                            _fromPropertyValue,
                            _schemePropertyValue,
                            _hostPropertyValue,
                            _portPropertyValue,
                            _userNamePropertyValue,
                            _passwordPropertyValue,
                            _securityPropertyValue,
                            _verifyPropertyValue,
                            _debugPropertyValue
                        )
                    );
                    super.run();
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.log(Level.INFO, "E-mail Notification Provider registered successfully.");
                    }
                } catch (final ClassNotFoundException exception) {
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.log(Level.INFO, "E-mail Notification Provider is off.  (Missing mail.jar)");
                    }
                }
            }
        }
    }
}
