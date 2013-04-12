package org.icefaces.mobi.component.dataview;

import org.icefaces.mobi.component.inputText.InputText;
import org.icefaces.mobi.utils.HTML;
import org.icemobile.component.IDataView;
import org.icemobile.model.DataViewColumnModel;
import org.icemobile.model.DataViewColumnsModel;
import org.icemobile.model.DataViewDataModel;
import org.icemobile.model.IndexedIterator;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.component.html.*;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

/**
 * Copyright 2010-2013 ICEsoft Technologies Canada Corp.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * <p/>
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * User: Nils Lundquist
 * Date: 2013-04-01
 * Time: 10:47 AM
 */
public class DataViewRenderer extends Renderer {
    private static Logger logger = Logger.getLogger(DataViewRenderer.class.getName());

    private DataView dataView;
    private List<ValueHolder> detailValueHolders;

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        dataView = (DataView) component;
        ResponseWriter writer = context.getResponseWriter();
        String clientId = dataView.getClientId();

        writer.startElement(HTML.DIV_ELEM, null);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);

        String styleClass = IDataView.DATAVIEW_CLASS;
        String userClass = dataView.getStyleClass();
        if (userClass != null) styleClass += " " + userClass;
        writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);

        String userStyle = dataView.getStyle();
        if (userStyle != null)
            writer.writeAttribute(HTML.STYLE_ATTR, userStyle, null);
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        DataView dataView = (DataView) component;
        ResponseWriter writer = context.getResponseWriter();

        encodeDetails(context, writer);
        encodeColumns(context, writer);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        encodeScript(context, writer);

        writer.endElement(HTML.DIV_ELEM);
    }

    private void encodeScript(FacesContext context, ResponseWriter writer) throws IOException {
        writer.startElement(HTML.SCRIPT_ELEM, null);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);
        writer.writeAttribute(HTML.ID_ATTR, dataView.getClientId() + "_js", null);

        String js =
            "ice.mobi.dataView.create("
                + '"' + dataView.getClientId() + '"'
                + ", {"
                + "}"
            + ");";

        writer.writeText(js, null);
        writer.endElement(HTML.SCRIPT_ELEM);
    };

    private void encodeColumns(FacesContext context,
                               ResponseWriter writer) throws IOException {
        DataViewColumns columns = dataView.getColumns();
        String var = dataView.getVar();

        if (columns == null) encodeEmptyBodyTable(writer);
        else {
            writer.startElement(HTML.DIV_ELEM, null);
            writer.writeAttribute(HTML.CLASS_ATTR, IDataView.DATAVIEW_MASTER_CLASS, null);

            DataViewColumnsModel columnModel = columns.getModel();
            DataViewDataModel dataModel = dataView.getDataModel();

            if (columnModel.hasHeaders())
                encodeHeaders(writer, columnModel, dataModel, true);

            encodeRows(context, writer, var, columnModel, dataModel);

            if (columnModel.hasFooters())
                encodeFooters(writer, columnModel, dataModel, true);

            writer.endElement(HTML.DIV_ELEM);
        }
    }

    private void encodeEmptyBodyTable(ResponseWriter writer) throws IOException {
        writer.startElement(HTML.DIV_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, IDataView.DATAVIEW_MASTER_CLASS, null);
        writer.startElement(HTML.DIV_ELEM, null);
        writer.startElement(HTML.TABLE_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, IDataView.DATAVIEW_BODY_CLASS, null);
        writer.endElement(HTML.TABLE_ELEM);
        writer.endElement(HTML.DIV_ELEM);
        writer.endElement(HTML.DIV_ELEM);
    }

    private void encodeHeaders(ResponseWriter writer,
                               DataViewColumnsModel columnModel,
                               DataViewDataModel dataModel,
                               boolean writeTable) throws IOException {
        /* Skip table when writing duplicate alignment header */
        if (writeTable) {
            writer.startElement(HTML.TABLE_ELEM, null);
            writer.writeAttribute(HTML.CLASS_ATTR, IDataView.DATAVIEW_HEAD_CLASS, null);
        }

        writer.startElement(HTML.THEAD_ELEM, null);
        writer.startElement(HTML.TR_ELEM, null);

        for (DataViewColumnModel column : columnModel) {
            writer.startElement(HTML.TH_ELEM, null);
            if (column.getHeaderText() != null)
                writer.write(column.getHeaderText());
            writer.endElement(HTML.TH_ELEM);
        }

        writer.endElement(HTML.TR_ELEM);
        writer.endElement(HTML.THEAD_ELEM);

        if (writeTable) writer.endElement(HTML.TABLE_ELEM);
    }

    private void encodeFooters(ResponseWriter writer,
                               DataViewColumnsModel columnModel,
                               DataViewDataModel dataModel, boolean writeTable) throws IOException {
        if (writeTable) {
            writer.startElement(HTML.TABLE_ELEM, null);
            writer.writeAttribute(HTML.CLASS_ATTR, IDataView.DATAVIEW_FOOT_CLASS, null);
        }

        writer.startElement(HTML.TFOOT_ELEM, null);
        writer.startElement(HTML.TR_ELEM, null);

        for (DataViewColumnModel column : columnModel) {
            writer.startElement(HTML.TD_ELEM, null);
            if (column.getFooterText() != null)
                writer.write(column.getFooterText());
            writer.endElement(HTML.TD_ELEM);
        }

        writer.endElement(HTML.TR_ELEM);
        writer.endElement(HTML.TFOOT_ELEM);

        if (writeTable) writer.endElement(HTML.TABLE_ELEM);
    }

    private void encodeRows(FacesContext context,
                            ResponseWriter writer,
                            String var,
                            DataViewColumnsModel columnModel,
                            DataViewDataModel dataModel) throws IOException {
        ELContext elContext = context.getELContext();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();

        writer.startElement(HTML.DIV_ELEM, null);
        writer.startElement(HTML.TABLE_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, IDataView.DATAVIEW_BODY_CLASS, null);

        encodeHeaders(writer, columnModel, dataModel, false);

        writer.startElement(HTML.TBODY_ELEM, null);

        for (IndexedIterator<Object> dataModelIterator = dataModel.iterator(); dataModelIterator.hasNext();) {
            Object rowData = dataModelIterator.next();
            // Init row context
            requestMap.put(var, rowData);

            writer.startElement(HTML.TR_ELEM, null);

            writer.writeAttribute("data-index", dataModelIterator.getIndex(), null);

            for (DataViewColumnModel column : columnModel) {
                writer.startElement(HTML.TD_ELEM, null);
                Object value = column.getValue().getValue(elContext);
                if (value != null) writer.write(value.toString());
                writer.endElement(HTML.TD_ELEM);
            }

            writer.startElement(HTML.INPUT_ELEM, null);
            writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_HIDDEN, null);
            writer.writeAttribute(HTML.VALUE_ATTR, encodeRowDetailString(context), null);
            writer.endElement(HTML.INPUT_ELEM);

            writer.endElement(HTML.TR_ELEM);
        }

        requestMap.remove(var);

        writer.endElement(HTML.TBODY_ELEM);

        encodeFooters(writer, columnModel, dataModel, false);

        writer.endElement(HTML.TABLE_ELEM);
        writer.endElement(HTML.DIV_ELEM);
    }

    private String encodeRowDetailString(FacesContext context) {
        StringBuilder detStr = new StringBuilder();

        if (detailValueHolders == null)
            detailValueHolders = new ArrayList<ValueHolder>(getValueHolders(dataView.getDetails()));

        ELContext elContext = context.getELContext();

        for (Iterator<ValueHolder> valueHolderIterator = detailValueHolders.iterator();
                valueHolderIterator.hasNext();) {
            ValueHolder valueHolder = valueHolderIterator.next();
            UIComponent vhComponent = (UIComponent) valueHolder;

            appendUpdateString(detStr, elContext, valueHolderIterator, vhComponent);
        }

        return detStr.toString();
    }

    private void appendUpdateString(StringBuilder detStr, ELContext elContext, Iterator<ValueHolder> valueHolderIterator, UIComponent vhComponent) {
        /* future inspections for other dynamic bindings must occur */
        ValueExpression ve = vhComponent.getValueExpression("value");

        if (ve == null) return; /* If component has no dynamic properties don't change its state */

        // TOOD : More detailed conversion to string form
        // TODO : ID / target compression
        // TODO : '|' & '=' escaping

        // Write Target Id
        detStr.append(vhComponent.getClientId()).append("=");
        // Write Update Directive
        detStr.append(getDirective(vhComponent)).append("=");
        // Write Update Value
        detStr.append(ve.getValue(elContext).toString()); /* will change as directives evolve */

        if (valueHolderIterator.hasNext()) detStr.append("|");
    }

    private String getDirective(UIComponent c) {
        if (c instanceof HtmlInputTextarea || c instanceof HtmlOutputText || c instanceof HtmlOutputLabel) {
            return "html"; /* swap inner html */
        } else if (c instanceof HtmlInputSecret || c instanceof InputText || c instanceof HtmlInputText) {
            return "attr=value";
        } else if (c instanceof HtmlSelectBooleanCheckbox) {
            return "attr=checked";
        }

        return "html";
    }

    private void encodeDetails(FacesContext context,
                               ResponseWriter writer) throws IOException {
        DataViewDetails details = dataView.getDetails();

        // Init row context
        Integer index = dataView.getActiveRowIndex();
        String var = dataView.getVar();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();

        if (index != null) {
            DataViewDataModel dataModel = dataView.getDataModel();
            requestMap.put(var, dataModel.getDataByIndex(index));
        }

        // Write detail region
        writer.startElement(HTML.DIV_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, IDataView.DATAVIEW_DETAIL_CLASS, null);
        writer.writeAttribute("data-index", index, null);

        if (details != null) details.encodeAll(context);

        writer.startElement(HTML.INPUT_ELEM, null);
        writer.writeAttribute(HTML.NAME_ATTR, dataView.getClientId() + "_active", null);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_HIDDEN, null);
        if (index == null)
            writer.writeAttribute(HTML.VALUE_ATTR, "", null);
        else
            writer.writeAttribute(HTML.VALUE_ATTR, index, null);
        writer.endElement(HTML.INPUT_ELEM);

        writer.endElement(HTML.DIV_ELEM);

        requestMap.remove(var);
    }

    public boolean getRendersChildren() {
        return true;
    }

    private List<ValueHolder> getValueHolders(UIComponent component) {
        if (component.getChildCount() > 0) {
            ArrayList<ValueHolder> valueHolders = new ArrayList<ValueHolder>();
            for (UIComponent child : component.getChildren()) {
                if (child instanceof ValueHolder) valueHolders.add((ValueHolder) child);
                valueHolders.addAll(getValueHolders(child));
            }
            return valueHolders;
        }
        else return Collections.emptyList();
    }
}
