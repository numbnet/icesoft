package org.icefaces.ace.component.roweditor;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

import javax.el.MethodExpression;

@Component(
        tagName = "rowEditor",
        componentClass = "org.icefaces.ace.component.roweditor.RowEditor",
        generatedClass = "org.icefaces.ace.component.roweditor.RowEditorBase",
        rendererClass = "org.icefaces.ace.component.roweditor.RowEditorRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentType = "org.icefaces.ace.component.RowEditor",
        rendererType = "org.icefaces.ace.component.RowEditorRenderer",
        componentFamily = "org.icefaces.ace.RowEditor",
        tlddoc = ""
)
public class RowEditorMeta extends UIComponentBaseMeta {
    @Property(expression = Expression.METHOD_EXPRESSION,
            methodExpressionArgument = "org.icefaces.ace.event.RowEditEvent",
            tlddoc = "MethodExpression reference called whenever a table row " +
                    "is edited. The method receives a single argument, RowEditEvent.")
    private MethodExpression rowEditListener;

    @Property(expression = Expression.METHOD_EXPRESSION,
            methodExpressionArgument = "org.icefaces.ace.event.RowEditCancelEvent",
            tlddoc = "MethodExpression reference called whenever a table row " +
                    "editing is canceled. The method receives a single argument, RowEditCancelEvent.")
    private MethodExpression rowEditCancelListener;
}
