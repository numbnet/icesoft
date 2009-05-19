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
 * 2004-2009 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
*/

package org.icefaces.context;

import org.icefaces.util.DOMUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.*;
import javax.faces.event.PhaseId;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.logging.Logger;

public class DOMPartialViewContext extends PartialViewContextWrapper {

    private static Logger log = Logger.getLogger("org.icefaces.context");

    private PartialViewContext wrapped;
    private FacesContext facesContext;
    private PartialResponseWriter partialWriter;

    public DOMPartialViewContext(PartialViewContext partialViewContext, FacesContext facesContext) {
        this.wrapped = partialViewContext;
        this.facesContext = facesContext;
    }

    @Override
    public PartialViewContext getWrapped() {
        return wrapped;
    }

    @Override
    public void setPartialRequest(boolean isPartialRequest) {
        wrapped.setPartialRequest(isPartialRequest);
    }

    @Override
    public PartialResponseWriter getPartialResponseWriter() {
        if (null == partialWriter) {
            try {
                //TODO: ensure this can co-exist with other PartialViewContext implementations
                Writer outputWriter = facesContext.getExternalContext().getResponseOutputWriter();
                ResponseWriter basicWriter = new BasicResponseWriter(outputWriter, "text/html", "utf-8");
                partialWriter = new PartialResponseWriter(basicWriter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return partialWriter;
    }


    @Override
    public void processPartial(PhaseId phaseId) {
        if (isRenderAll() && (phaseId == PhaseId.RENDER_RESPONSE)) {
            try {
                PartialResponseWriter partialWriter = getPartialResponseWriter();
                ResponseWriter orig = facesContext.getResponseWriter();
                //TODO: understand why the original writer must be restored
                //it may be for error handling cases
                //facesContext.getAttributes().put(ORIGINAL_WRITER, orig);
                Writer outputWriter = FacesContext.getCurrentInstance().getExternalContext().getResponseOutputWriter();
                DOMResponseWriter writer = new DOMResponseWriter(outputWriter);
                facesContext.setResponseWriter(writer);

                ExternalContext exContext = facesContext.getExternalContext();
                exContext.setResponseContentType("text/xml");
                exContext.addResponseHeader("Cache-Control", "no-cache");

                Document oldDOM = writer.getOldDocument();

                UIViewRoot viewRoot = facesContext.getViewRoot();
                writer.startDocument();
                Iterator<UIComponent> itr = viewRoot.getChildren().iterator();
                while (itr.hasNext()) {
                    UIComponent kid = itr.next();
                    kid.encodeAll(facesContext);
                }
                Document document = writer.getDocument();
                writer.endDocument();

                //the valid old document from the current pass is the new document
                Document newDOM = writer.getOldDocument();

                Node[] diffs = DOMUtils.domDiff(oldDOM, newDOM);

                if (null != diffs) {
                    partialWriter.startDocument();
                    for (int i = 0; i < diffs.length; i++) {
                        Element element = (Element) diffs[i];
                        partialWriter.startUpdate(element.getAttribute("id"));
                        DOMUtils.printNode(element, outputWriter);
                        partialWriter.endUpdate();
                    }
                } else {
                    partialWriter.startDocument();
                    partialWriter.startUpdate(PartialResponseWriter.RENDER_ALL_MARKER);
                    DOMUtils.printNode(document, outputWriter);
                    partialWriter.endUpdate();
                }

                renderState(facesContext);
                partialWriter.endDocument();
                return;
            } catch (IOException ex) {
                //should put back the original ResponseWriter
//                this.cleanupAfterView();
            } catch (RuntimeException ex) {
//                this.cleanupAfterView();
                // Throw the exception
                throw ex;
            }


        } else {
            super.processPartial(phaseId);
        }
    }

    private void renderState(FacesContext context) throws IOException {

        // Get the view state and write it to the response..
        PartialResponseWriter writer = getPartialResponseWriter();
        writer.startUpdate(PartialResponseWriter.VIEW_STATE_MARKER);
        String state = context.getApplication().getStateManager().getViewState(context);
        writer.write(state);
        writer.endUpdate();

    }

}
