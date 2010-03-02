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

import com.icesoft.faces.async.server.AbstractHttpConnection;
import com.icesoft.faces.async.server.HttpConnection;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IoHttpConnection
extends AbstractHttpConnection
implements HttpConnection {
    private static final Log LOG = LogFactory.getLog(IoHttpConnection.class);

    private IoHttpConnectionAcceptor ioHttpConnectionAcceptor;
    private Socket socket;

    public IoHttpConnection(
        final Socket socket,
        final IoHttpConnectionAcceptor ioHttpConnectionAcceptor) {

        super();
        this.socket = socket;
        this.ioHttpConnectionAcceptor = ioHttpConnectionAcceptor;
        try {
            inputStream = socket.getInputStream();
        } catch (IOException exception) {
            if (LOG.isErrorEnabled()) {
                LOG.error(
                    "An I/O error occurred while " +
                        "getting the input stream from the socket!",
                    exception);
            }
        }
        try {
            outputStream = socket.getOutputStream();
        } catch (IOException exception) {
            if (LOG.isErrorEnabled()) {
                LOG.error(
                    "An I/O error occurred while " +
                        "getting the output stream from the socket!",
                    exception);
            }
        }
    }

    public void close() {
        ioHttpConnectionAcceptor.closeSocket(socket);
        super.close();
    }

    public SocketAddress getRemoteSocketAddress() {
        return socket.getRemoteSocketAddress();
    }

    public Socket getSocket() {
        return socket;
    }

    public void reset() {
        super.reset();
        if (!closeRequested) {
            ioHttpConnectionAcceptor.handle(socket);
        }
    }
}
