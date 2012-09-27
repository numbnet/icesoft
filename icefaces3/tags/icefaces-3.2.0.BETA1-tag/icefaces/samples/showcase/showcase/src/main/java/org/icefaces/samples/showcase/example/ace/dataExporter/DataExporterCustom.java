/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
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

package org.icefaces.samples.showcase.example.ace.dataExporter;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;

import java.util.List;
import java.util.ArrayList;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.icefaces.ace.component.datatable.DataTable;
import org.icefaces.ace.component.panelexpansion.PanelExpansion;
import org.icefaces.ace.component.dataexporter.OuterTableCSVExporter;
import org.icefaces.ace.component.dataexporter.InnerTableCSVExporter;

@ComponentExample(
        parent = DataExporterBean.BEAN_NAME,
        title = "example.ace.dataExporter.custom.title",
        description = "example.ace.dataExporter.custom.description",
        example = "/resources/examples/ace/dataExporter/dataExporterCustom.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataExporterCustom.xhtml",
                    resource = "/resources/examples/ace/dataExporter/dataExporterCustom.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DataExporterCustom.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/dataExporter/DataExporterCustom.java")
        }
)
@ManagedBean(name= DataExporterCustom.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataExporterCustom extends ComponentExampleImpl<DataExporterCustom> implements Serializable {
	public static final String BEAN_NAME = "dataExporterCustom";
	
	private static final String OUTER_TABLE_ID = "carTable";
	private static final String LABEL_EXPRESSION = "#{car.id}";
	
    public DataExporterCustom() { 
		super(DataExporterCustom.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
	
	public static List<CustomCar> getRandomData() {
		CustomVehicleGenerator generator = new CustomVehicleGenerator();
		return generator.getRandomCars(30);
	}
	
    private List<CustomCar> carsData = getRandomData();
	public List<CustomCar> getCarsData() { return carsData; }
    public void setCarsData(List<CustomCar> carsData) { this.carsData = carsData; }
	
	private List<String> selectedItems;
	public List<String> getSelectedItems() { return selectedItems; }
	public void setSelectedItems(List<String> selectedItems) { this.selectedItems = selectedItems; }
	
	public List<SelectItem> getSelectItems() {
		List<SelectItem> selectItems = new ArrayList<SelectItem>();
		FacesContext facesContext = FacesContext.getCurrentInstance();
		UIComponent table = findComponentCustom(facesContext.getViewRoot(), OUTER_TABLE_ID);
		if (table != null) {
			DataTable outerTable = (DataTable) table;
			int rowCount = outerTable.getRowCount();
			int first = outerTable.getFirst();
			int size = first + outerTable.getRows();
			size = size > rowCount ? rowCount : size;
			String rowIndexVar = outerTable.getRowIndexVar();
			rowIndexVar = rowIndexVar == null ? "" : rowIndexVar;
			for (int i = first; i < size; i++) {
				outerTable.setRowIndex(i);
				if (!"".equals(rowIndexVar)) {
					facesContext.getExternalContext().getRequestMap().put(rowIndexVar, i);
				}
				Object property = facesContext.getApplication().evaluateExpressionGet(facesContext, LABEL_EXPRESSION, Object.class);
				String label = property != null ? property.toString() : "no label";
				PanelExpansion pe = outerTable.getPanelExpansion();
				if (pe != null) {
					for (UIComponent kid : pe.getChildren()) {
						if (kid instanceof DataTable) {
							selectItems.add(new SelectItem(kid.getClientId(), label));
							break;
						}
					}
				}
			}
			outerTable.setRowIndex(-1);
		} else {
			throw new FacesException("Table with id '" + OUTER_TABLE_ID + "' not found in view.");
		}
		return selectItems;
	}
	
	public Object getCustomExporter() {
		return new OuterTableCSVExporter(selectedItems);
	}
	
	private UIComponent findComponentCustom(UIComponent base, String id) {

		if (base.getId() != null && base.getId().equals(id)) return base;
		List<UIComponent> children = base.getChildren();
		UIComponent result = null;
		for (UIComponent child : children) {
			result = findComponentCustom(child, id);
			if (result != null) break;
		}
		return result;
	}
}
