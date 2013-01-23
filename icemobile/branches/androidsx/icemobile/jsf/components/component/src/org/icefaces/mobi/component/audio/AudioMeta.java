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

package org.icefaces.mobi.component.audio;


import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import org.icefaces.mobi.utils.TLDConstants;

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
        tlddoc = "This component renders an HTML5 audio element. The component can be bound to a variety of source types. " +
        		"Play back options can be controlled. JSF Resource management options are also available. "
)

@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})

public class AudioMeta extends UIComponentBaseMeta {

    @Property(defaultValue = "auto", tlddoc = "Options for preloading the audio file. Accepted values are " +
    		"\"auto\" (allow the browser to decide), \"none\" or \"metadata\" (preload only the metadata). ")
    private String preload;

    @Property(defaultValue = "true", tlddoc = "If \"true\", will render the audio player controls. ")
    private boolean controls;

    @Property(tlddoc = "The audio file source. The value may resolve to a byte array, the String of a file name, " +
    		"or a JSF Resource. ")
    private Object value;


    @Property(defaultValue = "false", tlddoc = "If \"true\", will play the the audio file repeatedly. ")
    private boolean loop;

    @Property(defaultValue = "false", tlddoc = TLDConstants.DISABLED)
    private boolean disabled;

    @Property(tlddoc = TLDConstants.TABINDEX)
    private Integer tabindex;

    @Property(tlddoc = TLDConstants.STYLE)
    private String style;

    @Property(tlddoc = TLDConstants.STYLECLASS)
    private String styleClass;

    @Property (defaultValue="false",
               tlddoc="Will cause the audio to automatically play when the page is load. This may be required " +
               		"for some older devices to play. ")
    private boolean autoPlay;

    @Property(defaultValue = "session", tlddoc="The JSF Resource scope of the object resolving from the \"value\" " +
    		"attribute. Possible values are \"flash\", \"request\", \"view\", \"session\", and \"application\". ")
    private String scope;

    @Property(tlddoc = "The name is used for JSF Resource registration. ")
    private String name;
    
    @Property(tlddoc = "The library used for JSF Resource regisration. ")
    private String library;

    @Property(tlddoc = "The URL of the audio file. If the \"value\" attribute is empty, the \"url\" attribute will" +
    		" be used. ")
    private String url;

    @Property(tlddoc = "The mime type of audio file. ")
    private String type;

    @Property( tlddoc = "The Label for the link that launches a system audio player. ")
    private String linkLabel;

}
