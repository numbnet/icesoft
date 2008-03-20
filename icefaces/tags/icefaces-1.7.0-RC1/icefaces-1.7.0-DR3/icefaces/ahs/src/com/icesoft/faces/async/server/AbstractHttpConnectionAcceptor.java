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
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
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
package com.icesoft.faces.async.server;

import com.icesoft.faces.async.common.ExecuteQueue;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.event.EventListenerList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractHttpConnectionAcceptor
extends Thread
implements HttpConnectionAcceptor {
    private static final Log LOG =
        LogFactory.getLog(AbstractHttpConnectionAcceptor.class);

    protected final AsyncHttpServer asyncHttpServer;
    protected final ExecuteQueue executeQueue;
    protected final EventListenerList httpConnectionAcceptorListenerList =
        new EventListenerList();
    protected final Map httpConnectionMap = new HashMap();
    protected final int port;

    protected boolean stopRequested;

    protected AbstractHttpConnectionAcceptor(
        final int port, final ExecuteQueue executeQueue,
        final AsyncHttpServer asyncHttpServer)
    throws IllegalArgumentException {
        checkPort(port);
        if (executeQueue == null) {
            throw new IllegalArgumentException("executeQueue is null");
        }
        if (asyncHttpServer == null) {
            throw new IllegalArgumentException("asyncHttpServer is null");
        }
        this.port = port;
        this.executeQueue = executeQueue;
        this.asyncHttpServer = asyncHttpServer;
    }

    public void addHttpConnectionAcceptorListener(
        final HttpConnectionAcceptorListener httpConnectionAcceptorListener) {

        httpConnectionAcceptorListenerList.add(
            HttpConnectionAcceptorListener.class,
            httpConnectionAcceptorListener);
    }

    public int getPort() {
        return port;
    }

    public boolean isStopRequested() {
        return stopRequested;
    }

    public void removeHttpConnectionAcceptorListener(
        final HttpConnectionAcceptorListener httpConnectionAcceptorListener) {

        httpConnectionAcceptorListenerList.add(
            HttpConnectionAcceptorListener.class,
            httpConnectionAcceptorListener);
    }

    public void requestStop() {
        fireStopRequestedEvent();
        stopRequested = true;
    }

    protected void fireStartedEvent() {
        EventListener[] _httpConnectionAcceptorListeners =
            httpConnectionAcceptorListenerList.getListeners(
                HttpConnectionAcceptorListener.class);
        if (_httpConnectionAcceptorListeners.length > 0) {
            HttpConnectionAcceptorEvent _httpConnectionAcceptorEvent =
                new HttpConnectionAcceptorEvent(this);
            for (int i = 0; i < _httpConnectionAcceptorListeners.length; i++) {
                ((HttpConnectionAcceptorListener)
                    _httpConnectionAcceptorListeners[i]).started(
                        _httpConnectionAcceptorEvent);
            }
        }
    }

    protected void fireStoppedEvent() {
        EventListener[] _httpConnectionAcceptorListeners =
            httpConnectionAcceptorListenerList.getListeners(
                HttpConnectionAcceptorListener.class);
        if (_httpConnectionAcceptorListeners.length > 0) {
            HttpConnectionAcceptorEvent _httpConnectionAcceptorEvent =
                new HttpConnectionAcceptorEvent(this);
            for (int i = 0; i < _httpConnectionAcceptorListeners.length; i++) {
                ((HttpConnectionAcceptorListener)
                    _httpConnectionAcceptorListeners[i]).stopped(
                        _httpConnectionAcceptorEvent);
            }
        }
    }

    protected void fireStopRequestedEvent() {
        EventListener[] _httpConnectionAcceptorListeners =
            httpConnectionAcceptorListenerList.getListeners(
                HttpConnectionAcceptorListener.class);
        if (_httpConnectionAcceptorListeners.length > 0) {
            HttpConnectionAcceptorEvent _httpConnectionAcceptorEvent =
                new HttpConnectionAcceptorEvent(this);
            for (int i = 0; i < _httpConnectionAcceptorListeners.length; i++) {
                ((HttpConnectionAcceptorListener)
                    _httpConnectionAcceptorListeners[i]).stopRequested(
                        _httpConnectionAcceptorEvent);
            }
        }
    }

    protected void setUp() {
        if (LOG.isInfoEnabled()) {
            LOG.info("Started listening at port: " + port);
        }
        fireStartedEvent();
    }

    protected void tearDown() {
        if (LOG.isInfoEnabled()) {
            LOG.info("Stopped listening at port: " + port);
        }
        fireStoppedEvent();
    }

    private void checkPort(final int port)
    throws IllegalArgumentException {
        if (port < 0 || port > 65536) {
            throw new IllegalArgumentException("Illegal port: " + port);
        }
    }
}
