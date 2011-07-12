package org.icefaces.samples.showcase.example.compat.dataTable;

import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = DataTableBean.BEAN_NAME,
        title = "example.compat.dataTable.hide.title",
        description = "example.compat.dataTable.hide.description",
        example = "/resources/examples/compat/dataTable/dataTableHide.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataTableHide.xhtml",
                    resource = "/resources/examples/compat/"+
                               "dataTable/dataTableHide.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DataTableHide.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/dataTable/DataTableHide.java")
        }
)
@ManagedBean(name= DataTableHide.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableHide extends ComponentExampleImpl<DataTableHide> implements Serializable {
	
	public static final String BEAN_NAME = "dataTableHide";
	
    private boolean renderId = false;
	private boolean renderName = true;
	private boolean renderChassis = true;
	private boolean renderWeight = true;
	private boolean renderAcceleration = false;
	private boolean renderMpg = false;
	private boolean renderCost = true;
	
	public DataTableHide() {
		super(DataTableHide.class);
	}

	public boolean isRenderId() {
		return renderId;
	}
	public void setRenderId(boolean renderId) {
		this.renderId = renderId;
	}
	public boolean isRenderName() {
		return renderName;
	}
	public void setRenderName(boolean renderName) {
		this.renderName = renderName;
	}
	public boolean isRenderChassis() {
		return renderChassis;
	}
	public void setRenderChassis(boolean renderChassis) {
		this.renderChassis = renderChassis;
	}
	public boolean isRenderWeight() {
		return renderWeight;
	}
	public void setRenderWeight(boolean renderWeight) {
		this.renderWeight = renderWeight;
	}
	public boolean isRenderAcceleration() {
		return renderAcceleration;
	}
	public void setRenderAcceleration(boolean renderAcceleration) {
		this.renderAcceleration = renderAcceleration;
	}
	public boolean isRenderMpg() {
		return renderMpg;
	}
	public void setRenderMpg(boolean renderMpg) {
		this.renderMpg = renderMpg;
	}
	public boolean isRenderCost() {
		return renderCost;
	}
	public void setRenderCost(boolean renderCost) {
		this.renderCost = renderCost;
	}
	
	public String toggleColumn() {
	    return null;
	}
	
	public void showAll(ActionEvent event) {
	    applyAll(true);
	}
	
	public void hideAll(ActionEvent event) {
	    applyAll(false);
	}
	
	private void applyAll(boolean set) {
	    renderId = set;
	    renderName = set;
	    renderChassis = set;
	    renderWeight = set;
	    renderAcceleration = set;
	    renderMpg = set;
	    renderCost = set;
	}
}
