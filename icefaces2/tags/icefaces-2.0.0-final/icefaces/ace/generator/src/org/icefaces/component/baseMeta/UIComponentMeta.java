package org.icefaces.component.baseMeta;

import org.icefaces.component.annotation.Property;
import org.icefaces.component.annotation.DefaultValueType;
import org.icefaces.component.annotation.Implementation;

import javax.el.ValueExpression;

/**
 * These are the properties for javax.faces.component.UIComponent
 */
public class UIComponentMeta {
    @Property (implementation=Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="Using an EL expression, bind the component reference to " +
            "a bean property, so that the component may be accessed in " +
            "the bean.")
    private ValueExpression binding;
    
    @Property (defaultValue="true",
        defaultValueType=DefaultValueType.EXPRESSION,
        implementation=Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="Return true if this component (and its children) should " +
            "be rendered during the Render Response phase of the request " +
            "processing lifecycle.")
    private boolean rendered;
}
