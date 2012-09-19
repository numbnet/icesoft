/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

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
              tlddoc=" to enable the SX Surf Expander")
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
