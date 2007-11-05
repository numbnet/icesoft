package com.icesoft.faces.component.inputrichtext;

import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicInputRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import org.w3c.dom.Element;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class InputRichTextRenderer extends DomBasicInputRenderer {

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        String clientId = uiComponent.getClientId(facesContext);
        InputRichText inputRichText = (InputRichText) uiComponent;
        DOMContext domContext =
                DOMContext.attachDOMContext(facesContext, uiComponent);
        if (!domContext.isInitialized()) {
            Element root = domContext.createRootElement(HTML.DIV_ELEM);
            root.setAttribute(HTML.ID_ATTR, clientId + "container");
            Element div = domContext.createElement(HTML.DIV_ELEM);
            root.setAttribute(HTML.CLASS_ATTR, inputRichText.getStyleClass());
            if (inputRichText.getStyle() != null) {
                root.setAttribute(HTML.STYLE_ATTR, inputRichText.getStyle());
            }
            root.appendChild(div);
            if (inputRichText.isToolbarOnly()) {
                div.setAttribute(HTML.ID_ATTR, inputRichText.getId());
                div.setAttribute(HTML.STYLE_ATTR, "width:"+ inputRichText.getWidth() +"px; height:"+ inputRichText.getHeight() +"px;");                
                domContext.stepOver();
                return;
            } else {
                div.setAttribute(HTML.ID_ATTR, clientId + "editor");
            }
            StringBuffer call = new StringBuffer();
            call.append("Ice.FCKeditor.register ('" + clientId + "', new Ice.FCKeditor('" + clientId + "', '','" + inputRichText.getLanguage() + "', '" + inputRichText.getFor() + "', '" + inputRichText.getBaseURI().getPath() + "','" + inputRichText.getWidth() + "', '" + inputRichText.getHeight() + "'));");
            if (inputRichText.getValue() != null) {
                call.append("Ice.FCKeditor.getInstance('" + clientId + "').value('" + clientId + "','');");
            }

            Element textFormat = (Element) domContext.createElement(HTML.INPUT_ELEM);
            textFormat.setAttribute(HTML.TYPE_ATTR, "hidden");
            textFormat.setAttribute(HTML.VALUE_ATTR, "text");
            textFormat.setAttribute(HTML.ID_ATTR, clientId + "Format");
            textFormat.setAttribute(HTML.NAME_ATTR, clientId + "Format");
            root.appendChild(textFormat);

            Element hiddenValueHolder = domContext.createElement(HTML.INPUT_ELEM);
            hiddenValueHolder.setAttribute(HTML.TYPE_ATTR, "hidden");
            hiddenValueHolder.setAttribute(HTML.ID_ATTR, clientId + "valueHolder");
            root.appendChild(hiddenValueHolder);

            if (inputRichText.getValue() != null) {
                hiddenValueHolder.setAttribute(HTML.VALUE_ATTR, inputRichText.getValue().toString());
            }

            Element script = domContext.createElement(HTML.SCRIPT_ELEM);
            script.setAttribute(HTML.TYPE_ATTR, "text/javascript");
            script.appendChild(domContext.createTextNode(call.toString()));
            root.appendChild(script);

            domContext.stepOver();
        }
    }


}
