package org.icefaces.component.baseMeta;

import org.icefaces.component.annotation.Property;

/**
 *
 */
public class UICommandMeta extends UIComponentBaseMeta {


    @Property(tlddoc="The current value of the simple component. The value to be rendered"   )
    private Object value;

    @Property(tlddoc="A flag indicating that conversion and validation of this component’s value should occur during Apply" +
                     " Request Values phase instead of Process Validations phase", defaultValue="false"  )
    private boolean immediate;

    // @SomeNewAnnotation (tlddoc=""   )
    private Object methodBindingActionListener;

//    @SomeNewAnnotatiopn (tlddoc="a MethodExpression expression that identifies an Application Action method that takes no parameters and returns a" +
//                     "String."   )
    private String actionExpression; 

}
