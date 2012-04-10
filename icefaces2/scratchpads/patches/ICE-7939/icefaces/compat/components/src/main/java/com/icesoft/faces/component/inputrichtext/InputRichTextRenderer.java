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

package com.icesoft.faces.component.inputrichtext;

import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.context.ResourceRegistryLocator;
import com.icesoft.faces.context.effects.JavascriptContext;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicInputRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import com.icesoft.faces.util.CoreUtils;
import com.icesoft.util.pooling.ClientIdPool;
import org.w3c.dom.Element;

import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.net.URI;

public class InputRichTextRenderer extends DomBasicInputRenderer {

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        String clientId = uiComponent.getClientId(facesContext);
        InputRichText inputRichText = (InputRichText) uiComponent;
        DOMContext domContext =
                DOMContext.attachDOMContext(facesContext, uiComponent);
        if (!domContext.isInitialized()) {
            Element root = domContext.createRootElement(HTML.DIV_ELEM);
            root.setAttribute(HTML.ID_ATTR, ClientIdPool.get(clientId + "container"));
            Element div = domContext.createElement(HTML.DIV_ELEM);
            root.setAttribute(HTML.CLASS_ATTR, inputRichText.getStyleClass());
            if (inputRichText.getStyle() != null) {
                root.setAttribute(HTML.STYLE_ATTR, inputRichText.getStyle());
            }
            ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
            Element lib = domContext.createElement(HTML.SCRIPT_ELEM);
            lib.setAttribute(HTML.TYPE_ATTR, "text/javascript");
            lib.setAttribute(HTML.SRC_ATTR, CoreUtils.resolveResourceURL(facesContext, inputRichText.getBaseURI().toString()));
            root.appendChild(lib);

            URI icelibURI = ResourceRegistryLocator.locate(facesContext).loadJavascriptCode(InputRichText.ICE_FCK_EDITOR_JS);
            Element icelib = domContext.createElement(HTML.SCRIPT_ELEM);
            icelib.setAttribute(HTML.TYPE_ATTR, "text/javascript");
            icelib.setAttribute(HTML.SRC_ATTR, CoreUtils.resolveResourceURL(facesContext, icelibURI.toString()));
            root.appendChild(icelib);

            root.appendChild(div);
            div.setAttribute(HTML.ID_ATTR, ClientIdPool.get(clientId + "editor"));
            StringBuffer call = new StringBuffer();
            boolean partialSubmit = inputRichText.getPartialSubmit();
            if (inputRichText.isSaveOnSubmit()) partialSubmit = false;
            call.append("Ice.FCKeditor.register ('" + clientId + "', new Ice.FCKeditor('" + clientId + "', '" + inputRichText.getLanguage()
                    + "', '" + inputRichText.getFor() + "', '" + CoreUtils.resolveResourceURL(facesContext, inputRichText.getBaseURI().getPath()) + "','" + inputRichText.getWidth() +
                    "', '" + inputRichText.getHeight() + "', '" + inputRichText.getToolbar() + "', '" + inputRichText.getCustomConfigPath() +
                    "', '" + inputRichText.getSkin() + "'," + partialSubmit + "));");
            //ICE-4760    
            call.append("Ice.Prototype.$('" + clientId + "')[\"focus\"]= function(){handleApplicationFocus('" + clientId + "');};");

            String value = "";
            if (inputRichText.getValue() != null) {
                value = inputRichText.getValue().toString();
            }
            addHiddenField(domContext, root, ClientIdPool.get(clientId + "valueHolder"),
                    value);
            addHiddenField(domContext, root, ClientIdPool.get(clientId + "Disabled"),
                    String.valueOf(inputRichText.isDisabled()));
            Element scriptHolder = domContext.createElement(HTML.DIV_ELEM);
            scriptHolder.setAttribute(HTML.ID_ATTR, ClientIdPool.get(clientId + "script"));
            scriptHolder.setAttribute(HTML.STYLE_ATTR, "display:none");
            Element script = domContext.createElement(HTML.SCRIPT_ELEM);
            script.setAttribute(HTML.TYPE_ATTR, "text/javascript");
            script.appendChild(domContext.createTextNodeUnescaped(call.toString()));
            scriptHolder.appendChild(script);
            root.appendChild(scriptHolder);
            if (inputRichText.isSaveOnSubmit()) {
                Element saveOnSubmit = domContext.createElement(HTML.INPUT_ELEM);
                saveOnSubmit.setAttribute(HTML.ID_ATTR, clientId + "saveOnSubmit");
                saveOnSubmit.setAttribute(HTML.NAME_ATTR, clientId + "saveOnSubmit");
                saveOnSubmit.setAttribute(HTML.TYPE_ATTR, "hidden");
                root.appendChild(saveOnSubmit);
            }
            Element onCompleteInvoked = domContext.createElement(HTML.INPUT_ELEM);
            onCompleteInvoked.setAttribute(HTML.ID_ATTR, clientId + "onCompleteInvoked");
            onCompleteInvoked.setAttribute(HTML.NAME_ATTR, clientId + "onCompleteInvoked");
            onCompleteInvoked.setAttribute(HTML.TYPE_ATTR, "hidden");
            root.appendChild(onCompleteInvoked);

            div.setAttribute(HTML.ONMOUSEOUT_ATTR, "Ice.FCKeditorUtility.updateFields('" + clientId + "');");
            div.setAttribute(HTML.ONMOUSEOVER_ATTR, "Ice.FCKeditorUtility.activeEditor ='" + clientId + "';");
            JavascriptContext.addJavascriptCall(facesContext, "Ice.FCKeditorUtility && Ice.FCKeditorUtility.updateValue ('" + clientId + "');");

            domContext.stepOver();
        }
    }

    private void addHiddenField(DOMContext domContext, Element root,
                                String fieldName, String value) {
        Element hiddenFld = (Element) domContext.createElement(HTML.INPUT_ELEM);
        hiddenFld.setAttribute(HTML.TYPE_ATTR, "hidden");
        hiddenFld.setAttribute(HTML.ID_ATTR, fieldName);
        hiddenFld.setAttribute(HTML.NAME_ATTR, fieldName);
        if (value != null) {
            hiddenFld.setAttribute(HTML.VALUE_ATTR, value);
        }
        root.appendChild(hiddenFld);
    }

}
