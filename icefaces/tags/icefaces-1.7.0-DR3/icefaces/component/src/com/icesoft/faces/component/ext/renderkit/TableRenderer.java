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

package com.icesoft.faces.component.ext.renderkit;

import com.icesoft.faces.component.CSS_DEFAULT;
import com.icesoft.faces.component.PORTLET_CSS_DEFAULT;
import com.icesoft.faces.component.ext.HtmlDataTable;
import com.icesoft.faces.component.ext.RowSelector;
import com.icesoft.faces.component.ext.UIColumns;
import com.icesoft.faces.component.ext.taglib.Util;
import com.icesoft.faces.component.panelseries.UISeries;
import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.context.effects.JavascriptContext;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import com.icesoft.faces.util.CoreUtils;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.*;


public class TableRenderer
        extends com.icesoft.faces.renderkit.dom_html_basic.TableRenderer {

    private static final String SELECTED_ROWS = "sel_rows";

    public String getComponentStyleClass(UIComponent uiComponent) {
        return (String) uiComponent.getAttributes().get("styleClass");

    }

    public String getHeaderClass(UIComponent component) {
        return (String) component.getAttributes().get("headerClass");
    }

    public String getFooterClass(UIComponent component) {
        return (String) component.getAttributes().get("footerClass");
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

    public String[] getHeaderStyles(UIComponent uiComponent) {
        String headerClass = getHeaderClass(uiComponent).
        replaceAll(CSS_DEFAULT.TABLE_STYLE_CLASS + CSS_DEFAULT.TABLE_HEADER_CLASS, "");
    	if (((String[]) getHeaderStyleClasses(uiComponent)).length <= 0) {
            String[] headerStyles = new String[2];
            headerStyles[0] = Util.getQualifiedStyleClass(uiComponent, 
                    CSS_DEFAULT.TABLE_COLUMN_HEADER_CLASS1) + ((headerClass.length() > 0)
                    ? headerClass : "");
            headerStyles[1] =Util.getQualifiedStyleClass(uiComponent, 
                    CSS_DEFAULT.TABLE_COLUMN_HEADER_CLASS2)+ ((headerClass.length() > 0)
                            ? headerClass : "");
            
            return headerStyles;
        } else {
            return getHeaderStyleClasses(uiComponent);
        }
    }
    
    public void writeColStyles(String[] columnStyles, int columnStylesMaxIndex,
                               int columnStyleIndex, Element td,
                               int colNumber, 
                               UIComponent uiComponent
                                ) {
        if (columnStyles.length > 0) {
            if (columnStylesMaxIndex >= 0) {
                td.setAttribute("class", columnStyles[columnStyleIndex]);
            }
        }
    }

    protected void renderFacet(FacesContext facesContext,
                               UIComponent uiComponent,
                               DOMContext domContext, boolean header)
            throws IOException {
        String facet, tag, element, facetClass;
        if (header) {
            facet = "header";
            tag = HTML.THEAD_ELEM;
            element = HTML.TH_ELEM;
            facetClass = getHeaderClass(uiComponent);
        } else {
            facet = "footer";
            tag = HTML.TFOOT_ELEM;
            element = HTML.TD_ELEM;
            facetClass = getFooterClass(uiComponent);
        }
        UISeries uiData = (UISeries) uiComponent;
        uiData.setRowIndex(-1);
        Element root = (Element) domContext.getRootNode();
        if (isScrollable(uiComponent)) {
            
            if (header) {
                // First table in first div path : table/tr/td/div/div0/table
                root = getScrollableHeaderTableElement(root);

            } else {
                // First table in second div path table/tr/td/div/div1/table
                root = getScrollableBodyTableElement(root);
            }
        }
        UIComponent headerFacet = getFacetByName(uiData, facet);
        boolean childHeaderFacetExists =
                childColumnHasFacetWithName(uiData, facet);
        Element thead = null;
        if (headerFacet != null || childHeaderFacetExists) {
            thead = domContext.createElement(tag);
            root.appendChild(thead);
        
        
            if (header) {
            	if(CoreUtils.getPortletStyleClass(PORTLET_CSS_DEFAULT
            			.PORTLET_SECTION_HEADER).length() > 1) {
                	thead.setAttribute(HTML.CLASS_ATTR, PORTLET_CSS_DEFAULT
                			.PORTLET_SECTION_HEADER);
            	}
                renderTableHeader(facesContext, uiComponent, headerFacet, thead, facetClass, element);
                renderColumnHeader(facesContext, uiComponent, thead, facet, element, header);
            } else {
            	if(CoreUtils.getPortletStyleClass(PORTLET_CSS_DEFAULT
            			.PORTLET_SECTION_FOOTER).length() > 1) {
                	thead.setAttribute(HTML.CLASS_ATTR, PORTLET_CSS_DEFAULT
                			.PORTLET_SECTION_FOOTER);
            	}            	
                renderColumnHeader(facesContext, uiComponent, thead, facet, element, header);
                renderTableHeader(facesContext, uiComponent, headerFacet, thead, facetClass, element);
             
            }
            domContext.setCursorParent(root);
        }
    }

    private void renderColumnHeader(FacesContext facesContext, 
                                    UIComponent uiComponent,
                                    Element thead, 
                                    String facet, 
                                    String element, 
                                    boolean header) throws IOException {
        StringTokenizer columnWitdths = getColumnWidths(uiComponent);
        DOMContext domContext =
            DOMContext.getDOMContext(facesContext, uiComponent);
            Element tr = domContext.createElement("tr");
            thead.appendChild(tr);
            List childList = getRenderedChildColumnsList(uiComponent);
            Iterator childColumns = childList.iterator();
            String width = null;
            int columnIndex = 1;
            int headerStyleLength = getHeaderStyles(uiComponent).length;
            int styleIndex = 0;
            while (childColumns.hasNext()) {

                UIComponent nextColumn = (UIComponent) childColumns.next();
                if (columnWitdths != null && columnWitdths.hasMoreTokens()) {
                    width = columnWitdths.nextToken();
                } else {
                    if (isScrollable(uiComponent)) {
                        width = "150px";
                    } else {
                        width = null;
                    }
                    
                }
                if (nextColumn instanceof UIColumn) {
                    processUIColumnHeader(facesContext, uiComponent,
                                          (UIColumn) nextColumn, tr, domContext,
                                          facet, element, width,
                                          columnIndex,
                                          styleIndex);
                    columnIndex++;
                } else if (nextColumn instanceof UIColumns) {
                    columnIndex = processUIColumnsHeader(facesContext,
                                                         uiComponent,
                                                         (UIColumns) nextColumn,
                                                         tr, domContext, facet,
                                                         element, width, columnIndex,
                                                         styleIndex,
                                                         headerStyleLength);
                }
                
                if (styleIndex++ == (headerStyleLength-1)) {
                    styleIndex = 0;
                }
            }
            if (header && isScrollable(uiComponent)) {
                tr.appendChild(scrollBarSpacer(domContext, facesContext));
            }
    }
    private void renderTableHeader(FacesContext facesContext, 
                                    UIComponent uiComponent,
                                    UIComponent headerFacet,
                                    Element thead,
                                    String facetClass,
                                    String element
                                    ) throws IOException{
        DOMContext domContext =
            DOMContext.getDOMContext(facesContext, uiComponent);
        if (headerFacet != null && headerFacet.isRendered()) {
            
            resetFacetChildId(headerFacet);
            Element tr = domContext.createElement("tr");
            thead.appendChild(tr);
            Element th = domContext.createElement(element);
            tr.appendChild(th);
            if (facetClass != null) {
                th.setAttribute("class", facetClass);
            }
            th.setAttribute("colspan",
                            String.valueOf(getNumberOfChildColumns(uiComponent)));
            th.setAttribute("scope", "colgroup");
            domContext.setCursorParent(th);
            domContext.streamWrite(facesContext, uiComponent,
                                   domContext.getRootNode(), th);
            encodeParentAndChildren(facesContext, headerFacet);
            if (isScrollable(uiComponent)) {
                tr.appendChild(scrollBarSpacer(domContext, facesContext));
            }
        }
    }
    private void processUIColumnHeader(FacesContext facesContext,
                                       UIComponent uiComponent,
                                       UIColumn nextColumn, Element tr,
                                       DOMContext domContext, String facet,
                                       String element, String width, int columnIndex,
                                       int styleIndex)
            throws IOException {
        HtmlDataTable htmlDataTable = (HtmlDataTable) uiComponent;
        Element th = domContext.createElement(element);
        tr.appendChild(th);
        if ("header".equalsIgnoreCase(facet) ){
            th.setAttribute("class",getHeaderStyles(uiComponent)[styleIndex]);
        } else {
            th.setAttribute("class",getFooterClass(htmlDataTable));
        }
      
        if (width != null) {
            th.setAttribute("style", "width:" + width + ";overflow:hidden;");
        }
        //th.setAttribute("colgroup", "col");
        UIComponent nextFacet = getFacetByName(nextColumn, facet);

        if (nextFacet != null) {
            resetFacetChildId(nextFacet);
            domContext.setCursorParent(th);
            domContext.streamWrite(facesContext, uiComponent,
                                   domContext.getRootNode(), th);
            encodeParentAndChildren(facesContext, nextFacet);
        }
    }

    private int processUIColumnsHeader(FacesContext facesContext,
                                       UIComponent uiComponent,
                                       UIColumns nextColumn, Element tr,
                                       DOMContext domContext, String facet,
                                       String element, String width, int columnIndex,
                                       int styleIndex,
                                       int headerStyleLength)
            throws IOException {
        HtmlDataTable htmlDataTable = (HtmlDataTable) uiComponent;
        int rowIndex = nextColumn.getFirst();
        //syleIndex should be increment here
        nextColumn.encodeBegin(facesContext);
        nextColumn.setRowIndex(rowIndex);
        while (nextColumn.isRowAvailable()) {
            UIComponent headerFacet = getFacetByName(nextColumn, facet);

            if (headerFacet != null) {
                Node oldParent = domContext.getCursorParent();
                Element th = domContext.createElement(element);
                tr.appendChild(th);
                th.setAttribute("class",getHeaderStyles(uiComponent)[styleIndex]);
                if (width != null) {
                    th.setAttribute("style", "width:" + width + ";");
                }
                //th.setAttribute("colgroup", "col");
                domContext.setCursorParent(th);
                domContext.streamWrite(facesContext, uiComponent,
                                       domContext.getRootNode(), th);
                	
                encodeParentAndChildren(facesContext, headerFacet);
                domContext.setCursorParent(oldParent);
            }
            if (styleIndex++ == (headerStyleLength-1)) {
                styleIndex = 0;
            }
            rowIndex++;
            columnIndex++;
            nextColumn.setRowIndex(rowIndex);
        }
        nextColumn.setRowIndex(-1);
        return columnIndex;
    }


    public void encodeChildren(FacesContext facesContext,
                               UIComponent uiComponent) throws IOException {
        validateParameters(facesContext, uiComponent, null);
        DOMContext domContext =
                DOMContext.getDOMContext(facesContext, uiComponent);
        Element root = (Element) domContext.getRootNode();

        if (isScrollable(uiComponent)) {
            root = getScrollableBodyTableElement(root);
        }
        DOMContext.removeChildrenByTagName(root, HTML.TBODY_ELEM);
        Element tBody = (Element) domContext.createElement(HTML.TBODY_ELEM);
/*
        if (CoreUtils.getPortletStyleClass(PORTLET_CSS_DEFAULT.
        							PORTLET_SECTION_BODY).length() > 1) {
        	tBody.setAttribute(HTML.CLASS_ATTR, PORTLET_CSS_DEFAULT.PORTLET_SECTION_BODY);
        }
*/
        root.appendChild(tBody);

        HtmlDataTable uiData = (HtmlDataTable) uiComponent;
        int rowIndex = uiData.getFirst();
        if (uiData.getRowCount() >=0 && uiData.getRowCount() <= rowIndex) {
            domContext.stepOver();
            return;
        }
        uiData.setRowIndex(rowIndex);
        int numberOfRowsToDisplay = uiData.getRows();
        int countOfRowsDisplayed = 0;
        String rowStyles[] = getRowStyles(uiComponent);
        int rowStyleIndex = 0;
        int rowStylesMaxIndex = rowStyles.length - 1;

        RowSelector rowSelector = getRowSelector(uiComponent);
        boolean rowSelectorFound = rowSelector != null;
        boolean toggleOnClick = false;
        String rowSelectionFunctionName = null;
        boolean rowSelectorCodeAdded = false; // Row selector code needs to be added to the first TD, adding it to the table body breaks safari
        Element scriptNode = null;
        Element hiddenInputNode = null;
        String paramId = getSelectedRowParameterName(uiComponent.getClientId(facesContext));
        if (rowSelectorFound) {
            toggleOnClick = rowSelector.getToggleOnClick().booleanValue();
            Element rowSelectedField =
                    domContext.createElement(HTML.INPUT_ELEM);

            rowSelectedField.setAttribute(HTML.ID_ATTR, paramId);
            rowSelectedField.setAttribute(HTML.NAME_ATTR, paramId);
            rowSelectedField.setAttribute(HTML.TYPE_ATTR, "hidden");
            hiddenInputNode = rowSelectedField;
            rowSelectionFunctionName = "ice_tableRowClicked"; 
        }

        String columnStyles[] = getColumnStyleClasses(uiComponent);
        int columnStyleIndex;
        int columnStylesMaxIndex = columnStyles.length - 1;
        while (uiData.isRowAvailable()) {
            columnStyleIndex = 0;
            String selectedClass = null;
            if (rowStylesMaxIndex >= 0) {
               selectedClass = rowStyles[rowStyleIndex];
            }
            
            Iterator childs = uiData.getChildren().iterator();
            Element tr = (Element) domContext.createElement(HTML.TR_ELEM);
            if (rowSelectorFound && toggleOnClick) {
            	 tr.setAttribute("onclick", rowSelectionFunctionName + "('" +
                       uiData.getRowIndex() + "', '"+ paramId +"');");            	
            }
            String id = uiComponent.getClientId(facesContext);
            tr.setAttribute(HTML.ID_ATTR, id);
            if (rowSelectorFound) {
                if (Boolean.TRUE.equals(rowSelector.getValue())){
                    selectedClass  += " "+ rowSelector.getSelectedClass();
                    tr.setAttribute(HTML.ONMOUSEOVER_ATTR, "this.className='"+ rowSelector.getSelectedMouseOverClass() +"'");                     
                } else {
                    selectedClass  += " "+ rowSelector.getStyleClass();
                    tr.setAttribute(HTML.ONMOUSEOVER_ATTR, "this.className='"+ rowSelector.getMouseOverClass() +"'");                    
                }
                tr.setAttribute(HTML.ONMOUSEOUT_ATTR, "this.className='"+ selectedClass +"'");
            }
            domContext.setCursorParent(tBody);
            tBody.appendChild(tr);
            selectedClass = getPortletAlternateRowClass(selectedClass, rowIndex);
            tr.setAttribute(HTML.CLASS_ATTR, selectedClass);
           
            if(rowStylesMaxIndex >= 0){ // Thanks denis tsyplakov
               if (++rowStyleIndex > rowStylesMaxIndex) {
                    rowStyleIndex = 0;
               }
            }
            int colNumber = 1;
            StringTokenizer columnWitdths =
                    getColumnWidths(uiData);
            while (childs.hasNext()) {
                UIComponent nextChild = (UIComponent) childs.next();
                if (nextChild.isRendered()) {
                    if (nextChild instanceof UIColumn) {
                        Element td = domContext.createElement(HTML.TD_ELEM);
                        if(!rowSelectorCodeAdded && scriptNode != null){
                            td.appendChild(scriptNode);
                        }
                        if (null != hiddenInputNode)  {
                            td.appendChild(hiddenInputNode);
                        }
                        writeColStyles(columnStyles, columnStylesMaxIndex,
                                       columnStyleIndex, td, colNumber++,
                                       uiComponent);
                        
                        if (isScrollable(uiComponent))  {
                            String width = "150px";
                            if( columnWitdths != null &&
                            columnWitdths.hasMoreTokens()) {
                                width = columnWitdths.nextToken();
                            }
                            td.setAttribute("style", "width:" + width +
                            ";overflow:hidden;");                            
                        }
                                                 
                        tr.appendChild(td);
                        // if column styles exist, then apply the appropriate one

                        if (++columnStyleIndex > columnStylesMaxIndex) {
                            columnStyleIndex = 0;
                        }

                        Node oldCursorParent = domContext.getCursorParent();
                        domContext.setCursorParent(td);
                        domContext.streamWrite(facesContext, uiComponent,
                                               domContext.getRootNode(), td);
                        
                        
                        encodeParentAndChildren(facesContext, nextChild);
                        domContext.setCursorParent(oldCursorParent);

                    } else if (nextChild instanceof UIColumns) {
                        String width = null;
                        if (isScrollable(uiComponent) &&
                            columnWitdths != null &&
                            columnWitdths.hasMoreTokens()) {
                            width = columnWitdths.nextToken();

                        }
                        nextChild.encodeBegin(facesContext);
                        encodeColumns(facesContext, nextChild, domContext, tr,
                                      columnStyles, columnStylesMaxIndex,
                                      columnStyleIndex, colNumber, width, hiddenInputNode);
                        nextChild.encodeEnd(facesContext);
                        colNumber = uiData.getColNumber();
                    }
                }

            }
            rowIndex++;
            countOfRowsDisplayed++;
            if ((numberOfRowsToDisplay > 0 &&
                    countOfRowsDisplayed >= numberOfRowsToDisplay) || 
                    (uiData.getRowCount() >=0 && rowIndex >= uiData.getRowCount())) {
                    break;
            }            
            
            uiData.setRowIndex(rowIndex);
        }
        uiData.setRowIndex(-1);
        domContext.stepOver();
        domContext.streamWrite(facesContext, uiComponent);
    }

    private void encodeColumns(FacesContext facesContext, UIComponent columns,
                               DOMContext domContext, Node tr,
                               String[] columnStyles, int columnStylesMaxIndex,
                               int columnStyleIndex, int colNumber,
                               String width,
                               Element rowSelectorHiddenField) throws IOException {
        UIColumns uiList = (UIColumns) columns;
        int rowIndex = uiList.getFirst();
        uiList.setRowIndex(rowIndex);
        int numberOfRowsToDisplay = uiList.getRows();
        int countOfRowsDisplayed = 0;
        domContext.setCursorParent(tr);
        Node oldCursorParent = domContext.getCursorParent();
        while (uiList.isRowAvailable()) {
            if ((numberOfRowsToDisplay > 0) &&
                (countOfRowsDisplayed >= numberOfRowsToDisplay)) {
                break;
            }
            Iterator childs;
            childs = columns.getChildren().iterator();
            Element td = domContext.createElement(HTML.TD_ELEM);
            if (null != rowSelectorHiddenField)  {
                td.appendChild(rowSelectorHiddenField);
            }
            if (width != null) {

                td.setAttribute("style",
                                "width:" + width + ";overflow:hidden;");
            }
            domContext.setCursorParent(oldCursorParent);
            tr.appendChild(td);
            while (childs.hasNext()) {
                UIComponent nextChild = (UIComponent) childs.next();
                if (nextChild.isRendered()) {
                    domContext.setCursorParent(td);
                    writeColStyles(columnStyles, columnStylesMaxIndex,
                                   columnStyleIndex, td, colNumber++,
                                   columns.getParent());
                    if (++columnStyleIndex > columnStylesMaxIndex) {
                        columnStyleIndex = 0;
                    }
                    encodeParentAndChildren(facesContext, nextChild);
                    domContext.setCursorParent(oldCursorParent);
                }
            }
            rowIndex++;
            countOfRowsDisplayed++;
            uiList.setRowIndex(rowIndex);
        }
        ((HtmlDataTable) uiList.getParent()).setColNumber(colNumber);
        uiList.setRowIndex(-1);
    }

    protected List getRenderedChildColumnsList(UIComponent component) {
        List results = new ArrayList();
        Iterator kids = component.getChildren().iterator();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            if (((kid instanceof UIColumn) && kid.isRendered()) ||
                kid instanceof UIColumns) {
                results.add(kid);
            }
        }
        return results;
    }

    protected boolean childColumnHasFacetWithName(UIComponent component,
                                                  String facetName) {
        Iterator childColumns = getRenderedChildColumnsIterator(component);
        while (childColumns.hasNext()) {
            UIComponent nextChildColumn = (UIComponent) childColumns.next();
            if (getFacetByName(nextChildColumn, facetName) != null) {
                return true;
            }
        }
        return false;
    }

    public static String getSelectedRowParameterName(String dataTableId) {
        // strip the last ':' because the Datatables client Id changes for each iterator
        int i = dataTableId.lastIndexOf(":");
        dataTableId = dataTableId.substring(0, i);
        return dataTableId + SELECTED_ROWS;
    }


    public static RowSelector getRowSelector(UIComponent comp) {
        if (comp instanceof RowSelector) {
            return (RowSelector) comp;
        }
        Iterator iter = comp.getChildren().iterator();
        while (iter.hasNext()) {
            UIComponent kid = (UIComponent) iter.next();
            if (kid instanceof HtmlDataTable){
                // Nested HtmlDataTable might be a peer of
                //  a later, valid RowSelector, so don't
                //  traverse in, but keep looking
                continue;
            }
            RowSelector rs = getRowSelector(kid);
            if (rs != null) {
                if(!rs.isRendered())
                    return null;
                return rs;
            }
        }
        return null;
    }

    private int rowSelectorNumber(FacesContext context){
        Map m = context.getExternalContext().getRequestMap();
        String key = RowSelector.class.getName() + "-Selector";
        Integer I = (Integer)m.get(key);
        int i = 0;
        if(I != null){
            i = I.intValue();
            i++;
        }

        I = new Integer(i);
        m.put(key, I);
        return i;
    }

    protected int getNumberOfChildColumns(UIComponent component) {
        int size = getRenderedChildColumnsList(component).size();
        Iterator it = getRenderedChildColumnsList(component).iterator();
        while (it.hasNext()) {
        	UIComponent uiComponent = (UIComponent)it.next(); 
        	if (uiComponent instanceof UIColumns) {
        		size +=((UIColumns)uiComponent).getRowCount();
        	}
        }
        return size;
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
    
    public String[] getHeaderStyleClasses(UIComponent uiComponent) {
        String headerClass = getHeaderClass(uiComponent).
        replaceAll(CSS_DEFAULT.TABLE_STYLE_CLASS + CSS_DEFAULT.TABLE_HEADER_CLASS, "");    	
       String[] headerClasses = getStyleClasses(uiComponent, "headerClasses");
       for (int i=0; i < headerClasses.length; i++) {
           headerClasses[i] = Util.getQualifiedStyleClass(uiComponent,
                   headerClasses[i],
                         CSS_DEFAULT.TABLE_COLUMN_HEADER_CLASS,
                         "headerClasses"                            
                                      )+ ((headerClass.length() > 0)
                                              ? headerClass : "");
       }
       return headerClasses;       
    }
    
    String getPortletAlternateRowClass(String selectedClass, int rowIndex) {
        String rowClass = PORTLET_CSS_DEFAULT.PORTLET_SECTION_ALTERNATE;
        if ((rowIndex % 2) == 0) {
            rowClass = PORTLET_CSS_DEFAULT.PORTLET_SECTION_BODY;
        }
        if (selectedClass.indexOf(' ') > 1) {
            return selectedClass.replaceFirst(" ", " " + CoreUtils.getPortletStyleClass(rowClass + " "));
        } else {
            return selectedClass += CoreUtils.getPortletStyleClass("" + rowClass);
        }
    }
    
}
