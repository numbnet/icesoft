package org.icefaces.ace.model.chart;

import org.icefaces.ace.model.SimpleEntry;
import org.icefaces.ace.util.JSONBuilder;

import javax.faces.component.UIComponent;
import java.util.Date;

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
 * Time: 12:58 PM
 */
public class BubbleSeries extends ChartSeries {
    private Boolean escapeHtml;
    private Boolean showLabels;
    private Boolean bubbleGradients;
    private Boolean varyBubbleColors;
    private Integer bubbleAlpha;

    private Boolean autoscaleBubbles;
    private Double autoscaleMultiplier;
    private Double autoscalePointsFactor;

    private Boolean highlightMouseOver;
    private Boolean highlightMouseDown;
    private String[] highlightColors;
    private Integer highlightAlpha;




    public static enum BubbleType implements ChartType {
        BUBBLE
    }

    public BubbleSeries() {}

    /**
     * Add a new bubble to this set of bubbles.
     * @param x x tick of the bubble
     * @param y y tick of the bubble
     * @param magnitude the area of the bubble
     */
    public void add(Object x, Object y, int magnitude) {
        getData().add(new Object[] {x, y, magnitude, null});
    }

    /**
     * Add a new bubble to this set of bubbles.
     * @param x x tick of the bubble
     * @param y y tick of the bubble
     * @param magnitude the area of the bubble
     * @param label the label of this bubble
     */
    public void add(Object x, Object y, int magnitude, String label) {
        getData().add(new Object[] {x, y, magnitude, label});
    }

    /**
     * Used by the ChartRenderer to produce a JSON representation of the configuration of this series.
     * @return the serialized JSON object
     * @param chart
     */
    @Override
    public JSONBuilder getDataJSON(UIComponent chart) {
        JSONBuilder cfg = super.getDataJSON(chart);

        Class xType = null;
        Class yType = null;

        for (Object o : getData()) {
            Object[] arr = (Object[]) o;
            cfg.beginArray();

            if (xType == null)
                xType = getObjType(arr[0]);

            if (yType == null)
                yType = getObjType(arr[1]);

            if (xType == Number.class) cfg.item(((Number)arr[0]).doubleValue());
            else if (xType == String.class) cfg.item((String)arr[0]);
            else if (xType == Date.class) cfg.item(((Date)arr[0]).getTime());

            if (yType == Number.class) cfg.item(((Number)arr[1]).doubleValue());
            else if (yType == String.class) cfg.item((String)arr[1]);
            else if (yType == Date.class) cfg.item(((Date)arr[1]).getTime());

            cfg.item((Integer)arr[2]);

            if (arr[3] != null)
                cfg.item((String)arr[3]);

            cfg.endArray();
        }

        cfg.endArray();
        return cfg;
    }

    private Class getObjType(Object o) {
        if (o instanceof Number) return Number.class;
        else if (o instanceof Date) return Date.class;
        else if (o instanceof String) return String.class;
        else throw new IllegalArgumentException("ace:chart - Bubble series coordinates can only be supplied as Date, Number or String.");
    }

    /**
     * Used by the ChartRenderer to produce a JSON representation of the data of this series.
     * @return the JSON object
     * @param component
     */
    @Override
    public JSONBuilder getConfigJSON(UIComponent component) {
        JSONBuilder cfg = super.getConfigJSON(component);

        if (type != null) {
            if (type.equals(BubbleType.BUBBLE))
                cfg.entry("renderer", "ice.ace.jq.jqplot.BubbleRenderer", true);
        }

        if (hasRenderOptionsSet()) {
            cfg.beginMap("rendererOptions");
            if (bubbleGradients != null)
                cfg.entry("bubbleGradients", bubbleGradients);

            if (bubbleAlpha != null)
                cfg.entry("bubbleAlpha",
                        bubbleAlpha.doubleValue() / 100d);

            if (highlightAlpha != null)
                cfg.entry("highlightAlpha",
                        highlightAlpha.doubleValue() / 100d);

            if (showLabels != null)
                cfg.entry("showLabels", showLabels);

            if (varyBubbleColors != null)
                cfg.entry("varyBubbleColors", varyBubbleColors);

            if (escapeHtml != null)
                cfg.entry("escapeHtml", escapeHtml);

            if (autoscaleBubbles != null)
                cfg.entry("autoscaleBubbles", autoscaleBubbles);

            if (autoscaleMultiplier != null)
                cfg.entry("autoscaleMultiplier", autoscaleMultiplier);

            if (autoscalePointsFactor != null)
                cfg.entry("autoscalePointsFactor", autoscalePointsFactor);

            if (highlightMouseDown != null)
                cfg.entry("highlightMouseDown", highlightMouseDown);

            if (highlightMouseOver != null)
                cfg.entry("highlightMouseOver", highlightMouseOver);

            if (highlightColors != null) {
                cfg.beginArray("highlightColors");
                for (String c : highlightColors) cfg.item(c);
                cfg.endArray();
            }

            cfg.endMap();
        }

        cfg.endMap();
        return cfg;
    }

    private boolean hasRenderOptionsSet() {
        return bubbleGradients != null || bubbleAlpha != null
                || highlightAlpha != null || showLabels != null
                || varyBubbleColors != null || escapeHtml != null
                || autoscaleBubbles != null || autoscaleMultiplier != null
                || autoscalePointsFactor != null || highlightMouseDown != null
                || highlightMouseOver != null || highlightColors != null;
    }

    @Override
    public ChartType getDefaultType() {
        return BubbleType.BUBBLE;
    }

    public Boolean isBubbleGradients() {
        return bubbleGradients;
    }

    public void setBubbleGradients(Boolean bubbleGradients) {
        this.bubbleGradients = bubbleGradients;
    }

    public Integer getBubbleAlpha() {
        return bubbleAlpha;
    }

    public void setBubbleAlpha(Integer bubbleAlpha) {
        this.bubbleAlpha = bubbleAlpha;
    }

    public Boolean getShowLabels() {
        return showLabels;
    }

    public void setShowLabels(Boolean showLabels) {
        this.showLabels = showLabels;
    }

    public Integer getHighlightAlpha() {
        return highlightAlpha;
    }

    public void setHighlightAlpha(Integer highlightAlpha) {
        this.highlightAlpha = highlightAlpha;
    }

    public Boolean getVaryBubbleColors() {
        return varyBubbleColors;
    }

    public void setVaryBubbleColors(Boolean varyBubbleColors) {
        this.varyBubbleColors = varyBubbleColors;
    }

    public Boolean getEscapeHtml() {
        return escapeHtml;
    }

    public void setEscapeHtml(Boolean escapeHtml) {
        this.escapeHtml = escapeHtml;
    }

    public Boolean getAutoscaleBubbles() {
        return autoscaleBubbles;
    }

    public void setAutoscaleBubbles(Boolean autoscaleBubbles) {
        this.autoscaleBubbles = autoscaleBubbles;
    }

    public Double getAutoscaleMultiplier() {
        return autoscaleMultiplier;
    }

    public void setAutoscaleMultiplier(Double autoscaleMultiplier) {
        this.autoscaleMultiplier = autoscaleMultiplier;
    }

    public Double getAutoscalePointsFactor() {
        return autoscalePointsFactor;
    }

    public void setAutoscalePointsFactor(Double autoscalePointsFactor) {
        this.autoscalePointsFactor = autoscalePointsFactor;
    }

    public Boolean getHighlightMouseOver() {
        return highlightMouseOver;
    }

    public void setHighlightMouseOver(Boolean highlightMouseOver) {
        this.highlightMouseOver = highlightMouseOver;
    }

    public Boolean getHighlightMouseDown() {
        return highlightMouseDown;
    }

    public void setHighlightMouseDown(Boolean highlightMouseDown) {
        this.highlightMouseDown = highlightMouseDown;
    }

    public String[] getHighlightColors() {
        return highlightColors;
    }

    public void setHighlightColors(String[] highlightColors) {
        this.highlightColors = highlightColors;
    }
}
