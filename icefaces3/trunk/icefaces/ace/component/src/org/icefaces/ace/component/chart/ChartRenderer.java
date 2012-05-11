package org.icefaces.ace.component.chart;

import com.sun.org.apache.xpath.internal.axes.AxesWalker;
import org.apache.poi.hssf.record.chart.AxisUsedRecord;
import org.icefaces.ace.event.SeriesSelectionEvent;
import org.icefaces.ace.model.chart.ChartSeries;
import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.ace.util.HTML;
import org.icefaces.ace.util.JSONBuilder;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ChartRenderer extends CoreRenderer {
    @Override
    public void	decode(FacesContext context, UIComponent component) {
        Chart chart = (Chart) component;
        String id = chart.getClientId(context);
        String select = id + "_selection";
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();

        String selectInput = params.get(select);

        processSelections(chart, selectInput.split(","));
    }

    private void processSelections(Chart chart, String[] select) {
        int seriesIndex = Integer.parseInt(select[0]);
        int pointIndex = Integer.parseInt(select[1]);
        chart.queueEvent(new SeriesSelectionEvent(chart, seriesIndex, pointIndex));
    }

    @Override
    public void	encodeBegin(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        Chart chart = (Chart) component;
        String clientId = component.getClientId();

        writer.startElement(HTML.DIV_ELEM, chart);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        encodeChartContainer(context, writer, clientId);
        encodeScript(context, writer, clientId, chart);
        writer.endElement(HTML.DIV_ELEM);
    }

    private void encodeScript(FacesContext context, ResponseWriter writer, String clientId, Chart component) throws IOException {
        String widgetVar = resolveWidgetVar(component);
        List<ChartSeries> data = component.getValue();
        ChartSeries seriesDefaults = component.getDefaultSeriesConfig();
        Boolean stacking = component.isStackSeries();
        Boolean animated = component.isAnimated();

        JSONBuilder dataBuilder = new JSONBuilder();
        JSONBuilder cfgBuilder = new JSONBuilder();

        writer.startElement(HTML.SPAN_ELEM, null);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_script", null);
        writer.startElement(HTML.SCRIPT_ELEM, null);
        writer.writeAttribute(HTML.TYPE_ATTR, "text/javascript", null);

        // Build data arrays
        dataBuilder.beginArray();
        if (data != null)
            for (ChartSeries series : data)
                dataBuilder.item(series.getDataJSON(), false);
        dataBuilder.endArray();


        // Build configuration object
        cfgBuilder.beginMap();
        encodeAxesConfig(cfgBuilder, component);
        encodeSeriesConfig(cfgBuilder, seriesDefaults, data);
        encodeLegendConfig(cfgBuilder, component);
        if (stacking) cfgBuilder.entry("stackSeries", true);
        if (animated == null) cfgBuilder.entry("animate", "!ice.ace.jq.jqplot.use_excanvas", true);
        else if (animated) cfgBuilder.entry("animate", true);
        if (component.getSelectListener() != null)
            cfgBuilder.entry("handlePointClick", true);
        cfgBuilder.endMap();


        // Call plot init
        writer.write("var " + widgetVar + " = new ice.ace.Chart('" + clientId + "', " + dataBuilder + ", " + cfgBuilder +");");

        writer.endElement(HTML.SCRIPT_ELEM);
        writer.endElement(HTML.SPAN_ELEM);
    }

    private void encodeSeriesConfig(JSONBuilder cfg, ChartSeries defaults, List<ChartSeries> series) {
        // If defined, add default series config
        if (defaults != null)
            cfg.entry("seriesDefaults", defaults.getConfigJSON(), true);

        // If defined, add per-series config
        cfg.beginArray("series");
        if (series != null)
            for (ChartSeries s : series)
                cfg.item(s.getConfigJSON(), false);
        cfg.endArray();
    }

    private void encodeAxesConfig(JSONBuilder cfgBuilder, Chart component) {
        Axis axesDefaults = component.getDefaultAxesConfig();

        if (axesDefaults != null)
            cfgBuilder.entry("axesDefaults", axesDefaults.toString(), true);

        if (component.hasAxisConfig()) {
            Axis xAxis = component.getXAxis();
            Axis x2Axis = component.getX2Axis();
            List<Axis> yAxes = component.getYAxes();
            cfgBuilder.beginMap("axes");
            if (xAxis != null)
                cfgBuilder.entry("xaxis", xAxis.toString(), true);
            if (x2Axis != null)
                cfgBuilder.entry("x2axis", xAxis.toString(), true);
            if (yAxes != null)
                for (int i = 0; i < yAxes.size(); i++)
                    cfgBuilder.entry(i == 0 ? "yaxis" : "y"+(i+1)+"axis",
                            yAxes.get(i).toString());
            cfgBuilder.endMap();
        }
    }

    private void encodeLegendConfig(JSONBuilder cfgBuilder, Chart component) {
        Boolean legend = component.isLegend();
        if (legend != null && legend) {
            cfgBuilder.beginMap("legend");
            cfgBuilder.entry("show", true);

            LegendLocation pos = component.getLegendLocation();
            LegendPlacement place = component.getLegendPlacement();

            if (place != null) cfgBuilder.entry("placement", place.toString());
            if (pos != null) cfgBuilder.entry("location", pos.toString());

            cfgBuilder.endMap();
        }
    }

    private void encodeChartContainer(FacesContext context, ResponseWriter writer, String clientId) throws IOException {
        writer.startElement(HTML.DIV_ELEM, null);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "_chart", null);
        writer.endElement(HTML.DIV_ELEM);
    }
}
