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
package org.icefaces.ace.component.messages;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Implementation;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIMessagesMeta;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
        tagName = "messages",
        componentClass = "org.icefaces.ace.component.messages.Messages",
        rendererClass = "org.icefaces.ace.component.messages.MessagesRenderer",
        generatedClass = "org.icefaces.ace.component.messages.MessagesBase",
        extendsClass = "javax.faces.component.UIMessages",
        componentType = "org.icefaces.ace.component.Messages",
        rendererType = "org.icefaces.ace.component.MessagesRenderer",
        componentFamily = "org.icefaces.ace.Messages",
        tlddoc = "The messages tag renders all Faces messages." +
                "<p>For more information, see the <a href=\"http://wiki.icefaces.org/display/ICE/Messages\">Messages Wiki Documentation</a>."
)
@ResourceDependencies({
        @ResourceDependency(library = "icefaces.ace", name = "util/combined.css"),
        @ResourceDependency(library = "icefaces.ace", name = "util/ace-jquery.js"),
        @ResourceDependency(library = "icefaces.ace", name = "util/ace-components.js")
})
public class MessagesMeta extends UIMessagesMeta {

    @Property(name = "for", implementation = Implementation.EXISTS_IN_SUPERCLASS)
    private String forValue;

    @Property(tlddoc = "The type of layout markup to use when rendering error messages." +
            " Valid values are \"table\" (an HTML table) and \"list\" (an HTML list)." +
            " If not specified, the default value is \"list\".")
    private String layout;
}
