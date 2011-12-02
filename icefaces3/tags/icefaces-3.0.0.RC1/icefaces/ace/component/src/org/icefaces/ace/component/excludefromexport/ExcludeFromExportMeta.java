package org.icefaces.ace.component.excludefromexport;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

@Component(
        tagName = "excludeFromExport",
        componentClass = "org.icefaces.ace.component.excludefromexport.ExcludeFromExport",
        generatedClass = "org.icefaces.ace.component.excludefromexport.ExcludeFromExportBase",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentType = "org.icefaces.ace.component.ExcludeFromExport",
        componentFamily = "org.icefaces.ace.component",
        tlddoc = "Embedding this component inside any component causes the data exporter to avoid including the values of such component in the exported file."
)
public class ExcludeFromExportMeta extends UIComponentBaseMeta {}
