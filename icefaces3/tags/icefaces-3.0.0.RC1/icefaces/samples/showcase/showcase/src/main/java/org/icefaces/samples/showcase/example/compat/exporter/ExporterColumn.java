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

package org.icefaces.samples.showcase.example.compat.exporter;

import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.icefaces.samples.showcase.util.FacesUtils;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = ExporterBean.BEAN_NAME,
        title = "example.compat.exporter.column.title",
        description = "example.compat.exporter.column.description",
        example = "/resources/examples/compat/exporter/exporterColumn.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="exporterColumn.xhtml",
                    resource = "/resources/examples/compat/"+
                               "exporter/exporterColumn.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ExporterColumn.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/exporter/ExporterColumn.java")
        }
)
@ManagedBean(name= ExporterColumn.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ExporterColumn extends ComponentExampleImpl<ExporterColumn> implements Serializable {
	
	public static final String BEAN_NAME = "exporterColumn";
	
	private SelectItem[] availableColumns = new SelectItem[] {
	    new SelectItem("0", "ID"),
	    new SelectItem("1", "Name"),
	    new SelectItem("2", "Chassis"),
	    new SelectItem("3", "Weight"),
	    new SelectItem("4", "Acceleration"),
	    new SelectItem("5", "MPG"),
	    new SelectItem("6", "Cost"),
	};
	private String[] includedColumns = new String[] {
	    availableColumns[0].getValue().toString(),
	    availableColumns[1].getValue().toString()
	};
	
	public ExporterColumn() {
		super(ExporterColumn.class);
	}
	
	public SelectItem[] getAvailableColumns() { return availableColumns; }
	public String[] getIncludedColumns() { return includedColumns; }
	public String getColumnString() { return FacesUtils.join(includedColumns); }
	
	public void setIncludedColumns(String[] includedColumns) { this.includedColumns = includedColumns; }
}
