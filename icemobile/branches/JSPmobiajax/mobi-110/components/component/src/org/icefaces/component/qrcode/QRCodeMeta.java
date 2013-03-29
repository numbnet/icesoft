/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
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
 * See the License for the specific language governing permissions an
 * limitations under the License.
 */

package org.icefaces.component.qrcode;


import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;
import org.icefaces.component.baseMeta.UIComponentBaseMeta;

@Component(
        tagName = "qrcode",
        componentClass = "org.icefaces.component.qrcode.QRCode",
        rendererClass = "org.icefaces.component.qrcode.QRCodeRenderer",
        generatedClass = "org.icefaces.component.qrcode.QRCodeBase",
        componentType = "org.icefaces.QRCode",
        rendererType = "org.icefaces.QRCodeRenderer",
        extendsClass = "javax.faces.component.UIOutput",
        componentFamily = "org.icefaces.QRCode",
        tlddoc = "This mobility component displays a QR code value."
)

public class QRCodeMeta extends UIComponentBaseMeta {

    @Property(defaultValue = "false",
            tlddoc = "When disabled, component is hidden.")
    private boolean disabled;

    @Property(tlddoc = "tabindex of the component")
    private int tabindex;

    @Property(tlddoc = "style will be rendered on the root element of this " +
            "component.")
    private String style;

    @Property(tlddoc = "style class will be rendered on the root element of " +
            "this component.")
    private String styleClass;

}
