
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
                    title="ace:accordion",
                    resource = ResourceRootPath.FOR_WIKI+"Accordion"),
                @ExampleResource(type = ResourceType.wiki,
                    title="ace:accordionPane",
                    resource = ResourceRootPath.FOR_WIKI+"AccordionPane"),
                
                // TLD Resources
                @ExampleResource(type = ResourceType.tld,
                    title="ace:accordion",
                    resource = ResourceRootPath.FOR_ACE_TLD + "accordion.html"),
                @ExampleResource(type = ResourceType.tld,
                    title="ace:accordionPane",
                    resource = ResourceRootPath.FOR_ACE_TLD + "accordionPane.html")
        }
)
@ManagedBean(name= AccordionPanelResources.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class AccordionPanelResources extends ComponentExampleImpl<AccordionPanelResources> implements Serializable {
    public static final String BEAN_NAME = "accordionPanelResources";
    public AccordionPanelResources()
    {
        super(AccordionPanelResources.class);
    }
}
