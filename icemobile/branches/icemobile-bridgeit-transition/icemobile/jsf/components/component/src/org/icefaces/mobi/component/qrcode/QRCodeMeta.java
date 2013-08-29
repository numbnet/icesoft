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

package org.icefaces.mobi.component.qrcode;


import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
        tagName = "qrcode",
        componentClass = "org.icefaces.mobi.component.qrcode.QRCode",
        rendererClass = "org.icefaces.mobi.component.qrcode.QRCodeRenderer",
        generatedClass = "org.icefaces.mobi.component.qrcode.QRCodeBase",
        componentType = "org.icefaces.QRCode",
        rendererType = "org.icefaces.QRCodeRenderer",
        extendsClass = "javax.faces.component.UIOutput",
        componentFamily = "org.icefaces.QRCode",
        tlddoc = "qrcode displays a QR code value."
)

@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
public class QRCodeMeta extends UIComponentBaseMeta {

    @Property(defaultValue = "false",
            tlddoc = org.icefaces.mobi.utils.TLDConstants.DISABLED)
    private boolean disabled;

    @Property(tlddoc = org.icefaces.mobi.utils.TLDConstants.TABINDEX)
    private int tabindex;

     @Property(tlddoc = org.icefaces.mobi.utils.TLDConstants.STYLE)
     private String style;

     @Property(tlddoc = org.icefaces.mobi.utils.TLDConstants.STYLECLASS)
     private String styleClass;
}
