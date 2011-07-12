package org.icefaces.samples.showcase.example.ace.dataExporter;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
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
        title = "example.ace.dataExporter.title",
        description = "example.ace.dataExporter.description",
        example = "/resources/examples/ace/dataExporter/dataExporter.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataExporter.xhtml",
                    resource = "/resources/examples/ace/dataExporter/dataExporter.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DataExporterBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/dataExporter/DataExporterBean.java")
        }
)
@Menu(
	title = "menu.ace.dataExporter.subMenu.title",
	menuLinks = {
	        @MenuLink(title = "menu.ace.dataExporter.subMenu.main",
	                isDefault = true,
                    exampleBeanName = DataExporterBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.dataExporter.subMenu.columns",
                    exampleBeanName = DataExporterColumns.BEAN_NAME)
    }
)
@ManagedBean(name= DataExporterBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataExporterBean extends ComponentExampleImpl<DataExporterBean> implements Serializable {
    public static final String BEAN_NAME = "dataExporterBean";
    
    private String type = "csv";

    public DataExporterBean() {
        super(DataExporterBean.class);
    }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}