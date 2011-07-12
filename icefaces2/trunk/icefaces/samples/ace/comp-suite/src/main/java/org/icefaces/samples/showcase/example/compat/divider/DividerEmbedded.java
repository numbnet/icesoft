package org.icefaces.samples.showcase.example.compat.divider;

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
        parent = DividerBean.BEAN_NAME,
        title = "example.compat.divider.embedded.title",
        description = "example.compat.divider.embedded.description",
        example = "/resources/examples/compat/divider/dividerEmbedded.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dividerEmbedded.xhtml",
                    resource = "/resources/examples/compat/"+
                               "divider/dividerEmbedded.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DividerEmbedded.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/divider/DividerEmbedded.java")
        }
)
@ManagedBean(name= DividerEmbedded.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DividerEmbedded extends ComponentExampleImpl<DividerEmbedded> implements Serializable {
	
	public static final String BEAN_NAME = "dividerEmbedded";
	
	private boolean embedTop = true;
	private boolean embedBottom = true;
	
	public DividerEmbedded() {
		super(DividerEmbedded.class);
	}
	
	public boolean getEmbedTop() { return embedTop; }
	public boolean getEmbedBottom() { return embedBottom; }
	
	public void setEmbedTop(boolean embedTop) { this.embedTop = embedTop; }
	public void setEmbedBottom(boolean embedBottom) { this.embedBottom = embedBottom; }
}
