package org.icefaces.component.baseMeta;

import org.icefaces.component.annotation.Property;
import org.icefaces.component.annotation.Implementation;
import org.icefaces.component.annotation.DefaultValueType;

/**
 * These are the properties for javax.faces.component.UIMessage
 */
public class UIMessageMeta extends UIComponentBaseMeta {
    @Property(//TODO ICE-6109
        implementation= Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="Identifier of the component for which to render error " +
            "messages. If this component is within the same NamingContainer " +
            "as the target component, this must be the component " +
            "identifier. Otherwise, it must be an absolute component " +
            "identifier (starting with \":\").")
    private String forValue;

    @Property(defaultValue="true",
        defaultValueType= DefaultValueType.EXPRESSION,
        implementation= Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="Flag indicating whether the \"detail\" property of messages " +
            "for the specified component should be rendered.")
    private boolean showDetail;

    @Property(defaultValue="false",
        defaultValueType= DefaultValueType.EXPRESSION,
        implementation= Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="Flag indicating whether the \"summary\" property of " +
            "messages for the specified component should be rendered.")
    private boolean showSummary;
    
    //TODO Check Mojarra 2.0.3+ code to see if exists there
    @Property(defaultValue="true",
        defaultValueType= DefaultValueType.EXPRESSION,
        implementation= Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="A flag indicating this UIMessage instance should redisplay " +
            "FacesMessages that have already been handled.")
    private boolean redisplay;
}
