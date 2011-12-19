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
                    title="ace:tabPane",
                    resource = ResourceRootPath.FOR_WIKI + "TabPane"),
                @ExampleResource(type = ResourceType.wiki,
                    title="ace:tabSet",
                    resource = ResourceRootPath.FOR_WIKI + "TabSet"),
                @ExampleResource(type = ResourceType.wiki,
                    title="ace:tabSetProxy",
                    resource = ResourceRootPath.FOR_WIKI + "TabSetProxy"),

                // TLD Resources
                @ExampleResource(type = ResourceType.tld,
                    title="ace:tabPane",
                    resource = ResourceRootPath.FOR_ACE_TLD + "tabPane.html"),
                @ExampleResource(type = ResourceType.tld,
                    title="ace:tabSet",
                    resource = ResourceRootPath.FOR_ACE_TLD + "tabSet.html"),
                @ExampleResource(type = ResourceType.tld,
                    title="ace:tabSetProxy",
                    resource = ResourceRootPath.FOR_ACE_TLD + "tabSetProxy.html")
                
        }
)
@ManagedBean(name= TabSetResources.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TabSetResources extends ComponentExampleImpl<TabSetResources> implements Serializable {
    public static final String BEAN_NAME = "tabSetResources";
    public TabSetResources()
    {
        super(TabSetResources.class);
    }
}
