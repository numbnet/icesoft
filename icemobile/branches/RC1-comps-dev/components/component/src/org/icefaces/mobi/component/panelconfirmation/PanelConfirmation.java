package org.icefaces.mobi.component.panelconfirmation;

import org.icefaces.mobi.utils.Attribute;


public class PanelConfirmation extends PanelConfirmationBase{
    public static final String BLACKOUT_PNL_CLASS = ".mobi-date-bg-inv ";
    public static final String CONTAINER_CLASS = "mobi-date-container-hide ";
    public static final String TITLE_CLASS = "mobi-date-title-container ";
    public static final String SELECT_CONT_CLASS = "mobi-date-select-container ";
    public static final String BUTTON_CONT_CLASS = "mobi-date-submit-container ";
    public static final String VALUE_CONT_CLASS = "mobi-date-select-value-cont ";
    public static final String BUTTON_CLASS = "mobi-button mobi-button-default ";

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
