/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.icefaces.mobi.component.panelconfirmation;

import org.icefaces.mobi.utils.Attribute;


public class PanelConfirmation extends PanelConfirmationBase{
    public static final String BLACKOUT_PNL_HIDE_CLASS = "mobi-panelconf-bg-hide ";
    public static final String BLACKOUT_PNL_CLASS = "mobi-panelconf-bg ";
    public static final String CONTAINER_HIDE_CLASS = "mobi-panelconf-container-hide ";
    public static final String CONTAINER_CLASS = "mobi-panelconf-container ";
    public static final String TITLE_CLASS = "mobi-panelconf-title-container ";
    public static final String SELECT_CONT_CLASS = "mobi-panelconf-body-container ";
    public static final String BUTTON_CONT_CLASS = "mobi-panelconf-submit-container ui-btn-up-a ";
    public static final String BUTTON_ACCEPT_CLASS = "mobi-button mobi-button-attention ui-btn-up-a ";
    public static final String BUTTON_CANCEL_CLASS = "mobi-button mobi-button-important ui-btn-up-a ";

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
