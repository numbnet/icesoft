/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
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
 * See the License for the specific language governing permissions an
 * limitations under the License.
 */

package org.icefaces.component.flipswitch;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Expression;
import org.icefaces.component.annotation.Property;
import org.icefaces.component.baseMeta.UIComponentBaseMeta;
import org.icefaces.component.baseMeta.UISelectBooleanMeta;

import javax.el.MethodExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

// Each Meta class requires a @Component annotation
@Component(
        // The tag name, as it will be used in view definitions (.xhtml files)
        tagName = "flipswitch",
        // The end class that will be used by applications. Hand-coded.
        // The componentClass extends generatedClass, which extends extendsClass.
        componentClass = "org.icefaces.component.flipswitch.FlipSwitch",
        // The renderer, which outputs the html markup and javascript. Hand-coded. 
        rendererClass = "org.icefaces.component.flipswitch.FlipSwitchRenderer",
        // Generated, to contain all of the properties, getters, setters, and
        //  state saving methods. 
        generatedClass = "org.icefaces.component.flipswitch.FlipSwitchBase",
        // The super-class of the component. Did not extend UIInput, because
        //  none of the conversion nor validation facilities it provides are
        //  relevant to a slider.
        extendsClass = "javax.faces.component.UISelectBoolean",
        componentType = "org.icesoft.faces.FlipSwitch",
        rendererType = "org.icesoft.faces.FlipSwitchRenderer",
        componentFamily = "org.icefaces.component.FlipSwitch",
        tlddoc = "The FlipSwitch is a component that enables the user to turn a feature on or off "                
)

@ResourceDependencies({
	 @ResourceDependency(name="component.js",library="org.icefaces.component.util")
})
public class FlipSwitchMeta extends UISelectBooleanMeta {

    @Property(defaultValue = "false",
            tlddoc = "When singleSubmit is true, changing the value of this component will submit and execute this " +
                    "component only (equivalent to &lt;f:ajax execute=\"@this\" render=\"@all\"&gt;). " +
                    "When singleSubmit is false, no submit will occur. The default value is false.")
    private boolean singleSubmit;

    @Property(tlddoc = "style of the component")
    private String style;

    @Property(defaultValue = "Integer.MIN_VALUE", tlddoc = "tabindex of the component")
    private int tabindex;

    @Property(tlddoc = "Boolean indicating if the component should be disabled.")
    private boolean disabled;

    @Property(defaultValue="ON", tlddoc = "label for switch when on ...not sure if we will use this yet?")
    private String labelOn;
    @Property(defaultValue="OFF", tlddoc = "label for switch when off ...not sure if we will use this yet?")
    private String labelOff;    

}
