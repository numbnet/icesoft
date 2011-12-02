package org.icefaces.ace.component.columngroup;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;


@Component(
    tagName = "columnGroup",
    componentClass = "org.icefaces.ace.component.columngroup.ColumnGroup",
    generatedClass = "org.icefaces.ace.component.columngroup.ColumnGroupBase",
    extendsClass = "javax.faces.component.UIComponentBase",
    componentType = "org.icefaces.ace.component.ColumnGroup",
    componentFamily = "org.icefaces.ace.ColumnGroup",
    tlddoc = "A grouping of column and row components to furnish table segments specified by the 'type' attribute."
)
public class ColumnGroupMeta extends UIComponentBaseMeta {
    @Property(tlddoc= "Indicator of the segment of the table these components will be used to rendered. Available values are header and footer.")
    private String type;
}
