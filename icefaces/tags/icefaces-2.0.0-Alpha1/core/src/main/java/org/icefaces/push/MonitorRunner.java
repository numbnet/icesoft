/*
 * Version: MPL 1.1
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
 * 2004-2009 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
*/

package org.icefaces.push;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class MonitorRunner {
    private static Logger log = Logger.getLogger("org.icefaces.pushservlet");
    private Collection monitors = new ArrayList();
    private boolean run = true;

    public MonitorRunner(final long interval) {
        Thread thread = new Thread("Monitor Runner") {
            public void run() {
                while (run) {
                    try {
                        Thread.sleep(interval);
                        Iterator i = new ArrayList(monitors).iterator();
                        while (i.hasNext()) {
                            Runnable monitor = (Runnable) i.next();
                            try {
                                monitor.run();
                            } catch (Throwable t) {
                                log.warning("Failed to run monitor: " + monitor);
                            }
                        }
                    } catch (InterruptedException e) {
                        //do nothing
                    }
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    public void registerMonitor(Runnable monitor) {
        monitors.add(monitor);
    }

    public void unregisterMonitor(Runnable monitor) {
        monitors.remove(monitor);
    }

    public void stop() {
        run = false;
    }
}
