/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
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
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package com.icesoft.faces.component.ext.renderkit;

import com.icesoft.faces.component.CSS_DEFAULT;
import com.icesoft.faces.component.ExtendedAttributeConstants;
import com.icesoft.faces.component.ext.taglib.Util;
import com.icesoft.faces.renderkit.dom_html_basic.PassThruAttributeRenderer;

import org.w3c.dom.Element;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class GridRenderer
        extends com.icesoft.faces.renderkit.dom_html_basic.GridRenderer {
    private static final String[] PASSTHRU =
        ExtendedAttributeConstants.getAttributes(
            ExtendedAttributeConstants.ICE_PANELGRID);

    protected void doPassThru(FacesContext facesContext, UIComponent uiComponent) {
        PassThruAttributeRenderer.renderHtmlAttributes(
            facesContext, uiComponent, PASSTHRU);
    }
    
    // row styles are returned by reference
    public String[] getRowStyles(UIComponent uiComponent) {
        if (((String[]) getRowStyleClasses(uiComponent)).length <= 0) {
            String[] rowStyles = new String[2];
            rowStyles[0] = Util.getQualifiedStyleClass(uiComponent, 
                    CSS_DEFAULT.TABLE_ROW_CLASS1);
            rowStyles[1] =Util.getQualifiedStyleClass(uiComponent, 
                    CSS_DEFAULT.TABLE_ROW_CLASS2);
            return rowStyles;
        } else {
            return getRowStyleClasses(uiComponent);
        }
    }

    public String[] getRowStyleClasses(UIComponent uiComponent) {
        String[] rowClasses = super.getRowStyleClasses(uiComponent);
        for (int i=0; i < rowClasses.length; i++) {
            rowClasses[i] = Util.getQualifiedStyleClass(uiComponent,
                            rowClasses[i],
                          CSS_DEFAULT.TABLE_ROW_CLASS,
                          "rowClasses"                            
                                       ); 
        }
        return rowClasses;
    }
    
    public void writeColStyles(String[] columnStyles, int columnStylesMaxIndex,
                               int columnStyleIndex, Element td,
                               int colNumber, UIComponent uiComponent) {
        if (columnStyles.length > 0) {
            if (columnStylesMaxIndex >= 0) {
                td.setAttribute("class", columnStyles[columnStyleIndex]);
            }
        } else {
            td.setAttribute("class", getColumnStyle(uiComponent, CSS_DEFAULT.COLUMN +
                                                    colNumber++));
        }
    }
    
    protected String getRowStyle(UIComponent uiComponent, String style) {
        return Util.getQualifiedStyleClass(uiComponent,
                                            style,
                                            CSS_DEFAULT.TABLE_ROW_CLASS,
                                            "rowClasses"); 
    }
    
    protected String getColumnStyle(UIComponent uiComponent, String style) {
        return Util.getQualifiedStyleClass(uiComponent, 
                                            style);
    }
    
    protected String[] getColumnStyleClasses(UIComponent uiComponent) {
        String[] columnStyles = super.getColumnStyleClasses(uiComponent);
        if (columnStyles.length == 0) {
            columnStyles = new String[2];
            columnStyles[0] = Util.getQualifiedStyleClass(uiComponent, 
                    CSS_DEFAULT.TABLE_COLUMN_CLASS1); 
            columnStyles[1] = Util.getQualifiedStyleClass(uiComponent, 
                    CSS_DEFAULT.TABLE_COLUMN_CLASS2);   
        } else {
            for (int i=0; i < columnStyles.length; i++) {
                columnStyles[i] = Util.getQualifiedStyleClass(uiComponent,
                              columnStyles[i],
                              CSS_DEFAULT.TABLE_COLUMN_CLASS,
                              "columnClasses"                            
                                           ); 
            }
        }
        return columnStyles;
    }
}
