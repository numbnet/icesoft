package org.icefaces.component.linkbutton;

import java.io.IOException;
import java.util.*;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;


import org.icefaces.component.utils.HTML;
import org.icefaces.component.utils.JSONBuilder;
import org.icefaces.component.utils.ScriptWriter;
import org.icefaces.component.utils.Utils;

import org.icefaces.util.EnvUtils;
import org.icefaces.render.MandatoryResourceComponent;


@MandatoryResourceComponent("org.icefaces.component.linkbutton.LinkButton")
public class LinkButtonRenderer extends Renderer {

    List <UIParameter> uiParamChildren;

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        if (requestParameterMap.containsKey("ice.event.captured")) {
            LinkButton link  = (LinkButton) uiComponent;
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

        LinkButton linkButton = (LinkButton) uiComponent;

        // capture any children UIParameter (f:param) parameters.
        uiParamChildren = Utils.captureParameters( linkButton );

        writer.startElement(HTML.DIV_ELEM, uiComponent );
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);

        //writer.startElement(HTML.INPUT_ELEM, uiComponent);
        writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_span", null);
        String styleClass = "yui-button yui-link-button";
        String myStyleClass = linkButton.getStyleClass();
        if ((myStyleClass != null) && (!"".equals(myStyleClass) )) {
            styleClass += " " + myStyleClass;
        } 
        writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);

        String style = linkButton.getStyle();
        if (style != null && style.trim().length() > 0) {
            writer.writeAttribute(HTML.STYLE_ATTR, style, HTML.STYLE_ATTR);
        }

        // first child
        writer.startElement(HTML.SPAN_ELEM, uiComponent);
        styleClass = "first-child";
        if ((myStyleClass != null) && (!"".equals(myStyleClass) )) {
            styleClass += " " + myStyleClass;
        }
        writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);
        writer.writeAttribute(HTML.ID_ATTR, "first-child", null);

        // button element
        writer.startElement(HTML.ANCHOR_ELEM, uiComponent);

        // Uncomment this for the so - called inline model onclick handler 
        writer.writeAttribute(HTML.ONCLICK_ATTR,
                              "return ice.component.linkButton.clickHandler(event, '" + clientId + "' );",
                              null);
        String temp;
        if ((temp = linkButton.getHref()) != null) {
            if (uiParamChildren != null) {
                temp += "?" + Utils.asParameterString( uiParamChildren );
            }
            writer.writeAttribute(HTML.HREF_ATTR, temp, null );
        }
        if ((temp  = linkButton.getHrefLang()) != null) {
            writer.writeAttribute(HTML.HREFLANG_ATTR, temp , null );
        }
        if ((temp = linkButton.getTarget()) != null) {
            writer.writeAttribute(HTML.TARGET_ATTR, temp, null );
        } 
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);

        LinkButton linkButton = (LinkButton) uiComponent;
        writer.endElement(HTML.ANCHOR_ELEM);
        writer.endElement(HTML.SPAN_ELEM);
        writer.endElement(HTML.SPAN_ELEM);

        ActionListener[] al = linkButton.getActionListeners();
        boolean doAction = (al.length > 0); 

        String jsProps = JSONBuilder.create().beginMap().
                entry("type", "link").
                entry("tabindex", linkButton.getTabindex()).
                entry("label", (String) linkButton.getValue()).
                entry("disabled", linkButton.isDisabled()).endMap().toString();

        JSONBuilder paramBuilder = JSONBuilder.create().
                                beginMap().
                                entry("singleSubmit", linkButton.isSingleSubmit()).
                                entry("doAction", doAction).
                                entry("ariaEnabled", EnvUtils.isAriaEnabled(facesContext));
        if (doAction && uiParamChildren != null) {
            paramBuilder.entry("postParameters",  Utils.asCommaSeperated(uiParamChildren) );
        }
        String params = "'" + clientId + "'," +
                         jsProps
                        + "," + paramBuilder.endMap().toString();

        String finalScript = "ice.component.linkButton.updateProperties(" + params + ");";
        ScriptWriter.insertScript(facesContext, uiComponent, finalScript);
        writer.endElement(HTML.DIV_ELEM);
    }
}