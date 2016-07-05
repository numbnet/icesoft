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

import static org.icesoft.util.StringUtilities.getValue;
import static org.icesoft.util.StringUtilities.isNotNullAndIsNotEmpty;
import static org.icesoft.util.StringUtilities.isNullOrIsEmpty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
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
import org.icesoft.util.NameValuePair;
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
        private static final class NameIdentifier {
            private static final String DEBUG = "com.icesoft.notify.cloud.email.debug.property.name";
            private static final String ENABLED = "com.icesoft.notify.cloud.email.enabled.property.name";
            private static final String FROM = "com.icesoft.notify.cloud.email.from.property.name";
            private static final String HOST = "com.icesoft.notify.cloud.email.host.property.name";
            private static final String PASSWORD = "com.icesoft.notify.cloud.email.password.property.name";
            private static final String PORT = "com.icesoft.notify.cloud.email.port.property.name";
            private static final String SCHEME = "com.icesoft.notify.cloud.email.scheme.property.name";
            private static final String SECURITY = "com.icesoft.notify.cloud.email.security.property.name";
            private static final String USER_NAME = "com.icesoft.notify.cloud.email.userName.property.name";
            private static final String VERIFY = "com.icesoft.notify.cloud.email.verify.property.name";
        }
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

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss (z)");
    {
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
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
                        "Verify: '" + verify + "', " +
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

    public void send(final Map<String, String> propertyMap, final Set<String> notifyBackURISet) {
        send(propertyMap, notifyBackURISet, 3, 1000);
    }

    public void send(final Map<String, String> propertyMap, final String notifyBackURI) {
        send(propertyMap, new HashSet<String>(Arrays.asList(notifyBackURI)), 3, 1000);
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
                append("password: '").append(getPassword().replaceAll(".", "*")).append("', ").
                append("port: '").append(getPort()).append("', ").
                append("scheme: '").append(getScheme()).append("', ").
                append("session: '").append(getSession()).append("', ").
                append("userName: ").append(getUserName()).append("'").
                    toString();
    }

    protected DateFormat getDateFormat() {
        return dateFormat;
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

    protected void send(
        final Map<String, String> propertyMap, final Set<String> notifyBackURISet, final int retries,
        final long delay) {

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(
                Level.FINE,
                "Sending E-mail Cloud Push Notification with " +
                    "Properties '" + propertyMap + "' to Notify-Back-URIs '" + notifyBackURISet + "'."
            );
        }
        try {
            String _content = null;
            String _contentType = null;
            String _template = propertyMap.get(NotificationProvider.Property.Name.TEMPLATE);
            if (isNotNullAndIsNotEmpty(_template)) {
                try {
                    URLConnection _urlConnection = new URL(_template).openConnection();
                    BufferedReader _in = new BufferedReader(new InputStreamReader(_urlConnection.getInputStream()));
                    StringBuilder _entityBody = new StringBuilder();
                    String _line;
                    while ((_line = _in.readLine()) != null) {
                        _entityBody.append(_line);
                    }
                    _in.close();
                    _content =
                        _entityBody.toString().
                            replace(
                                "{{subject}}",
                                propertyMap.get(NotificationProvider.Property.Name.SUBJECT)
                            ).
                            replace(
                                "{{detail}}",
                                propertyMap.get(NotificationProvider.Property.Name.DETAIL)
                            ).
                            replace(
                                "{{priority}}",
                                getValue(
                                    propertyMap.get(NotificationProvider.Property.Name.PRIORITY),
                                    "default"
                                )
                            ).
                            replace(
                                "{{time}}",
                                getValue(
                                    propertyMap.get(NotificationProvider.Property.Name.TIME),
                                    getDateFormat().format(new Date(System.currentTimeMillis()))
                                )
                            ).
                            replace(
                                "{{url}}",
                                propertyMap.get(NotificationProvider.Property.Name.URL)
                            );
                    _contentType = "text/html";
                } catch (final MalformedURLException exception) {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(
                            Level.WARNING,
                            "Malformed URL for Template: '" + _template + "'.  Reverting to plain text format."
                        );
                    }
                } catch (final IOException exception) {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(
                            Level.WARNING,
                            "An I/O error occurred: '" + exception.getMessage() + "'.  Reverting to plain text format."
                        );
                    }
                }
            }
            if (isNullOrIsEmpty(_content) && isNullOrIsEmpty(_contentType)) {
                _content =
                    new StringBuilder().
                        append(propertyMap.get(NotificationProvider.Property.Name.DETAIL)).
                        append(" [").append(propertyMap.get(NotificationProvider.Property.Name.URL)).append("]").
                            toString();
                _contentType = "text/plain";
            }
            MimeMessage _mimeMessage = new MimeMessage(getSession());
            _mimeMessage.setFrom(getFrom());
            _mimeMessage.setSubject(propertyMap.get(NotificationProvider.Property.Name.SUBJECT));
            _mimeMessage.setContent(_content, _contentType);
            if (notifyBackURISet.size() == 1) {
                Set<InternetAddress> _toSet = new HashSet<InternetAddress>();
                for (final String _notifyBackURI : notifyBackURISet) {
                    _toSet.add(new InternetAddress(_notifyBackURI.substring("mailto:".length())));
                }
                _mimeMessage.setRecipients(
                    Message.RecipientType.TO, _toSet.toArray(new InternetAddress[_toSet.size()])
                );
            } else {
                Set<InternetAddress> _bccSet = new HashSet<InternetAddress>();
                for (final String _notifyBackURI : notifyBackURISet) {
                    _bccSet.add(new InternetAddress(_notifyBackURI.substring("mailto:".length())));
                }
                _mimeMessage.setRecipients(
                    Message.RecipientType.BCC, _bccSet.toArray(new InternetAddress[_bccSet.size()])
                );
            }
            // throws NoSuchProviderException
            Transport _transport = getSession().getTransport(getScheme());
            // throws IllegalStateException, AuthenticationFailedException, MessagingException
            _transport.connect(getHost(), getPort(), getUserName(), getPassword());
            // throws SendFailedException, MessagingException
            _transport.sendMessage(_mimeMessage, _mimeMessage.getAllRecipients());
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(
                    Level.FINE,
                    "Successfully sent E-mail Cloud Push Notification with " +
                        "Properties '" + propertyMap + "' to Notify-Back-URIs '" + notifyBackURISet + "'."
                );
            }
        } catch (final MessagingException exception) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(
                    Level.WARNING,
                    "Failed to send E-mail Cloud Push Notification with " +
                        "Properties '" + propertyMap + "' to Notify-Back-URIs '" + notifyBackURISet + "'.",
                    exception
                );
            }
        }
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
                    LOGGER.log(
                        Level.INFO,
                        "E-mail Notification Provider unregistered successfully."
                    );
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
                NameValuePair<String, Boolean> _enabled =
                    getNameBooleanValuePair(
                        _configuration,
                        Property.NameIdentifier.ENABLED,
                        Property.Name.ENABLED,
                        Property.DefaultValue.ENABLED
                    );
                NameValuePair<String, String> _security =
                    getNameValuePair(
                        _configuration,
                        Property.NameIdentifier.SECURITY,
                        Property.Name.SECURITY,
                        Property.DefaultValue.SECURITY
                    );
                NameValuePair<String, Boolean> _verify =
                    getNameBooleanValuePair(
                        _configuration,
                        Property.NameIdentifier.VERIFY,
                        Property.Name.VERIFY,
                        Property.DefaultValue.VERIFY
                    );
                NameValuePair<String, String> _from =
                    getNameValuePair(
                        _configuration,
                        Property.NameIdentifier.FROM,
                        Property.Name.FROM,
                        Property.DefaultValue.FROM
                    );
                NameValuePair<String, String> _scheme =
                    getNameValuePair(
                        _configuration,
                        Property.NameIdentifier.SCHEME,
                        Property.Name.SCHEME,
                        _security.getValue().equalsIgnoreCase(Security.SSL) ||
                        _security.getValue().equalsIgnoreCase(Security.TLS) ?
                            Property.DefaultValue.SECURE_SCHEME :
                            Property.DefaultValue.SCHEME
                    );
                NameValuePair<String, String> _host =
                    getNameValuePair(
                        _configuration,
                        Property.NameIdentifier.HOST,
                        Property.Name.HOST,
                        Property.DefaultValue.HOST
                    );
                NameValuePair<String, Integer> _port =
                    getNameIntegerValuePair(
                        _configuration,
                        Property.NameIdentifier.PORT,
                        Property.Name.PORT,
                        _security.getValue().equalsIgnoreCase(Security.SSL) ||
                        _security.getValue().equalsIgnoreCase(Security.TLS) ?
                            Property.DefaultValue.SECURE_PORT :
                            Property.DefaultValue.PORT
                    );
                NameValuePair<String, String> _userName =
                    getNameValuePair(
                        _configuration,
                        Property.NameIdentifier.USER_NAME,
                        Property.Name.USER_NAME,
                        Property.DefaultValue.USER_NAME
                    );
                NameValuePair<String, String> _password =
                    getNameValuePair(
                        _configuration,
                        Property.NameIdentifier.PASSWORD,
                        Property.Name.PASSWORD,
                        Property.DefaultValue.PASSWORD
                    );
                NameValuePair<String, Boolean> _debug =
                    getNameBooleanValuePair(
                        _configuration,
                        Property.NameIdentifier.DEBUG,
                        Property.Name.DEBUG,
                        Property.DefaultValue.DEBUG
                    );
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(
                        Level.FINE,
                        "\r\n" +
                        "\r\n" +
                        "E-mail Notification Provider configuration: \r\n" +
                        "-    " + _enabled.getName() + " = " + _enabled.getValue() +
                            (_enabled.getValue().equals(Property.DefaultValue.ENABLED) ? " [default]" : "") +
                                "\r\n" +
                        "-    " + _security.getName() + " = " + _security.getValue() +
                            (_security.getValue().equals(Property.DefaultValue.SECURITY) ? " [default]" : "") +
                                "\r\n" +
                        "-    " + _verify.getName() + " = " + _verify.getValue() +
                            (_verify.getValue().equals(Property.DefaultValue.VERIFY) ? " [default]" : "") +
                                "\r\n" +
                        "-    " + _from.getName() + " = " + _from.getValue() +
                            (_from.getValue().equals(Property.DefaultValue.FROM) ? " [default]" : "") +
                                "\r\n" +
                        "-    " + _scheme.getName() + " = " + _scheme.getValue() +
                            (
                                _scheme.getValue().equals(
                                    _security.getValue().equalsIgnoreCase(Security.SSL) ||
                                    _security.getValue().equalsIgnoreCase(Security.TLS) ?
                                        Property.DefaultValue.SECURE_SCHEME :
                                        Property.DefaultValue.SCHEME
                                ) ?
                                    " [default]" : ""
                            ) +
                                "\r\n" +
                        "-    " + _host.getName() + " = " + _host.getValue() +
                            (_host.getValue().equals(Property.DefaultValue.HOST) ? " [default]" : "") +
                                "\r\n" +
                        "-    " + _port.getName() + " = " + _port.getValue() +
                            (
                                _port.getValue().equals(
                                    _security.getValue().equalsIgnoreCase(Security.SSL) ||
                                    _security.getValue().equalsIgnoreCase(Security.TLS) ?
                                        Property.DefaultValue.SECURE_PORT :
                                        Property.DefaultValue.PORT
                                ) ?
                                    " [default]" : ""
                            ) +
                                "\r\n" +
                        "-    " + _userName.getName() + " = " + _userName.getValue() +
                            (_userName.getValue().equals(Property.DefaultValue.USER_NAME) ? " [default]" : "") +
                                "\r\n" +
                        "-    " + _password.getName() + " = " + _password.getValue().replaceAll(".", "*") +
                            (_password.getValue().equals(Property.DefaultValue.PASSWORD) ? " [default]" : "") +
                                "\r\n" +
                        "-    " + _debug.getName() + " = " + _debug.getValue() +
                            (_debug.getValue().equals(Property.DefaultValue.DEBUG) ? " [default]" : "") +
                                "\r\n"
                    );
                }
                if (!_enabled.getValue()) {
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
                    if (isNullOrIsEmpty(_from.getValue())) {
                        if (LOGGER.isLoggable(Level.INFO)) {
                            LOGGER.log(
                                Level.INFO,
                                "E-mail Notification Provider is off.  (Missing " + _from.getName() + ")"
                            );
                        }
                        return;
                    }
                    if (isNullOrIsEmpty(_host.getValue())) {
                        if (LOGGER.isLoggable(Level.INFO)) {
                            LOGGER.log(
                                Level.INFO,
                                "E-mail Notification Provider is off.  (Missing " + _host.getName() + ")"
                            );
                        }
                        return;
                    }
                    if (isNullOrIsEmpty(_security.getValue())) {
                        if (LOGGER.isLoggable(Level.INFO)) {
                            LOGGER.log(
                                Level.INFO,
                                "E-mail Notification Provider is off.  (Missing " + _security.getName() + ")"
                            );
                        }
                        return;
                    }
                    if (isNullOrIsEmpty(_scheme.getValue())) {
                        if (LOGGER.isLoggable(Level.INFO)) {
                            LOGGER.log(
                                Level.INFO,
                                "E-mail Notification Provider is off.  (Missing " + _scheme.getName() + ")"
                            );
                        }
                        return;
                    }
                    if (isNullOrIsEmpty(_userName.getValue())) {
                        if (LOGGER.isLoggable(Level.INFO)) {
                            LOGGER.log(
                                Level.INFO,
                                "E-mail Notification Provider is off.  (Missing " + _userName.getName() + ")"
                            );
                        }
                        return;
                    }
                    if (isNullOrIsEmpty(_password.getValue())) {
                        if (LOGGER.isLoggable(Level.INFO)) {
                            LOGGER.log(
                                Level.INFO,
                                "E-mail Notification Provider is off.  (Missing " + _password.getName() + ")"
                            );
                        }
                        return;
                    }
                    setNotificationProvider(
                        new EmailNotificationProvider(
                            _from.getValue(),
                            _scheme.getValue(),
                            _host.getValue(),
                            _port.getValue(),
                            _userName.getValue(),
                            _password.getValue(),
                            _security.getValue(),
                            _verify.getValue(),
                            _debug.getValue()
                        )
                    );
                    super.run();
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.log(
                            Level.INFO,
                            "E-mail Notification Provider registered successfully."
                        );
                    }
                } catch (final ClassNotFoundException exception) {
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.log(
                            Level.INFO,
                            "E-mail Notification Provider is off.  " +
                                "(Missing mail.jar)"
                        );
                    }
                }
            }
        }
    }
}
