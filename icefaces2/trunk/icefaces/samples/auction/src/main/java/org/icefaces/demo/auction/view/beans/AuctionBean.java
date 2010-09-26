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
package org.icefaces.demo.auction.view.beans;

import org.icefaces.application.PushRenderer;
import org.icefaces.demo.auction.view.controllers.AuctionController;
import org.icefaces.demo.auction.view.controllers.IntervalPushRenderer;
import org.icefaces.demo.auction.view.names.BeanNames;
import org.icefaces.demo.auction.view.util.FacesUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Extension wraps the dao object with state information stored in window
 * scope.  Every time there call to the service layer to get the current state
 * of the auction items we match up auctionItem state with the current
 * AuctionItemBean state.  If an AuctionItemBean can not be found with the same
 * AuctionItem data a new instance of AuctionItemBean must be inserted.  The
 * UI controller AuctionController takes care of this logic.
 *
 * @author ICEsoft Technologies Inc.
 * @since 2.0
 */
@ManagedBean
//@ViewScoped
@CustomScoped(value = "#{window}")
public class AuctionBean implements Serializable {

    private static Logger logger = Logger.getLogger(AuctionBean.class.getName());

    public static final String AUCTION_RENDER_GROUP = "auctionRenderGroup";

    // columns that can sorted on
    public static final String ITEM_NAME_COLUMN = "nameColumn";
    public static final String PRICE_COLUMN = "priceColumn";
    public static final String BIDS_COLUMN = "bidsColumn";
    public static final String TIME_LEFT_COLUMN = "timeLeftColumn";

    // default sort column
    private String sortColumn = TIME_LEFT_COLUMN;
    private boolean isDescending;

    // list of auction items to display on screen
    private List<AuctionItemBean> auctionItems;

    public AuctionBean() {
        System.out.println("Creating new actionBean");
    }

    /**
     * After the bean is created it calls service layer to refresh its
     * bit items.  The refresh will preserve the data view only updating
     * the auction val
     */
    @PostConstruct
    private void setInitialize() {

        // get instance of the service bean and assign
        AuctionController auctionController = (AuctionController)
                FacesUtils.getManagedBean(BeanNames.AUCTION_CONTROLLER);

        // use the controller to apply the uPdate logic.
        auctionController.refreshAuctionBean(this);

        // register with bid renderer, used for bid pushes
        PushRenderer.addCurrentSession(AUCTION_RENDER_GROUP);
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("added current session to renderer 'auction'");
        }
        // Interval render does pushes every x seconds, to refresh time remaining
        // time stamps and graphics. 
        PushRenderer.addCurrentSession(IntervalPushRenderer.INTERVAL_RENDER_GROUP);
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("added current session to renderer 'auction'");
        }

    }

    @PreDestroy
    private void destroy() {
        // remove from bid render group
        PushRenderer.removeCurrentView(AUCTION_RENDER_GROUP);
        PushRenderer.removeCurrentView(IntervalPushRenderer.INTERVAL_RENDER_GROUP);
    }


    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public boolean isAscending() {
        return isDescending;
    }

    public void setAscending(boolean descending) {
        isDescending = descending;
    }

    public List<AuctionItemBean> getAuctionItems() {
        return auctionItems;
    }

    public void setAuctionItems(List<AuctionItemBean> auctionItems) {
        this.auctionItems = auctionItems;
    }

    public String getItemNameColumn() {
        return ITEM_NAME_COLUMN;
    }

    public String getPriceColumn() {
        return PRICE_COLUMN;
    }

    public String getBidsColumn() {
        return BIDS_COLUMN;
    }

    public String getTimeLeftColumn() {
        return TIME_LEFT_COLUMN;
    }
}
