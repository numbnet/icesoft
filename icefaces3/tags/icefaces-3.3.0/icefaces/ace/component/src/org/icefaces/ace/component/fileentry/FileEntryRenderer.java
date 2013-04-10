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

package org.icefaces.ace.component.fileentry;

import org.icefaces.ace.util.Utils;
import org.icefaces.render.MandatoryResourceComponent;
import org.icefaces.util.EnvUtils;

import javax.faces.FacesException;
import javax.faces.render.Renderer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import java.io.IOException;
import java.util.logging.Logger;

@MandatoryResourceComponent(tagName="fileEntry", value="org.icefaces.ace.component.fileentry.FileEntry")
public class FileEntryRenderer extends Renderer {
    private static Logger log = Logger.getLogger(FileEntry.class.getName());
    
    @Override
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        UIComponent form = Utils.findParentForm(uiComponent);
        if (form == null) {
            throw new FacesException("FileEntry component must be contained in a form.");
        }

        FileEntry fileEntry = (FileEntry) uiComponent;
        String clientId = uiComponent.getClientId(facesContext);
        log.finer("FileEntryRenderer.encode  clientId: " + clientId);
        
        FileEntryConfig config = fileEntry.storeConfigForNextLifecycle(facesContext, clientId);
        
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement("div", uiComponent);
        writer.writeAttribute("id", clientId, "clientId");
        boolean disabled = fileEntry.isDisabled();
        /* Ideally we'd add these styles, but Firefox makes the Browse button flow outside
         * of the widget bordering.
         * ui-widget ui-widget-content ui-corner-all
         */
        Utils.writeConcatenatedStyleClasses(writer, "ice-file-entry",
            fileEntry.getStyleClass());
		writer.writeAttribute("style", fileEntry.getStyle(), "style");

        writer.startElement("div", uiComponent);
        writer.startElement("input", uiComponent);
        writer.writeAttribute("type", "file", "type");
        writer.writeAttribute("id", config.getIdentifier(), "clientId");
        writer.writeAttribute("name", config.getIdentifier(), "clientId");
        //writer.writeAttribute("multiple", "multiple", "multiple");
        if (disabled) {
            writer.writeAttribute("disabled", "true", "disabled");
        }
        boolean ariaEnabled = EnvUtils.isAriaEnabled(facesContext);
        Integer tabindex = fileEntry.getTabindex();
        if (ariaEnabled && tabindex == null) tabindex = 0;
        if (tabindex != null) {
            writer.writeAttribute("tabindex", tabindex, "tabindex");
        }
        int size = fileEntry.getSize();
        if (size > 0) {
            writer.writeAttribute("size", size, "size");
        }
        writer.endElement("input");
        writer.endElement("div");

        writer.startElement("div", uiComponent);
        writer.writeAttribute("class", "inactive", null);
		writer.startElement("div", uiComponent);
		writer.writeAttribute("class", "ui-progressbar ui-widget ui-widget-content ui-corner-all", null);
        writer.startElement("div", uiComponent);
        writer.writeAttribute("class", "ui-progressbar-value ui-widget-header ui-corner-left ui-corner-right", null);
		writer.endElement("div");
        writer.endElement("div");
        writer.endElement("div");


        writer.endElement("div");
    }
    
    @Override
    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        FileEntry fileEntry = (FileEntry) uiComponent;
        String clientId = uiComponent.getClientId(facesContext);
        log.finer("FileEntryRenderer.decode  clientId: " + clientId);
        FileEntryResults results = FileEntry.retrieveResultsFromEarlierInLifecycle(facesContext, clientId);
        // If no new files have been uploaded, leave the old upload results in-place.
        if (results != null || fileEntry.isRequired()) {
            log.finer(
                "FileEntryRenderer.decode\n" +
                "  results ve: " + uiComponent.getValueExpression("results") + "\n" +
                "  results   : " + results);
            fileEntry.setResults(results);
        }

        boolean filesUploadedThisLifecycle = (results != null);
        FileEntryEvent event = new FileEntryEvent(
                fileEntry, filesUploadedThisLifecycle);
        fileEntry.queueEvent(event);
    }
}
