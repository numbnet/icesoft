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


import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;
import org.icefaces.component.baseMeta.UIComponentBaseMeta;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;


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
	@ResourceDependency(library="org.icefaces.component.carousel", name="carousel.js")
})
public class CarouselMeta extends UIComponentBaseMeta {
	
    @Property(defaultValue="Integer.MIN_VALUE",
    		  tlddoc = "number of child li elements that make up the carousel")
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
    
    @Property(defaultValue = "0", tlddoc=" selectedItem will show the item in the list at the center location")
    private int selectedItem;
    /** other possible attributes include vertical, scroll increment, circular, numbershown, currentIndex */
}
