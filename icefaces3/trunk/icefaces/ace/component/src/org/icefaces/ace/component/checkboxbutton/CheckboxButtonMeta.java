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

package org.icefaces.ace.component.checkboxbutton;


import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.ace.meta.baseMeta.UISelectBooleanMeta;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.api.IceClientBehaviorHolder;

@Component(
        tagName        = "checkboxButton",
        componentClass = "org.icefaces.ace.component.checkboxbutton.CheckboxButton",
        rendererClass  = "org.icefaces.ace.component.checkboxbutton.CheckboxButtonRenderer",
        generatedClass = "org.icefaces.ace.component.checkboxbutton.CheckboxButtonBase",
        extendsClass   = "javax.faces.component.UISelectBoolean",
        componentType  = "org.icefaces.ace.component.CheckboxButton",
        rendererType   = "org.icefaces.ace.component.CheckboxButtonRenderer",
		componentFamily= "org.icefaces.ace.CheckboxButton",
		tlddoc="This component allows entry of a button which "+
		       "supports browsers that see checkbox as true or false, "+
		       "yes or no, on or off.  LabelPosition property allows label "+
		       "to be placed on the button-in case of sam style, or to the left "+
		       "of the button - in the case of rime style."
        )
@ResourceDependencies({
		@ResourceDependency(name="yahoo-dom-event/yahoo-dom-event.js",library="yui/2_8_2"),
		@ResourceDependency(name="element/element-min.js",library="yui/2_8_2"),
		@ResourceDependency(name="button/button-min.js",library="yui/2_8_2"),
        @ResourceDependency(name="util/combined.js",library="icefaces.ace"),
		@ResourceDependency(library="icefaces.ace", name="jquery/jquery.js"),
        @ResourceDependency(library="icefaces.ace", name="jquery/ui/jquery-ui.css"),
        @ResourceDependency(library="icefaces.ace", name="checkboxbutton/checkboxbutton.css"),
        @ResourceDependency(name="button/assets/button-core.css",library="yui/2_8_2")
})
@ClientBehaviorHolder(events = {
	@ClientEvent(name="activate", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@all")
}, defaultEvent="activate")
public class CheckboxButtonMeta extends UISelectBooleanMeta {

    @Property(tlddoc="A label to be printed either on the buttton or to the left of it "+
    		" according to labelPosition parameter")
    private String label;

/*    @Property(defaultValue="left",
    		tlddoc="Default is left for rime theme. Other possibility is \"on\" " +
    				"for sam skin.")
    private String labelPosition; */

    @Property(tlddoc="style of the component, rendered on the root div of the component")
	private String style;

    @Property(tlddoc="style class of the component, rendered on the root div of the component.")
	private String styleClass;

    @Property (tlddoc="tabindex of the component")
    private Integer tabindex;

    @Property (defaultValue="false",
    		tlddoc="disabled property. If true no input may be submitted via this" +
    				" component.  Is required by aria")
    private boolean disabled;
}
