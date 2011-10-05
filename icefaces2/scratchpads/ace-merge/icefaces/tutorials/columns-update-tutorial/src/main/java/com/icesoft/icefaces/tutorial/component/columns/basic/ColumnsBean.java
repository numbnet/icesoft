package com.icesoft.icefaces.tutorial.component.columns.basic;

import javax.faces.event.ActionEvent;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import java.util.Random;

public class ColumnsBean
{
    private Random randomizer = new Random(System.currentTimeMillis());
    private int startFrom = 1;
    private String[] index = {"1.", "2.", "3.", "4."};
    private String[] heading = {"First", "Second"};
    private DataModel rowModel = new ArrayDataModel(index);
    private DataModel columnsModel = new ArrayDataModel(heading);
    
    public DataModel getRowModel() {
        return rowModel;
    }
    
    public void setRowModel(DataModel rowModel) {
        this.rowModel = rowModel;
    }
    
    public DataModel getColumnsModel() {
        return columnsModel;
    }
    
    public void setColumnsModel(DataModel columnsModel) {
        this.columnsModel = columnsModel;
    }
    
    public Object getStartFrom() {
        return new Integer(startFrom);
    }
    
    public void next(ActionEvent event){
        startFrom += 1+randomizer.nextInt(4);
    }
}
