package org.icefaces.component.baseMeta;

import org.icefaces.component.annotation.Property;

/**
 *
 */
public class UIMessagesMeta extends UIComponentBaseMeta {

    @Property(defaultValue="false", tlddoc="Flag indicating whether only global messages (that is, messages with no associated client identifier) should be " +
                                           "rendered.  Mutually exclusive with the 'for' property which takes precedence. Defaults to false." )
    private boolean globalOnly;

    @Property(defaultValue="null", tlddoc="Identifier of the component for which to render error messages. If this component is within the same " +
                                          "NamingContainer as the target component, this must be the component identifier. Otherwise, it must be " +
                                          "an absolute component identifier (starting with “:”)" )
    private String forValue;

    @Property(defaultValue="true", tlddoc="Flag indicating whether the “detail” property of " +
                                          "messages for the specified component should be rendered. Default value is “true”" )
    private boolean showDetail;

    @Property(defaultValue="false",  tlddoc="Flag indicating whether the “summary” property of " +
                                            "messages for the specified component should be rendered. Default value is “false”" )
    private boolean showSummary;

    @Property(defaultValue="true", tlddoc="A flag indicating this UIMessage instance should redisplay FacesMessages that have already been handled" )
    private boolean redisplay;
}
