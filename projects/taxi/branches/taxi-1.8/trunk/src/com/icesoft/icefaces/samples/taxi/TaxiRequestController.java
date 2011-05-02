package com.icesoft.icefaces.samples.taxi;

import java.util.ArrayList;

/**
 * <p>The Taxi Request controller is responsible for managing all Taxi Driver and
 * Taxi Client transactions.  This central location is intended to minimize
 * an concurrency issue.</p>
 * <p/>
 * <p><b>Note:</b>The taxi request should be in application scope</p>
 */
public class TaxiRequestController {

    private ArrayList<TaxiRequest> taxiRequests =
            new ArrayList<TaxiRequest>(15);


    public TaxiRequestController() {
        /*taxiRequests.add(
                new TaxiRequest("Patrick", "address 1", "10 and 16 NW",
                        "Calgary", "test 1", TaxiRequest.STATUS_COMPLETE, "5:00pm"));
        taxiRequests.add(
                new TaxiRequest("Yip", "address 2", "10 and 16 NW",
                        "Calgary", "test 2", TaxiRequest.STATUS_PENDING, "6:00pm"));
        taxiRequests.add(
                new TaxiRequest("Ken", "address 3", "10 and 16 NW",
                        "Calgary", "test 3", TaxiRequest.STATUS_DISPATCHED, "7:00pm"));
        taxiRequests.add(
                new TaxiRequest("Frank", "address 4", "10 and 16 NW",
                        "Calgary", "test 4", TaxiRequest.STATUS_PENDING, "8:00pm"));
        taxiRequests.add(
                new TaxiRequest("Carlo", "address 5", "10 and 16 NW",
                        "Calgary", "test 5", TaxiRequest.STATUS_COMPLETE, "9:00pm"));*/
    }

    /**
     * Requests for a taxi driver pickup.  This method binds taxiClient to the
     * taxiRequestthe also adds the taxiRequest object to the taxiRequest list.
     * Once in the list is can be selected by a taxi driver.
     *
     * @param taxiClient  taxi client making the request
     * @param taxiRequest taxi request object create by user.
     */
    public void requestTaxiDriverPickup(User taxiClient,
                                        TaxiRequest taxiRequest) {

        // append this user
        taxiRequest.setTaxiClient(taxiClient);

        // add the new list to the static list for taxi drivers to see
        taxiRequests.add(taxiRequest);

    }

    /**
     * Cancels the taxi driver pickup for a given request.  If the request
     * was successful then the method returns true otherwise it returns false.
     * The UI should only encounter a false cancellation status if the pending
     * state of the the request was changed before the server push was sent to
     * the end user.
     *
     * @param taxiRequest taxi request to be canceled.
     * @return true if taxiRequest was successfully canceled, otherwise; false.
     */
    public boolean cancelTaxiDriverPickup(TaxiRequest taxiRequest) {

        // find taxi Request
        if (taxiRequests.contains(taxiRequest)) {

            // make sure status hasen't change during request
            if (taxiRequest.getStatus() != TaxiRequest.STATUS_DISPATCHED) {
                taxiRequest.setStatus(TaxiRequest.STATUS_CANCELED);
                return true;
            }
        }

        return false;
    }

    /**
     * Taxi driver accepts a taxi client request.  This method binds a taxi
     * driver to the selected request.  If the request has been looked then
     * then false is returned by the function call and the driver should
     * be notified.  If the request assignment was successful then true is
     * returned.
     *
     * @param taxiDriver  driver wishing to make the pickup
     * @param taxiRequest request to make pickup on.
     * @param eta         estimated time of arrival in minutes
     * @return true if client pickup was successfully accepted, otherwise; false;
     */
    public synchronized boolean acceptTaxiClientPickup(User taxiDriver,
                                          TaxiRequest taxiRequest,
                                          int eta) {

        // add taxi driver reference
        if (taxiRequest.getStatus() != TaxiRequest.STATUS_CANCELED &&
                lockTaxiRequest(taxiDriver, taxiRequest)) {

            // update status
            taxiRequest.setStatus(TaxiRequest.STATUS_DISPATCHED);

            return true;

        }

        return false;
    }

    /**
     * Lock the record so that no other taxi can take it while being
     * vied by the current user.
     *
     * @param taxiDriver  taxi wanting to lock the record
     * @param taxiRequest request to lock
     * @return true if lock as successful, false if another user locked the request.
     */
    public synchronized boolean lockTaxiRequest(User taxiDriver, TaxiRequest taxiRequest) {

        if (taxiRequest.getStatus() == TaxiRequest.STATUS_DISPATCHED &&
                !taxiDriver.equals(taxiRequest.getTaxiDriver())) {
            return false;
        } else {
            taxiRequest.setSelected(true);
            taxiRequest.setTaxiDriver(taxiDriver);

            return true;
        }
    }

    /**
     * Unlock the record so that other taxi can look at the potential pickup.
     *
     * @param taxiRequest request to lock
     */
    public synchronized void unLockTaxiRequest(TaxiRequest taxiRequest) {

        if (taxiRequest.getStatus() != TaxiRequest.STATUS_DISPATCHED) {
            taxiRequest.setTaxiDriver(null);
        }
    }

    /**
     * Taxi driver has successfully picked-up a client.  This method is
     * responsible for updating the state of the taxi request.
     *
     * @param taxiRequest request to mark as picked up.
     */
    public void arrived(TaxiRequest taxiRequest) {
        taxiRequest.setStatus(TaxiRequest.STATUS_COMPLETE);
    }

    /**
     * Tax driver can update the eta of a request if he will be late or early.
     * The update will only date place if the eta is a positive number.
     *
     * @param eta         new eat time in minutes
     * @param taxiRequest request to make changes to.
     */
    public void etaUpdate(int eta, TaxiRequest taxiRequest) {

        int newETA = taxiRequest.getEta() + eta;
        if (newETA >= 0) {
            taxiRequest.setEta(newETA);
        }
    }

    /**
     * Gets the list of of Taxi Requests in an order which will make
     * the taxi driver life a little easier.  The list should be first sorted
     * by status and then by address.
     *
     * @param taxiDriver taxi driver requesting the list of requests.
     * @return smart ordered list of taxi requests.
     */
    public ArrayList<TaxiRequestWrapper> getTaxiList(User taxiDriver) {

        ArrayList<TaxiRequestWrapper> requests =
                new ArrayList<TaxiRequestWrapper>(taxiRequests.size());

        // send a copy of the requests so that index are correct. 
        TaxiRequestWrapper tmp;
        for (TaxiRequest request : taxiRequests) {
            if (request.getStatus() != TaxiRequest.STATUS_CANCELED &&
                    request.getStatus() != TaxiRequest.STATUS_COMPLETE) {
                // create wrapper
                tmp = new TaxiRequestWrapper();
                tmp.setTaxiRequest(request);

                // set relative if relevant
                if (request.getTaxiDriver() != null &&
                        taxiDriver != null) {

                    updateRelativeStatus(taxiDriver, tmp);

                }

                requests.add(tmp);
            }
        }

        return requests;
    }


    public void updateRelativeStatus(User taxiDriver, TaxiRequestWrapper taxiRequest) {

        TaxiRequest request = taxiRequest.getTaxiRequest();

        // found a relative ownership match
        if (taxiDriver.equals(request.getTaxiDriver()) &&
              taxiRequest.getTaxiRequest().getStatus() == TaxiRequest.STATUS_DISPATCHED) {
            taxiRequest.setRelativeStatus(TaxiRequest.STATUS_DISPATCHED);
        }
        // no user match so treat it as complete, in colors that is.
        else if (!taxiDriver.equals(request.getTaxiDriver()) &&
              taxiRequest.getTaxiRequest().getStatus() == TaxiRequest.STATUS_DISPATCHED) {
            taxiRequest.setRelativeStatus(TaxiRequest.STATUS_COMPLETE);
        }
        // otherwise we just set the status as it was. 
        else {
            taxiRequest.setRelativeStatus(taxiRequest.getTaxiRequest().getStatus());
        }
    }

}
