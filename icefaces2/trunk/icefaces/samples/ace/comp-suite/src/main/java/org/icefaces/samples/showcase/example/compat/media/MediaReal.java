package org.icefaces.samples.showcase.example.compat.media;

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
        parent = MediaBean.BEAN_NAME,
        title = "example.compat.media.real.title",
        description = "example.compat.media.real.description",
        example = "/resources/examples/compat/media/mediaReal.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="mediaReal.xhtml",
                    resource = "/resources/examples/compat/"+
                               "media/mediaReal.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MediaReal.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/media/MediaReal.java")
        }
)
@ManagedBean(name= MediaReal.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MediaReal extends ComponentExampleImpl<MediaReal> implements Serializable {
	
	public static final String BEAN_NAME = "mediaReal";
	
	public MediaReal() {
		super(MediaReal.class);
	}
}
