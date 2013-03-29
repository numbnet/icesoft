package org.icefaces.mobi.component.deviceresource;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

@Component(
        tagName = "deviceResource",
        componentClass = "org.icefaces.mobi.component.deviceresource.DeviceResource",
        rendererClass = "org.icefaces.mobi.component.deviceresource.DeviceResourceRenderer",
        generatedClass = "org.icefaces.mobi.component.deviceresource.DeviceResourceBase",
        componentType = "org.icefaces.DeviceResource",
        rendererType = "org.icefaces.DeviceResourceRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.DeviceResource",
        tlddoc = "This mobility component renders the necessary head resources for a device, " +
                "including the device-specific meta tags, the CSS themes, and javascript."
        )
@ResourceDependencies({
    @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})

public class DeviceResourceMeta extends UIComponentBaseMeta {

    @Property(tlddoc = "The CSS theme to use")
    private String theme;

    @Property(defaultValue="true", tlddoc = "If true (default), the deviceResource tag will cause IOS 6 to"
            +" render a Smart App Banner for the application, allowing the user to install ICEmobile SX,"
            +"  if it is not already installed, and open it, if the app is installed."
            +" If the user chooses to open the installed app, the session will be auto-registered.")   
    private boolean includeIOSSmartAppBanner = true;

    @Property(tlddoc = "library is also used by h comp outputStyleSheet")
    private String library;

    @Property(tlddoc = "View size, small or large ")
    private String view;

}
