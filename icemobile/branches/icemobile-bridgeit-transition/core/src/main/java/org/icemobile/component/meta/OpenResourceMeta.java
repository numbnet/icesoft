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

package org.icemobile.component.meta;


import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Field;
import org.icefaces.ace.meta.annotation.Only;
import org.icefaces.ace.meta.annotation.OnlyType;
import org.icefaces.ace.meta.annotation.JSP;
import org.icemobile.component.baseMeta.SimpleMeta;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;


@Component(
        tagName = "openResource",
        componentClass = "org.icefaces.mobi.component.openresource.OpenResource",
        rendererClass = "org.icefaces.mobi.component.openresource.OpenResourceRenderer",
        generatedClass = "org.icefaces.mobi.component.openresource.OpenResourceBase",
        componentType = "org.icefaces.OpenResource",
        rendererType = "org.icefaces.OpenResourceRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.OpenResource",
        tlddoc = "This component provides a link to a resource that will be opened on a mobile device" +
                " For desktop, this falls back to a simple hyperlink. For the short term it can be styled" +
                " as a button or a link, whichever is easier. On compatible devices," +
                " this is a device command where the value of the link is passed to an \"open\" command. "
)
@JSP(tagName                        = "openResource",
     tagClass                       = "org.icemobile.jsp.tags.resource.OpenResourceTag",
     generatedTagClass              = "org.icemobile.jsp.tags.resource.OpenResourceBaseTag",
     generatedInterfaceClass        = "org.icemobile.component.IOpenResource",
     generatedInterfaceExtendsClass = "org.icemobile.component.IBaseClientComponent",
     tlddoc = "This component provides a link to a resource that will be opened on a mobile device" +
                " For desktop, this falls back to a simple hyperlink. For the short term it can be styled" +
                " as a button or a link, whichever is easier. On compatible devices," +
                " this is a device command where the value of the link is passed to an \"open\" command. " )
@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})

public class OpenResourceMeta extends SimpleMeta{

    @Property(tlddoc = "The link to resource source. for now a url ")
    private Object value;

    @Only(OnlyType.JSF)
    @Property(defaultValue = "session", tlddoc="The JSF Resource scope of the object resolving from the \"value\" " +
    		"attribute. Possible values are \"flash\", \"request\", \"view\", \"session\", and \"application\". ")
    private String scope;

    @Only(OnlyType.JSF)
    @Property(tlddoc = "The name is used for JSF Resource registration. ")
    private String name;

    @Only(OnlyType.JSF)
    @Property(tlddoc = "The library used for JSF Resource registration. ")
    private String library;

    @Property( tlddoc = "The label for the anchor element that is visible to the user")
    private String label;

    @Property(tlddoc="The mimeType of the resource.  Not sure if it is required yet")
    private String mimeType;

    @Field
    private String srcAttribute;

}

