package org.icefaces.ace.component.chart;

import org.icefaces.ace.event.SeriesSelectionEvent;
import org.icefaces.ace.model.chart.ChartSeries;
import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.ace.util.HTML;
import org.icefaces.ace.util.JSONBuilder;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
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

        if (selectInput != null) processSelections(chart, selectInput.split(","));

        decodeBehaviors(context, component);
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
        Boolean hiddenInit = component.isHiddenInitPolling();
        Boolean zoom = component.isZoom();
        Boolean cursor = component.isCursor();
        Boolean showTooltip = component.isShowTooltip();

        String title = component.getTitle();

        JSONBuilder dataBuilder = new JSONBuilder();
        JSONBuilder cfgBuilder = new JSONBuilder();

        writer.startElement(HTML.SPAN_ELEM, null);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "_script", null);
        writer.startElement(HTML.SCRIPT_ELEM, null);
        writer.writeAttribute(HTML.TYPE_ATTR, "text/javascript", null);

        // Build data arrays
        dataBuilder.beginArray();
        if (data != null)
            for (ChartSeries series : data)
                dataBuilder.item(series.getDataJSON().toString(), false);
        dataBuilder.endArray();


        // Build configuration object
        cfgBuilder.beginMap();
        encodeAxesConfig(cfgBuilder, component);
        encodeSeriesConfig(cfgBuilder, seriesDefaults, data);
        encodeLegendConfig(cfgBuilder, component);
        encodeHighlighterConfig(cfgBuilder, component);
        if (title != null) cfgBuilder.entry("title", title);
        if (stacking) cfgBuilder.entry("stackSeries", true);
        if (animated == null) cfgBuilder.entry("animate", "!ice.ace.jq.jqplot.use_excanvas", true);
        else if (animated) cfgBuilder.entry("animate", true);
        if (isAjaxClick(component))
            cfgBuilder.entry("handlePointClick", true);
        if (!hiddenInit) cfgBuilder.entry("disableHiddenInit", true);
        encodeClientBehaviors(context, component, cfgBuilder);

        if (cursor != null) {
            cfgBuilder.beginMap("cursor");
            cfgBuilder.entry("show", cursor);
            if (zoom != null) cfgBuilder.entry("zoom", zoom);
            if (showTooltip != null) cfgBuilder.entry("showTooltip", showTooltip);
            cfgBuilder.endMap();
        }

        if (cursor != null) {
            cfgBuilder.beginMap("cursor");
            cfgBuilder.entry("show", cursor);
            if (zoom != null) cfgBuilder.entry("zoom", zoom);
            if (showTooltip != null) cfgBuilder.entry("showTooltip", showTooltip);
            cfgBuilder.endMap();
        }
        cfgBuilder.endMap();

        // Call plot init
        writer.write("var " + widgetVar + " = new ice.ace.Chart('" + clientId + "', " + dataBuilder + ", " + cfgBuilder +");");

        writer.endElement(HTML.SCRIPT_ELEM);
        writer.endElement(HTML.SPAN_ELEM);
    }

    private void encodeHighlighterConfig(JSONBuilder cfgBuilder, Chart component) {
        if (component.isHighlighter() != null && component.isHighlighter()) {
            cfgBuilder.beginMap("highlighter");
            cfgBuilder.entry("show", true);

            Boolean showMarker = component.isHighlighterShowMarker();
            Location location = component.getHighlighterLocation();
            HighlighterTooltipAxes axes = component.getHighlighterAxes();
            String formatString = component.getHighlighterFormatString();
            Integer yvals = component.getHighlighterYValueCount();
            Boolean btf = component.isHighlighterBringSeriesToFront();

            if (showMarker != null) cfgBuilder.entry("showMarker", showMarker);
            if (location != null) cfgBuilder.entry("tooltipLocation", location.toString());
            if (axes != null) cfgBuilder.entry("tooltipAxes", axes.toString());
            if (formatString != null) cfgBuilder.entry("formatString", formatString);
            if (yvals != null) cfgBuilder.entry("yvalues", yvals);
            if (btf != null) cfgBuilder.entry("bringSeriesToFront", btf);

            cfgBuilder.endMap();
        }
    }
    
    private void encodeSeriesConfig(JSONBuilder cfg, ChartSeries defaults, List<ChartSeries> series) {
        // If defined, add default series config
        if (defaults != null)
            cfg.entry("seriesDefaults", defaults.getConfigJSON().toString(), true);
        else if (series != null && series.size() > 0) {
            try {
                ChartSeries firstSeries = series.get(0);

                // Create series to encode default renderer configuration
                Class seriesClass = series.get(0).getClass().getSuperclass();
                ChartSeries dummySeries = ((ChartSeries)seriesClass.newInstance());

                ChartSeries.ChartType firstSeriesType = firstSeries.getType();

                dummySeries.setType(firstSeriesType != null ? firstSeriesType : dummySeries.getDefaultType());
                cfg.entry("seriesDefaults", dummySeries.getConfigJSON().toString(), true);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        // If defined, add per-series config
        cfg.beginArray("series");
        if (series != null)
            for (ChartSeries s : series)
                cfg.item(s.getConfigJSON().toString(), false);
        cfg.endArray();
    }

    private void encodeAxesConfig(JSONBuilder cfgBuilder, Chart component) {
        Axis axesDefaults = component.getDefaultAxesConfig();

        if (axesDefaults != null)
            cfgBuilder.entry("axesDefaults", axesDefaults.toString(), true);

        if (component.hasAxisConfig()) {
            Axis xAxis = component.getXAxis();
            Axis x2Axis = component.getX2Axis();
            Axis[] yAxes = component.getYAxes();
            cfgBuilder.beginMap("axes");
            if (xAxis != null)
                cfgBuilder.entry("xaxis", xAxis.toString(), true);
            if (x2Axis != null)
                cfgBuilder.entry("x2axis", xAxis.toString(), true);
            if (yAxes != null)
                for (int i = 0; i < yAxes.length; i++)
                    cfgBuilder.entry(i == 0 ? "yaxis" : "y"+(i+1)+"axis", yAxes[i].toString(), true);
            cfgBuilder.endMap();
        }
    }

    private void encodeLegendConfig(JSONBuilder cfgBuilder, Chart component) {
        Boolean legend = component.isLegend();
        if (legend != null && legend) {
            cfgBuilder.beginMap("legend");
            cfgBuilder.entry("show", true);

            Location pos = component.getLegendLocation();
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

    public boolean isAjaxClick(Chart component) {
        if (component.getClientBehaviors().get("click") != null) return true;
        return component.getSelectListener() != null;
    }
}
