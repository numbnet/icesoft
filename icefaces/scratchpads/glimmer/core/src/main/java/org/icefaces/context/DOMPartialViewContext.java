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
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseId;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.EnumSet;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.icefaces.event.DetectNavigationPhaseListener;

public class DOMPartialViewContext extends PartialViewContextWrapper {

    private static Logger log = Logger.getLogger(DOMPartialViewContext.class.getName());

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

        if (phaseId == PhaseId.RENDER_RESPONSE) {
            try {
                PartialResponseWriter partialWriter = getPartialResponseWriter();
                //TODO: understand why the original writer must be restored
                //it may be for error handling cases
                //facesContext.getAttributes().put(ORIGINAL_WRITER, orig);
                Writer outputWriter = facesContext.getExternalContext().getResponseOutputWriter();
                DOMResponseWriter writer = createDOMResponseWriter(outputWriter);
                facesContext.setResponseWriter(writer);

                ExternalContext exContext = facesContext.getExternalContext();
                exContext.setResponseContentType("text/xml");
                exContext.addResponseHeader("Cache-Control", "no-cache");

                Document oldDOM = writer.getOldDocument();
                applyBrowserChanges(exContext.getRequestParameterValuesMap(), oldDOM);                
                
                UIViewRoot viewRoot = facesContext.getViewRoot();
                Node[] diffs = new Node[0];
                Document newDOM = null;
                writer.startDocument();

                if (isRenderAll())  {
                    Iterator<UIComponent> itr = viewRoot.getChildren().iterator();
                    while (itr.hasNext()) {
                        UIComponent kid = itr.next();
                        kid.encodeAll(facesContext);
                    }
                    writer.endDocument();
                    //the valid old document from the current pass is the new document
                    newDOM = writer.getOldDocument();

                    if (oldDOM != null && newDOM != null) {
                        //the diff can also be made subtree-aware
                        diffs = DOMUtils.domDiff(oldDOM, newDOM);
                    } else {
                        // This shouldn't be the case. Typically it is a symptom that
                        // There is something else wrong so log it as a warning.
                        String viewState = facesContext.getExternalContext()
                            .getRequestParameterMap().get("javax.faces.ViewState");
                        if (oldDOM == null) {
                            log.warning("Old DOM is null during domDiff calculation for javax.faces.ViewState " + viewState);
                        } else {
                            log.warning("New DOM is null during domDiff calculation for javax.faces.ViewState " + viewState);
                        }
                    }
                }  else {
                    writer.startSubtreeRendering();
                    Collection <String> renderIds = getRenderIds();
                    if (renderIds == null || renderIds.isEmpty()) {
                    } else {
                        diffs = renderSubtrees(viewRoot, renderIds);
                    }
                    writer.endDocument();
                    newDOM = writer.getOldDocument();
                }


                //reload page in case "html" or "head" element is updated
                boolean reload = false;
                for (int i = 0; i < diffs.length; i++) {
                    Element element = (Element) diffs[i];
                    String tag = element.getTagName();
                    if ("html".equals(tag) || "head".equals(tag)) {
                        reload = true;
                        break;
                    }
                }

                partialWriter.startDocument();

                if (reload) {
                    partialWriter.startEval();
                    partialWriter.write("window.location.reload();");
                    partialWriter.endEval();
                } else if (didNavigate()) {
                    partialWriter.startUpdate(PartialResponseWriter.RENDER_ALL_MARKER);
                    writeXMLPreamble(outputWriter);
                    DOMUtils.printNode(newDOM.getDocumentElement(), outputWriter);
                    partialWriter.endUpdate();
                    renderState();
                    renderExtensions();
                } else {
                    for (int i = 0; i < diffs.length; i++) {
                        Element element = (Element) diffs[i];
                        partialWriter.startUpdate(element.getAttribute("id"));
                        DOMUtils.printNode(element, outputWriter);
                        partialWriter.endUpdate();
                    }
                    renderState();
                    renderExtensions();
                }

                partialWriter.endDocument();

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

    private void writeXMLPreamble(Writer writer) throws IOException {
        //Add the xml and DOCTYPE preambles if they were originally there
        FacesContext fc = FacesContext.getCurrentInstance();
        UIViewRoot root = fc.getViewRoot();
        Object val = root.getAttributes().get(DOMResponseWriter.XML_MARKER);
        if( val != null ){
            writer.write(val.toString());
        }
        val = root.getAttributes().get(DOMResponseWriter.DOCTYPE_MARKER);
        if( val != null ){
            writer.write(val.toString());
        }
    }

    private boolean didNavigate(){
        UIViewRoot root = facesContext.getViewRoot();
        if( root == null ){
            return false;
        }
        if(!root.getViewMap().containsKey(DetectNavigationPhaseListener.NAVIGATION_MARK)){
            //If the navigation mark is missing, then a new view root was created
            //during INVOKE_APPLICATION phase indicating that navigation took place.
            return true;
        }

        return false;
    }

    private Node[] renderSubtrees(UIViewRoot viewRoot, Collection<String> renderIds)  {
        EnumSet<VisitHint> hints = EnumSet.of(VisitHint.SKIP_UNRENDERED);
        VisitContext visitContext =
            VisitContext.createVisitContext(facesContext, renderIds, hints);
        DOMPartialRenderCallback renderCallback =
            new DOMPartialRenderCallback(facesContext);
        viewRoot.visitTree(visitContext, renderCallback);
        //if subtree diffs fail, consider throwing an exception to trigger
        //a full page diff.  This may depend on development vs production
        return renderCallback.getDiffs();
    }

    private void applyBrowserChanges(Map parameters, Document document) {
        if (null == document)  {
            log.warning("DOM is null during applyBrowserChanges");
            return;
        }
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

    protected DOMResponseWriter createDOMResponseWriter(Writer outputWriter) {
        return new DOMResponseWriter(outputWriter);
    }
}

class DOMPartialRenderCallback implements VisitCallback {
    private static Logger log = Logger.getLogger(DOMPartialRenderCallback.class.getName());
    private FacesContext facesContext;
    //keep track of all diffs
    private ArrayList<Node> diffs;
    private boolean exception;


    public DOMPartialRenderCallback(FacesContext facesContext)  {
        this.facesContext = facesContext;
        this.diffs = new ArrayList<Node>();
        this.exception = false;
    }
    
    public VisitResult visit(VisitContext visitContext, UIComponent component) {
        String clientId = component.getClientId(facesContext);
        DOMResponseWriter domWriter = (DOMResponseWriter) 
                facesContext.getResponseWriter();
        Node oldSubtree = domWriter.seekSubtree(clientId);
        try {
            component.encodeAll(facesContext);
            Node newSubtree = domWriter.getDocument().getElementById(clientId);
            //these should be non-overlapping by application design
            if (null == oldSubtree) {
                diffs.add(newSubtree);
            } else {
                diffs.addAll(Arrays.asList(DOMUtils.nodeDiff(oldSubtree, newSubtree)));
            }
        } catch (Exception e) {
            //if errors occur in any of the subtrees, we likely should perform
            //a full diff, because a given subtree could be completely incompatible,
            //making the subtree diff invalid.  This is not yet implemented.
            exception = true;
            if (log.isLoggable(Level.SEVERE))  {
                log.severe("Subtree rendering failed for " + component.getClass() 
                        + " " + clientId + e.toString());
            }
        }
        //Return REJECT to skip subtree visiting
        return VisitResult.REJECT;
    }
    
    public Node[] getDiffs()  {
        return (Node[]) diffs.toArray(new Node[0]);
    }
    
    public boolean didFail()  {
        return exception;
    }
}
