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

/*
 * ICESOFT COMMERCIAL SOURCE CODE LICENSE V 1.1
 *
 * The contents of this file are subject to the ICEsoft Commercial Source
 * Code License Agreement V1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the
 * License at
 * http://www.icesoft.com/license/commercial-source-v1.1.html
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * Copyright 2009-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 */

@ComponentExample(
        parent = DataTableBean.BEAN_NAME,
        title = "example.ace.dataTable.scrolling.title",
        description = "example.ace.dataTable.scrolling.description",
        example = "/resources/examples/ace/dataTable/dataTableScrolling.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataTableScrolling.xhtml",
                    resource = "/resources/examples/ace/dataTable/dataTableScrolling.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DataTableScrolling.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/dataTable/DataTableScrolling.java")
        }
)
@ManagedBean(name= DataTableScrolling.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableScrolling extends ComponentExampleImpl<DataTableScrolling> implements Serializable {
    public static final String BEAN_NAME = "dataTableScrolling";
    
    private boolean scrolling = true;
    private int height = 200;

    public DataTableScrolling() {
        super(DataTableScrolling.class);
    }
    
    public boolean getScrolling() { return scrolling; }
    public int getHeight() { return height; }
    
    public void setScrolling(boolean scrolling) { this.scrolling = scrolling; }
    public void setHeight(int height) { this.height = height; }
}
