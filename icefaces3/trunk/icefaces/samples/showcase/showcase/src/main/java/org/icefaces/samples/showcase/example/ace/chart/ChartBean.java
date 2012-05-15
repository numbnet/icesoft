package org.icefaces.samples.showcase.example.ace.chart;

import org.icefaces.ace.component.chart.Axis;
import org.icefaces.ace.component.chart.AxisType;
import org.icefaces.ace.event.SelectEvent;
import org.icefaces.ace.event.SeriesSelectionEvent;
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

    private Integer[][] tableData = new Integer[][] {
            {2,3,1,4},
            {5,6,8,7},
            {10,9,12,11},
    };

    private List<CartesianSeries> lineData = new ArrayList<CartesianSeries>() {{
        add(new CartesianSeries() {{
            setType(CartesianType.LINE);
            setShowMarker(false);
            setLabel("Bar Data");
        }});
    }};

    private List<CartesianSeries> barData = new ArrayList<CartesianSeries>() {{
        add(new CartesianSeries() {{
            setType(CartesianType.BAR);
            add("HDTV Receiver", 15);
            add("Cup Holder Pinion Bob", 7);
            add("Generic Fog Lamp", 9);
            add("8 Track Control Module", 12);
            add("Sludge Pump Fourier Modulator", 3);
            add("Transcender/Spice Rack", 6);
            add("Hair Spray Danger Indicator", 18);
            setLabel("Product / Sales");
        }});

        add(new CartesianSeries() {{
            add("Nickle", 28);
            add("Aluminum", 13);
            add("Xenon", 54);
            add("Silver", 47);
            add("Sulfer", 16);
            add("Silicon", 14);
            add("Vanadium", 23);
            setLabel("Resources / Demand");
            setYAxis(2);
            setXAxis(2);
        }});
    }};

    private Axis tableDemoAxis = new Axis() {{
        setType(AxisType.CATEGORY);
        setLabel("Letter Axis");
        setTicks(new String[]{"A","B","C","D"});
    }};

    private Axis barDemoDefaultAxis = new Axis() {{
        setTickAngle(-30);
    }};

    private Axis barDemoX1Axis = new Axis() {{
        setType(AxisType.CATEGORY);
    }};

    private Axis[] barDemoYAxes = new Axis[] {
        new Axis() {{
            setAutoscale(true);
            setLabel("USD Millions");
        }},
        new Axis() {{
            setAutoscale(true);
            setLabel("Tonnes");
        }}
    };

    private Axis barDemoX2Axis = new Axis() {{
        setType(AxisType.CATEGORY);
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

    public void selectionListener(SeriesSelectionEvent s) {
        System.out.println(s.getSeriesIndex());
        System.out.println(s.getPointIndex());
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

    public Axis getTableDemoAxis() {
        return tableDemoAxis;
    }

    public void setTableDemoAxis(Axis tableDemoAxis) {
        this.tableDemoAxis = tableDemoAxis;
    }

    public Integer[][] getTableData() {
        return tableData;
    }

    public void setTableData(Integer[][] data) {
        this.tableData = data;
    }

    public Axis getBarDemoX1Axis() {
        return barDemoX1Axis;
    }

    public void setBarDemoX1Axis(Axis barDemoX1Axis) {
        this.barDemoX1Axis = barDemoX1Axis;
    }

    public Axis getBarDemoDefaultAxis() {
        return barDemoDefaultAxis;
    }

    public void setBarDemoDefaultAxis(Axis barDemoDefaultAxis) {
        this.barDemoDefaultAxis = barDemoDefaultAxis;
    }

    public Axis getBarDemoX2Axis() {
        return barDemoX2Axis;
    }

    public void setBarDemoX2Axis(Axis barDemoX2Axis) {
        this.barDemoX2Axis = barDemoX2Axis;
    }

    public Axis[] getBarDemoYAxes() {
        return barDemoYAxes;
    }

    public void setBarDemoYAxes(Axis[] barDemoYAxes) {
        this.barDemoYAxes = barDemoYAxes;
    }
}
