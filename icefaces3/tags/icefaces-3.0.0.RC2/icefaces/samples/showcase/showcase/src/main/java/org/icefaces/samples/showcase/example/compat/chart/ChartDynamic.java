/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.samples.showcase.example.compat.chart;

import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import com.icesoft.faces.component.outputchart.OutputChart;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = ChartBean.BEAN_NAME,
        title = "example.compat.chart.dynamic.title",
        description = "example.compat.chart.dynamic.description",
        example = "/resources/examples/compat/chart/chartDynamic.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="chartDynamic.xhtml",
                    resource = "/resources/examples/compat/"+
                               "chart/chartDynamic.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ChartDynamic.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/chart/ChartDynamic.java")
        }
)
@ManagedBean(name= ChartDynamic.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ChartDynamic extends ComponentExampleImpl<ChartDynamic> implements Serializable {
	
	public static final String BEAN_NAME = "chartDynamic";
	
	private ChartModelAxial model = new ChartModelAxial("Dynamic Bar", true, true, true, true);
	
	public ChartDynamic() {
		super(ChartDynamic.class);
	}
	
	public String getType() { return OutputChart.BAR_CHART_TYPE; }
	public ChartModelAxial getModel() { return model; }
	
	public void setModel(ChartModelAxial model) { this.model = model; }
	
	public void addData(ActionEvent event) {
	    model.addData();
	}
	
	public void removeData(ActionEvent event) {
	    model.removeData();
	}
	    
    public void defaultData(ActionEvent event) {
        model.resetData();
    }
}
