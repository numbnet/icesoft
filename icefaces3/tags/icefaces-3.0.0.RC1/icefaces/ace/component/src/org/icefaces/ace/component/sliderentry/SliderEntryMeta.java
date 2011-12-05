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

package org.icefaces.ace.component.sliderentry;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import org.icefaces.ace.api.IceClientBehaviorHolder;

import javax.el.MethodExpression;

@Component(
        // The tag name, as it will be used in view definitions (.xhtml files)
        tagName ="sliderEntry",
        // The end class that will be used by applications. Hand-coded.
        // The componentClass extends generatedClass, which extends extendsClass.
        componentClass ="org.icefaces.ace.component.sliderentry.SliderEntry",
        // The renderer, which outputs the html markup and javascript. Hand-coded. 
        rendererClass ="org.icefaces.ace.component.sliderentry.SliderEntryRenderer",
        // Generated, to contain all of the properties, getters, setters, and
        //  state saving methods. 
        generatedClass = "org.icefaces.ace.component.sliderentry.SliderEntryBase",
        // The super-class of the component. Did not extend UIInput, because
        //  none of the conversion nor validation facilities it provides are
        //  relevant to a slider.
        extendsClass = "javax.faces.component.UIComponentBase",
        componentType = "org.icefaces.ace.component.SliderEntry",
        rendererType = "org.icefaces.ace.component.SliderEntryRenderer",
        componentFamily="org.icefaces.ace.SliderEntry",
        tlddoc="The Slider Entry is a component that enables the user to adjust values in a finite range along a " +
                "horizontal or vertical axis. It can be used as a visual replacement for an input box that takes a " +
                "number as input. For more information, see the " +
                "<a href=\"http://wiki.icefaces.org/display/ICE/SliderEntry\">Wiki doc</a>."
        )
@ResourceDependencies({
	@ResourceDependency(library="icefaces.ace", name="jquery/ui/jquery-ui.css"),
	@ResourceDependency(library="icefaces.ace", name="jquery/jquery.js"),
	@ResourceDependency(library="icefaces.ace", name="jquery/ui/jquery-ui.js"),
	@ResourceDependency(library="icefaces.ace", name="core/core.js"),
	@ResourceDependency(library="icefaces.ace", name="sliderentry/slider.js")
})
@ClientBehaviorHolder(events = {
	@ClientEvent(name="slideStart", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@all"),
	@ClientEvent(name="slide", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@all"),
	@ClientEvent(name="slideEnd", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@all")
}, defaultEvent="slideEnd")
public class SliderEntryMeta extends UIComponentBaseMeta {

	@Property(tlddoc="")
	private String widgetVar;
	
	@Property(tlddoc="", defaultValue="0")
	private int min;
	
	@Property(tlddoc="", defaultValue="100")
	private int max;
	
	@Property(tlddoc="")
	private String style;
	
	@Property(tlddoc="")
	private String styleClass;
	
	@Property(tlddoc="", defaultValue="true")
	private boolean animate;
	
	@Property(tlddoc="", defaultValue="x")
	private String axis;
	
	@Property(tlddoc="", defaultValue="1f")
	private float stepPercent;
	
	@Property(tlddoc="", defaultValue="false")
	private boolean disabled;
	
	@Property(tlddoc="")
	private String onSlideStart;
	
	@Property(tlddoc="")
	private String onSlide;
	
	@Property(tlddoc="")
	private String onSlideEnd;
	
    @Property (tlddoc="The value of slider, default is 0.", defaultValue="0")
    private int value;
	
    @Property (defaultValue="150px",
            tlddoc="The physical length of slider, in pixels, default is 150px. " +
            "Note: If the range of the slider (max-min) is greater than the length, " +
            "then the slider can not accurately represent every value in the range. " +
            "If the discrepancy is too great, then arrow key stepping will not " +
            "precisely reflect the stepPercent property.")
    private String length;
	
    @Property(defaultValue="true", tlddoc="Allows clicking on the rail to move the thumb. Default is true.")
    private boolean clickableRail;
	
    @Property(defaultValue="false",
            tlddoc="If set to true, it will show min, mid and max value labels. The default value is false.")
    private boolean showLabels;
	
    @Property(defaultValue="false", tlddoc="A flag indicating that conversion and validation of this component's value " +
            "should occur during Apply Request Values phase instead of Process Validations phase.")
    private boolean immediate;
	
    @Property (tlddoc="tabindex of the component")
    private Integer tabindex;
	
    // A MethodExpression Property is a special type, that does not generate
    //  the same code, as it does not use a ValueExpression, but instead
    //  describes a method to be called, and the parameter to pass to it.
    @Property(expression= Expression.METHOD_EXPRESSION, methodExpressionArgument="javax.faces.event.ValueChangeEvent",
    tlddoc = "MethodExpression representing a value change listener method that will be notified when a new value has " +
            "been set for this input component. The expression must evaluate to a public method that takes a " +
            "ValueChangeEvent  parameter, with a return type of void, or to a public method that takes no arguments " +
            "with a return type of void. In the latter case, the method has no way of easily knowing what the new value " +
            "is, but this can be useful in cases where a notification is needed that \"this value changed\".")
    private MethodExpression valueChangeListener;
}
