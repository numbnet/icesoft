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
import org.icefaces.util.EnvUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.context.PartialViewContext;
import javax.faces.context.PartialViewContextWrapper;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseId;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class DOMPartialViewContext extends PartialViewContextWrapper {

    private static Logger log = Logger.getLogger("org.icefaces.context");

    private PartialViewContext wrapped;
    protected FacesContext facesContext;
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

        if (!EnvUtils.isICEfacesView(facesContext)) {
            return wrapped.getPartialResponseWriter();
        }

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

        if (!EnvUtils.isICEfacesView(facesContext)) {
            wrapped.processPartial(phaseId);
            return;
        }

        if (isRenderAll() && (phaseId == PhaseId.RENDER_RESPONSE)) {
            try {
                PartialResponseWriter partialWriter = getPartialResponseWriter();
                //TODO: understand why the original writer must be restored
                //it may be for error handling cases
                //facesContext.getAttributes().put(ORIGINAL_WRITER, orig);
                Writer outputWriter = facesContext.getExternalContext().getResponseOutputWriter();
                DOMResponseWriter writer = new DOMResponseWriter(outputWriter);
                facesContext.setResponseWriter(writer);

                ExternalContext exContext = facesContext.getExternalContext();
                exContext.setResponseContentType("text/xml");
                exContext.addResponseHeader("Cache-Control", "no-cache");

                Document oldDOM = writer.getOldDocument();
                applyBrowserChanges(exContext.getRequestParameterValuesMap(), oldDOM);
                UIViewRoot viewRoot = facesContext.getViewRoot();
                writer.startDocument();
                Iterator<UIComponent> itr = viewRoot.getChildren().iterator();
                while (itr.hasNext()) {
                    UIComponent kid = itr.next();
                    kid.encodeAll(facesContext);
                }
//                Document document = writer.getDocument();
                writer.endDocument();

                //the valid old document from the current pass is the new document
                Document newDOM = writer.getOldDocument();

                partialWriter.startDocument();
                Node[] diffs = new Node[0];
                if (oldDOM != null && newDOM != null) {
                    diffs = DOMUtils.domDiff(oldDOM, newDOM);
                } else {
                    // This shouldn't be the case. Typically it is a symptom that
                    // There is something else wrong so log it as a warning. 
                    if (oldDOM == null) {
                        log.warning("Old DOM is null during domDiff calculation");
                    } else {
                        log.warning("New DOM is null during domDiff calculation");
                    }
                }

                for (int i = 0; i < diffs.length; i++) {
                    Element element = (Element) diffs[i];
                    partialWriter.startUpdate(element.getAttribute("id"));
                    DOMUtils.printNode(element, outputWriter);
                    partialWriter.endUpdate();
                }

                renderState();
                renderExtensions();
                partialWriter.endDocument();
                return;
            } catch (IOException ex) {
                ex.printStackTrace();
                //should put back the original ResponseWriter
//                this.cleanupAfterView();
            } catch (RuntimeException ex) {
                ex.printStackTrace();
//                this.cleanupAfterView();
                // Throw the exception
                throw ex;
            }


        } else {
            super.processPartial(phaseId);
        }
    }

    private void applyBrowserChanges(Map parameters, Document document) {
        NodeList inputElements = document.getElementsByTagName("input");
        int inputElementsLength = inputElements.getLength();
        for (int i = 0; i < inputElementsLength; i++) {
            Element inputElement = (Element) inputElements.item(i);
            String id = inputElement.getAttribute("id");
            if (!"".equals(id)) {
                String name;
                if (parameters.containsKey(id)) {
                    String value = ((String[]) parameters.get(id))[0];
                    //empty string is implied (default) when 'value' attribute is missing
                    if ("".equals(value)) {
                        inputElement.setAttribute("value", "");
                    } else {
                        if (inputElement.hasAttribute("value")) {
                            inputElement.setAttribute("value", value);
                        } else if (inputElement.getAttribute("type").equals("checkbox")) {
                            inputElement.setAttribute("checked", "checked");
                        }
                    }
                } else if (!"".equals(name = inputElement.getAttribute("name")) && parameters.containsKey(name)) {
                    String type = inputElement.getAttribute("type");
                    if (type != null && (type.equals("checkbox") || type.equals("radio"))) {
                        String currValue = inputElement.getAttribute("value");
                        if (!"".equals(currValue)) {
                            boolean found = false;
                            // For multiple checkboxes, values can have length > 1,
                            // but for multiple radios, values would have at most length=1
                            String[] values = (String[]) parameters.get(name);
                            if (values != null) {
                                for (int v = 0; v < values.length; v++) {
                                    if (currValue.equals(values[v])) {
                                        found = true;
                                        break;
                                    }
                                }
                            }
                            if (found) {
                                // For some reason, our multiple checkbox
                                // components use checked="true", while
                                // our single checkbox components use
                                // checked="checked". The latter complying
                                // with the HTML specification.
                                // Also, radios use checked="checked"
                                if (type.equals("checkbox")) {
                                    inputElement.setAttribute("checked", "true");
                                } else if (type.equals("radio")) {
                                    inputElement.setAttribute("checked", "checked");
                                }
                            } else {
                                inputElement.removeAttribute("checked");
                            }
                        }
                    }
                } else {
                    if (inputElement.getAttribute("type").equals("checkbox")) {
                        inputElement.removeAttribute("checked");
                    }
                }
            }
        }

        NodeList textareaElements = document.getElementsByTagName("textarea");
        int textareaElementsLength = textareaElements.getLength();
        for (int i = 0; i < textareaElementsLength; i++) {
            Element textareaElement = (Element) textareaElements.item(i);
            String id = textareaElement.getAttribute("id");
            if (!"".equals(id) && parameters.containsKey(id)) {
                String value = ((String[]) parameters.get(id))[0];
                Node firstChild = textareaElement.getFirstChild();
                if (null != firstChild) {
                    //set value on the Text node
                    firstChild.setNodeValue(value);
                } else {
                    //DOM brought back from compression may have no
                    //child for empty TextArea
                    if (value != null && value.length() > 0) {
                        textareaElement.appendChild(document.createTextNode(value));
                    }
                }
            }
        }

        NodeList selectElements = document.getElementsByTagName("select");
        int selectElementsLength = selectElements.getLength();
        for (int i = 0; i < selectElementsLength; i++) {
            Element selectElement = (Element) selectElements.item(i);
            String id = selectElement.getAttribute("id");
            if (!"".equals(id) && parameters.containsKey(id)) {
                List values = Arrays.asList((String[]) parameters.get(id));

                NodeList optionElements =
                        selectElement.getElementsByTagName("option");
                int optionElementsLength = optionElements.getLength();
                for (int j = 0; j < optionElementsLength; j++) {
                    Element optionElement = (Element) optionElements.item(j);
                    if (values.contains(optionElement.getAttribute("value"))) {
                        optionElement.setAttribute("selected", "selected");
                    } else {
                        optionElement.removeAttribute("selected");
                    }
                }
            }
        }
    }

    private void renderState() throws IOException {
        // Get the view state and write it to the response..
        PartialResponseWriter writer = getPartialResponseWriter();
        writer.startUpdate(PartialResponseWriter.VIEW_STATE_MARKER);
        String state = facesContext.getApplication().getStateManager().getViewState(facesContext);
        writer.write(state);
        writer.endUpdate();

    }

    protected void renderExtensions() {
        //do nothing.
    }
}
