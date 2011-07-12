package org.icefaces.samples.showcase.example.compat.progress;

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
        parent = ProgressBean.BEAN_NAME,
        title = "example.compat.progress.indeterminate.title",
        description = "example.compat.progress.indeterminate.description",
        example = "/resources/examples/compat/progress/progressIndeterminate.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="progressIndeterminate.xhtml",
                    resource = "/resources/examples/compat/"+
                               "progress/progressIndeterminate.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ProgressIndeterminate.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/progress/ProgressIndeterminate.java")
        }
)
@ManagedBean(name= ProgressIndeterminate.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ProgressIndeterminate extends ComponentExampleImpl<ProgressIndeterminate> implements Serializable {
	
	public static final String BEAN_NAME = "progressIndeterminate";
	
	public ProgressIndeterminate() {
		super(ProgressIndeterminate.class);
	}
	
	public void startTask(ActionEvent event) {
	    LongTaskManager threadBean =
	        (LongTaskManager)FacesUtils.getManagedBean(LongTaskManager.BEAN_NAME);
	    
	    threadBean.startThread(40, 1, 700);
	}
}
