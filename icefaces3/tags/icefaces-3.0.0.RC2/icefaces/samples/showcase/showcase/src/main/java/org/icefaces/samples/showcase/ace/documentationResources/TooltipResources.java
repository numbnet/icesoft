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
                    title="ace:tooltip",
                    resource = ResourceRootPath.FOR_WIKI + "Tooltip"),

                // TLD Resources
                @ExampleResource(type = ResourceType.tld,
                    title="ace:tooltip",
                    resource = ResourceRootPath.FOR_ACE_TLD + "tooltip.html")
                
        }
)
@ManagedBean(name= TooltipResources.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TooltipResources extends ComponentExampleImpl<TooltipResources> implements Serializable {
    public static final String BEAN_NAME = "tooltipResources";
    public TooltipResources()
    {
        super(TooltipResources.class);
    }
}

