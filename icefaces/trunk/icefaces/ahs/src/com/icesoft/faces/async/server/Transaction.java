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

import com.icesoft.faces.net.http.HttpRequest;
import com.icesoft.faces.net.http.HttpResponse;

/**
 * <p>
 *   The <code>Transaction</code> class represents an object that contains an
 *   HTTP Request and HTTP Response together forming an HTTP transaction.
 * </p>
 * <p>
 *   In any point of time this <code>Transaction</code> can either hold no
 *   messages, an HTTP Request message, or an HTTP Request and HTTP Response
 *   message. It SHOULD never happen that this <code>Transaction</code> only
 *   holds an HTTP Response message.
 * </p>
 */
public class Transaction {
    private HttpRequest httpRequest;
    private HttpResponse httpResponse;

    /**
     * <p>
     *   Gets the HTTP Request message of this <code>Transaction</code>.
     * </p>
     *
     * @return     the HTTP Request message.
     * @see        #setHttpRequest(HttpRequest)
     */
    public HttpRequest getHttpRequest() {
        return httpRequest;
    }

    /**
     * <p>
     *   Gets the HTTP Response message of this <code>Transaction</code>.
     * </p>
     *
     * @return     the HTTP Response message.
     * @see        #setHttpResponse(HttpResponse)
     */
    public HttpResponse getHttpResponse() {
        return httpResponse;
    }

    /**
     * <p>
     *   Convenience method to determine if this <code>Transaction</code> has an
     *   HTTP Request message.
     * </p>
     *
     * @return     <code>true</code> if this <code>Transaction</code> has an
     *             HTTP Request message, <code>false</code> if not.
     * @see        #getHttpRequest()
     */
    public boolean hasHttpRequest() {
        return httpRequest != null;
    }

    /**
     * <p>
     *   Convenience method to determine if this <code>Transaction</code> has an
     *   HTTP Response message.
     * </p>
     *
     * @return     <code>true</code> if this <code>Transaction</code> has an
     *             HTTP Response message, <code>false</code> if not.
     * @see        #getHttpResponse()
     */
    public boolean hasHttpResponse() {
        return httpResponse != null;
    }

    /**
     * <p>
     *   Sets the HTTP Request message of this <code>Transaction</code> to the
     *   specified <code>httpRequest</code>.
     * </p>
     *
     * @param      httpRequest
     *                 the new HTTP Request message.
     * @see        #getHttpRequest()
     */
    public void setHttpRequest(final HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    /**
     * <p>
     *   Sets the HTTP Response message of this <code>Transaction</code> to the
     *   specified <code>httpResponse</code>.
     * </p>
     *
     * @param      httpResponse
     *                 the new HTTP Response message.
     * @see        #getHttpResponse()
     */
    public void setHttpResponse(final HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    public String toString() {
        return "Transaction: " + httpRequest + " -> " + httpResponse;
    }
}
