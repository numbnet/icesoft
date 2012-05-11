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
 * Time: 1:01 PM
 */
public class OHLCSeries extends ChartSeries {
    public static enum OHLCType implements ChartType {
        OHLC,
        CANDLESTICK;
    }

    boolean isHLC; // Ignores opening value if indicated.
    int tickLength; // pixels
    int bodyWidth;
    String openColor;
    String closeColor;
    String wickColor;
    String upBodyColor;
    String downBodyColor;
    boolean fillUpBody;
    boolean fillDownBody;

    public OHLCSeries() {
        setType(OHLCType.OHLC);
    }

    public void add(String x, String o, String h, String l, String c) {
        getData().add(new SimpleEntry<String, String[]>(x, new String[]{o, h, l, c}));
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
