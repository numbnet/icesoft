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
        title = "example.compat.divider.content.title",
        description = "example.compat.divider.content.description",
        example = "/resources/examples/compat/divider/dividerContent.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dividerContent.xhtml",
                    resource = "/resources/examples/compat/"+
                               "divider/dividerContent.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DividerContent.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/divider/DividerContent.java")
        }
)
@ManagedBean(name= DividerContent.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DividerContent extends ComponentExampleImpl<DividerContent> implements Serializable {
	
	public static final String BEAN_NAME = "dividerContent";
	
	private String[] availableUrls = new String[] {
	    "http://www.icefaces.org/",
	    "http://wiki.icefaces.org/",
	    "http://auctionmonitor.icefaces.org/",
	    "http://memorygame.icefaces.org/",
	    "http://www.google.com/",
	    "http://www.yahoo.com/",
	    "http://www.bing.com/",
	    "http://www.ask.com/"
	};
	private String leftUrl = availableUrls[4];
	private String rightUrl = availableUrls[7];
	
	public DividerContent() {
		super(DividerContent.class);
	}
	
	public String[] getAvailableUrls() { return availableUrls; }
	public String getLeftUrl() { return leftUrl; }
	public String getRightUrl() { return rightUrl; }
	
	public void setLeftUrl(String leftUrl) { this.leftUrl = leftUrl; }
	public void setRightUrl(String rightUrl) { this.rightUrl = rightUrl; }
}
