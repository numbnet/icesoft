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

/**
 * <p>
 *   The <code>HttpConnectionAcceptor</code> interface defines the interface for
 *   classes that need to accept incoming HTTP connection requests.
 * </p>
 */
public interface HttpConnectionAcceptor {
    /**
     * <p>
     *   Adds the specified <code>httpConnectionAcceptorListener</code> to this
     *   <code>HttpConnectionAcceptor</code>.
     * </p>
     *
     * @param      httpConnectionAcceptorListener
     *                 the HTTP connection acceptor listener to be added.
     * @see        #removeHttpConnectionAcceptorListener(HttpConnectionAcceptorListener)
     */
    public void addHttpConnectionAcceptorListener(
        final HttpConnectionAcceptorListener httpConnectionAcceptorListener);

    public void doneReading(final HttpConnection httpConnection);

    /**
     * <p>
     *   Gets the port this <code>HttpConnectionAcceptor</code> listens to to
     *   accept incoming HTTP connections.
     * </p>
     *
     * @return     the port.
     */
    public int getPort();

    /**
     * <p>
     *   Checks to see if a stop has been requested to stop this
     *   <code>HttpConnectionAcceptor</code>.
     * </p>
     *
     * @return     <code>true</code> if a stop has been requested,
     *             <code>false</code> if not.
     * @see        #requestStop()
     */
    public boolean isStopRequested();

    /**
     * <p>
     *   Removes the specified <code>httpConnectionAcceptorListener</code> from
     *   this <code>HttpConnectionAcceptor</code>.
     * </p>
     *
     * @param      httpConnectionAcceptorListener
     *                 the HTTP connection acceptor listener to be removed.
     * @see        #addHttpConnectionAcceptorListener(HttpConnectionAcceptorListener)
     */
    public void removeHttpConnectionAcceptorListener(
        final HttpConnectionAcceptorListener httpConnectionAcceptorListener);

    /**
     * <p>
     *   Requests a stop of this <code>HttpConnectionAcceptor</code>.
     * </p>
     * <p>
     *   Please note that this is only a request to stop. This
     *   <code>HttpConnectionAcceptor</code> will not be immediately stopped by
     *   invoking this method.
     * </p>
     * <p>
     *   When a stop has been requested an
     *   <code>HttpConnectionAcceptorEvent</code> is fired to all registered
     *   <code>HttpConnectionAcceptorListener</code>s indicating a stop has been
     *   requested.
     * </p>
     * <p>
     *   When this <code>HttpConnectionAcceptor</code> has finally stopped
     *   another <code>HttpConnectionAcceptorEvent</code> is fired to all
     *   registered <code>HttpConnectionAcceptorListener</code>s indicating it
     *   has stopped.
     * </p>
     *
     * @see        HttpConnectionAcceptorEvent
     * @see        HttpConnectionAcceptorListener
     * @see        #isStopRequested()
     * @see        #start()
     */
    public void requestStop();

    public void setPendingReadHandler(
        final Object key, final ReadHandler readHandler);

    /**
     * <p>
     *   Starts this <code>HttpConnectionAcceptor</code>.
     * </p>
     * <p>
     *   When it is started an <code>HttpConnectionAcceptorEvent</code> is fired
     *   to all registered <code>HttpConnectionAcceptorListener</code>s
     *   indicating it has started.
     * </p>
     *
     * @see        HttpConnectionAcceptorEvent
     * @see        HttpConnectionAcceptorListener
     * @see        #requestStop()
     * @see        #isStopRequested()
     */
    public void start();
}
