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
package com.icesoft.faces.async.server.io;

import com.icesoft.faces.async.common.ExecuteQueue;
import com.icesoft.faces.async.server.AbstractHttpConnectionAcceptor;
import com.icesoft.faces.async.server.AsyncHttpServer;
import com.icesoft.faces.async.server.HttpConnection;
import com.icesoft.faces.async.server.HttpConnectionAcceptor;
import com.icesoft.faces.async.server.ReadHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IoHttpConnectionAcceptor
extends AbstractHttpConnectionAcceptor
implements HttpConnectionAcceptor {
    private static final Log LOG =
        LogFactory.getLog(IoHttpConnectionAcceptor.class);

    protected ServerSocket serverSocket;

    public IoHttpConnectionAcceptor(
        final int port, final ExecuteQueue executeQueue,
        final AsyncHttpServer asyncHttpServer)
    throws IllegalArgumentException {
        super(port, executeQueue, asyncHttpServer);
    }

    public void closeSocket(final Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException exception) {
                if (LOG.isErrorEnabled()) {
                    LOG.error(
                        "An I/O error occurred while closing the socket!",
                        exception);
                }
            }
            if (httpConnectionMap.containsKey(socket)) {
                httpConnectionMap.remove(socket);
            }
        }
    }

    public void doneReading(final HttpConnection httpConnection) {
        // do nothing.
    }

    public void handle(final Socket socket) {
        handleRead(socket);
    }

    public void requestStop() {
        super.requestStop();
        tearDown();
    }

    public void run() {
        setUp();
        try {
            while (!stopRequested) {
                Socket _socket = serverSocket.accept();
                if (LOG.isDebugEnabled()) {
                    LOG.debug(
                        "Incoming connection from " +
                            _socket.getRemoteSocketAddress() + " accepted.");
                }
                handleRead(_socket);
            }
        } catch (SocketException exception) {
            // do nothing...
        } catch (IOException exception) {
            if (LOG.isErrorEnabled()) {
                LOG.error(
                    "An I/O error occurred while " +
                        "waiting for a connection!",
                    exception);
            }
            tearDown();
        }
    }

    public void setPendingReadHandler(
        final Object key, final ReadHandler readHandler) {

        throw new UnsupportedOperationException();
    }

    protected void setUp() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException exception) {
            if (LOG.isErrorEnabled()) {
                LOG.error(
                    "An I/O error occurred while opening the server socket!",
                    exception);
            }
        }
        super.setUp();
    }

    protected void tearDown() {
        try {
            serverSocket.close();
        } catch (IOException exception) {
            if (LOG.isErrorEnabled()) {
                LOG.error(
                    "An I/O error occurred while closing the server socket!",
                    exception);
            }
        }
        super.tearDown();
    }

    private void handleRead(final Socket socket) {
        IoHttpConnection _ioHttpConnection;
        if (httpConnectionMap.containsKey(socket)) {
            _ioHttpConnection = (IoHttpConnection)httpConnectionMap.get(socket);
        } else {
            _ioHttpConnection = new IoHttpConnection(socket, this);
            httpConnectionMap.put(socket, _ioHttpConnection);
        }
        try {
            ReadHandler _readHandler =
                new ReadHandler(executeQueue, asyncHttpServer);
            _readHandler.setHttpConnection(_ioHttpConnection);
            _readHandler.handle();
        } catch (Exception exception) {
            if (LOG.isErrorEnabled()) {
                LOG.error(
                    "An error occurred while getting a read handler!",
                    exception);
            }
        }
    }
}
