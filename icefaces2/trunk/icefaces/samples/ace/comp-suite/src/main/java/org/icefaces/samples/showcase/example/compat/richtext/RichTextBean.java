package org.icefaces.samples.showcase.example.compat.richtext;

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
        title = "example.compat.richtext.title",
        description = "example.compat.richtext.description",
        example = "/resources/examples/compat/richtext/richtext.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="richtext.xhtml",
                    resource = "/resources/examples/compat/"+
                               "richtext/richtext.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="RichTextBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/richtext/RichTextBean.java")
        }
)
@Menu(
	title = "menu.compat.richtext.subMenu.title",
	menuLinks = {
            @MenuLink(title = "menu.compat.richtext.subMenu.main",
                    isDefault = true,
                    exampleBeanName = RichTextBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.richtext.subMenu.save",
                    exampleBeanName = RichTextSave.BEAN_NAME),
            @MenuLink(title = "menu.compat.richtext.subMenu.language",
                    exampleBeanName = RichTextLanguage.BEAN_NAME),
            @MenuLink(title = "menu.compat.richtext.subMenu.skin",
                    exampleBeanName = RichTextSkin.BEAN_NAME),
            @MenuLink(title = "menu.compat.richtext.subMenu.toolbar",
                    exampleBeanName = RichTextToolbar.BEAN_NAME),
            @MenuLink(title = "menu.compat.richtext.subMenu.disable",
                    exampleBeanName = RichTextDisable.BEAN_NAME)
})
@ManagedBean(name= RichTextBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class RichTextBean extends ComponentExampleImpl<RichTextBean> implements Serializable {
	
	public static final String BEAN_NAME = "richtext";
	private static final String DEFAULT_HEIGHT = "300";
	
	private String text;
	
	public RichTextBean() {
		super(RichTextBean.class);
	}
	
	public String getDefaultHeight() { return DEFAULT_HEIGHT; }
	
	public String getText() { return text; }
	
	public void setText(String text) { this.text = text; }
}
