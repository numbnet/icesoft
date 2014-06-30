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
package org.icefaces.mobi.component.cloudpush;

import static org.icefaces.mobi.utils.HTML.BUTTON_ELEM;
import static org.icefaces.mobi.utils.HTML.DISABLED_ATTR;
import static org.icefaces.mobi.utils.HTML.ID_ATTR;
import static org.icefaces.mobi.utils.HTML.ONCLICK_ATTR;
import static org.icefaces.mobi.utils.HTML.SPAN_ELEM;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.icefaces.impl.application.AuxUploadSetup;
import org.icefaces.mobi.component.contactlist.ContactList;
import org.icefaces.mobi.renderkit.RenderUtils;
import org.icefaces.mobi.renderkit.ResponseWriterWrapper;
import org.icefaces.mobi.utils.MobiJSFUtils;
import org.icemobile.util.BridgeItCommand;
import org.icemobile.util.CSSUtils;
import org.icemobile.util.ClientDescriptor;

public class RegisterCloudPushRenderer extends Renderer{
    
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        //bridgeit.register();
        
        RegisterCloudPush registerCloudPush = (RegisterCloudPush) uiComponent;
        ClientDescriptor client = MobiJSFUtils.getClientDescriptor();
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = registerCloudPush.getClientId();
        
        writer.startElement(SPAN_ELEM, null);
        writer.writeAttribute(ID_ATTR, clientId + "_wpr", null);
        RenderUtils.writeStyle(uiComponent, writer);
        RenderUtils.writeStyleClassAndBase(uiComponent, writer, "mobi-wrapper");
        if( client.isBridgeItSupportedPlatform(BridgeItCommand.REGISTER) ){
        
            RenderUtils.startButtonElem(uiComponent, writer);
            
            writer.writeAttribute(ONCLICK_ATTR, "bridgeit.register('" + clientId + "');", null);
            
            RenderUtils.writeDisabled(uiComponent, writer);
            writer.writeAttribute("class",CSSUtils.STYLECLASS_BUTTON, null);
            RenderUtils.writeTabIndex(uiComponent, writer);
            
            writer.startElement(SPAN_ELEM, registerCloudPush);
            writer.writeText(registerCloudPush.getButtonLabel(), null);
            writer.endElement(SPAN_ELEM);
            
            writer.endElement(BUTTON_ELEM);
        }
        else{
            RenderUtils.startButtonElem(uiComponent, writer);
            writer.writeAttribute(DISABLED_ATTR, DISABLED_ATTR, DISABLED_ATTR);
            writer.writeAttribute("class",CSSUtils.STYLECLASS_BUTTON, null);
            RenderUtils.writeTabIndex(uiComponent, writer);
            writer.startElement(SPAN_ELEM, uiComponent);
            writer.writeText(registerCloudPush.getButtonLabel(), null);
            writer.endElement(SPAN_ELEM);
            writer.endElement(BUTTON_ELEM);
        }
        writer.endElement(SPAN_ELEM);
    }

}
