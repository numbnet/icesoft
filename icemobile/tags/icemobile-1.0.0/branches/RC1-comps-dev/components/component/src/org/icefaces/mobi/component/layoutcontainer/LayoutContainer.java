package org.icefaces.mobi.component.layoutcontainer;

public class LayoutContainer extends LayoutContainerBase{

    public static final String LAYOUT_CONTAINER_CLASS = "mobi-layout-container ";

    private boolean active = false;

    public LayoutContainer(){
        super();
    }

    public void setActive (boolean actIn){
        this.active = actIn;
    }

    public boolean  isActive() {
        return this.active;
    }
}
