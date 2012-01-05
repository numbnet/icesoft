/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
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
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.context.PartialViewContext;
import javax.faces.context.PartialViewContextWrapper;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseId;
import javax.faces.application.ProjectStage;
import java.io.IOException;
import java.io.Writer;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DOMPartialViewContext extends PartialViewContextWrapper {
    private static final String JAVAX_FACES_VIEW_HEAD = "javax.faces.ViewHead";
    private static final String JAVAX_FACES_VIEW_BODY = "javax.faces.ViewBody";
    private static final Logger log = Logger.getLogger(DOMPartialViewContext.class.getName());
    private static final Pattern SPACE_SEPARATED = Pattern.compile("[ ]+");
    public static final String CUSTOM_UPDATE = "ice.customUpdate";

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
        ExternalContext ec = facesContext.getExternalContext();
        String customUpdate = ec.getRequestParameterMap().get(CUSTOM_UPDATE);
        //custom update set for entire response
        if ("true".equals(customUpdate))  {
            wrapped.processPartial(phaseId);
            return;
        }
        if (phaseId == PhaseId.RENDER_RESPONSE) {
            try {
                PartialResponseWriter partialWriter = getPartialResponseWriter();

                //TODO: need to revisit the strategy for getting the "raw" output writer directly
                Writer outputWriter = getResponseOutputWriter();
                ec.setResponseContentType("text/xml");
                ec.addResponseHeader("Cache-Control", "no-cache");

                DOMResponseWriter writer = new DOMResponseWriter(outputWriter,
                        ec.getResponseCharacterEncoding(),
                        ec.getResponseContentType());
                facesContext.setResponseWriter(writer);

                Document oldDOM = writer.getOldDocument();
                applyBrowserChanges(ec.getRequestParameterValuesMap(), oldDOM);

                UIViewRoot viewRoot = facesContext.getViewRoot();
                List<DOMUtils.EditOperation> diffs = null;
                Collection<String> customIds = null;
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
                    customIds = getCustomIds(customUpdate);
                    if (null != customIds)  {
                        renderIds.removeAll(customIds);
                    }
                    if (renderIds == null || renderIds.isEmpty()) {
                    } else {
                        diffs = renderSubtrees(viewRoot, renderIds);
                    }
                    writer.endDocument();
                    newDOM = writer.getOldDocument();
                }

                partialWriter.startDocument();
                if (null != customIds)  {
                    customRenderSubtrees(viewRoot, customIds);
                }

                if ((null == oldDOM) && isRenderAll()) {
                    partialWriter.startUpdate(PartialResponseWriter.RENDER_ALL_MARKER);
                    writeXMLPreamble(outputWriter);
                    DOMUtils.printNodeCDATA(newDOM.getDocumentElement(), outputWriter);
                    partialWriter.endUpdate();
                } else if (null != diffs)  {
                    for (DOMUtils.EditOperation op : diffs)  {

                        //client throws error on receving an update for the 'head' element
                        //avoid sending 'head' tag to not compromise the other updates
                        //todo: remove this test once the 'head' updates are applied by the client
                        String tagName = "";
                        if ( (null != op.element) && 
                             (op.element instanceof Element) )  {
                            tagName = ((Element) op.element).getTagName();
                        }
                        if ( ("head".equalsIgnoreCase(tagName)) ||
                            (JAVAX_FACES_VIEW_HEAD.equals(op.id)) )  {
                            continue;
                        }
                        if (op instanceof DOMUtils.InsertOperation)  {
                            partialWriter.startInsertAfter(op.id);
                            DOMUtils.printNodeCDATA(op.element, outputWriter);
                            partialWriter.endInsert();
                        } else if (op instanceof DOMUtils.DeleteOperation)  {
                            partialWriter.delete(op.id);
                        } else if (op instanceof DOMUtils.ReplaceOperation)  {
                            //we allow for the case where the "element" is a
                            //text node generated by means other than DOM diff
                            String updateId = op.id;
                            if (null == op.id)  {
                                updateId = getUpdateId((Element) op.element);
                            }
                            partialWriter.startUpdate(updateId);
                            DOMUtils.printNodeCDATA(op.element, outputWriter);
                            partialWriter.endUpdate();
                        }
                    }
                }

                renderState();
                renderExtensions();
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

    private Collection<String> getCustomIds(String idList)  {
        if (null == idList) {
            return null;
        } else {
            String[] ids = SPACE_SEPARATED.split(idList);
            return new ArrayList<String>(Arrays.asList(ids));
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

    private static List<DOMUtils.EditOperation> domDiff(Document oldDOM, Document newDOM) {
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

    private void customRenderSubtrees(UIViewRoot viewRoot, Collection<String> renderIds) {
        EnumSet<VisitHint> hints = EnumSet.of(VisitHint.SKIP_UNRENDERED);
        VisitContext visitContext =
                VisitContext.createVisitContext(facesContext, renderIds, hints);
        CustomPartialRenderCallback renderCallback =
                new CustomPartialRenderCallback(facesContext);
        viewRoot.visitTree(visitContext, renderCallback);
        return;
    }

    private List<DOMUtils.EditOperation> renderSubtrees(UIViewRoot viewRoot, Collection<String> renderIds) {
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

                    //The PortletFaces Bridge may return null rather than an empty
                    //string as the default so we guard against that.
                    String value = "";
                    Object rawValue = parameters.get(id);
                    if( rawValue != null){
                        if(rawValue instanceof String[]){
                            value =  ((String[])rawValue)[0];
                        }
                    }
                    //empty string is implied (default) when 'value' attribute is missing
                    if ("".equals(value)) {
                        inputElement.setAttribute("value", "");
                    } else {
                        if (inputElement.getAttribute("type").equals("checkbox")) {
                            inputElement.setAttribute("checked", "checked");
                        } else {
                            inputElement.setAttribute("value", value);
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
    private ArrayList<DOMUtils.EditOperation> diffs;
    private boolean exception;


    public DOMPartialRenderCallback(FacesContext facesContext) {
        this.facesContext = facesContext;
        this.diffs = new ArrayList<DOMUtils.EditOperation>();
        this.exception = false;
    }

    public VisitResult visit(VisitContext visitContext, UIComponent component) {
        FacesContext facesContext = visitContext.getFacesContext();
        boolean isDevMode = 
                !facesContext.isProjectStage(ProjectStage.Production);
        String clientId = component.getClientId(facesContext);
        DOMResponseWriter domWriter = (DOMResponseWriter)
                facesContext.getResponseWriter();
        Node oldSubtree = domWriter.seekSubtree(clientId);
        if (null == oldSubtree)  {
            log.fine("DOM Subtree rendering for " + clientId +
                " could not be found and is reverting to standard rendering.");
            ResponseWriter originalWriter = facesContext.getResponseWriter();
            StringWriter stringWriter = new StringWriter();
            ResponseWriter captureWriter = facesContext.getRenderKit()
                .createResponseWriter(stringWriter,
                    originalWriter.getContentType(), 
                    originalWriter.getCharacterEncoding() );
            facesContext.setResponseWriter(captureWriter);
            try {
                component.encodeAll(facesContext);
            } catch (IOException e)  {
                exception = true;
                if (log.isLoggable(Level.SEVERE)) {
                    log.log(Level.SEVERE, "Subtree rendering failed for " +
                            component.getClass() + " " + clientId, e);
                }
            }
            facesContext.setResponseWriter(originalWriter);
            DOMUtils.ReplaceOperation replaceOp = 
                new DOMUtils.ReplaceOperation( clientId,
                    domWriter.getDocument()
                    .createTextNode(stringWriter.toString()) );
            diffs.add(replaceOp);
            return VisitResult.REJECT;
        }
        try {
            component.encodeAll(facesContext);
            Node newSubtree = domWriter.getDocument().getElementById(clientId);
            //these should be non-overlapping by application design
            if (log.isLoggable(Level.FINEST)) {
                log.finest("Subtree rendering for " + clientId + 
                " oldSubtree: " + oldSubtree + " newSubtree: " + newSubtree);
            }
            if (null == oldSubtree) {
                //ReplaceOperation may be discarded by the client
                //and likely indicates an application design flaw
                if (null != newSubtree)  {
                    diffs.add(new DOMUtils.ReplaceOperation(newSubtree));
                }
                if (isDevMode)  {
                    log.warning("Subtree rendering " + clientId +
                        " which not exist on client and replace may fail.");
                }
            } else {
                if (null != newSubtree)  {
                    //typical case
                    diffs.addAll((DOMUtils.nodeDiff(oldSubtree, newSubtree)));
                } else {
                    //delete component no longer rendered, but there is now
                    //no way to add it again
                    diffs.add(new DOMUtils.DeleteOperation(clientId));
                    if (isDevMode)  {
                        log.warning("Subtree rendering deleting " + clientId +
                            " and subsequent updates may fail.");
                    }
                }
            }
        } catch (Exception e) {
            //if errors occur in any of the subtrees, we likely should perform
            //a full diff, because a given subtree could be completely incompatible,
            //making the subtree diff invalid.  This is not yet implemented.
            exception = true;
            if (log.isLoggable(Level.SEVERE)) {
                log.log(Level.SEVERE, "Subtree rendering failed for " +
                        component.getClass() + " " + clientId, e);
            }
        }
        //Return REJECT to skip subtree visiting
        return VisitResult.REJECT;
    }

    public List<DOMUtils.EditOperation> getDiffs() {
        return diffs;
    }

    public boolean didFail() {
        return exception;
    }
}


class CustomPartialRenderCallback implements VisitCallback {
    private static Logger log = Logger.getLogger(CustomPartialRenderCallback.class.getName());
    private FacesContext facesContext;

    public CustomPartialRenderCallback(FacesContext facesContext) {
        this.facesContext = facesContext;
    }

    public VisitResult visit(VisitContext visitContext, UIComponent component) {
        String clientId = component.getClientId(facesContext);
        try {
            PartialResponseWriter writer = facesContext
                    .getPartialViewContext().getPartialResponseWriter();

            ResponseWriter originalWriter = facesContext.getResponseWriter();
            ResponseWriter updateWriter = facesContext.getRenderKit()
                .createResponseWriter(
                    facesContext.getExternalContext().getResponseOutputWriter(),
                    originalWriter.getContentType(), 
                    originalWriter.getCharacterEncoding() );
            facesContext.setResponseWriter(updateWriter);
            Map extensionAttributes = new HashMap();
            extensionAttributes.put("id", clientId);
            extensionAttributes.put(
                    DOMPartialViewContext.CUSTOM_UPDATE, "true");
            writer.startExtension(extensionAttributes);
            writer.flush();
            //apply CDATA to inner updateWriter so that it
            //properly escapes its CDATA sections
            updateWriter.startCDATA();
            try {
                component.encodeAll(facesContext);
            }
            catch (Exception x) {
                if (log.isLoggable(Level.SEVERE)) {
                    log.log(Level.SEVERE, "Subtree rendering failed for " +
                            component.getClass() + " " + clientId, x);
                }
            }
            updateWriter.endCDATA();
            writer.endExtension();
            facesContext.setResponseWriter(originalWriter);
        } catch (Exception e) {
            if (log.isLoggable(Level.SEVERE)) {
                log.log(Level.SEVERE, "Subtree rendering failed for " +
                        component.getClass() + " " + clientId, e);
            }
        }
        //Return REJECT to skip subtree visiting
        return VisitResult.REJECT;
    }

}
