package org.icefaces.samples.showcase.example.compat.exporter;

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
        title = "example.compat.exporter.title",
        description = "example.compat.exporter.description",
        example = "/resources/examples/compat/exporter/exporter.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="exporter.xhtml",
                    resource = "/resources/examples/compat/"+
                               "exporter/exporter.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ExporterBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/exporter/ExporterBean.java")
        }
)
@Menu(
	title = "menu.compat.exporter.subMenu.title",
	menuLinks = {
            @MenuLink(title = "menu.compat.exporter.subMenu.main",
                    isDefault = true,
                    exampleBeanName = ExporterBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.exporter.subMenu.type",
                    exampleBeanName = ExporterType.BEAN_NAME),
            @MenuLink(title = "menu.compat.exporter.subMenu.column",
                    exampleBeanName = ExporterColumn.BEAN_NAME),
            @MenuLink(title = "menu.compat.exporter.subMenu.pagination",
                    exampleBeanName = ExporterPagination.BEAN_NAME),
            @MenuLink(title = "menu.compat.exporter.subMenu.label",
                    exampleBeanName = ExporterLabel.BEAN_NAME)
})
@ManagedBean(name= ExporterBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ExporterBean extends ComponentExampleImpl<ExporterBean> implements Serializable {
	
	public static final String BEAN_NAME = "exporter";
	
	public ExporterBean() {
		super(ExporterBean.class);
	}
}
