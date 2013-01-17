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

package org.icefaces.ace.component.textareaentry;

import org.icefaces.ace.meta.annotation.*;
import org.icefaces.ace.meta.baseMeta.UIInputMeta;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
        tagName = "textAreaEntry",
        componentClass = "org.icefaces.ace.component.textareaentry.TextAreaEntry",
        rendererClass = "org.icefaces.ace.component.textareaentry.TextAreaEntryRenderer",
        generatedClass = "org.icefaces.ace.component.textareaentry.TextAreaEntryBase",
        extendsClass = "javax.faces.component.html.HtmlInputTextarea",
        componentType = "org.icefaces.ace.component.TextAreaEntry",
        rendererType = "org.icefaces.ace.component.TextAreaEntryRenderer",
        componentFamily = "org.icefaces.ace.TextAreaEntry",
        tlddoc = "TextAreaEntry is a text area input component that can display some placeholder text inside the text area when the component doesn't have a value and is not focussed." +
                " It also has custom styling for invalid state and required status." +
                "<p>For more information, see the <a href=\"http://wiki.icefaces.org/display/ICE/TextAreaEntry\">TextAreaEntry Wiki Documentation</a>."
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
public class TextAreaEntryMeta extends UIInputMeta {

    @Property(tlddoc = "Name of the widget variable to access client-side API.")
    private String widgetVar;

    @Property(tlddoc = "Indicator indicating that the user is required to provide a submitted value for this input component.")
    private String requiredIndicator;

    @Property(tlddoc = "Indicator indicating that the user is NOT required to provide a submitted value for this input component.")
    private String optionalIndicator;

    @Property(tlddoc = "Position of label relative to input field. Supported values are \"left/right/top/bottom/inField/none\". Default is \"none\".")
    private String labelPosition;

    @Property(tlddoc = "Position of input-required or input-optional indicator relative to input field or label. " +
            "Supported values are \"left/right/top/bottom/labelLeft/labelRight/none\". " +
            "Default is \"labelRight\" if labelPosition is \"inField\", \"right\" otherwise.")
    private String indicatorPosition;

    @Property(tlddoc = "Make the text area resizable via dragging the bottom right corner. Works only if browser supports CSS3 resize property.", defaultValue = "true")
    private boolean resizable;

    @Property(implementation = Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "The number of rows to be displayed.")
    private int rows;

    @Property(implementation = Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "The number of columns to be displayed.")
    private int cols;

    @Property(implementation = Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "A localized user presentable name for this component.")
    private String label;
}
