package org.icefaces.ace.model.chart;

import org.icefaces.ace.util.JSONBuilder;

import java.util.ArrayList;
import java.util.List;

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
 * Time: 1:02 PM
 */
public abstract class ChartSeries {
    public interface ChartType {}

    List<Object> data;

    // Bar, line, etc.
    ChartType type;

    Boolean show;
    Integer xAxis;
    Integer yAxis;
    String label;
    Boolean showLabel;
    String color; // CSS color representation
    Integer lineWidth;
    String lineJoin; // Round, bevel, miter
    String lineCap; // Butt, line, square
    Integer shadow;
    Integer shadowAngle; // degrees
    Integer shadowOffset; // pixels
    Integer shadowDepth; // number of shadow strokes, each 1 shadow offset from the last
    Integer shadowAlpha; // 0 - 100 alpha
    Boolean showLine;
    Boolean showMarker;
    Boolean fill;
    String fillColor; // CSS
    Integer fillAlpha; // 0 - 100 alpha
    Boolean fillAndStroke;
    Boolean disableStack;
    Boolean fillToZero;
    String fillToValue;
    String fillAxis; // x or y
    Boolean useNegativeColors;

    /**
     * Return the truth value of this series visibility.
     * @return series visibility truth value
     */
    public Boolean getShow() {
        return show;
    }

    /**
     * Set the visibility of this series.
     * @param show series visibility truth value
     */
    public void setShow(Boolean show) {
        this.show = show;
    }

    /**
     * Return the truth value of the point marker visibility
     * @return point marker visibility truth value
     */
    public Boolean getShowMarker() {
        return showMarker;
    }

    /**
     * Set the visibility of the point markers of this series
     * @param showMarker point marker visibility truth value
     */
    public void setShowMarker(Boolean showMarker) {
        this.showMarker = showMarker;
    }


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

    /**
     * Return fillToZero behaviour truth value
     * @return fillToZero behaviour truth value
     */
    public Boolean getFillToZero() {
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


    /**
     * Return the label of the chart.
      * @return String label value
     */
    public String getLabel() {
        return label;
    }

    /**
     * Set the label of the chart used in the Legend etc.
     * @param label String value
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Return the list of plain Java objects backing the chart.
     * @return List value
     */
    public List<Object> getData() {
        if (data == null)
            data = new ArrayList<Object>();

        return data;
    }

    /**
     * Set the list of plain Java objects backing the chart.
     * @param data List value
     */
    public void setData(List<Object> data) {
        this.data = data;
    }

    /**
     * Enum value determining the type of renderer that will be used client side to plot the
     * data of this series. These ChartTypes are defined as enums fields of the subclasses of ChartSeries
     * in the org.icefaces.ace.model.chart package. These series subclasses are configurable with the options
     * of their contained (supported) ChartTypes.
     * @return enum determining series rendering engine
     */
    public ChartType getType() {
        return type;
    }

    /**
     * Set an enum value determining the type of renderer that will be used client side to plot the
     * data of this series. ChartTypes are defined as enum fields of the subclasses of ChartSeries
     * in the org.icefaces.ace.model.chart package. These series subclasses are configurable with the
     * options of their contained (supported) ChartTypes.
     * @param type an enum defined in this subclass of ChartSeries defining the series rendering type
     */
    public void setType(ChartType type) {
        this.type = type;
    }

    /**
     * Get whether or not we are using colors inverse to
     * those normally generated.
     * @return negative colors enabled
     */
    public Boolean getUseNegativeColors() {
        return useNegativeColors;
    }

    /**
     * Enable using colors inverse to those normally generated to
     * color this series.
     * @param useNegativeColors negative colors enabled
     */
    public void setUseNegativeColors(Boolean useNegativeColors) {
        this.useNegativeColors = useNegativeColors;
    }

    /**
     * Get whether or not this series fills the bars, sectors or region it covers with
     * the its given color.
     * @return fill enabled
     */
    public Boolean getFill() {
        return fill;
    }

    /**
     * Set whether or not this series fills the bars, sectors or region it covers with
     * the its given color.
     * @param fill fill enabled
     */
    public void setFill(Boolean fill) {
        this.fill = fill;
    }

    public JSONBuilder getDataJSON() {
        return JSONBuilder.create().beginArray();
    };

    public JSONBuilder getConfigJSON() {
        JSONBuilder cfg = JSONBuilder.create();

        String label = getLabel();

        Boolean show = getShow();
        Boolean showMarker = getShowMarker();
        Boolean useNegativeColors = getUseNegativeColors();
        Boolean ftz = getFillToZero();
        Boolean fill = getFill();

        Integer xAxis = getXAxis();
        Integer yAxis = getYAxis();

        cfg.beginMap();

        if (show != null)
            cfg.entry("show", show);

        if (label != null)
            cfg.entry("label", label);

        if (xAxis != null)
            cfg.entry("xaxis", "x"+xAxis+"axis");

        if (yAxis != null)
            cfg.entry("yaxis", "y"+yAxis+"axis");

        if (useNegativeColors != null)
            cfg.entry("useNegativeColors", useNegativeColors);

        if (showMarker != null)
            cfg.entry("showMarker", showMarker);

        if (ftz != null)
            cfg.entry("fillToZero", ftz);

        if (fill != null)
            cfg.entry("fill", fill);

        return cfg;
    };

    public abstract ChartType getDefaultType();

    public void clear() {
        data.clear();
    }
}
