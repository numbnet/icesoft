package org.icefaces.ace.component.rowexpander;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

import javax.el.MethodExpression;


@Component(
        tagName = "rowExpansion",
        componentClass = "org.icefaces.ace.component.rowexpander.RowExpander",
        generatedClass = "org.icefaces.ace.component.rowexpander.RowExpanderBase",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentType = "org.icefaces.ace.component.RowExpander",
        componentFamily = "org.icefaces.ace.RowExpander",
        tlddoc = ""
)
public class RowExpanderMeta extends UIComponentBaseMeta {}
