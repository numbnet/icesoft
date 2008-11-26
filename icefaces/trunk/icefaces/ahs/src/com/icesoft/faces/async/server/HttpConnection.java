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

import com.icesoft.faces.async.server.io.IoHttpConnection;
import com.icesoft.faces.async.server.nio.NioHttpConnection;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketAddress;

/**
 * <p>
 *   The <code>HttpConnection</code> interface defines the interface for classes
 *   that represent an HTTP connection. This can either be a blocking
 *   (standard I/O) or a non-blocking (new I/O) HTTP connection.
 * </p>
 *
 * @see        IoHttpConnection
 * @see        NioHttpConnection
 */
public interface HttpConnection {
    /**
     * <p>
     *   Closes the <code>HttpConnection</code>.
     * </p>
     */
    public void close();

    /**
     * <p>
     *   Get the exception, if any, associated with the
     *   <code>HttpConnection</code>.
     * </p>
     *
     * @return     the exception, or <code>null</code> if no exception is
     *             associated with the <code>HttpConnection</code>.
     * @see        #setException(Exception)
     */
    public Exception getException();

    public HttpConnectionState getHttpConnectionState();

    /**
     * <p>
     *   Gets the input stream of the <code>HttpConnection</code>.
     * </p>
     *
     * @return     the input stream.
     * @see        #getOutputStream()
     */
    public InputStream getInputStream();

    /**
     * <p>
     *   Gets the output stream of the <code>HttpConnection</code>.
     * </p>
     *
     * @return     the output stream.
     * @see        #getInputStream()
     */
    public OutputStream getOutputStream();

    public SocketAddress getRemoteSocketAddress();

    public Throwable getThrowable();

    /**
     * <p>
     *   Gets the current transaction, containing the possible HTTP Request and
     *   HTTP Response messages, of the <code>HttpConnection</code>.
     * </p>
     *
     * @return     the transaction.
     */
    public Transaction getTransaction();

    public boolean hasException();

    public boolean hasThrowable();

    /**
     * <p>
     *   Checks to see if a close has been requested to close the
     *   <code>HttpConnection</code>.
     * </p>
     *
     * @return     <code>true</code> if a close has been requested,
     *             <code>false</code> if not.
     * @see        #requestClose()
     * @see        #close()
     */
    public boolean isCloseRequested();

    /**
     * <p>
     *   Reads the next byte of data from the <code>HttpConnection</code>'s
     *   input stream. The value byte is returned as an int in the range
     *   <code>0</code> to <code>255</code>. If no byte is available because the
     *   end of the input stream has been reached, the value <code>-1</code> is
     *   returned.
     * </p>
     * <p>
     *   The invocation of this method is the same as
     *   <code>getInputStream().read()</code>.
     * </p>
     *
     * @return     the next byte of data, or <code>-1</code> if there is no more
     *             data because the end of the input stream has been reached.
     * @throws     IOException
     *                 if an I/O error occurs.
     * @throws     NullPointerException
     *                 if the input stream is <code>null</code>.
     * @see        #read(byte[])
     * @see        #read(byte[], int, int)
     */
    public int read()
    throws IOException, NullPointerException;

    /**
     * <p>
     *   Reads some number of bytes from the <code>HttpConnection</code>'s input
     *   stream and stores them into the specified <code>bytes</code>. The
     *   number of bytes actually read is returned as an integer.
     * </p>
     * <p>
     *   The invocation of this method is the same as
     *   <code>getInputStream().read(byte[])</code>.
     * </p>
     * <p>
     *   If <code>bytes</code> is <code>null</code>, a
     *   <code>NullPointerException</code> is thrown.
     * </p>
     * <p>
     *   If the length of <code>bytes</code> is <code>0</code>, then no bytes
     *   are read and <code>0</code> is returned; otherwise, there is an attempt
     *   to read at least one byte. If no byte is available because the end of
     *   the input stream has been reached, the value <code>-1</code> is
     *   returned; otherwise, at least one byte is read and stored into
     *   <code>bytes</code>.
     * </p>
     * <p>
     *   The first byte read is stored into element <code>bytes[0]</code>, the
     *   next one into <code>bytes[1]</code>, and so on. The number of bytes
     *   read is, at most, equal to the length of <code>bytes</code>. Let
     *   <i>k</i> be the number of bytes actually read; these bytes will be
     *   stored in elements <code>bytes[0]</code> through
     *   <code>bytes[</code><i>k</i><code>-1]</code>, leaving elements
     *   <code>bytes[</code><i>k</i><code>]</code> through
     *   <code>b[b.length-1]</code> unaffected.
     * </p>
     * <p>
     *   If the first byte cannot be read for any reason other than end of input
     *   stream, then an <code>IOException</code> is thrown. In particular, an
     *   <code>IOException</code> is thrown if the input stream has been closed.
     * </p>
     * <p>
     *   The <code>read(bytes)</code> method invocation has the same effect
     *   as:<br><br>
     *   <code> read(bytes, 0, bytes.length)</code>
     * </p>
     *
     * @param      bytes
     *                 the buffer into which the data is to be read.
     * @return     the total number of bytes read into the buffer, or
     *             <code>-1</code> if there is no more data because the end of
     *             the input stream has been reached.
     * @throws     IOException
     *                 if an I/O error occurs.
     * @throws     NullPointerException
     *                 if the input stream is <code>null</code>, or if the
     *                 specified <code>bytes</code> is <code>null</code>.
     * @see        #read()
     * @see        #read(byte[], int, int)
     */
    public int read(final byte[] bytes)
    throws IOException, NullPointerException;

    /**
     * <p>
     *   Reads up to the specified <code>length</code> bytes of data from the
     *   <code>HttpConnection</code>'s input stream into the specified
     *   <code>bytes</code> from the specified <code>offset</code>. An attempt
     *   is made to read as many as <code>length</code> bytes, but a smaller
     *   number may be read, possibly <code>0</code>. The number of bytes
     *   actually read is returned as an integer.
     * </p>
     * <p>
     *   The invocation of this method is the same as
     *   <code>getInputStream().read(byte[], int, int)</code>.
     * </p>
     * <p>
     *   If <code>bytes</code> is <code>null</code>, a
     *   <code>NullPointerException</code> is thrown. If <code>offset</code> is
     *   negative, or <code>length</code> is negative, or
     *   <code>offset+length</code> is greater than the length of
     *   <code>bytes</code>, then an <code>IndexOutOfBoundsException</code> is
     *   thrown.
     * </p>
     * <p>
     *   If <code>length</code> is <code>0</code>, then no bytes are read and
     *   <code>0</code> is returned; otherwise, there is an attempt to read at
     *   least one byte. If no byte is available because the end of the input
     *   stream has been reached, the value <code>-1</code> is returned;
     *   otherwise, at least one byte is read and stored into
     *   <code>bytes</code>.
     * </p>
     * <p>
     *   The first byte read is stored into element <code>bytes[offset]</code>,
     *   the next one into <code>bytes[offset+1]</code>, and so on. The number
     *   of bytes read is, at most, equal to <code>length</code>. Let <i>k</i>
     *   be the number of bytes actually read; these bytes will be stored in
     *   elements <code>bytes[offset]</code> through
     *   <code>bytes[offset+</code><i>k</i><code>-1]</code>, leaving elements
     *   <code>bytes[offset+</code><i>k</i><code>]</code> through
     *   <code>bytes[offset+length-1]</code> unaffected.
     * </p>
     * <p>
     *   In every case, elements <code>bytes[0]</code> through
     *   <code>bytes[offset]</code> and elements
     *   <code>bytes[offset+length]</code> through
     *   <code>bytes[bytes.length-1]</code> are unaffected.
     * </p>
     * <p>
     *   If the first byte cannot be read for any reason other than end of input
     *   stream, then an <code>IOException</code> is thrown. In particular, an
     *   <code>IOException</code> is thrown if the input stream has been closed.
     * </p>
     *
     * @param      bytes
     *                 the buffer into which the data is to be read.
     * @param      offset
     *                 the start offset in <code>bytes</code> at which the data
     *                 is written.
     * @param      length
     *                 the maximum number of bytes to be read.
     * @return     the total number of bytes read into the buffer, or
     *             <code>-1</code> if there is no more data because the end of
     *             the input stream has been reached.
     * @throws     IndexOutOfBoundsException
     *                 if the specified <code>offset</code> is negative, or the
     *                 specified <code>length</code> is negative, or
     *                 <code>offset+length</code> is greater than the length of
     *                 the specified <code>bytes</code>.
     * @throws     IOException
     *                 if an I/O error occurs.
     * @throws     NullPointerException
     *                 if the input stream is <code>null</code>, or if the
     *                 specified <code>bytes</code> is <code>null</code>.
     * @see        #read()
     * @see        #read(byte[])
     */
    public int read(final byte[] bytes, final int offset, final int length)
    throws IndexOutOfBoundsException, IOException, NullPointerException;

    /**
     * <p>
     *   Requests a close of the <code>HttpConnection</code>.
     * </p>
     * <p>
     *   Please note that this is only a request to close. The
     *   <code>HttpConnection</code> will not be immediately closed by invoking
     *   this method. When the <code>HttpConnection</code> is to be reset and
     *   a close request is done, it will actually be closed.
     * </p>
     *
     * @see        #reset()
     * @see        #isCloseRequested()
     * @see        #close()
     */
    public void requestClose();

    /**
     * <p>
     *   Resets the <code>HttpConnection</code>.
     * </p>
     * <p>
     *   That is, if a close has been requested the <code>HttpConnection</code>
     *   will be closed. Otherwise, the transaction is cleared and the
     *   <code>HttpConnection</code> is made eligible to receive another HTTP
     *   Request message.
     * </p>
     */
    public void reset();

    /**
     * <p>
     *   Sets the associated exception of the <code>HttpConnection</code> to the
     *   specified <code>exception</code..
     * </p>
     *
     * @param      exception
     *                 the new exception.
     * @see        #getException()
     */
    public void setException(final Exception exception);

    public void setThrowable(final Throwable throwable);

    /**
     * <p>
     *   Writes the specified <code>b</code> to the
     *   <code>HttpConnection</code>'s output stream. The byte to be written is
     *   the eight low-order bits of the argument <code>b</code>. The 24
     *   high-order bits of <code>b</code> are ignored.
     * <p>
     *   The invocation of this method is the same as
     *   <code>getOutputStream().write(byte)</code>.
     * </p>
     *
     * @param      b
     *                 the byte to be written.
     * @throws     IOException
     *                 if an I/O error occurs.
     * @throws     NullPointerException
     *                 if the output stream is <code>null</code>.
     * @see        #write(byte[])
     * @see        #write(byte[], int, int)
     */
    public void write(final int b)
    throws IOException, NullPointerException;

    /**
     * <p>
     *   Writes <code>bytes.length</code> bytes from the specified
     *   <code>bytes</code> to the <code>HttpConnection</code>'s output stream.
     * </p>
     * <p>
     *   The invocation of this method is the same as
     *   <code>getOutputStream().write(byte[])</code>.
     * </p>
     * <p>
     *   The <code>read(bytes)</code> method invocation has the same effect
     *   as:<br><br>
     *   <code> write(bytes, 0, bytes.length)</code>
     * </p>
     *
     * @param      bytes
     *                 the bytes to be written.
     * @throws     IOException
     *                 if an I/O error occurs.
     * @throws     NullPointerException
     *                 if the output stream is <code>null</code>.
     * @see        #write(int)
     * @see        #write(byte[], int, int)
     */
    public void write(final byte[] bytes)
    throws IOException, NullPointerException;

    /**
     * <p>
     *   Writes the specified <code>length</code> bytes from the specified
     *   <code>bytes</code> starting at the specified <code>offset</code> off to
     *   the <code>HttpConnection</code>'s output stream.
     * </p>
     * <p>
     *   The invocation of this method is the same as
     *   <code>getOutputStream().write(byte[], int, int)</code>.
     * </p>
     * <p>
     *   If <code>bytes</code> is <code>null</code>, a
     *   <code>NullPointerException</code> is thrown.
     * </p>
     * <p>
     *   If <code>offset</code> is negative, or <code>length</code> is negative,
     *   or <code>offset+length</code> is greater than the length of
     *   <code>bytes</code>, then an <code>IndexOutOfBoundsException</code> is
     *   thrown.
     * </p>
     * <p>
     *   Element <code>bytes[offset]</code> is the first byte written and
     *   element <code>bytes[offset+length-1]</code> is the last byte written by
     *   this operation.
     * </p>
     *
     * @param      bytes
     *                 the bytes to be written.
     * @param      offset
     *                 the start offset in <code>bytes</code> from which the
     *                 bytes are to be written.
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
     *                 if the output stream is <code>null</code>, or if the
     *                 specified <code>bytes</code> is <code>null</code>.
     * @see        #write(int)
     * @see        #write(byte[])
     */
    public void write(final byte[] bytes, final int offset, final int length)
    throws IndexOutOfBoundsException, IOException, NullPointerException;
}
