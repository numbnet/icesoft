package org.icefaces.component.baseMeta;

import org.icefaces.component.annotation.Property;
import org.icefaces.component.annotation.DefaultValueType;
import org.icefaces.component.annotation.Implementation;
import org.icefaces.component.annotation.Expression;

import javax.el.MethodExpression;

/**
 * These are the properties for javax.faces.component.UIInput
 */
public class UIInputMeta extends UIOutputMeta {
    @Property(defaultValue="false",
        defaultValueType= DefaultValueType.EXPRESSION,
        implementation= Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="A flag indicating that conversion and validation of this " +
            "component's value should occur during Apply Request Values " +
            "phase instead of Process Validations phase.")
    private boolean immediate;
    
    @Property(implementation= Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="If present, will be used as the text of the converter " +
            "message, replacing any message that comes from the converter.")
    private String converterMessage;
    
    @Property(defaultValue="false",
        defaultValueType= DefaultValueType.EXPRESSION,
        implementation= Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="A flag indicating whether the user required to provide a " +
            "non-empty submitted value for this component.")
    private boolean required;
    
    @Property(implementation= Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="If present, will be used as the text of the validation " +
            "message for the \"required\" facility, if the \"required\" " +
            "facility is used.")
    private String requiredMessage;
    
    @Property (expression= Expression.METHOD_EXPRESSION,
        methodExpressionArgument="", //TODO ICE-6114
        // void validate(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
        implementation=Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="MethodExpression representing a validator method that will " +
            "be called during Process Validations to perform correctness " +
            "checks on the value of this component. The expression must " +
            "evaluate to a public method that takes FacesContext, " +
            "UIComponent, and Object parameters, with a return type of void.")
    private MethodExpression validator;
    
    @Property(implementation= Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="If present, will be used as the text of the validator " +
            "message, replacing any message that comes from the validator.")
    private String validatorMessage;
    
    @Property (expression= Expression.METHOD_EXPRESSION,
        methodExpressionArgument="javax.faces.event.ValueChangeEvent",
        implementation=Implementation.EXISTS_IN_SUPERCLASS,
        tlddoc="MethodExpression representing a value change listener " +
            "method that will be notified when a new value has been set for " +
            "this input component. The expression must evaluate to a public " +
            "method that takes a <code>ValueChangeEvent</code> parameter, " +
            "with a return type of void, or to a public method that takes " +
            "no arguments with a return type of void. In the latter case, " +
            "the method has no way of easily knowing what the new value is, " +
            "but this can be useful in cases where a notification is needed " +
            "that \"this value changed\".")
    private MethodExpression valueChangeListener;
}
