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
import com.icesoft.icefaces.tutorial.component.outputchart.basic.Sales;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

/**
 * The PieChartBean is responsible for holding all the UI information and
 * generating sales data for the pie chart
 *
 * @since 1.5
 */
public class PieChartBean extends PieChart {

    //flag to determine if the graph needs rendering
    private boolean pieNeedsRendering = false;
    
    //the temporary value for the selected color
    private Color selectedColor;
    
    //array of the available paints used in the chart
    public static final SelectItem[] availablePaints = new SelectItem[]{

            new SelectItem("E6EDF2", "Blue 1"),
            new SelectItem("CAE1EF", "Blue 2"),
            new SelectItem("C1D3DF", "Blue 3"),
            new SelectItem("B4C7D4", "Blue 4"),
            new SelectItem("94B3CB", "Blue 5"),
            new SelectItem("4C7EA7", "Blue 6"),
            new SelectItem("4FAADC", "Blue 7"),
            new SelectItem("4397C5", "Blue 8"),
            new SelectItem("1A568A", "Blue 9"),
            new SelectItem("0D4274", "Blue 10"),
            new SelectItem("CCCCCC", "Grey 1"),
            new SelectItem("ACACAC", "Grey 2"),
            new SelectItem("F78208", "Orange"),
            new SelectItem("000000", "Black")};
    
    //a temporary string for the current label
    private String label;

    private float value;
    
    //index to delete from
    int deleteIndex = 0;
    
    //list of items to delete
    private List deleteList = new ArrayList();

    public PieChartBean() {
        super();
        // Generate data for chart from Sales class.
        Iterator it = Sales.getMasterSales().values().iterator();
        double price;
        String label;
        int r = 3;
        while (it.hasNext()) {

            Sales[] yearSale = (Sales[]) it.next();
            price = 0;
            label = "";
            for (int i = 0; i < yearSale.length; i++) {
                price += (yearSale[i]).getPrice();
                label = (yearSale[i]).getYear();

            }
            labels.add(label);
            data.add(new Double(price));
            //adds paint from availablePaints list
            paints.add(new Color(Integer.parseInt(
                    (String) availablePaints[r].getValue(), 16)));
            r++;
        }
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
    
    /**
     * Method to listen for the change in color in the graph
     *
     * @param event JSF value changed event
     */
    public void paintChangeListener(ValueChangeEvent event) {

        if (event.getNewValue() != null) {
            selectedColor =
                    new Color(
                            Integer.parseInt(
                                    event.getNewValue().toString(), 16));
            System.out.println("Hex Color: " + 
                        Integer.parseInt(
                                    event.getNewValue().toString(), 16));
        }
    }
    
    public SelectItem[] getAvailablePaints() {
        return availablePaints;
    }
    
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        if (null == label || label.length() < 1) {
            label = " ";
        }
        this.label = label;
    }
    
    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
    
    /**
     * Method to add a value and a color to the chart
     *
     * @param event JSF action event.
     */
    public void addToChart(ActionEvent event) {
        paints.add(selectedColor);
        labels.add(label);
        data.add(new Double(value));
        pieNeedsRendering = true;
    }
    
    /**
     * Method to listen for an action to delete from the chart
     *
     * @param event JSF value changed event
     */
    public void deleteListValueChangeListener(ValueChangeEvent event) {
        if (event.getNewValue() != null) {
            deleteIndex = Integer.parseInt(event.getNewValue().toString());
        }
    }
    
    public List getDeleteList() {
        deleteList.clear();
        deleteList.add(new SelectItem("-1", "Select..."));
        for (int i = 0; i < labels.size(); i++) {
            deleteList.add(new SelectItem("" + i, "" + labels.get(i)));
        }
        return deleteList;
    }

    public void setDeleteList(List deleteList) {
        this.deleteList = deleteList;
    }
    
    public boolean isDeleteAllowed() {
        return labels.size() > 2;
    }
    
    /**
     * Method to delete an item from the chart
     *
     * @param event JSF action event
     */
    public void deleteChart(ActionEvent event) {
        if (deleteIndex >= 0 && labels.size() > 2) {
            labels.remove(deleteIndex);
            data.remove(deleteIndex);
            paints.remove(deleteIndex);
            pieNeedsRendering = true;
        }
    }


}
