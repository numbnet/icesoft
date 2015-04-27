/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

package org.icefaces.mobi.component.contactlist;

import static org.icefaces.mobi.utils.HTML.BUTTON_ELEM;
import static org.icefaces.mobi.utils.HTML.DISABLED_ATTR;
import static org.icefaces.mobi.utils.HTML.ID_ATTR;
import static org.icefaces.mobi.utils.HTML.ONCLICK_ATTR;
import static org.icefaces.mobi.utils.HTML.SPAN_ELEM;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.icefaces.impl.application.AuxUploadResourceHandler;
import org.icefaces.impl.application.AuxUploadSetup;
import org.icefaces.mobi.renderkit.RenderUtils;
import org.icefaces.mobi.utils.MobiJSFUtils;
import org.icemobile.component.ContactDecoder;
import org.icemobile.util.BridgeItCommand;
import org.icemobile.util.CSSUtils;
import org.icemobile.util.ClientDescriptor;

public class ContactListRenderer extends Renderer {
    
 private static final Logger log = Logger.getLogger(ContactListRenderer.class.getName());
    
    @Override
    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        ContactList contactList = (ContactList) uiComponent;
        String clientId = contactList.getClientId();
        try {
            Map requestParameterMap = facesContext.getExternalContext()
                .getRequestParameterMap();
            String contactListResult = (String) requestParameterMap.get(clientId);
            if (null == contactListResult)  {
                Map auxMap = AuxUploadResourceHandler.getAuxRequestMap();
                contactListResult = (String) auxMap.get(clientId);
            }
            if (null != contactListResult)  {
                contactList.setValue(contactListResult);
                ContactDecoder modelHelper = new ContactDecoder(contactListResult);
                contactList.setName(modelHelper.getName());
                contactList.setEmail(modelHelper.getEmail());
                contactList.setPhone(modelHelper.getPhone());
            }
        } catch (Exception e) {
            log.log(Level.WARNING, "Error decoding fetchContacts request paramaters.", e);
        }
    }


    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {

        ContactList contactList = (ContactList) uiComponent;
        ClientDescriptor client = MobiJSFUtils.getClientDescriptor();
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = contactList.getClientId();
        
        writer.startElement(SPAN_ELEM, null);
        writer.writeAttribute(ID_ATTR, clientId + "_wpr", null);
        RenderUtils.writeStyle(uiComponent, writer);
        RenderUtils.writeStyleClassAndBase(uiComponent, writer, "mobi-wrapper");
        if( client.isBridgeItSupportedPlatform(BridgeItCommand.SCAN) ){
        
            RenderUtils.startButtonElem(uiComponent, writer);
            
            String script = "bridgeit.fetchContact('" + clientId + "', '', {postURL:'" + AuxUploadSetup.getInstance().getUploadURL() + "', "
                + "cookies:{'JSESSIONID':'" + MobiJSFUtils.getSessionIdCookie(facesContext) + "'}, "
                + "fields: '" +contactList.getFields() + "'});";
            writer.writeAttribute(ONCLICK_ATTR, script, null);
            
            RenderUtils.writeDisabled(uiComponent, writer);
            writer.writeAttribute("class",CSSUtils.STYLECLASS_BUTTON, null);
            RenderUtils.writeTabIndex(uiComponent, writer);
            
            writer.startElement(SPAN_ELEM, contactList);
            writer.writeText(contactList.getButtonLabel(), null);
            writer.endElement(SPAN_ELEM);
            
            writer.endElement(BUTTON_ELEM);
        }
        else{
            UIComponent fallbackFacet = uiComponent.getFacet("fallback");
            if( fallbackFacet != null ){
                fallbackFacet.encodeAll(facesContext);
            }
            else{
                RenderUtils.startButtonElem(uiComponent, writer);
                writer.writeAttribute(DISABLED_ATTR, DISABLED_ATTR, DISABLED_ATTR);
                writer.writeAttribute("class",CSSUtils.STYLECLASS_BUTTON, null);
                RenderUtils.writeTabIndex(uiComponent, writer);
                writer.startElement(SPAN_ELEM, uiComponent);
                writer.writeText(contactList.getButtonLabel(), null);
                writer.endElement(SPAN_ELEM);
                writer.endElement(BUTTON_ELEM);
            }
        }
        writer.endElement(SPAN_ELEM);
    }

}

