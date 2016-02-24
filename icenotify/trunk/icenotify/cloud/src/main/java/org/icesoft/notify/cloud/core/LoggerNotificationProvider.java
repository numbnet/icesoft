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

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.icesoft.util.Configuration;
import org.icesoft.util.ConfigurationException;
import org.icesoft.util.SystemConfiguration;
import org.icesoft.util.servlet.ServletContextConfiguration;

public class LoggerNotificationProvider
extends AbstractNotificationProvider
implements NotificationProvider {
    private static final Logger LOGGER = Logger.getLogger(LoggerNotificationProvider.class.getName());

    private static final class Constant {
        private static final String PROTOCOL = "log";
    }

    private static final class Property {
        private static final class Name {
            private static final String ENABLED = "notify.cloud.logger.enabled";
        }
        private static class DefaultValue {
            private static final boolean ENABLED = true;
        }
    }

    private LoggerNotificationProvider() {
        super();
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(
                Level.FINE,
                "Initializing Logger Notification Provider."
            );
        }
    }

    public String getProtocol() {
        return Constant.PROTOCOL;
    }

    public void send(final Map<String, String> propertyMap, final String notifyBackURI) {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(
                Level.FINE,
                "Sending Logger Cloud Push Notification with " +
                    "Properties '" + propertyMap + "' to Notify-Back-URI '" + notifyBackURI + "'."
            );
        }
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(
                Level.INFO,
                "Successfully sent Logger Cloud Push Notification with " +
                    "Properties '" + propertyMap + "' to Notify-Back-URI '" + notifyBackURI + "'."
            );
        }
    }

    @Override
    public String toString() {
        return
            new StringBuilder().
                append("LoggerNotificationProvider[").
                    append(classMembersToString()).
                append("]").
                    toString();
    }

    protected String classMembersToString() {
        return
            new StringBuilder().
                toString();
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
                        "Logger Notification Provider unregistered successfully."
                    );
                }
            }
        }

        @Override
        public void contextInitialized(final ServletContextEvent event) {
            new Thread(
                new RegistrationTask(event.getServletContext()), "Logger Notification Provider set-up thread"
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
                        "notify.cloud.logger.enabled.property.name", Property.Name.ENABLED
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
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(
                        Level.FINE,
                        "\r\n" +
                        "\r\n" +
                        "Logger Notification Provider configuration: \r\n" +
                        "-    " + _enabledPropertyName + " = " + _enabledPropertyValue + "\r\n"
                    );
                }
                if (!_enabledPropertyValue) {
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.log(
                            Level.INFO,
                            "Logger Notification Provider is disabled by configuration."
                        );
                    }
                    return;
                }
                setNotificationProvider(
                    new LoggerNotificationProvider()
                );
                super.run();
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.log(
                        Level.INFO,
                        "Logger Notification Provider registered successfully."
                    );
                }
            }
        }
    }
}
