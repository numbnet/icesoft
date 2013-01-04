package org.icefaces.samples.showcase.example.ace.chart;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
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
 * Date: 2012-10-09
 * Time: 3:06 PM
 */

@ComponentExample(
        parent = ChartBean.BEAN_NAME,
        title = "example.ace.chart.export.title",
        description = "example.ace.chart.export.description",
        example = "/resources/examples/ace/chart/chartExport.xhtml"
)
@ExampleResources(
        resources ={
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title="ChartExport.xhtml",
                        resource = "/resources/examples/ace/chart/chartExport.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title="ChartExportBean.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/chart/ChartExportBean.java"),
                @ExampleResource(type = ResourceType.java,
                    title="ChartCombinedBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/chart/ChartCombinedBean.java")
        }
)
@ManagedBean(name= ChartExportBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ChartExportBean extends ComponentExampleImpl<ChartExportBean> implements Serializable {
    public static final String BEAN_NAME = "chartExportBean";

    public ChartExportBean() {
        super(ChartExportBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}
