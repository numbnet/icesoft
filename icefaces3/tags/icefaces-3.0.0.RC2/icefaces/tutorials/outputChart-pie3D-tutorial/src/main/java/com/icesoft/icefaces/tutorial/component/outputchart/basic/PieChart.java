package com.icesoft.icefaces.tutorial.component.outputchart.basic;

import java.util.ArrayList;
import java.util.List;

/**
 * PieChart is the backend bean that supplies all the data for the pie
 * charts.
 *
 * @since 1.5
 */
public class PieChart {
    
    //list of labels for the chart
    protected List labels = new ArrayList();

    //list of the data used by the chart
    protected List data = new ArrayList();

    //list of the colors used in the pie chart
    protected List paints = new ArrayList();
    
    public List getPaints() {
        return paints;
    }

    public void setPaints(List paints) {
        this.paints = paints;
    }
    
    public List getData() {
        return data;
    }

    public List getLabels() {
        return labels;
    }

}
