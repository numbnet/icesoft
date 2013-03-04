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
package org.icefaces.mobi.component.datatable;

import static org.icemobile.util.HTML.TD_ELEM;
import static org.icemobile.util.HTML.TR_ELEM;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import org.icefaces.mobi.renderkit.ResponseWriterWrapper;
import org.icefaces.mobi.utils.MobiJSFUtils;
import org.icemobile.component.IColumn;
import org.icemobile.component.IDataTable;
import org.icemobile.renderkit.DataTableCoreRenderer;
import org.icemobile.util.ClientDescriptor;

/**
 * Render the JSF Mobile DataTable
 *
 */
public class DataTableRenderer extends Renderer{
    
    private static Logger LOG = Logger.getLogger(DataTableRenderer.class.getName());
    
    /**
     * Render the DataTable. The renderer renders all child columns in the encodeEnd
     * method.
     */
    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if( !component.isRendered()){
            return;
        }
        
        DataTable dataTable = (DataTable)component;
        ResponseWriterWrapper writer = new ResponseWriterWrapper(context.getResponseWriter());
        DataTableCoreRenderer renderer = new DataTableCoreRenderer();
            
        renderer.renderRootStart(dataTable, writer);
        renderer.renderResponsiveColumnStyles(dataTable, writer);
        
        ClientDescriptor client = MobiJSFUtils.getClientDescriptor();
        String largeViewOrientation = dataTable.getLargeViewDetailOrientation();
        String orientation = dataTable.getDetailOrientation();
        if( ( client.isDesktopBrowser() || client.isTabletBrowser())
                && largeViewOrientation != null && largeViewOrientation.length() > 0){
           orientation = dataTable.getLargeViewDetailOrientation();
        }
        if( IDataTable.TOP.equals(orientation) || IDataTable.LEFT.equals(orientation)){
            renderer.renderDetailView(dataTable,writer);
        }
        
        renderer.renderMasterViewStart(dataTable,writer);
        renderer.renderHeaders(dataTable, writer);
        renderer.renderDataRegionStart(dataTable, writer);
        if( dataTable.getFirst() > -1 ){
            dataTable.setRowIndex(dataTable.getFirst());
        }
        else{
            dataTable.setRowIndex(0);
        }
        
        while( dataTable.isRowAvailable()){
            renderer.renderNextRow(dataTable, writer);
            for (IColumn column : dataTable.getColumns()) {
                writer.startElement(TD_ELEM, column);
                if( column.isOptimizeExpression() ){
                    ((Column)column).setBaseVar(dataTable.getRowData());
                    writer.writeText(column.getValueFromCachedExpression());
                }
                else{
                    writer.writeText(column.getValue());
                }
                writer.endElement(TD_ELEM);
            }
            dataTable.setRowIndex(dataTable.getRowIndex()+1);
        }
        writer.endElement(TR_ELEM);
        renderer.renderDataRegionEnd(dataTable,writer);
        renderer.renderMasterViewEnd(dataTable,writer);   
        
        if( IDataTable.BOTTOM.equals(orientation) 
             || IDataTable.RIGHT.equals(orientation)){
            renderer.renderDetailView(dataTable,writer);
        }
                        
        renderer.renderJavaScript(dataTable, writer);        
        renderer.renderRootEnd(dataTable, writer);  
    }
    
    @Override
    public boolean getRendersChildren() {
        return true;
    }
    
    

}
