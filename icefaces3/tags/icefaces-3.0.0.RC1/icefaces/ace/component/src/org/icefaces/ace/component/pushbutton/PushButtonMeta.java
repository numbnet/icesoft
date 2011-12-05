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

package org.icefaces.ace.component.pushbutton;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.baseMeta.UICommandMeta;
import org.icefaces.ace.meta.annotation.Property;

import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.api.IceClientBehaviorHolder;

  @Component(
        tagName         = "pushButton",
        componentClass  = "org.icefaces.ace.component.pushbutton.PushButton",
        rendererClass   = "org.icefaces.ace.component.pushbutton.PushButtonRenderer",
        generatedClass  = "org.icefaces.ace.component.pushbutton.PushButtonBase",
        extendsClass    = "javax.faces.component.UICommand",
        componentType   = "org.icefaces.ace.component.PushButton",
        rendererType    = "org.icefaces.ace.component.PushButtonRenderer",
		componentFamily = "org.icefaces.ace.PushButton",
	    tlddoc = "This component allows entry of a complete form or just itself. " +
	         "It has athe same functionality of a regular jsf command button " +
	         "but without having to add extra attributes other than determining singleSubmit " +
	         "to be true or false"
        )
@ResourceDependencies({
	@ResourceDependency(name="yahoo-dom-event/yahoo-dom-event.js",library="yui/2_8_2"),
	@ResourceDependency(name="element/element-min.js",library="yui/2_8_2"),
	@ResourceDependency(name="button/button-min.js",library="yui/2_8_2"),
    @ResourceDependency(name="util/combined.js",library="icefaces.ace"),
	@ResourceDependency(library="icefaces.ace", name="jquery/jquery.js"),
    @ResourceDependency(library="icefaces.ace", name="jquery/ui/jquery-ui.css"),
    @ResourceDependency(library="icefaces.ace", name="pushbutton/pushbutton.css"),
	@ResourceDependency(name="button/assets/button-core.css",library="yui/2_8_2")
})
@ClientBehaviorHolder(events = {
	@ClientEvent(name="activate", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@all")
}, defaultEvent="activate")
public class PushButtonMeta extends UICommandMeta {
    
    @Property(tlddoc=" A localized user presentable name for this component. Used by aria.")
    private String label;
	
    @Property (defaultValue="false",
            tlddoc="disabled property. If true no input may be submitted via this" +
    				"component.  Is required by aria specs")
    private boolean disabled;
    
    @Property (tlddoc="tabindex of the component")
    private Integer tabindex;
  
    @Property(tlddoc="style class of the component, rendered on the div root of the component")
    private String styleClass;  

    @Property(tlddoc="style of the component, rendered on the div root of the component")
    private String style;
}
