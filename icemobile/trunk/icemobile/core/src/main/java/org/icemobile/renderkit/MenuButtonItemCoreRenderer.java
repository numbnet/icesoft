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

import org.icemobile.component.IMenuButtonItem;

import java.io.IOException;
import java.util.logging.Logger;

import static org.icemobile.util.HTML.*;

public class MenuButtonItemCoreRenderer extends BaseCoreRenderer {
    private static final Logger logger =
            Logger.getLogger(MenuButtonItemCoreRenderer.class.toString());

    public void encodeEnd(IMenuButtonItem item, IResponseWriter writer)
            throws IOException{
        String clientId = item.getClientId();
        writer.startElement(OPTION_ELEM, item);
        writer.writeAttribute(ID_ATTR, clientId);
        writer.writeAttribute(NAME_ATTR, clientId);
        if (item.isDisabled()) {
            writer.writeAttribute(DISABLED_ATTR, DISABLED_ATTR);
        }
        writer.writeAttribute("data-singleSubmit",item.isSingleSubmit());
        if (null != item.getPanelConfirmationId()){
            writer.writeAttribute("data-pcId",item.getPanelConfirmationId());
        }
        if (null != item.getSubmitNotificationId()){
            writer.writeAttribute("data-snId",item.getSubmitNotificationId());
        }
        if (null != item.getBehaviors()){
            writer.writeAttribute("behaviors",item.getBehaviors());
        }
        // ask Philip about styleClass and style.
        writer.writeAttribute(VALUE_ATTR, item.getValue());
        writer.write(item.getLabel());
        writer.endElement(OPTION_ELEM);
    }

}
