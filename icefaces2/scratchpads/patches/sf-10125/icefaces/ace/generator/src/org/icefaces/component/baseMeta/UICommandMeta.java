package org.icefaces.component.baseMeta;

import org.icefaces.component.annotation.Property;
import org.icefaces.component.annotation.Expression;
import org.icefaces.component.annotation.Implementation;
import org.icefaces.component.annotation.DefaultValueType;

import javax.el.MethodExpression;

/**
 * These are the properties for javax.faces.component.UICommand
 */
public class UICommandMeta extends UIComponentBaseMeta {
    @Property (expression=Expression.METHOD_EXPRESSION,
        implementation=Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="MethodExpression representing the application action to " +
            "invoke when this component is activated by the user. The " +
            "expression must evaluate to a public method that takes no " +
            "parameters, and returns an Object (the toString() of which is " +
            "called to derive the logical outcome) which is passed to the " +
            "NavigationHandler for this application.")
    private MethodExpression action;

    @Property (expression=Expression.METHOD_EXPRESSION,
        methodExpressionArgument="javax.faces.event.ActionEvent",
        implementation=Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="MethodExpression representing an action listener method that " +
            "will be notified when this component is activated by the user. " +
            "The expression must evaluate to a public method that takes an " +
            "ActionEvent parameter, with a return type of void, or to a " +
            "public method that takes no arguments with a return type of " +
            "void. In the latter case, the method has no way of easily " +
            "knowing where the event came from, but this can be useful in " +
            "cases where a notification is needed that \"some action happened\".")
    private MethodExpression actionListener;
    
    @Property (defaultValue="false",
        defaultValueType= DefaultValueType.EXPRESSION,
        implementation=Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="Flag indicating that, if this component is activated by " +
            "the user, notifications should be delivered to interested " +
            "listeners and actions immediately (that is, during Apply " +
            "Request Values phase) rather than waiting until Invoke " +
            "Application phase.")
    private boolean immediate;
    
    @Property (implementation=Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="The current value of the simple component.")
    private Object value;
}
