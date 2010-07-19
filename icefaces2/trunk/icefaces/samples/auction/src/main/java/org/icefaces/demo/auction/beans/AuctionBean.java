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

package org.icefaces.demo.auction.beans;

import org.icefaces.demo.auction.AuctionEvent;
import org.icefaces.demo.auction.AuctionMonitorItemDetailer;
import org.icefaces.demo.auction.comparator.AuctionMonitorItemBidsComparator;
import org.icefaces.demo.auction.comparator.AuctionMonitorItemComparator;
import org.icefaces.demo.auction.comparator.AuctionMonitorItemPriceComparator;
import org.icefaces.demo.auction.comparator.AuctionMonitorItemTimeLeftComparator;
import org.icefaces.demo.auction.comparator.AuctionMonitorItemTitleComparator;
import org.icefaces.demo.auction.stubs.ItemType;
import org.icefaces.demo.auction.stubs.StubServer;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.Serializable;
import java.util.Arrays;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * Class used to handle searching and sorting of auction items, as well as front
 * end interpretation
 */
@ManagedBean(name = "AuctionBean")
@ViewScoped
public class AuctionBean implements Serializable {
//    private static Log log = LogFactory.getLog(AuctionBean.class);
    private static int userCount = 0;
    public static final String RENDERER_NAME = "demand";
    private static final String SUCCESS = "success";
    private String queryItemID;
    private String queryString;
    private AuctionMonitorItemBean[] searchItemBeans;
    private AuctionMonitorItemComparator searchItemsComparator =
            new AuctionMonitorItemTimeLeftComparator();
    private String autoLoad = " ";
    private boolean isFreshSearch;
    private AuctionMonitorItemBean queryItem;

    // style related constants

    // title text
    private static final String TABLE_HEADER_ASC_TITLE = "tableHeaderSelAsc1";
    private static final String TABLE_HEADER_DESC_TITLE = "tableHeaderSelDesc1";
    private static final String TABLE_HEADER_TITLE = "column1";

    // bids text
    private static final String TABLE_HEADER_ASC_BID = "tableHeaderSelAsc2";
    private static final String TABLE_HEADER_DESC_BID = "tableHeaderSelDesc2";
    private static final String TABLE_HEADER_BID = "column2";

    // price text
    private static final String TABLE_HEADER_ASC_PRICE = "tableHeaderSelAsc3";
    private static final String TABLE_HEADER_DESC_PRICE = "tableHeaderSelDesc3";
    private static final String TABLE_HEADER_PRICE = "column3";

    // time left column
    private static final String TABLE_HEADER_ASC_TIME = "tableHeaderSelAsc4";
    private static final String TABLE_HEADER_DESC_TIME = "tableHeaderSelDesc4";
    private static final String TABLE_HEADER_TIME = "column4";


    public AuctionBean() {
    }

    public ItemType getItem(String itemIDStr) throws Exception {
        return StubServer.getInstance().getItem(itemIDStr);
    }

    public ItemType[] getSearchResults(String filterString) throws Exception {
        return StubServer.getInstance().getSearchResults();
    }

    public synchronized AuctionMonitorItemBean[] getSearchItems() {
        if (null == queryString) {
            return null;
        }

        if (!isFreshSearch) {
            sortSearchItems();
            return searchItemBeans;
        }

        try {
            ItemType searchItems[] = getSearchResults(queryString);
            if (null == searchItems) {
                return null;
            }
            searchItemBeans = new AuctionMonitorItemBean[searchItems.length];

            for (int i = 0; i < searchItems.length; i++) {
                searchItemBeans[i] = new AuctionMonitorItemBean(searchItems[i]);
            }
            sortSearchItems();
            isFreshSearch = false;
            Thread t = new Thread(
                    new AuctionMonitorItemDetailer(this, searchItemBeans));
            t.start();
            return searchItemBeans;
        } catch (Exception e) {
//            if (log.isErrorEnabled()) {
//                log.error(
//                        "Failed to read the available search items because of " +
//                        e);
//            }
        }
        return null;
    }

    public void reSearchItems() {
        isFreshSearch = true;
        getSearchItems();
    }

    public String getTitleTextStyle() {
        if (searchItemsComparator instanceof AuctionMonitorItemTitleComparator)
        {
            if (searchItemsComparator.isAscending) {
                return TABLE_HEADER_ASC_TITLE;
            } else {
                return TABLE_HEADER_DESC_TITLE;
            }
        } else {
            return TABLE_HEADER_TITLE;
        }
    }

    public String getBidsTextStyle() {
        if (searchItemsComparator instanceof AuctionMonitorItemBidsComparator) {
            if (searchItemsComparator.isAscending) {
                return TABLE_HEADER_ASC_BID;
            } else {
                return TABLE_HEADER_DESC_BID;
            }
        } else {
            return TABLE_HEADER_BID;
        }
    }

    public String getPriceTextStyle() {
        if (searchItemsComparator instanceof AuctionMonitorItemPriceComparator)
        {
            if (searchItemsComparator.isAscending) {
                return TABLE_HEADER_ASC_PRICE;
            } else {
                return TABLE_HEADER_DESC_PRICE;
            }
        } else {
            return TABLE_HEADER_PRICE;
        }
    }

    public String getTimeLeftTextStyle() {
        if (searchItemsComparator instanceof AuctionMonitorItemTimeLeftComparator)
        {
            if (searchItemsComparator.isAscending) {
                return TABLE_HEADER_ASC_TIME;
            } else {
                return TABLE_HEADER_DESC_TIME;
            }
        } else {
            return TABLE_HEADER_TIME;
        }
    }

    public void setQueryItemID(String queryItemID) {
        this.queryItemID = queryItemID;
        try {
            queryItem = new AuctionMonitorItemBean(getItem(queryItemID));
        } catch (Exception e) {
//            if (log.isWarnEnabled()) {
//                log.warn("Failed to retrieve a query item based on ID " +
//                         queryItemID + " because of " + e);
//            }
        }
    }

    public String getQueryItemID() {
        return queryItemID;
    }

    public void setQueryItem(AuctionMonitorItemBean queryItem) {
        this.queryItem = queryItem;
    }

    public AuctionMonitorItemBean getQueryItem() {
        return queryItem;
    }

    public void setQueryString(String queryString) {
        if ("".equals(queryString)) {
            return;
        }

        if (queryString.equals(this.queryString)) {
            return;
        }

        this.queryString = queryString;
        isFreshSearch = true;
    }

    public String getQueryString() {
        return queryString;
    }

/*
    private Collection getListAsSelectItems(ArrayList list) {
        if (list == null) {
            return new ArrayList();
        }
        
        ArrayList selectItems = new ArrayList( list.size() );
        Integer val;
        
        for(int index = 0; index < list.size(); index++) {
            val = (Integer)list.get(index);
            int seconds = val.intValue()/1000;
            selectItems.add(new SelectItem(val, Integer.toString(seconds), ""));
        }
        
        return selectItems;
    }
*/

    public String getAutoLoad() {
        if (this.autoLoad.equals(" ")) {
            this.autoLoad = "Loaded";
            this.setQueryString("ice");
        }

        return this.autoLoad;
    }

    public String sortByTitle() {
        if (searchItemsComparator instanceof AuctionMonitorItemTitleComparator)
        {
            searchItemsComparator.isAscending =
                    !searchItemsComparator.isAscending;
        } else {
            searchItemsComparator = new AuctionMonitorItemTitleComparator();
        }

        sortSearchItems();
        return SUCCESS;
    }

    public String sortByTimeLeft() {
        if (searchItemsComparator instanceof AuctionMonitorItemTimeLeftComparator)
        {
            searchItemsComparator.isAscending =
                    !searchItemsComparator.isAscending;
        } else {
            searchItemsComparator = new AuctionMonitorItemTimeLeftComparator();
        }

        sortSearchItems();
        return SUCCESS;
    }

    public String sortByBids() {
        if (searchItemsComparator instanceof AuctionMonitorItemBidsComparator) {
            searchItemsComparator.isAscending =
                    !searchItemsComparator.isAscending;
        } else {
            searchItemsComparator = new AuctionMonitorItemBidsComparator();
        }

        sortSearchItems();
        return SUCCESS;
    }

    public String sortByPrice() {
        if (searchItemsComparator instanceof AuctionMonitorItemPriceComparator)
        {
            searchItemsComparator.isAscending =
                    !searchItemsComparator.isAscending;
        } else {
            searchItemsComparator = new AuctionMonitorItemPriceComparator();
        }

        sortSearchItems();
        return SUCCESS;
    }

    private void sortSearchItems() {
        if (searchItemBeans == null) {
            return;
        }

        Arrays.sort(searchItemBeans, searchItemsComparator);
    }

    public static synchronized void incrementUsers() {
        userCount++;
    }

    public static synchronized void decrementUsers() {
        userCount--;

        if (userCount <= 0) {
            userCount = 0;
            StubServer.resetAuction();
        }
    }

}
