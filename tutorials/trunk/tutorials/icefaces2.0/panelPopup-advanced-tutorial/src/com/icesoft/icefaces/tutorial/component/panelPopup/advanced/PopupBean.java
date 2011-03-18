package com.icesoft.icefaces.tutorial.component.panelPopup.advanced;

/**
 * Class used to allow the dynamic opening and closing of panelPopups
 * That means the visibility status is tracked, as well as supporting
 *  methods for button clicks on the page
 */
public class PopupBean
{
    private boolean visible = true;
    
    public boolean isVisible() { return visible; }
    
    public void setVisible(boolean visible) { this.visible = visible; }
    
    public void closePopup() {
        visible = false;
    }
    
    public void openPopup() {
        visible = true;
    }
}