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

package org.icefaces.mobi.component.flipswitch;

import org.icefaces.ace.meta.annotation.*;
import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;

import org.icefaces.ace.meta.baseMeta.UISelectBooleanMeta;

import javax.el.MethodExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

// Each Meta class requires a @Component annotation
@Component(
        // The tag name, as it will be used in view definitions (.xhtml files)
        tagName = "flipswitch",
        // The end class that will be used by applications. Hand-coded.
        // The componentClass extends generatedClass, which extends extendsClass.
        componentClass = "org.icefaces.mobi.component.flipswitch.FlipSwitch",
        // The renderer, which outputs the html markup and javascript. Hand-coded. 
        rendererClass = "org.icefaces.mobi.component.flipswitch.FlipSwitchRenderer",
        // Generated, to contain all of the properties, getters, setters, and
        //  state saving methods. 
        generatedClass = "org.icefaces.mobi.component.flipswitch.FlipSwitchBase",
        // The super-class of the component. Did not extend UIInput, because
        //  none of the conversion nor validation facilities it provides are
        //  relevant to a slider.
        extendsClass = "javax.faces.component.UISelectBoolean",
        componentType = "org.icesoft.faces.FlipSwitch",
        rendererType = "org.icesoft.faces.FlipSwitchRenderer",
        componentFamily = "org.icefaces.component.FlipSwitch",
        tlddoc = "The flipswitch provides a control that toggles between on and off."                
)


@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
@ClientBehaviorHolder(events = {
	@ClientEvent(name="activate", javadoc="...", tlddoc="...", defaultRender="@this", defaultExecute="@all")
}, defaultEvent="activate")
public class FlipSwitchMeta extends UISelectBooleanMeta {

    @Property(defaultValue = "false",
            tlddoc = org.icefaces.mobi.utils.TLDConstants.SINGLESUBMIT)
    private boolean singleSubmit;

    @Property(tlddoc = org.icefaces.mobi.utils.TLDConstants.STYLE)
    private String style;

    @Property(defaultValue = "Integer.MIN_VALUE", tlddoc = org.icefaces.mobi.utils.TLDConstants.TABINDEX)
    private int tabindex;

    @Property(tlddoc = org.icefaces.mobi.utils.TLDConstants.DISABLED)
    private boolean disabled;

    @Property(tlddoc=org.icefaces.mobi.utils.TLDConstants.READONLY)
    private boolean readonly;

    @Property(defaultValue="ON", tlddoc = "The label for switch when on.")
    private String labelOn;
    @Property(defaultValue="OFF", tlddoc = "The label for switch when off.")
    private String labelOff;    

}
