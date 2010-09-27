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

package org.icefaces.component.inputFiles;

import javax.faces.render.Renderer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import javax.faces.application.FacesMessage;
import javax.faces.event.PhaseId;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.text.MessageFormat;

public class InputFilesRenderer extends Renderer {
    @Override
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        InputFiles inputFiles = (InputFiles) uiComponent;
        String clientId = uiComponent.getClientId(facesContext);
//System.out.println("InputFilesRenderer.encode  clientId: " + clientId);
        
        InputFilesConfig config = inputFiles.storeConfigForNextLifecycle(facesContext, clientId);
        
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement("input", uiComponent);
        writer.writeAttribute("type", "file", "type");
        writer.writeAttribute("id", config.getIdentifier(), "clientId");
        writer.writeAttribute("name", config.getIdentifier(), "clientId");
		writer.writeAttribute("class", "iceInpFile", "class");
        writer.endElement("input");
    }
    
    @Override
    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        String clientId = uiComponent.getClientId(facesContext);
//System.out.println("InputFilesRenderer.decode  clientId: " + clientId);
        InputFilesInfo info = InputFiles.retrieveInfoFromEarlierInLifecycle(facesContext, clientId);
        // If no new files have been uploaded, leave the old upload info in-place.
        if (info != null) {
//System.out.println("InputFilesRenderer.decode    info: " + info);
            InputFiles inputFiles = (InputFiles) uiComponent;
            inputFiles.setInfo(info);
            
            InputFilesEvent event = new InputFilesEvent(inputFiles);
            inputFiles.queueEvent(event);
            
            // ICE-5750 deals with re-adding FacesMessages from previous
            // lifecycles, so this component only deals with initially adding,
            // not re-adding.
            inputFiles.addMessagesFromInfo(facesContext, clientId, info);
        }
    }
}
