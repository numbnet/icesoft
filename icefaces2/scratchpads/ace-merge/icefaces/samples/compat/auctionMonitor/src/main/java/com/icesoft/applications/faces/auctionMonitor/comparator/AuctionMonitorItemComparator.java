/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
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
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package com.icesoft.applications.faces.auctionMonitor.comparator;

import com.icesoft.applications.faces.auctionMonitor.beans.AuctionMonitorItemBean;

import java.util.Comparator;

import java.io.Serializable;

/**
 * General comparator used as a base class for all other comparators Overall
 * this class performs a comparison of two auction items
 */
public abstract class AuctionMonitorItemComparator implements Comparator, Serializable {
    public boolean isAscending = true;

    public int compare(Object o1, Object o2) {
        if ((!(o1 instanceof AuctionMonitorItemBean))) {
            throw new ClassCastException(
                    "AuctionMonitorItemBean comparator compare invoked on arbitrary object");
        }

        AuctionMonitorItemBean item1 = (AuctionMonitorItemBean) o1;
        AuctionMonitorItemBean item2 = (AuctionMonitorItemBean) o2;

        if (isAscending) {
            return (compare(item1, item2));
        }

        return (compare(item2, item1));
    }

    public abstract int compare(AuctionMonitorItemBean item1,
                                AuctionMonitorItemBean item2);
}
