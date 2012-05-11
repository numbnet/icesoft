package org.icefaces.ace.component.chart;

import org.icefaces.ace.util.JSONBuilder;

import java.io.Serializable;

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
 * Date: 12-05-07
 * Time: 10:44 AM
 */
public class Axis implements Serializable {
//    // Unimplemented
//    private boolean forceTickAt0;
//    // Unimplemented
//    private boolean forceTickAt100;
//
//    // Unimplemented
//    private boolean show;
//
//    // Unimplemented
//    private double min;
//    // Unimplemented
//    private double max;
//
//    // Unimplemented
//    private double pad;

    private boolean sortMergedLabels;
    private String[] ticks;
    private AxisType type;

    /**
     * Used by the ChartRenderer to produce a JSON representation of the configuration of this axis.
     * @return the JSON configuration object
     */
    @Override
    public String toString() {
        JSONBuilder json = JSONBuilder.create();

        json.beginMap();
        if (type != null)
            json.entry("renderer", type.toString(), true);
        if (hasRendererOptionsSet()) {
            json.beginMap("rendererOptions");
            if (type == AxisType.CATEGORY && sortMergedLabels)
                json.entry("sortMergedLabels", true);
            json.endMap();
        }

        if (ticks != null)
            encodeTicks(json);
        json.endMap();

        return json.toString();
    }

    private boolean hasRendererOptionsSet() {
        if (type == AxisType.CATEGORY && sortMergedLabels)
            return true;
        return false;
    }

    private void encodeTicks(JSONBuilder json) {
        json.beginArray("ticks");
        Class tickType = null;
        for (Object tick : ticks) {
            if (tickType == null) {
                if (tick instanceof Number) tickType = Number.class;
                else if (tick instanceof String) tickType = String.class;
            }

            if (tickType == Number.class)
                json.item(((Number)tick).doubleValue());
            else if (tickType == String.class) {
                json.item(tick.toString());
            }
        }
        json.endArray();
    }

//    public boolean isForceTickAt0() {
//        return forceTickAt0;
//    }
//
//    public void setForceTickAt0(boolean forceTickAt0) {
//        this.forceTickAt0 = forceTickAt0;
//    }
//
//    public boolean isForceTickAt100() {
//        return forceTickAt100;
//    }
//
//    public void setForceTickAt100(boolean forceTickAt100) {
//        this.forceTickAt100 = forceTickAt100;
//    }
//
//    public boolean isShow() {
//        return show;
//    }
//
//    public void setShow(boolean show) {
//        this.show = show;
//    }
//
//    public double getMin() {
//        return min;
//    }
//
//    public void setMin(double min) {
//        this.min = min;
//    }
//
//    public double getMax() {
//        return max;
//    }
//
//    public void setMax(double max) {
//        this.max = max;
//    }
//
//    public double getPad() {
//        return pad;
//    }
//
//    public void setPad(double pad) {
//        this.pad = pad;
//    }

    /**
     *
     * @return
     */
    public AxisType getType() {
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(AxisType type) {
        this.type = type;
    }

    /**
     *
     * @return
     */
    public String[] getTicks() {
        return ticks;
    }

    /**
     *
     * @param ticks
     */
    public void setTicks(String[] ticks) {
        this.ticks = ticks;
    }

    /**
     *
     * @return
     */
    public boolean isSortMergedLabels() {
        return sortMergedLabels;
    }

    /**
     *
     * @param sortMergedLabels
     */
    public void setSortMergedLabels(boolean sortMergedLabels) {
        this.sortMergedLabels = sortMergedLabels;
    }
}
