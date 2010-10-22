package org.icefaces.component.baseMeta;

import org.icefaces.component.annotation.Property;
import org.icefaces.component.annotation.DefaultValueType;
import org.icefaces.component.annotation.Implementation;

/**
 * These are the properties for javax.faces.component.UIForm
 */
public class UIFormMeta extends UIComponentBaseMeta {
    @Property(defaultValue="true",
        defaultValueType= DefaultValueType.EXPRESSION,
        implementation= Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="Flag indicating whether or not this form should prepend its " +
            "id to its descendent's id during the clientId generation process.")
    private boolean prependId;
}
