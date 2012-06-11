package org.icefaces.ace.model.chart;

import org.icefaces.ace.model.SimpleEntry;

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
 * Time: 1:29 PM
 */
public class ChartSector extends ChartSeries {
    public static enum SectorType implements ChartType {
        PIE, DONUT
    }

    int diameter; // pixels
    int padding;
    int sliceMargin; // degrees
    boolean fill;
    boolean shadow;
    int shadowAngle; // degrees
    int shadowOffset; // pixels
    int shadowDepth; // number of shadow strokes, each 1 shadow offset from the last
    int shadowAlpha; // 0 - 100 alpha
    boolean highlightMouseOver;
    boolean highlightMouseDown;
    String[] highlightColors;
    String dataLabels; // 'label','value','percent' or a comma separated list of labels
    boolean showDataLabels;
    String dataLabelFormatString;
    int dataLabelPositionFactor; // 0 - 100 - multiplier of label position radius
    int dataLabelThreshold;
    int dataLabelNudge; // + or - pixels away from the center of the pie
    boolean dataLabelCenter;
    int startAngle;

    public ChartSector() {
        setType(SectorType.PIE);
    }

    public void add(String key, String val) {
        getData().add(new SimpleEntry<String, String>(key, val));
    }

    @Override
    public String getDataJSON() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getConfigJSON() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
