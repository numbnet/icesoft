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

package org.icefaces.ace.component.textentry;

import org.icefaces.ace.meta.annotation.*;
import org.icefaces.ace.meta.baseMeta.HtmlInputTextMeta;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
        tagName = "textEntry",
        componentClass = "org.icefaces.ace.component.textentry.TextEntry",
        rendererClass = "org.icefaces.ace.component.textentry.TextEntryRenderer",
        generatedClass = "org.icefaces.ace.component.textentry.TextEntryBase",
        extendsClass = "javax.faces.component.html.HtmlInputText",
        componentType = "org.icefaces.ace.component.TextEntry",
        rendererType = "org.icefaces.ace.component.TextEntryRenderer",
        componentFamily = "org.icefaces.ace.TextEntry",
        tlddoc = "TextEntry is a text input component that can display some placeholder text inside the input field when the component doesn't have a value and is not focussed." +
                " It also has custom styling for invalid state and required status." +
                "<p>For more information, see the <a href=\"http://wiki.icefaces.org/display/ICE/TextEntry\">TextEntry Wiki Documentation</a>."
)

@ResourceDependencies({
        @ResourceDependency(library = "icefaces.ace", name = "util/combined.css"),
        @ResourceDependency(library = "icefaces.ace", name = "util/ace-jquery.js"),
        @ResourceDependency(library = "icefaces.ace", name = "util/ace-components.js")
})
@ClientBehaviorHolder(events = {
        @ClientEvent(name = "blur", javadoc = "Fired when the text input field loses focus (default event).",
                tlddoc = "Fired when the text input field loses focus (default event).", defaultRender = "@this", defaultExecute = "@this")
}, defaultEvent = "blur")
public class TextEntryMeta extends HtmlInputTextMeta {

    @Property(tlddoc = "Name of the widget variable to access client-side API.")
    private String widgetVar;

    @Property(tlddoc = "Indicator indicating that the user is required to provide a submitted value for this input component.")
    private String requiredIndicator;

    @Property(tlddoc = "Indicator indicating that the user is NOT required to provide a submitted value for this input component.")
    private String optionalIndicator;

    @Property(tlddoc = "Position of label relative to input field. Supported values are \"left/right/top/bottom/inField\".")
    private String labelPosition;

    @Property(tlddoc = "Position of input-required or input-optional indicator relative to input field or label. " +
            "Supported values are \"left/right/top/bottom/labelLeft/labelRight\". ")
    private String indicatorPosition;

    @Property(tlddoc = "When true the component will automatically tab to the next component once the maxLength number of characters have been entered.")
    private boolean autoTab;
}
