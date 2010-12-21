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

package com.icesoft.faces.renderkit.dom_html_basic;

import com.icesoft.faces.component.AttributeConstants;
import com.icesoft.faces.context.DOMContext;
import com.icesoft.util.SeamUtilities;
import org.w3c.dom.Element;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UINamingContainer;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FormRenderer extends DomBasicRenderer {
    private static Logger log = Logger.getLogger("com.icesoft.faces.compat");
    public static final String COMMAND_LINK_HIDDEN_FIELD = "command_link_hidden_field";
    public static final String COMMAND_LINK_HIDDEN_FIELDS_KEY = "com.icesoft.faces.FormRequiredHidden";
    public static final String STATE_SAVING_MARKER = "~com.sun.faces.saveStateFieldMarker~";
    private static final String[] passThruAttributes = AttributeConstants.getAttributes(AttributeConstants.H_FORMFORM);

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        validateParameters(facesContext, uiComponent, UIForm.class);
        UIForm uiForm = (UIForm) uiComponent;
        Map requestParameterMap =
                facesContext.getExternalContext().getRequestParameterMap();
        String formClientId = uiForm.getClientId(facesContext);
        if (requestParameterMap.containsKey(formClientId) ||
                uiComponent.getAttributes().containsKey("fileUploaded")) {
            uiForm.setSubmitted(true);
        } else {
            uiForm.setSubmitted(false);
        }
    }

    public void encodeBegin(final FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        validateParameters(facesContext, uiComponent, UIForm.class);
        validateNestingForm(uiComponent);
        final DOMContext domContext =
                DOMContext.attachDOMContext(facesContext, uiComponent);
        String formClientId = uiComponent.getClientId(facesContext);

        if (!domContext.isInitialized()) {
            final Element root = domContext.createElement("form");

            domContext.setRootNode(root);
            root.setAttribute("id", formClientId);
            root.setAttribute("method", "post");
            ExternalContext externalContext = facesContext.getExternalContext();
            root.setAttribute("action", getAction(facesContext));

            String styleClass =
                    (String) uiComponent.getAttributes().get("styleClass");
            if (styleClass != null) {
                root.setAttribute("class", styleClass);
            }
            String acceptcharset =
                    (String) uiComponent.getAttributes().get("acceptcharset");
            if (acceptcharset != null) {
                root.setAttribute("accept-charset", acceptcharset);
            }

            // this hidden field will be checked in the decode method to
            // determine if this form has been submitted.
            Element formHiddenField = domContext.createElement("input");
            formHiddenField.setAttribute("type", "hidden");
            formHiddenField.setAttribute("name", formClientId);
            formHiddenField.setAttribute("value", formClientId);
            root.appendChild(formHiddenField);

            //JSF 2.0 portlet hidden field
            String viewId = facesContext.getViewRoot().getViewId();
            String actionURL = facesContext.getApplication().getViewHandler()
                    .getActionURL(facesContext, viewId);
            String encodedActionURL = externalContext.encodeActionURL(actionURL);
            String encodedPartialActionURL = externalContext.encodePartialActionURL(actionURL);
            if (encodedPartialActionURL != null) {
                if (!encodedPartialActionURL.equals(encodedActionURL)) {
                    Element encodedURLField = domContext.createElement("input");
                    encodedURLField.setAttribute("type", "hidden");
                    encodedURLField.setAttribute("name", "javax.faces.encodedURL");
                    encodedURLField.setAttribute("value", encodedPartialActionURL);
                    root.appendChild(encodedURLField);
                }
            }

        }

        //Write state so that full component state is available when marker is replaced
        //Currently we have to put the marker node in the DOM to position the marker correctly
        domContext.getRootNode().appendChild(domContext.createTextNode(STATE_SAVING_MARKER));

        // This has to occur outside the isInitialized test, as it has to happen
        // all the time, even if the form otherwise has not changed.
        Element root = (Element) domContext.getRootNode();

        String conversationId = SeamUtilities.getSeamConversationId();
        if (conversationId != null) {
            String conversationParamName =
                    SeamUtilities.getConversationIdParameterName();

            Element conversationIDElement =
                    domContext.createElement(HTML.INPUT_ELEM);
            if (log.isLoggable(Level.FINE)) {
                log.fine("Embedding Seam Param - name: " + conversationParamName +
                        ", value: " + conversationId);
            }
            conversationIDElement
                    .setAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_HIDDEN);
            conversationIDElement
                    .setAttribute(HTML.NAME_ATTR, conversationParamName);

            conversationIDElement.setAttribute(HTML.VALUE_ATTR, conversationId);
            root.appendChild(conversationIDElement);

        }

        String flowId = SeamUtilities.getSpringFlowId();
        if (flowId != null) {
            String flowParamName =
                    SeamUtilities.getFlowIdParameterName();

            Element flowIDElement =
                    domContext.createElement(HTML.INPUT_ELEM);
            if (log.isLoggable(Level.FINE)) {
                log.fine("Embedding Spring Param - name: " + flowParamName +
                        ", value: " + flowId);
            }
            String flowParamId = formClientId + UINamingContainer.getSeparatorChar(facesContext) + flowParamName;
            flowIDElement
                    .setAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_HIDDEN);
            flowIDElement
                    .setAttribute(HTML.NAME_ATTR, flowParamName);
            flowIDElement
                    .setAttribute(HTML.ID_ATTR, flowParamId);

            flowIDElement.setAttribute(HTML.VALUE_ATTR, flowId);
            root.appendChild(flowIDElement);

        }

        PassThruAttributeRenderer.renderHtmlAttributes(facesContext, uiComponent, passThruAttributes);
        String autoComplete = (String) uiComponent.getAttributes().get(HTML.AUTOCOMPLETE_ATTR);
        if (autoComplete != null && "off".equalsIgnoreCase(autoComplete)) {
            root.setAttribute(HTML.AUTOCOMPLETE_ATTR, "off");
        }

        // don't override user-defined value
        String userDefinedValue = root.getAttribute("onsubmit");
        if (userDefinedValue == null || userDefinedValue.equalsIgnoreCase("")) {
            root.setAttribute("onsubmit", "return false;");
        }

        domContext.stepInto(uiComponent);
    }

    public void encodeChildren(FacesContext facesContext,
                               UIComponent uiComponent) {
        validateParameters(facesContext, uiComponent, UIForm.class);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        validateParameters(facesContext, uiComponent, UIForm.class);

        // render needed hidden fields added by CommandLinkRenderer (and perhaps
        // other renderers as well)
        DOMContext domContext =
                DOMContext.getDOMContext(facesContext, uiComponent);
        //check if the messages renderer asked to be rendered later,
        //if yes, then re-render it
        if (uiComponent.getAttributes().get("$ice-msgs$") != null) {
            UIComponent messages = (UIComponent) uiComponent.getAttributes().get("$ice-msgs$");
            messages.encodeBegin(facesContext);
            messages.encodeChildren(facesContext);
            messages.encodeEnd(facesContext);
        }

        //facesContext.getApplication().getViewHandler().writeState(facesContext);
        domContext.stepOver();
    }

    /**
     * @param facesContext
     * @param fieldName
     */
    public static void addHiddenField(FacesContext facesContext,
                                      String fieldName) {
        addHiddenField(facesContext, fieldName, COMMAND_LINK_HIDDEN_FIELD);
    }

    /**
     * @param facesContext
     * @param fieldName
     * @param value
     */
    //make this method public when we modify the hidden field rendering
    //to accept arbitrary hidden fields
    private static void addHiddenField(FacesContext facesContext,
                                       String fieldName, String value) {
        Map hiddenFieldMap = getCommandLinkFields(facesContext);
        if (hiddenFieldMap == null) {
            hiddenFieldMap = createCommandLinkFieldsOnRequestMap(facesContext);
        }
        if (!hiddenFieldMap.containsKey(fieldName)) {
            hiddenFieldMap.put(fieldName, value);
        }
    }

    /**
     * @param facesContext
     * @return Map the hiddenFieldMap
     */
    private static Map getCommandLinkFields(FacesContext facesContext) {
        Map requestMap = facesContext.getExternalContext().getRequestMap();
        Map hiddenFieldMap =
                (Map) requestMap.get(COMMAND_LINK_HIDDEN_FIELDS_KEY);
        if (hiddenFieldMap == null) {
            hiddenFieldMap = new HashMap();
            requestMap.put(COMMAND_LINK_HIDDEN_FIELDS_KEY, hiddenFieldMap);
        }
        return hiddenFieldMap;
    }

    /**
     * @param facesContext
     * @return Map hiddenFieldMap
     */
    private static Map createCommandLinkFieldsOnRequestMap(
            FacesContext facesContext) {
        Map requestMap = facesContext.getExternalContext().getRequestMap();
        Map hiddenFieldMap =
                (Map) requestMap.get(COMMAND_LINK_HIDDEN_FIELDS_KEY);
        if (hiddenFieldMap == null) {
            hiddenFieldMap = new HashMap();
            requestMap.put(COMMAND_LINK_HIDDEN_FIELDS_KEY, hiddenFieldMap);
        }
        return hiddenFieldMap;
    }

    private void validateNestingForm(UIComponent uiComponent) throws IOException {
        UIComponent parent = uiComponent.getParent();
        if (parent == null) {
            return;
        }
        if (parent instanceof UIForm) {
            throw new FacesException("Nested form found on the page. The form " +
                    "action element can not be nested");
        }
        validateNestingForm(parent);
    }

    private static String getAction(FacesContext facesContext) {
        String viewId = facesContext.getViewRoot().getViewId();
        String actionURL = facesContext.getApplication().getViewHandler().
                getActionURL(facesContext, viewId);
        return (facesContext.getExternalContext().encodeActionURL(actionURL));
    }
}
