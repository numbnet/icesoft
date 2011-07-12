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
        title = "example.compat.chart.area.title",
        description = "example.compat.chart.area.description",
        example = "/resources/examples/compat/chart/chartArea.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="chartArea.xhtml",
                    resource = "/resources/examples/compat/"+
                               "chart/chartArea.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ChartArea.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/chart/ChartArea.java")
        }
)
@ManagedBean(name= ChartArea.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ChartArea extends ComponentExampleImpl<ChartArea> implements Serializable {
	
	public static final String BEAN_NAME = "chartArea";
	
	private ChartModelAxial model = new ChartModelAxial("Area", false, false, true, true);
	
	public ChartArea() {
		super(ChartArea.class);
	}
	
	public String getType() { return OutputChart.AREA_CHART_TYPE; }
	public ChartModelAxial getModel() { return model; }
	
	public void setModel(ChartModelAxial model) { this.model = model; }
}
