package com.icesoft.icefaces.tutorial.component.outputchart.basic;

import java.awt.Color;
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
    private List paints;
    
    public PieChart(){
        paints = new ArrayList();
        paints.add(new Color(26, 86, 138)); //#1A568A
        paints.add(new Color(76, 126, 167)); //#4C7EA7
        paints.add(new Color(148, 179, 203)); //#94B3CB
        paints.add(new Color(193, 211, 223)); //#C1D3DF
    }
    
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
