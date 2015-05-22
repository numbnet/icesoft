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
import static org.icefaces.mobi.utils.HTML.SCRIPT_ELEM;
import static org.icefaces.mobi.utils.HTML.TYPE_ATTR;

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
        boolean supported = client.isBridgeItSupportedPlatform(BridgeItCommand.REGISTER);
        if( !supported && registerCloudPush.isHideWhenUnsupported()){
            return;
        }
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = registerCloudPush.getClientId();
        RenderUtils.startButtonElem(uiComponent, writer);
        RenderUtils.writeStyle(uiComponent, writer);
        RenderUtils.writeStyleClassAndBase(uiComponent, writer, CSSUtils.STYLECLASS_BUTTON);
        if( supported ){
            writer.writeAttribute(ONCLICK_ATTR, 
                    String.format("bridgeit.register('%s', '"+clientId+"_hide', {postURL:'%s', cookies:{'JSESSIONID':'%s'}});", 
                            clientId, AuxUploadSetup.getInstance().getUploadURL(), MobiJSFUtils.getSessionIdCookie(facesContext)), null);
            RenderUtils.writeDisabled(uiComponent, writer);
        }
        else{
            writer.writeAttribute(DISABLED_ATTR, DISABLED_ATTR, DISABLED_ATTR);
        }
        RenderUtils.writeTabIndex(uiComponent, writer);
        writer.startElement(SPAN_ELEM, registerCloudPush);
        writer.writeText(registerCloudPush.getButtonLabel(), null);
        writer.endElement(SPAN_ELEM);
		writer.startElement(SCRIPT_ELEM, registerCloudPush);
		writer.writeAttribute(TYPE_ATTR, "text/javascript", TYPE_ATTR);
		writer.writeText("window['"+clientId+"_hide'] = function() {if (bridgeit.isRegistered()) document.getElementById('"+clientId+"').style.display = 'none';};", null);
		writer.writeText("window['"+clientId+"_hide']();", null);
		writer.endElement(SCRIPT_ELEM);
        writer.endElement(BUTTON_ELEM);
        
    }

}
