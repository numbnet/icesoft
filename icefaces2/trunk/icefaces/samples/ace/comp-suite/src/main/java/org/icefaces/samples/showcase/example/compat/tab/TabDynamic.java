package org.icefaces.samples.showcase.example.compat.tab;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
        parent = TabBean.BEAN_NAME,
        title = "example.compat.tab.dynamic.title",
        description = "example.compat.tab.dynamic.description",
        example = "/resources/examples/compat/tab/tabDynamic.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="tabDynamic.xhtml",
                    resource = "/resources/examples/compat/"+
                               "tab/tabDynamic.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TabDynamic.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/tab/TabDynamic.java")
        }
)
@ManagedBean(name= TabDynamic.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TabDynamic extends ComponentExampleImpl<TabDynamic> implements Serializable {
	
	public static final String BEAN_NAME = "tabDynamic";
	
	private List<TabObject> tabList = generateTabList(5);
	private TabObject toAdd = new TabObject();
	private String toRemove;
	
	public TabDynamic() {
		super(TabDynamic.class);
	}
	
	public List<TabObject> getTabList() { return tabList; }
	public TabObject getToAdd() { return toAdd; }
	public String getToRemove() { return toRemove; }
	
	public void setTabList(List<TabObject> tabList) { this.tabList = tabList; }
	public void setToAdd(TabObject toAdd) { this.toAdd = toAdd; }
	public void setToRemove(String toRemove) { this.toRemove = toRemove; }
	
	private List<TabObject> generateTabList(int count) {
	    List<TabObject> toReturn = new ArrayList<TabObject>(count);
	    
	    for (int i = 0; i < count; i++) {
	        toReturn.add(new TabObject("Tab " + i,
	                                   "Some basic content for tab " + i +
	                                    ". This could just as easily be a URL to dynamically include."));
	    }
	    
	    return toReturn;
	}
	
	public void addTab(ActionEvent event) {
	    tabList.add(toAdd);
	    
	    toAdd = new TabObject();
	}
	
	public String removeTab() {
	    if (!FacesUtils.isBlank(toRemove)) {
	        tabList.remove(Integer.parseInt(toRemove));
	    }
	    
	    if (tabList.size() == 0) {
	        restoreDefault(null);
	    }
	    
	    return null;
	}
	
	public void restoreDefault(ActionEvent event) {
	    tabList = generateTabList(5);
	}
}
