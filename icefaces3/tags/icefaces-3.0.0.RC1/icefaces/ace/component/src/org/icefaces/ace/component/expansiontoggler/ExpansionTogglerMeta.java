package org.icefaces.ace.component.expansiontoggler;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

import javax.el.MethodExpression;

@Component(
    tagName = "expansionToggler",
    componentClass = "org.icefaces.ace.component.expansiontoggler.ExpansionToggler",
    generatedClass = "org.icefaces.ace.component.expansiontoggler.ExpansionTogglerBase",
    rendererClass = "org.icefaces.ace.component.expansiontoggler.ExpansionTogglerRenderer",
    extendsClass = "javax.faces.component.UIComponentBase",
    componentType = "org.icefaces.ace.component.ExpansionToggler",
    rendererType = "org.icefaces.ace.component.ExpansionTogglerRenderer",
    componentFamily = "org.icefaces.ace.ExpansionToggler",
    tlddoc = ""
)
public class ExpansionTogglerMeta extends UIComponentBaseMeta {
    @Property(expression = Expression.METHOD_EXPRESSION,
            methodExpressionArgument = "org.icefaces.ace.event.ExpansionChangeEvent",
            tlddoc = "MethodExpression reference called whenever a row " +
                    "element is expanded. The method receives a single " +
                    "argument, ExpansionChangeEvent.")
    private MethodExpression changeListener;
}
