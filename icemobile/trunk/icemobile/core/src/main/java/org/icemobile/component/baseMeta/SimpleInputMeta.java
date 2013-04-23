package org.icemobile.component.baseMeta;

import org.icefaces.ace.meta.annotation.*;
import org.icefaces.ace.meta.baseMeta.UIInputMeta;

@JSPBaseMeta(tagClass = "javax.servlet.jsp.tagext.SimpleTagSupport")
public class SimpleInputMeta extends UIInputMeta {
    @Property(tlddoc=ConstantsMeta.DISABLED,
        implementation=Implementation.GENERATE)
    private boolean disabled;

    @Only(OnlyType.JSP)
    @Property(tlddoc=ConstantsMeta.ID,
        implementation=Implementation.GENERATE)
    private String id;

    @Property(tlddoc=ConstantsMeta.STYLE,
        implementation=Implementation.GENERATE)
    private String style;

    @Property(tlddoc=ConstantsMeta.STYLECLASS,
        implementation=Implementation.GENERATE)
    private String styleClass;
}
