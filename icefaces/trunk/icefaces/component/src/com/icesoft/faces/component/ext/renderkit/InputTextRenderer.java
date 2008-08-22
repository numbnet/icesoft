package com.icesoft.faces.component.ext.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

import com.icesoft.faces.component.IceExtended;
import com.icesoft.faces.component.ext.HtmlInputText;
import com.icesoft.faces.component.ext.KeyEvent;
import com.icesoft.faces.component.ext.taglib.Util;
import com.icesoft.faces.context.effects.LocalEffectEncoder;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import com.icesoft.faces.renderkit.dom_html_basic.PassThruAttributeWriter;

public class InputTextRenderer extends com.icesoft.faces.renderkit.dom_html_basic.InputTextRenderer {
    
    //private static final String[] passThruAttributes = ExtendedAttributeConstants.getAttributes(ExtendedAttributeConstants.ICE_INPUTTEXT);
    //handled HTML.ONKEYPRESS_ATTR, HTML.ONFOCUS_ATTR, HTML.ONBLUR_ATTR
    private static final String[] passThruAttributes = 
            new String[]{ HTML.ACCESSKEY_ATTR, HTML.ALT_ATTR, HTML.AUTOCOMPLETE_ATTR, HTML.DIR_ATTR, HTML.LANG_ATTR,  HTML.MAXLENGTH_ATTR ,  HTML.ONCHANGE_ATTR,  HTML.ONCLICK_ATTR,  HTML.ONDBLCLICK_ATTR, HTML.ONKEYDOWN_ATTR,  HTML.ONKEYUP_ATTR,  HTML.ONMOUSEDOWN_ATTR,  HTML.ONMOUSEMOVE_ATTR,  HTML.ONMOUSEOUT_ATTR,  HTML.ONMOUSEOVER_ATTR,  HTML.ONMOUSEUP_ATTR,  HTML.ONSELECT_ATTR,  HTML.SIZE_ATTR,  HTML.STYLE_ATTR,  HTML.TABINDEX_ATTR,  HTML.TITLE_ATTR };                     
           
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        writer.startElement(HTML.INPUT_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);

        PassThruAttributeWriter.renderHtmlAttributes(writer, uiComponent, passThruAttributes);

        writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
        Object styleClass = uiComponent.getAttributes().get("styleClass");
        if (styleClass != null) {
            writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);
        }

        String onkeypress = ((HtmlInputText) uiComponent).getOnkeypress() != null ? ((HtmlInputText) uiComponent).getOnkeypress() : "";
        String onfocus = ((HtmlInputText) uiComponent).getOnfocus() != null ? ((HtmlInputText) uiComponent).getOnfocus() : "";
        String onblur = ((HtmlInputText) uiComponent).getOnblur() != null ? ((HtmlInputText) uiComponent).getOnblur() : "";

        writer.writeAttribute(HTML.ONKEYPRESS_ATTR, onkeypress + DomBasicRenderer.ICESUBMIT, null);
        writer.writeAttribute(HTML.ONFOCUS_ATTR, onfocus + "setFocus(this.id);", null);


        if (((IceExtended) uiComponent).getPartialSubmit()) {
            writer.writeAttribute("onblur", onblur + "setFocus('');" +
                    "iceSubmitPartial(form,this,event); return false;", null);
        } else {
            writer.writeAttribute(HTML.ONBLUR_ATTR, onblur + "setFocus('');", null);
        }
        LocalEffectEncoder.encodeLocalEffects(uiComponent, writer,
                FacesContext.getCurrentInstance());
    }
    
    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        super.decode(facesContext, uiComponent);
        Object focusId = facesContext.getExternalContext()
                .getRequestParameterMap().get(FormRenderer.getFocusElementId());
        if (focusId != null) {
            if (focusId.toString()
                    .equals(uiComponent.getClientId(facesContext))) {
                ((HtmlInputText) uiComponent).setFocus(true);
            } else {
                ((HtmlInputText) uiComponent).setFocus(false);
            }
        }

        if (Util.isEventSource(facesContext, uiComponent)) {
            queueEventIfEnterKeyPressed(facesContext, uiComponent);
        }
    }


    public void queueEventIfEnterKeyPressed(FacesContext facesContext,
                                            UIComponent uiComponent) {
        try {
            KeyEvent keyEvent =
                    new KeyEvent(uiComponent, facesContext.getExternalContext()
                .getRequestParameterMap());
            if (keyEvent.getKeyCode() == KeyEvent.CARRIAGE_RETURN) {
                uiComponent.queueEvent(new ActionEvent(uiComponent));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    
}
