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

package com.icesoft.faces.renderkit;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;

import com.icesoft.faces.renderkit.dom_html_basic.PassThruAttributeRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.PassThruAttributeWriter;

public class RendererUtil {

    public static String SUPPORTED_PASSTHRU_ATT = "supportedPassThru";
    public static void renderPassThruAttributes(ResponseWriter writer, 
            UIComponent uiComponent,
            String[] excludeArray) throws IOException{
        String[] attributes = (String[]) uiComponent.getAttributes().get(SUPPORTED_PASSTHRU_ATT);
        if (attributes == null) {
            PassThruAttributeWriter.renderAttributes(writer, uiComponent, excludeArray);
            return;
        }
        List excludeArrayList = Arrays.asList(excludeArray);
        for (int i=0; i < attributes.length; i++) {
            if (excludeArrayList.contains(attributes[i])) continue;
            Object value = null;
            if ((value = uiComponent.getAttributes().get(attributes[i])) != null &&
                    !PassThruAttributeRenderer.attributeValueIsSentinel(value)) {
                writer.writeAttribute(attributes[i], value, null);
            }
        }
        //Boolean attributes
        boolean result;
        for (int i=0; i < PassThruAttributeRenderer.booleanPassThruAttributeNames.length; i++) {
            if (excludeArrayList.
                    contains(PassThruAttributeRenderer.booleanPassThruAttributeNames[i])) continue;
            Object value = null;
            if ((value = uiComponent.getAttributes().
                  get(PassThruAttributeRenderer.booleanPassThruAttributeNames[i])) != null) {
                if (value instanceof Boolean) {
                    result = ((Boolean)value).booleanValue();
                } else {
                    if (!(value instanceof String)) {
                        value = value.toString();
                    }
                    result = (new Boolean ((String)value).booleanValue());
                }
                if (result) {
                    writer.writeAttribute(PassThruAttributeRenderer.booleanPassThruAttributeNames[i], 
                            PassThruAttributeRenderer.booleanPassThruAttributeNames[i], null);
                    result = false;
                }
            }
        }        
        
    } 
}
