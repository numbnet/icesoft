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
        title = "example.compat.chart.custom.title",
        description = "example.compat.chart.custom.description",
        example = "/resources/examples/compat/chart/chartCustom.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="chartCustom.xhtml",
                    resource = "/resources/examples/compat/"+
                               "chart/chartCustom.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ChartCustom.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/chart/ChartCustom.java")
        }
)
@ManagedBean(name= ChartCustom.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ChartCustom extends ComponentExampleImpl<ChartCustom> implements Serializable {
	
	public static final String BEAN_NAME = "chartCustom";
	
	private ChartModelCustom model = new ChartModelCustom(true, false, false, false);
	
	public ChartCustom() {
		super(ChartCustom.class);
	}
	
	public String getType() { return OutputChart.CUSTOM_CHART_TYPE; }
	public ChartModelCustom getModel() { return model; }
	
	public void setModel(ChartModelCustom model) { this.model = model; }
}
