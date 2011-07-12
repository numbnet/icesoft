package org.icefaces.samples.showcase.metadata.context;

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
