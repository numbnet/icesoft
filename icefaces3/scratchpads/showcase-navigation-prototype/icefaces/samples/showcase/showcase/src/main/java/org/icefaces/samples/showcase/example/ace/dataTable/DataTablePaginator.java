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

package org.icefaces.samples.showcase.example.ace.dataTable;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import javax.faces.model.SelectItem;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@ComponentExample(
        parent = DataTableBean.BEAN_NAME,
        title = "example.ace.dataTable.paginator.title",
        description = "example.ace.dataTable.paginator.description",
        example = "/resources/examples/ace/dataTable/dataTablePaginator.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataTablePaginator.xhtml",
                    resource = "/resources/examples/ace/dataTable/dataTablePaginator.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DataTablePaginator.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/dataTable/DataTablePaginator.java")
        }
)
@ManagedBean(name= DataTablePaginator.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTablePaginator extends ComponentExampleImpl<DataTablePaginator> implements Serializable {
    public static final String BEAN_NAME = "dataTablePaginator";
    
    private static final SelectItem[] POSITION_AVAILABLE = { new SelectItem("bottom", "Bottom"),
                                                           new SelectItem("top", "Top"),
                                                           new SelectItem("both", "Both") };
    
    private boolean paginator = true;
    private String position = POSITION_AVAILABLE[0].getValue().toString();

    private int rows = 10;
    private int startPage = 1;

    public DataTablePaginator() {
        super(DataTablePaginator.class);
    }
    
    public boolean getPaginator() { return paginator; }
    public String getPosition() { return position; }
    public int getRows() { return rows; }
    public int getStartPage() { return startPage; }
    public SelectItem[] getPositionAvailable() { return POSITION_AVAILABLE; }
    public int getStartPageMaximum() {
        return (int)Math.ceil(30.0/(double)rows);
    }
    
    public void setPaginator(boolean paginator) { this.paginator = paginator; }
    public void setPosition(String position) { this.position = position; }
    public void setRows(int rows) { this.rows = rows; }
    public void setStartPage(int startPage) { this.startPage = startPage; }
}
