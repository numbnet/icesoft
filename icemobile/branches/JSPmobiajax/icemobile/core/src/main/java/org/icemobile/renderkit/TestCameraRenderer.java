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

package org.icemobile.renderkit;

import org.icemobile.context.RenderContext;
import org.icemobile.context.RenderWriter;

import org.icemobile.component.TestCamera;

public class TestCameraRenderer  {

    public void decode(RenderContext renderContext, TestCamera camera)  {
//        String clientId = camera.getClientId();
        if (!camera.isDisabled()) {
//            String value = renderContext.getRequestParameterMap().get(clientId);
//            camera.queueValueChangeEvent(value);
        }

    }

    public void encodeBegin(RenderContext renderContext, TestCamera camera)  {
        RenderWriter writer = renderContext.getRenderWriter();
//        writer.writeAttribute(ONCLICK_ATTR, "ice.mobilesx(this)");
        writer.writeAttribute("onclick", "ice.mobilesx(this)");
    }

    public void encodeEnd(RenderContext renderContext, TestCamera camera)  {
        RenderWriter writer = renderContext.getRenderWriter();
//        writer.writeAttribute(ONCLICK_ATTR, "ice.mobilesx(this)");
        writer.writeAttribute("onclick", "ice.mobilesx(this)");
    }
}
