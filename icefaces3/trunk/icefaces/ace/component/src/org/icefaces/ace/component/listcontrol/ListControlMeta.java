package org.icefaces.ace.component.listcontrol;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.DefaultValueType;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
    tagName = "listControl",
    componentClass = "org.icefaces.ace.component.listcontrol.ListControl",
    generatedClass = "org.icefaces.ace.component.listcontrol.ListControlBase",
    extendsClass = "javax.faces.component.UIComponentBase",
    componentType = "org.icefaces.ace.component.ListControl",
    rendererType  = "org.icefaces.ace.component.ListControlRenderer",
    componentFamily = "org.icefaces.ace.ListControl",
    tlddoc = ""
)
@ResourceDependencies({
    @ResourceDependency(library="icefaces.ace", name="util/combined.css"),
    @ResourceDependency(library = "icefaces.ace", name = "util/ace-jquery.js"),
    @ResourceDependency(library = "icefaces.ace", name = "util/ace-components.js")
})
public class ListControlMeta extends UIComponentBaseMeta {
    @Property(tlddoc="Style class to apply to the container DIV element.",
            defaultValue = "ui-widget-content ui-corner-all",
            defaultValueType = DefaultValueType.STRING_LITERAL)
    private String styleClass;

    @Property(tlddoc="Style class to apply to the left button icon element.",
            defaultValue = "ui-icon ui-icon-arrow-1-w",
            defaultValueType = DefaultValueType.STRING_LITERAL )
    private String leftClass;

    @Property(tlddoc="Style class to apply to the all-left button icon element.",
            defaultValue = "ui-icon ui-icon-arrowstop-1-w",
            defaultValueType = DefaultValueType.STRING_LITERAL)
    private String allLeftClass;

    @Property(tlddoc="Style class to apply to the right button icon element.",
            defaultValue = "ui-icon ui-icon-arrow-1-e",
            defaultValueType = DefaultValueType.STRING_LITERAL)
    private String rightClass;

    @Property(tlddoc="Style class to apply to the all-right button icon element.",
            defaultValue = "ui-icon ui-icon-arrowstop-1-e",
            defaultValueType = DefaultValueType.STRING_LITERAL)
    private String allRightClass;

    @Property(tlddoc="Style class to apply to the spacer container around each button element.")
    private String spacerClass;

    @Property(tlddoc="Style class to apply to the container around each button icon element.",
            defaultValue = "ui-state-default ui-corner-all",
            defaultValueType = DefaultValueType.STRING_LITERAL)
    private String controlClass;

    @Property(tlddoc="Style class to apply to the header DIV element.",
            defaultValue = "ui-state-default",
            defaultValueType = DefaultValueType.STRING_LITERAL)
    private String headerClass;

    @Property(tlddoc="Style class to apply to the footer DIV element.",
            defaultValue = "ui-widget-content",
            defaultValueType = DefaultValueType.STRING_LITERAL)
    private String footerClass;

    @Property(tlddoc="Style rules to apply to the header DIV element.")
    private String headerStyle;

    @Property(tlddoc="Style rules to apply to the footer DIV element.")
    private String footerStyle;

    @Property(tlddoc="Style rules to apply to the container DIV element.")
    private String style;

    @Property(tlddoc="JQuery/CSS selector defining the group of lists this " +
            "control navigates and creates mutually exclusive selection between.",
            defaultValue = ".if-list",
            defaultValueType = DefaultValueType.STRING_LITERAL)
    private String selector;

    @Property(tlddoc = "", defaultValue = "alll lft rgt allr",
            defaultValueType = DefaultValueType.STRING_LITERAL)
    private String format;

    @Property(tlddoc = "When dual list mode is used, this property will determine " +
            "if we render the migration control on top, bottom or both.",
            defaultValue = "top",
            defaultValueType = DefaultValueType.STRING_LITERAL
    )
    private String position;
}
