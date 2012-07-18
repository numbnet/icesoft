/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.samples.showcase.example.ace.chart;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        parent = ChartBean.BEAN_NAME,
        title = "example.ace.chart.candlestick.title",
        description = "example.ace.chart.candlestick.description",
        example = "/resources/examples/ace/chart/chartCandlestick.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="ChartCandlestick.xhtml",
                    resource = "/resources/examples/ace/chart/chartCandlestick.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ChartCandlestickBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/chart/ChartCandlestickBean.java")
        }
)
@ManagedBean(name= ChartCandlestickBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ChartCandlestickBean extends ComponentExampleImpl<ChartCandlestickBean> implements Serializable
{
    public static final String BEAN_NAME = "chartCandlestickBean";
    
    public ChartCandlestickBean() {
        super(ChartCandlestickBean.class);
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}
