/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.icesoft.applications.faces.auctionMonitor.comparator;

import com.icesoft.applications.faces.auctionMonitor.beans.AuctionMonitorItemBean;

/**
 * Comparator used to compare two auction monitor items
 */
public class AuctionMonitorItemBidsComparator
        extends AuctionMonitorItemComparator {
    public int compare(AuctionMonitorItemBean bean1,
                       AuctionMonitorItemBean bean2) {
        return ((bean1.getBidCount() - bean2.getBidCount()));
    }
}
