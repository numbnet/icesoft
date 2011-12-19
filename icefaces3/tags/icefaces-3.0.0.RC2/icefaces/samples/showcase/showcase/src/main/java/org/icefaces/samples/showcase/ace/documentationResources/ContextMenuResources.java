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
                    title="ace:menu",
                    resource = ResourceRootPath.FOR_WIKI +"Menu+and+Menubar"),
                @ExampleResource(type = ResourceType.wiki,
                    title="ace:menuItem",
                    resource = ResourceRootPath.FOR_WIKI +"MenuItem"),

                // TLD Resources
                @ExampleResource(type = ResourceType.tld,
                    title="ace:contextMenu",
                    resource = ResourceRootPath.FOR_ACE_TLD + "contextMenu.html"),
                @ExampleResource(type = ResourceType.tld,
                    title="ace:menuItem",
                    resource = ResourceRootPath.FOR_ACE_TLD + "menuitem.html")
                
        }
)
@ManagedBean(name= ContextMenuResources.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ContextMenuResources extends ComponentExampleImpl< ContextMenuResources > implements Serializable {
    public static final String BEAN_NAME = "contextMenuResources";
    public ContextMenuResources()
    {
        super(ContextMenuResources.class);
    }
}
