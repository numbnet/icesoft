package org.icemobile.component.baseMeta;

import org.icefaces.ace.meta.annotation.*;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

@JSPBaseMeta(tagClass = "javax.servlet.jsp.tagext.BodyTagSupport")
public class BodyBaseMeta extends UIComponentBaseMeta {
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
