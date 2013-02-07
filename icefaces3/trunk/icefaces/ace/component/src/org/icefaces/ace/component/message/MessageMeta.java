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
package org.icefaces.ace.component.message;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Implementation;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIMessageMeta;
import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;

@Component(
        tagName = "message",
        componentClass = "org.icefaces.ace.component.message.Message",
        rendererClass = "org.icefaces.ace.component.message.MessageRenderer",
        generatedClass = "org.icefaces.ace.component.message.MessageBase",
        extendsClass = "javax.faces.component.UIMessage",
        componentType = "org.icefaces.ace.component.Message",
        rendererType = "org.icefaces.ace.component.MessageRenderer",
        componentFamily = "org.icefaces.ace.Message",
        tlddoc = "The message tag renders the first Faces message (if redisplay is true) or " +
                "the first undisplayed Faces message (if redisplay is false) for a specific component."
)
@ICEResourceDependency(library = ACEResourceNames.ACE_LIBRARY,
                       name = ACEResourceNames.COMBINED_CSS)
public class MessageMeta extends UIMessageMeta {

    @Property(name = "for", implementation = Implementation.EXISTS_IN_SUPERCLASS)
    private String forValue;

    @Property(tlddoc = "CSS style(s) to be applied when this component is rendered.")
    private String style;

    @Property(tlddoc = "Space-separated list of CSS style class(es) to be applied when this element is rendered. " +
            "This value must be passed through as the \"class\" attribute on generated markup.")
    private String styleClass;
}
