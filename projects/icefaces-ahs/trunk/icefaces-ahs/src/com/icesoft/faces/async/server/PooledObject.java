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
 *   The <code>PooledObject</code> class represents an object that can be
 *   pooled.
 * </p>
 * <p>
 *   When a <code>PooledObject</code> is to be "leased" to a client, the
 *   <code>{@link #lease()}</code> method SHOULD be invoked before giving the
 *   <code>PooledObject</code> to the client. When the client returns the
 *   <code>PooledObject</code>, the <code>{@link #expireLease()}</code> method
 *   SHOULD be invoked to make the <code>PooledObject</code> eligible for being
 *   "leased" to another client again.
 * </p>
 */
public abstract class PooledObject {
    protected boolean inUse;

    /**
     * <p>
     *   Checks to see if this <code>PooledObject</code> is eligible to be
     *   leased.
     * </p>
     * <p>
     *   If this <code>PooledObject</code> is already in use, <code>false</code>
     *   is returned, otherwise this <code>PooledObject</code> is marked in use
     *   and <code>true</code> is returned.
     * </p>
     *
     * @return     <code>true</code> if this <code>PooledObject</code> is
     *             eligible to be leased, <code>false</code> if not.
     * @see        #expireLease()
     */
    protected synchronized boolean lease() {
        if (inUse) {
            return false;
        } else {
            inUse = true;
            return true;
        }
    }

    /**
     * <p>
     *   Expires the lease of this <code>PooledObject</code> by marking it not
     *   in use.
     * </p>
     *
     * @see        #lease()
     */
    protected void expireLease() {
        inUse = false;
    }
}
