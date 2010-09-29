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
}
