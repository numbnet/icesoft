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
package org.icefaces.mobi.component.tabset;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.annotation.Property;
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
    private String currentId;

    @Property(tlddoc = " The id of the child contentPane that will be selected by default.  " +
            "This is only used once on load, and currentId must be used after to dynamically change" +
            "the currentId.")
    private String defaultId;

    @Property(tlddoc = "Inline style of the container element.")
    private String style;

    @Property(tlddoc = "Style class of the container element.")
    private String styleClass;

    /*    @Property(tlddoc = "If true then all tabs except the active one will " +
    "be disabled and can not be selected.")
 private boolean disabled; */

    @Property(
            tlddoc = "fixeHeight can be used to fix the height of the container. Must be valid height for element.style.height, " +
                    "for example: 200px.  If this attribute it not used, a calculation will be made to fix the height of the" +
                    " container to the largest height of its children")
    private String fixedHeight;

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
            tlddoc = TLDConstants.SINGLESUBMIT)
    private boolean singleSubmit;

    @Property(defaultValue = "bottom",
            tlddoc = "This attribute sets the orientation of the tabset bar " +
                    "to either bottom or top. Currently this attribute is applicable only " +
                    "to small view. " +
                    "The default value if not specified is bottom. ")
    private String orientation;

    @Property(defaultValue = "false",
            tlddoc = "Flag indicating that parent pagePanel is present with a footer.  This insures " +
                    "that the tabset with bottom orientation will be displayed above the pagePanel footer.")
    private boolean parentFooter;

    @Property(defaultValue = "true",
            tlddoc = "Flag indicating that parent pagePanel is present with a header.  This insures " +
                    "that the tabset with top orientation will be displayed below the pagePanel header.")
    private boolean parentHeader;

    @Property(defaultValue = "true",
            tlddoc = "Flag indicating that tabset render will apply an auto width calculation in order to" +
                    "display each tab evenly spaced with in the confines of the parent container.")
    private boolean autoWidth;
}
