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
package org.icefaces.demo.auction.services.impl;

import org.icefaces.demo.auction.services.AuctionService;
import org.icefaces.demo.auction.services.Comparator.*;
import org.icefaces.demo.auction.services.beans.AuctionItem;
import org.icefaces.demo.auction.view.beans.AuctionBean;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Simple in memory representation of the auction items.
 */
@ManagedBean(eager = true)
@ApplicationScoped
public class AuctionServiceImpl implements AuctionService {

    private ArrayList<AuctionItem> auctionItems;

    /**
     * This initialization procedure is just for this basic service impl.  IN
     * The real work the data was be fetched from data store.
     */
    @PostConstruct
    public void initializeData() {
        // build  simple list of 4 auction items.
        auctionItems = new ArrayList<AuctionItem>(4);
        // ICE breaker
        auctionItems.add(new AuctionItem(1,
                "ICEsoft Ice Breaker",
                "Used icebreaker with very few dents, comes with manual.",
                "Calgary, Alberta Canada",
                "ICEsoft Technologies, Inc.",
                new GregorianCalendar(),
                5.0, 0, "icebreaker.jpg"));
//        auctionItems.get(0).getExpiryDate().add(GregorianCalendar.DAY_OF_YEAR, 1);
//        auctionItems.get(0).getExpiryDate().add(GregorianCalendar.HOUR_OF_DAY, 24);
        auctionItems.get(0).getExpiryDate().add(GregorianCalendar.SECOND, 60);
        // ICE Skate
        auctionItems.add(new AuctionItem(2,
                "ICEsoft Ice Skate",
                "A single sharpened ice skate, size 7.",
                "Calgary, Alberta Canada",
                "ICEsoft Technologies, Inc.",
                new GregorianCalendar(),
                100.0, 0, "iceskate.jpg"));
//        auctionItems.get(1).getExpiryDate().add(GregorianCalendar.DAY_OF_YEAR, 5);
        auctionItems.get(1).getExpiryDate().add(GregorianCalendar.HOUR_OF_DAY, 5);
        auctionItems.get(1).getExpiryDate().add(GregorianCalendar.SECOND, 15);
        // ICE Car
        auctionItems.add(new AuctionItem(3,
                "ICEsoft Ice Car",
                "Beautiful ice car with metal car filling.",
                "Calgary, Alberta Canada",
                "ICEsoft Technologies, Inc.",
                new GregorianCalendar(),
                10.0, 0, "icecar.jpg"));
        auctionItems.get(2).getExpiryDate().add(GregorianCalendar.DAY_OF_YEAR, 3);
        auctionItems.get(2).getExpiryDate().add(GregorianCalendar.HOUR_OF_DAY, 5);
        auctionItems.get(2).getExpiryDate().add(GregorianCalendar.SECOND, 10);
        // Ice Sailor
        auctionItems.add(new AuctionItem(4,
                "ICEsoft Ice Sailor",
                "Put him on the ice and watch him go!  Requires food and water.",
                "Calgary, Alberta Canada",
                "ICEsoft Technologies, Inc.",
                new GregorianCalendar(),
                10000.0, 0, "icesailor.jpg"));
        auctionItems.get(3).getExpiryDate().add(GregorianCalendar.DAY_OF_YEAR, 7);
        auctionItems.get(3).getExpiryDate().add(GregorianCalendar.HOUR_OF_DAY, 2);
        auctionItems.get(3).getExpiryDate().add(GregorianCalendar.SECOND, 19);
    }

    /**
     * This implementation of AuctionServiceImpl doesn't use caching. The
     * principle idea is that we only do one DB call per push, not for every
     * call to getAllAuctionItems();   This method would be used to effectively
     * clear the persistence layer cache so new data could be fetched from the
     * data store.
     */
    @Override
    public void resetAuctionItemCache() {
        initializeData();
    }

    /**
     * Attempts to make a pid for a specific auctionItem.  If the auctionItem
     * being bid on has a price that doesn't match the proposed bid then we likely
     * got beat out by some competing bidder.
     *
     * @param auctionItem item being bid on.
     * @param bid         new bid.
     * @return true if the bid was successfully, false if the bid could not be
     *         make, likely do to a competing bidder.
     */
    @Override
    public synchronized boolean bidOnAuctionItem(AuctionItem auctionItem, double bid) {
        // check to see if bid is valid, did someone else bid before use?
        for (AuctionItem item : auctionItems) {
            if (item.getId() == auctionItem.getId()) {
                // apply the bid if things match up more or less
                if (item.getPrice() == auctionItem.getPrice()) {
                    item.setPrice(bid);
                    item.setBids(item.getBids() + 1);
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Gets all the auction items for the current auction sorted on the
     * specified column and order.
     *
     * @param sortColumn  name of column to sort data by.
     * @param isAscending true to sort data ascending, false specifies descending.
     * @return list of auction items in list. Can be an empty or null list.
     */
    @Override
    public List<AuctionItem> getAllAuctionItems(String sortColumn, boolean isAscending) {

        // always return a copy of the core object to insure that the data
        // refresh via push is working as expected.
        ArrayList<AuctionItem> currentAuctionItems =
                new ArrayList<AuctionItem>(auctionItems.size());
        for (AuctionItem auctionItem : auctionItems) {
            currentAuctionItems.add(new AuctionItem(auctionItem));
        }

        // apply single column sorting. 
        if (AuctionBean.ITEM_NAME_COLUMN.equals(sortColumn)) {
            AbstractItemComparator itemNameComparator =
                    new ItemNameComparator(isAscending);
            Collections.sort(currentAuctionItems, itemNameComparator);
        } else if (AuctionBean.PRICE_COLUMN.equals(sortColumn)) {
            AbstractItemComparator itemNameComparator =
                    new ItemPriceComparator(isAscending);
            Collections.sort(currentAuctionItems, itemNameComparator);
        } else if (AuctionBean.BIDS_COLUMN.equals(sortColumn)) {
            AbstractItemComparator itemBidComparator =
                    new ItemBidsComparator(isAscending);
            Collections.sort(currentAuctionItems, itemBidComparator);
        } else if (AuctionBean.TIME_LEFT_COLUMN.equals(sortColumn)) {
            AbstractItemComparator itemExpiresComparator =
                    new ItemExpiresComparator(isAscending);
            Collections.sort(currentAuctionItems, itemExpiresComparator);
        }

        return currentAuctionItems;
    }
}
