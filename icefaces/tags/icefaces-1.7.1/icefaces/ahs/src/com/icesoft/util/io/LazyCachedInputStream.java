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
package com.icesoft.util.io;

import java.io.InputStream;
import java.io.IOException;

/**
 * <p>
 *   A <code>LazyCachedInputStream</code> adds functionality to another input
 *   stream, namely the ability to lazily cache the input. As bytes from the
 *   underlying stream are read, the internal byte cache is filled as
 *   necessary.
 * </p>
 * <p>
 *   By using the methods <code>{@link #read(int)}</code>,
 *   <code>{@link #read(int, byte[])}</code> and
 *   <code>{@link #read(int, byte[], int, int)}</code> one could even have a
 *   custom <code>InputStream</code> that uses a
 *   <code>LazyCachedInputStream</code> as underlying <code>InputStream</code>
 *   and administers its own position. In this way you could have more than one
 *   client of a <code>LazyCachedInputStream</code> and every client can read
 *   all the data from start to finish.
 * </p>
 */
public class LazyCachedInputStream
extends InputStream {
    private static final int INITIAL_CACHE_SIZE_DEFAULT = 4096;
    private static final int READ_BUFFER_SIZE = 4096;

    private byte[] cachedBytes;
    private boolean closed;
    private InputStream inputStream;
    private int position = -1;
    private byte[] readBuffer = new byte[READ_BUFFER_SIZE];

    /**
     * <p>
     *   Creates a <code>LazyCachedInputStream</code> and saves its argument,
     *   the specified <code>inputStream<code>, for later use.
     * </p>
     *
     * @param      inputStream
     *                 the underlying input stream.
     * @throws     IllegalArgumentException
     *                 if the specified <code>inputStream</code> is
     *                 <code>null</code>.
     */
    public LazyCachedInputStream(final InputStream inputStream)
    throws IllegalArgumentException {
        this(inputStream, INITIAL_CACHE_SIZE_DEFAULT);
    }

    /**
     * <p>
     *   Creates a <code>LazyCachedInputStream</code> and saves its argument,
     *   the specified <code>inputStream<code>, for later use. An internal byte
     *   cache of <code>initialCacheSize</code> is created.
     * </p>
     *
     * @param      inputStream
     *                 the underlying input stream.
     * @param      initialCacheSize
     *                 the initial cache size.
     * @throws     IllegalArgumentException
     *                 if one of the following occurs:
     *                 <ul>
     *                     <li>the specified <code>inputStream</code> is
     *                         <code>null</code> or
     *                     </li>
     *                     <li>the specified <code>initialCacheSize</code> is
     *                         less than <code>0</code>.
     *                     </li>
     *                 </ul>
     */
    public LazyCachedInputStream(
        final InputStream inputStream, final int initialCacheSize)
    throws IllegalArgumentException {
        if (inputStream == null) {
            throw new IllegalArgumentException("inputStream is null");
        }
        if (initialCacheSize < 0) {
            throw new IllegalArgumentException("initialCacheSize < 0");
        }
        this.inputStream = inputStream;
        cachedBytes = new byte[initialCacheSize];
    }

    public int available()
    throws IOException {
        if (closed) {
            throw new IOException("stream is closed");
        }
        int _available = position + 1;
        if (inputStream != null) {
            _available += inputStream.available();
        }
        return _available;
    }

    public void close()
    throws IOException {
        if (!closed) {
            try {
                if (inputStream != null) {
                    InputStream _inputStream = inputStream;
                    inputStream = null;
                    _inputStream.close();
                }
            } finally {
//                inputStream = null;
                cachedBytes = null;
                position = -1;
                closed = true;
            }
        }
    }

    public int read()
    throws IOException {
        return read(this.position + 1);
    }

    public int read(final byte[] bytes)
    throws IOException {
        return read(this.position + 1, bytes, 0, bytes.length);
    }

    public int read(final byte[] bytes, final int offset, final int length)
    throws IOException {
        return read(this.position + 1, bytes, offset, length);
    }

    /**
     * <p>
     *   Reads the next byte of data from the input stream from the specified
     *   <code>position</code>.
     * </p>
     *
     * @param      position
     *                 the position to start reading from.
     * @return     the next byte of data, or <code>-1</code> if the end of the
     *             stream is reached.
     * @throws     IndexOutOfBoundsException
     *                 if the specified <code>position</code> is less than
     *                 </code>0</code>.
     * @throws     IOException
     *                 if an I/O error occurs.
     * @see        #read()
     */
    public int read(final int position)
    throws IndexOutOfBoundsException, IOException {
        if (closed) {
            throw new IOException("read error: stream is closed");
        }
        if (position < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (position > this.position) {
            if (readUpTo(position) == -1 || position > this.position) {
                return -1;
            } else {
                return toUnsignedByte(cachedBytes[position]);
            }
        } else {
            return toUnsignedByte(cachedBytes[position]);
        }
    }

    /**
     * <p>
     *   Reads some number of bytes from the input stream from the specified
     *   <code>position</code> and stores them into the specified buffer array
     *   <code>bytes</code>.
     * </p>
     *
     * @param      position
     *                 the position to start reading from.
     * @param      bytes
     *                 the buffer into which the data is read.
     * @return     the total number of bytes read into the buffer, or
     *             <code>-1</code> if there is no more data because the end of
     *             the stream has been reached.
     * @throws     IllegalArgumentException
     *                 if the specified <code>bytes</code> is <code>null</code>.
     * @throws     IndexOutOfBoundsException
     *                 if the specified <code>position</code> is less than
     *                 </code>0</code>.
     * @throws     IOException
     *                 if an I/O error occurs.
     * @see        #read(byte[])
     * @see        #read(int, byte[], int, int)
     */
    public int read(final int position, final byte[] bytes)
    throws IllegalArgumentException, IndexOutOfBoundsException, IOException {
        return read(position, bytes, 0, bytes.length);
    }

    /**
     * <p>
     *   Reads up to the specified <code>length</code> bytes of data from the
     *   input stream from the specified <code>position</code> into the
     *   specified array <code>bytes</code> at the specified
     *   <code>offset</code>.
     * </p>
     *
     * @param      position
     *                 the position to start reading from.
     * @param      bytes
     *                 the buffer into which the data is read.
     * @param      offset
     *                 the start offset in array <code>bytes</code> at which the
     *                 data is written.
     * @param      length
     *                 the maximum number of bytes to read.
     * @return     the total number of bytes read into the buffer, or
     *             <code>-1</code> if there is no more data because the end of
     *             the stream has been reached.
     * @throws     IllegalArgumentException
     *                 if the specified <code>bytes</code> is <code>null</code>.
     * @throws     IndexOutOfBoundsException
     *                 if one of the following occurs:
     *                 <ul>
     *                     <li>the specified <code>position</code> is less than
     *                         </code>0</code>,
     *                     </li>
     *                     <li>the specified <code>offset</code> is either less
     *                         than <code>0</code> or greater than
     *                         <code>bytes.length</code>,
     *                     </li>
     *                     <li>the specified <code>length</code> is less than
     *                         <code>0</code> or
     *                     </li>
     *                     <li>the <code>offset + length</code> is either less
     *                         than <code>0</code> or greater than
     *                         <code>bytes.length</code>.
     *                     </li>
     *                 </ul>
     * @throws     IOException
     *                 if an I/O error occurs.
     * @see        #read(byte[], int, int)
     */
    public int read(
        final int position, final byte[] bytes, final int offset,
        final int length)
    throws IllegalArgumentException, IndexOutOfBoundsException, IOException {
        if (closed) {
            throw new IOException("read error: stream is closed");
        }
        if (bytes == null) {
            throw new IllegalArgumentException("bytes is null");
        } else if (
            position < 0 || offset < 0 || offset > bytes.length || length < 0 ||
            offset + length > bytes.length || offset + length < 0) {

            throw new IndexOutOfBoundsException();
        } else if (length == 0) {
            return 0;
        }
        if (position + length - 1 > this.position) {
            if (readUpTo(position + length - 1) == -1 &&
                position > this.position) {

                return -1;
            }
        }
        int _bytesRead;
        System.arraycopy(
            cachedBytes, position,
            bytes, offset,
            _bytesRead = Math.min(length, this.position - position + 1));
        return _bytesRead;
    }

    /**
     * <p>
     *   Increases the capacity of the internal byte cache, if necessary, to
     *   ensure that it can hold at least the number of bytes specified by the
     *   <code>minimumCapacity</code> argument.
     * </p>
     *
     * @param      minimumCapacity
     *                 the desired minimum capacity.
     */
    private void ensureCapacity(final int minimumCapacity) {
        int _oldCapacity = cachedBytes.length;
        if (minimumCapacity > _oldCapacity) {
            Object _oldCachedBytes = cachedBytes;
            cachedBytes =
                new byte[Math.max((_oldCapacity * 3) / 2 + 1, minimumCapacity)];
            System.arraycopy(
                _oldCachedBytes, 0, cachedBytes, 0, this.position + 1);
        }
    }

    /**
     * <p>
     *   Reads up to the specified <code>position</code>. All read bytes are
     *   stored in the internal byte cache.
     * </p>
     * <p>
     *   It is possible that, after invoking this method, the desired position
     *   is not reached, because there is no more data to read (the end of the
     *   stream has been reached).
     * </p>
     *
     * @param      position
     *                 the desired position to read up to.
     * @return     the total number of bytes read into the buffer, or
     *             <code>-1</code> if there is no more data because the end of
     *             the stream has been reached.
     * @throws     IOException
     *                 if an I/O error occurs.
     */
    private int readUpTo(final int position)
    throws IOException {
        if (inputStream == null) {
            return -1;
        }
        int _totalBytesRead = 0;
        if (this.position != position) {
            int _bytesRead = inputStream.read(readBuffer, 0, readBuffer.length);
            if (_bytesRead == -1) {
                InputStream _inputStream = inputStream;
                inputStream = null; // let garbage collector do its work!
                _inputStream.close(); // everything is cached now, close it!
                return -1;
            }
            if (_bytesRead != 0) {
                ensureCapacity(this.position + 1 + _bytesRead);
                System.arraycopy(
                    readBuffer, 0, cachedBytes, this.position + 1, _bytesRead);
                this.position += _bytesRead;
                _totalBytesRead += _bytesRead;
            }
            while (position > this.position) {
                _bytesRead = inputStream.read(readBuffer, 0, readBuffer.length);
                if (_bytesRead == -1) {
                    InputStream _inputStream = inputStream;
                    inputStream = null; // let garbage collector do its work!
                    _inputStream.close(); // everything is cached now, close it!
                    return _totalBytesRead;
                } else if (_bytesRead != 0) {
                    ensureCapacity(this.position + 1 + _bytesRead);
                    System.arraycopy(
                        readBuffer, 0,
                        cachedBytes, this.position + 1,
                        _bytesRead);
                    this.position += _bytesRead;
                    _totalBytesRead += _bytesRead;
                }
            }
        }
        return _totalBytesRead;
    }

    private int toUnsignedByte(final byte signedByte) {
        return (int)signedByte >= 0 ? signedByte : 256 + signedByte;
    }
}
