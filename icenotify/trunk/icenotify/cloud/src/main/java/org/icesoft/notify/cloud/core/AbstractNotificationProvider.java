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

import static org.icesoft.util.PreCondition.checkIfIsNotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.icesoft.util.Configuration;
import org.icesoft.util.ConfigurationException;
import org.icesoft.util.NameValuePair;
import org.icesoft.util.servlet.ExtensionRegistry;

public abstract class AbstractNotificationProvider
implements NotificationProvider {
    private static final Logger LOGGER = Logger.getLogger(AbstractNotificationProvider.class.getName());

    protected AbstractNotificationProvider() {
        // Do nothing.
    }

    public void registerTo(final CloudNotificationService cloudNotificationService)
    throws IllegalArgumentException {
        // throws IllegalArgumentException
        checkIfIsNotNull(
            cloudNotificationService, "Illegal argument cloudNotificationService: '" + cloudNotificationService + "'"
        ).registerNotificationProvider(getProtocol(), this);
    }

    public void unregisterFrom(final CloudNotificationService cloudNotificationService) {
        // throws IllegalArgumentException
        checkIfIsNotNull(
            cloudNotificationService, "Illegal argument cloudNotificationService: '" + cloudNotificationService + "'"
        ).unregisterNotificationProvider(getProtocol());
    }

    protected static abstract class AbstractAutoRegistration
    implements ServletContextListener {
        private static final Logger LOGGER = Logger.getLogger(AbstractAutoRegistration.class.getName());

        private NotificationProvider notificationProvider;

        public void contextDestroyed(final ServletContextEvent event) {
            if (hasNotificationProvider()) {
                ExtensionRegistry.unregisterExtension(
                    NotificationProvider.class.getName(),
                    getNotificationProvider(),
                    event.getServletContext()
                );
                setNotificationProvider(null);
            }
        }

        public void contextInitialized(final ServletContextEvent event) {
            // Do nothing.
        }

        protected final NotificationProvider getNotificationProvider() {
            return notificationProvider;
        }

        protected final boolean hasNotificationProvider() {
            return getNotificationProvider() != null;
        }

        protected final void setNotificationProvider(final NotificationProvider notificationProvider) {
            this.notificationProvider = notificationProvider;
        }

        protected abstract class AbstractRegistrationTask
        implements Runnable {
            private final ServletContext servletContext;

            protected AbstractRegistrationTask(final ServletContext servletContext) {
                this.servletContext = servletContext;
            }

            public void run() {
                if (hasNotificationProvider()) {
                    ExtensionRegistry.registerExtension(
                        NotificationProvider.class.getName(),
                        getNotificationProvider(),
                        10,
                        getServletContext()
                    );
                }
            }

            protected void awaitSignal() {
                CloudNotificationService.getSetUpLock(getServletContext()).lock();
                try {
                    try {
                        CloudNotificationService.getSetUpCondition(getServletContext()).await();
                    } catch (final InterruptedException exception) {
                        // Do nothing.
                    }
                } finally {
                    CloudNotificationService.getSetUpLock(getServletContext()).unlock();
                }
            }

            protected NameValuePair<String, String> getNameValuePair(
                final Configuration configuration, final String nameIdentifier, final String defaultName,
                final String defaultValue) {

                String _name = configuration.getAttribute(nameIdentifier, defaultName);
                String _value;
                try {
                    _value = configuration.getAttribute(_name);
                } catch (final ConfigurationException exception) {
                    if (!_name.equals(defaultName)) {
                        _name = defaultName;
                        _value = configuration.getAttribute(_name, defaultValue);
                    } else {
                        _value = defaultValue;
                    }
                }
                return new NameValuePair<String, String>(_name, _value);
            }

            protected NameValuePair<String, Boolean> getNameBooleanValuePair(
                final Configuration configuration, final String nameIdentifier, final String defaultName,
                final boolean defaultValue) {

                String _name = configuration.getAttribute(nameIdentifier, defaultName);
                boolean _value;
                try {
                    _value = configuration.getAttributeAsBoolean(_name);
                } catch (final ConfigurationException exception) {
                    if (!_name.equals(defaultName)) {
                        _name = defaultName;
                        _value = configuration.getAttributeAsBoolean(_name, defaultValue);
                    } else {
                        _value = defaultValue;
                    }
                }
                return new NameValuePair<String, Boolean>(_name, _value);
            }

            protected NameValuePair<String, Integer> getNameIntegerValuePair(
                final Configuration configuration, final String nameIdentifier, final String defaultName,
                final int defaultValue) {

                String _name = configuration.getAttribute(nameIdentifier, defaultName);
                int _value;
                try {
                    _value = configuration.getAttributeAsInteger(_name);
                } catch (final ConfigurationException exception) {
                    if (!_name.equals(defaultName)) {
                        _name = defaultName;
                        _value = configuration.getAttributeAsInteger(_name, defaultValue);
                    } else {
                        _value = defaultValue;
                    }
                }
                return new NameValuePair<String, Integer>(_name, _value);
            }

            protected final ServletContext getServletContext() {
                return servletContext;
            }
        }
    }
}
