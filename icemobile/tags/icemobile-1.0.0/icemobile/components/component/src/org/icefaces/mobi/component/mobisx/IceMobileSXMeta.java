package org.icefaces.mobi.component.mobisx;

import org.icefaces.ace.meta.annotation.*;
import org.icefaces.ace.meta.baseMeta.UICommandMeta;
import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;


@Component(
        tagName = "icemobilesx",
        componentClass = "org.icefaces.mobi.component.mobisx.IceMobileSX",
        rendererClass = "org.icefaces.mobi.component.mobisx.IceMobileSXRenderer",
        generatedClass = "org.icefaces.mobi.component.mobisx.IceMobileSXBase",
        extendsClass = "javax.faces.component.UICommand",
        componentType = "org.icefaces.component.IceMobileSX",
        rendererType = "org.icefaces.component.IceMobileSXRenderer",
        componentFamily = "org.icefaces.IceMobileSX",
        tlddoc = "This component assists ICEmobile-SX Surf Expander"
)

public class IceMobileSXMeta extends UICommandMeta {

    @Property(defaultValue = "true",
              tlddoc=" to enable the SX Surf Exapnder")
    private boolean enabled;

    @Property(defaultValue = "false",
            tlddoc = "disabled property. If true no input may be submitted via this" +
                    "component.  Is required by aria specs")
    private boolean disabled;

    @Property(tlddoc = "tabindex of the component")
    private Integer tabindex;

    @Property(tlddoc = "style class of the component, rendered on the div root of the component")
    private String styleClass;

    @Property(tlddoc = "style of the component, rendered on the div root of the component")
    private String style;
}
