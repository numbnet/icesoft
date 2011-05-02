package com.icesoft.icefaces.samples.taxi;

import com.icesoft.faces.async.render.RenderManager;
import com.icesoft.faces.async.render.Renderable;
import com.icesoft.faces.component.ext.RowSelectorEvent;
import com.icesoft.faces.webapp.xmlhttp.FatalRenderingException;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import com.icesoft.faces.webapp.xmlhttp.RenderingException;
import com.icesoft.faces.webapp.xmlhttp.TransientRenderingException;
import com.icesoft.icefaces.samples.taxi.dialogs.DialogManager;
import com.icesoft.icefaces.samples.taxi.dialogs.OptionPane;
import com.icesoft.icefaces.samples.taxi.util.Command;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.util.ArrayList;
import java.util.Map;

/**
 * <p>The Taxi Mobile example is mediated by this class.  All JSF commands will
 * pass through this class and the necessary supporting classes will be
 * called.  The mediator should be in session scope.</p>
 */
public class Mediator implements Renderable {

    private static Log log = LogFactory.getLog(Mediator.class);

    // general user object, either client or driver
    private User user;

    // current request, if for a particular user
    private TaxiRequestWrapper currentRequest;

    // broker for all taxi requests
    private TaxiRequestController taxiRequestController;


    // Simulation Manager user for automated demo interaction
    private SimulationManager simulationManager;

    // manages dynamic includes and corresponding page titles
    private ScreensManager screensManager;

    // manage visibility and access to popop dialog properties
    private DialogManager dialogManager;

    // user factory, used for creating users at login
    private UserFactory userFactory;

    // local array of taxi request references.  This is only used so that
    // we can be sure the selected row index corresponds to our local list.
    private ArrayList<TaxiRequestWrapper> taxiRequests;


    // render manager for the application
    private RenderManager renderManager;
    private PersistentFacesState persistentFacesState;
    // render group name for driver, group update of taxi request list...
    public static final String TAXI_DRIVER_GROUP_RENDERER = "TAXI_DRIVER";
    
    private boolean taxiCab;


    public Mediator() {
        persistentFacesState = PersistentFacesState.getInstance();

        // setup the screen manager
        screensManager = new ScreensManager();
        screensManager.setCurrentContenScreen(ScreensManager.LOGIN_INCLUDE);

        // setup a user factory
        userFactory = new UserFactory();

        // setup dialog manager
        dialogManager = new DialogManager();

    }

    /**
     * Return the reference to the {@link com.icesoft.faces.webapp.xmlhttp.PersistentFacesState
     * PersistentFacesState} associated with this Renderable.
     * <p/>
     * The typical (and recommended usage) is to get and hold a reference to the
     * PersistentFacesState in the constructor of your managed bean and return
     * that reference from this method.
     *
     * @return the PersistentFacesState associated with this Renderable
     */
    public PersistentFacesState getState() {
        return persistentFacesState;
    }

    /**
     * Callback method that is called if any exception occurs during an attempt
     * to render this Renderable.
     * <p/>
     * It is up to the application developer to implement appropriate policy
     * when a RenderingException occurs.  Different policies might be
     * appropriate based on the severity of the exception.  For example, if the
     * exception is fatal (the session has expired), no further attempts should
     * be made to render this Renderable and the application may want to remove
     * the Renderable from some or all of the {@link com.icesoft.faces.async.render.GroupAsyncRenderer}s it
     * belongs to. If it is a transient exception (like a client's connection is
     * temporarily unavailable) then the application has the option of removing
     * the Renderable from GroupRenderers or leaving them and allowing another
     * render call to be attempted.
     *
     * @param renderingException The exception that occurred when attempting to
     *                           render this Renderable.
     */
    public void renderingException(RenderingException renderingException) {
        if (log.isInfoEnabled() &&
                renderingException instanceof TransientRenderingException) {
            log.info("Transient Rendering excpetion:", renderingException);
        } else if (renderingException instanceof FatalRenderingException) {
            if (log.isInfoEnabled()) {
                log.info("Fatal rendering exception: ", renderingException);
            }

            if (user instanceof TaxiDriverUser) {
                renderManager.getOnDemandRenderer(
                        TAXI_DRIVER_GROUP_RENDERER).remove(this);

            }
        }
    }

    /**
     * Request a taxi driver.  A dialog will be presented to confirm the
     * request.
     *
     * @param event jsf action event
     */
    public void requestTaxiDriverPickup(ActionEvent event) {
        // show popup requesting pickup, using command pattern
        OptionPane dialog = dialogManager.getPickupRequestDialog();

        // cancel request taxi command.
        final Command requestTaxiCancelCommand = new Command() {
            public void execute() {

                // broker the request to cancel the request.
                boolean success =
                        taxiRequestController.cancelTaxiDriverPickup(
                                currentRequest.getTaxiRequest());

                taxiRequestController.updateRelativeStatus(user, currentRequest);

                if (success) {
                    currentRequest = new TaxiRequestWrapper();
                    currentRequest.setTaxiRequest(new TaxiRequest());

                    // we need to notify taxi cab drivers that the request
                    // has been canceled.
//                    OptionPane cancelledDialog =
//                            dialogManager.getPickupRequestCanceledDialog();
//                    cancelledDialog.setRendered(true);
//                    dialogManager.setCurrentDialog(cancelledDialog);

                }
                renderManager.getOnDemandRenderer(
                        TAXI_DRIVER_GROUP_RENDERER).requestRender();

                log.info("Canceled request: " + success);

            }
        };

        final Command taxiArrivedCloseCommand = new Command() {
            public void execute() {

                // reset tax request
                currentRequest = new TaxiRequestWrapper();
                currentRequest.setTaxiRequest(new TaxiRequest());
            }
        };

        // build command for requesting Taxi Driver pickup
        Command requestTaxiCommand = new Command() {
            public void execute() {

                // broker the request out
                taxiRequestController.requestTaxiDriverPickup(
                        user,
                        currentRequest.getTaxiRequest());

                taxiRequestController.updateRelativeStatus(user, currentRequest);

                // show status dialog.
                OptionPane chainedDialog =
                        dialogManager.getPickupRequestStatusDialog();

                chainedDialog.setCancelButtonCommand(requestTaxiCancelCommand);
                chainedDialog.setNoButtonCommand(taxiArrivedCloseCommand);
                chainedDialog.setRendered(true);

                dialogManager.setCurrentDialog(chainedDialog);

                renderManager.getOnDemandRenderer(
                        TAXI_DRIVER_GROUP_RENDERER).requestRender();

            }
        };

        // assign command for later executions
        dialog.setYesButtonCommand(requestTaxiCommand);
        dialog.setRendered(true);

        dialogManager.setCurrentDialog(dialog);
    }

    /**
     * Cancel a taxi driver pickup. A dialog will be presented to confirm the
     * cancel request.
     *
     * @param event jsf action event
     */
    public void cancelTaxiDriverPickup(ActionEvent event) {


        // hide the cancel dialog, command pattern on cancel button call
        // the broker.  See requestTaxiDriver method.
        OptionPane chainedDialog =
                dialogManager.getPickupRequestStatusDialog();
        chainedDialog.setRendered(false);

        renderManager.getOnDemandRenderer(
                TAXI_DRIVER_GROUP_RENDERER).requestRender();

    }

    /**
     * Checks to see if the current Request is owned/locked by the current user
     * and if the status is pending we return true otherwise we return false.
     * <p/>
     * The method is used to show either the accept button or arrived/late buttons
     *
     * @return true if the taxi request has not accepted by the current user,
     *         false otherwise.
     */
    public boolean isAcceptableTaxiClientPickup() {
        // update relative status
        taxiRequestController.updateRelativeStatus(user, currentRequest);

        return currentRequest != null &&
                (user.equals(currentRequest.getTaxiRequest().getTaxiDriver()) &&
                        currentRequest.getTaxiRequest().getStatus() == TaxiRequest.STATUS_PENDING);
    }

    /**
     * Accept a client pick up request.  A dialog will be presented to confirm
     * the acceptance of the request
     *
     * @param event jsf action event
     */
    public void acceptTaxiClientPickup(ActionEvent event) {

        log.info("Accepting taxi client");

        // we accept responsibility for this record
        boolean result =
                taxiRequestController.acceptTaxiClientPickup(user,
                        currentRequest.getTaxiRequest(), 15);


        taxiRequestController.updateRelativeStatus(user, currentRequest);


        log.info("Accepting client..." + result);
        // todo: if false dialog to explain user has all ready canceled request

        renderManager.getOnDemandRenderer(
                TAXI_DRIVER_GROUP_RENDERER).requestRender();
    }

    /**
     * Indicates that a taxi has arrived at the given request location.
     *
     * @param event jsf action event.
     */
    public void arrived(ActionEvent event) {
        log.info("Arrived action event");

        taxiRequestController.arrived(currentRequest.getTaxiRequest());

        renderManager.getOnDemandRenderer(
                TAXI_DRIVER_GROUP_RENDERER).requestRender();

    }


    /**
     * Indicates that the taxi driver will be late.  The exact amount is set via
     * a f:param, lateArrival.  This value is then added to the eta.
     *
     * @param event jsf action event
     */
    public void subtractETATime(ActionEvent event) {

        int eta = 0 - getETAParam();

        taxiRequestController.etaUpdate(eta, currentRequest.getTaxiRequest());

        renderManager.getOnDemandRenderer(
                TAXI_DRIVER_GROUP_RENDERER).requestRender();
    }

    /**
     * Indicates that the end user will be late.  The exact amount is set via
     * a f:param, earlyArrival.  This value is then subtracted to the eta.
     *
     * @param event jsf action event
     */
    public void addETATime(ActionEvent event) {

        // get late value from f:param...
        int eta = getETAParam();

        taxiRequestController.etaUpdate(eta, currentRequest.getTaxiRequest());

        renderManager.getOnDemandRenderer(
                TAXI_DRIVER_GROUP_RENDERER).requestRender();
    }

    /**
     * Utility method for getting the eta param from the faces context.
     *
     * @return integer representing eta in minutes.
     */
    private int getETAParam() {
        // get late value from f:param...
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map params = facesContext.getExternalContext().getRequestParameterMap();
        String myParam = (String) params.get("etaParam");
        int eta = 5;

        try {
            eta = Integer.parseInt(myParam);
        } catch (NumberFormatException e) {
            log.debug("Error getting eta param: ", e);
        }

        return eta;
    }

    /**
     * A taxi driver makes this call to get a more detailed view o the taxi
     * request.
     *
     * @param event jsf action event
     */
    public void selectTaxiClientRequest(RowSelectorEvent event) {

        // We get the selected index of our local array list.  We then get
        // the selected object so that we can find it in the main list.  This
        // is needed to avoid concurrency issue.

        int selectedIndex = event.getRow();


        TaxiRequestWrapper currentRequest = taxiRequests.get(selectedIndex);

        log.info("selecting taxi client request " + selectedIndex + " " + currentRequest);

        // check if this currentRequest has been locked by another user
        if (taxiRequestController.lockTaxiRequest(user,
                currentRequest.getTaxiRequest())) {

            // set the current request
            this.currentRequest = currentRequest;

            // set the dynamic include to go to the detailed view of the request
            screensManager.setCurrentContenScreen(
                    ScreensManager.REQUEST_DETAILS_INCLUDE);

            // setup command button for notification of a canceled request.
            // build command for requesting Taxi Driver pickup
//            Command requestCanceledConfirmationCommand = new Command() {
//                public void execute() {
//
//                    // navigate to the request screen
//                    screensManager.setCurrentContenScreen(
//                        ScreensManager.REQUEST_VIEW_INCLUDE);
//
//                }
//            };
//            OptionPane cancelledDialog =
//                        dialogManager.getPickupRequestCanceledDialog();
//            // assign command for later executions
//            cancelledDialog.setOKButtonCommand(requestCanceledConfirmationCommand);

        }
        // else we don't do anything the list should be updated to reflect the
        // updated list.
        // popup indicating the record has already been requested.
        // todo: add popup for you can't select this. 
    }

    /**
     * Cancels the lock a taxi driver will have on a taxi request.  This occurs
     * if a taxi driver requests more detail on a request but dosen't choose
     * to accept it.
     *
     * @param event jsf action event.
     */
    public void unselectTaxiClientRequest(ActionEvent event) {

        // unlock the taxi request
        taxiRequestController.unLockTaxiRequest(currentRequest.getTaxiRequest());
        currentRequest = null;

        // navigate back to the requests view.
        screensManager.setCurrentContenScreen(
                ScreensManager.REQUEST_VIEW_INCLUDE);
    }


    /**
     * Sets up the a new user and start the application
     *
     * @param event jsf action event
     */
    public void login(ActionEvent event) {
        // get a user from factory
        user = userFactory.getSelectedUser();
        user.setLoggedIn(true);

        // based on the user type setup the next screen

        // if user is a cabbie
        if (user instanceof TaxiDriverUser) {
            // select the appropriate screen
            screensManager.setCurrentContenScreen(
                    ScreensManager.REQUEST_VIEW_INCLUDE);
        }
        // else is a client wanting a cabbie.
        else if (user instanceof TaxiClientUser) {
            // set the dynamic include
            screensManager.setCurrentContenScreen(
                    ScreensManager.CLIENT_INCLUDE);

            // create a new blank request to work off of
            currentRequest = new TaxiRequestWrapper();
            currentRequest.setTaxiRequest(new TaxiRequest());

        }

        // start the simulation,  should only start once.
        //simulationManager.startSimulation();
    }

    /**
     * Gets a list of the most appropriate taxi pickup request for a given user.
     *
     * @return list of taxi pickups for the given user.
     */
    public ArrayList<TaxiRequestWrapper> getTaxiPickupRequests() {

        // get a local reference of the taxi request so that we can insure
        // that the RowSelectorEvent selected row ID is the one we clicked on.

        taxiRequests = taxiRequestController.getTaxiList(user);

        return taxiRequests;
    }

    /**
     * Logs the user out of the system.
     *
     * @param event jsf action event
     */
    public void logout(ActionEvent event) {
        // navigate to login
        screensManager.setCurrentContenScreen(ScreensManager.LOGIN_INCLUDE);

        // reset user
        user = null;
        currentRequest = null;
    }

    /**
     * Gets the current User.
     *
     * @return
     */
    public User getUser() {
        return user;
    }

    /**
     * Gets the request currently associated with the user, if any.
     *
     * @return taxi request associated with user, null if none is associated.
     */
    public TaxiRequestWrapper getCurrentRequest() {
        return currentRequest;
    }

    /**
     * Gets the screen manager for this session.
     *
     * @return screen manager data, dynamic include and page title information.
     */
    public ScreensManager getScreensManager() {
        return screensManager;
    }

    /**
     * Sets the RenderManager, this method is called from the faces config, and
     * should not be called again.
     *
     * @param renderManager ICEfaces server push render manager.
     */
    public void setRenderManager(RenderManager renderManager) {
        this.renderManager = renderManager;
        this.renderManager.getOnDemandRenderer(
                TAXI_DRIVER_GROUP_RENDERER).add(this);
    }


    /**
     * Sets the simulation manager singleton and starts it.
     * @param simulationManager
     */
    public void setSimulationManager(SimulationManager simulationManager) {
        this.simulationManager = simulationManager;
    }

    /**
     * Gets the user Factory for synthetic login by anonymous users.
     *
     * @return factory instance for creating new users.
     */
    public UserFactory getUserFactory() {
        return userFactory;
    }

    /**
     * Gets the dialog manager which has direct access to application dialog
     * controls.
     *
     * @return dialog manager
     */
    public DialogManager getDialogManager() {
        return dialogManager;
    }

    /**
     * Sets the Taxi Controller which is a application scoped bean that is
     * responsible for handling the request transactions.
     *
     * @param taxiRequestController request taxi transaction controller.
     */
    public void setTaxiRequestController(TaxiRequestController taxiRequestController) {
        this.taxiRequestController = taxiRequestController;
    }

    public boolean isTaxiCab() {
        return taxiCab;
    }

    public void setTaxiCab(boolean taxiCab) {
        this.taxiCab = taxiCab;
        
        // get a user from factory
        if (taxiCab) {
            user = new TaxiDriverUser();
        } else {
            user = new TaxiClientUser();
        }
        user.setLoggedIn(true);

        // based on the user type setup the next screen

        // if user is a cabbie
        if (user instanceof TaxiDriverUser) {
            // select the appropriate screen
            screensManager.setCurrentContenScreen(
                    ScreensManager.REQUEST_VIEW_INCLUDE);
        }
        // else is a client wanting a cabbie.
        else if (user instanceof TaxiClientUser) {
            // set the dynamic include
            screensManager.setCurrentContenScreen(
                    ScreensManager.CLIENT_INCLUDE);

            // create a new blank request to work off of
            currentRequest = new TaxiRequestWrapper();
            currentRequest.setTaxiRequest(new TaxiRequest());

        }

        // start the simulation,  should only start once.
        //simulationManager.startSimulation();
    }
}
