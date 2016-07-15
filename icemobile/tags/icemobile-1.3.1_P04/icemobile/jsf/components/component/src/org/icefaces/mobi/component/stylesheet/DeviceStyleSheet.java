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


import org.icefaces.mobi.utils.Attribute;

import java.util.logging.Logger;

public class DeviceStyleSheet extends DeviceStyleSheetBase {
    private static Logger log = Logger.getLogger(DeviceStyleSheet.class.getName());
    public static final String DEFAULT_LIBRARY = "org.icefaces.component.skins";
    // pass through attributes for input text
    public Attribute[] PASS_THOUGH_ATTRIBUTES = {
            new Attribute("media", null)
    };

    public DeviceStyleSheet() {
        super();
    }

    public Attribute[] getPASS_THOUGH_ATTRIBUTES() {
        return PASS_THOUGH_ATTRIBUTES;
    }

}
