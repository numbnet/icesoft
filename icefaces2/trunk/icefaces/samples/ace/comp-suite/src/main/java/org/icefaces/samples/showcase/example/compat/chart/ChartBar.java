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
        title = "example.compat.chart.bar.title",
        description = "example.compat.chart.bar.description",
        example = "/resources/examples/compat/chart/chartBar.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="chartBar.xhtml",
                    resource = "/resources/examples/compat/"+
                               "chart/chartBar.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ChartBar.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/chart/ChartBar.java")
        }
)
@ManagedBean(name= ChartBar.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ChartBar extends ComponentExampleImpl<ChartBar> implements Serializable {
	
	public static final String BEAN_NAME = "chartBar";
	
	private ChartModelAxial model = new ChartModelAxial("Bar", true, true, true, true);
	
	public ChartBar() {
		super(ChartBar.class);
	}
	
	public String getType() { return OutputChart.BAR_CHART_TYPE; }
	public ChartModelAxial getModel() { return model; }
	
	public void setModel(ChartModelAxial model) { this.model = model; }
}
