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
                    title="ace:checkboxButton",
                    resource = ResourceRootPath.FOR_WIKI + "CheckboxButton"),

                // TLD Resources
                @ExampleResource(type = ResourceType.tld,
                    title="ace:checkboxButton",
                    resource = ResourceRootPath.FOR_ACE_TLD + "checkboxButton.html")
                
        }
)
@ManagedBean(name= CheckboxButtonResources.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class CheckboxButtonResources extends ComponentExampleImpl<CheckboxButtonResources> implements Serializable {
    public static final String BEAN_NAME = "checkboxButtonResources";
    public CheckboxButtonResources()
    {
        super(CheckboxButtonResources.class);
    }
}