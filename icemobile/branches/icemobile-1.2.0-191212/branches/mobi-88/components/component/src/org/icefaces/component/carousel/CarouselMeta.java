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

package org.icefaces.component.carousel;


import org.icefaces.component.annotation.*;


import org.icefaces.component.baseMeta.UIComponentBaseMeta;

import javax.el.MethodExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;


@Component(
        tagName = "carousel",
        componentClass = "org.icefaces.component.carousel.Carousel",
        rendererClass = "org.icefaces.component.carousel.CarouselRenderer",
        generatedClass = "org.icefaces.component.carousel.CarouselBase",
        componentType = "org.icefaces.Carousel",
        rendererType = "org.icefaces.CarouselRenderer",
        extendsClass = "javax.faces.component.UIData",
        componentFamily = "org.icefaces.Carousel",
        tlddoc = "This mobility component " +
                "represents a grouping of a grouping of <li> tags that make up a scrollable "+
                "array of pictures, touch enabled. "
)

@ResourceDependencies({
	@ResourceDependency(library="org.icefaces.component.util", name="iscroll.js"),
	@ResourceDependency(library="org.icefaces.component.util", name="component.js")
})
public class CarouselMeta extends UIComponentBaseMeta {
	
    @Property(defaultValue="Integer.MIN_VALUE",
    		  tlddoc = "number of child li elements that make up the carousel.  Is provided "+
    		  " by the component for access in backing bean in case the user wants to control "+
    		  " the number of items to be viewed.  Will possilby be used for paging at a later "+
    		  " stage of component development.")
    private int count;

    @Property(defaultValue = "false",
            tlddoc = "When singleSubmit is true, triggering an action on this component will submit" +
                    " and execute this component only. Equivalent to <f:ajax execute='@this' render='@all'>." +
                    " When singleSubmit is false, triggering an action on this component will submit and execute " +
                    " the full form that this component is contained within." +
                    " The default value is false.")
    private boolean singleSubmit;
    
    @Property(tlddoc = "style will be rendered on the root element of this " +
    "component.")
    private String style;

    @Property(tlddoc = "style class will be rendered on the root element of " +
        "this component.")
    private String styleClass;
    
    @Property(tlddoc = "style class for item will be rendered list element of " +
    "this component.")
    private String itemStyle; 
    

    @Property(tlddoc = "style class will be rendered on the list element of " +
        "this component.")
    private String itemStyleClass;
    
    @Property(expression= Expression.METHOD_EXPRESSION, methodExpressionArgument="javax.faces.event.ValueChangeEvent",
    	    tlddoc = "MethodExpression representing a value change listener method that will be notified when a new value has " +
    	            "been set for this input component. The expression must evaluate to a public method that takes a " +
    	            "ValueChangeEvent  parameter, with a return type of void, or to a public method that takes no arguments " +
    	            "with a return type of void. In the latter case, the method has no way of easily knowing what the new value " +
    	            "is, but this can be useful in cases where a notification is needed that \"this value changed\".")
    private MethodExpression valueChangeListener;
    
    @Property(defaultValue = "0", tlddoc=" selectedItem will show the item in the list at the center location")
    private int selectedItem;
    
    @Property(defaultValue="false", tlddoc="The default value of this attribute is false. If true then value change event will happen in APPLY_REQUEST_VALUES phase and if the value of this attribute is false then event change will happen in INVOKE_APPLICATION phase")    
    private boolean immediate; 
    /** other possible attributes include vertical, scroll increment, circular, numbershown, currentIndex */
}
