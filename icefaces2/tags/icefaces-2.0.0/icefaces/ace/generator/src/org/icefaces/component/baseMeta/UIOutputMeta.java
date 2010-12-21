package org.icefaces.component.baseMeta;

import org.icefaces.component.annotation.Property;
import org.icefaces.component.annotation.Implementation;

import javax.faces.convert.Converter;

/**
 * These are the properties for javax.faces.component.UIOutput
 */
public class UIOutputMeta extends UIComponentBaseMeta {

    @Property (implementation= Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="The current value of the simple component. The value to be " +
            "rendered.")
    private Object value;

    @Property (implementation= Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="Converter is an interface describing a Java class that can " +
            "perform Object-to-String and String-to-Object conversions " +
            "between model data objects and a String representation of " +
            "those objects that is suitable for rendering.")
    private Converter converter;
}
