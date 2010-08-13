package org.icefaces.component.baseMeta;

import org.icefaces.component.annotation.Property;
import javax.faces.convert.Converter;

/**
 *
 */
public class UIOutputMeta extends UIComponentBaseMeta {

    @Property (
            tlddoc="The current value of the simple component. The value to be rendered")
    private Object value;

    @Property (
            tlddoc="Converter is an interface describing a Java class that can perform Object-to-String and String-to-Object" +
                   " conversions between model data objects and a String representation of those objects that is suitable for rendering")
    private Converter converter;
}
