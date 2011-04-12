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

package org.icefaces.impl.context;

import org.icefaces.impl.util.DOMUtils;
import org.icefaces.util.EnvUtils;
import org.icefaces.util.FocusController;
import org.icefaces.util.JavaScriptRunner;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.context.PartialViewContext;
import javax.faces.context.PartialViewContextWrapper;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseId;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DOMPartialViewContext extends PartialViewContextWrapper {
    private static final String JAVAX_FACES_VIEW_HEAD = "javax.faces.ViewHead";
    private static final String JAVAX_FACES_VIEW_BODY = "javax.faces.ViewBody";
    private static final Logger log = Logger.getLogger(DOMPartialViewContext.class.getName());

    private PartialViewContext wrapped;
    protected FacesContext facesContext;
    private PartialResponseWriter partialWriter;
    private Boolean isAjaxRequest;

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
    public boolean isAjaxRequest() {
        if (isAjaxRequest != null)
            return isAjaxRequest;
        return wrapped.isAjaxRequest();
    }

    public void setAjaxRequest(boolean isAjaxRequest) {
        this.isAjaxRequest = isAjaxRequest;
    }


    @Override
    public void processPartial(PhaseId phaseId) {
        if (!EnvUtils.isICEfacesView(facesContext)) {
            wrapped.processPartial(phaseId);
            return;
        }
        if (!isRenderAll() && !EnvUtils.isSubtreeDiff(facesContext))  {
            wrapped.processPartial(phaseId);
            return;
        }

        if (phaseId == PhaseId.RENDER_RESPONSE) {
            try {
                PartialResponseWriter partialWriter = getPartialResponseWriter();
                ExternalContext ec = facesContext.getExternalContext();

                //TODO: need to revisit the strategy for getting the "raw" output writer directly
                Writer outputWriter = getResponseOutputWriter();
                ec.setResponseContentType("text/xml");
                ec.addResponseHeader("Cache-Control", "no-cache");

                DOMResponseWriter writer = new DOMResponseWriter(outputWriter,
                        ec.getRequestCharacterEncoding(),
                        ec.getRequestContentType());
                facesContext.setResponseWriter(writer);

                Document oldDOM = writer.getOldDocument();
                applyBrowserChanges(ec.getRequestParameterValuesMap(), oldDOM);

                UIViewRoot viewRoot = facesContext.getViewRoot();
                Node[] diffs = new Node[0];
                Document newDOM = null;
                writer.startDocument();

                if (isRenderAll()) {
                    Iterator<UIComponent> itr = viewRoot.getChildren().iterator();
                    while (itr.hasNext()) {
                        UIComponent kid = itr.next();
                        kid.encodeAll(facesContext);
                    }
                    writer.endDocument();
                    //the valid old document from the current pass is the new document
                    newDOM = writer.getOldDocument();

                    if (oldDOM != null && newDOM != null) {
                        diffs = domDiff(oldDOM, newDOM);
                    }
                } else {
                    writer.startSubtreeRendering();
                    Collection<String> renderIds = getRenderIds();
                    if (renderIds == null || renderIds.isEmpty()) {
                    } else {
                        diffs = renderSubtrees(viewRoot, renderIds);
                    }
                    writer.endDocument();
                    newDOM = writer.getOldDocument();
                }

                partialWriter.startDocument();

                if ((null == oldDOM) && isRenderAll()) {
                    partialWriter.startUpdate(PartialResponseWriter.RENDER_ALL_MARKER);
                    writeXMLPreamble(outputWriter);
                    DOMUtils.printNodeCDATA(newDOM.getDocumentElement(), outputWriter);
                    partialWriter.endUpdate();
                    renderState();
                    renderExtensions();
                } else {
                    for (int i = 0; i < diffs.length; i++) {
                        Element element = (Element) diffs[i];

                        //client throws error on receving an update for the 'head' element
                        //avoid sending 'head' tag to not compromise the other updates
                        //todo: remove this test once the 'head' updates are applied by the client
                        if (!"head".equalsIgnoreCase(element.getTagName())) {
                            partialWriter.startUpdate(getUpdateId(element));
                            DOMUtils.printNodeCDATA(element, outputWriter);
                            partialWriter.endUpdate();
                        }
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

    protected Writer getResponseOutputWriter() throws IOException {
        return facesContext.getExternalContext().getResponseOutputWriter();
    }

    private void writeXMLPreamble(Writer writer) throws IOException {
        //Add the xml and DOCTYPE preambles if they were originally there
        FacesContext fc = FacesContext.getCurrentInstance();
        UIViewRoot root = fc.getViewRoot();
        Object val = root.getAttributes().get(DOMResponseWriter.XML_MARKER);
        if (val != null) {
            writer.write(val.toString());
        }
        val = root.getAttributes().get(DOMResponseWriter.DOCTYPE_MARKER);
        if (val != null) {
            writer.write(val.toString());
        }
    }

    private static String getUpdateId(Element element) {
        if ("head".equalsIgnoreCase(element.getTagName())) {
            return JAVAX_FACES_VIEW_HEAD;
        } else if ("body".equalsIgnoreCase(element.getTagName())) {
            return JAVAX_FACES_VIEW_BODY;
        } else {
            return element.getAttribute("id");
        }
    }

    private static Node[] domDiff(Document oldDOM, Document newDOM) {
        final Runnable oldHeadRollback = setHeadID(oldDOM);
        final Runnable oldBodyRollback = setBodyID(oldDOM);
        final Runnable newHeadRollback = setHeadID(newDOM);
        final Runnable newBodyRollback = setBodyID(newDOM);
        try {
            return DOMUtils.domDiff(oldDOM, newDOM);
        } finally {
            oldHeadRollback.run();
            oldBodyRollback.run();
            newHeadRollback.run();
            newBodyRollback.run();
        }
    }

    private static final Runnable NOOP = new Runnable() {
        public void run() {
        }
    };

    private static Runnable setBodyID(Document document) {
        NodeList nodes = document.getElementsByTagName("body");
        if (nodes.getLength() > 0) {
            final Element body = (Element) nodes.item(0);
            if (!body.hasAttribute("id")) {
                body.setAttribute("id", JAVAX_FACES_VIEW_BODY);
                return new Runnable() {
                    public void run() {
                        body.removeAttribute("id");
                    }
                };
            }
        }

        return NOOP;
    }

    private static Runnable setHeadID(Document document) {
        NodeList nodes = document.getElementsByTagName("head");
        if (nodes.getLength() > 0) {
            final Element head = (Element) nodes.item(0);
            if (!head.hasAttribute("id")) {
                head.setAttribute("id", JAVAX_FACES_VIEW_HEAD);
                return new Runnable() {
                    public void run() {
                        head.removeAttribute("id");
                    }
                };
            }
        }

        return NOOP;
    }

    private Node[] renderSubtrees(UIViewRoot viewRoot, Collection<String> renderIds) {
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
        if (null == document) {
            //partial rendering should be valid in this case
            //since complete partial subtrees will be produced by the diff
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
        manageFocus();
        runScripts();
    }

    private void manageFocus() {
        String focusId = FocusController.getReceivedFocus(facesContext);
        boolean focusNotYetSet = !FocusController.isFocusSet(facesContext);

        //preserve focus received if not already set by one of the components
        if (focusNotYetSet && focusId != null) {
            FocusController.setFocus(facesContext, focusId);
        }
        if (FocusController.isFocusSet(facesContext)) {
            JavaScriptRunner.runScript(facesContext, "ice.applyFocus('" + FocusController.getFocus(facesContext) + "');");
        }
    }

    private void runScripts() {
        String scripts = JavaScriptRunner.collateScripts(facesContext);
        if (scripts.length() > 0) {
            try {
                PartialResponseWriter partialWriter = getPartialResponseWriter();
                partialWriter.startEval();
                partialWriter.write(scripts);
                partialWriter.endEval();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

class DOMPartialRenderCallback implements VisitCallback {
    private static Logger log = Logger.getLogger(DOMPartialRenderCallback.class.getName());
    private FacesContext facesContext;
    //keep track of all diffs
    private ArrayList<Node> diffs;
    private boolean exception;


    public DOMPartialRenderCallback(FacesContext facesContext) {
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
            if (log.isLoggable(Level.SEVERE)) {
                log.severe("Subtree rendering failed for " + component.getClass()
                        + " " + clientId + e.toString());
            }
        }
        //Return REJECT to skip subtree visiting
        return VisitResult.REJECT;
    }

    public Node[] getDiffs() {
        return (Node[]) diffs.toArray(new Node[0]);
    }

    public boolean didFail() {
        return exception;
    }
}
