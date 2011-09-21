/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions an
 * limitations under the License.
 */

package org.icemobile.samples.mobileshowcase.view.examples.device.notification;


import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 *
 */
@ManagedBean(name = ScheduledPushExecutor.BEAN_NAME)
@ApplicationScoped
public class ScheduledPushExecutor implements Serializable {

    private final static Logger log =
            Logger.getLogger(ScheduledPushExecutor.class.getName());

    public static final String BEAN_NAME = "scheduledPushExecutor";

    private ScheduledThreadPoolExecutor timerThreadPool =
            new ScheduledThreadPoolExecutor(10);

    public void schedule(Runnable runner, long delay, TimeUnit unit) {
        timerThreadPool.schedule(runner, delay, unit);
    }

    @PreDestroy
    public void shutdown() {
        timerThreadPool.purge();
        timerThreadPool.shutdownNow();
    }

}
