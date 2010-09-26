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
package org.icefaces.demo.auction.services;


import org.icefaces.demo.auction.services.beans.AuctionItem;

import java.util.List;

/**
 * The Auction Service is responsible for all auction interactions.  This
 * actions include basic auction actions such as add, remove and view action
 * items and placing bids.  There is also a utility portion that is designed to reset the
 * action items which is purely a maintenance option for demo purposes.
 *
 * @author ICEsoft Technologies Inc.
 * @since 2.0
 */
public interface AuctionService {

    public void resetAuctionItemCache();

    public boolean bidOnAuctionItem(AuctionItem auctionItem, double bid);

    public List<AuctionItem> getAllAuctionItems(String sortColumn,
                                                boolean isAscending);

}
