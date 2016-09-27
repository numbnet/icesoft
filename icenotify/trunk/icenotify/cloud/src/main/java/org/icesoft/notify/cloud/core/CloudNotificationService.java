package org.icesoft.notify.cloud.core;

import static org.icesoft.util.PreCondition.checkIfIsNotNull;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

import org.icesoft.util.servlet.Service;

public abstract class CloudNotificationService
implements Service {
    private static final Logger LOGGER = Logger.getLogger(CloudNotificationService.class.getName());

    private static boolean setUpComplete = false;

    public abstract void pushToNotifyBackURI(
        final String notifyBackURI, final Map<String, String> properties);

    public abstract void pushToNotifyBackURINow(
        final String notifyBackURI, final Map<String, String> properties);

    public abstract void pushToNotifyBackURIs(
        final Set<String> notifyBackURISet, final Map<String, String> properties);

    public abstract void pushToNotifyBackURIsNow(
        final Set<String> notifyBackURISet, final Map<String, String> properties);

    public static void waitForSetUpToComplete(final ServletContext servletContext)
    throws NullPointerException {
        checkIfIsNotNull(
            servletContext, "Illegal argument servletContext: '" + servletContext + "'.  Argument cannot be null."
        );
        lockSetUp(servletContext);
        try {
            if (!setUpComplete) {
                try {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE, "Waiting for set-up to complete...");
                    }
                    getSetUpCondition(servletContext).await();
                } catch (final InterruptedException exception) {
                    // Do nothing.
                }
            }
        } finally {
            unlockSetUp(servletContext);
        }
    }

    public static void wakeUpAllAsSetUpIsComplete(final ServletContext servletContext)
    throws NullPointerException {
        checkIfIsNotNull(
            servletContext, "Illegal argument servletContext: '" + servletContext + "'.  Argument cannot be null."
        );
        lockSetUp(servletContext);
        try {
            if (!setUpComplete) {
                setUpComplete = true;
            }
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Wake up all as set-up is complete.");
            }
            getSetUpCondition(servletContext).signalAll();
        } finally {
            unlockSetUp(servletContext);
        }
    }

    private static synchronized Condition getSetUpCondition(final ServletContext servletContext) {
        Condition _setUpCondition =
            (Condition)servletContext.getAttribute(CloudNotificationService.class.getName() + "#setUpCondition");
        if (_setUpCondition == null) {
            _setUpCondition = getSetUpLock(servletContext).newCondition();
            servletContext.setAttribute(CloudNotificationService.class.getName() + "#setUpCondition", _setUpCondition);
        }
        return _setUpCondition;
    }

    private static synchronized Lock getSetUpLock(final ServletContext servletContext) {
        Lock _setUpLock =
            (Lock)servletContext.getAttribute(CloudNotificationService.class.getName() + "#setUpLock");
        if (_setUpLock == null) {
            _setUpLock = new ReentrantLock();
            servletContext.setAttribute(CloudNotificationService.class.getName() + "#setUpLock", _setUpLock);
        }
        return _setUpLock;
    }

    private static void lockSetUp(final ServletContext servletContext) {
        getSetUpLock(servletContext).lock();
    }

    private static void unlockSetUp(final ServletContext servletContext) {
        getSetUpLock(servletContext).unlock();
    }
}
