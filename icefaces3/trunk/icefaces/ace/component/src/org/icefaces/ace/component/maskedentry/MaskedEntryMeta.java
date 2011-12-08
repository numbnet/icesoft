/*
 * Copyright 2010-2011 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.icefaces.ace.component.maskedentry;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Required;

import javax.faces.component.html.HtmlInputText;
//import org.icefaces.ace.meta.baseMeta.UIInputMeta;
import org.icefaces.ace.meta.baseMeta.*;

import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.api.IceClientBehaviorHolder;

@Component(
        tagName         = "maskedEntry",
        componentClass  = "org.icefaces.ace.component.maskedentry.MaskedEntry",
        rendererClass   = "org.icefaces.ace.component.maskedentry.MaskedEntryRenderer",
        generatedClass  = "org.icefaces.ace.component.maskedentry.MaskedEntryBase",
        extendsClass    = "javax.faces.component.html.HtmlInputText",
        componentType   = "org.icefaces.ace.component.MaskedEntry",
        rendererType    = "org.icefaces.ace.component.MaskedEntryRenderer",
		componentFamily = "org.icefaces.ace.MaskedEntry",
		tlddoc = "Text input component that forces input to be formatted in a specific way."
        )

@ResourceDependencies({
	@ResourceDependency(library="icefaces.ace", name="forms/forms.css"),
	@ResourceDependency(library="icefaces.ace", name="util/ace-jquery.js"),
	@ResourceDependency(library="icefaces.ace", name="util/ace-components.js")
})
@ClientBehaviorHolder(events = {
	@ClientEvent(name="valueChange", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@all")
}, defaultEvent="valueChange")
public class MaskedEntryMeta extends HtmlInputTextMeta {

	@Property(tlddoc="Name of the widget to access client side api")
    private String widgetVar;
	
	@Property(required=Required.yes, tlddoc="Masked input for separating input texts with given pattern. \nThese mask definitions can be used: \na - Represents an alpha character (A-Z,a-z) \n9 - Represents a numeric character (0-9) \n* - Represents an alphanumeric character (A-Z,a-z,0-9)")
	private String mask;
	
	@Property(tlddoc="Seperator and placeholder in input")
	private String placeHolder;
	
}
