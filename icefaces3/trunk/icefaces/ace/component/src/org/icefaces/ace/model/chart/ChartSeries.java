package org.icefaces.ace.model.chart;

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
    interface ChartType {}

    List<Object> data;

    // Bar, line, etc.
    ChartType type;
    String label;


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Object> getData() {
        if (data == null)
            data = new ArrayList<Object>();

        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public void clear() {
        this.data = null;
    }

    public ChartType getType() {
        return type;
    }

    public void setType(ChartType type) {
        this.type = type;
    }

    abstract public String getDataJSON();
    abstract public String getConfigJSON();
}
