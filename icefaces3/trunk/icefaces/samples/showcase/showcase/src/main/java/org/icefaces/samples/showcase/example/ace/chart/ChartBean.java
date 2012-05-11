package org.icefaces.samples.showcase.example.ace.chart;

import org.icefaces.ace.component.chart.Axis;
import org.icefaces.ace.component.chart.AxisType;
import org.icefaces.ace.event.SelectEvent;
import org.icefaces.ace.event.UnselectEvent;
import org.icefaces.ace.model.chart.CartesianSeries;
import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ComponentExample(
    title = "example.ace.chart.title",
    description = "example.ace.chart.description",
    example = "/resources/examples/ace/chart/chart.xhtml"
)
@ExampleResources(
    resources ={
        // xhtml
        @ExampleResource(type = ResourceType.xhtml,
                title="Chart.xhtml",
                resource = "/resources/examples/ace/"+
                        "chart/chart.xhtml"),
        // Java Source
        @ExampleResource(type = ResourceType.java,
                title="ChartBean.java",
                resource = "/WEB-INF/classes/org/icefaces/samples/"+
                        "showcase/example/ace/chart/ChartBean.java")
    }
)
@Menu(
    title = "menu.ace.chart.subMenu.title",
    menuLinks = {
        @MenuLink(title = "menu.ace.chart.subMenu.main",
                isDefault = true, exampleBeanName = ChartBean.BEAN_NAME)
    }
)

@ManagedBean(name= ChartBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ChartBean extends ComponentExampleImpl<ChartBean> implements Serializable {
    public static final String BEAN_NAME = "chartBean";

    private Integer[][] data = new Integer[][] {
            {1,2,3,4},
            {5,6,7,8},
            {9,10,11,12},
    };

    private List<CartesianSeries> lineData = new ArrayList<CartesianSeries>() {{
        add(new CartesianSeries() {{
            setType(CartesianType.BAR);
            setLabel("Bar Data");
        }});
    }};

    private List<CartesianSeries> barData = new ArrayList<CartesianSeries>() {{

    }};

    private Axis defaultAxis = new Axis() {{
        setType(AxisType.CATEGORY);
        setTicks(new String[]{"A","B","C","D"});
    }};

    public ChartBean() {
        super(ChartBean.class);
    }

    public void redrawChartListener(SelectEvent e) {
        CartesianSeries s = lineData.get(0);
        s.clear();
        for (Integer i : (Integer[]) e.getObject())
            s.add(i);
    }

    public void clearChartListener(UnselectEvent e) {
        CartesianSeries s = lineData.get(0);
        s.clear();
    }

    public List<CartesianSeries> getLineData() {
        return lineData;
    }

    public void setLineData(List<CartesianSeries> lineData) {
        this.lineData = lineData;
    }

    public List<CartesianSeries> getBarData() {
        return barData;
    }

    public void setBarData(List<CartesianSeries> barData) {
        this.barData = barData;
    }

    public Axis getDefaultAxis() {
        return defaultAxis;
    }

    public void setDefaultAxis(Axis defaultAxis) {
        this.defaultAxis = defaultAxis;
    }

    public Integer[][] getData() {
        return data;
    }

    public void setData(Integer[][] data) {
        this.data = data;
    }
}
