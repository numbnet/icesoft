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
                    title="ace:maskedEntry",
                    resource = ResourceRootPath.FOR_WIKI + "MaskedEntry"),

                // TLD Resources
                @ExampleResource(type = ResourceType.tld,
                    title="ace:maskedEntry",
                    resource = ResourceRootPath.FOR_ACE_TLD + "maskedEntry.html")
        }
)
@ManagedBean(name= MaskedEntryResources.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MaskedEntryResources extends ComponentExampleImpl<MaskedEntryResources> implements Serializable {
    public static final String BEAN_NAME = "maskedEntryResources";
    public MaskedEntryResources()
    {
        super(MaskedEntryResources.class);
    }
}
