/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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
package org.icefaces.mobi.component.onlinestatus;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

@Component(
        tagName = "onlineStatus",
        componentClass = "org.icefaces.mobi.component.onlinestatus.OnlineStatus",
        rendererClass = "org.icefaces.mobi.component.onlinestatus.OnlineStatusRenderer",
        generatedClass = "org.icefaces.mobi.component.onlinestatus.OnlineStatusBase",
        componentType = "org.icefaces.OnlineStatus",
        rendererType = "org.icefaces.OnlineStatusRenderer",
        extendsClass = "javax.faces.component.UIPanel",
        componentFamily = "org.icefaces.OnlineStatus",
        tlddoc = "Renders a header/body/footer layout that " +
                "is defined using the facet tag.  The facets for header and footer " +
                "are optional."
)

public class OnlineStatusMeta extends UIComponentBaseMeta {
    
    @Property(tlddoc = org.icefaces.mobi.utils.TLDConstants.STYLE)
    private String style;

    @Property(tlddoc = org.icefaces.mobi.utils.TLDConstants.STYLECLASS)
    private String styleClass;
    
}

