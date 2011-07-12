package org.icefaces.samples.showcase.example.compat.selector;

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
        parent = SelectorBean.BEAN_NAME,    
        title = "example.compat.selector.pre.title",
        description = "example.compat.selector.pre.description",
        example = "/resources/examples/compat/selector/selectorPre.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="selectorPre.xhtml",
                    resource = "/resources/examples/compat/"+
                               "selector/selectorPre.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="SelectorPre.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/selector/SelectorPre.java")
        }
)
@ManagedBean(name= SelectorPre.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class SelectorPre extends ComponentExampleImpl<SelectorPre> implements Serializable {
	
	public static final String BEAN_NAME = "selectorPre";
	
	private boolean enable = false;
	
	public SelectorPre() {
		super(SelectorPre.class);
	}
	
	public boolean getEnable() { return enable; }
	
	public void setEnable(boolean enable) { this.enable = enable; }
}
