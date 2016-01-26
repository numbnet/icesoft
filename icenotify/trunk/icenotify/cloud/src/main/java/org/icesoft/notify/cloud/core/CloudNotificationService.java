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
import static org.icesoft.util.PreCondition.checkIfIsNotNullAndIsNotEmpty;

import java.net.ProtocolException;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

import org.icesoft.util.Configuration;
import org.icesoft.util.SystemConfiguration;
import org.icesoft.util.concurrent.NamedThreadFactory;
import org.icesoft.util.servlet.ExtensionRegistry;
import org.icesoft.util.servlet.Service;
import org.icesoft.util.servlet.ServletContextConfiguration;

public class CloudNotificationService
implements Service {
    private static final Logger LOGGER = Logger.getLogger(CloudNotificationService.class.getName());

    private static class Property {
        private static class Name {
            private static final String CORE_POOL_SIZE = "com.icesoft.notify.cloud.corePoolSize";
            private static final String KEEP_ALIVE_TIME = "com.icesoft.notify.cloud.keepAliveTime";
            private static final String MAXIMUM_POOL_SIZE = "com.icesoft.notify.cloud.maximumPoolSize";
        }
        private static class DefaultValue {
            private static final int CORE_POOL_SIZE = 10;
            private static final long KEEP_ALIVE_TIME = 900;
            private static final int MAXIMUM_POOL_SIZE = 25;
        }
    }
    private final Map<String, NotificationProvider> protocolToNotificationProviderMap =
        new HashMap<String, NotificationProvider>();

    private ExecutorService executorService;

    public CloudNotificationService(final ServletContext servletContext) {
        setUp(servletContext);
    }

    public void pushToNotifyBackURI(
        final String notifyBackURI, final Map<String, String> properties)
    throws IllegalArgumentException {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(
                Level.FINE,
                "Executing Push Task for Notify-Back-URI '" + notifyBackURI + "' with " +
                    "Properties '" + properties + "'."
            );
        }
        new PushTask(
            new HashSet<String>(
                Arrays.asList(
                    // throws IllegalArgumentException
                    checkIfIsNotNullAndIsNotEmpty(
                        notifyBackURI, "Illegal argument notifyBackURI: '" + notifyBackURI + "'"
                    )
                )
            ),
            // throws IllegalArgumentException
            checkIfIsNotNullAndIsNotEmpty(properties, "Illegal argument properties: '" + properties + "'"),
            getExecutorService(),
            this
        ).execute();
    }

    public void pushToNotifyBackURINow(
        final String notifyBackURI, final Map<String, String> properties)
    throws IllegalArgumentException {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(
                Level.FINE,
                "Executing Push Task for Notify-Back-URI '" + notifyBackURI + "' with " +
                    "Properties '" + properties + "' now."
            );
        }
        new PushTask(
            new HashSet<String>(
                Arrays.asList(
                    // throws IllegalArgumentException
                    checkIfIsNotNullAndIsNotEmpty(
                        notifyBackURI, "Illegal argument notifyBackURI: '" + notifyBackURI + "'"
                    )
                )
            ),
            // throws IllegalArgumentException
            checkIfIsNotNullAndIsNotEmpty(properties, "Illegal argument properties: '" + properties + "'"),
            getExecutorService(),
            this
        ).executeNow();
    }

    public void pushToNotifyBackURIs(
        final Set<String> notifyBackURISet, final Map<String, String> properties)
    throws IllegalArgumentException {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(
                Level.FINE,
                "Executing Push Task for Notify-Back-URIs '" + notifyBackURISet + "' with " +
                    "Properties '" + properties + "'."
            );
        }
        new PushTask(
            // throws IllegalArgumentException
            checkIfIsNotNullAndIsNotEmpty(
                notifyBackURISet, "Illegal argument notifyBackURISet: '" + notifyBackURISet + "'"
            ),
            // throws IllegalArgumentException
            checkIfIsNotNullAndIsNotEmpty(properties, "Illegal argument properties: '" + properties + "'"),
            getExecutorService(),
            this
        ).execute();
    }

    public void pushToNotifyBackURIsNow(
        final Set<String> notifyBackURISet, final Map<String, String> properties)
    throws IllegalArgumentException, NullPointerException {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(
                Level.FINE,
                "Executing Push Task for Notify-Back-URIs '" + notifyBackURISet + "' with " +
                    "Properties '" + properties + "' now."
            );
        }
        new PushTask(
            // throws IllegalArgumentException
            checkIfIsNotNullAndIsNotEmpty(
                notifyBackURISet, "Illegal argument notifyBackURISet: '" + notifyBackURISet + "'"
            ),
            // throws IllegalArgumentException
            checkIfIsNotNullAndIsNotEmpty(properties, "Illegal argument properties: '" + properties + "'"),
            getExecutorService(),
            this
        ).executeNow();
    }

    public void registerNotificationProvider(final String protocol, final NotificationProvider notificationProvider)
    throws IllegalArgumentException, NullPointerException {
        // throws IllegalArgumentException
        checkIfIsNotNullAndIsNotEmpty(protocol, "Illegal argument protocol: '" + protocol + "'");
        // throws NullPointerException
        checkIfIsNotNull(notificationProvider, "Illegal argument notificationProvider: '" + notificationProvider + "'");
        if (!getModifiableProtocolToNotificationProviderMap().containsKey(protocol)) {
            getModifiableProtocolToNotificationProviderMap().put(protocol, notificationProvider);
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(
                    Level.FINE,
                    "Successfully registered Notification Provider '" + notificationProvider + "' " +
                        "for Protocol '" + protocol + "'."
                );
            }
        } else {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(
                    Level.WARNING,
                    "Duplicate Notification Provider registration detected for Protocol '" + protocol + "'.  " +
                        "(" +
                            "Existing: '" + getModifiableProtocolToNotificationProviderMap().get(protocol) + "', " +
                            "Duplicate: '" + notificationProvider + "'" +
                        ")"
                );
            }
        }
    }

    public void setUp(final ServletContext servletContext)
    throws NullPointerException {
        // throws NullPointerException
        checkIfIsNotNull(servletContext, "Illegal argument servletContext: null");
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Setting up Notification Service.");
        }
        Configuration _configuration =
            new SystemConfiguration(
                new ServletContextConfiguration(
                    servletContext
                )
            );
        executorService =
            new ThreadPoolExecutor(
                _configuration.getAttributeAsInteger(
                    Property.Name.CORE_POOL_SIZE, Property.DefaultValue.CORE_POOL_SIZE
                ),
                _configuration.getAttributeAsInteger(
                    Property.Name.MAXIMUM_POOL_SIZE, Property.DefaultValue.MAXIMUM_POOL_SIZE
                ),
                _configuration.getAttributeAsLong(
                    Property.Name.KEEP_ALIVE_TIME, Property.DefaultValue.KEEP_ALIVE_TIME
                ),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new NamedThreadFactory("Notification Service"),
                new ThreadPoolExecutor.CallerRunsPolicy() {
                    @Override
                    public void rejectedExecution(
                        final Runnable runnable, final ThreadPoolExecutor threadPoolExecutor) {

                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.log(
                                Level.WARNING,
                                "Rejected execution of Runnable '" + runnable + "'.  " +
                                    "Trying to execute on calling thread."
                            );
                        }
                        super.rejectedExecution(runnable, threadPoolExecutor);
                    }
                }
            );
        Object[] _notificationProviders =
            ExtensionRegistry.getExtensions(NotificationProvider.class.getName(), servletContext);
        if (_notificationProviders != null) {
            for (final Object _notificationProvider : _notificationProviders) {
                ((NotificationProvider)_notificationProvider).registerTo(this);
            }
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Successfully set up Notification Service.");
        }
    }

    public void tearDown(final ServletContext servletContext)
    throws NullPointerException {
        // throws NullPointerException
        checkIfIsNotNull(servletContext, "Illegal argument servletContext: null");
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Tearing down Notification Service.");
        }
        Object[] _notificationProviders =
            ExtensionRegistry.getExtensions(NotificationProvider.class.getName(), servletContext);
        if (_notificationProviders != null) {
            for (final Object _notificationProvider : _notificationProviders) {
                ((NotificationProvider)_notificationProvider).unregisterFrom(this);
            }
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Successfully tore down Notification Service.");
        }
    }

    public void unregisterNotificationProvider(final String protocol)
    throws IllegalArgumentException, NullPointerException {
        // throws IllegalArgumentException
        checkIfIsNotNullAndIsNotEmpty(protocol, "Illegal argument protocol: '" + protocol + "'");
        if (!getModifiableProtocolToNotificationProviderMap().containsKey(protocol)) {
            getModifiableProtocolToNotificationProviderMap().remove(protocol);
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(
                    Level.FINE,
                    "Successfully registered Notification Provider for Protocol '" + protocol + "'."
                );
            }
        }
    }

    protected ExecutorService getExecutorService() {
        return executorService;
    }

    protected Map<String, NotificationProvider> getModifiableProtocolToNotificationProviderMap() {
        return protocolToNotificationProviderMap;
    }

    private NotificationProvider getNotificationProvider(final String notifyBackURI)
    throws ProtocolException {
        URI _notifyBackURI= URI.create(notifyBackURI);
        if (getModifiableProtocolToNotificationProviderMap().containsKey(_notifyBackURI.getScheme())) {
            return getModifiableProtocolToNotificationProviderMap().get(_notifyBackURI.getScheme());
        } else {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(
                    Level.FINE,
                    "Notification Provider for Protocol '" + _notifyBackURI.getScheme() + "' not found."
                );
            }
            throw new ProtocolException("Unknown Protocol '" + _notifyBackURI.getScheme() + "'.");
        }
    }

    protected static class PushTask
    implements Runnable {
        private static final Logger LOGGER = Logger.getLogger(PushTask.class.getName());

        private final Lock futureLock = new ReentrantLock();
        private final Set<String> notifyBackURISet = new HashSet<String>();
        private final Map<String, String> propertyMap = new HashMap<String, String>();

        private final ExecutorService executorService;
        private final CloudNotificationService cloudNotificationService;

        private Future future;

        protected PushTask(
            final Set<String> notifyBackURISet, final Map<String, String> propertyMap,
            final ExecutorService executorService, final CloudNotificationService cloudNotificationService) {

            getModifiableNotifyBackURISet().addAll(notifyBackURISet);
            getModifiablePropertyMap().putAll(propertyMap);
            this.executorService = executorService;
            this.cloudNotificationService = cloudNotificationService;
        }

        public void execute() {
            setFuture(getExecutorService().submit(this));
        }

        public void executeNow() {
            run();
        }

        public ExecutorService getExecutorService() {
            return executorService;
        }

        public void cancel() {
            getFutureLock().lock();
            try {
                if (getFuture() != null && !getFuture().isCancelled() && !getFuture().isDone()) {
                    getFuture().cancel(false);
                }
            } finally {
                getFutureLock().unlock();
            }
        }

        public void run() {
            for (final String _notifyBackURI : getNotifyBackURISet()) {
                try {
                    getCloudNotificationService().
                        getNotificationProvider(_notifyBackURI).
                            send(getPropertyMap(), _notifyBackURI);
                } catch (final ProtocolException exception) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(
                            Level.FINE,
                            "Failed to send Cloud Push Notification with " +
                                "Properties '" + getPropertyMap() + "' to " +
                                "Notify-Back-URI '" + _notifyBackURI + "'.  " +
                                    "(" + exception.getMessage() + ")"
                        );
                    }
                } catch (final Throwable throwable) {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(
                            Level.WARNING,
                            "Failed to send Cloud Push Notification with " +
                                "Properties '" + getPropertyMap() + "' to " +
                                "Notify-Back-URI '" + _notifyBackURI + "'." +
                            throwable
                        );
                    }
                }
            }
        }

        @Override
        public String toString() {
            return
                new StringBuilder().
                    append("PushTask[").
                        append(classMembersToString()).
                    append("]").
                        toString();
        }

        protected String classMembersToString() {
            return
                new StringBuilder().
                    append("notifyBackURISet: '").append(getNotifyBackURISet()).append("', ").
                    append("propertyMap: '").append(getPropertyMap()).append("'").
                        toString();
        }

        protected Future getFuture() {
            return future;
        }

        protected Lock getFutureLock() {
            return futureLock;
        }

        protected Set<String> getModifiableNotifyBackURISet() {
            return notifyBackURISet;
        }

        protected Map<String, String> getModifiablePropertyMap() {
            return propertyMap;
        }

        protected CloudNotificationService getCloudNotificationService() {
            return cloudNotificationService;
        }

        protected Set<String> getNotifyBackURISet() {
            return Collections.unmodifiableSet(getModifiableNotifyBackURISet());
        }

        protected Map<String, String> getPropertyMap() {
            return Collections.unmodifiableMap(getModifiablePropertyMap());
        }

        protected void setFuture(final Future future) {
            getFutureLock().lock();
            try {
                this.future = future;
            } finally {
                getFutureLock().unlock();
            }
        }
    }
}
