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

package org.icefaces.application.showcase.view.bean.examples.component.inputRichText;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.icefaces.application.showcase.view.bean.BaseBean;

/**
 * <p>The InputRichTextBean class stores the value of the inputRichText
 * component on the server.  When the save button is pressed on the RichText
 * component the backing bean value is updated.  The bean also contains the
 * two menubar states available; default and basic. </p>
 *
 * @since 1.7
 */
@ManagedBean(name = "inputRichTextBean")
@ViewScoped
public class InputRichTextBean extends BaseBean {

    public String getToolbarModeDefault() { return "Default"; }
    public String getToolbarModeBasic() { return "Basic"; }
    
    private String toolbarMode = getToolbarModeDefault();

    private String value = "";

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        // highlight backing bean display. 
        valueChangeEffect.setFired(false);
        this.value = value;
    }

    public String getToolbarMode() {
        return toolbarMode;
    }

    public void setToolbarMode(String toolbarMode) {
        this.toolbarMode = toolbarMode;
    }
}
