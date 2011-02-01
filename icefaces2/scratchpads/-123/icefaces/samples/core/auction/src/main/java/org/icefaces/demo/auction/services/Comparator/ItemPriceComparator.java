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

package org.icefaces.demo.auction.services.Comparator;

import org.icefaces.demo.auction.services.beans.AuctionItem;

/**
 * Sorts an AuctionItem using the field "price" in the order specified by the
 * constructor's ascending attribute.
 *
 * @author ICEsoft Technologies Inc.
 * @since 2.0
 */
public class ItemPriceComparator extends AbstractItemComparator {

    public ItemPriceComparator(boolean ascending) {
        super(ascending);
    }

    /**
     * Compares its two arguments for order.  Returns a negative integer,
     * zero, or a positive integer as the first argument is less than, equal
     * to, or greater than the second.<p>
     * <p/>
     *
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the
     *         first argument is less than, equal to, or greater than the
     *         second.
     */
    @Override
    public int compare(AuctionItem o1, AuctionItem o2) {
        if (isAscending) {
            return Double.valueOf(o1.getPrice()).compareTo(o2.getPrice());
        } else {
            return Double.valueOf(o2.getPrice()).compareTo(o1.getPrice());
        }
    }
}
