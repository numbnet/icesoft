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
package org.icefaces.application.showcase.view.bean.examples.common.inventory;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Simple interface to describe an inventory model. Not really need for this
 * example but it fun to play with generics.
 *
 * @since 1.7
 */
public interface InventoryInterface extends Serializable {

    /**
     * Gets a collection of items that makes up the inventory.
     *
     * @return collection of items contained in the inventory
     */
    public ArrayList getInventory();

    /**
     * Gets the total price of all the items that are contained in this
     * iventory.
     *
     * @return total price of all inventory items.
     */
    public double getInventoryPriceTotal();

    /**
     * Add the specified item to the inventory.
     *
     * @param inventory iventory to add to this instance
     */
    public void addInventoryItem(InventoryItem inventory);

    /**
     * Remove an item from this instance of inventory.
     *
     * @param inventory        inventory item to add
     * @param removeItemOnZero if true, item will be removed from the inventory
     *                         when quantity reach zero otherwise the item is left in the invenotry with
     *                         a quantity of zero.
     */
    public void removeInventoryItem(InventoryItem inventory, boolean removeItemOnZero);
}
