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

package org.icefaces.component.uploadhelper;


import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;
import org.icefaces.component.baseMeta.UIComponentBaseMeta;

import javax.faces.application.ResourceDependencies;
import java.util.Map;

@Component(
        tagName = "uploadhelper",
        componentClass = "org.icefaces.component.uploadhelper.UploadHelper",
        rendererClass = "org.icefaces.component.uploadhelper.UploadHelperRenderer",
        generatedClass = "org.icefaces.component.uploadhelper.UploadHelperBase",
        componentType = "org.icefaces.UploadHelper",
        rendererType = "org.icefaces.UploadHelperRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.UploadHelper",
        tlddoc = "This mobility component " +
                " detects mobile and desktop cases to support upload. "
)

@ResourceDependencies({
})
public class UploadHelperMeta extends UIComponentBaseMeta {



}
