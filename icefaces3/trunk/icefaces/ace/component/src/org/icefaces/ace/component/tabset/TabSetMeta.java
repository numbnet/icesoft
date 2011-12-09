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

package org.icefaces.ace.component.tabset;

import javax.el.MethodExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;

import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import org.icefaces.ace.meta.annotation.*;

import java.util.List;

@Component(
    tagName = "tabSet",
    componentClass  = "org.icefaces.ace.component.tabset.TabSet",
    rendererClass   = "org.icefaces.ace.component.tabset.TabSetRenderer",
    generatedClass  = "org.icefaces.ace.component.tabset.TabSetBase",
    extendsClass    = "javax.faces.component.UIComponentBase",
    componentType   = "org.icefaces.ace.component.TabSet",
    rendererType    = "org.icefaces.ace.component.TabSetRenderer",
    componentFamily = "org.icefaces.ace.TabSet",
    tlddoc = "The TabSet component is a container for its TabPane children, " +
        "each of which may contain any arbitrary components. Only one " +
        "TabPane component is currently active, and its contents shown, " +
        "at any given time. The TabSet may operate in a server-side mode, " +
        "where only the current TabPane's contents exist in the browser; or " +
        "in client-side mode, where every TabPane's contents exist in the " +
        "browser, and no server round-trip is necessary to change TabPanes. " +
        "The entire TabSet may exist within a single parent form, so that " +
        "validation will apply to all of its contents, and so that " +
        "validation may enforce remaining on the current TabPane if the " +
        "user attempts to change the selected TabPane while other input " +
        "components are in an invalid state. Also, a TabSet may exist " +
        "outside of any form, perhaps with each TabPane containing their " +
        "own child form, so that validation may be more limited in scope. " +
        "In this case, a TabSetProxy may be used, in conjunction with the " +
        "TabSet, so that the TabSetProxy may be placed in a form, for " +
        "communicating with the server, removing the need for the TabSet " +
        "itself to be in a form. When changing the selected TabPane, the " +
        "TabSet may use application configurable animations to accentuate " +
        "the transition from the previously selected TabPane to the newly " +
        "selected TabPane. The label portion of the TabPanes may be shown " +
        "on the bottom, top, left, or right of the TabSet."
)
@ResourceDependencies({
    @ResourceDependency(name="util/combined.css",library="icefaces.ace"),
	@ResourceDependency(library = "icefaces.ace", name = "util/ace-jquery.js"),
	@ResourceDependency(library = "icefaces.ace", name = "util/ace-yui.js")
})
@ClientBehaviorHolder(events = {
	@ClientEvent(name="clientSideTabChange", defaultExecute="@none", defaultRender="@none",
            tlddoc="When the tabSet has clientSide=true, and a tab change occurs"),
	@ClientEvent(name="serverSideTabChange", defaultExecute="@all", defaultRender="@all",
            tlddoc="When the tabSet has clientSide=false, and a tab change occurs")
}, defaultEvent="serverSideTabChange")
public class TabSetMeta extends UIComponentBaseMeta {

    @Property(defaultValue="false", tlddoc="The default value of this " +
            "attribute is false. If true then tab change event will happen " +
            "in APPLY_REQUEST_VALUES phase and if the value of this " +
            "attribute is false then event change will happen in " +
            "INVOKE_APPLICATION phase")
    private boolean immediate;

    @Property(defaultValue="0", tlddoc="This attribute represents index of " +
            "the current selected tab")
    private int selectedIndex;

    @Property(defaultValue="top", tlddoc="This attribute represents " +
            "orientation of tabs. Valid values are bottom, top, left and right")
    private String orientation;

    @Property(defaultValue="false", tlddoc="This component supports both " +
            "client and server side tab change modal. When this attribute " +
            "is set to true, then contents of all tabs gets rendered on " +
            "client and tabchange would also occur on client. If this " +
            "attribute is set to false which is default then only current " +
            "selected tab will get rendered to the client and tab change " +
            "request will goto server to render requested tab, which allows " +
            "to send dynamic contents back.")
    private boolean clientSide;

    @Property(defaultValue="true", tlddoc="This attribute comes into " +
            "effect when there is a validation error. By default it is set " +
            "to true, which means that if on a tab change there is a " +
            "validation error, that error will be ignored and the tab will " +
            "be changed successfully. And if this attribute is set to " +
            "false, then on a validation error the tab will not be changed " +
            "until the validation error is gone.")
    private boolean cancelOnInvalid;

    @Property (tlddoc="style class will be rendered on a root element of " +
            "this component")
    private String styleClass;

    @Property (tlddoc="style will be rendered on a root element of this component")
    private String style;

    @Property(expression= Expression.METHOD_EXPRESSION,
            methodExpressionArgument="javax.faces.event.ValueChangeEvent",
            tlddoc="on tabchange value change event can be captured using this listener")
    private MethodExpression tabChangeListener;

    @Property(tlddoc = "If true then all tabs except the active one will " +
            "be disabled and can not be selected.")
    private boolean disabled;

	@Property(tlddoc="The effect when showing the contents of a tab after selecting it.")
	private String showEffect;

	@Property(tlddoc="The duration of the show effect in milliseconds. The " +
            "default value varies depending on the effect.")
	private int showEffectLength;


    @Field(javadoc="Maintains the record of which tabs have been visited")
    private List visitedTabClientIds;

    @Facets
    class FacetsMeta{
        @Facet
        UIComponent header;
        @Facet
        UIComponent body;
        @Facet
        UIComponent footer;
    }

}
