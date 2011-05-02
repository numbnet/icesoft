package com.icesoft.icefaces.samples.taxi.dialogs;

/**
 * The DialogManager is responsible for not all that much.  In a large application
 * it could manage the visibility of open dialogs.  But in this app its
 * just moves the getters and setters out of the mediator.
 */
public class DialogManager {

    private OptionPane pickupRequestDialog;

    private OptionPane pickupRequestStatusDialog;

    private OptionPane pickupRequestCanceledDialog;

    // we have the notion of a current dialog to avoid screen flicker when
    // moving from one dialog to another.
    private OptionPane currentDialog;


    public DialogManager() {
        pickupRequestDialog = new OptionPane("Pickup Request Confirmation",
                "/WEB-INF/inc-common/dialogs/pickup_request_dialog.jspx",
                OptionPane.NO_MESSAGE, OptionPane.YES_NO_OPTION);
        pickupRequestDialog.setCssStyle("clientRequestDialog");

        pickupRequestStatusDialog = new OptionPane("Pickup Request Status",
                "/WEB-INF/inc-common/dialogs/pickup_request_status_dialog.jspx",
                OptionPane.NO_MESSAGE, OptionPane.NO_OPTOINS);
        pickupRequestStatusDialog.setCssStyle("clientRequestDialog");

        pickupRequestCanceledDialog = new OptionPane("Pickup Request Canceled",
                "/WEB-INF/inc-common/dialogs/pickup_request_canceled_dialog.jspx",
                OptionPane.NO_MESSAGE, OptionPane.NO_OPTOINS);
        pickupRequestCanceledDialog.setCssStyle("clientRequestDialog");

        currentDialog = pickupRequestDialog;
    }


    public OptionPane getPickupRequestDialog() {
        return pickupRequestDialog;
    }


    public OptionPane getPickupRequestStatusDialog() {
        return pickupRequestStatusDialog;
    }


    public OptionPane getCurrentDialog() {
        return currentDialog;
    }

    public void setCurrentDialog(OptionPane currentDialog) {
        this.currentDialog = currentDialog;
    }


    public OptionPane getPickupRequestCanceledDialog() {
        return pickupRequestCanceledDialog;
    }

    public void setPickupRequestCanceledDialog(OptionPane pickupRequestCanceledDialog) {
        this.pickupRequestCanceledDialog = pickupRequestCanceledDialog;
    }
}
