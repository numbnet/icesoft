package org.icefaces.ace.component.rowpanelexpander;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIColumnMeta;

import javax.el.MethodExpression;

@Component(
        tagName = "panelExpansion",
        componentClass = "org.icefaces.ace.component.rowpanelexpander.RowPanelExpander",
        generatedClass = "org.icefaces.ace.component.rowpanelexpander.RowPanelExpanderBase",
        extendsClass = "javax.faces.component.UIColumn",
        componentType = "org.icefaces.ace.component.RowPanelExpander",
        componentFamily = "org.icefaces.ace.RowPanelExpander",
        tlddoc = ""
)
public class RowPanelExpanderMeta extends UIColumnMeta {}
