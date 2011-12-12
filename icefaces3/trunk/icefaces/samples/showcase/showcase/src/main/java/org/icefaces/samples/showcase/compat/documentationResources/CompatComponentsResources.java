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
                    title="General Link",
                    resource = ResourceRootPath.FOR_WIKI + "ICEfaces+Components"),

                // TLD Resources
                @ExampleResource(type = ResourceType.tld,
                    title="General Link",
                    resource = ResourceRootPath.FOR_ICE_TLD)
        }
)
@ManagedBean(name= CompatComponentsResources.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class CompatComponentsResources extends ComponentExampleImpl<CompatComponentsResources> implements Serializable {
    public static final String BEAN_NAME = "compatComponentsResources";
    public CompatComponentsResources()
    {
        super(CompatComponentsResources.class);
    }
}

