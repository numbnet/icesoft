/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 *
 */

package com.icesoft.icefaces.tutorial.component.outputchart.basic;

import com.icesoft.faces.component.outputchart.OutputChart;

import java.util.List;

import javax.faces.event.ActionEvent;

/**
 * The PieChartBean is responsible for holding all the UI information and
 * generating detailed sales data for the pie chart
 *
 * @since 1.5
 */
public class PieChartBean extends PieChart {
    
    //String displayed in the UI
    private static String clickedAreaValue = "Click on the image map below to display a chart value: ";

    //flag to determine if the graph needs rendering
    private boolean pieNeedsRendering = false;
    
    //list of the sales data from the sales class
    private List sales = SalesBean.buildSales(this);

    public PieChartBean() {
        super();
    }

    /**
     * Method to call the rendering of the chart based on the pieNeedsRendering
     * flag
     *
     * @param component chart component which will be rendered.
     * @return boolean true if OutputChart should be re-rendered; otherwise,
     *         false.
     */
    public boolean newChart(OutputChart component) {
        if (pieNeedsRendering) {
            pieNeedsRendering = false;
            return true;
        } else {
            return false;
        }
    }

    public String getClickedAreaValue() {
        return clickedAreaValue;
    }

    public void setClickedAreaValue(String clickedAreaValue) {
        PieChartBean.clickedAreaValue = clickedAreaValue;
    }

    // When a piece of the pie is clicked, retrieve the sales data for that year.
    public void imageClicked(ActionEvent event) {
        
        if (event.getSource() instanceof OutputChart) {
            OutputChart chart = (OutputChart) event.getSource();
            if (chart.getClickedImageMapArea().getLengendLabel() != null) {
                setClickedAreaValue(clickedAreaValue + chart
                        .getClickedImageMapArea().getLengendLabel()
                        + " : " +
                        chart.getClickedImageMapArea().getValue());
                SalesBean.setSalesForYear(this,
                        chart.getClickedImageMapArea().getLengendLabel());
                
                
            }
        }
    }
    
    public List getSales() {
        return sales;
    }

}
