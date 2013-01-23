/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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

package org.icefaces.mobi.component.carousel;


import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.baseMeta.UISeriesBaseMeta;
import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.mobi.utils.TLDConstants;

import javax.el.MethodExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;



@Component(
        tagName = "carousel",
        componentClass = "org.icefaces.mobi.component.carousel.Carousel",
        rendererClass = "org.icefaces.mobi.component.carousel.CarouselRenderer",
        generatedClass = "org.icefaces.mobi.component.carousel.CarouselBase",
        componentType = "org.icefaces.Carousel",
        rendererType = "org.icefaces.CarouselRenderer",
        extendsClass = "org.icefaces.impl.component.UISeriesBase",
        componentFamily = "org.icefaces.Carousel",
        tlddoc = "Renders a scrollable, touch-enabled, horizontal list. "
)
@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util",
                name = "component.js")
})
@ClientBehaviorHolder(events = {
        @ClientEvent(name = "change",
                javadoc = "Fired when the carousel position is changed.",
                tlddoc = "Fired when the carousel position is changed. ",
                defaultRender = "@this", defaultExecute = "@all")
})
public class CarouselMeta extends UISeriesBaseMeta {

    @Property(defaultValue = "false", tlddoc = TLDConstants.SINGLESUBMIT)
    private boolean singleSubmit;

    @Property(tlddoc = TLDConstants.STYLE)
    private String style;

    @Property(tlddoc = TLDConstants.STYLECLASS)
    private String styleClass;

  /*  @Property(tlddoc = "The inline CSS style to be rendered for every list " +
    		"element of this component. ")
    private String itemStyle;


    @Property(tlddoc = "The CSS style class to be rendered for every list " +
    		"element of this component")
    private String itemStyleClass; */

    @Property(expression = Expression.METHOD_EXPRESSION, methodExpressionArgument = "javax.faces.event.ValueChangeEvent",
            tlddoc = TLDConstants.VALUECHANGELISTENER)
    private MethodExpression valueChangeListener;

    @Property(defaultValue = "0", tlddoc = "The index of the currently selected item. ")
    private int selectedItem;

    @Property(defaultValue = "false", tlddoc = TLDConstants.IMMEDIATE_INPUT)
    private boolean immediate;

    @Property(tlddoc = "When \"true\", the label for the previous link will be shown. ")
    private String previousLabel;

    @Property(tlddoc = "When \"true\", the label for the next link will be shown. ")
    private String nextLabel;

    @Property(defaultValue="false", tlddoc = TLDConstants.DISABLED)
    private boolean disabled;
}
