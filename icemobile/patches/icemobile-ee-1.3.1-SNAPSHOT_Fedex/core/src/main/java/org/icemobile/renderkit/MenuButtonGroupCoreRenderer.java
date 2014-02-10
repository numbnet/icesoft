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

import org.icemobile.component.IMenuButtonGroup;

import java.io.IOException;
import java.util.logging.Logger;

import static org.icemobile.util.HTML.*;

public class MenuButtonGroupCoreRenderer extends BaseCoreRenderer {
    private static final Logger logger =
            Logger.getLogger(MenuButtonGroupCoreRenderer.class.toString());

    public void encodeBegin(IMenuButtonGroup group, IResponseWriter writer)
            throws IOException{
      //  String clientId = group.getClientId();
        writer.startElement(OPTGROUP_ELEM, group);
        // apply disabled style state if specified.
  /*      if (menu.isDisabled()) {
            baseClass.append(IMenuButton.DISABLED_STYLE_CLASS);
        } */
        writer.writeAttribute(LABEL_ATTR, group.getLabel());
        if (null != group.getStyle() ) {
            writer.writeAttribute(STYLE_ATTR, group.getStyle());
        }

  /*      if (group.isDisabled()) {
  // some styleClass to show disabled??
            writer.writeAttribute(DISABLED_ATTR, "disabled");  //what about disabled class?
        }  */

    }

    public void encodeEnd(IMenuButtonGroup group, IResponseWriter writer)
            throws IOException{
         writer.endElement(OPTGROUP_ELEM);
    }

}
