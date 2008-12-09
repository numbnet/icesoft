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

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractHttpConnection
implements HttpConnection {
    private static final Log LOG =
        LogFactory.getLog(AbstractHttpConnection.class);

    protected boolean closeRequested;
    protected Exception exception;
    protected HttpConnectionState httpConnectionState =
        new HttpConnectionState();
    protected InputStream inputStream;
    protected OutputStream outputStream;
    protected Transaction transaction = new Transaction();
    protected Throwable throwable;

    protected AbstractHttpConnection() {
        // do nothing.
    }

    public void close() {
        try {
            inputStream.close();
        } catch (IOException exception) {
            if (LOG.isErrorEnabled()) {
                LOG.error(
                    "An I/O error occurred while closing the input stream!",
                    exception);
            }
        }
        try {
            outputStream.close();
        } catch (IOException exception) {
            if (LOG.isErrorEnabled()) {
                LOG.error(
                    "An I/O error occurred while closing the output stream!",
                    exception);
            }
        }
    }

    public Exception getException() {
        return exception;
    }

    public HttpConnectionState getHttpConnectionState() {
        return httpConnectionState;
    }

    public InputStream getInputStream() {
        return new WrapperInputStream(inputStream);
    }

    public OutputStream getOutputStream() {
        return new WrapperOutputStream(outputStream);
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public boolean hasException() {
        return exception != null;
    }

    public boolean hasThrowable() {
        return throwable != null;
    }

    public boolean isCloseRequested() {
        return closeRequested;
    }

    public int read()
    throws IOException, NullPointerException {
        if (inputStream == null) {
            throw new NullPointerException("inputStream is null");
        }
        return inputStream.read();
    }

    public int read(final byte[] bytes)
    throws IOException, NullPointerException {
        if (inputStream == null) {
            throw new NullPointerException("inputStream is null");
        }
        return inputStream.read(bytes);
    }

    public int read(final byte[] bytes, final int offset, final int length)
    throws IndexOutOfBoundsException, IOException, NullPointerException {
        if (inputStream == null) {
            throw new NullPointerException("inputStream is null");
        }
        return inputStream.read(bytes, offset, length);
    }

    public void requestClose() {
        closeRequested = true;
    }

    public void reset() {
        if (closeRequested) {
            close();
        } else {
            transaction = new Transaction();
        }
    }

    public void setException(final Exception exception) {
        this.exception = exception;
    }

    public void setThrowable(final Throwable throwable) {
        this.throwable = throwable;
    }

    public void write(final int b)
    throws IOException, NullPointerException {
        if (outputStream == null) {
            throw new NullPointerException("outputStream is null");
        }
        outputStream.write(b);
        outputStream.flush();
    }

    public void write(final byte[] bytes)
    throws IOException, NullPointerException {
        if (outputStream == null) {
            throw new NullPointerException("outputStream is null");
        }
        outputStream.write(bytes);
        outputStream.flush();
    }

    public void write(final byte[] bytes, final int offset, final int length)
    throws IndexOutOfBoundsException, IOException, NullPointerException {
        if (outputStream == null) {
            throw new NullPointerException("outputStream is null");
        }
        outputStream.write(bytes, offset, length);
        outputStream.flush();
    }

    private static class WrapperInputStream
    extends InputStream {
        private boolean closed;
        private InputStream inputStream;

        private WrapperInputStream(final InputStream inputStream) {
            this.inputStream = inputStream;
        }

        public int available()
        throws IOException {
            if (closed) {
                throw new IOException("Stream is closed!");
            }
            return inputStream.available();
        }

        public void close()
        throws IOException {
            if (!closed) {
                /*
                 * This does not close the underlying InputStream! Just loses
                 * the reference to it.
                 */
                inputStream = null;
                closed = true;
            }
        }

        public void mark(final int readLimit) {
            // do nothing.
        }

        public boolean markSupported() {
            return false;
        }

        public int read()
        throws IOException {
            if (closed) {
                throw new IOException("Stream is closed!");
            }
            return inputStream.read();
        }

        public int read(final byte[] bytes)
        throws IOException {
            if (closed) {
                throw new IOException("Stream is closed!");
            }
            return inputStream.read(bytes);
        }

        public int read(final byte[] bytes, final int offset, final int length)
        throws IOException {
            if (closed) {
                throw new IOException("Stream is closed!");
            }
            return inputStream.read(bytes, offset, length);
        }

        public void reset()
        throws IOException {
            throw new IOException("mark/reset not supported!");
        }

        public long skip(final long numberOfBytes)
        throws IOException {
            if (closed) {
                throw new IOException("Stream is closed!");
            }
            return inputStream.skip(numberOfBytes);
        }
    }

    private static class WrapperOutputStream
    extends OutputStream {
        private boolean closed;
        private OutputStream outputStream;

        private WrapperOutputStream(final OutputStream outputStream) {
            this.outputStream = outputStream;
        }

        public void close()
        throws IOException {
            if (!closed) {
                /*
                 * This does not close the underlying OutputStream! Just loses
                 * the reference to it.
                 */
                outputStream = null;
                closed = true;
            }
        }

        public void flush()
        throws IOException {
            if (closed) {
                throw new IOException("Stream is closed!");
            }
            outputStream.flush();
        }

        public void write(final int b)
        throws IOException {
            if (closed) {
                throw new IOException("Stream is closed!");
            }
            outputStream.write(b);
        }

        public void write(final byte[] bytes)
        throws IOException {
            if (closed) {
                throw new IOException("Stream is closed!");
            }
            outputStream.write(bytes);
        }

        public void write(
            final byte[] bytes, final int offset, final int length)
        throws IOException {
            if (closed) {
                throw new IOException("Stream is closed!");
            }
            outputStream.write(bytes, offset, length);
        }
    }
}
