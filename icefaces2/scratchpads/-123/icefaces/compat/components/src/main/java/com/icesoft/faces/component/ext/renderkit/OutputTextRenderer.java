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

package com.icesoft.faces.component.ext.renderkit;

import com.icesoft.faces.context.effects.LocalEffectEncoder;
import com.icesoft.faces.component.ExtendedAttributeConstants;
import com.icesoft.faces.renderkit.dom_html_basic.PassThruAttributeWriter;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import java.io.IOException;

public class OutputTextRenderer extends com.icesoft.faces.renderkit.dom_html_basic.OutputTextRenderer {
    // LocalEffectEncoder takes ownership of any passthrough attributes
    private static final String[] jsEvents = LocalEffectEncoder.maskEvents(
        ExtendedAttributeConstants.getAttributes(
            ExtendedAttributeConstants.ICE_OUTPUTTEXT));
    private static final String[] passThruAttributes =
        ExtendedAttributeConstants.getAttributes(
            ExtendedAttributeConstants.ICE_OUTPUTTEXT,
            jsEvents);
    
    protected void renderHtmlAttributes(
        FacesContext facesContext, ResponseWriter writer, UIComponent uiComponent)
        throws IOException {
        PassThruAttributeWriter.renderHtmlAttributes(
            writer, uiComponent, passThruAttributes);
        LocalEffectEncoder.encode(
            facesContext, uiComponent, jsEvents, null, null, writer);
    }
}
