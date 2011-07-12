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
        title = "example.compat.chart.areaStacked.title",
        description = "example.compat.chart.areaStacked.description",
        example = "/resources/examples/compat/chart/chartAreaStacked.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="chartAreaStacked.xhtml",
                    resource = "/resources/examples/compat/"+
                               "chart/chartAreaStacked.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ChartAreaStacked.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/chart/ChartAreaStacked.java")
        }
)
@ManagedBean(name= ChartAreaStacked.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ChartAreaStacked extends ComponentExampleImpl<ChartAreaStacked> implements Serializable {
	
	public static final String BEAN_NAME = "chartAreaStacked";
	
	private ChartModelAxial model = new ChartModelAxial("Area Stacked", false, false, true, true);
	
	public ChartAreaStacked() {
		super(ChartAreaStacked.class);
	}
	
	public String getType() { return OutputChart.AREA_STACKED_CHART_TYPE; }
	public ChartModelAxial getModel() { return model; }
	
	public void setModel(ChartModelAxial model) { this.model = model; }
}
