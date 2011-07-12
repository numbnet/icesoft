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
        title = "example.compat.media.windows.title",
        description = "example.compat.media.windows.description",
        example = "/resources/examples/compat/media/mediaWindows.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="mediaWindows.xhtml",
                    resource = "/resources/examples/compat/"+
                               "media/mediaWindows.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MediaWindows.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/media/MediaWindows.java")
        }
)
@ManagedBean(name= MediaWindows.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MediaWindows extends ComponentExampleImpl<MediaWindows> implements Serializable {
	
	public static final String BEAN_NAME = "mediaWindows";
	
	public MediaWindows() {
		super(MediaWindows.class);
	}
}
