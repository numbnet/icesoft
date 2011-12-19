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
                    title="ace:panel",
                    resource = ResourceRootPath.FOR_WIKI + "Panel"),

                // TLD Resources
                @ExampleResource(type = ResourceType.tld,
                    title="ace:panel",
                    resource = ResourceRootPath.FOR_ACE_TLD + "panel.html")
        }
)
@ManagedBean(name= PanelResources.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PanelResources extends ComponentExampleImpl<PanelResources> implements Serializable {
    public static final String BEAN_NAME = "panelResources";
    public PanelResources()
    {
        super(PanelResources.class);
    }
}