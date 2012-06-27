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

    public ChartType getDefaultType() {
        return CartesianType.LINE;
    }

    Boolean pointLabels;
    Integer pointLabelTolerance;
    Boolean pointLabelStacked;
    String[] pointLabelList;

    Boolean dragable;
    DragConstraintAxis dragConstraintAxis;

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
    public JSONBuilder getDataJSON() {
        JSONBuilder data = super.getDataJSON();
        Class valueType = null;
        Class keyType = null;

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
        return data;
    }

    /**
     * Used by the ChartRenderer to produce a JSON representation of the data of this series.
     * @return the JSON object
     */
    @Override
    public JSONBuilder getConfigJSON() {
        JSONBuilder cfg = super.getConfigJSON();

        if (type != null) {
            if (type.equals(CartesianType.BAR))
                cfg.entry("renderer", "ice.ace.jq.jqplot.BarRenderer", true) ;
            else if (type.equals(CartesianType.LINE))
                cfg.entry("renderer", "ice.ace.jq.jqplot.LineRenderer", true) ;
        }

        if (hasPointLabelOptionSet()) encodePointLabelOptions(cfg);

        if (hasRenderOptionsSet()) {
            cfg.beginMap("rendererOptions");
            Boolean fill = getFill();
            Boolean horiz = isHorizontalBar();

            if (fill != null) cfg.entry("fill", fill);
            if (horiz != null) cfg.entry("barDirection", horiz ? "horizontal" : "vertical");
            cfg.endMap();
        }

        Boolean dragable = getDragable();
        if (dragable != null) {
            cfg.entry("isDragable", dragable);
            if (getDragConstraintAxis() != null) {
                cfg.beginMap("dragable");
                cfg.entry("constrainTo", getDragConstraintAxis().toString());
                cfg.endMap();
            }
        }

        cfg.endMap();
        return cfg;
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
        return (getFill() != null || isHorizontalBar() != null);
    }


    /**
     * Determine if this series is bar type, is it horizontal?
     * @return true if horizontal
     */
    public Boolean isHorizontalBar() {
        return horizontalBar;
    }

    /**
     * Set if this bar series is a horizontal type.
     * @param horizontalBar bar series horizontal
     */
    public void setHorizontalBar(Boolean horizontalBar) {
        this.horizontalBar = horizontalBar;
    }

    /**
     * Get if this series has labels rendered near each point
     * @return true if labels are rendered
     */
    public Boolean isPointLabels() {
        return pointLabels;
    }

    /**
     * Set if this series has labels rendered near each point.
     * @param pointLabels series point labelling
     */
    public void setPointLabels(Boolean pointLabels) {
        this.pointLabels = pointLabels;
    }

    /**
     * Set the distances that point labels must be from a boundary
     * if they are to be rendered.
     * @return distance in pixels
     */
    public Integer getPointLabelTolerance() {
        return pointLabelTolerance;
    }

    /**
     * Get the distance that point labels must be from a boundary
     * if they are to be rendered,
     * @param pointLabelTolerance distance in pixels
     */
    public void setPointLabelTolerance(Integer pointLabelTolerance) {
        this.pointLabelTolerance = pointLabelTolerance;
    }

    /**
     * Get if the point labels are to be rendered in a stacked plot.
     * @return true if the point labels are intended for a stacked plot.
     */
    public Boolean getPointLabelStacked() {
        return pointLabelStacked;
    }

    /**
     * Set if the point labels are to be rendered in a stacked plot.
     * @param pointLabelStacked if the labels are intended for a stacked plot
     */
    public void setPointLabelStacked(Boolean pointLabelStacked) {
        this.pointLabelStacked = pointLabelStacked;
    }

    /**
     * Get the list of labels for the points of this series.
     * @return list of labels to be applied to points of associated index.
     */
    public String[] getPointLabelList() {
        return pointLabelList;
    }

    /**
     * Set the list of labels for the points of this series.
     * @param pointLabelList list of labels to be applied to points of associated index
     */
    public void setPointLabelList(String[] pointLabelList) {
        this.pointLabelList = pointLabelList;
    }

    /**
     * Get if the points of this series are draggable.
     * @return true, if the points of this series are draggable.
     */
    public Boolean getDragable() {
        return dragable;
    }

    /**
     * Enable dragging for the points of this series. Enables dragStart / dragStop client
     * behaviour events as well as raising PointValueChangeEvents on the on the server.
     * @param dragable if the points of this series are drabbale.
     */
    public void setDragable(Boolean dragable) {
        this.dragable = dragable;
    }

    /**
     * Get the configured axis that dragging of points is confined to.
     * @return enum representation of the X, Y, or no axis.
     */
    public DragConstraintAxis getDragConstraintAxis() {
        return dragConstraintAxis;
    }

    /**
     * Set the configured axis that dragging of points is confined to.
     * @param dragConstraintAxis enum representation of the X, Y, or no axis.
     */
    public void setDragConstraintAxis(DragConstraintAxis dragConstraintAxis) {
        this.dragConstraintAxis = dragConstraintAxis;
    }
}
