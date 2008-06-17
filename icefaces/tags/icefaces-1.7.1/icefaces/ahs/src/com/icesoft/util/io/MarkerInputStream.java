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

public class MarkerInputStream
extends InputStream {
    private static final int INVALID = Integer.MIN_VALUE;
    private static final int UNLIMITED = Integer.MAX_VALUE;
    private boolean closed;
    private LazyCachedInputStream lazyCachedInputStream;
    private int mark = INVALID;
    private int position = -1;
    private int readLimit = -1;

    public MarkerInputStream(
        final LazyCachedInputStream lazyCachedInputStream) {

        this.lazyCachedInputStream = lazyCachedInputStream;
    }

    public int available()
    throws IOException {
        if (closed) {
            throw new IOException("stream is closed");
        }
        return lazyCachedInputStream.available() - (position + 1);
    }

    private void checkReadLimit() {
        if (mark != INVALID && readLimit != UNLIMITED) {
            if (position - mark > readLimit) {
                mark = INVALID;
                readLimit = -1;
            }
        }
    }

    public void close()
    throws IOException {
        if (!closed) {
            lazyCachedInputStream = null;
            position = -1;
            closed = true;
        }
    }

    /**
     * <p>
     *   Marks the current position in this input stream. A subsequent call to
     *   the <code>reset()</code> method repositions this stream at the last
     *   marked position so that subsequent reads re-read the same bytes.
     * </p>
     * <p>
     *   Using this method, there is no read limit!
     * </p>
     *
     * @see        #mark(int)
     * @see        #reset()
     */
    public void mark() {
        mark(UNLIMITED);
    }

    public void mark(final int readLimit) {
        mark = position;
        this.readLimit = (readLimit >= 0 ? readLimit : 0);
    }

    public boolean markSupported() {
        return true;
    }

    public int read()
    throws IOException {
        if (closed) {
            throw new IOException("read error: stream is closed");
        }
        int _byte = lazyCachedInputStream.read(position + 1);
        if (_byte != -1) {
            position++;
        }
        checkReadLimit();
        return _byte;
    }

    public int read(final byte[] bytes)
    throws IOException {
        if (closed) {
            throw new IOException("read error: stream is closed");
        }
        int _bytesRead =
            lazyCachedInputStream.read(
                position + 1, bytes, 0, bytes.length);
        if (_bytesRead != -1) {
            position += _bytesRead;
        }
        checkReadLimit();
        return _bytesRead;
    }

    public int read(final byte[] bytes, final int offset, final int length)
    throws IOException {
        if (closed) {
            throw new IOException("read error: stream is closed");
        }
        int _bytesRead =
            lazyCachedInputStream.read(position + 1, bytes, offset, length);
        if (_bytesRead != -1) {
            position += _bytesRead;
        }
        checkReadLimit();
        return _bytesRead;
    }

    /**
     * <p>
     *   Repositions this stream to the position at the time the mark method was
     *   last called on this input stream.
     * </p>
     * <p>
     *   If either of the methods <code>mark()</code> and <code>mark(int)</code>
     *   have not been called since the stream was created, or the number of
     *   bytes read from the stream since <code>mark(int)</code> was last called
     *   is larger than the argument to <code>mark(int)</code> at that last
     *   call, then an <code>IOException</code> is be thrown.
     * </p>
     *
     * @throws     IOException if this stream has not been marked or if the mark
     *             has been invalidated.
     * @see        #mark()
     * @see        #mark(int)
     */
    public void reset()
    throws IOException {
        if (mark == INVALID) {
            throw
                new IOException(
                    "mark has not been set or read limit was exceeded");
        }
        position = mark;
    }
}
