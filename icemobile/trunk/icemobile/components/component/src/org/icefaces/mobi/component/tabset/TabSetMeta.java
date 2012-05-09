package org.icefaces.mobi.component.tabset;

import javax.el.MethodExpression;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;
import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.meta.annotation.Required;

@Component(
        tagName = "tabSet",
        componentClass = "org.icefaces.mobi.component.tabset.TabSet",
        rendererClass = "org.icefaces.mobi.component.tabset.TabSetRenderer",
        generatedClass = "org.icefaces.mobi.component.tabset.TabSetBase",
        componentType = "org.icefaces.TabSet",
        rendererType = "org.icefaces.TabSetRenderer",
        extendsClass = "javax.faces.component.UIPanel",
        componentFamily = "org.icefaces.TabSet",
        tlddoc = "This mobility component allows contentPane's to be placed within tabset controls in  " +
                "a stacked manner.  Only a single contentPane may be active at a time."
)

public class TabSetMeta extends UIPanelMeta {
    @Property(tlddoc="Int of the active pane.", defaultValue="0")
     private int tabIndex;

     @Property(tlddoc="Inline style of the container element.")
     private String style;

     @Property(tlddoc="Style class of the container element.")
     private String styleClass;

     @Property(tlddoc = "If true then all tabs except the active one will " +
        "be disabled and can not be selected.")
     private boolean disabled;

     @Property(tlddoc="Effect to use when toggling the panes.", defaultValue="fade")
     private String effect;

     @Property(tlddoc="MethodExpression representing a method that will be " +
        "invoked when the selected TabPane has changed. The expression " +
        "must evaluate to a public method that takes a ValueChangeEvent " +
        "parameter, with a return type of void.",
        expression= Expression.METHOD_EXPRESSION,
        methodExpressionArgument="javax.faces.event.ValueChangeEvent")
     private MethodExpression tabChangeListener;


}
