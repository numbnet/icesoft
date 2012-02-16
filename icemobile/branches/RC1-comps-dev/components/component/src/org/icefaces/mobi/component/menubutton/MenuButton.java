package org.icefaces.mobi.component.menubutton;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: jguglielmin
 * Date: 12-02-12
 * Time: 12:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class MenuButton extends MenuButtonBase {
    public static final String BASE_STYLE_CLASS = "mobi-menu-btn ";
    public static final String BUTTON_STYLE_CLASS = "mobi-menu-btn-btn ";
    public static final String DISABLED_STYLE_CLASS = "mobi-button-dis ";
    public static final String MENU_SELECT_CLASS =  "mobi-menu-btn-menu ";

    protected Map<String, StringBuilder> menuItemCfg = new HashMap<String, StringBuilder>() ;

    public Map<String, StringBuilder> getMenuItemCfg() {
        return menuItemCfg;
    }

    public void setMenuItemCfg(Map<String, StringBuilder> menuItemCfg) {
        this.menuItemCfg = menuItemCfg;
    }
    public void addMenuItem(String itemId, StringBuilder cfg){
        this.menuItemCfg.put(itemId, cfg);
    }

}
