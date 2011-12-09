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
                    title="ace:linkButton",
                    resource = ResourceRootPath.FOR_WIKI + "LinkButton"),

                // TLD Resources
                @ExampleResource(type = ResourceType.tld,
                    title="ace:linkButton",
                    resource = ResourceRootPath.FOR_ACE_TLD + "linkButton.html")
                
        }
)
@ManagedBean(name= LinkButtonResources.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class LinkButtonResources extends ComponentExampleImpl<LinkButtonResources> implements Serializable {
    public static final String BEAN_NAME = "linkButtonResources";
    public LinkButtonResources()
    {
        super(LinkButtonResources.class);
    }
}
