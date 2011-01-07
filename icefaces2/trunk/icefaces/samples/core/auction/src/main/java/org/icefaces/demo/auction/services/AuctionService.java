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

package org.icefaces.demo.auction.services;


import org.icefaces.demo.auction.services.beans.AuctionItem;

import java.util.List;

/**
 * The Auction Service is responsible for all auction interactions.  This
 * service class can be implemented using a variety of different technologies
 * such as JPA, Spring, JAXB or JWSDP to name a few.  The main idea beind this
 * implementation is that we can sub in different system back ends and not
 * affect the UI code in any way.
 *
 * @author ICEsoft Technologies Inc.
 * @since 2.0
 */
public interface AuctionService {

    /**
     * Executes a bid transaction on the specified auctionItem for the specified
     * amount.
     *
     * @param auctionItem auction item being bid on.
     * @param bid         bid value to execute.
     * @return true if the bid was successful.  False, if another users beats us
     *         in making a equal or larger bid before us.
     */
    public boolean bidOnAuctionItem(AuctionItem auctionItem, double bid);

    /**
     * Get a snapshot of the auction items in the auction for a given moment
     * in time.  The method is responsible for sorting the data as well, we
     * generally don't won't the UI code to to this as most DB's can do this
     * a lot more effeciently.
     *
     * @param sortColumn  column being sorted, should be constant as defined in
     *                    {@link org.icefaces.demo.auction.view.beans.AuctionBean}
     * @param isAscending true indicates ascending sort, false indicates
     *                    descending sort.
     * @return list of auction items in auction, can be null.
     */
    public List<AuctionItem> getAllAuctionItems(String sortColumn,
                                                boolean isAscending);

    /**
     * Utility method that resets the auction item list.  This used for demo
     * purposes as the auction items may need to be reset if the bids become
     * too large or have expired.
     */
    public void resetAuctionItemCache();

}
