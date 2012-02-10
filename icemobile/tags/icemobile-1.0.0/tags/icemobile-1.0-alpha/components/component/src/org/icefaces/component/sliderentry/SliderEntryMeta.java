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

package org.icefaces.component.sliderentry;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Expression;
import org.icefaces.component.annotation.Property;
import org.icefaces.component.baseMeta.UIComponentBaseMeta;

import javax.el.MethodExpression;
import javax.faces.application.ResourceDependencies;

// Each Meta class requires a @Component annotation
@Component(
        // The tag name, as it will be used in view definitions (.xhtml files)
        tagName = "slider",
        // The end class that will be used by applications. Hand-coded.
        // The componentClass extends generatedClass, which extends extendsClass.
        componentClass = "org.icefaces.component.sliderentry.SliderEntry",
        // The renderer, which outputs the html markup and javascript. Hand-coded. 
        rendererClass = "org.icefaces.component.sliderentry.SliderEntryRenderer",
        // Generated, to contain all of the properties, getters, setters, and
        //  state saving methods. 
        generatedClass = "org.icefaces.component.sliderentry.SliderEntryBase",
        // The super-class of the component. Did not extend UIInput, because
        //  none of the conversion nor validation facilities it provides are
        //  relevant to a slider.
        extendsClass = "javax.faces.component.UIComponentBase",
        componentType = "org.icesoft.faces.SliderEntry",
        rendererType = "org.icesoft.faces.SliderEntryRenderer",
        componentFamily = "org.icefaces.component.SliderEntry",
        tlddoc = "The Slider Entry is a component that enables the user to adjust values in a finite range along a " +
                "horizontal or vertical axis. It can be used as a visual replacement for an input box that takes a " +
                "number as input.  "
)

@ResourceDependencies({
//	 @ResourceDependency(name="slider.js",library="org.icefaces.component.sliderentry"),
//	 @ResourceDependency(name="jquery.mobile-1.0b1.js",library="jquery.mobile"),
//	 @ResourceDependency(name="jquery.mobile-1.0b1.css",library="jquery.mobile")
})
public class SliderEntryMeta extends UIComponentBaseMeta {
    // Every java field in the Meta class can be annotated to become either a
    //  Property, Field or Facet, in the generatedClass.
    // @Property annotations are for properties that should have a StateHelper,
    //  getter method, setter method, and state saving code generated for it.
    //  It should be a public property for the component tag.
    // @Field annotations are for fields that are internal to the component,
    //  to maintain state through-out a lifecycle, or even between lifecycles,
    //  if it is not transient, and so will participate in state saving.
    // @Facet annotations, on fields, provide a means of describing and
    //  documenting component facets, in a standard way. @Facets annotations,
    //  on inner classes, provides a means of using a facet specific namespace
    //  that will not collide with samely named properties or fields.

    @Property(defaultValue = "x",
            tlddoc = "It could be either x, for horizontal or y, for vertical. Default value is set to x.")
    private String axis;

    @Property(defaultValue = "0",
            tlddoc = "The minimum value of slider, default is 0.")
    private int min;

    @Property(defaultValue = "100",
            tlddoc = "The maximum value of slider, default is 100.")
    private int max;

    @Property(defaultValue = "5",
            tlddoc = "The step of the range (max-min) to shift the " +
                    "value by.")
    private int step;

    @Property(defaultValue = "0",
            tlddoc = "The value of slider, default is 0.")
    private int value;

    @Property(defaultValue = "150px",
            tlddoc = "The physical length of slider, in pixels, default is 150px. " +
                    "Note: If the range of the slider (max-min) is greater than the length, " +
                    "then the slider can not accurately represent every value in the range. " +
                    "If the discrepancy is too great, then arrow key stepping will not " +
                    "precisely reflect the stepPercent property.")
    private String length;

    @Property(defaultValue = "false",
            tlddoc = "When singleSubmit is true, changing the value of this component will submit and execute this " +
                    "component only (equivalent to &lt;f:ajax execute=\"@this\" render=\"@all\"&gt;). " +
                    "When singleSubmit is false, no submit will occur. The default value is false.")
    private boolean singleSubmit;

    @Property(tlddoc = "style of the component")
    private String style;

    @Property(tlddoc = "tabindex of the component")
    private int tabindex;

    @Property(defaultValue = "false", tlddoc = "A flag indicating that conversion and validation of this component's value " +
            "should occur during Apply Request Values phase instead of Process Validations phase.")
    private boolean immediate;

    @Property(defaultValue = "false", tlddoc = "readonly value as passed through to html5 input")
    private boolean readonly;

    // A MethodExpression Property is a special type, that does not generate
    //  the same code, as it does not use a ValueExpression, but instead
    //  describes a method to be called, and the parameter to pass to it.
    @Property(expression = Expression.METHOD_EXPRESSION, methodExpressionArgument = "javax.faces.event.ValueChangeEvent",
            tlddoc = "MethodExpression representing a value change listener method that will be notified when a new value has " +
                    "been set for this input component. The expression must evaluate to a public method that takes a " +
                    "ValueChangeEvent  parameter, with a return type of void, or to a public method that takes no arguments " +
                    "with a return type of void. In the latter case, the method has no way of easily knowing what the new value " +
                    "is, but this can be useful in cases where a notification is needed that \"this value changed\".")
    private MethodExpression valueChangeListener;

    @Property(tlddoc = "Boolean indicating if the component should be disabled.")
    private boolean disabled;

    @Property(tlddoc = "label for jquery mobile prototyping")
    private String label;
}
