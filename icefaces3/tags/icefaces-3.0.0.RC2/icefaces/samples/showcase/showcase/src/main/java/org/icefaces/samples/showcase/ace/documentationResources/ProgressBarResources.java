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
                    title="ace:progressBar",
                    resource = ResourceRootPath.FOR_WIKI + "ProgressBar"),

                // TLD Resources
                @ExampleResource(type = ResourceType.tld,
                    title="ace:progressBar",
                    resource = ResourceRootPath.FOR_ACE_TLD + "progressBar.html")
                
        }
)
@ManagedBean(name= ProgressBarResources.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ProgressBarResources extends ComponentExampleImpl<ProgressBarResources> implements Serializable {
    public static final String BEAN_NAME = "progressBarResources";
    public ProgressBarResources()
    {
        super(ProgressBarResources.class);
    }
}
