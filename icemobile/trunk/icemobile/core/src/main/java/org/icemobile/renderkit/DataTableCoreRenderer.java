/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.icemobile.renderkit;

import static org.icemobile.util.HTML.CLASS_ATTR;
import static org.icemobile.util.HTML.DIV_ELEM;
import static org.icemobile.util.HTML.ID_ATTR;
import static org.icemobile.util.HTML.SCRIPT_ELEM;
import static org.icemobile.util.HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT;
import static org.icemobile.util.HTML.SPAN_ELEM;
import static org.icemobile.util.HTML.STYLE_ELEM;
import static org.icemobile.util.HTML.TABLE_ELEM;
import static org.icemobile.util.HTML.TBODY_ELEM;
import static org.icemobile.util.HTML.TD_ELEM;
import static org.icemobile.util.HTML.THEAD_ELEM;
import static org.icemobile.util.HTML.TH_ELEM;
import static org.icemobile.util.HTML.TITLE_ATTR;
import static org.icemobile.util.HTML.TR_ELEM;
import static org.icemobile.util.HTML.TYPE_ATTR;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.icemobile.component.IColumn;
import org.icemobile.component.IDataTable;

/**
 * Core Renderer for the Mobile JSF and JSP DataTable
 *
 */
public class DataTableCoreRenderer  extends BaseCoreRenderer{
    
    private static final Logger LOG =
            Logger.getLogger(DataTableCoreRenderer.class.toString());
    
    /**
     * Render the start of the root element 
     */
    public void renderRootStart(IDataTable dataTable, IResponseWriter writer)
        throws IOException{
        writer.startElement(DIV_ELEM);
        writer.writeAttribute(ID_ATTR, dataTable.getClientId());
        writer.writeAttribute(CLASS_ATTR, "mobi-tbl " + dataTable.getId());
    }
    
    /**
     * Render the end of the root element
     */
    public void renderRootEnd(IDataTable dataTable, IResponseWriter writer)
            throws IOException{
        writer.endElement(DIV_ELEM);
    }
    
    /**
     * Render the start of the data region
     */
    public void renderDataRegionStart(IDataTable dataTable, IResponseWriter writer)
       throws IOException{
           writer.startElement(DIV_ELEM);
           writer.writeAttribute(ID_ATTR, dataTable.getClientId()+"_data");
           writer.writeAttribute(CLASS_ATTR, "mobi-tbl-data");
               writer.startElement(TABLE_ELEM);
                   writer.startElement(TBODY_ELEM);
    }
   
    /**
     * Render the end of the data region
     */
    public void renderDataRegionEnd(IDataTable dataTable, IResponseWriter writer)
           throws IOException{
                   writer.endElement(TBODY_ELEM);
               writer.endElement(TABLE_ELEM);
           writer.endElement(DIV_ELEM);
    }
   
    /**
     * Render the start of the master section
     */
    public void renderMasterViewStart(IDataTable dataTable, IResponseWriter writer)
       throws IOException{
       writer.startElement(DIV_ELEM);
       writer.writeAttribute(ID_ATTR, dataTable.getClientId()+"_master");
       String styleClass = "mobi-tbl-master";
       if( usesHorizontalLayout(dataTable) ){
           styleClass += " mobi-tbl-hor";
       }
       writer.writeAttribute(CLASS_ATTR, styleClass);
    }
      
    /**
     * Render the end of the master section
     */
    public void renderMasterViewEnd(IDataTable dataTable, IResponseWriter writer)
        throws IOException{
                        
        writer.endElement(DIV_ELEM);
    }
    
    /**
     * Render an empty table cell
     */
    public void renderEmptyCell(IResponseWriter writer)
        throws IOException{
        writer.startElement(TD_ELEM);
        writer.closeOffTag();
    }
    
    /**
     * Render the continuation of a table row, ending the last TR, if 
     * necessary, and beginning a new row.
     */
    public void renderNextRow(IDataTable dataTable, IResponseWriter writer)
        throws IOException{
        if( dataTable.getRowIndex() > 0 && dataTable.getFirst() <= dataTable.getRowIndex() ){
            writer.endElement(TR_ELEM);
        }
        writer.startElement(TR_ELEM);
        writer.writeAttribute(ID_ATTR, dataTable.getClientId() + ":" + dataTable.getRowIndex());
        writer.closeOffTag();
    }
    
    /**
     * @param dataTable The IDataTable 
     * @return true, if a dataTable has another row to render
     */
    public boolean hasNext(IDataTable dataTable){
        if( dataTable.getValue() == null ){
            return false;
        }
        boolean rowAvail = dataTable.isRowAvailable();
        if( rowAvail && dataTable.getRows() > 0 ){
            if( dataTable.getRowIndex() >= (dataTable.getRows() + dataTable.getFirst())){
                return false;
            }
        }
        if (!rowAvail) {
            return false;
        }
        return true;
    }
    
    /**
     * Render the detail section of the master/detail view
     */
    public void renderDetailView(IDataTable dataTable, IResponseWriter writer)
        throws IOException{
        writer.startElement(DIV_ELEM);
        writer.writeAttribute(ID_ATTR, dataTable.getClientId()+"_detail");
        String styleClass = "mobi-tbl-detail";
        if( usesHorizontalLayout(dataTable) ){
            styleClass += " mobi-tbl-hor";
        }
        writer.writeAttribute(CLASS_ATTR, styleClass);
        writer.startElement(DIV_ELEM, dataTable);
        writer.writeAttribute(CLASS_ATTR, "mobi-tbl-detail-render");
        writer.endElement(DIV_ELEM);
        if( dataTable.isClientSide() ){
            writer.startElement(DIV_ELEM, dataTable);
            writer.writeAttribute(CLASS_ATTR, "mobi-tbl-detail-template");
            dataTable.renderDetailView();
            writer.endElement(DIV_ELEM);
        }
        else{
            dataTable.renderDetailView();
        }
        writer.endElement(DIV_ELEM);
    }
    
    /**
     * @param dataTable The IDataTable
     * @return true if the dataTable has an orientation of left or right
     */
    private boolean usesHorizontalLayout(IDataTable dataTable){
        if( (dataTable.getClient().isTabletBrowser() || dataTable.getClient().isDesktopBrowser())
            && (IDataTable.LEFT.equals(dataTable.getLargeViewDetailOrientation())
                || IDataTable.RIGHT.equals(dataTable.getLargeViewDetailOrientation()))){
            return true;
        }
        return false;
    }
    
    private String calculateOrientation(IDataTable dataTable) {
        String orientation = null;
        if( dataTable.getClient().isTabletBrowser() || dataTable.getClient().isDesktopBrowser()){
            orientation = dataTable.getLargeViewDetailOrientation();
            if( orientation == null || orientation.length() == 0 ){
                orientation = dataTable.getDetailOrientation();
            }
        }
        else{
            orientation = dataTable.getDetailOrientation();
        }
        return orientation;
    }
    
    /**
     * Render the javascript to initialize the DataTable
     */
    public void renderJavaScript(IDataTable dataTable, IResponseWriter writer)
        throws IOException{
        writer.startElement(SPAN_ELEM);
        writer.writeAttribute(ID_ATTR, dataTable.getClientId() + "_js");
        writer.startElement(SCRIPT_ELEM);
        writer.writeAttribute(TYPE_ATTR, SCRIPT_TYPE_TEXT_JAVASCRIPT);
        
        //get the property names from the columns
        String properties = "{";
        List<IColumn> columns = dataTable.getColumns();
        boolean hasProperties = false;
        for( IColumn column : columns ){
            if( column.getProperty() != null && column.getProperty().length() > 0 ){
                properties += "" + column.getProperty() + ":" + 
                        dataTable.getColumns().indexOf(column) + ",";
                hasProperties = true;
            }
        }
        if( hasProperties ){
            properties = properties.substring(0,properties.length()-1) + "}";
        }
        
        String js = "ice.mobi.dataTable.initClient('"
                + dataTable.getClientId() + "', { " +
                "orientation: '" + calculateOrientation(dataTable) + "'," +
                "clientSide: " + dataTable.isClientSide() + "";
        if( hasProperties ){
            js += ",properties: " + properties;
        }
        js += "});";
        writer.writeText(js);
        writer.endElement(SCRIPT_ELEM);
        writer.endElement(SPAN_ELEM);
    }
    
    /**
     * Render the headers for the data region
     */
    public void renderHeaders(IDataTable dataTable, IResponseWriter writer)
        throws IOException{
        List<IColumn> columns = dataTable.getColumns();
        if( columns == null || columns.isEmpty() ){
            return;
        }
        
        writer.startElement(DIV_ELEM);
        writer.writeAttribute(ID_ATTR, dataTable.getClientId()+"_hdrs");
        writer.writeAttribute(CLASS_ATTR, "mobi-tbl-headers");
            writer.startElement(TABLE_ELEM);
                writer.startElement(THEAD_ELEM);
                    writer.startElement(TR_ELEM);
                    for (IColumn column : columns) {
                        writer.startElement(TH_ELEM, column);
                        writer.writeAttribute(TITLE_ATTR, column.getHeaderText());
                        writer.writeText(column.getHeaderText());
                        writer.endElement(TH_ELEM);
                    }
                    writer.endElement(TR_ELEM);
                writer.endElement(THEAD_ELEM);
            writer.endElement(TABLE_ELEM);
        writer.endElement(DIV_ELEM);
    }
    
    public void renderResponsiveColumnStyles(IDataTable dataTable, IResponseWriter writer)
        throws IOException{
        List<IColumn> columns = dataTable.getColumns();
        List<IColumn> responsiveColumns = new ArrayList<IColumn>();
        for( IColumn column : columns ){
            if( column.getMinDeviceWidth() != null && column.getMinDeviceWidth().length() > 0 ){
                responsiveColumns.add(column);
            }
        }
        
        if( responsiveColumns.size() > 0 ){
            writer.startElement(STYLE_ELEM, dataTable);
            for( IColumn column : responsiveColumns ){
                int columnIndex = dataTable.getColumns().indexOf(column) + 1; //CSS nth-child is 1-based
                writer.writeText("@media only screen and (max-width: " 
                        + column.getMinDeviceWidth() + "){ .mobi-tbl." + dataTable.getId() + " .mobi-tbl-master table td:nth-child(" 
                        + columnIndex + "), .mobi-tbl-master table th:nth-child(" + columnIndex 
                        + ") {display:none;}}");
            }
            writer.endElement(STYLE_ELEM);
        }
       
    }
    

 
}
