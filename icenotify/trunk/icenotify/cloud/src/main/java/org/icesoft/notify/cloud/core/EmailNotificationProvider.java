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
            private static final String DEBUG = "com.icesoft.notify.cloud.email.debug";
            private static final String ENABLED = "com.icesoft.notify.cloud.email.enabled";
            private static final String FROM = "com.icesoft.notify.cloud.email.from";
            private static final String HOST = "com.icesoft.notify.cloud.email.host";
            private static final String PASSWORD = "com.icesoft.notify.cloud.email.password";
            private static final String PORT = "com.icesoft.notify.cloud.email.port";
            private static final String SCHEME = "com.icesoft.notify.cloud.email.scheme";
            private static final String SECURITY = "com.icesoft.notify.cloud.email.security";
            private static final String USER_NAME = "com.icesoft.notify.cloud.email.userName";
            private static final String VERIFY = "com.icesoft.notify.cloud.email.verify";
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
        final InternetAddress from, final String scheme, final String host, final int port, final String userName,
        final String password, final String security, final boolean verify, final boolean debug) {

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
        this.from = from;
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
            ServletContext _servletContext = event.getServletContext();
            Configuration _configuration =
                new SystemConfiguration(
                    new ServletContextConfiguration(
                        _servletContext
                    )
                );
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(
                    Level.FINE,
                    "\r\n" +
                    "\r\n" +
                    "E-mail Notification Provider configuration: \r\n" +
                    "-    " + Property.Name.ENABLED + " = " + _configuration.getAttributeAsBoolean(Property.Name.ENABLED, Property.DefaultValue.ENABLED) + "\r\n" +
                    "-    " + Property.Name.SECURITY + " = " + _configuration.getAttribute(Property.Name.SECURITY, Property.DefaultValue.SECURITY) + "\r\n" +
                    "-    " + Property.Name.VERIFY + " = " + _configuration.getAttributeAsBoolean(Property.Name.VERIFY, Property.DefaultValue.VERIFY) + "\r\n" +
                    "-    " + Property.Name.FROM + " = " + _configuration.getAttribute(Property.Name.FROM, Property.DefaultValue.FROM) + "\r\n" +
                    "-    " + Property.Name.SCHEME + " = " + _configuration.getAttribute(Property.Name.SCHEME, Property.DefaultValue.SCHEME) + "\r\n" +
                    "-    " + Property.Name.HOST + " = " + _configuration.getAttribute(Property.Name.HOST, Property.DefaultValue.HOST) + "\r\n" +
                    "-    " + Property.Name.PORT + " = " + _configuration.getAttributeAsInteger(Property.Name.PORT, Property.DefaultValue.PORT) + "\r\n" +
                    "-    " + Property.Name.USER_NAME + " = " + _configuration.getAttribute(Property.Name.USER_NAME, Property.DefaultValue.USER_NAME) + "\r\n" +
                    "-    " + Property.Name.PASSWORD + " = " + _configuration.getAttribute(Property.Name.PASSWORD, Property.DefaultValue.PASSWORD).replaceAll(".", "*") + "\r\n" +
                    "-    " + Property.Name.DEBUG + " = " + _configuration.getAttributeAsBoolean(Property.Name.DEBUG, Property.DefaultValue.DEBUG) + "\r\n"
                );
            }
            if (!_configuration.getAttributeAsBoolean(Property.Name.ENABLED, Property.DefaultValue.ENABLED)) {
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
                try {
                    InternetAddress _from =
                        new InternetAddress(
                            _configuration.getAttribute(Property.Name.FROM, Property.DefaultValue.FROM)
                        );
                    String _host =
                        _configuration.getAttribute(Property.Name.HOST, Property.DefaultValue.HOST);
                    if (isNullOrIsEmpty(_host)) {
                        if (LOGGER.isLoggable(Level.INFO)) {
                            LOGGER.log(
                                Level.INFO,
                                "E-mail Notification Provider is off.  (Missing " + Property.Name.HOST + ")"
                            );
                        }
                        return;
                    }
                    String _security =
                        _configuration.getAttribute(Property.Name.SECURITY, Property.DefaultValue.SECURITY);
                    if (isNullOrIsEmpty(_security)) {
                        if (LOGGER.isLoggable(Level.INFO)) {
                            LOGGER.log(
                                Level.INFO,
                                "E-mail Notification Provider is off.  (Missing " + Property.Name.SECURITY + ")"
                            );
                        }
                        return;
                    }
                    String _scheme;
                    int _port;
                    if (_security.equalsIgnoreCase(Security.SSL) || _security.equalsIgnoreCase(Security.TLS)) {
                        _scheme =
                            _configuration.getAttribute(Property.Name.SCHEME, Property.DefaultValue.SECURE_SCHEME);
                        _port =
                            _configuration.getAttributeAsInteger(Property.Name.PORT, Property.DefaultValue.SECURE_PORT);
                    } else {
                        _scheme =
                            _configuration.getAttribute(Property.Name.SCHEME, Property.DefaultValue.SCHEME);
                        _port =
                            _configuration.getAttributeAsInteger(Property.Name.PORT, Property.DefaultValue.PORT);
                    }
                    if (isNullOrIsEmpty(_scheme)) {
                        if (LOGGER.isLoggable(Level.INFO)) {
                            LOGGER.log(
                                Level.INFO,
                                "E-mail Notification Provider is off.  (Missing " + Property.Name.SCHEME + ")"
                            );
                        }
                        return;
                    }
                    String _userName =
                        _configuration.getAttribute(Property.Name.USER_NAME, Property.DefaultValue.USER_NAME);
                    if (isNullOrIsEmpty(_userName)) {
                        if (LOGGER.isLoggable(Level.INFO)) {
                            LOGGER.log(
                                Level.INFO,
                                "E-mail Notification Provider is off.  (Missing " + Property.Name.USER_NAME + ")"
                            );
                        }
                        return;
                    }
                    String _password =
                        _configuration.getAttribute(Property.Name.PASSWORD, Property.DefaultValue.PASSWORD);
                    if (isNullOrIsEmpty(_password)) {
                        if (LOGGER.isLoggable(Level.INFO)) {
                            LOGGER.log(
                                Level.INFO,
                                "E-mail Notification Provider is off.  (Missing " + Property.Name.PASSWORD + ")"
                            );
                        }
                        return;
                    }
                    boolean _debug =
                        _configuration.getAttributeAsBoolean(Property.Name.DEBUG, Property.DefaultValue.DEBUG);
                    boolean _verify =
                        _configuration.getAttributeAsBoolean(Property.Name.VERIFY, Property.DefaultValue.VERIFY);
                    setNotificationProvider(
                        new EmailNotificationProvider(
                            _from, _scheme, _host, _port, _userName, _password, _security, _verify, _debug
                        )
                    );
                    super.contextInitialized(event);
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.log(Level.INFO, "E-mail Notification Provider registered successfully.");
                    }
                } catch (final AddressException exception) {
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.log(
                            Level.INFO,
                            "E-mail Notification Provider is off.  " +
                                "(" +
                                    "Parse failed for " +
                                        "'" +
                                            _configuration.
                                                getAttribute(Property.Name.FROM, Property.DefaultValue.FROM) +
                                        "'" +
                                ")"
                        );
                    }
                }
            } catch (final ClassNotFoundException exception) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.log(Level.INFO, "E-mail Notification Provider is off.  (Missing mail.jar)");
                }
            }
        }
    }
}
