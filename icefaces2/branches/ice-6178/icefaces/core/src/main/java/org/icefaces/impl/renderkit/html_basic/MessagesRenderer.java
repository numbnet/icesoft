package org.icefaces.impl.renderkit.html_basic;


import com.sun.faces.renderkit.html_basic.HtmlBasicRenderer;
import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import javax.faces.component.UIMessages;
import javax.faces.application.FacesMessage;
import java.io.IOException;
import java.util.Iterator;

/**
 *  ICE-5716 Override the encoding behaviour to always enclose the
 * &lt;h:messages&gt; output with a div. This will allow updates to consistently
 * find the section. 
 */
public class MessagesRenderer extends HtmlBasicRenderer {

    private static final Attribute[] ATTRIBUTES =
            AttributeManager.getAttributes(AttributeManager.Key.MESSAGESMESSAGES);

    @Override
    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException {

        rendererParamsNotNull(context, component);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component)
            throws IOException {

        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        //  If id is user specified, we must render
        boolean mustRender = shouldWriteIdAttribute(component);

        UIMessages messages = (UIMessages) component;
        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);

        String clientId = ((UIMessages) component).getFor();
        // if no clientId was included
        if (clientId == null) {
            // and the author explicitly only wants global messages
            if (messages.isGlobalOnly()) {
                // make it so only global messages get displayed.
                clientId = "";
            }
        }

        //"for" attribute optional for Messages
        Iterator messageIter = getMessageIter(context, clientId, component);

        assert(messageIter != null);

        if (!messageIter.hasNext()) {
            writer.startElement("div", component);
            if (mustRender) {
                writeIdAttributeIfNecessary(context, writer, component);
            } else {
                writer.writeAttribute("id", component.getClientId(), "id");
            }
            writer.endElement("div");
            return;
        }

        String layout = (String) component.getAttributes().get("layout");
        boolean showSummary = messages.isShowSummary();
        boolean showDetail = messages.isShowDetail();
        String styleClass = (String) component.getAttributes().get(
                "styleClass");

        boolean wroteTable = false;

        // ICE-5716 Put our own div around whatever else is being rendered for dom diffing
        writer.startElement("div", component);
        if (!mustRender) {
            writer.writeAttribute("id", component.getClientId(), "id");
        } else {
            writeIdAttributeIfNecessary(context, writer, component);
        } 

        //For layout attribute of "table" render as HTML table.
        //If layout attribute is not present, or layout attribute
        //is "list", render as HTML list.
        if ((layout != null) && (layout.equals("table"))) {
            writer.startElement("table", component);
            wroteTable = true;
        } else {
            writer.startElement("ul", component);
        }

        //Render "table" or "ul" level attributes.
        writeIdAttributeIfNecessary(context, writer, component);
        if (null != styleClass) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }
        // style is rendered as a passthru attribute
        RenderKitUtils.renderPassThruAttributes(context,
                                                writer,
                                                component,
                                                ATTRIBUTES);

        while (messageIter.hasNext()) {
            FacesMessage curMessage = (FacesMessage) messageIter.next();
            if (curMessage.isRendered() && !messages.isRedisplay()) {
                continue;
            }
            curMessage.rendered();

            String severityStyle = null;
            String severityStyleClass = null;

            // make sure we have a non-null value for summary and
            // detail.
            String summary = (null != (summary = curMessage.getSummary())) ?
                             summary : "";
            // Default to summary if we have no detail
            String detail = (null != (detail = curMessage.getDetail())) ?
                            detail : summary;


            if (curMessage.getSeverity() == FacesMessage.SEVERITY_INFO) {
                severityStyle =
                        (String) component.getAttributes().get("infoStyle");
                severityStyleClass = (String)
                        component.getAttributes().get("infoClass");
            } else if (curMessage.getSeverity() == FacesMessage.SEVERITY_WARN) {
                severityStyle =
                        (String) component.getAttributes().get("warnStyle");
                severityStyleClass = (String)
                        component.getAttributes().get("warnClass");
            } else
            if (curMessage.getSeverity() == FacesMessage.SEVERITY_ERROR) {
                severityStyle =
                        (String) component.getAttributes().get("errorStyle");
                severityStyleClass = (String)
                        component.getAttributes().get("errorClass");
            } else
            if (curMessage.getSeverity() == FacesMessage.SEVERITY_FATAL) {
                severityStyle =
                        (String) component.getAttributes().get("fatalStyle");
                severityStyleClass = (String)
                        component.getAttributes().get("fatalClass");
            }

            //Done intializing local variables. Move on to rendering.

            if (wroteTable) {
                writer.startElement("tr", component);
            } else {
                writer.startElement("li", component);
            }

            if (severityStyle != null) {
                writer.writeAttribute("style", severityStyle, "style");
            }
            if (severityStyleClass != null) {
                styleClass = severityStyleClass;
                writer.writeAttribute("class", styleClass, "styleClass");
            }

            if (wroteTable) {
                writer.startElement("td", component);
            }

            Object val = component.getAttributes().get("tooltip");
            boolean isTooltip = (val != null) && Boolean.valueOf(val.toString());

            boolean wroteTooltip = false;
            if (showSummary && showDetail && isTooltip) {
                writer.startElement("span", component);
                String title = (String) component.getAttributes().get("title");
                if (title == null || title.length() == 0) {
                    writer.writeAttribute("title", summary, "title");
                }
                writer.flush();
                writer.writeText("\t", component, null);
                wroteTooltip = true;
            }

            if (!wroteTooltip && showSummary) {
                writer.writeText("\t", component, null);
                writer.writeText(summary, component, null);
                writer.writeText(" ", component, null);
            }
            if (showDetail) {
                writer.writeText(detail, component, null);
            }

            if (wroteTooltip) {
                writer.endElement("span");
            }

            //close table row if present
            if (wroteTable) {
                writer.endElement("td");
                writer.endElement("tr");
            } else {
                writer.endElement("li");
            }

        } //messageIter

        //close table if present
        if (wroteTable) {
            writer.endElement("table");
        } else {
            writer.endElement("ul");
        }

        // close div always present
        writer.endElement("div");
    }

}
