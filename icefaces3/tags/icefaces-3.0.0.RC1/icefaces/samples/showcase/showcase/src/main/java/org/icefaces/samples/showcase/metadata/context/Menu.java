/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.samples.showcase.metadata.context;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 */
    public class Menu<T> implements ContextBase, Serializable {

    private Class<T> parentClass;

    protected String title;
    protected MenuLink defaultExample;
    protected ArrayList<MenuLink> menuLinks;

   public Menu(Class<T> parentClass) {
        this.parentClass = parentClass;
        menuLinks = new ArrayList<MenuLink>();
    }

    public void initMetaData() {

        // check for the menu annotation and parse out any child menu links.
        if (parentClass.isAnnotationPresent(
                org.icefaces.samples.showcase.metadata.annotation.Menu.class)) {
            org.icefaces.samples.showcase.metadata.annotation.Menu menu =
                    parentClass.getAnnotation(org.icefaces.samples.showcase.metadata.annotation.Menu.class);
            title = menu.title();
            org.icefaces.samples.showcase.metadata.annotation.MenuLink[] menuExample = menu.menuLinks();
            MenuLink menuLink;
            for (org.icefaces.samples.showcase.metadata.annotation.MenuLink link : menuExample) {
                menuLink = new MenuLink(link.title(), link.isDefault(),
                        link.isNew(), link.isDisabled(), link.exampleBeanName());
                menuLinks.add(menuLink);
                if (menuLink.isDefault()){
                    defaultExample = menuLink;
                }
            }
        }
    }

    public ArrayList<MenuLink> getMenuLinks() {
        return menuLinks;
    }

    public MenuLink getDefaultExample() {
        return defaultExample;
    }

    public String getTitle() {
        return title;
    }

    public String getBeanName(){
        return null;
    }

}
