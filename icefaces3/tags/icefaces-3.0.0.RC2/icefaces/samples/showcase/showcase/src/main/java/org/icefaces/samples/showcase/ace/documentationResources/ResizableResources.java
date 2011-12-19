package org.icefaces.samples.showcase.ace.documentationResources;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import org.icefaces.samples.showcase.metadata.context.ResourceRootPath;

@ExampleResources(
        resources ={
                
                // WIKI Resources
                @ExampleResource(type = ResourceType.wiki,
                    title="ace:resizable",
                    resource = ResourceRootPath.FOR_WIKI + "Resizable"),

                // TLD Resources
                @ExampleResource(type = ResourceType.tld,
                    title="ace:resizable",
                    resource = ResourceRootPath.FOR_ACE_TLD + "resizable.html")
        }
)
@ManagedBean(name= ResizableResources.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ResizableResources extends ComponentExampleImpl<ResizableResources> implements Serializable {
    public static final String BEAN_NAME = "resizableResources";
    public ResizableResources()
    {
        super(ResizableResources.class);
    }
}