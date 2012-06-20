package org.icefaces.ace.model.chart;

import org.icefaces.ace.model.SimpleEntry;
import org.icefaces.ace.util.JSONBuilder;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Copyright 2010-2011 ICEsoft Technologies Canada Corp.
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
 * User: Nils
 * Date: 12-05-03
 * Time: 12:53 PM
 */
public class CartesianSeries extends ChartSeries {


    public static enum CartesianType implements ChartType {
        BAR,
        LINE;
    }

    boolean show;
    boolean showLine;
    Boolean showMarker;
    boolean disableStack;
    boolean breakOnNull;
    boolean useNegativeColors;
    Boolean pointLabels;
    Integer pointLabelTolerance;
    Boolean pointLabelStacked;
    String[] pointLabelList;
    Integer xAxis;
    Integer yAxis;
    int neighborThreshold; // How close a cursor must be to a point to trigger hover
    String markerStyle; // Diamond, circle, square, x, plus, dash, filledDiamond, filledCircle, filledSquare
    int markerLineWidth; // For unfilled
    int markerSize;
    String markerColor;
    boolean markerShadow;
    int markerShadowAngle; // degrees
    int markerShadowOffset; // pixels
    int markerShadowDepth; // number of shadow strokes,¬†each 1 shadow offset from the last
    int markerShadowAlpha; // 0 - 100 alpha
    boolean showLabel;
    int lineWidth;
    String lineJoin; // Round, bevel, miter
    String lineCap; // Butt, line, square
    boolean shadow;
    int shadowAngle; // degrees
    int shadowOffset; // pixels
    int shadowDepth; // number of shadow strokes, each 1 shadow offset from the last
    int shadowAlpha; // 0 - 100 alpha
    boolean fill;
    boolean fillAndStroke;
    Boolean fillToZero;
    long fillToValue;
    String fillAxis; // x or y
    String fillColor; // CSS
    int fillAlpha; // 0 - 100 alpha
    boolean highlightMouseOver;
    boolean highlightMouseDown;
    String[] highlightColors; // When rendering as bar, and varyBarColor = true, list of colors applied
    int barMargin;
    int barPadding;
    int barWidth;
    Boolean horizontalBar;
    boolean varyBarColor;
    boolean waterfall;
    int groups;


    // Add object as y-value with x-index relative to position in series vs separately determined x-axis ticks

    /**
     * Add an point as a y-value with a x-value relative to the position in this series and the value at an accompanying index in a
     * separately determined list of x-axis ticks.
     * @param y the value of the point
     */
    public void add(Object y) {
        getData().add(new SimpleEntry<Object, Object>(null, y));
    }

    /**
     * Add an point as an explicit x and y value pair.
     * @param x the x coordinate of the point
     * @param y the y coordinate of the point
     */
    public void add(Object x, Object y) {
        getData().add(new SimpleEntry<Object, Object>(x, y));
    }

    /**
     * Used by the ChartRenderer to produce a JSON representation of the configuration of this series.
     * @return the serialized JSON object
     */
    @Override
    public String getDataJSON() {
        JSONBuilder data = JSONBuilder.create();
        Class valueType = null;
        Class keyType = null;
        data.beginArray();

        for (Object obj : (List<Object>)getData()) {
            Map.Entry<Object, Object> point = (Map.Entry<Object, Object>)obj;
            Object key = point.getKey();
            Object value = point.getValue();
            // Narrow type of value to write correct JS type.
            // If JS type is String for all cases (which is, and should be supported)
            // x-axis is not autoscaled correctly.
            Object outObj = null;

            // TODO: Add java date obj support
            if (key != null && keyType == null) {
                if (key instanceof Number) keyType = Number.class;
                else if (key instanceof String) keyType = String.class;
                else if (key instanceof Date) keyType = Date.class;
            }

            if (valueType == null) {
                if (value instanceof Number) valueType = Number.class;
                else if (value instanceof String) valueType = String.class;
                else if (value instanceof Date) valueType = Date.class;
            }

            if (key != null) {
                data.beginArray();
                if (keyType == Number.class)
                    data.item(((Number)key).doubleValue());
                else if (keyType == String.class)
                    data.item(key.toString());
                else if (keyType == Date.class)
                    data.item(((Date)key).getTime());
            }

            if (valueType == Number.class)
                data.item(((Number)value).doubleValue());
            else if (valueType == String.class)
                data.item(value.toString());
            else if (keyType == Date.class)
                data.item(((Date)key).getTime());

            if (key != null)
                data.endArray();
        }
        data.endArray();
        return data.toString();
    }

    /**
     * Used by the ChartRenderer to produce a JSON representation of the data of this series.
     * @return the JSON object
     */
    @Override
    public String getConfigJSON() {
        JSONBuilder cfg = JSONBuilder.create();
        Class valueType = null;
        String label = getLabel();
        Boolean showMarker = isShowMarker();
        Integer xAxis = getXAxis();
        Integer yAxis = getYAxis();

        cfg.beginMap();

        if (label != null)
            cfg.entry("label", label);

        if (xAxis != null)
            cfg.entry("xaxis", "x"+xAxis+"axis");

        if (yAxis != null)
            cfg.entry("yaxis", "y"+yAxis+"axis");

        if (showMarker != null)
            cfg.entry("showMarker", showMarker);

        if (type != null) {
            if (type.equals(CartesianType.BAR))
                cfg.entry("renderer", "ice.ace.jq.jqplot.BarRenderer", true) ;
            else if (type.equals(CartesianType.LINE))
                cfg.entry("renderer", "ice.ace.jq.jqplot.LineRenderer", true) ;
        }

        if (hasPointLabelOptionSet()) encodePointLabelOptions(cfg);

        if (hasRenderOptionsSet()) {
            cfg.beginMap("rendererOptions");
            Boolean ftz = isFillToZero();
            Boolean horiz = isHorizontalBar();

            if (ftz != null) cfg.entry("fillToZero", ftz);
            if (horiz != null) cfg.entry("barDirection", horiz ? "horizontal" : "vertical");
            cfg.endMap();
        }

        cfg.endMap();
        return cfg.toString();
    }

    private void encodePointLabelOptions(JSONBuilder cfg) {
        cfg.beginMap("pointLabels");

        if (pointLabels != null)
            cfg.entry("show", pointLabels);

        if (pointLabelList != null) {
            cfg.beginArray("labels");
            for (String s : pointLabelList)
                cfg.item(s);
            cfg.endArray();
        }

        if (pointLabelTolerance != null)
            cfg.entry("edgeTolerance", pointLabelTolerance);

        if (pointLabelStacked != null)
            cfg.entry("stackedValue", pointLabelStacked);

        cfg.endMap();
    }

    private boolean hasPointLabelOptionSet() {
        return (pointLabels != null || pointLabelList != null || pointLabelTolerance != null || pointLabelStacked != null);
    }

    private boolean hasRenderOptionsSet() {
        return (isFillToZero() != null || isHorizontalBar() != null);
    }

    public Boolean isHorizontalBar() {
        return horizontalBar;
    }

    public void setHorizontalBar(Boolean horizontalBar) {
        this.horizontalBar = horizontalBar;
    }

    public Boolean isPointLabels() {
        return pointLabels;
    }

    public void setPointLabels(Boolean pointLabels) {
        this.pointLabels = pointLabels;
    }


    public Integer getPointLabelTolerance() {
        return pointLabelTolerance;
    }

    public void setPointLabelTolerance(Integer pointLabelTolerance) {
        this.pointLabelTolerance = pointLabelTolerance;
    }

    public Boolean getPointLabelStacked() {
        return pointLabelStacked;
    }

    public void setPointLabelStacked(Boolean pointLabelStacked) {
        this.pointLabelStacked = pointLabelStacked;
    }

    public String[] getPointLabelList() {
        return pointLabelList;
    }

    public void setPointLabelList(String[] pointLabelList) {
        this.pointLabelList = pointLabelList;
    }

    //    /**
//     * Return the truth value of this series visibility.
//     * @return series visibility truth value
//     */
//    public boolean isShow() {
//        return show;
//    }
//
//    /**
//     * Set the visibility of this series.
//     * @param show series visibility truth value
//     */
//    public void setShow(boolean show) {
//        this.show = show;
//    }
//
//    /**
//     * Return the truth value of the line visibility
//     * @return line visibility truth value
//     */
//    public boolean isShowLine() {
//        return showLine;
//    }
//
//    /**
//     * Set the visibility of the line of this series
//     * @param showLine line visibility truth value
//     */
//    public void setShowLine(boolean showLine) {
//        this.showLine = showLine;
//    }
//
    /**
     * Return the truth value of the point marker visibility
     * @return point marker visibility truth value
     */
    public Boolean isShowMarker() {
        return showMarker;
    }

    /**
     * Set the visibility of the point markers of this series
     * @param showMarker point marker visibility truth value
     */
    public void setShowMarker(Boolean showMarker) {
        this.showMarker = showMarker;
    }
//
//    /**
//     * Return the truth value of the stack disabling behaviour on this series.
//     * @return stack disabling truth value
//     */
//    public boolean isDisableStack() {
//        return disableStack;
//    }
//
//    /**
//     * Enable to not stack this series with other series in the plot.
//     * To render properly, in the backing collection non-stacked series
//     * must follow any stacked series in the plot’s data series array.
//     * @param disableStack stack disabling truth value
//     */
//    public void setDisableStack(boolean disableStack) {
//        this.disableStack = disableStack;
//    }
//
//    public boolean isBreakOnNull() {
//        return breakOnNull;
//    }
//
//    public void setBreakOnNull(boolean breakOnNull) {
//        this.breakOnNull = breakOnNull;
//    }
//
//    public boolean isUseNegativeColors() {
//        return useNegativeColors;
//    }
//
//    public void setUseNegativeColors(boolean useNegativeColors) {
//        this.useNegativeColors = useNegativeColors;
//    }
//

    /**
     * Get a integer defining which axis this Series is plotted against.
     * @return the index of the x axis
     */
    public Integer getXAxis() {
        return xAxis;
    }

    /**
     * Set a integer defining which axis this Series is plotted against.
     * @param xAxis the index of the x axis
     */
    public void setXAxis(Integer xAxis) {
        this.xAxis = xAxis;
    }

    /**
     * Get a integer defining which axis this Series is plotted against.
     * @return the index of the y axis
     */
    public Integer getYAxis() {
        return yAxis;
    }

    /**
     * Set a integer defining which axis this Series is plotted against.
     * @param yAxis the index of the y axis
     */
    public void setYAxis(Integer  yAxis) {
        this.yAxis = yAxis;
    }
//
//    public int getNeighborThreshold() {
//        return neighborThreshold;
//    }
//
//    public void setNeighborThreshold(int neighborThreshold) {
//        this.neighborThreshold = neighborThreshold;
//    }
//
//    public String getMarkerStyle() {
//        return markerStyle;
//    }
//
//    public void setMarkerStyle(String markerStyle) {
//        this.markerStyle = markerStyle;
//    }
//
//    public int getMarkerLineWidth() {
//        return markerLineWidth;
//    }
//
//    public void setMarkerLineWidth(int markerLineWidth) {
//        this.markerLineWidth = markerLineWidth;
//    }
//
//    public int getMarkerSize() {
//        return markerSize;
//    }
//
//    public void setMarkerSize(int markerSize) {
//        this.markerSize = markerSize;
//    }
//
//    public String getMarkerColor() {
//        return markerColor;
//    }
//
//    public void setMarkerColor(String markerColor) {
//        this.markerColor = markerColor;
//    }
//
//    public boolean isMarkerShadow() {
//        return markerShadow;
//    }
//
//    public void setMarkerShadow(boolean markerShadow) {
//        this.markerShadow = markerShadow;
//    }
//
//    public int getMarkerShadowAngle() {
//        return markerShadowAngle;
//    }
//
//    public void setMarkerShadowAngle(int markerShadowAngle) {
//        this.markerShadowAngle = markerShadowAngle;
//    }
//
//    public int getMarkerShadowOffset() {
//        return markerShadowOffset;
//    }
//
//    public void setMarkerShadowOffset(int markerShadowOffset) {
//        this.markerShadowOffset = markerShadowOffset;
//    }
//
//    public int getMarkerShadowDepth() {
//        return markerShadowDepth;
//    }
//
//    public void setMarkerShadowDepth(int markerShadowDepth) {
//        this.markerShadowDepth = markerShadowDepth;
//    }
//
//    public int getMarkerShadowAlpha() {
//        return markerShadowAlpha;
//    }
//
//    public void setMarkerShadowAlpha(int markerShadowAlpha) {
//        this.markerShadowAlpha = markerShadowAlpha;
//    }
//
//    public boolean isShowLabel() {
//        return showLabel;
//    }
//
//    public void setShowLabel(boolean showLabel) {
//        this.showLabel = showLabel;
//    }
//
//    public int getLineWidth() {
//        return lineWidth;
//    }
//
//    public void setLineWidth(int lineWidth) {
//        this.lineWidth = lineWidth;
//    }
//
//    public String getLineJoin() {
//        return lineJoin;
//    }
//
//    public void setLineJoin(String lineJoin) {
//        this.lineJoin = lineJoin;
//    }
//
//    public String getLineCap() {
//        return lineCap;
//    }
//
//    public void setLineCap(String lineCap) {
//        this.lineCap = lineCap;
//    }
//
//    public boolean isShadow() {
//        return shadow;
//    }
//
//    public void setShadow(boolean shadow) {
//        this.shadow = shadow;
//    }
//
//    public int getShadowAngle() {
//        return shadowAngle;
//    }
//
//    public void setShadowAngle(int shadowAngle) {
//        this.shadowAngle = shadowAngle;
//    }
//
//    public int getShadowOffset() {
//        return shadowOffset;
//    }
//
//    public void setShadowOffset(int shadowOffset) {
//        this.shadowOffset = shadowOffset;
//    }
//
//    public int getShadowDepth() {
//        return shadowDepth;
//    }
//
//    public void setShadowDepth(int shadowDepth) {
//        this.shadowDepth = shadowDepth;
//    }
//
//    public int getShadowAlpha() {
//        return shadowAlpha;
//    }
//
//    public void setShadowAlpha(int shadowAlpha) {
//        this.shadowAlpha = shadowAlpha;
//    }
//
//    public boolean isFill() {
//        return fill;
//    }
//
//    public void setFill(boolean fill) {
//        this.fill = fill;
//    }
//
//    public boolean isFillAndStroke() {
//        return fillAndStroke;
//    }
//
//    public void setFillAndStroke(boolean fillAndStroke) {
//        this.fillAndStroke = fillAndStroke;
//    }
//

    /**
     * Return fillToZero behaviour truth value
     * @return fillToZero behaviour truth value
     */
    public Boolean isFillToZero() {
        return fillToZero;
    }

    /**
     * Enables bar charts to fill to zero but not beyond it. Used in cases
     * where scale shows the bars extending beyond 0 undesirably.
     * @param fillToZero
     */
    public void setFillToZero(Boolean fillToZero) {
        this.fillToZero = fillToZero;
    }
//
//    public long getFillToValue() {
//        return fillToValue;
//    }
//
//    public void setFillToValue(long fillToValue) {
//        this.fillToValue = fillToValue;
//    }
//
//    public String getFillAxis() {
//        return fillAxis;
//    }
//
//    public void setFillAxis(String fillAxis) {
//        this.fillAxis = fillAxis;
//    }
//
//    public String getFillColor() {
//        return fillColor;
//    }
//
//    public void setFillColor(String fillColor) {
//        this.fillColor = fillColor;
//    }
//
//    public int getFillAlpha() {
//        return fillAlpha;
//    }
//
//    public void setFillAlpha(int fillAlpha) {
//        this.fillAlpha = fillAlpha;
//    }
//
//    public boolean isHighlightMouseOver() {
//        return highlightMouseOver;
//    }
//
//    public void setHighlightMouseOver(boolean highlightMouseOver) {
//        this.highlightMouseOver = highlightMouseOver;
//    }
//
//    public boolean isHighlightMouseDown() {
//        return highlightMouseDown;
//    }
//
//    public void setHighlightMouseDown(boolean highlightMouseDown) {
//        this.highlightMouseDown = highlightMouseDown;
//    }
//
//    public String[] getHighlightColors() {
//        return highlightColors;
//    }
//
//    public void setHighlightColors(String[] highlightColors) {
//        this.highlightColors = highlightColors;
//    }
//
//    public int getBarMargin() {
//        return barMargin;
//    }
//
//    public void setBarMargin(int barMargin) {
//        this.barMargin = barMargin;
//    }
//
//    public int getBarPadding() {
//        return barPadding;
//    }
//
//    public void setBarPadding(int barPadding) {
//        this.barPadding = barPadding;
//    }
//
//    public int getBarWidth() {
//        return barWidth;
//    }
//
//    public void setBarWidth(int barWidth) {
//        this.barWidth = barWidth;
//    }
//
//    public String getBarDirection() {
//        return barDirection;
//    }
//
//    public void setBarDirection(String barDirection) {
//        this.barDirection = barDirection;
//    }
//
//    public boolean isVaryBarColor() {
//        return varyBarColor;
//    }
//
//    public void setVaryBarColor(boolean varyBarColor) {
//        this.varyBarColor = varyBarColor;
//    }
//
//    public boolean isWaterfall() {
//        return waterfall;
//    }
//
//    public void setWaterfall(boolean waterfall) {
//        this.waterfall = waterfall;
//    }
//
//    public int getGroups() {
//        return groups;
//    }
//
//    public void setGroups(int groups) {
//        this.groups = groups;
//    }
}
