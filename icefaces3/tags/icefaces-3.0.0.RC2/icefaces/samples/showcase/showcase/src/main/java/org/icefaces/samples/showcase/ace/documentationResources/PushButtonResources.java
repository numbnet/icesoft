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
                    title="ace:pushButton",
                    resource = ResourceRootPath.FOR_WIKI + "PushButton"),

                // TLD Resources
                @ExampleResource(type = ResourceType.tld,
                    title="ace:pushButton",
                    resource = ResourceRootPath.FOR_ACE_TLD + "pushButton.html")
                
        }
)
@ManagedBean(name= PushButtonResources.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PushButtonResources extends ComponentExampleImpl<PushButtonResources> implements Serializable {
    public static final String BEAN_NAME = "pushButtonResources";
    public PushButtonResources()
    {
        super(PushButtonResources.class);
    }
}
