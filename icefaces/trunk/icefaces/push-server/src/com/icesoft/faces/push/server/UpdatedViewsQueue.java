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
package com.icesoft.faces.push.server;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.icefaces.push.server.UpdatedViews;

public class UpdatedViewsQueue
extends org.icefaces.push.server.UpdatedViewsQueue {
    private static final Log LOG = LogFactory.getLog(UpdatedViewsQueue.class);

    private int purgeCounter;
    private double threshold;

    public UpdatedViewsQueue(
        final String iceFacesId,
        final UpdatedViewsManager updatedViewsManager) {

        super(iceFacesId, updatedViewsManager);
        if (this.updatedViewsManager != null) {
            this.threshold =
                ((UpdatedViewsManager)this.updatedViewsManager).
                    getUpdatedViewsQueueThreshold();
        }
    }

    public int getPurgeCounter() {
        return purgeCounter;
    }

    public void purge(final long sequenceNumber) {
        purge(sequenceNumber, false);
    }

    public void purge(
        final long sequenceNumber, final boolean ignorePurgeCounter) {

        if (sequenceNumber > 0) {
            final Iterator _updatedViewsIterator = iterator();
            while (_updatedViewsIterator.hasNext()) {
                final UpdatedViews _updatedViews =
                    (UpdatedViews)_updatedViewsIterator.next();
                if (_updatedViews.getSequenceNumber() > sequenceNumber) {
                    break;
                }
                _updatedViewsIterator.remove();
                if (!ignorePurgeCounter) {
                    purgeCounter++;
                }
                if (updatedViewsManager != null) {
                    ((UpdatedViewsManager)updatedViewsManager).
                        purged(iceFacesId, _updatedViews.getSequenceNumber());
                }
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Purged up to " + sequenceNumber + " from " + this);
            }
            if (updatedViewsManager != null &&
                purgeCounter > size * threshold) {

                ((UpdatedViewsManager)updatedViewsManager).
                    publishPurgeMap(iceFacesId);
            }
        }
    }

    public void resetPurgeCounter() {
        purgeCounter = 0;
    }

    public String toString() {
        return
            super.toString() + "\r\n" +
            "        purge counter   : " + purgeCounter;
    }
}
