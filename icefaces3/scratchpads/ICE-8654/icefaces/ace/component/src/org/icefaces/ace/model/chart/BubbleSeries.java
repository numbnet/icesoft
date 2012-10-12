package org.icefaces.ace.model.chart;

import org.icefaces.ace.model.SimpleEntry;
import org.icefaces.ace.util.JSONBuilder;

import javax.faces.component.UIComponent;

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
    public static enum BubbleType implements ChartType {
        BUBBLE
    }

    public BubbleSeries() {}

    public void add(String x, String y, String magnitude) {
        getData().add(new SimpleEntry<String, String[]>(x, new String[]{y, magnitude}));
    }

    @Override
    public JSONBuilder getDataJSON(UIComponent chart) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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

        cfg.endMap();
        return cfg;
    }

    @Override
    public ChartType getDefaultType() {
        return BubbleType.BUBBLE;
    }
}
