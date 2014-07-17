/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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
package org.icefaces.mobi.component.tabset;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Field;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;
import org.icefaces.mobi.utils.TLDConstants;

import javax.el.MethodExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
        tagName = "tabSet",
        componentClass = "org.icefaces.mobi.component.tabset.TabSet",
        rendererClass = "org.icefaces.mobi.component.tabset.TabSetRenderer",
        generatedClass = "org.icefaces.mobi.component.tabset.TabSetBase",
        componentType = "org.icefaces.TabSet",
        rendererType = "org.icefaces.TabSetRenderer",
        extendsClass = "javax.faces.component.UIPanel",
        componentFamily = "org.icefaces.TabSet",
        tlddoc = "This mobility component allows contentPane's to be placed within tabset controls in  " +
                "a stacked manner.  Only a single contentPane may be active at a time."
)
@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
public class TabSetMeta extends UIPanelMeta {

    @Property(tlddoc = "The id of the contentPane that is active.")
    private String selectedId;

    @Property(tlddoc = " The id of the child contentPane that will be selected by default.  " +
            "This is only used once on load, and currentId must be used after to dynamically change" +
            "the currentId.")
    private String defaultId;

    @Property(tlddoc = "Inline style of the container element.")
    private String style;

	@Property(tlddoc = "When \"true\", (default is false), the pane with highest known content is used to " +
            "calculate the height. When client is false, this can only be calculated after all content panes have" +
            "been selected for view.  Until then, it can only reflect the max height of the selected " +
            "contentPanes.  If height attribute is used, this attribute cannot be true. ", defaultValue="false")
	private boolean autoHeight;

    @Property(tlddoc = "Style class of the container element.")
    private String styleClass;

    @Property(tlddoc = "If true then all tabs except the active one will " +
        "be disabled and can not be selected.")
    private boolean disabled;

    @Property(
            tlddoc = "attribute height can be used to fix the height of the contents. Must be valid height for element.style.height, " +
                    "for example: 200px.  If this attribute it not used, a calculation may be made to fix the height of the" +
                    " container to the largest height of its children by use of autoHeight attribute")
    private String height;

    @Property(tlddoc = "MethodExpression representing a method that will be " +
            "invoked when the selected TabPane has changed. The expression " +
            "must evaluate to a public method that takes a ValueChangeEvent " +
            "parameter, with a return type of void.",
            expression = Expression.METHOD_EXPRESSION,
            methodExpressionArgument = "javax.faces.event.ValueChangeEvent")
    private MethodExpression tabChangeListener;

    @Property(defaultValue = "false", tlddoc = TLDConstants.IMMEDIATE_INPUT)
    private boolean immediate;

    @Property(defaultValue = "true",
            tlddoc = TLDConstants.SINGLESUBMIT+" currently may only be true.")
    private boolean singleSubmit;

    @Property(defaultValue = "top",
            tlddoc = "This attribute sets the orientation of the tabset bar " +
                    "to either bottom or top. " +
                    "The default value if not specified is top. ")
    private String orientation;

    @Property(defaultValue = "true")
    private boolean autoWidth;

    @Property(defaultValue = "true", tlddoc="This attribute, when true, will allow the tab content " +
            "region to fit to the bounds of the parent container of the tabset. " )
    private boolean fitToParent;

    @Field
    private String hashVal;

    @Field(defaultValue = "false")
    private Boolean parentFooter;

    @Field(defaultValue = "true")
    private Boolean parentHeader;

}
