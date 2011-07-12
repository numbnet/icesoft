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
        title = "example.compat.paginator.fast.title",
        description = "example.compat.paginator.fast.description",
        example = "/resources/examples/compat/paginator/paginatorFast.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="paginatorFast.xhtml",
                    resource = "/resources/examples/compat/"+
                               "paginator/paginatorFast.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="PaginatorFast.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/paginator/PaginatorFast.java")
        }
)
@ManagedBean(name= PaginatorFast.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PaginatorFast extends ComponentExampleImpl<PaginatorFast> implements Serializable {
	
	public static final String BEAN_NAME = "paginatorFast";
	
	private int stepCount = 3;
	
	public PaginatorFast() {
		super(PaginatorFast.class);
	}
	
	public int getStepCount() { return stepCount; }
	
	public void setStepCount(int stepCount) { this.stepCount = stepCount; }
}
