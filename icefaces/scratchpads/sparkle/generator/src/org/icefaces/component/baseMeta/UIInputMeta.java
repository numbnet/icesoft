package org.icefaces.component.baseMeta;

import org.icefaces.component.annotation.Property;


/**
 *
 */
public class UIInputMeta extends UIOutputMeta {

    @Property(defaultValue="false",  tlddoc="A flag indicating whether the user required to provide a non-empty value " +
                                            "for this component. Default value must be false.")
    private boolean required;

    @Property(defaultValue="false",  tlddoc="A flag indicating that conversion and validation of this component’s value should occur during Apply" +
                                            "Request Values phase instead of Process Validations phase")
    private boolean immediate;

}
