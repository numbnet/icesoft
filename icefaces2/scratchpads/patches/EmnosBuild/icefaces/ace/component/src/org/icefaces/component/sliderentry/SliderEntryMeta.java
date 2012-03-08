/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.component.sliderentry;

import javax.el.MethodExpression;

import org.icefaces.component.annotation.*;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.component.baseMeta.UIComponentBaseMeta;

// Each Meta class requires a @Component annotation
@Component(
        // The tag name, as it will be used in view definitions (.xhtml files)
        tagName ="sliderEntry",
        // The end class that will be used by applications. Hand-coded.
        // The componentClass extends generatedClass, which extends extendsClass.
        componentClass ="org.icefaces.component.sliderentry.SliderEntry",
        // The renderer, which outputs the html markup and javascript. Hand-coded. 
        rendererClass ="org.icefaces.component.sliderentry.SliderEntryRenderer",
        // Generated, to contain all of the properties, getters, setters, and
        //  state saving methods. 
        generatedClass = "org.icefaces.component.sliderentry.SliderEntryBase",
        // The super-class of the component. Did not extend UIInput, because
        //  none of the conversion nor validation facilities it provides are
        //  relevant to a slider.
        extendsClass = "javax.faces.component.UIComponentBase",
        componentType = "org.icesoft.faces.SliderEntry",
        rendererType = "org.icesoft.faces.SliderEntryRenderer",
        componentFamily="org.icefaces.component.SliderEntry",
        tlddoc="The Slider Entry is a component that enables the user to adjust values in a finite range along a " +
                "horizontal or vertical axis. It can be used as a visual replacement for an input box that takes a " +
                "number as input. For more information, see the " +
                "<a href=\"http://wiki.icefaces.org/display/facesDev/Slider\">Wiki doc</a>."
    )
	
/*
	@ResourceDependency(name="loader/loader-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="oop/oop-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="yui/yui-later-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="event-custom/event-custom-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="attribute/attribute-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="event/event-base-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="pluginhost/pluginhost-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="dom/dom-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="node/node-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="event/event-delegate-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="event/event-focus-min.js",library="yui/3_1_1"),
//	@ResourceDependency(name="event/event-synthetic-min.js",library="yui/3_2_0"),
	@ResourceDependency(name="base/base-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="classnamemanager/classnamemanager-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="widget/widget-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="yui/yui-throttle-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="dd/dd-ddm-base-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="dd/dd-drag-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="dd/dd-constrain-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="dump/dump-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="substitute/substitute-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="slider/slider-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="intl/intl-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="widget/assets/skins/sam/widget.css",library="yui/3_1_1"),
	@ResourceDependency(name="yahoo-dom-event/yahoo-dom-event.js",library="yui/2_8_1"),
*/

@ResourceDependencies({
    @ResourceDependency(name="yui/yui-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="loader/loader-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="util.js",library="org.icefaces.component.util"),
    @ResourceDependency(name="component.js",library="org.icefaces.component.util"),
    @ResourceDependency(name="slider.js",library="org.icefaces.component.sliderentry"),
	@ResourceDependency(name="slider.css",library="org.icefaces.component.sliderentry")
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
    
    @Property (defaultValue="x", 
            tlddoc="It could be either x, for horizontal or y, for vertical. Default value is set to x.")
    private String axis;
    
    @Property (defaultValue="0", 
            tlddoc="The minimum value of slider, default is 0.")
    private int min;
    
    @Property (defaultValue="100",
            tlddoc="The maximum value of slider, default is 100.")
    private int max;

    @Property (defaultValue="10.0f",
            tlddoc="The percentage of the range (max-min) to shift the " +
                    "value by when the up, down, left or right arrow keys " +
                    "are pressed. Expressed as a percentage from 0 to 100.")
    private float stepPercent;
    
    @Property (defaultValue="0",
            tlddoc="The value of slider, default is 0.")
    private int value;    
    
    @Property (defaultValue="150px",
            tlddoc="The physical length of slider, in pixels, default is 150px. " +
            "Note: If the range of the slider (max-min) is greater than the length, " +
            "then the slider can not accurately represent every value in the range. " +
            "If the discrepancy is too great, then arrow key stepping will not " +
            "precisely reflect the stepPercent property.")
    private String length;
    
    @Property(defaultValue="false",
            tlddoc="When singleSubmit is true, changing the value of this component will submit and execute this " +
                    "component only (equivalent to &lt;f:ajax execute=\"@this\" render=\"@all\"&gt;). " +
                    "When singleSubmit is false, no submit will occur. The default value is false.")
    private boolean singleSubmit;
    
    @Property(tlddoc="style of the component")
    private String style;
    
    @Property(tlddoc="style class of the component, the renderer doesn't render any default class.")
    private String styleClass;  

    @Property(tlddoc = "Path to the thumb image.")
    private String thumbUrl; 

    @Property (tlddoc="tabindex of the component")
    private Integer tabindex;
    
    @Property(defaultValue="false", tlddoc="A flag indicating that conversion and validation of this component's value " +
            "should occur during Apply Request Values phase instead of Process Validations phase.")
    private boolean immediate;
	
    @Property(defaultValue="true", tlddoc="Allows clicking on the rail to move the thumb. Default is true.")
    private boolean clickableRail;
    
    // A MethodExpression Property is a special type, that does not generate
    //  the same code, as it does not use a ValueExpression, but instead
    //  describes a method to be called, and the parameter to pass to it.
    @Property(expression=Expression.METHOD_EXPRESSION, methodExpressionArgument="javax.faces.event.ValueChangeEvent",
    tlddoc = "MethodExpression representing a value change listener method that will be notified when a new value has " +
            "been set for this input component. The expression must evaluate to a public method that takes a " +
            "ValueChangeEvent  parameter, with a return type of void, or to a public method that takes no arguments " +
            "with a return type of void. In the latter case, the method has no way of easily knowing what the new value " +
            "is, but this can be useful in cases where a notification is needed that \"this value changed\".")
    private MethodExpression valueChangeListener;

    @Property (tlddoc="Boolean indicating if the component should be disabled.")
    private boolean disabled;
    
    @Property(defaultValue="false",
            tlddoc="If set to true, it will show min, mid and max value labels. The default value is false.")
    private boolean showLabels;    
}
