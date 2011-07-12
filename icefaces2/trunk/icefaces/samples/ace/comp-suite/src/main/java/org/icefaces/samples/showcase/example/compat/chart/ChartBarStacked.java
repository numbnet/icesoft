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
        title = "example.compat.chart.barStacked.title",
        description = "example.compat.chart.barStacked.description",
        example = "/resources/examples/compat/chart/chartBarStacked.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="chartBarStacked.xhtml",
                    resource = "/resources/examples/compat/"+
                               "chart/chartBarStacked.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ChartBarStacked.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/chart/ChartBarStacked.java")
        }
)
@ManagedBean(name= ChartBarStacked.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ChartBarStacked extends ComponentExampleImpl<ChartBarStacked> implements Serializable {
	
	public static final String BEAN_NAME = "chartBarStacked";
	
	private ChartModelAxial model = new ChartModelAxial("Bar Stacked", true, false, true, true, false);
	
	public ChartBarStacked() {
		super(ChartBarStacked.class);
	}
	
	public String getType() { return OutputChart.BAR_STACKED_CHART_TYPE; }
	public ChartModelAxial getModel() { return model; }
	
	public void setModel(ChartModelAxial model) { this.model = model; }
}
