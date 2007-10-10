package com.icesoft.faces.component.inputrichtext;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.w3c.dom.Element;

import com.icesoft.faces.component.inputrichtext.InputRichText;
import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.context.effects.JavascriptContext;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicInputRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;

public class InputRichTextRenderer extends DomBasicInputRenderer{
	 
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
	        	if(inputRichText.isToolbarOnly()) {
	        		div.setAttribute(HTML.ID_ATTR, inputRichText.getId());
		        	domContext.stepOver();
		        	return;
	        	} else {
	        		div.setAttribute(HTML.ID_ATTR, clientId+"editor");
	        	}
	        	StringBuffer call = new StringBuffer();
	        	if (inputRichText.getValue() != null) {
	        		call.append("Ice.FCKeditor.register ('"+ clientId+"', new Ice.FCKeditor('"+ clientId+"', '','"+ inputRichText.getLanguage() +"', '"+ inputRichText.getFor()  +"', '"+inputRichText.getBaseURI().getPath() +"/','"+ inputRichText.getWidth() +"', '"+ inputRichText.getHeight() +"'));");
	        		call.append("Ice.FCKeditor.getInstance('"+ clientId+"').value('"+ clientId+"','');");
	        	} else {
	        		call.append("Ice.FCKeditor.register ('"+ clientId+"', new Ice.FCKeditor('"+ clientId+"','','"+ inputRichText.getLanguage() +"', '"+ inputRichText.getFor()  +"', '"+ inputRichText.getBaseURI().getPath() +"/','"+ inputRichText.getWidth() +"', '"+  inputRichText.getHeight() +"'));");
	        	}


	        	Element textFormat = (Element) domContext.createElement(HTML.INPUT_ELEM);
	        	textFormat.setAttribute(HTML.TYPE_ATTR, "hidden");
	        	textFormat.setAttribute(HTML.VALUE_ATTR, "text");
	        	textFormat.setAttribute(HTML.ID_ATTR, clientId+"Format");
	        	textFormat.setAttribute(HTML.NAME_ATTR, clientId+"Format");
	        	root.appendChild(textFormat);

	        	Element hiddenValueHolder = domContext.createElement(HTML.INPUT_ELEM);
	        	hiddenValueHolder.setAttribute(HTML.TYPE_ATTR, "hidden");
	        	hiddenValueHolder.setAttribute(HTML.ID_ATTR, clientId+ "valueHolder");
	        	root.appendChild(hiddenValueHolder);
	        	
	        	if (inputRichText.getValue() != null) {
	        		hiddenValueHolder.setAttribute(HTML.VALUE_ATTR, inputRichText.getValue().toString());
	        	}
	        	
	        	JavascriptContext.addJavascriptCall(facesContext, call.toString());
	        	domContext.stepOver();
	        }
	 }


}
