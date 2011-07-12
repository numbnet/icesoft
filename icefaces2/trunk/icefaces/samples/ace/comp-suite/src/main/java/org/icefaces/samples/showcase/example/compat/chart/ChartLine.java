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
        title = "example.compat.chart.line.title",
        description = "example.compat.chart.line.description",
        example = "/resources/examples/compat/chart/chartLine.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="chartLine.xhtml",
                    resource = "/resources/examples/compat/"+
                               "chart/chartLine.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ChartLine.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/chart/ChartLine.java")
        }
)
@ManagedBean(name= ChartLine.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ChartLine extends ComponentExampleImpl<ChartLine> implements Serializable {
	
	public static final String BEAN_NAME = "chartLine";
	
	private ChartModelAxial model = new ChartModelAxial("Line", true, false, true, true);
	
	public ChartLine() {
		super(ChartLine.class);
	}
	
	public String getType() { return OutputChart.LINE_CHART_TYPE; }
	public ChartModelAxial getModel() { return model; }
	
	public void setModel(ChartModelAxial model) { this.model = model; }
}
