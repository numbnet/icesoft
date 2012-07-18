package org.icefaces.ace.component.monitor;

import com.sun.xml.internal.ws.api.PropertySet;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;

import java.lang.Boolean;
import java.lang.String;

@Component(
        tagName = "submitMonitor",
        componentClass = "org.icefaces.ace.component.monitor.SubmitMonitor",
        generatedClass = "org.icefaces.ace.component.monitor.SubmitMonitorBase",
        rendererClass = "org.icefaces.ace.component.monitor.SubmitMonitorRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.ace.component.Montior",
        componentType = "org.icefaces.ace.component.SubmitMonitor",
        rendererType = "org.icefaces.ace.component.SubmitMonitorRenderer",
        tlddoc = ""
)
public class SubmitMonitorMeta extends UIComponentBaseMeta {
    @Property(tlddoc = "A string to be displayed centered on the UI blocking layer.")
    String label;

//    @Property
//    String serverErrorLabel;
//
//    @Property
//    String networkErrorLabel;
//
//    @Property
//    String sessionExpiredLabel;
//
//    @Property(name="for", tlddoc = "Define a comma separated list of component fully qualified component IDs who, " +
//            "along with their contents, will be observed by this component when they act as the source for requests.")
//    String forValue;
//
//    @Property(tlddoc = "")
//    Boolean showIcon;
//
//    @Property(tlddoc = "Enable to display an hourglass when hovering over the translucent plane displayed by " +
//            "the 'blockUI' feature.")
//    Boolean showHourglass;
//
//    @Property(tlddoc = "Enabling displays a translucent plane over the application when requests are underway.")
//    Boolean blockUI;
//
//    @Property(tlddoc = "Disabling displays the translucent plane of the 'blockUI' feature over the component region(s) " +
//            "defined by the 'for' attribute to rather than obscuring the whole page.")
//    Boolean blockWindow;
//
//    @Property(tlddoc = "Enable to prevent keyboard input when blockUI is enabled.")
//    Boolean blockKeyboard;
//
//    @Property(tlddoc = "Enable to display the activity labels at the center of the UI blocking plane.")
//    Boolean centered;
}

