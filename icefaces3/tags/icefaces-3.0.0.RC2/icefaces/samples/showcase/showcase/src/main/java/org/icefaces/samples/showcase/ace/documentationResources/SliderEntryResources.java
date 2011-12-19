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
                    title="ace:sliderEntry",
                    resource = ResourceRootPath.FOR_WIKI + "SliderEntry"),

                // TLD Resources
                @ExampleResource(type = ResourceType.tld,
                    title="ace:sliderEntry",
                    resource = ResourceRootPath.FOR_ACE_TLD + "sliderEntry.html")
                
        }
)
@ManagedBean(name= SliderEntryResources.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class SliderEntryResources extends ComponentExampleImpl<SliderEntryResources> implements Serializable {
    public static final String BEAN_NAME = "sliderEntryResources";
    public SliderEntryResources()
    {
        super(SliderEntryResources.class);
    }
}
