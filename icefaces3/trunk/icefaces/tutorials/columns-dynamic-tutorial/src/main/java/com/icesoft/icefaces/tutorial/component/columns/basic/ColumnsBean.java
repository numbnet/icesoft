package com.icesoft.icefaces.tutorial.component.columns.basic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

public class ColumnsBean
{
    private List users = new ArrayList();
    private DataModel rowModel;
    private DataModel columnsModel = null;
    private int numberOfColumns = 3;
    
    public ColumnsBean() {
        String[] robert = {"Robert", "Ben", "rober.ben@yahoo.com", "male", "403-619-0909", "Calgary", "AB", "Canada"};
        String[] chris = {"Chris", "Angelo", "cangelo@tips.com", "male", "403-449-3212", "Edmonton", "AB", "Canada"};
        String[] jack = {"Jack", "Rudy", "rjack@hotmail.com", "male", "905-543-3342", "Toronto", "ON", "Canada"};
        users.add(new ArrayList(Arrays.asList(robert)));
        users.add(new ArrayList(Arrays.asList(chris)));
        users.add(new ArrayList(Arrays.asList(jack)));
        rowModel = new ListDataModel(users);
    }
    
    public DataModel getRowModel() {
        return rowModel;
    }
    
    public void setRowModel(DataModel rowModel) {
        this.rowModel = rowModel;
    }
    
    public DataModel getColumnsModel() {
        if (columnsModel==null) {
            updateModel();
        }
        return columnsModel;
    }
    
    public void setColumnsModel(DataModel columnsModel) {
        this.columnsModel = columnsModel;
    }  
    
    public Object getCellValue(){
        if (rowModel.isRowAvailable() && columnsModel.isRowAvailable()) 
        {
            int col = columnsModel.getRowIndex();
            return ((List)rowModel.getRowData()).get(col).toString();
        }
        return null;
    }
    
    public void change(ValueChangeEvent event) {
        int columnValue = (event.getNewValue()!= null?
            Integer.parseInt(event.getNewValue().toString()) : 7);
            
        if ((columnValue >= 1) && (columnValue <= 8)) {
            numberOfColumns = columnValue;
            updateModel();
        }
    }
    
    public void updateModel() {
        String[] array = new String[numberOfColumns];
        for (int i = 0; i<numberOfColumns; i++) {
            array[i] = String.valueOf(i+1);
        }
        columnsModel = new ArrayDataModel(array);
    }
    
    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }
}
