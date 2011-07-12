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
        title = "example.compat.media.quicktime.title",
        description = "example.compat.media.quicktime.description",
        example = "/resources/examples/compat/media/mediaQuicktime.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="mediaQuicktime.xhtml",
                    resource = "/resources/examples/compat/"+
                               "media/mediaQuicktime.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MediaQuicktime.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/media/MediaQuicktime.java")
        }
)
@ManagedBean(name= MediaQuicktime.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MediaQuicktime extends ComponentExampleImpl<MediaQuicktime> implements Serializable {
	
	public static final String BEAN_NAME = "mediaQuicktime";
	
	public MediaQuicktime() {
		super(MediaQuicktime.class);
	}
}
