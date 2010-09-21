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

import org.icefaces.impl.event.FormSubmit;

import javax.faces.event.SystemEventListener;
import javax.faces.event.SystemEvent;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UINamingContainer;
import java.util.Iterator;
import java.io.IOException;

public class InputFilesFormSubmit implements SystemEventListener {
    public void processEvent(SystemEvent event) throws AbortProcessingException {
//System.out.println("InputFilesFormSubmit.processEvent()  event: " + event);
        FacesContext context = FacesContext.getCurrentInstance();

        //using PostAddToViewEvent ensures that the component resource is added to the view only once
        final HtmlForm form = (HtmlForm) ((PostAddToViewEvent) event).getComponent();
        if (form.getAttributes().get(FormSubmit.DISABLE_CAPTURE_SUBMIT) != null) {
//System.out.println("InputFilesFormSubmit  DISABLE_CAPTURE_SUBMIT  " + form.getClientId(context));
            return;
        }

        // See if there is at least one InputFiles component in the form,
        // which should alter the form submission method.
        if (!foundInputFiles(form)) {
//System.out.println("InputFilesFormSubmit  !foundInputFiles");
            return;
        }
        
        form.getAttributes().put(FormSubmit.DISABLE_CAPTURE_SUBMIT, "true");
        
        final String iframeId = "hiddenIframe";
        UIOutput output = new UIOutput() {
            public void encodeBegin(FacesContext context) throws IOException {
                String clientId = getClientId(context);
                ResponseWriter writer = context.getResponseWriter();
                writer.startElement("iframe", this);
                writer.writeAttribute("id", clientId, "clientId");
                writer.writeAttribute("name", clientId, "clientId");
                writer.writeAttribute("style", "display:none;", "style");
                writer.writeAttribute("src", "about:blank", "src");
                writer.endElement("iframe");
            }
            public void encodeEnd(FacesContext context) throws IOException {
            }
        };
        output.setId(iframeId);
        output.setTransient(true);
        form.getChildren().add(output);
        
        String iframeClientIdSuffix = UINamingContainer.getSeparatorChar(context) + iframeId;
        FormScriptWriter scriptWriter = new FormScriptWriter(
            "ice_inputFiles'.captureFormOnsubmit'(''{0}'', ''{0}" + iframeClientIdSuffix + "''')';",
            null,
            "_captureFileOnsubmit");
        form.getChildren().add(0, scriptWriter);        
    }
    
    private static boolean foundInputFiles(UIComponent parent) {
        Iterator<UIComponent> kids = parent.getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent kid = kids.next();
            if (kid instanceof InputFiles) {
                return true;
            }
            if (foundInputFiles(kid)) {
                return true;
            }
        }
        return false;
    }

    public boolean isListenerForSource(Object source) {
        return source instanceof HtmlForm;
    }
}
