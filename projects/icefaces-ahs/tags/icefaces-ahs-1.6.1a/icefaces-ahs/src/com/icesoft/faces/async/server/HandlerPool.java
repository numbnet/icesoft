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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 *   The <code>HandlerPool</code> class is responsible for creating, leasing and
 *   managing read, process and write handlers.
 * </p>
 */
public class HandlerPool {
    private static final Log LOG = LogFactory.getLog(HandlerPool.class);

    private AsyncHttpServer asyncHttpServer;
    private List processHandlerList = new ArrayList();
    private List readHandlerList = new ArrayList();
    private List writeHandlerList = new ArrayList();

    /**
     * <p>
     *   Constructs a new <code>HandlerPool</code> object with the specified
     *   <code>asyncHttpServer</code>. All handlers created by the
     *   <code>HandlerPool</code> will have the <code>asyncHttpServer</code>
     *   associated with it.
     * </p>
     *
     * @param      asyncHttpServer
     *                 the Asynchronous HTTP Server to be associated with the
     *                 <code>HandlerPool</code> to be created.
     * @throws     IllegalArgumentException
     *                 if the specified <code>asyncHttpServer</code> is
     *                 <code>null</code>.
     */
    public HandlerPool(final AsyncHttpServer asyncHttpServer)
    throws IllegalArgumentException {
        if (asyncHttpServer == null) {
            throw new IllegalArgumentException("asyncHttpServer is null");
        }
        this.asyncHttpServer = asyncHttpServer;
    }

    /**
     * <p>
     *   Gets a process handler from the pool to handle the specified
     *   <code>httpConnection</code>. If there is no process handler available
     *   it will create a new one and add it to the pool.
     * </p>
     *
     * @param      httpConnection
     *                 the HTTP connection to be handled.
     * @return     a process handler or <code>null</code> if the specified
     *             <code>httpConnection</code> is <code>null</code>.
     * @see        #returnProcessHandler(ProcessHandler)
     */
    public synchronized ProcessHandler getProcessHandler(
        final HttpConnection httpConnection) {

        if (httpConnection == null) {
            return null;
        }
        ProcessHandler _processHandler;
        int _size = processHandlerList.size();
        for (int i = 0; i < _size; i++) {
            _processHandler = (ProcessHandler)processHandlerList.get(i);
            if (_processHandler.lease()) {
                _processHandler.setHttpConnection(httpConnection);
                return _processHandler;
            }
        }
        _processHandler = new ProcessHandler(httpConnection);
        _processHandler.lease();
        _processHandler.setAsyncHttpServer(asyncHttpServer);
        _processHandler.setHandlerPool(this);
        processHandlerList.add(_processHandler);
        if (LOG.isDebugEnabled()) {
            LOG.debug(
                "HandlerPool status:\r\n" +
                "        ReadHandlers    : " +
                    readHandlerList.size() + "\r\n" +
                "        ProcessHandlers : " +
                    processHandlerList.size() + "\r\n" +
                "        WriteHandlers   : " +
                    writeHandlerList.size());
        }
        return _processHandler;
    }

    /**
     * <p>
     *   Gets a read handler from the pool to handle the specified
     *   <code>httpConnection</code>. If there is no read handler available it
     *   will create a new one and add it to the pool.
     * </p>
     *
     * @param      httpConnection
     *                 the HTTP connection to be handled.
     * @return     a read handler or <code>null</code> if the specified
     *             <code>httpConnection</code> is <code>null</code>.
     * @see        #returnReadHandler(ReadHandler)
     */
    public synchronized ReadHandler getReadHandler(
        final HttpConnection httpConnection) {

        if (httpConnection == null) {
            return null;
        }
        ReadHandler _readHandler;
        int _size = readHandlerList.size();
        for (int i = 0; i < _size; i++) {
            _readHandler = (ReadHandler)readHandlerList.get(i);
            if (_readHandler.lease()) {
                _readHandler.setHttpConnection(httpConnection);
                return _readHandler;
            }
        }
        _readHandler = new ReadHandler(httpConnection);
        _readHandler.lease();
        _readHandler.setAsyncHttpServer(asyncHttpServer);
        _readHandler.setHandlerPool(this);
        readHandlerList.add(_readHandler);
        if (LOG.isDebugEnabled()) {
            LOG.debug(
                "HandlerPool status:\r\n" +
                "        ReadHandlers    : " +
                    readHandlerList.size() + "\r\n" +
                "        ProcessHandlers : " +
                    processHandlerList.size() + "\r\n" +
                "        WriteHandlers   : " +
                    writeHandlerList.size());
        }
        return _readHandler;
    }

    /**
     * <p>
     *   Gets a write handler from the pool to handle the specified
     *   <code>httpConnection</code>. If there is no write handler available it
     *   will create a new one and add it to the pool.
     * </p>
     *
     * @param      httpConnection
     *                 the HTTP connection to be handled.
     * @return     a write handler or <code>null</code> if the specified
     *             <code>httpConnection</code> is <code>null</code>.
     * @see        #returnWriteHandler(WriteHandler)
     */
    public synchronized WriteHandler getWriteHandler(
        final HttpConnection httpConnection) {

        if (httpConnection == null) {
            return null;
        }
        WriteHandler _writeHandler;
        int _size = writeHandlerList.size();
        for (int i = 0; i < _size; i++) {
            _writeHandler = (WriteHandler)writeHandlerList.get(i);
            if (_writeHandler.lease()) {
                _writeHandler.setHttpConnection(httpConnection);
                return _writeHandler;
            }
        }
        _writeHandler = new WriteHandler(httpConnection);
        _writeHandler.lease();
        _writeHandler.setAsyncHttpServer(asyncHttpServer);
        _writeHandler.setHandlerPool(this);
        writeHandlerList.add(_writeHandler);
        if (LOG.isDebugEnabled()) {
            LOG.debug(
                "HandlerPool status:\r\n" +
                "        ReadHandlers    : " +
                    readHandlerList.size() + "\r\n" +
                "        ProcessHandlers : " +
                    processHandlerList.size() + "\r\n" +
                "        WriteHandlers   : " +
                    writeHandlerList.size());
        }
        return _writeHandler;
    }

    /**
     * <p>
     *   Returns the specified <code>processHandler</code> to the pool of
     *   process handlers. The lease of the <code>processHandler</code> will be
     *   expired and the associated HTTP connection will be nullified in the
     *   process.
     * </p>
     *
     * @param      processHandler
     *                 the process handler to be returned to the pool.
     * @see        #getProcessHandler(HttpConnection)
     */
    public void returnProcessHandler(final ProcessHandler processHandler) {
        if (processHandler != null) {
            processHandler.reset();
            processHandler.expireLease();
        }
    }

    /**
     * <p>
     *   Returns the specified <code>readHandler</code> to the pool of read
     *   handlers. The lease of the <code>readHandler</code> will be expired and
     *   the associated HTTP connection will be nullified in the process.
     * </p>
     *
     * @param      readHandler
     *                 the read handler to be returned to the pool.
     * @see        #getReadHandler(HttpConnection)
     */
    public void returnReadHandler(final ReadHandler readHandler) {
        if (readHandler != null) {
            readHandler.reset();
            readHandler.expireLease();
        }
    }

    /**
     * <p>
     *   Returns the specified <code>writeHandler</code> to the pool of write
     *   handlers. The lease of the <code>writeHandler</code> will be expired
     *   and the associated HTTP connection will be nullified in the process.
     * </p>
     *
     * @param      writeHandler
     *                 the write handler to be returned to the pool.
     * @see        #getWriteHandler(HttpConnection)
     */
    public void returnWriteHandler(final WriteHandler writeHandler) {
        if (writeHandler != null) {
            writeHandler.reset();
            writeHandler.expireLease();
        }
    }
}
