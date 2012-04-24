package org.icefaces.ace.component.list;

import org.icefaces.ace.meta.annotation.*;
import org.icefaces.ace.meta.baseMeta.UIDataMeta;

import javax.el.MethodExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import java.util.Set;

@Component(
    tagName = "list",
    componentClass = "org.icefaces.ace.component.list.ACEList",
    generatedClass = "org.icefaces.ace.component.list.ListBase",
    extendsClass = "org.icefaces.impl.component.UISeriesBase",
    componentType = "org.icefaces.ace.component.List",
    rendererType  = "org.icefaces.ace.component.ListRenderer",
    rendererClass = "org.icefaces.ace.component.list.ListRenderer",
    componentFamily = "org.icefaces.ace.List",
    tlddoc = ""
)
@ResourceDependencies({
    @ResourceDependency(library="icefaces.ace", name="util/combined.css"),
    @ResourceDependency(library = "icefaces.ace", name = "util/ace-jquery.js"),
    @ResourceDependency(library = "icefaces.ace", name = "util/ace-components.js")
})
@ClientBehaviorHolder(events = {
        @ClientEvent(name="select", defaultRender="@this", defaultExecute="@this",
                javadoc="", tlddoc=""),
        @ClientEvent(name="deselect", defaultRender="@this", defaultExecute="@this",
                javadoc="", tlddoc=""),
        @ClientEvent(name="move", defaultRender="@this", defaultExecute="@this",
                javadoc="", tlddoc=""),
        @ClientEvent(name="migrate", defaultRender="@all", defaultExecute="@all",
                javadoc="", tlddoc="")
    },
    defaultEvent = "select"
)
public class ListMeta extends UIDataMeta {
    // Properties
    @Property(tlddoc="Style class to apply to the iterative LI element.")
    private String itemClass;

    @Property(tlddoc="Style class to apply to the container UL element.")
    private String bodyClass;

    @Property(tlddoc="Style class to apply to the container DIV element.")
    private String styleClass;

    @Property(tlddoc="Style class to apply to the header DIV element.")
    private String headerClass;

    @Property(tlddoc="Style class to apply to the footer DIV element.")
    private String footerClass;

    @Property(tlddoc="Style class to apply to the optional dragging placeholder LI element.")
    private String placeholderClass;



    @Property(tlddoc = "Style rules to apply to the container DIV element")
    private String style;

    @Property(tlddoc="Style rules to apply to the iterative LI element.")
    private String itemStyle;

    @Property(tlddoc="Style rules to apply to the container UL element.")
    private String bodyStyle;

    @Property(tlddoc="Style rules to apply to the header DIV element.")
    private String headerStyle;

    @Property(tlddoc="Style rules to apply to the footer DIV element.")
    private String footerStyle;



    @Property(tlddoc = "Enable adding a style to the whitespace that is cleared for a" +
            " list item being dragged / dropped.", defaultValue = "true",
            defaultValueType = DefaultValueType.EXPRESSION)
    private Boolean placeholder;

    @Property(tlddoc = "Enable the dragging of list items in this list.",
            defaultValue = "true", defaultValueType = DefaultValueType.EXPRESSION)
    private Boolean dragging;

    @Property(tlddoc = "Enables inter-list dragging and dropping; an identifier" +
            " used to link this region and others for bi-directional dropping.")
    private String dropGroup;



    @Property(tlddoc = "",
            defaultValue = "false", defaultValueType = DefaultValueType.EXPRESSION)
    private Boolean selection;

    @Property(tlddoc = "",
            defaultValue = "false", defaultValueType = DefaultValueType.EXPRESSION)
    private Boolean singleSelection;

    @Property(tlddoc = "")
    private Set<Object> selections;

    @Property(expression = Expression.METHOD_EXPRESSION,
            methodExpressionArgument = "org.icefaces.ace.event.ListSelectEvent",
            tlddoc = "")
    private MethodExpression selectionListener;



    @Property(tlddoc = "",
            defaultValue = "false", defaultValueType = DefaultValueType.EXPRESSION)
    private Boolean controlsEnabled;

    @Property(tlddoc = "", defaultValue = "top up dwn btm",
            defaultValueType = DefaultValueType.STRING_LITERAL)
    private String controlsFormat;

    @Property(tlddoc = "", defaultValue = "",
            defaultValueType = DefaultValueType.STRING_LITERAL)
    private String controlsContainerClass;

    @Property(tlddoc = "", defaultValue = "ui-state-default ui-corner-all",
            defaultValueType = DefaultValueType.STRING_LITERAL)
    private String controlsItemClass;

    @Property(tlddoc="Style class to apply to the spacer container around each button element.")
    private String controlsSpacerClass;

    @Property(tlddoc = "", defaultValue = "ui-icon ui-icon-arrowstop-1-n",
            defaultValueType = DefaultValueType.STRING_LITERAL)
    private String topButtonClass;

    @Property(tlddoc = "", defaultValue = "ui-icon ui-icon-arrow-1-n",
            defaultValueType = DefaultValueType.STRING_LITERAL)
    private String upButtonClass;

    @Property(tlddoc = "", defaultValue = "ui-icon ui-icon-arrow-1-s",
            defaultValueType = DefaultValueType.STRING_LITERAL)
    private String downButtonClass;

    @Property(tlddoc = "", defaultValue = "ui-icon ui-icon-arrowstop-1-s",
            defaultValueType = DefaultValueType.STRING_LITERAL)
    private String bottomButtonClass;

}
