package org.icefaces.component.commandlink;

import java.io.IOException;
import java.util.*;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;


import org.icefaces.component.utils.HTML;
import org.icefaces.component.utils.JSONBuilder;
import org.icefaces.component.utils.ScriptWriter;
import org.icefaces.util.EnvUtils;


public class CommandLinkRenderer extends Renderer {

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        if (requestParameterMap.containsKey("ice.event.captured")) {
            CommandLink link  = (CommandLink) uiComponent;
            String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
            String clientId = link.getClientId();
            if (clientId.equals(source)) {
                try {
                    uiComponent.queueEvent(new ActionEvent(uiComponent));
                } catch (Exception e) {}
            }
        }
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {

        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);

        CommandLink commandLink = (CommandLink) uiComponent;
        // root element

        writer.startElement(HTML.DIV_ELEM, uiComponent );
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);

        //writer.startElement(HTML.INPUT_ELEM, uiComponent);
        writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_span", null);
        writer.writeAttribute(HTML.CLASS_ATTR, "yui-button yui-link-button", null);
         String style = commandLink.getStyle();
        if (style != null && style.trim().length() > 0) {
            writer.writeAttribute(HTML.STYLE_ATTR, style, HTML.STYLE_ATTR);
        }

        // first child
        writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute(HTML.CLASS_ATTR, "first-child", null);
        writer.writeAttribute(HTML.ID_ATTR, "first-child", null);

        // button element
        String temp;
        writer.startElement(HTML.ANCHOR_ELEM, uiComponent);

        // Uncomment this for the so - called inline model onclick handler 
        writer.writeAttribute(HTML.ONCLICK_ATTR, "return ice.component.commandlink.clickHandler(event, '" + clientId + "' );", null);
        if ((temp = commandLink.getHref()) != null) {
            writer.writeAttribute(HTML.HREF_ATTR, temp, null );
        }
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);

        CommandLink commandLink = (CommandLink) uiComponent;
        writer.endElement(HTML.ANCHOR_ELEM);
        writer.endElement(HTML.SPAN_ELEM);
        writer.endElement(HTML.SPAN_ELEM);

        ActionListener[] al = commandLink.getActionListeners();
        boolean doAction = (al.length > 0); 

        String jsProps = JSONBuilder.create().beginMap().
                entry("type", "link").
                entry("tabindex", commandLink.getTabindex()).
                entry("disabled", commandLink.isDisabled()).endMap().toString();
        
        String params = "'" + clientId + "'," +
                         jsProps
                        + "," +
                        JSONBuilder.create().
                                beginMap().
                                entry("singleSubmit", commandLink.isSingleSubmit()).
                                entry("doAction", doAction).
                                entry("ariaEnabled", EnvUtils.isAriaEnabled(facesContext)).
                                endMap().toString();


        String finalScript = "ice.component.commandlink.updateProperties(" + params + ");";
        ScriptWriter.insertScript(facesContext, uiComponent, finalScript);
        writer.endElement(HTML.DIV_ELEM);
    }
}