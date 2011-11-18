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

package org.icefaces.samples.showcase.example.ace.dataExporter;

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
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import org.icefaces.ace.event.SelectEvent;
import org.icefaces.ace.event.UnselectEvent;

@ComponentExample(
        parent = DataExporterBean.BEAN_NAME,
        title = "example.ace.dataExporter.columns.title",
        description = "example.ace.dataExporter.columns.description",
        example = "/resources/examples/ace/dataExporter/dataExporterColumns.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataExporterColumns.xhtml",
                    resource = "/resources/examples/ace/dataExporter/dataExporterColumns.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DataExporterColumns.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/dataExporter/DataExporterColumns.java")
        }
)
@ManagedBean(name= DataExporterColumns.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataExporterColumns extends ComponentExampleImpl<DataExporterColumns> implements Serializable {
    public static final String BEAN_NAME = "dataExporterColumns";
    
    private String[] chosenColumns = {"0", "3", "5"};
    private String chosenColumnsString = "0,3,5";
    
    public DataExporterColumns() {
        super(DataExporterColumns.class);
    }
    
    public String[] getChosenColumns() { return chosenColumns; }
    public String getChosenColumnsString() { return chosenColumnsString; }
    
    public void setChosenColumns(String[] chosenColumns) {
        this.chosenColumns = chosenColumns;
        
        chosenColumnsString = parseArrayToCommas(this.chosenColumns);
    }
    public void setChosenColumnsString(String chosenColumnsString) { this.chosenColumnsString = chosenColumnsString; }
    
    private String parseArrayToCommas(String[] array) {
        if ((array != null) && (array.length > 0)) {
            Arrays.sort(array);
            
            StringBuilder sb = new StringBuilder(array.length * 2);
            for (String currentColumn : array) {
                sb.append(currentColumn);
                sb.append(",");
            }
            sb.deleteCharAt(sb.length()-1); // Remove the trailing comma
            
            return sb.toString();
        }
        
        return "";
    }
}
