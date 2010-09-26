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

import org.icefaces.demo.auction.view.names.BeanNames;
import org.icefaces.demo.auction.view.names.ParameterNames;
import org.icefaces.demo.auction.view.util.FacesUtils;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * This phase listener check to see if the life cycle was generate via
 * a push.  If so the model for each auction is updated with new data
 * from the auction cache.
 */
public class PushDataRefreshListener implements PhaseListener {

    @Override
    public void afterPhase(PhaseEvent phaseEvent) {

    }

    @Override
    public void beforePhase(PhaseEvent phaseEvent) {
        // check to see if this request is a push generated
        ExternalContext externalContext =
                FacesContext.getCurrentInstance().getExternalContext();
        boolean pushRequest = externalContext.getRequestParameterMap().get(
                ParameterNames.CLIENT_REQUEST) == null;

        // client users action driven requests will have "clientRequest"
        // request parameter, server pushes won't have the param
        if (pushRequest) {
            // get instance of controller
            AuctionController auctionController = (AuctionController)
                    FacesUtils.getManagedBean(BeanNames.AUCTION_CONTROLLER);

            // trigger a refresh, which will get latest data from service.
            auctionController.refreshAuctionBean();
        }

    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }
}
