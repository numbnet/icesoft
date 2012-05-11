package org.icefaces.ace.component.chart;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.model.chart.ChartSeries;
import org.icefaces.ace.meta.annotation.*;

import javax.el.MethodExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import java.util.List;

@Component(
        tagName = "chart",
        componentClass  = "org.icefaces.ace.component.chart.Chart",
        generatedClass  = "org.icefaces.ace.component.chart.ChartBase",
        extendsClass    = "javax.faces.component.UIComponentBase",
        componentType   = "org.icefaces.ace.component.Chart",
        rendererType    = "org.icefaces.ace.component.ChartRenderer",
        rendererClass   = "org.icefaces.ace.component.chart.ChartRenderer",
        componentFamily = "org.icefaces.ace.Chart",
        tlddoc =
        ""
)
@ResourceDependencies({
        @ResourceDependency(library = "icefaces.ace", name = "util/ace-jquery.js"),
        @ResourceDependency(library = "icefaces.ace", name = "chart/ace-chart.js"),
        @ResourceDependency(library = "icefaces.ace", name = "chart/jquery.jqplot.css")
})
public class ChartMeta {
    @Property(tlddoc = 
                "Define a collection of ChartSeries object to draw on this plot.")
    private List<ChartSeries> value;

    @Property(tlddoc = 
                "Define a ChartSeries whose configuration is used where other ChartSeries " +
                "have not made an explicit configuration. The data of this ChartSeries is irrelevant.")
    private ChartSeries defaultSeriesConfig;

    @Property(tlddoc = 
                "Enabling displays the legend.")
    private Boolean legend;
    @Property(tlddoc = 
                "Defines the location of legend relative to the bounds of the chart. " +
                "All of the cardinal directions are available in the following format: " +
                "N, NE, E, SE, S, etc.")
    private LegendLocation legendLocation;
    @Property(tlddoc = 
                "Defines the placement of the legend relative to the content of the " +
                "chart. The available configurations are: INSIDE_GRID, OUTSIDE_GRID " +
                "and OUTSIDE")
    private LegendPlacement legendPlacement;

    @Property(tlddoc =
                "Enables a stack or \"mountain\" plot.  Not all types of series " +
                "may support this mode.",
        defaultValue = "false", defaultValueType = DefaultValueType.EXPRESSION)
    private Boolean stackSeries;

    // Unimplemented
//    @Property(tlddoc = "")
//    private Boolean trapRightClick;

    @Property (tlddoc =
                "Enables the draw animation behaviour of the chart. By default " +
                "is enabled for all browsers but IE8 and lower for performance" +
                "concerns.")
    private Boolean animated;

    @Property(tlddoc =
                "Define a Axis whose configuration is used where other axes have " +
                "not made an explicit configuration.")
    private Axis defaultAxesConfig;

    @Property(tlddoc =
                "Defines the configuration of the x axis. Attempts are made to " +
                "interpret a configuration if undefined.")
    private Axis xAxis;

    @Property(tlddoc =
                "Defines the configuration of the x1 axis. Attempts are made to " +
                "interpret a configuration if undefined.")
    private Axis x2Axis;

    @Property(tlddoc =
                "Defines the configuration of the y axes (up to 9). Attempts are made to " +
                "interpret a configuration if undefined.")
    private List<Axis> yAxes;


    @Property(expression = Expression.METHOD_EXPRESSION,
            methodExpressionArgument = "org.icefaces.ace.event.SeriesSelectionEvent",
            tlddoc = "MethodExpression reference called whenever a series " +
                    "element is selected. The method receives a single " +
                    "argument, SeriesSelectionEvent.")
    private MethodExpression selectListener;
}
