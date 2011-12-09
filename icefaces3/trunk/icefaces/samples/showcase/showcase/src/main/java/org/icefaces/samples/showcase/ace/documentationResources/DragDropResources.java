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
                    title="ace:draggable",
                    resource = ResourceRootPath.FOR_WIKI + "Draggable"),
                @ExampleResource(type = ResourceType.wiki,
                    title="ace:droppable",
                    resource = ResourceRootPath.FOR_WIKI + "Droppable"),

                // TLD Resources
                @ExampleResource(type = ResourceType.tld,
                    title="ace:draggable",
                    resource = ResourceRootPath.FOR_ACE_TLD + "draggable.html"),
                @ExampleResource(type = ResourceType.tld,
                    title="ace:droppable",
                    resource = ResourceRootPath.FOR_ACE_TLD + "droppable.html")
        }
)
@ManagedBean(name= DragDropResources.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DragDropResources extends ComponentExampleImpl<DragDropResources> implements Serializable {
    public static final String BEAN_NAME = "dragDropResources";
    public DragDropResources()
    {
        super(DragDropResources.class);
    }
}