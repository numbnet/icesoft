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
 *   The <code>Handler</code> interface defines the interface for classes that
 *   need to handle an HTTP connection.
 * </p>
 * <p>
 *   Please note that it is preferred to invoke <code>{@link #handle()}</code>
 *   instead of <code>{@link #run()}</code>.
 * </p>
 */
public interface Handler
extends com.icesoft.faces.async.common.Handler {
    /**
     * <p>
     *   Gets the Asynchronous HTTP Server associated with the
     *   <code>Handler</code>.
     * </p>
     *
     * @return     the Asynchronous HTTP Server.
     */
    public AsyncHttpServer getAsyncHttpServer();

    /**
     * <p>
     *   Gets the HTTP connection associated with the <code>Handler</code>.
     * </p>
     *
     * @return     the HTTP connection.
     * @see        #setHttpConnection(HttpConnection)
     */
    public HttpConnection getHttpConnection();

    /**
     * <p>
     *   Sets the HTTP connection associated with the <code>Handler</code> to
     *   the specified <code>asyncHttpServer</code>.
     * </p>
     *
     * @param      httpConnection
     *                 the new HTTP connection.
     * @see        #getHttpConnection()
     */
    public void setHttpConnection(final HttpConnection httpConnection);
}
