package org.icefaces.component.baseMeta;

import org.icefaces.component.annotation.Property;


/**
 *
 */
public class UIInputMeta extends UIOutputMeta {

    //@SomeNewAnnotation(defaultValue="false",  tlddoc="A boolean flag that indicates that the value of this component is set locally")
    //  private boolean localValueSet;

    @Property(defaultValue="false",  tlddoc="A flag indicating whether the user required to provide a non-empty value " +
                                            "for this component. Default value must be false.")
    private boolean required;


    @Property( tlddoc="If there has been a call to setRequiredMessage() on this instance, return the message.  Otherwise, call (getValueExpression() )" +
                      " passing the key 'requiredMessage', get the result of the expression, and return it")
    private String requiredMessage;

    @Property(  tlddoc="If there has been a call to setValidatorMessage() on this instance, return the message.  Otherwise," +
                       " call getValueExpression() passing the key 'validatorMessage', get the result of the expression, and return it")
    private String validatorMessage;

    @Property(defaultValue="false",  tlddoc="Represents whether the local value of this component\n" +
                                            "is valid (no conversion error has occurred)")
    private boolean valid;

    @Property(defaultValue="false",  tlddoc="A flag indicating that conversion and validation of this component’s value should occur during Apply" +
                                            "Request Values phase instead of Process Validations phase")
    private boolean immediate;

}
