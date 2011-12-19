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
                    title="ace:dialog",
                    resource = ResourceRootPath.FOR_WIKI + "Dialog"),

                // TLD Resources
                @ExampleResource(type = ResourceType.tld,
                    title="ace:dialog",
                    resource = ResourceRootPath.FOR_ACE_TLD + "dialog.html")
                
        }
)
@ManagedBean(name= DialogResources.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DialogResources extends ComponentExampleImpl<DialogResources> implements Serializable {
    public static final String BEAN_NAME = "dialogResources";
    public DialogResources()
    {
        super(DialogResources.class);
    }
}
