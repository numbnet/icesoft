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
import com.icesoft.util.Properties;

import edu.emory.mathcs.backport.java.util.concurrent.ScheduledFuture;
import edu.emory.mathcs.backport.java.util.concurrent.ScheduledThreadPoolExecutor;
import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;
import edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantLock;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DefaultMessageService
implements MessageServiceClient.Administrator {
    private static final Log LOG = LogFactory.getLog(DefaultMessageService.class);

    private static final int STATE_UNINITIALIZED = 0;
    private static final int STATE_SET_UP = 1;
    private static final int STATE_SET_UP_DONE = 2;
    private static final int STATE_STARTED = 3;
    private static final int STATE_STOPPED = 4;
    private static final int STATE_TEAR_DOWN = 5;
    private static final int STATE_TEAR_DOWN_DONE = 6;
    private static final int STATE_CLOSED = 7;

    private static final int DEFAULT_INTERVAL = 10000;
    private static final int DEFAULT_MAX_RETRIES = 30;
    private static final int DEFAULT_INTERVAL_ON_RECONNECT = 5000;
    private static final int DEFAULT_MAX_RETRIES_ON_RECONNECT = 60;

    private final Object stateLock = new Object();

    private final Configuration configuration;
    private final MessageServiceClient messageServiceClient;
    private final boolean retryOnFail;
    private final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private final TaskManager taskManager = new TaskManager();

    private MessagePublisher currentMessagePublisher;
    private long successTimestamp;

    private int currentState = STATE_UNINITIALIZED;
    private int requestedState = STATE_UNINITIALIZED;

    public DefaultMessageService(
        final MessageServiceClient messageServiceClient, final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor)
    throws IllegalArgumentException {
        // throws IllegalArgumentException 
        this(messageServiceClient, scheduledThreadPoolExecutor, null, false);
    }

    public DefaultMessageService(
        final MessageServiceClient messageServiceClient, final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor,
        final boolean retryOnFail)
    throws IllegalArgumentException {
        // throws IllegalArgumentException
        this(messageServiceClient, scheduledThreadPoolExecutor, null, retryOnFail);
    }

    public DefaultMessageService(
        final MessageServiceClient messageServiceClient, final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor,
        final Configuration configuration)
    throws IllegalArgumentException {
        // throws IllegalArgumentException
        this(messageServiceClient, scheduledThreadPoolExecutor, configuration, false);
    }

    public DefaultMessageService(
        final MessageServiceClient messageServiceClient, final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor,
        final Configuration configuration, final boolean retryOnFail)
    throws IllegalArgumentException {

        DefaultMessageServiceContextListener.setMessageService(this);

        if (messageServiceClient == null) {
            throw new IllegalArgumentException("messageServiceClient is null");
        }
        if (scheduledThreadPoolExecutor == null) {
            throw new IllegalArgumentException("scheduledThreadPoolExecutor is null");
        }
        this.messageServiceClient = messageServiceClient;
        this.messageServiceClient.setAdministrator(this);
        this.scheduledThreadPoolExecutor = scheduledThreadPoolExecutor;
        this.configuration = configuration;
        this.retryOnFail = retryOnFail;
        this.currentMessagePublisher =
            new QueueMessagePublisher(this, messageServiceClient, scheduledThreadPoolExecutor);
    }

    public final void close() {
        synchronized (stateLock) {
            requestedState = STATE_CLOSED;
            if (LOG.isDebugEnabled()) {
                LOG.debug("Requested State: CLOSED");
            }
            if (currentState == STATE_TEAR_DOWN_DONE) {
                try {
                    currentMessagePublisher.stop();
                    // switch from a queue-based message publisher to a noop message publisher.
                    currentMessagePublisher = new NoopMessagePublisher();
                    // throws MessageServiceException
                    messageServiceClient.close();
                    currentState = STATE_CLOSED;
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Current State: CLOSED");
                    }
                } catch (MessageServiceException exception) {
                    if (LOG.isErrorEnabled()) {
                        LOG.error("Failed to close the message service client.", exception);
                    }
                }
            }
        }
    }

    public MessageServiceClient getMessageServiceClient() {
        return messageServiceClient;
    }

    public final void publish(final Serializable object) {
        currentMessagePublisher.publish(object);
    }

    public final void publish(final Serializable object, final Properties messageProperties) {
        currentMessagePublisher.publish(object, messageProperties);
    }

    public final void publish(final Serializable object, final Properties messageProperties, final String messageType) {
        currentMessagePublisher.publish(object, messageProperties, messageType);
    }

    public final void publish(final Serializable object, final String messageType) {
        currentMessagePublisher.publish(object, messageType);
    }

    public final void publish(final String text) {
        currentMessagePublisher.publish(text);
    }

    public final void publish(final String text, final Properties messageProperties) {
        currentMessagePublisher.publish(text, messageProperties);
    }

    public final void publish(final String text, final Properties messageProperties, final String messageType) {
        currentMessagePublisher.publish(text, messageProperties, messageType);
    }

    public final void publish(final String text, final String messageType) {
        currentMessagePublisher.publish(text, messageType);
    }

    public final void publishNow(final Serializable object) {
        currentMessagePublisher.publishNow(object);
    }

    public final void publishNow(final Serializable object, final Properties messageProperties) {
        currentMessagePublisher.publishNow(object, messageProperties);
    }

    public final void publishNow(final Serializable object, final Properties messageProperties, final String messageType) {
        currentMessagePublisher.publishNow(object, messageProperties, messageType);
    }

    public final void publishNow(final Serializable object, final String messageType) {
        currentMessagePublisher.publishNow(object, messageType);
    }

    public final void publishNow(final String text) {
        currentMessagePublisher.publishNow(text);
    }

    public final void publishNow(final String text, final Properties messageProperties) {
        currentMessagePublisher.publishNow(text, messageProperties);
    }

    public final void publishNow(final String text, final Properties messageProperties, final String messageType) {
        currentMessagePublisher.publishNow(text, messageProperties, messageType);
    }

    public final void publishNow(final String text, final String messageType) {
        currentMessagePublisher.publishNow(text, messageType);
    }

    public final void reconnect() {
        LOG.debug("Reconnecting...");
        if (scheduledThreadPoolExecutor == null) {
            // blocking reconnect
            reconnectNow();
        } else {
            // non-blocking reconnect
            new ReconnectTask(scheduledThreadPoolExecutor).execute();
        }
    }

    public final boolean reconnectNow() {
        LOG.debug("Reconnecting now...");
        // blocking reconnect
        return new ReconnectTask().executeNow();
    }

    public final void setUp() {
        LOG.debug("Setting up...");
        setUp(
            !retryOnFail ? 0 : configuration.getAttributeAsInteger("interval", DEFAULT_INTERVAL),
            !retryOnFail ? 0 : configuration.getAttributeAsInteger("maxRetries", DEFAULT_MAX_RETRIES));
    }

    public final boolean setUpNow() {
        LOG.debug("Setting up now...");
        return
            setUpNow(
                !retryOnFail ? 0 : configuration.getAttributeAsInteger("interval", DEFAULT_INTERVAL),
                !retryOnFail ? 0 : configuration.getAttributeAsInteger("maxRetries", DEFAULT_MAX_RETRIES));
    }

    public final void start() {
        synchronized (stateLock) {
            requestedState = STATE_STARTED;
            if (LOG.isDebugEnabled()) {
                LOG.debug("Requested State: STARTED");
            }
            if (currentState == STATE_SET_UP_DONE) {
                try {
                    // throws MessageServiceException
                    messageServiceClient.start();
                    currentState = STATE_STARTED;
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Current State: STARTED");
                    }
                } catch (MessageServiceException exception) {
                    if (LOG.isFatalEnabled()) {
                        LOG.fatal("Failed to start message delivery!", exception);
                    }
                }
            }
        }
    }

    public final void stop() {
        synchronized (stateLock) {
            requestedState = STATE_STOPPED;
            if (LOG.isDebugEnabled()) {
                LOG.debug("Requested State: STOPPED");
            }
            if (currentState == STATE_STARTED) {
                try {
                    // throws MessageServiceException
                    messageServiceClient.stop();
                    currentState = STATE_STOPPED;
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Current State: STOPPED");
                    }
                } catch (MessageServiceException exception) {
                    if (LOG.isFatalEnabled()) {
                        LOG.fatal("Failed to stop message delivery!", exception);
                    }
                }
            }
        }
    }

    public final void tearDown() {
        LOG.debug("Tearing down...");
        taskManager.cancelAll();
        if (scheduledThreadPoolExecutor == null) {
            // blocking tear down
            tearDownNow();
        } else {
            // non-blocking tear down
            new TearDownTask(scheduledThreadPoolExecutor).execute();
        }
    }

    public final boolean tearDownNow() {
        LOG.debug("Tearing down now...");
        taskManager.cancelAll();
        // blocking tear down
        return new TearDownTask().executeNow();
    }

    protected void setUpMessageServiceClient()
    throws MessageServiceException {

    }

    protected void tearDownMessageServiceClient()
    throws MessageServiceException {

    }

    private void setUp(final int interval, final int maxRetries) {
        LOG.debug("Setting up... (interval: [" + interval + "], maxRetries: [" + maxRetries + "])");
        if (scheduledThreadPoolExecutor == null) {
            // blocking set up
            setUpNow(interval, maxRetries);
        } else {
            // non-blocking set up
            new SetUpTask(interval, maxRetries, scheduledThreadPoolExecutor).execute();
        }
    }

    private boolean setUpNow(final int interval, final int maxRetries) {
        LOG.debug("Setting up now... (interval: [" + interval + "], maxRetries: [" + maxRetries + "])");
        // blocking set up
        return new SetUpTask(interval, maxRetries).executeNow();
    }

    private interface Task {
        void cancel();

        boolean cancelled();

        void execute();

        boolean executeNow();

        boolean succeeded();
    }

    private abstract class AbstractTask
    implements Runnable, Task {
        private final ReentrantLock scheduledFutureLock = new ReentrantLock();

        protected final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

        protected final long interval;

        private boolean cancelled = false;

        private ScheduledFuture scheduledFuture;

        protected AbstractTask(final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor) {
            this(scheduledThreadPoolExecutor, -1);
        }

        protected AbstractTask(final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor, final long interval) {
            this.scheduledThreadPoolExecutor = scheduledThreadPoolExecutor;
            this.interval = interval;
        }

        public void cancel() {
            cancel(true);
        }

        public boolean cancelled() {
            return cancelled;
        }

        public void execute() {
            if (scheduledThreadPoolExecutor != null) {
                scheduledFutureLock.lock();
                try {
                    if (interval == -1) {
                        scheduledFuture =
                            scheduledThreadPoolExecutor.scheduleAtFixedRate(this, 0, interval, TimeUnit.MILLISECONDS);
                    } else {
                        scheduledFuture =
                            scheduledThreadPoolExecutor.schedule(this, 0, TimeUnit.MILLISECONDS);
                    }
                } finally {
                    scheduledFutureLock.unlock();
                }
            } else {
                executeNow();
            }
        }

        public boolean executeNow() {
            run();
            return succeeded();
        }

        public final void run() {
            taskManager.addTask(this);
            try {
                executeTask();
            } finally {
                taskManager.removeTask(this);
            }
        }

        protected void cancel(final boolean mayInterruptIfRunning) {
            scheduledFutureLock.lock();
            try {
                if (scheduledFuture != null) {
                    cancelled = scheduledFuture.cancel(mayInterruptIfRunning);
                    scheduledFuture = null;
                } else {
                    cancelled = true;
                }
            } finally {
                scheduledFutureLock.unlock();
            }
        }

        protected abstract void executeTask();
    }

    private class ReconnectTask
    extends AbstractTask
    implements Runnable, Task {
        private ReentrantLock reconnectLock = new ReentrantLock();

        private boolean succeeded = false;

        private ReconnectTask() {
            this(null);
        }

        private ReconnectTask(final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor) {
            super(scheduledThreadPoolExecutor);
        }

        public boolean succeeded() {
            return succeeded;
        }

        protected void executeTask() {
            LOG.debug("Executing Reconnect task...");
            long _failTimestamp = System.currentTimeMillis();
            reconnectLock.lock();
            try {
                if (_failTimestamp > successTimestamp + 5000) {
                    tearDownNow();
                    if (setUpNow(
                            !retryOnFail ?
                                0 :
                                configuration.getAttributeAsInteger(
                                    "intervalOnReconnect", DEFAULT_INTERVAL_ON_RECONNECT),
                            !retryOnFail ?
                                0 :
                                configuration.getAttributeAsInteger(
                                    "maxRetriesOnReconnect", DEFAULT_MAX_RETRIES_ON_RECONNECT))) {

                        successTimestamp = System.currentTimeMillis();
                        succeeded = true;
                    }
                } else {
                    succeeded = true;
                }
            } finally {
                reconnectLock.unlock();
            }
            cancel(false);
            LOG.debug("Executing Reconnect task... (succeeded: [" + succeeded + "])");
        }
    }

    private class SetUpTask
    extends AbstractTask
    implements Runnable, Task {
        private final int maxRetries;

        private int retries = 0;
        private boolean succeeded = false;

        private SetUpTask(final int interval, final int maxRetries) {
            this(interval, maxRetries, null);
        }

        private SetUpTask(
            final int interval, final int maxRetries, final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor) {

            super(scheduledThreadPoolExecutor, interval);
            this.maxRetries = maxRetries;
        }

        public void execute() {
            synchronized (stateLock) {
                currentState = STATE_SET_UP;
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Current State: SET UP");
                }
            }
            super.execute();
        }

        public boolean executeNow() {
            synchronized (stateLock) {
                currentState = STATE_SET_UP;
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Current State: SET UP");
                }
            }
            return super.executeNow();
        }

        public boolean succeeded() {
            return succeeded;
        }

        protected void executeTask() {
            LOG.debug("Executing Set Up task...");
            do {
                if (!cancelled()) {
                    synchronized (stateLock) {
                        if (requestedState == STATE_CLOSED) {
                            cancel(false);
                            break;
                        }
                    }
                }
                try {
                    // throws InvalidDestinationException, JMSException, NamingException
                    setUpMessageServiceClient();
                    cancel(false);
                    succeeded = true;
                    synchronized (stateLock) {
                        currentState = STATE_SET_UP_DONE;
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Current State: SET UP DONE");
                        }
                        if (requestedState == STATE_STARTED) {
                            start();
                        }
                    }
                } catch (Exception exception) {
                    LOG.debug("Exception: " + exception.getClass().getName() + ": " + exception.getMessage());
                    tearDownNow();
                    if (retries++ == maxRetries) {
                        cancel(false);
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Set up of the message service client failed: " + exception.getMessage());
                        }
                        close();
                    }
                }
                if (!cancelled()) {
                    synchronized (stateLock) {
                        if (requestedState == STATE_CLOSED) {
                            cancel(false);
                            break;
                        }
                    }
                    if (scheduledThreadPoolExecutor == null) {
                        try {
                            // throws InterruptedException
                            Thread.sleep(interval);
                        } catch (InterruptedException exception) {
                            // do nothing
                        }
                    }
                }
            } while (scheduledThreadPoolExecutor == null && !cancelled());
            LOG.debug("Executing Set Up task... (succeeded: [" + succeeded + "])");
        }
    }

    private class TearDownTask
    extends AbstractTask
    implements Runnable, Task {
        private boolean succeeded = false;

        private TearDownTask() {
            this(null);
        }

        private TearDownTask(final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor) {
            super(scheduledThreadPoolExecutor);
        }

        public void execute() {
            synchronized (stateLock) {
                currentState = STATE_TEAR_DOWN;
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Current State: TEAR DOWN");
                }
            }
            super.execute();
        }

        public boolean executeNow() {
            synchronized (stateLock) {
                currentState = STATE_TEAR_DOWN;
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Current State: TEAR DOWN");
                }
            }
            return super.executeNow();
        }

        public boolean succeeded() {
            return succeeded;
        }

        protected void executeTask() {
            LOG.debug("Executing Tear Down task...");
            try {
                tearDownMessageServiceClient();
                cancel(false);
                succeeded = true;
                synchronized (stateLock) {
                    currentState = STATE_TEAR_DOWN_DONE;
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Current State: TEAR DOWN DONE");
                    }
                    if (requestedState == STATE_CLOSED) {
                        close();
                    }
                }
            } catch (Exception exception) {
                cancel(false);
            }
            LOG.debug("Executing Tear Down task... (succeeded: [" + succeeded + "])");
        }
    }

    private class TaskManager {
        private final ReentrantLock taskLock = new ReentrantLock();
        private final Set taskSet = new HashSet();

        private void addTask(final Task task) {
            taskLock.lock();
            try {
                if (!taskSet.contains(task)) {
                    taskSet.add(task);
                }
            } finally {
                taskLock.unlock();
            }
        }

        private void cancelAll() {
            taskLock.lock();
            try {
                Iterator _tasks = taskSet.iterator();
                while (_tasks.hasNext()) {
                    ((Task)_tasks.next()).cancel();
                }
                taskSet.clear();
            } finally {
                taskLock.unlock();
            }
        }

        private void removeTask(final Task task) {
            taskLock.lock();
            try {
                if (taskSet.contains(task)) {
                    taskSet.remove(task);
                }
            } finally {
                taskLock.unlock();
            }
        }
    }
}