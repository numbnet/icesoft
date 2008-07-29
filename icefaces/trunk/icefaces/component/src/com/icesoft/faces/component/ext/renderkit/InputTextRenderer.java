package com.icesoft.faces.component.ext.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.icesoft.faces.component.IceExtended;
import com.icesoft.faces.component.ext.HtmlInputText;
import com.icesoft.faces.context.effects.LocalEffectEncoder;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicInputRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;



public class InputTextRenderer extends 
            com.icesoft.faces.renderkit.dom_html_basic.InputTextRender {

    protected void writeRootElement(ResponseWriter writer, 
            UIComponent uiComponent,
            String clientId,
            String element,
            String[] excludeArray) throws IOException{
        excludeArray = new String[] {HTML.ONKEYPRESS_ATTR, HTML.ONFOCUS_ATTR, HTML.ONBLUR_ATTR};
        
        super.writeRootElement(writer, uiComponent, clientId, element, excludeArray);
        String onkeypress = ((HtmlInputText)uiComponent).getOnkeypress() != null ? ((HtmlInputText)uiComponent).getOnkeypress() : "";
        String onfocus = ((HtmlInputText)uiComponent).getOnfocus() != null ? ((HtmlInputText)uiComponent).getOnfocus() : "";
        String onblur = ((HtmlInputText)uiComponent).getOnblur() != null ? ((HtmlInputText)uiComponent).getOnblur() : "";

        writer.writeAttribute(HTML.ONKEYPRESS_ATTR, onkeypress + DomBasicRenderer.ICESUBMIT, null); 
        writer.writeAttribute(HTML.ONFOCUS_ATTR, onfocus + "setFocus(this.id);", null);

        
        if (((IceExtended) uiComponent).getPartialSubmit()) {
            writer.writeAttribute("onblur", onblur + "setFocus('');" + 
                                        "iceSubmitPartial(form,this,event); return false;", null);
        } else {
            writer.writeAttribute(HTML.ONBLUR_ATTR, onblur + "setFocus('');", null);            
        }
        LocalEffectEncoder.encodeLocalEffects(uiComponent,writer, 
                FacesContext.getCurrentInstance());  
    }
}
