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
package org.icefaces.demo.auction.services.beans;

import java.util.GregorianCalendar;

/**
 * AuctionItem represents one auction item that is contained in an an auction.
 * An item stores descriptive information about the item being auctioned as
 * well as the bid price and number of bids made on the item.  The last very
 * important piece of information is when the auction will expire for this item.
 * <p/>
 * Most instance variables in the is class are immutable except for the bid
 * count and the bid price.
 *
 * @author ICEsoft Technologies Inc.
 * @since 2.0
 */
public class AuctionItem {

    private long id;
    private String name;
    private String description;
    private String sellerLocation;
    private String sellerName;
    private GregorianCalendar expiryDate;
    private double price;
    private int bids;
    private String imageName;

    public AuctionItem(long id, String name, String description,
                       String sellerLocation, String sellerName,
                       GregorianCalendar expiryDate, double currentPidPrice, int bids,
                       String imageName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.sellerLocation = sellerLocation;
        this.sellerName = sellerName;
        this.expiryDate = expiryDate;
        this.price = currentPidPrice;
        this.bids = bids;
        this.imageName = imageName;
    }

    public AuctionItem(AuctionItem auctionItem) {
        this.id = auctionItem.id;
        this.name = auctionItem.name;
        this.description = auctionItem.description;
        this.sellerLocation = auctionItem.sellerLocation;
        this.sellerName = auctionItem.sellerName;
        this.expiryDate = auctionItem.expiryDate;
        this.price = auctionItem.price;
        this.bids = auctionItem.bids;
        this.imageName = auctionItem.imageName;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setBids(int bids) {
        this.bids = bids;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSellerLocation() {
        return sellerLocation;
    }

    public String getSellerName() {
        return sellerName;
    }

    public GregorianCalendar getExpiryDate() {
        return expiryDate;
    }

    public double getPrice() {
        return price;
    }

    public int getBids() {
        return bids;
    }

    public String getImageName() {
        return imageName;
    }
}
