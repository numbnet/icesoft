package com.icesoft.icefaces.samples.taxi;

import javax.faces.event.ActionEvent;

/**
 * Taxi Request Wrapper is pretty self descriptive, we need to wrap
 * the application scoped TaxiRequest objects with session specific data.  This
 * is needed as TaxiRequest state color is dependent on a users point of view
 * <p/>
 * For example consider a TaxiRequest that is dispatched and associated with
 * one user, the status color should be green when viewed from this user.  Now
 * if another taxi driver is viewing the list the same dispatched event should
 * look yellow as he is not the associated user.
 * <p/>
 * We don't care about the other states...
 */
public class TaxiRequestWrapper {

    private TaxiRequest taxiRequest;

    private int relativeStatus;
    
    private boolean mapRendered= false;

    public TaxiRequest getTaxiRequest() {
        return taxiRequest;
    }

    public void setTaxiRequest(TaxiRequest taxiRequest) {
        this.taxiRequest = taxiRequest;
    }

    public int getRelativeStatus() {
        return relativeStatus;
    }

    public void setRelativeStatus(int relativeStatus) {
        this.relativeStatus = relativeStatus;
    }

    public void toggleMap(ActionEvent event){
    	mapRendered = !mapRendered;
    }
	public boolean isMapRendered() {
		return mapRendered;
	}

	public void setMapRendered(boolean mapRendered) {
		this.mapRendered = mapRendered;
	}
    
}
