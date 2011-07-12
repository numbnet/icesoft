package org.icefaces.samples.showcase.metadata.context;

/**
 *
 */
public class MenuLink {

    private String title;
    private boolean isDisabled;
    private boolean isDefault;
    private boolean isNew;
    private String exampleBeanName;

    public MenuLink(String title, boolean aDefault, boolean aNew, boolean isDisabled, String exampleBeanName) {
        this.title = title;
        this.isDisabled = isDisabled;
        isDefault = aDefault;
        isNew = aNew;
        this.exampleBeanName = exampleBeanName;
    }

    public String getTitle() {
        return title;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public boolean isNew() {
        return isNew;
    }

    public String getExampleBeanName() {
        return exampleBeanName;
    }
}
