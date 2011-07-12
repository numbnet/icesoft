package org.icefaces.samples.showcase.example.compat.positioned;

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
        parent = PositionedBean.BEAN_NAME,
        title = "example.compat.positioned.dynamic.title",
        description = "example.compat.positioned.dynamic.description",
        example = "/resources/examples/compat/positioned/positionedDynamic.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="positionedDynamic.xhtml",
                    resource = "/resources/examples/compat/"+
                               "positioned/positionedDynamic.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="PositionedDynamic.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/positioned/PositionedDynamic.java")
        }
)
@ManagedBean(name= PositionedDynamic.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PositionedDynamic extends ComponentExampleImpl<PositionedDynamic> implements Serializable {
	
	public static final String BEAN_NAME = "positionedDynamic";
	
	private String toAdd;
	private String toRemove;
	
	public PositionedDynamic() {
		super(PositionedDynamic.class);
	}
	
	public String getToAdd() { return toAdd; }
	public String getToRemove() { return toRemove; }
	
	public void setToAdd(String toAdd) { this.toAdd = toAdd; }
	public void setToRemove(String toRemove) { this.toRemove = toRemove; }
	
	/**
	 * Add the current item
	 */
	public void addItem(ActionEvent event) {
	    if (!FacesUtils.isBlank(toAdd)) {
	        PositionedData.addFood(toAdd);
	        
	        // Reset the field once it's been added to the list
	        toAdd = null;
	    }
	}
	
	/**
	 * Remove the current item
	 *
	 * This needs to be an action (instead of an actionListener)
	 *  because f:setPropertyActionListener is used, and that value
	 *  is set before the action is fired, but NOT before actionListeners are fired
	 */
	public String removeItem() {
	    // Check if we have a valid item index to remove
	    if (!FacesUtils.isBlank(toRemove)) {
	        // Attempt to remove the item
	        PositionedData.removeFood(Integer.parseInt(toRemove));
	        
	        // If we have no items left then restore the original list
	        if (PositionedData.empty()) {
	            PositionedData.defaultFoods();
	        }
	    }
	    
	    return null;
	}
	
	public void restoreDefault(ActionEvent event) {
	    PositionedData.defaultFoods();
	}
}
