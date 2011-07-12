package org.icefaces.samples.showcase.example.ace.dataTable;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;
import org.icefaces.samples.showcase.example.compat.dataTable.Car;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import javax.faces.model.SelectItem;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import org.icefaces.ace.event.SelectEvent;
import org.icefaces.ace.event.UnselectEvent;

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
        title = "example.ace.dataTable.filtering.title",
        description = "example.ace.dataTable.filtering.description",
        example = "/resources/examples/ace/dataTable/dataTableFiltering.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataTableFiltering.xhtml",
                    resource = "/resources/examples/ace/dataTable/dataTableFiltering.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DataTableFiltering.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/dataTable/DataTableFiltering.java")
        }
)
@ManagedBean(name= DataTableFiltering.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableFiltering extends ComponentExampleImpl<DataTableFiltering> implements Serializable {
    public static final String BEAN_NAME = "dataTableFiltering";
    
    public DataTableFiltering() {
        super(DataTableFiltering.class);
    }
}
