package org.icefaces.samples.showcase.example.compat.selector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.icefaces.samples.showcase.example.compat.dataTable.*;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        title = "example.compat.selector.title",
        description = "example.compat.selector.description",
        example = "/resources/examples/compat/selector/selector.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="selector.xhtml",
                    resource = "/resources/examples/compat/"+
                               "selector/selector.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="SelectorBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/selector/SelectorBean.java")
        }
)
@Menu(
	title = "menu.compat.selector.subMenu.title",
	menuLinks = {
            @MenuLink(title = "menu.compat.selector.subMenu.main",
                    isDefault = true,
                    exampleBeanName = SelectorBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.selector.subMenu.type",
                    exampleBeanName = SelectorType.BEAN_NAME),
            @MenuLink(title = "menu.compat.selector.subMenu.events",
                    exampleBeanName = SelectorEvents.BEAN_NAME),
            @MenuLink(title = "menu.compat.selector.subMenu.toggle",
                    exampleBeanName = SelectorToggle.BEAN_NAME),
            @MenuLink(title = "menu.compat.selector.subMenu.pre",
                    exampleBeanName = SelectorPre.BEAN_NAME)
})
@ManagedBean(name= SelectorBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class SelectorBean extends ComponentExampleImpl<SelectorBean> implements Serializable {
	
	public static final String BEAN_NAME = "selector";
	
	private List<SelectableCar> data = wrapList(DataTableData.CARS);
	
	public SelectorBean() {
		super(SelectorBean.class);
	}
	
	public List<SelectableCar> getData() { return data; }
	
	public void setData(List<SelectableCar> data) { this.data = data; }
	
	private List<SelectableCar> wrapList(List<Car> toWrap) {
	    List<SelectableCar> toReturn = new ArrayList<SelectableCar>(toWrap.size());
	    
	    for (Car currentItem : toWrap) {
	        toReturn.add(new SelectableCar(currentItem));
	    }
	    
	    return toReturn;
	}
}
