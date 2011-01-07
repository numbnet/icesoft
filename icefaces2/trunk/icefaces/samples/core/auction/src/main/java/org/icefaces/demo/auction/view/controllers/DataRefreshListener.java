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
package org.icefaces.demo.auction.view.controllers;

import org.icefaces.demo.auction.view.names.BeanNames;
import org.icefaces.demo.auction.view.util.FacesUtils;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * This phase listener refreshes the auction data by calling the service layer.
 * It is important that the service layer has a caching layer implemented to insure
 * that a data refresh does not hit the DB or web service directly with each
 * call to refresh.
 *
 * @author ICEsoft Technologies Inc.
 * @since 2.0
 */
public class DataRefreshListener implements PhaseListener {

    public void beforePhase(PhaseEvent phaseEvent) {
        // get instance of controller for the current context.
        AuctionController auctionController = (AuctionController)
                FacesUtils.getManagedBean(BeanNames.AUCTION_CONTROLLER);

        // trigger a refresh, which will get latest data from service, we do
        // this for every request and thus it is important that the service
        // layer uses a caching layer that insures we don't hit the DB or
        // web service on every call. 
        auctionController.refreshAuctionBean();

    }

    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }

    public void afterPhase(PhaseEvent phaseEvent) {

    }
}
