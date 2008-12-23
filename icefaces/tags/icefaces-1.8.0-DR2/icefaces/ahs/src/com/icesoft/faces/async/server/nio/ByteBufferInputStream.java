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

import java.io.InputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ByteBufferInputStream
extends InputStream {
    private static final Log LOG =
        LogFactory.getLog(ByteBufferInputStream.class);

    private ByteBuffer byteBuffer;
    private boolean endOfStream = false;
    private SelectionKey selectionKey;

    public ByteBufferInputStream(
        final ByteBuffer byteBuffer, final SelectionKey selectionKey)
    throws IllegalArgumentException {
        if (byteBuffer == null) {
            throw new IllegalArgumentException("byteBuffer is null");
        }
        if (selectionKey == null) {
            throw new IllegalArgumentException("selectionKey is null");
        }
        this.byteBuffer = byteBuffer;
        this.selectionKey = selectionKey;
    }

    public int available()
    throws IOException {
        return byteBuffer.remaining();
    }

    public void close()
    throws IOException {
        // do nothing.
    }

    public boolean markSupported() {
        return false;
    }

    /**
     * <p>
     *   Reads the next byte of data of the stream.
     * </p>
     *
     * @return     the next byte of data, or <code>-1</code> if the end of the
     *             stream is reachted.
     * @throws     IOException
     *                 if an I/O error occurs.
     */
    public int read()
    throws IOException {
        if (endOfStream) {
            return -1;
        }
        if (!byteBuffer.hasRemaining()) {
            if (read(1) <= 0) {
                return -1;
            }
        }
        return byteBuffer.get() & 0xFF;
    }

    /**
     *
     * @param      bytes
     *                 the buffer into which the read data is written.
     * @return
     * @throws     IllegalArgumentException
     *                 if the specified <code>bytes</code> is <code>null</code>.
     * @throws     IOException
     *                 if an I/O error occurs.
     */
    public int read(final byte[] bytes)
    throws IllegalArgumentException, IOException {
        if (bytes == null) {
            throw new IllegalArgumentException("bytes is null");
        }
        if (endOfStream) {
            return -1;
        }
        return super.read(bytes);
    }

    /**
     *
     * @param      bytes
     *                 the buffer into which the read data is written.
     * @param      offset
     *                 the start offset in the specified <code>bytes</code>
     *                 array at which the read data is written.
     * @param      length
     *                 the maximum number bytes to read.
     * @return
     * @throws     ArrayIndexOutOfBoundsException
     *                 if one of the following occurs:
     *                 <ul>
     *                   <li>
     *                     the specified <code>offset</code> is either lesser
     *                     than <code>0</code> or greater than or equal to
     *                     <code>bytes.length</code>; or
     *                   </li>
     *                   <li>
     *                     the specified <code>offset</code> and
     *                     <code>length</code> accumulated are greater than
     *                     <code>bytes.length</code>.
     *                   </li>
     *                 </ul>
     * @throws     IllegalArgumentException
     *                 if one of the following occurs:
     *                 <ul>
     *                   <li>
     *                     the specified <code>bytes</code> is
     *                     <code>null</code>; or
     *                   </li>
     *                   <li>
     *                     the specified <code>length</code> is lesser than
     *                     <code>0</code>.
     *                   </li>
     *                 </ul>
     * @throws     IOException
     *                 if an I/O error occurs.
     */
    public int read(final byte[] bytes, final int offset, final int length)
    throws
        ArrayIndexOutOfBoundsException, IllegalArgumentException, IOException {

        if (bytes == null) {
            throw new IllegalArgumentException("bytes is null");
        }
        if (length < 0) {
            throw new IllegalArgumentException("Illegal length: " + length);
        }
        if (offset < 0 ||
            offset >= bytes.length ||
            offset + length > bytes.length) {

            throw new ArrayIndexOutOfBoundsException();
        }
        if (length == 0) {
            return 0;
        }
        if (endOfStream) {
            return -1;
        }
        if (!byteBuffer.hasRemaining()) {
            if (read(length) == -1) {
                return -1;
            }
        }
        int _numberOfBytes = Math.min(length, byteBuffer.remaining());
        byteBuffer.get(bytes, offset, _numberOfBytes);
        return _numberOfBytes;
    }

    /**
     *
     * @param      numberOfBytesToRead
     *                 the desired number of bytes to be read.
     * @return     the number of bytes read, possibly zero, or <code>-1</code>
     *             if the channel has reached end-of-stream.
     */
    private int read(final int numberOfBytesToRead) {
        // todo: necessary?
//        if (selectionKey == null) {
//            return -1;
//        }
        try {
            byteBuffer.clear();
            SocketChannel _socketChannel =
                (SocketChannel)selectionKey.channel();
            int _numberOfBytesRead;
            int _totalNumberOfBytesRead = 0;
            /*
             * Throws:
             * - NotYetConnectedException     - If this channel is not yet
             *                                  connected.
             * - IOException                  - If some other I/O error occurs.
             */
            while ((_numberOfBytesRead = _socketChannel.read(byteBuffer)) > 0) {
                _totalNumberOfBytesRead += _numberOfBytesRead;
                if (_totalNumberOfBytesRead >= numberOfBytesToRead) {
                    break;
                }
            }
            if (_numberOfBytesRead == -1) {
                endOfStream = true;
                if (_totalNumberOfBytesRead == 0) {
                    return -1;
                }
            }
            return _totalNumberOfBytesRead;
        } catch (NotYetConnectedException exception) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Socket channel is not yet connected!", exception);
            }
            return -1;
        } catch (IOException exception) {
            if (LOG.isErrorEnabled()) {
                LOG.error(
                    "An I/O error occurred while " +
                        "reading from the socket channel!",
                    exception);
            }
            return -1;
        } finally {
            byteBuffer.flip();
        }
    }
}
