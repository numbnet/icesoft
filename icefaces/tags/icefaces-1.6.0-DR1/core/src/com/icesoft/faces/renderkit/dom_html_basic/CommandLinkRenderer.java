/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
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
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 *
 */

package com.icesoft.faces.renderkit.dom_html_basic;

import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.util.DOMUtils;

import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.faces.FacesException;
import javax.faces.component.NamingContainer;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import java.beans.Beans;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class CommandLinkRenderer extends DomBasicRenderer {

    private static final String HIDDEN_FIELD_NAME = "cl";

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        validateParameters(facesContext, uiComponent, UICommand.class);
        if (isStatic(uiComponent)) {
            return;
        }
        String commandLinkClientId = uiComponent.getClientId(facesContext);
        Map requestParameterMap =
                facesContext.getExternalContext().getRequestParameterMap();
        String commonCommandLinkHiddenFieldName = deriveCommonHiddenFieldName(
                facesContext, (UICommand) uiComponent);
        String hiddenFieldNameInRequestMap = (String) requestParameterMap
                .get(commonCommandLinkHiddenFieldName);
        if (hiddenFieldNameInRequestMap == null
            || hiddenFieldNameInRequestMap.equals("")
            || !commandLinkClientId.equals(hiddenFieldNameInRequestMap)) {
            // this command link did not invoke the submit
            return;
        }
        // this command link caused the submit so queue an event
        ActionEvent actionEvent = new ActionEvent(uiComponent);
        uiComponent.queueEvent(actionEvent);
    }

    protected static String deriveCommonHiddenFieldName(
            FacesContext facesContext,
            UIComponent uiComponent) {
        
        if (Beans.isDesignTime()){
            return "";
        }

        UIComponent parentNamingContainer = findForm(uiComponent);
        String parentClientId = parentNamingContainer.getClientId(facesContext);
        String hiddenFieldName = parentClientId
                                 + NamingContainer.SEPARATOR_CHAR
                                 + UIViewRoot.UNIQUE_ID_PREFIX
                                 + HIDDEN_FIELD_NAME;
        return hiddenFieldName;
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {

        validateParameters(facesContext, uiComponent, UICommand.class);

        DOMContext domContext =
                DOMContext.attachDOMContext(facesContext, uiComponent);

        if (!domContext.isInitialized()) {
            Element root = domContext.createElement("a");
            domContext.setRootNode(root);
            setRootElementId(facesContext, root, uiComponent);
            root.setAttribute("href", "# ");
        }
        Element root = (Element) domContext.getRootNode();
        DOMContext.removeChildren(root);
        renderLinkText(uiComponent, domContext, root);

        Map parameterMap = getParameterMap(uiComponent);
        renderOnClick(facesContext, uiComponent, root, parameterMap);

        String styleClass =
                (String) uiComponent.getAttributes().get("styleClass");
        if (styleClass != null) {
            root.setAttribute("class", styleClass);
        }

        PassThruAttributeRenderer.renderAttributes(facesContext, uiComponent,
                                                   new String[]{"onclick"});

        FormRenderer.addHiddenField(facesContext,
                                    deriveCommonHiddenFieldName(
                                            facesContext,
                                            (UICommand) uiComponent));
        Iterator parameterKeys = parameterMap.keySet().iterator();
        while (parameterKeys.hasNext()) {
            String nextKey = (String) parameterKeys.next();
            FormRenderer.addHiddenField(facesContext, nextKey);
        }
        domContext.stepInto(uiComponent);
    }


    /**
     * @param uiComponent
     * @param domContext
     * @param root
     */
    private void renderLinkText(UIComponent uiComponent, DOMContext
            domContext, Element root) {
        Object currentValue = ((UICommand) uiComponent).getValue();
        String linkText = null;
        if (currentValue != null) {
            linkText = DOMUtils.escapeAnsi(currentValue.toString());
        }
        // create a new or update the old text node for the label
        if (linkText != null && linkText.length() != 0) {
            Text labelNode = (Text) root.getFirstChild();
            if (labelNode == null) {
                labelNode = domContext.getDocument().createTextNode(linkText);
                root.appendChild(labelNode);
            } else {
                labelNode.setData(linkText);
            }
        }
    }

    /**
     * @param facesContext
     * @param uiComponent
     * @param root
     * @param parameters
     */
    protected void renderOnClick(FacesContext facesContext,
                                 UIComponent uiComponent,
                                 Element root, Map parameters) {
        UIComponent uiForm = findForm(uiComponent);
        if (uiForm == null) {
            throw new FacesException("CommandLink must be contained in a form");
        }
        String uiFormClientId = uiForm.getClientId(facesContext);
        Object onClick = uiComponent.getAttributes().get(HTML.ONCLICK_ATTR);

        //if onClick attribute set by the user, pre append it.
        if (onClick != null) {
            onClick = onClick.toString() + ";" +
                      getJavaScriptOnClickString(facesContext,
                                                 uiComponent, uiFormClientId,
                                                 parameters);
        } else {
            onClick = getJavaScriptOnClickString(facesContext,
                                                 uiComponent, uiFormClientId,
                                                 parameters);
        }
        root.setAttribute("onclick",
                          onClick.toString()); // replaced command w/component
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        validateParameters(facesContext, uiComponent, UICommand.class);
        DOMContext domContext =
                DOMContext.getDOMContext(facesContext, uiComponent);
        domContext.stepOver();
        domContext.streamWrite(facesContext, uiComponent);
    }


    private String getJavaScriptOnClickString(FacesContext facesContext,
                                              UIComponent uiComponent,
                                              String formClientId,
                                              Map parameters) {
        return getJavascriptHiddenFieldSetters(facesContext,
                                               (UICommand) uiComponent,
                                               formClientId, parameters)
               + "iceSubmit("
               + " document.forms['" + formClientId + "'],"
               + " this,event); "
               + "return false;";
    }

    /**
     * @param facesContext
     * @param uiCommand
     * @param formClientId
     * @param parameters
     * @return
     */
    protected static String getJavascriptHiddenFieldSetters(
            FacesContext facesContext,
            UICommand uiCommand, String formClientId, Map parameters) {
        StringBuffer buffer;
        buffer = new StringBuffer("document.forms['" + formClientId + "']['");
        buffer.append(deriveCommonHiddenFieldName(facesContext, uiCommand));
        buffer.append(
                "'].value='" + uiCommand.getClientId(facesContext) + "';");
        Iterator parameterKeys = parameters.keySet().iterator();
        while (parameterKeys.hasNext()) {
            String nextParamName = (String) parameterKeys.next();
            Object nextParamValue = parameters.get(nextParamName);
            buffer.append("document.forms['" + formClientId + "']['");
            buffer.append(nextParamName);
            buffer.append("'].value='");
            buffer.append(nextParamValue);
            buffer.append("';");
        }
        return buffer.toString();
    }

    public boolean getRendersChildren() {
        return true;
    }

    public void encodeChildren(FacesContext facesContext,
                               UIComponent uiComponent)
            throws IOException {
        validateParameters(facesContext, uiComponent, UICommand.class);
        Iterator children = uiComponent.getChildren().iterator();
        while (children.hasNext()) {
            UIComponent nextChild = (UIComponent) children.next();
            nextChild.encodeBegin(facesContext);
            if (nextChild.getRendersChildren()) {
                nextChild.encodeChildren(facesContext);
            }
            nextChild.encodeEnd(facesContext);
        }
    }
}
