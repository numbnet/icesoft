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
 * Time: 12:58 PM
 */
public class BubbleSeries extends ChartSeries {
    public static enum BubbleType implements ChartType {
        BUBBLE
    }

    public BubbleSeries() {
        setType(BubbleType.BUBBLE);
    }

    public void add(String x, String y, String magnitude) {
        getData().add(new SimpleEntry<String, String[]>(x, new String[]{y, magnitude}));
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
