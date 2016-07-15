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

package org.icefaces.mobi.component.stylesheet;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;


@Component(
        tagName = "deviceStylesheet",
        componentClass = "org.icefaces.mobi.component.stylesheet.DeviceStyleSheet",
        rendererClass = "org.icefaces.mobi.component.stylesheet.DeviceStyleSheetRenderer",
        generatedClass = "org.icefaces.mobi.component.stylesheet.DeviceStyleSheetBase",
        componentType = "org.icefaces.DeviceStyleSheet",
        rendererType = "org.icefaces.DeviceStyleSheetRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.DeviceStyleSheet",
        tlddoc = "deviceStylesheet component loads an outputStyleSheet in the head of a jsf view page based " +
                "upon a detected device/desktop or a user-specified theme. " +
                "If the JSF content parameter of Project Stage evaluates to \"Production\" the " +
                "compressed css file is loaded into the head.  " +
                "The component has three modes in which it executes. \n"+
                "1.) no attributes - then component tries to detect a mobile device "+
                "in from the user-agent.  If a mobile device is discovered, then "+
                "it will fall into three possible matches, iphone, ipad,  android and "+
                "blackberry.  If the mobile device is not not know then ipad as default "+
                "is loaded. Library is always assumed to be DEFAULT_LIBRARY. \n"+
                "2.) name attribute - component will default to using a library name  "+
                "of DEFAULT_LIBRARY (currently set to org.icefaces.component.skins. " +
                "The name attribute specifies one of the "+
                "possible device themes; iphone.css, android.css or bberry.css "+
                "An Error will result if named resource could not be resolved.\n" +
                "3.) name and libraries attributes. - component will use the library "+
                "and name specified by the user.  Component is fully manual in this  "+
                "mode. Error will result if name and library can not generate a "+
                "value resource. "
)


@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
public class DeviceStyleSheetMeta {

 /*   @Property(tlddoc = "href for css file to be loaded.  The default for iPhone will be default_iPhone")
    private String href; */

    @Property(tlddoc = "The name reference for the stylesheet as a JSF resource.")
    private String name;

    @Property(tlddoc = "The library reference for the stylesheet as a  JSF resource.")
    private String library;

    @Property(tlddoc = "The CSS media type, screen, print and others. ")
    private String media;

    @Property(tlddoc = "View size, small or large for user designated selection of stylesheet to be loaded. ")
    private String view;
}
