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
package com.icesoft.faces.async.server.nio;

import com.icesoft.faces.async.common.ExecuteQueue;
import com.icesoft.faces.async.server.AbstractHttpConnectionAcceptor;
import com.icesoft.faces.async.server.AsyncHttpServer;
import com.icesoft.faces.async.server.HttpConnection;
import com.icesoft.faces.async.server.HttpConnectionAcceptor;
import com.icesoft.faces.async.server.ReadHandler;

import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentLinkedQueue;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.IllegalBlockingModeException;
import java.nio.channels.IllegalSelectorException;
import java.nio.channels.NotYetBoundException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NioHttpConnectionAcceptor
extends AbstractHttpConnectionAcceptor
implements HttpConnectionAcceptor {
    private static final Log LOG =
        LogFactory.getLog(NioHttpConnectionAcceptor.class);

    private final Map pendingReadHandlerMap = new HashMap();
    private final ConcurrentLinkedQueue selectionKeyQueue =
        new ConcurrentLinkedQueue();

    private Selector selector;
    private ServerSocket serverSocket;
    private ServerSocketChannel serverSocketChannel;

    public NioHttpConnectionAcceptor(
        final int port, final ExecuteQueue executeQueue,
        final AsyncHttpServer asyncHttpServer)
    throws IllegalArgumentException {
        super(port, executeQueue, asyncHttpServer);
    }

    public void closeChannel(final SelectionKey selectionKey) {
        if (selectionKey != null) {
            try {
                selectionKey.channel().close();
            } catch (IOException exception) {
                if (LOG.isErrorEnabled()) {
                    LOG.error(
                        "An I/O error occured while " +
                            "closing the socket channel!",
                        exception);
                }
            }
            if (httpConnectionMap.containsKey(selectionKey)) {
                httpConnectionMap.remove(selectionKey);
            }
            selector.wakeup();
        }
    }

    public void doneReading(final HttpConnection httpConnection) {
        registerSelectionKey(
            ((NioHttpConnection)httpConnection).getSelectionKey(),
            SelectionKey.OP_READ);
    }

    public void enableSelectionKeys() {
        int _size = selectionKeyQueue.size();
        for (int i = 0; i < _size; i++) {
            Registration _registration =
                (Registration)selectionKeyQueue.poll();
            if (_registration.selectionKey.isValid()) {
                try {
                    // throws CancelledKeyException
                    _registration.selectionKey.interestOps(
                        _registration.selectionKey.interestOps() |
                            _registration.operations);
                } catch (CancelledKeyException exception) {
                    if (LOG.isWarnEnabled()) {
                        LOG.warn("Selection Key got cancelled!", exception);
                    }
                }
            }
        }
    }

    public void registerSelectionKey(
        final SelectionKey selectionKey, final int operations) {

        if (selectionKey == null) {
            return;
        }
        selectionKeyQueue.add(new Registration(selectionKey, operations));
        selector.wakeup();
    }

    public void requestStop() {
        super.requestStop();
        selector.wakeup();
    }

    public void run() {
        setUp();
        try {
            while (!stopRequested) {
                enableSelectionKeys();
                if (LOG.isTraceEnabled()) {
                    StringBuffer _string = new StringBuffer();
                    _string.
                        append(
                            "ACCEPT\tCONNECT\tREAD\tWRITE\tChannel\r\n");
                    Iterator _keys = selector.keys().iterator();
                    while (_keys.hasNext()) {
                        SelectionKey _selectionKey = (SelectionKey)_keys.next();
                        try {
                            boolean _OP_ACCEPT = _selectionKey.isAcceptable();
                            boolean _OP_CONNECT = _selectionKey.isConnectable();
                            boolean _OP_READ = _selectionKey.isReadable();
                            boolean _OP_WRITE = _selectionKey.isWritable();
                            SelectableChannel _channel =
                                _selectionKey.channel();
                            _string.
                                append(_OP_ACCEPT).append('\t').
                                append(_OP_CONNECT).append('\t').
                                append(_OP_READ).append('\t').
                                append(_OP_WRITE).append('\t').
                                append(_channel).append("\r\n");
                        } catch (CancelledKeyException exception) {
                            // do nothing. (this is logging.)
                        }
                    }
                    LOG.trace("Keys:\r\n" + _string);
                }
                if (LOG.isTraceEnabled()) {
                    LOG.trace("Selection Process started.");
                }
                int _numberOfKeys = selector.select(60000);
                if (LOG.isTraceEnabled()) {
                    LOG.trace("Selection Process done or timed out.");
                }
                Set _selectedKeySet = selector.selectedKeys();
                if (_numberOfKeys == 0) {
                    _selectedKeySet.clear();
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("Nothing to do. Continuing...");
                    }
                    continue;
                }
                if (_selectedKeySet.isEmpty()) {
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("Nothing to do. Continuing...");
                    }
                    continue;
                }
                if (LOG.isTraceEnabled()) {
                    StringBuffer _string = new StringBuffer();
                    _string.
                        append(
                            "ACCEPT\tCONNECT\tREAD\tWRITE\tValid\tChannel\r\n");
                    Iterator _keys = _selectedKeySet.iterator();
                    while (_keys.hasNext()) {
                        SelectionKey _selectionKey = (SelectionKey)_keys.next();
                        try {
                            boolean _OP_ACCEPT = _selectionKey.isAcceptable();
                            boolean _OP_CONNECT = _selectionKey.isConnectable();
                            boolean _OP_READ = _selectionKey.isReadable();
                            boolean _OP_WRITE = _selectionKey.isWritable();
                            SelectableChannel _channel =
                                _selectionKey.channel();
                            _string.
                                append(_OP_ACCEPT).append('\t').
                                append(_OP_CONNECT).append('\t').
                                append(_OP_READ).append('\t').
                                append(_OP_WRITE).append('\t').
                                append(_channel).append("\r\n");
                        } catch (CancelledKeyException exception) {
                            // do notning. (this is logging.)
                        }
                    }
                    LOG.trace("Selected Keys:\r\n" + _string);
                }
                Iterator _selectedKeys = _selectedKeySet.iterator();
                while (_selectedKeys.hasNext()) {
                    SelectionKey _selectedKey =
                        (SelectionKey)_selectedKeys.next();
                    _selectedKeys.remove();
                    if (_selectedKey.isValid()) {
                        handleSelectionKey(_selectedKey);
                    } else {
                        cancelSelectionKey(_selectedKey);
                    }
                }
            }
        } catch (IOException exception) {
            if (LOG.isErrorEnabled()) {
                LOG.error(
                    "An I/O error occurred during select operation!",
                    exception);
            }
        } catch (ClosedSelectorException exception) {
            if (LOG.isErrorEnabled()) {
                LOG.error("The selector is closed!", exception);
            }
        }
        tearDown();
    }

    public void setPendingReadHandler(
        final Object key, final ReadHandler readHandler) {

        if (key != null && readHandler != null) {
            pendingReadHandlerMap.put(key, readHandler);
            registerSelectionKey(
                ((NioHttpConnection)readHandler.getHttpConnection()).
                    getSelectionKey(),
                SelectionKey.OP_READ);
        }
    }

    protected void setUp() {
        try {
            /*
             * Throws:
             * - IOException                  - If an I/O error occurs.
             */
            serverSocketChannel = ServerSocketChannel.open();
        } catch (IOException exception) {
            if (LOG.isFatalEnabled()) {
                LOG.fatal("Failed to open server socket channel!", exception);
            }
        }
        try {
            /*
             * Throws:
             * - IOException                  - If an I/O error occurs.
             */
            selector = Selector.open();
        } catch (IOException exception) {
            if (LOG.isFatalEnabled()) {
                LOG.fatal("Failed to open selector!", exception);
            }
        }
        serverSocket = serverSocketChannel.socket();
        try {
            /*
             * Throws:
             * - SocketException              - if an error occurs enabling or
             *                                  disabling the SO_RESUEADDR
             *                                  socket option, or the socket is
             *                                  closed.
             */
            serverSocket.setReuseAddress(true);
        } catch (SocketException exception) {
            if (LOG.isFatalEnabled()) {
                LOG.fatal(
                    "Failed to set address reuse on server socket!", exception);
            }
        }
        try {
            /*
             * Throws:
             * - IOException                  - if the bind operation fails, or
             *                                  if the socket is already bound.
             * - SecurityException            - if a SecurityManager is present
             *                                  and its checkListen method
             *                                  doesn't allow the operation.
             * - IllegalArgumentException     - if endpoint is a SocketAddress
             *                                  subclass not supported by this
             *                                  socket.
             */
            serverSocket.bind(new InetSocketAddress(port));
        } catch (BindException exception) {
            if (LOG.isFatalEnabled()) {
                LOG.fatal(
                    "Port " + port + " already in use!",
                    exception);
            }
            asyncHttpServer.stop();
            return;
        } catch (IOException exception) {
            if (LOG.isFatalEnabled()) {
                LOG.fatal(
                    "Failed to bind Internet socket address to server socket!",
                    exception);
            }
        }
        try {
            configureBlocking(serverSocketChannel, false);
        } catch (IOException exception) {
            if (LOG.isFatalEnabled()) {
                LOG.fatal(
                    "Failed to configure non-blocking I/O connection listener!",
                    exception);
            }
        }
        try {
            /*
             * Throws:
             * - ClosedChannelException       - If this channel is closed.
             * - IllegalBlockingModeException - If this channel is in blocking
             *                                  mode.
             * - IllegalSelectorException     - If this channel was not created
             *                                  by the same provider as the
             *                                  given selector.
             * - CancelledKeyException        - If this channel is currently
             *                                  registered with the given
             *                                  selector but the corresponding
             *                                  key has already been cancelled.
             * - IllegalArgumentException     - If a bit in ops does not
             *                                  correspond to an operation that
             *                                  is supported by this channel,
             *                                  that is,
             *                                  if set & ~validOps() != 0
             */
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException exception) {
            if (LOG.isFatalEnabled()) {
                LOG.fatal(
                    "Failed to register selector with server socket channel!",
                    exception);
            }
        }
        super.setUp();
    }

    protected void tearDown() {
        try {
            selector.close();
        } catch (IOException exception) {
            if (LOG.isFatalEnabled()) {
                LOG.fatal(
                    "An I/O error occurred while closing the selector!",
                    exception);
            }
        }
        try {
            serverSocket.close();
        } catch (IOException exception) {
            if (LOG.isFatalEnabled()) {
                LOG.fatal(
                    "An I/O error occurred while closing the server socket!",
                    exception);
            }
        }
        try {
            serverSocketChannel.close();
        } catch (IOException exception) {
            if (LOG.isFatalEnabled()) {
                LOG.fatal(
                    "An I/O error occurred while " +
                        "closing the server socket channel!",
                    exception);
            }
        }
        super.tearDown();
    }

    private void cancelSelectionKey(final SelectionKey selectionKey) {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Canceling SelectionKey: " + selectionKey);
        }
        if (selectionKey.isValid()) {
            selectionKey.cancel();
            try {
                selectionKey.channel().close();
            } catch (IOException exception) {
                if (LOG.isErrorEnabled()) {
                    LOG.error(
                        "An I/O error occurred while " +
                            "closing the socket channel!",
                        exception);
                }
            }
        }
    }

    private static void configureBlocking(
        final SelectableChannel selectableChannel, final boolean blocking)
    throws IOException {
        try {
            /*
             * Throws:
             * - IOException                  - If an I/O error occurs.
             */
            selectableChannel.configureBlocking(blocking);
        } catch (IOException exception) {
            if (LOG.isErrorEnabled()) {
                LOG.error(
                    "Failed to configure " +
                        (!blocking ? "non-" : "") +
                        "blocking I/O on selectable channel!",
                    exception);
            }
            throw exception;
        }
    }

    private void handleAccept(final SelectionKey selectedKey) {
        if (LOG.isTraceEnabled()) {
            LOG.trace(
                "Handling OP_ACCEPT for Selection Key " +
                    "with associated Channel: " +
                        selectedKey.channel());
        }
        ServerSocketChannel _serverSocketChannel =
            (ServerSocketChannel)selectedKey.channel();
        try {
            SocketChannel _socketChannel = _serverSocketChannel.accept();
            if (_socketChannel != null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(
                        "Incoming connection from " +
                            _socketChannel.socket().
                                getRemoteSocketAddress() + " " +
                            "accepted.");
                }
                try {
                    _socketChannel.configureBlocking(false);
                } catch (IOException exception) {
                    if (LOG.isErrorEnabled()) {
                        LOG.error(
                            "An I/O error occurred while configuring blocking!",
                            exception);
                    }
                }
                try {
                    _socketChannel.register(selector, SelectionKey.OP_READ);
                } catch (ClosedChannelException exception) {
                    if (LOG.isErrorEnabled()) {
                        LOG.error("Socket channel is closed!", exception);
                    }
                } catch (IllegalBlockingModeException exception) {
                    if (LOG.isErrorEnabled()) {
                        LOG.error(
                            "Socket channel is in blocking mode!", exception);
                    }
                } catch (IllegalSelectorException exception) {
                    if (LOG.isErrorEnabled()) {
                        LOG.error(
                            "Socket channel is created by " +
                                "a different provider!",
                            exception);
                    }
                } catch (CancelledKeyException exception) {
                    if (LOG.isErrorEnabled()) {
                        LOG.error("Selection key is cancelled!", exception);
                    }
                }
                registerSelectionKey(selectedKey, SelectionKey.OP_ACCEPT);
            }
        } catch (ClosedByInterruptException exception) {
            if (LOG.isErrorEnabled()) {
                LOG.error(
                    "Thread got interrupted while accept was in progress!",
                    exception);
            }
        } catch (AsynchronousCloseException exception) {
            if (LOG.isErrorEnabled()) {
                LOG.error(
                    "Server socket channel got closed by another thread " +
                        "while accept was in progress",
                    exception);
            }
        } catch (ClosedChannelException exception) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Server socket channel is closed!", exception);
            }
        } catch (NotYetBoundException exception) {
            if (LOG.isErrorEnabled()) {
                LOG.error(
                    "Server socket channel's socket has not yet been bound!",
                    exception);
            }
            /*
             * Another process is already listening to this port, possibly
             * another Asynchronous HTTP Server. Stop this server!
             *
             * Todo: Let the BlockingServlet know this server is not running!
             */
            asyncHttpServer.stop();
        } catch (IOException exception) {
            if (LOG.isErrorEnabled()) {
                LOG.error(
                    "An I/O error occurred while accept was in progress!",
                    exception);
            }
        }
    }

    private void handleRead(final SelectionKey selectedKey) {
        if (LOG.isTraceEnabled()) {
            LOG.trace(
                "Handling OP_READ for Selection Key " +
                    "with associated Channel: " +
                        selectedKey.channel());
        }
        try {
            selectedKey.interestOps(
                selectedKey.interestOps() & (~SelectionKey.OP_READ));
            NioHttpConnection _nioHttpConnection;
            if (httpConnectionMap.containsKey(selectedKey)) {
                _nioHttpConnection =
                    (NioHttpConnection)httpConnectionMap.get(selectedKey);
            } else {
                _nioHttpConnection = new NioHttpConnection(selectedKey, this);
                httpConnectionMap.put(selectedKey, _nioHttpConnection);
            }
            if (pendingReadHandlerMap.containsKey(_nioHttpConnection)) {
                ((ReadHandler)pendingReadHandlerMap.remove(_nioHttpConnection)).
                    handle();
            } else {
                try {
                    ReadHandler _readHandler =
                        new ReadHandler(executeQueue, asyncHttpServer);
                    _readHandler.setHttpConnection(_nioHttpConnection);
                    _readHandler.handle();
                } catch (Exception exception) {
                    if (LOG.isErrorEnabled()) {
                        LOG.error(
                            "An error occurred while getting a read handler!",
                            exception);
                    }
                }
            }
        } catch (CancelledKeyException exception) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Selection Key got cancelled!", exception);
            }
        }
    }

    private void handleSelectionKey(final SelectionKey selectionKey) {
        if (LOG.isTraceEnabled()) {
            LOG.trace(
                "Handling Selection Key with associated Channel: " +
                    selectionKey.channel());
        }
        try {
            int _readyOps = selectionKey.readyOps();
            if ((_readyOps & SelectionKey.OP_ACCEPT) ==
                    SelectionKey.OP_ACCEPT) {

                handleAccept(selectionKey);
            } else if (
                (_readyOps & SelectionKey.OP_READ) == SelectionKey.OP_READ) {

                handleRead(selectionKey);
            }
        } catch (CancelledKeyException exception) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Selection Key got cancelled!", exception);
            }
        }
    }

    private static class Registration {
        private SelectionKey selectionKey;
        private int operations;

        private Registration(
            final SelectionKey selectionKey, final int operations) {

            this.selectionKey = selectionKey;
            this.operations = operations;
        }
    }
}
