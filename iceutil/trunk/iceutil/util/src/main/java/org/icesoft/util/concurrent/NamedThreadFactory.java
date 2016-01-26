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
package org.icesoft.util.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NamedThreadFactory
implements ThreadFactory {
    private static final Logger LOGGER = Logger.getLogger(NamedThreadFactory.class.getName());

    private final AtomicInteger counter = new AtomicInteger(0);

    private final boolean daemon;
    private final String prefix;

    public NamedThreadFactory() {
        this("Thread", false);
    }

    public NamedThreadFactory(final boolean daemon) {
        this("Thread", daemon);
    }

    public NamedThreadFactory(final String prefix) {
        this(prefix, false);
    }

    public NamedThreadFactory(final String prefix, final boolean daemon) {
        this.prefix = prefix;
        this.daemon = daemon;
    }

    public boolean getDaemon() {
        return daemon;
    }

    public String getPrefix() {
        return prefix;
    }

    public Thread newThread(final Runnable runnable) {
        Thread _thread = new Thread(runnable, getPrefixedName(getCounter().incrementAndGet()));
        _thread.setDaemon(getDaemon());
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "New Thread '" + _thread.getName() + "' created.");
        }
        return _thread;
    }

    protected AtomicInteger getCounter() {
        return counter;
    }

    protected String getPrefixedName(final int counter) {
        return
            new StringBuilder().
                append(getPrefix()).append(" [").append(counter).append("]").
                    toString();
    }
}
