package org.icefaces.mobi.component.panelconfirmation;

import org.icefaces.mobi.utils.Attribute;


public class PanelConfirmation extends PanelConfirmationBase{
    public static final String BLACKOUT_PNL_HIDE_CLASS = "mobi-panelconf-bg-hide ";
    public static final String BLACKOUT_PNL_CLASS = "mobi-panelconf-bg ";
    public static final String CONTAINER_HIDE_CLASS = "mobi-panelconf-container-hide ";
    public static final String CONTAINER_CLASS = "mobi-panelconf-container ";
    public static final String TITLE_CLASS = "mobi-panelconf-title-container ";
    public static final String SELECT_CONT_CLASS = "mobi-panelconf-body-container ";
    public static final String BUTTON_CONT_CLASS = "mobi-panelconf-submit-container ";
    public static final String BUTTON_ACCEPT_CLASS = "mobi-button mobi-button-attention ";
    public static final String BUTTON_CANCEL_CLASS = "mobi-button mobi-button-important ";

    private Attribute[] commonAttributeNames = {
            new Attribute("style", null),
    };

    public PanelConfirmation() {
        super();
    }

    public Attribute[] getCommonAttributeNames() {
        return commonAttributeNames;
    }
}
