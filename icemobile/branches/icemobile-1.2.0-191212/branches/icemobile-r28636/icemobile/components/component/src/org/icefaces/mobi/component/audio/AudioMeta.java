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

package org.icefaces.mobi.component.audio;


import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;


@Component(
        tagName = "audioPlayer",
        componentClass = "org.icefaces.mobi.component.audio.Audio",
        rendererClass = "org.icefaces.mobi.component.audio.AudioRenderer",
        generatedClass = "org.icefaces.mobi.component.audio.AudioBase",
        componentType = "org.icefaces.Audio",
        rendererType = "org.icefaces.AudioRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.Audio",
        tlddoc = "This mobility component renders an html5 audio object"
)

@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})

public class AudioMeta extends UIComponentBaseMeta {

    @Property(defaultValue = "auto", tlddoc = "whether to preload the file accepted values are auto,none or metadata,  " +
            "where metadata means preload just the metadata and auto leaves the broswer to " +
            "decide whether to preload the entire file")
    private String preload;

    @Property(defaultValue = "true", tlddoc = "whether the controls are used or not")
    private boolean controls;

    @Property(tlddoc = "value can be byte[] or String filename or Resource")
    private Object value;


    @Property(defaultValue = "false", tlddoc = "boolean which specifies whether the file should be " +
            "played repeatedly")
    private boolean loop;

    @Property(defaultValue = "false",
            tlddoc = "When disabled, audio is not activated")
    private boolean disabled;

    @Property(tlddoc = "tabindex of the component")
    private Integer tabindex;

    @Property(tlddoc = "style will be rendered on the root element of this " +
            "component.")
    private String style;

    @Property(tlddoc = "style class will be rendered on the root element of " +
            "this component.")
    private String styleClass;

    @Property(defaultValue = "session")
    private String scope;

    @Property(tlddoc = "name is used for resource registering")
    private String name;
    @Property(tlddoc = "library is used for resource registering")
    private String library;

    @Property(tlddoc = "if value is empty will look for url to use")
    private String url;

    @Property(tlddoc = "mimeType of audio file")
    private String type;

    @Property( tlddoc = "Label for link that launches a system audio player")
    private String linkLabel;

}
