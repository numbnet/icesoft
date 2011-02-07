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

package org.icefaces.component.tab;

import javax.el.MethodExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;

import org.icefaces.component.annotation.*;

import org.icefaces.component.baseMeta.UIComponentBaseMeta;

@Component(
    tagName = "tabSet",
    componentClass  = "org.icefaces.component.tab.TabSet",
    rendererClass   = "org.icefaces.component.tab.TabSetRenderer",
    generatedClass  = "org.icefaces.component.tab.TabSetBase",
    extendsClass    = "javax.faces.component.UIComponentBase",
    componentType   = "org.icefaces.TabSet",
    rendererType    = "org.icefaces.TabSetRenderer",
    componentFamily = "org.icefaces.TabSet",
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
	@ResourceDependency(name="yui/yui-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="loader/loader-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="oop/oop-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="event-custom/event-custom-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="attribute/attribute-base-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="base/base-base-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="event/event-base-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="dom/dom-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="node/node-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="event/event-delegate-min.js",library="yui/3_1_1"),
    @ResourceDependency(name ="anim/anim-min.js",library = "yui/3_1_1"),
    @ResourceDependency(name ="plugin/plugin-min.js",library = "yui/3_1_1"),    
    @ResourceDependency(name ="pluginhost/pluginhost-min.js",library = "yui/3_1_1"),      
    @ResourceDependency(name="util.js",library="org.icefaces.component.util"),
    @ResourceDependency(name="component.js",library="org.icefaces.component.util"),    
    @ResourceDependency(name="yui3.js",library="org.icefaces.component.util"),   
    @ResourceDependency(name="animation.js",library="org.icefaces.component.animation"),
    @ResourceDependency(name="animation.css",library="org.icefaces.component.animation") ,	
	@ResourceDependency(name="2_8_1/fonts/fonts-min.css",library="yui"),
	@ResourceDependency(name="2_8_1/yahoo-dom-event/yahoo-dom-event.js",library="yui"),
	@ResourceDependency(name="2_8_1/connection/connection-min.js",library="yui"),
	@ResourceDependency(name="2_8_1/element/element-min.js",library="yui"),
	@ResourceDependency(name="2_8_1/tabview/tabview-min.js",library="yui"),
    @ResourceDependency(name="tabset.js",library="org.icefaces.component.tab"),
    @ResourceDependency(name="tabset.css",library="org.icefaces.component.tab")    
})
@ClientBehaviorHolder (events={"transition"}, defaultEvent="transition")
public class TabSetMeta extends UIComponentBaseMeta {
    
    @Property(defaultValue="false", tlddoc="The default value of this attribute is fals. If true then tab change event will happen in APPLY_REQUEST_VALUES phase and if the value of this attribute is false then event change will happen in INVOKE_APPLICATION phase")    
    private boolean immediate; 
    
    @Property(defaultValue="0", tlddoc="This attribute represents index of the current selected tab")
    private int selectedIndex;
    
    @Property(defaultValue="top", tlddoc="This attribute represents orientation of tabs. Valid values are bottom, top, left and right")   
    private String orientation;
    
    @Property(defaultValue="false", tlddoc="This component supports both client and server side tab change modal. When this attribute is set to true, then contents of all tabs gets rendered on client and tabchange would also occur on client. If this attribute is set to false which is default then only current selected tab will get rendered to the client and tab change request will goto server to render requested tab, which allows to send dynamic contents back.")       
    private boolean clientSide; 
   
    @Property(defaultValue="false", tlddoc="The default value of this attribute is false, so in this case full submit is being used, where all component gets rendered and executed. If this attribute is set to true, then only this component gets executed and entire view gets rendered")
    private boolean singleSubmit;
    
    @Property(defaultValue="true", tlddoc="This attribute comes into effect when there is a validation error. By default it is set to true, which means that if on a tab change there is a validation error, that error will be ignored and tab will be changed successfully and if this attribute is set to false then on a validation error tab will not be changed untill validation error gone.") 
    private boolean cancelOnInvalid;    
    
    @Property (tlddoc="style class will be rendered on a root element of this component")
    private String styleClass;
    
    @Property (tlddoc="style will be rendered on a root element of this component") 
    private String style;
    
    @Property(expression=Expression.METHOD_EXPRESSION, methodExpressionArgument="javax.faces.event.ValueChangeEvent",
            tlddoc="on tabchange value change event can be captured using this listener")
    private MethodExpression tabChangeListener;

    @Property(tlddoc = "If true then all tabs except the active one will be disabled and can not be selected.")
    private boolean disabled;

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
