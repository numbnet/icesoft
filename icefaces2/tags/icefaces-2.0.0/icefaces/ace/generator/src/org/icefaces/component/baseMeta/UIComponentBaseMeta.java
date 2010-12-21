package org.icefaces.component.baseMeta;

import org.icefaces.component.annotation.Property;
import org.icefaces.component.annotation.Implementation;

/**
 * These are the properties for javax.faces.component.UIComponentBase
 */
public class UIComponentBaseMeta extends UIComponentMeta {
    @Property (implementation= Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="The component identifier for this component. This value " +
            "must be unique within the closest parent component that is a " +
            "naming container.")
    private String id;
}
