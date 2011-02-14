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

package org.icefaces.component.fileentry;

import org.icefaces.component.utils.Utils;
import org.icefaces.render.MandatoryResourceComponent;

import javax.faces.render.Renderer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import java.io.IOException;

//@MandatoryResourceComponent("org.icefaces.component.fileentry.FileEntry")
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
        writer.writeAttribute("id", clientId, "clientId");
        boolean disabled = fileEntry.isDisabled();
        Utils.writeConcatenatedStyleClasses(writer, "ice-file-entry",
            fileEntry.getStyleClass());
		writer.writeAttribute("style", fileEntry.getStyle(), "style");
        
        writer.startElement("input", uiComponent);
        writer.writeAttribute("type", "file", "type");
        writer.writeAttribute("id", config.getIdentifier(), "clientId");
        writer.writeAttribute("name", config.getIdentifier(), "clientId");
        //writer.writeAttribute("multiple", "multiple", "multiple");
        if (disabled) {
            writer.writeAttribute("disabled", "true", "disabled");
        }
        writer.writeAttribute("tabindex", fileEntry.getTabindex(), "tabindex");
        writer.endElement("input");

        writer.startElement("div", uiComponent);
        writer.writeAttribute("class", "inactive", null);
        writer.startElement("div", uiComponent);
        writer.writeAttribute("class", "progress-bar", null);
        writer.endElement("div");
        writer.endElement("div");


        writer.endElement("div");
    }
    
    @Override
    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        FileEntry fileEntry = (FileEntry) uiComponent;
        String clientId = uiComponent.getClientId(facesContext);
//System.out.println("FileEntryRenderer.decode  clientId: " + clientId);
        FileEntryResults results = FileEntry.retrieveResultsFromEarlierInLifecycle(facesContext, clientId);
        // If no new files have been uploaded, leave the old upload results in-place.
        if (results != null) {
//System.out.println("FileEntryRenderer.decode    results: " + results);
            fileEntry.setResults(results);
//System.out.println("FileEntryRenderer.decode      results ve: " + uiComponent.getValueExpression("results"));
        }

        boolean filesUploadedThisLifecycle = (results != null);
        FileEntryEvent event = new FileEntryEvent(
                fileEntry, filesUploadedThisLifecycle);
        fileEntry.queueEvent(event);

        fileEntry.validateResults(facesContext, results);
    }
}
