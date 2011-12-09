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
                    title="ace:menuButton",
                    resource = ResourceRootPath.FOR_ACE_TLD + "menuButton.html"),
                @ExampleResource(type = ResourceType.tld,
                    title="ace:menuItem",
                    resource = ResourceRootPath.FOR_ACE_TLD + "menuitem.html")
        }
)
@ManagedBean(name= MenuButtonResources.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MenuButtonResources extends ComponentExampleImpl<MenuButtonResources> implements Serializable {
    public static final String BEAN_NAME = "menuButtonResources";
    public MenuButtonResources()
    {
        super(MenuButtonResources.class);
    }
}