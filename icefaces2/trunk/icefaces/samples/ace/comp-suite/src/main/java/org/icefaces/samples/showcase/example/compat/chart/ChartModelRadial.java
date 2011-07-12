package org.icefaces.samples.showcase.example.compat.chart;

import com.icesoft.faces.component.outputchart.OutputChart;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.io.Serializable;

/**
 * <p>The ChartModelAxial class purpose is to show how the outputChart component
 * can be used to display radia data.  It is very important that data and
 * label arrays are of the same length.</p>
 *
 * @see org.icefaces.application.showcase.view.bean.examples.component.outputChart.AbstractChartData
 * @since 1.7
 */
public class ChartModelRadial extends AbstractChartData implements Serializable {

    public ArrayList chartData;
    private Random randomizer = new Random(System.nanoTime());

    public ChartModelRadial(String chartTitle,
                            boolean areaMapEnabled,
                            boolean axisOrientationEnabled,
                            boolean enableDynamicValues,
                            boolean enabledLengendPosition) {
        this.chartTitle = chartTitle;
        this.areaMapEnabled = areaMapEnabled;
        this.axisOrientationEnabled = axisOrientationEnabled;
        this.enableDynamicValues = enableDynamicValues;
        this.enabledLengendPosition = enabledLengendPosition;
        init();
    }

    public int getChartType(){
        return RADIAL_CHART_TYPE;
    }

    /**
     * Initialze the default chart data. It is very important that the first
     * dimension of the arrays are equal in length.
     */
    private void init() {
        // build default data

        chartData = new ArrayList(Arrays.asList(
                new Double[]{new Double(50.0),
                             new Double(60.0),
                             new Double(80.0)}));
        // build x axis labels
        xAxisLabels = new ArrayList(
                Arrays.asList(new String[]{"2008", "2009", "2010"}));

        // build colors
        colors = new ArrayList(Arrays.asList(
                new Color[]{new Color(26, 86, 138),
                new Color(76, 126, 167),
                new Color(148, 179, 203)}));

        // build legend lables
        xAxisTitle = "Years";

        yAxisTitle = "Occurences";

        legendPlacement = "bottom";

        renderOnSubmit = true;
    }

    /**
     * Add new data to implementing classes data model.
     */
    public void addData() {
        // add some new data randomly.
        chartData.add(new Double(getRandomDouble(25.0, 100)));
        // get previous year and increment
        int year = Integer.parseInt(
                (String)xAxisLabels.get(xAxisLabels.size() - 1)) + 1;
        xAxisLabels.add(String.valueOf(year));

        renderOnSubmit = true;
    }

    /**
     * Remvoe new data to implementing classes data model.
     */
    public void removeData() {
        // remove last added data
        if (chartData.size() > 0 && xAxisLabels.size() > 0) {
            chartData.remove(chartData.size() - 1);
            xAxisLabels.remove(xAxisLabels.size() - 1);

            renderOnSubmit = true;
        }
    }

    /**
     * Remvoe new data to implementing classes data model.
     */
    public void resetData() {
        init();
    }

    /**
     * Gets data assosiated with this type of chart. For Axial type outputChart
     * the dat should be return as an array.
     *
     * @return return the chart data associated with this data model.
     */
    public ArrayList getChartData() {
        return chartData;
    }
    
    public double getRandomDouble(double low, double high) {
        return (randomizer.nextDouble() * (high - low)) + low;
    }
}

