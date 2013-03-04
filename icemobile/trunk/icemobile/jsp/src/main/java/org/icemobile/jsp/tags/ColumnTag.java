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
package org.icemobile.jsp.tags;

import java.io.IOException;
import java.util.logging.Logger;

import javax.el.ValueExpression;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.tagext.JspTag;

import org.icemobile.component.IColumn;
import org.icemobile.component.IDataTable;
import org.icemobile.renderkit.IResponseWriter;
import static org.icemobile.util.HTML.*;

/**
 * JSP Tag class for the Mobile DataTable column
 */
public class ColumnTag extends BaseSimpleTag implements IColumn{
    
    private static Logger LOG = Logger.getLogger(ColumnTag.class.getName());
    
    private String headerText;
    private String value;
    private String minDeviceWidth;
    private String property;
    private boolean optimizeExpression;
    
    private void init(){
        this.headerText = null;
        this.value = null;
        this.minDeviceWidth = null;
        this.property = null;
        this.optimizeExpression = false;
    }

    public String getHeaderText() {
        return headerText;
    }

    public void setHeaderText(String text) {
        this.headerText = text;
   }

    public String getValue() {
        ValueExpression ve = JspFactory.getDefaultFactory()
                .getJspApplicationContext(getContext().getServletContext())
                .getExpressionFactory()
                .createValueExpression(getContext().getELContext(), value,  Object.class);
        String resolved = (String)ve.getValue(getContext().getELContext());
        //LOG.warning("expr=" + value + "value=" + resolved);
        return resolved;
    }

    public void setValue(String value) {
        //LOG.warning("setValue " + value);
        this.value = value;
    }

    @Override
    public void setParent(JspTag parent) {
        if( ! (parent instanceof IDataTable) )
            throw new IllegalStateException("column tags must have only a dataTable tag as parent");
        super.setParent(parent);
        DataTableTag dataTable = (DataTableTag)parent;
        if( !dataTable.readyToRenderChildren() ){
            dataTable.getColumns().add(this);
        }
        
    }

    @Override
    public void doTag() throws JspException, IOException {
        super.doTag();
        DataTableTag parent = (DataTableTag)getParent();
        if( parent.readyToRenderChildren() ){
            IResponseWriter writer = new TagWriter(getContext());
            writer.startElement(TD_ELEM);
            writer.writeText(value);
            writer.endElement(TD_ELEM);
        }
    }

    public String getValueFromCachedExpression() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setMinDeviceWidth(String deviceWidth) {
        this.minDeviceWidth = deviceWidth;        
    }

    public String getMinDeviceWidth() {
        return minDeviceWidth;
    }

    public boolean isOptimizeExpression() {
        return optimizeExpression;
    }

    public void setOptimizeExpression(boolean optimize) {
        this.optimizeExpression = optimize;
    }

    public String getProperty() {
        return this.property;
    }

    public void setProperty(String property) {
        this.property = property;
    }
    
    public void release(){
        init();
    }

}
