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
                    title="ace:dateTimeEntry",
                    resource = ResourceRootPath.FOR_WIKI + "DateTimeEntry"),

                // TLD Resources
                @ExampleResource(type = ResourceType.tld,
                    title="ace:dateTimeEntry",
                    resource = ResourceRootPath.FOR_ACE_TLD + "dateTimeEntry.html")
                
        }
)
@ManagedBean(name= DateTimeEntryResources.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DateTimeEntryResources extends ComponentExampleImpl<DateTimeEntryResources> implements Serializable {
    public static final String BEAN_NAME = "dateTimeEntryResources";
    public DateTimeEntryResources()
    {
        super(DateTimeEntryResources.class);
    }
}