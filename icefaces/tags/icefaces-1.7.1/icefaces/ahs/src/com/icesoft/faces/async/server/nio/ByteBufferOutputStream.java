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

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ByteBufferOutputStream
extends OutputStream {
    private static final Log LOG =
        LogFactory.getLog(ByteBufferOutputStream.class);

    private ByteBuffer byteBuffer;
    private boolean closed = false;
    private SelectionKey selectionKey;

    public ByteBufferOutputStream(
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

    public void close()
    throws IOException {
        closed = true;
    }

    public void flush()
    throws IOException {
        if (closed) {
            throw new IOException("Stream is closed!");
        }
    }

    /**
     * <p>
     *   Writes the specified <code>b</code> to this output stream. The general
     *   contract for <code>write</code> is that one byte is written to the
     *   output stream. The byte to be written is the 8 low-order bits of the
     *   argument <code>b</code>. The 24 high-order bits of <code>b</code> are
     *   ignored.
     * </p>
     *
     * @param      b
     *                 the byte to be written.
     * @throws     IOException
     *                 if an I/O error occurs.
     * @see        #write(byte[])
     * @see        #write(byte[], int, int)
     */
    public void write(final int b)
    throws IOException {
        write(new byte[] { (byte)b }, 0, 1);
    }

    /**
     * <p>
     *   Writes <code>bytes.length</code> bytes from the specified
     *   <code>bytes</code> byte array to this output stream. The general
     *   contract for <code>write(bytes)</code> is that it should have exactly
     *   the same effect as the call <code>write(bytes, 0, bytes.length)</code>.
     * </p>
     *
     * @param      bytes
     *                 the byte array containing the bytes to be written.
     * @throws     IOException
     *                 if an I/O error occurs.
     * @throws     NullPointerException
     *                 if the specified <code>bytes</code> byte array is
     *                 <code>null</code>.
     * @see        #write(int)
     * @see        #write(byte[], int, int)
     */
    public void write(final byte[] bytes)
    throws IOException, NullPointerException {
        write(bytes, 0, bytes.length);
    }

    /**
     * <p>
     * </p>
     *
     * @param      bytes
     *                 the byte array containing the bytes to be written.
     * @param      offset
     *                 the start offset in the <code>bytes</code> byte array
     *                 from which bytes are taken to be written.
     * @param      length
     *                 the number of bytes to be written.
     * @throws     IndexOutOfBoundsException
     *                 if the specified <code>offset</code> is negative, or the
     *                 specified <code>length</code> is negative, or
     *                 <code>offset+length</code> is greater than the length of
     *                 the specified <code>bytes</code>.
     * @throws     IOException
     *                 if an I/O error occurs.
     * @throws     NullPointerException
     *                 if the specified <code>bytes</code> byte array is
     *                 <code>null</code>.
     * @see        #write(int)
     * @see        #write(byte[])
     */
    public void write(final byte[] bytes, final int offset, final int length)
    throws IndexOutOfBoundsException, IOException, NullPointerException {
        if (bytes == null) {
            throw new NullPointerException("bytes is null");
        }
        if (offset < 0) {
            throw new IndexOutOfBoundsException("offset < 0: " + offset);
        }
        if (length < 0) {
            throw new IndexOutOfBoundsException("length < 0: " + length);
        }
        if (offset + length > bytes.length) {
            throw
                new IndexOutOfBoundsException(
                    "offset + length > bytes.length: " +
                        offset + ", " +
                        length + ", " +
                        bytes.length);
        }
        if (closed) {
            throw new IOException("Stream is closed!");
        }
        for (int i = 0; i < length;) {
            byteBuffer.clear();
            if (length - i > byteBuffer.capacity()) {
                byteBuffer.put(bytes, offset + i, byteBuffer.capacity());
                i += byteBuffer.capacity();
            } else {
                byteBuffer.put(bytes, offset + i, length - i);
                i += length - i;
            }
            byteBuffer.flip();
            try {
                SocketChannel _socketChannel =
                    (SocketChannel)selectionKey.channel();
                while (byteBuffer.hasRemaining()) {
                    _socketChannel.write(byteBuffer);
                }
            } catch (NotYetConnectedException exception) {
                if (LOG.isErrorEnabled()) {
                    LOG.error(
                        "This socket channel is not yet connected!", exception);
                }
            } catch (IOException exception) {
                if (LOG.isErrorEnabled()) {
                    LOG.error(
                        "An I/O error error occurred while " +
                            "writing to the socket channel!",
                        exception);
                }
            }
        }
    }
}
