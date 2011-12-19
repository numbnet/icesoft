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
                    title="ace:notificationPanel",
                    resource = ResourceRootPath.FOR_WIKI + "NotificationPanel"),

                // TLD Resources
                @ExampleResource(type = ResourceType.tld,
                    title="ace:notificationPanel",
                    resource = ResourceRootPath.FOR_ACE_TLD + "notificationPanel.html")
                
        }
)
@ManagedBean(name= NotificationPanelResources.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class NotificationPanelResources extends ComponentExampleImpl<NotificationPanelResources> implements Serializable {
    public static final String BEAN_NAME = "notificationPanelResources";
    public NotificationPanelResources()
    {
        super(NotificationPanelResources.class);
    }
}
