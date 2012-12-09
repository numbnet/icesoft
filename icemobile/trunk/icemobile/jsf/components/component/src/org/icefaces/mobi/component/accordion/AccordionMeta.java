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

package org.icefaces.mobi.component.accordion;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;
import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.meta.annotation.Required;
import javax.el.MethodExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;


@Component(
        tagName = "accordion",
        componentClass = "org.icefaces.mobi.component.accordion.Accordion",
        rendererClass = "org.icefaces.mobi.component.accordion.AccordionRenderer",
        generatedClass = "org.icefaces.mobi.component.accordion.AccordionBase",
        componentType = "org.icefaces.Accordion",
        rendererType = "org.icefaces.AccordionRenderer",
        extendsClass = "javax.faces.component.UIPanel",
        componentFamily = "org.icefaces.Accordion",
        tlddoc = "This mobility component allows panels to be placed within accordion controls in  " +
                "a stacked manner.  Only a single pane may be active at a time which is the selectedId " +
                "which must be bound to a value in a backing bean as the accordion component makes use of " +
                "the ContentPaneTagHandler class. " +
                "Default for the" +
                "content pane height will be autoHeight of true, in which case the contentPanes " +
                "height will all be set to the largest height of all the contentPanes.  Since some of the " +
                "contentPane children may not be rendered yet, the process to find the maximum height " +
                "of contentPanes may be ongoing until all the panes have been opened.  " +
                "If autoHeight is set to false, then a fixedHeight may be used.  For older mobile browsers, " +
                "the scrollablePaneContent attribute should be set to false."
)
@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
public class AccordionMeta extends UIPanelMeta {
    
    @Property( tlddoc="id of the panel that is active in the accordion.")
    private String selectedId;

	@Property(tlddoc="Inline style of the container element.")
	private String style;

	@Property(tlddoc="Style class of the container element.")
	private String styleClass;

	@Property(tlddoc="Disables or enables the accordion.", defaultValue="false")
	private boolean disabled;

	@Property(tlddoc="When set to true (default), pane with highest content is used to calculate the height.", defaultValue="true")
	private boolean autoHeight;

    @Property(tlddoc="This attribute can be used only when autoHeight is false. It must represent a " +
            " valid height for element.style.height.  " +
            "If no height is set and autoHeight is false, then " +
            "panes for accordion will just render at their content height. if content is larger than" +
            "height of content pane, then contents will scroll")
    private String height;

    @Property(defaultValue="false",
              tlddoc="This attribute is only used when autoHeight is false and a value is set into " +
            "the height property.  Currently, android container and stock browsers on older mobile " +
            "devices will not work correctly when using this feature. When all browsers support the " +
            "scrolling feature, this attribute will be removed and the feature made automatic.")
    private boolean scrollablePaneContent;

 	@Property(tlddoc="Server side listener to invoke when active pane changes",
                  expression= Expression.METHOD_EXPRESSION,
                  methodExpressionArgument="javax.faces.event.ValueChangeEvent")
	private MethodExpression paneChangeListener;
}
