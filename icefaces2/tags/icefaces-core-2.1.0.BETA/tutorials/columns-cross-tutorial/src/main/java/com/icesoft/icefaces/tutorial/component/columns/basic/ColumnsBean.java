package com.icesoft.icefaces.tutorial.component.columns.basic;

import java.util.HashMap;
import java.util.Map;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;

public class ColumnsBean
{
    private String[] events = {"onclick", "onkeyup", "ondrag"};
    private String[] browsers = {"IE", "Firefox", "Safari"};
    private DataModel rowModel = new ArrayDataModel(events);
    private DataModel columnsModel = new ArrayDataModel(browsers);
    private final Map eventSupport = new HashMap();
    
    public ColumnsBean() {
        for (int i = 0; i < events.length; i++) {
            for (int j = 0; j < browsers.length; j++) {
                eventSupport.put(events[i] + browsers[j],
                    String.valueOf(!(events[i].equals("ondrag") && browsers[j].equals("Safari"))));
            }
        }
    }
    
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
    
    public Object getSupportInfo(){
        DataModel rowDataModel = getRowModel();
        if (rowDataModel.isRowAvailable())
        {
          Object event = getRowModel().getRowData();
          DataModel columnsDataModel = getColumnsModel();
          if (columnsDataModel.isRowAvailable())
          {
            Object browser = columnsDataModel.getRowData();
            return eventSupport.get(event+browser.toString());
          }
        }
        return null;
    }
}
