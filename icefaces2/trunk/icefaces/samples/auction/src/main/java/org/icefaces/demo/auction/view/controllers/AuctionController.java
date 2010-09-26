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
package org.icefaces.demo.auction.view.controllers;

import org.icefaces.application.PushRenderer;
import org.icefaces.demo.auction.services.AuctionService;
import org.icefaces.demo.auction.services.beans.AuctionItem;
import org.icefaces.demo.auction.view.beans.AuctionBean;
import org.icefaces.demo.auction.view.beans.AuctionItemBean;
import org.icefaces.demo.auction.view.names.BeanNames;
import org.icefaces.demo.auction.view.names.ParameterNames;
import org.icefaces.demo.auction.view.util.FacesUtils;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * AuctionController is a stateless UI controller that handles JSF action
 * event generate from the UI.  The controller interacts with the service layer
 * and various model beans for the current request.
 *
 * @author ICEsoft Technologies Inc.
 * @since 2.0
 */
@ManagedBean
@ApplicationScoped
public class AuctionController {

    private static Logger logger = Logger.getLogger(AuctionController.class.getName());

    /**
     * Calls the service layer for new auction data and merges it with the current
     * state of the auctionBean.
     */
    public void refreshAuctionBean() {
        AuctionBean auctionBean = (AuctionBean)
                FacesUtils.getManagedBean(BeanNames.AUCTION_BEAN);
        refreshAuctionBean(auctionBean);

    }

    /**
     * Calls the service layer for new auction data and merges it with the current
     * state of the auctionBean.
     *
     * @param auctionBean model data to update/refresh auction item data.
     */
    public void refreshAuctionBean(AuctionBean auctionBean) {

        AuctionService auctionService = (AuctionService)
                FacesUtils.getManagedBean(BeanNames.AUCTION_SERVICE);

        // get latest data from the auction service.
        List<AuctionItem> auctionItems = auctionService.getAllAuctionItems(
                auctionBean.getSortColumn(),
                auctionBean.isAscending());

        // not data so clear our bean and return.
        if (auctionItems == null) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("Service layer contains no auction items. ");
            }
            auctionBean.setAuctionItems(null);
            return;
        }

        // apply the new data against the current view data.
        if (auctionBean.getAuctionItems() != null) {
            auctionBean.setAuctionItems(mergeAuctionData(auctionItems,
                    auctionBean.getAuctionItems()));
        }
        // no auction items means this is our first time making the refresh call.
        else {
            // wrap data
            List<AuctionItemBean> auctionItemBeans =
                    new ArrayList<AuctionItemBean>(auctionItems.size());
            for (AuctionItem auction : auctionItems) {
                auctionItemBeans.add(new AuctionItemBean(auction));
            }
            auctionBean.setAuctionItems(auctionItemBeans);
        }
    }

    /**
     * Utility method to do a bruit force merge of current view data with data
     * retrieved from the service layer.
     *
     * @param newAuctionItems
     * @param currentAuctionItems
     * @return merged list of AuctionItems beans.
     */
    private List<AuctionItemBean> mergeAuctionData(
            List<AuctionItem> newAuctionItems, List<AuctionItemBean> currentAuctionItems) {

        List<AuctionItemBean> currentAuctionItemBeans =
                new ArrayList<AuctionItemBean>(newAuctionItems.size());
        // bruit force merge/sync of the auctionItem data, respect the
        // new auctionItems order as it may have changed.
        for (AuctionItem auctionItem : newAuctionItems) {
            for (AuctionItemBean auctionItemBean : currentAuctionItems) {
                if (auctionItemBean.getAuctionItem().getId() ==
                        auctionItem.getId()) {
                    double oldPrice = auctionItemBean.getAuctionItem().getPrice();
                    double newPrice = auctionItem.getPrice();
                    auctionItemBean.setAuctionItem(auctionItem);
                    currentAuctionItemBeans.add(auctionItemBean);
                    if (newPrice > oldPrice) {
                        auctionItemBean.setNewBidPrice(true);
                    } else {
                        auctionItemBean.setNewBidPrice(false);
                    }
                }
            }
        }
        currentAuctionItems.clear();
        return currentAuctionItemBeans;
    }

    /**
     * Place a bid on the current auction item input bid field.  It is necessary
     * to get the actionItem that represent this row so that we only apply
     * the update to the current row, we don't want to update any other row
     * mistakenly with the bid action form submit.
     *
     * @param event JSF action event.
     */
    public void placeBid(ActionEvent event) {
        // get the service.
        AuctionService auctionService = (AuctionService)
                FacesUtils.getManagedBean(BeanNames.AUCTION_SERVICE);

        // get the auction item that initiated the bid.
        AuctionItemBean auctionItemBean = (AuctionItemBean)
                FacesUtils.getRequestMapValue(ParameterNames.AUCTION_ITEM);

        // bid value will already be validate by JSF so go ahead and apply the
        // new bid
        boolean success = auctionService.bidOnAuctionItem(
                auctionItemBean.getAuctionItem(),
                auctionItemBean.getBid());

        // someone beat us to it so show the error
        if (!success) {
            FacesMessage message = new FacesMessage();
            message.setDetail("Someone beat you to it, rebid");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }

        // finally get the auctionBean model and update it with the new service
        // layer bid information.
        AuctionBean auctionBean = (AuctionBean)
                FacesUtils.getManagedBean(BeanNames.AUCTION_BEAN);
        refreshAuctionBean(auctionBean);

        // do a push out to anyone that is viewing the auction.
        PushRenderer.render(AuctionBean.AUCTION_RENDER_GROUP);
    }


    /**
     * Toggles visibility state of the bid button for the auctionItem id specified
     * by the context parameter?....
     *
     * @param event JSF action event.
     */
    public void toggleBidInput(ActionEvent event) {

        AuctionItemBean auctionItemBean = (AuctionItemBean)
                FacesUtils.getRequestMapValue(ParameterNames.AUCTION_ITEM);
        auctionItemBean.setShowBidInput(!auctionItemBean.isShowBidInput());

        // hide the others.
        AuctionBean auctionBean = (AuctionBean)
                FacesUtils.getManagedBean(BeanNames.AUCTION_BEAN);
        List<AuctionItemBean> auctionItems = auctionBean.getAuctionItems();
        for (AuctionItemBean auctionItem : auctionItems) {
            if (!auctionItem.equals(auctionItemBean)) {
                auctionItem.setShowBidInput(false);
            }
        }

        // assign view scoped bid with the current bid value.
        auctionItemBean.setBid(auctionItemBean.getAuctionItem().getPrice());
    }

    /**
     * Toggles visibility state of the extended auction description for the
     * auctionItem id specified by the context parameter?....
     *
     * @param event JSF action event.
     */
    public void toggleExtendedDescription(ActionEvent event) {
        AuctionItemBean auctionItemBean = (AuctionItemBean)
                FacesUtils.getRequestMapValue(ParameterNames.AUCTION_ITEM);
        auctionItemBean.setShowExtendedDescription(
                !auctionItemBean.isShowExtendedDescription());
    }
}
