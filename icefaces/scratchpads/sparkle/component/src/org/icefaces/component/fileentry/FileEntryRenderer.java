/*
 * Version: MPL 1.1
 *
 * "The contents of this file are subject to the Mozilla Public License
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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 */

package org.icefaces.component.fileentry;

import javax.faces.render.Renderer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import java.io.IOException;

public class FileEntryRenderer extends Renderer {
    @Override
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        FileEntry fileEntry = (FileEntry) uiComponent;
        String clientId = uiComponent.getClientId(facesContext);
//System.out.println("FileEntryRenderer.encode  clientId: " + clientId);
        
        FileEntryConfig config = fileEntry.storeConfigForNextLifecycle(facesContext, clientId);
        
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement("div", uiComponent);
		writer.writeAttribute("class", "ice-file-entry", "class");
        
        writer.startElement("input", uiComponent);
        writer.writeAttribute("type", "file", "type");
        writer.writeAttribute("id", config.getIdentifier(), "clientId");
        writer.writeAttribute("name", config.getIdentifier(), "clientId");
        //writer.writeAttribute("multiple", "multiple", "multiple");
        writer.endElement("input");
        
        writer.endElement("div");
        
        /*
        Resource res = new Resource() {
            public Map<String, String> getResponseHeaders() {
                return null;
            }

            public String getRequestPath() {
                return null;
            }

            public URL getURL() {
                return null;
            }

            public boolean userAgentNeedsUpdate(FacesContext facesContext) {
                return false;
            }

            public InputStream getInputStream() {
                return null;
            }
        };
        String resPath = ResourceRegistry.addSessionResource(res);
        */
    }
    
    @Override
    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        FileEntry fileEntry = (FileEntry) uiComponent;
        String clientId = uiComponent.getClientId(facesContext);
//System.out.println("FileEntryRenderer.decode  clientId: " + clientId);
        FileEntryInfo info = FileEntry.retrieveInfoFromEarlierInLifecycle(facesContext, clientId);
        // If no new files have been uploaded, leave the old upload info in-place.
        if (info != null) {
//System.out.println("FileEntryRenderer.decode    info: " + info);
            fileEntry.setInfo(info);
//System.out.println("FileEntryRenderer.decode      info ve: " + uiComponent.getValueExpression("info"));
            
            FileEntryEvent event = new FileEntryEvent(fileEntry);
            fileEntry.queueEvent(event);
        }
        
        // ICE-5750 deals with re-adding faces messages for components that
        // have no re-executed. Components that are executing should re-add
        // their faces messages themselves.
        if (info == null) {
            info = fileEntry.getInfo();
        }
        fileEntry.addMessagesFromInfo(facesContext, clientId, info);
    }
}
