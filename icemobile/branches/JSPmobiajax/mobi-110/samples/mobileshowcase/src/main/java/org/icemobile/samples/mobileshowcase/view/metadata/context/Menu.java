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

package org.icemobile.samples.mobileshowcase.view.metadata.context;


import java.util.ArrayList;

/**
 *
 */
public class Menu<T> implements ContextBase {

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
                org.icemobile.samples.mobileshowcase.view.metadata.annotation.Menu.class)) {
            org.icemobile.samples.mobileshowcase.view.metadata.annotation.Menu menu =
                    parentClass.getAnnotation(org.icemobile.samples.mobileshowcase.view.metadata.annotation.Menu.class);
            title = menu.title();
            org.icemobile.samples.mobileshowcase.view.metadata.annotation.MenuLink[] menuExample = menu.menuLinks();
            MenuLink menuLink;
            for (org.icemobile.samples.mobileshowcase.view.metadata.annotation.MenuLink link : menuExample) {
                menuLink = new MenuLink(link.title(), link.isDefault(),
                        link.isNew(), link.isDisabled(), link.exampleBeanName());
                menuLinks.add(menuLink);
                if (menuLink.isDefault()) {
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

    public String getBeanName() {
        return null;
    }

}
