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

/*
 * ChartMediator.java
 *
 * Created on January 24, 2007, 10:13 AM
 *
 * This class is the governing class for the chart component in the Component Showcase.
 *It is used to set up the types of charts, create the charts, and manage the actions of the different charts.
 * 
 */

package com.icesoft.icefaces.samples.showcase.components.charts;


import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.icesoft.faces.component.outputchart.OutputChart;
import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.Highlight;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

/**
 * The chart factory is responsible for returning a Chart fo rthe given
 * OutputChart type.  
 *
 * @author dnorthcott
 * @version 1.5
 */
public class ChartMediator {
    
    private static final List areaPaints =
        new ArrayList(Arrays.asList(new Color[]{new Color(26, 86, 138),
                                                new Color(76, 126, 167),
                                                new Color(148, 179, 203)}));
    
    //List of charts that the user can seleect from the drop down menu
    private static SelectItem[] chartList = new SelectItem[]{
        new SelectItem(OutputChart.AREA_CHART_TYPE),
        new SelectItem(OutputChart.AREA_STACKED_CHART_TYPE),
        new SelectItem(OutputChart.BAR_CHART_TYPE),
        new SelectItem(OutputChart.BAR_CLUSTERED_CHART_TYPE),
        new SelectItem(OutputChart.BAR_STACKED_CHART_TYPE),
        new SelectItem(OutputChart.LINE_CHART_TYPE),
        new SelectItem(OutputChart.POINT_CHART_TYPE),
        new SelectItem(OutputChart.PIE2D_CHART_TYPE),
        new SelectItem(OutputChart.PIE3D_CHART_TYPE),};
    
    
    //boolean to determine if the chart is a 2D pie chart
    private boolean pie = false;
    
    
    
    //boolean to determine if the chart is an axis chart
    private boolean axis = true;
    
    //boolean to determine if the chart is a 3D pie chart
    public boolean pie3D = false;
    
    //Flag to determine whether the type of chart was changed
    private boolean chartChangedFlag = true;
    
    //Temporary holding string when the type of chart has been changed
    private String wasChanged;
    
    public static final String DEFAULT_STRING =
            "Click on the image map below to display a chart value: ";
    
    //Highlight effect on the text when the image is clicked
    private Highlight effectOutputText = new Highlight("#ffff99");
    
    //sets the chart type to bar for default
    private String chart = OutputChart.BAR_CHART_TYPE;
    
    public Chart chartObject = new Chart(chart);
    
    //sets the string returned when the chart is clicked to the default value
    private String clickedValue = DEFAULT_STRING;
    
    // Used for bar and barclustered horizontal attribute
    private boolean horizontal = false;
    
    // Used to show the orientation radio buttons
    private boolean orientationChange = true;
    
    //Title for the x-axis
    private String titleX = "Years";
    
    //Title for the y-axis
    private String titleY = "Problems";
    
    
    /*
     * Returns the list of charts to choose from
     * @return list of possible charts.
     */
    public SelectItem[] getChartList() {
        return chartList;
    }
    
    /*
     * Sets the isAxis Boolean
     *@param boolean isAxis
     */
    public boolean isAxis() {
        return axis;
    }
    
    /*
     * Returns whether the graph is a 3D PieChart
     *@return boolean
     */
    public boolean isPie3D() {
        return pie3D;
    }
    
    /*
     * Sets the graph to a 3D pie
     *@param boolean pie3D
     */
    public void setPie3D(boolean pie3D) {
        this.pie3D = pie3D;
    }
    
    /*
     * Returns whether the graph is a pie chart(2d)
     *@return boolean
     */
    public boolean isPie() {
        return pie;
    }
    
    /*
     * Sets the graph to an axis type
     *@param boolean axis
     */
    public void setAxis(boolean axis) {
        this.axis = axis;
    }
    
    /*
     * Sets the graph to a 2dPie type
     *@param boolean pie
     */
    public void setPie(boolean pie) {
        this.pie = pie;
    }
    
    
    /*
     * Returns the chart String
     *@return String chart
     */
    public String getChart() {
        
        return chart;
    }
    
    
    /*
     * Sets the chart String
     * Determines what type of graph it is based on the parameter passed in
     *@param String chart
     */
    public void setChart(String chart) {
        
        if (wasChanged != null) {
            this.chart = wasChanged;
            wasChanged = null;
        } else {
            this.chart = chart;
        }
        
        chartObject.setType(chart);
        
        if (!chart.equals(OutputChart.PIE3D_CHART_TYPE)) {
            pie3D = false;
            
        }
        if (chart.equals(OutputChart.PIE3D_CHART_TYPE) ||
                chart.equals(OutputChart.PIE2D_CHART_TYPE)) {
            axis = false;
            
            if (chart.equals(OutputChart.PIE3D_CHART_TYPE)) {
                pie3D = true;
                
                
            }
        } else {
            axis = true;
        }
        
    }
    
    /*
     * Determines whether or not the application should render based on status change
     *@return boolean
     */
    public boolean allCharts(OutputChart component) {
        
        if (chartChangedFlag) {
            chartChangedFlag = false;
            return true;
        } else {
            return chartChangedFlag;
        }
    }
    
    
    /*
     * Determines whether the chart was changed
     * Sets the default_string if the area type charts are selected
     *@param ValueChangeEvent event
     */
    public void chartChanged(ValueChangeEvent event) {
        chartChangedFlag = true;
        wasChanged = (String) event.getNewValue();
        
        if (event.getNewValue().equals(OutputChart.AREA_CHART_TYPE) ||
                event.getNewValue().equals(OutputChart.AREA_STACKED_CHART_TYPE)) {
            setClickedValue(
                    "A client side image map is not supported for Area charts " +
                    "(clicking on the chart will not display any values)");
        } else {
            setClickedValue(DEFAULT_STRING);
        }
        //if it is a bar chart then show the orientation radio buttons
        if(event.getNewValue().equals(OutputChart.BAR_CHART_TYPE) || 
              event.getNewValue().equals(OutputChart.BAR_CLUSTERED_CHART_TYPE)){
            orientationChange = true;
            if(horizontal == true){
                //align labels to the proper axis
                titleX = "Problems";
                titleY = "Years";
            }
        }
        else {
            orientationChange = false;
            //align labels to the proper axis
            titleX = "Years";
            titleY = "Problems";
        }
            
    }
    
    
    /*
     * Returns the clickedValue
     *@return String clickedValue
     */
    public String getClickedValue() {
        
        return clickedValue;
    }
    
    public Chart getChartObject(){
        return chartObject;
    }
    /*
     * Returns the text effect
     *@return Effect EffectOutputText
     */
    public Effect getEffectOutputText() {
        return effectOutputText;
    }
    
    /*
     * Sets the output text effect
     *@param Effect effectOutputText
     */
    public void setEffectOutputText(Effect effectOutputText) {
        this.effectOutputText = (Highlight) effectOutputText;
    }
    
    /*
     * Sets the clicked value
     *@param String clickedValue
     */
    public void setClickedValue(String clickedValue) {
        
        this.clickedValue = clickedValue;
    }
    
    /*
     * When the image map has been clicked this method returns the axis label plus the value
     *@param ActionEvent event
     */
    public void imageClicked(ActionEvent event) {
        
        if (event.getSource() instanceof OutputChart) {
            OutputChart chart = (OutputChart) event.getSource();
            if (chart.getClickedImageMapArea().getXAxisLabel() != null) {
                
                setClickedValue(DEFAULT_STRING +
                        chart.getClickedImageMapArea()
                        .getXAxisLabel() +
                        "  :  " +  chart.getClickedImageMapArea()
                        .getValue());
                
                effectOutputText.setFired(false);
            }
            
        }
        
    }
    
    public void pieAction(ActionEvent event) {
        
        if (event.getSource() instanceof OutputChart) {
            OutputChart chart = (OutputChart) event.getSource();
            if (chart.getClickedImageMapArea().getLengendLabel() != null) {
                setClickedValue(DEFAULT_STRING + chart
                        .getClickedImageMapArea().getLengendLabel()
                        + " : " +
                        chart.getClickedImageMapArea().getValue());
                PieChartBean.setSalesForYear(
                        chart.getClickedImageMapArea().getLengendLabel());
                effectOutputText.setFired(false);
                
                
            }
        }
        
    }
    /**
     * Sets the horizontal, true the chart will be displayed in a 
     * horizontal orientation, false the chart will be in a vertical 
     * orientation.
     *
     * @param horizontal true for horizontal, false for vertical.
     */
    public void setHorizontal(boolean horizontal){
        this.horizontal = horizontal;
    }
    
    /**
     * Gets the horizontal boolean value for the charts orientation.
     *
     * @return horizontal boolean value for chart orientation.
     */
    public boolean getHorizontal(){
        return horizontal;
    }
    
    /**
     * Gets the orientationChange value.  True is for when it is a bar or
     * barclustered chart.  False is all other charts.  Used to display the
     * radio button group for changing chart orientation.
     *
     * @return orientationChange true for visible, false is not visible.
     */
    public boolean getOrientationChange() {
        return orientationChange;
    }
    
    /**
     * Sets the orientationChange value, true, radio button group is visivble,
     * false, the radio button group is not visible.
     *
     * @param orientationChange true for visible, false is not.  
     */
    public void setOrientationChange(boolean orientationChange) {
        this.orientationChange = orientationChange;
    }

    /**
     * Method called when the orientation is change by the user.
     *
     * @param event contains new orientation value.
     */
    public void selectOrientation(ValueChangeEvent event){
        String temp = titleX;
        if(event.getNewValue().equals("true")){
            //set the orientation and reverse the axis labels
            horizontal = true;
            titleX = titleY;
            titleY = temp;
        }
        else{
            //set the orientation and reverse the axis labels
            horizontal = false;
            titleX = titleY;
            titleY = temp;
        }
        //render the chart
        chartChangedFlag = true;
    }
    
    /**
     * Gets the title of the x-axis.
     *
     * @return titleX name for the x-axis.
     */
    public String getTitleX(){
        return titleX;
    }
    
    /**
     * Sets the title of the x-axis.
     *
     * @param titleX new title of the x-axis.
     */
    public void setTitleX(String titleX){
        this.titleX = titleX;
    }
    
    /**
     * Gets the title of the y-axis.
     *
     * @return titleY name for the y-axis.
     */
    public String getTitleY(){
        return titleY;
    }
    
    /**
     * Sets the title of the y-axis.
     *
     * @param titleY new title of the y-axis.
     */
    public void setTitleY(String titleY){
        this.titleY = titleY;
    }
    
    public List getAreaPaints() {
        return areaPaints;
    }
}
