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
package org.icefaces.mobi.component.contentpane;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;
import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.meta.annotation.Required;
import org.icefaces.ace.meta.annotation.Implementation;
import org.icefaces.mobi.utils.TLDConstants;

import javax.faces.component.UIComponent;

@Component(
        tagName = "contentPane",
        componentClass = "org.icefaces.mobi.component.contentpane.ContentPane",
        rendererClass = "org.icefaces.mobi.component.contentpane.ContentPaneRenderer",
        generatedClass = "org.icefaces.mobi.component.contentpane.ContentPaneBase",
        handlerClass = "org.icefaces.mobi.component.contentpane.ContentPaneHandler",
        componentType = "org.icefaces.ContentPane",
        rendererType = "org.icefaces.ContentPaneRenderer",
        extendsClass = "javax.faces.component.UIPanel",
        componentFamily = "org.icefaces.ContentPane",
        tlddoc = "This mobility component is a child of several different layout components such as contentStack, " +
                "accordion or tabSet, or any other future mobility component which implements ContentPaneController.  " +
                "The facelet attribute determines whether the children of this component are constructed in the " +
                "server side component tree. A special tag handler is utilised to assist with this task.  The desired " +
                "outcome of this feature is to allow developers to maintain as small a server-side component tree as " +
                "possible for purposes of increased scalability." +
                "The facelet attribute defaults to false meaning that the children are constructed " +
                "and present in server side component tree. " +
                "If the client attribute is true, this means children of this component are not only constructed but " +
                "area also always rendered on the client --best for static data  "
)

public class ContentPaneMeta extends UIPanelMeta{

    @Property(defaultValue=" ", tlddoc="This attribute is only used when ContentPane is child of accordion " +
            "or tabSet component, and refers to the text that is rendered on the accordion handle or tabSet tab.")
    private String title;

    @Property(tlddoc = TLDConstants.STYLE)
    private String style;

    @Property(tlddoc = TLDConstants.STYLECLASS)
    private String styleClass;

    @Property(defaultValue="false", implementation = Implementation.EXISTS_IN_SUPERCLASS,
               tlddoc = "If this attribute is \"true\" the component utilizes the ContentPaneHandler to optimise " +
                       "server-side performance by reducing the size of the server-side component tree. " +
                       "Any non-selected contentPane will not have its children added to the component tree. " +
                       "If this attribute evaluates to \"false\", then normal jsf construction of the component tree occurs. " +
                       "Must have client=\"false\" which is the default value for that attribute.. " +
                       "Default value for this attribute is false.")
    private boolean facelet;

    @Property(defaultValue="false",
             tlddoc = "If this attribute evaluates to \"true\", the contentPane's children are always rendered to the client/browser. " +
                     "This is ideal for static content. " +
                     "The acelet attribute is only relevant if this attribute is false.")
    private boolean client;

    @Property(defaultValue="false",
             tlddoc = "Determines if this contentPane contains either a layoutMenu or Home page for a single " +
                     "page application using contentStackMenu with a contentStack containing contentPanes. " +
                     "This attribute helps assist in sliding panes effects for transition definition. ")
    private boolean menuOrHome;

    @Property(tlddoc = "The Font Awesome icon to render (Tabs)")
    private String icon;


}
