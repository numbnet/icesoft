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

import com.icesoft.faces.async.common.ExecuteQueue;

/**
 * <p>
 *   The <code>AbstractHandler</code> class provides a base implementation of
 *   the <code>Handler</code> interface. In addition, as the
 *   <code>AbstractHandler</code> is a pooled object, it also keeps the handler
 *   pool that created it as an accessible association.
 * </p>
 */
public abstract class AbstractHandler
extends com.icesoft.faces.async.common.AbstractHandler
implements Handler, Runnable {
    /**
     * The Asynchronous HTTP Server associated with this
     * <code>AbstractHandler</code>.
     */
    protected AsyncHttpServer asyncHttpServer;
    /**
     * The HTTP connection that needs to be handled by this
     * <code>AbstractHandler</code>.
     */
    protected HttpConnection httpConnection;

    /**
     * <p>
     *   Constructs an <code>AbstractHandler</code> object.
     * </p>
     */
    protected AbstractHandler(
        final ExecuteQueue executeQueue, final AsyncHttpServer asyncHttpServer)
    throws IllegalArgumentException {
        super(executeQueue);
        if (asyncHttpServer == null) {
            throw new IllegalArgumentException("asyncHttpServer is null");
        }
        this.asyncHttpServer = asyncHttpServer;
    }

    public AsyncHttpServer getAsyncHttpServer() {
        return asyncHttpServer;
    }

    public HttpConnection getHttpConnection() {
        return httpConnection;
    }

    public void handle() {
        if (httpConnection != null) {
            executeQueue.execute(this);
        }
    }

    public void setHttpConnection(final HttpConnection httpConnection) {
        this.httpConnection = httpConnection;
    }
}
