package org.icefaces.component.baseMeta;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;

import javax.el.ValueExpression;
import java.util.Map;
import java.util.Set;

/**
 * Meta description for JSF 2.0 base class 
 */
public class UIComponentMeta {


    @Property(defaultValue="true",
              tlddoc="Return true if this component (and its children) should be rendered during the Render Response phase of the request processing lifecycle.")
    private boolean rendered;

    //@SomeNewAnnotation (
    //           tlddoc="The set of named attributes for this component, keyed by property name.")
    private Map<String, Object> attributes;

    //@SomeNewAnnotation (
    //           tlddoc="The set of ValueExpressions for this component, keyed by property name. This collection is lazily instantiated")
    private Map<String, ValueExpression> bindings;

    //@SomeNewAnnotation (tlddoc="The Renderer Class for this UIComponent or null for components that render themselves")
    private String rendererType;

    //@SomeNewAnnotation (tlddoc="Not certain. UIComponent contains listenersByEventClass ")
    // Map<Class<? extends SystemEvent>, List<SystemEventListener>> listenersByEventClass;
    // structure and the List<SystemEventListeners> getListenersForEventClass (Class<? extends SystemEvent> ) method
    // which should return the listeners for all subclasses of SystemEvent
//    private Set systemEventListeners;

    //@SomeNewAnnotation ( tlddoc="Not certain. UIComponentBase contains
    // Map<String, List<ClientBehaviors>> getClientBehaviors() method ")
//    private <String, List<ClientBehavior>> behaviors;
}
