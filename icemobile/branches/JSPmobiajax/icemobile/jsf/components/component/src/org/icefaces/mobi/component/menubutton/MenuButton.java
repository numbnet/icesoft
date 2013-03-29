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
package org.icefaces.mobi.component.menubutton;

import org.icefaces.mobi.utils.Attribute;

import java.util.HashMap;
import java.util.Map;

public class MenuButton extends MenuButtonBase {
    public static final String BASE_STYLE_CLASS = "mobi-menu-btn ";
    public static final String BUTTON_STYLE_CLASS = "mobi-menu-btn-btn ";
    public static final String DISABLED_STYLE_CLASS = "mobi-button-dis ";
    public static final String MENU_SELECT_CLASS =  "mobi-menu-btn-menu ";

    protected Map<String, StringBuilder> menuItemCfg = new HashMap<String, StringBuilder>() ;

    private Attribute[] commonInputAttributeNames = {
            new Attribute("name", null),
            new Attribute("tabindex", null),
    };
    public Map<String, StringBuilder> getMenuItemCfg() {
        return menuItemCfg;
    }

    public void setMenuItemCfg(Map<String, StringBuilder> menuItemCfg) {
        this.menuItemCfg = menuItemCfg;
    }
    public void addMenuItem(String itemId, StringBuilder cfg){
        this.menuItemCfg.put(itemId, cfg);
    }

    public Attribute[] getCommonInputAttributeNames() {
        return commonInputAttributeNames;
    }

    public void setCommonInputAttributeNames(Attribute[] commonInputAttributeNames) {
        this.commonInputAttributeNames = commonInputAttributeNames;
    }
}
