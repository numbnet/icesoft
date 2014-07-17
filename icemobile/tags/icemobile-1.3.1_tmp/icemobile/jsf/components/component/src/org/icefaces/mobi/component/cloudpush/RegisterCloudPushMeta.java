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
package org.icefaces.mobi.component.cloudpush;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import org.icefaces.mobi.utils.TLDConstants;

@Component(
        tagName = "registerCloudPush",
        componentClass = "org.icefaces.mobi.component.cloudpush.RegisterCloudPush",
        rendererClass = "org.icefaces.mobi.component.cloudpush.RegisterCloudPushRenderer",
        generatedClass = "org.icefaces.mobi.component.cloudpush.RegisterCloudPushBase",
        extendsClass = "javax.faces.component.UICommand",
        componentType = "org.icefaces.component.RegisterCloudPush",
        rendererType = "org.icefaces.component.RegisterCloudPushRenderer",
        componentFamily = "org.icefaces.RegisterCloudPush",
        tlddoc = "Renders a button to allow the user to register for Cloud Push device notifications through BridgeIt" 
)

@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "bridgeit.js")
})
public class RegisterCloudPushMeta extends UIComponentBaseMeta{
    
    @Property(tlddoc = "The button label. ", defaultValue="Register for Cloud Push")
    private String buttonLabel;
    
    @Property(defaultValue = "false", 
            tlddoc = TLDConstants.DISABLED)
    private boolean disabled;

    @Property(tlddoc = TLDConstants.STYLE)
    private String style;

    @Property(tlddoc = TLDConstants.STYLECLASS)
    private String styleClass;
    
    @Property(tlddoc = "Normally the <mobi:registerCloudPush> component is disabled, "
            + "but still displayed on unsupported platforms. Set this to true to "
            + "hide the button on unsupported platforms", defaultValue="false")
    private boolean hideWhenUnsupported = false;
    
    
    

}
