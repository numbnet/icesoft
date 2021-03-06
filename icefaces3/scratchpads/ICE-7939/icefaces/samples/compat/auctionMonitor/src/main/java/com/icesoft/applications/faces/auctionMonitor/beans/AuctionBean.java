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

package com.icesoft.applications.faces.auctionMonitor.beans;

import com.icesoft.applications.faces.auctionMonitor.AuctionEvent;
import com.icesoft.applications.faces.auctionMonitor.AuctionMonitorItemDetailer;
import com.icesoft.applications.faces.auctionMonitor.comparator.AuctionMonitorItemBidsComparator;
import com.icesoft.applications.faces.auctionMonitor.comparator.AuctionMonitorItemComparator;
import com.icesoft.applications.faces.auctionMonitor.comparator.AuctionMonitorItemPriceComparator;
import com.icesoft.applications.faces.auctionMonitor.comparator.AuctionMonitorItemTimeLeftComparator;
import com.icesoft.applications.faces.auctionMonitor.comparator.AuctionMonitorItemTitleComparator;
import com.icesoft.applications.faces.auctionMonitor.stubs.ItemType;
import com.icesoft.applications.faces.auctionMonitor.stubs.StubServer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.util.Arrays;
import java.io.Serializable;

/**
 * Class used to handle searching and sorting of auction items, as well as front
 * end interpretation
 */
public class AuctionBean implements Serializable  {
    private static Log log = LogFactory.getLog(AuctionBean.class);
    private static int userCount = 0;
    public static final String RENDERER_NAME = "demand";
    private static final String SUCCESS = null;
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
            if (log.isErrorEnabled()) {
                log.error(
                        "Failed to read the available search items because of " +
                        e);
            }
        }
        return null;
    }

    /**
     * Called when a bit button is pressed which causes the current auction
     * element to collapse
     * @return
     */
    public String pressBidButton() {
        // get row instance that was clicked on .
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        AuctionMonitorItemBean auctionMonitorItemBean = (AuctionMonitorItemBean)
                externalContext.getRequestMap().get("item");
        // toggle the expanded state.
        auctionMonitorItemBean.setBidExpanded(
                !auctionMonitorItemBean.isBidExpanded());
        // hide the all other auction items as the bid inputs cause some
        // validation issues.
        for (AuctionMonitorItemBean item :searchItemBeans){
            if (item.isBidExpanded() &&
                    !item.equals(auctionMonitorItemBean)){
                item.setBidExpanded(false);
            }
        }
        return SUCCESS;
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
            if (log.isWarnEnabled()) {
                log.warn("Failed to retrieve a query item based on ID " +
                         queryItemID + " because of " + e);
            }
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
