package org.icefaces.ace.component.columns;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIColumnMeta;

@Component(
        tagName = "columns",
        componentClass = "org.icefaces.ace.component.columns.Columns",
        generatedClass = "org.icefaces.ace.component.columns.ColumnsBase",
        extendsClass = "javax.faces.component.UIColumn",
        componentType = "org.icefaces.ace.component.Columns",
        componentFamily = "org.icefaces.ace.Columns",
        tlddoc = ""
)
public class ColumnsMeta extends UIColumnMeta {
    @Property(tlddoc= "")
    Object value;
    @Property(tlddoc= "")
    String var;
    @Property(tlddoc= "")
    String columnIndexVar;
}
