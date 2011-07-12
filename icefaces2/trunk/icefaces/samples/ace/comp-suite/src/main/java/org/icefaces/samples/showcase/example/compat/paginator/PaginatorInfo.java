package org.icefaces.samples.showcase.example.compat.paginator;

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
        parent = PaginatorBean.BEAN_NAME,
        title = "example.compat.paginator.info.title",
        description = "example.compat.paginator.info.description",
        example = "/resources/examples/compat/paginator/paginatorInfo.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="paginatorInfo.xhtml",
                    resource = "/resources/examples/compat/"+
                               "paginator/paginatorInfo.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="PaginatorInfo.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/paginator/PaginatorInfo.java")
        }
)
@ManagedBean(name= PaginatorInfo.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PaginatorInfo extends ComponentExampleImpl<PaginatorInfo> implements Serializable {
	
	public static final String BEAN_NAME = "paginatorInfo";
	
	public PaginatorInfo() {
		super(PaginatorInfo.class);
	}
}
