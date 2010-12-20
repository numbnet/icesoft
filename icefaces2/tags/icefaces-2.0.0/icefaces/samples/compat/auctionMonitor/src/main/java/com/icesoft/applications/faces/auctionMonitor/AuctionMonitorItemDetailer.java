/*
 * Version: MPL 1.1
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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 */

package com.icesoft.applications.faces.auctionMonitor;

import com.icesoft.applications.faces.auctionMonitor.beans.AuctionBean;
import com.icesoft.applications.faces.auctionMonitor.beans.AuctionMonitorItemBean;
import com.icesoft.applications.faces.auctionMonitor.stubs.ItemType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class used to populate auction items with information on urls, descriptions,
 * etc. All item detailing is done in a separate thread so user interaction is
 * not blocked
 */
public class AuctionMonitorItemDetailer implements Runnable {
    private static Log log =
            LogFactory.getLog(AuctionMonitorItemDetailer.class);
    private AuctionMonitorItemBean[] searchItemBeans;
    private AuctionBean auctionBean;

    public AuctionMonitorItemDetailer(AuctionBean auctionBean,
                                      AuctionMonitorItemBean[] searchItemBeans) {
        this.auctionBean = auctionBean;
        this.searchItemBeans =
                (AuctionMonitorItemBean[]) searchItemBeans.clone();
    }

    public void run() {
        AuctionMonitorItemBean itemBean;
        for (int i = 0, max = searchItemBeans.length; i < max; i++) {
            try {
                itemBean = searchItemBeans[i];
                ItemType item = auctionBean.getItem(itemBean.getItemID());
                itemBean.setDescription(item.getDescription());
                itemBean.setSeller(item.getSeller());
                itemBean.setLocation(item.getLocation());
            } catch (NullPointerException npe) {
                // intentionally left blank
            } catch (Exception e) {
                if (log.isWarnEnabled()) {
                    log.warn("A threaded item detailer failed because of " + e);
                }
            }
        }
    }
}