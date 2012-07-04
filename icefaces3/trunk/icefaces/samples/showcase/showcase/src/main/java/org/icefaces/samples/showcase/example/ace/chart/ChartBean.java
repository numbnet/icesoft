package org.icefaces.samples.showcase.example.ace.chart;

import org.icefaces.ace.component.chart.Axis;
import org.icefaces.ace.component.chart.AxisType;
import org.icefaces.ace.event.PointValueChangeEvent;
import org.icefaces.ace.event.SelectEvent;
import org.icefaces.ace.event.SeriesSelectionEvent;
import org.icefaces.ace.event.UnselectEvent;
import org.icefaces.ace.model.chart.CartesianSeries;
import org.icefaces.ace.model.chart.DragConstraintAxis;
import org.icefaces.ace.model.chart.OHLCSeries;
import org.icefaces.ace.model.chart.SectorSeries;
import org.icefaces.ace.model.table.RowStateMap;
import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;
import org.icefaces.util.JavaScriptRunner;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;
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


    private List<CartesianSeries> dateLineData = new ArrayList<CartesianSeries>() {{
        add(new CartesianSeries(){{
            setDragable(true);
            add(new GregorianCalendar(2009,1,2).getTime(),  89.1);

//            add(new GregorianCalendar(2009,5,8).getTime(),  143.82);
//            add(new GregorianCalendar(2009,5,1).getTime(),  136.47);
//            add(new GregorianCalendar(2009,4,26).getTime(), 124.76);
//            add(new GregorianCalendar(2009,4,18).getTime(), 123.73);
//            add(new GregorianCalendar(2009,4,18).getTime(), 123.73);
//            add(new GregorianCalendar(2009,4,11).getTime(), 127.37);
//            add(new GregorianCalendar(2009,4,4).getTime(),  128.24);
//            add(new GregorianCalendar(2009,3,27).getTime(), 122.9);
//            add(new GregorianCalendar(2009,3,20).getTime(), 121.73);
            add(new GregorianCalendar(2009,3,13).getTime(), 120.01);
//            add(new GregorianCalendar(2009,3,6).getTime(),  114.94);
//            add(new GregorianCalendar(2009,2,30).getTime(), 104.51);
//            add(new GregorianCalendar(2009,2,23).getTime(), 102.71);
//            add(new GregorianCalendar(2009,2,16).getTime(), 96.53);
//            add(new GregorianCalendar(2009,2,9).getTime(),  84.18);
//            add(new GregorianCalendar(2009,2,2).getTime(),  88.12);
//            add(new GregorianCalendar(2009,1,23).getTime(), 91.65);
//            add(new GregorianCalendar(2009,1,17).getTime(), 96.87);
//            add(new GregorianCalendar(2009,1,9).getTime(),  100);
            add(new GregorianCalendar(2009,5,15).getTime(), 136.01);

        }});
    }};

    private List<OHLCSeries> ohlcData = new ArrayList<OHLCSeries>() {{
        add(new OHLCSeries(){{
            setType(OHLCType.CANDLESTICK);
            add(new GregorianCalendar(2009,5,15).getTime(), 136.01, 139.5,  134.53, 139.48);
            add(new GregorianCalendar(2009,5,8).getTime(),  143.82, 144.56, 136.04, 136.97);
            add(new GregorianCalendar(2009,5,1).getTime(),  136.47, 146.4,  136,    144.67);
            add(new GregorianCalendar(2009,4,26).getTime(), 124.76, 135.9,  124.55, 135.81);
            add(new GregorianCalendar(2009,4,18).getTime(), 123.73, 129.31, 121.57, 122.5);
            add(new GregorianCalendar(2009,4,18).getTime(), 123.73, 129.31, 121.57, 122.5);
            add(new GregorianCalendar(2009,4,11).getTime(), 127.37, 130.96, 119.38, 122.42);
            add(new GregorianCalendar(2009,4,4).getTime(),  128.24, 133.5,  126.26, 129.19);
            add(new GregorianCalendar(2009,3,27).getTime(), 122.9,  127.95, 122.66, 127.24);
            add(new GregorianCalendar(2009,3,20).getTime(), 121.73, 127.2,  118.6,  123.9);
            add(new GregorianCalendar(2009,3,13).getTime(), 120.01, 124.25, 115.76, 123.42);
            add(new GregorianCalendar(2009,3,6).getTime(),  114.94, 120,    113.28, 119.57);
            add(new GregorianCalendar(2009,2,30).getTime(), 104.51, 116.13, 102.61, 115.99);
            add(new GregorianCalendar(2009,2,23).getTime(), 102.71, 109.98, 101.75, 106.85);
            add(new GregorianCalendar(2009,2,16).getTime(), 96.53,  103.48, 94.18,  101.59);
            add(new GregorianCalendar(2009,2,9).getTime(),  84.18,  97.2,   82.57,  95.93);
            add(new GregorianCalendar(2009,2,2).getTime(),  88.12,  92.77,  82.33,  85.3);
            add(new GregorianCalendar(2009,1,23).getTime(), 91.65,  92.92,  86.51,  89.31);
            add(new GregorianCalendar(2009,1,17).getTime(), 96.87,  97.04,  89,     91.2);
            add(new GregorianCalendar(2009,1,9).getTime(),  100,    103,    95.77,  99.16);
            add(new GregorianCalendar(2009,1,2).getTime(),  89.1,   100,    88.9,   99.72);
            add(new GregorianCalendar(2009,0,26).getTime(), 88.86,  95,     88.3,   90.13);
            add(new GregorianCalendar(2009,0,20).getTime(), 81.93,  90,     78.2,   88.36);
            add(new GregorianCalendar(2009,0,12).getTime(), 90.46,  90.99,  80.05,  82.33);
            add(new GregorianCalendar(2009,0,5).getTime(),  93.17,  97.17,  90.04,  90.58);
            add(new GregorianCalendar(2008,11,29).getTime(),86.52,  91.04,  84.72,  90.75);
            add(new GregorianCalendar(2008,11,22).getTime(),90.02,  90.03,  84.55,  85.81);
            add(new GregorianCalendar(2008,11,15).getTime(),95.99,  96.48,  88.02,  90);
            add(new GregorianCalendar(2008,11,8).getTime(), 97.28,  103.6,  92.53,  98.27);
            add(new GregorianCalendar(2008,11,1).getTime(), 91.3,   96.23,  86.5,   94);
            add(new GregorianCalendar(2008,10,24).getTime(),85.21,  95.25,  84.84,  92.67);
            add(new GregorianCalendar(2008,10,17).getTime(),88.48,  91.58,  79.14,  82.58);
            add(new GregorianCalendar(2008,10,10).getTime(),100.17, 100.4,  86.02,  90.24);
            add(new GregorianCalendar(2008,10,3).getTime(), 105.93, 111.79, 95.72,  98.24);
            add(new GregorianCalendar(2008,9,27).getTime(), 95.07,  112.19, 91.86,  107.59);
            add(new GregorianCalendar(2008,9,20).getTime(), 99.78,  101.25, 90.11,  96.38);
            add(new GregorianCalendar(2008,9,13).getTime(), 104.55, 116.4,  85.89,  97.4);
            add(new GregorianCalendar(2008,9,6).getTime(),  91.96,  101.5,  85,     96.8);
            add(new GregorianCalendar(2008,8,29).getTime(), 119.62, 119.68, 94.65,  97.07);
            add(new GregorianCalendar(2008,8,22).getTime(), 139.94, 140.25, 123,    128.24);
            add(new GregorianCalendar(2008,8,15).getTime(), 142.03, 147.69, 120.68, 140.91);
        }});
    }};

    private String formatString = "<table class=\"jqplot-highlighter\"><tr><td>date:</td><td>%s</td></tr> \n" +
            "<tr><td>open:</td><td>%s</td></tr>\n" +
            "<tr><td>hi:</td><td>%s</td></tr>\n" +
            "<tr><td>low:</td><td>%s</td></tr> \n" +
            "<tr><td>close:</td><td>%s</td></tr></table>";

    private Axis ohlcXAxis = new Axis() {{
        setType(AxisType.DATE);
        setFormatString("%b %e");
        setMin("09-01-2008");
        setMax("06-22-2009");
        setTickInterval("6 weeks");
    }};

    private Axis[] ohlcYAxes = new Axis[] {
       new Axis() {{ setTickPrefix("$"); }}
    };

    private Integer[][] tableData = new Integer[][] {
            {2,3,1,4},
            {5,6,8,7},
            {10,9,12,11},
    };

    private List<CartesianSeries> lineData = new ArrayList<CartesianSeries>() {{
        add(new CartesianSeries() {{
            setType(CartesianType.LINE);
            setLabel("Plot Data");
            setDragable(true);
            setDragConstraintAxis(DragConstraintAxis.Y);
        }});
    }};

    private RowStateMap rowStateMap;

    public void pointChange(PointValueChangeEvent event) {
        JavaScriptRunner.runScript(FacesContext.getCurrentInstance(),
                "ice.ace.jq('.ui-datatable-data tr.ui-selected td:eq("+event.getPointIndex()+")').effect('pulsate', {}, 500);");

        for (Object o : rowStateMap.getSelected()) {
            Integer[] values = (Integer[]) o;
            values[event.getPointIndex()] = ((Double)((Object[])event.getNewValue())[1]).intValue();
        }
    }

    private List<CartesianSeries> barData = new ArrayList<CartesianSeries>() {{
        add(new CartesianSeries() {{
            add("Nickle", 28);
            add("Aluminum", 13);
            add("Xenon", 54);
            add("Silver", 47);
            add("Sulfer", 16);
            add("Silicon", 14);
            add("Vanadium", 23);
            setDragable(true);
            setLabel("Resources / Demand");
            setYAxis(2);
            setXAxis(2);
        }});

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
    }};

    private List<CartesianSeries> dateSeries = new ArrayList<CartesianSeries>() {{
       add(new CartesianSeries() {{
           add(new GregorianCalendar(2008,9,30,16,0).getTime(),4);
           add(new GregorianCalendar(2008,10,30,16,0).getTime(),6.5);
           add(new GregorianCalendar(2008,11,30,16,0).getTime(),5.7);
       }});
    }};

    private List<SectorSeries> donutData = new ArrayList<SectorSeries>() {{
        add(new SectorSeries() {{
            add("a", 6);
            add("b", 8);
            add("c", 14);
            add("d", 20);
            setType(SectorType.DONUT);
        }});
        add(new SectorSeries() {{
            add("a", 8);
            add("b", 12);
            add("c", 6);
            add("d", 9);
        }});
    }};

    private Axis dateAxis = new Axis() {{
        setType(AxisType.DATE);
    }};

    private List<SectorSeries> pieData = new ArrayList<SectorSeries>() {{
        add(new SectorSeries() {{
            add("Heavy Industry", 12);
            add("Retail", 9);
            add("Light Industry", 14);
            add("Out of Home", 16);
            add("Commuting", 7);
            add("Orientation", 9);
            setShowDataLabels(true);
            //setDataLabels("value");
            setSliceMargin(4);
            setFill(false);
        }});
    }};

    private Axis tableDemoAxis = new Axis() {{
        setType(AxisType.CATEGORY);
        setLabel("Letter Axis");
        setTicks(new String[]{"A", "B", "C", "D"});
    }};

    private Axis barDemoDefaultAxis = new Axis() {{
        setTickAngle(-30);
    }};

    private Axis barDemoXOneAxis = new Axis() {{
        setType(AxisType.CATEGORY);
    }};

    private Axis[] barDemoYAxes = new Axis[] {
        new Axis() {{
            setAutoscale(true);
            setTickInterval("5");
            setLabel("USD Millions");
        }},
        new Axis() {{
            setAutoscale(true);
            setTickInterval("5");
            setLabel("Tonnes");
        }}
    };

    private Axis barDemoXTwoAxis = new Axis() {{
        setTicks(new String[] {"Nickle", "Aluminum", "Xenon", "Silver", "Sulfer", "Silicon", "Vanadium"});
        setType(AxisType.CATEGORY);
    }};

    public ChartBean() {
        super(ChartBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public void redrawChartListener(SelectEvent e) {
        CartesianSeries s = lineData.get(0);
        s.clear();
        Integer[] indicies = (Integer[]) e.getObject();
        s.add("A", indicies[0]);
        s.add("B", indicies[1]);
        s.add("C", indicies[2]);
        s.add("D", indicies[3]);
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

    public Axis getBarDemoXOneAxis() {
        return barDemoXOneAxis;
    }

    public void setBarDemoXOneAxis(Axis barDemoXOneAxis) {
        this.barDemoXOneAxis = barDemoXOneAxis;
    }

    public Axis getBarDemoDefaultAxis() {
        return barDemoDefaultAxis;
    }

    public void setBarDemoDefaultAxis(Axis barDemoDefaultAxis) {
        this.barDemoDefaultAxis = barDemoDefaultAxis;
    }

    public Axis getBarDemoXTwoAxis() {
        return barDemoXTwoAxis;
    }

    public void setBarDemoXTwoAxis(Axis barDemoXTwoAxis) {
        this.barDemoXTwoAxis = barDemoXTwoAxis;
    }

    public Axis[] getBarDemoYAxes() {
        return barDemoYAxes;
    }

    public void setBarDemoYAxes(Axis[] barDemoYAxes) {
        this.barDemoYAxes = barDemoYAxes;
    }

    public List<SectorSeries> getPieData() {
        return pieData;
    }

    public void setPieData(List<SectorSeries> pieData) {
        this.pieData = pieData;
    }

    public Axis getDateAxis() {
        return dateAxis;
    }

    public void setDateAxis(Axis dateAxis) {
        this.dateAxis = dateAxis;
    }

    public List<CartesianSeries> getDateSeries() {
        return dateSeries;
    }

    public void setDateSeries(List<CartesianSeries> dateSeries) {
        this.dateSeries = dateSeries;
    }

    public List<SectorSeries> getDonutData() {
        return donutData;
    }

    public void setDonutData(List<SectorSeries> donutData) {
        this.donutData = donutData;
    }

    public List<OHLCSeries> getOhlcData() {
        return ohlcData;
    }

    public void setOhlcData(List<OHLCSeries> ohlcData) {
        this.ohlcData = ohlcData;
    }

    public Axis getOhlcXAxis() {
        return ohlcXAxis;
    }

    public void setOhlcXAxis(Axis ohlcXAxis) {
        this.ohlcXAxis = ohlcXAxis;
    }

    public Axis[] getOhlcYAxes() {
        return ohlcYAxes;
    }

    public void setOhlcYAxes(Axis[] ohlcYAxes) {
        this.ohlcYAxes = ohlcYAxes;
    }

    public String getFormatString() {
        return formatString;
    }

    public void setFormatString(String formatString) {
        this.formatString = formatString;
    }

    public RowStateMap getRowStateMap() {
        return rowStateMap;
    }

    public void setRowStateMap(RowStateMap rowStateMap) {
        this.rowStateMap = rowStateMap;
    }

    public List<CartesianSeries> getDateLineData() {
        return dateLineData;
    }

    public void setDateLineData(List<CartesianSeries> dateLineData) {
        this.dateLineData = dateLineData;
    }
}
